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

}
