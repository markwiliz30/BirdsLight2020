package com.example.blapp.common

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.blapp.R

class LoadingDialog : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val view: View = layoutInflater.inflate(R.layout.layout_loading_dialog, null)

        builder.setView(view)
        return builder.create()
    }
}