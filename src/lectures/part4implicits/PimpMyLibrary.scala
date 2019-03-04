package lectures.part4implicits

object PimpMyLibrary extends App {

  // 2.isPrime
  implicit class RichInt(value: Int) {
    def isEven: Boolean = value % 2 == 0
    def squareRoot: Double = Math.sqrt(value)
  }

  new RichInt(42).squareRoot

  println(42.isEven) // Type enrichment or "pimping"
  // Rewritten as new RichInt(42).isEven()

  1 to 10 // example of Pimping

  import scala.concurrent.duration._
  3.seconds // another example

}
