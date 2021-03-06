package com.example.blapp


import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.CurrentId.extensions.CurrentID
import com.example.blapp.adapter.LogAdapter
import com.example.blapp.collection.*
import com.example.blapp.common.*
import com.example.blapp.databasehelper.DBmanager
import kotlinx.android.synthetic.main.fragment_program.*
import kotlinx.android.synthetic.main.fragment_set_step.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_test.*
import kotlinx.android.synthetic.main.layout_loading_dialog.*
import kotlinx.android.synthetic.main.layout_loading_dialog.view.*
import me.itangqi.waveloadingview.WaveLoadingView
import java.util.*


class TestFragment : Fragment() {

    lateinit var navController: NavController
    internal var pVal: Int = 0
    internal var tVal:Int = 0
    internal var bVal:Int = 0
    internal var ButtonStatus:Boolean = true
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: LogAdapter
    var currentProg = 0
    internal lateinit var dbm: DBmanager
    private lateinit var loadingAlert: AlertDialog
    private lateinit var loadingView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_test, container, false)

    }

    override fun onDetach() {
        super.onDetach()
        val command: Byte = 0x01
        val data = byteArrayOf(
            0.toByte(),
            0.toByte(),
            0.toByte()
        )
        Protocol.cDeviceProt!!.transferData(command, data)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dbm = DBmanager(activity!!)
        super.onViewCreated(view, savedInstanceState)
        loadingAlert = AlertDialog.Builder(activity!!, R.style.CustomDialog).create()
        loadingAlert.setCancelable(false)
        loadingView = layoutInflater.inflate(R.layout.layout_loading_dialog, null)
        LogCollection.logCollection.clear()
        layoutManager = LinearLayoutManager(activity)
        log_rec.layoutManager = layoutManager

        navController = Navigation.findNavController(view)
        var data: ByteArray
        LanguageTranslate()

        val displaylabel = Runnable {
            //if (canAccess)
//            lblVersion.text = TestTransferRateVal.verVal
            // use arrayadapter and define an array
            // access the listView from xml file

//            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,listOf("car", "plane")) as ListAdapter
//            logView.adapter = adapter

            getItems()
        }

        tglPgm1.setOnClickListener{

            if(ButtonStatus || TestRunning.TestPgm1){
                if(!TestRunning.TestPgm1){
                    data = byteArrayOf(
                        0x01.toByte(),
                        0x01.toByte()
                    )
                    Protocol.cDeviceProt!!.transferData(0x11.toByte(), data)
                    TestRunning.TestPgm1 = true
                    Toast.makeText(context, "Testing Program 1 Running", Toast.LENGTH_SHORT).show()
                    tglPgm1.setBackgroundResource(R.drawable.bottom_border)
                    lblProgramRunning.text = "Test Program 1 is running"
                    ButtonStatus = false

                    val postDdisplay = Handler()
                    postDdisplay.postDelayed(displaylabel, 800)
                }else{
                    data = byteArrayOf(
                        0x01.toByte(),
                        0x00.toByte()
                    )
                    Protocol.cDeviceProt!!.transferData(0x11.toByte(), data)
                    TestRunning.TestPgm1 = false
                    Toast.makeText(context, "Stopping Program 1", Toast.LENGTH_SHORT).show()
                    tglPgm1.setBackgroundResource(R.drawable.button_model)
                    lblProgramRunning.text = "No test program is running"
                    ButtonStatus = true

                    val postDdisplay = Handler()
                    postDdisplay.postDelayed(displaylabel, 800)
                }
                //test code
//                Protocol.cDeviceProt = DeviceProtocol()
//                Protocol.cDeviceProt!!.startChannel()


               // Protocol.cDeviceProt!!.transferData(0x11.toByte(), data)

//                data = byteArrayOf(
//                    0x01.toByte(),
//                    0x01.toByte(),
//                    255.toByte(),
//                    255.toByte(),
//                    255.toByte(),
//                    0x01.toByte()
//                )
//                Protocol.cDeviceProt!!.transferData(command, data)

            }else{
                Toast.makeText(context, "Other Test Program is running", Toast.LENGTH_SHORT).show()
            }

        }

        tglPgm2.setOnClickListener{
            if(ButtonStatus || TestRunning.TestPgm2){
                if(!TestRunning.TestPgm2){
                    data = byteArrayOf(
                        0x02.toByte(),
                        0x01.toByte()
                    )
                    Protocol.cDeviceProt!!.transferData(0x11.toByte(), data)
                    TestRunning.TestPgm2 = true
                    Toast.makeText(context, "Testing Program 2 Running", Toast.LENGTH_SHORT).show()
                    tglPgm2.setBackgroundResource(R.drawable.bottom_border)
                    lblProgramRunning.text = "Test Program 2 is running"
                    ButtonStatus = false

                    val postDdisplay = Handler()
                    postDdisplay.postDelayed(displaylabel, 800)
                }else{
                    data = byteArrayOf(
                        0x02.toByte(),
                        0x00.toByte()
                    )
                    Protocol.cDeviceProt!!.transferData(0x11.toByte(), data)
                    TestRunning.TestPgm2 = false
                    Toast.makeText(context, "Stopping Program 2", Toast.LENGTH_SHORT).show()
                    tglPgm2.setBackgroundResource(R.drawable.button_model)
                    lblProgramRunning.text = "No test program is running"
                    ButtonStatus = true

                    val postDdisplay = Handler()
                    postDdisplay.postDelayed(displaylabel, 800)
                }

                //Protocol.cDeviceProt!!.transferData(0x11, data)

//                data = byteArrayOf(
//                    0x01.toByte(),
//                    0x01.toByte(),
//                    12.toByte(),
//                    53.toByte(),
//                    22.toByte(),
//                    0x01.toByte()
//                )
//                Protocol.cDeviceProt!!.transferData(command, data)
            }else{
                Toast.makeText(context, "Other Test Program is running", Toast.LENGTH_SHORT).show()
            }
        }

        tglPgm3.setOnClickListener{
            if(ButtonStatus || TestRunning.TestPgm3){
                if(!TestRunning.TestPgm3){
                    data = byteArrayOf(
                        0x03.toByte(),
                        0x01.toByte()
                    )
                   Protocol.cDeviceProt!!.transferData(0x11.toByte(), data)
                    TestRunning.TestPgm3 = true
                    Toast.makeText(context, "Testing Program 3 Running", Toast.LENGTH_SHORT).show()
                    tglPgm3.setBackgroundResource(R.drawable.bottom_border)
                    lblProgramRunning.text = "Test Program 3 is running"
                    ButtonStatus = false

                    val postDdisplay = Handler()
                    postDdisplay.postDelayed(displaylabel, 800)
                }else{
                    data = byteArrayOf(
                        0x03.toByte(),
                        0x00.toByte()
                    )
                    Protocol.cDeviceProt!!.transferData(0x11.toByte(), data)
                    TestRunning.TestPgm3 = false
                    Toast.makeText(context, "Stopping Program 3", Toast.LENGTH_SHORT).show()
                    tglPgm3.setBackgroundResource(R.drawable.button_model)
                    lblProgramRunning.text = "No test program is running"
                    ButtonStatus = true

                    val postDdisplay = Handler()
                    postDdisplay.postDelayed(displaylabel, 800)
                }
                //uncomment me
                //Protocol.cDeviceProt!!.transferData(0x11, data)

                //for testing
//                val date = Date() // given date
//
//                val calendar =
//                    GregorianCalendar.getInstance() // creates a new calendar instance
//
//                calendar.time = date // assigns calendar to given date
//
//                val tdYearStr = calendar[Calendar.YEAR].toString()
//                val tdYearInt = tdYearStr.substring(1..3).toInt()
//                val tdMonth = calendar[Calendar.MONTH] // 0 based
//                val tdDay = calendar[Calendar.DAY_OF_MONTH]
//                val tdHour = calendar[Calendar.HOUR_OF_DAY]
//                val tdMinute = calendar[Calendar.MINUTE]
//                val tdSecond = calendar[Calendar.SECOND]
//
//
//                var myData = byteArrayOf(
//                    tdYearInt.toByte(),
//                    tdMonth.plus(1).toByte(),
//                    tdDay.toByte(),
//                    tdHour.toByte(),
//                    tdMinute.toByte(),
//                    tdSecond.toByte()
//                )
//
//                Protocol.cDeviceProt!!.transferData(0x04.toByte(), myData)

//                    data = byteArrayOf(
//                        0x01.toByte(),
//                        0x01.toByte(),
//                        128.toByte(),
//                        128.toByte(),
//                        128.toByte(),
//                        0x01.toByte()
//                    )
//                    Protocol.cDeviceProt!!.transferData(command, data)


            }else{
                Toast.makeText(context, "Other Test Program is running", Toast.LENGTH_SHORT).show()
            }
        }

        btnReset.setOnClickListener{
            if(ButtonStatus){
                data = byteArrayOf(
                    0x80.toByte(),
                    0x80.toByte(),
                    0xff.toByte()
                )

                Protocol.cDeviceProt!!.transferData(0x01.toByte(), data)

                val postDdisplay = Handler()
                postDdisplay.postDelayed(displaylabel, 800)

                Toast.makeText(context, "Homing position", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(
                    context,
                    "Stop other test program before setting to home position",
                    Toast.LENGTH_SHORT
                ).show()
            }
//            var dataSetCollection: MutableList<DataSetItem> = mutableListOf()
//
//            var dataHold = DataSetItem()
//
//            dataHold.myCommand = 0x11
//            dataHold.myDatas = byteArrayOf(
//                0x01.toByte(),
//                0x00.toByte()
//            )
//            dataSetCollection.add(dataHold)
//
//            dataHold = DataSetItem()
//            dataHold.myCommand = 0x11
//            dataHold.myDatas = byteArrayOf(
//                0x02.toByte(),
//                0x00.toByte()
//            )
//            dataSetCollection.add(dataHold)
//
//            dataHold = DataSetItem()
//            dataHold.myCommand = 0x11
//            dataHold.myDatas = byteArrayOf(
//                0x03.toByte(),
//                0x00.toByte()
//            )
//            dataSetCollection.add(dataHold)
//
//            dataHold = DataSetItem()
//            dataHold.myCommand = 0x01
//            dataHold.myDatas = byteArrayOf(
//                0x80.toByte(),
//                0x80.toByte(),
//                0xff.toByte()
//            )
//            dataSetCollection.add(dataHold)

//            tglPgm1.setBackgroundResource(R.drawable.button_model)
//            tglPgm2.setBackgroundResource(R.drawable.button_model)
//            tglPgm3.setBackgroundResource(R.drawable.button_model)
//            TestRunning.TestPgm1 = false
//            TestRunning.TestPgm2 = false
//            TestRunning.TestPgm3 = false
//            lblProgramRunning.text = "No test program is running"
//            ButtonStatus = true
        }

        btnRet.setOnClickListener{
            retrieveWarning()

//            val postDdisplay = Handler()
//            postDdisplay.postDelayed(displaylabel, 800)
        }

        btnSoftUpdate.setOnClickListener {
            var dUpdate = byteArrayOf(
                0x01
            )
            Protocol.cDeviceProt!!.transferData(0x20.toByte(), dUpdate)

            val postDdisplay = Handler()
            postDdisplay.postDelayed(displaylabel, 800)
        }

        btnVerCheck.setOnClickListener {
            var dVCheck = byteArrayOf(
                0x01
            )
            Protocol.cDeviceProt!!.transferData(0x21.toByte(), dVCheck)

//            var testData = byteArrayOf(0x65, 0x66, 0x67)
//            TestTransferRateVal.verVal = String(testData)

            val postDdisplay = Handler()
            postDdisplay.postDelayed(displaylabel, 800)
        }

        btnClearLog.setOnClickListener{
            LogCollection.logCollection.clear()

            val postDdisplay = Handler()
            postDdisplay.postDelayed(displaylabel, 800)
        }
    }

    private fun retrieveWarning(){
        val mAlertDialog = AlertDialog.Builder(activity!!)
        mAlertDialog.setIcon(R.mipmap.ic_launcher_round) //set alertdialog icon
        mAlertDialog.setTitle("Are you sure?") //set alertdialog title
        mAlertDialog.setMessage("Retrieving data from the moving head will clear all your created programs. Do you want to continue?" ) //set alertdialog message
        mAlertDialog.setPositiveButton("Yes") { dialog, id ->

            dbm.deletePgm(Protocol.currentSSID.toString())
            dbm.deleteStep(Protocol.currentSSID.toString())
            dbm.deleteSchedule(Protocol.currentSSID.toString())

            DayCollection.dayCollection.clear()
            PgmCollection.pgmCollection.clear()
            ScheduleCollection.scheduleCollection.clear()
            StepCollection.stepCollection.clear()


            loadingView.loading_dialog.progressValue = 0
            LoadingPopup()
            GlobalVars.hasProg = true
            currentProg++
            getProgs(currentProg, 0x16.toByte())

        }
        mAlertDialog.setNegativeButton("No") { dialog, id ->

        }
        mAlertDialog.show()
    }

    fun LoadingPopup(){
        loadingAlert.setView(loadingView)
        loadingAlert.show()
    }
    private fun addToDatabaseSave(){
        for ( i in 1..PgmCollection.pgmCollection.count()){
            var pgmSave = PgmCollection.pgmCollection.find { it.pgm == i.toByte() }


            pgmSave!!.name = Protocol.currentSSID.toString()
            var stepSave = StepCollection.stepCollection.filter { it.pgm == i.toByte() }
            for(steps in stepSave){
                steps.pgm_name = Protocol.currentSSID.toString()
                dbm.addStep(steps)
            }
            var schedSave = ScheduleCollection.scheduleCollection.filter {it.pgm == i.toByte()}
            for(sched in schedSave){
                sched.pgmname = Protocol.currentSSID.toString()
                dbm.addSchedule(sched)
            }
            pgmSave.save = 0
            val date = Date() // given date

            val calendar =
                GregorianCalendar.getInstance() // creates a new calendar instance

            calendar.time = date // assigns calendar to given date

            val tdYearStr = calendar[Calendar.YEAR].toString()
            val tdMonth = calendar[Calendar.MONTH].toString() // 0 based
            val tdDay = calendar[Calendar.DAY_OF_MONTH].toString()
            val tdHour = calendar[Calendar.HOUR_OF_DAY].toString()
            val tdMinute = calendar[Calendar.MINUTE].toString()
            val tdSecond = calendar[Calendar.SECOND].toString()

            pgmSave.timestamp = ""+tdMonth+tdDay+tdYearStr+tdHour+":"+tdMinute+":"+tdSecond+""
            pgmSave.pgm = i.toByte()
            dbm.addPgm(pgmSave)

        }


    }


    private fun getProgs(data: Int, command: Byte){
        GlobalVars.willRetreive = true
        var dRec = byteArrayOf(
            data.toByte()
        )
        Protocol.cDeviceProt!!.transferData(command, dRec)

        if(currentProg >= 24)
        {
            currentProg = 0
            GlobalVars.hasProg = false
        }

        if(GlobalVars.hasProg)
        {
            currentProg++
            var firstNum: Double = currentProg.toDouble()
            var secondNum: Double = 24.toDouble()
            var perProg: Double = (firstNum/secondNum) * 100
            loadingView.loading_dialog.progressValue = perProg.toInt()
            loadingView.loading_dialog.centerTitle = perProg.toInt().toString() + "%"
            val getNext = Handler()
            getNext.postDelayed(getNextProg, 800)
        }else
        {
            currentProg = 0
            loadingView.loading_dialog.progressValue = 100
            loadingView.loading_dialog.centerTitle = 100.toString() + "%"
            val alertRemove = Handler()
            alertRemove.postDelayed(dismissAlert, 500)
        }
    }

    val dismissAlert = Runnable {
        addToDatabaseSave()
        loadingAlert.dismiss()
        Toast.makeText(context, "retrieved", Toast.LENGTH_SHORT).show()
    }

    private val getNextProg = Runnable {
        getProgs(currentProg, 0x16.toByte())
    }

    private fun getItems() {
//        val a = PgmCollection.pgmCollection.sortedWith(compareBy({ it.pgm }))
//        PgmCollection.pgmCollection = a as MutableList<PgmItem>
        adapter = LogAdapter(activity, LogCollection.logCollection)
        log_rec.adapter = adapter
        log_rec.scrollToPosition(LogCollection.logCollection.size - 1)
    }

    fun LanguageTranslate(){
        if (Language.Lang == "Chinese"){
            lblProgram.text = "选择程序"
            lblProgramRunning.text = "没有测试程序正在运行"
            btnReset.text = "重启"
        }
    }
}

