package rpcs.serializer

import rpcs.message._
import log.LogImpl

import akka.util.ByteString

import rpc.message.{ProtoMessage, Type}
import rpc.append_entry_request.AppendEntryRequest
import rpc.append_entry_response.AppendEntryResponse
import rpc.vote_request.VoteRequest
import rpc.vote_response.VoteResponse 
import rpc.can_vote.CanVote

class RpcSerializerImpl extends RpcSerializer: 
  override def parseFrom(msg: ByteString): Rpc = 
    val rawMsg: ProtoMessage = ProtoMessage.parseFrom(msg.toArrayUnsafe())
    retreivePayload(rawMsg._1)(rawMsg._2.toByteArray())

  private def retreivePayload(ty: Type)(payload: Array[Byte]): Rpc = 
    ty match {
      case Type.ARESP => {
        val msg: AppendEntryResponse = AppendEntryResponse.parseFrom(payload)
        AppendResponseRpc.newAppendResponse(msg.id, msg.success, msg.term, msg.logIndexError)
      }
      case Type.VRESP => {
        val msg: VoteResponse = VoteResponse.parseFrom(payload)
        VoteResponseRpc.newVoteResponseRpc(msg.id, msg.voteGranted, msg.term) 
      }
      case Type.VREQ => {
        val msg: VoteRequest = VoteRequest.parseFrom(payload)
        VoteRequestRpc.newVoteRequestRpc(msg.term, msg.candidateId, msg.lastLogIndex, msg.lastLogTerm)
      }
      case Type.VOTE => {
        val msg: rpc.can_vote.CanVote = rpc.can_vote.CanVote.parseFrom(payload)
        CanVoteRpc.newCanVoteRpc(msg.canVote)
      }
      case Type.AENTRY => {
        val msg: AppendEntryRequest = AppendEntryRequest.parseFrom(payload)
        AppendEntryRpc.newAppendEntry(msg.term, msg.prevLogIndex, msg.prevLogTerm, msg.commitIndex, msg.leaderId, LogImpl.newEmptyLog().decode(msg.entries), msg.leaderCommit)
      }
      case Type.Unrecognized(n) => {
        throw new UnsupportedOperationException 
      }
    }

object RpcSerializerImpl:
  def toByte(ty: Type, payload: Rpc): ByteString =
    ByteString(
      bytes = 
        ProtoMessage(ty, 
          com.google.protobuf.ByteString
            .copyFrom(payload
              .encode()
              .toArrayUnsafe()))
              .toByteArray)
