package ipc

class RequestData(var requestType : String = "", var device : String = "", var action : String = "", var params : Seq[Any] = Seq[Any]()) {
  
}