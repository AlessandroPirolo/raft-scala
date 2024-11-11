import com.typesafe.config.ConfigFactory
import akka.actor.typed.ActorSystem
import server.Server
import scala.io.StdIn

@main def main(): Unit =
  val config = ConfigFactory.load()
  val host = config.getString("host")
  val port = config.getInt("port")
  ActorSystem.apply(Server(host, port), "Raft")
  StdIn.readLine()
