package com.example.blapp.databasehelper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.blapp.model.PgmItem
import com.example.blapp.model.ScheduleItem
import com.example.blapp.model.StepItem


class DBmanager(context: Context):SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VER) {
    companion object{
        const val DATABASE_NAME = "BIRDSLIGHT.db"
        const val DATABASE_VER = 1

        const val STEP_TABLE = "TABLE_STEP"
        const val STEP_ID = "STEP_ID"
        const val STEP_COMMAND = "STEP_COMMAND"
        const val STEP_STEP = "STEP_STEP"
        const val STEP_PAN = "STEP_PAN"
        const val STEP_TILT = "STEP_TILT"
        const val STEP_BLINK = "STEP_BLINK"
        const val STEP_TIME = "STEP_TIME"
        const val STEP_PGM_NAME = "STEP_PGM_NAME"

        const val PROGRAM_TABLE = "TABLE_PROGRAM"
        const val PROGRAM_ID = "PROGRAM_ID"
        const val PROGRAM_COMMAND = "PROGRAM_COMMAND"
        const val PROGRAM_NAME = "PROGRAM_NAME"

        const val SCHEDULE_TABLE = "TABLE_SCHEDULE"
        const val SCHEDULE_ID = "SCHEDULE_ID"
        const val SCHEDULE_PGM_NAME = "SCHEDULE_PGM_NAME"
        const val SCHEDULE_COMMAND = "SCHEDULE_COMMAND"
        const val SCHEDULE_SMONTH = "SCHEDULE_SMONTH"
        const val SCHEDULE_SDAY = "SCHEDULE_SDAY"
        const val SCHEDULE_EMONTH = "SCHEDULE_EMONTH"
        const val SCHEDULE_EDAY = "SCHEDULE_EDAY"
        const val SCHEDULE_WDAY = "SCHEDULE_WDAY"
        const val SCHEDULE_SHOUR = "SCHEDULE_SHOUR"
        const val SCHEDULE_SMINUTE = "SCHEDULE_SMINUTE"
        const val SCHEDULE_EHOUR = "SCHEDULE_EHOUR"
        const val SCHEDULE_EMINUTE = "SCHEDULE_EMINUTE"
        const val SCHEDULE_SCHED = "SCHEDULE_SCHED"




    }
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(("CREATE TABLE $STEP_TABLE ($STEP_ID INTEGER PRIMARY KEY AUTOINCREMENT, $STEP_COMMAND BYTE, $STEP_STEP BYTE, $STEP_PAN BYTE,$STEP_TILT BYTE , $STEP_BLINK BYTE , $STEP_TIME BYTE , $STEP_PGM_NAME TEXT)"))
        db!!.execSQL(("CREATE TABLE $PROGRAM_TABLE ($PROGRAM_ID INTEGER PRIMARY KEY AUTOINCREMENT , $PROGRAM_COMMAND BYTE, $PROGRAM_NAME TEXT)"))
        db!!.execSQL(("CREATE TABLE $SCHEDULE_TABLE($SCHEDULE_ID INTEGER PRIMARY KEY AUTOINCREMENT, $SCHEDULE_COMMAND BYTE , $SCHEDULE_PGM_NAME TEXT, $SCHEDULE_SMONTH BYTE ," +
                "$SCHEDULE_SDAY BYTE , $SCHEDULE_EMONTH BYTE, $SCHEDULE_EDAY BYTE , $SCHEDULE_WDAY BYTE , $SCHEDULE_SHOUR BYTE , $SCHEDULE_SMINUTE BYTE , " +
                "$SCHEDULE_EHOUR BYTE , $SCHEDULE_EMINUTE BYTE ,$SCHEDULE_SCHED BYTE)"))
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
    db!!.execSQL("DROP TABLE IF EXISTS" + STEP_TABLE)
        db!!.execSQL("DROP TABLE IF EXISTS" + PROGRAM_TABLE)
        db!!.execSQL("DROP TABLE IF EXISTS" + SCHEDULE_TABLE)
    }

    fun getTables(){
        val db: SQLiteDatabase = this.writableDatabase
        val arrTblNames = ArrayList<String>()
        val c =
            db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null)

        if (c.moveToFirst()) {
            while (!c.isAfterLast) {
                arrTblNames.add(c.getString(c.getColumnIndex("name")))
                c.moveToNext()
            }
        }
    }

    fun getProfilesCount(): Int {
        val countQuery = "SELECT  * FROM $SCHEDULE_TABLE"
        val db = this.readableDatabase
        val cursor = db.rawQuery(countQuery, null)
        val count = cursor.count
        cursor.close()
        return count
    }

    val allStep:List<StepItem>

        get(){
            val lstStep = ArrayList<StepItem>()
            val selectQuery = "SELECT * FROM $STEP_TABLE"
            val db:SQLiteDatabase = this.writableDatabase
            val cursor: Cursor = db.rawQuery(selectQuery, null)
            if(cursor.moveToFirst()){
                do{
                    val step = StepItem()
                    step.step_id = cursor.getInt(cursor.getColumnIndex(STEP_ID.toString())).toByte()
                    step.pgm_name = cursor.getString(cursor.getColumnIndex(STEP_PGM_NAME.toString()))
                    step.step = cursor.getInt(cursor.getColumnIndex(STEP_STEP.toString())).toByte()
                    step.pan = cursor.getInt(cursor.getColumnIndex(STEP_PAN.toString())).toByte()
                    step.tilt = cursor.getInt(cursor.getColumnIndex(STEP_TILT.toString())).toByte()
                    step.blink = cursor.getInt(cursor.getColumnIndex(STEP_BLINK.toString())).toByte()
                    step.time = cursor.getInt(cursor.getColumnIndex(STEP_TIME.toString())).toByte()

                    lstStep.add(step)

                }while(cursor.moveToNext())

            }
            db.close()
            return lstStep
        }


    fun addStep(step:StepItem){
        val db:SQLiteDatabase = this.writableDatabase
        val values = ContentValues()
        values.put(STEP_PGM_NAME,step.pgm_name)
        values.put(STEP_STEP, step.step)
        values.put(STEP_PAN, step.pan)
        values.put(STEP_TILT, step.tilt)
        values.put(STEP_BLINK, step.blink)
        values.put(STEP_TIME, step.time)

        db.insert(STEP_TABLE , null , values)
        db.close()
    }


    fun updateStep(step: StepItem): Int {
        val db:SQLiteDatabase = this.writableDatabase
        val values = ContentValues()
        values.put(STEP_PGM_NAME,step.pgm_name)
        values.put(STEP_STEP, step.step)
        values.put(STEP_PAN, step.pan)
        values.put(STEP_TILT, step.tilt)
        values.put(STEP_BLINK, step.blink)
        values.put(STEP_TIME, step.time)

        return db.update(STEP_TABLE , values , "$STEP_PGM_NAME =" , arrayOf(step.pgm_name.toString()))
        db.close()
    }


    val allpgm:List<PgmItem>

        get() {
            val lstpgm = ArrayList<PgmItem>()
            val selectQuery = "SELECT * FROM $PROGRAM_TABLE"
            val db: SQLiteDatabase = this.writableDatabase
            val cursor: Cursor = db.rawQuery(selectQuery, null)
            if(cursor.moveToFirst()){
                do{
                    val pgm = PgmItem()
                    pgm.pgm_id = cursor.getInt(cursor.getColumnIndex(PROGRAM_ID.toString())).toByte()
                    pgm.command = cursor.getInt(cursor.getColumnIndex(PROGRAM_COMMAND.toString())).toByte()
                    pgm.name = cursor.getString(cursor.getColumnIndex(PROGRAM_NAME.toString()))

                    lstpgm.add(pgm)
                }while(cursor.moveToNext())

            }
            db.close()
            return lstpgm
        }
    val allSched:List<ScheduleItem>

        get()  {
            val lstSched = ArrayList<ScheduleItem>()
            val selectQuery = "SELECT * FROM $SCHEDULE_TABLE"
            val db: SQLiteDatabase = this.writableDatabase
            val cursor: Cursor = db.rawQuery(selectQuery , null)
            if(cursor.moveToFirst()){
                do{
                    val schedule = ScheduleItem()
                    schedule.pgmname = cursor.getString(cursor.getColumnIndex(SCHEDULE_PGM_NAME.toString()))
                    schedule.command = cursor.getInt(cursor.getColumnIndex(SCHEDULE_COMMAND.toString())).toByte()
                    schedule.smonth = cursor.getInt(cursor.getColumnIndex(SCHEDULE_SMONTH.toString())).toByte()
                    schedule.sday = cursor.getInt(cursor.getColumnIndex(SCHEDULE_SDAY.toString())).toByte()
                    schedule.emonth = cursor.getInt(cursor.getColumnIndex(SCHEDULE_EMONTH.toString())).toByte()
                    schedule.eday = cursor.getInt(cursor.getColumnIndex(SCHEDULE_EDAY.toString())).toByte()
                    schedule.wday = cursor.getInt(cursor.getColumnIndex(SCHEDULE_WDAY.toString())).toByte()
                    schedule.shour = cursor.getInt(cursor.getColumnIndex(SCHEDULE_SHOUR.toString())).toByte()
                    schedule.sminute = cursor.getInt(cursor.getColumnIndex(SCHEDULE_SMINUTE.toString())).toByte()
                    schedule.ehour = cursor.getInt(cursor.getColumnIndex(SCHEDULE_EHOUR.toString())).toByte()
                    schedule.eminute = cursor.getInt(cursor.getColumnIndex(SCHEDULE_EMINUTE.toString())).toByte()
                    schedule.sched = cursor.getInt(cursor.getColumnIndex(SCHEDULE_SCHED.toString())).toByte()
                    lstSched.add(schedule)
                }while (cursor.moveToNext())
            }
            db.close()
            return lstSched
        }

    fun addSchedule(schedule: ScheduleItem){
        val db:SQLiteDatabase = this.writableDatabase
        val values = ContentValues()
        values.put(SCHEDULE_PGM_NAME , schedule.pgmname)
        values.put(SCHEDULE_COMMAND , schedule.command)
        values.put(SCHEDULE_SMONTH , schedule.smonth)
        values.put(SCHEDULE_SDAY , schedule.sday)
        values.put(SCHEDULE_EMONTH , schedule.emonth)
        values.put(SCHEDULE_EDAY , schedule.eday)
        values.put(SCHEDULE_WDAY , schedule.wday)
        values.put(SCHEDULE_SHOUR , schedule.shour)
        values.put(SCHEDULE_SMINUTE , schedule.sminute)
        values.put(SCHEDULE_EHOUR , schedule.ehour)
        values.put(SCHEDULE_EMINUTE , schedule.eminute)
        values.put(SCHEDULE_SCHED , schedule.sched)
        db.insert(SCHEDULE_TABLE , null , values)
        db.close()

    }


    fun addPgm(pgm: PgmItem){
        val db:SQLiteDatabase = this.writableDatabase
        val values = ContentValues()
        values.put(PROGRAM_COMMAND,pgm.command)
        values.put(PROGRAM_NAME, pgm.name)
        db.insert(PROGRAM_TABLE, null , values)
        db.close()

    }

    fun updatePgm(pgm:PgmItem): Int{
        val db:SQLiteDatabase = this.writableDatabase
        val values = ContentValues()
        values.put(PROGRAM_ID, pgm.pgm_id)
        values.put(PROGRAM_COMMAND,pgm.command)
        values.put(PROGRAM_NAME, pgm.name)

        return db.update(PROGRAM_TABLE, values , "$PROGRAM_ID = ?" , arrayOf(pgm.pgm_id.toString()))
        db.close()
    }

    fun deletePgm(name: String){
        val db:SQLiteDatabase = this.writableDatabase

        db.delete(PROGRAM_TABLE, "$PROGRAM_NAME = ?", arrayOf(name))
        db.close()
    }

    fun deleteStep(name: String){
        val db:SQLiteDatabase = this.writableDatabase
        db.delete(STEP_TABLE, "$STEP_PGM_NAME = ?" , arrayOf(name))
        db.close()
    }

    fun deleteSchedule(name: String){
        val db:SQLiteDatabase = this.writableDatabase
        db.delete(SCHEDULE_TABLE, "$SCHEDULE_PGM_NAME = ?" , arrayOf(name))
    }

    fun deleteAllPgm(){
        val db:SQLiteDatabase = this.writableDatabase

        db.execSQL("DELETE from $PROGRAM_TABLE")
    }

    fun deleteAllStep(){
        val db:SQLiteDatabase = this.writableDatabase
        db.execSQL("DELETE from $STEP_TABLE")
    }


}