package lectures.part5ts

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

}
