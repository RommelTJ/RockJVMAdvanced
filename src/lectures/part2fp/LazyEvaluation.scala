package lectures.part2fp

object LazyEvaluation extends App {

  // Lazy Values are evaluated once, but only when they are used.
  // Lazy delays the evaluation of values.
  // val x: Int = throw new RuntimeException // Crash!
  // lazy val x: Int = throw new RuntimeException // No Crash!
  // println(x) // Now we crash!

  lazy val x: Int = {
    println("hello the first time")
    42
  }
  println(x)
  println(x) // X is not evaluated again.

  // Examples of implications:
  // Side-Effects
  def sideEffectCondition: Boolean = {
    println("Boolean")
    true
  }
  def simpleCondition: Boolean = false

  lazy val lazyCondition = sideEffectCondition
  println(if (simpleCondition && lazyCondition) "yes" else "no") // no, but doesn't print "Boolean" because the
  // the lazyCondition is not evaluated unless it's needed and the run time is smart enough to not evaluate it because
  // the first condition will short-circuit!
  println(if (lazyCondition && simpleCondition) "yes" else "no") // no, but prints "Boolean"

  // In Conjunction with Call-by-name
  // def byNameMethod(n: => Int): Int = n + n + n + 1
  def byNameMethod(n: => Int): Int = {
    // This is a technique called "CALL BY NEED".
    lazy val t = n // only evaluated once
    t + t + t + 1
  }
  def retrieveMagicValue = {
    // Side-effect or long computation
    println("waiting")
    Thread.sleep(1000)
    42
  }
  // println(byNameMethod(retrieveMagicValue)) // This evaluates retrieveMagicValue 3 times! (using old implementation)
  // Using Lazy Evaluation:
  println(byNameMethod(retrieveMagicValue)) // This evaluates retrieveMagicValue 1 time! (using new implementation)

  // Filtering with Lazy Evaluation
  def lessThan30(i: Int): Boolean = {
    println(s"$i is less than 30?")
    i < 30
  }
  def greaterThan20(i: Int): Boolean = {
    println(s"$i is greater than 20?")
    i > 20
  }

  val numbers = List(1, 25, 30, 40, 5, 23)
  val lt30 = numbers.filter(lessThan30) // List(1, 25, 5, 23)
  println(lt30)

  val gt20 = lt30.filter(greaterThan20) // List(25, 23)
  println(gt20)

  val lt30lazy = numbers.withFilter(lessThan30) // withFilter uses lazy values under the hood.
  val gt20lazy = lt30lazy.withFilter(greaterThan20)
  println("--")
  println(gt20lazy) // scala.collection.TraversableLike$WithFilter@3498ed
  gt20lazy.foreach(println) // Side-effects run on a by-need basis

  // For-Comprehensions use withFilter with guards
  for {
    a <- List(1, 2, 3) if a % 2 == 0 // use lazy vals!
  } yield a + 1
  List(1, 2, 3).withFilter(_ % 2 == 0).map(_ + 1) // List[Int]

}
