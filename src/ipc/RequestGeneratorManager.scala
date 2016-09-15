package ipc

class RequestGeneratorManager(client : RepeatClient) {
  
  final val systemHostRequest = new SystemHostRequest(client)
  final val systemClientRequest =  new SystemClientRequest(client)
  
  final val mouseRequest = new MouseRequest(client)
  final val keyboardRequest = new KeyboardRequest(client)
  final val sharedMemoryRequest = new SharedMemoryRequest(client)
}