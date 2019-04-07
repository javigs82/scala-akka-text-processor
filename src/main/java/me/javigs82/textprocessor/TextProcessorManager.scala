package me.javigs82.textprocessor

import java.io.File
import java.nio.file.Paths

import akka.actor.{Actor, ActorLogging, Props}
import me.javigs82.textprocessor.Analyzer.RequestTf
import me.javigs82.textprocessor.TextProcessorManager.{FileCreated, RequestTopNTF, ResponseTF, StartTextProcessor}
import me.javigs82.textprocessor.util.WatchServiceTask

object TextProcessorManager {
  def props(directory: String, tt: String): Props = Props(new TextProcessorManager(directory, tt))

  final val name = "text-processor-manager"

  final case class StartTextProcessor()

  final case class FileCreated(file: File)

  final case class ResponseTF(file: File, tf: Double)

  final case class RequestTopNTF(topN: Integer)

}

class TextProcessorManager(directory: String, tt: String) extends Actor with ActorLogging {

  val watchServiceTask = new WatchServiceTask(self)
  val watchThread = new Thread(watchServiceTask, "WatchServiceTask")

  val analyzer = context.actorOf(Analyzer.props(), Analyzer.name)

  //map to store tf
  var fileCounter = 0
  var tfMap = scala.collection.mutable.Map.empty[File, Double]


  override def preStart() {
    log.info("TextProcessorManager actor started")
    watchThread.setDaemon(true)
    watchThread.start()
  }

  override def postStop() {
    watchThread.interrupt()
    log.info("TextProcessorManager actor stopped")
  }

  override def receive: Receive = {
    case StartTextProcessor() =>
      log.info("Starting text processor in directory {} for term {}", directory, tt)
      //init scan directory
      val d = new File(directory)
      if (d.exists && d.isDirectory) {
        d.listFiles.filter(_.isFile).foreach(f => analyzer ! RequestTf(f, tt))
        //watch for new files
        watchServiceTask.watch(Paths.get(directory))
      } else {
        log.error("{} is not a directory")
        throw new IllegalArgumentException()
      }


    case FileCreated(file) =>
      log.info("file created {}", file.getAbsolutePath)
      analyzer ! RequestTf(file, tt)

    case ResponseTF(file, tf) =>
      tfMap += (file -> tf)
      fileCounter += 1
      log.info("new tf file {} processed with tf {}", file.getName, tf)

    case RequestTopNTF(topN) => {
      log.info("request top {} tf for documents", topN)
      sender() ! tfMap.toSeq.sortWith(_._2 > _._2).take(topN)

    }
  }

}
