package com.example.blapp


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.DialogInterface
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.blapp.common.Protocol
import kotlinx.android.synthetic.main.fragment_settings.*


/**
 * A simple [Fragment] subclass.
 */
class Settings : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onDetach() {
        super.onDetach()
        val command: Byte = 0x01
        val data = byteArrayOf(
            0.toByte(),
            0.toByte(),
            0.toByte()
        )
        Protocol.cDeviceProt!!.transferData(command, data)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        LanguageID.setOnClickListener{

            var languageSelected = 0
            val listItems = arrayOf("Arabic ",
                "Armenian",
                "Bengali",
                "Chinese",
                "Czech",
                "Dutch",
                "English",
                "Filipino",
                "French",
                "German",
                "Indonesian",
                "Japanese",
                "Korean",
                "Malay",
                "Portuguese",
                "Russian ",
                "Spanish",
                "Swedish",
                "Thai",
                "Vietnamese")

            val builder = AlertDialog.Builder(activity!!)
            builder.setTitle("Choose item")

            val checkedItem = 0 //this will checked the item when user open the dialog
            builder.setSingleChoiceItems(listItems, checkedItem,
                DialogInterface.OnClickListener { dialog, which ->
//                    Toast.makeText(
//                        activity,
//                        "Position: " + which + " Value: " + listItems[which],
//                        Toast.LENGTH_LONG
//                    ).show()
                    languageSelected = which
                })

            builder.setPositiveButton("Done",
                DialogInterface.OnClickListener { dialog, which ->
                    lblSelectedLanguage.text =listItems[languageSelected]
                        dialog.dismiss() })

            val dialog = builder.create()
            dialog.show()
        }


        FactoryReset.setOnClickListener{
            val command: Byte = 0x0f
            val data = byteArrayOf(
                0xf0.toByte()
            )
            Protocol.cDeviceProt!!.transferData(command, data)
        }
    }

    fun ShowSaveAlert(){

        val mAlertDialog = AlertDialog.Builder(activity!!)
        mAlertDialog.setIcon(R.mipmap.ic_launcher_round)
        mAlertDialog.setTitle("Are you sure?")
        mAlertDialog.setMessage("This will delete all created program and reset to default")


        mAlertDialog.setPositiveButton("Okay") { dialog, id ->
            Toast.makeText(activity , "Reseting device" , Toast.LENGTH_SHORT).show()
        }

        mAlertDialog.setNegativeButton("Cancel") { dialog, id ->

        }

        mAlertDialog.show()
    }
}
