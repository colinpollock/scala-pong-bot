package com.xlorm.pongbot

import org.jibble.pircbot._

import com.xlorm.pongbot.models.{ Player, Game }
import com.xlorm.pongbot.dao.{ PlayerDAO, GameDAO }

class PongBot(botName: String) extends PircBot {
  this.setName(botName)


  override def onMessage(
    channel: String, 
    sender: String, 
    login: String, 
    hostname: String, 
    message: String
  ){
    if (message.startsWith("!pong ")){
      callDispatch(message.toLowerCase.substring(6), channel)
    } else if (message.startsWith("PongBot: ")) {
      callDispatch(message.toLowerCase.substring(9), channel)
    }
  }

  private def callDispatch(msgMinusBotName: String, channel: String){
    val messageTokens = msgMinusBotName.trim.split(" ")
    PongBot.dispatch(messageTokens) match {
      case Left(response) => sendMessage(channel, response)
      case Right(responses) => responses.zipWithIndex.foreach{
        case (resp, ln) => sendMessage(channel, "[%d] %s".format(ln + 1, resp))
      }
    }
  }
}


object PongBot {

  def dispatch(msg: Seq[String]): Either[String, Seq[String]] = msg match {
    case Seq("commands") => Right(commandsList)
    case Seq(winner, "beat", loser, winScore, "to", loseScore) =>
      Left(addGame(winner, loser, winScore.toInt, loseScore.toInt))
    case Seq("add", "player", playerName) => Left(addPlayer(playerName))
    case Seq("player", "info", playerName) => Left(playerInfo(playerName))
    case Seq("ladder") => Right(ladder)
    case Seq(p1Name, "vs", p2Name) => Left(headToHead(p1Name, p2Name))
    case Seq("recent", "games") => Right(recentGames)
    case _ => Left("Failed to recognize your command")
  }


  def commandsList: Seq[String] = List(
    "commands: show available commands",
    "{WINNER} beat {LOSER} {SCORE} to {SCORE}: enter a new game",
    "add player {PLAYER_NAME}: add a new player",
    "player info {PLAYER_NAME}: show info for player",
    "players: show all current players' names",
    "ladder: show all players ordered by ranks",
    "{PLAYER_NAME} vs {PLAYER_NAME}: show the head to head record",
    "recent games: show the 10 most recent games"
  )
  

  def addGame(
    winnerName: String, 
    loserName: String, 
    winnerScore: Int, 
    loserScore: Int
  ): String =
    (PlayerDAO.findByName(winnerName), PlayerDAO.findByName(loserName)) match {
      case (Some(winner), Some(loser)) => {
        GameLogic.addGame(winner, loser, winnerScore, loserScore) match {
          case Some(gm) => "Successfully added game"
          case None => "Failed to add game"
        }
      }
      case (None, None) => "Players '%s' and '%s' not found".format(
        winnerName, loserName)
      case (None, _) => "Player '%s' not found".format(winnerName)
      case (_, None) => "Player '%s' not found".format(loserName)
    }


  def ladder: Seq[String] = PlayerDAO.iterator.toSeq.sortWith{
    case (p1, p2) => p1.rating > p2.rating
  }.map(_.infoString)

  def recentGames: Seq[String] = 
    Game.sortByDate(GameDAO.iterator.toSeq).take(10).map(_.infoString)
  

  def headToHead(p1Name: String, p2Name: String): String =
    (PlayerDAO.findByName(p1Name), PlayerDAO.findByName(p2Name)) match {
      case (Some(p1), Some(p2)) => {
        val (p1Wins, p2Wins) = GameLogic.headToHead(p1, p2)
        "%s: %d, %s: %d".format(p1Name, p1Wins, p2Name, p2Wins)
      }
      case (None, None) => 
        "Players '%s' and '%s' not found".format(p1Name, p2Name)
      case (None, _) => "Player '%s' not found".format(p1Name)
      case (_, None) => "Player '%s' not found".format(p2Name)
    }


  def playerInfo(name: String): String = PlayerDAO.findByName(name) match {
    case Some(player) => player.infoString
    case None => "Failed to find player '%s'".format(name)
  }
  

  def addPlayer(name: String): String = GameLogic.addPlayer(name) match {
    case Some(nm) => "Player '%s' has been added".format(nm)
    case None => "Failed to add player '%s'. Does that name exist already?"
  }
  

  def main(args: Array[String]){
    require(args.size > 1, "First arg must be channel to join")
    val channels = args(0).split(",")
    val name = args(1)

    val bot = new PongBot(name)
    bot.setVerbose(true)
    bot.connect("irc.freenode.net")
    channels.foreach(bot.joinChannel(_))
  }
}
