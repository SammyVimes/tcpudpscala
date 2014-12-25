package study.server

import java.net.InetAddress

/**
 * Created by Семён on 25.12.2014.
 */
object App {

  def main(args: Array[String]): Unit = {
    val server = new UDPServer(port = 5555, address = null, bufferSize = 256)
    server.setHandler((msg: Message, server: Server) => handle(msg, server))
    server start()
  }

  def handle(msg: Message, server: Server) = {
    println(new String(msg.data))
    server.addOutgoingMessage(new Message(msg.data, msg.address))
  }

}
