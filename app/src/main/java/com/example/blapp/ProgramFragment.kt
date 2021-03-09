package com.example.blapp


import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.CurrentId.extensions.CurrentID
import com.example.blapp.adapter.PgmAdapter
import com.example.blapp.collection.DayCollection
import com.example.blapp.collection.PgmCollection
import com.example.blapp.collection.ScheduleCollection
import com.example.blapp.collection.StepCollection
import com.example.blapp.common.DayState
import com.example.blapp.common.DeviceProtocol
import com.example.blapp.common.Language
import com.example.blapp.common.Protocol
import com.example.blapp.databasehelper.DBmanager
import com.example.blapp.helper.MyButton
import com.example.blapp.helper.MySwipeHelper
import com.example.blapp.listener.MyButtonClickListener
import com.example.blapp.model.DayManager
import com.example.blapp.model.PgmItem
import com.example.blapp.model.StepItem
import kotlinx.android.synthetic.main.fragment_info.view.*
import kotlinx.android.synthetic.main.fragment_input_dialog.*
import kotlinx.android.synthetic.main.fragment_input_dialog.view.*
import kotlinx.android.synthetic.main.fragment_program.*
import kotlinx.android.synthetic.main.fragment_set_step.*
import java.util.*
import kotlin.collections.ArrayList


class ProgramFragment : Fragment(){

