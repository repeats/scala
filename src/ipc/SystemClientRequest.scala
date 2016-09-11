package ipc

class SystemClientRequest(client : RepeatClient) extends RequestGenerator(client) {
  
  override def getDefaultData() : RequestData = {
    val output = new RequestData()
    output.requestType = "system_client"
    output.device = "system"
    
    return output
  }
  
  def identify() = {
    val data = getDefaultData()
    data.action = "identify"
    data.params = Seq[String]("scala", "12345") // TODO fill in the real port
    
    this.sendRequest(data, false)
  }
}