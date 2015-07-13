package business

import Utils.Utils._
import carControl.InputManager
import models.DataInput
import scala.concurrent.Future
import play.api.libs.json._
import reactivemongo.bson._
import models._
import models.DataInputFormat.DataStreamWriter
import models.DataInputFormat.toDataInputUpdate
import access.BaseAccess._
import models.DataInputFormat._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Try, Success, Failure}
import play.api.libs.iteratee.Iteratee
import scala.collection.mutable.ListBuffer
/**
 * Created by Moi on 30/06/2015.
 */



object dataBusiness {

  //Collect data every 2 meters
  def dataCollection(data : DataInput): Unit ={
      insertOneStream(data,false)
  }

  //Insert one stream in MongoDB
  def insertOneStream(data : DataInput, obstacle: Boolean): Unit = {
    val dataUpToDate : DataInputUpdate = DataInputFormat.toDataInputUpdate(data,obstacle)
    val ee = BSON.write(dataUpToDate)
    val future = collectionGround.insert(ee)

    future onComplete {
      case Failure(e) => throw e
      case Success(writeResult) => {
        //println(s"successfully inserted document: $writeResult")
      }
    }
  }

  def findNear(x: Double, y: Double): Future[List[geoDataFromMongo]] = {
    val query = BSONDocument("coordinates" -> BSONDocument("$geoWithin" -> BSONDocument("$center" -> BSONArray(BSONArray(x, y), BSONDouble(10050.0)))))
    collectionGround.
      find(query).
      cursor[geoDataFromMongo].
      collect[List]()
  }

  def findInFrontOfTheCar(pos:  Tuple3[Double,Double,Double], dir:  Tuple3[Double,Double,Double], length : Int): Tuple3[Future[List[geoDataFromMongo]],Future[List[geoDataFromMongo]],Future[List[geoDataFromMongo]]] = {


    val normalVector :  Tuple2[Double,Double] = (-dir._2, dir._1)
    var normDir = Math.sqrt((dir._1 * dir._1 + dir._2 * dir._2))
    //FRONT
    val leftPoint : Tuple2[Double,Double]= (pos._1 + 3 * normalVector._1,pos._2 + 3*normalVector._2)
    val rightPoint = (pos._1 -  3 * normalVector._1,pos._2 - 3 *normalVector._2)
    val leftFrontFarPoint : Tuple2[Double,Double]= (leftPoint._1 + length*dir._1,leftPoint._2 + length*dir._2)
    val rightFrontFarPoint : Tuple2[Double,Double]= (rightPoint._1  + length*dir._1,rightPoint._2 + length*dir._2)
    //LZFT FRONT
    val dirRotatedLeft = (dir._1 * Math.cos(3.14/5) + dir._2 * Math.sin(3.14/5),dir._2 * Math.cos(3.14/5) - dir._1 * Math.sin(3.14/5))
    val leftLeftFarPoint : Tuple2[Double,Double]= (leftPoint._1 + length*dirRotatedLeft._1,leftPoint._2 + length*dirRotatedLeft._2)
    val rightLeftFarPoint : Tuple2[Double,Double]= (rightPoint._1  + length*dirRotatedLeft._1,rightPoint._2 + length*dirRotatedLeft._2)
    //RIGHT FRONT
    val dirRotatedRight = (dir._1 * Math.cos(-3.14/5) + dir._2 * Math.sin(-3.14/5),dir._2 * Math.cos(-3.14/5) - dir._1 * Math.sin(-3.14/5))
    val leftRightFarPoint : Tuple2[Double,Double]= (leftPoint._1 + length*dirRotatedRight._1,leftPoint._2 + length*dirRotatedRight._2)
    val rightRightFarPoint : Tuple2[Double,Double]= (rightPoint._1  + length*dirRotatedRight._1,rightPoint._2 + length*dirRotatedRight._2)

    //BACK
    //val leftBackFarPoint : Tuple2[Double,Double]= (leftPoint._1 - length*dir._1,leftPoint._2 - length*dir._2)
    //val rightBackFarPoint : Tuple2[Double,Double]= (rightPoint._1  - length*dir._1,rightPoint._2 - length*dir._2)

    val queryFront = BSONDocument("coordinates" -> BSONDocument("$geoWithin" -> BSONDocument("$polygon" -> BSONArray(BSONArray(leftPoint._1, leftPoint._2),BSONArray(rightPoint._1, rightPoint._2),BSONArray(rightFrontFarPoint._1, rightFrontFarPoint._2),BSONArray(leftFrontFarPoint._1, leftFrontFarPoint._2)))))
    val queryLeftFront = BSONDocument("coordinates" -> BSONDocument("$geoWithin" -> BSONDocument("$polygon" -> BSONArray(BSONArray(leftPoint._1, leftPoint._2),BSONArray(rightPoint._1, rightPoint._2),BSONArray(rightLeftFarPoint._1, rightLeftFarPoint._2),BSONArray(leftLeftFarPoint._1, leftLeftFarPoint._2)))))
    val queryRightFront = BSONDocument("coordinates" -> BSONDocument("$geoWithin" -> BSONDocument("$polygon" -> BSONArray(BSONArray(leftPoint._1, leftPoint._2),BSONArray(rightPoint._1, rightPoint._2),BSONArray(rightRightFarPoint._1, rightRightFarPoint._2),BSONArray(leftRightFarPoint._1, leftRightFarPoint._2)))))

    val front = collectionGround.
      find(queryFront).
      cursor[geoDataFromMongo].
      collect[List]()
    val frontLeft =  collectionGround.
       find(queryLeftFront).
       cursor[geoDataFromMongo].
       collect[List]()
    val frontRight = collectionGround.
       find(queryRightFront).
       cursor[geoDataFromMongo].
       collect[List]()
    (front,frontLeft,frontRight)
  }

  def getEverything(): Future[List[geoDataFromMongo]] = {
    collectionGround.
      find(BSONDocument()).
      cursor[geoDataFromMongo].
      collect[List]()
  }
}
