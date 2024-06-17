package com.supercarlounge.supercar

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.webkit.JavascriptInterface
import com.supercarlounge.supercar.ui.activity.LoginPassActivity


class WebPush  {
    var url_tag="users/update/token"
    var mContext :Context? = null
    var application: MainApplication?=null
    constructor(context: Context)  {
        mContext=context
      //  application=context.applicationContext as MainApplication

    }

    @JavascriptInterface
        fun PaymentResult(
//        u_name: String,
        data:String)
//        ,u_birthday:String,u_phone:String)
        {
//        Log.d("jsdata1",data)
//        Log.d("jsdata2 u_name:",u_name)

//        Log.d("jsdata2 u_birthday:",u_birthday)
//        Log.d("jsdata2 u_phone:",u_phone)
//        resultLauncherimageInsuranceimage.launch(intent)
//        Toast.makeText(mContext,data,Toast.LENGTH_SHORT).show()

            var str_arr = data.split("&")
            var u_name = ""
            var u_gender = ""
            var u_birthday = ""
            var u_phone = ""
            for(i in str_arr.indices step(1)){
                when (i){
                    0->u_name = str_arr[i]
                    1->u_gender = str_arr[i]
                    2->u_birthday = str_arr[i]
                    3->u_phone = str_arr[i]
                    else -> {}
                }
            }

            val sb = StringBuffer()
            sb.append(u_birthday)
            sb.insert(4, "-")
            sb.insert(7, "-")
            u_birthday = sb.toString()



            var i = Intent().apply {
                putExtra("u_name",u_name)
                putExtra("u_gender",u_gender)
                putExtra("u_birthday",u_birthday)
                putExtra("u_phone",u_phone)
            }
            (mContext as LoginPassActivity).setResult(Activity.RESULT_OK,i)

        (mContext as LoginPassActivity).finish()

    }


    @JavascriptInterface
    fun PaymentResultfail() {
        Log.d("패스", "PaymentResultfail: ")
        (mContext as LoginPassActivity).finish()

    }






}