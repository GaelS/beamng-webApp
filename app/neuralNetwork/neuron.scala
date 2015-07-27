package neuralNetwork
import scala.collection.mutable.ListBuffer
import scala.collection.immutable.Seq
import scala.util.Random
/**
 * Created by Moi on 02/07/2015.
 */
class Neuron(numInputs : Int) {
  private var weights : Seq[Double] = Seq[Double]()
  //Initialization weigths for one neuron
  def setWeights(weightsFromGen : Seq[Double]) : Unit = {
      weights = weightsFromGen
  }
  def getWeights(): Seq[Double] ={
    return this.weights
  }
}
