package com.xlorm.pongbot.models

import java.util.Date

import org.bson.types.ObjectId
import com.novus.salat.annotations.Key
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import com.xlorm.pongbot.dao.GameDAO

case class Game (
  winnerName: String,
  loserName: String,
  winnerScore: Int,
  loserScore: Int,
  date: Long = System.currentTimeMillis,
  @Key("_id") id: ObjectId = new ObjectId
) {
  import Game.jsonMapper


  //TODO: long to date string for this below
  def infoString: String = """%s beat %s %d to %d on %s""".format(
    winnerName, loserName, winnerScore, loserScore, date.toString
  )


  def toJson: String = jsonMapper.writeValueAsString(this)
}

object Game {

  val jsonMapper = new ObjectMapper
  jsonMapper.registerModule(DefaultScalaModule)


  def fromJson(json: String): Option[Game] = {
    val gm = jsonMapper.readValue(json, classOf[Game])

    if (gm.winnerName == null || gm.loserName == null || 
        gm.winnerScore == null || gm.loserScore == null)  {
      None
    } else Some(
      if (gm.date == null && gm.id == null) {
        gm.copy(date = System.currentTimeMillis, id = new ObjectId)
      } else if (gm.date == null) {
        gm.copy(date = System.currentTimeMillis)
      } else if (gm.id == null) {
        gm.copy(id = new ObjectId)
      } else {
        gm
      }
    )
  }


  def apply(gameJson: String): Option[Game] = fromJson(gameJson)


  def sortByDate(games: Seq[Game]): Seq[Game] = 
    games.sortWith((g1, g2) => g1.date < g2.date)
}
