package com.example.blapp.model

class ScheduleItem{
    var command: Byte? = 0
    var pgm: Byte? = 0
    var smonth: Byte? = 0
    var sday: Byte? = 0
    var emonth: Byte? = 0
    var eday: Byte? = 0
    var wday: Byte? = 0
    var shour: Byte? = 0
    var sminute: Byte? = 0
    var ehour: Byte? = 0
    var eminute: Byte? = 0
    var pgmname: String? = ""
    var sched: Byte? = 0

    get() = field
    set(value){
        field = value
    }
}