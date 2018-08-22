package main.scala

import akka.actor._

class CompletableApp(val steps:Int) extends App {
  val canComplete = new java.util.concurrent.CountDownLatch(1)
  val canStart = new java.util.concurrent.CountDownLatch(1)
  val completion = new java.util.concurrent.CountDownLatch(steps)

  val system = ActorSystem("undefined-scala")

  def awaitCanCompleteNow = canComplete.await

  def awaitCanStartNow = canStart.await

  def awaitCompletion = {
    completion.await
    system.terminate()
  }

  def canCompleteNow() = canComplete.countDown()

  def canStartNow() = canStart.countDown()

  def completeAll() = {
    while (completion.getCount > 0) {
      completion.countDown()
    }
  }

  def completedStep() = completion.countDown()
}
