package lectures.part4implicits

import java.util.Date

object JSONSerialization extends App {

  /*
  Users, Posts, Feeds
  Serialize to JSON
   */

  case class User(name: String, age: Int, email: String)
  case class Post(content: String, createdAt: Date)
  case class Feed(user: User, posts: List[Post])

  /*
  Steps to serialize the above
  1 - Create intermediate data types: Int, String, List, Date
  2 - Create Type Classes for Conversion to Intermediate Data Types
  3 - Serialize to JSON
   */

  // Step 1 - Intermediate Data Type
  sealed trait JSONValue {
    def stringify: String
  }

  final case class JSONString(value: String) extends JSONValue {
    override def stringify: String = "\"" + value + "\""
  }
  final case class JSONNumber(value: Int) extends JSONValue {
    override def stringify: String = value.toString
  }
  final case class JSONArray(values: List[JSONValue]) extends JSONValue {
    override def stringify: String = values.map(_.stringify).mkString("[", ",", "]")
  }
  final case class JSONObject(values: Map[String, JSONValue]) extends JSONValue {
    override def stringify: String = values.map {
      case (key, value) => "\"" + key + "\": " + value.stringify
    }.mkString("{", ",", "}")
  }

  // Testing
  val data = JSONObject(Map(
    "user" -> JSONString("Rommel"),
    "posts" -> JSONArray(
      List(JSONString("Scala Rocks!"), JSONNumber(42))
    )
  ))
  // println(data.stringify)

  // Step 2 - Type Class
  /*
  What we need:
  1 - Type Class
  2 - Type Class Instances (implicit)
  3 - Pimp Library to use Type Class instances
   */

  trait JSONConverter[T] {
    def convert(value: T): JSONValue
  }

  // Step 3 - Call stringify on result

}
