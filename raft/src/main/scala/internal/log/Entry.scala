package log

import rpc.log_entry.logEntry

trait Entry:
  def encode(): logEntry
  def decode(): Entry
