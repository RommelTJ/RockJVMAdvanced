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
  def map[B](f: A => B): MySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B]
  def filter(predicate: A => Boolean): MySet[A]
  def foreach(f: A => Unit): Unit
}

class EmptySet[A] extends MySet[A]
class NonEmptySet[A](head: A, tail: MySet[A]) extends MySet[A]