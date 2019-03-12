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

  val ac = new AnimalCollection
  val dog: ac.AnimalType = ??? // no constructor to go from here
  // val cat: ac.BoundedAnimal = new Cat // wrong! compiler doesn't know what kind of animal
  val pup: ac.SuperBoundedAnimal = new Dog // Ok, because Dog is a super type
  val cat: ac.AnimalC = new Cat // Ok

  type CatAlias = Cat
  val anotherCat: CatAlias = new Cat

  // Valid overriding of a type member. Alternative to Generics.
  trait MyList{
    type T
    def add(element: T): MyList
  }
  class NonEmptyList(value: Int) extends MyList {
    override type T = Int
    override def add(element: Int): MyList = ???
  }

}
