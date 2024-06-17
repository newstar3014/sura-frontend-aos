package com.supercarlounge.supercar.enumset

enum class TimeValue(val value: Int,val maximum : Int, val msg : String) {
    SEC(60,60,"분 전"),
    MIN(60,24,"시간 전"),
    HOUR(24,30,"일 전"),
    DAY(30,12,"오래 전"),
    MONTH(12,Int.MAX_VALUE,"오래 전")
}