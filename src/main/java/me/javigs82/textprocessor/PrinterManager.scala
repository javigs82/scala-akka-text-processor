package me.javigs82.textprocessor

import java.io.File

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Timers}
import akka.util.Timeout
import me.javigs82.textprocessor.PrinterManager.{PrintTf, StartPrintTfPeriodically}
import me.javigs82.textprocessor.TextProcessorManager.RequestTopNTF

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}


object PrinterManager {
  def props(textProcessorManager: ActorRef): Props = Props(new PrinterManager(textProcessorManager))

  final val name = "printer"

  //milliseconds
  final case class StartPrintTfPeriodically(p: Long, topN: Int)

  final case class PrintTf(topN: Int)

}

class PrinterManager(textProcessorManager: ActorRef) extends Actor with ActorLogging with Timers {

  import akka.pattern.ask

  // implicit ExecutionContext should be in scope
  implicit val ec: ExecutionContext = context.dispatcher
  implicit val timeout = Timeout(3 seconds)

  def receive = {
    case StartPrintTfPeriodically(p, topN) =>
      log.info("starting printing for {} periods", p)
      timers.startPeriodicTimer(self, PrintTf(topN), p.millis)

    case PrintTf(topN) =>
      val resultTopN: Future[Seq[(File, Double)]] = ask(textProcessorManager, RequestTopNTF(topN)).mapTo[Seq[(File, Double)]]
      resultTopN map (item => item.foreach(p => println(p._1.getName + " " + p._2)))
      println("********************")

  }

}
