package com.supercarlounge.supercar.ui.priview.activity

import FinishDialog
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.supercarlounge.supercar.Constans
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.MainApplication.Companion.token
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.MatchingMiniData
import com.supercarlounge.supercar.data.ProfileRequestData
import com.supercarlounge.supercar.data.RecentDriveListData
import com.supercarlounge.supercar.databinding.ActivityMainBinding
import com.supercarlounge.supercar.dialog.QuickUserDialog
import com.supercarlounge.supercar.enumset.QuickTypeStatus
import com.supercarlounge.supercar.ui.activity.JoinActivity
import com.supercarlounge.supercar.ui.activity.WebViewActivity
import com.supercarlounge.supercar.ui.fragment.*
import com.supercarlounge.supercar.ui.priview.adapter.PrvPagerFragmentStateAdapter
import com.supercarlounge.supercar.ui.priview.fragment.*
import com.supercarlounge.supercar.viewmodel.MainViewModel


class PrvMainActivity : AppCompatActivity() {
    private var viewModel: MainViewModel? = null
    var application: MainApplication? = null
    lateinit var binding: ActivityMainBinding
    private var viewPager: ViewPager2? = null
    var fm: FragmentManager? = null
    var pageradapter: PrvPagerFragmentStateAdapter? = null
    private var quickUserDialog: QuickUserDialog? = null

    var view = ""
    var finishDialog: FinishDialog? = null

    @SuppressLint("UseCompatLoadingForDrawables", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding = ActivityMainBinding.inflate(layoutInflater)
        application = applicationContext as MainApplication
        viewModel!!.my_seq.value = application!!.userData?.u_seq.toString()
        binding!!.viewModel = viewModel
        setContentView(binding.root)
        binding!!.lifecycleOwner = this
        fm = supportFragmentManager

        application!!.startLocationUpdates()


        var u_phone = intent.getStringExtra("u_phone")
        view = intent.getStringExtra("view").toString()
        var u_seq =intent.getStringExtra("u_seq").toString()
        application!!.userBasicData?.u_name = intent.getStringExtra("u_name").toString()
        application!!.userBasicData?.u_gender = intent.getStringExtra("u_gender").toString()
        application!!.userBasicData?.u_birthday = intent.getStringExtra("u_birthday").toString()
        application!!.userBasicData?.u_phone = intent.getStringExtra("u_phone").toString()
        if (view.equals("퀵유저")) {
            application!!.quick_view = "퀵유저"
            token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MTIyMiwiaWF0IjoxNjY4Njg1NzE5LCJleHAiOjE2Njg3NzIxMTl9.AmNJtfXlnUsNCqq9ROfBu-nsJvsUsUiRLcYVXIgVQGk"
            Log.d("폰", u_phone!!)
           viewModel?.checkLogin(u_phone!!,application!!.push_token,application!!)

        }
        if (!application!!.previewLog){
            application.let {
                it?.joinLog(this,"미리보기",application!!.userBasicData?.u_name.toString())
                it?.previewLog = true
            }

        }


        if (application?.u_phone?.isEmpty() == true) {
            if (u_phone != null) {
                application!!.savePhoneNumber(u_phone)
            }
        }
        setclicklistner()
        setpageadapter()
        resolution_px()

        // 가이드팝업 제거
//        var i = Intent(this,PrvGuideActivity::class.java)
//        startActivity(i)

        viewModel!!.CheckLogInsert()
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

    fun setclicklistner(){
        binding!!.ivHeartShop.setOnClickListener {
            ShowQuickUserDialog()
        }
        binding!!.ivVipNotice.setOnClickListener {
            if (application?.quick_view.equals("퀵유저")) {
                ShowQuickUserDialog()
            }
        }
        binding!!.rlAlim.setOnClickListener {
            ShowQuickUserDialog()
        }


        binding!!.ivVipDrive.setOnClickListener {
                ShowQuickUserDialog()

        }
        binding!!.ivVipI.setOnClickListener {
            ShowQuickUserDialog()
        }
    }
    fun setpageadapter() {
        pageradapter = PrvPagerFragmentStateAdapter(this as FragmentActivity)
        pageradapter!!.addFragment(PrvHomeFragment2())
        pageradapter!!.addFragment(LocationSpotListFragment())
        pageradapter!!.addFragment(PrvBoardFragment())
        pageradapter!!.addFragment(PrvMyPageFragment())

        // Adapter
        binding.pager.adapter = pageradapter
//        binding!!.pager.setCurrentItem(0, false)
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
        binding!!.pager.offscreenPageLimit = 1

        binding.tabLayout.selectTab(binding!!.tabLayout.getTabAt(0),true)
//        binding!!.tabLayout.getTabAt(2)?.view?.isSelected = true
//        binding!!.tabLayout.getTabAt(0)?.view?.isSelected = false


    }

    override fun onResume() {
        super.onResume()
        setreceiver()
        viewModel!!.CheckAlim("")
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(foregorundpushreceiver)
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
//        overridePendingTransition(
//            com.supercarlounge.supercar.R.anim.fadein,
//            com.supercarlounge.supercar.R.anim.fadeout
//        )
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

//            if (channel_id != null) {
//                if (b_seq != null) {
//                    application!!.SendNotification(title!!, body!!, channel_id!!,b_seq)
//                }
//            }
            Log.d("check_noti", "checknoti")
        }
    }
    fun movepagehome() {
        var d = binding.tabLayout.getTabAt(2)
        d!!.select()
    }
    fun movepageboard() {
        var d = binding.tabLayout.getTabAt(3)
        d!!.select()

        (pageradapter!!.fragments.get(3) as PrvBoardFragment).MoveBoardPage(1)
    }
    fun movepage() {
        var d = binding.tabLayout.getTabAt(3)
        d!!.select()

        (pageradapter!!.fragments.get(3) as PrvBoardFragment).MoveBoardPage(0)
    }

