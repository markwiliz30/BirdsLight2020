package com.example.blapp.model

class DayManager {
     var pgm: Byte? = 0
     var monday: Boolean = false
     var tuesday: Boolean = false
     var wednesday: Boolean = false
     var thursday: Boolean = false
     var friday: Boolean = false
     var saturday: Boolean = false
     var sunday: Boolean = false
     var alldays: Boolean = false
     var sMonth: String? = ""
     var sDay: String? = ""
     var eMonth: String? = ""
     var eDay: String? = ""

     get() = field
     //Setter
     set(value){
         field = value
     }
}