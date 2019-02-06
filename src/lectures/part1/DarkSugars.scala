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

}
