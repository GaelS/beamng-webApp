package neuralNetwork

/**
 * Created by Moi on 02/07/2015.
 */
import scala.collection.mutable.ListBuffer
import scala.collection.immutable.Seq

object NeuralNetController {

  //Out neural network
  private var net = new NeuralNet()

  //numInput : number of Inputs coming in the net
  //numOutput : number of Inputs coming out of the net
  //numNeurons : number of Neurons on each hidden layer (numNeurons[0] = 4 => 4 neurons in the first hidden layer and so on
  def createNet(numInput : Int, numOutput : Int, numNeurons : Seq[Int]) : Unit = {
    var layers = ListBuffer[NeuronLayer]()
    var numberOfInputs = numInput //At first num of inputs = numInputs

    numNeurons.foreach( numberOfNeurons => {
      var neurons = ListBuffer[Neuron]()
      (1 to numberOfNeurons).foreach(num => {
        val neu = new Neuron(numberOfInputs)
        //TODO neu.setWeights()
        neurons.append(neu)
      })
      layers.append(new NeuronLayer(neurons))
    })
    net =  new NeuralNet(layers.toList)
  }

  def updateNet(inputs : Seq[Double]): Seq[Double] ={
    net.calculateOutput(inputs)
  }
}
