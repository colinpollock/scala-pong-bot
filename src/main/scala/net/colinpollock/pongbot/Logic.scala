package net.colinpollock.pongbot

 

object Logic {
  val GAMES_AS_PROVISIONAL = 3  //TODO: pull out
  val STARTER_RANK = 1000

  /*
   * startRank defaults to some basic rank (1k?), provisional at first
   */
  def addPlayer(
    name: String,
    startRank: Option[Int],
    isProvisional: Boolean
  ): Boolean = {
    PlayerDAO.findByName(name) match {
      case Some(player) => false
      case None => {
        val rank = startRank match {
          case Some(r) => r
          case None => STARTER_RANK
        }
        val player = Player(name, rank, isProvisional)
        PlayerDAO.insert(player)
        true
      }
    }
  }


  def addGame(
    winnerName: String,
    loserName: String,
    winnerScore: Int,
    loserScore: Int
  ): Boolean = {
    // Add to GameDAO
    // Update each player's rank
    val game = Game(winnerName, loserName, winnerScore, loserScore)
    GameDAO.insert(game)

    val winner = PlayerDAO.findByName(winnerName) match {
      case None => return false  //Raise exception?
      case Some(player) => player
    }

    val loser = PlayerDAO.findByName(loserName) match {
      case None => return false  //Raise exception?
      case Some(player) => player
    }

    val newRanks = ELORanking.calculate(winner.rank, loser.rank)

    if (!winner.isProvisional) {
      val newLoser = loser.clone(rank = newRanks._1
                                 numGames = loser.numGames + 1)
      PlayerDAO.save(newLoser)
    }

    if (!loser.isProvisional) {
      val newWinner = winner.clone(rank = newRanks._2, 
                                   numGames = winner.numGames + 1)
      PlayerDAO.save(newWinner)
    }

    true
  }


  def lastNGamesInfo(n: Int, player: Option=String = None): String = {
    val games = player match {
      case Some(name) => GameDAO.iterator //TODO: inefficient
      case None => GameDAO.findManyByPlayer(playerName)
    }.toSeq

    Game.sortByDate(games).takeRight(n).map(_.infoString).mkString("\n")
  }

  def playerInfo(playerName: String): String = {
    PlayerDAO.findByName(playerName) match {
      case None => "Player '%s' not found.".format(playerName)
      case Some(player) => {
        val games = Game.sortByDate(GameDAO.findManyByPlayer(playerName))
      }
    }
  }
}
