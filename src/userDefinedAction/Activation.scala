package userDefinedAction

import play.api.libs.json.JsValue

object activationBuilder {
  def fromJson(json : JsValue) : Activation = {
    var hotkeys = (json \ "hotkey").get.as[Seq[JsValue]]
    var hotkeyJson = if (hotkeys.seq.length > 0) hotkeys(0).as[Seq[JsValue]] else Seq[JsValue]()
    
    val hotkey = hotkeyJson.map((x : JsValue) => x.as[Int])
    val mouseGestureList = (json \ "mouse_gesture").get.as[Seq[JsValue]]
    val mouseGestureName = if (mouseGestureList.seq.length > 0) (mouseGestureList(0) \ "name").get.as[String] else ""
    return new Activation(hotkey, mouseGestureName)
  }
}

class Activation(var hotkeys : Seq[Int], var mouseGesture : String) {
}