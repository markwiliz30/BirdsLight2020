package com.example.blapp.common

import android.os.Build
import androidx.annotation.RequiresApi

interface WifiConnectorBuilder {
    fun start()

    interface WifiUtilsBuilder {
        fun enableWifi(wifiStateListener: WifiStateListener)

        fun connectWith(ssid: String, password: String): WifiSuccessListener

        fun cancelAutoConnect()
    }

    interface WifiSuccessListener {
        fun setTimeout(timeOutMillis: Long): WifiSuccessListener

        fun onConnectionResult(successListener: ConnectionSuccessListener?): WifiConnectorBuilder
    }
}
