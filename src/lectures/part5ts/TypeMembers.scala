package lectures.part5ts

object TypeMembers extends App {

  class Animal
  class Dog extends Animal
  class Cat extends Animal

  class AnimalCollection {
    type AnimalType // abstract type member
    type BoundedAnimal <: Animal // abstract type member upper-bounded with Animal
    type SuperBoundedAnimal >: Dog <: Animal // super-bounded on Dog and upper-bounded on Animal
    type AnimalC = Cat // another name for an existing type. Also an abstract type member
  }

}
