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

  // Making Person class available for Pattern Matching - i.e. making an unapply method in the companion object.
  class Person(val name: String, val age: Int)
  object Person {
    def unapply(person: Person): Option[(String, Int)] =
      if (person.age < 21) None
      else Some((person.name, person.age))

    def unapply(age: Int): Option[String] = Some(if (age < 21) "minor" else "major")
  }

  val bob = new Person(name = "Bob", age = 22)
  val greeting = bob match {
    case Person(n, a) => s"Hi, my name is $n and I'm $a years old"
  }
  println(greeting)

  val bob2 = new Person(name = "Bob", age = 20)
  val legalStatus = bob2.age match {
    case Person(status) => s"I'm a $status"
  }
  println(legalStatus)

}
