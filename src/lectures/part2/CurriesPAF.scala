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
  val insertName = concatenator("Hello, I'm ", _: String, ", how are you?")
  println(insertName("Rommel")) // Injecting with ETA-EXPANSION turns it into "x: String => concatenator(hello, x, how are you)"

  val fillInTheBlanks = concatenator("Hello, ", _: String, _: String) // (x, y) => concatenator("Hello", x, y)
  println(fillInTheBlanks("Rommel! ", "Scala is awesome!")) // Hello, Rommel! Scala is awesome!

  // Exercise:
  // 1. Process a list of numbers and return their string representation with different formats.
  //    Use the %4.2f, %8.6f and %14.12f with a curried formatter function.
  //    println("%8.6f".format(Math.PI))
  // 2. Difference between functions vs methods
  //    Difference between parameters (by name) vs 0-lambda
  //    Calling byName and byFunction with:
  //     - Int
  //     - method
  //     - parenthesisMethod
  //     - lambda
  //     - PAF
  def byName(n: => Int): Int = n + 1
  def byFunction(f: () => Int): Int = f() + 1
  def method: Int = 42
  def parenthesisMethod(): Int = 42

  // Solution 1
  def curriedFormatter(s: String)(num: Double): String = s.format(num)
  val numbers = List(Math.PI, Math.E, 1, 9.8, 1.3e-12)
  val simpleFormat = curriedFormatter("%4.2f") _ // Lifting
  val seriousFormat = curriedFormatter("%8.6f") _
  val preciseFormat = curriedFormatter("%14.12f") _
  println(numbers.map(simpleFormat)) // List(3.14, 2.72, 1.00, 9.80, 0.00)
  println(numbers.map(seriousFormat)) // List(3.141593, 2.718282, 1.000000, 9.800000, 0.000000)
  println(numbers.map(preciseFormat)) // List(3.141592653590, 2.718281828459, 1.000000000000, 9.800000000000, 0.000000000001)

}
