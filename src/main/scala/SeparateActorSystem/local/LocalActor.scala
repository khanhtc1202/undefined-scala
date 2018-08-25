package SeparateActorSystem.local

import java.io.File

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

class LocalActor extends Actor {

  override def preStart(): Unit = {
    val remoteActor = context.system.actorSelection("akka.tcp://RemoteSystem@127.0.0.1:3000/user/remote")
    println(s"That's remote: $remoteActor")
    remoteActor ! "hi"
  }

  override def receive: Receive = {
    case msg: String => {
      println(s"Got message: $msg from $sender")
    }
  }
}

object LocalActor {
  def main(args: Array[String]): Unit = {
    val configFile = getClass.getClassLoader.getResource("local_application.conf").getFile
    val config = ConfigFactory.parseFile(new File(configFile))
    val system = ActorSystem("ClientSystem", config)
    val localActor = system.actorOf(Props[LocalActor], name = "local")

    println(s"Actor $localActor started")
  }
}