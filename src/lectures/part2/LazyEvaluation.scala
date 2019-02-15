package lectures.part2

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

}
