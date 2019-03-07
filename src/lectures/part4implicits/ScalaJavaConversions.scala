package lectures.part4implicits

import java.{util => ju}

object ScalaJavaConversions extends App {

  // Object that implicitly converts to Java
  import collection.JavaConverters._
  val javaSet: ju.Set[Int] = new ju.HashSet[Int]()
  (1 to 5).foreach(javaSet.add)
  println(javaSet)
  val scalaSet = javaSet.asScala // implicit wrapping conversion to mutable Set

  /* Conversion methods that exist:
  - Iterable
  - Iterator
  - ju.List - scala.mutable.Buffer
  - ju.Set - scala.mutable.Set
  - ju.Map - scala.mutable.Map
   */

  // Converting to Java through implicit wrapping
  import collection.mutable._
  val numbersBuffer = ArrayBuffer[Int](1, 2, 3)
  val juNumbersBuffer = numbersBuffer.asJava
  println(juNumbersBuffer.asScala eq numbersBuffer) // true

  // Comparing converted collections
  val numbers = List(1, 2, 3)
  val juNumbers = numbers.asJava
  val backToScala = juNumbers.asScala
  println(backToScala eq numbers) // false! Because not referentially correct (different instances)
  println(backToScala == numbers) // true! Deep equality.
  // juNumbers.add(7) // EXCEPTION! Can't do this because juNumbers is immutable.

  /*
  Exercise:
  Create a Scala to Java Conversion between an Optional and a Scala Option
   */

}
