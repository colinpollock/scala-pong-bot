package net.colinpollock.pongbot.models

import java.util.Date

import com.novus.salat.annotations.Key


case class Player (
  @Key("_id") name: String,
  rating: Int,
  isProvisional: Boolean,
  numWins: Int = 0,
  numLosses: Int = 0,
  dateAdded: Date = new Date
) {
  def numGames: Int = numWins + numLosses

  def ratingString: String = "%d%s".format(rating, isProvisional match {
    case true => " (Provisional)"
    case false => ""
  })

  def infoString: String = "%s %d-%d (%s)".format(
    name, numWins, numLosses, if (isProvisional) "Provisional" else rating)

}
