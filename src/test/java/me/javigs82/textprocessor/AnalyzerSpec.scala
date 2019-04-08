package me.javigs82.textprocessor

import java.io.File

import akka.actor.ActorSystem
import akka.testkit.{TestKit, TestProbe}
import me.javigs82.textprocessor.Analyzer.RequestTf
import me.javigs82.textprocessor.TextProcessorManager.ResponseTF
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.duration._
import scala.language.postfixOps


//#test-classes
class AnalyzerSpec(_system: ActorSystem)
  extends TestKit(_system)
    with Matchers
    with WordSpecLike
    with BeforeAndAfterAll {
  //#test-classes

  def this() = this(ActorSystem("AnalyzerSpec"))

  override def afterAll: Unit = {
    shutdown(system)
  }

  "A Analyzer Actor" should {
    "pass on a ResponseTF message when instructed to Request TF" in {

      val testProbe = TestProbe()
      val tt = "Frequency"
      val path = getClass.getResource("/test.txt")
      val file = new File(path.getPath)

      val analyzer = system.actorOf(Analyzer.props())

      analyzer.tell(RequestTf(file, tt), testProbe.ref)

      //has to startup spark
      testProbe.expectMsg(8000 millis, ResponseTF(file, 2.0))
    }
  }

}

