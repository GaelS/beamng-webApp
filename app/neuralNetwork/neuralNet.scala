package neuralNetwork

import scala.collection.mutable.ListBuffer
import scala.collection.immutable.Seq
import scala.math.exp
/**
 * Created by Moi on 02/07/2015.
 */
class NeuralNet(hiddenLayers : Seq[neuralNetwork.NeuronLayer] ) {
  def this() =  this(Seq[NeuronLayer]())

  def calculateOutput(inputs : Seq[Double]): Seq[Double] = {
    //initialization outputs (at beginning == inputs)
    var outputs : ListBuffer[Double] = ListBuffer[Double]()
    (1 until inputs.length).foreach{ in => outputs.append(in)}

    //Go through Layers of the net
    hiddenLayers.foreach {
      elt => {
        var outTemp = ListBuffer[Double]()
        //Go through neurons of one layer
        elt.neurons.foreach( n => {
          val weights = n.getWeights()
          var out : Double = 0
          //Go through weights of one layer to calculate output
          weights.zipWithIndex.foreach{ weight =>
            out = out + weight._1 * inputs(weight._2)
          }
          outTemp.append(sigmoidFunction(out)) //Sigmoid Outputs of each neuron in one layer
        }
        )
        outputs = outTemp
      }
    }
    outputs.toList
  }

  def sigmoidFunction(in : Double) : Double = {
    1/(1+ exp(-in))
  }
}
