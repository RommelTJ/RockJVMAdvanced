package exercises

object EqualityPlayground extends App {

  trait HTMLWriteable {
    def toHTML: String
  }

  case class User(name: String, age: Int, email: String) extends HTMLWriteable {
    override def toHTML: String = s"<div>$name ($age yo) <a href=$email/> </div"
  }

  // This only works for the types we write.
  // This is only ONE implementation out of quite a number.
  val rommel = User("Rommel", 30, "rommel@myemail.com") // .toHTML

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
  implicit object UserNameCompare extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name
  }
  object UserNameEmailCompare extends Equal[User] {
    override def apply(a: User, b: User): Boolean = {
      (a.name == b.name) && (a.email == b.email)
    }
  }
  println(UserNameCompare(rommel, rommel2))
  println(UserNameEmailCompare(rommel, rommel2))

  /*
  Exercise:
  Implement Type Class Pattern for the Equality Type Class.
   */

  // Solution
  object Equal {
    def apply[T](a: T, b: T)(implicit equalizer: Equal[T]): Boolean =  equalizer.apply(a, b)
  }
  val rommel3 = User("Rommel", 45, "rommel3@testemail.com")
  println(Equal(rommel3, rommel)) // Ad-hoc polymorphism

  /*
  Exercise:
  Improve Equal Type Class with an implicit conversion class
  - Add ===(anotherValue: T)
  - Add !==(anotherValue: T)
   */

  implicit class TypeSafeEqual[T](value: T) {
    def ===(anotherValue: T)(implicit equalizer: Equal[T]): Boolean = equalizer(value, anotherValue)
    def !==(anotherValue: T)(implicit equalizer: Equal[T]): Boolean = !equalizer(value, anotherValue)
  }
  
}
