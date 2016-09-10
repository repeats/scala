package compiler

import userDefinedAction.UserDefinedAction
import userDefinedAction.UserDefinedAction
import reflect.runtime.currentMirror
import scala.tools.reflect.ToolBox
import io.Source

class ScalaCompiler {
  
  private var _config = new CompilerConfig();
  
  def config = _config;
  
  def compile(source : String) : UserDefinedAction = {
    val toolbox = currentMirror.mkToolBox();
    import toolbox.u._
    
    val fileContents = Source.fromFile("/home/vda/test.scala").getLines.mkString("\n");
    val tree = toolbox.parse(fileContents);
    val compiledCode = toolbox.compile(tree);
  
    return  compiledCode().asInstanceOf[UserDefinedAction];
  }
}