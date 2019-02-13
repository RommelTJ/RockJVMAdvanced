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

  // Lifting = ETA-EXPANSION.

}
