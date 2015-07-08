package business

import models.DataInput
import scala.concurrent.Future
import play.api.libs.json._
import reactivemongo.bson._
import models.geoData
import access.BaseAccess._
import models.DataInputFormat.DataStreamWriter
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Try, Success, Failure}
/**
 * Created by Moi on 30/06/2015.
 */



object dataBusiness {

  def insertOneStream(data : DataInput): Unit = {
    val ee = BSON.write(data)
    val future = collectionGround.insert(ee)

    future.onComplete {
      case Failure(e) => throw e
      case Success(writeResult) => {
        println(s"successfully inserted document: $writeResult")
      }
    }

  }

  def getNearestPoints(pos :  Tuple3[Double,Double,Double]): Unit ={

  }

  def environmentManager(data : DataInput): Unit ={
   val pos : Tuple3[Double,Double,Double] = (data.x,data.y,data.z)
   val direction :  Tuple3[Double,Double,Double] = (data.dx,data.dy,data.dz)

  }
}
