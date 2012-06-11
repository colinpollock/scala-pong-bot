package net.colinpollock.pongbot.models

import java.util.Date

import org.bson.types.ObjectId

import com.novus.salat.annotations.Key

import net.colinpollock.pongbot.dao.GameDAO

case class Game (
  winnerName: String,
  loserName: String,
  winnerScore: Int,
  loserScore: Int,
  date: Date = new Date,
  @Key("_id") id: ObjectId = new ObjectId
) {
  def infoString: String = """%s beat %s %d to %d on %s""".format(
    winnerName, loserName, winnerScore, loserScore, date.toString
  )
}

object Game {
  def sortByDate(games: Seq[Game]): Seq[Game] = 
    games.sortWith((g1, g2) => g1.date.before(g2.date))
}
