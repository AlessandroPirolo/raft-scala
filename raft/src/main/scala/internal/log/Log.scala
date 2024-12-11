package log

import rpc.log_entry.logEntry

trait Log:
  def encode(): Seq[logEntry]
  def decode(raw: Seq[logEntry]): Log
