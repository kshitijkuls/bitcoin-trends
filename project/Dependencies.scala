import sbt._

object Dependencies {

  object Versions {

    val ficusVersion = "1.4.3"
    val circeVersion = "0.9.1"
    val finagleVersion = "0.16.1"
    val jodaTime = "2.10"
    val spark = "2.3.1"
    val sparkts = "0.4.1"
    val scalaLogging = "3.9.0"
    val json4s = "3.6.1"
  }

  import Versions._

  val coreDependencies: Seq[ModuleID] = Seq(
    "com.iheart" %% "ficus" % ficusVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "joda-time" % "joda-time" % jodaTime,
    "org.json4s" %% "json4s-native" % json4s,
    "org.apache.spark" %% "spark-core" % spark,
    "org.apache.spark" %% "spark-sql" % spark,
    "org.apache.spark" %% "spark-mllib" % spark,
    "com.cloudera.sparkts" % "sparkts" % sparkts,
    "com.typesafe.scala-logging" %% "scala-logging" % scalaLogging,
    "org.scalatest" %% "scalatest" % "3.0.4" % Test
  )

  val testUtilsDependencies : Seq[ModuleID] = {
    Seq(
      "org.scalatest" %% "scalatest" % "3.0.4",
      "org.mockito" % "mockito-all" % "1.10.19"
    )
  }

  val apiDependencies : Seq[ModuleID] = coreDependencies ++ {
    Seq(
      "com.github.finagle" %% "finch-core" % finagleVersion,
      "com.github.finagle" %% "finch-circe" % finagleVersion,
      "net.liftweb" %% "lift-json" % "3.2.0",
      "org.scalatest" %% "scalatest" % "3.0.4" % Test,
      "org.mockito" % "mockito-all" % "1.10.19" % Test
    )
  }
}