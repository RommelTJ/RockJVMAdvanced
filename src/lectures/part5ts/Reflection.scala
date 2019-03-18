package lectures.part5ts

object Reflection extends App {

  // Reflection
  // How do I instantiate a class by calling its name at runtime?

  // Scala Reflection API + Macros + Quasiquotes
  // METAPROGRAMMING (outside the scope here)

  case class Person(name: String) {
    def sayMyName(): Unit = println(s"Hi, my name is $name.")
  }

}
