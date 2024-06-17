package com.supercarlounge.supercar.ui.priview.fragment

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.TypefaceSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.dynamiclinks.ktx.*
import com.google.firebase.ktx.Firebase
import com.supercarlounge.supercar.GradientDrawable
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.adapter.RecentDrivePagerAdapter
import com.supercarlounge.supercar.databinding.FragmentMyPageBinding
import com.supercarlounge.supercar.dialog.*
import com.supercarlounge.supercar.enumset.RecDriveDialogType
import com.supercarlounge.supercar.ui.activity.*
import com.supercarlounge.supercar.viewmodel.MyPageViewModel
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.RecentDriveData
import com.supercarlounge.supercar.enumset.QuickTypeStatus
import com.supercarlounge.supercar.ui.priview.activity.PrvMainActivity
import com.supercarlounge.supercar.ui.priview.activity.PrvPointMallActivity
import com.supercarlounge.supercar.ui.priview.activity.PrvProfileActivity
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timer

class PrvMyPageFragment : Fragment() {

    private var _binding: FragmentMyPageBinding? = null
    private lateinit var application:MainApplication
    private lateinit var viewModel : MyPageViewModel
    var recentpageradapter:RecentDrivePagerAdapter?=null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding
    var timer = Timer()
    private var quickUserDialog: QuickUserDialog? = null
    var fm: FragmentManager? = null
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
        viewModel.text.value = "마이페이지"
        application=requireContext()!!.applicationContext as MainApplication
        GetNowAppVersion()
        return root
    }

    override fun onResume() {
        super.onResume()
//       viewModel. GetMyvipDrive()
//        viewModel.GetAppVersion()
//        viewModel.GetRecentDrive()

    }

    override fun onStart() {
        super.onStart()

    }
    override fun onPause() {
        super.onPause()
        viewModel.isviptimer.value =false
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnclickListeners()
        fm = childFragmentManager
        viewModel.my_seq.value = application.userData?.u_seq.toString()
       // viewModel.swLockScreen.value = application.userData?.u_lock_screen == "Y"
        //iewModel.swRecProfile.value = application.userData?.u_feed == "1"
        //viewModel.swPush.value = application.getall_push()
        binding!!.swLockScreen.isEnabled = false
        binding!!.swPush.isEnabled =false
        binding!!.swRecprofile.isEnabled =false
        val range = (0..2)  // 1 <= n <= 15
        setpointext(range.random())
        binding!!.tvPoint.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        recentpageradapter =  RecentDrivePagerAdapter(requireContext(),viewModel){ data,position,type->
            ShowQuickUserDialog()
           // viewModel.DrivePassCheck(data)
        }
        binding!!.pagerRecent.adapter = recentpageradapter
        binding!!.pagerRecent.addOnPageChangeListener(object:OnPageChangeListener{
            override fun onPageSelected(position: Int) {
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
        var relist:ArrayList<RecentDriveData> = arrayListOf()
        relist.add(RecentDriveData(0,"달리는 자동차는 왜 하늘을 날지 못할까...",0,0,0,"","","","","",""))
        viewModel.recentdrivelist.value = relist
        observes()
        colorset()
        Log.d("setPushAndLockScreen :잠금", viewModel.swLockScreen.value.toString())
//        viewModel.swPush.value = application.userData?.u_lock_screen == "Y"
    }
    fun colorset(){
        binding!!.onclickHeart.background = gradientset()
        binding!!.onclickProfile.background = gradientset()
        binding!!.onclickProfileBrowse.background = gradientset()
        binding!!.onclickMatching.background = gradientset()
    }
    private fun setOnclickListeners() {
        binding!!.onclickHeart.setOnClickListener {
           ShowQuickUserDialog()
          //  requireActivity().overridePendingTransition(R.anim. fadein, R.anim.fadeout)

        }
        binding!!.onclickProfile.setOnClickListener {
            val i= Intent(requireActivity(), PrvProfileActivity::class.java)
            i.putExtra("sub_seq",application.userData!!.u_seq.toString())
            startActivity(i)
          //  requireActivity().overridePendingTransition(R.anim. fadein, R.anim.fadeout)

        }
        binding!!. onclickProfileBrowse.setOnClickListener {
            ShowQuickUserDialog()
         //   requireActivity().overridePendingTransition(R.anim. fadein, R.anim.fadeout)

        }

        binding!!.onclickMatching.setOnClickListener {
            ShowQuickUserDialog()
          //  requireActivity().overridePendingTransition(R.anim. fadein, R.anim.fadeout)

        }
        binding!!.btnPointQuestion.setOnClickListener {
            val i = Intent(requireActivity(), PointMallGuideActivity::class.java)
            resultLauncherGuide.launch(i)

        }
        binding!!.llPointmall.setOnClickListener {
            val i = Intent(requireActivity(), PrvPointMallActivity::class.java)
            startActivity(i)
        }
        binding!!.llPointset.setOnClickListener {
           ShowQuickUserDialog()
        }
        binding!!.onclickVip.setOnClickListener {
            ShowQuickUserDialog()
          //  requireActivity().overridePendingTransition(R.anim. fadein, R.anim.fadeout)
        }
        binding!!.onclickPass.setOnClickListener {
           ShowQuickUserDialog()
        }
        binding!!.clLock.setOnClickListener {
            ShowQuickUserDialog()
        }
        binding!!.clPush.setOnClickListener {
            ShowQuickUserDialog()
        }
        binding!!.clRecprofile.setOnClickListener {
            ShowQuickUserDialog()
        }
        binding!!.tvTermsOfService.setOnClickListener {
            val termsUrl = "https://supercarlounge.com/page1.php"

            val termsIntent = Intent(Intent.ACTION_VIEW, Uri.parse(termsUrl))
           startActivity(termsIntent)
        }
        binding!!.tvPolicy.setOnClickListener {
            val privacyUrl = "https://supercarlounge.com/page3.php"
            val privacyIntent = Intent(Intent.ACTION_VIEW, Uri.parse(privacyUrl))
            startActivity(privacyIntent)
        }
        binding!!.tvIssued.setOnClickListener {
            ShowQuickUserDialog()
        }
        binding!!.tvBlock.setOnClickListener {
            ShowQuickUserDialog()
         //   requireActivity().overridePendingTransition(R.anim. fadein, R.anim.fadeout)
        }
        binding!!.tvEnter.setOnClickListener {
            ShowQuickUserDialog()
          //  requireActivity().overridePendingTransition(R.anim. fadein, R.anim.fadeout)
        }
        binding!!.btnRight.setOnClickListener {
            var now = binding!!.pagerRecent.currentItem
            var max =recentpageradapter!!.mlist.size
            if(now!=max) {
                viewModel.nowrecentpage.value = now + 1

            }
        }
        binding!!.btnLeft.setOnClickListener {
            var now = binding!!.pagerRecent.currentItem
            if(now!=0) {
                viewModel.nowrecentpage.value = now -1

            }
        }
        binding!!.tvNotice.setOnClickListener {
            ShowQuickUserDialog()
//            (requireActivity() as PrvMainActivity).movepage()
        }
        binding!!.tvServiceCenter.setOnClickListener {
            ShowQuickUserDialog()
          //  requireActivity().overridePendingTransition(R.anim. fadein, R.anim.fadeout)

        }
        binding!!.tvLounge.setOnClickListener {
            ShowQuickUserDialog()
        }

        binding!!.tvLogout.setOnClickListener {
            application!!.savePhoneNumber("")
            application!!.u_phone =""
            val i= Intent(requireActivity(), LoginActivity::class.java)
            i.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(i)
         //   requireActivity().overridePendingTransition(R.anim. fadein, R.anim.fadeout)
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
                ShowQuickUserDialog()
//                var rectype : RecDriveDialogType? = RecDriveDialogType.REC_DRIVE_OFF
//                var did = RecDriveDialog(requireContext(), rectype!!) { type, okcancel ->
//                    if(okcancel) {
//                        viewModel.onRecChecked(false)
//                    }
//
//                }
//                did.show(childFragmentManager!!, "")
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
                  if(it)
                    ShowQuickUserDialog()
                 //   requireActivity().overridePendingTransition(R.anim. fadein, R.anim.fadeout)
                //}

        })
        viewModel.swPush.observe(viewLifecycleOwner, Observer {
            if(it)
            ShowQuickUserDialog()
              //application!!.setsetting(it!!)


        })
        viewModel.isviptimer.observe(viewLifecycleOwner,Observer{
            if(it){
                setTimer()
            }else{
                stoptimer()
            }
        })
        viewModel.drivepassset.observe(viewLifecycleOwner,Observer{recentdata->
            if(recentdata==null) {
                var drivepassdialog = DrivePassDialog(requireContext()) { isok->
                    if(isok){
                        val i= Intent(requireActivity(), HeartsShopActivity::class.java)
                        startActivity(i)
                        requireActivity().overridePendingTransition(R.anim. fadein, R.anim.fadeout)
                    }

                }
                drivepassdialog!!.show(childFragmentManager,"")
            }else{
                var i =Intent(requireContext(),RecentDriveListActivity::class.java)
                i.putExtra("data",recentdata)
                startActivity(i)
            }


        })
        viewModel.nowrecentpage.observe(viewLifecycleOwner,Observer{ index->
            if(recentpageradapter!!.mlist.size!=0) {
                binding!!.pagerRecent.currentItem = index
            }
        })
    }

    fun newInstance(): PrvMyPageFragment {
        val args = Bundle()

        val fragment = PrvMyPageFragment()
        fragment.arguments = args
        return fragment
    }
    fun gradientset():Drawable{
       var d = GradientDrawable(
            requireContext().getColor(R.color.cooper_gradi_st),
           requireContext().getColor(R.color.cooper_gradi_ed),
           requireContext().getColor(
                R.color.trn
            ),
           requireContext().getColor(R.color.trn),
            2f,
            10f,
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
    private fun ShowQuickUserDialog() {
        quickUserDialog =
            QuickUserDialog(requireContext(), QuickTypeStatus.QUICK_WARNING) { type, okcancel ->
                if (okcancel) {

                    quickUserDialog!!.dismiss()
                    val i = Intent(requireContext(), JoinActivity::class.java)

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
    @RequiresApi(Build.VERSION_CODES.P)
    fun setpointext(type:Int){
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(16))
        if (type == 0 ) {

            Glide.with(binding!!.ivPointLogo).load(com.supercarlounge.supercar.R.drawable.img_gas).override(binding!!.ivPointLogo.width, binding!!.ivPointLogo.height) .apply(requestOptions).into(binding!!.ivPointLogo)
            var content = getString(com.supercarlounge.supercar.R.string.pointtext_set1)
            val spannableString = SpannableString(content)
            spannableString.setSpan(
                ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
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
                ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
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
                ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
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
                (requireActivity() as PrvMainActivity).movepagehome()
            }else if (result.resultCode == 11){
                (requireActivity() as PrvMainActivity).movepageboard()
            }else if (result.resultCode == 12){
                val i= Intent(requireActivity(), PrvPointMallActivity::class.java)
                startActivity(i)
            }
        }
}