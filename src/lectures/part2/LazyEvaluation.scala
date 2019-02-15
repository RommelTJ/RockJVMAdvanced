package lectures.part2

object LazyEvaluation extends App {

  // val x: Int = throw new RuntimeException // Crash!
  lazy val x: Int = throw new RuntimeException // No Crash!

}
