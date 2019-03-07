package lectures.part4implicits

import java.{util => ju}

object ScalaJavaConversions extends App {

  // Object that implicitly converts to Java
  import collection.JavaConverters._
  val javaSet: ju.Set[Int] = new ju.HashSet[Int]()
  (1 to 5).foreach(javaSet.add)
  println(javaSet)
  val scalaSet = javaSet.asScala // implicit conversion to mutable Set

  /* Conversion methods that exist:
  - Iterable
  - Iterator
  - ju.List - scala.mutable.Buffer
  - ju.Set - scala.mutable.Set
  - ju.Map - scala.mutable.Map
   */

}
