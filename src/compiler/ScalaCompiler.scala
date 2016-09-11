package compiler

import userDefinedAction.UserDefinedAction
import userDefinedAction.UserDefinedAction
import reflect.runtime.currentMirror
import scala.tools.reflect.ToolBox
import io.Source

/**
 * Scala native compiler.
 * 
 * @constructor create a compiler object with default configuration. 
 */
class ScalaCompiler {
  
  private var _config = new CompilerConfig()
  private val toolbox = currentMirror.mkToolBox()
  import toolbox.u._
  
  def config = _config
  
  /**
   * Compile source code given the file in which the code is in.
   * 
   * @param sourceFile absolute path to the file containing the source code.
   */
  def compileFromFile(sourceFile : String) : UserDefinedAction = {
    val fileContents = Source.fromFile(sourceFile).getLines.mkString("\n")
    return compile(fileContents)
  }
  
  /**
   * Compile source code.
   * 
   * @param source the source code in string format.
   */
  def compile(source : String) : UserDefinedAction = {
    try {
      val tree = toolbox.parse(source)
      val compiledCode = toolbox.compile(tree)
  
      return compiledCode().asInstanceOf[UserDefinedAction]
    } catch {
      // TODO log this
      case e: Exception => null
    }
  }
}