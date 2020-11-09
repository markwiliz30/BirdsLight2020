package com.example.blapp

import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.activity_landing.*
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.CurrentId.extensions.CurrentID
import android.os.Handler
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.blapp.common.InputDialog
import com.example.blapp.common.Protocol
import com.example.blapp.common.WifiUtils
import kotlin.system.exitProcess
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.content.ContextCompat.startActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.os.bundleOf
import com.example.blapp.common.DeviceProtocol
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.*


class Landing : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    private var Request_Code = 1000
    
    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onConnected(p0: Bundle?) {

    }

    override fun onConnectionSuspended(p0: Int) {

    }

    companion object {
        private const val ID_HOME = 1
        private const val ID_TESTFRAGMENT = 2
        private const val ID_PROGRAMFRAGMENT = 3
        private const val ID_SCHEDULEFRAGMENT = 4
        private const val ID_SETTINGSFRAGMENT = 5
        private const val ID_STEPFRAGMENT = 6
        private const val ID_TIMESCHEDFRAGMENT = 7
        private const val ID_DAYPICKERFRAGMENT = 8
        private const val ID_IMPORTFRAGMENT = 9
        private const val ID_CALENDAR = 10

        private var aax = true

        var isBNavVisible = false
    }
    private val REQUEST_LOCATION_CODE = 1
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        setPermission()

        //testing code
//        val deviceProtocol = DeviceProtocol()
//        Protocol.cDeviceProt = deviceProtocol
//        Protocol.cDeviceProt!!.startChannel()

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        bottomNavigation.add(MeowBottomNavigation.Model(ID_HOME, R.drawable.ic_home_black_24dp))
        bottomNavigation.add(
            MeowBottomNavigation.Model(
                ID_TESTFRAGMENT,
                R.drawable.ic_test_black_24dp
            )
        )
        bottomNavigation.add(
            MeowBottomNavigation.Model(
                ID_PROGRAMFRAGMENT,
                R.drawable.ic_view_list_black_24dp
            )
        )
