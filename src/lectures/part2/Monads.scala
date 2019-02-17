package lectures.part2

object Monads extends App {

  trait Attempt[A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B]
  }

}
