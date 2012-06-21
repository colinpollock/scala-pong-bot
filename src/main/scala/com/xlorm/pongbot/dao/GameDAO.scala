package com.xlorm.pongbot.dao

import org.bson.types.ObjectId

import com.mongodb.casbah.Imports._

import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.dao._
import com.mongodb.casbah.MongoConnection

import com.xlorm.pongbot.models.Game


//TODO: pull out db name
object GameDAO extends SalatDAO[Game, ObjectId] (
  collection = MongoConnection()("pongbot")("games")
) {

  val WINNER_NAME = "winnerName"
  val LOSER_NAME = "loserName"

  def iterator: Iterator[Game] = find(MongoDBObject())

  def dateSortedGames: Seq[Game] = Game.sortByDate(find(MongoDBObject()).toSeq)

  def count: Long = count(MongoDBObject())


  def findManyByPlayer(playerName: String): Set[Game] =
    find(MongoDBObject(WINNER_NAME -> playerName)).toSet |
    find(MongoDBObject(LOSER_NAME -> playerName)).toSet

  def findManyByPlayers(p1Name: String, p2Name: String): Set[Game] =
    find(MongoDBObject(WINNER_NAME -> p1Name, LOSER_NAME -> p2Name)).toSet |
    find(MongoDBObject(WINNER_NAME -> p2Name, LOSER_NAME -> p1Name)).toSet
}
