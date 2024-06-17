package com.supercarlounge.supercar.ui.activity

import FinishDialog
import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.tabs.TabLayout
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.supercarlounge.supercar.Constans
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.MainApplication.Companion.token
import com.supercarlounge.supercar.Network.MatchingService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.PagerFragmentStateAdapter
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.customview.BlurTransformation
import com.supercarlounge.supercar.data.*
import com.supercarlounge.supercar.databinding.ActivityMainBinding
import com.supercarlounge.supercar.dialog.ConciergeServiceDialog
import com.supercarlounge.supercar.dialog.QuickUserDialog
import com.supercarlounge.supercar.enumset.QuickTypeStatus
import com.supercarlounge.supercar.ui.fragment.*
import com.supercarlounge.supercar.viewmodel.MainViewModel
import com.supercarlounge.supercarnative.ui.fragment.HomeFragment2
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response


class MainActivity : BaseActivitiy() {
    private var viewModel: MainViewModel? = null
    var application: MainApplication? = null
    lateinit var binding: ActivityMainBinding
    private var viewPager: ViewPager2? = null
    var fm: FragmentManager? = null
    var pageradapter: PagerFragmentStateAdapter? = null
    private var quickUserDialog: QuickUserDialog? = null

    var view = ""
    var finishDialog: FinishDialog? = null
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            applyColors()
        }
    }
    private fun applyColors() {

        getWindow().setStatusBarColor(Color.BLACK);
        getWindow().setNavigationBarColor(Color.BLACK);
    }
    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("UseCompatLoadingForDrawables", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("체크 메인액티", "온크리에잇")

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding = ActivityMainBinding.inflate(layoutInflater)
        application = applicationContext as MainApplication
        viewModel!!.iscompany.value = application!!.CheckCompany()
        viewModel!!.my_seq.value = application!!.userData?.u_seq.toString()
        binding!!.viewModel = viewModel
        setContentView(binding.root)
        binding!!.lifecycleOwner = this
        fm = supportFragmentManager
        application!!.startLocationUpdates()


        var u_phone = intent.getStringExtra("u_phone")
        var screen = intent.getStringExtra("screen").toString()
        Log.d("푸시 스크린", screen.toString())

        if (intent.getStringExtra("pushData") != null) {
            var pushData = intent.getStringExtra("pushData")
            Log.d("푸시 pushData", pushData.toString())

            try {
                if (screen.isNotEmpty()) {
                    val obj: JsonObject = JsonParser.parseString(pushData).getAsJsonObject()

                    when(screen){
                        "ApplyProfile","SendProfile" ,"ReadProfile","SendLike","GivenLike","AcceptLike","SendOpenPhone","OpenPhone","GivenDrive" ->{


                            var send_seq = obj.get("send_seq").toString().replace("\"", "")
                            var intent = Intent(this, ProfileActivity::class.java);
                            intent!!.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            intent.putExtra("screen", screen)
                            intent.putExtra("sub_seq", send_seq)
                            startActivity(intent)

                        }
                        "SendProfile","SendDrive"->{
                            var send_seq = obj.get("send_seq").toString().replace("\"", "")
                            getMatchingDataYN(obj,application?.userData?.u_seq.toString(),send_seq)

                        }
                        "BoardComment","BoardSubComment","BoardLike" ->{

                            var b_seq = obj.get("b_seq").toString().replace("\"", "")
                            var send_seq = obj.get("send_seq").toString().replace("\"", "")
                            var uid = obj.get("uid").toString().replace("\"", "")
                            var intent = Intent(this, PostActivity::class.java);
                            intent!!.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            intent.putExtra("screen", screen)
                            intent.putExtra("sub_seq", send_seq)
                            intent.putExtra("u_seq", send_seq.toInt())
                            intent.putExtra("b_seq", b_seq)
                            intent.putExtra("uid", uid)
                            startActivity(intent)
                        }
                        "LocationDrive"->{

                            var b_seq = obj.get("b_seq").toString().replace("\"", "")
                            var intent = Intent(this, LocationSpotDetailActivity::class.java);
                            intent!!.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            intent.putExtra("screen", screen)
                            intent.putExtra("spot_seq", b_seq)
                            startActivity(intent)
                            Log.d("이승주푸시 seq", b_seq)
                        }

                        "MycarComment","MycarSubComment","MycarLike" ->{
                            var b_seq = obj.get("b_seq").toString().replace("\"", "")
                            var send_seq = obj.get("send_seq").toString().replace("\"", "")
                            var intent = Intent(this, MyCarBoardActivity::class.java);
                            intent!!.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            intent.putExtra("mmi_seq", b_seq.toInt())
                            intent.putExtra("screen", screen)
                            intent.putExtra("sub_seq", send_seq)
                            intent.putExtra("u_seq", send_seq.toInt())
                            startActivity(intent)
                        }

                        "DriveLike" ->{
                            var b_seq = obj.get("b_seq").toString().replace("\"", "")
                            var send_seq = obj.get("send_seq").toString().replace("\"", "")
                            var uid = obj.get("uid").toString().replace("\"", "")
                            DrivePassCheck(screen, send_seq, b_seq, uid)
                        }
                        "MycarRankingHeart" ->{
                            var intent = Intent(this, MyCarBestBoardActivity::class.java);
                            startActivity(intent)
                        }
                        "DrivePass30Buy","DrivePass1Buy","HeartCharge" ->{
                            var intent = Intent(this, HeartsShopActivity::class.java);
                            startActivity(intent)
                        }
                        else -> {}
                    }

                }

            } catch (e: java.lang.Exception) {
                Log.d("Exception", e.toString())
            }

        }



        view = intent.getStringExtra("view").toString()
        application!!.userBasicData?.u_name = intent.getStringExtra("u_name").toString()
        application!!.userBasicData?.u_gender = intent.getStringExtra("u_gender").toString()
        application!!.userBasicData?.u_birthday = intent.getStringExtra("u_birthday").toString()
        application!!.userBasicData?.u_phone = intent.getStringExtra("u_phone").toString()
        if (view.equals("퀵유저")) {
            application!!.quick_view = "퀵유저"
            token =
                "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MTIyMiwiaWF0IjoxNjY4Njg1NzE5LCJleHAiOjE2Njg3NzIxMTl9.AmNJtfXlnUsNCqq9ROfBu-nsJvsUsUiRLcYVXIgVQGk"
        }


        if (application?.u_phone?.isEmpty() == true) {
            if (u_phone != null) {
                application!!.savePhoneNumber(u_phone)
            }
        }
        setclicklistner()
        setpageadapter()
        resolution_px()
        viewModel!!.CheckLogInsert()
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val fragmentId = intent.getIntExtra("FRAGMENT_ID", -1)
        if (fragmentId == 1) {
            binding!!.tabLayout.selectTab(binding!!.tabLayout.getTabAt(fragmentId), true)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    fun getMatchingDataYN(obj:JsonObject,my_seq:String ,send_seq: String){
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(MatchingService::class.java).getMatchingDataYN(my_seq,send_seq)
            d!!.enqueue(object : retrofit2.Callback<ResultAlimYNData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultAlimYNData>,
                    response: Response<ResultAlimYNData>
                ) {
                    var data = response.body()
                    var message = data?.message
                    var pcDateCheck = data?.rows
                    if (data != null) {
                        Log.d("알림 테스트", data.toString())
                        if (message.equals("조회 되었습니다")){
                            if (pcDateCheck.equals("true")){
                                var send_seq = obj.get("send_seq").toString().replace("\"", "")
                                var intent = Intent(this@MainActivity, ProfileActivity::class.java);
                                intent!!.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                                intent.putExtra("sub_seq", send_seq)
                                startActivity(intent)
                            }else{
                                Toast.makeText(this@MainActivity, "승인대기 중입니다.", Toast.LENGTH_SHORT).show()
                            }


                        }

                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultAlimYNData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                }

            })

        }
    }
    fun DrivePassCheck(screen: String, send_seq: String, b_seq: String, uid: String) {
        var intent: Intent? = null
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(MatchingService::class.java)
                .DrivePassCheck(application?.userData?.u_seq.toString())
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if (type.equals("success")) {


                            intent = Intent(this@MainActivity, RecentDriveListActivity::class.java);
                            intent?.putExtra("passCheck", true)

                        } else {
                            intent = Intent(this@MainActivity, RecentDriveListActivity::class.java);
                            intent?.putExtra("passCheck", false)
                            //intent = Intent(this@MainActivity, AlimActivity::class.java);

                        }

                        intent?.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        intent?.putExtra("screen", screen)
                        intent?.putExtra("sub_seq", send_seq)
                        intent?.putExtra("b_seq", b_seq)
                        intent?.putExtra("uid", uid)
                        intent?.putExtra("push", false)
                        if (intent != null) {
                            startActivity(intent)
                        }


                    }

                }

                override fun onFailure(
                    call: retrofit2.Call<ResultData>,
                    t: Throwable
                ) {
                    Log.d("check", t.toString())
                }

            })

        }
    }

    fun setclicklistner() {



        binding!!.ivVipNotice.setOnClickListener {
            if (application?.quick_view.equals("퀵유저")) {
                ShowQuickUserDialog()
            } else {
                val i = Intent(this, AlimActivity::class.java)
                startActivity(i)
                // overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }



        binding!!.ivVipDrive.setOnClickListener {
            if (application?.quick_view.equals("퀵유저")) {
                ShowQuickUserDialog()
            } else {
                val i = Intent(this, DriveVipRankActivity::class.java)
                startActivity(i)
                //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

            }
        }
        binding!!.ivVipI.setOnClickListener {
            var cond = ConciergeServiceDialog("",this, this){

            }
            cond.show(supportFragmentManager, "")
        }

        binding!!.ivHeartShop.setOnClickListener {
            if (application?.quick_view.equals("퀵유저")) {
                ShowQuickUserDialog()
            } else {
                val i = Intent(this, HeartsShopActivity::class.java)
                startActivity(i)
                //  overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

            }
        }
    }

    fun setpageadapter() {
        pageradapter = PagerFragmentStateAdapter(this as FragmentActivity)
        pageradapter!!.addFragment(HomeFragment2())
        pageradapter!!.addFragment(LocationSpotListFragment())
        pageradapter!!.addFragment(BoardFragment())
        pageradapter!!.addFragment(MyPageFragment())

        // Adapter
        binding.pager.adapter = pageradapter
        binding.pager.setCurrentItem(0, false)

        viewPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        })


        binding.pager.run {
            isUserInputEnabled = false
        }


        val view1: View = layoutInflater.inflate(R.layout.item_tab, null)
        view1.findViewById<ImageView>(R.id.imbtn)
            .setImageDrawable(getDrawable(R.drawable.selector_main_3))
        val view2: View = layoutInflater.inflate(R.layout.item_tab, null)
        view2.findViewById<ImageView>(R.id.imbtn)
            .setImageDrawable(getDrawable(R.drawable.selector_location))
//        val view3: View = layoutInflater.inflate(R.layout.item_tab, null)
//        view3.findViewById<ImageView>(R.id.imbtn)
//            .setImageDrawable(getDrawable(R.drawable.selector_main_3))
        val view4: View = layoutInflater.inflate(R.layout.item_tab, null)
        view4.findViewById<ImageView>(R.id.imbtn)
            .setImageDrawable(getDrawable(R.drawable.selector_main_4))
        val view5: View = layoutInflater.inflate(R.layout.item_tab, null)
        view5.findViewById<ImageView>(R.id.imbtn)
            .setImageDrawable(getDrawable(R.drawable.selector_main_5))
        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setCustomView(view1))
        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setCustomView(view2))
