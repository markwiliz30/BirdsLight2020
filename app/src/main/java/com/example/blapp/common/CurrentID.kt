package com.CurrentId.extensions

object CurrentID {

    private var CurrentSelectedID: Int = 1
    private var IsBackEnable: Boolean = false
    public var parentPgmIndex: Int = 0

    fun UpdateID(num: Int): Int{
        CurrentSelectedID = num
        return CurrentSelectedID
    }
    fun getID(): Int {

        return CurrentSelectedID
    }

    fun Updatebool(x: Boolean): Boolean{
        IsBackEnable = x
        return IsBackEnable
    }
    fun getStatus(): Boolean{
        return IsBackEnable
    }
}