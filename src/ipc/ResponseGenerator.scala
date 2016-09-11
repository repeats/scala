package ipc

import play.api.libs.json.Json

class ResponseGenerator {
  
  
  def generateResponse(status : String, message : String) = {
    Json.obj(
      "status" -> status,
      "message" -> message
    )
  }
}