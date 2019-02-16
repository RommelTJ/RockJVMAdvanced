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
  override def isEmpty: Boolean = ???

  override def head: Nothing = ???

  override def tail: MyStream[Nothing] = ???

  override def #::[B >: Nothing](element: B): MyStream[B] = ???

  override def ++[B >: Nothing](anotherStream: MyStream[B]): MyStream[B] = ???

  override def foreach(f: Nothing => Unit): Unit = ???

  override def map[B](f: Nothing => B): MyStream[B] = ???

  override def flatMap[B](f: Nothing => MyStream[B]): MyStream[B] = ???

  override def filter(predicate: Nothing => Boolean): MyStream[Nothing] = ???

  override def take(n: Int): MyStream[Nothing] = ???

  override def takeAsList(n: Int): List[Nothing] = ???
}

class Cons[+A] extends MyStream[A] {
  override def isEmpty: Boolean = ???

  override def head: A = ???

  override def tail: MyStream[A] = ???

  override def #::[B >: A](element: B): MyStream[B] = ???

  override def ++[B >: A](anotherStream: MyStream[B]): MyStream[B] = ???

  override def foreach(f: A => Unit): Unit = ???

  override def map[B](f: A => B): MyStream[B] = ???

  override def flatMap[B](f: A => MyStream[B]): MyStream[B] = ???

  override def filter(predicate: A => Boolean): MyStream[A] = ???

  override def take(n: Int): MyStream[A] = ???

  override def takeAsList(n: Int): List[A] = ???
}

object MyStream {
  def from[A](start: A)(generator: A => A): MyStream[A] = ???
}

object StreamsPlayground extends App {

}
