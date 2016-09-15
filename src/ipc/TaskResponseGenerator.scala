package ipc

import play.api.libs.json.Json
import play.api.libs.json.JsValue

class TaskResponseGenerator {
  
  final val Success = "Success"
  final val Failure = "Failure"
  
  def generateResponse(status : String, message : JsValue) = {
    Json.obj(
      "status" -> status,
      "message" -> message
    )
  }
}