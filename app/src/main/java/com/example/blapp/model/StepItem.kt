package com.example.blapp.model

class StepItem {
    var command: Byte? = 0
    var step_id: Byte? = 0
    var pgm: Byte? = 0
    var step: Byte? = 0
    var pan: Byte? = 0
    var tilt: Byte? = 0
    var blink: Byte? = 0
    var time: Byte? = 0
    var pgm_name: String? = ""
    //Getter
    get() = field
    //Setter
    set(value){
        field = value
    }

}