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

  // Exercise:
  // 1 - Add ordering for totalPrice. Used 50% of the time by our backend.
  // 2 - Add ordering by Unit Count. Used 25% of the time by our backend.
  // 3 - By Unit Price. Used 25% of the time by our backend.
  case class Purchase(nUnits: Int, unitPrice: Double)

  // Solution
  val purchases = List(
    Purchase(2, 23.45),
    Purchase(100, 0.99),
    Purchase(15, 25.00),
    Purchase(13, 1.25)
  )
  implicit val totalPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan((a, b) => (a.nUnits * a.unitPrice) > (b.nUnits * b.unitPrice))

  object UnitCountOrdering {
    implicit val unitCountOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.nUnits < _.nUnits)
  }
  object UnitPriceOrdering {
    implicit val unitPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.unitPrice < _.unitPrice)
  }

  println(purchases.sorted)
  println(purchases.sorted(UnitCountOrdering.unitCountOrdering))
  println(purchases.sorted(UnitPriceOrdering.unitPriceOrdering))

}
