package lectures.part3

import scala.collection.parallel.immutable.ParVector

object ParallelUtils extends App {

  // 1 - Parallel Collections
  val parList = List(1, 2, 3).par
  val parVector = ParVector[Int](1, 2, 3)
  /*
  Seq
  Vector
  Array
  Map - Hash, Trie
  Set - Hash, Trie
   */
  // Leads to increased performance (in general)
  
}
