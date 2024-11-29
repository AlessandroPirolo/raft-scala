package server 

import java.net.InetSocketAddress
import akka.io.{IO, Tcp}
import akka.actor.{Actor, Props}
import akka.actor.ActorRef
import messageHandler.MessageHandler
import akka.actor.typed.scaladsl.Behaviors
import Tcp._ 
import akka.actor.typed.{Behavior, ActorSystem}
import akka.actor.typed.scaladsl.adapter._

object Server: 
  
  def apply(host: String)(port: Int): Behavior[Tcp.Message] = Behaviors.setup { ctx =>

    /* Necessary to use IO(Tcp) ! ... */
    val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "Server")
    implicit val classicSystem: akka.actor.ActorSystem = system.toClassic
    /********/

    IO(Tcp) ! Bind(ctx.self.toClassic, new InetSocketAddress(host, port))

    var nodeList: List[InetSocketAddress] = List.empty
    var connHndlSet: Set[akka.actor.typed.ActorRef[Message]] = Set.empty
    val messageHandler = ctx.spawn(MessageHandler(), "msgHandler")

    def running(): Behavior[Tcp.Message] =
      Behaviors.receiveMessage[Tcp.Message] {
        case b @ Bound(localAddress) =>
          ctx.log.info("Server started at http://{}:{}/", 
              localAddress.getHostName(), 
              localAddress.getPort())
          Behaviors.same
      
        case CommandFailed(_: Bind) => 
          throw new RuntimeException("Failed to bind")

        case c @ Connected(remoteAddress, localAddress) => 
          val connection = ctx.toClassic.sender() 
          if (!nodeList.contains(remoteAddress)) {
            nodeList :+ remoteAddress //TODO: substitute this with a msg sent to a some config handler 
            /* create an handler and register it */ 
            val connHandler: akka.actor.typed.ActorRef[Message] = ctx.spawnAnonymous(ConnHandler(messageHandler)) 
            connection ! Register(connHandler.toClassic)
            connHndlSet += connHandler
          }
          Behaviors.same

        case _ =>
          Behaviors.unhandled
      }

    running()
  }
