package lectures.part3

object ThreadCommunication extends App {

  /*
  The Producer-Consumer problem
  Forcing the thread to run in a particular order (otherwise the process has no value).
  Producer -> [ x ] -> Consumer
   */

  class SimpleContainer {
    private var value: Int = 0

    def isEmpty: Boolean = value == 0

    // Consuming method
    def get: Int = {
      val result = value
      value = 0
      result
    }

    // Producing method
    def set(newValue: Int): Unit = value = newValue

  }

  /*
  def naiveProducerConsumer(): Unit = {
    val container = new SimpleContainer
    val consumer = new Thread(() => {
      println("[consumer] waiting...")
      while(container.isEmpty) {
        println("[consumer] still waiting...")
      }
      println(s"[consumer] I have consumed ${container.get}")
    })
    val producer = new Thread(() => {
      println("[producer] computing...")
      Thread.sleep(500)
      val value = 42
      println(s"[producer] I have produced value $value")
      container.set(value)
    })
    consumer.start()
    producer.start()
  }
  naiveProducerConsumer()
  */

  /*
  // wait and notify
  def smartProducerConsumer(): Unit = {
    val container = new SimpleContainer
    val consumer = new Thread(() => {
      println("[consumer] waiting...")
      container.synchronized {
        container.wait()
      }
      // container must have some value because it can only be notified by producer.
      println(s"[consumer] I have consumed ${container.get}")
    })
    val producer = new Thread(() => {
      println("[producer] computing...")
      Thread.sleep(2000)
      val value = 42
      container.synchronized {
        println(s"[producer] I'm producing the value $value")
        container.set(value)
        container.notify()
      }
    })
    consumer.start()
    producer.start()
  }
  smartProducerConsumer()
  */

  // Producer-Consumer Level 2

  /*
  producer -> [?, ?, ?] -> consumer
   */

  

}
