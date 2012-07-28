pong-bot
======

This is an IRC bot for tracking a ping pong ladder. It's built using Scala, 
MongoDB, and [Salat](https://github.com/novus/salat/).


The bot uses a simple version of the 
[Elo rating system](http://en.wikipedia.org/wiki/Elo_rating_system). New players
begin with ratings of 1600. When a new game is entered into the system both
players' ratings are updated: the winner gains N points and the loser loses N
points. N depends on the difference between the two players' ratings. A player
rated 1200 will gain more points by beating someone rated 1800 than by someone
rated 1500.


Running
-------
Enter sbt and then run the main program of the class com.xlorm.pongbot.PongBot.
The first command line argument should be a comma-separated string of channels
to join. The second argument is the bot's name.

```
$ sbt
> run-main com.xlorm.pongbot.PongBot #wordnik-pong,#pingpong PongBot
```


Functionality
-------------
* PongBot: add player xlorm  
  Adds xlorm if that name isn't taken already. xlorm will start off with a
  0-0 record and the default rating.
* PongBot: ladder  
  Displays all of the players' names, records, and ratings ordered by their 
  ratings. 
* PongBot: kumanan beat xlorm 21 to 19  
  Add a new game to the system. The winner's score must be 21 and the loser's 
  score must be greater than or equal to 0 and less than 20. If the game goes
  to deuce then it should be entered as "22 to 20".
* xlorm vs kumanan  
  Display the head-to-head record between these two players.


TODO
----
* maven build
* command like "player xlorm" that lists all of xlorm's games
* command like "xlorm record" that lists head-to-head records against each 
  player
* have bot recognize whatever name is set during initialization
* put provisional ratings back in
* suggest "commands" when user input isn't recognized
* store the irc user who enters a game in the Game object
* use regexes to make user input more flexible
* check the scores coming in: must be (22 to 20) or (21 to 0 >= n >= 19)
* migrate deuce games to 22-20
* tests for ELO
* switch to Jerkson
* make persisted config object to hold:
  * number of games as provisional
  * initial rating
  * K value
* back-end server
* Non-IRC interface

Stats
-----
* number of games by player
* number of points by player
* number of games by day


Bugs
----
* All usernames are downcased.
