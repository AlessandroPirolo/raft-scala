package server 

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.stream.scaladsl.Flow
import akka.http.scaladsl.model.ws.{Message, TextMessage, BinaryMessage}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.Http
import scala.util.Success
import scala.util.Failure
import akka.http.scaladsl.Http.ServerBinding
import messageHandler.MessageHandler
import akka.stream.javadsl.Sink

object Server {

  sealed trait ResultMessage
  private final case class Started(binding: ServerBinding) extends ResultMessage
  private final case class StartedFailed(ex: Throwable) extends ResultMessage
  
  def apply(host: String, port: Int): Behavior[ResultMessage] = Behaviors.setup { ctx => 

    implicit val system = ctx.system

    val messageHandler = ctx.spawn(MessageHandler(), "messageHandler")

    def handler: Flow[Message, Message, Any] = 
      Flow[Message].map(
        mex => {
          messageHandler ! mex
          mex 
        }
      )

    val webSocketRoute = 
      path("/") {
        handleWebSocketMessages(handler)
      }

    val binding = Http().newServerAt(host, port).bindFlow(webSocketRoute)

    ctx.pipeToSelf(binding) {
      case Success(binding) => {
        ctx.log.info("Binding successful: server started")
        Started(binding)
      }
      case Failure(exception) => {
        ctx.log.error("Error starting the server: " + exception.getMessage())
        StartedFailed(exception)
      }
    }

    def starting(): Behaviors.Receive[ResultMessage] = 
      Behaviors.receiveMessage[ResultMessage] {
        case Started(binding) => { 
          ctx.log.info("Server started at http://{}:{}/", 
                binding.localAddress.getHostName(), 
                binding.localAddress.getPort())
          Behaviors.same
        }
        case StartedFailed(ex) =>
          throw new RuntimeException(ex)
      }

    starting()
 
  }
}
