package log

import rpc.log_entry.logEntry

final class LogImpl private (log: Seq[Entry]) extends Log:
  override def encode(): Seq[logEntry] =
    ???
  override def decode(raw: Seq[logEntry]): Log = 
    ???


object LogImpl:
  def newEmptyLog(): Log = LogImpl(log = Seq.empty) 