    companion object {
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:round_photo")
        @JvmStatic
        fun setroundphoto(imageview: ImageView, url: String?) {
            if (!url.isNullOrEmpty()) {
                Glide.with(imageview).load(url).override(imageview.width, imageview.height)
                    .centerInside().circleCrop().into(imageview)
            } else {
                Glide.with(imageview.context).load(R.drawable.logo_sura_146x146)
                    .override(imageview.width, imageview.height).circleCrop()
                    .into(imageview)
            }
        }
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:round_photo2")
        @JvmStatic
        fun setroundphoto2(imageview: ImageView, data: RecentDriveListData?) {
            var pastImage =data?.im_past_img
            var image = data?.im_url
            if (pastImage?.startsWith("http") == true) {
                Glide.with(imageview).load(pastImage).override(imageview.width, imageview.height)
                    .centerInside().circleCrop().into(imageview)
            } else {
                if (!image.isNullOrEmpty()) {
                    Glide.with(imageview).load(image).override(imageview.width, imageview.height).centerInside().circleCrop().into(imageview)
                }else{
                    Glide.with(imageview.context).load(R.drawable.logo_sura_146x146)
                        .override(imageview.width, imageview.height).circleCrop()
                        .into(imageview)
                }

            }
        }

        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:round_photo3")
        @JvmStatic
        fun setroundphoto3(imageview: ImageView, data: ProfileRequestData?) {
            Log.d("테스트", data.toString())
            var pastImage =data?.im_past_img
            var image = data?.u_image
            var pc_ok = data?.pc_ok
            var am = imageview.context.applicationContext as MainApplication
            var myseq = am.userData?.u_seq
            var sub_seq = data!!.sub_seq
            if (pastImage?.startsWith("http") == true) {
                Glide.with(imageview).load(pastImage).override(imageview.width, imageview.height).centerInside().circleCrop().into(imageview)

            } else {
                if (pc_ok.equals("Y") || sub_seq == myseq) {
                    Glide.with(imageview).load(image).override(imageview.width, imageview.height).centerInside().circleCrop().into(imageview)
                }else{
                    Glide.with(imageview.context).load(R.drawable.frame_x4).override(100, 100).into(imageview)
                }

            }
        }

        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:round_photo4")
        @JvmStatic
        fun setroundphoto4(imageview: ImageView, data: MatchingMiniData?) {
            var pastImage =data?.im_past_img
            var image = data?.im_url
            if (pastImage?.startsWith("http") == true) {
                Glide.with(imageview).load(pastImage).override(imageview.width, imageview.height)
                    .centerInside().circleCrop().into(imageview)
            } else {
                if (!image.isNullOrEmpty()) {
                    Glide.with(imageview).load(image).override(imageview.width, imageview.height).centerInside().circleCrop().into(imageview)
                }else{
                    Glide.with(imageview.context).load(R.drawable.frame_x4)
                        .override(100, 100)
                        .into(imageview)
                }

            }
        }

        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("photo")
        fun setphoto(view: ImageView, url: String?) {
            // This methods should not have any return type, = declaration would make it return that object declaration.
            if (!url.isNullOrEmpty())
                Glide.with(view).load(url).override(view.width, view.height).centerCrop()
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
                Glide.with(view.context).load(url).override(view.width, view.height).centerInside()
                    .into(view)
            } else {
                Glide.with(view.context).load(R.drawable.logo_sura_146x146)
                    .override(view.width, view.height).centerInside()
                    .into(view)
            }
        }

    }
    private fun ShowQuickUserDialog() {
        if(quickUserDialog==null) {
            quickUserDialog =
                QuickUserDialog(this, QuickTypeStatus.QUICK_WARNING) { type, okcancel ->
                    if (okcancel) {

                        quickUserDialog!!.dismiss()
                        val i = Intent(this, JoinActivity::class.java)
                        i.putExtra("qickview","퀵유저")
                        i.putExtra("u_name", application?.userData?.u_name)
                        i.putExtra("u_gender", application?.userData?.u_gender)
                        i.putExtra("u_birthday", application?.userData?.u_birthday)
                        i.putExtra("u_phone", application?.userData?.u_phone)
                        Log.d("아이템3", application?.userBasicData.toString())
                        startActivity(i)
                    } else {
                        quickUserDialog!!.dismiss()
                    }
                }
        }
        if(quickUserDialog!!.isVisible == false)
        quickUserDialog!!.show(fm!!, "")
    }
    fun settitle(position: Int) {
        when (position) {
            0 -> {
                viewModel!!.title.value = "SUPERCAR LOUNGE"
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

    override fun onBackPressed() {
            if (finishDialog == null) {
                finishDialog = FinishDialog(this) { event ->
                    if (Constans.FinishDialog_OK == event) {
                        super.onBackPressed()
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


}