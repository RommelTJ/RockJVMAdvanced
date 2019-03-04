package lectures.part4implicits

object PimpMyLibrary extends App {

  // 2.isPrime
  implicit class RichInt(val value: Int) extends AnyVal {
    def isEven: Boolean = value % 2 == 0
    def squareRoot: Double = Math.sqrt(value)

    def times(function: () => Unit): Unit = {
      def timesAux(n: Int): Unit =
        if (n <= 0) ()
        else {
          function()
          timesAux(n - 1)
        }
      timesAux(value)
    }

    def *[T](list: List[T]): List[T] = {
      def concatenate(n: Int): List[T] =
        if (n <= 0) List()
        else concatenate(n - 1) ++ list
      concatenate(value)
    }
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

  /*
  Exercise:
  1 - Enrich the String class by
    -> Add asInt method
    -> Add encrypt method (Caesar cypher), e.g. "John" with 2 chars -> "Lqjp"

  2 - Keep enriching the Int class by
    -> times(function)
       3.times(() => ...)
    -> *
       3 * List(1, 2) => List(1, 2, 1, 2, 1, 2)
   */

  // Solution 1
  implicit class RichString(val value: String) extends AnyVal {
    def asInt: Int = Integer.valueOf(value)
    def encrypt(n: Int): String = value.map(c => (c + n).asInstanceOf[Char])
  }
  println("22".asInt)
  println("dfhfdh".encrypt(4))

  // Solution 2
  // See above

}
