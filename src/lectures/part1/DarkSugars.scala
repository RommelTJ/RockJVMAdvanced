package lectures.part1

import scala.util.Try

object DarkSugars extends App {

  // Syntax Sugar #1: Methods with single param

  def singleArgMethod(arg: Int): String = s"$arg little ducks..."
  val description = singleArgMethod {
    // Write some code
    42
  }
  println(description) // 42 little ducks...

  val aTryInstance = Try { // Java's try {...}
    throw new RuntimeException
  }

  List(1,2,3).map { x =>
    x + 1
  }

  // Syntax Sugar #2: Single Abstract Method Pattern

  trait Action {
    def act(x: Int): Int
  }

  val anInstance: Action = new Action {
    override def act(x: Int): Int = x + 1
  }
  val anInstance2: Action = (x: Int) => x + 1

  // Example: Runnables.
  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("Hello Scala")
  })
  val aSweeterThread = new Thread(() => println("Hello Scala"))

  abstract class AnAbstractType {
    def implemented: Int = 23
    def f(a: Int): Unit
  }
  val anAbstractInstance: AnAbstractType = (a: Int) => println("sweet")

  // Syntax Sugar #3: the :: and #:: methods are special

  val prependedList = 2 :: List(3, 4)
  // 2.::(List(3, 4)) is wrong
  // List(3, 4).::(2) is correct
  // Scala Spec: Last char decides associativity of method.
  println(1 :: 2 :: 3 :: List(4, 5)) // List(1, 2, 3, 4, 5)
  println(List(4, 5).::(3).::(2).::(1)) // equivalent

  class MyStream[T] {
    def -->:(value: T): MyStream[T] = this // actual implementation here.
  }
  val myStream = 1 -->: 2 -->: 3 -->: new MyStream[Int] // Valid
  // This is ok because '-->:' is right associative because it ends in a colon ':'.

  // Syntax Sugar #4: Multi-word method naming

  class TeenGirl(name: String) {
    def `and then said`(gossip: String) = println(s"$name said $gossip")
  }
  val lilly = new TeenGirl("Lilly")
  lilly `and then said` "Scala is so sweet!"

}
