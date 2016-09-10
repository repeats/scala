package main

import compiler.ScalaCompiler

object Blank {
  def main(args : Array[String]) : Unit = {
    val x = new ScalaCompiler();
    val y = x.compile("");
    y.execute(null, null);
  }
}