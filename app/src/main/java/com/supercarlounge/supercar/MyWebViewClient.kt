package com.supercarlounge.supercar

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient


class MyWebViewClient(_applicationContext: Context?) : WebViewClient() {
    private val TAG = "MyWebViewClient"
    private var mApplicationContext: Context? = null
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest): Boolean {
        Log.i(TAG, "shouldOverrideUrlLoading(view:$view, request:$request)")
        return super.shouldOverrideUrlLoading(view, request)
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        Log.i(TAG, "onPageStarted(view:$view, url:$url, favicon:$favicon)")
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        Log.i(TAG, "onPageFinished(view:$view, url:$url)")
    }

    override fun onReceivedError(
        view: WebView,
        request: WebResourceRequest,
        error: WebResourceError
    ) {
        super.onReceivedError(view, request, error)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Log.i(TAG, "onReceivedError() " + error.getErrorCode() + " ---> " + error.getDescription());
            onReceivedError(error.errorCode, error.description.toString())
        }
    }


    private fun onReceivedError(errorCode: Int, description: String) {

        when (errorCode) {
            ERROR_TIMEOUT, ERROR_CONNECT, ERROR_FILE_NOT_FOUND, ERROR_HOST_LOOKUP, ERROR_UNSUPPORTED_AUTH_SCHEME, ERROR_AUTHENTICATION, ERROR_PROXY_AUTHENTICATION, ERROR_IO, ERROR_REDIRECT_LOOP, ERROR_UNSUPPORTED_SCHEME, ERROR_FAILED_SSL_HANDSHAKE, ERROR_BAD_URL, ERROR_FILE, ERROR_TOO_MANY_REQUESTS, ERROR_UNSAFE_RESOURCE -> {

                Log.e(TAG, "WebViewClient,onReceivedError($errorCode) 에러 발생 ")
            }
            else -> {}
        }
    }

    init {
        mApplicationContext = _applicationContext
    }
}