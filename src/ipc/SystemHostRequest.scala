package ipc

class SystemHostRequest(client : RepeatClient) extends RequestGenerator(client) {
  
  override def getDefaultData() : RequestData = {
    val output = new RequestData()
    output.requestType = "system_host"
    output.device = "system"
    
    return output
  }
  
  def keepAlive() = {
    val data = getDefaultData()
    data.action = "keep_alive"
    data.params = Seq[Any]()
    
    this.sendRequest(data, false)
  }
}