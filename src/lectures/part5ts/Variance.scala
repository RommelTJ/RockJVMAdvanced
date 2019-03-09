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

}
