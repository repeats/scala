package ipc

class MouseRequest(client : RepeatClient) extends RequestGenerator(client) {
  
  override def getDefaultData() : RequestData = {
    val output = new RequestData()
    output.requestType = "action"
    output.device = "mouse"
    
    return output
  }
  
  def leftClick(x : Option[Int] = None, y : Option[Int] = None) = {
    val data = getDefaultData()
    data.action = "left_click"
    data.params = if (x.isEmpty || y.isEmpty) List[Int]() else List[Int](x.get, y.get);
    
    sendRequest(data)
  }
  
  def rightClick(x : Option[Int] = None, y : Option[Int] = None) = {
    val data = getDefaultData()
    data.action = "right_click"
    data.params = if (x.isEmpty || y.isEmpty) List[Int]() else List[Int](x.get, y.get);
    
    sendRequest(data)
  }
  
  def move(x : Int, y : Int) = {
    val data = getDefaultData()
    data.action = "move"
    data.params = List[Int](x, y)
    sendRequest(data)
  }
  
  def moveBy(x : Int, y : Int) = {
    val data = getDefaultData()
    data.action = "move_by"
    data.params = List[Int](x, y)
    sendRequest(data)
  }
  
  def drag(x : Int, y : Int) = {
    val data = getDefaultData()
    data.action = "drag"
    data.params = List[Int](x, y)
    sendRequest(data)
  }
  
  def dragBy(x : Int, y : Int) = {
    val data = getDefaultData()
    data.action = "drag_by"
    data.params = List[Int](x, y)
    sendRequest(data)
  }
  
  def getPosition() = {
    val data = getDefaultData()
    data.action = "get_position"
    data.params = List[Int]()
    sendRequest(data)
  }

  def getColor(x : Option[Int] = None, y : Option[Int] = None) = {
    val data = getDefaultData()
    data.action = "get_color"
    data.params = if (x.isEmpty || y.isEmpty) List[Int]() else List[Int](x.get, y.get);
    
    sendRequest(data)
  }
}