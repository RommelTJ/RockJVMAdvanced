package lectures.part1

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

}
