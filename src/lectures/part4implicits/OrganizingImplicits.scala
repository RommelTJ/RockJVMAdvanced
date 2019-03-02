package lectures.part4implicits

object OrganizingImplicits extends App {

  // Implicit ordering value for Int in "scala.Predef".
  implicit val reverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  // implicit val normalOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _) // Compilation error because of ambiguous implicit values
  println(List(1, 4, 5, 3, 2).sorted) // and now it's reversed

  /*
  Potential Implicit Values are:
  - val/var
  - object
  - accessor methods = defs with no parenthesis
   */

  // Exercise
  case class Person(name: String, age: Int)
  val persons = List(
    Person("Steve", 30),
    Person("Amy", 22),
    Person("John", 66)
  )
  implicit val alphabeticalOrdering: Ordering[Person] = Ordering.fromLessThan(_.name < _.name)
  println(persons.sorted)

}
