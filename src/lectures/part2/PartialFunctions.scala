package lectures.part2

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
  println(aPartialFunction.isDefinedAt(3)) // Returns false

  // Lift
  val lifted = aPartialFunction.lift // Int => Option[Int]
  println(lifted(2)) // Some(56)
  println(lifted(3)) // None

}
