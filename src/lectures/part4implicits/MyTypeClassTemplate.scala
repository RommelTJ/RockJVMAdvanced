package lectures.part4implicits

// Type Class Template
trait MyTypeClassTemplate[T] {
  def action(value: T): String
}
object MyTypeClassTemplate {
  // Allows surfacing the instance of the Type Class
  def apply[T](implicit instance: MyTypeClassTemplate[T]): MyTypeClassTemplate[T] = instance
}
