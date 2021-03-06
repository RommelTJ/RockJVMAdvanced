package lectures.part3concurrency

import scala.collection.mutable
import scala.util.Random

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

  /*
  def producerConsumerLargeBuffer() = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 3

    val consumer = new Thread(() => {
      val random = new Random()

      while(true) {
        buffer.synchronized {
          if (buffer.isEmpty) {
            println("[consumer] buffer empty. waiting.")
            buffer.wait()
          }

          // there must be at least one value in the buffer, either because buffer is not empty or
          // i'm woken up by the producer.
          val x = buffer.dequeue()
          println(s"[consumer] I consumed x => $x")

          // Notify that there's empty space in case producer is sleeping.
          buffer.notify()
        }

        Thread.sleep(random.nextInt(500)) // random time between 0 and 500 ms.
      }
    })

    val producer = new Thread(() => {
      val random = new Random()
      var i = 0

      while(true) {
        buffer.synchronized {
          if (buffer.size == capacity) {
            // CASE: Full buffer.
            println("[producer] buffer full. waiting.")
            buffer.wait()
          }

          // there must be at least one empty spot in the buffer, either because the buffer is not full
          // or i'm woken up by the consumer.
          println(s"[producer] producing i => $i")
          buffer.enqueue(i)

          // Notify that there's empty space in case consumer is sleeping.
          buffer.notify()

          i += 1
        }

        Thread.sleep(random.nextInt(500)) // random time between 0 and 500 ms.
      }
    })
    consumer.start()
    producer.start()
  }
  producerConsumerLargeBuffer()
  */

  // Producer-Consumer Level 3

  /*
  producer1 -> [?, ?, ?] -> consumer1
  producer2 ----^     ^---- consumer2
  producer3 ----^     ^---- consumer3
  etc.
   */

  class Consumer(id: Int, buffer: mutable.Queue[Int]) extends Thread {
    override def run(): Unit = {
      val random = new Random()

      while(true) {
        buffer.synchronized {
          /*
          Producer produced value, two Consumers are waiting
          notifies ONE Consumer, notifies ONE on buffer
          notifies the other Consumer
           */
          while (buffer.isEmpty) {
            println(s"[consumer $id] buffer empty. waiting.")
            buffer.wait()
          }

          // there must be at least one value in the buffer, either because buffer is not empty or
          // i'm woken up by the producer.
          val x = buffer.dequeue()
          println(s"[consumer $id] consumed x => $x")

          // Notify that there's empty space in case producer is sleeping.
          buffer.notify()
        }

        Thread.sleep(random.nextInt(500)) // random time between 0 and 500 ms.
      }
    }
  }

  class Producer(id: Int, buffer: mutable.Queue[Int], capacity: Int = 3) extends Thread {
    override def run(): Unit = {
      val random = new Random()
      var i = 0

      while(true) {
        buffer.synchronized {
          while (buffer.size == capacity) {
            // CASE: Full buffer.
            println(s"[producer $id] buffer full. waiting.")
            buffer.wait()
          }

          // there must be at least one empty spot in the buffer, either because the buffer is not full
          // or i'm woken up by the consumer.
          println(s"[producer $id] producing i => $i")
          buffer.enqueue(i)

          // Notify that there's empty space in case consumer is sleeping.
          buffer.notify()

          i += 1
        }

        Thread.sleep(random.nextInt(500)) // random time between 0 and 500 ms.
      }
    }
  }

  def multiProducerConsumers(nConsumers: Int, nProducers: Int): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 20

    (1 to nConsumers).foreach(i => new Consumer(i, buffer).start())
    (1 to nProducers).foreach(i => new Producer(i, buffer, capacity).start())
  }
  // multiProducerConsumers(3, 3)

  /*
  Exercises:
  1) Think of an example where notifyAll acts in a different way than notify?
  2) Create a deadlock
  3) Create a livelock (cannot continue but threads are all active)
   */

  // Solution 1
  def testNotifyAll() = {
    val bell = new Object

    (1 to 10).foreach(i => new Thread(() => {
      bell.synchronized {
        println(s"[thread $i] waiting...")
        bell.wait()
        println(s"[thread $i] hooray!")
      }
    }).start())

    new Thread(() => {
      Thread.sleep(2000)
      println(s"[announcer] rock'n'roll!")
      bell.synchronized {
        bell.notifyAll() // if you use notify, only one thread will go on.
      }
    }).start()
  }
  // testNotifyAll()

  // Solution 2
  case class Friend(name: String) {

    var sideOfRoad = "right"

    def bow(other: Friend) = {
      this.synchronized {
        println(s"$this: I am bowing to my friend $other")
        other.rise(this)
        println(s"$this: My friend $other has risen")
      }
    }

    def rise(other: Friend) = {
      this.synchronized {
        println(s"$this: I am rising to my friend $other")
      }
    }

    def switchSide() = {
      if (sideOfRoad == "right") sideOfRoad = "left"
      else sideOfRoad = "right"
    }

    def pass(other: Friend) = {
      while(this.sideOfRoad == other.sideOfRoad) {
        println(s"$this: Oh, but please, $other, feel free to pass...")
        switchSide()
        Thread.sleep(1000)
      }
    }
  }
  val Sam = Friend("Sam")
  val Pierre = Friend("Pierre")
  // new Thread(() => Sam.bow(Pierre)).start()
  // new Thread(() => Pierre.bow(Sam)).start() // Boom - deadlocked!

  // Solution 3
  new Thread(() => Sam.pass(Pierre)).start()
  new Thread(() => Pierre.pass(Sam)).start() // Boom - livelocked!

}
