package me.javigs82.textprocessor

import java.io.File

import akka.actor.{Actor, ActorLogging, Props}
import me.javigs82.textprocessor.Analyzer.RequestTf
import me.javigs82.textprocessor.TextProcessorManager.ResponseTF
import me.javigs82.textprocessor.util.TfIdfUtil

object Analyzer {
  def props(): Props = Props(new Analyzer)

  final val name = "analyzer"

  final case class RequestTf(f: File, tt: String)

}

class Analyzer extends Actor with ActorLogging {
  override def receive: Receive = {
    case RequestTf(f, tt) =>
      log.info("analyzing {} for tt {}", f.getPath, tt)
      //supposing tt is "unlock password"
      //sender() ! ResponseTF(f, scala.util.Random.nextDouble())//
      sender() ! ResponseTF(f, TfIdfUtil.getTF(f.getPath, tt))
  }
}
