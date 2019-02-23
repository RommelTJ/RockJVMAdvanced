package lectures.part3

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object FuturesPromises extends App {

  def calculateMeaningOfLife: Int = {
    Thread.sleep(2000)
    42
  }
  val aFuture = Future {
    calculateMeaningOfLife // calculates the meaning of life on another thread
  } // compiler adds (global) implicit here.

  // println(aFuture.value) // Option[Try[Int]] --> None
  println("Waiting on the future")
  aFuture.onComplete(t => t match {
    case Success(meaningOfLife) => println(s"the meaning of life is $meaningOfLife")
    case Failure(exception) => println(s"I failed with $exception")
  })
  Thread.sleep(3000)
  

}
