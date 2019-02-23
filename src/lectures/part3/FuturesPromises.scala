package lectures.part3

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object FuturesPromises extends App {

  def calculateMeaningOfLife: Int = {
    Thread.sleep(2000)
    42
  }
  val aFuture = Future {
    calculateMeaningOfLife // calculates the meaning of life on another thread
  } // compiler adds (global) implicit here.

}
