package rpcs.message

import akka.util.ByteString
import rpc.append_entry_request.AppendEntryRequest 
import log.*

final case class AppendEntryRpc private (term: Int, prevLogIndex: Int,
                                         prevLogTerm: Int, commitIndex: Int, 
                                         leaderId: String, entries: Log, leaderCommit: Int) extends Rpc:  
  override def encode(): ByteString = 
    val protoMsg: AppendEntryRequest = 
        AppendEntryRequest(
            this.term, 
            this.prevLogIndex, 
            this.prevLogTerm, 
            this.commitIndex, 
            this.leaderId, 
            this.entries.encode(), 
            this.leaderCommit)
    ByteString(bytes = protoMsg.toByteArray) 

  override def decode(payload: ByteString): Rpc =
    val protoMsg: AppendEntryRequest = AppendEntryRequest.parseFrom(payload.toArrayUnsafe())
    AppendEntryRpc(
        term = protoMsg.term,
        prevLogIndex = protoMsg.prevLogIndex,
        prevLogTerm = protoMsg.prevLogTerm,
        commitIndex = protoMsg.commitIndex,
        leaderId = protoMsg.leaderId,
        leaderCommit = protoMsg.leaderCommit,
        entries = LogImpl.newEmptyLog().decode() )

  override def toString(): String = 
    "AppendEntry(term: " + this.term 
        + ", prevLogIndex: " + this.prevLogIndex 
        + ", prevLogTerm: " + this.prevLogTerm 
        + ", commitIndex: " + this.commitIndex 
        + ", leaderId: " + this.leaderId 
        + ", entries: " + this.entries 
        + ", leaderCommit: " + this.leaderCommit + ")"

object AppendEntryRpc:  
  def newAppendEntry (term: Int = 0, prevLogIndex: Int = 0, 
                      prevLogTerm: Int = 0, commitIndex: Int = 0, 
                      leaderId: String = "", entries: Log = LogImpl.newEmptyLog(), 
                      leaderCommit: Int = 0): Rpc =
    AppendEntryRpc(term, 
          prevLogIndex,
          prevLogTerm,
          commitIndex,
          leaderId,
          entries,
          leaderCommit)

  def heartbeat(): AppendEntryRpc = ???
