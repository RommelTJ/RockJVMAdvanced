package lectures.part5ts

object SelfTypes extends App {

  // Self Types are a way of requiring a type to be mixed in.

  trait Instrumentalist {
    def play(): Unit
  }

  // Marker at language level that forces whoever implements Singer to implement Instrumentalist
  trait Singer { self: Instrumentalist =>
    def sing(): Unit
  }

}
