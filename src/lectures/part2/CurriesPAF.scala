package lectures.part2

object CurriesPAF extends App {

  // Curried Functions

  val superAdder: Int => Int => Int =
    x => y => x + y

  val add3 = superAdder(3)(2)
  println(add3)

}
