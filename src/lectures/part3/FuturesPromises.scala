package lectures.part3

import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
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

  // Functional Composition of Futures
  // Map, flatMap, filter
  val nameOnTheWall = mark.map(profile => profile.name)
  val marksBestFriend = mark.flatMap(profile => SocialNetwork.fetchBestFriend(profile))
  val zucksBestFriendRestricted = marksBestFriend.filter(profile => profile.name.startsWith("Z"))

  // For-Comprehensions
  for {
    mark <- SocialNetwork.fetchProfile("fb.id.1-zuck")
    bill <- SocialNetwork.fetchBestFriend(mark)
  } yield mark.poke(bill)

  Thread.sleep(1000)

  // Fallbacks
  val aProfileNoMatterWhat = SocialNetwork.fetchProfile("unknown-id").recover {
    case e: Throwable => Profile("fb.id.0-dummy", "Forever Alone")
  }

  val aFetchedProfileNoMatterWhat = SocialNetwork.fetchProfile("unknown-id").recoverWith {
    case e: Throwable => SocialNetwork.fetchProfile("fb.id.0-dummy")
  }

  val fallbackResult = SocialNetwork.fetchProfile("unknown-id").fallbackTo(SocialNetwork.fetchProfile("fb.id.0-dummy"))

  // Online Banking App

  case class User(name: String)
  case class Transaction(sender: String, receiver: String, amount: Double, status: String)

  object BankingApp {
    val name = "Rommel Banking"

    def fetchUser(name: String): Future[User] = Future {
      // Simulate fetching from the DB
      Thread.sleep(500)
      User(name)
    }

    def createTransaction(user: User, merchantName: String, amount: Double): Future[Transaction] = Future {
      // Simulate some processes
      Thread.sleep(1000)
      Transaction(user.name, merchantName, amount, "SUCCESS")
    }

    def purchase(username: String, item: String, merchantName: String, cost: Double): String = {
      // Fetch the user from the DB
      // Create a Transaction from the username to the merchantname
      // WAIT for the transaction to finish.
      val transactionStatusFuture = for {
        user <- fetchUser(username)
        transaction <- createTransaction(user, merchantName, cost)
      } yield transaction.status

      Await.result(transactionStatusFuture, 2.seconds)
    }
  }

  println(BankingApp.purchase("Rommel", "iPhone12", "Rommel Banking Store", 1200))

  // Promises

  val promise = Promise[Int]() // "controller" over a future
  val future = promise.future

  // Thread 1 - consumer
  future.onComplete {
    case Success(r) => println(s"[consumer] I've received $r")
  }

  // Thread 2 - producer
  val producer = new Thread(() => {
    println("[producer] Crunching numbers...")
    Thread.sleep(500)
    // "fulfilling" the Promise
    promise.success(42)
    println("[producer] Done")
  })

  producer.start()
  Thread.sleep(1000)

  /*
  Exercises:
  1) Fulfill a Future IMMEDIATELY with a value.
  2) Write a function called inSequence(futureA, futureB) that runs futureB after futureA is completed.
  3) first(futureA, futureB) => new future with the first value of two futures
  4) last(futureA, futureB) => new future with the last value of two futures
  5) retryUntil[T](action: () => Future[T], condition: T => Boolean): Future[T]
   */

  // 1 - Fulfill Immediately
  def fulfillImmediately[T](value: T): Future[T] = Future(value)

  // 2 - inSequence
  def inSequence[A, B](futureA: Future[A], futureB: Future[B]): Future[B] = {
    // Once the first future is completed and I have the value, run the second future.
    futureA.flatMap(a => futureB)
  }

}
