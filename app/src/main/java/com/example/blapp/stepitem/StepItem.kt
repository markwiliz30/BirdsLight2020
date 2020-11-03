package com.example.blapp.stepitem

class StepItem {
    var pgm: Byte? = 0
    var step: Byte? = 0
    var pan: Byte? = 0
    var tilt: Byte? = 0
    var blink: Byte? = 0
    var time: Byte? = 0

    //Getter
    get() = field
    //Setter
    set(value){
        field = value
    }
}