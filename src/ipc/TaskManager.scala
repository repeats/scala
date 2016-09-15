package ipc

import play.api.libs.json.JsValue
import compiler.ScalaCompiler
import scala.collection.mutable.HashMap
import userDefinedAction.UserDefinedAction
import play.api.libs.json.Json
import play.api.libs.json.JsString
import java.nio.file.Files
import java.nio.file.Paths
import userDefinedAction.Activation
import userDefinedAction.activationBuilder

class TaskManager(val client : RepeatClient) {
  
  private var baseId : Long = 1
  private val compiler = new ScalaCompiler()
  private val responseGenerator = new TaskResponseGenerator()
  private val tasks = new HashMap[Long, UserDefinedAction]()
  
  private def nextId() : Long = {
    baseId += 1
    return baseId - 1
  }
  
  def processMessage(id : Long, message : JsValue) : JsValue = {
    val action = (message \ "task_action").get.as[String];
    val params = (message \ "parameters").get;
    
    if (action.equals("create_task")) {
      return createTask(params(0).get.as[String])
    } else if (action.equals("run_task")) {
      println("Running task " + params)
      return runTask(params(0).get.as[Long], activationBuilder.fromJson(params(1).get))
    } else if (action.equals("remove_task")) {
      return removeTask(params(0).get.as[Long])
    }
    
    return responseGenerator.generateResponse(
            responseGenerator.Failure,
            JsString("Unknown action " + action)
          );
  }
  
  def createTask(fileName : String) : JsValue = {
    if (!Files.exists(Paths.get(fileName)) || !Paths.get(fileName).toFile().canRead()) {
      return responseGenerator.generateResponse(
            responseGenerator.Failure,
            JsString("Path not exist or not readable " + fileName)
          )
    }
    
    try {
      var newTask = compiler.compileFromFile(fileName)
      newTask.fileName = fileName
      val id = nextId()
      tasks.put(id, newTask)
      
      return responseGenerator.generateResponse(
          responseGenerator.Success,
          Json.obj(
            "id" -> id,
            "file_name" -> fileName
          ))
    } catch {
      // TODO more detail logging
      case e : Exception => responseGenerator.generateResponse(
            responseGenerator.Failure,
            JsString("Cannot compile file " + fileName)
          )
    }
  }
  
  def runTask(id : Long, activation : Activation) : JsValue = {
    if (!tasks.contains(id)) {
      return responseGenerator.generateResponse(
            responseGenerator.Failure,
            JsString("Cannot find task id " + id)
          )
    }
    
    val task = tasks.get(id).get
    try {
      task.execute(client, activation)
    } catch {
      case e : Exception => responseGenerator.generateResponse(
            responseGenerator.Failure,
            JsString("Cannot execute task id " + id)
          )
    }

    return responseGenerator.generateResponse(
        responseGenerator.Success,
        Json.obj(
          "id" -> id,
          "file_name" -> task.fileName
        ))
  }
  
  def removeTask(id : Long) : JsValue = {
    if (!tasks.contains(id)) {
      return responseGenerator.generateResponse(
        responseGenerator.Success,
        Json.obj(
          "id" -> id,
          "file_name" -> ""
        ))
    }
    
    val removing = tasks.remove(id)
    return responseGenerator.generateResponse(
        responseGenerator.Success,
        Json.obj(
          "id" -> id,
          "file_name" -> removing.get.fileName
        ))
  }
}
