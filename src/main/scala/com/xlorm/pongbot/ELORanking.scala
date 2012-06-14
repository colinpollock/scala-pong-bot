package com.xlorm.pongbot

//TODO: change filename

object ELORating {


  private def kValueFromRating(rating: Double): Int = rating match {
    case r if r > 2400 => 16
    case r if r >= 2100 => 24
    case _ => 32
  }
    

  def titleFromRating(rating: Double): String = rating match {
    case r if r >= 2400 => "Super Grandmaster"
    case r if r >= 2200 => "Grandmaster"
    case r if r >= 1800 => "Master"
    case r if r >= 1600 => "Expert"
    case r if r >= 1400 => "Class D"
    case r if r >= 1200 => "Class C"
    case r if r >= 1000 => "Class B"
    case r if r >= 800 => "Class A"
    case _ => "Novice"
  }


  def updated(winnerRating: Int, loserRating: Int): (Int, Int) = {
    val loserExpectation = expectation(loserRating, winnerRating)
    val k = kValueFromRating(winnerRating)
    val newWinnerRating = 
      math.round(winnerRating + (k * (loserExpectation))).toInt
    val newLoserRating = loserRating - newWinnerRating + winnerRating
    (newWinnerRating, newLoserRating)
  }

   /* 
    * Return the expected probability that a player with rating `a` will beat
    * a player with rating `b`.
   */
  private def expectation(a: Int, b: Int): Double = 
    1. / (1 + math.pow(10, (b.toDouble - a.toDouble) / 400.0))
}
