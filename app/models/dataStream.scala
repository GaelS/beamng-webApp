package models
import play.api.libs.json.{Json,Format}
import play.api.libs.json._
import reactivemongo.bson._
case class DataInput(x : Double,y:Double, z: Double, heading : Double, roll : Double, pitch : Double, wheelFR : Int, wheelFL : Int, wheelRR : Int, wheelRL : Int, currentSteer : Double, dx : Double, dy : Double, dz : Double, airspeed : Double)
case class DataInputUpdate(x : Double,y:Double, z: Double, heading : Double, roll : Double, pitch : Double, wheelFR : Int, wheelFL : Int, wheelRR : Int, wheelRL : Int, currentSteer : Double, dx : Double, dy : Double, dz : Double, airspeed : Double, obstacle : Boolean)
case class geoData(coordinates : Array[Double], z: Double , wheelFR : Int, wheelFL : Int, wheelRR : Int, wheelRL : Int, obstacle : Boolean)
case class geoDataFromMongo(x : Double, y : Double, z: Double , wheelFR : Int, wheelFL : Int, wheelRR : Int, wheelRL : Int, obstacle : Boolean)
object DataInputFormat {

  implicit val format: Format[models.DataInput] = Json.format[models.DataInput]
  implicit val formatGeo: Format[models.geoDataFromMongo] = Json.format[models.geoDataFromMongo]

  implicit object DataStreamWriter extends BSONDocumentWriter[DataInputUpdate] {
    def write(data: DataInputUpdate): BSONDocument = BSONDocument(
      "coordinates" -> BSONArray(BSONDouble(data.x), BSONDouble(data.y)),
      "alt" -> BSONDouble(data.z),
      "FL" -> data.wheelFL,
      "FR" -> data.wheelFR,
      "RL" -> data.wheelRL,
      "RR" -> data.wheelRR,
      "obstacle" -> data.obstacle
    )
  }

  implicit object geoDataReader extends BSONDocumentReader[geoDataFromMongo] {
    def read(doc: BSONDocument): geoDataFromMongo = {
      geoDataFromMongo(
        doc.getAs[Array[Double]]("coordinates").get(0),
        doc.getAs[Array[Double]]("coordinates").get(1),
        doc.getAs[Double]("alt").get,
        doc.getAs[Int]("FL").get,
        doc.getAs[Int]("FR").get,
        doc.getAs[Int]("RL").get,
        doc.getAs[Int]("RR").get,
        doc.getAs[Boolean]("obstacle").get
      )
    }
  }

  def toDataInputUpdate(data : DataInput, obstacle : Boolean): DataInputUpdate ={
    new DataInputUpdate(data.x,data.y,data.z,data.heading,data.roll,data.pitch,data.wheelFL,data.wheelFR,data.wheelRL,data.wheelRR,data.currentSteer,data.dx,data.dy,data.dz,data.airspeed,obstacle)
  }

}