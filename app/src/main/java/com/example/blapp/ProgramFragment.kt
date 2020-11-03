package com.example.blapp


import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
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
import com.example.blapp.common.DeviceProtocol
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
import java.util.*
import kotlin.collections.ArrayList


class ProgramFragment : Fragment(){

    lateinit var navController: NavController
    lateinit var layoutManager: LinearLayoutManager
    var parentPgmIndex: Int = 0
    lateinit var adapter: PgmAdapter

   // internal lateinit var dbStep:DBmanager
    internal lateinit var dbm:DBmanager
    internal var lstStep: List<StepItem> = ArrayList<StepItem>()
    internal var lstPgm: List<PgmItem> = ArrayList<PgmItem>()


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
        ResetBirdsLight()
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
                                  val bundle = bundleOf("parentPgmIndex" to  pos+1)
                                  val pgmChecker =  DayCollection.dayCollection.filter { it.pgm!!.toInt() == pos+1 }

                                  if(pgmChecker.isEmpty()){
                                      val newItem = DayManager()
                                      newItem.pgm = pos.toByte().inc()
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
                                showCreateCategoryDialog(pos+1)
                            }
                        }
                    )
                )

//                buffer.add(
//                    MyButton(activity,
//                        "DayPicker",
//                        30,
//                        R.drawable.ic_date_range_dark_blue_24dp,
//                        Color.parseColor("#14BED1"),
//                        object : MyButtonClickListener{
//                            override fun onClick(pos: Int) {
//                                val bundle = bundleOf("parentPgmIndex" to  pos+1)
//                                val pgmChecker =  DayCollection.dayCollection.filter { it.pgm!!.toInt() == pos+1 }
//
//                                if(pgmChecker.isEmpty()){
//                                    val newItem = DayManager()
//                                    newItem.pgm = pos.toByte().inc()
//                                    DayCollection.dayCollection.add(newItem)
//                                    navController.navigate(R.id.action_programFragment_to_dayPicker , bundle)
//                                    CurrentID.UpdateID(num = 8)
//                                    CurrentID.Updatebool(x = true)
//
//                                }else{
//                                    navController.navigate(R.id.action_programFragment_to_dayPicker , bundle)
//                                    CurrentID.UpdateID(num = 8)
//                                    CurrentID.Updatebool(x = true)
//                                }
//                            }
//                        }
//                    )
//                )
            }
        }

        generateItem()
    }

    private fun generateItem() {
        adapter = PgmAdapter(activity, PgmCollection.pgmCollection)
        recycler_pgm.adapter = adapter
    }

    private fun SynchronizePgmCollection(){
        var pgmCollectionIndex = 1
        for(item in PgmCollection.pgmCollection)
        {
            item.pgm = pgmCollectionIndex.toByte()
            pgmCollectionIndex++
        }

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
        btn_new_pgm.setOnClickListener{
//            var createdPgmIndex: Int = 0
            if(PgmCollection.pgmCollection == null)
            {
                CurrentID.parentPgmIndex = 1
            }
            else
            {
                CurrentID.parentPgmIndex = PgmCollection.pgmCollection!!.count() + 1
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
            val date = Date() // given date

            val calendar =
                GregorianCalendar.getInstance() // creates a new calendar instance

            calendar.time = date // assigns calendar to given date

            calendar[Calendar.HOUR_OF_DAY] // gets hour in 24h format

            calendar[Calendar.HOUR] // gets hour in 12h format

            calendar[Calendar.MONTH] // gets month number, NOTE this is zero based!

//            Toast.makeText(context, calendar[Calendar.YEAR].toString(), Toast.LENGTH_SHORT).show()

            var tes = calendar[Calendar.YEAR].toString()

//            Toast.makeText(context, tes.substring(1..3), Toast.LENGTH_SHORT).show()

//            val tdYearStr = calendar[Calendar.YEAR].toString()
//            val tdYearInt = tdYearStr.substring(1..3).toInt()
//            Toast.makeText(context, tdYearInt.toString(), Toast.LENGTH_SHORT).show()

            var test: MutableList<String> = mutableListOf()
            test.add("wew")
            test.add("waw")
            test.add("wow")
            Toast.makeText(context, test.count().toString(), Toast.LENGTH_SHORT).show()
        }
    }
    private fun DeleteAlert(pos: Int){
        val mAlertDialog = AlertDialog.Builder(activity!!)
        mAlertDialog.setIcon(R.mipmap.ic_launcher_round) //set alertdialog icon
        mAlertDialog.setTitle("Are you sure?") //set alertdialog title
        mAlertDialog.setMessage("Do you want to delete Program " + (pos + 1) + "?" ) //set alertdialog message
        mAlertDialog.setPositiveButton("Yes") { dialog, id ->
            val pgmIndex = PgmCollection.pgmCollection[pos]
            RefreshStepCollection(pgmIndex.pgm!!.toInt())
            PgmCollection.pgmCollection.removeAt(pos)
            SynchronizePgmCollection()
            DeleteSchedule(pgmIndex.pgm!!.toInt())

        }
        mAlertDialog.setNegativeButton("No") { dialog, id ->
            Toast.makeText(activity!!, "No", Toast.LENGTH_SHORT).show()
        }
        mAlertDialog.show()
    }

    private fun SaveName(name: String){

    }

    private fun RefreshStepCollection(pgm: Int)
    {
        do{
            val retreivedStep = StepCollection.stepCollection.find { it.pgm!!.toInt() == pgm }
            StepCollection.stepCollection.remove(retreivedStep)

        }while (retreivedStep != null)

        for(item in StepCollection.stepCollection)
        {
            if(item.pgm!!.toInt() > pgm)
            {
                val newPgm = item.pgm!!.toInt() -1
                item.pgm = newPgm.toByte()
            }
        }
    }

    private fun DeleteSchedule(pgm: Int){
        do{
            val schedFilter = ScheduleCollection.scheduleCollection.find { it.pgm!!.toInt() == pgm }
            ScheduleCollection.scheduleCollection.remove(schedFilter)
        }while (schedFilter!= null)

        for(item in ScheduleCollection.scheduleCollection){
            if(item.pgm!!.toInt() > pgm){
                item.pgm = item!!.pgm!!.dec()
            }
        }

    }

    fun showCreateCategoryDialog(pgm: Int) {

        val editAlert = AlertDialog.Builder(activity!!).create()

        val editView = layoutInflater.inflate(R.layout.fragment_input_dialog, null)

        editAlert.setView(editView)
        editAlert.show()

        editView.input_save.setOnClickListener{
            val textName = editAlert.name_input.text
            var pgmAdd = PgmCollection.pgmCollection.find { it.pgm == pgm.toByte() }
            var pgmNameCheck = dbm.allpgm.find { it.name == textName.toString() }



            if (pgmAdd != null) {
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
                        pgmAdd.name = textName.toString()
                            var stepAdd = StepCollection.stepCollection.filter { it.pgm == pgm.toByte() }
                        for(steps in stepAdd){
                            steps.pgm_name = textName.toString()
                            dbm.addStep(steps)
                        }
                            var schedAdd = ScheduleCollection.scheduleCollection.filter { it.pgm == pgm.toByte() }
                        for(scheds in schedAdd){
                            scheds.pgmname =textName.toString()
                            dbm.addSchedule(scheds)
                        }

                        dbm.addPgm(pgmAdd)
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
            0.toByte(),
            0.toByte(),
            0.toByte()
        )
//        undo moko
//        Protocol.cDeviceProt!!.transferData(command, data)
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
}
