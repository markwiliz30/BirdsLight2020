package com.example.blapp.common

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.blapp.R
import kotlinx.android.synthetic.main.layout_input_dialog.*
import kotlinx.android.synthetic.main.layout_input_dialog.view.*
import android.text.Editable
import android.text.TextWatcher

class InputDialog : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)

        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.layout_input_dialog, null)
        val SelSSID = WifiUtils.wifiList[WifiUtils.selectedWifiIndex!!].name

        view.btn_input_positive.isEnabled = false
        view.input_password_box.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                view.btn_input_positive.isEnabled = view.input_password_box.length() >= 8
            }
        })

        view.btn_input_negative.setOnClickListener{
            dismiss()
        }

        view.btn_input_positive.setOnClickListener{
            val mConnectWifi = WifiUtils()
            //val inputPassword: String? = input_password_box.text.toString()
            val isConnected = mConnectWifi.connectWiFi(context!! ,WifiUtils.sharedWifiManager!!, SelSSID!!, view.input_password_box.text.toString(), WifiUtils.wifiList[WifiUtils.selectedWifiIndex!!].capabilities!!)
            dismiss()
            //Toast.makeText(context, "Connect pressed", Toast.LENGTH_SHORT).show()
//                val wifiUtils = WifiUtils()
//                val selSSID = WifiUtils.wifiList.find { it.name ==  WifiUtils.wifiList[WifiUtils.selectedWifiIndex!!].name}
            //selSSID!!.status = wifiUtils.IsWiFiConnected(context!!, selSSID!!.name!!)
            //Toast.makeText(context, selSSID.status.toString(), Toast.LENGTH_SHORT).show()
        }

        builder.setView(view)
            .setTitle(SelSSID)
//            .setNegativeButton("Cancel") { dialog, which ->
//                Toast.makeText(context, "Cancel pressed", Toast.LENGTH_SHORT).show()
//            }
//            .setPositiveButton("Connect") { dialog, which ->
//                val mConnectWifi = WifiUtils()
//                //val inputPassword: String? = input_password_box.text.toString()
//                val isConnected = mConnectWifi.connectWiFi(context!! ,WifiUtils.sharedWifiManager!!, SelSSID!!, view.input_password_box.text.toString(), WifiUtils.wifiList[WifiUtils.selectedWifiIndex!!].capabilities!!)
//                WifiUtils.wifiList[WifiUtils.selectedWifiIndex!!].selected = true
//                WifiUtils.sharedWifiAdapter!!.notifyDataSetChanged()
//                //Toast.makeText(context, "Connect pressed", Toast.LENGTH_SHORT).show()
////                val wifiUtils = WifiUtils()
////                val selSSID = WifiUtils.wifiList.find { it.name ==  WifiUtils.wifiList[WifiUtils.selectedWifiIndex!!].name}
//                //selSSID!!.status = wifiUtils.IsWiFiConnected(context!!, selSSID!!.name!!)
//                //Toast.makeText(context, selSSID.status.toString(), Toast.LENGTH_SHORT).show()
//            }

        return builder.create()
    }

}