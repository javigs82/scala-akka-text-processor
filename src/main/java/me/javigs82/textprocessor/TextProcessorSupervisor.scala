package me.javigs82.textprocessor

import akka.actor.{Actor, ActorLogging, Props}
import me.javigs82.textprocessor.PrinterManager.StartPrintTfPeriodically
import me.javigs82.textprocessor.TextProcessorManager.StartTextProcessor
import me.javigs82.textprocessor.TextProcessorSupervisor.InitApp


object TextProcessorSupervisor {

  def props(): Props = Props(new TextProcessorSupervisor)

  final val name = "text-processor-supervisor"

  //d is the the directory
  //n is the top to be returned
  //tt is the term to be analyzed
  //p is the period to retrieve the top N
  final case class InitApp(d: String, n: Int, tt: String, p: Long)

}

class TextProcessorSupervisor extends Actor with ActorLogging {

  override def preStart(): Unit = log.info("Text Processor Supervisor started")

  override def postStop(): Unit = log.info("Text Processor Supervisor stopped")

  override def receive: Receive = {
    case InitApp(d, n, tt, p) =>
      log.info("Starting text processor in directory {} for term {}, with a period of {}, and returning " +
        "the top N {}", d, tt, p, n)
      //Init TextProcessor manager
      val textProcessorManager = context.actorOf(TextProcessorManager.props(d, tt), TextProcessorManager.name)
      textProcessorManager ! StartTextProcessor()

      //Init printer manager as scheduler with text processor actor
      val printerManger = context.actorOf(PrinterManager.props(textProcessorManager), PrinterManager.name)
      printerManger ! StartPrintTfPeriodically(p, n)

  }


}
