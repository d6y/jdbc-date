name := "slick-jdbc-localdate"

version := "1.0.0"

scalaVersion := "2.12.9"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-unchecked",
  "-feature",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-Ywarn-dead-code",
  "-Xlint",
  "-Xfatal-warnings"
)

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick"           % "3.3.2",
  "org.postgresql"      % "postgresql"      % "42.2.6",
  "ch.qos.logback"      % "logback-classic" % "1.2.3"
)

