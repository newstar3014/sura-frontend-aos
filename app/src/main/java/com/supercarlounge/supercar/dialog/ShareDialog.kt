package com.supercarlounge.supercar.dialog

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link
import com.supercarlounge.supercar.DialogEvent
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.databinding.*
import com.supercarlounge.supercar.enumset.ShareDialogType


class ShareDialog(
    context: Context,
    var type: ShareDialogType,
    var event: (ShareDialogType, Boolean) -> Unit
) : DialogFragment() {
    var baseUrl = ""
    var TAG = "UpdateDialog"
    var binding: DialogShareBinding? = null
    var mListener: DialogEvent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        baseUrl =  getString(R.string.main_url)
    }

    override fun getTheme() = R.style.RoundedCornersDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_share,
            null,
            false
        );
        return binding!!.root
    }

    override fun onResume() {
        super.onResume()
        setDialogWindowSize()
    }

    //다이얼로그 사이즈 초기화
    private fun setDialogWindowSize() {

        val metrisc: DisplayMetrics = resources.displayMetrics
        val displayPW = metrisc.widthPixels
        val displayPH = metrisc.heightPixels
        val window = dialog!!.window
        window!!.setLayout(displayPW, displayPH)
        var x = 0
        var y = 0


        x = (displayPW * 0.65).toInt()
        y = (displayPH * 0.3).toInt()


        var params = FrameLayout.LayoutParams(x, y)
        params.gravity = Gravity.CENTER
        binding!!.llDialog.layoutParams = params
    }


    override fun dismiss() {
        super.dismiss()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        binding!!.setLifecycleOwner(this)

        onclickListers()

    }




    private fun onclickListers() {
        binding!!.ivKakao.setOnClickListener {
            kakaoshare()

        }
        binding!!.ivSms.setOnClickListener {
            smsshare()
        }
        binding!!.ivGmail.setOnClickListener {

            mailshare()
        }
        binding!!.tvCopy.setOnClickListener {
            val clipboardManager = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("CODE", getString(R.string.main_url))
            clipboardManager!!.setPrimaryClip(clipData)
            Toast.makeText(requireContext(), "내용이 복사 됐습니다.", Toast.LENGTH_SHORT).show()
        }
        binding!!.ok.setOnClickListener{
            dismiss()
        }
    }

    private fun smsshare() {

        val intent = Intent(Intent.ACTION_SENDTO)
        intent.putExtra("sms_body", "슈퍼카 라운지와 함께 하세요 \n $baseUrl")
        intent.data = Uri.parse("smsto:")
        startActivity(intent)
    }

    private fun mailshare() {

        val emailIntent = Intent(Intent.ACTION_SEND)

        try {
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(""))
            emailIntent.type = "text/html"
            emailIntent.setPackage("com.google.android.gm")
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "슈퍼카 라운지 공유");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "슈퍼카 라운지와 함께 하세요 \n$baseUrl");
            if (emailIntent.resolveActivity(requireContext().packageManager) != null) startActivity(emailIntent)
            startActivity(emailIntent)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            emailIntent.type = "text/html"
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(""))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "슈퍼카 라운지 공유");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "슈퍼카 라운지와 함께 하세요 \n$baseUrl");
            startActivity(Intent.createChooser(emailIntent, "Send Email"))
        }
//        ShareCompat.IntentBuilder.from(this)
//            .setChooserTitle("제목")
//            .setType("message/rfc822")
//            .setText("공유될 텍스트")
//            .startChooser()
    }
    private fun kakaoshare() {


        val defaultFeed = FeedTemplate(
            content = Content(
                title = "SuperCar Lounge",
                description = "슈퍼카 라운지",
                imageUrl = "https://supercarlounge.com/img/dynamic_img3.png",
                link = Link(
                    webUrl = baseUrl,
                    mobileWebUrl = baseUrl
                )
            ),


            buttons = listOf(

                Button(
                    "앱으로 보기",
                    Link(
                        androidExecutionParams = mapOf(baseUrl to baseUrl),

                        )
                )
            )
        )

        if (ShareClient.instance.isKakaoTalkSharingAvailable(requireContext())) {
            // 카카오톡으로 카카오톡 공유 가능
            ShareClient.instance.shareDefault(requireContext(), defaultFeed) { sharingResult, error ->
                if (error != null) {
                    Log.e("TAG", "카카오톡 공유 실패", error)
                } else if (sharingResult != null) {
                    Log.d("TAG", "카카오톡 공유 성공 ${sharingResult.intent}")
                    startActivity(sharingResult.intent)

                    // 카카오톡 공유에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                    Log.w("TAG", "Warning Msg: ${sharingResult.warningMsg}")
                    Log.w("TAG", "Argument Msg: ${sharingResult.argumentMsg}")
                }
            }
        } else {
            Toast.makeText(requireContext(), "카카오톡앱을 설치해주세요.", Toast.LENGTH_SHORT).show()
            // 카카오톡 미설치: 웹 공유 사용 권장
            // 웹 공유 예시 코드
//            val sharerUrl = WebSharerClient.instance.makeDefaultUrl(defaultFeed)
//
//            // CustomTabs으로 웹 브라우저 열기
//
//            // 1. CustomTabsServiceConnection 지원 브라우저 열기
//            // ex) Chrome, 삼성 인터넷, FireFox, 웨일 등
//            try {
//                KakaoCustomTabsClient.openWithDefault(this, sharerUrl)
//            } catch(e: UnsupportedOperationException) {
//                // CustomTabsServiceConnection 지원 브라우저가 없을 때 예외처리
//            }
//
//            // 2. CustomTabsServiceConnection 미지원 브라우저 열기
//            // ex) 다음, 네이버 등
//            try {
//                KakaoCustomTabsClient.open(context, sharerUrl)
//            } catch (e: ActivityNotFoundException) {
//                // 디바이스에 설치된 인터넷 브라우저가 없을 때 예외처리
//            }
        }
    }
}