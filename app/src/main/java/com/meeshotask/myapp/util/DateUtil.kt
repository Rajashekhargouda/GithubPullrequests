package com.meeshotask.myapp.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class DateUtil {

    companion object {
        fun getDate(timeStamp:String):String{
            val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .parse(timeStamp)
            val formattedDate = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault())
            return formattedDate.format(date)
        }
    }

}