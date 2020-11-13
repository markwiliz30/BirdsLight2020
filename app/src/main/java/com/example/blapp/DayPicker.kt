package com.example.blapp


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.CurrentId.extensions.CurrentID
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.fragment.PrimeDatePickerBottomSheet
import com.example.blapp.adapter.TimeAdapter
import com.example.blapp.collection.DayCollection
import com.example.blapp.collection.ScheduleCollection
import com.example.blapp.common.DayState
import com.example.blapp.common.Protocol
import com.example.blapp.model.DayManager
import com.example.blapp.model.ScheduleItem
import kotlinx.android.synthetic.main.fragment_day_picker.*
import kotlinx.android.synthetic.main.fragment_info.*
import kotlinx.android.synthetic.main.fragment_info.view.*

/**
 * A simple [Fragment] subclass.
 */
class DayPicker : Fragment(), PrimeDatePickerBottomSheet.OnDayPickedListener {

    lateinit var navController: NavController
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: TimeAdapter

    lateinit var sMonth: String
    lateinit var sDay: String
    lateinit var eMonth: String
    lateinit var eDay: String
    var collection: DayManager? = null
    var Disabled: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        CurrentID.parentPgmIndex = arguments!!.getInt("parentPgmIndex")
        return inflater.inflate(R.layout.fragment_day_picker, container, false)
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

        if(DayState.firstboot){
            InfoPopup()
        }

       var filtered = DayCollection.dayCollection.filter { it.pgm!!.toInt() == CurrentID.parentPgmIndex }
        collection = filtered.find { it.pgm!!.toInt() == CurrentID.parentPgmIndex }

        if(collection!!.sMonth == "" && collection!!.sDay == "" && collection!!.eMonth == "" && collection!!.eDay == ""){
            Disabled = true
        }else{
            sMonth = collection!!.sMonth.toString()
            sDay = collection!!.sDay.toString()
            eMonth = collection!!.eMonth.toString()
            eDay = collection!!.eDay.toString()
            btn_set_calender.text = "$sMonth/$sDay ~ $eMonth/$eDay"
        }


        btnMonday.setOnClickListener{
            if(Disabled){
                Toast.makeText(activity, "Please Set A Date Range" , Toast.LENGTH_SHORT).show()
            }else{
//                collection!!.monday =! collection!!.monday
//                ShowSaveAlert(1 ,collection!!.monday)
                ShowTimeSchedule(1)
            }
        }

//        btnMonday.setOnLongClickListener{
//            if(Disabled){
//                Toast.makeText(activity, "Please Set A Date Range" , Toast.LENGTH_SHORT).show()
//                true
//            }else{
//            collection!!.monday =! collection!!.monday
//            BorderOrganize(1)
//            true
//            }
//        }

        btnTuesday.setOnClickListener{
            if(Disabled){
                Toast.makeText(activity, "Please Set A Date Range" , Toast.LENGTH_SHORT).show()
            }else {
//                collection!!.tuesday = !collection!!.tuesday
//                ShowSaveAlert(2, collection!!.tuesday)
                ShowTimeSchedule(2)
            }
        }
//        btnTuesday.setOnLongClickListener{
//            if(Disabled){
//                Toast.makeText(activity, "Please Set A Date Range" , Toast.LENGTH_SHORT).show()
//                true
//            }else {
//                collection!!.tuesday = !collection!!.tuesday
//                BorderOrganize(2)
//                true
//            }
//        }

