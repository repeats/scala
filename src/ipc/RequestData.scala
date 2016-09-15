package ipc

import play.api.libs.json.Json

class RequestData(var requestType : String = "", var device : String = "", var action : String = "", var params : Seq[Any] = Seq[Any]()) {

}