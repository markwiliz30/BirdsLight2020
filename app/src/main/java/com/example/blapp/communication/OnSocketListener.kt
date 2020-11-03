package com.example.blapp.communication

interface OnSocketListener {
    fun onReceived(msg: String)
}