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

}
