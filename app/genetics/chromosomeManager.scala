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
      num => temp.append(Seq.fill(28)(Math.abs(Random.nextInt)/Random.nextInt * Random.nextDouble()))}
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
      createANewPopulation()
      Thread.sleep(500000)
    }
  }

  def createANewPopulation(): Unit ={
    val sumFitnessFactor = fitness.foldLeft(0.0)(_ + _)
    println(sumFitnessFactor)
    val fitnessProb = fitness.toSeq.map( factor => {
      factor/sumFitnessFactor
    }).sortWith(_ < _)
    println(fitnessProb)
    val newPopulation = Seq.fill(population.length)(Random.nextDouble()).foreach({
          //TODO Create NEW POPULATIOn
      elt => println(population(fitnessProb.zipWithIndex.filter(elt > _._1).filter(elt < _._1)(0)._2))
    })
    println("blop")
    println(newPopulation)
  }
}
