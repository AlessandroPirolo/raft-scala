package server 

import java.net.InetSocketAddress
import akka.io.{IO, Tcp}
import akka.actor.{Actor, Props}
import akka.actor.ActorRef
import config.Config
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

    val config = ctx.spawn(Config(), "config")

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
          config ! Config.AddNode(connection, remoteAddress) 
          Behaviors.same

        case _ =>
          Behaviors.unhandled
      }

    running()
  }