//        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setCustomView(view3))
        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setCustomView(view4))
        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setCustomView(view5))
        binding!!.tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

                Log.d("체크", "탭셀렉트 들어옴")

                var position = tab!!.position
                binding!!.pager.currentItem = position
                viewModel!!.pagerposition.value = position
                settitle(position)

                tab?.position?.let { binding.pager.setCurrentItem(it, false) }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        val tabStrip = binding!!.tabLayout.getChildAt(0) as LinearLayout
        if (view.equals("퀵유저")) {
            binding!!.tabLayout.getTabAt(3)?.view?.isClickable = false;


            tabStrip.getChildAt(0).setOnClickListener {
                ShowQuickUserDialog()

            }
            tabStrip.getChildAt(1).setOnClickListener {
                ShowQuickUserDialog()

            }

            tabStrip.getChildAt(3).setOnClickListener {
                ShowQuickUserDialog()
            }
        } else {

            tabStrip.getChildAt(2).setOnClickListener {
                if (application!!.Check30days(application!!.board_check_date) || application!!.Check1days(
                        application!!.board_check_date
                    )
                ) {

                } else {
                    var i = Intent(this, PostDriveCheckActivity::class.java)
                    i.putExtra("viewtype", "익명게시판")
                    backresultLauncher.launch(i)
                }
                (pageradapter!!.fragments.get(2) as BoardFragment).MoveBoardPage(1)

            }
        }
        binding!!.pager.offscreenPageLimit = 1


