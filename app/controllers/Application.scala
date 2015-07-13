package controllers

import play.api._
import play.api.mvc._

import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.libs.iteratee.Concurrent
import play.api.libs.iteratee.Iteratee
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.iteratee.Enumerator
import play.api.libs.json._
import carControl.InputManager
import models.DataInput
import  business.dataBusiness
import models.DataInputFormat._
import scala.collection.immutable._
import Utils.Utils._
import scala.util.{Try, Success, Failure}
import com.github.nscala_time.time.Imports._

class Application extends Controller {
	var prevZ = 0.0
	var prevTime = DateTime.now
	var velocity = 0.0
	def socket = WebSocket.using[String] { request =>

  		// Log events to the console

  		val in = Iteratee.foreach[String](s => {
				val data = Json.parse(s).as[JsValue].as[models.DataInput]
				//println(data.wheelFL + " " + data.wheelFR + " " + data.wheelRL + " " + data.wheelRR)
				val currentTime = DateTime.now
				velocity = Math.abs((data.z - prevZ) /((currentTime.getMillis - prevTime.getMillis)/100.0))
				println("velocity " + data.airspeed)
				prevTime = currentTime
				prevZ = data.z

				if (data.airspeed < 1) {
					println("One in DB" + data.wheelFL + " "+data.wheelFR + " " + data.wheelRR + " " + data.wheelRL)
					dataBusiness.dataCollection(data)
					InputManager.discoveryMode(data, true)
				} else {
					InputManager.discoveryMode(data, false)
				}

			})
  		// Send a single 'Hello!' message
  		val out = Enumerator("Ok!")

  (in, out)
}

	def getPoints = Action.async { request =>
		val futureRes = dataBusiness.getEverything()
		futureRes.map(list => Ok(Json.toJson((list))))
	}
	def map = Action { request =>
		Ok(views.html.index(""))
	}
	def javascriptRoutes = Action{
		implicit request =>
			import routes.javascript._
			Ok(
				Routes.javascriptRouter("jsRoutes")(
					routes.javascript.Application.getPoints
				)
			).as("text/javascript")
	}
}
