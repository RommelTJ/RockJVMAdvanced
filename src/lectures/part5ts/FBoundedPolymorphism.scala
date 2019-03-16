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

}