//        binding!!.tabLayout.getTabAt(2)?.view?.isSelected = true
        binding!!.tabLayout.selectTab(binding!!.tabLayout.getTabAt(0), true)
//        binding!!.tabLayout.getTabAt(0)?.view?.isSelected = false

    }

    var backresultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                binding!!.tabLayout.selectTab(binding!!.tabLayout.getTabAt(0))
            }
        }

    override fun onResume() {
        super.onResume()
        Log.d("체크 메인액티", "온리썸")
        setreceiver()
        viewModel!!.CheckAlim(application?.userData?.u_email.toString())
    }

    override fun onPause() {
        super.onPause()
        Log.d("체크 메인액티", "온퍼즈")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("체크 메인액티", "온디스트로이")
        unregisterReceiver(foregorundpushreceiver)
    }

    override fun isFinishing(): Boolean {
        return super.isFinishing()
        Log.d("MainActivity", "isFinishing: ")
    }

    private fun resolution_px() {
        val metrics = resources.displayMetrics
        val screenWidth = metrics.widthPixels
        val screenHeight = metrics.heightPixels

        val deviceName = Build.DEVICE
        val deviceDpi = metrics.densityDpi
        Log.d(
            "디바이스 정보", "디바이스 이름  : $deviceName \n" +
                    "디바이스 DPI  : $deviceDpi \n" +
                    "디바이스 넓이  : $screenWidth \n" +
                    "디바이스 높이  : $screenHeight \n"
        )
        var f = 39
        var s = 13.5
        Log.d("픽셀 정보 ", px2dp(f.toFloat()).toString() + px2dp(s.toFloat()).toString())
    }

    //dp를 px로 변환 (dp를 입력받아 px을 리턴)
    fun dp2px(dp: Float): Float {
        val resources: Resources = this.resources
        val metrics: DisplayMetrics = resources.displayMetrics

        return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    //px을 dp로 변환 (px을 입력받아 dp를 리턴)
    fun px2dp(px: Float): Float {
        val resources: Resources = this.resources
        val metrics: DisplayMetrics = resources.displayMetrics
        return px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    override fun finish() {
        super.finish()
        Log.d("MainActivity", "finish: ")
    }

    fun setreceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.package.notification")
        registerReceiver(foregorundpushreceiver, intentFilter)
    }

    private val foregorundpushreceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            var title = intent.getStringExtra("title")
            var body = intent.getStringExtra("body")
            var channel_id = intent.getStringExtra("channel_id")
            var screen = intent.getStringExtra("screen")
            var send_seq = intent.getStringExtra("send_seq")
            var b_seq = intent.getStringExtra("b_seq")
            var uid = intent.getStringExtra("uid")


//            if (channel_id != null) {

            application!!.SendNotification(
                title!!,
                body!!,
                channel_id!!,
                screen.toString(),
                send_seq.toString(),
                b_seq,
                uid.toString()
            )


//            }
            Log.d("check_noti", "checknoti")
        }
    }

    fun movepage() {
        var d = binding.tabLayout.getTabAt(2)
        d!!.select()

        (pageradapter!!.fragments.get(2) as BoardFragment).MoveBoardPage(0)
    }

    fun movepagehome() {
        var d = binding.tabLayout.getTabAt(0)
        d!!.select()
    }

    fun movepageboard() {
        var d = binding.tabLayout.getTabAt(2)
        d!!.select()

        (pageradapter!!.fragments.get(2) as BoardFragment).MoveBoardPage(1)
    }

    fun movepageMypage() {
        var d = binding.tabLayout.getTabAt(3)
        d!!.select()


    }

    companion object {
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:gif")
        @JvmStatic
        fun setloading(imageview: ImageView, id: Boolean) {
            Glide.with(imageview)
                .asGif()
                .load(R.raw.loading_anim)
                .into(imageview);
        }

        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:round_photo")
        @JvmStatic
        fun setroundphoto(imageview: ImageView, url: String?) {
            if (!url.isNullOrEmpty()) {
                Glide.with(imageview).load(url).override(imageview.width, imageview.height)
                    .centerInside().circleCrop().into(imageview)
//                Glide.with(imageview).load(url).centerInside().circleCrop()
//                    .apply(
//                        RequestOptions.bitmapTransform(
//                            BlurTransformation(this, 25, 2)
//                        )
//                    ).into(imageview);
            } else {
                Glide.with(imageview.context).load(R.drawable.logo_sura_146x146)
                    .override(imageview.width, imageview.height).circleCrop()
                    .into(imageview)
            }
        }


        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:myCarSelect")
        @JvmStatic
        fun myCarSelect(imageview: ImageView, url: String?) {
            if (!url.isNullOrEmpty()) {
                Glide.with(imageview).load(url).override(imageview.width, imageview.height)
                    .centerInside().circleCrop().into(imageview)
            }
        }

        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:round_photo_blur", "app:isblur")
        @JvmStatic
        fun setroundphotoblur(imageview: ImageView, url: String?, isblur: Boolean) {
            if (!url.isNullOrEmpty()) {
                if (isblur) {

                    Glide.with(imageview).load(url).override(imageview.width, imageview.height)
                        .circleCrop().transform(BlurTransformation(imageview.context, 25))
                        .into(imageview)
                } else {
                    Glide.with(imageview).load(url).override(imageview.width, imageview.height)
                        .centerInside().circleCrop().into(imageview)
                }
            } else {
                Glide.with(imageview.context).load(R.drawable.logo_sura_146x146)
                    .override(imageview.width, imageview.height).circleCrop()
                    .into(imageview)
            }
        }

        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("photo")
        fun setphoto(view: ImageView, url: String?) {
            // This methods should not have any return type, = declaration would make it return that object declaration.
            if (!url.isNullOrEmpty())
                Glide.with(view).load(url).override(500, 500).centerCrop()
                    .into(view)
        }

        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("MainProfile", "sub_seq")
        fun setprofile(view: ImageView, data: UserInformationData?, sub_seq: String) {
            // This methods should not have any return type, = declaration would make it return that object declaration.

            var nowImage = data?.main_url
            var pastImage = data?.main_past_img
            var application = view.context.applicationContext as MainApplication
            if (data != null) {

                Log.d("pastImage", pastImage.toString())
                Log.d("seq", sub_seq)
                Log.d("m_seq", data?.u_seq.toString())

                if (data?.u_seq.toString() == application.userData?.u_seq.toString()) {
                    if (!nowImage.isNullOrEmpty()) {
                        Glide.with(view).load(nowImage).override(300, 300).centerCrop().into(view)
                    }
                    Log.d("ㅇㅇ", "setprofile: 0")
                } else {

                    if (pastImage?.startsWith("http") == true) {
                        Glide.with(view).load(pastImage).override(300, 300).centerCrop()
                            .into(view)
                        Log.d("ㅇㅇ", "setprofile: 1")
                    } else {
                        Glide.with(view).load(nowImage).override(300, 300).centerCrop()
                            .into(view)
                        Log.d("ㅇㅇ", "setprofile: 2")
                    }


                }
            }


        }

        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("subprofile1", "sub_seq1")
        fun setsubprofile1(view: ImageView, data: UserInformationData?, sub_seq: String) {
            // This methods should not have any return type, = declaration would make it return that object declaration

            if (data?.subImage != null) {
                if (data?.subImage?.size!! > 0) {
                    var nowImage = data?.subImage[0].im_url
                    var pastImage = data?.subImage[0].im_past_img
                    Log.d("과거이미지", pastImage.toString())
                    var ap = view.context.applicationContext as MainApplication
                    if (ap.userData?.u_seq.toString() == sub_seq) {
                        if (!nowImage.isNullOrEmpty()) {
                            Glide.with(view).load(nowImage).override(500, 500).centerCrop()
                                .into(view)
                            Log.d("서브이미지", "setsubprofile0: ")
                        }

                    } else {

                        if (pastImage?.startsWith("http") == true) {
                            Glide.with(view).load(pastImage).override(500, 500).centerCrop()
                                .into(view)
                            Log.d("서브이미지", "setsubprofile1: ")
                        } else {
                            Glide.with(view).load(nowImage).override(500, 500).centerCrop()
                                .into(view)
                            Log.d("서브이미지", "setsubprofile2: ")
                        }


                    }
                }
            }


        }

        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("subprofile2", "sub_seq2")
        fun setsubprofile2(view: ImageView, data: UserInformationData?, sub_seq: String) {
            // This methods should not have any return type, = declaration would make it return that object declaration
            if (data?.subImage != null) {
                if (data?.subImage?.size!! > 1) {
                    var nowImage = data?.subImage[1].im_url
                    var pastImage = data?.subImage[1].im_past_img
                    var ap = view.context.applicationContext as MainApplication
                    if (ap.userData?.u_seq.toString() == sub_seq) {
                        if (!nowImage.isNullOrEmpty()) {
                            Glide.with(view).load(nowImage).override(500, 500).centerCrop()
                                .into(view)
                        }
                    } else {

                        if (pastImage?.startsWith("http") == true) {
                            Glide.with(view).load(pastImage).override(500, 500).centerCrop()
                                .into(view)
                        } else {
                            Glide.with(view).load(nowImage).override(500, 500).centerCrop()
                                .into(view)
                        }


                    }
                }
            }


        }

        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("subprofile3", "sub_seq3")
        fun setsubprofile3(view: ImageView, data: UserInformationData?, sub_seq: String) {
            // This methods should not have any return type, = declaration would make it return that object declaration
            if (data?.subImage != null) {
                if (data?.subImage?.size!! > 2) {
                    var nowImage = data?.subImage[2].im_url
                    var pastImage = data?.subImage[2].im_past_img
                    var ap = view.context.applicationContext as MainApplication
                    if (ap.userData?.u_seq.toString() == sub_seq) {
                        if (!nowImage.isNullOrEmpty()) {
                            Glide.with(view).load(nowImage).override(500, 500).centerCrop()
                                .into(view)
                        }
                    } else {

                        if (pastImage?.startsWith("http") == true) {
                            Glide.with(view).load(pastImage).override(500, 500).centerCrop()
                                .into(view)
                        } else {
                            Glide.with(view).load(nowImage).override(500, 500).centerCrop()
                                .into(view)
                        }


                    }
                }
            }


        }

        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("MainProfile", "sub_seq")
        fun setprofile(view: ImageView, data: UserInfomationCompanyData?, sub_seq: String) {
            // This methods should not have any return type, = declaration would make it return that object declaration.

            var nowImage = data?.main_url
            var pastImage = data?.main_past_img
            var application = view.context.applicationContext as MainApplication
            if (data != null) {

                Log.d("pastImage", pastImage.toString())
                Log.d("seq", sub_seq)
                Log.d("m_seq", data?.u_seq.toString())

                if (data?.u_seq.toString() == application.userData?.u_seq.toString()) {
                    if (!nowImage.isNullOrEmpty()) {
                        Glide.with(view).load(nowImage).override(500, 500).centerCrop().into(view)
                    }
                    Log.d("ㅇㅇ", "setprofile: 0")
                } else {

                    if (pastImage?.startsWith("http") == true) {
                        Glide.with(view).load(pastImage).override(500, 500).centerCrop()
                            .into(view)
                        Log.d("ㅇㅇ", "setprofile: 1")
                    } else {
                        Glide.with(view).load(nowImage).override(500, 500).centerCrop()
                            .into(view)
                        Log.d("ㅇㅇ", "setprofile: 2")
                    }


                }
            }


        }

        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("subprofile1", "sub_seq1")
        fun setsubprofile1(view: ImageView, data: UserInfomationCompanyData?, sub_seq: String) {
            // This methods should not have any return type, = declaration would make it return that object declaration

            if (data?.subImage != null) {
                if (data?.subImage?.size!! > 0) {
                    var nowImage = data?.subImage[0].im_url
                    var pastImage = data?.subImage[0].im_past_img
                    Log.d("과거이미지", pastImage.toString())
                    var ap = view.context.applicationContext as MainApplication
                    if (ap.userData?.u_seq.toString() == sub_seq) {
                        if (!nowImage.isNullOrEmpty()) {
                            Glide.with(view).load(nowImage).override(500, 500).centerCrop()
                                .into(view)
                            Log.d("서브이미지", "setsubprofile0: ")
                        }

                    } else {

                        if (pastImage?.startsWith("http") == true) {
                            Glide.with(view).load(pastImage).override(500, 500).centerCrop()
                                .into(view)
                            Log.d("서브이미지", "setsubprofile1: ")
                        } else {
                            Glide.with(view).load(nowImage).override(500, 500).centerCrop()
                                .into(view)
                            Log.d("서브이미지", "setsubprofile2: ")
                        }


                    }
                }
            }


        }

        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("subprofile2", "sub_seq2")
        fun setsubprofile2(view: ImageView, data: UserInfomationCompanyData?, sub_seq: String) {
            // This methods should not have any return type, = declaration would make it return that object declaration
            if (data?.subImage != null) {
                if (data?.subImage?.size!! > 1) {
                    var nowImage = data?.subImage[1].im_url
                    var pastImage = data?.subImage[1].im_past_img
                    var ap = view.context.applicationContext as MainApplication
                    if (ap.userData?.u_seq.toString() == sub_seq) {
                        if (!nowImage.isNullOrEmpty()) {
                            Glide.with(view).load(nowImage).override(500, 500).centerCrop()
                                .into(view)
                        }
                    } else {

                        if (pastImage?.startsWith("http") == true) {
                            Glide.with(view).load(pastImage).override(500, 500).centerCrop()
                                .into(view)
                        } else {
                            Glide.with(view).load(nowImage).override(500, 500).centerCrop()
                                .into(view)
                        }


                    }
                }
            }


        }

        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("subprofile3", "sub_seq3")
        fun setsubprofile3(view: ImageView, data: UserInfomationCompanyData?, sub_seq: String) {
            // This methods should not have any return type, = declaration would make it return that object declaration
            if (data?.subImage != null) {
                if (data?.subImage?.size!! > 2) {
                    var nowImage = data?.subImage[2].im_url
                    var pastImage = data?.subImage[2].im_past_img
                    var ap = view.context.applicationContext as MainApplication
                    if (ap.userData?.u_seq.toString() == sub_seq) {
                        if (!nowImage.isNullOrEmpty()) {
                            Glide.with(view).load(nowImage).override(500, 500).centerCrop()
                                .into(view)
                        }
                    } else {

                        if (pastImage?.startsWith("http") == true) {
                            Glide.with(view).load(pastImage).override(500, 500).centerCrop()
                                .into(view)
                        } else {
                            Glide.with(view).load(nowImage).override(500, 500).centerCrop()
                                .into(view)
                        }


                    }
                }
            }


        }

        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("profile", "sub_seq")
        fun setsubprofile(view: ImageView, data: UserInformationData?, sub_seq: String) {
            // This methods should not have any return type, = declaration would make it return that object declaration.
            var nowImage = data?.main_url
            var pastImage = data?.main_past_img
            var ap = view.context.applicationContext as MainApplication
            Log.d("ㅇㅇ", "setprofile: 0")
            if (ap.userData?.u_seq.toString() == sub_seq) {
                if (!nowImage.isNullOrEmpty()) {
                    Glide.with(view).load(nowImage).override(500, 500).centerCrop()
                        .into(view)
                }
                Log.d("ㅇㅇ", "setprofile: 1")
            } else {

                if (pastImage?.startsWith("http") == true) {
                    Glide.with(view).load(pastImage).override(500, 500).centerCrop()
                        .into(view)
                    Log.d("ㅇㅇ", "setprofile: 2")
                } else {
                    Glide.with(view).load(nowImage).override(500, 500).centerCrop()
                        .into(view)
                    Log.d("ㅇㅇ", "setprofile: 2")
                }


            }

        }

        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("selectsub", "sub_seqset")
        fun setselectview(view: ImageView, data: SubImage?, sub_seq: String) {
            // This methods should not have any return type, = declaration would make it return that object declaration
            if (data != null) {

                var nowImage = data?.im_url
                var pastImage = data.im_past_img
                var ap = view.context.applicationContext as MainApplication
                if (ap.userData?.u_seq.toString() == sub_seq) {
                    if (!nowImage.isNullOrEmpty()) {
                        Glide.with(view).load(nowImage).override(500, 500).centerCrop()
                            .into(view)
                    }
                } else {

                    if (pastImage?.startsWith("http") == true) {
                        Glide.with(view).load(pastImage).override(500, 500).centerCrop()
                            .into(view)
                    } else {
                        Glide.with(view).load(nowImage).override(500, 500).centerCrop()
                            .into(view)
                    }


                }

            }


        }


        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("photo2")
        fun setphoto2(view: ImageView, url: String?) {
            // This methods should not have any return type, = declaration would make it return that object declaration.
            if (!url.isNullOrEmpty())
                Glide.with(view).load(url)
                    .into(view)

            //0211 사진&이미지
//                    .override(500, 500)
        }

        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("photo3")
        fun setphoto3(view: ImageView, url: String?) {
            // This methods should not have any return type, = declaration would make it return that object declaration.
            if (!url.isNullOrEmpty())
                Glide.with(view).load(url).override(500, 500).circleCrop()
                    .into(view)
        }


        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("photoin")
        fun setphotoinside(view: ImageView, url: String?) {
            // This methods should not have any return type, = declaration would make it return that object declaration.
            if (!url.isNullOrEmpty())
                Glide.with(view).load(url).override(view.width, view.height).fitCenter()
                    .into(view)
        }

        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("logo")
        fun setlogo(view: ImageView, url: String?) {
            // This methods should not have any return type, = declaration would make it return that object declaration.
            if (!url.isNullOrEmpty()) {
                Glide.with(view.context).load(url).override(view.width, view.height).centerInside()
                    .into(view)
            }
        }

        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("photocenter")
        fun setphotocenter(view: ImageView, url: String?) {
            // This methods should not have any return type, = declaration would make it return that object declaration.
            if (!url.isNullOrEmpty()) {
                Glide.with(view.context).load(url)
                    .apply(RequestOptions.overrideOf(Target.SIZE_ORIGINAL)).fitCenter()
                    .into(view)
            } else {
                Glide.with(view.context).load(R.drawable.logo_sura_146x146)
                    .override(view.width, view.height).centerCrop()
                    .into(view)
            }
        }

        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("loading")
        fun setLoadingView(view: ImageView, isloading: Boolean) {
            // This methods should not have any return type, = declaration would make it return that object declaration.
            Glide.with(view).load(R.raw.loading_anim).into(view)
        }

        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("rankImage")
        fun rankImage(view: ImageView, url: String?) {
            // This methods should not have any return type, = declaration would make it return that object declaration.
            if (!url.isNullOrEmpty()) {
                Glide.with(view).load(url).circleCrop()
                    .into(view)
            } else {
//                Glide.with(view.context).load(R.drawable.ic_main_gold).circleCrop().override(20)
//                    .into(view)
            }
        }

        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("rankSpaceImage")
        fun rankSpaceImage(view: ImageView, url: String?) {
            // This methods should not have any return type, = declaration would make it return that object declaration.
            if (!url.isNullOrEmpty()) {
//                Glide.with(view).load(url).circleCrop()
//                    .into(view)
            } else {
                Glide.with(view.context).load(R.drawable.ic_main_gold).circleCrop()
                    .into(view)
            }
        }

        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("OnwerAndPassengerImage","OnwerAndPassengerImageIndex","OnwerAndPassengerImageList")
        fun OnwerAndPassengerImage(view: ImageView, s: ArrayList<ProfileSuggestionData>? ,index :Int,imagelist:ArrayList<String>) {
            // This methods should not have any return type, = declaration would make it return that object declaration.
            if (s?.size!! > index){
                var url = s[index].u_image
                var u_seq = s[index].u_seq
                var check =false
                val multi = MultiTransformation<Bitmap>(BlurTransformation(view.context,99,100), RoundedCornersTransformation(  0, 0, RoundedCornersTransformation.CornerType.ALL)
                )
                if (!url.isNullOrEmpty()) {

                    if (imagelist!!.contains(u_seq.toString())) {
                        check = true
                    }
                    if (check){
                        (view.context as OwnerAndPassengerActivity).runOnUiThread {
                            Glide.with(view.context).load(url).into(view) }
                    }else {
                        (view.context as OwnerAndPassengerActivity).runOnUiThread {
                             Glide.with(view.context).load(url)

                                .apply(
                                    RequestOptions.bitmapTransform(multi)
                                )
                                .into(view)
//                                Picasso.get()
//                                    .load(data.u_image)
//                                    .transform(BlurTransformation(con, 25, 1))
//                                    .into(binding.ivProfile)
                        }
                    }
                } else {
                    Glide.with(view.context).load(R.drawable.logo_sura_146x146).into(view)
                }
            }

        }



        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("OnwerAndPassengerImageBrand","OnwerAndPassengerImageBrandIndex")
        fun OnwerAndPassengerImageBrand(view: ImageView, s: ArrayList<ProfileSuggestionData>? ,index :Int) {
            // This methods should not have any return type, = declaration would make it return that object declaration.
            if (s?.size!! > index){
                var url = s[index].carb_image
                if (!url.isNullOrEmpty()) {
                    Glide.with(view).load(url).into(view)
                } else {
                    Glide.with(view.context).load(R.drawable.logo_sura_146x146).into(view)
                }
            }

        }


        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("OnwerAndPassengerBack")
        fun OnwerAndPassengerBack(view: ImageView, s: ArrayList<ProfileSuggestionData>? ) {
            // This methods should not have any return type, = declaration would make it return that object declaration.
                    Glide.with(view.context).load(R.drawable.background_bg).into(view)
        }

        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("homeVipImage")
        fun homeVipImage(view: ImageView, url: String?) {
            // This methods should not have any return type, = declaration would make it return that object declaration.
            if (url.isNullOrEmpty()) {
                Glide.with(view)
                    .load(R.drawable.app_icon)
                    .override(view.width, view.height)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .listener(object : RequestListener<Drawable> {
                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }
                    })
                    .into(view)
            }

        }


        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("homeImage")
        fun homeImage(view: ImageView, url: String?) {
            // This methods should not have any return type, = declaration would make it return that object declaration.
            if (url.isNullOrEmpty()) {
                Glide.with(view)
                    .load(R.drawable.app_icon)
                    .override(view.width, view.height)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .listener(object : RequestListener<Drawable> {
                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
//                            initView(binding)

//                            initView(binding)
                            return false
                        }

                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
//                            initView(binding)
                            return false
                        }
                    })

                    .into(view)
            } else {
                Glide.with(view)
                    .load(url)
                    .override(view.width, view.height).centerCrop()
//                    .placeholder()
                    .listener(object : RequestListener<Drawable> {
                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {

                            return false
                        }


                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {

                            return false
                        }
                    })

                    .into(view)
            }

