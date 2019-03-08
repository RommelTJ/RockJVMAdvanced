package lectures.part5ts

object RockingInheritance extends App {

  // Convenience
  trait Writer[T] {
    def write(value: T): Unit
  }
  trait Closeable {
    def close(status: Int): Unit
  }

}
