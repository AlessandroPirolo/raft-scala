package rpcs

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors 
import rpc.append_entry_response.AppendEntryResponse
import rpc.append_entry_request.AppendEntryRequest
import rpc.vote_request.VoteRequest
import rpc.vote_response.VoteResponse
import rpc.can_vote.CanVote
  
sealed trait Rpc 
case object CanVote extends Rpc
final case class AppendEntryReq(msg: AppendEntryRequest) extends Rpc
final case class AppendEntryResp(msg: AppendEntryResponse) extends Rpc
final case class VoteReq(msg: VoteRequest) extends Rpc
final case class VoteResp(msg: VoteResponse) extends Rpc

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

