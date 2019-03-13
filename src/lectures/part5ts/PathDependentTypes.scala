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

}
