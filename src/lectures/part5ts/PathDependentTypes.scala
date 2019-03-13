package lectures.part5ts

object PathDependentTypes extends App {

  class Outer {
    class Inner
    object InnerObject
    type InnerType
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

}
