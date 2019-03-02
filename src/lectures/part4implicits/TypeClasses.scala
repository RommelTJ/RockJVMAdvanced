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
  val rommel = User("Rommel", 30, "rommel@myemail.com") // .toHTML

  // Option 2 - Pattern Matching
  // Disadvantages:
  // Lost type safety
  // Need to modify code every time
  // Still only ONE implementation.
  object HTMLSerializerPatternMatching {
    def serializeToHTML(value: Any) = value match {
      case User(n, a, e) =>
      case _ =>
    }
  }

  // Better design
  // Advantages:
  // We can define serializers for other types
  // We can define MULTIPLE serializers for a certain type
  trait HTMLSerializer[T] {
    def serialize(value: T): String
  }
  object UserSerializer extends HTMLSerializer[User] {
    def serialize(user: User): String = s"<div>${user.name} (${user.age} yo) <a href='${user.email}'/> </div"
  }
  println(UserSerializer.serialize(rommel))

  // Defining serializer for some other (random) type
  import java.util.Date
  object DateSerializer extends HTMLSerializer[Date] {
    def serialize(date: Date): String = s"<div>${date.toString}</div>"
  }

  // Defining multiple serializers
  object PartialUserSerializer extends HTMLSerializer[User] {
    override def serialize(user: User): String = s"<div>${user.name}</div>"
  }

  // Type Class Template
  trait MyTypeClassTemplate[T] {
    def action(value: T): String
  }

  /*
  Exercise:
  Implement an equal type class that compares two values.
  Then implement two instances of this Equal Type class that compare users by name and name and email.
   */

  // Exercise solution
  trait Equal[T] {
    def apply(a: T, b: T): Boolean
  }
  val rommel2 = User("Rommel", 30, "rommel@myemail2.com")
  object UserNameCompare extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name
  }
  object UserNameEmailCompare extends Equal[User] {
    override def apply(a: User, b: User): Boolean = {
      (a.name == b.name) && (a.email == b.email)
    }
  }
  println(UserNameCompare(rommel, rommel2))
  println(UserNameEmailCompare(rommel, rommel2))

  // Companion object serializer with implicit
  object HTMLSerializer {
    def serialize[T](value: T)(implicit serializer: HTMLSerializer[T]): String =
      serializer.serialize(value)
  }

  implicit object IntSerializer extends HTMLSerializer[Int] {
    override def serialize(value: Int): String = s"<div style='color:blue;'>$value</div>"
  }
  println(HTMLSerializer.serialize(42)(IntSerializer))
  println(HTMLSerializer.serialize(42)) // Not magic! Implicit parameter!

}
