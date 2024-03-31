package com.vimal.teachers.util

import android.util.Log


object Utils {

    fun getErrors(e: Exception?) {
        println("TAG :: " + Log.getStackTraceString(e))
    }
}