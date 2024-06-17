package com.supercarlounge.supercar.ui.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.firebase.dynamiclinks.ktx.*
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.template.model.*
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.DriveLocationAdapter
import com.supercarlounge.supercar.adapter.LocationDescAdapter
import com.supercarlounge.supercar.adapter.LocationSpotHoriAdapter
import com.supercarlounge.supercar.adapter.PopInfoPagerAdapter
import com.supercarlounge.supercar.adapter.SpotImagePagerAdapter
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.databinding.ActivityLocationMainDetailBinding
import com.supercarlounge.supercar.databinding.DialogBottomSheetRegionBinding
import com.supercarlounge.supercar.databinding.DialogHelpBottomQuestionBallonBinding
import com.supercarlounge.supercar.databinding.DialogRightQuestionBallonBinding
import com.supercarlounge.supercar.dialog.QuickUserDialog
import com.supercarlounge.supercar.enumset.QuickTypeStatus
import com.supercarlounge.supercar.viewmodel.AlimViewModel
import com.supercarlounge.supercar.viewmodel.LocationSpotDetailViewModel


class LocationSpotDetailActivity: BaseActivitiy() {
    private lateinit var binding: ActivityLocationMainDetailBinding
    private lateinit var viewModel: LocationSpotDetailViewModel
    private lateinit var application: MainApplication
    var pageradapter:SpotImagePagerAdapter? = null
    private var quickUserDialog: QuickUserDialog?= null
    var menupopup:PopupWindow? = null
    var ada2 :LocationDescAdapter? =null
    var ada1 :LocationDescAdapter? =null
    var initview =false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LocationSpotDetailViewModel::class.java)
        application = applicationContext as MainApplication
        viewModel.my_seq.value = application!!.userData?.u_seq.toString()
        var seq = intent.getStringExtra("spot_seq")

        viewModel.location_seq.value = seq
        Log.d("이승주푸시 seq", viewModel.location_seq.value.toString())
        viewModel.spotseq.value = seq
        binding = ActivityLocationMainDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding!!.viewModel = viewModel
        binding.lifecycleOwner = this
        setadapters()
        setonclick()
        setobserve()

    }
    fun setadapters(){
        ada1 = LocationDescAdapter(this@LocationSpotDetailActivity,viewModel.descdata.value!!){data,index ->

       }
        binding!!.rvDesc.adapter  = ada1
        binding!!.rvDesc.layoutManager = LinearLayoutManager(this)
        ada2 = LocationDescAdapter(this@LocationSpotDetailActivity,viewModel.menudata.value!!){data,index ->

        }
        binding!!.rvMenu.adapter  = ada2
        binding!!.rvMenu.layoutManager = LinearLayoutManager(this)

        if (binding.rvMenu.adapter?.itemCount!! > 5){

        }




        pageradapter = SpotImagePagerAdapter(
            this, viewModel.subimagelist!!.value!!
        ) { data, position ->
        }



        binding!!.pager.adapter = pageradapter

        binding!!.pager.addOnPageChangeListener(object :
            ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                // Check if this is the page you want.


                viewModel.position.value = position


            }
        })
        binding!!.rvDrive.adapter = DriveLocationAdapter(this,viewModel.drivelist.value!!){data,index->
            if (application!!.quick_view.equals("퀵유저")) {
                ShowQuickUserDialog()
            } else {
                val i = Intent(this@LocationSpotDetailActivity, DriveDetailActivity::class.java)
                i.putExtra("data", data)
                i.putExtra("totalCount", viewModel.totalCountDrive.value)
                i.putExtra("page", viewModel.totalPageDrive.value)
                i.putParcelableArrayListExtra("allList", viewModel.drivelist.value)
                i.putExtra("driveType", "notvip")
                i.putExtra("position", index)
                startActivity(i)
            }
        }
        binding!!.rvDrive.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

        binding!!.rvRecentspot.adapter = LocationSpotHoriAdapter(this,viewModel.recentspotlist.value!!){data,index->
            if (application!!.quick_view.equals("퀵유저")) {
                ShowQuickUserDialog()
            } else {
                var i =
                    Intent(this@LocationSpotDetailActivity, LocationSpotDetailActivity::class.java)
                i.putExtra("spot_seq", data.spot_seq)
                startActivity(i)
            }
        }
        binding!!.rvRecentspot.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
    }

    fun setonclick(){
        binding!!.btnPrv.setOnClickListener {
            var now = viewModel.position.value!!
            if (now != 0) {
                binding!!.pager.currentItem =  now - 1

            }
        }
        binding.llInquiry.setOnClickListener{
            if (application!!.quick_view.equals("퀵유저")) {
                ShowQuickUserDialog()
            } else {
                mailshare(viewModel.detaildata.value?.spot_title.toString())
            }
        }
        binding!!.btnNext.setOnClickListener {
            var now = viewModel.position.value!!
            if (now != viewModel.subimagelist.value!!.size-1) {
                binding!!.pager.currentItem  = now + 1
            }
        }
        binding!!.btnCopy.setOnClickListener {
            if (application!!.quick_view.equals("퀵유저")) {
                ShowQuickUserDialog()
            } else {
                var address = viewModel.detaildata.value!!.spot_address
                val clipboardManager =
                    getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("ADRRESS", address)
                clipboardManager!!.setPrimaryClip(clipData)
                viewModel.toast.value = "주소가 복사되었습니다."
            }
        }
        binding!!.ivBack.setOnClickListener {
            finish()
        }
        binding!!.llBottomBar.setOnClickListener {
            if (application!!.quick_view.equals("퀵유저")) {
                ShowQuickUserDialog()
            } else {

                var i = Intent(this, NewDriveWritingActivity::class.java)
                i.putExtra("writingType", "location")
                i.putExtra("location_seq", viewModel.location_seq.value)
                startActivity(i)
            }


        }
        binding!!.ivBookmark.setOnClickListener {
                        if (application!!.quick_view.equals("퀵유저")) {
                ShowQuickUserDialog()
            } else {
                            viewModel.bookmark.value =
                                if (viewModel.bookmark.value!!) false else true
                            if (viewModel.bookmark.value == true) {
                                viewModel.boockCount.value = viewModel.boockCount.value?.plus(1)
                                viewModel.tvBoockCount.value = "북마크 ${viewModel.boockCount.value}"

                            } else {
                                viewModel.boockCount.value = viewModel.boockCount.value?.minus(1)
                                viewModel.tvBoockCount.value = "북마크 ${viewModel.boockCount.value}"
                            }

                            viewModel.InsertBookmark()
                        }
        }
        binding!!.btn1.setOnClickListener {
            if (application!!.quick_view.equals("퀵유저")) {
                ShowQuickUserDialog()
            } else {
                if (viewModel.detaildata.value != null) {
                    var phone = viewModel.detaildata.value!!.spot_phone
                    val number: Uri = Uri.parse("tel:" + phone)
                    val callIntent = Intent(Intent.ACTION_DIAL, number)
                    startActivity(callIntent)
                }
            }
        }
        binding!!.btn2.setOnClickListener {
            if (application!!.quick_view.equals("퀵유저")) {
                ShowQuickUserDialog()
            } else {
                StartNaverMap()
            }
        }
        binding!!.btn3.setOnClickListener {
            if (application!!.quick_view.equals("퀵유저")) {
                ShowQuickUserDialog()
            } else {
                viewModel.toast.value = "준비중 입니다."
            }
        }
        binding!!.btn4.setOnClickListener {
            if (application!!.quick_view.equals("퀵유저")) {
                ShowQuickUserDialog()
            } else {
                onDynamicLinkClick(this,"")
//                viewModel.toast.value = "준비중 입니다."
//                kakaoshare()
            }
        }
    }
    fun setobserve(){
        viewModel.toast.observe(this, Observer {
            if(it.isNullOrEmpty() == false){
                Toast.makeText(this@LocationSpotDetailActivity,it,Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.subimagelist.observe(this, Observer {
            if(it.size != 0){
                if (!initview){
                    if(!application!!.locationDetailPop){
                        application!!.locationDetailPop = true
                        setpop(this,binding.ivBookmark,"북마크를 등록하고 알림을 받아보세요!")
                        initview = true
                    }
                }
                pageradapter!!.addAllItem(it)
            }
        })

        viewModel.location_seq.observe(this, Observer {
            if (it.isNotEmpty()){
                viewModel.getSpotDetail()
            }
        })
        viewModel.menudata.observe(this, Observer {
            if (it.size != 0){

                if (it.size > 5){
                    ada2?.setDataSize(5)
                    viewModel.tvmore.value = "더보기"
                }else{
                    ada2?.setDataSize(it.size)
                }

            }
        })
        viewModel.descdata.observe(this, Observer {
            if (it.size != 0){

                    ada1?.setDataSize(it.size)



            }
        })

        viewModel.eventMore.observe(this, Observer {
            if (it){
                if (viewModel.tvmore.value.equals("더보기")){
                    viewModel.tvmore.value = "접기"

                    viewModel.menudata.value?.size?.let { it1 -> ada2?.setDataSize(it1) }
                }else{
                    viewModel.tvmore.value = "더보기"
                    ada2?.setDataSize(5)
                }
            }
        })
    }

    private fun mailshare(text :String) {

        val emailIntent = Intent(Intent.ACTION_SEND)
        val eMailId1 = "help@supercarlounge.com"
        var eMailIds: Array<kotlin.String> = arrayOf(eMailId1)
        try {
            emailIntent.putExtra(Intent.EXTRA_EMAIL, eMailIds)
            emailIntent.type = "text/html"
            emailIntent.setPackage("com.google.android.gm")
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "슈퍼카 라운지 로케이션");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "$text  로케이션에 대한 정보수정 요청을 남겨주세요\n");
            if (emailIntent.resolveActivity(packageManager) != null) startActivity(emailIntent)
            startActivity(emailIntent)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            emailIntent.type = "text/html"
            emailIntent.putExtra(Intent.EXTRA_EMAIL, eMailIds)
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "슈퍼카 라운지 로케이션");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "$text  로케이션에 대한 정보수정 요청을 남겨주세요\n");
            startActivity(Intent.createChooser(emailIntent, "Send Email"))
        }
//        ShareCompat.IntentBuilder.from(this)
//            .setChooserTitle("제목")
//            .setType("message/rfc822")
//            .setText("공유될 텍스트")
//            .startChooser()
    }
    override fun onResume() {
        super.onResume()
        viewModel.getSpotDetail()
    }
    private fun appInstalledOrNot(packagename: String): Boolean {
        val pm: PackageManager = packageManager
        try {
            pm.getPackageInfo(packagename,0)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
        }
        return false
    }
    private fun ShowQuickUserDialog() {
        quickUserDialog =
            QuickUserDialog(this, QuickTypeStatus.QUICK_WARNING) { type, okcancel ->
                if (okcancel) {
                    quickUserDialog!!.dismiss()
                    val i = Intent(this, JoinActivity::class.java)
                    i.putExtra("qickview", "퀵유저")
                    i.putExtra("u_name", application?.userBasicData?.u_name)
                    i.putExtra("u_gender", application?.userBasicData?.u_gender)
                    i.putExtra("u_birthday", application?.userBasicData?.u_birthday)
                    i.putExtra("u_phone", application?.userBasicData?.u_phone)
                    Log.d("아이템3", application?.userBasicData.toString())
                    startActivity(i)
                } else {
                    quickUserDialog!!.dismiss()
                }
            }
        quickUserDialog!!.show(supportFragmentManager, "")
    }


    fun setpop(con: Context, view: View, question:String){

        val originalPos = IntArray(2)
        view.getLocationInWindow(originalPos)
        val x = originalPos[0]
        val y = originalPos[1]
        val metrisc: DisplayMetrics = resources.displayMetrics
        val displayPH = metrisc.heightPixels
        var radiusH = displayPH/2
        var maginH= y/4
        var h =radiusH - maginH
        var yy =maginH
        Log.d("해상도", maginH.toString())

        var popbinding: DialogHelpBottomQuestionBallonBinding = DataBindingUtil.inflate(
            LayoutInflater.from(con),
            com.supercarlounge.supercar.R.layout.dialog_help_bottom_question_ballon,
            null,
            false
        )
        popbinding.tvQuestion.text = question
        menupopup = PopupWindow(
            popbinding.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )
        menupopup?.showAtLocation(view, Gravity.RIGHT, 0,   -yy.toInt())
        PopupAutoClose()


    }
    private fun PopupAutoClose() {
        val handler = Handler()
        val mHandler = Handler(Looper.getMainLooper())

        mHandler.removeCallbacksAndMessages(null);
        mHandler.postDelayed({
            val msg = handler.obtainMessage()
            handler.sendMessage(msg)
            menupopup!!.dismiss()
        }, 3000)

    }
    val handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            if (menupopup != null){
                if (menupopup!!.isShowing){
                    menupopup!!.dismiss()
                }
            }
        }
    }
    fun StartNaverMap(){
        var point_lat = viewModel.detaildata.value?.spot_lat
        var point_lon = viewModel.detaildata.value?.spot_lon
        if(appInstalledOrNot("com.nhn.android.nmap")) {
            var i = Intent(Intent.ACTION_VIEW, Uri.parse("navermaps://?menu=location&pinType=place&lat=${point_lat}&lng=${point_lon}&title=${viewModel.detaildata.value?.spot_title}")).apply {
                `package` = "com.nhn.android.nmap"
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(i)
        }else{
            Toast.makeText(this,"해당앱이 없습니다", Toast.LENGTH_SHORT).show()
        }
    }
    fun StartKaKaoMap(){
        if(appInstalledOrNot("net.daum.android.map")) {
            var point_lat = viewModel.detaildata.value?.spot_lat
            var point_lon = viewModel.detaildata.value?.spot_lon
            var i = Intent(Intent.ACTION_VIEW, Uri.parse("kakaomap://look?p=${point_lat},${point_lon}")).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(i)
        }else{
            Toast.makeText(this,"해당앱이 없다요", Toast.LENGTH_SHORT).show()
        }

    }

    fun StartTmap(){
        if(appInstalledOrNot("com.skt.tmap.ku")) {
            var point_lat = viewModel.detaildata.value?.spot_lat
            var point_lon = viewModel.detaildata.value?.spot_lon
            var i = Intent(Intent.ACTION_VIEW, Uri.parse("tmap://route?goalx=${point_lon}&goaly=${point_lat}&goalname=${title}")).apply {
                `package` = "com.skt.tmap.ku"
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(i)
        }else{
            Toast.makeText(this,"해당앱이 없다요", Toast.LENGTH_SHORT).show()
        }
    }
    private fun kakaoshare() {


        val defaultFeed = FeedTemplate(
            content = Content(
                title = "SuperCar Lounge",
                description = viewModel.detaildata.value?.spot_title,
                imageUrl = viewModel.detaildata.value?.spot_main_image.toString(),
                link = Link(
//                   webUrl = baseUrl,
//                  mobileWebUrl = baseUrl),
                )
            ),

            buttons = listOf(

                Button(
                    "앱으로 보기",
                    Link(
                        androidExecutionParams = mapOf("data" to "1"),

                        )
                )
            )
        )

        if (ShareClient.instance.isKakaoTalkSharingAvailable(this)) {
            // 카카오톡으로 카카오톡 공유 가능
            ShareClient.instance.shareDefault(this, defaultFeed) { sharingResult, error ->
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
            Toast.makeText(this, "카카오톡앱을 설치해주세요.", Toast.LENGTH_SHORT).show()
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
    fun onDynamicLinkClick(context: Context,code:String) {

        var img= Uri.parse("https://supercarlounge.com/img/dynamic_img3.png")


        var text2=""
        val dynamicLink = Firebase.dynamicLinks.shortLinkAsync{
            link = Uri.parse("https://supercarnative.page.link/rniX/Location?seq= ${viewModel.detaildata.value?.spot_seq.toString()}")
            domainUriPrefix = "https://supercarnative.page.link"
            // Open links with this app on Android
            androidParameters { }
            // Open links with com.example.ios on iOS
            iosParameters("com.supercarlounge.supercar") { }
            socialMetaTagParameters {
                title = viewModel.detaildata.value?.spot_title.toString()
                description = viewModel.detaildata.value?.spot_detail.toString()
                imageUrl= viewModel.detaildata.value?.spot_main_image?.toUri()!!
            }
        }
        dynamicLink.addOnCompleteListener{
            if(it.isSuccessful) {
                var shortLink = it.getResult().getShortLink();


                var sendIntent = Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, shortLink.toString());
                sendIntent.putExtra(Intent.EXTRA_TEXT, text2);
                sendIntent.setType("text/plain");




                context.startActivity(Intent.createChooser(sendIntent, "Share"));
            }

        }

    }
}