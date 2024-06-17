package com.supercarlounge.supercar

import android.R
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.webkit.JavascriptInterface
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebView.WebViewTransport
import com.supercarlounge.supercar.ui.activity.AddressWebViewActivity


class MyWebChromeClient : WebChromeClient() {
    private val TAG = "MyWebChromeClient"
    override fun onProgressChanged(view: WebView, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
//        MyLog.i(TAG, "onProgressChanged(view:$view, newProgress:$newProgress)")
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateWindow(
        view: WebView,
        isDialog: Boolean,
        isUserGesture: Boolean,
        resultMsg: Message
    ): Boolean {
//        MyLog.toastMakeTextShow(view.context, "TAG", "window.open 협의가 필요합니다.")
        val newWebView = WebView(view.context)
        val webSettings = newWebView.settings
        val settings = newWebView.settings
        settings.javaScriptEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.setSupportMultipleWindows(true)
        newWebView.addJavascriptInterface(AndroidBridge(), "Android")
        //final Dialog dialog = new Dialog(view.getContext(),R.style.Theme_DialogFullScreen);
        val dialog = Dialog(view.context)
        dialog.setContentView(newWebView)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog.show()
        dialog.getWindow()?.setAttributes(lp)

        dialog.setOnKeyListener(DialogInterface.OnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                //MyLog.toastMakeTextShow(view.getContext(), "TAG", "KEYCODE_BACK");
                if (newWebView.canGoBack()) {
                    newWebView.goBack()
                } else {
//                    MyLog.toastMakeTextShow(view.context, "TAG", "Window.open 종료")
                    dialog.dismiss()
                }
                true
            } else {
                false
            }
        })
        newWebView.webViewClient = MyWebViewClient(view.context)
        newWebView.webChromeClient = object : WebChromeClient() {
            override fun onCloseWindow(window: WebView?) {
                dialog.dismiss()
            }
        }
        val transport = resultMsg.obj as WebViewTransport
        transport.webView = newWebView
        resultMsg.sendToTarget()
        return true
    }

    override fun onCloseWindow(window: WebView) {
        Log.i(javaClass.name, "onCloseWindow")
        window.visibility = View.GONE
        window.destroy()
        //mWebViewSub=null;
        super.onCloseWindow(window)
    }

    override fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
        Log.i(javaClass.name, "onJsAlert() url:$url, message:$message")
        //return super.onJsAlert(view, url, message, result);
        AlertDialog.Builder(view.context)
            .setTitle("")
            .setMessage(message)
            .setPositiveButton(
                R.string.ok
            ) { dialog, which -> result.confirm() }
            .setCancelable(false)
            .create()
            .show()
        return true
    }

    override fun onJsConfirm(
        view: WebView,
        url: String,
        message: String,
        result: JsResult
    ): Boolean {
        Log.i(javaClass.name, "onJsConfirm() url:$url, message$message")
        //return super.onJsConfirm(view, url, message, result);
        AlertDialog.Builder(view.context)
            .setTitle("")
            .setMessage(message)
            .setPositiveButton(
                R.string.ok
            ) { dialog, which -> result.confirm() }
            .setNegativeButton(
                R.string.cancel
            ) { dialog, which -> result.cancel() }
            .create()
            .show()
        return true
    }

    internal class AndroidBridge {
        @JavascriptInterface
        fun ADDRESS(address: String?,latitude: String?,longitude: String?) {

            Log.d("콜백", address.toString())



        }
    }
}