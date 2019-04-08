package me.javigs82.textprocessor

import akka.actor.ActorSystem
import akka.testkit.TestKit
import me.javigs82.textprocessor.PrinterManager.PrintTf
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import scala.concurrent.duration._
import scala.language.postfixOps


//#test-classes
class PrinterManagerSpec(_system: ActorSystem)
  extends TestKit(_system)
    with Matchers
    with WordSpecLike
    with BeforeAndAfterAll {
  //#test-classes

  def this() = this(ActorSystem("PrinterManagerSpec"))

  override def afterAll: Unit = {
    shutdown(system)
  }

  "A Printer Actor" should {
    "should print tf in stdout" in {
      val path = getClass.getResource("/")

      val textProcessorManager = system.actorOf(TextProcessorManager.props(path.getPath, "Frequency"))
      val printer = system.actorOf(PrinterManager.props(textProcessorManager))

      printer ! PrintTf(5)

      expectNoMessage(200.millis)

    }
  }

}

