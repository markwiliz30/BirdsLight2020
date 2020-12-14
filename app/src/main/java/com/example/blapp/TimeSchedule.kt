package com.example.blapp

import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.CurrentId.extensions.CurrentID
import com.example.blapp.adapter.TimeAdapter
import com.example.blapp.collection.DayCollection
import com.example.blapp.collection.ScheduleCollection
import com.example.blapp.common.DayState
import com.example.blapp.common.Language
import com.example.blapp.helper.MyButton
import com.example.blapp.helper.MySwipeHelper
import com.example.blapp.helper.MySwipeHelper2
import com.example.blapp.listener.MyButtonClickListener
import com.example.blapp.model.DayManager
import com.example.blapp.model.ScheduleItem
import kotlinx.android.synthetic.main.fragment_day_picker.*
import kotlinx.android.synthetic.main.fragment_test.*
import kotlinx.android.synthetic.main.fragment_time_list.*
import kotlinx.android.synthetic.main.fragment_time_schedule.*
import java.sql.Time
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class TimeSchedule : Fragment() {

    lateinit var navController: NavController
    private var day: Int = 0
    private var tempshour: Int = 25
    private var tempsminute: Int = 25
    private var tempehour: Int = 25
    private var tempeminute: Int = 25
    var conflicts: Boolean = true
    lateinit var adapter: TimeAdapter
    lateinit var layoutManager: LinearLayoutManager
    var collection: DayManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        CurrentID.parentPgmIndex = arguments!!.getInt("parentPgmIndex")
        day = arguments!!.getInt("days")
        return inflater.inflate(R.layout.fragment_time_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        LanguageTranslate()
        var filtered = DayCollection.dayCollection.filter { it.pgm!!.toInt() == CurrentID.parentPgmIndex }
        collection = filtered.find { it.pgm!!.toInt() == CurrentID.parentPgmIndex }

        lst_time.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        lst_time.layoutManager = layoutManager


        val mTimePickerStart: TimePickerDialog
        val mTimePickerEnd: TimePickerDialog
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)

        mTimePickerEnd = TimePickerDialog(activity, object : TimePickerDialog.OnTimeSetListener{
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                time_end.text= "To: "+hourOfDay.toString()+":"+minute.toString()
                tempehour = hourOfDay
                tempeminute = minute

                
                    if(tempshour == tempehour && tempeminute == tempsminute){
                        btn_save_time.startAnimation(AnimationUtils.loadAnimation(activity , R.anim.shake))
                        Toast.makeText(activity, "Invalid Time Set" , Toast.LENGTH_LONG).show()
                        time_end.text= ""
                        tempehour = 25
                        tempeminute = 25
                    }
                    else{
                        if(!timeConflict()){
                            btn_save_time.startAnimation(AnimationUtils.loadAnimation(activity , R.anim.shake))
                            Toast.makeText(activity, "Time has Conflict" , Toast.LENGTH_LONG).show()
                            time_end.text= ""
                            tempehour = 25
                            tempeminute = 25
//                }else if(!DateConflict()){
//                    btn_save_time.startAnimation(AnimationUtils.loadAnimation(activity , R.anim.shake))
//                    Toast.makeText(activity, "Date has Conflict" , Toast.LENGTH_LONG).show()
                        }else{
                            //if(day == 8){
                            //  collection!!.alldays = true
                            //  addAllDaysCollection()
                            //  }else{
                            //ChangeDayStatus(day)
                            // addToTimeCollection()

                            // }
                        }

                    }
                }
        }, hour , minute , false)


        mTimePickerStart = TimePickerDialog(activity, object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                time_start.text = "From: "+hourOfDay.toString()+":"+minute.toString()
                tempshour = hourOfDay
                tempsminute = minute

                btn_End_Time.isEnabled = true
            }

        }, hour, minute, false)

        btn_Start_Time.setOnClickListener{
            mTimePickerStart.show()}

        btn_End_Time.setOnClickListener{
            mTimePickerEnd.show()
        }
        btn_save_time.setOnClickListener{
            if(tempehour == 25 && tempeminute == 25 && tempshour == 25 && tempsminute == 25){
                btn_save_time.startAnimation(AnimationUtils.loadAnimation(activity , R.anim.shake))
                Toast.makeText(activity, "No time set" , Toast.LENGTH_LONG).show()
                time_end.text= ""
                tempehour = 25
                tempeminute = 25
            }else if (tempeminute == 25 && tempehour == 25){
                btn_save_time.startAnimation(AnimationUtils.loadAnimation(activity , R.anim.shake))
                Toast.makeText(activity, "End Time is not Set" , Toast.LENGTH_LONG).show()
                time_end.text= ""
                tempehour = 25
                tempeminute = 25
            }else{
                if(getFilteredSchedCount() >= 11)
                {
                    Toast.makeText(context, "You can only have 10 unique time schedule", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    if(day == 8){

                        collection!!.alldays = true
                        addAllDaysCollection()
                    }else{
                        ChangeDayStatus(day)
                        addToTimeCollection()
                    }

                    refreshList()
                    returnToInitial()
                    DayState.ScheduleComplete = true
                    Toast.makeText(activity, "Save Success" , Toast.LENGTH_LONG).show()
                }
            }
        }
        generateitem()
    }

    private fun getFilteredSchedCount(): Int
    {
        val newItem = ScheduleItem()
        var filtered =  ScheduleCollection.scheduleCollection.filter { it.pgm!!.toInt() == CurrentID.parentPgmIndex && it.wday!!.toInt() == day }
        var DateFind = DayCollection.dayCollection.find { it.pgm!!.toInt() == CurrentID.parentPgmIndex }
        val schedNumber = filtered.count()+1
        newItem.command = 0x04
        newItem.pgm = CurrentID.parentPgmIndex.toByte()
        newItem.shour = tempshour.toByte()
        newItem.sminute = tempsminute.toByte()
        newItem.ehour = tempehour.toByte()
        newItem.eminute = tempeminute.toByte()
        newItem.sched = schedNumber.toByte()
        newItem.wday = day.toByte()
        newItem.smonth = DateFind!!.sMonth!!.toByte()
        newItem.sday = DateFind!!.sDay!!.toByte()
        newItem.emonth= DateFind!!.eMonth!!.toByte()
        newItem.eday = DateFind!!.eDay!!.toByte()

        val filteredSchedCollection = ScheduleCollection.scheduleCollection.filter{it.pgm == CurrentID.parentPgmIndex.toByte()} as MutableList
        filteredSchedCollection.add(newItem)

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
        return groupFilteredSchedCollect.count()
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

    private fun generateitem(){
        adapter = TimeAdapter(activity,ScheduleCollection.scheduleCollection.filter { it.pgm!!.toInt() == CurrentID.parentPgmIndex && it.wday!!.toInt() == day } as MutableList<ScheduleItem>)
        lst_time.adapter = adapter
    }

    fun refreshList(){
        adapter.itemList.clear()
        generateitem()
    }

    private fun addToTimeCollection(){
        val newItem = ScheduleItem()
        var filtered =  ScheduleCollection.scheduleCollection.filter { it.pgm!!.toInt() == CurrentID.parentPgmIndex && it.wday!!.toInt() == day }
        var DateFind = DayCollection.dayCollection.find { it.pgm!!.toInt() == CurrentID.parentPgmIndex }
        val schedNumber = filtered.count()+1
        newItem.command = 0x04
        newItem.pgm = CurrentID.parentPgmIndex.toByte()
        newItem.shour = tempshour.toByte()
        newItem.sminute = tempsminute.toByte()
        newItem.ehour = tempehour.toByte()
        newItem.eminute = tempeminute.toByte()
        newItem.sched = schedNumber.toByte()
        newItem.wday = day.toByte()
        newItem.smonth = DateFind!!.sMonth!!.toByte()
        newItem.sday = DateFind!!.sDay!!.toByte()
        newItem.emonth= DateFind!!.eMonth!!.toByte()
        newItem.eday = DateFind!!.eDay!!.toByte()
        ScheduleCollection.scheduleCollection.add(newItem)
    }

    private fun returnToInitial(){
        time_start.text = "Set Time"
        time_end.text = "Set Time"
        btn_End_Time.isEnabled = false
        tempehour = 25
        tempeminute = 25
        tempshour = 25
        tempsminute = 25
    }

//    private fun deleteTime(pos: Int){
//        var filtered =  ScheduleCollection.scheduleCollection.filter { it.pgm!!.toInt() == parentPgmIndex && it.wday!!.toInt() == day }
//        var Del = filtered.find { it.sched!!.toInt() == pos }
//
//        ScheduleCollection.scheduleCollection.remove(Del)
//
//
//        for(update in ScheduleCollection.scheduleCollection.filter { it.pgm!!.toInt() == parentPgmIndex && it.wday!!.toInt() == day }){
//            if(update.sched!!.toInt() > Del!!.sched!!.toInt()){
//                update.sched = update.sched!!.dec()
//            }
//        }
//    }

    fun ShowDeleteAlert(schd: Int){

        val mAlertDialog = AlertDialog.Builder(activity!!)
        mAlertDialog.setIcon(R.mipmap.ic_launcher_round)
        mAlertDialog.setTitle("Are you sure?")
        mAlertDialog.setMessage("Do you want to delete this schedule " + (schd + 1) + "?")

        mAlertDialog.setPositiveButton("Yes") { dialog, id ->

        }

        mAlertDialog.setNegativeButton("No") { dialog, id ->

        }

        mAlertDialog.show()
    }

    fun timeConflict():Boolean{
        var tempStime: Int = 0
        var tempEtime: Int = 0
        var TimeRestric = ScheduleCollection.scheduleCollection.filter {it.wday!!.toInt() == day }
        conflicts = true
       // var TimeRestric = ScheduleCollection.scheduleCollection.filter { it.pgm!!.toInt() == CurrentID.parentPgmIndex && it.wday!!.toInt() == day }
        if(day == 8) {
            TimeRestric = ScheduleCollection.scheduleCollection.filter {  it.pgm!!.toInt() == CurrentID.parentPgmIndex}
        }


        tempStime = if(tempsminute < 10){
            (""+tempshour+"0"+tempsminute+"").toInt()
        }else{
            (""+tempshour+tempsminute+"").toInt()
        }

        tempEtime = if(tempeminute < 10){
            (""+tempehour+"0"+tempeminute+"").toInt()
        }else{
            (""+tempehour+tempeminute+"").toInt()
        }

        if(tempStime > tempEtime){
            conflicts = false
        }

        for (time in TimeRestric){
            var tstart2 = if(time.sminute!!.toInt() < 10){
                (""+time.shour+"0"+time.sminute+"").toInt()
            }else{
                (""+time.shour+time.sminute+"").toInt()
            }
            var tend2 = if(time.eminute!!.toInt() < 10){
                (""+time.ehour+"0"+time.eminute+"").toInt()
            }else{
                (""+time.ehour+time.eminute+"").toInt()
            }

          for(x in tstart2..tend2){
                    if(tempStime == x || tempEtime == x){
                        //if(!DateConflict()){
                            conflicts = false

                        //}
                    }
                }
            }
        return conflicts
    }

    fun ChangeDayStatus(wday: Int){
        when(wday){
            1 -> {
                collection!!.monday = true
            }
            2 -> {
                collection!!.tuesday = true
            }
            3 -> {
                collection!!.wednesday = true
            }
            4 -> {
                collection!!.thursday = true
            }
            5 -> {
                collection!!.friday = true
            }
            6 -> {
                collection!!.saturday = true
            }
            7 -> {
                collection!!.sunday = true
            }
        }
    }

    private fun  addAllDaysCollection(){

        var DateFind = DayCollection.dayCollection.find { it.pgm!!.toInt() == CurrentID.parentPgmIndex }

       for(x in 1..8){
           val newItem = ScheduleItem()
           var timeCount = ScheduleCollection.scheduleCollection.filter { it.pgm!!.toInt() == CurrentID.parentPgmIndex && it.wday!!.toInt() == x }
           newItem.command = 0x02
           newItem.pgm = CurrentID.parentPgmIndex.toByte()
           newItem.shour = tempshour.toByte()
           newItem.sminute = tempsminute.toByte()
           newItem.ehour = tempehour.toByte()
           newItem.eminute = tempeminute.toByte()
           newItem.sched = timeCount.count().toByte().inc()
           newItem.wday = x.toByte()
           newItem.smonth = DateFind!!.sMonth!!.toByte()
           newItem.sday = DateFind!!.sDay!!.toByte()
           newItem.emonth= DateFind!!.eMonth!!.toByte()
           newItem.eday = DateFind!!.eDay!!.toByte()
           ScheduleCollection.scheduleCollection.add(newItem)
       }
    }

    fun DateConflict():Boolean{
        var DateConflicts = true
        var CurSDate =""+ collection!!.sMonth + collection!!.sDay +""
        var CurEDate = ""+ collection!!.eMonth + collection!!.eDay+""
        var DateFilter = ScheduleCollection.scheduleCollection.filter { it.pgm!!.toInt() != CurrentID.parentPgmIndex }

        if(!DateFilter.isEmpty()){
            for(DateCon in DateFilter){
            var difSDate = DateCon.smonth!!.toString() + DateCon.sday!!.toString()
            var difEDate = DateCon.emonth!!.toString() + DateCon.eday!!.toString()

                if(CurSDate.toInt() >= difSDate.toInt() && CurSDate.toInt() >= difEDate.toInt()){
                    if(CurEDate.toInt() >= difSDate.toInt() && CurEDate.toInt() <= difEDate.toInt()){
                        DateConflicts = false
                        break
                    }else{
                        DateConflicts = true
                        break
                    }
                }else{
                    DateConflicts = false
                }
            }
        }

        return DateConflicts
    }

    fun LanguageTranslate(){
        if (Language.Lang == "Chinese"){
            lblTime.text = "时间表"
            time_start.text = "设置时间"
            time_end.text = "设置时间"
            btn_Start_Time.text = "开始"
            btn_End_Time.text = "结束"
            btn_save_time.text ="救"
        }
    }
}