        btnWednesday.setOnClickListener{
            if(Disabled){
                Toast.makeText(activity, "Please Set A Date Range" , Toast.LENGTH_SHORT).show()
            }else {
//                collection!!.wednesday = !collection!!.wednesday
//                ShowSaveAlert(3, collection!!.wednesday)
                ShowTimeSchedule(3)
            }
        }
//        btnWednesday.setOnLongClickListener{
//            if(Disabled){
//                Toast.makeText(activity, "Please Set A Date Range" , Toast.LENGTH_SHORT).show()
//                true
//            }else {
//                collection!!.wednesday = !collection!!.wednesday
//                BorderOrganize(3)
//                true
//            }
//        }
        btnThursday.setOnClickListener{
            if(Disabled){
                Toast.makeText(activity, "Please Set A Date Range" , Toast.LENGTH_SHORT).show()
            }else {
//                collection!!.thursday = !collection!!.thursday
//                ShowSaveAlert(4, collection!!.thursday)
                ShowTimeSchedule(4)
            }
        }
//        btnThursday.setOnLongClickListener{
//            if(Disabled){
//                Toast.makeText(activity, "Please Set A Date Range" , Toast.LENGTH_SHORT).show()
//                true
//            }else {
//                collection!!.thursday = !collection!!.thursday
//                BorderOrganize(4)
//                true
//            }
//        }
        btnFriday.setOnClickListener{
            if(Disabled){
                Toast.makeText(activity, "Please Set A Date Range" , Toast.LENGTH_SHORT).show()
            }else {
//                collection!!.friday = !collection!!.friday
//                ShowSaveAlert(5, collection!!.friday)
                ShowTimeSchedule(5)
            }
        }
//        btnFriday.setOnLongClickListener{
//            if(Disabled){
//                Toast.makeText(activity, "Please Set A Date Range" , Toast.LENGTH_SHORT).show()
//                true
//            }else {
//                collection!!.friday = !collection!!.friday
//                BorderOrganize(5)
//                true
//            }
//        }
        btnSaturday.setOnClickListener{
            if(Disabled){
                Toast.makeText(activity, "Please Set A Date Range" , Toast.LENGTH_SHORT).show()
            }else {
//                collection!!.saturday = !collection!!.saturday
//                ShowSaveAlert(6, collection!!.saturday)
                ShowTimeSchedule(5)
            }
        }

//        btnSaturday.setOnLongClickListener{
//            if(Disabled){
//                Toast.makeText(activity, "Please Set A Date Range" , Toast.LENGTH_SHORT).show()
//                true
//            }else {
//                collection!!.saturday = !collection!!.saturday
//                BorderOrganize(6)
//                true
//            }
//        }
        btnSunday.setOnClickListener{
            if(Disabled){
                Toast.makeText(activity, "Please Set A Date Range" , Toast.LENGTH_SHORT).show()
            }else {
//                collection!!.sunday = !collection!!.sunday
//                ShowSaveAlert(7, collection!!.sunday)
                ShowTimeSchedule(7)
            }
        }

//        btnSunday.setOnLongClickListener{
//            if(Disabled){
//                Toast.makeText(activity, "Please Set A Date Range" , Toast.LENGTH_SHORT).show()
//                true
//            }else {
//                collection!!.sunday = !collection!!.sunday
//                BorderOrganize(7)
//                true
//            }
//        }
        btnAll.setOnClickListener{
            if(Disabled){
                Toast.makeText(activity , "Please Set A Date Range" , Toast.LENGTH_SHORT).show()
            }else{
//                collection!!.alldays = !collection!!.alldays
//                ShowSaveAlert(8 , collection!!.alldays)
                ShowTimeSchedule(8)
            }

        }

//        btnAll.setOnLongClickListener{
//            if(Disabled){
//                Toast.makeText(activity, "Please Set A Date Range" , Toast.LENGTH_SHORT).show()
//                true
//            }else {
//                collection!!.alldays = !collection!!.alldays
//                BorderOrganize(8)
//                true
//            }
//        }

        for(i in 1..8){
            initialOrganize(i)
        }


        btn_set_calender.setOnClickListener{
           CreateCalander()
        }

//        btn_info.setOnClickListener{
//            InfoPopup()
//        }

