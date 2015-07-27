package Utils

object Utils{
  def getDistance(vec1 : Vector[Double],vec2 : Tuple3[Double,Double,Double]): Double ={
      Math.sqrt( (vec1(0) - vec2._1) * (vec1(0) - vec2._1) + (vec1(1) - vec2._2) * (vec1(1) - vec2._2)  )
  }
  def getDistanceT(vec1 : Tuple3[Double,Double,Double],vec2 : Tuple3[Double,Double,Double]): Double ={
    Math.sqrt( (vec1._1 - vec2._1) * (vec1._1 - vec2._1) + (vec1._2 - vec2._2) * (vec1._2 - vec2._2)  )
    Math.sqrt( (vec1._1 - vec2._1) * (vec1._1 - vec2._1) + (vec1._2 - vec2._2) * (vec1._2 - vec2._2)  )
  }

  def norme(a : Tuple3[Double,Double,Double]): Double ={
    Math.sqrt(a._1 * a._1 + a._2 * a._2)
  }
  def angle( a : Tuple3[Double,Double,Double], b : Tuple3[Double,Double,Double]): Double = {
    val cos = (a._1 * b._1 + b._2* a._2)/(norme(a) * norme(b))
    val sin = (a._1 * b._2 - a._2 * b._1)/(norme(a) * norme(b))
    var angle = Math.acos(cos)

    if(cos < 0 && sin >= 0){
      angle = angle + Math.PI/2
    } else if(cos <= 0 && sin <=0){
      angle = angle + Math.PI
    } else if(cos >= 0 && sin <= 0){
      angle = - angle
    }
    angle
}
}