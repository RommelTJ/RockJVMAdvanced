package lectures.part2

object LazyEvaluation extends App {

  // Lazy Values are evaluated once, but only when they are used.
  // Lazy delays the evaluation of values.
  // val x: Int = throw new RuntimeException // Crash!
  // lazy val x: Int = throw new RuntimeException // No Crash!
  // println(x) // Now we crash!

  lazy val x: Int = {
    println("hello the first time")
    42
  }
  println(x)
  println(x) // X is not evaluated again.

  // Examples
  def sideEffectCondition: Boolean = {
    println("Boolean")
    true
  }
  def simpleCondition: Boolean = false
}
