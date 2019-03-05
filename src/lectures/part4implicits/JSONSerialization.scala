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

}
