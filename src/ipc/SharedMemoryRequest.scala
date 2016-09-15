package ipc

class SharedMemoryRequest(client : RepeatClient) extends RequestGenerator(client) {
  
  override def getDefaultData() : RequestData = {
    val output = new RequestData()
    output.requestType = "shared_memory"
    output.device = "shared_memory"
    
    return output
  }
  
  def get(namespace : String, variableName : String) : Option[String] = {
    val data = getDefaultData()
    data.action = "get"
    data.params = Seq[String](namespace, variableName)
    
    val result = sendRequest(data)
    return if (result.isDefined) Some(result.get.toString()) else None
  }
  
  def set(namespace : String, variableName : String, value : String) : Option[String] = {
    val data = getDefaultData()
    data.action = "set"
    data.params = Seq[String](namespace, variableName, value)
    
    val result = sendRequest(data)
    return if (result.isDefined) Some(result.get.toString()) else None
  }
  
  def del(namespace : String, variableName : String) : Option[String] = {
    val data = getDefaultData()
    data.action = "del"
    data.params = Seq[String](namespace, variableName)
    
    val result = sendRequest(data)
    return if (result.isDefined) Some(result.get.toString()) else None
  }
  
  def getInstance(namespace : String) : SharedMemoryInstance = {
    return new SharedMemoryInstance(this, namespace)
  } 
}

class SharedMemoryInstance(var requestGenerator : SharedMemoryRequest, var namespace : String) {
  
  def get(variableName : String) : Option[String] = {
    return requestGenerator.get(namespace, variableName)
  }
  
  def set(variableName : String, value : String) : Option[String] = {
    return requestGenerator.set(namespace, variableName, value)
  }
  
  def del(variableName : String) : Option[String] = {
    return requestGenerator.del(namespace, variableName)
  }
}