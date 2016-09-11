package ipc

class SharedMemoryRequest(client : RepeatClient) extends RequestGenerator(client) {
  
  override def getDefaultData() : RequestData = {
    val output = new RequestData()
    output.requestType = "shared_memory"
    output.device = "shared_memory"
    
    return output
  }
  
  def get(namespace : String, variableName : String) = {
    val data = getDefaultData()
    data.action = "get"
    data.params = Seq[String](namespace, variableName)
    
    sendRequest(data)
  }
  
  def set(namespace : String, variableName : String, value : String) = {
    val data = getDefaultData()
    data.action = "set"
    data.params = Seq[String](namespace, variableName, value)
    
    sendRequest(data)
  }
  
  def del(namespace : String, variableName : String) = {
    val data = getDefaultData()
    data.action = "del"
    data.params = Seq[String](namespace, variableName)
    
    sendRequest(data)
  }
  
  def getInstance(namespace : String) = {
    
  }
}

class SharedMemoryInstance(var requestGenerator : SharedMemoryRequest, var namespace : String) {
  
  def get(variableName : String) = {
    requestGenerator.get(namespace, variableName)
  }
  
  def set(variableName : String, value : String) = {
    requestGenerator.set(namespace, variableName, value)
  }
  
  def del(variableName : String) = {
    requestGenerator.del(namespace, variableName)
  }
}