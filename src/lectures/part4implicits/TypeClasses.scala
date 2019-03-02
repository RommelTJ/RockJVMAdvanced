package lectures.part4implicits

object TypeClasses extends App {

  trait HTMLWriteable {
    def toHTML: String
  }

  case class User(name: String, age: Int, email: String) extends HTMLWriteable {
    override def toHTML: String = s"<div>$name ($age yo) <a href=$email/> </div"
  }

  // This only works for the types we write.
  // This is only ONE implementation out of quite a number.
  User("Rommel", 30, "rommel@myemail.com").toHTML

  // Option 2 - Pattern Matching
  // Disadvantages:
  // Lost type safety
  // Need to modify code every time
  // Still only ONE implementation.
  object HTMLSerializerPatternMatching {
    def serializeToHTML(value: Any) = value match {
      case User(n, a, e) =>
      case java.util.Date =>
      case _ =>
    }
  }

}
