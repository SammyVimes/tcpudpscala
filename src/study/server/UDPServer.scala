package study.server

import java.io.IOException
import java.net.{InetSocketAddress, InetAddress}
import java.nio.ByteBuffer
import java.nio.channels.{SelectionKey, DatagramChannel, Selector}
import java.util

/**
 * Created by Семён on 25.12.2014.
 */
class UDPServer(override val port: Int, override val address: InetAddress, override val bufferSize: Int)
  extends Server(port, address, bufferSize) {

  val selector: Selector = Selector open()
  val serverChannel: DatagramChannel = DatagramChannel open()
  serverChannel bind socketAddress
  serverChannel configureBlocking false
  val selectionKey: SelectionKey = serverChannel register (selector, SelectionKey.OP_READ)



  override def runServer(): Unit = {
    while (running) {
      var continue: Boolean = false
      try {
        if (selector.selectNow() <= 0) {
          continue = true
        }
      } catch {
        case ioe: IOException => ioe printStackTrace()
      }
      if (!continue) {
        val selectedKeys: util.Set[SelectionKey] = selector.selectedKeys()
        val iterator = selectedKeys.iterator()
        while (iterator.hasNext) {
          val key = iterator next()

          var _continue = false
          if (!key.isValid) {
            println("Invalid key!")
            _continue = true
          }

          if (key.isReadable) {
            val readBuffer: ByteBuffer = ByteBuffer.allocate(bufferSize)
            var senderAddress: InetSocketAddress  = null

            val dChannel:DatagramChannel = key.channel().asInstanceOf[DatagramChannel]

            try {
              senderAddress = dChannel.receive(readBuffer).asInstanceOf[InetSocketAddress]
            } catch {
              case ioe: IOException => ioe printStackTrace()
            }

            readBuffer.flip()
            val b:Array[Byte] = new Array[Byte](readBuffer.remaining())
            readBuffer.get(b, 0, b.length)
            handler(new Message(b, senderAddress), this)
          }

        }
        selectedKeys.clear()
      }
    }
  }

  override def sendMessage(msg: Message): Unit = {
    try {
      serverChannel send (ByteBuffer.wrap(msg.data), msg.address)
    } catch {
      case ioe: IOException => ioe printStackTrace()
    }
  }

}
