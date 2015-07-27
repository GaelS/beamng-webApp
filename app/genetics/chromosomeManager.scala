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
  var currentC: Int = 0
  var currentGeneration: Int = 1
  private val mutationRate = 0.1 //1/28 //[0.05:2] for real number

  //Creation of n chromosome
  def instantiatePopulation(numChromosome: Int): Unit = {
    val r : Random = new Random()
    val temp = (1 to numChromosome).map {
      num => Seq.fill(28)((Math.abs(r.nextInt +0.1) / r.nextInt) * Random.nextFloat.toDouble)
    }
    population = temp
    println(population)
  }

  def updateFitnessFactor(value: Double): Unit = {
    fitness.append(value)
    if (currentC < population.length - 1) {
      currentC = currentC + 1
      println("nouveau chrom")
      println(population.length)
    } else {
      //Time for a new genetics batch
      currentC = 0
      println("///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////")
      println("///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////")
      println("/////////////////////////i//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////")
      println("///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////")
      println("///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////")
      println("///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////")
      println("///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////")
      println("///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////")
      println("///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////")
      println("fin "+currentGeneration+" generation")
      createANewPopulation
      currentGeneration = currentGeneration+1
      fitness.clear
    }
  }

  def createANewPopulation(): Unit = {
    val sumFitnessFactor = fitness.foldLeft(0.0)(_ + _)
    val fitnessProbRogue = fitness.toSeq.map(factor => {
      factor / sumFitnessFactor
    }).zipWithIndex.sorted

    val max = fitnessProbRogue(fitnessProbRogue.length - 1)._1
    val fitnessProb = fitnessProbRogue.map({ e => (e._1 / max, e._2) }) //Get Prob from 0 to 1
    println(fitnessProb)
    val e = Seq.fill(population.length)(Random.nextDouble())
    val newPop = e.map({
      elt => {
        val index = fitnessProb.filter(_._1 > elt)(0)._2
        population(index)
      }
    })
    val finalNewPopulation = crossOver(newPop)
    val finalPop = mutation(finalNewPopulation)
    println(newPop.length + " " + finalNewPopulation.length + " " + finalPop.length)
    population = finalPop
  }

  def crossOver(pop: Seq[Seq[Double]]): Seq[Seq[Double]] = {
    var population: Seq[Seq[Double]] = Nil
    (0 until pop.length - 1 by 2).foreach({
      index => {
        val indexCross = Random.nextInt(pop(0).length)
        val lengthC = pop(0).length
        val oneElt = pop(index)
        val secondElt = pop(index + 1)
        val newSecond: Seq[Double] = secondElt.take(indexCross) ++ oneElt.drop(indexCross)
        val newFirst: Seq[Double] = oneElt.take(indexCross) ++ secondElt.drop(indexCross)
        population = population.+:(newFirst)
        population = population.+:(newSecond)
      }
    })
    println("crossed ")
    population.foreach(println)
    population
  }

  def mutation(pop: Seq[Seq[Double]]): Seq[Seq[Double]] = {
    //Iterating through one chromosome and check whether it must mutate
    val finalPop = pop.map(oneC => {
      oneC.map(oneWeight => {
        val probOfMutating = Random.nextDouble()
        if (probOfMutating <= mutationRate) {
          Math.abs(Random.nextInt) / Random.nextInt * Random.nextDouble
        } else {
          oneWeight
        }
      })
    })
    finalPop
  }
}
