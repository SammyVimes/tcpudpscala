package study.server

import java.net.{Socket, ServerSocket, InetAddress}

/**
 * Created by Семён on 25.12.2014.
 */
class TCPServer(val port: Int, val onSocketAccepted: (Socket => Unit)) {

  val serverSocket = new ServerSocket(port, 5000)

  new Thread() {
    override def run() = {
      runServer()
    }
  } start()

  def runServer(): Unit = {
    while (true) {
      val socket = serverSocket.accept()
      onSocketAccepted(socket)
    }
  }

}
