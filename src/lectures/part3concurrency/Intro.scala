package lectures.part3concurrency

import java.util.concurrent.Executors

object Intro extends App {

  /*
  interface Runnable {
    public void run()
  }
   */
  // JVM Threads
  val runnable = new Runnable {
    override def run(): Unit = println("Running in parallel")
  }
  val aThread = new Thread(runnable)

  // Start gives the signal to the JVM to start a JVM thread.
//  aThread.start() // Will create a JVM thread, which runs on top of an OS system thread.
  // runnable.run() // This doesn't do anything in parallel.

//  aThread.join() // will block until thread finished running.

//  val threadHello = new Thread(() => (1 to 5).foreach(_ => println("Hello")))
//  val threadGoodbye = new Thread(() => (1 to 5).foreach(_ => println("Goodbye")))
//  threadHello.start()
//  threadGoodbye.start() // Different runs produce different results.

  // Executors
  val aPool = Executors.newFixedThreadPool(10)
//  aPool.execute(() => println("Something in the thread pool")) // will be executed by one of the 10 threads in the pool.
//  aPool.execute(() => {
//    Thread.sleep(1000)
//    println("Done after 1 second")
//  })
//  aPool.execute(() => {
//    Thread.sleep(1000)
//    println("Almost done")
//    Thread.sleep(1000)
//    println("Done after 2 seconds")
//  })
//  aPool.shutdown()
//  // aPool.execute(() => println("Should Not Appear")) // RejectedExecutionException
//
//  // aPool.shutdownNow() // Throws InterruptedException
//  println(aPool.isShutdown) // true

//  def runInParallel = {
//    var x = 0
//
//    val thread1 = new Thread(() => {
//      x = 1
//    })
//
//    val thread2 = new Thread(() => {
//      x = 2
//    })
//
//    thread1.start()
//    thread2.start()
//    println(x)
//  }

  // Race condition!
//  for (_ <- 1 to 100) runInParallel

//  class BankAccount(var amount: Int) {
//    override def toString: String = s"$amount"
//  }

  def buy(account: BankAccount, thing: String, price: Int) = {
    account.amount -= price // account.amount = account.amount - price
    println(s"I've bought $thing. My account is now ${account.amount}")
  }

  // Race condition!
//  for (_ <- 1 to 1000) {
//    val account = new BankAccount(50000)
//    val thread1 = new Thread(() => buy(account, "Shoes", 3000))
//    val thread2 = new Thread(() => buy(account, "iPhone 12", 2000))
//    thread1.start()
//    thread2.start()
//    Thread.sleep(100)
//    println()
//  }

  /*
  thread 1 = 50000
  - account = 50000 - 3000 = 47000
  thread 2 = 50000
  - account = 50000 - 2000 = 48000
   */

  // Two options to solve race conditions in Scala.

  // Option #1: use synchronized()
  def buySafe(account: BankAccount, thing: String, price: Int) =
    account.synchronized {
      account.amount -= price
      println(s"I've bought $thing and my account balance is now ${account.amount}")
    }

  // Option #2: use @volatile - All reads and writes to amount are synchronized.
  class BankAccount(@volatile var amount: Int) {
    override def toString: String = s"$amount"
  }

  /*
  Exercises:
  1 - Construct 50 "inception" threads: Thread1 -> Thread2 -> Thread3 -> ...
      println("hello from thread #3")
      in REVERSE ORDER
  2 - Given the below code, what is the biggest possible value for x?
      What is the smallest possible value for x?
  3 - Given the below code, what is the value of message? Is it guaranteed? Why or why not?
   */

  // Code 2
  var x = 0
  val threads = (1 to 100).map(_ => new Thread(() => x += 1))
  threads.foreach(_.start())

  // Solution 2
  // 1 - 100
  // 2 - 1, for all threads might read x = 0 and write back x = 1 at same time.

  // Code 3
  var message = ""
  val awesomeThread = new Thread(() => {
    Thread.sleep(1000)
    message = "Scala is awesome"
  })

  message = "Scala sucks"
  awesomeThread.start()
  Thread.sleep(2000)
  println(message)

  // Solution 3
  // 1 - Scala is awesome.
  // 2 - No
  // 3 - Because the below could happen:
  /*
  (main thread)
    message = "Scala sucks"
    awesomeThread.start()
    sleep() - relieves execution
  (awesome thread)
    sleep() - relieves execution
  (OS gives the CPU to some important thread - takes CPU for more than 2 seconds)
  (OS gives CPU back to main thread)
    println("Scala sucks")
  (OS gives the CPU to awesomeThread)
    message = "Scala is awesome"
   */

  // Solution 1
  def inceptionThreads(maxThreads: Int, i: Int = 1): Thread = new Thread(() => {
    if (i < maxThreads) {
      val newThread = inceptionThreads(maxThreads, i + 1)
      newThread.start()
      newThread.join()
    }
    println(s"Hello from Thread $i")
  })
  inceptionThreads(50).start()

}
