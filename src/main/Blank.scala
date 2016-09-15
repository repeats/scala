package main

import compiler.ScalaCompiler
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import ipc.RepeatClient
import userDefinedAction.UserDefinedAction
import userDefinedAction.Activation

object Blank {
  def main(args : Array[String]) : Unit = {
    val g = new RepeatClient()
    g.start()
    
    Thread.sleep(4000)
    println("AAAAAAAAAAAAAAAAAAA")
    g.requests.mouseRequest.moveBy(10, 0)
  }
}