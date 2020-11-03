package com.example.blapp.adapter

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_import_list.view.*

class ImportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var imporTitle : TextView
    var importCard : CardView
    lateinit var importCardCheckBox: CheckBox
    init {
        imporTitle = itemView.import_title
        importCard = itemView.import_card
        importCardCheckBox = itemView.import_checkbox
    }



}