package net.colinpollock.pongbot.dao

import org.bson.types.ObjectId

import com.mongodb.casbah.Imports._

import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.dao._
import com.mongodb.casbah.MongoConnection

import net.colinpollock.pongbot.model.Player


//TODO: pull out db name
object PlayerDAO extends SalatDAO[Player, String] (
  collection = MongoConnection()("pongbot")("players")
) {
  def iterator: Iterator[Player] = find(MongoDBObject()).toIterator
}
