package com.example.blapp.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_program_list.view.*

class PgmViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView)
{
    var txt_pgm_num : TextView
    init {
        txt_pgm_num = itemView.pgm_Num
    }
}