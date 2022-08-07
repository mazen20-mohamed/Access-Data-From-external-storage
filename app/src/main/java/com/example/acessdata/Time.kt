package com.example.acessdata

import java.util.*

public fun getDaysAgo(ago:Int):String{
    val calender = Calendar.getInstance()
    calender.add(Calendar.DATE,ago)
    val date:Int = (calender.timeInMillis/1000).toInt()
    return date.toString()
}