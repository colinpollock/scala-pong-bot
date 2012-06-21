package com.xlorm.pongbot

import com.xlorm.pongbot.dao.{ GameDAO, PlayerDAO }
import com.xlorm.pongbot.GameLogic

object RecomputeRatings {

  def main(args: Array[String]): Unit = {

    val startRating = GameLogic.DEFAULT_START_RATING
    PlayerDAO.iterator.foreach(player =>
      PlayerDAO.save(player.copy(rating=startRating, numWins=0, numLosses=0)))


    GameDAO.dateSortedGames.foreach{game =>
      val winner = PlayerDAO.findByName(game.winnerName).get
      val loser = PlayerDAO.findByName(game.loserName).get
      GameLogic.updateRatings(winner, loser)
    }
  }
}
