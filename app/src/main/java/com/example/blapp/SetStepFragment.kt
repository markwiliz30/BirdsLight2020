package com.example.blapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.CurrentId.extensions.CurrentID
import com.example.blapp.collection.PgmCollection
import com.example.blapp.collection.StepCollection
import com.example.blapp.common.Protocol
import com.example.blapp.model.PgmItem
import com.example.blapp.model.StepItem
import kotlinx.android.synthetic.main.fragment_set_step.*
import com.example.blapp.collection.ScheduleCollection
import com.example.blapp.common.DeviceProtocol
import com.example.blapp.common.Language
import com.example.blapp.model.DataSetItem
import com.example.blapp.model.ScheduleItem
import java.lang.Exception

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
        val command: Byte = 0x01
        var data: ByteArray
        LanguageTranslate()

        val searchItem = PgmCollection.pgmCollection.find { it.pgm!!.toInt() == CurrentID.parentPgmIndex }

        if(searchItem != null){
            editClicked= true
            FindCurrentStepOnPgmCollection(CurrentID.parentPgmIndex)

        }

        if(editClicked){
            btn_step_save.text= "Update"
            data = byteArrayOf(
                pVal.toByte(),
                tVal.toByte(),
                bVal.toByte()
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
                    pVal.toByte(),
                    tVal.toByte(),
                    bVal.toByte()
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
                    pVal.toByte(),
                    tVal.toByte(),
                    bVal.toByte()
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
                    pVal.toByte(),
                    tVal.toByte(),
                    bVal.toByte()
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
            if(tmVal < 20)
            {
                tmVal++
                txt_step_time.setText(tmVal.toString())
            }
        }

        btn_dec_time.setOnClickListener{
            if(tmVal > 1)
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
                   txt_step_time.setText("1")
                    tmVal = 1
                }
                else if(txt_step_time.text.toString().toInt() > 20)
                {
                    txt_step_time.setText("20")
                    tmVal = 20
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

//            var dataSetCollection: MutableList<DataSetItem> = mutableListOf()
//
//            var dataHold: DataSetItem
//
//            for(item in StepCollection.stepCollection)
//            {
//                dataHold = DataSetItem()
//                dataHold.myCommand = item.command
//                dataHold.myDatas = byteArrayOf(
//                    item.pgm!!.toByte(),
//                    item.step!!.toByte(),
//                    item.pan!!.toByte(),
//                    item.tilt!!.toByte(),
//                    item.blink!!.toByte(),
//                    item.time!!.toByte()
//                )
//                dataSetCollection.add(dataHold)
//            }
//
//            for(item in PgmCollection.pgmCollection)
//            {
//                dataHold = DataSetItem()
//                dataHold.myCommand = item.command
//                dataHold.myDatas = byteArrayOf(
//                    item.pgm!!.toByte()
//                )
//                dataSetCollection.add(dataHold)
//            }
//
//            for(item in ScheduleCollection.scheduleCollection)
//            {
//                dataHold = DataSetItem()
//                dataHold.myCommand = item.command
//                dataHold.myDatas = byteArrayOf(
//                    item.pgm!!.toByte(),
//                    item.smonth!!.toByte(),
//                    item.sday!!.toByte(),
//                    item.emonth!!.toByte(),
//                    item.eday!!.toByte(),
//                    item.wday!!.toByte(),
//                    item.shour!!.toByte(),
//                    item.sminute!!.toByte(),
//                    item.ehour!!.toByte(),
//                    item.eminute!!.toByte()
//                )
//                dataSetCollection.add(dataHold)
//            }
//
//            Protocol.cDeviceProt!!.upload(dataSetCollection)

            UploadDataSets()

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

    private fun ConvertDecToBinVal(decVal: Byte?): Byte{
        var binVal = 0.toByte()
        if(decVal == 0x01.toByte())
        {
            binVal = 0x01.toByte()
        }else if(decVal == 0x02.toByte())
        {
            binVal = 0x02.toByte()
        }else if(decVal == 0x03.toByte())
        {
            binVal = 0x04.toByte()
        }else if(decVal == 0x04.toByte())
        {
            binVal = 0x08.toByte()
        }else if(decVal == 0x05.toByte())
        {
            binVal = 0x10.toByte()
        }else if(decVal == 0x06.toByte())
        {
            binVal = 0x20.toByte()
        }else if(decVal == 0x07.toByte())
        {
            binVal = 0x40.toByte()
        }else if(decVal == 0x08.toByte())
        {
            binVal = 0x80.toByte()
        }

        return binVal
    }

    private fun UploadDataSets()
    {
        val filteredSchedCollection = ScheduleCollection.scheduleCollection.filter{it.pgm == CurrentID.parentPgmIndex.toByte()} as MutableList
        val filteredStepCollection = StepCollection.stepCollection.filter{it.pgm == CurrentID.parentPgmIndex.toByte()}
        var groupFilteredSchedCollect: MutableList<ScheduleItem> = mutableListOf()

        for(i in 0 until filteredSchedCollection.count())
        {
            val currItem = filteredSchedCollection[0]
            var groupSched = filteredSchedCollection.filter { it.shour == currItem.shour && it.sminute == currItem.sminute && it.ehour == currItem.ehour && it.eminute == currItem.eminute }

            if(groupSched.count() > 1)
            {
                var newWday = 0
                for(i in groupSched)
                {
                    newWday += ConvertDecToBinVal(i.wday).toInt()
                }

                val item = ScheduleItem()
                item.command = currItem.command
                item.pgm = currItem.pgm
                item.smonth = currItem.smonth
                item.sday = currItem.sday
                item.emonth = currItem.emonth
                item.eday = currItem.eday
                item.wday = newWday.toByte()
                item.shour = currItem.shour
                item.sminute = currItem.sminute
                item.ehour = currItem.ehour
                item.eminute = currItem.eminute
                item.pgmname = currItem.pgmname
                item.sched = currItem.sched
                groupFilteredSchedCollect.add(item)
            }
            else
            {
                val item = ScheduleItem()
                item.command = currItem.command
                item.pgm = currItem.pgm
                item.smonth = currItem.smonth
                item.sday = currItem.sday
                item.emonth = currItem.emonth
                item.eday = currItem.eday
                item.wday = ConvertDecToBinVal(currItem.wday)
                item.shour = currItem.shour
                item.sminute = currItem.sminute
                item.ehour = currItem.ehour
                item.eminute = currItem.eminute
                item.pgmname = currItem.pgmname
                item.sched = currItem.sched
                groupFilteredSchedCollect.add(item)
            }

            for(grpItem in groupSched)
            {
                filteredSchedCollection.remove(grpItem)
            }

            if(filteredSchedCollection.count() == 0)
            {
                break
            }
        }

        var dataArray = byteArrayOf(
            groupFilteredSchedCollect[0].pgm!!.toByte(),
            groupFilteredSchedCollect[0].smonth!!.toByte(),
            groupFilteredSchedCollect[0].sday!!.toByte(),
            groupFilteredSchedCollect[0].emonth!!.toByte(),
            groupFilteredSchedCollect[0].eday!!.toByte(),
            groupFilteredSchedCollect.count().toByte()
        )

        for(lSchedItem in groupFilteredSchedCollect)
        {
            dataArray += lSchedItem.wday!!
            dataArray += lSchedItem.shour!!
            dataArray += lSchedItem.sminute!!
            dataArray += lSchedItem.ehour!!
            dataArray += lSchedItem.eminute!!
        }

        dataArray += filteredStepCollection.count().toByte()

        for(lStepItem in filteredStepCollection)
        {
            dataArray += lStepItem.pan!!
            dataArray += lStepItem.tilt!!
            dataArray += lStepItem.blink!!
            dataArray += lStepItem.time!!
        }

        var dataArray2 = byteArrayOf( 255.toByte())

        if(Protocol.cDeviceProt != null)
        {

            Protocol.cDeviceProt!!.transferData(0x06.toByte(), dataArray)
        }

//        for(lStepItem in filteredStepCollection)
//        {
//            for(lSchedItem in filteredSchedCollection)
//            {
//                dataHold = DataSetItem()
//                dataHold.myCommand = 0x06.toByte()
//                dataHold.myDatas = byteArrayOf(
//                    lStepItem.pgm!!.toByte(),
//                    lSchedItem.smonth!!.toByte(),
//                    lSchedItem.sday!!.toByte(),
//                    lSchedItem.emonth!!.toByte(),
//                    lSchedItem.eday!!.toByte(),
//                    lSchedItem.wday!!.toByte(),
//                    lSchedItem.shour!!.toByte(),
//                    lSchedItem.sminute!!.toByte(),
//                    lSchedItem.ehour!!.toByte(),
//                    lSchedItem.eminute!!.toByte(),
//                    lStepItem.step!!.toByte(),
//                    lStepItem.pan!!.toByte(),
//                    lStepItem.tilt!!.toByte(),
//                    lStepItem.blink!!.toByte(),
//                    lStepItem.time!!.toByte()
//                )
//
//                dataSetCollection.add(dataHold)
//            }
//        }

//        Protocol.cDeviceProt!!.upload(dataSetCollection)
    }

    private fun AddPgmToCollection(pgm: PgmItem, stepList: List<StepItem>)
    {
        for(item in stepList)
        {
            StepCollection.stepCollection!!.add(item)
        }

        PgmCollection.pgmCollection!!.add(pgm)
    }

    private fun UpdatePgmAtCollection(pgm: Int, stepList: List <StepItem>){

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
        val command: Byte = 0x01
        pVal = 128
        tVal = 128
        bVal = 0
        tmVal = 1

        data = byteArrayOf(
            pVal.toByte(),
            tVal.toByte(),
            bVal.toByte()
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
        newItem.command = 0x06
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

    fun LanguageTranslate(){
        if (Language.Lang == "Chinese"){
            lbl_New_Program.text = "新"
            lbl_Step.text = "步"
            btn_add_step.text = "添加步骤"
            lbl_Pan.text = "泛"
            lbl_Tilt.text = "倾斜"
            lbl_Blink.text ="眨"
            lbl_Time.text = "时间"
            btn_step_save.text = "救"
            btnDeleteStep.text = "删除"
        }
    }

}
