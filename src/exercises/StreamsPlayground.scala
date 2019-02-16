package exercises

/*
  Exercise: Implement a Lazily Evaluated single-linked Stream of elements.
  Head of stream is always available, but tail is available on demand.

  Ex: naturals = MyStream.from(1)(x => x + 1) = stream of natural numbers (potentially infinite!)
  Ex: naturals.take(100) // Lazily Evaluated Stream of the first 100 naturals (finite stream)
  Ex: naturals.take(100).foreach(println) // Prints 100 numbers
  Ex: naturals.foreach(println) // Should crash - Infinite list
  Ex: naturals.map(_ * 2) // Stream of all even numbers (potentially infinite)
   */
abstract class MyStream[+A] {
  def isEmpty: Boolean
  def head: A
  def tail: MyStream[A]

  def #::[B >: A](element: B): MyStream[B] // Prepend operator
  def ++[B >: A](anotherStream: MyStream[B]): MyStream[B] // Concatenate streams

  def foreach(f: A => Unit): Unit
  def map[B](f: A => B): MyStream[B]
  def flatMap[B](f: A => MyStream[B]): MyStream[B]
  def filter(predicate: A => Boolean): MyStream[A]

  def take(n: Int): MyStream[A] // Takes the first n elements out of this stream and returns a finite stream of n elements.
  def takeAsList(n: Int): List[A]
}

object EmptyStream extends MyStream[Nothing] {
  override def isEmpty: Boolean = true

  override def head: Nothing = throw new NoSuchElementException

  override def tail: MyStream[Nothing] = throw new NoSuchElementException

  override def #::[B >: Nothing](element: B): MyStream[B] = new Cons(element, this)

  override def ++[B >: Nothing](anotherStream: MyStream[B]): MyStream[B] = anotherStream

  override def foreach(f: Nothing => Unit): Unit = ()

  override def map[B](f: Nothing => B): MyStream[B] = this

  override def flatMap[B](f: Nothing => MyStream[B]): MyStream[B] = this

  override def filter(predicate: Nothing => Boolean): MyStream[Nothing] = this

  override def take(n: Int): MyStream[Nothing] = this

  override def takeAsList(n: Int): List[Nothing] = Nil
}

// tl is call by name so that it can be lazily evaluated
class Cons[+A](hd: A, tl: => MyStream[A]) extends MyStream[A] {
  override def isEmpty: Boolean = false

  override val head: A = hd

  override lazy val tail: MyStream[A] = tl // Call by need

  // val someStream = new Cons(1, EmptyStream)
  // val prepended = 1 #:: someStream = new Cons(1, someStream)
  // Note: EmptyStream is not yet evaluated when we prepend.
  override def #::[B >: A](element: B): MyStream[B] = new Cons(element, this)

  // Tail will be lazily evaluated when it's needed.
  override def ++[B >: A](anotherStream: MyStream[B]): MyStream[B] = new Cons(head, tail ++ anotherStream)

  override def foreach(f: A => Unit): Unit = {
    f(head)
    tail.foreach(f)
  }

  // This still preserves lazy evaluation.
  // val myStream = new Cons(1, otherStream)
  // val mapped = myStream.map(_ + 1) = new Cons(2, myStream.tail.map(_ + 1))
  // Won't be evaluated unless I somehow use ... mapped.tail in a later expression.
  override def map[B](f: A => B): MyStream[B] = new Cons(f(head), tail.map(f))

  // This still preserves lazy evaluation because Concatenation also does.
  override def flatMap[B](f: A => MyStream[B]): MyStream[B] = f(head) ++ tail.flatMap(f)

  // Lazily Evaluated
  override def filter(predicate: A => Boolean): MyStream[A] = {
    if (predicate(head)) new Cons(head, tail.filter(predicate))
    else tail.filter(predicate)
  }

  override def take(n: Int): MyStream[A] = ???

  override def takeAsList(n: Int): List[A] = ???
}

object MyStream {
  def from[A](start: A)(generator: A => A): MyStream[A] = ???
}

object StreamsPlayground extends App {

}
