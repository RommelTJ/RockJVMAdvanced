package lectures.part5ts

object Reflection extends App {

  // Reflection
  // How do I instantiate a class by calling its name at runtime?

  // Scala Reflection API + Macros + Quasiquotes
  // METAPROGRAMMING (outside the scope here)

  case class Person(name: String) {
    def sayMyName(): Unit = println(s"Hi, my name is $name.")
  }

  // How to use the Scala Reflection API

  // 0 - import
  import scala.reflect.runtime.{universe => ru}

  // 1 - Instantiate a MIRROR
  val m = ru.runtimeMirror(getClass.getClassLoader)

  // 2 - Create a Class object = "description"
  val clazz = m.staticClass("lectures.part5ts.Reflection.Person") // Creating a class object by NAME.

  // 3 - Create a reflected mirror = "can DO things"
  val cm = m.reflectClass(clazz)

  // 4 - Get the constructor
  val constructor = clazz.primaryConstructor.asMethod

  // 5 - Create a reflected constructor
  val constructorMirror = cm.reflectConstructor(constructor)

  // 6 - Invoke the constructor
  val instance = constructorMirror.apply("Liza")
  println(instance) // Person(Liza)

  // Case: I have an instance already computed
  val p = Person("Mary") // from the "wire" as a serialized object
  val methodName = "sayMyName" // method name computed from somewhere else

  // 1 - Obtain the mirror
  // Already done above.

  // 2 - Reflect the instance
  val reflected = m.reflect(p)

  // 3 - Method Symbol
  val methodSymbol = ru.typeOf[Person].decl(ru.TermName(methodName)).asMethod

  // 4 - Reflect the Method = "can DO things"
  val method = reflected.reflectMethod(methodSymbol)

  // 5 - Invoke the method
  method.apply() // Hi, my name is Mary.

}
