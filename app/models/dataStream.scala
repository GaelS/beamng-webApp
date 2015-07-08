package models
import play.api.libs.json.{Json,Format}
import play.api.libs.json._
import reactivemongo.bson._
case class DataInput(x : Double,y:Double, z: Double, heading : Double, roll : Double, pitch : Double, wheelFR : Int, wheelFL : Int, wheelRR : Int, wheelRL : Int, currentSteer : Double, dx : Double, dy : Double, dz : Double, airspeed : Double)
case class geoData(x : Double, y:Double, z: Double , wheelFR : Int, wheelFL : Int, wheelRR : Int, wheelRL : Int)

object DataInputFormat {
  implicit val format : Format[models.DataInput] = Json.format[models.DataInput]
  implicit object DataStreamWriter extends BSONDocumentWriter[DataInput] {
    def write(data: DataInput): BSONDocument = BSONDocument(
      "coordinates" -> BSONArray(BSONDouble(data.x), BSONDouble(data.y)),
      "properties" -> BSONDocument("alt" -> BSONDouble(data.z), "FL" -> data.wheelFL, "FR" -> data.wheelFR, "RL" -> data.wheelRL, "RR" ->data.wheelRR))
  }

  implicit object geoDataReader extends BSONDocumentReader[geoData] {
    def read(doc: BSONDocument): geoData = geoData(
      doc.getAs[Double]("x").get,
      doc.getAs[Double]("y").get,
      doc.getAs[Double]("alt").get,
      doc.getAs[Int]("FL").get,
      doc.getAs[Int]("FR").get,
      doc.getAs[Int]("RL").get,
      doc.getAs[Int]("RR").get)
  }
}