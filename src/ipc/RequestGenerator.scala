package ipc

import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.libs.json.JsString
import play.api.libs.json.JsNumber
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

abstract class RequestGenerator(var client : RepeatClient) {
  
  private final val logger = Logger(LoggerFactory.getLogger(this.getClass))
  
  private final val RequestTimeoutSecond = 1
  
  def getDefaultData() : RequestData;
  
  def generateRequest(requestData : RequestData, id : Long) : JsValue = {
    Json.obj(
      "type" -> requestData.requestType,
      "id" -> id,
      "content" -> Json.obj(
        "device" -> requestData.device,
        "action" -> requestData.action,
        "parameters" -> requestData.params.map(x => if (x.isInstanceOf[Int]) JsNumber(x.asInstanceOf[Int]) else JsString(x.toString()))
      )
    )
  }
  
  def sendRequest(data : RequestData, blockingWait : Boolean = true) : Option[JsValue] = {
    val id = requestIdGenerator.generateId()
    val stringData = generateRequest(data, id).toString()
    
    val signal = new Semaphore(0)
    client.receiveSignal.put(id, signal)
    client.sendQueue.add(stringData)
    
    if (blockingWait) {
      if (!signal.tryAcquire(RequestTimeoutSecond, TimeUnit.SECONDS)) {
        logger.info("Timeout waiting for request to finish.")
        return None;
      }
    }
    
    client.receiveSignal.remove(id)
    if (client.receivedJSON.contains(id)) {
      val result = client.receivedJSON.remove(id)
      if (result != null) {
        return Some(result)
      }
    }

    return None;
  }
}