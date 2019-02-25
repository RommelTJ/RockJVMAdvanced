package lectures.part3

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Random, Success}

object FuturesPromises extends App {

  def calculateMeaningOfLife: Int = {
    Thread.sleep(2000)
    42
  }
  val aFuture = Future {
    calculateMeaningOfLife // calculates the meaning of life on another thread
  } // compiler adds (global) implicit here.

  // println(aFuture.value) // Option[Try[Int]] --> None
//  println("Waiting on the future")
//  aFuture.onComplete(t => t match {
//    case Success(meaningOfLife) => println(s"the meaning of life is $meaningOfLife")
//    case Failure(exception) => println(s"I failed with $exception")
//  })
//  aFuture.onComplete {
//    case Success(meaningOfLife) => println(s"the meaning of life is $meaningOfLife")
//    case Failure(exception) => println(s"I failed with $exception")
//  } // Not necessarily called by the same thread that created this
//  Thread.sleep(3000)

  // Mini Social Network

  case class Profile(id: String, name: String) {
    def poke(anotherProfile: Profile): Unit = println(s"${this.name} poking ${anotherProfile.name}")
  }

  object SocialNetwork {
    // "database"

    val names = Map(
      "fb.id.1-zuck" -> "Mark",
      "fb.id.2-bill" -> "Bill",
      "fb.id.0-dummy" -> "Dummy"
    )

    val friends = Map("fb.id.1-zuck" -> "fb.id.2-bill")

    val random = new Random()

    // API

    def fetchProfile(id: String): Future[Profile] = Future {
      // Simulates fetching from the DB
      Thread.sleep(random.nextInt(300))
      Profile(id, names(id))
    }

    def fetchBestFriend(profile: Profile): Future[Profile] = Future {
      Thread.sleep(random.nextInt(400))
      val bestFriendId = friends(profile.id)
      Profile(bestFriendId, names(bestFriendId))
    }
  }

  // Client: Mark to Poke Bill

  val mark = SocialNetwork.fetchProfile("fb.id.1-zuck")
  mark.onComplete {
    case Success(markProfile) =>
      val bill = SocialNetwork.fetchBestFriend(markProfile)
      bill.onComplete {
        case Success(billProfile) => markProfile.poke(billProfile)
        case Failure(e) => e.printStackTrace()
      }
    case Failure(e) => e.printStackTrace()
  }

  Thread.sleep(1000)
  
}
