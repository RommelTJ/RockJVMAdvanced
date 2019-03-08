package lectures.part5ts

object RockingInheritance extends App {

  // Convenience
  trait Writer[T] {
    def write(value: T): Unit
  }
  trait Closeable {
    def close(status: Int): Unit
  }
  trait GenericStream[T] {
    // some methods
    def foreach(f: T => Unit): Unit
  }

  // Convenience method - Whenever you don't know who exactly mixes in our specific traits you can
  // use them all in a specific type "GenericStream[T] with Writer[T] with Closeable"
  def processStream[T](stream: GenericStream[T] with Writer[T] with Closeable): Unit = {
    stream.foreach(println)
    stream.close(200)
  }

}
