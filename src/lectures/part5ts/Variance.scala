package lectures.part5ts

object Variance extends App {

  trait Animal
  class Dog extends Animal
  class Cat extends Animal
  class Crocodile extends Animal

  // What is variance?
  // "inheritance" - type substitution of Generics

  class Cage[T]
  // Should a Cage[Cat] also inherit from Cage[Animal]?
  // Possible Answers:
  // 1 - yes - Covariance
  class CCage[+T]
  val ccage: CCage[Animal] = new CCage[Cat]

  // 2 - no - Invariance
  class ICage[T]
  // val icage: ICage[Animal] = new ICage[Cat] // wrong!
  // val x: Int = "hello" // just as wrong!

  // 3 - Hell No! (opposite) - Contravariance
  class XCage[-T]
  val xcage: XCage[Cat] = new XCage[Animal] // valid!
  // I.e. Replacing a specific cage of cat with a generic cage of animal

  class InvariantCage[T](val animal: T) // Invariant

  // Covariant Positions
  class CovariantCage[+T](val animal: T) // animal is in a COVARIANT POSITION
  // compiler accepts a field in a covariant position with an invariant type

  // class ContravariantCage[-T](val animal: T) // boom!
  // compiler doesn't accept a field in a contravariant position with an invariant type
  // Why? If the compiler allowed it, then I would be able to write something like:
  // val catCage: XCage[Cat] = new XCage[Animal](new Crocodile)

  // class CovariantVariableCage[+T](var animal: T) // boom!
  // compiler doesn't accept a var of covariant type in a contravariant position
  // Types of vars are in CONTRAVARIANT POSITION
  // Why? If the compiler allowed it, then I would be able to write something like:
  // val ccage: CCage[Animal] = new CCage[Cat](new Cat)
  // ccage.animal = new Crocodile

  // class ContravariantVariableCage[-T](var animal: T) // this also won't compile -> also in COVARIANT POSITION
  // Why? Contravariant Type T (-T) occurs in covariant position in type T of variable animal
  // val catCage: XCage[Cat] = new XCage[Animal](new Crocodile)

  // Because variables are in Covariant and Contravariant position, the only acceptable type for a variable field is
  // INVARIANT
  class InvariantVariableCage[T](var animal: T) // OK

//  trait AnotherCovariantCage[+T] {
//    def addAnimal(animal: T) // method arguments are in CONTRAVARIANT POSITION
//  }
  /*
  val ccage: CCage[Animal] = new CCage[Dog]
  ccage.add(new Cat) // wrong!
   */

  class AnotherContravariantCage[-T] {
    def addAnimal(animal: T) = true // OK!
  }
  val acc: AnotherContravariantCage[Cat] = new AnotherContravariantCage[Animal]
  // acc.addAnimal(new Dog) // Compiler stops you!
  acc.addAnimal(new Cat)
  class Kitty extends Cat
  acc.addAnimal(new Kitty)

  class MyList[+A] {
    // def add(element: A): MyList[A] // wrong!
    def add[B >: A](element: B): MyList[B] = new MyList[B] // widening the type! This is what we want.
  }
  val emptyList = new MyList[Kitty]
  val animals = emptyList.add(new Kitty)
  val moreAnimals = animals.add(new Cat)
  animals.add(moreAnimals)
  val evenMoreAnimals = moreAnimals.add(new Dog)
  animals.add(evenMoreAnimals)

  // METHOD ARGUMENTS ARE IN CONTRAVARIANT POSITION

  // Return Types
  class PetShop[-T] {
    // def get(isItAPuppy: Boolean): T // Method return types are in covariant position
    /*
    Why not allowed?
    val catShop = new PetShop[Animal] {
      def get(isItAPuppy: Boolean): Animal = new Cat
    }
    val dogShop: PetShop[Dog] = catShop
    dogShop.get(true) => Evil Cat!
     */

    def get[S <: T](isItAPuppy: Boolean, defaultAnimal: S): S = defaultAnimal
  }
  val shop: PetShop[Dog] = new PetShop[Animal]
  // val evilCat = shop.get(true, new Cat) // Compiler error!
  class TerraNova extends Dog
  val bigFurry = shop.get(true, new TerraNova) // OK!

}
