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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.dynamiclinks.ktx.*
import com.google.firebase.ktx.Firebase
import com.supercarlounge.supercar.Constans.Companion.BRIDGE_MOVE_NOTICE
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.WebBridge
import com.supercarlounge.supercar.WebPush
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.databinding.ActivityWebviewBinding
import com.supercarlounge.supercar.`interface`.PaymentCallback
import com.supercarlounge.supercar.`interface`.WebCallback
import com.supercarlounge.supercar.viewmodel.MatchingViewModel
import com.supercarlounge.supercar.viewmodel.WebViewViewModel
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogBlockPostViewModel
import java.net.URISyntaxException


class WebViewActivity : BaseActivitiy() {
    var application:MainApplication ? = null
    private lateinit var binding: ActivityWebviewBinding
    var viewModel: WebViewViewModel? = null
    var webView: WebView? = null
    var websettings: WebSettings? = null
    var URL_INFO =""
    var dialog: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.fitsSystemWindows=true
        application = applicationContext as MainApplication
        viewModel = ViewModelProvider(this).get(WebViewViewModel::class.java)
        viewModel?.my_seq?.value =   application!!.userData?.u_seq.toString()
        binding = ActivityWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var url = intent.getStringExtra("url")
        URL_INFO = url!!
        binding!!.lifecycleOwner = this
        init()

        viewModel!!.inviteset.observe(this, Observer {
            if(it){
                application!!.onDynamicLinkClick(this@WebViewActivity,viewModel!!.invite_code.value!!)
            }
        })
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
        websettings!!.builtInZoomControls = false
        websettings!!.setSupportZoom(true)
        websettings!!.setSupportMultipleWindows(true)
        webView!!.addJavascriptInterface(WebBridge(callback), "Android");
        websettings!!.loadWithOverviewMode = true //메타태그 허용여부
        websettings!!.useWideViewPort = true //
        websettings!!.setSupportMultipleWindows(true)
        val userAgent: String = websettings!!.getUserAgentString()
        websettings!!.setUserAgentString(userAgent+" mobile");
        webView!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                var url = request!!.url.toString()
         if (url.startsWith("intent:")) {
             if (URL_INFO.contains("lotto.php")){
                 viewModel!!.GetInviteCode()

             }else {
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

             }
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


                val newWebView = WebView(this@WebViewActivity)
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

                nwebsettings!!.setSupportMultipleWindows(true)
                nwebsettings!!.loadWithOverviewMode = true //메타태그 허용여부
                nwebsettings!!.useWideViewPort = true //
                val userAgent: String = nwebsettings!!.getUserAgentString()
                nwebsettings!!.setUserAgentString(userAgent+" mobile");
                dialog = Dialog(this@WebViewActivity)
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
                      //  var url = view!!.url!!
                        var url = request!!.url.toString()
                     if (url.startsWith("intent:")) {
                         if (URL_INFO.contains("lotto.php")){
                             viewModel!!.GetInviteCode()
                         }else {
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

                         }
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

    var callback = object : WebCallback {
        override fun MoveNotice() {
          setResult(BRIDGE_MOVE_NOTICE)
            finish()
            Log.d("CheckMOVE","MOVE")
        }

        override fun WebViewFinish() {
            finish()
        }

    }
}
