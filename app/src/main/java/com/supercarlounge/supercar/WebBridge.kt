package com.supercarlounge.supercar

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.webkit.JavascriptInterface
import com.supercarlounge.supercar.`interface`.WebCallback
import com.supercarlounge.supercar.ui.activity.LoginPassActivity


class WebBridge  {

    var webcallback :WebCallback? = null
    var application: MainApplication?=null
    constructor(callback: WebCallback)  {
        webcallback=callback
      //  application=context.applicationContext as MainApplication

    }




    @JavascriptInterface
    fun NOTICE() {
        if (webcallback != null){
            webcallback!!.MoveNotice()
        }

    }

    @JavascriptInterface
    fun FINISH() {
        if (webcallback != null){
            webcallback!!.WebViewFinish()
        }

    }




}