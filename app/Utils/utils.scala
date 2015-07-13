package Utils

object Utils{
  def getDistance(vec1 : Vector[Double],vec2 : Tuple3[Double,Double,Double]): Double ={
      Math.sqrt( (vec1(0) - vec2._1) * (vec1(0) - vec2._1) + (vec1(1) - vec2._2) * (vec1(1) - vec2._2)  )
  }

}