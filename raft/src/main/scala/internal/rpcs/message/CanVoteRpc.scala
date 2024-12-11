package rpcs.message

import akka.util.ByteString
import rpc.can_vote.CanVote

case class CanVoteRpc private (ok: Boolean) extends Rpc:  
  this: CanVoteRpc =>
  override def encode(): ByteString = 
    val protoMsg: CanVote = CanVote(this.ok)
    ByteString(bytes = protoMsg.toByteArray) 

  override def decode(payload: ByteString): Rpc =
    val protoMsg: CanVote = CanVote.parseFrom(payload.toArrayUnsafe())
    CanVoteRpc(ok = protoMsg.canVote)

  override def toString(): String = 
    "CanVoteRpc(" + this.ok + ")"

object CanVoteRpc:
  def newCanVoteRpc(ok: Boolean = false): Rpc = CanVoteRpc(ok)
