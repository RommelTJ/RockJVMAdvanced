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
  def apply(elem: A): Boolean =
    contains(elem)

  def contains(elem: A): Boolean
  def +(elem: A): MySet[A]
  def ++(anotherSet: MySet[A]): MySet[A]
  def map[B](f: A => B): MySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B]
  def filter(predicate: A => Boolean): MySet[A]
  def foreach(f: A => Unit): Unit
}

class EmptySet[A] extends MySet[A] {
  override def contains(elem: A): Boolean = ???

  override def +(elem: A): MySet[A] = ???

  override def ++(anotherSet: MySet[A]): MySet[A] = ???

  override def map[B](f: A => B): MySet[B] = ???

  override def flatMap[B](f: A => MySet[B]): MySet[B] = ???

  override def filter(predicate: A => Boolean): MySet[A] = ???

  override def foreach(f: A => Unit): Unit = ???
}

class NonEmptySet[A](head: A, tail: MySet[A]) extends MySet[A] 
