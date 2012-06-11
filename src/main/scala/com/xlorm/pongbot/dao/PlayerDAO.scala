package com.xlorm.pongbot.dao

import org.bson.types.ObjectId

import com.mongodb.casbah.Imports._

import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.dao._
import com.mongodb.casbah.MongoConnection

import com.xlorm.pongbot.models.Player


//TODO: pull out db name
object PlayerDAO extends SalatDAO[Player, String] (
  collection = MongoConnection()("pongbot")("players")
) {

  val NAME = "_id"

  def count: Long = count(MongoDBObject())

  def findByName(name: String): Option[Player] = 
    findOne(MongoDBObject(NAME -> name))

  def iterator: Iterator[Player] = find(MongoDBObject()).toIterator
}
