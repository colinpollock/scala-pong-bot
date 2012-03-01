package net.colinpollock.pongbot


object ELORanking {

  def calculate(rankA: Int, rankB: Int): (Int, Int) =
    (calcOneRank(rankA, rankB), calcOneRank(rankB, rankA))

  private def calcOneRank(a: Int, b: Int): Int =
    1 / (1 + math.pow(10, (a - b) / 400.0))
}
