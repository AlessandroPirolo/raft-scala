package rpcs.serializer

import rpcs.message._
import akka.util.ByteString

trait RpcSerializer:
  def parseFrom(msg: ByteString): Rpc 
