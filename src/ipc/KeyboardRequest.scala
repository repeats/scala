package ipc

class KeyboardRequest(client : RepeatClient) extends RequestGenerator(client) {
  
  override def getDefaultData() : RequestData = {
    val output = new RequestData()
    output.requestType = "action"
    output.device = "keyboard"
    
    return output
  }
  
  def typeKeys(keys : Int*) = {
    val data = getDefaultData()
    data.action = "type"
    data.params = keys
    sendRequest(data)
  }
  
  def typeStrings(strings : String*) = {
    val data = getDefaultData()
    data.action = "type_string"
    data.params = strings
    sendRequest(data)
  }
  
  def combination(keys : Int*) = {
    val data = getDefaultData()
    data.action = "combination"
    data.params = keys
    sendRequest(data)
  }
}