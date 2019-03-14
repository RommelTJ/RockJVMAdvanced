package lectures.part5ts

object StructuralTypes extends App {

  // structural types
  type JavaCloseable = java.io.Closeable

  class HipsterCloseable {
    def close(): Unit = println("yeah yeah I'm closing")
    def closeSilently(): Unit = println("Not making a sound")
  }

  // def closeQuietly(closeable: JavaCloseable OR HipsterCloseable) // !?

  // A Structural Type
  type UnifiedCloseable = {
    def close(): Unit
  }

  def closeQuietly(unifiedCloseable: UnifiedCloseable): Unit = unifiedCloseable.close()
  closeQuietly(new JavaCloseable {
    override def close(): Unit = println("Closing Java")
  })
  closeQuietly(new HipsterCloseable)


  // TYPE REFINEMENTS

  // Example of refining the JavaCloseable type
  type AdvancedCloseable = JavaCloseable {
    def closeSilently(): Unit
  }

  class AdvancedJavaCloseable extends JavaCloseable {
    override def close(): Unit = println("Java closes")
    def closeSilently(): Unit = println("Java closes silently")
  }

  def closeShush(advCloseable: AdvancedCloseable): Unit = advCloseable.closeSilently()
  closeShush(new AdvancedJavaCloseable)
  // closeShush(new HipsterCloseable) // Wrong! Because HipsterCloseable doesn't originate from JavaCloseable

  // Using Structural Types as Standalone Types
  def altClose(closeable: { def close(): Unit }): Unit = closeable.close()

  // Type-Checking => Duck Typing

  type SoundMaker = {
    def makeSound(): Unit
  }
  class Dog {
    def makeSound(): Unit = println("Bark!")
  }
  class Car {
    def makeSound(): Unit = println("Vroom!")
  }

  // Examples of static duck-typing
  // Looks like a dog, swims like a dog, flies like a duck, must be a duck!
  val dog: SoundMaker = new Dog
  val car: SoundMaker = new Car

  // CAVEAT: Duck typing in Scala is based on reflection
  // Reflective calls have a big impact on performance.

}
