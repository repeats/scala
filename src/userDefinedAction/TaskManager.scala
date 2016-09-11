package userDefinedAction

import scala.collection.mutable.HashMap
import compiler.ScalaCompiler
import ipc.RepeatClient

class TaskManager(var controller : RepeatClient, var compiler : ScalaCompiler) {
  
  private var baseId = 0
  private val tasks = new HashMap[Long, UserDefinedAction]()
  
  private def nextId() : Long = synchronized {
    val output = baseId
    baseId += 1
    return output
  }
  
  def createTask(sourceFile : String) : Option[Long] = {
    val id = nextId()
    val task = compiler.compileFromFile(sourceFile)
    tasks.put(id, task)
    return Option(id)
  }

  def executeTask(id : Long, activation : Activation) : Boolean = {
    val task = tasks.get(id)
    if (task.isEmpty) {
      return false
    }
    
    task.get.execute(controller, activation)
    return true
  }
  
  def deleteTask(id : Long) : Option[UserDefinedAction] = {
    return tasks.remove(id)
  }
}