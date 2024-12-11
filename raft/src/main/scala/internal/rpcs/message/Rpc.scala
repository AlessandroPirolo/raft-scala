package rpcs.message

import rpc.append_entry_response.AppendEntryResponse
import rpc.append_entry_request.AppendEntryRequest
import rpc.vote_request.VoteRequest
import rpc.vote_response.VoteResponse
import rpc.can_vote.CanVote
import akka.util.ByteString

trait Rpc:
  def encode(): ByteString
  def decode(payload: ByteString): Rpc
  def toString(): String
