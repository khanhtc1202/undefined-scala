package GuessingGame

import akka.actor.ActorSystem

object Application extends App {
  val system = ActorSystem()

  val game = system.actorOf(Game.props)
}