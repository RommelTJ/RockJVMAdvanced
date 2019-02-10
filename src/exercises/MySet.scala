package exercises

/*
  Exercise: Implement a functional set.
  - implement contains
  - adding an element
  - concatenating another set
  - map
  - flatMap
  - filter
  - foreach
   */
trait MySet[A] extends (A => Boolean) {
  def contains(elem: A): Boolean
  def +(elem: A): MySet[A]
  def ++(anotherSet: MySet[A]): MySet[A]
}
