package net.colinpollock.pongbot.models

import java.util.Date


case class Player (
  @Key("_id") name: String,
  rank: Int,
  isProvisional: Boolean,
  numWins: Int = 0,
  numLosses: Int = 0,
  dateAdded: Date = new Date
) {
  def numGames: Int = numWins + numLosses

  def rankString: String = rank.toString + isProvisional match {
    case true => " (Provisional)"
    case false => ""
  }

  def infoString: String = "%s %d-%d (%s)".format(
    name, numWins, numLosses, if (isProvisional) "Provisional" else rank)

}
