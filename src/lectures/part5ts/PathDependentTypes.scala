package lectures.part5ts

object PathDependentTypes extends App {

  class Outer {
    class Inner
    object InnerObject
    type InnerType

    def print(i: Inner) = println(i)
    def printGeneral(i: Outer#Inner) = println(i)
  }

  // Inner class in a method
  def aMethod: Int = {
    class HelperClass
    type HelperType = String
    2
  }

  // per-instance
  val o = new Outer
  // val inner = new Inner // Wrong!
  // val inner = new Outer.Inner // Wrong!
  val inner = new o.Inner // Ok

  val oo = new Outer
  // val otherInner: oo.Inner = new o.Inner // Bad

  o.print(inner) // Ok
  // oo.print(inner) // Wrong!

  // These are called path-dependent types

  // All inner types have common supertype: Outer#Inner.
  o.printGeneral(inner)
  oo.printGeneral(inner)

  /*
  Exercise:
  Developer of a small database keyed by Int or String, but maybe others.
  Hint:
  Use path-dependent types
  Abstract type members and/or type aliases
   */
  trait Item[Key]
  trait IntItem extends Item[Int]
  trait StringItem extends Item[String]

  def get[ItemType](key: Key): ItemType
  get[IntItem](42) // ok
  get[StringItem]("home") // ok
  get[IntItem]("scala") // not ok

}
