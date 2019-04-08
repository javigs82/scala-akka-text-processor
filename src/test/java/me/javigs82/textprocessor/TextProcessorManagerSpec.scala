package me.javigs82.textprocessor

import java.io.File

import akka.actor.ActorSystem
import akka.testkit.{TestKit, TestProbe}
import me.javigs82.textprocessor.TextProcessorManager.{FileCreated, RequestTopNTF, ResponseTF, StartTextProcessor}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.duration._
import scala.language.postfixOps


//#test-classes
class TextProcessorManagerSpec(_system: ActorSystem)
  extends TestKit(_system)
    with Matchers
    with WordSpecLike
    with BeforeAndAfterAll {
  //#test-classes

  def this() = this(ActorSystem("TextProcessorManagerSpec"))

  override def afterAll: Unit = {
    shutdown(system)
  }


  "A Text Processor Actor" should {
    "should start text processor in a directory" in {
      val path = getClass.getResource("/")
      val textProcessorManager = system.actorOf(TextProcessorManager.props(path.getPath, "Frequency"))
      textProcessorManager ! StartTextProcessor
      expectNoMessage(200.millis)
    }

    "should analyze on File Created" in {
      val path = getClass.getResource("/")
      val textProcessorManager = system.actorOf(TextProcessorManager.props(path.getPath, "Frequency"))
      val file = new File(path.getPath + "test.txt")
      textProcessorManager ! FileCreated(file)
      expectNoMessage(200.millis)
    }

    "should response to Request Top N " in {
      val path = getClass.getResource("/")
      val textProcessorManager = system.actorOf(TextProcessorManager.props(path.getPath, "Frequency"))
      val file = new File(path.getPath + "test.txt")
      textProcessorManager ! ResponseTF(file, 4.0)
      expectNoMessage(200.millis)

      val testProbe = TestProbe()

      textProcessorManager.tell(RequestTopNTF(3), testProbe.ref)
      testProbe.expectMsg(200 millis, ArrayBuffer(Tuple2(file, 4.0)))

    }
  }

}

