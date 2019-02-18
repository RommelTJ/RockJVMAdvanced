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

  // Exercise 1: Implement a Lazy[T] Monad = computation which will only be executed when it's needed.
  //             Unit/Apply
  //             flatMap
  // Exercise 2: Monads = unit + flatMap
  //             Monads = unit + map + flatten
  //             Monad[T] {
  //               def flatMap[B](f: T => Monad[B]): Monad[B] = ... (already implemented)
  //               def map[B](f: T => B): Monad[B] = ???
  //               def flatten(m: Monad[Monad[T]]): Monad[T] = ???
  //             }
  //             (Have List in mind)

  // 1 - Lazy Monad
  class Lazy[+A](value: => A) {
    // Call by need
    private lazy val internalValue = value
    def flatMap[B](f: (=> A) => Lazy[B]): Lazy[B] = f(internalValue)
    def use: A = internalValue
  }
  object Lazy {
    def apply[A](value: => A): Lazy[A] = new Lazy(value)
  }

  val lazyInstance = Lazy {
    println("Today I don't feel like doing anything!")
    42
  }
  println(lazyInstance.use) // lazy instance only now gets evaluated.

  val flatMappedInstance = lazyInstance.flatMap(x => Lazy {
    10 * x
  })
  val flatMappedInstance2 = lazyInstance.flatMap(x => Lazy {
    10 * x
  })
  flatMappedInstance.use
  flatMappedInstance2.use

  /*
  left identity:
  unit.flatMap(f) = f(v)
  Lazy(v).flatMap(f) = f(v)

  right identity:
  1.flatMap(unit) = 1
  Lazy(v).flatMap(x => Lazy(x)) = Lazy(v)

  Associativity:
  1.flatMap(f).flatMap(g) = 1.flatMap(x => f(x).flatMap(g))
  Lazy(v).flatMap(f).flatMap(g) = f(v).flatMap(g)
  Lazy(v).flatMap(x => f(x).flatMap(g)) = f(v).flatMap(g)
   */

  // 2 - map && flatten in terms of flatMap
  /*
  Monad[T] {
    def flatMap[B](f: T => Monad[B]): Monad[B] = ... (already implemented)

    def map[B](f: T => B): Monad[B] = flatMap(x => unit(f(x))) // Monad[B]
    def flatten(m: Monad[Monad[T]]): Monad[T] = ???
  }
   */

}
