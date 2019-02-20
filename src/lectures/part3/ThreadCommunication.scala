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

}
