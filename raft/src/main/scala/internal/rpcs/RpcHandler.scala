package rpcs

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors  
import rpcs.message.*

object RpcHandler {

  def apply(): Behavior[Rpc] = Behaviors.setup { ctx =>

    def running(): Behavior[Rpc] =
      Behaviors.receiveMessage[Rpc] {
        case appendReq: AppendEntryReq => {
          ctx.log.info("AppendEntry received")
          Behaviors.same
        }
        case appendResp: AppendEntryResp => {
          ctx.log.info("AppendResponse received")
          Behaviors.same
        }
        case voteReq: VoteReq => {
          ctx.log.info("VoteRequest received")
          Behaviors.same
        }
        case voteResp: VoteResp => {
          ctx.log.info("VoteResponse received")
          Behaviors.same
        }
        case CanVote => {
          ctx.log.info("AppendEntry received")
          Behaviors.same
        }
      }

    running()
    
  } 
}

