package com.supercarlounge.supercar.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import com.google.firebase.dynamiclinks.ktx.*
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.MyWebChromeClient
import com.supercarlounge.supercar.MyWebViewClient
import com.supercarlounge.supercar.databinding.ActivityAddressWebviewBinding
import com.supercarlounge.supercar.viewmodel.WebViewViewModel


class AddressWebViewActivity( val event: (String, String,String) -> Unit) : DialogFragment() {
    var application:MainApplication ? = null
    private lateinit var binding: ActivityAddressWebviewBinding
    private lateinit var viewModel: WebViewViewModel

    var websettings: WebSettings? = null
    var URL_INFO =""
    val chromeClient = WebChromeClient()

    var navController: NavController? = null
    var webView: WebView? = null
    var handler: Handler? = null


    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        //binding 설정
        binding = ActivityAddressWebviewBinding.inflate(inflater, container, false)

        // 핸들러를 통한 JavaScript 이벤트 반응
        handler = Handler()

        //Controller 설정

        init_webView()
//        binding.buttonBack.setOnClickListener(View.OnClickListener { activity!!.onBackPressed() })
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun init_webView() {
        // WebView 설정
        webView = binding.wvPage

        // JavaScript 허용
        webView!!.settings.javaScriptEnabled = true

        // JavaScript의 window.open 허용
        webView!!.settings.javaScriptCanOpenWindowsAutomatically = true

        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌

        //DOMStorage 허용
        webView!!.settings.domStorageEnabled = true
        webView!!.settings.setSupportMultipleWindows(true)

        // javascipt 의 window.open 허용.

        webView!!.settings.loadWithOverviewMode = true;
        webView!!.settings.useWideViewPort = true;

        //ssl 인증이 없는 경우 해결을 위한 부분
        webView!!.addJavascriptInterface(AndroidBridge(), "Android")
        webView!!.webChromeClient = MyWebChromeClient()

        webView!!.webViewClient = MyWebViewClient(view?.context)


            object : WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest) {
                request.grant(request.resources)
            }
        }
//        webView!!.webViewClient = object : WebViewClient() {
//            @SuppressLint("WebViewClientOnReceivedSslError")
//            override fun onReceivedSslError(
//                view: WebView?,
//                handler: SslErrorHandler?,
//                error: SslError?
//            ) {
//                // SSL 에러가 발생해도 계속 진행
//                handler?.proceed()
//            }
//
//            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//                view?.loadUrl(url!!)
//                return true
//            }
//
//            // 페이지 로딩 시작시 호출
//            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
//                Log.e("페이지 시작", url.toString())
//    //                binding.webProgress.setVisibility(View.VISIBLE)
//            }
//
//            override fun onPageFinished(view: WebView?, url: String?) {
//    //                binding.webProgress.setVisibility(View.GONE)
//                Log.e("페이지 로딩", url!!)
//                    webView?.loadUrl("javascript:sample2_execDaumPostcode();")
//
//            }
//            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
//
//                return super.shouldOverrideUrlLoading(view, request)
//            }
//            override fun onReceivedError(
//                view: WebView,
//                request: WebResourceRequest,
//                error: WebResourceError
//            ) {
//                super.onReceivedError(view, request, error)
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    //Log.i(TAG, "onReceivedError() " + error.getErrorCode() + " ---> " + error.getDescription());
//                    onReceivedError(error.errorCode, error.description.toString())
//                }
//            }
//        }

        // webview url load. php or html 파일 주소
        webView!!.loadUrl("https://www.supercarlounge.com/wv/spot/road_search.php")
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

    inner class AndroidBridge {

        @JavascriptInterface
        fun ADDRESS(address: String?,latitude: String?,longitude: String?) {


            Log.d("콜백", address.toString())
            Log.d("콜백", latitude.toString())
            Log.d("콜백", longitude.toString())

                handler!!.post {
                    event(address.toString(), latitude.toString(), longitude.toString())


                    dismiss()
                }


            }

    }
    override fun dismiss() {
        super.dismiss()

    }
    private fun onReceivedError(errorCode: Int, description: String) {

        when (errorCode) {
            WebViewClient.ERROR_TIMEOUT, WebViewClient.ERROR_CONNECT, WebViewClient.ERROR_FILE_NOT_FOUND, WebViewClient.ERROR_HOST_LOOKUP, WebViewClient.ERROR_UNSUPPORTED_AUTH_SCHEME, WebViewClient.ERROR_AUTHENTICATION, WebViewClient.ERROR_PROXY_AUTHENTICATION, WebViewClient.ERROR_IO, WebViewClient.ERROR_REDIRECT_LOOP, WebViewClient.ERROR_UNSUPPORTED_SCHEME, WebViewClient.ERROR_FAILED_SSL_HANDSHAKE, WebViewClient.ERROR_BAD_URL, WebViewClient.ERROR_FILE, WebViewClient.ERROR_TOO_MANY_REQUESTS, WebViewClient.ERROR_UNSAFE_RESOURCE -> {

                Log.e("", "WebViewClient,onReceivedError($errorCode) 에러 발생 ")
            }
            else -> {}
        }
    }
}
