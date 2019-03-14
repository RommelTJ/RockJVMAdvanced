package lectures.part5ts

object StructuralTypes extends App {

  // structural types
  type JavaCloseable = java.io.Closeable

  class HipsterCloseable {
    def close(): Unit = println("yeah yeah I'm closing")
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

}
