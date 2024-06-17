package com.supercarlounge.supercar.ui.fragment

import HeartInsertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.firebase.dynamiclinks.ktx.*
import com.google.firebase.ktx.Firebase
import com.supercarlounge.supercar.GradientDrawable
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.adapter.RecentDrivePagerAdapter
import com.supercarlounge.supercar.databinding.FragmentMyPageBinding
import com.supercarlounge.supercar.dialog.*
import com.supercarlounge.supercar.enumset.RecDriveDialogType
import com.supercarlounge.supercar.ui.activity.*
import android.R
import android.app.Activity
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.TypefaceSpan
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.supercarlounge.supercar.Constans
import com.supercarlounge.supercar.enumset.GuidePopType
import com.supercarlounge.supercar.viewmodel.MyPageViewModel

import java.util.*
import kotlin.concurrent.timer

class MyPageFragment : Fragment() {

    private var _binding: FragmentMyPageBinding? = null
    private lateinit var application:MainApplication
    private lateinit var viewModel : MyPageViewModel
    var heartInsertDialog :HeartInsertDialog? = null
    var fm :FragmentManager? = null
    var recentpageradapter:RecentDrivePagerAdapter?=null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding
    var timer = Timer()
    var passtimer = Timer()
    var click = false
    var type = 0
    var init = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(MyPageViewModel::class.java)
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        binding!!.viewModel = viewModel
        binding!!.setLifecycleOwner(this)
        val root: View = binding!!.root
        fm = childFragmentManager
        viewModel.text.value = "마이페이지"
        application=requireContext()!!.applicationContext as MainApplication
        GetNowAppVersion()
        return root
    }

    override fun onResume() {
        super.onResume()
        if(application!!.Check30days(application!!.checkguide8)) {

        }else{
            // 가이드팝업 제거
//            var d = GuideDialog(requireContext(), GuidePopType.MY_PAGE) {
//                application!!.save_guide(GuidePopType.MY_PAGE)
//            }
//            d.show(fm!!, "")
        }
            viewModel.GetMyvipDrive()
        viewModel.CheckDrivePassDate()
        viewModel.GetAppVersion()
//    if (init) {
        if (application!!.userData!!.u_staff_yn.equals("C") == false) {
            viewModel.GetMyPoint()
            viewModel.GetRecentDrive()
        }

//    }

        var feed = application.userData?.u_feed
        viewModel.swRecProfile.value = feed.equals( "1")
        click = false
        val range = (0..2)  // 1 <= n <= 15
        setpointext(range.random())
    }

    override fun onStart() {
        super.onStart()

    }
    override fun onPause() {
        super.onPause()
        viewModel.isviptimer.value =false
        viewModel.ispasstimer.value =false

    }

    var  resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK){

            (requireActivity() as MainActivity).movepage()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnclickListeners()
        viewModel.my_seq.value = application.userData?.u_seq.toString()
        viewModel.swLockScreen.value = application.userData?.u_lock_screen == "Y"
        viewModel.iscompany.value  = application.userData?.u_staff_yn.equals("C")
        viewModel.swPush.value = application.getall_push()
        recentpageradapter =  RecentDrivePagerAdapter(requireContext(),viewModel){ data,position, viewtype->
            type = viewtype
            viewModel.DrivePassCheck(data)
        }

        binding!!.pagerRecent.adapter = recentpageradapter
        binding!!.pagerRecent.addOnPageChangeListener(object:OnPageChangeListener{
            override fun onPageSelected(position: Int) {
                viewModel.nowrecentpage.value = position
                viewModel.nowpage_b_seq.value = viewModel.recentdrivelist.value?.get(position)?.b_seq
                viewModel.getcountCheck()

                var max  = recentpageradapter!!.mlist.size -1
                if(position == max ){

                }else if(position == 0 && max>1){

                }
            }
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }



            override fun onPageScrollStateChanged(state: Int) {

            }

        })
        if (application!!.userData!!.u_staff_yn.equals("C") == false) {
            viewModel.GetRecentDrive()
            viewModel.GetMyPoint()
            binding!!.tvPoint.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        }

        observes()
        colorset()
    }
    fun colorset(){
        binding!!.onclickHeart.background = gradientset()
        binding!!.onclickProfile.background = gradientset()
        binding!!.onclickProfileBrowse.background = gradientset()
        binding!!.onclickMatching.background = gradientset()
    }
    private fun setOnclickListeners() {
        binding!!.llPointmall.setOnClickListener {
            val i = Intent(requireActivity(), PointMallActivity::class.java)
            startActivity(i)
        }
        binding!!.llPointset.setOnClickListener {
            val i = Intent(requireActivity(), PointMallLogActivity::class.java)
            startActivity(i)
        }
        binding!!.btnPointQuestion.setOnClickListener {
            val i = Intent(requireActivity(), PointMallGuideActivity::class.java)
            resultLauncherGuide.launch(i)

        }
        binding!!.onclickComprofile.setOnClickListener {
            val i = Intent(requireActivity(), ProfileCompanyActivity::class.java)
            i.putExtra("sub_seq",viewModel.my_seq.value!!)
            startActivity(i)
        }
        binding!!.onclickHeart.setOnClickListener {
            if (!click){
                click = true
                val i= Intent(requireActivity(), HeartsShopActivity::class.java)
                startActivity(i)

                requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }


        }
        binding!!.onclickProfile.setOnClickListener {
            if (!click) {
                click = true
                val i = Intent(requireActivity(), ProfileActivity::class.java)
                i.putExtra("sub_seq", application.userData!!.u_seq.toString())
                startActivity(i)
                requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }

        }
        binding!!. onclickProfileBrowse.setOnClickListener {
            if (!click){
                click = true
            val i= Intent(requireActivity(), ProfileRequestActivity::class.java)
            startActivity(i)
                requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }

        }

        binding!!.onclickMatching.setOnClickListener {
            if (!click) {
                click = true
                val i = Intent(requireActivity(), MatchingActivity::class.java)
                startActivity(i)
                requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }

        }



        binding!!.onclickVip.setOnClickListener {
            if (!click) {
                click = true
                val i = Intent(requireActivity(), DriveVipRankActivity::class.java)
                i.putExtra("inputtype", false)
                startActivity(i)
                requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }
        binding!!.onclickPass.setOnClickListener {
            val i = Intent(requireActivity(), HeartsShopActivity::class.java)
            startActivity(i)
        }
        binding!!.swLockScreen.setOnClickListener {

            if (viewModel.swLockScreen.value == true){
                viewModel.passwordSettingTrueFalse(viewModel.my_seq.value!!)

            }
        }
        binding!!.tvTermsOfService.setOnClickListener {
            val termsUrl = "https://supercarlounge.com/page1.php"

            val termsIntent = Intent(Intent.ACTION_VIEW, Uri.parse(termsUrl))
           startActivity(termsIntent)
        }

        binding!!.tvLounge.setOnClickListener {
            if (!click) {
                click = true
                val i = Intent(requireActivity(), LoungeActivity::class.java)
                resultLauncher.launch(i)
            }
        }
        binding!!.tvPolicy.setOnClickListener {
            val privacyUrl = "https://supercarlounge.com/page3.php"
            val privacyIntent = Intent(Intent.ACTION_VIEW, Uri.parse(privacyUrl))
            startActivity(privacyIntent)
        }
        binding!!.tvIssued.setOnClickListener {
            viewModel.GetInviteCode()
        }
        binding!!.tvBlock.setOnClickListener {
            if (!click) {
                click = true
                var i = Intent(requireContext(), BlockAcquaintancesActivity::class.java)
                startActivity(i)
                requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }
        binding!!.onclickHeartCoupon.setOnClickListener {

                heartInsertDialog = HeartInsertDialog(requireContext(),application.userData?.u_seq.toString()) { isfinish ,ischeckde,text->


                }
                heartInsertDialog!!.show(fm!!, "")

        }
        binding!!.btnRight.setOnClickListener {
            var now = binding!!.pagerRecent.currentItem
            var max =recentpageradapter!!.mlist.size
            if(now!=max) {
                binding!!.pagerRecent.currentItem = now + 1

            }
        }
        binding!!.btnLeft.setOnClickListener {
            var now = binding!!.pagerRecent.currentItem
            if(now!=0) {
                binding!!.pagerRecent.currentItem = now -1

            }
        }
        binding!!.tvNotice.setOnClickListener {
            (requireActivity() as MainActivity).movepage()
        }
        binding!!.tvServiceCenter.setOnClickListener {
            if (!click) {
                click = true
                val i = Intent(requireActivity(), ServiceCenterActivity::class.java)
                startActivity(i)
                requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }

        }
        binding!!.tvLogout.setOnClickListener {

            application!!.savePhoneNumber("")
            application!!.u_phone =""
            val i= Intent(requireActivity(), LoginActivity::class.java)
            i.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(i)
            requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        binding!!.tvRateTheApp.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.data = Uri.parse("market://details?id=com.supercarlounge.supercar")
            startActivity(intent)
        }
        binding!!.tvAppVersion.setOnClickListener {
            if(viewModel.isupdate.value!!){
                val intent = Intent(Intent.ACTION_VIEW)
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                intent.data = Uri.parse("market://details?id=com.supercarlounge.supercar")
                startActivity(intent)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun observes() {
        viewModel.isrec_click.observe(viewLifecycleOwner, Observer {
            if(it){
                var rectype : RecDriveDialogType? = RecDriveDialogType.REC_DRIVE_OFF
                var did = RecDriveDialog(requireContext(), rectype!!) { type, okcancel ->
                    if(okcancel) {
                        viewModel.onRecChecked(false)
                    }

                }
                did.show(childFragmentManager!!, "")
            }
        })

        viewModel.rec_profile_update.observe(viewLifecycleOwner, Observer {
            if(it!=null){
                if(it) {
                    application!!.userData?.u_feed = "1"
                }else{
                    application!!.userData?.u_feed = "0"
                }
            }
        })
        viewModel.swLockScreenTouch.observe(viewLifecycleOwner, Observer {
                if (it){


                }

        })
        viewModel.passwordTrueFalse.observe(viewLifecycleOwner, Observer {
            if (it== 2){

                    val i= Intent(requireActivity(), LockActivity::class.java)
                    startActivity(i)
                    requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)



            }

        })
        viewModel.swPush.observe(viewLifecycleOwner, Observer {

              application!!.setsetting(it!!)


        })
        viewModel!!.inviteset.observe(viewLifecycleOwner,Observer{
            if(it){
                var d = InviteDialog(requireContext(),viewModel.invite_code.value!!){ btn->
                    if(btn){
                        application!!.onDynamicLinkClick(requireContext(),viewModel.invite_code.value!!)
                    }
                }
                d.show(childFragmentManager,"")
            }
        })
        viewModel.isviptimer.observe(viewLifecycleOwner,Observer{
            if(it){
                setTimer()
            }else{
                stoptimer()
            }
        })
        viewModel.ispasstimer.observe(viewLifecycleOwner,Observer{
            if(it){
                setPassTimer()
            }else{
                stoppasstimer()
            }
        })
        viewModel.recentdrivelist.observe(viewLifecycleOwner,Observer{
            if(it != null){
                if (it.size> 0){
                    Log.d("recentdrivelist.observe", it.size.toString())
                    recentpageradapter =  RecentDrivePagerAdapter(requireContext(),viewModel){ data,position, viewtype->
                        type = viewtype
                        viewModel.DrivePassCheck(data)
                    }

                    binding!!.pagerRecent.adapter = recentpageradapter
                    if (it.size > viewModel.nowrecentpage.value!!) {

//                        if (!init){

                                Handler(Looper.getMainLooper()).postDelayed({
                                    if (it.size ==1){
                                        binding?.pagerRecent?.setCurrentItem(1 ,false)
                                        viewModel.nowrecentpage.value = 1
                                    }else{
                                    binding?.pagerRecent?.setCurrentItem(it.size-1 ,false)
                                    viewModel.nowrecentpage.value = it.size-1

                                    }
                                }, 100)


//                            init = true
//                        }
                        viewModel.nowpage_b_seq.value = it.get(viewModel.nowrecentpage.value!!).b_seq
                        viewModel.getcountCheck()
                    }
                }



            }
        })
        viewModel.drivepassset.observe(viewLifecycleOwner,Observer{recentdata->
            if(recentdata==null) {
                var i =Intent(requireContext(),RecentDriveListActivity::class.java)
                i.putExtra("type",type)
                i.putExtra("push",true)
                i.putExtra("passCheck",false)
                startActivity(i)
            }else{
                var i =Intent(requireContext(),RecentDriveListActivity::class.java)
                i.putExtra("data",recentdata)
                i.putExtra("type",type)
                i.putExtra("push",true)
                i.putExtra("passCheck",true)
                startActivity(i)
            }


        })
//        viewModel.nowrecentpage.observe(viewLifecycleOwner,Observer{ index->
//            if(recentpageradapter!!.mlist.size!=0) {
//                binding!!.pagerRecent.currentItem = index
//            }
//        })
    }

    fun newInstance(): MyPageFragment {
        val args = Bundle()

        val fragment = MyPageFragment()
        fragment.arguments = args
        return fragment
    }
    fun gradientset():Drawable{
       var d = GradientDrawable(
            requireContext().getColor(com.supercarlounge.supercar.R.color.cooper_gradi_st),
           requireContext().getColor(com.supercarlounge.supercar.R.color.cooper_gradi_ed),
           requireContext().getColor(
               com.supercarlounge.supercar.R.color.trn
            ),
           requireContext().getColor(com.supercarlounge.supercar.R.color.trn),
            2f,
            20f,
            GradientDrawable.Direction.TL_BR,
            GradientDrawable.Direction.TL_BR
        )
        return d
    }


     fun setTimer() {


        if (timer != null){
            timer!!.cancel()

        }
        timer= timer(period = 1000, initialDelay = 1000) {

            requireActivity().runOnUiThread {
                viewModel.settime()
            }
        }


    }
    fun setPassTimer() {


        if (passtimer != null){
            passtimer!!.cancel()

        }
        passtimer= timer(period = 1000, initialDelay = 1000) {

            requireActivity().runOnUiThread {
                viewModel.setpasstime()
            }
        }


    }
    fun GetNowAppVersion(){
        var version = ""
        try {
            val pInfo = requireContext().packageManager.getPackageInfo(requireContext().packageName, 0)
            version = pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        viewModel.nowversion.value = version
    }
    fun stoptimer(){
        if (timer != null){
            timer!!.cancel()

        }
    }
    fun stoppasstimer(){
        if (passtimer != null){
            passtimer!!.cancel()

        }
    }
    @RequiresApi(Build.VERSION_CODES.P)
    fun setpointext(type:Int){
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(16))
        if (type == 0 ) {

            Glide.with(binding!!.ivPointLogo).load(com.supercarlounge.supercar.R.drawable.img_gas).override(binding!!.ivPointLogo.width, binding!!.ivPointLogo.height) .apply(requestOptions).into(binding!!.ivPointLogo)
            var content = getString(com.supercarlounge.supercar.R.string.pointtext_set1)
            val spannableString = SpannableString(content)
            spannableString.setSpan(
                ForegroundColorSpan(Color.parseColor(getString(com.supercarlounge.supercar.R.color.main_color))),
                8,
                12,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            var span = TypefaceSpan("optimus_princeps_pemi_bold")
            spannableString.setSpan(
                span,
                8,
                12,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            binding!!.tvPointText.text = spannableString
        }else if (type == 1){
            Glide.with(binding!!.ivPointLogo).load(com.supercarlounge.supercar.R.drawable.img_emart).override(binding!!.ivPointLogo.width, binding!!.ivPointLogo.height).apply(requestOptions).into(binding!!.ivPointLogo)
            var content = getString(com.supercarlounge.supercar.R.string.pointtext_set2)
            val spannableString = SpannableString(content)
            spannableString.setSpan(
                ForegroundColorSpan(Color.parseColor(getString(com.supercarlounge.supercar.R.color.main_color))),
                8,
                16,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            var span = TypefaceSpan( "optimus_princeps_pemi_bold.ttf")
            spannableString.setSpan(
                span,
                8,
                16,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            binding!!.tvPointText.text = spannableString
        }else if (type == 2) {
            Glide.with(binding!!.ivPointLogo).load(com.supercarlounge.supercar.R.drawable.img_starbugs).override(binding!!.ivPointLogo.width, binding!!.ivPointLogo.height).apply(requestOptions).into(binding!!.ivPointLogo)
            var content = getString(com.supercarlounge.supercar.R.string.pointtext_set3)
            val spannableString = SpannableString(content)
            var span = TypefaceSpan( "optimus_princeps_pemi_bold.ttf")
            spannableString.setSpan(
                span,
                8,
                17,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                ForegroundColorSpan(Color.parseColor(getString(com.supercarlounge.supercar.R.color.main_color))),
                8,
                17,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )


            binding!!.tvPointText.text = spannableString
        }

    }

    var resultLauncherGuide =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == 10) {
                (requireActivity() as MainActivity).movepagehome()
            }else if (result.resultCode == 11){
                (requireActivity() as MainActivity).movepageboard()
            }else if (result.resultCode == 12){
                val i= Intent(requireActivity(), PointMallActivity::class.java)
                startActivity(i)
            }
        }
}