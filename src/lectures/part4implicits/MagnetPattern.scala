package lectures.part4implicits

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

// Aims solving problems with Method overloading
object MagnetPattern extends App {

  class P2PRequest
  class P2PResponse
  class Serializer[T]

  trait Actor {
    def receive(statusCode: Int): Int
    def receive(request: P2PRequest)
    def receive(response: P2PResponse)
    def receive[T: Serializer](message: T): Int
    def receive[T: Serializer](message: T, statusCode: Int): Int
    def receive(future: Future[P2PRequest]): Int
    //... and lots of other overloads
  }

  /*
  Problems:
  1 - Type Erasure
  2 - Lifting doesn't work for all overloads
  3 - Code duplication
  4 - Type inference and default args
   */

  trait MessageMagnet[Result] {
    def apply(): Result
  }

  // Actor API receive method in form of magnet
  def receive[R](magnet: MessageMagnet[R]): R = magnet()

  implicit class FromP2PRequest(request: P2PRequest) extends MessageMagnet[Int] {
    override def apply(): Int = {
      println("Handling P2P request")
      42
    }
  }
  implicit class FromP2PResponse(response: P2PResponse) extends MessageMagnet[Int] {
    override def apply(): Int = {
      println("Handling P2P response")
      24
    }
  }

  receive(new P2PRequest)
  receive(new P2PResponse)

  /*
  Advantages of Magnet Pattern:
  1 - No more type erasure problems
   */

  // Example of Type Erasure problem
  implicit class FromResponseFuture(future: Future[P2PResponse]) extends MessageMagnet[Int] {
    override def apply(): Int = 2
  }
  implicit class FromRequestFuture(future: Future[P2PRequest]) extends MessageMagnet[Int] {
    override def apply(): Int = 3
  }
  println(receive(Future(new P2PResponse))) // This now compiles
  println(receive(Future(new P2PRequest))) // This now compiles

}
