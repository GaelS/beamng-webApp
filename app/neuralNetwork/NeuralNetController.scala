package neuralNetwork

/**
 * Created by Moi on 02/07/2015.
 */
import scala.collection.mutable.ListBuffer
import scala.collection.immutable.Seq

object NeuralNetController {

  //Out neural network
  private var net = new NeuralNet(Seq[NeuronLayer](),new NeuronLayer(Seq[Neuron]()))

  //numInput : number of Inputs coming in the net
  //numOutput : number of Inputs coming out of the net
  //numNeurons : number of Neurons on each hidden layer (numNeurons[0] = 4 => 4 neurons in the first hidden layer and so on
  def createNet(numInput : Int, numOutput : Int, numNeurons : Seq[Int], weightsFromGenetics : Seq[Double]) : Unit = {
    var layers = ListBuffer[NeuronLayer]()
    var numberOfInputs = numInput //At first num of inputs = numInputs
    var beginning = 0
    var end =0
    numNeurons.foreach( numberOfNeurons => {
      var neurons = ListBuffer[Neuron]()
      (1 to numberOfNeurons).foreach(num => {
        val neu = new Neuron(numberOfInputs)

        neurons.append(neu)
        //Here Genetics Shit
        end = numberOfInputs - 1
        neu.setWeights(weightsFromGenetics.slice(beginning,end+1))

        beginning = end+1
        //End Genetics Shit
      })
      numberOfInputs = numberOfNeurons
      layers.append(new NeuronLayer(neurons))
    })
    //For Output layer
    var neurons = ListBuffer[Neuron]()
    var outputLayer = new NeuronLayer(Seq[Neuron]())
    (1 to numOutput).foreach(num => {
      val neu = new Neuron(numberOfInputs)
      //Here Genetics Shit
      end = end + numberOfInputs
      neu.setWeights(weightsFromGenetics.slice(beginning,end+1))
      beginning = end
      //End Genetics Shit
      neurons.append(neu)
    })
    outputLayer = new NeuronLayer(neurons)
    net =  new NeuralNet(layers.toList, outputLayer)
  }

  def updateNet(inputs : Seq[Double]): Seq[Double] ={
    net.calculateOutput(inputs)
  }
}
