package main

import compiler.ScalaCompiler
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import ipc.RepeatClient
import userDefinedAction.UserDefinedAction
import userDefinedAction.Activation
import sun.misc.Signal
import sun.misc.SignalHandler
import org.slf4j.LoggerFactory
import com.typesafe.scalalogging.Logger

class RepeatScala {
  private final val logger = Logger(LoggerFactory.getLogger(this.getClass))
  
  def main(args : Array[String]) : Unit = {
    logger.info("Starting Repeat Scala subsystem.")
    val client = new RepeatClient()
    client.start()
    logger.info("Repeat Scala client started.")
    
    Signal.handle(new Signal("INT"), new SignalHandler() {
      def handle(sig: Signal) {
        println("Caught interrupt signal. Terminating Scala client...")
        client.stop()
      }
    })
    logger.info("Registered signal handler.")
  }
}