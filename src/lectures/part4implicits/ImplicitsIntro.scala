package lectures.part4implicits

object ImplicitsIntro extends App {

  val pair = "Rommel" -> "619" // How does this compile?
  val intPair = 1 -> 2 // Implicits!

}
