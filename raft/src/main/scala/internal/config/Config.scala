package config

import akka.actor.typed.{Behavior, ActorSystem}
import akka.actor.typed.scaladsl.adapter._
import java.net.InetSocketAddress
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.ActorRef
import messageHandler.MessageHandler
import server.ConnHandler
import akka.io.Tcp 

object Config:

  sealed trait ConfigMessage
  case class AddNode(tcpConnection: ActorRef, newNode: InetSocketAddress) extends ConfigMessage
  case class GetNodes(replyTo: akka.actor.typed.ActorRef[$0]) extends ConfigMessage
  
  def apply(): Behavior[ConfigMessage] = Behaviors.setup { ctx =>

    val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "Server")
    implicit val classicSystem: akka.actor.ActorSystem = system.toClassic
    
    val messageHandler = ctx.spawn(MessageHandler(), "msgHandler")
    var connHndlSet: Set[akka.actor.typed.ActorRef[Tcp.Message]] = Set.empty
    var currentConfig: List[InetSocketAddress] = List.empty 

    def running(): Behavior[ConfigMessage] = 
      Behaviors.receiveMessage[ConfigMessage] {
        case AddNode(tcpConnection, newNode) =>
          if (!currentConfig.contains(newNode)) {
            currentConfig :+ newNode
            val connHandler: akka.actor.typed.ActorRef[Tcp.Message] = ctx.spawnAnonymous(ConnHandler(messageHandler)) 
            tcpConnection ! Tcp.Register(connHandler.toClassic)
            connHndlSet += connHandler
          }
          Behaviors.same
      }
    
    running()
    
  }   
