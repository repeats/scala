package ipc

import java.net.Socket
import java.net.InetAddress
import scala.io.BufferedSource
import java.io.PrintStream
import java.util.concurrent.ConcurrentLinkedQueue
import scala.collection.mutable.SynchronizedQueue
import scala.collection.mutable.SynchronizedMap
import java.util.Collections.SynchronizedMap
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Semaphore
import play.api.libs.json.JsValue
import java.util.concurrent.TimeUnit
import java.io.OutputStreamWriter
import java.io.BufferedWriter
import java.io.PrintWriter
import java.io.InputStreamReader
import java.io.BufferedReader
import play.api.libs.json.Json

class RepeatClient {
  
  private final val MaxBufferSize = 1024
  private final val ServerTimeoutMilliseconds = 10 * 1000
  private final val ClientTimeoutMilliseconds =  (ServerTimeoutMilliseconds * 0.3).toInt
  private final val Delimiter : Char = 2
  
  private final var DefaultHost = "localhost"
  private final val DefaultPort = 9999
  
  var isTerminated = false
  val sendQueue = new LinkedBlockingQueue[String]()
  val receiveSignal = new ConcurrentHashMap[Long, Semaphore]()
  val receivedJSON = new ConcurrentHashMap[Long, JsValue]()
  val requests = new RequestGeneratorManager(this)
  val taskManager = new TaskManager(this)
  
  private var socket : Socket = null
  private var inputStream : BufferedReader = null
  private var outputStream : PrintWriter = null
  
  object State extends Enumeration {
    type State = Value
    val FIRST_DELIMITER_START, SECOND_DELIMITER_START = Value
    val FIRST_DELIMITER_END, SECOND_DELIMITER_END = Value
  }
  private var processState = State.FIRST_DELIMITER_START;
  private var currentMessage = new StringBuilder();
  
  private def processMessage(message : JsValue) = {
    val requestType = (message \ "type").get.as[String];
    val id = (message \ "id").get.as[Long];
    val content = message \ "content";
    
    if (receiveSignal.contains(id)) {
      val resultObject = content \ "message";
      receivedJSON.put(id, resultObject.get);
      
      // Notify the caller that message with this id has been replied.
      receiveSignal.get(id).release();
    } else if (requestType.equals("task")) { // Then pass on to the task manager
      val respondingThread = new Thread {
        override def run() = {
          val response = taskManager.processMessage(id, content.get);
          sendQueue.add(Json.obj(
              "type" -> requestType,
              "id" -> id,
              "content" -> response
          ).toString())

//          println("Added to send queue")
        }
      }
      respondingThread.start()
    } else {
      // TODO log this instead
      println("Unknown id " + id + ". Drop message...");
    }
  }
  
  private def processByte(value : Int) = {
    if (processState == State.FIRST_DELIMITER_START) {
      processState = if (value == Delimiter) State.SECOND_DELIMITER_START else processState
    } else if (processState == State.SECOND_DELIMITER_START) {
      processState = if (value == Delimiter) State.FIRST_DELIMITER_END else processState
    } else if (processState == State.FIRST_DELIMITER_END) {
      processState = if (value == Delimiter) State.SECOND_DELIMITER_END else processState
      if (processState == State.SECOND_DELIMITER_END) {
        try {
          processMessage(Json.parse(currentMessage.toString()))
        } catch {
          // TODO more detail logging
          case e : Exception => println("Exception parsing message " + currentMessage.toString())
        }
        currentMessage.clear()
      } else {
        currentMessage.append(value.toChar);
      }
    } else if (processState == State.SECOND_DELIMITER_END) {
      processState = if (value == Delimiter) State.FIRST_DELIMITER_START else processState
    }
  }
  
  def read() = {
    while (!isTerminated) {
      val value : Int = inputStream.read()
      processByte(value)
    }
  }
  
  def write() = {
    while (!isTerminated) {
      val data = sendQueue.poll(ClientTimeoutMilliseconds, TimeUnit.MILLISECONDS)
      if (data == null) { // Then send a keep alive
        requests.systemHostRequest.keepAlive()
      } else { // Send the data
        println("Sending " + data)
        val toSend = "%s%s%s%s%s".format(Delimiter, Delimiter, data, Delimiter, Delimiter)
        outputStream.println(toSend)
        outputStream.flush()
      }
    }
  }

  private def clear() = {
    sendQueue.clear()
    receiveSignal.clear()
    receivedJSON.clear()
  }
  
  def start() = {
    isTerminated = false
    socket = new Socket(InetAddress.getByName(DefaultHost), DefaultPort)
    inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()))
    outputStream = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream)))
    
    requests.systemClientRequest.identify(socket.getLocalPort())
    
    val readThread = new Thread {
      override def run = {
        read()
      }
    }
    
    val writeThread = new Thread {
      override def run = {
        write()
      }
    }
    
    readThread.start()
    writeThread.start()
  }
  
  def stop() = {
    isTerminated = true
    socket.close()
  }
}