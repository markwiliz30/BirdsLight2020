package com.example.blapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentController
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.CurrentId.extensions.CurrentID
import com.bumptech.glide.load.model.ByteBufferEncoder
import com.example.blapp.collection.PgmCollection
import com.example.blapp.collection.StepCollection
import com.example.blapp.common.Protocol
import com.example.blapp.model.PgmItem
import com.example.blapp.model.StepItem
import kotlinx.android.synthetic.main.fragment_program.*
import kotlinx.android.synthetic.main.fragment_set_step.*
import com.example.blapp.ProgramFragment
import com.example.blapp.collection.ScheduleCollection
import com.example.blapp.model.DataSetItem
import kotlinx.android.synthetic.main.activity_landing.*

/**
 * A simple [Fragment] subclass.
 */
class SetStepFragment : Fragment() {

    lateinit var navController: NavController
//    var parentPgmIndex: Int = 0
    var stepIndex: Int = 0
    internal var pVal: Int = 0
    internal var tVal:Int = 0
    internal var bVal:Int = 0
    internal var tmVal: Int = 0
    internal var editClicked: Boolean = false
    internal var tempDelete: Int =0
    var tempStepList: MutableList<StepItem> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        CurrentID.parentPgmIndex = arguments!!.getInt("parentPgmIndex")

        stepIndex = 1
        return inflater.inflate(R.layout.fragment_set_step, container, false)
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
        val command: Byte = 0x02
        var data: ByteArray

        if(PgmCollection.pgmCollection.count() >= CurrentID.parentPgmIndex){
            editClicked= true
            FindCurrentStepOnPgmCollection(CurrentID.parentPgmIndex)

        }

        if(editClicked){
            btn_step_save.text= "Update"
            data = byteArrayOf(
                0x01.toByte(),
                0x01.toByte(),
                pVal.toByte(),
                tVal.toByte(),
                bVal.toByte(),
                0x01.toByte()
            )
            Protocol.cDeviceProt?.transferDataWithDelay(command, data)

        }else{
            btn_step_save.text="Save"
            ResetCurrentStep()
        }


        step_parent_pgm.text = CurrentID.parentPgmIndex.toString()
        txt_step_time.setText(tmVal.toString())
        txt_step_num.setText(stepIndex.toString())

        edit_pan_sb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                pVal = progress
                data = byteArrayOf(
                   0x01.toByte(),
                    0x01.toByte(),
                    pVal.toByte(),
                   tVal.toByte(),
                  bVal.toByte(),
                   0x01.toByte()
                )
              Protocol.cDeviceProt?.transferDataWithDelay(command, data)
                updateTextOnPan(pVal)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        edit_tilt_sb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tVal = progress
                data = byteArrayOf(
                    0x01.toByte(),
                    0x01.toByte(),
                    pVal.toByte(),
                    tVal.toByte(),
                    bVal.toByte(),
                    0x01.toByte()
                )
                Protocol.cDeviceProt?.transferDataWithDelay(command, data)
                updateTextOnTilt(tVal)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        edit_blink_sb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                bVal = progress
               data = byteArrayOf(
                    0x01.toByte(),
                    0x01.toByte(),
                    pVal.toByte(),
                    tVal.toByte(),
                    bVal.toByte(),
                    0x01.toByte()
                )
                Protocol.cDeviceProt?.transferDataWithDelay(command, data)
                update_step_blink.text = bVal.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        btn_inc_time.setOnClickListener{
            if(tmVal < 100)
            {
                tmVal++
                txt_step_time.setText(tmVal.toString())
            }
        }

        btn_dec_time.setOnClickListener{
            if(tmVal > 0)
            {
                tmVal--
                txt_step_time.setText(tmVal.toString())
            }
        }

