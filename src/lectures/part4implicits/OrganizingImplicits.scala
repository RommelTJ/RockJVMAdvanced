package lectures.part4implicits

object OrganizingImplicits extends App {

  // Implicit ordering value for Int in "scala.Predef".
  implicit val reverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  println(List(1, 4, 5, 3, 2).sorted) // and now it's reversed

}
