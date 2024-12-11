package rpcs.message

import akka.util.ByteString
import rpc.vote_response.VoteResponse

case class VoteResponseRpc private (id: String, voteGranted: Boolean, term: Int) extends Rpc:  
  override def encode(): ByteString = 
    val protoMsg: VoteResponse = VoteResponse(this.id, this.voteGranted, this.term)
    ByteString(bytes = protoMsg.toByteArray) 

  override def decode(payload: ByteString): Rpc = 
    val protoMsg: VoteResponse = VoteResponse.parseFrom(payload.toArrayUnsafe())
    VoteResponseRpc(id = protoMsg.id, voteGranted = protoMsg.voteGranted, term = protoMsg.term)

  override def toString(): String = 
    "VoteResponse(id: " + this.id 
        + ", granted: " + this.voteGranted 
        + ", term: " + this.term + ")"

object VoteResponseRpc:
  def newVoteResponseRpc(id: String = "", voteGranted: Boolean = false, term: Int = 0): Rpc =
    VoteResponseRpc(id, voteGranted, term)
