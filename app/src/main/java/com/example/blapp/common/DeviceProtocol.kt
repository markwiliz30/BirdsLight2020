package com.example.blapp.common

import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.example.blapp.communication.Channel
import com.example.blapp.communication.OnSocketListener
import com.example.blapp.model.DataSetItem
import com.example.blapp.model.DayManager
import java.net.InetSocketAddress
import java.net.SocketException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

class DeviceProtocol : Handler.Callback, OnSocketListener {
    var sendCount = 0
    var executorService: ExecutorService = Executors.newSingleThreadExecutor()
    var longRunningTaskFuture = null
    var version: Byte = 0x01
    var byteDataLength = ByteArray(2)
    var signature = byteArrayOf(
        0xAA.toByte(),
        0x42.toByte(),
        0x49.toByte(),
        0x52.toByte(),
        0x44.toByte(),
        0x4C.toByte(),
        0x49.toByte(),
        0x47.toByte(),
        0x48.toByte(),
        0x54.toByte()
    )
    lateinit var data: ByteArray
    var command: Byte = 0
    lateinit var dataToSend: ByteArray
    var dataLength = 0
    private var address: InetSocketAddress? = null
    private var channel: Channel? = null
    private var handler: Handler? = null
    var postDelayedSendToModule = Handler()
    var canAccess = false
    var isRecognized = false
    var canSend = false
    fun startChannel(): String {
        address =
            InetSocketAddress(DeviceInformation.destinationIP, DeviceInformation.destinationPort)
        handler = Handler(this)
        return if (null == channel) {
            try {
                channel = Channel(this)
                channel!!.bind(DeviceInformation.sourcePort)
                channel!!.start()
                "Channel started"
            } catch (e: SocketException) {
                e.toString()
                //getActivity().finish();
            }
        } else "Channel is not null"
    }

    fun stopChannel() {
//        if (null == channel) {
//            channel!!.stop()
//        }
        channel!!.stop()
    }

    private fun dataLengthCounterHex(byteCount: Int): ByteArray {
        var lsd: Byte = 0x00
        var msd: Byte = 0x00
        var counter = 0
        val returnedBytes = ByteArray(2)
        for (i in 1..byteCount) {
            lsd++
            counter++
            if (counter > 255) {
                msd++
                lsd = 0x00
            }
        }
        returnedBytes[0] = lsd
        returnedBytes[1] = msd
        return returnedBytes
    }

    private val run = Runnable {
        if (canAccess) {
            sendData()
        }
    }
    var sendToModule = Runnable {
        if (canSend) {
            sendAll()
            canSend = false
        }
    }
    private val postSendData = Runnable {
        //if (canAccess)
        if (canAccess) {
            sendData()
        }
    }
    private val postSendAck = Runnable {
        if (isRecognized) {
            if (isRecognized) {
                sendFinalAck()
            }
        }
    }

//    fun setData(lXData: Int, lYData: Int, lLData: Int): ByteArray {
//        var x: Byte = 0
//        var y: Byte = 0
//        var l: Byte = 0
//        for (i in 0 until lXData) {
//            x++
//        }
//        for (j in 0 until lYData) {
//            y++
//        }
//        for (k in 0 until lLData) {
//            l++
//        }
//        return byteArrayOf(x, y, l)
//    }

    private fun sendData() {
        byteDataLength = dataLengthCounterHex(dataLength)
        val length = 1 + byteDataLength.size + 1 + data.size
        dataToSend = ByteArray(length)
        var i = 0
        while (i < length) {
            dataToSend[i] = version
            i++
            var l = 0
            while (l < byteDataLength.size) {
                dataToSend[i] = byteDataLength[l]
                l++
                i++
            }
            dataToSend[i] = command
            i++
            var m = 0
            while (m < data.size) {
                dataToSend[i] = data[m]
                m++
                i++
            }
            channel!!.sendTo(address, dataToSend)
            i++
        }
    }

    private fun sendFinalAck() {
        val finalAcknowledment = ByteArray(2)
        val O: Byte = 0x4f
        val K: Byte = 0x4b
        finalAcknowledment[0] = O
        finalAcknowledment[1] = K
        channel!!.sendTo(address, finalAcknowledment)

        if(sendCount > 0)
        {
            sendCount--
        }
    }

    private fun sendSignature() {
        channel!!.sendTo(address, signature)
    }

