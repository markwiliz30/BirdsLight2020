package com.example.blapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blapp.adapter.ImportAdapter
import com.example.blapp.collection.DayCollection
import com.example.blapp.collection.PgmCollection
import com.example.blapp.collection.ScheduleCollection
import com.example.blapp.collection.StepCollection
import com.example.blapp.databasehelper.DBmanager
import com.example.blapp.model.DayManager
import com.example.blapp.model.PgmItem
import com.example.blapp.model.ScheduleItem
import com.example.blapp.model.StepItem
import kotlinx.android.synthetic.main.fragment_import.*
import kotlinx.android.synthetic.main.fragment_import_list.*
import kotlinx.android.synthetic.main.fragment_import_list.view.*


class ImportFragment : Fragment() {

    companion object{
        var isSelectedAll = false
    }
    var lstCheck = mutableListOf<String>()
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter : ImportAdapter
    internal lateinit var dbm:DBmanager
    lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_import, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbm = DBmanager(activity!!)
        navController = Navigation.findNavController(view)
        recycler_import.setHasFixedSize(true)
        recycler_import.setItemViewCacheSize(25)
        layoutManager = LinearLayoutManager(activity)
        recycler_import.layoutManager = layoutManager

        btn_import_list.setOnClickListener{
            for (item in adapter.itemList){
                if(item.isClicked){
                    lstCheck.add(item.name)
                }
            }
            if(lstCheck.isEmpty()){
                Toast.makeText(activity,"Select a Program to Import!" , Toast.LENGTH_SHORT).show()
            }else{
                for (itemname in lstCheck){
                var lastPgm = PgmCollection.pgmCollection.count() + 1
                    for(item in dbm.allpgm){
                        if(item.name == itemname){
                            val newPgm = PgmItem()
                            newPgm.command =item.command
                            newPgm.pgm = lastPgm.toByte()
                            PgmCollection.pgmCollection.add(newPgm)
                        }
                    }
                    for(item in dbm.allStep){
                        if(item.pgm_name == itemname){
                            val newStep = StepItem()
                            newStep.command = item.command
                            newStep.pgm = lastPgm.toByte()
                            newStep.step = item.step
                            newStep.pan = item.pan
                            newStep.tilt = item.tilt
                            newStep.blink = item.blink
                            newStep.time = item.time
                            StepCollection.stepCollection.add(newStep)
                        }
                    }
                    for (item in dbm.allSched){
                        if(item.pgmname == itemname){
                            val newSchedule = ScheduleItem()
                            newSchedule.command = item.command
                            newSchedule.pgm = lastPgm.toByte()
                            newSchedule.shour = item.shour
                            newSchedule.sminute = item.sminute
                            newSchedule.ehour = item.ehour
                            newSchedule.eminute = item.eminute
                            newSchedule.sched = item.sched
                            newSchedule.wday = item.wday
                            newSchedule.smonth = item.smonth
                            newSchedule.sday = item.sday
                            newSchedule.emonth = item.emonth
                            newSchedule.eday = item.eday
                            ScheduleCollection.scheduleCollection.add(newSchedule)

                            val newDaymanager = DayManager()
                            newDaymanager.pgm = lastPgm.toByte()
                            newDaymanager.sMonth = item.smonth.toString()
                            newDaymanager.sDay = item.sday.toString()
                            newDaymanager.eMonth = item.emonth.toString()
                            newDaymanager.eDay = item.eday.toString()
                            DayCollection.dayCollection.add(newDaymanager)
                        }
                    }
                }
                lstCheck.clear()
                refreshList()
                select_all_checkbox.isChecked = false
                adapter.notifyDataSetChanged()
                Toast.makeText(activity, "Import Success!" , Toast.LENGTH_SHORT).show()
            }
        }

        delete_import.setOnClickListener{
            for (item in adapter.itemList){
                if(item.isClicked){
                    lstCheck.add(item.name)
                }
            }
            if(lstCheck.isEmpty()){
                Toast.makeText(activity,"Select a program to delete!" , Toast.LENGTH_SHORT).show()
            }else{
                ShowDeleteAlert()
            }

        }
        select_all_checkbox.setOnCheckedChangeListener { _, isChecked ->
//                for (id in adapter.itemList) {
//                    adapter.context!!.import_checkbox.isChecked = true
//                }

            isSelectedAll = !isSelectedAll
            adapter.notifyDataSetChanged()
        }
        ShowImports()
    }

    private fun ShowImports(){
        adapter = ImportAdapter(activity, dbm.allpgm as MutableList<PgmItem>)
        recycler_import.adapter = adapter
    }
    private fun refreshList(){
        adapter.itemList.clear()
        ShowImports()
    }

    fun ShowDeleteAlert(){

        val mAlertDialog = AlertDialog.Builder(activity!!)
        mAlertDialog.setIcon(R.mipmap.ic_launcher_round)
        mAlertDialog.setTitle("Are you Sure you want to Delete?")


        mAlertDialog.setPositiveButton("Yes") { dialog, id ->

                for (itemname in lstCheck){

                    for(item in dbm.allpgm){
                        if(item.name == itemname){
                            dbm.deletePgm(itemname)
                        }
                    }
                    for(item in dbm.allStep){
                        if(item.pgm_name == itemname){
                            dbm.deleteStep(itemname)
                        }
                    }
                    for (item in dbm.allSched){
                        if(item.pgmname == itemname){
                            dbm.deleteSchedule(itemname)
                        }
                    }
                }
                select_all_checkbox.isChecked = false
                refreshList()
                lstCheck.clear()
            select_all_checkbox.isChecked = false
            adapter.notifyDataSetChanged()
        }

        mAlertDialog.setNegativeButton("Cancel") { dialog, id ->
            lstCheck.clear()
        }

        mAlertDialog.show()
    }
}
