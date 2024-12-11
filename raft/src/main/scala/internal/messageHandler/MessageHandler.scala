package messageHandler

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.util.ByteString
import rpcs.message._
import scala.annotation.switch
import rpcs.RpcHandler

object MessageHandler {
 
  def apply(): Behavior[ByteString] = Behaviors.setup { ctx =>

    val rpcHandler = ctx.spawn(RpcHandler(), "RpcHandler")

    def running(): Behavior[ByteString] =
      Behaviors.receiveMessage[ByteString] {
        case bm: ByteString => {
          ctx.log.info("Received binary message")
          rpcHandler ! rpc
          Behaviors.same
        }

      }

    running()
  }
}
