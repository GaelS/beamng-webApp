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
import carControl._
import models.DataInput
import  business.dataBusiness
import models.DataInputFormat.format


class Application extends Controller {

	def socket = WebSocket.using[String] { request =>

  		// Log events to the console

  		val in = Iteratee.foreach[String](s => {
				val data = Json.parse(s).as[JsValue].as[models.DataInput]
				InputManager.discoveryMode(data.wheelFL,data.wheelFR, data.wheelRL, data.wheelRR,data.airspeed)
				dataBusiness.insertOneStream(data)
				dataBusiness.environmentManager(data)

			})
  		// Send a single 'Hello!' message
  		val out = Enumerator("Ok!")

  (in, out)
}
	def menu = Action { request =>
		//InputManager.test
		Ok("blop")
	}

}
