package com.meeshotask.myapp.util

import android.content.Context
import android.net.ConnectivityManager

class NetworkUtil {
    companion object {
        fun isNetworkConnected(context: Context):Boolean{
            return try {
                val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val netInfo = cm.activeNetworkInfo
                //should check null because in air plan mode it will be null
                netInfo != null && netInfo.isConnected
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}