package messageHandler

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.model.ws.{Message, TextMessage, BinaryMessage}

object MessageHandler {
 
  def apply(): Behavior[Message] = Behaviors.setup { ctx =>

    def running(): Behavior[Message] =
      Behaviors.receiveMessage[Message] {
        case bm: BinaryMessage => {
          ctx.log.info("Received binary mess")
          Behaviors.same
        }

        case tm: TextMessage => {
          ctx.log.info("Received text mess")
          Behaviors.same
        }
      }

    running()
  }
}
