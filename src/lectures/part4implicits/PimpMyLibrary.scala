package lectures.part4implicits

object PimpMyLibrary extends App {

  // 2.isPrime
  implicit class RichInt(val value: Int) extends AnyVal {
    def isEven: Boolean = value % 2 == 0
    def squareRoot: Double = Math.sqrt(value)
  }

  implicit class RicherInt(richInt: RichInt) {
    def isOdd: Boolean = richInt.value % 2 != 0
  }
  new RichInt(42).squareRoot

  println(42.isEven) // Type enrichment or "pimping"
  // Rewritten as new RichInt(42).isEven()

  1 to 10 // example of Pimping

  import scala.concurrent.duration._
  3.seconds // another example

  // Compiler doesn't do multiple implicit searches.
  // 42.isOdd // Cannot compile
}
