package lectures.part1as

object AdvancedPatternMatching extends App {

  val numbers = List(1)
  val description = numbers match {
    case head :: Nil => println(s"The only element is $head")
    case _ =>
  }

  /*
  Structures available for pattern matching:
  - Constants
  - Wildcards
  - Case Classes
  - Tuples
  - Special Magic (like above)
   */

  // Making Person class available for Pattern Matching - i.e. making an unapply method in the companion object.
  class Person(val name: String, val age: Int)
  object Person {
    def unapply(person: Person): Option[(String, Int)] =
      if (person.age < 21) None
      else Some((person.name, person.age))

    def unapply(age: Int): Option[String] = Some(if (age < 21) "minor" else "major")
  }

  val bob = new Person(name = "Bob", age = 22)
  val greeting = bob match {
    case Person(n, a) => s"Hi, my name is $n and I'm $a years old"
  }
  println(greeting)

  val bob2 = new Person(name = "Bob", age = 20)
  val legalStatus = bob2.age match {
    case Person(status) => s"I'm a $status"
  }
  println(legalStatus)

  /*
  Exercise: Devise a pattern matching solution for these conditions with custom pattern matching.
   */
  val n: Int = 45
  val mathProperty = n match {
    case x if x < 10 => "single digit"
    case x if x % 2 == 0 => "even number"
    case _ => "no property"
  }

//  object even {
//    def unapply(n: Int): Option[Boolean] = if (n % 2 == 0) Some(true) else None
//  }
  object even {
    def unapply(n: Int): Boolean = n % 2 == 0
  }
//  object single_digit {
//    def unapply(n: Int): Option[Boolean] = if(n < 10) Some(true) else None
//  }
  object single_digit {
    def unapply(n: Int): Boolean = n < 10
  }

  val m: Int = 100
  val mathPropertyTwo = m match {
    case even() => "Even"
    case single_digit() => "Single Digit"
    case _ => "No Property"
  }
  println(mathPropertyTwo)

  // Infix Patterns (e.g. '::')
  case class Or[A, B](a: A, b: B) // Either
  val either = Or(2, "two")
  val humanDescription = either match {
    // case Or(number, string) => s"$number is written as $string"
    case number Or string => s"$number is written as $string" // equivalent to the above
  }
  println(humanDescription)

  // Decomposing Sequences
  val vararg = numbers match {
    case List(1, _*) => "Starting with 1"
  }
  println(vararg)

  abstract class MyList[+A] {
    def head: A = ???
    def tail: MyList[A] = ???
  }
  case object Empty extends MyList[Nothing]
  case class Cons[+A](override val head: A, override val tail: MyList[A]) extends MyList[A]

  object MyList {
    def unapplySeq[A](list: MyList[A]): Option[Seq[A]] = {
      if (list == Empty) Some(Seq.empty)
      else unapplySeq(list.tail).map(list.head +: _)
    }
  }

  val myList: MyList[Int] = Cons(1, Cons(2, Cons(3, Empty)))
  val myList2: MyList[Int] = Cons(1, Cons(3, Cons(43, Empty)))
  val decomposed = myList match {
    case MyList(1, 2, _*) => "Starting with 1 and 2"
    case _ => "Something else"
  }
  println(decomposed)

  // Custom return types for unapply
  // Data structure that you use as your return types needs to have these methods:
  // - isEmpty: Boolean, get: Something.
  abstract class Wrapper[T] {
    def isEmpty: Boolean
    def get: T
  }

  object PersonWrapper {
    def unapply(person: Person): Wrapper[String] = new Wrapper[String] {
      def isEmpty = false
      def get: String = person.name
    }
  }

  val test = bob match {
    case PersonWrapper(n) => s"This person's name is $n"
    case _ => "An Alien"
  }
  println(test)

}
