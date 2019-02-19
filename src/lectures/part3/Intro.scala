package lectures.part3

object Intro extends App {

  /*
  interface Runnable {
    public void run()
  }
   */
  // JVM Threads
  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("Running in parallel")
  })
  aThread.start() // Will create a JVM thread, which runs on top of an OS system thread.

}
