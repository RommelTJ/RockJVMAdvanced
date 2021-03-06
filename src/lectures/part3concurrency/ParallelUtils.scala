package lectures.part3concurrency

import java.util.concurrent.ForkJoinPool
import java.util.concurrent.atomic.AtomicReference

import scala.collection.parallel.{ForkJoinTaskSupport, Task, TaskSupport}
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

  def measure[T](operation: => T): Long = {
    val time = System.currentTimeMillis()
    operation
    System.currentTimeMillis() - time
  }
  val list = (1 to 10000000).toList
  val serialTime = measure { list.map(_ + 1)}
  val parallelTime = measure { list.par.map(_ + 1) }
  println(s"serialTime: $serialTime") // 3619ms
  println(s"parallelTime: $parallelTime") // 2047ms

  /*
  Parallel Collections operate on a Map-reduce model
  - split the elements into chunks with Splitter
  - operation on every chunk is done on separate threads
  - recombine with Combiner

  Works with map, flatMap, filter, foreach, reduce, fold.
  Be careful with reduce and fold. You may have issues with non-associative operators
   */
  println(List(1, 2, 3).reduce(_ - _)) // -4 = 1 - 2 - 3
  println(List(1, 2, 3).par.reduce(_ - _)) // 2 = 3 - (2 - 1)

  // Synchronization

  var sum = 0
  List(1, 2, 3).par.foreach(sum += _)
  // race conditions
  println(sum) // prints 6 but it's not guaranteed because sum might be accessed by a different thread.

  // Configuring

  parVector.tasksupport = new ForkJoinTaskSupport(new ForkJoinPool(2))
//  parVector.tasksupport = new TaskSupport {
//    override val environment: AnyRef = ???
//    override def execute[R, Tp](fjtask: Task[R, Tp]): () => R = ???
//    override def executeAndWaitResult[R, Tp](task: Task[R, Tp]): R = ???
//    override def parallelismLevel: Int = ???
//  }

  // 2 - Atomic Operations and References

  val atomic = new AtomicReference[Int](2)
  val currentValue = atomic.get() // thread-safe read
  atomic.set(4) // thread-safe write
  atomic.getAndSet(5) // thread-safe read and write
  atomic.compareAndSet(38, 56) // if value is 38, set to 56 (thread-safe) reference equality
  atomic.updateAndGet(_ + 1) // thread-safe function run
  atomic.getAndUpdate(_ + 1) // reverse of the above
  atomic.accumulateAndGet(12, _ + _) // thread-safe accumulation in one-go
  atomic.getAndAccumulate(12, _ + _) // reverse of the above

}
