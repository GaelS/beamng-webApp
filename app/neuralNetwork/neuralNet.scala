package neuralNetwork

import scala.collection.mutable.ListBuffer
import scala.collection.immutable.Seq
import scala.math.exp
/**
 * Created by Moi on 02/07/2015.
 */
class NeuralNet(hiddenLayers : Seq[NeuronLayer], outputLayer : NeuronLayer) {

  def calculateOutput(inputs : Seq[Double]): Seq[Double] = {
    println("!!!!!!! " + inputs)
    //initialization outputs (at beginning == inputs)
    var outputs : ListBuffer[Double] = ListBuffer[Double]()
    var outTemp = ListBuffer[Double]()
    outputs = inputs.to[ListBuffer]

    //Go through Layers of the net
    hiddenLayers.foreach {
      elt => {
        outTemp = ListBuffer[Double]()
        //Go through neurons of one layer
        elt.neurons.foreach( n => {
          val weights = n.getWeights()
          var out : Double = 0
          //Go through weights of one layer to calculate output
          weights.zipWithIndex.foreach{ weight => {
            out = out + weight._1 * inputs(weight._2)
          }
          }
          outTemp.append(sigmoidFunction(out)) //Sigmoid Outputs of each neuron in one layer
        }
        )
        outputs = outTemp
      }
    }
    println(inputs + " || " + outputs)
    outputs.toList
    //GoIn Output Layer
    outputLayer.neurons.foreach{
      outTemp = ListBuffer[Double]()
      neuron => {
        val weights = neuron.getWeights()
        var out : Double = 0
        //Go through weights of one layer to calculate output
        println("weights " + weights )
        weights.zipWithIndex.foreach{ weight => {
        var input = 0.0
          if(weight._2 == outputs.length){
          input = -1
        } else { input = outputs(weight._2)}
          out = out + weight._1 *input
        }
        }
        println("out " +out)
        outTemp.append(sigmoidFunction(out)) //Sigmoid Outputs of each neuron in one layer
      }
    }
    outputs = outTemp
    outputs.toList
  }

  def sigmoidFunction(in : Double) : Double = {
    1/(1+ exp(-in))
  }
}
