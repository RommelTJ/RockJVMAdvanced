package lectures.part1

object Recap extends App {

  val aCondition: Boolean = false
  val aConditionedVal = if (aCondition) 42 else 65
  // instructions vs expressions

  // Compiler infers types for us
  val aCodeBlock = {
    if (aCondition) 54
    56
  }

  // Unit - expressions that only do side-effects (void) in Java.
  val theUnit: Unit = println("Hello Scala")

}
