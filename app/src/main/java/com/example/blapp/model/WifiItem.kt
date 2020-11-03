package com.example.blapp.model

import kotlin.properties.Delegates

class WifiItem{
    var name: String? = ""
    var status: Int = 0
    var level: Int? = 0
    var selected: Boolean = false
    var capabilities: String? = ""

        //Getter
        get() = field
        //Setter
        set(value){
            field = value
        }

//    var a: String by Delegates.observable(a) { _, old, new ->
//        println("Name changed from $old to $new")
//    }
}