package com.example.blapp.model

class DataSetItem {
    var myCommand: Byte? = 0
    var myDatas: ByteArray? = null

        //Getter
        get() = field
        //Setter
        set(value){
            field = value
        }
}