package lectures.part1as

import scala.annotation.tailrec

object Recap extends App {

  val aCondition: Boolean = false
  val aConditionedVal = if (aCondition) 42 else 65
  // instructions vs expressions

  // Compiler infers types for us
  val aCodeBlock = {
    if (aCondition) 54
    56
  }

  // Unit - expressions that only do side-effects (void) in Java.
  val theUnit: Unit = println("Hello Scala")

  // Functions
  def aFunction(x: Int): Int = x + 1

  // Recursion: Stack and Tail. Tailrec avoids Stack Overflow errors.
  @tailrec def factorial(n: Int, accumulator: Int): Int =
    if (n <= 0) accumulator
    else factorial(n - 1, n * accumulator)

  // Object Orientated Programming
  class Animal
  class Dog extends Animal
  val aDog: Animal = new Dog // Polymorphism by subtyping

  trait Carnivore {
    def eat(a: Animal): Unit
  }

  class Crocodile extends Animal with Carnivore {
    override def eat(a: Animal): Unit = println("Crunch!")
  }

  // Method notations
  val aCroc = new Crocodile
  aCroc.eat(aDog)
  aCroc eat aDog // natural language

  1 + 2
  1.+(2) // Equivalent

  // Anonymous Classes
  val aCarnivore = new Carnivore {
    override def eat(a: Animal): Unit = println("Nom!")
  }

  // Generics
  abstract class MyList[+A] // (covariant) Variance and Variance Problems in this course.

  // Singletons and Companions
  object MyList

  // Case Classes
  case class Person(name: String, age: Int)

  // Exceptions and Try-Catch-Finally expressions
  // val throwsException = throw new RuntimeException // Type = Nothing.
  val aPotentialFailure = try {
    throw new RuntimeException
  } catch {
    case e: Exception => println("I caught an exception")
  } finally {
    println("Some Logs")
  }

  // Packaging and imports - won't go into that this much in this course.

  // Functional Programming
  val incrementer = new Function1[Int, Int] {
    override def apply(v1: Int): Int = v1 + 1
  }
  println(incrementer(1))

  val anonymousIncrementer = (x: Int) => x + 1
  println(anonymousIncrementer(4))

  val x = List(1, 2, 3).map(anonymousIncrementer)
  println(x) // List(2, 3, 4) - Higher-Order Function
  // map, flatMap, filter.

  // For-Comprehension
  val pairs = for {
    num <- List(1, 2, 3) // if condition (guards)
    char <- List('a', 'b', 'c')
  } yield num + "-" + char
  println(pairs) // List(1-a, 1-b, 1-c, 2-a, 2-b, 2-c, 3-a, 3-b, 3-c)

  // Scala Collections: Seqs, Arrays, Lists, Vectors, Maps, Tuples.
  val aMap = Map("Daniel" -> 789, "Jess" -> 555)

  // "Collections" - more like Abstract computations (Options, Try)
  val anOption = Some(2)

  // Pattern matching
  val z = 2
  val order = z match {
    case 1 => "first"
    case 2 => "second"
    case 3 => "third"
    case _ => x + "th"
  }

  val bob = Person("Bob", 22)
  val greeting = bob match {
    case Person(n, _) => s"Hi, my name is $n"
  }
  println(greeting)

}
