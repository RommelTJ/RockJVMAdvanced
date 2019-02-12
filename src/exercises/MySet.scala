package exercises

import scala.annotation.tailrec

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

/*
Exercise: Add the following operations to MySet
- Removing an element
- Intersection with another set
- Difference with another set
 */
trait MySet[A] extends (A => Boolean) {
  def apply(elem: A): Boolean =
    contains(elem)

  def contains(elem: A): Boolean
  def +(elem: A): MySet[A]
  def ++(anotherSet: MySet[A]): MySet[A] // Union
  def map[B](f: A => B): MySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B]
  def filter(predicate: A => Boolean): MySet[A]
  def foreach(f: A => Unit): Unit

  def -(elem: A): MySet[A] // Remove
  def &(anotherSet: MySet[A]): MySet[A] // Intersect
  def --(anotherSet: MySet[A]): MySet[A] // Difference

  def unary_! : MySet[A]
}

class EmptySet[A] extends MySet[A] {
  override def contains(elem: A): Boolean = false

  override def +(elem: A): MySet[A] = new NonEmptySet[A](elem, this)

  override def ++(anotherSet: MySet[A]): MySet[A] = anotherSet

  override def map[B](f: A => B): MySet[B] = new EmptySet[B]

  override def flatMap[B](f: A => MySet[B]): MySet[B] = new EmptySet[B]

  override def filter(predicate: A => Boolean): MySet[A] = this

  override def foreach(f: A => Unit): Unit = ()

  override def -(elem: A): MySet[A] = this

  override def &(anotherSet: MySet[A]): MySet[A] = this

  override def --(anotherSet: MySet[A]): MySet[A] = this

  override def unary_! : MySet[A] = new PropertyBasedMySet[A]
}

class NonEmptySet[A](head: A, tail: MySet[A]) extends MySet[A] {
  override def contains(elem: A): Boolean = {
    if (elem == head) true
    else tail.contains(elem)
  }

  override def +(elem: A): MySet[A] = {
    if (this contains elem) this
    else new NonEmptySet[A](elem, this)
  }

  override def ++(anotherSet: MySet[A]): MySet[A] = tail ++ anotherSet + head

  override def map[B](f: A => B): MySet[B] = (tail map f) + f(head)

  override def flatMap[B](f: A => MySet[B]): MySet[B] = (tail flatMap f) ++ f(head)

  override def filter(predicate: A => Boolean): MySet[A] = {
    val filteredTail = tail filter predicate
    if (predicate(head)) filteredTail + head
    else filteredTail
  }

  override def foreach(f: A => Unit): Unit = {
    f(head)
    tail foreach f
  }

  override def -(elem: A): MySet[A] = {
    if (head == elem) tail
    else tail - elem + head
  }

  override def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet) // Intersecting and Filtering is the same thing!

  override def --(anotherSet: MySet[A]): MySet[A] = filter(x => !anotherSet(x))

  override def unary_! : MySet[A] = ???
}

// All elements of type A which satisfy a property
// { x in A | property(x) }
class PropertyBasedMySet[A](property: A => Boolean) extends MySet[A] {
  override def contains(elem: A): Boolean = property(elem)

  // { x in A | property(x) } + elem = { x in A | property(x) || x == elem }
  override def +(elem: A): MySet[A] = new PropertyBasedMySet[A](x => property(x) || x == elem)

  // { x in A | property(x) } ++ anotherSet = { x in A | property(x) || anotherSet contains x }
  override def ++(anotherSet: MySet[A]): MySet[A] = new PropertyBasedMySet[A](x => property(x) || anotherSet(x))

  // All Integers => (_ % 3) => [0 1 2]. Can't know if Set is Finite or Infinite.
  override def map[B](f: A => B): MySet[B] = politelyFail
  override def flatMap[B](f: A => MySet[B]): MySet[B] = politelyFail
  override def foreach(f: A => Unit): Unit = politelyFail

  override def filter(predicate: A => Boolean): MySet[A] = new PropertyBasedMySet[A](x => property(x) && predicate(x))


  override def -(elem: A): MySet[A] = ???

  override def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)
  override def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)

  override def unary_! : MySet[A] = new EmptySet[A]

  def politelyFail = throw new IllegalArgumentException("Really deep rabbit hole!")
}

/*
val s = MySet(1, 2, 3) = buildSet(seq(1, 2, 3), [])
= buildSet(seq(2, 3), [] + 1
= buildSet(seq(3), [1] + 2
= buildSet(seq(), [1, 2] + 3
= [1, 2, 3]
 */
object MySet {
  def apply[A](values: A*): MySet[A] = {
    @tailrec
    def buildSet(valSeq: Seq[A], accumulator: MySet[A]): MySet[A] =
      if (valSeq.isEmpty) accumulator
      else buildSet(valSeq.tail, accumulator + valSeq.head)

    buildSet(values.toSeq, new EmptySet[A])
  }
}

object MySetPlayground extends App {
  val mySet = MySet(1, 2, 3, 4)
  // mySet foreach println

  val mySet2 = mySet + 5
  // mySet2 foreach println

  val mySet3 = mySet2 ++ MySet(-1, -2)
  // mySet3 foreach println

  val mySet4 = mySet3 + 2
  // mySet4 foreach println

  val mySet5 = mySet4.map(x => x * 10)
  // mySet5 foreach println

  val mySet6 = mySet5.flatMap(x => MySet(x, 10 * x))
  // mySet6 foreach println

  val mySet7 = mySet4.filter(_ % 2 == 0)
  mySet7 foreach println

}