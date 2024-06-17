package com.supercarlounge.supercar.ui.activity

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.supercarlounge.supercar.WebPush
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.databinding.ActivityLoginpassBinding
import com.supercarlounge.supercar.viewmodel.LoginPassViewModel
import java.net.URISyntaxException


class LoginPassActivity : BaseActivitiy() {
    private lateinit var binding: ActivityLoginpassBinding
    private lateinit var viewModel: LoginPassViewModel
    var webView: WebView? = null
    var websettings: WebSettings? = null
    val URL_INFO ="https://supercarlounge.com/nice/index.php"
    var dialog: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.fitsSystemWindows=true
        binding = ActivityLoginpassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

    }
    fun init() {
        webView = binding!!.wvPage
        websettings = webView!!.settings
        websettings!!.javaScriptEnabled = true //웹페이지 자바스크립트 허용여부
        websettings!!.domStorageEnabled = true //로컬 저장소 허용여부
        websettings!!.javaScriptCanOpenWindowsAutomatically = true //로컬 저장소 허용여부
        websettings!!.cacheMode = WebSettings.LOAD_NO_CACHE
        websettings!!.layoutAlgorithm=WebSettings.LayoutAlgorithm.NORMAL
        websettings!!.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        websettings!!.loadsImagesAutomatically = true
        websettings!!.builtInZoomControls = true
        websettings!!.setSupportZoom(true)
        websettings!!.setSupportMultipleWindows(true)
        websettings!!.loadWithOverviewMode = true //메타태그 허용여부
        websettings!!.useWideViewPort = true //
        webView!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                var url = view!!.url!!


                if (url.startsWith("intent://")) {
                    var intent: Intent? = null
                    try {
                        intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                        intent?.let { startActivity(it) }
                    } catch (e: URISyntaxException) {
                        //URI 문법 오류 시 처리 구간
                    } catch (e: ActivityNotFoundException) {
                        val packageName = intent!!.getPackage()
                        if (packageName != "") {
                            // 앱이 설치되어 있지 않을 경우 구글마켓 이동
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=$packageName")
                                )
                            )
                        }
                    }
                    //return  값을 반드시 true로 해야 합니다.
                    return true
                } else if (url.startsWith("https://play.google.com/store/apps/details?id=") || url.startsWith(
                        "market://details?id="
                    )
                ) {
                    //표준창 내 앱설치하기 버튼 클릭 시 PlayStore 앱으로 연결하기 위한 로직
                    val uri = Uri.parse(url)
                    val packageName = uri.getQueryParameter("id")
                    if (packageName != null && packageName != "") {
                        // 구글마켓 이동
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=$packageName")
                            )
                        )
                    }
                    //return  값을 반드시 true로 해야 합니다.
                    return true
                }
                //return  값을 반드시 false로 해야 합니다.
                return false
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                super.onReceivedSslError(view, handler, error)
            }
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }


        }
        webView!!.webChromeClient = object : WebChromeClient() {
            override fun onCreateWindow(
                view: WebView?,
                isDialog: Boolean,
                isUserGesture: Boolean,
                resultMsg: Message?
            ): Boolean {


                val newWebView = WebView(this@LoginPassActivity)
                val nwebsettings = newWebView.settings
                nwebsettings!!.javaScriptEnabled = true //웹페이지 자바스크립트 허용여부
                nwebsettings!!.domStorageEnabled = true //로컬 저장소 허용여부
                nwebsettings!!.javaScriptCanOpenWindowsAutomatically = true //로컬 저장소 허용여부
                nwebsettings!!.cacheMode = WebSettings.LOAD_NO_CACHE
                nwebsettings!!.layoutAlgorithm=WebSettings.LayoutAlgorithm.NORMAL
                nwebsettings!!.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                nwebsettings!!.loadsImagesAutomatically = true
                nwebsettings!!.builtInZoomControls = true
                nwebsettings!!.setSupportZoom(true)
                newWebView!!.addJavascriptInterface(WebPush(this@LoginPassActivity), "Android");
                nwebsettings!!.setSupportMultipleWindows(true)
                nwebsettings!!.loadWithOverviewMode = true //메타태그 허용여부
                nwebsettings!!.useWideViewPort = true //
                dialog = Dialog(this@LoginPassActivity)
                dialog?.setContentView(newWebView)
                dialog?.setOnDismissListener {
                    Log.d("pagefinish","finish")
                    finish()
                }
                val params: ViewGroup.LayoutParams = dialog!!.getWindow()!!.getAttributes()
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.height = ViewGroup.LayoutParams.MATCH_PARENT
                dialog?.getWindow()?.setAttributes(params as WindowManager.LayoutParams)

                dialog?.show()
                newWebView!!.webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        var url = view!!.url!!


                        if (url.startsWith("intent://")) {
                            var intent: Intent? = null
                            try {
                                intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                                intent?.let { startActivity(it) }
                            } catch (e: URISyntaxException) {
                                //URI 문법 오류 시 처리 구간
                            } catch (e: ActivityNotFoundException) {
                                val packageName = intent!!.getPackage()
                                if (packageName != "") {
                                    // 앱이 설치되어 있지 않을 경우 구글마켓 이동
                                    startActivity(
                                        Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("market://details?id=$packageName")
                                        )
                                    )
                                }
                            }
                            //return  값을 반드시 true로 해야 합니다.
                            return true
                        } else if (url.startsWith("https://play.google.com/store/apps/details?id=") || url.startsWith(
                                "market://details?id="
                            )
                        ) {
                            //표준창 내 앱설치하기 버튼 클릭 시 PlayStore 앱으로 연결하기 위한 로직
                            val uri = Uri.parse(url)
                            val packageName = uri.getQueryParameter("id")
                            if (packageName != null && packageName != "") {
                                // 구글마켓 이동
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("market://details?id=$packageName")
                                    )
                                )
                            }
                            //return  값을 반드시 true로 해야 합니다.
                            return true
                        }

                        //return  값을 반드시 false로 해야 합니다.

                        //return  값을 반드시 false로 해야 합니다.
                        return false
                    }
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)

                    }


                }
                newWebView!!.webChromeClient = object:WebChromeClient() {
                    override fun onCreateWindow(
                        view: WebView?,
                        isDialog: Boolean,
                        isUserGesture: Boolean,
                        resultMsg: Message?
                    ): Boolean {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(view!!.url))

                        startActivity(intent)
                        return false
                    }


                }
                (resultMsg!!.obj as WebView.WebViewTransport).webView = newWebView
                resultMsg!!.sendToTarget()
                return true
            }
            override fun onCloseWindow(window: WebView?) {
                super.onCloseWindow(window)

            }
        }
        webView!!.loadUrl(URL_INFO);
    }
}
