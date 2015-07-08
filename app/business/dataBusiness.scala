package business

import models.DataInput
import scala.concurrent.Future
import play.api.libs.json._
import reactivemongo.bson._
import models._
import models.DataInputFormat._
import access.BaseAccess._
import models.DataInputFormat.DataStreamWriter
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Try, Success, Failure}
import play.api.libs.iteratee.Iteratee
import scala.collection.mutable.ListBuffer
/**
 * Created by Moi on 30/06/2015.
 */



object dataBusiness {

  def insertOneStream(data : DataInput): Unit = {
    val ee = BSON.write(data)
    val future = collectionGround.insert(ee)

    future onComplete {
      case Failure(e) => throw e
      case Success(writeResult) => {
        println(s"successfully inserted document: $writeResult")
      }
    }

  }

  def findNear(x: Double, y: Double):Unit = {
    val pointsToConsider : ListBuffer[geoDataFromMongo] = ListBuffer[geoDataFromMongo]()
    val query = BSONDocument("coordinates" -> BSONDocument("$geoWithin" -> BSONDocument("$center" -> BSONArray(BSONArray(x, y), BSONDouble(10050.0)))))
    val futureRes = collectionGround.
      find(query).
      cursor[geoDataFromMongo].
      collect[List]()

    val out = futureRes.onComplete{
      case Success(res) => { println(res) }
      case _ => List[geoDataFromMongo]()
    }
  }

  def environmentManager(data : DataInput): Unit ={
   val pos : Tuple3[Double,Double,Double] = (data.x,data.y,data.z)
   val direction :  Tuple3[Double,Double,Double] = (data.dx,data.dy,data.dz)

  }
}
