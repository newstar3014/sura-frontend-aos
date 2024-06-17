package com.supercarlounge.supercar

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager


class AlarmReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent) {
        val reqCode = intent.extras!!.getInt("종료")
        if (reqCode == 1) {
//            val packageManager: PackageManager? = context?.packageManager
//            val intent = packageManager?.getLaunchIntentForPackage(context.packageName!!)
//            val componentName = intent!!.component
//            val mainIntent = Intent.makeRestartActivityTask(componentName)
//            context.startActivity(mainIntent)
//            System.exit(0)
        }
    }

    companion object {
        private const val TAG = "AlarmReceiver"
    }
}