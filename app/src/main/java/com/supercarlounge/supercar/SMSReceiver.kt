package com.supercarlounge.supercar

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status


class SMSReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive() called")
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent!!.action) {
            val extras = intent!!.extras
            val status: Status? = extras!![SmsRetriever.EXTRA_STATUS] as Status?
            Log.d(TAG, "SmsReceiver : onReceiver")
            when (status!!.getStatusCode()) {
                CommonStatusCodes.SUCCESS -> {
                    val message = extras!![SmsRetriever.EXTRA_SMS_MESSAGE] as String?
                    Log.d(TAG, "SmsReceiver : onReceiver(CommonStatusCodes.SUCCESS)")
                    Log.d(TAG, "message : $message")
                    var num = messageClip(message!!)
                    Log.d("RECEIVE_NUM",num)
                    if(!num.isNullOrEmpty()){
                    val local = Intent()
                    local.action = "SuperCar"
                    local.putExtra("number",num)
                    context!!.sendBroadcast(local)
                    }
                    //        val bundle = intent!!.extras
                }
                CommonStatusCodes.TIMEOUT -> Log.d(
                    TAG,
                    "SmsReceiver : onReceiver(CommonStatusCodes.TIMEOUT)"
                )
                else -> {}
            }
        }


    }
    fun messageClip(message:String):String{
        var num  =""
        try {
            var ss = message!!.split(":")
            var mes = ss.get(1)
            var stindex = mes!!.indexOf("[")
            var edindex = mes!!.indexOf("]")
            num = mes.substring(stindex, edindex)
        }catch (e:java.lang.IndexOutOfBoundsException){

        }
        return  num
    }
    companion object {
        private const val TAG = "SMSReceiver"
    }
}
