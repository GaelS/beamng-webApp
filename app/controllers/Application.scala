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
import neuralNetwork._
import genetics._


class Application extends Controller {

	chromosomeManager.instantiatePopulation(50)
	NeuralNetController.createNet(2,4,Seq(),genetics.chromosomeManager.population(chromosomeManager.currentC))
	var cpt = 0
	var timer =0
	var posInit = Tuple3(0.0,0.0,0.0)
	var posFinale = Tuple3(0.0,0.0,0.0)

	def socket = WebSocket.using[String] { request =>

  		// Log events to the console
  		val in = Iteratee.foreach[String](s => {
				val data = Json.parse(s).as[JsValue].as[models.DataInput]
					//dataBusiness.dataCollection(data)
				if(posInit == (0,0,0)){
					posInit = (data.x,data.y,data.z)
				}
				dataBusiness.getDirection(data) onComplete {
					case Success(res) => {
						val out = NeuralNetController.updateNet(Seq(data.airspeed,res))
						if(data.airspeed < 0.5){
							cpt = cpt + 1
						} else {
							cpt = 0
						}
						if(cpt == 100) {
							cpt = 0
							println("av "+chromosomeManager.currentC)
							chromosomeManager.updateFitnessFactor(Utils.Utils.getDistanceT(posFinale,posInit))
							println("ap " +chromosomeManager.currentC)
							NeuralNetController.createNet(2,4,Seq(),genetics.chromosomeManager.population(chromosomeManager.currentC))
							posFinale = (data.x, data.y, data.z)
							InputManager.restart()
						}
						timer = timer +1
						InputManager.inputsFromNeuralNet(out)
					}
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
		chromosomeManager.instantiatePopulation(10)
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
