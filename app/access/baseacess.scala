package access
import reactivemongo.api._
import scala.concurrent.ExecutionContext.Implicits.global

object BaseAccess{
  val driver = new MongoDriver
  val connection = driver.connection(List("localhost"))
  val db = connection("data")
  //Ground Index2d
  val collectionGround = db("ground")


}