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
  // implicit val alphabeticalOrdering: Ordering[Person] = Ordering.fromLessThan(_.name < _.name)
  // println(persons.sorted)

  /*
  Implicit Scope
  - normal scope = LOCAL SCOPE, where we write our code
  - imported scope, when we import it
  - companion objects of all types involved in method signature
    - In exercise, it would be List, Ordering, and all the types involved (Person or any supertype)
   */

  /*
  Best Practice:
  When defining an implicit val:
  1 - If there is a single possible value for it and you can edit the code for the type
    -> Define the implicit in the companion object
  2 - If there are many possible values for the implicit val but a single good one and you can edit the code for the type
    -> Define the good implicit in the companion and the other implicits elsewhere
   */

  // Example
  object AlphabeticalNameOrdering {
    implicit val alphabeticalOrdering: Ordering[Person] = Ordering.fromLessThan(_.name < _.name)
  }
  object AgeOrdering {
    implicit val ageOrdering: Ordering[Person] = Ordering.fromLessThan(_.age < _.age)
  }
  // import AlphabeticalNameOrdering._\
  import AgeOrdering.ageOrdering
  println(persons.sorted)

}
