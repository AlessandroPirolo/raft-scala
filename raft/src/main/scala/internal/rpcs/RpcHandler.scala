package rpcs

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors


object RpcHandler {
  sealed trait Rpc 
  case object CanVote extends Rpc
  final case class AppendEntryReq(msg: String) extends Rpc
  final case class AppendEntryResp(term: Int, success: Boolean, indexError: Option[Int]) extends Rpc
  final case class VoteReq(term: Int, candidateId: String, lastLogIndex: Int, lastLogTerm: Int) extends Rpc
  final case class VoteResp(term: Int, voteGranted: Boolean) extends Rpc

  def apply(): Behavior[Rpc] = Behaviors.setup { ctx =>

    def running(): Behavior[Rpc] =
      Behaviors.receiveMessage[Rpc] {
        case appendReq: AppendEntryReq => {
          Behaviors.same
        }
        case appendResp: AppendEntryResp => {
          Behaviors.same
        }
        case voteReq: VoteReq => {
          Behaviors.same
        }
        case voteResp: VoteResp => {
          Behaviors.same
        }
        case CanVote => {
          Behaviors.same
        }
      }

    running()
    
  } 
}

