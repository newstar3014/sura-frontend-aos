package com.supercarlounge.supercar

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log


class AlarmController(context: Context) {
    private val context: Context
    private val alarmMgr: AlarmManager?

    init {
        this.context = context
        alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
    }

//    fun setAlarm(reqCode: Int, timeMill: Long) {
//
//        val intent = Intent(context, AlarmReceiver::class.java)
//        intent.putExtra("종료", reqCode)
//        val alarmIntent = PendingIntent.getBroadcast(context, reqCode, intent,
//            PendingIntent.FLAG_IMMUTABLE)
//        alarmMgr!![AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() +
//                timeMill] = alarmIntent
//
//    }
//
//    fun cancelAlarm(reqCode: Int) {
//        val intent = Intent(context, AlarmReceiver::class.java)
//        val alarmIntent = PendingIntent.getBroadcast(context, reqCode, intent,
//            PendingIntent.FLAG_IMMUTABLE)
//        alarmMgr?.cancel(alarmIntent)
//    }

    companion object {
        private const val TAG = "AlarmController"
        private val sInstance: AlarmController? = null
    }
}