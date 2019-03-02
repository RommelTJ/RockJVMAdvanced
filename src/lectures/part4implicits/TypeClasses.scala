package lectures.part4implicits

object TypeClasses extends App {

  trait HTMLWriteable {
    def toHTML: String
  }

  case class User(name: String, age: Int, email: String) extends HTMLWriteable {
    override def toHTML: String = s"<div>$name ($age yo) <a href=$email/> </div"
  }

}
