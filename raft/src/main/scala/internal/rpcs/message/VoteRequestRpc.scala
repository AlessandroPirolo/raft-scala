package rpcs.message

import akka.util.ByteString
import rpc.vote_request.VoteRequest

case class VoteRequestRpc private (term: Int, candidateId: String, 
                                   lastLogIndex: Int, lastLogTerm: Int) extends Rpc:  
  override def encode(): ByteString = 
    val protoMsg: VoteRequest = VoteRequest(this.term, this.candidateId, this.lastLogIndex, this.lastLogTerm)
    ByteString(bytes = protoMsg.toByteArray) 

  override def decode(payload: ByteString): Rpc =
    val protoMsg: VoteRequest = VoteRequest.parseFrom(payload.toArrayUnsafe())
    VoteRequestRpc(term = protoMsg.term, candidateId = protoMsg.candidateId,
                   lastLogIndex = protoMsg.lastLogIndex, lastLogTerm = protoMsg.lastLogTerm)

  override def toString(): String = 
    "VoteRequest(term: " + this.term 
        + ", candidateId: " + this.candidateId 
        + ", lastLogIndex: " + this.lastLogIndex 
        + ", lastLogTerm: " + this.lastLogTerm + ")"

object VoteRequestRpc:
  def newVoteRequestRpc(term: Int = 0, candidateId: String = "", 
                        lastLogIndex: Int = 0, lastLogTerm: Int = 0): Rpc =
    VoteRequestRpc(term, candidateId, lastLogIndex, lastLogTerm)
