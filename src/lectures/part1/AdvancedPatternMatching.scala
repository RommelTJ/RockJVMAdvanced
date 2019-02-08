package lectures.part1

object AdvancedPatternMatching extends App {

  val numbers = List(1)
  val description = numbers match {
    case head :: Nil => println(s"The only element is $head")
    case _ =>
  }

  /*
  Structures available for pattern matching:
  - Constants
  - Wildcards
  - Case Classes
  - Tuples
  - Special Magic (like above)
   */

}
