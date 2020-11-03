package com.example.blapp

import android.content.Context
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.CurrentId.extensions.CurrentID
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.OnDayPickedListener
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.calendarview.PrimeCalendarView
import com.aminography.primedatepicker.fragment.PrimeDatePickerBottomSheet
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.fragment_calendar.view.*

class CalendarFragment : Fragment(){

    lateinit var navController: NavController
    private var calendarType = CalendarType.JAPANESE

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        this.calendarView.onDayPickedListener
        calendarView.calendarType = calendarType
        calendarView.flingOrientation = PrimeCalendarView.FlingOrientation.HORIZONTAL

        calendarView.pickType = PickType.RANGE_START
        end_Date.isChecked = false

        start_Date.setOnClickListener{
            calendarView.pickType = PickType.RANGE_START
            end_Date.isChecked = false
        }

        end_Date.setOnClickListener{
            calendarView.pickType = PickType.RANGE_END
            start_Date.isChecked = false
        }


//        if(start_Date.isChecked){
//            calendarView.pickType = PickType.RANGE_START
//            end_Date.isChecked = false
//        }
//        if(end_Date.isChecked){
//            calendarView.pickType = PickType.RANGE_END
//            start_Date.isChecked = false
//        }

        btn_Save_Calender.setOnClickListener{

            var startmon = calendarView.pickedRangeStartCalendar?.month?.plus(1)
            var starday = calendarView.pickedRangeStartCalendar?.dayOfMonth
            var endmon = calendarView.pickedRangeEndCalendar?.month?.plus(1)
            var endday =calendarView.pickedRangeEndCalendar?.dayOfMonth

           startView.text = "From:    "+startmon.toString()+"-"+starday.toString()+""
           endView.text = "To:    "+endmon.toString()+"-"+endday.toString()+""
        }


    }


//    override fun onDayPicked(pickType: PickType,
//                             singleDay: PrimeCalendar?,
//                             startDay: PrimeCalendar?,
//                             endDay: PrimeCalendar?,
//                             multipleDays: List<PrimeCalendar>?) {
//        btnEnd.isEnabled = false
//            pickedTextView.text = ""
//            when (pickType) {
//                PickType.RANGE_START, PickType.RANGE_END -> {
//                    calendarView.pickedRangeStartCalendar?.let { start ->
//                        btnEnd.isEnabled = true
//                        pickedTextView.visibility = View.VISIBLE
//                        var text = "Start Range Day: ${start.longDateString}"
//                        calendarView.pickedRangeEndCalendar?.let { end ->
//                            text += "\n"
//                            text += "End Range Day: ${end.longDateString}"
//                        }
//                        pickedTextView.text = text
//                    }
//                }
//                PickType.NOTHING -> {
//                    pickedTextView.visibility = View.INVISIBLE
//                }
//            }
//
//    }


}