    private fun sendAll() {
        sendSignature()
        val postDelayedSendData = Handler()
        postDelayedSendData.postDelayed(postSendData, 100)
        val postDelayedSendAck = Handler()
        postDelayedSendAck.postDelayed(postSendAck, 200)
        canAccess = false
        isRecognized = false
    }

    fun transferData(exCommand: Byte, exData: ByteArray) {
        command = exCommand
        data = exData
        dataLength = data.size
        sendAll()
    }

    fun upload(dataSets: MutableList<DataSetItem>){
        val dataCount = dataSets.count()
        var i =0
        var canEnter = true
        sendSignature()
        while (canAccess){
            if(i < dataCount)
            {
                if(canEnter){
                    canEnter =  false
                    command = dataSets[i].myCommand!!
                    data = dataSets[i].myDatas!!
                    dataLength = data.size
                    sendData()
                    isRecognized = false
                }

                if(isRecognized)
                {
                    i++
                    canEnter = true
                }
            } else{
                canAccess=false
                i=0
            }
        }
    }

    fun sendMultiple(count: Int)
    {
        sendCount = count
        
    }

    fun transferDataWithDelay(exCommand: Byte, exData: ByteArray) {
        command = exCommand
        data = exData
        dataLength = data.size
        canSend = true
        postDelayedSendToModule.postDelayed(sendToModule, 100)
    }

    //for debugging
    fun AdjTransferDataWithDelay(exCommand: Byte, exData: ByteArray) {
        command = exCommand
        data = exData
        dataLength = data.size
        canSend = true
        postDelayedSendToModule.postDelayed(sendToModule, TestTransferRateVal.tRate.toLong())
    }

    //for debugging
    fun newAdjTransferWithDelay(exCommand: Byte, exData: ByteArray) {
        postDelayedSendToModule.removeCallbacks(sendToModule)
        command = exCommand
        data = exData
        dataLength = data.size
        canSend = true
        postDelayedSendToModule.postDelayed(sendToModule, TestTransferRateVal.tRate.toLong())
//        longRunningTaskFuture = executorService.submit(sendToModule) as Nothing?
    }

    fun receiveBLData(msg: String)
    {
        var getWday = msg.get(5)
        var wDayList = DayManager()
        var subWday = getWday.toByte()  //96
        while(subWday != 0.toByte())
        {
            subWday = breakWdays(subWday, wDayList)
        }

    }

    fun breakWdays(wdays: Byte, wDayList: DayManager): Byte
    {
        if(wdays >= 64.toByte())
        {
            wDayList.sunday = true
            return (wdays - 64.toByte()).toByte()
        }
        else if(wdays >= 32.toByte())
        {
            wDayList.saturday = true
            return (wdays - 32.toByte()).toByte()
        }
        else if(wdays >= 16.toByte())
        {
            wDayList.friday = true
            return (wdays - 16.toByte()).toByte()
        }
        else if(wdays >= 8.toByte())
        {
            wDayList.thursday = true
            return (wdays - 8.toByte()).toByte()
        }
        else if(wdays >= 4.toByte())
        {
            wDayList.wednesday = true
            return (wdays - 4.toByte()).toByte()
        }
        else if(wdays >= 2.toByte())
        {
            wDayList.tuesday = true
            return (wdays - 2.toByte()).toByte()
        }
        else if(wdays >= 1.toByte())
        {
            wDayList.monday = true
            return (wdays - 1.toByte()).toByte()
        }
        else
        {
            return wdays
        }
    }

    override fun handleMessage(msg: Message): Boolean {
        val bundle = msg.data
        val text = bundle.getString("text")
        val firstChar = text!!.get(0) + ""
        val secondChar = text!!.get(1) + ""
        val thirdChar = text!!.get(2) + ""
        val receivedAuth = firstChar + secondChar + thirdChar
        val authComp = "AUD"
        val recogComp = "RED"
        if (receivedAuth.equals(authComp)) {
            canAccess = true
//            isRecognized = true
            WifiUtils.isConnectedToBL = true
        }
        if (receivedAuth.equals(recogComp)) {
            isRecognized = true
            WifiUtils.isConnectedToBL = true
        }

        if(firstChar.toByte() == 0x16.toByte())
        {
            receiveBLData(text)
        }
        return true
    }

    override fun onReceived(msg: String) {
        val bundle = Bundle()
        bundle.putString("text", msg)
        val msg = Message()
        msg.data = bundle
        handler!!.sendMessage(msg)
    }
}
