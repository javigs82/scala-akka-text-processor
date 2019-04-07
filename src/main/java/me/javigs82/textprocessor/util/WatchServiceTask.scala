package me.javigs82.textprocessor.util

import java.nio.file.StandardWatchEventKinds._
import java.nio.file._

import akka.actor.ActorRef
import me.javigs82.textprocessor.TextProcessorManager.FileCreated

import scala.collection.JavaConversions._

class WatchServiceTask(notifyActor: ActorRef) extends Runnable {

  private val watchService = FileSystems.getDefault.newWatchService()


  def watch(path: Path) = path.register(watchService, ENTRY_CREATE)

  def run() {
    try {
      while (!Thread.currentThread().isInterrupted) {
        val key = watchService.take()
        key.pollEvents() foreach {
          event =>
            val relativePath = event.context().asInstanceOf[Path]
            val path = key.watchable().asInstanceOf[Path].resolve(relativePath)
            event.kind() match {
              case ENTRY_CREATE =>
                notifyActor ! FileCreated(path.toFile)
              case ENTRY_DELETE =>
              //Do nothing intentionally
              case x =>
                print(s"Unknown event $x")
            }
        }
        key.reset()
      }
    } catch {
      case e: InterruptedException =>
        print("Stopping WatchServiceTask")
    } finally {
      watchService.close()
    }
  }
}
