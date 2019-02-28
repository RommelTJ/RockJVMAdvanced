package lectures.part4implicits

object ImplicitsIntro extends App {

  val pair = "Rommel" -> "619" // How does this compile?
  val intPair = 1 -> 2 // Implicits!

  case class Person(name: String) {
    def greet = s"Hi, my name is $name!"
  }
  implicit def fromStringToPerson(str: String): Person = Person(str)
  println("Peter".greet) // Hi, my name is Peter!

}
