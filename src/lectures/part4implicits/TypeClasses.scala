package lectures.part4implicits

object TypeClasses extends App {

  trait HTMLWriteable {
    def toHTML: String
  }

}
