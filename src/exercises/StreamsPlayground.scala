package exercises

import scala.annotation.tailrec

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
  def ++[B >: A](anotherStream: => MyStream[B]): MyStream[B] // Concatenate streams

  def foreach(f: A => Unit): Unit
  def map[B](f: A => B): MyStream[B]
  def flatMap[B](f: A => MyStream[B]): MyStream[B]
  def filter(predicate: A => Boolean): MyStream[A]

  def take(n: Int): MyStream[A] // Takes the first n elements out of this stream and returns a finite stream of n elements.
  def takeAsList(n: Int): List[A] = take(n).toList()

  // [1, 2, 3].toList([]) =
  // [2, 3].toList([1]) =
  // [3].toList([2, 1]) =
  // [].toList([3, 2, 1]) =
  // [1, 2, 3]
  @tailrec
  final def toList[B >: A](accumulator: List[B] = Nil): List[B] = {
    if (isEmpty) accumulator.reverse
    else tail.toList(head :: accumulator)
  }
}

object EmptyStream extends MyStream[Nothing] {
  override def isEmpty: Boolean = true

  override def head: Nothing = throw new NoSuchElementException

  override def tail: MyStream[Nothing] = throw new NoSuchElementException

  override def #::[B >: Nothing](element: B): MyStream[B] = new Cons(element, this)

  override def ++[B >: Nothing](anotherStream: => MyStream[B]): MyStream[B] = anotherStream

  override def foreach(f: Nothing => Unit): Unit = ()

  override def map[B](f: Nothing => B): MyStream[B] = this

  override def flatMap[B](f: Nothing => MyStream[B]): MyStream[B] = this

  override def filter(predicate: Nothing => Boolean): MyStream[Nothing] = this

  override def take(n: Int): MyStream[Nothing] = this
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
  override def ++[B >: A](anotherStream: => MyStream[B]): MyStream[B] = new Cons(head, tail ++ anotherStream)

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

  // This still preserves lazy evaluation.
  override def take(n: Int): MyStream[A] = {
    if (n <= 0) EmptyStream
    else if (n == 1) new Cons(head, EmptyStream)
    else new Cons(head, tail.take(n - 1))
  }

}

object MyStream {
  // Calling from recursively in a lazily evaluated expression.
  def from[A](start: A)(generator: A => A): MyStream[A] = {
    new Cons(start, MyStream.from(generator(start))(generator))
  }
}

object StreamsPlayground extends App {
  val naturals = MyStream.from(1)(_ + 1)
  println(naturals.head) // 1
  println(naturals.tail.head) // 2
  println(naturals.tail.tail.head) // 3

  val startFrom0 = 0 #:: naturals // rewritten to naturals.#::(0)
  println(startFrom0.head) // 0

  startFrom0.take(10000).foreach(println)

  println(startFrom0.map(_ * 2).take(100).toList())

  // List(0, 1, 1, 2, 2, 3, 3, 4, 4, 5)
  println(startFrom0.flatMap(x => new Cons(x, new Cons(x + 1, EmptyStream))).take(10).toList())

  println(startFrom0.filter(_ < 10).take(10).toList()) // List(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)

  /*
  Exercises on streams.
  1 - Stream of Fibonacci numbers.
  2 - Stream of Prime numbers with Eratosthenes' sieve.

  Eratosthenes' Sieve example:
  [2, 3, 4, ...]
  filter out all numbers divisible by 2 (except for 2)
  [2, 3, 5, 7, 9, 11, ...]
  filter out all numbers divisible by 3
  [2, 3, 5, 7, 11, 13, 17, ...]
  filter out all numbers divisible by 5
    ...
   */

  // Solution 1
  // [first, [ ...
  // [first, fibo(second, first + second)
  def fibonacci(first: BigInt, second: BigInt): MyStream[BigInt] = new Cons(first, fibonacci(second, first + second))
  println(fibonacci(1, 1).take(100).toList())

  // Solution 2
  // [2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, ...]
  // [2, 3, 5, 7, 9, 11, ...]
  // [2 eratosthenes applied to (numbers filtered by n % 2 != 0)]
  // [2, 3, eratosthenes applied to [5, 7, 9, 11, ...] filtered by n % 3 != 0]
  // [2, 3, 5, eratosthenes applied to [7, 11, ...] filtered by n % 5 != 0]
  def eratosthenes(numbers: MyStream[Int]): MyStream[Int] =
    if (numbers.isEmpty) numbers
    else new Cons(numbers.head, eratosthenes(numbers.tail.filter(_ % numbers.head != 0))) // n => n % numbers.head != 0

}
