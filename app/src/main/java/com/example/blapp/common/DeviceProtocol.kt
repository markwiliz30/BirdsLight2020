package com.example.blapp.common

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import com.CurrentId.extensions.CurrentID
import com.example.blapp.collection.*
import com.example.blapp.communication.Channel
import com.example.blapp.communication.OnSocketListener
import com.example.blapp.model.*
import java.net.InetSocketAddress
import java.net.SocketException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.coroutines.coroutineContext

class  DeviceProtocol : Handler.Callback, OnSocketListener {
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
    var canSend =  false
    var retCnt = 0

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
        postDelayedSendData.postDelayed(postSendData, 300)
        val postDelayedSendAck = Handler()
        postDelayedSendAck.postDelayed(postSendAck, 600)
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
        postDelayedSendToModule.postDelayed(sendToModule, GlobalVars.tRate.toLong())
    }

    //for debugging
    fun newAdjTransferWithDelay(exCommand: Byte, exData: ByteArray) {
        postDelayedSendToModule.removeCallbacks(sendToModule)
        command = exCommand
        data = exData
        dataLength = data.size
        canSend = true
        postDelayedSendToModule.postDelayed(sendToModule, GlobalVars.tRate.toLong())
//        longRunningTaskFuture = executorService.submit(sendToModule) as Nothing?
    }

    fun receiveBLData(msg: ByteArray)
    {
        val receivedAuth = String(msg!!, 0, 3)
        if(receivedAuth == "AUD" || receivedAuth == "RED")
        {
            return
        }

        var getWdayCount = msg.get(5)
        if(getWdayCount.toInt() != 0)
        {
            val getPgm = msg.get(0)
            var checkPgm = PgmCollection.pgmCollection.find { it.pgm == getPgm }
            if(checkPgm != null)
            {
                return
            }
            val getSmonth = msg.get(1)
            val getSday = msg.get(2)
            val getEmonth = msg.get(3)
            val getEday = msg.get(4)

            var dayManagerItem = DayManager()
            dayManagerItem.pgm = getPgm
            dayManagerItem.sMonth = getSmonth.toString()
            dayManagerItem.sDay = getSday.toString()
            dayManagerItem.eMonth = getEmonth.toString()
            dayManagerItem.eDay = getEday.toString()

            var wdayPos = 0
            var subWday = 0.toByte()
            var getShour = 0.toByte()
            var getSmins = 0.toByte()
            var getEhour = 0.toByte()
            var getEmins = 0.toByte()

//            while(i != getWdayCount.toInt())
            for(i in 1..getWdayCount.toInt())
            {
                wdayPos = ((5*i) - (i-1))+i

                getShour = msg.get(wdayPos+1)
                getSmins = msg.get(wdayPos+2)
                getEhour = msg.get(wdayPos+3)
                getEmins = msg.get(wdayPos+4)

                var getWday = msg.get(wdayPos)
                subWday = getWday

                var wDayList: MutableList<Int> = mutableListOf()

                while(subWday > 0)
                {
                    subWday = breakWdays(subWday, wDayList, dayManagerItem)
                    var schedItem = ScheduleItem()
                    schedItem.pgm = getPgm
                    schedItem.smonth = getSmonth
                    schedItem.sday = getSday
                    schedItem.emonth = getEmonth
                    schedItem.eday = getEday
                    schedItem.wday = convertToLocalBinFormat(wDayList[0].toByte())
                    var filteredSched = ScheduleCollection.scheduleCollection.filter { it.wday == schedItem.wday }
                    var schedNumber = filteredSched.count() + 1
                    schedItem.sched = schedNumber.toByte()
                    wDayList.clear()
                    schedItem.shour = getShour
                    schedItem.sminute = getSmins
                    schedItem.ehour = getEhour
                    schedItem.eminute = getEmins

                    ScheduleCollection.scheduleCollection.add(schedItem)
                }
            }
            var pgmItem = PgmItem()
            pgmItem.pgm = getPgm
            PgmCollection.pgmCollection.add(pgmItem)

            DayCollection.dayCollection.add(dayManagerItem)

            collectRecSteps(msg, wdayPos, getPgm)
        }
    }

    fun collectRecSteps(msg: ByteArray, wdayPos: Int, pgm: Byte)
    {
        //adjust the additional to wdayPos
        val stepCountPos = wdayPos + 5
        var stepCount = msg.get(stepCountPos)
        for(i in 0 until stepCount.toInt())
        {
            var posHold = (stepCountPos) + (i*4)
            var xPos = posHold + 1
            var yPos = posHold + 2
            var lPos = posHold + 3
            var tPos = posHold + 4

            val stp = i+1
            val xVal = msg.get(xPos)
            val yVal = msg.get(yPos)
            val lVal = msg.get(lPos)
            val tVal = msg.get(tPos)

            var stepItem = StepItem()
            stepItem.pgm = pgm
            stepItem.step = stp.toByte()
            stepItem.pan = xVal
            stepItem.tilt = yVal
            stepItem.blink = lVal
            stepItem.time = tVal

            StepCollection.stepCollection.add(stepItem)
        }
    }

    fun convertToLocalBinFormat(binVal: Byte?): Byte{
        var retVal = 0.toByte()
        if(binVal == 1.toByte())
        {
            retVal = 1.toByte()
        }else if(binVal == 2.toByte())
        {
            retVal = 2.toByte()
        }else if(binVal == 4.toByte())
        {
            retVal = 3.toByte()
        }else if(binVal == 8.toByte())
        {
            retVal = 4.toByte()
        }else if(binVal == 16.toByte())
        {
            retVal = 5.toByte()
        }else if(binVal == 32.toByte())
        {
            retVal = 6.toByte()
        }else if(binVal == 64.toByte())
        {
            retVal = 7.toByte()
        }

        return retVal
    }

    fun breakWdays(wdays: Byte, wDayList: MutableList<Int>, dayManager: DayManager): Byte
    {
        if(wdays >= 64.toByte())
        {
            dayManager.sunday = true
            wDayList+=64
            return (wdays - 64.toByte()).toByte()
        }
        else if(wdays >= 32.toByte())
        {
            dayManager.saturday = true
            wDayList+= 32
            return (wdays - 32.toByte()).toByte()
        }
        else if(wdays >= 16.toByte())
        {
            dayManager.friday = true
            wDayList += 16
            return (wdays - 16.toByte()).toByte()
        }
        else if(wdays >= 8.toByte())
        {
            dayManager.thursday = true
            wDayList += 8
            return (wdays - 8.toByte()).toByte()
        }
        else if(wdays >= 4.toByte())
        {
            dayManager.wednesday = true
            wDayList += 4
            return (wdays - 4.toByte()).toByte()
        }
        else if(wdays >= 2.toByte())
        {
            dayManager.tuesday = true
            wDayList += 2
            return (wdays - 2.toByte()).toByte()
        }
        else if(wdays >= 1.toByte())
        {
            dayManager.monday = true
            wDayList += 1
            return (wdays - 1.toByte()).toByte()
        }
        else
        {
            return 0.toByte()
        }
    }

    override fun handleMessage(msg: Message): Boolean {
        val bundle = msg.data
        val data = bundle.getByteArray("text")
        lateinit var receivedAuth: String
        if(data!!.size >=3) {
            receivedAuth = String(data!!, 0, 3)
        }
        val authComp = "AUD"
        val recogComp = "RED"

        canAccess = false
        isRecognized = false

//        LogCollection.logCollection.add(String(data!!))

        if (receivedAuth.equals(authComp)) {
            canAccess = true
            if(retCnt != 0)
            {
                retCnt = 0
            }
//            isRecognized = true
            WifiUtils.isConnectedToBL = true
        }

        if (receivedAuth.equals(recogComp)) {
            isRecognized = true
            WifiUtils.isConnectedToBL = true
        }

//        if(GlobalVars.willRetreive)
//        {
//            if(retCnt == 2)
//            {
//                retCnt = 0
//                GlobalVars.willRetreive = false
//                receiveBLData(data)
//            }
//            else
//            {
//                retCnt++
//            }
//
//        }

        return true
    }

    override fun onReceived(msg: ByteArray) {
        val bundle = Bundle()
        bundle.putByteArray("text", msg)
        val msg = Message()
        msg.data = bundle
        handler!!.sendMessage(msg)
    }
}
