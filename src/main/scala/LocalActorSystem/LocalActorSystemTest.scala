package LocalActorSystem

import java.io.File

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

class SlaveActor extends Actor {
  override def receive: Receive = {
    case "START" => {
      lazy val managerActor = ActorSystemRef.instance.actorSelection("user/master")
      managerActor ! "Hello sir"
    }
    case msg: String => {
      println(s"Receive ${msg} from ${sender}")
    }
  }
}

class MasterActor extends Actor {
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
  val instance = ActorSystem("LocalActorSystem", config)
}

object LocalActorSystemTest {
  def main(args: Array[String]): Unit = {
    val system = ActorSystemRef.instance

    val masterActor = system.actorOf(Props[MasterActor], name = "master")
    val slaveActor = system.actorOf(Props[SlaveActor], name = "slave")

    println(s"Actors: ${masterActor}, ${slaveActor}")

    slaveActor ! "START"
  }
}
