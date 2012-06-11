
name := "ladderbot"

organization := "net.colinpollock"

version := "0.1.10"

scalaVersion := "2.9.2-SNAPSHOT"

libraryDependencies ++= Seq(
  "com.mongodb.casbah" % "casbah_2.9.0-1" % "2.1.5.0"
)

resolvers += "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
