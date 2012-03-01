package net.colinpollock.pongbot.dao

import org.bson.types.ObjectId

import com.mongodb.casbah.Imports._

import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.dao._
import com.mongodb.casbah.MongoConnection

import net.colinpollock.pongbot.model.Game


//TODO: pull out db name
object GameDAO extends SalatDAO[Game, ObjectId] (
  collection = MongoConnection()("pongbot")("games")
) 
