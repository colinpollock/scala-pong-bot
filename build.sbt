
name := "pongbot"

version := "1.0.0-SNAPSHOT"


libraryDependencies ++= Seq(
  "org.mongodb" % "mongo-java-driver" % "2.6.2" % "compile->default",
  "com.mongodb.casbah" % "casbah_2.9.0-1" % "2.1.5.0",
  "com.fasterxml.jackson.module" % "jackson-module-scala" % "2.0.0",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.0.0",
  "com.fasterxml.jackson.core" % "jackson-annotations" % "2.0.0",
  "com.novus" % "salat-core_2.9.0-1" % "0.0.8-SNAPSHOT",
  "com.novus" % "salat-util_2.9.0-1" % "0.0.8-SNAPSHOT",
  "pircbot" % "pircbot" % "1.5.0"
)

resolvers ++= Seq(
  "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases",
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/releases",
  "repo.novus rels" at "http://repo.novus.com/releases/",
  "repo.novus snaps" at "http://repo.novus.com/snapshots/"
)