//            Glide.with(binding!!.itemIv).load(data!!.src).centerInside().into(binding!!.)

        }


        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("app:homeVipIndex", "app:homeVipIsBoardOne", "app:homeVipList")
        fun homeVipImage(
            view: ImageView,
            homeVipIndex: Int,
            homeVipIsBoardOne: Boolean,
            homeVipList: ArrayList<DriveAwayData>
        ) {

        }

        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("app:homeVipVisIndex", "app:homeVipVisSize", "app:homeVipVisIsBoardOne2")
        fun homeVipVisible(
            view: RelativeLayout,
            homeVipVisIndex: Int,
            homeVipVisSize: Int,
            homeVipVisIsBoardOne2: Boolean
        ) {
        }
    }

    private fun ShowQuickUserDialog() {
        quickUserDialog =
            QuickUserDialog(this, QuickTypeStatus.QUICK_WARNING) { type, okcancel ->
                if (okcancel) {

                    quickUserDialog!!.dismiss()
                    val i = Intent(this, JoinActivity::class.java)

                    i.putExtra("u_name", application?.userBasicData?.u_name)
                    i.putExtra("u_gender", application?.userBasicData?.u_gender)
                    i.putExtra("u_birthday", application?.userBasicData?.u_birthday)
                    i.putExtra("u_phone", application?.userBasicData?.u_phone)

                    startActivity(i)
                } else {
                    quickUserDialog!!.dismiss()
                }
            }
        quickUserDialog!!.show(fm!!, "")
    }

    override fun onBackPressed() {
        var positon = binding!!.pager!!.currentItem
        if (positon == 1) {
            var fr = pageradapter!!.fragments.get(positon) as LocationSpotListFragment
//            var fr = pageradapter!!.fragments.get(positon) as VipLounge2Fragment
            // var fr = pageradapter!!.fragments.get(positon) as LocationMainFragment
//            var fr = pageradapter!!.fragments.get(positon) as VipLoungeFragment
            var vstack = fr.viewModel!!.viewStack.value!!
            if (vstack == 0) {
//                fr.reset()
            } else {
                if (finishDialog == null) {
                    finishDialog = FinishDialog(this) { event ->
                        if (Constans.FinishDialog_OK == event) {
                                    super.onBackPressed()


                        } else if (event == Constans.FinishDialog_BANNER) {
                            var i = Intent(this, WebViewActivity::class.java)
                            var ap = applicationContext as MainApplication
                            var linkset =
                                "https://www.supercarlounge.com/wv/event/lotto.php?u_seq=" + ap.userData!!.u_seq
                            i.putExtra("url", linkset)
                            resultLauncherPopUp.launch(i)

                        }
                    }
                }
                if (finishDialog!!.isVisible) {
//                    finishDialog!!.dismiss()
                } else {
                    finishDialog?.isCancelable = false
                    finishDialog!!.show(fm!!, "")
                }

            }
        } else {
            if (finishDialog == null) {
                finishDialog = FinishDialog(this) { event ->
                    if (Constans.FinishDialog_OK == event) {
                        super.onBackPressed()


                    } else if (event == Constans.FinishDialog_BANNER) {
                        var i = Intent(this, WebViewActivity::class.java)
                        var ap = applicationContext as MainApplication
                        var linkset =
                            "https://www.supercarlounge.com/wv/event/lotto.php?u_seq=" + ap.userData!!.u_seq
                        i.putExtra("url", linkset)
                        resultLauncherPopUp.launch(i)

                    }
                }
            }
            if (finishDialog!!.isVisible) {
//                finishDialog!!.dismiss()
            } else {
                finishDialog?.isCancelable = false
                finishDialog!!.show(fm!!, "")
            }
        }

    }

    fun settitle(position: Int) {
        when (position) {
            0 -> {
                viewModel!!.title.value = "SURA"
            }
            1 -> {
                viewModel!!.title.value = "LOCATION"
            }
            2 -> {
                viewModel!!.title.value = "BOARD"
            }
            3 -> {
                viewModel!!.title.value = "MY PAGE"
            }
            else -> {}
        }

    }

    var resultLauncherPopUp =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Constans.BRIDGE_MOVE_NOTICE) {
                movepage()
            }
        }

    @SuppressLint("NotifyDataSetChanged")
    private fun observers() {

    }

}