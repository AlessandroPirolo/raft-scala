package server

import akka.actor.typed.ActorRef
import akka.io.Tcp.{Message, Received, PeerClosed}
import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.Behaviors
import akka.util.ByteString

object ConnHandler:

  def apply(msgHandler: ActorRef[ByteString]): Behavior[Message] = Behaviors.setup { ctx =>

    def receiving(): Behavior[Message] = 
      Behaviors.receiveMessage[Message] {
        case Received(msg) => 
          msgHandler ! msg
          Behaviors.same
        case PeerClosed => 
          ctx.stop(ctx.getSelf)
          Behaviors.same
        case _ => Behaviors.unhandled
      }

    receiving()
  }
