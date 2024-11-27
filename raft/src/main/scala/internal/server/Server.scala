package server 

import java.net.InetSocketAddress
import akka.io.{IO, Tcp}
import akka.actor.{Actor, Props}

object Server: 
  def props(host: String, port: Int) =
    Props(classOf[Server], host, port)

  class Server(host: String, port: Int) extends Actor {
    import Tcp._
    import context.system

    IO(Tcp) ! Bind(self, new InetSocketAddress(host, port))

    def receive: Actor.Receive = 
      case b @ Bound(localAddress) => 
        context.system.log.info("Server started at http://{}:{}/", 
            localAddress.getHostName(), 
            localAddress.getPort())
      
      case CommandFailed(_: Bind) => throw new RuntimeException("Failed to bind")

      case c @ Connected(remoteAddress, localAddress) => 
        val connection = sender()

  }
