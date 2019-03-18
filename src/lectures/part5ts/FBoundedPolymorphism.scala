package lectures.part5ts

object FBoundedPolymorphism extends App {

  /*
  trait Animal {
    def breed(): List[Animal]
  }
  class Cat extends Animal {
    override def breed(): List[Animal] = ??? // Want this to be a List[Cat] !!!
  }
  class Dog extends Animal {
    override def breed(): List[Animal] = ??? // Want this to be a List[Dog] !!!
  }
  */

  // How do I make the compiler force the above?

  /*
  // Solution 1 - Naive
  trait Animal {
    def breed(): List[Animal]
  }
  class Cat extends Animal {
    override def breed(): List[Cat] = ??? // Want this to be a List[Cat] !!!
  }
  class Dog extends Animal {
    override def breed(): List[Cat] = ??? // Want this to be a List[Dog] !!!
  }
  */

  /*
  // Solution 2 - F-Bounded Polymorphism
  trait Animal[A <: Animal[A]] { // Recursive type: F-Bounded Polymorphism
    def breed(): List[Animal[A]]
  }
  class Cat extends Animal[Cat] {
    override def breed(): List[Animal[Cat]] = List(new Cat()) // Want this to be a List[Cat] !!!
  }
  class Dog extends Animal[Dog] {
    override def breed(): List[Animal[Dog]] = List(new Dog()) // Want this to be a List[Dog] !!!
  }

  // Example often present in Database APIs or ORMs
  trait Entity[E <: Entity[E]]

  // Another example of F-Bounded Polymorphism
  class Person extends Comparable[Person] {
    override def compareTo(o: Person): Int = 1
  }

  // Not exactly right
  class Crocodile extends Animal[Dog] {
    override def breed(): List[Animal[Dog]] = List(new Dog()) // WTF! This is compile but not correct
  }
  */

  // Solution 3 - FBP with Self Types
//  trait Animal[A <: Animal[A]] { self: A =>
//    def breed(): List[Animal[A]]
//  }
//  class Cat extends Animal[Cat] {
//    override def breed(): List[Animal[Cat]] = List(new Cat()) // Want this to be a List[Cat] !!!
//  }
//  class Dog extends Animal[Dog] {
//    override def breed(): List[Animal[Dog]] = List(new Dog()) // Want this to be a List[Dog] !!!
//  }

  // Compiler error prevents us from making mistakes.
//  class Crocodile extends Animal[Dog] {
//    override def breed(): List[Animal[Dog]] = List(new Dog()) // WTF! This is compile but not correct
//  }

  // Limitation with FBP
//  trait Fish extends Animal[Fish]
//  class Shark extends Fish {
//    override def breed(): List[Animal[Fish]] = List(new Cod) // wrong! Sharks breeding Cods!
//  }
//  class Cod extends Fish {
//    override def breed(): List[Animal[Fish]] = List(new Cod)
//  }

  // Solution 4 - Using Type Classes
//  trait Animal
//  trait CanBreed[A] {
//    def breed(a: A): List[A]
//  }
//
//  class Dog extends Animal
//  object Dog {
//    implicit object DogsCanBreed extends CanBreed[Dog] {
//      override def breed(a: Dog): List[Dog] = List(new Dog)
//    }
//  }
//
//  implicit class CanBreedOps[A](animal: A) {
//    def breed(implicit canBreed: CanBreed[A]): List[A] =
//      canBreed.breed(animal)
//  }
//
//  val dog = new Dog // new CanBreedOps[Dog](dog).breed(Dog.DogsCanBreed)
//  dog.breed // implicit value to pass to breed: Dog.DogsCanBreed

  // Making this on purpose wrong.
//  class Cat extends Animal
//  object Cat {
//    implicit object CatsCanBreed extends CanBreed[Dog] {
//      override def breed(a: Dog): List[Dog] = List(new Dog)
//    }
//  }
//  val cat = new Cat
//  cat.breed // Compiler stops us because no implicits found!

  // Solution 5 - Pure Type Classes
  trait Animal[A] {
    def breed(a: A): List[A]
  }

  class Dog
  object Dog {
    implicit object DogAnimal extends Animal[Dog] {
      override def breed(a: Dog): List[Dog] = List(new Dog)
    }
  }

  class Cat
  object Cat {
    implicit object CatAnimal extends Animal[Dog] {
      override def breed(a: Dog): List[Dog] = List(new Dog)
    }
  }

  implicit class AnimalOps[A](animal: A) {
    def breed(implicit animalTypeClassInstance: Animal[A]): List[A] =
      animalTypeClassInstance.breed(animal)
  }

  val dog = new Dog
  dog.breed
  val cat = new Cat
  // cat.breed // Compiler stops us!

}
