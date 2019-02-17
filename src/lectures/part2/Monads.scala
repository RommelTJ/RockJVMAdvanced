package lectures.part2

object Monads extends App {

  // Our Own "Try" Monad.
  trait Attempt[+A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B]
  }

  object Attempt {
    def apply[A](a: => A): Attempt[A] =
      try {
        Success(a)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Success[+A](value: A) extends Attempt[A] {
    override def flatMap[B](f: A => Attempt[B]): Attempt[B] =
      try {
        f(value)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Fail(e: Throwable) extends Attempt[Nothing] {
    override def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = this
  }

  /*
  Monad laws and testing
  1 - Left-Identity:
      unit.flatMap(f) = f(x)
      Attempt(x).flatMap(f) = f(x) // Only makes sense for Success case.
      Success(x).flatMap(f) = f(x) // Proved!

  2 - Right-identity:
      attempt.flatMap(unit) = attempt
      Success(x).flatMap(x => Attempt(x)) = Attempt(x) = Success(x)
      Fail(_).flatMap(x) = Fail(e) // Proved!

  3 - Associativity:
      attempt.flatMap(f).flatMap(g) == attempt.flatMap(x => f(x).flatMap(g))
      Fail(e).flatMap(f).flatMap(g) = Fail(e)
      Fail(e).flatMap(x => f(x).flatMap(g)) = Fail(e) // Satisfied for Fail.
      Success(v).flatMap(f).flatMap(g) =
        f(v).flatMap(g) OR Fail(e)
      Success(v).flatMap(x => f(x).flatMap(g)) =
        f(v).flatMap(g) OR Fail(e) // Proved!
   */

  // Testing
  val attempt = Attempt {
    throw new RuntimeException("My own monad!")
  }
  println(attempt)

}
