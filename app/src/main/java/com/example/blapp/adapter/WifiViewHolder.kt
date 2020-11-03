package com.example.blapp.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.wifi_item.view.*

class WifiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
{
    var lblWifiName : TextView
    var lblWifiStatus : TextView
    var lblWifiLvl : ImageView
    init {
        lblWifiName = itemView.lbl_wifi_name
        lblWifiStatus = itemView.lbl_wifi_status
        lblWifiLvl = itemView.img_wifi_lvl
    }
}