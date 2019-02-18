package lectures.part2

import scala.io.Source.stdin

object PartialFunctions extends App {

  val aFunction = (x: Int) => x + 1 // Function[Int, Int] === Int => Int

  val aFussyFunction = (x: Int) =>
    if (x == 1) 42
    else if (x == 2) 56
    else if (x == 5) 999
    else throw new FunctionNotApplicableException

  class FunctionNotApplicableException extends RuntimeException

  val aNicerFussyFunction = (x: Int) => x match {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  }
  // {1, 2, 5} => Int, A Partial Function from Int to Int (because it accepts only a subset of Int numbers).

  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  } // Partial Function Value.
  println(aPartialFunction(2))
  // println(aPartialFunction(3)) // match error

  // Partial Function Use Cases
  // isDefinedAt
  println(aPartialFunction.isDefinedAt(3)) // Returns false

  // Lift
  val lifted = aPartialFunction.lift // Int => Option[Int]
  println(lifted(2)) // Some(56)
  println(lifted(3)) // None

  // Chaining
  val pfChain = aPartialFunction.orElse[Int, Int] {
    case 45 => 67
  }
  println(pfChain(2)) // 56
  println(pfChain(45)) // 67

  // Partial Functions extend Normal Functions
  val aTotalFunction: Int => Int = {
    case 1 => 99
  }

  // Higher-Order Functions (HOFs) accept partial functions
  val aMappedList = List(1, 2, 3).map {
    case 1 => 42
    case 2 => 78
    case 3 => 1000
  }
  println(aMappedList) // List(42, 78, 1000)

  // Note: Unlike functions, Partial Functions can only have 1 parameter type.

  // Exercise 1: Construct a PF instance yourself using an anonymous class.
  // Exercise 2: Implement a Chatbot as a partial function.

  // stdin.getLines().foreach(line => println(s"You said: $line"))

  // Solution 1
  val myPartialFunction: PartialFunction[String, String] = new PartialFunction[String, String] {
    override def isDefinedAt(x: String): Boolean = x == "Hello"
    override def apply(v1: String): String = "Hello"
  }

  // Solution 2
  val myChatBot: PartialFunction[String, String] = {
    case "Hi" => "Hello"
    case "End" => "Goodbye"
    case "Boogie?" => "Heck yeah"
    case _ => "I only understand Hi, End, or Boogie"
  }
  stdin.getLines().map(myChatBot).foreach(println)

}
