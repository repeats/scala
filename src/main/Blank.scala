package main

import compiler.ScalaCompiler
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import ipc.RepeatClient

object Blank {
  def main(args : Array[String]) : Unit = {
//    val x = new ScalaCompiler()
//    val y = x.compileFromFile("/home/vda/repeat_scala/scala/RepeatTemplate.scala")
//    y.execute(null, null)

//    var strin = "{\"name\" : \"test\", \"a\" : [1,2,3]}"
//    val json: JsValue = Json.parse(strin)
//    val first = (json \ "a")(0)
//    println(first.get.asOpt[Int])

    val g = new RepeatClient()
    g.start()
  }
}