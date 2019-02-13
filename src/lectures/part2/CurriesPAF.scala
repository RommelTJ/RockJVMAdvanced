package lectures.part2

object CurriesPAF extends App {

  // Curried Functions

  // Function that takes function as an argument.
  val superAdder: Int => Int => Int =
    x => y => x + y

  val add3 = superAdder(3)(2)
  println(add3)

  // Curried Methods

  def curriedAdder(x: Int)(y: Int): Int = x + y

  val add4: Int => Int = curriedAdder(4)
  println(add4(1))

  // Note: You can't remove the type parameters or the compiler will complain that there's a missing argument list
  // for method curriedAdder. Behind the scenes it's done via "lifting".

  // Lifting = ETA-EXPANSION. (functions != methods due to JVM limitation)

  def inc(x: Int): Int = x + 1
  List(1, 2, 3).map(inc) // ETA-EXPANSION.
  // Compiler rewrites it as "List(1, 2, 3).map(x => inc(x))"

  // Partial Function Applications (how to force ETA-EXPANSION)

  val add5 = curriedAdder(5) _ // ETA-EXPANSION! Converted to "Int => Int"
  println(add5(2))

  // Exercise:
  // Implement add7: Int => Int = y => 7 + y.
  // As many different implementations of add7.
  val simpleAddFunction = (x: Int, y: Int) => x + y
  def simpleAddMethod(x: Int, y: Int): Int = x + y
  def curriedAddMethod(x: Int)(y: Int): Int = x + y

  // Solutions
  val add7_1 = (x: Int) => simpleAddFunction(7, x) // Simplest
  val add7_2 = simpleAddFunction.curried(7)
  val add7_3 = curriedAddMethod(7) _ // PAF
  val add7_4 = curriedAddMethod(7)(_) // PAF = alternative syntax
  val add7_5 = simpleAddMethod(7, _: Int) // Alternative syntax for turning methods into function values. // y => simpleAddMethod(7, y)
  val add7_6 = simpleAddFunction(7, _: Int) // Underscore forces compiler to do ETA-EXPANSION.

  // Underscores are powerful
  def concatenator(a: String, b: String, c: String): String = a + b + c

}
