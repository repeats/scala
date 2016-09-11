package ipc

import java.net.Socket
import java.net.InetAddress
import scala.io.BufferedSource
import java.io.PrintStream

class RepeatClient {
  
  private final val MaxBufferSize = 1024
  private final val ServerTimeoutMilliseconds = 10 * 1000
  private final val ClientTimeoutMilliseconds =  (ServerTimeoutMilliseconds * 0.8).toInt
  final val Delimiter = 2
  
  private final var DefaultHost = "localhost"
  private final val DefaultPort = 9999
  
  def start() = {
    val s = new Socket(InetAddress.getByName(DefaultHost), DefaultPort)
    lazy val in = new BufferedSource(s.getInputStream()).getLines()
    val out = new PrintStream(s.getOutputStream());
    
    out.println("Hello, world")
    out.flush()
    println("Received: " + in.next())
    
    s.close()
  }
}