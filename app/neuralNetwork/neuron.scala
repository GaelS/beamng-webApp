package neuralNetwork
import scala.collection.mutable._
import scala.util.Random
/**
 * Created by Moi on 02/07/2015.
 */
class Neuron(numInputs : Int) {
  private var weights : Seq[Int] = Seq[Int]()
  //Initialization weigths for one neuron
  def initWeights() : Unit = {
    val r = new Random()
    var temp = ListBuffer[Int]()
    (1 to numInputs+1).foreach( e =>
      temp.append(r.nextInt(1))
    )
    temp.toSeq
  }
  def getWeights(): Seq[Int] ={
    return this.weights
  }
}
