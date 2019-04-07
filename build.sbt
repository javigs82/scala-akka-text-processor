ThisBuild / version := "1.0.0"
ThisBuild / scalaVersion := "2.12.8"
ThisBuild / organization := "me.javigs82"

lazy val akkaVersion = "2.5.16"
lazy val scalaTest =  "3.0.5"
lazy val sparkVersion = "2.4.1"

// allows us to include spark packages
resolvers += "bintray-spark-packages" at
  "https://dl.bintray.com/spark-packages/maven/"

lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .settings(
    name := "scala-akka-text-processor",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % akkaVersion,
      "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
      "org.apache.spark" %% "spark-core" % sparkVersion,
      "org.apache.spark" %% "spark-mllib" % sparkVersion,
      "org.scalatest" %% "scalatest" % scalaTest)
  )




