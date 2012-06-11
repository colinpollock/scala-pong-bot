package net.colinpollock.pongbot

import org.bson.types.ObjectId

import net.colinpollock.pongbot.models.{ Player, Game }
import net.colinpollock.pongbot.dao.{ PlayerDAO, GameDAO}
 

object GameLogic {
  //TODO: pull this out into config
  val GAMES_AS_PROVISIONAL = 0
  val DEFAULT_START_RATING = 1600


  /*
   * startrating defaults to some basic rating (1k?), provisional at first
   */
  def addPlayer(
    name: String,
    isProvisional: Boolean = true,
    startrating: Int = DEFAULT_START_RATING
  ): Option[String] = {
    PlayerDAO.findByName(name) match {
      case Some(player) => {
        //TODO: logging. or throw?
        println("LOG: player %s exists".format(player.name))
        Some(player.name)
      }
      case None => PlayerDAO.insert(Player(name, startrating, isProvisional))
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


  private def updateRatings(winner: Player, loser: Player){
    val (winnerRating, loserRating) = 
      ELORating.updated(winner.rating, loser.rating)

    var newWinner = winner.copy(numWins = winner.numWins + 1)
    if (!loser.isProvisional) { newWinner = winner.copy(rating = winnerRating) }
    if (newWinner.isProvisional && newWinner.numGames >= GAMES_AS_PROVISIONAL) {
      newWinner = newWinner.copy(isProvisional = false)
    }
    PlayerDAO.save(newWinner)

    var newLoser = loser.copy(numLosses = loser.numLosses + 1)
    if (!winner.isProvisional) { newLoser = loser.copy(rating = loserRating) }
    if (newLoser.isProvisional && newLoser.numGames >= GAMES_AS_PROVISIONAL) {
      newLoser = newLoser.copy(isProvisional = false)
    }
    PlayerDAO.save(newLoser)
  }


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


  /*
  def main(args: Array[String]): Unit = {
    addPlayer("xlorm", false)
    addPlayer("robovoyo", false)
    (1 to 10).foreach{i =>
      addGame("xlorm", "robovoyo", 21, 18)
      println(ratings)
    }
    println("***********")
    (1 to 3).foreach{i =>
      addGame("robovoyo", "xlorm", 21, 13)
      println(ratings)
    }

    val xlorm = PlayerDAO.findOneByID("xlorm").get
    val robovoyo = PlayerDAO.findOneByID("robovoyo").get

    println("There are %d players in the DB".format(PlayerDAO.count))
    println("There are %d games in the DB".format(GameDAO.count))
    println(lastNGamesInfo(5))
    println("xlorm's rating: %d".format(xlorm.rating))
    println("robovoyo's rating: %d".format(robovoyo.rating))
  }
  */
}
