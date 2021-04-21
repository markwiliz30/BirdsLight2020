package com.example.blapp

import android.app.Activity
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.CurrentId.extensions.CurrentID
import com.example.blapp.adapter.WifiAdapter
import com.example.blapp.collection.DayCollection
import com.example.blapp.collection.PgmCollection
import com.example.blapp.collection.ScheduleCollection
import com.example.blapp.collection.StepCollection
import com.example.blapp.common.DeviceProtocol
import com.example.blapp.common.Language
import com.example.blapp.common.Protocol
import com.example.blapp.common.WifiUtils
import com.example.blapp.databasehelper.DBmanager
import com.example.blapp.model.*
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_landing.*
import kotlinx.android.synthetic.main.fragment_day_picker.*
import kotlinx.android.synthetic.main.fragment_landing.*
import java.io.IOException
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
@Suppress("DEPRECATION")
class LandingFragment : Fragment() {
    lateinit var navController: NavController

    lateinit var layoutManager: LinearLayoutManager
    //var wifiList = ArrayList<WifiItem>()
    lateinit var currentConnectedSSID: String
    internal lateinit var dbm: DBmanager
    var resultList = ArrayList<ScanResult>()
    lateinit var wifiManager: WifiManager
    lateinit var adapter: WifiAdapter
    private lateinit var dialog: AlertDialog

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            resultList = wifiManager.scanResults as ArrayList<ScanResult>
//            val handler = Handler()
//            handler.postDelayed({
//                stopScanning()
//            }, 5000)
            dialog.dismiss()

