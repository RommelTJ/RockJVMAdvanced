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

}