        btn_next_page.setOnClickListener{
            if(collection!!.monday || collection!!.tuesday || collection!!.wednesday || collection!!.thursday || collection!!.friday || collection!!.saturday || collection!!.sunday || collection!!.alldays){
                val bundle = bundleOf("parentPgmIndex" to  CurrentID.parentPgmIndex)
                navController.navigate(R.id.action_dayPicker_to_setStepFragment, bundle)
                CurrentID.UpdateID(num = 6)
                CurrentID.Updatebool(x = true)

            }else {
                if(Disabled){
                    Toast.makeText(activity, "Please Set A Date Range" , Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(activity, "Please Set atleast one day" , Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    fun SelectAllDays(){
        btnMonday.setBackgroundResource(R.drawable.bottom_border)
        collection!!.monday = true
        btnTuesday.setBackgroundResource(R.drawable.bottom_border)
        collection!!.tuesday = true
        btnWednesday.setBackgroundResource(R.drawable.bottom_border)
        collection!!.wednesday = true
        btnThursday.setBackgroundResource(R.drawable.bottom_border)
        collection!!.thursday =true
        btnFriday.setBackgroundResource(R.drawable.bottom_border)
        collection!!.friday = true
        btnSaturday.setBackgroundResource(R.drawable.bottom_border)
        collection!!.saturday = true
        btnSunday.setBackgroundResource(R.drawable.bottom_border)
        collection!!.sunday = true
        btnAll.setBackgroundResource(R.drawable.bottom_border)
    }

    fun DeselectAllDays(){
        btnMonday.setBackgroundResource(R.drawable.button_model)
        btnTuesday.setBackgroundResource(R.drawable.button_model)
        btnWednesday.setBackgroundResource(R.drawable.button_model)
        btnThursday.setBackgroundResource(R.drawable.button_model)
        btnFriday.setBackgroundResource(R.drawable.button_model)
        btnSaturday.setBackgroundResource(R.drawable.button_model)
        btnSunday.setBackgroundResource(R.drawable.button_model)
        btnAll.setBackgroundResource(R.drawable.button_model)
        collection!!.alldays = false
    }

    fun ShowTimeSchedule(day: Int) {
        when (day) {
            1 -> {
                val bundle = bundleOf("parentPgmIndex" to  CurrentID.parentPgmIndex , "days" to day)
                navController.navigate(R.id.action_dayPicker_to_timeSchedule, bundle)
                CurrentID.UpdateID(num = 7)
                CurrentID.Updatebool(x = true)

            }
            2 -> {
                val bundle = bundleOf("parentPgmIndex" to  CurrentID.parentPgmIndex , "days" to day)
                navController.navigate(R.id.action_dayPicker_to_timeSchedule, bundle)
                CurrentID.UpdateID(num = 7)
                CurrentID.Updatebool(x = true)

            }
            3 -> {
                val bundle = bundleOf("parentPgmIndex" to  CurrentID.parentPgmIndex , "days" to day)
                navController.navigate(R.id.action_dayPicker_to_timeSchedule, bundle)
                CurrentID.UpdateID(num = 7)
                CurrentID.Updatebool(x = true)

            }
            4 -> {
                val bundle = bundleOf("parentPgmIndex" to  CurrentID.parentPgmIndex , "days" to day)
                navController.navigate(R.id.action_dayPicker_to_timeSchedule, bundle)
                CurrentID.UpdateID(num = 7)
                CurrentID.Updatebool(x = true)

            }
            5 -> {
                val bundle = bundleOf("parentPgmIndex" to  CurrentID.parentPgmIndex , "days" to day)
                navController.navigate(R.id.action_dayPicker_to_timeSchedule, bundle)
                CurrentID.UpdateID(num = 7)
                CurrentID.Updatebool(x = true)

            }
            6 -> {
                val bundle = bundleOf("parentPgmIndex" to  CurrentID.parentPgmIndex , "days" to day)
                navController.navigate(R.id.action_dayPicker_to_timeSchedule, bundle)
                CurrentID.UpdateID(num = 7)
                CurrentID.Updatebool(x = true)

            }
            7 -> {
                val bundle = bundleOf("parentPgmIndex" to  CurrentID.parentPgmIndex , "days" to day)
                navController.navigate(R.id.action_dayPicker_to_timeSchedule, bundle)
                CurrentID.UpdateID(num = 7)
                CurrentID.Updatebool(x = true)

            }

            8 -> {
               val bundle = bundleOf("parentPgmIndex" to CurrentID.parentPgmIndex , "days" to day)
                navController.navigate(R.id.action_dayPicker_to_timeSchedule, bundle)
                CurrentID.UpdateID(num = 7)
                CurrentID.Updatebool(x = true)
            }
        }
    }

    fun onCancelChangeStatus(day: Int){
        when (day){
            1->{
                collection!!.monday =! collection!!.monday
            }
            2->{
                collection!!.tuesday =! collection!!.tuesday
            }
            3->{
                collection!!.wednesday =! collection!!.wednesday
            }
            4->{
                collection!!.thursday =! collection!!.thursday
            }
            5->{
                collection!!.friday =! collection!!.friday
            }
            6->{
                collection!!.saturday =! collection!!.saturday
            }
            7->{
                collection!!.sunday =! collection!!.sunday
            }
            8->{
                collection!!.alldays =! collection!!.alldays
            }
        }
    }



    fun initialOrganize(day: Int){
        var schedcollection = ScheduleCollection.scheduleCollection.filter { it.pgm!!.toInt() == CurrentID.parentPgmIndex && it.wday!!.toInt() == day }
        when (day){
            1->{
            if(schedcollection.isNotEmpty()){
                btnMonday.setBackgroundResource(R.drawable.bottom_border)
                collection!!.monday = true
                }else{
                btnMonday.setBackgroundResource(R.drawable.button_model)
                collection!!.monday = false
                }
            }
            2->{
                if(schedcollection.isNotEmpty()){
                    btnTuesday.setBackgroundResource(R.drawable.bottom_border)
                    collection!!.tuesday = true
                }else{
                    btnTuesday.setBackgroundResource(R.drawable.button_model)
                    collection!!.tuesday = false
                }
            }
            3->{
                if(schedcollection.isNotEmpty()){
                    btnWednesday.setBackgroundResource(R.drawable.bottom_border)
                    collection!!.wednesday = true
                }else{
                    btnWednesday.setBackgroundResource(R.drawable.button_model)
                    collection!!.wednesday = false
                }
            }
            4->{
                if(schedcollection.isNotEmpty()){
                    btnThursday.setBackgroundResource(R.drawable.bottom_border)
                    collection!!.thursday = true
                }else{
                    btnThursday.setBackgroundResource(R.drawable.button_model)
                    collection!!.thursday = false
                }
            }
            5->{
                if(schedcollection.isNotEmpty()){
                    btnFriday.setBackgroundResource(R.drawable.bottom_border)
                    collection!!.friday = true
                }else{
                    btnFriday.setBackgroundResource(R.drawable.button_model)
                    collection!!.friday = false
                }
            }
            6->{
                if(schedcollection.isNotEmpty()){
                    btnSaturday.setBackgroundResource(R.drawable.bottom_border)
                    collection!!.saturday = true
                }else{
                    btnSaturday.setBackgroundResource(R.drawable.button_model)
                    collection!!.saturday = false
                }
            }
            7->{
                if(schedcollection.isNotEmpty()){
                    btnSunday.setBackgroundResource(R.drawable.bottom_border)
                    collection!!.sunday = true
                }else{
                    btnSunday.setBackgroundResource(R.drawable.button_model)
                    collection!!.sunday = false
                }
            }
            8-> {
                if(schedcollection.isNotEmpty()){
                    SelectAllDays()
                    collection!!.alldays = true
                }else{
                    collection!!.alldays = false
                    if(!collection!!.monday){
                        btnMonday.setBackgroundResource(R.drawable.button_model)
                    }
                    else if(!collection!!.tuesday){
                        btnTuesday.setBackgroundResource(R.drawable.button_model)
                    }
                    else if(!collection!!.wednesday){
                        btnWednesday.setBackgroundResource(R.drawable.button_model)
                    }
                    else if(!collection!!.thursday){
                        btnThursday.setBackgroundResource(R.drawable.button_model)
                    }
                    else if(!collection!!.friday){
                        btnFriday.setBackgroundResource(R.drawable.button_model)
                    }
                    else if(!collection!!.saturday){
                        btnSaturday.setBackgroundResource(R.drawable.button_model)
                    }
                    else if(!collection!!.sunday){
                        btnSunday.setBackgroundResource(R.drawable.button_model)
                    }
                }
            }
        }

    }

    fun BorderOrganize(day: Int ){
        when (day){
            1->{
                if(collection!!.monday){
                    btnMonday.setBackgroundResource(R.drawable.bottom_border)
                }else{
                    btnMonday.setBackgroundResource(R.drawable.button_model)
                    btnAll.setBackgroundResource(R.drawable.button_model)

                }
            }
            2->{
                if(collection!!.tuesday){
                    btnTuesday.setBackgroundResource(R.drawable.bottom_border)
                }else{
                    btnTuesday.setBackgroundResource(R.drawable.button_model)
                    btnAll.setBackgroundResource(R.drawable.button_model)

                }
            }
            3->{
                if(collection!!.wednesday){
                    btnWednesday.setBackgroundResource(R.drawable.bottom_border)
                }else{
                    btnWednesday.setBackgroundResource(R.drawable.button_model)
                    btnAll.setBackgroundResource(R.drawable.button_model)

                }
            }
            4->{
                if(collection!!.thursday){
                    btnThursday.setBackgroundResource(R.drawable.bottom_border)
                }else{
                    btnThursday.setBackgroundResource(R.drawable.button_model)
                    btnAll.setBackgroundResource(R.drawable.button_model)

                }
            }
            5->{
                if(collection!!.friday){
                    btnFriday.setBackgroundResource(R.drawable.bottom_border)
                }else{
                    btnFriday.setBackgroundResource(R.drawable.button_model)
                    btnAll.setBackgroundResource(R.drawable.button_model)

                }
            }
            6->{
                if(collection!!.saturday){
                    btnSaturday.setBackgroundResource(R.drawable.bottom_border)
                }else{
                    btnSaturday.setBackgroundResource(R.drawable.button_model)
                    btnAll.setBackgroundResource(R.drawable.button_model)

                }
            }
            7->{
                if(collection!!.sunday){
                    btnSunday.setBackgroundResource(R.drawable.bottom_border)
                }else{
                    btnSunday.setBackgroundResource(R.drawable.button_model)
                    btnAll.setBackgroundResource(R.drawable.button_model)

                }
            }
            8->{
                if(collection!!.alldays){
                    SelectAllDays()
                }else{
                    DeselectAllDays()
                }
            }
        }

    }
    override fun onMultipleDaysPicked(multipleDays: List<PrimeCalendar>) {

    }

    override fun onRangeDaysPicked(startDay: PrimeCalendar, endDay: PrimeCalendar) {
        Disabled = false
        collection!!.sMonth = startDay.month.plus(1).toString()
        collection!!.sDay = startDay.dayOfMonth.toString()
        collection!!.eMonth = endDay.month.plus(1).toString()
        collection!!.eDay = endDay.dayOfMonth.toString()

        btn_set_calender.text = startDay.month.plus(1).toString()+"/"+startDay.dayOfMonth+" ~ "+endDay.month.plus(1)+"/"+endDay.dayOfMonth


     //   Toast.makeText(activity , collection!!.sMonth+collection!!.sDay+collection!!.eMonth+collection!!.eDay , Toast.LENGTH_SHORT).show()
    }

    override fun onSingleDayPicked(singleDay: PrimeCalendar) {

    }

    fun ShowSaveAlert(day: Int , Status: Boolean){

        var dayState = "Enable"
        val mAlertDialog = AlertDialog.Builder(activity!!)
        mAlertDialog.setIcon(R.mipmap.ic_launcher_round)
        mAlertDialog.setTitle("Choose Option")

        when(Status)
        {
            true->{

                dayState = "Enable"
            }
            false->{
                dayState = "Disable"
            }
        }

        mAlertDialog.setPositiveButton(dayState) { dialog, id ->
            var checker = ScheduleCollection.scheduleCollection.filter {it.pgm!!.toInt() == CurrentID.parentPgmIndex && it.wday!!.toInt() == day }

            if(checker.isEmpty()){
                Toast.makeText(activity, "No Time Set" , Toast.LENGTH_SHORT).show()
               // collection!!.alldays =! collection!!.alldays
                ShowSaveAlert(day, Status)
            }else{
                BorderOrganize(day)
            }
        }

        mAlertDialog.setNegativeButton("Manage Schedule") { dialog, id ->
            onCancelChangeStatus(day)
            ShowTimeSchedule(day)
        }

        mAlertDialog.setOnCancelListener { onCancelChangeStatus(day) }
        mAlertDialog.show()
    }

//    fun ShowSaveAlert_2(day: Int , Status: Boolean){
//
//        var dayState = "Enable"
//
//        when(Status)
//        {
//            true->{
//
//                dayState = "Enable"
//            }
//            false->{
//                dayState = "Disable"
//            }
//        }
//
//            var checker = ScheduleCollection.scheduleCollection.filter {it.pgm!!.toInt() == parentPgmIndex && it.wday!!.toInt() == day
//
//            if(checker.isEmpty()){
//                Toast.makeText(activity, "No Time Set" , Toast.LENGTH_SHORT).show()
//                // collection!!.alldays =! collection!!.alldays
//                ShowSaveAlert(day, Status)
//            }else{
//                BorderOrganize(day)
//            }
//        }
//
//        mAlertDialog.setNegativeButton("Manage Schedule") { dialog, id ->
//            onCancelChangeStatus(day)
//            ShowTimeSchedule(day)
//        }
//
//        mAlertDialog.setOnCancelListener { onCancelChangeStatus(day) }
//        mAlertDialog.show()
//    }
    fun InfoPopup(){
    val infoAlert = AlertDialog.Builder(activity!!).create()
    val InfoView = layoutInflater.inflate(R.layout.fragment_info, null)

    InfoView.lblInfoMessage.text = "Set date first before clicking any buttons in Day Picker panel."
    infoAlert.setView(InfoView)
    infoAlert.show()
    InfoView.btn_got_it.setOnClickListener{
        if( DayState.firstboot){
            DayState.firstboot=false
            }
        infoAlert.dismiss()
        }
    }

    fun CreateCalander(){
        var datePicker: PrimeDatePickerBottomSheet?

        val calendarType = CalendarType.CIVIL

        val pickType = PickType.RANGE_START

        val minDateCalendar= null

        val maxDateCalendar = null

        val typeface = null

        val today = CalendarFactory.newInstance(calendarType)


        datePicker = PrimeDatePickerBottomSheet.newInstance(
            currentDateCalendar = today,
            minDateCalendar = minDateCalendar,
            maxDateCalendar = maxDateCalendar,
            pickType = pickType,
            typefacePath = typeface
        )
        datePicker?.setOnDateSetListener(this)
        datePicker?.show(activity!!.supportFragmentManager, "PrimeDatePickerBottomSheet")
    }
}
