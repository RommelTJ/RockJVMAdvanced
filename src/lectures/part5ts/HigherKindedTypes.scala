package lectures.part5ts

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object HigherKindedTypes extends App {

  trait AHigherKindedType[F[_]]

  trait MyList[T] {
    def flatMap[B](f: T => B): MyList[B]
  }

  trait MyOption[T] {
    def flatMap[B](f: T => B): MyOption[B]
  }

  trait MyFuture[T] {
    def flatMap[B](f: T => B): MyFuture[B]
  }

  // Combine/multiply List(1,2) * List("a", "b") => List(1a, 1b, 2a, 2b)
  // etc.

  def multiply[A, B](listA: List[A], listB: List[B]): List[(A, B)] =
    for {
      a <- listA
      b <- listB
    } yield (a, b)

  def multiply[A, B](listA: Option[A], listB: Option[B]): Option[(A, B)] =
    for {
      a <- listA
      b <- listB
    } yield (a, b)

  def multiply[A, B](listA: Future[A], listB: Future[B]): Future[(A, B)] =
    for {
      a <- listA
      b <- listB
    } yield (a, b)

  // Use Higher Kinded Type

  trait Monad[F[_], A] {
    def flatMap[B](f: A => F[B]): F[B]
    def map[B](f: A => B): F[B]
  }

  class MonadList extends Monad[List, Int] {
    override def flatMap[B](f: Int => List[B]): List[B] = List()
    override def map[B](f: Int => B): List[B] = List()
  }

  def multiply[F[_], A, B](ma: Monad[F, A], mb: Monad[F, B]): F[(A, B)] =
    for {
      a <- ma
      b <- mb
    } yield (a, b)

  val monadList = new MonadList
  monadList.flatMap(x => List(x, x + 1)) // List[Int]
  // Monad[List, Int] => List[Int]
  monadList.map(_ * 2) // List[Int]
  // Monad[List, Int] => List[Int]

  multiply(new MonadList, new MonadList)

}
