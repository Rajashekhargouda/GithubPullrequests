package com.meeshotask.myapp.util

import android.content.Context
import android.widget.Toast

class ViewUtil {
    companion object {
        fun showToastErrorMsg(context:Context,errorMsg:String,length:Int){
            Toast.makeText(context,errorMsg, length).show()
        }
    }
}