            displayScannedWifi()
            if(resultList.count() == 0)
            {
                Toast.makeText(activity, "No device(s) found, Please make sure that your Wifi and GPS are turned on." , Toast.LENGTH_LONG).show()
            }
            else
            {
                Toast.makeText(activity, resultList.count().toString() + "device(s) found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun delayedDialogDismiss(){
        val handler = Handler()
        handler.postDelayed({
            dialog.dismiss()
        }, 10000)
    }

    private fun displayScannedWifi() {
        val wifiUtils = WifiUtils()
        currentConnectedSSID = wifiUtils.findCurrentConnectedSSID()

        WifiUtils.wifiList.clear()
        //var sameSSIDCount = 1
        for(item in resultList)
        {
            if(item.SSID.contains("LAGO"))
            {
                var newWifiItem = WifiItem()
                val found = WifiUtils.wifiList.filter { it.name == item.SSID }
                if(found.count() != 0)
                {
//                    break
                }
                else
                {
                    newWifiItem.name = item.SSID

                    newWifiItem.level = WifiManager.calculateSignalLevel(item.level, 5)
                    if("\"" + newWifiItem.name + "\""  == currentConnectedSSID) {
                        if(Protocol.cDeviceProt != null)
                        {
                            Protocol.cDeviceProt!!.stopChannel()
                            WifiUtils.isConnectedToBL = false
                        }

                        var dProtocol = DeviceProtocol()
                        try {
                            Protocol.cDeviceProt = dProtocol
                            Protocol.cDeviceProt!!.startChannel()
                        }catch (e: Exception)
                        {

                        }

                        var dataSetCollection: MutableList<DataSetItem> = mutableListOf()

                        var dataHold:DataSetItem

                        val date = Date() // given date

                        val calendar =
                            GregorianCalendar.getInstance() // creates a new calendar instance

                        calendar.time = date // assigns calendar to given date

                        val tdYearStr = calendar[Calendar.YEAR].toString()
                        val tdYearInt = tdYearStr.substring(1..3).toInt()
                        val tdMonth = calendar[Calendar.MONTH] // 0 based
                        val tdDay = calendar[Calendar.DAY_OF_MONTH]
                        val tdHour = calendar[Calendar.HOUR_OF_DAY]
                        val tdMinute = calendar[Calendar.MINUTE]
                        val tdSecond = calendar[Calendar.SECOND]

//                    dataHold = DataSetItem()
//                    dataHold.myCommand = 0x04
//                    dataHold.myDatas = byteArrayOf(
//                        tdYearInt.toByte(),
//                        tdMonth.plus(1).toByte(),
//                        tdDay.toByte(),
//                        tdHour.toByte(),
//                        tdMinute.toByte(),
//                        tdSecond.toByte()
//                    )
//                    dataSetCollection.add(dataHold)

//                    dataHold = DataSetItem()
//                    dataHold.myCommand = 0x01
//                    dataHold.myDatas = byteArrayOf(
//                        0x80.toByte(),
//                        0x80.toByte(),
//                        0xff.toByte()
//                    )
//                    dataSetCollection.add(dataHold)

//                    Protocol.cDeviceProt!!.upload(dataSetCollection)

                        var dtDataArray = byteArrayOf(
                            tdYearInt.toByte(),
                            tdMonth.plus(1).toByte(),
                            tdDay.toByte(),
                            tdHour.toByte(),
                            tdMinute.toByte(),
                            tdSecond.toByte()
                        )

                        try {
                            Protocol.cDeviceProt!!.transferData(0x04.toByte(), dtDataArray)
                        }catch (e: Exception)
                        {

                        }

//                    val data = byteArrayOf(
//                        0x80.toByte(),
//                        0x80.toByte(),
//                        0xff.toByte()
//                    )
//                    Protocol.cDeviceProt!!.transferDataWithDelay(0x01, data)
                        newWifiItem.status = 2
                        newWifiItem.selected = true
                    }
                    else
                    {
                        newWifiItem.status = 0
                        newWifiItem.selected = false
                    }
                    newWifiItem.capabilities = item.capabilities

                    WifiUtils.wifiList.add(newWifiItem)
                }
                ///
            }
        }

        WifiUtils.wifiList.sortByDescending { it.level }
        adapter.notifyDataSetChanged()
    }

    fun startScanning() {
        activity!!.registerReceiver(broadcastReceiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
        wifiManager.startScan()
    }

    fun stopScanning() {
        activity!!.unregisterReceiver(broadcastReceiver)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog = SpotsDialog(activity, R.style.Searching)
        wifiManager = activity!!.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        WifiUtils.sharedWifiManager = wifiManager

        if(!wifiManager!!.isWifiEnabled)
        {
            wifiManager!!.isWifiEnabled = true
            Toast.makeText(activity, "Wifi Enabled", Toast.LENGTH_SHORT).show()
        }

        return inflater.inflate(R.layout.fragment_landing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbm = DBmanager(activity!!)
        WifiUtils.isConnectedToBL = false
        LanguageTranslate()
        lst_wifi.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        lst_wifi.layoutManager = layoutManager

        navController = Navigation.findNavController(view)

        adapter = WifiAdapter(activity, WifiUtils.wifiList)
        WifiUtils.sharedWifiAdapter = adapter
        lst_wifi.adapter = adapter
        startScanning()
        dialog.show()
        delayedDialogDismiss()

        btn_scan.setOnClickListener{
            startScanning()
            dialog.show()
            delayedDialogDismiss()
 //           retrieveFromDatabase()
//            var postDelayedSendToModule = Handler()
//            var sendToModule = Runnable {
//                dialog.dismiss()
//            }
//            postDelayedSendToModule.postDelayed(sendToModule, 10000)
        }
    }

    fun startProtocol(SSID: String){
        for(item in WifiUtils.wifiList){
            if(item.name == currentConnectedSSID)
            {
                item.status = 2
                item.selected = true
                WifiUtils.sharedWifiAdapter!!.notifyDataSetChanged()

                val dProtocol = DeviceProtocol()
                Protocol.cDeviceProt = dProtocol
                Protocol.cDeviceProt!!.startChannel()
                return
            }
        }
    }
    fun retrieveFromDatabase(){
          var getDatabase = dbm.allSaved.filter { it.name == Protocol.currentSSID }
          var getStepDatabase = dbm.allStep.filter { it.pgm_name == Protocol.currentSSID }
          var getSchedDatabase = dbm.allSched.filter { it.pgmname == Protocol.currentSSID }
            if(getDatabase.isNotEmpty() && getStepDatabase.isNotEmpty() && getSchedDatabase.isNotEmpty()){
                for(get in getDatabase){
                    val getPgm = PgmItem()
                    getPgm.command = get.command
                    getPgm.pgm = get.pgm
                    getPgm.save = get.save
                    getPgm.timestamp = get.timestamp
                    getPgm.name = get.name
                    getPgm.pgm_id = get.pgm_id
                    PgmCollection.pgmCollection.add(getPgm)
                }
                for(getstep in getStepDatabase){
                    val newStep = StepItem()
                    newStep.command = getstep.command
                    newStep.pgm = getstep.pgm
                    newStep.step = getstep.step
                    newStep.pan = getstep.pan
                    newStep.tilt = getstep.tilt
                    newStep.blink = getstep.blink
                    newStep.time = getstep.time
                    newStep.pgm_name = getstep.pgm_name
                    newStep.step_id = getstep.step_id
                    StepCollection.stepCollection.add(getstep)
                }
                for(getsched in getSchedDatabase){
                    val newsched = ScheduleItem()
                    newsched.command = getsched.command
                    newsched.pgm = getsched.pgm
                    newsched.shour = getsched.shour
                    newsched.sminute = getsched.sminute
                    newsched.ehour = getsched.ehour
                    newsched.eminute = getsched.eminute
                    newsched.sched = getsched.sched
                    newsched.wday = getsched.wday
                    newsched.smonth = getsched.smonth
                    newsched.sday = getsched.sday
                    newsched.emonth = getsched.emonth
                    newsched.eday = getsched.eday
                    newsched.pgmname = getsched.pgmname
                    newsched.sched_id = getsched.sched_id
                    ScheduleCollection.scheduleCollection.add(newsched)

                    val newDaymanager = DayManager()
                    newDaymanager.pgm = getsched.pgm
                    newDaymanager.sMonth = getsched.smonth.toString()
                    newDaymanager.sDay = getsched.sday.toString()
                    newDaymanager.eMonth = getsched.emonth.toString()
                    newDaymanager.eDay = getsched.eday.toString()
                    DayCollection.dayCollection.add(newDaymanager)
                }
            }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopScanning()
    }

    //    private fun generateItem() {
//
//        var item1 = WifiItem()
//        item1.name = "waw"
//        item1.status = 0
//        item1.level = 3
//        wifiList.add(item1)
//
//        var item2 = WifiItem()
//        item2.name = "wew"
//        item2.status = 2
//        item2.level = 4
//        wifiList.add(item2)
//    }
    fun LanguageTranslate(){
        if (Language.Lang == "Chinese"){
            lblProgramList.text = "扫描设备"
            btn_scan.text = "刷新"
        }
    }
}
