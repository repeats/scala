package ipc

private[ipc] object requestIdGenerator {
  private var baseId : Long = 1
  
  def generateId() : Long = synchronized {
    baseId += 1
    return baseId - 1
  }
}