        txt_step_time.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(txt_step_time.text.isEmpty())
                {
                   txt_step_time.setText("0")
                    tmVal = 0
                }
                else if(txt_step_time.text.toString().toInt() > 99)
                {
                    txt_step_time.setText("99")
                    tmVal = 99
                }
                else
                {
                    tmVal = txt_step_time.text.toString().toInt()
                }
            }
        })

        btn_inc_step.setOnClickListener{
            stepIndex++
            if((stepIndex-2) < tempStepList.count())
            {
                    AddStep(stepIndex -1)
                    SetCurrentStepValues(stepIndex)
                    txt_step_num.setText(stepIndex.toString())
            }
            else
            {
                stepIndex--
            }
        }

        btn_dec_step.setOnClickListener{
            stepIndex--
            if(stepIndex == 0){
                stepIndex = 1
            }
            else
            {
                AddStep(stepIndex + 1)
                SetCurrentStepValues(stepIndex)
                txt_step_num.setText(stepIndex.toString())
            }
        }
        btnDeleteStep.setOnClickListener{
            if(tempStepList.count() == 0){
                Toast.makeText(activity!!, "You can't delete", Toast.LENGTH_SHORT).show()
            }else{
                AdjustIndex(stepIndex)
                if(stepIndex == 1){
                    SetCurrentStepValues(stepIndex)
                    txt_step_num.setText(stepIndex.toString())
                }else{
                    stepIndex--
                    SetCurrentStepValues(stepIndex)
                    txt_step_num.setText(stepIndex.toString())
                }
            }
        }

        btn_add_step.setOnClickListener{
            AddStep(stepIndex)
            ResetCurrentStep()
            stepIndex = tempStepList.count() + 1
            txt_step_num.setText(stepIndex.toString())
        }

        btn_step_save.setOnClickListener{
            if(editClicked){
                AddStep(stepIndex)
                UpdatePgmAtCollection(CurrentID.parentPgmIndex, tempStepList)
                Toast.makeText(activity!!, "Update Success!", Toast.LENGTH_SHORT).show()
                editClicked = false
                navController.navigate(R.id.action_setStepFragment_to_programFragment)
                CurrentID.Updatebool(x = false)
                CurrentID.UpdateID(num = 3)

            }else{
                var createdPgm = PgmItem()
                val createdPgmCommand = 0x03
                createdPgm.command = createdPgmCommand.toByte()
                createdPgm.pgm = CurrentID.parentPgmIndex.toByte()
                AddStep(stepIndex)
                AddPgmToCollection(createdPgm, tempStepList)
                navController.navigate(R.id.action_setStepFragment_to_programFragment)
                CurrentID.Updatebool(x = false)
                CurrentID.UpdateID(num = 3)
            }

            var dataSetCollection: MutableList<DataSetItem> = mutableListOf()

            var dataHold = DataSetItem()

            for(item in StepCollection.stepCollection)
            {
                dataHold.myCommand = item.command
                dataHold.myDatas = byteArrayOf(
                    item.pgm!!.toByte(),
                    item.step!!.toByte(),
                    item.pan!!.toByte(),
                    item.tilt!!.toByte(),
                    item.blink!!.toByte(),
                    item.time!!.toByte()
                )
                dataSetCollection.add(dataHold)
            }

            for(item in PgmCollection.pgmCollection)
            {
                dataHold.myCommand = item.command
                dataHold.myDatas = byteArrayOf(
                    item.pgm!!.toByte()
                )
                dataSetCollection.add(dataHold)
            }

            for(item in ScheduleCollection.scheduleCollection)
            {
                dataHold.myCommand = item.command
                dataHold.myDatas = byteArrayOf(
                    item.pgm!!.toByte(),
                    item.smonth!!.toByte(),
                    item.sday!!.toByte(),
                    item.emonth!!.toByte(),
                    item.eday!!.toByte(),
                    item.wday!!.toByte(),
                    item.shour!!.toByte(),
                    item.sminute!!.toByte(),
                    item.ehour!!.toByte(),
                    item.eminute!!.toByte()
                )
                dataSetCollection.add(dataHold)
            }

            Protocol.cDeviceProt!!.upload(dataSetCollection)

//            data = byteArrayOf(
//                0x01.toByte(),
//                0x01.toByte(),
//                0.toByte(),
//                0.toByte(),
//                0.toByte(),
//                0x01.toByte()
//            )
            //undo moko
//            Protocol.cDeviceProt!!.transferData(command, data)

        }

    }

    private fun AddPgmToCollection(pgm: PgmItem, stepList: List<StepItem>)
    {
        for(item in stepList)
        {
            StepCollection.stepCollection!!.add(item)
        }

        PgmCollection.pgmCollection!!.add(pgm)
    }

    private fun UpdatePgmAtCollection(pgm: Int, stepList: List<StepItem>){

        do{
            var found = StepCollection.stepCollection.find{it.pgm == pgm.toByte()}
            StepCollection.stepCollection.remove(found)
        }while(found != null)

        for(item in stepList){
            StepCollection.stepCollection!!.add(item)
        }

    }

    private fun SetCurrentStepValues(index: Int)
    {
        val newCurrentitem = tempStepList.find { it.step == index.toByte() }
        tempStepList.remove(newCurrentitem)
        pVal = newCurrentitem!!.pan!!.toUByte().toInt()
        tVal = newCurrentitem!!.tilt!!.toUByte().toInt()
        bVal = newCurrentitem!!.blink!!.toUByte().toInt()
        tmVal = newCurrentitem!!.time!!.toUByte().toInt()

        edit_pan_sb.progress = pVal
        edit_tilt_sb.progress = tVal
        edit_blink_sb.progress = bVal

        updateTextOnTilt(tVal)
        updateTextOnPan(pVal)
        update_step_blink.text = bVal.toString()

        txt_step_time.setText(tmVal.toString())
    }

    private fun FindCurrentStepOnPgmCollection(index: Int){

            val newCurrentitems = StepCollection.stepCollection.filter { it.pgm == index.toByte()}
            for (item in newCurrentitems){
                tempStepList.add(item)
            }
        val newCurrentitem = tempStepList.find { it.step == stepIndex.toByte() }
        tempStepList.remove(newCurrentitem)


            pVal = newCurrentitem!!.pan!!.toUByte().toInt()
            tVal = newCurrentitem!!.tilt!!.toUByte().toInt()
            bVal = newCurrentitem!!.blink!!.toUByte().toInt()
            tmVal = newCurrentitem!!.time!!.toUByte().toInt()

        updateTextOnTilt(tVal)
        updateTextOnPan(pVal)
        update_step_blink.text = bVal.toString()

            edit_pan_sb.progress = pVal
            edit_tilt_sb.progress = tVal
            edit_blink_sb.progress = bVal
            txt_step_time.setText(tmVal.toString())
    }

    private fun ResetCurrentStep()
    {
        var data: ByteArray
        val command: Byte = 0x02
        pVal = 128
        tVal = 128
        bVal = 0
        tmVal = 0

        data = byteArrayOf(
                    0x01.toByte(),
                  0x01.toByte(),
                   pVal.toByte(),
                    tVal.toByte(),
                   bVal.toByte(),
                   0x01.toByte()
                )
        Protocol.cDeviceProt?.transferDataWithDelay(command, data)

        updateTextOnTilt(tVal)
        updateTextOnPan(pVal)
        update_step_blink.text = bVal.toString()

        edit_pan_sb.progress = pVal
        edit_tilt_sb.progress = tVal
        edit_blink_sb.progress = bVal
        txt_step_time.setText(tmVal.toString())
    }

    private fun AddStep(index: Int)
    {
        val newItem = StepItem()
        newItem.command = 0x02
        newItem.pgm = CurrentID.parentPgmIndex.toByte()
        newItem.step = index.toByte()
        newItem.pan = pVal.toByte()
        newItem.tilt = tVal.toByte()
        newItem.blink = bVal.toByte()
        newItem.time = tmVal.toByte()
        tempStepList.add(newItem)
    }

    private fun AdjustIndex(index: Int){
        var ToAdjust = tempStepList.filter { it.step!! > index.toByte() }
        for(adjust in ToAdjust){
            adjust.step = adjust.step!!.dec()
        }
    }
    private fun updateTextOnTilt(value: Int) {
        var passVal: Double = 0.0
        if(value == 255){
            update_step_tilt.text = "90"
        }
        else if(value == 128){
            update_step_tilt.text = "0"
        }
        else if(value >= 129){
            passVal = (value-128) * 0.703125
            update_step_tilt.text = ""+passVal.toInt().toString()
        }else if(value <= 127){
            passVal = (128-value) * 0.703125
            update_step_tilt.text = "-"+passVal.toInt()
        }

    }

    private fun updateTextOnPan(value: Int) {
        var passVal: Double = 0.0
        if(value == 255){
            update_step_pan.text = "270"
        }
        else if(value == 128){
            update_step_pan.text = "0"
        }
        else if(value >= 129){
            passVal = (value-128) * 2.109375
            update_step_pan.text = ""+passVal.toInt()
        }else if(value <= 127){
            passVal = (128-value) * 2.109375
            update_step_pan.text = "-"+passVal.toInt()
        }

    }

}
