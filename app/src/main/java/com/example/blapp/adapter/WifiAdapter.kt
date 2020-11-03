package com.example.blapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.blapp.R
import com.example.blapp.common.InputDialog
import com.example.blapp.common.WifiUtils
import com.example.blapp.model.WifiItem
import android.content.DialogInterface.BUTTON_POSITIVE
import androidx.appcompat.app.AlertDialog

class WifiAdapter(internal var context: FragmentActivity?, internal var itemList:MutableList<WifiItem>):
RecyclerView.Adapter<WifiViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WifiViewHolder {
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.wifi_item, parent, false)
        return WifiViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: WifiViewHolder, position: Int) {
        var wifiNameHold: String? = itemList[position].name
        holder.lblWifiName.text = wifiNameHold.toString()

        var wifiStatusHold: Int? = itemList[position].status
        when(wifiStatusHold){
            0 -> holder.lblWifiStatus.text = "Not Connected"
            1 -> holder.lblWifiStatus.text = "Connecting..."
            2 -> holder.lblWifiStatus.text = "Connected!"
            else -> holder.lblWifiStatus.text = "Not Connected"
        }

        var wifiLvlHold: Int? = itemList[position].level
        if(itemList[position].selected)
        {
            when(wifiLvlHold){
                0 -> holder.lblWifiLvl.setImageResource(R.drawable.ic_signal_wifi_0_bar_light_blue_24dp)
                1 -> holder.lblWifiLvl.setImageResource(R.drawable.ic_signal_wifi_1_bar_light_blue_24dp)
                2 -> holder.lblWifiLvl.setImageResource(R.drawable.ic_signal_wifi_2_bar_light_blue_24dp)
                3 -> holder.lblWifiLvl.setImageResource(R.drawable.ic_signal_wifi_3_bar_light_blue_24dp)
                4 -> holder.lblWifiLvl.setImageResource(R.drawable.ic_signal_wifi_4_bar_light_blue_24dp)
                else -> holder.lblWifiLvl.setImageResource(R.drawable.ic_signal_wifi_0_bar_light_blue_24dp)
            }
        }
        else
        {
            when(wifiLvlHold){
                0 -> holder.lblWifiLvl.setImageResource(R.drawable.ic_signal_wifi_0_bar_dark_blue_24dp)
                1 -> holder.lblWifiLvl.setImageResource(R.drawable.ic_signal_wifi_1_bar_dark_blue_24dp)
                2 -> holder.lblWifiLvl.setImageResource(R.drawable.ic_signal_wifi_2_bar_dark_blue_24dp)
                3 -> holder.lblWifiLvl.setImageResource(R.drawable.ic_signal_wifi_3_bar_dark_blue_24dp)
                4 -> holder.lblWifiLvl.setImageResource(R.drawable.ic_signal_wifi_4_bar_dark_blue_24dp)
                else -> holder.lblWifiLvl.setImageResource(R.drawable.ic_signal_wifi_0_bar_dark_blue_24dp)
            }
        }

        holder.itemView.setOnClickListener{
            for(item in itemList)
            {
                item.status = 0
                item.selected = false
            }
            //itemList[position].selected = !itemList[position].selected
            WifiUtils.selectedWifiIndex = position
            //notifyDataSetChanged()

            //SelectedDevice.SSID = itemList[position].name.toString()

            if(!itemList[position].capabilities!!.toUpperCase().contains("WPA") && !itemList[position].capabilities!!.toUpperCase().contains("WEP"))
            {
                val connectWifi = WifiUtils()
                connectWifi.connectWiFi(context!! ,WifiUtils.sharedWifiManager!!, WifiUtils.wifiList[WifiUtils.selectedWifiIndex!!].name!!, "", itemList[position].capabilities!!)
            }
            else
            {
                val inputDialog = InputDialog()
                inputDialog.show((context as FragmentActivity).supportFragmentManager, "wifiInput")
            }

//            if(itemList[position].selected)
//            {
//
//            }
//            else
//            {
//                holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"))
//            }
        }

        when(itemList[position].status){
            0->{holder.lblWifiStatus.text = "Not Connected"}
            1->{holder.lblWifiStatus.text = "Connecting..."}
            2->{holder.lblWifiStatus.text = "Connected"}
        }

        if(itemList[position].selected)
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#39405A"))
            holder.lblWifiName.setTextColor(Color.parseColor("#14BED1"))
            holder.lblWifiStatus.setTextColor(Color.parseColor("#14BED1"))
        }
        else
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#14BED1"))
            holder.lblWifiName.setTextColor(Color.parseColor("#39405A"))
            holder.lblWifiStatus.setTextColor(Color.parseColor("#39405A"))
        }
    }
}