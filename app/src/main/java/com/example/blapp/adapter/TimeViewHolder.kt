package com.example.blapp.adapter

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_time_list.view.*

class TimeViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {

    var txt_num : TextView
    var txt_title : TextView
    var btn_del : ImageView
    init{
        txt_num = itemView.time_num
        txt_title = itemView.time_title
        btn_del = itemView.btn_delete_time
    }

}