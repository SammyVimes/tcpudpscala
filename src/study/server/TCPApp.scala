package study.server

import java.io.{OutputStreamWriter, PrintWriter, InputStreamReader, BufferedReader}
import java.net.Socket

/**
 * Created by Семён on 25.12.2014.
 */
object TCPApp {

  def main(args: Array[String]): Unit = {
    new TCPServer(port = 5555, onSocketAccepted = socket => handle(socket))
  }

  def handle(socket: Socket) = {
    write(socket, read(socket))
  }

  def read(socket: Socket) : String = {
    val bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream))
    var request = ""
    var line:String = null

    while ((line = bufferedReader.readLine()) != null && !(line equals "q")) {

      request += line + "\n"

    }
    request
  }

  def write(socket: Socket, string: String) = {
    val out: PrintWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream), true)
    out.println(string)
    out.flush()
    socket.close()
  }

}
