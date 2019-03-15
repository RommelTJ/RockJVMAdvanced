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

  // Valid
  class LeadSinger extends Singer with Instrumentalist {
    override def sing(): Unit = println("Singing")
    override def play(): Unit = println("Cowbell")
  }

  // Illegal - Vocalist doesn't conform to Instrumentalist
//  class Vocalist extends Singer {
//    override def sing(): Unit = ???
//  }

}
