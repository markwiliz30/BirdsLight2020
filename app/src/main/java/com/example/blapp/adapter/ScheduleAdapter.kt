package com.example.blapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.blapp.R
import com.example.blapp.model.ScheduleItem

class ScheduleAdapter(internal var context: FragmentActivity?, internal var itemList:MutableList<ScheduleItem>):
    RecyclerView.Adapter<TimeViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.fragment_time_list, parent , false)
        return TimeViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        holder.txt_num.text = "Pgm- " + itemList[position].pgm + "."
        holder.txt_title.text = convertToMonthString(itemList[position].smonth!!.toInt()) + ", " + itemList[position].sday + " ~ " + convertToMonthString(itemList[position].emonth!!.toInt()) + ", " + itemList[position].eday
        holder.btn_del.isVisible = false
    }

    fun convertToMonthString(month: Int): String{
        when(month) {
            0->{
                return "Jan"
            }
            1->{
                return "Feb"
            }
            2->{
                return "Mar"
            }
            3->{
                return "Apr"
            }
            4->{
                return "May"
            }
            5->{
                return "Jun"
            }
            6->{
                return "Jul"
            }
            7->{
                return "Aug"
            }
            8->{
                return "Sep"
            }
            9->{
                return "Oct"
            }
            10->{
                return "Nov"
            }
            11->{
                return "Dec"
            }
            else->
            {
                return ""
            }
        }
    }
}