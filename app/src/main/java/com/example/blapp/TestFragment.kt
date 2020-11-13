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
import com.example.blapp.common.DeviceProtocol
import com.example.blapp.common.Protocol
import com.example.blapp.model.DataSetItem
import kotlinx.android.synthetic.main.fragment_set_step.*
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
        var command: Byte = 0x02
        var data: ByteArray

        tglPgm1.setOnClickListener{

            if(ButtonStatus){
                //test code
//                Protocol.cDeviceProt = DeviceProtocol()
//                Protocol.cDeviceProt!!.startChannel()

                data = byteArrayOf(
                    0x01.toByte(),
                    0x01.toByte()
                )
                Protocol.cDeviceProt!!.transferData(0x11.toByte(), data)

//                data = byteArrayOf(
//                    0x01.toByte(),
//                    0x01.toByte(),
//                    255.toByte(),
//                    255.toByte(),
//                    255.toByte(),
//                    0x01.toByte()
//                )
//                Protocol.cDeviceProt!!.transferData(command, data)
                Toast.makeText(context, "Testing Program 1 Running", Toast.LENGTH_SHORT).show()
                tglPgm1.setBackgroundResource(R.drawable.bottom_border)
                lblProgramRunning.text = "Test Program 1 is running"
                ButtonStatus = false
            }else{
                Toast.makeText(context, "Disabled", Toast.LENGTH_SHORT).show()
            }

        }

        tglPgm2.setOnClickListener{
            if(ButtonStatus){
                data = byteArrayOf(
                    0x02.toByte(),
                    0x01.toByte()
                )
                Protocol.cDeviceProt!!.transferData(0x11, data)

//                data = byteArrayOf(
//                    0x01.toByte(),
//                    0x01.toByte(),
//                    12.toByte(),
//                    53.toByte(),
//                    22.toByte(),
//                    0x01.toByte()
//                )
//                Protocol.cDeviceProt!!.transferData(command, data)
                Toast.makeText(context, "Testing Program 2 Running", Toast.LENGTH_SHORT).show()
               tglPgm2.setBackgroundResource(R.drawable.bottom_border)
                ButtonStatus = false
            }else{
                Toast.makeText(context, "Disabled", Toast.LENGTH_SHORT).show()
            }
        }

        tglPgm3.setOnClickListener{
            if(ButtonStatus){
                //uncomment me
//                data = byteArrayOf(
//                    0x03.toByte(),
//                    0x01.toByte()
//                )
//                Protocol.cDeviceProt!!.transferData(0x11, data)

                //for testing
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


                var myData = byteArrayOf(
                    tdYearInt.toByte(),
                    tdMonth.plus(1).toByte(),
                    tdDay.toByte(),
                    tdHour.toByte(),
                    tdMinute.toByte(),
                    tdSecond.toByte()
                )

                Protocol.cDeviceProt!!.transferData(0x04, myData)
//                    data = byteArrayOf(
//                        0x01.toByte(),
//                        0x01.toByte(),
//                        128.toByte(),
//                        128.toByte(),
//                        128.toByte(),
//                        0x01.toByte()
//                    )
//                    Protocol.cDeviceProt!!.transferData(command, data)


                Toast.makeText(context, "Testing Program 3 Running", Toast.LENGTH_SHORT).show()
               tglPgm3.setBackgroundResource(R.drawable.bottom_border)
                ButtonStatus = false
            }else{
                Toast.makeText(context, "Disabled", Toast.LENGTH_SHORT).show()
            }
        }

        btnReset.setOnClickListener{
            var dataSetCollection: MutableList<DataSetItem> = mutableListOf()

            var dataHold = DataSetItem()

            dataHold.myCommand = 0x11
            dataHold.myDatas = byteArrayOf(
                0x01.toByte(),
                0x00.toByte()
            )
            dataSetCollection.add(dataHold)

            dataHold = DataSetItem()
            dataHold.myCommand = 0x11
            dataHold.myDatas = byteArrayOf(
                0x02.toByte(),
                0x00.toByte()
            )
            dataSetCollection.add(dataHold)

            dataHold = DataSetItem()
            dataHold.myCommand = 0x11
            dataHold.myDatas = byteArrayOf(
                0x03.toByte(),
                0x00.toByte()
            )
            dataSetCollection.add(dataHold)

            dataHold = DataSetItem()
            dataHold.myCommand = 0x01
            dataHold.myDatas = byteArrayOf(
                0x80.toByte(),
                0x80.toByte(),
                0xff.toByte()
            )
            dataSetCollection.add(dataHold)

            Protocol.cDeviceProt!!.upload(dataSetCollection)

            tglPgm1.setBackgroundResource(R.drawable.button_model)
            tglPgm2.setBackgroundResource(R.drawable.button_model)
            tglPgm3.setBackgroundResource(R.drawable.button_model)
            lblProgramRunning.text = "No test program is running"
            ButtonStatus = true
        }

        btn_test_inc.setOnClickListener{

            var curNum:Int = test_Number.text.toString().toInt()
            var newNum:Int = curNum+50
            if(newNum < 1000){
                test_Number.setText(newNum.toString())

            }else{
                Toast.makeText(context, "You've reached the maxed input", Toast.LENGTH_SHORT).show()
                test_Number.setText("1000")
            }
        }

        btn_test_dec.setOnClickListener{

            var curNum:Int = test_Number.text.toString().toInt()
            var newNum:Int = curNum-50
            if(newNum > 50){
                test_Number.setText(newNum.toString())
            }else{
                Toast.makeText(context, "You've reached the min input", Toast.LENGTH_SHORT).show()
                test_Number.setText("50")
            }
        }


    }
}
