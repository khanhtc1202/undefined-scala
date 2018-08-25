package SeparateActorSystem.local

import java.io.File

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

class LocalActor extends Actor {
//  override def preStart(): Unit = {
//    val remoteActor = context.system.actorSelection("akka.tcp://RemoteSystem@127.0.0.1:5552/user/remote")
//    println("That's remote:" + remoteActor)
//    remoteActor ! "hi"
//  }
  override def receive: Receive = {
    case "Start" => {
      val remoteActor = context.system.actorSelection("akka.tcp://RemoteSystem@127.0.0.1:5552/user/remote")
      println(s"Remote actor: $remoteActor")
      remoteActor ! "hi"
    }
    case msg: String => {
      println("Got message from remote: " + msg)
    }
  }
}

object LocalActor {
  def main(args: Array[String]): Unit = {
    val configFile = getClass.getClassLoader.getResource("local_application.conf").getFile
    val config = ConfigFactory.parseFile(new File(configFile))
    val system = ActorSystem("ClientSystem", config)
    val localActor = system.actorOf(Props[LocalActor], name = "local")

//    localActor ! "Start"

    val remoteActor = system.actorSelection("akka.tcp://RemoteSystem@127.0.0.1:5552/user/remote")
    remoteActor ! "hello"
  }
}