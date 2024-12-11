package log

import rpc.log_entry.*
import com.google.protobuf.ByteString

enum Op:
  case WRITE, READ, RENAME, DELETE, CREATE

class EntryImpl private (idx: Int, term: Int, desc: String, operation: Op, fileName: String, payload: ByteString) extends Entry:
  private def getProtoOperation(operation: Op): rpc.log_entry.Operation =
    operation match {
      case Op.WRITE => rpc.log_entry.Operation.WRITE
      case Op.RENAME => rpc.log_entry.Operation.RENAME
      case Op.READ => rpc.log_entry.Operation.READ
      case Op.CREATE => rpc.log_entry.Operation.CREATE
      case Op.DELETE => rpc.log_entry.Operation.DELETE
    }

  private def getOperation(operation: rpc.log_entry.Operation): Op =
    operation match {
      case rpc.log_entry.Operation.WRITE => Op.WRITE
      case rpc.log_entry.Operation.RENAME => Op.RENAME
      case rpc.log_entry.Operation.READ => Op.READ
      case rpc.log_entry.Operation.CREATE => Op.CREATE
      case rpc.log_entry.Operation.DELETE => Op.DELETE
    }

  override def encode(): logEntry =
    rpc.log_entry.logEntry(this.idx, this.term, this.desc, getProtoOperation(this.operation), this.fileName, this.payload)
     
  override def decode(entry: logEntry): Entry =
    EntryImpl(idx = entry.idx,
          term = entry.term,
          desc = entry.description,
          operation = getOperation(entry.opType),
          fileName = entry.fileName,
          payload = entry.payload)
          
