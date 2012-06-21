package com.xlorm.pongbot

import org.bson.types.ObjectId

import com.xlorm.pongbot.models.{ Player, Game }
import com.xlorm.pongbot.dao.{ PlayerDAO, GameDAO}
 



object GameLogic {
  
  val DEFAULT_START_RATING = System.getProperty("startRating") match {
    case Int(n) => n
    case _ => 1600
  }


  def addPlayer(
    name: String,
    startrating: Int = DEFAULT_START_RATING
  ): Option[String] = {
    PlayerDAO.findByName(name) match {
      case Some(player) => {
        //TODO: logging. or throw?
        println("LOG: player %s exists".format(player.name))
        Some(player.name)
      }
      case None => PlayerDAO.insert(Player(name, startrating))
    }
  }


  def addGame(
    winner: Player,
    loser: Player,
    winnerScore: Int,
    loserScore: Int
  ): Option[ObjectId] = {
    updateRatings(winner, loser)
    GameDAO.insert(Game(winner.name, loser.name, winnerScore, loserScore))
  }


  def updateRatings(winner: Player, loser: Player){
    val (winnerRating, loserRating) = 
      ELORating.updated(winner.rating, loser.rating)


    val newWinner = winner.copy(
      numWins = winner.numWins + 1,
      rating = winnerRating)

    PlayerDAO.save(newWinner)

    val newLoser = loser.copy(
      numLosses = loser.numLosses + 1,
      rating = loserRating)

    PlayerDAO.save(newLoser)
  }

  def makeGameHistString: String =
    GameDAO.iterator.map(_.infoString).mkString("\n")



  def headToHead(p1: Player, p2: Player): (Int, Int) = 
    GameDAO.findManyByPlayers(p1.name, p2.name).foldLeft(0, 0) {
      case ((winCnt1, winCnt2), gm) => gm.winnerName match {
        case p1.name => (winCnt1 + 1, winCnt2)
        case p2.name => (winCnt1, winCnt2 + 1)
      }
    }


  def lastNGamesInfo(n: Int, player: Option[String] = None): String = {
    val games = player match {
      case Some(name) => GameDAO.findManyByPlayer(name).toSeq
      case None => GameDAO.iterator.toSeq //TODO: inefficient
    }

    Game.sortByDate(games).takeRight(n).map(_.infoString).mkString("\n")
  }
}

object Int {
  def unapply(s : String) : Option[Int] = try {
    Some(s.toInt)
  } catch {
    case _ : java.lang.NumberFormatException => None
  }
}
