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

  /*
  Big rule:
  - Method arguments are in CONTRAVARIANT position
  - Return types are in COVARIANT position
   */

  /**
    * Exercise:
    * 1. Design an Invariant, Covariant, and Contravariant
    * Parking[T](things: List[T]) {
    *   park(vehicle: T)
    *   impound(vehicles: List[T])
    *   checkVehicles(conditions: String): List[T]
    * }
    *
    * 2. What if we used someone else's (invariant) API: IList[T]
    * 3. Make Parking a Monad!
    *   - flatMap
    */
  class Vehicle
  class Bike extends Vehicle
  class Car extends Vehicle
  class IList[T]

  // Solution 1
  // Invariant - Limiting because they only allow one type
  class IParking[T](vehicles: List[T]) {
    def park(vehicle: T): IParking[T] = ???
    def impound(vehicles: List[T]): IParking[T] = ???
    def checkVehicles(conditions: String): List[T] = ???
  }

  // Rule of thumb:
  // Use Covariance = COLLECTION OF THINGS
  // Use Contravariance = GROUP OF ACTIONS

  // Covariant
  class CParking[+T](vehicles: List[T]) {
    def park[S >: T](vehicle: S): CParking[S] = ???
    def impound[S >: T](vehicles: List[S]): CParking[S] = ???
    def checkVehicles(conditions: String): List[T] = ???
  }

  // Contravariant
  class XParking[-T](vehicles: List[T]) {
    def park(vehicle: T): XParking[T] = ???
    def impound(vehicles: List[T]): XParking[T] = ???
    def checkVehicles[S <: T](conditions: String): List[S] = ???
  }

  // Solution 2
  // Invariant would stay the same.

  // Covariant
  class CParking2[+T](vehicles: IList[T]) {
    def park[S >: T](vehicle: S): CParking2[S] = ???
    def impound[S >: T](vehicles: IList[S]): CParking2[S] = ???
    def checkVehicles[S >: T](conditions: String): IList[S] = ???
  }

  // Contravariant
  class XParking2[-T](vehicles: IList[T]) {
    def park(vehicle: T): XParking2[T] = ???
    def impound[S <: T](vehicles: IList[S]): XParking2[S] = ???
    def checkVehicles[S <: T](conditions: String): IList[S] = ???
  }

  // Solution 3
  // Invariant
  class IParking3[T](vehicles: List[T]) {
    def park(vehicle: T): IParking3[T] = ???
    def impound(vehicles: List[T]): IParking3[T] = ???
    def checkVehicles(conditions: String): List[T] = ???

    def flatMap[S](f: T => IParking3[S]): IParking3[S] = ???
  }

  // Covariant
  class CParking3[+T](vehicles: List[T]) {
    def park[S >: T](vehicle: S): CParking3[S] = ???
    def impound[S >: T](vehicles: List[S]): CParking3[S] = ???
    def checkVehicles(conditions: String): List[T] = ???

    def flatMap[S](f: T => CParking3[S]): CParking3[S] = ???
  }

  // Contravariant
  class XParking3[-T](vehicles: List[T]) {
    def park(vehicle: T): XParking3[T] = ???
    def impound(vehicles: List[T]): XParking3[T] = ???
    def checkVehicles[S <: T](conditions: String): List[S] = ???

    def flatMap[R <: T, S](f: R => XParking3[S]): XParking3[S] = ???
  }

}
