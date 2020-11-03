package com.example.blapp.communication

import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress
import java.net.SocketException

class Channel(onSocketListener: OnSocketListener?) : Runnable {
    private var socket: DatagramSocket? = null
    private var running = false
    private val onSocketListener: OnSocketListener? = onSocketListener
    @Throws(SocketException::class)
    fun bind(port: Int) {
        socket = DatagramSocket(port)
    }

    fun start() {
        val thread = Thread(this)
        thread.start()
    }

    fun stop() {
        running = false
        socket!!.close()
    }

    override fun run() {
        val buffer = ByteArray(1024)
        val packet = DatagramPacket(buffer, buffer.size)
        running = true
        while (running) {
            try {
                socket!!.receive(packet)
                val msg = String(buffer, 0, packet.length)

                onSocketListener?.onReceived(msg)
            } catch (e: IOException) {
                break
            }
        }
    }

    fun sendTo(address: InetSocketAddress?, msg: ByteArray) {
        val runnable = Runnable {
            val packet = DatagramPacket(msg, msg.size)
            packet.socketAddress = address
            try {
                socket!!.send(packet)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        val thread = Thread(runnable)
        thread.start()
    }
}