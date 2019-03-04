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
  implicit object UserSerializer extends HTMLSerializer[User] {
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

  // Companion object serializer with implicit
  object HTMLSerializer {
    def serialize[T](value: T)(implicit serializer: HTMLSerializer[T]): String =
      serializer.serialize(value)
    def apply[T](implicit serializer: HTMLSerializer[T]) = serializer
  }

  implicit object IntSerializer extends HTMLSerializer[Int] {
    override def serialize(value: Int): String = s"<div style='color:blue;'>$value</div>"
  }
  println(HTMLSerializer.serialize(42)(IntSerializer))
  println(HTMLSerializer.serialize(42)) // Not magic! Implicit parameter!
  println(HTMLSerializer.serialize(rommel)) // Double not magic!

  // Access to the entire Type Class interface
  println(HTMLSerializer[User].serialize(rommel))

  // Part 3

  implicit class HTMLEnrichment[T](value: T) {
    def toHTMLEnriched(implicit serializer: HTMLSerializer[T]): String = serializer.serialize(value)
  }
  // Compiler: println(new HTMLEnrichment[User](rommel).toHTMLEnriched(UserSerialize))
  println(s"here ==> ${rommel.toHTMLEnriched(UserSerializer)}")
  println(s"here2 ==> ${rommel.toHTMLEnriched}") // Using an implicit param
  /*
  Advantages:
  - Can extend functionality to new types
  - Can have different implementations for the same type
    -> Either by importing an implicit into the local scope,
    -> Or, passing the serializer explicitly
  - Super expressive
   */
  println(2.toHTMLEnriched)
  println(rommel.toHTMLEnriched(PartialUserSerializer))

  /*
  Type class pattern now composes of several parts:
  - Type Class itself (HTMLSerializer[T] { ... })
  - Type Class instances (some of which are implicit) (UserSerializer, IntSerializer)
  - Conversion with implicit classes (HTMLEnrichment)
   */

  // Context bounds

  def htmlBoilerplate[T](content: T)(implicit serializer: HTMLSerializer[T]): String =
    s"<html><body>${content.toHTMLEnriched(serializer)}</body></html>"

  def htmlSugar[T: HTMLSerializer](content: T): String =
    s"<html><body>${content.toHTMLEnriched}</body></html>"

  // Implicitly

  case class Permissions(mask: String)

}
