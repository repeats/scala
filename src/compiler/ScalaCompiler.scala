package compiler

import userDefinedAction.UserDefinedAction
import userDefinedAction.UserDefinedAction
import reflect.runtime.currentMirror
import scala.tools.reflect.ToolBox
import io.Source
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

/**
 * Scala native compiler.
 * 
 * @constructor create a compiler object with default configuration. 
 */
class ScalaCompiler {
  
  private final val logger = Logger(LoggerFactory.getLogger(this.getClass))
  
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
    def handleException(e : Exception) : UserDefinedAction = {
      logger.warn("Failed to compile source code.", e)
      return null
    }
    
    try {
      val tree = toolbox.parse(source)
      val compiledCode = toolbox.compile(tree)
  
      return compiledCode().asInstanceOf[UserDefinedAction]
    } catch {
      case e: Exception => handleException(e)
    }
  }
}