//        bottomNavigation.add(
//            MeowBottomNavigation.Model(
//                ID_SCHEDULEFRAGMENT,
//                R.drawable.ic_schedule_black_24dp
//            )
//        )
        bottomNavigation.add(
            MeowBottomNavigation.Model(
                ID_SETTINGSFRAGMENT,
                R.drawable.ic_settings_black_24dp
            )
        )

        //bottomNavigation.setCount(ID_NOTIFICATION, "69")
        bottomNavigation.show(ID_HOME)

        bottomNavigation.setOnClickMenuListener {
            when (it.id) {
                ID_HOME -> if (CurrentID.getID() == ID_TESTFRAGMENT) {
                    navController.navigate(R.id.action_testFragment_to_landingFragment)
                    CurrentID.UpdateID(num = it.id)
                } else if (CurrentID.getID() == ID_PROGRAMFRAGMENT) {
                    navController.navigate(R.id.action_programFragment_to_landingFragment)
                    CurrentID.UpdateID(num = it.id)
                } else if (CurrentID.getID() == ID_SCHEDULEFRAGMENT) {
                    navController.navigate(R.id.action_scheduleFragment_to_landingFragment)
                    CurrentID.UpdateID(num = it.id)
                } else if (CurrentID.getID() == ID_SETTINGSFRAGMENT) {
                    navController.navigate(R.id.action_settings_to_landingFragment)
                    CurrentID.UpdateID(num = it.id)
                } else if (CurrentID.getID() == ID_STEPFRAGMENT) {
                    bottomNavigation.isVisible = false
                    ShowSaveAlert(clicked = it.id, current = CurrentID.getID())
                } else if (CurrentID.getID() == ID_TIMESCHEDFRAGMENT) {
                    bottomNavigation.isVisible = false
                    ShowSaveAlert(clicked = it.id, current = CurrentID.getID())
                } else if (CurrentID.getID() == ID_DAYPICKERFRAGMENT) {
                    bottomNavigation.isVisible = false
                    ShowSaveAlert(clicked = it.id, current = CurrentID.getID())
                } else if (CurrentID.getID() == ID_IMPORTFRAGMENT) {
                    bottomNavigation.isVisible = false
                    ShowSaveAlert(clicked = it.id, current = CurrentID.getID())
                } else if (CurrentID.getID() == ID_CALENDAR) {
                    bottomNavigation.isVisible = false
                    ShowSaveAlert(clicked = it.id, current = CurrentID.getID())
                }

                //replace if statement "WifiUtils.isConnectedToBL"
                ID_TESTFRAGMENT ->
                    if (WifiUtils.isConnectedToBL) {
                        if (CurrentID.getID() == ID_HOME) {
                            navController.navigate(R.id.action_landingFragment_to_testFragment)
                            CurrentID.UpdateID(num = it.id)
                        } else if (CurrentID.getID() == ID_PROGRAMFRAGMENT) {
                            navController.navigate(R.id.action_programFragment_to_testFragment)
                            CurrentID.UpdateID(num = it.id)
                        } else if (CurrentID.getID() == ID_SCHEDULEFRAGMENT) {
                            navController.navigate(R.id.action_scheduleFragment_to_testFragment)
                            CurrentID.UpdateID(num = it.id)
                        } else if (CurrentID.getID() == ID_SETTINGSFRAGMENT) {
                            navController.navigate(R.id.action_settings_to_testFragment)
                            CurrentID.UpdateID(num = it.id)
                        } else if (CurrentID.getID() == ID_STEPFRAGMENT) {
                            bottomNavigation.isVisible = false
                            ShowSaveAlert(clicked = it.id, current = CurrentID.getID())
                        } else if (CurrentID.getID() == ID_TIMESCHEDFRAGMENT) {
                            bottomNavigation.isVisible = false
                            ShowSaveAlert(clicked = it.id, current = CurrentID.getID())
                        } else if (CurrentID.getID() == ID_DAYPICKERFRAGMENT) {
                            bottomNavigation.isVisible = false
                            ShowSaveAlert(clicked = it.id, current = CurrentID.getID())
                        } else if (CurrentID.getID() == ID_IMPORTFRAGMENT) {
                            bottomNavigation.isVisible = false
                            ShowSaveAlert(clicked = it.id, current = CurrentID.getID())
                        } else if (CurrentID.getID() == ID_CALENDAR) {
                            bottomNavigation.isVisible = false
                            ShowSaveAlert(clicked = it.id, current = CurrentID.getID())
                        }
                    } else {
                        bottomNavigation.isVisible = false
                        NotConnectedAlert()
                    }

                // replace if statement "WifiUtils.isConnectedToBL"
                ID_PROGRAMFRAGMENT ->
                    if (WifiUtils.isConnectedToBL) {
                        if (CurrentID.getID() == ID_HOME) {
                            navController.navigate(R.id.action_landingFragment_to_programFragment)
                            CurrentID.UpdateID(num = it.id)
                        } else if (CurrentID.getID() == ID_TESTFRAGMENT) {
                            navController.navigate(R.id.action_testFragment_to_programFragment)
                            CurrentID.UpdateID(num = it.id)
                        } else if (CurrentID.getID() == ID_SCHEDULEFRAGMENT) {
                            navController.navigate(R.id.action_scheduleFragment_to_programFragment)
                            CurrentID.UpdateID(num = it.id)
                        } else if (CurrentID.getID() == ID_SETTINGSFRAGMENT) {
                            navController.navigate(R.id.action_settings_to_programFragment)
                            CurrentID.UpdateID(num = it.id)
                        } else if (CurrentID.getID() == ID_CALENDAR) {
                            bottomNavigation.isVisible = false
                            ShowSaveAlert(clicked = it.id, current = CurrentID.getID())
                        }
                    } else {
                        bottomNavigation.isVisible = false
                        NotConnectedAlert()
                    }

                //replace the if statement "WifiUtils.isConnectedToBL"
                ID_SCHEDULEFRAGMENT -> if (WifiUtils.isConnectedToBL) {
                    if (CurrentID.getID() == ID_HOME) {
                        navController.navigate(R.id.action_landingFragment_to_scheduleFragment)
                        CurrentID.UpdateID(num = it.id)
                    } else if (CurrentID.getID() == ID_TESTFRAGMENT) {
                        navController.navigate(R.id.action_testFragment_to_scheduleFragment)
                        CurrentID.UpdateID(num = it.id)
                    } else if (CurrentID.getID() == ID_PROGRAMFRAGMENT) {
                        navController.navigate(R.id.action_programFragment_to_scheduleFragment)
                        CurrentID.UpdateID(num = it.id)
                    } else if (CurrentID.getID() == ID_SETTINGSFRAGMENT) {
                        navController.navigate(R.id.action_settings_to_scheduleFragment)
                        CurrentID.UpdateID(num = it.id)
                    } else if (CurrentID.getID() == ID_STEPFRAGMENT) {
                        bottomNavigation.isVisible = false
                        ShowSaveAlert(clicked = it.id, current = CurrentID.getID())
                    } else if (CurrentID.getID() == ID_IMPORTFRAGMENT) {
                        bottomNavigation.isVisible = false
                        ShowSaveAlert(clicked = it.id, current = CurrentID.getID())
                    } else if (CurrentID.getID() == ID_TIMESCHEDFRAGMENT) {
                        bottomNavigation.isVisible = false
                        ShowSaveAlert(clicked = it.id, current = CurrentID.getID())
                    } else if (CurrentID.getID() == ID_DAYPICKERFRAGMENT) {
                        bottomNavigation.isVisible = false
                        ShowSaveAlert(clicked = it.id, current = CurrentID.getID())
                    }
                } else {
                    bottomNavigation.isVisible = false
                    NotConnectedAlert()
                }

                //replace the if statement "WifiUtils.isConnectedToBL"
                ID_SETTINGSFRAGMENT -> if (WifiUtils.isConnectedToBL) {
                    if (CurrentID.getID() == ID_HOME) {
                        navController.navigate(R.id.action_landingFragment_to_settings)
                        CurrentID.UpdateID(num = it.id)
                    } else if (CurrentID.getID() == ID_TESTFRAGMENT) {
                        navController.navigate(R.id.action_testFragment_to_settings)
                        CurrentID.UpdateID(num = it.id)
                    } else if (CurrentID.getID() == ID_PROGRAMFRAGMENT) {
                        navController.navigate(R.id.action_programFragment_to_settings)
                        CurrentID.UpdateID(num = it.id)
                    } else if (CurrentID.getID() == ID_SCHEDULEFRAGMENT) {
                        navController.navigate(R.id.action_scheduleFragment_to_settings)
                        CurrentID.UpdateID(num = it.id)
                    } else if (CurrentID.getID() == ID_STEPFRAGMENT) {
                        bottomNavigation.isVisible = false
                        ShowSaveAlert(clicked = it.id, current = CurrentID.getID())
                    } else if (CurrentID.getID() == ID_TIMESCHEDFRAGMENT) {
                        bottomNavigation.isVisible = false
                        ShowSaveAlert(clicked = it.id, current = CurrentID.getID())
                    } else if (CurrentID.getID() == ID_DAYPICKERFRAGMENT) {
                        bottomNavigation.isVisible = false
                        ShowSaveAlert(clicked = it.id, current = CurrentID.getID())
                    } else if (CurrentID.getID() == ID_IMPORTFRAGMENT) {
                        bottomNavigation.isVisible = false
                        ShowSaveAlert(clicked = it.id, current = CurrentID.getID())
                    } else if (CurrentID.getID() == ID_CALENDAR) {
                        bottomNavigation.isVisible = false
                        ShowSaveAlert(clicked = it.id, current = CurrentID.getID())
                    }
                } else {
                    bottomNavigation.isVisible = false
                    NotConnectedAlert()
                }

                else -> navController.navigate(R.id.action_landingFragment_to_testFragment)
            }
//          }  Toast.makeText(this, "$name is clicked", Toast.LENGTH_SHORT).show()
        }
