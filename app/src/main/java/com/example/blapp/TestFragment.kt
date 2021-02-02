package com.example.blapp


import android.content.Intent
import android.os.Bundle
import android.service.autofill.Dataset
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.blapp.common.*
import com.example.blapp.model.DataSetItem
import kotlinx.android.synthetic.main.fragment_set_step.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_test.*
import java.util.*
import kotlin.math.round

class TestFragment : Fragment() {

    lateinit var navController: NavController
    internal var pVal: Int = 0
    internal var tVal:Int = 0
    internal var bVal:Int = 0
    internal var ButtonStatus:Boolean = true

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
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        var data: ByteArray
        LanguageTranslate()
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
                }else{
                    data = byteArrayOf(
                        0x01.toByte(),
                        0x00.toByte()
                    )
                    Protocol.cDeviceProt!!.transferData(0x11.toByte(),data)
                    TestRunning.TestPgm1 = false
                    Toast.makeText(context, "Stopping Program 1", Toast.LENGTH_SHORT).show()
                    tglPgm1.setBackgroundResource(R.drawable.button_model)
                    lblProgramRunning.text = "No test program is running"
                    ButtonStatus = true
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
                    0xff.toByte())

                Protocol.cDeviceProt!!.transferData(0x01.toByte(),data)

                Toast.makeText(context, "Homing position", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, "Stop other test program before setting to home position", Toast.LENGTH_SHORT).show()
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

        btnRecTest.setOnClickListener{
            var dRec = byteArrayOf(
                0x01)
            Protocol.cDeviceProt!!.transferData(0x16.toByte(), dRec)
        }

    }
    fun LanguageTranslate(){
        if (Language.Lang == "Chinese"){
            lblProgram.text = "选择程序"
            lblProgramRunning.text = "没有测试程序正在运行"
            btnReset.text = "重启"
        }
    }
}
