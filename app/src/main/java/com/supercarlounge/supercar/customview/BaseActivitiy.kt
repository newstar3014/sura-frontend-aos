package com.supercarlounge.supercar.customview

import android.app.AlarmManager
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.supercarlounge.supercar.AlarmController

open class BaseActivitiy :AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE);


    }

    override fun onResume() {
        super.onResume()
    }
    override fun onPause() {
        super.onPause()

        Log.d("백그라운드 진입", "테스트: ")
    }

}