//
    }

    private var doubleBackToExitPressedOnce = false

    override fun onBackPressed() {

        if (CurrentID.getStatus()) {
            if (CurrentID.getID() == ID_STEPFRAGMENT) {
                val bundle = bundleOf("parentPgmIndex" to CurrentID.parentPgmIndex)
                navController.navigate(R.id.action_setStepFragment_to_dayPicker, bundle)
                CurrentID.Updatebool(x = true)
                CurrentID.UpdateID(num = ID_DAYPICKERFRAGMENT)
            } else if (CurrentID.getID() == ID_TIMESCHEDFRAGMENT) {
                val bundle = bundleOf("parentPgmIndex" to CurrentID.parentPgmIndex)
                navController.navigate(R.id.action_timeSchedule_to_dayPicker , bundle)
                CurrentID.Updatebool(x = true)
                CurrentID.UpdateID(num = ID_DAYPICKERFRAGMENT)
            } else if (CurrentID.getID() == ID_DAYPICKERFRAGMENT) {
                navController.navigate(R.id.action_dayPicker_to_programFragment)
                CurrentID.Updatebool(x = false)
                CurrentID.UpdateID(num = ID_PROGRAMFRAGMENT)
            } else if (CurrentID.getID() == ID_IMPORTFRAGMENT) {
                navController.navigate(R.id.action_importFragment_to_programFragment)
                CurrentID.Updatebool(x = false)
                CurrentID.UpdateID(num = ID_PROGRAMFRAGMENT)
            } else if (CurrentID.getID() == ID_CALENDAR) {
                navController.navigate(R.id.action_calendarFragment_to_scheduleFragment)
                CurrentID.Updatebool(x = false)
                CurrentID.UpdateID(num = ID_SCHEDULEFRAGMENT)
            }

        } else {
            if (doubleBackToExitPressedOnce) {
                ExitDialog()

            } else {
                this.doubleBackToExitPressedOnce = true
                Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show()

                Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)

            }
        }
    }

    private fun EnableGPSAutoMatically() {
        var googleApiClient: GoogleApiClient? = null
        if (googleApiClient == null) {
            googleApiClient = GoogleApiClient.Builder(this).addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build()
            googleApiClient!!.connect()
            val locationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = (30 * 1000).toLong()
            locationRequest.fastestInterval = (5 * 1000).toLong()
            val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

            // **************************
            builder.setAlwaysShow(true) // this is the key ingredient
            // **************************

            val result = LocationServices.SettingsApi
                .checkLocationSettings(googleApiClient, builder.build())
            result.setResultCallback { result ->
                val status = result.status
                val state = result
                    .locationSettingsStates
                when (status.statusCode) {
                    LocationSettingsStatusCodes.SUCCESS -> Toast.makeText(this, "GPS is Enabled", Toast.LENGTH_SHORT).show()
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        Toast.makeText(this, "GPS need to Enable", Toast.LENGTH_SHORT).show()
                        // Location settings are not satisfied.
                        // But could be fixed by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult()
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(this, Request_Code)

                        } catch (e: IntentSender.SendIntentException) {
                            // Ignore the error.
                        }

                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> Toast.makeText(this, "GPS is Unavailable", Toast.LENGTH_SHORT).show()
                }// All location settings are satisfied.
                // The client can initialize location requests here.
                // Location settings are not satisfied. However, we have
                // no way to fix the settings so we won't show the dialog.
            }
        }
    }

    fun NotConnectedAlert() {
        val mAlertDialog = AlertDialog.Builder(this)
        mAlertDialog.setIcon(R.mipmap.ic_launcher_round)
        mAlertDialog.setTitle("You are not connected to a Bird's Light Device")

        mAlertDialog.setNegativeButton("OK") { dialog, id ->
            bottomNavigation.isVisible = true
            bottomNavigation.show(ID_HOME)
        }

        mAlertDialog.setOnCancelListener {
            bottomNavigation.isVisible = true
            bottomNavigation.show(ID_HOME)
        }

        mAlertDialog.show()
    }

    private fun setPermission() {
//        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
//        if(permission!= PackageManager.PERMISSION_GRANTED)
//        {
//            Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show()
//        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            makeRequest()
        } else {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_LOCATION_CODE
        )
    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when(requestCode)
