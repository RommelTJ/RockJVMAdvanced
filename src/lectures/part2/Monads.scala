package lectures.part2

object Monads extends App {

  // Our Own "Try" Monad.
  trait Attempt[A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B]
  }

  object Attempt {
    def apply[A](a: => A): Attempt[A] = ???
  }

  case class Success[A](value: A) extends Attempt[A]

}
