package com.example.blapp.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.log_list.view.*

class LogViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    var txt_log : TextView
    init {
        txt_log = itemView.log_detail
    }
}