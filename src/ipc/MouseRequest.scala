package ipc

import play.api.libs.json.JsValue

class MouseRequest(client : RepeatClient) extends RequestGenerator(client) {
  
  override def getDefaultData() : RequestData = {
    val output = new RequestData()
    output.requestType = "action"
    output.device = "mouse"
    
    return output
  }
  
  def press(mask : Int) = {
    val data = getDefaultData()
    data.action = "press"
    data.params = List[Int](mask)
    
    sendRequest(data, false)
  }
  
  def release(mask : Int) = {
    val data = getDefaultData()
    data.action = "release"
    data.params = List[Int](mask)
    
    sendRequest(data, false)
  }
  
  def click(mask : Int) = {
    val data = getDefaultData()
    data.action = "click"
    data.params = List[Int](mask)
    
    sendRequest(data, false)
  }
  
  def leftClick(x : Option[Int] = None, y : Option[Int] = None) = {
    val data = getDefaultData()
    data.action = "left_click"
    data.params = if (x.isEmpty || y.isEmpty) List[Int]() else List[Int](x.get, y.get);
    
    sendRequest(data, false)
  }
  
  def rightClick(x : Option[Int] = None, y : Option[Int] = None) = {
    val data = getDefaultData()
    data.action = "right_click"
    data.params = if (x.isEmpty || y.isEmpty) List[Int]() else List[Int](x.get, y.get);
    
    sendRequest(data, false)
  }
  
  def move(x : Int, y : Int) = {
    val data = getDefaultData()
    data.action = "move"
    data.params = List[Int](x, y)
    sendRequest(data, false)
  }
  
  def moveBy(x : Int, y : Int) = {
    val data = getDefaultData()
    data.action = "move_by"
    data.params = List[Int](x, y)
    sendRequest(data, false)
  }
  
  def drag(x : Int, y : Int) = {
    val data = getDefaultData()
    data.action = "drag"
    data.params = List[Int](x, y)
    sendRequest(data, false)
  }
  
  def dragBy(x : Int, y : Int) = {
    val data = getDefaultData()
    data.action = "drag_by"
    data.params = List[Int](x, y)
    sendRequest(data, false)
  }
  
  def getPosition() : Seq[Int] = {
    val data = getDefaultData()
    data.action = "get_position"
    data.params = List[Int]()
    
    return sendRequest(data).get.as[Seq[JsValue]].map { x => x.as[Int] }
  }

  def getColor(x : Option[Int] = None, y : Option[Int] = None) : Seq[Int] = {
    val data = getDefaultData()
    data.action = "get_color"
    data.params = if (x.isEmpty || y.isEmpty) List[Int]() else List[Int](x.get, y.get);
    
    sendRequest(data).get.as[Seq[JsValue]].map { x => x.as[Int] }
  }
}