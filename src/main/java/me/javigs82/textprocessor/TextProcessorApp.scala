package me.javigs82.textprocessor

import java.io.File

import akka.actor.ActorSystem

import scala.io.StdIn

object TextProcessorApp {

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("text-processor-system")

    val d = "/home/javi/testMonitoring/"
    val n = 5
    val tt = "createDataFrame MinHash"
    val p = 3000

    //validations
    val dir = new File(d)
    if (!dir.exists || !dir.isDirectory)
      throw new IllegalArgumentException(d + " is not a directory")

    try {
      // Create top level supervisor
      val supervisor = system.actorOf(TextProcessorSupervisor.props(), TextProcessorSupervisor.name)

      supervisor ! TextProcessorSupervisor.InitApp(d, n, tt, p)

      println(">>> Press ENTER to exit <<<")
      StdIn.readLine()

    } finally {
      system.terminate()
    }
  }

}
