package com.xlorm.pongbot.models

import java.util.Date

import com.novus.salat.annotations.Key


case class Player (
  @Key("_id") name: String,
  rating: Int,
  numWins: Int = 0,
  numLosses: Int = 0,
  dateAdded: Long = System.currentTimeMillis / 1000
) {
  def numGames: Int = numWins + numLosses

  def infoString: String = "%s %d-%d (%d)".format(
    name, numWins, numLosses, rating)

}
