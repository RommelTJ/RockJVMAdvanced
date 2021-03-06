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
  2 - Lifting works
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

  // Example of lifting problem
  trait MathLib {
    def add1(x: Int): Int = x + 1
    def add1(s: String): Int = s.toInt + 1
  }
  trait AddMagnet {
    def apply(): Int
  }
  def add1(magnet: AddMagnet): Int = magnet()
  implicit class AddInt(x: Int) extends AddMagnet {
    override def apply(): Int = x + 1
  }
  implicit class AddString(s: String) extends AddMagnet {
    override def apply(): Int = s.toInt + 1
  }
  val addFV = add1 _
  println(addFV(1)) // This now compiles
  println(addFV("3")) // This now compiles

//  Cannot do this in receive because our Trait MessageMagnet has a type
//  val receiveFV = receive _
//  receiveFV(new P2PResponse)

  /*
  Drawbacks of Magnet Pattern:
  1 - More verbose
  2 - Harder to Read (what the hell is a "magnet"?)
  3 - You can't name or place default arguments
  4 - Call-By-Name doesn't work correctly
   */

  /*
  Exercise:
  Prove that call-by-name doesn't work correctly. Hint: Think of side effects.
   */

  // Solution
  class Handler {
    def handle(s: => String) = {
      println(s)
      println(s)
    }
    // other overloads
  }
  trait HandleMagnet {
    def apply(): Unit
  }
  def handle(magnet: HandleMagnet) = magnet()
  implicit class StringHandle(s: => String) extends HandleMagnet {
    override def apply(): Unit = {
      println(s)
      println(s)
    }
  }
  def sideEffectMethod(): String = {
    println("Hello Scala")
    "hahaha"
  }
  println("---")
  handle(sideEffectMethod())
  println("---")
  handle {
    println("Hello Scala")
    "hahaha" // Converted to new StringHandle("hahaha")
  }

}
