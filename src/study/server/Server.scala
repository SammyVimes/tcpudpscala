package study.server

import java.net.{InetSocketAddress, InetAddress}
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by Семён on 25.12.2014.
 */
abstract class Server(val port: Int, val address: InetAddress, val bufferSize: Int) {

  val MESSAGES_FLUSH_THRESHOLD = 1

  val monitor = new Object()

  val running: Boolean = true

  val outgoingMessageQueue = new ConcurrentLinkedQueue[Message]()
  val socketAddress = if (address == null) new InetSocketAddress(port) else new InetSocketAddress(address, port)

  var handler = (message:Message, server: Server) => ()

  def setHandler(handler: (Message, Server) => Unit) = {
    this.handler = handler
  }

  def start() = {
    new Thread {

      override def run(): Unit = runServer()

    } start()
  }

  def runServer()

  def addOutgoingMessage(message: Message) = {
    monitor.synchronized {
      outgoingMessageQueue add message
      if (outgoingMessageQueue.size() >= MESSAGES_FLUSH_THRESHOLD) {
        processOutgoingMessages()
      }
    }
  }

  def processOutgoingMessages() {
    monitor.synchronized {
      if (outgoingMessageQueue == null)
        throw new NullPointerException("outgoingMessagesQueue should have been created before this moment")

      while (!outgoingMessageQueue.isEmpty) {
        sendMessage(outgoingMessageQueue poll())
      }
    }
  }

  def sendMessage(msg: Message)

}
