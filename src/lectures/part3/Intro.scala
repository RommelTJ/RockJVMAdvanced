package lectures.part3

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
  aThread.start() // Will create a JVM thread, which runs on top of an OS system thread.
  // runnable.run() // This doesn't do anything in parallel.

  aThread.join() // will block until thread finished running.

  val threadHello = new Thread(() => (1 to 5).foreach(_ => println("Hello")))
  val threadGoodbye = new Thread(() => (1 to 5).foreach(_ => println("Goodbye")))
  threadHello.start()
  threadGoodbye.start()

}