//        {
//            REQUEST_LOCATION_CODE -> {
//                if(grantResults.isEmpty()||grantResults[0]!= PackageManager.PERMISSION_GRANTED)
//                {
//                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
//                    exitProcess(-1)
//                }
//                else
//                {
//                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        Protocol.cDeviceProt!!.stopChannel()
        Toast.makeText(this, "app exit", Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_LOCATION_CODE) {
            // for each permission check if the user granted/denied them
            // you may want to group the rationale in a single dialog,
            // this is just an example
            var i = 0
            val len = permissions.size
            while (i < len) {
                val permission = permissions[i]
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    val showRationale = shouldShowRequestPermissionRationale(permission)
                    if (!showRationale) {
                        // user also CHECKED "never ask again"
                        // you can either enable some fall back,
                        // disable features of your app
                        // or open another dialog explaining
                        // again the permission and directing to
                        // the app setting
                        val mAlertDialog = AlertDialog.Builder(this)
                        mAlertDialog.setIcon(R.mipmap.ic_launcher_round) //set alertdialog icon
                        mAlertDialog.setTitle("Location Settings Required!!") //set alertdialog title
                        mAlertDialog.setMessage("This Application need to enable the location settings GOTO BLApp>Permission and eneble the location setting") //set alertdialog message
                        mAlertDialog.setPositiveButton("Open Settings") { dialog, which ->
                            startActivityForResult(
                                Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS),
                                0
                            )
                        }
                        mAlertDialog.show()
                    } else if (Manifest.permission.ACCESS_FINE_LOCATION == permission) {
                        //showRationale(permission, R.string.permission_denied_contacts)
                        // user did NOT check "never ask again"
                        // this is a good place to explain the user
                        // why you need the permission and ask if he wants
                        // to accept it (the rationale)
                        exitProcess(-1)
                    } else {
                    }/* possibly check more permissions...*/
                }else{
                    EnableGPSAutoMatically()
                }
                i++
            }
        }
    }

    fun ShowSaveAlert(clicked: Int, current: Int) {
        val mAlertDialog = AlertDialog.Builder(this)
        mAlertDialog.setIcon(R.mipmap.ic_launcher_round) //set alertdialog icon
        mAlertDialog.setTitle("Title!") //set alertdialog title
        mAlertDialog.setMessage("Your message here") //set alertdialog message
        mAlertDialog.setPositiveButton("Yes") { dialog, id ->
            if (clicked == ID_HOME && current == ID_STEPFRAGMENT) {
                bottomNavigation.isVisible = true
                bottomNavigation.show(clicked)
                navController.navigate(R.id.action_setStepFragment_to_landingFragment)
                CurrentID.UpdateID(num = clicked)
                CurrentID.Updatebool(x = false)
            } else if (clicked == ID_HOME && current == ID_TIMESCHEDFRAGMENT) {
                bottomNavigation.isVisible = true
                bottomNavigation.show(clicked)
                navController.navigate(R.id.action_timeSchedule_to_landingFragment)
                CurrentID.UpdateID(num = clicked)
                CurrentID.Updatebool(x = false)
            } else if (clicked == ID_HOME && current == ID_DAYPICKERFRAGMENT) {
                bottomNavigation.isVisible = true
                bottomNavigation.show(clicked)
                navController.navigate(R.id.action_dayPicker_to_landingFragment)
                CurrentID.UpdateID(num = clicked)
                CurrentID.Updatebool(x = false)
            } else if (clicked == ID_HOME && current == ID_IMPORTFRAGMENT) {
                bottomNavigation.isVisible = true
                navController.navigate(R.id.action_importFragment_to_landingFragment)
                CurrentID.UpdateID(num = clicked)
                CurrentID.Updatebool(x = false)
            } else if (clicked == ID_HOME && current == ID_CALENDAR) {
                bottomNavigation.isVisible = true
                navController.navigate(R.id.action_calendarFragment_to_landingFragment)
                CurrentID.UpdateID(num = clicked)
                CurrentID.Updatebool(x = false)
            } else if (clicked == ID_HOME && current == ID_DAYPICKERFRAGMENT) {
                bottomNavigation.isVisible = true
                navController.navigate(R.id.action_dayPicker_to_landingFragment)
                CurrentID.UpdateID(num = clicked)
                CurrentID.Updatebool(x = false)
            } else if (clicked == ID_TESTFRAGMENT && current == ID_STEPFRAGMENT) {
                bottomNavigation.isVisible = true
                navController.navigate(R.id.action_setStepFragment_to_testFragment)
                CurrentID.UpdateID(num = clicked)
                CurrentID.Updatebool(x = false)
            } else if (clicked == ID_TESTFRAGMENT && current == ID_TIMESCHEDFRAGMENT) {
                bottomNavigation.isVisible = true
                navController.navigate(R.id.action_timeSchedule_to_testFragment)
                CurrentID.UpdateID(num = clicked)
                CurrentID.Updatebool(x = false)
            } else if (clicked == ID_TESTFRAGMENT && current == ID_DAYPICKERFRAGMENT) {
                bottomNavigation.isVisible = true
                navController.navigate(R.id.action_dayPicker_to_testFragment)
                CurrentID.UpdateID(num = clicked)
                CurrentID.Updatebool(x = false)
            } else if (clicked == ID_TESTFRAGMENT && current == ID_IMPORTFRAGMENT) {
                bottomNavigation.isVisible = true
                navController.navigate(R.id.action_importFragment_to_testFragment)
                CurrentID.UpdateID(num = clicked)
                CurrentID.Updatebool(x = false)
            } else if (clicked == ID_TESTFRAGMENT && current == ID_CALENDAR) {
                bottomNavigation.isVisible = true
                navController.navigate(R.id.action_calendarFragment_to_testFragment)
                CurrentID.UpdateID(num = clicked)
                CurrentID.Updatebool(x = false)
            } else if (clicked == ID_TESTFRAGMENT && current == ID_DAYPICKERFRAGMENT) {
                bottomNavigation.isVisible = true
                navController.navigate(R.id.action_dayPicker_to_testFragment)
                CurrentID.UpdateID(num = clicked)
                CurrentID.Updatebool(x = false)
            } else if (clicked == ID_PROGRAMFRAGMENT && current == ID_TIMESCHEDFRAGMENT) {
                bottomNavigation.isVisible = true
                navController.navigate(R.id.action_timeSchedule_to_programFragment)
                CurrentID.UpdateID(num = clicked)
                CurrentID.Updatebool(x = false)
            } else if (clicked == ID_PROGRAMFRAGMENT && current == ID_DAYPICKERFRAGMENT) {
                bottomNavigation.isVisible = true
                navController.navigate(R.id.action_dayPicker_to_programFragment)
                CurrentID.UpdateID(num = clicked)
                CurrentID.Updatebool(x = false)
            } else if (clicked == ID_PROGRAMFRAGMENT && current == ID_CALENDAR) {
                bottomNavigation.isVisible = true
                navController.navigate(R.id.action_calendarFragment_to_programFragment)
                CurrentID.UpdateID(num = clicked)
                CurrentID.Updatebool(x = false)
            } else if (clicked == ID_SCHEDULEFRAGMENT && current == ID_STEPFRAGMENT) {
                bottomNavigation.isVisible = true
                navController.navigate(R.id.action_setStepFragment_to_scheduleFragment)
                CurrentID.UpdateID(num = clicked)
                CurrentID.Updatebool(x = false)
            } else if (clicked == ID_SCHEDULEFRAGMENT && current == ID_IMPORTFRAGMENT) {
                bottomNavigation.isVisible = true
                navController.navigate(R.id.action_importFragment_to_scheduleFragment)
                CurrentID.UpdateID(num = clicked)
                CurrentID.Updatebool(x = false)
            } else if (clicked == ID_SCHEDULEFRAGMENT && current == ID_DAYPICKERFRAGMENT) {
                bottomNavigation.isVisible = true
                navController.navigate(R.id.action_dayPicker_to_scheduleFragment)
                CurrentID.UpdateID(num = clicked)
                CurrentID.Updatebool(x = false)
            } else if (clicked == ID_SCHEDULEFRAGMENT && current == ID_TIMESCHEDFRAGMENT) {
                bottomNavigation.isVisible = true
                navController.navigate(R.id.action_timeSchedule_to_scheduleFragment)
                CurrentID.UpdateID(num = clicked)
                CurrentID.Updatebool(x = false)
            } else if (clicked == ID_SETTINGSFRAGMENT && current == ID_STEPFRAGMENT) {
                bottomNavigation.isVisible = true
                navController.navigate(R.id.action_setStepFragment_to_settings)
                CurrentID.UpdateID(num = clicked)
                CurrentID.Updatebool(x = false)
            } else if (clicked == ID_SETTINGSFRAGMENT && current == ID_TIMESCHEDFRAGMENT) {
                bottomNavigation.isVisible = true
                navController.navigate(R.id.action_timeSchedule_to_settings)
                CurrentID.UpdateID(num = clicked)
                CurrentID.Updatebool(x = false)
            } else if (clicked == ID_SETTINGSFRAGMENT && current == ID_DAYPICKERFRAGMENT) {
                bottomNavigation.isVisible = true
                navController.navigate(R.id.action_dayPicker_to_settings)
                CurrentID.UpdateID(num = clicked)
                CurrentID.Updatebool(x = false)
            } else if (clicked == ID_SETTINGSFRAGMENT && current == ID_IMPORTFRAGMENT) {
                bottomNavigation.isVisible = true
                navController.navigate(R.id.action_importFragment_to_settings)
                CurrentID.UpdateID(num = clicked)
                CurrentID.Updatebool(x = false)
            } else if (clicked == ID_SETTINGSFRAGMENT && current == ID_CALENDAR) {
                navController.navigate(R.id.action_calendarFragment_to_settings)
                bottomNavigation.isVisible = true
                CurrentID.UpdateID(num = clicked)
                CurrentID.Updatebool(x = false)
            }

            Toast.makeText(this, "Yes", Toast.LENGTH_SHORT).show()
        }
        mAlertDialog.setNegativeButton("No") { dialog, id ->
            bottomNavigation.isVisible = true
            if (current == ID_STEPFRAGMENT || current == ID_IMPORTFRAGMENT || current == ID_DAYPICKERFRAGMENT || current == ID_TIMESCHEDFRAGMENT) {
                bottomNavigation.show(ID_PROGRAMFRAGMENT)
            } else if (current == ID_DAYPICKERFRAGMENT || current == ID_CALENDAR) {
                bottomNavigation.show(ID_SCHEDULEFRAGMENT)
            }

            Toast.makeText(this, "No", Toast.LENGTH_SHORT).show()
        }
        mAlertDialog.show()
    }

    fun ExitDialog(){
        val mAlertDialog = AlertDialog.Builder(this)
        mAlertDialog.setIcon(R.mipmap.ic_launcher_round) //set alertdialog icon
        mAlertDialog.setTitle("Are you sure you want to exit?") //set alertdialog title
        mAlertDialog.setMessage("If you continue to exit all scheduled programs will not be activated.") //set alertdialog message
        mAlertDialog.setPositiveButton("Yes") { dialog, id ->
            CurrentID.UpdateID(num = ID_HOME)
            finishAffinity()
            Toast.makeText(this, "Yes", Toast.LENGTH_SHORT).show()
        }
        mAlertDialog.setNegativeButton("No") { dialog, id ->

            Toast.makeText(this, "No", Toast.LENGTH_SHORT).show()
        }
        mAlertDialog.show()
    }
}
