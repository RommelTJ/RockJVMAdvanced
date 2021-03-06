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


  // Type Erasure

  // Pain Point #1 - You cannot differentiate types at runtime.
  val numbers = List(1,2,3)
  numbers match {
    case listOfStrings: List[String] => println("list of strings")
    case listOfNumbers: List[Int] => println("list of numbers")
  }
  // prints "list of strings" because the types [String] and [Int] are erased at runtime, so both are case "List"

  // Pain Point #2 - You have limitations on overloads
  // def processList(list: List[Int]): Int = 42
  // def processList(list: List[String]): Int = 43
  // After type erasure, the above methods have identical method signatures and therefore cannot be overloaded.

  // TypeTags

  // 0 - import
  import ru._

  // 1 - Create a Type Tag ("manually")
  val ttag = typeTag[Person]
  println(ttag.tpe) // lectures.part5ts.Reflection.Person

  class MyMap[K, V]

  // 2 - Pass Type Tags as implicit parameters ("preferred way")
  def getTypeArguments[T](value: T)(implicit typeTag: TypeTag[T]) = typeTag.tpe match {
    case TypeRef(_, _, typeArguments) => typeArguments
    case _ => List()
  }

  // Example
  val myMap = new MyMap[Int, String]
  val typeArgs = getTypeArguments(myMap) // (typeTag: TypeTag[MyMap[Int, String]])
  println(typeArgs) // List(Int, String)

  // Checking if a class is a subtype of another class using Type Tags.
  def isSubtype[A, B](implicit ttagA: TypeTag[A], ttagB: TypeTag[B]): Boolean = {
    ttagA.tpe <:< ttagB.tpe
  }
  class Animal
  class Dog extends Animal
  println(isSubtype[Dog, Animal]) // true!

  // Case: I have an instance already computed but using TypeTags
  // 1 - Obtain the mirror
  // Already done above.

  // 2 - Method Symbol
  val anotherMethodSymbol = typeTag[Person].tpe.decl(ru.TermName(methodName)).asMethod

  // 3 - Reflect the Method = "can DO things"
  val anotherMethod = reflected.reflectMethod(anotherMethodSymbol)

  // 4 - Invoke the method
  anotherMethod.apply() // Hi, my name is Mary.

}
