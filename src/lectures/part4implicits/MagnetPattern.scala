package lectures.part4implicits

import scala.concurrent.Future

// Aims solving problems with Method overloading
object MagnetPattern extends App {

  class P2PRequest
  class P2PResponse
  class Serializer[T]

  trait Actor {
    def receive(statusCode: Int): Int
    def receive(request: P2PRequest)
    def receive(response: P2PResponse)
    def receive[T: Serializer](message: T): Int
    def receive[T: Serializer](message: T, statusCode: Int): Int
    def receive(future: Future[P2PRequest]): Int
    //... and lots of other overloads
  }

}
