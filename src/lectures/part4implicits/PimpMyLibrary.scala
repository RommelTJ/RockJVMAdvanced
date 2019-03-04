package lectures.part4implicits

object PimpMyLibrary extends App {

  // 2.isPrime
  implicit class RichInt(value: Int) {
    def isEven: Boolean = value % 2 == 0
    def squareRoot: Double = Math.sqrt(value)
  }

}
