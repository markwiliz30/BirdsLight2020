package com.example.blapp


import android.app.DownloadManager
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.blapp.common.Language
import com.example.blapp.common.Protocol
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import kotlinx.android.synthetic.main.fragment_settings.*


/**
 * A simple [Fragment] subclass.
 */
class Settings : Fragment(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener  {
    private val STORAGE_PERMISSION_CODE: Int = 1000


    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onConnected(p0: Bundle?) {

    }

    override fun onConnectionSuspended(p0: Int) {

    }
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

        LanguageTranslate()

        LanguageID.setOnClickListener{

            //Language.LanguageSelected = 0
            val listItems = arrayOf(
//                "Arabic ",
//                "Armenian",
//                "Bengali",
//                "Chinese",
//                "Czech",
//                "Dutch",
                "Chinese",
                "English"
//                "French",
//                "German",
//                "Indonesian",
//                "Japanese",
//                "Korean",
//                "Malay",
//                "Portuguese",
//                "Russian ",
//                "Spanish",
//                "Swedish",
//                "Thai",
//                "Vietnamese"
            )

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
                    Language.LanguageSelected = which
                })

            builder.setPositiveButton("Done",
                DialogInterface.OnClickListener { dialog, which ->
                    lblSelectedLanguage.text = listItems[Language.LanguageSelected!!]
                    Language.Lang = lblSelectedLanguage.text.toString()
                    LanguageTranslate()
                    dialog.dismiss()
                })

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
        //lblUpdate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow, 0)
        UpdateBL.setOnClickListener{
            val connectionManager:ConnectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork : NetworkInfo? = connectionManager.activeNetworkInfo
            val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

            if(isConnected){

                val url = "https://emoji.gg/assets/emoji/6757_Sadge.png"

                val request = DownloadManager.Request(Uri.parse(url))
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                request.setTitle("Download")
                request.setDescription("The file is downloading")

                request.allowScanningByMediaScanner()
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                request.setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    "${System.currentTimeMillis()}.png"
                )

                val downloadManager =
                    activity!!.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                downloadManager.enqueue(request)

                Toast.makeText(activity, "Connected", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(activity, "Not Connected", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
       when(requestCode){
           STORAGE_PERMISSION_CODE -> {
               if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

               } else {
                   Toast.makeText(activity, "Not Connected", Toast.LENGTH_SHORT).show()
               }
           }
       }
    }
    fun ShowSaveAlert(){

        val mAlertDialog = AlertDialog.Builder(activity!!)
        mAlertDialog.setIcon(R.mipmap.ic_launcher_round)
        mAlertDialog.setTitle("Are you sure?")
        mAlertDialog.setMessage("This will delete all created program and reset to default")


        mAlertDialog.setPositiveButton("Okay") { dialog, id ->
            Toast.makeText(activity, "Reseting device", Toast.LENGTH_SHORT).show()
        }

        mAlertDialog.setNegativeButton("Cancel") { dialog, id ->

        }

        mAlertDialog.show()
    }

    fun LanguageTranslate(){
        if (Language.Lang == "Chinese"){
            lbl_Settings.text = "设定"
            lblLanguage.text = "语言"
            lblFactoryReset.text = "恢复出厂设置"
        }
    }
}
