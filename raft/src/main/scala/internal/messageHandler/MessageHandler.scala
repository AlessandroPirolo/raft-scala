package messageHandler

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import rpc.message.{ProtoMessage, Type}
import rpc.append_entry_request.AppendEntryRequest
import rpc.append_entry_response.AppendEntryResponse
import rpc.vote_request.VoteRequest
import rpc.vote_response.VoteResponse 
import rpc.can_vote.CanVote
import akka.util.ByteString
import rpcs.{Rpc, AppendEntryReq, AppendEntryResp, VoteReq, VoteResp, CanVote}
import scala.annotation.switch
import rpcs.RpcHandler

object MessageHandler {
 
  def apply(): Behavior[ByteString] = Behaviors.setup { ctx =>

    val rpcHandler = ctx.spawn(RpcHandler(), "RpcHandler")

    def retreivePayload(ty: Type)(payload: Array[Byte]): Rpc = 
      ty match {
        case Type.ARESP => {
          val msg: AppendEntryResponse = AppendEntryResponse.parseFrom(payload)
          AppendEntryResp(msg)
        }
        case Type.VRESP => {
          val msg: VoteResponse = VoteResponse.parseFrom(payload)
          VoteResp(msg)
        }
        case Type.VREQ => {
          val msg: VoteRequest = VoteRequest.parseFrom(payload)
          VoteReq(msg)
        }
        case Type.VOTE => {
          val msg: rpc.can_vote.CanVote = rpc.can_vote.CanVote.parseFrom(payload)
          rpcs.CanVote
        }
        case Type.AENTRY => {
          val msg: AppendEntryRequest = AppendEntryRequest.parseFrom(payload)
          AppendEntryReq(msg)
        }
        case Type.Unrecognized(n) => {
          ctx.log.error("Unrecognized message")
          throw new UnsupportedOperationException 
        }
      }

    def running(): Behavior[ByteString] =
      Behaviors.receiveMessage[ByteString] {
        case bm: ByteString => {
          ctx.log.info("Received binary message")
          val rawMsg: ProtoMessage = ProtoMessage.parseFrom(bm.toArrayUnsafe())
          val rpc: Rpc = retreivePayload(rawMsg._1)(rawMsg._2.toByteArray())
          rpcHandler ! rpc
          Behaviors.same
        }

      }

    running()
  }
}
