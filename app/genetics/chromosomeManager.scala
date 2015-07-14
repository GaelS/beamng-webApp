package genetics

import scala.collection.mutable.ListBuffer
import scala.collection.immutable.Seq
import scala.util.Random
import neuralNetwork.NeuralNetController._
/**
 * Created by Moi on 14/07/2015.
 */
object chromosomeManager {
  var population = Seq[Seq[Double]]()
  private var fitness = scala.collection.mutable.ListBuffer[Double]()
  var currentC : Int = 0

  //Creation of n chromosome
  def instantiatePopulation(numChromosome : Int) : Unit =  {
    var temp = ListBuffer[scala.collection.immutable.Seq[Double]]()
    (1 to numChromosome).foreach {
      num => temp.append(Seq.fill(12)(Random.nextDouble()))
    }
    population = temp.to[scala.collection.immutable.Seq]
  }

  def updateFitnessFactor(value : Double): Unit ={
    fitness.append(value)
    if(currentC < population.length - 1) {
      currentC = currentC + 1
      println("nouveau chrom")
    } else {
      //Time for a new genetics batch
      println("fin premiere generation")
    }
  }
  def createANewPopulation(): Unit ={

  }
}