    lateinit var navController: NavController
    lateinit var layoutManager: LinearLayoutManager
    var parentPgmIndex: Int = 0
    lateinit var adapter: PgmAdapter
    var collection: DayManager? = null
   // internal lateinit var dbStep:DBmanager
    internal lateinit var dbm:DBmanager
    internal var lstStep: List<StepItem> = ArrayList<StepItem>()
    internal var lstPgm: List<PgmItem> = ArrayList<PgmItem>()
    var postDelayedSendToModule = Handler()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_program, container, false)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dbm = DBmanager(activity!!)

        recycler_pgm.setHasFixedSize(true)
        recycler_pgm.setItemViewCacheSize(25)
        layoutManager = LinearLayoutManager(activity)
        recycler_pgm.layoutManager = layoutManager
        DayState.editPressed = false
       // ResetBirdsLight()
        //Add Swipe
        val swipe = object: MySwipeHelper(activity, recycler_pgm, 200)
        {
            override fun instantiateMyButton(
                viewHolder: RecyclerView.ViewHolder,
                buffer: MutableList<MyButton>
            ) {
                buffer.add(
                    MyButton(activity,
                        "Delete",
                        30,
                        R.drawable.ic_delete_dark_blue_24dp,
                        Color.parseColor("#14BED1"),
                        object : MyButtonClickListener{
                            override fun onClick(pos: Int) {
                                DeleteAlert(pos)


                            }
                        }
                    )
                )

                buffer.add(
                    MyButton(activity,
                        "Update",
                        30,
                        R.drawable.ic_edit_dark_blue_24dp,
                        Color.parseColor("#14BED1"),
                        object : MyButtonClickListener{
//                            override fun onClick(pos: Int) {
//                                val bundle = bundleOf("parentPgmIndex" to  pos + 1)
//                                navController.navigate(R.id.action_programFragment_to_scheduleFragment, bundle )
//                                CurrentID.UpdateID(num = 6)
//                                CurrentID.Updatebool(x = true)
//                            }
                              override fun onClick(pos: Int) {
                                  DayState.editPressed = true
                                  val getItem = PgmCollection.pgmCollection[pos]
                                  val bundle = bundleOf("parentPgmIndex" to  getItem.pgm!!.toInt())
                                  val pgmChecker =  DayCollection.dayCollection.filter { it.pgm!!.toInt() == getItem.pgm!!.toInt() }

                                  if(pgmChecker.isEmpty()){
                                      val newItem = DayManager()
                                      newItem.pgm = getItem.pgm
                                      DayCollection.dayCollection.add(newItem)
                                      navController.navigate(R.id.action_programFragment_to_dayPicker , bundle)
                                      CurrentID.UpdateID(num = 8)
                                      CurrentID.Updatebool(x = true)

                                  }else{
                                      navController.navigate(R.id.action_programFragment_to_dayPicker , bundle)
                                      CurrentID.UpdateID(num = 8)
                                      CurrentID.Updatebool(x = true)
                                  }
                              }
                        }
                    )
                )

                buffer.add(
                    MyButton(activity,
                        "Save",
                        30,
                        R.drawable.ic_save_dark_blue_24dp,
                        Color.parseColor("#14BED1"),
                        object : MyButtonClickListener{
                            override fun onClick(pos: Int) {
                                showCreateCategoryDialog(pos)
                            }
                        }
                    )
                )
            }
        }

        generateItem()
    }

    private fun generateItem() {
        PgmCollection.pgmCollection.sortBy { it.pgm!!.toInt() }
//        val a = PgmCollection.pgmCollection.sortedWith(compareBy({ it.pgm }))
//        PgmCollection.pgmCollection = a as MutableList<PgmItem>
        adapter = PgmAdapter(activity, PgmCollection.pgmCollection)
        recycler_pgm.adapter = adapter
    }

    private fun SynchronizePgmCollection(){
//        var pgmCollectionIndex = 1
//        for(item in PgmCollection.pgmCollection)
//        {
//            item.pgm = pgmCollectionIndex.toByte()
//            pgmCollectionIndex++
//        }

        adapter = PgmAdapter(activity, PgmCollection.pgmCollection)
        recycler_pgm.adapter = adapter

//        itemList.clear()
//        for(item in PgmCollection.pgmCollection){
//            itemList.add(item)
//        }
//        adapter = PgmAdapter(activity, itemList)
//        recycler_pgm.adapter = adapter
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        LanguageTranslate()
        btn_new_pgm.setOnClickListener{
//            var createdPgmIndex: Int = 0
            if(PgmCollection.pgmCollection == null)
            {
                CurrentID.parentPgmIndex = 1
            }
            else
            {
                var checker = 1
                for(item in PgmCollection.pgmCollection)
                {
                    if(item.pgm!!.toInt() != checker)
                    {
                        break
                    }
                    checker++
                }
                CurrentID.parentPgmIndex = checker
//                CurrentID.parentPgmIndex = PgmCollection.pgmCollection!!.count() + 1
            }

//            val bundle = bundleOf("parentPgmIndex" to  createdPgmIndex)
//            navController.navigate(R.id.action_programFragment_to_setStepFragment, bundle)
//            CurrentID.UpdateID(num = 6)
//            CurrentID.Updatebool(x = true)


            val bundle = bundleOf("parentPgmIndex" to  CurrentID.parentPgmIndex)
            val pgmChecker =  DayCollection.dayCollection.filter { it.pgm!!.toInt() == CurrentID.parentPgmIndex}

            if(pgmChecker.isEmpty()){
                val newItem = DayManager()
                newItem.pgm = CurrentID.parentPgmIndex.toByte()
                DayCollection.dayCollection.add(newItem)
                navController.navigate(R.id.action_programFragment_to_dayPicker , bundle)
                CurrentID.UpdateID(num = 8)
                CurrentID.Updatebool(x = true)

            }else{
                navController.navigate(R.id.action_programFragment_to_dayPicker , bundle)
                CurrentID.UpdateID(num = 8)
                CurrentID.Updatebool(x = true)
            }
        }
        btnImportFragment.setOnClickListener{
            navController.navigate(R.id.action_programFragment_to_importFragment)
            CurrentID.UpdateID(num = 9)
            CurrentID.Updatebool(x = true)
        }

        btn_activate_info.setOnClickListener{
            InfoPopup()
        }
        btn_activate.setOnClickListener{
            val lData = byteArrayOf(0x00.toByte())
            Protocol.cDeviceProt!!.transferData(0x02.toByte(), lData)
            Toast.makeText(activity!!, "Closing Application" , Toast.LENGTH_LONG).show()

            postDelayedSendToModule.postDelayed(EndActivity, 1000)

        }
    }
    var EndActivity = Runnable {
        CurrentID.UpdateID(num = 1)
        getActivity()!!.moveTaskToBack(true)
        getActivity()!!.finish()
    }
    private fun DeleteAlert(pos: Int){
        val getItem = PgmCollection.pgmCollection[pos]
        val mAlertDialog = AlertDialog.Builder(activity!!)
        mAlertDialog.setIcon(R.mipmap.ic_launcher_round) //set alertdialog icon
        mAlertDialog.setTitle("Are you sure?") //set alertdialog title
        mAlertDialog.setMessage("Do you want to delete Program " + (getItem.pgm!!.toInt()) + "?" ) //set alertdialog message
        mAlertDialog.setPositiveButton("Yes") { dialog, id ->
            RefreshStepCollection(getItem.pgm!!.toInt())
            DeletePGM(getItem.pgm!!.toInt())
//            PgmCollection.pgmCollection.removeAt(pos)
//            SynchronizePgmCollection()
            DeleteSchedule(getItem.pgm!!.toInt())

            var filter= DayCollection.dayCollection.filter {  it.pgm!!.toInt() == pos+1}
           // collection = filter.find {it.pgm!!.toInt() == pos+1}
            for(item in filter){
                DayCollection.dayCollection.remove(item)
            }

            var filterPgmInDatabase = dbm.allSaved.filter { it.pgm!!.toInt() == getItem.pgm!!.toInt() && it.name == Protocol.currentSSID }
            for(pgmInDatabase in filterPgmInDatabase){
                dbm.deletePgm(pgmInDatabase.name , pgmInDatabase.pgm!!.toByte())
            }

            var filterStepInDatabase = dbm.allStep.filter { it.pgm!!.toInt() == getItem.pgm!!.toInt() && it.pgm_name == Protocol.currentSSID }
            for(stepInDatabase in filterStepInDatabase){
                dbm.deleteStep(stepInDatabase.pgm_name!!, stepInDatabase.pgm!!.toByte())
            }

            var filterSchedInDatabase = dbm.allSched.filter { it.pgm!!.toInt() == getItem.pgm!!.toInt() && it.pgmname == Protocol.currentSSID }
            for(schedInDatabase in filterSchedInDatabase){
                dbm.deleteSchedule(schedInDatabase.pgmname!! , schedInDatabase.pgm!!.toByte())
            }
        }
        mAlertDialog.setNegativeButton("No") { dialog, id ->

        }
        mAlertDialog.show()
    }


    private fun DeletePGM(pgm: Int)
    {
        do{
            val pgmFilter = PgmCollection.pgmCollection.find { it.pgm!!.toInt() == pgm }
            PgmCollection.pgmCollection.remove(pgmFilter)
        }while (pgmFilter!= null)
//        PgmCollection.pgmCollection.removeAt(pgm)
        SynchronizePgmCollection()
    }

    private fun RefreshStepCollection(pgm: Int)
    {
        do{
            val retreivedStep = StepCollection.stepCollection.find { it.pgm!!.toInt() == pgm }
            StepCollection.stepCollection.remove(retreivedStep)

        }while (retreivedStep != null)

//        for(item in StepCollection.stepCollection)
//        {
//            if(item.pgm!!.toInt() > pgm)
//            {
//                val newPgm = item.pgm!!.toInt() -1
//                item.pgm = newPgm.toByte()
//            }
//        }
    }

    private fun DeleteSchedule(pgm: Int){
        do{
            val schedFilter = ScheduleCollection.scheduleCollection.find { it.pgm!!.toInt() == pgm }
            ScheduleCollection.scheduleCollection.remove(schedFilter)
        }while (schedFilter!= null)

//        for(item in ScheduleCollection.scheduleCollection){
//            if(item.pgm!!.toInt() > pgm){
//                item.pgm = item!!.pgm!!.dec()
//            }
//        }

    }


    private fun CopyProgram(pgm: PgmItem, stepList: List<StepItem>)
    {
        for(item in stepList)
        {
            StepCollection.stepCollection!!.add(item)
        }

        PgmCollection.pgmCollection!!.add(pgm)
    }

    fun showCreateCategoryDialog(pgm: Int) {

        val editAlert = AlertDialog.Builder(activity!!).create()

        val editView = layoutInflater.inflate(R.layout.fragment_input_dialog, null)

        editAlert.setView(editView)
        editAlert.show()

        editView.input_save.setOnClickListener{
            val textName = editAlert.name_input.text
            var getPgm = PgmCollection.pgmCollection[pgm]
            //var pgmAdd = PgmCollection.pgmCollection.find { it.pgm == getPgm.pgm }
            var pgmNameCheck = dbm.allpgm.find { it.name == textName.toString() }



            if (getPgm != null) {
                when {
                    textName.isEmpty() -> {
                        editView.name_input.setBackgroundResource(R.drawable.redborder)
                        Toast.makeText(activity!!, "Please fill required field!" , Toast.LENGTH_LONG).show()
                    }
                    pgmNameCheck!= null -> {
                        editView.name_input.setBackgroundResource(R.drawable.redborder)
                        Toast.makeText(activity!!, "Name Already Taken!" , Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        getPgm.name = textName.toString()
                            var stepAdd = StepCollection.stepCollection.filter { it.pgm == getPgm.pgm }
                        for(steps in stepAdd){
                            steps.pgm_name = textName.toString()
                            dbm.addStep(steps)
                        }
                            var schedAdd = ScheduleCollection.scheduleCollection.filter { it.pgm == getPgm.pgm }
                        for(scheds in schedAdd){
                            scheds.pgmname =textName.toString()
                            dbm.addSchedule(scheds)
                        }
                        getPgm.save = 1

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

                        getPgm.timestamp = ""+tdMonth+tdDay+tdYearStr+tdHour+":"+tdMinute+":"+tdSecond+""
                        //pgmAdd.pgm = 0
                        dbm.addPgm(getPgm)
                        editAlert.dismiss()
                        Toast.makeText(activity!!, "Save Success!" , Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        editView.input_cancel.setOnClickListener{
            editAlert.dismiss()
            Toast.makeText(activity!!, "Save Canceled!" , Toast.LENGTH_LONG).show()
        }
    }

    fun ResetBirdsLight(){
        val command: Byte = 0x01
        val data = byteArrayOf(
            128.toByte(),
            128.toByte(),
            0.toByte()
        )
//        undo moko
        if(Protocol.cDeviceProt != null) {
            Protocol.cDeviceProt!!.transferData(command, data)
        }
    }

    fun InfoPopup(){
        val infoAlert = AlertDialog.Builder(activity!!).create()
        val InfoView = layoutInflater.inflate(R.layout.fragment_program_info, null)
        infoAlert.setView(InfoView)
        infoAlert.show()
        InfoView.btn_got_it.setOnClickListener{

            infoAlert.dismiss()
        }
    }


    fun LanguageTranslate(){
        if (Language.Lang == "Chinese"){
            lblProgramList.text = "程序清单"
            btn_new_pgm.text = "新"
            btnImportFragment.text = "出口"
        }
    }
}
