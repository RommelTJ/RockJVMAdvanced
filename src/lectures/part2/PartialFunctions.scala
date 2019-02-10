package lectures.part2

object PartialFunctions extends App {

  val aFunction = (x: Int) => x + 1 // Function[Int, Int] === Int => Int

  val aFussyFunction = (x: Int) =>
    if (x == 1) 42
    else if (x == 2) 56
    else if (x == 5) 999
    else throw new FunctionNotApplicableException

  class FunctionNotApplicableException extends RuntimeException
  
}
