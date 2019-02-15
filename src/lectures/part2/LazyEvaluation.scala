package lectures.part2

object LazyEvaluation extends App {

  // Lazy Values are evaluated once, but only when they are used.
  // Lazy delays the evaluation of values.
  // val x: Int = throw new RuntimeException // Crash!
  lazy val x: Int = throw new RuntimeException // No Crash!
  println(x) // Now we crash!

}
