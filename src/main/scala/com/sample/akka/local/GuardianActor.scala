package com.sample.akka.local

import java.io.File

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

class GuardianActor extends Actor {

  override def receive: Receive = {
    case "START" => {
      lazy val managerActor = ActorSystemRef.instance.actorSelection("user/manager")
      managerActor ! "Hello sir"
    }
    case msg: String => {
      println(s"Receive ${msg} from ${sender}")
    }
  }

}

class ManagerActor extends Actor {
  override def receive: Receive = {
    case msg: String => {
      println(s"Receive ${msg} from ${sender}")
      sender ! "hi"
    }
  }
}

object ActorSystemRef {
  private val configFile = getClass.getClassLoader.getResource("local_application.conf").getFile
  private val config = ConfigFactory.parseFile(new File(configFile))
  val instance = ActorSystem("ClientSystem", config)
}

object GuardianActor {
  def main(args: Array[String]): Unit = {
    val system = ActorSystemRef.instance

    val managerActor = system.actorOf(Props[ManagerActor], name = "manager")
    val guardianActor = system.actorOf(Props[GuardianActor], name = "local")

    println(s"Actors:  ${managerActor}, ${guardianActor}")

    guardianActor ! "START"
  }
}
