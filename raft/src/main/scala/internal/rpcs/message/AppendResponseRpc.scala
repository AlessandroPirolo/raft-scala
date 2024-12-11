package rpcs.message

import akka.util.ByteString
import rpc.append_entry_response.AppendEntryResponse

final case class AppendResponseRpc private (id: String, success: Boolean, term: Int, logIndexError: Option[Int]) extends Rpc:  
  override def encode(): ByteString = 
    val protoMsg: AppendEntryResponse = AppendEntryResponse(this.id, this.success, this.term, this.logIndexError)
    ByteString(bytes = protoMsg.toByteArray) 

  override def decode(payload: ByteString): Rpc =
    val protoMsg: AppendEntryResponse = AppendEntryResponse.parseFrom(payload.toArrayUnsafe())
    AppendResponseRpc(id = protoMsg.id, success = protoMsg.success, term = protoMsg.term, logIndexError = protoMsg.logIndexError)

  override def toString(): String = 
    "AppendResponse(id: " + this.id 
          + ", success: " + this.success 
          + ", term: " + this.term 
          + ", logIndexError: " + this.logIndexError + ")"

object AppendResponseRpc:
  def newAppendResponse(id: String = "", success: Boolean = false,
                        term: Int = 0, logIndexError: Option[Int] = None): Rpc = 
    AppendResponseRpc(id, success, term, logIndexError)
