package ipc

import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.libs.json.JsString
import play.api.libs.json.JsNumber

abstract class RequestGenerator(var client : RepeatClient) {
  
  private var baseId = 0
  
  private def generateId() : Long = synchronized {
    baseId += 1
    return baseId - 1
  }
  
  def getDefaultData() : RequestData;
  
  def generateRequest(requestData : RequestData) : JsValue = {
    Json.obj(
      "type" -> requestData.requestType,
      "id" -> generateId(),
      "content" -> Json.obj(
        "content" -> Json.obj(
          "device" -> requestData.device,
          "action" -> requestData.action,
          "parameters" -> Json.arr(requestData.params.map(x => if (x.isInstanceOf[Int]) JsNumber(x.asInstanceOf[Int]) else JsString(x.toString())))
        )
      )
    )
  }
  
  def sendRequest(data : RequestData, blockingWait : Boolean = true) : JsValue = {
    return null;
  }
}