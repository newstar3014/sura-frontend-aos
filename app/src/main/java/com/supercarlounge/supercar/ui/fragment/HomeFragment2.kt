package com.supercarlounge.supercarnative.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.animation.TranslateAnimation
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.google.android.flexbox.*
import com.google.android.material.tabs.TabLayout
import com.supercarlounge.supercar.*
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.*
import com.supercarlounge.supercar.customview.MiddleItemFinder
import com.supercarlounge.supercar.customview.SuggestItemDeco
import com.supercarlounge.supercar.data.ProfileSuggestionData
import com.supercarlounge.supercar.databinding.*
import com.supercarlounge.supercar.dialog.*
import com.supercarlounge.supercar.enumset.*
import com.supercarlounge.supercar.ui.activity.*
import com.supercarlounge.supercar.ui.fragment.DriveAwayHotFragment
import com.supercarlounge.supercar.ui.fragment.DriveAwayNearFragment
import com.supercarlounge.supercar.ui.fragment.DriveAwayNewFragment
import com.supercarlounge.supercar.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class HomeFragment2 : Fragment(), DialogEvent {
    private var _binding: FragmentHome2Binding? = null
    private val binding get() = _binding
    lateinit var viewModel: HomeViewModel
    var scrolly = 0
    var fm: FragmentManager? = null
    lateinit var application: MainApplication
    var homeAdapter : HomeMultiViewAdapter? = null
    var pagerbanneradapter1: BannerPager2Adapter? = null
    var pagerbanneradapter2: BannerPagerAdapter? = null
    private var vipDriveAdapter: VipDriveAdapter? = null
    var topAdapter: VipTopDriveAdapter? = null
    var pageradapter: DriveAwayPagerAdapter? = null
    var locationpageradapter: LocationPagerAdapter? = null
    var profileSuggestionAdapter: ProfileSugesstAdapter? = null
    var dialogDriveExposure : DriveExposureDialog? =null
    var locationPermissionDialog:LocationPermissionDialog? =null
    var mnewState = 0
    var index =0
    var menupopup: PopupWindow? = null
    var timer = Timer()
    var check = false
    var viewWidth =0
    private var quickUserDialog: QuickUserDialog? = null
    var detector: GestureDetector? = null
    var warningDialog : WarningDialog? = null
    var init = false
    var nowposi = 0
    private var resultLauncherWriting : ActivityResultLauncher<Intent>?= null
    private var resultLauncherPopUp: ActivityResultLauncher<Intent>? = null

    private var dd :AddressWebViewActivity? =null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHome2Binding.inflate(inflater, container, false)
        application = context?.applicationContext as MainApplication
        viewModel.my_seq.value = application.userData?.u_seq.toString()!!
        binding!!.viewModel = viewModel
        binding!!.setLifecycleOwner(this)
        val root: View = binding!!.root
        return root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fm = childFragmentManager
        SetOnClick()
         resultLauncherWriting =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    // There are no request codes
                    val data: Intent? = result.data

                    if (data != null) {

                        var secess =data.getBooleanExtra("vip",false)
                        if (secess) {
                            dialogDriveExposure = DriveExposureDialog(
                                requireContext(),
                                Constans.DIALOG_DRIVE_EXPOSURE_1,
                                application!!.userData?.u_seq.toString()
                                ,0
                            ){ type, isonoff,addHeart ->
                                if (type == Constans.DIALOG_DRIVE_EXPOSURE_1){
                                    dialogDriveExposure?.dismiss()

                                }
                            }
                            dialogDriveExposure!!.show(this.fm!!, "")
                            Log.d("드라이브 노출1", ": ")
                        }
                    }


//                        var ft: FragmentTransaction? = fragmentManager?.beginTransaction()
//                        ft?.detach(pageradapter?.driveAwayNewFragment!!)?.attach(pageradapter?.driveAwayNewFragment!!)?.commit()
//
                        Log.d("드라이브 노출1", viewModel?.driveTabPosition?.value.toString())
                        pageradapter?.changePost(viewModel?.driveTabPosition?.value!!)








                }
            }

//        var i = Intent(context, LocationWritingActivity::class.java)
//        startActivity(i)

        requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        binding?.rlProfilesugesst?.viewTreeObserver?.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                var deviceW = binding?.rlProfilesugesst?.width
                var deviceH = binding?.rlProfilesugesst?.height

//                    model.startTopViewPosition.value =con.getViewCoordinate(com.example.supercarx.R.id.pager_banner_2)
                binding?.rlProfilesugesst?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
            }
        })

        setadapter()
        SetOserver()
        setScrollLister()
        setOnClicks()
        viewControl()
        showUpdateDialog()
        GetNowAppVersion()
        viewModel?.GetAppVersion(application)
        setLikeLocation()
        binding?.tabLayout2?.viewTreeObserver?.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                for (i in 0..5){
                    binding!!.tabLayout2.setTabWidthAsWrapContent(i)
                }

                        binding?.tabLayout2?.viewTreeObserver?.removeOnGlobalLayoutListener(this)

            }
        })

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            viewModel?.isPermission?.value =
                ContextCompat.checkSelfPermission(requireActivity(),android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED||
                        ContextCompat.checkSelfPermission(requireActivity(),android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(requireActivity(),android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
        }else{
            viewModel?.isPermission?.value =
                ContextCompat.checkSelfPermission(requireActivity(),android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED||
                        ContextCompat.checkSelfPermission(requireActivity(),android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        }


        resultLauncherPopUp = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (application!!.Check30days(application!!.checkguide1)) {

                } else {
//                        가이드팝업 제거
//                    var cond = ConciergeServiceDialog("홈가이드",requireContext(), this){
//                        application!!.save_guide(GuidePopType.GUIDE_DRIVE_VIEW)
//                        Log.d("HomeFragment", "onResume: ")
//                    }
//                    cond.show(fm!!, "")
                }

            }else if (result.resultCode == Constans.BRIDGE_MOVE_NOTICE){
                (activity as MainActivity).movepage()

            }else if (result.resultCode == Constans.MYPAGEMOVE){
                (activity as MainActivity).movepageMypage()
            }

        }
        viewModel.getBannerTop2("메인홈 큰배너")

        viewModel.getBannerBottom("드라이브 하단")



//        2023-07-19
//        binding?.appbar?.setOnTouchListener(object : View.OnTouchListener {
//            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
//                Log.d("이벤트123",p1.toString())
//                view.getParent().requestDisallowInterceptTouchEvent(false);
//                return false
//            }
//
//        })







//        binding?.pager3?.children?.find { it is RecyclerView }?.let {
//            (it as RecyclerView).isNestedScrollingEnabled = false
//        }
//        binding?.view?.setOnTouchListener { _, event ->
//            detector!!.onTouchEvent(event)
//        }
        viewModel?.isupdate?.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {

            }else{
                if (it.equals("true")){
                    var d = UpdateDialog(requireContext()) { btn ->
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.addCategory(Intent.CATEGORY_DEFAULT)
                        intent.data = Uri.parse("market://details?id=com.supercarlounge.supercar")
                        startActivity(intent)
                    }
                    d.isCancelable = false;
                    d.show(fm!!, "")
                }else{
                    viewModel.getPopUpinfo()
                }
            }
        })
        viewModel.profilesuggest.value = arrayListOf()
        if (application.CheckSuggestDate() == false) {
            viewModel.getprofileSuggestio()
        } else {
            viewModel.profilesuggest.value = application.sugeestlist
            Log.d("데이터확인", viewModel.profilesuggest.value.toString())
        }
        viewModel.isnotheart.observe(viewLifecycleOwner, Observer {
            if(it){
                var notheart  = TwoButtonDialog(requireContext(), Constans.DIALOG_LACK_HEART,this, Constans.DIALOG_LACK_HEART)
                notheart!!.show(childFragmentManager,"")
            }
        })
        viewModel.intentActivityDriveWayListEvent.observe(viewLifecycleOwner, Observer {
            if(it){
                if(it) {
                    val i = Intent(requireActivity(), DriveAwayListActivity::class.java)
                    var type= "신규"
                    when(binding!!.pager3.currentItem){
                       0->{
                            type= "신규"
                        }
                        1->{
                            type= "인기"
                        }
                       2->{
                            type= "근처"
                        }
                        else -> {}
                    }
                    i.putExtra("type",type)
                    startActivity(i)
                    requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            }
        })

        viewModel.intentIu.observe(viewLifecycleOwner, Observer {
            if (it){
                val i = Intent(requireActivity(), InformationUseActivity::class.java)
                startActivity(i)
                requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        })
        viewModel.intentFriend.observe(viewLifecycleOwner, Observer {
            if (it){

                    val i= Intent(requireActivity(), HeartsShopActivity::class.java)
                    i.putExtra("index",1)
                    startActivity(i)

                    requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

            }
        })
        viewModel.intentlocationEvent.observe(viewLifecycleOwner, Observer {
            if (it){
                val i= Intent(requireActivity(), LocationMainActivity::class.java)
                startActivity(i)

                requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        })

        viewModel.intentOnwerAndPassengerEvent.observe(viewLifecycleOwner, Observer {
            if (it){

                val i= Intent(requireActivity(), OwnerAndPassengerActivity::class.java)
                i.putExtra("list",viewModel.profilesuggest.value)
                i.putExtra("index",viewModel.selectproindex.value)
                i.putExtra("posi", nowposi)
                i.putExtra("mypicks", 0)

                resultLauncherOnwerAndPassenger.launch(i)

                requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        })


        viewModel.intentOnwerAndPassengerEvent2.observe(viewLifecycleOwner, Observer {
            if (it){

                val i= Intent(requireActivity(), OwnerAndPassengerActivity::class.java)
                i.putExtra("list",viewModel.profilesuggest.value)
                i.putExtra("index",viewModel.selectproindex.value)
                i.putExtra("posi", nowposi)
                i.putExtra("mypicks", 1)

                resultLauncherOnwerAndPassenger.launch(i)

                requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        })
    }

    fun GetNowAppVersion(){
        var version = ""
        try {
            val pInfo = requireContext().packageManager.getPackageInfo(requireContext().packageName, 0)
            version = pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        viewModel?.nowversion?.value = version

    }

    private fun showUpdateDialog() {
        Log.d("업데이트", application.userData?.checkupdate.toString())
        Log.d("업데이트", application.userData?.u_seq.toString())
        Log.d("업데이트", application.userData!!.toString())

        if (!application.userData?.u_version.equals("N")){
            if (application.userData?.checkupdate.equals("0")) {
                if (application.userData?.u_tempData.isNullOrEmpty()){
                    var oneButtonDialog = OneButtonSetDialog(
                        requireContext(),
                        PopOneTypeStatus.DIALOG_UPDATE
                    ) { type, check ->

                        if (type == PopOneTypeStatus.DIALOG_UPDATE) {
                            if (check) {
                                val i = Intent(requireContext(), ProfileCorrectionActivity::class.java)
                                i.putExtra("u_seq", viewModel.my_seq.value)
                                i.putExtra("view", "수정")
                                i.putExtra("update", "Y")
                                startActivity(i)
                                requireActivity().finish()
                                requireActivity().overridePendingTransition(
                                    com.supercarlounge.supercar.R.anim.fadein,
                                    com.supercarlounge.supercar.R.anim.fadeout
                                )
                            }
                        }
                    }
                    oneButtonDialog.isCancelable = false;
                    oneButtonDialog!!.show(fm!!, "")
                }else{
                    val i = Intent(requireContext(), ProfileCorrectionActivity::class.java)
                    i.putExtra("u_seq", viewModel.my_seq.value)
                    i.putExtra("view", "수정")
                    i.putExtra("update", "Y")
                    startActivity(i)
                    requireActivity().finish()
                    requireActivity().overridePendingTransition(
                        com.supercarlounge.supercar.R.anim.fadein,
                        com.supercarlounge.supercar.R.anim.fadeout
                    )
                }

            }
        }

    }

    fun CustomViewPager.getRecyclerView(): RecyclerView? {
        try {
            val field = CustomViewPager::class.java.getDeclaredField("mRecyclerView")
            field.isAccessible = true
            return field.get(this) as RecyclerView
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        return null
    }




















    fun setOnClicks() {



        binding?.ivTopBarShow?.setOnClickListener {
            var height = binding!!.rvTopDrive.height.toString().toFloat()
            viewModel.isShowing.value = true

            if (viewModel.isDetail.value == false) {

                if (viewModel.isShow.value == true) {
                    binding?.rvTopDrive?.visibility = View.VISIBLE

                    binding!!.ivTopBarShow.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_icon_close_16x16
                        )
                    )
                    var ani = TranslateAnimation(0f, 0f, -height, 0f)
                    ani.duration = 500
                    ani.fillAfter = true
                    binding!!.llviptop.startAnimation(ani)
                    viewModel.isShow.value = false
                } else {
                    binding!!.rvTopDrive.visibility = View.GONE
                    binding!!.ivTopBarShow.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_icon_open_16x16

                        )
                    )

                    viewModel.isShow.value = true
                }
            } else {

                if (viewModel.isShow.value == true) {
                    binding?.llTopDetail?.visibility = View.VISIBLE

                    binding!!.ivTopBarShow.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_icon_close_16x16
                        )
                    )
                    var ani = TranslateAnimation(0f, 0f, -height, 0f)
                    ani.duration = 500
                    ani.fillAfter = true
                    binding!!.llviptop.startAnimation(ani)
                    viewModel.isShow.value = false
                } else {
                    binding!!.llTopDetail.visibility = View.GONE
                    binding!!.ivTopBarShow.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_icon_open_16x16

                        )
                    )




                    viewModel.isShow.value = true
                }
            }


        }

        binding?.ivTopBarCancel?.setOnClickListener {
            viewModel.isDetail.value = false
        }
    }

    private fun viewControl() {
        binding!!.llviptop.setOnTouchListener(View.OnTouchListener { v, e -> false })
    }

    private fun setScrollLister() {
        binding!!.pagerBanner1.addOnPageChangeListener(object :
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


                viewModel!!.bannerPosition.value = position


            }
        })
        binding!!.pagerBanner2.addOnPageChangeListener(object :
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


                viewModel!!.banner2Position.value = position


            }
        })


    }

    override fun onStart() {
        super.onStart()

    }

    override fun onResume() {
        super.onResume()

        if (application.CheckSuggestDate() == false) {
            viewModel.getprofileSuggestio()
        } else {
        }
//        viewModel.getVipDrive()
//        viewModel.CheckDrivePass()
//        binding?.root?.requestLayout()

    }

    @SuppressLint("ClickableViewAccessibility")
    fun setadapter() {
        viewModel.setpid(application!!.userData!!.u_gender!!)
        profileSuggestionAdapter = ProfileSugesstAdapter(
//            deviceW,
//            deviceH,
            requireContext(),
            0,
            arrayListOf(), viewModel
        ) { data, position, width ->
            var listsize = profileSuggestionAdapter!!.datalist.size
            Log.d("리스트 사이즈 : ", listsize.toString())
            var selindex = position % listsize
            viewWidth = width
            if (viewModel.selectproindex.value == selindex) {
                if (application!!.CheckCompany() == false) {
                    var slist = application.select_index
                    var check = false
                    if (slist!!.contains(data.u_seq.toString())) {
                        check = true
                    }
                    if (!data.u_image.isNullOrEmpty()) {
                        if (check) {
                            val i = Intent(requireActivity(), ProfileActivity::class.java)
                            i.putExtra("sub_seq", data.u_seq.toString())
                            startActivity(i)
                            requireActivity().overridePendingTransition(
                                android.R.anim.fade_in,
                                android.R.anim.fade_out
                            )
                        } else {
                            var pid: RecDriveDialogType? = RecDriveDialogType.NOT_REC_DIRVE
                            if (application!!.userData?.u_feed.equals("1") && data.u_seq > 0) {
                                if (viewModel.isDrivePass.value!! && viewModel.isDrivePassCount.value!! >= 3) {
                                    pid = RecDriveDialogType.PROFILE_NOTPASS_OPEN
                                }else{
                                    pid = RecDriveDialogType.PROFILE_OPEN
                                }

                            }
                            var did = RecDriveDialog(requireContext(), pid!!) { type, okcancel ->
                                if (type == RecDriveDialogType.PROFILE_OPEN|| type == RecDriveDialogType.PROFILE_NOTPASS_OPEN) {
                                    if (okcancel) {
                                        viewModel.RecProfile(data.u_seq.toString(), viewModel.isDrivePass.value!!)
                                        //viewModel.CheckDrivePass(data)

                                    }
                                }
                            }
                            did.show(fm!!, "")
                        }

                    } else {
                        warningDialog = WarningDialog(requireContext(), 0)
                        warningDialog!!.show(this.childFragmentManager, "")
                    }
                }else{
                    viewModel.toast.value = requireContext().getString(R.string.company_profile_open_text)
                }

            } else {
                Log.d("추천프로필1","들어옴")
                viewModel.selectprodata.value = data
                viewModel.selectproindex.value = selindex
                viewModel.selectProdataUcharmStr.value = getCharmStr(data.u_charm.toString())
                val offset = (binding!!.rvProfilesugesst.width / 2 - width / 2)
                var mnan = (binding!!.rvProfilesugesst.layoutManager!! as LinearLayoutManager)
                mnan.scrollToPositionWithOffset(position, offset )


            }
        }

        binding!!.rvProfilesugesst.adapter = profileSuggestionAdapter
        binding!!.rvProfilesugesst.addItemDecoration(SuggestItemDeco(5))
        binding!!.rvProfilesugesst.apply {
            itemAnimator = null
        }

        //var layoutmanager =  CenterZoomLayoutManager(context,RecyclerView.HORIZONTAL,false,profileSuggestionAdapter!!)
        var layoutmanager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding!!.rvProfilesugesst.layoutManager = layoutmanager
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding!!.rvProfilesugesst)
        binding!!.rvProfilesugesst.addOnScrollListener(
            MiddleItemFinder(
                requireContext(),
                layoutmanager,
                object : MiddleItemFinder.MiddleItemCallback {
                    override fun scrollFinished(middleElement: Int) {
                        nowposi = middleElement
                        var listsize = profileSuggestionAdapter!!.datalist.size
                        if (listsize != 0) {
                            Log.d("추천프로필2","들어옴")
                            viewModel.selectproindex.value = middleElement % listsize
                            viewModel.selectprodata.value = profileSuggestionAdapter!!.datalist.get(middleElement % profileSuggestionAdapter!!.datalist.size)
                            viewModel.selectProdataUcharmStr.value = getCharmStr(profileSuggestionAdapter!!.datalist.get(middleElement % profileSuggestionAdapter!!.datalist.size).u_charm.toString())
                        }
                    }

                },
                RecyclerView.SCROLL_STATE_IDLE
            )
        )
        binding!!.rvProfilesugesst.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

//                if (mnewState == 0) {
//                    var scrollPosition= (binding!!.rvProfilesugesst.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
//                    val offset = (binding!!.rvProfilesugesst.width / 2 - binding!!.rvProfilesugesst.layoutManager!!.width / 2)
//                    var mnan = (binding!!.rvProfilesugesst.layoutManager!! as LinearLayoutManager)
//                    mnan.scrollToPositionWithOffset(scrollPosition, offset)
//                }

            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                Log.d("스크롤", newState.toString())

                mnewState = newState


//                    var position =
//                        (binding!!.rvProfilesugesst.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
//                    var width =
//                        (binding!!.rvProfilesugesst.layoutManager as LinearLayoutManager).width
//                    val offset = (binding!!.rvProfilesugesst.width / 2 - width / 2)
//                    var mnan = (binding!!.rvProfilesugesst.layoutManager!! as LinearLayoutManager)
//                    val position = (binding!!.rvProfilesugesst.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
//                    binding!!.rvProfilesugesst.scrollToPosition(position)

//                    mnan.scrollToPositionWithOffset(position, 100)

            }
        })

        topAdapter =
            VipTopDriveAdapter(requireContext(), arrayListOf(), viewModel) { data, position ->
                if (application?.quick_view.equals("퀵유저")) {
                    ShowQuickUserDialog()
                } else {
                    if (data!!.u_gender.equals("MALE")) {
                        Glide.with(binding!!.ivSex)
                            .load(R.drawable.ic_man_12x12)
                            .override(50, 50).centerInside().into(binding!!.ivSex)
                    } else {
                        Glide.with(binding!!.ivSex)
                            .load(R.drawable.ic_woman_12x12)
                            .override(50, 50).centerInside().into(binding!!.ivSex)
                    }

                    Glide.with(binding!!.ivTopImage).load(data.b_cover).into(binding!!.ivTopImage)
                    viewModel.isDetail.value = true
                    viewModel.choiceVipDrive.value = data
                    if (data.ub_type.equals("DRIVER")) {

                    } else {
                        viewModel.topBrand.value = data.ub_type
                    }


                    Log.d("받아온 데이터", data.toString())

                }

            }
        vipDriveAdapter =
            VipDriveAdapter(requireContext(), arrayListOf(), viewModel) { data, position ->
                if (application?.quick_view.equals("퀵유저")) {
                    ShowQuickUserDialog()
                } else {
                    val i = Intent(requireContext(), DriveDetailActivity::class.java)
                    i.putExtra("data", data)
                    i.putExtra("isvip", true)
                    i.putExtra("type", "신규")
                    i.putExtra("page", viewModel.page.value)
                    i.putExtra("allList", vipDriveAdapter!!.data)
                    i.putExtra("driveType","vip")
                    requireContext().startActivity(i)
                    requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            }
        binding!!.rvTopDrive.adapter = topAdapter
        binding!!.rvTopDrive.layoutManager = FlexboxLayoutManager(context).apply {
            justifyContent = JustifyContent.CENTER
            alignItems = AlignItems.CENTER
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.WRAP
        }
//            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        binding!!.rvVipDrive.adapter = vipDriveAdapter
//        binding!!.rvVipDrive.layoutManager =
//            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
//        binding!!.rvVipDrive.setHasFixedSize(true)

//        homeAdapter = HomeMultiViewAdapter(requireContext(), viewModel,viewLifecycleOwner,fm!!,application)
//        binding!!.rvHome.adapter = homeAdapter
//        binding!!.rvHome.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
//        (binding!!.rvHome.adapter as HomeMultiViewAdapter).apply{
//
//
//            setData()
//        }


//        application.setlistview_padding(20, binding!!.rvVipDrive, 2, false)
        pagerbanneradapter1 = BannerPager2Adapter(
            requireContext(),
            viewModel.mHomeBannerTopList.value!!
            ,"탑"
        ) { data, position ->
            if (application?.quick_view.equals("퀵유저")) {
                ShowQuickUserDialog()
            } else {
                var bc_link = data.bc_link
                if (!bc_link.isNullOrEmpty()) {
                    if (bc_link.startsWith("https")){
                        val url = bc_link
                        viewModel.viewLogBanner(data.bc_seq,"BANNER")
                        if (url.startsWith("https://www.supercarlounge.com/wv/event")){
                            var i = Intent(requireContext(),WebViewActivity::class.java)
                            var linkset = url!!+"?u_seq="+application!!.userData!!.u_seq
                            i.putExtra("url",linkset)
                            resultLauncherPopUp!!.launch(i)
                        }else {
                            val packageManager: PackageManager = requireContext().packageManager
                            if (isAppInstalled(
                                    getString(R.string.chrome_package),
                                    packageManager
                                )
                            ) {

                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                startActivity(intent)
                            } else {
                                if (isAppInstalled(
                                        getString(R.string.internet_package),
                                        packageManager
                                    )
                                ) {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "크롬이나 삼성 인터넷이 설치되어 있지 않습니다",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }else if (bc_link.equals("HEARTSHOP")){
                        viewModel.viewLogBanner(data.bc_seq,"BANNER")
                        val i= Intent(requireContext(), HeartsShopActivity::class.java)
                        startActivity(i)
                    }else if (bc_link.equals("HEARTFRIEND")){
                        viewModel!!.viewLogBanner(data.bc_seq,"BANNER")
                        val i= Intent(requireContext(), HeartsShopActivity::class.java)
                        i.putExtra("index",1)
                        startActivity(i)
                    }else if (bc_link.equals("FRIEND")){
                        viewModel!!.viewLogBanner(data.bc_seq,"BANNER")
                        viewModel!!.GetInviteCode()
                    }else if (bc_link.equals("MYCARWRITE")){
                        viewModel.viewLogBanner(data.bc_seq,"BANNER")
                        val i= Intent(requireContext(), MyCarBoardWritingActivity::class.java)
                        startActivity(i)
                    }else if (bc_link.equals("BOARDWRITE")){
                        viewModel!!.viewLogBanner(data.bc_seq,"BANNER")
                        var i = Intent(requireContext(), PostWritingActivity::class.java)
                        startActivity(i)
                    }
                    else if (bc_link.equals("DRIVEWRITE")){
                        pageradapter?.changePost(viewModel?.driveTabPosition?.value!!)
                        viewModel!!.viewLogBanner(data.bc_seq,"BANNER")
                        var i = Intent(requireContext(), NewDriveWritingActivity::class.java)
                        startActivity(i)
                    }else if (bc_link.equals("USEREDIT")){
                        viewModel!!.viewLogBanner(data.bc_seq,"BANNER")
                        val i = Intent(requireContext(), ProfileCorrectionActivity::class.java)
                        i.putExtra("u_seq", viewModel!!.my_seq.value!!)
                        i.putExtra("view", "수정")
                        startActivity(i)
                    }else if (bc_link.equals("MYPAGE")){
                        viewModel!!.viewLogBanner(data.bc_seq,"BANNER")
                        (requireActivity() as MainActivity).movepageMypage()
                    }else {
                        viewModel.viewLogBanner(data.bc_seq,"BANNER")
                        viewModel.getOneNoti(bc_link)
                    }
                }

            }
        }
        binding!!.pagerBanner1.adapter = pagerbanneradapter1
        pagerbanneradapter2 = BannerPagerAdapter(requireContext(), viewModel.mHomeBanner1List.value!!,"바텀") { data, position ->
            if (application?.quick_view.equals("퀵유저")) {
                ShowQuickUserDialog()
            } else {
                var bc_link = data.bc_link
                if (!bc_link.isNullOrEmpty()) {
                    if (bc_link.startsWith("https")){
                        val url = bc_link
                        viewModel.viewLogBanner(data.bc_seq,"BANNER")
                        if (url.startsWith("https://www.supercarlounge.com/wv/event")){
                            var i = Intent(requireContext(),WebViewActivity::class.java)
                            var linkset = url!!+"?u_seq="+application!!.userData!!.u_seq
                            i.putExtra("url",linkset)
                            resultLauncherPopUp!!.launch(i)
                        }else {
                            val packageManager: PackageManager = requireContext().packageManager
                            if (isAppInstalled(
                                    getString(R.string.chrome_package),
                                    packageManager
                                )
                            ) {

                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                startActivity(intent)
                            } else {
                                if (isAppInstalled(
                                        getString(R.string.internet_package),
                                        packageManager
                                    )
                                ) {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "크롬이나 삼성 인터넷이 설치되어 있지 않습니다",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }else if (bc_link.equals("HEARTSHOP")){
                        viewModel.viewLogBanner(data.bc_seq,"BANNER")
                        val i= Intent(requireContext(), HeartsShopActivity::class.java)
                        startActivity(i)
                    }else if (bc_link.equals("HEARTFRIEND")){
                        viewModel!!.viewLogBanner(data.bc_seq,"BANNER")
                        val i= Intent(requireContext(), HeartsShopActivity::class.java)
                        i.putExtra("index",1)
                        startActivity(i)
                    }else if (bc_link.equals("FRIEND")){
                        viewModel!!.viewLogBanner(data.bc_seq,"BANNER")
                        viewModel!!.GetInviteCode()
                    }else if (bc_link.equals("MYCARWRITE")){
                        viewModel.viewLogBanner(data.bc_seq,"BANNER")
                        val i= Intent(requireContext(), MyCarBoardWritingActivity::class.java)
                        startActivity(i)
                    }else if (bc_link.equals("BOARDWRITE")){
                        viewModel!!.viewLogBanner(data.bc_seq,"BANNER")
                        var i = Intent(requireContext(), PostWritingActivity::class.java)
                        startActivity(i)
                    }
                    else if (bc_link.equals("DRIVEWRITE")){
                        pageradapter?.changePost(viewModel?.driveTabPosition?.value!!)
                        viewModel!!.viewLogBanner(data.bc_seq,"BANNER")
                        var i = Intent(requireContext(), NewDriveWritingActivity::class.java)
                        startActivity(i)
                    }else if (bc_link.equals("USEREDIT")){
                        viewModel!!.viewLogBanner(data.bc_seq,"BANNER")
                        val i = Intent(requireContext(), ProfileCorrectionActivity::class.java)
                        i.putExtra("u_seq", viewModel!!.my_seq.value!!)
                        i.putExtra("view", "수정")
                        startActivity(i)
                    }else if (bc_link.equals("MYPAGE")){
                        viewModel!!.viewLogBanner(data.bc_seq,"BANNER")
                        (requireActivity() as MainActivity).movepageMypage()
                    }else {
                        viewModel.viewLogBanner(data.bc_seq,"BANNER")
                        viewModel.getOneNoti(bc_link)
                    }
                }
            }
        }
        binding!!.pagerBanner2.adapter = pagerbanneradapter2


        pageradapter = DriveAwayPagerAdapter(fm!!)

        binding!!.pager3.adapter = pageradapter
        binding!!.pager3.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding!!.tabLayout))
        binding!!.pager3.setPagingEnabled(true)
        binding!!.tabLayout!!.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewModel.driveTabPosition.value =tab?.position
                if (tab?.position == 2) {
                    if (viewModel.isPermission.value == true){
                        binding!!.pager3.currentItem = tab?.position!!

                        var now = binding!!.pager3.currentItem
                        pageradapter!!.changeviewtype(now, viewModel.rvItemType.value!!)
                        tab?.position?.let {
                            binding!!.pager3.setCurrentItem(it, false)
                        }
                    }else{

                        viewModel.permissionEvent.value=true

                    }
                } else {

                    binding!!.pager3.currentItem = tab?.position!!

                    var now = binding!!.pager3.currentItem
                    pageradapter!!.changeviewtype(now, viewModel.rvItemType.value!!)
                    tab?.position?.let {
                        binding!!.pager3.setCurrentItem(it, false)
                    }
                }
            }
                override fun onTabUnselected(tab: TabLayout.Tab?) {
                viewModel.tabLayoutBeforePosition.value = tab!!.position
                }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                Log.d("탭레이아웃 포지션2", tab!!.position.toString())
            }


        })


        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("NEW"))
        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("HOT"))
        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("NEAR"))
        binding!!.pager3.offscreenPageLimit = 3
        var tab1 = binding!!.tabLayout.getTabAt(0)!!.view.setOnClickListener { view ->
            var dim10 =
                view.resources.getDimensionPixelSize(R.dimen.dimen_20)
            var start = view.width / 2 - dim10
            setpop(
                requireContext(),
                binding!!.tabLayout.getTabAt(1)!!.view,
                "새로운 게시글을 확인해 보세요!",
                start,
                DialogBallonType.LEFT
            )
        }
        var tab2 = binding!!.tabLayout.getTabAt(1)!!.view.setOnClickListener { view ->
            var dim10 =
                view.resources.getDimensionPixelSize(R.dimen.dimen_20)
            var start = view.width / 2
            setpop(requireContext(), view, "인기 드라이브를 확인해 보세요!", start, DialogBallonType.CENTER)
        }
        var tab3 = binding!!.tabLayout.getTabAt(2)!!.view.setOnClickListener { view ->
//            viewModel.permissionEvent.value=true

            if (viewModel.isPermission.value ==true){
                var dim10 =
                    view.resources.getDimensionPixelSize(R.dimen.dimen_40)
                var start = -view.width / 2 - dim10
                setpop(
                    requireContext(),
                    binding!!.tabLayout.getTabAt(1)!!.view,
                    "가까운 드라이브를 확인해 보세요!",
                    start,
                    DialogBallonType.RIGHT
                )
            }


//            application?.setlocation()
        }

        //2023-07-19
//        val appBar = binding?.appbar
//        if (appBar?.layoutParams != null) {
//            val layoutParams = appBar?.layoutParams as CoordinatorLayout.LayoutParams
//            val appBarLayoutBehaviour = AppBarLayout.Behavior()
//            appBarLayoutBehaviour.setDragCallback(object : DragCallback() {
//                override fun canDrag(appBarLayout: AppBarLayout): Boolean {
//                    return true
//                }
//            })
//            layoutParams.behavior = appBarLayoutBehaviour
//        }

//        2023-07-19
//        binding!!.appbar.setExpanded(true)
//        binding!!.appbar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
//
//            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
//                scrolly = verticalOffset
//                if (binding?.llTopStart?.y != null) {
//
//
//                    if (Math.abs(verticalOffset) > binding?.llTopStart?.y!!) {
//
//
//                        if (viewModel.mVipDriveList.value!!.size != 0) {
//                            var zeroset = viewModel.mVipDriveList.value!!.get(0)
//
//                            if (zeroset.b_seq > 0) viewModel.topView.value = true
//
//                        }
//                        application.scrollcheck = true
////                        Log.d("스크롤", "onOffsetChanged: 1")
//                        Log.d("홈프래그먼트222", "true")
//
//
//                    } else {
//
//
//                        application.scrollcheck = false
//
//
//                        viewModel.topView.value = false;  //  on expand
//
//
//                    }
//
//                    if (Math.abs(verticalOffset) > binding?.tabLayout?.y!!.toInt()) {
////                        Log.d("스크롤", "onOffsetChanged: 3")
//                    } else {
////                        Log.d("스크롤", "onOffsetChanged: 4")
//                    }
//                }
//            }
//        })



//        binding!!.pidpager.adapter = SlidePidPagerAdapter(
//            requireContext(), viewLifecycleOwner,
//            viewModel.pidlist.value!!
//        ) { feeddata, position ->
//            var istip = feeddata.istip
//            if (!istip) {
//                if (application!!.CheckCompany() == false) {
//                    var rectype: RecDriveDialogType? = RecDriveDialogType.NOT_REC_DIRVE
//                    if (application!!.userData?.u_feed.equals("1")) {
//                        var piddialog = PidDialog(requireContext(), feeddata.type) { event, isok ->
//                            if (isok) {
//                                viewModel.FeedProfileSearch(feeddata.type)
//                            }
//                        }
//                        piddialog.show(fm!!, "")
//                    } else {
//                        var did = RecDriveDialog(requireContext(), rectype!!) { type, okcancel ->
//                            if (type == RecDriveDialogType.PROFILE_OPEN) {
//
//                            }
//                        }
//                        did.show(fm!!, "")
//                    }
//                }else{
//                    viewModel.toast.value = requireContext().getString(R.string.company_profile_open_text)
//                }
//            }
//        }
//        binding!!.pidpager.setPageMargin(20);
//        binding!!.tlDots.setupWithViewPager(binding!!.pidpager)


//        binding!!.pager3.getChildAt(0).setOnTouchListener { v, event ->
//            Log.d("터치", event?.action.toString())
//            when (event?.action) {
//                MotionEvent.ACTION_DOWN -> {
//                    //터치가 눌리면...
//                    binding!!.appbar.requestDisallowInterceptTouchEvent(true);
//                    //                        application.scrollcheck = false
//                    false
//                }
//                MotionEvent.ACTION_DOWN -> {
//                }
//
//            }
//            //리턴값이 false면 seekbar 동작 안됨
//            true //or false
//        }


    }
//    val onPageChangeListener = object : ViewPager.OnPageChangeListener {
//        override fun onPageScrolled(posi: Int, positionOffset: Float, positionOffsetPixels: Int) {
//            // 페이지가 스크롤되기 전 호출됩니다.
//            // 필요한 경우 스크롤을 막을 수 있는 조건을 확인합니다.
//            // 예를 들어, 특정 조건에서 스크롤을 막는다고 가정하면 다음과 같이 할 수 있습니다:
//            if (shouldBlockPageChange(posi)) {
//                    if (viewModel.isPermission.value == true){
//                        binding!!.pager3.currentItem = posi
//
//                        var now = binding!!.pager3.currentItem
//                        pageradapter!!.changeviewtype(now, viewModel.rvItemType.value!!)
//                        posi?.let {
//                            binding!!.pager3.setCurrentItem(it, false)
//                        }
//                    }else{
//
//                        viewModel.permissionEvent.value=true
//
//                    }
//            } else {
//
//                binding!!.pager3.currentItem = posi
//
//                var now = binding!!.pager3.currentItem
//                pageradapter!!.changeviewtype(now, viewModel.rvItemType.value!!)
//                posi?.let {
//                    binding!!.pager3.setCurrentItem(it, false)
//                }
//            }
//
//        }
//
//        override fun onPageSelected(position: Int) {
//            // 페이지가 선택될 때 호출됩니다.
//
//
//            var position =position
//
//        }
//
//        override fun onPageScrollStateChanged(state: Int) {
//            // 페이지 스크롤 상태가 변경될 때 호출됩니다.
//        }
//    }


    fun shouldBlockPageChange(posi:Int): Boolean {
        if (posi == 2){
            return  true
        }else{
            return  false
        }
        // 여기에 특정 조건을 확인하고, 조건을 만족하는 경우 true를 반환하여 페이지 스크롤을 막을 수 있습니다.
      // 스크롤 막기
    }
    private fun setLikeLocation(){
        locationpageradapter = LocationPagerAdapter(fm!!)

        binding!!.pager2.adapter = locationpageradapter
        binding!!.pager2.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding!!.tabLayout2))
        binding!!.pager2.setPagingEnabled(true)

        binding!!.tabLayout2!!.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

                var position = tab!!.position
                binding!!.pager2.currentItem = position
                viewModel.tabLayoutPosition.value = position
                var now = binding!!.pager2.currentItem
                tab?.position?.let { binding!!.pager2.setCurrentItem(it, false) }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })


        binding!!.tabLayout2.addTab(binding!!.tabLayout2.newTab().setText("전체"))
        binding!!.tabLayout2.addTab(binding!!.tabLayout2.newTab().setText("추천"))
        binding!!.tabLayout2.addTab(binding!!.tabLayout2.newTab().setText("카페"))
        binding!!.tabLayout2.addTab(binding!!.tabLayout2.newTab().setText("맛집"))
        binding!!.tabLayout2.addTab(binding!!.tabLayout2.newTab().setText("드라이브 코스"))
        binding!!.tabLayout2.addTab(binding!!.tabLayout2.newTab().setText("파인다이닝"))


//        binding!!.tabLayout2.setTabWidthAsWrapContent(1)
//        binding!!.tabLayout2.setTabWidthAsWrapContent(2)
//        binding!!.tabLayout2.setTabWidthAsWrapContent(3)
//        binding!!.tabLayout2.setTabWidthAsWrapContent(4)
//        binding!!.tabLayout2.setTabWidthAsWrapContent(5)
//        binding!!.tabLayout2.addTab(binding!!.tabLayout2.newTab().setText("파인다이닝"))
//        binding!!.tabLayout2.addTab(binding!!.tabLayout2.newTab().setText("맛집"))
//        binding!!.tabLayout2.addTab(binding!!.tabLayout2.newTab().setText("카페"))
        binding!!.pager2.offscreenPageLimit = 1
        binding!!.pager2.setCurrentItem(1, false)
    }

    private fun TabLayout.setTabWidthAsWrapContent(tabPosition: Int) {
        val layout = (this.getChildAt(0) as LinearLayout).getChildAt(tabPosition) as LinearLayout
        val layoutParams = layout.layoutParams as LinearLayout.LayoutParams

        var short = layout.width * 70 /100
        layoutParams.weight = 0f


            layoutParams.width = short

        Log.d("테스트 넓이", layoutParams.weight.toString())
        layout.layoutParams = layoutParams
    }
    fun SetOserver() {
        viewModel!!.inviteset.observe(viewLifecycleOwner, Observer {
            if(it){
                application!!.onDynamicLinkClick(requireContext(),viewModel!!.invite_code.value!!)
            }
        })
        viewModel.rvItemType.observe(viewLifecycleOwner, Observer {
            var page = binding!!.pager3.currentItem
            if (page == 0) {
                var drivenewfrag = pageradapter!!.getfragment(0) as DriveAwayNewFragment
//                if (it == 0) {
//                    drivenewfrag.changeviewtype(0)
//                } else {
//                    drivenewfrag.changeviewtype(1)
//                }
            } else if (page == 1) {
                var drivehotfrag = pageradapter!!.getfragment(1) as DriveAwayHotFragment
//                if (it == 0) {
//                    drivehotfrag.changeviewtype(0)
//                } else {
//                    drivehotfrag.changeviewtype(1)
//                }
            } else {
                var drivenewfrag = pageradapter!!.getfragment(2) as DriveAwayNearFragment
//                if (it == 0) {
//                    drivenewfrag.changeviewtype(0)
//                } else {
//                    drivenewfrag.changeviewtype(1)
//                }

            }
        })
        viewModel.iswriteview.observe(viewLifecycleOwner, Observer {
            if (it) {

                if (application!!.Check30days(application!!.drive_check_date) || application!!.Check30days(
                        application!!.board_check_date
                    )
                ) {

                    var i = Intent(context, NewDriveWritingActivity::class.java)

                    resultLauncherWriting?.launch(i)
                    requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                } else {
                    var i = Intent(context, PostDriveCheckActivity::class.java)
                    startActivity(i)
                    requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }

            }
        })
        viewModel.mHomeBannerTopList.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Log.d("홈프래그먼트", it.size.toString())
                Log.d("데이터", it.toString())
                if (it.size != 0) {
                    bannerAutoSlide()
                }

            }
        })
        viewModel.topView.observe(viewLifecycleOwner, Observer {
            var height = binding!!.llviptop.height.toString().toFloat()
            if (it) {
                if (binding!!.llviptop.visibility == View.GONE) {
                    binding!!.llviptop.visibility = View.VISIBLE
                    var ani = TranslateAnimation(0f, 0f, -height, 0f)
                    ani.duration = 500
                    ani.fillAfter = true
                    binding!!.llviptop.startAnimation(ani)
                }
            } else {
                if (binding!!.llviptop.visibility == View.VISIBLE) {
                    binding!!.llviptop.visibility = View.GONE
                    var ani = TranslateAnimation(0f, 0f, 0f, -height)
                    ani.duration = 500
                    ani.fillAfter = true
                    binding!!.llviptop.startAnimation(ani)
                }
            }
        })
        viewModel.open_profile_seq.observe(viewLifecycleOwner, Observer { seq ->
            if (!seq.isNullOrEmpty()) {
                application.setselectdata(seq)
                profileSuggestionAdapter?.notifyDataSetChanged()
//                profileSuggestionAdapter!!.notifyDataSetChanged()
                val i = Intent(requireActivity(), ProfileActivity::class.java)
                i.putExtra("sub_seq", seq)
                startActivity(i)
                requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

            }
        })
        viewModel.toast.observe(viewLifecycleOwner, Observer { toast ->
            if (!toast.isNullOrEmpty()) {
                if (toast.equals("하트가 부족합니다. 하트를 충전해주세요.")){
                    var drivePassPayDialog = DrivePassPayDialog(requireContext(), DrivePassDialogStatus.NOTHEART,"",0) {type, isok->
                        if(isok){
                            val i= Intent(requireContext(), HeartsShopActivity::class.java)
                            startActivity(i)
                            requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        }

                    }
                    drivePassPayDialog!!.show(fm!!,"")
                }else{
                    Toast.makeText(requireContext(), toast, Toast.LENGTH_SHORT).show()
                }

            }
        })
        viewModel.select_bannernoti_data.observe(viewLifecycleOwner, Observer { data ->
            if (data != null) {
                val i = Intent(requireActivity(), PostNoticeActivity::class.java)

                i.putExtra("n_seq", data.n_seq)
                i.putExtra("u_seq", data.u_seq)
                i.putExtra("n_sday", data.n_sday)
                i.putExtra("n_title", data.n_title)
                i.putExtra("n_eday", data.n_eday)
                i.putExtra("n_type", data.n_type)
                i.putExtra("n_date", data.n_date)
                i.putExtra("n_click",data.n_click)
                application!!.htmlText = data.n_text!!
                startActivity(i)
                requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        })
        viewModel.profilesuggest.observe(viewLifecycleOwner, Observer {
            if (it.size != 0) {
                application.setStringArrayPref("profilesugesst", it)

            }
        })

        viewModel.popUpinfoData_size.observe(viewLifecycleOwner, Observer {

            if (!init){
                init=true
                if (it != 0){
                    Log.d("테스트", "SetOserver: 1")
                    if (application!!.Check1days(application!!.checkPopUp)) {

                    } else {

                        var i = Intent(requireContext(), PopInfoActivity::class.java)
                        i.putExtra("popUpinfoData",viewModel.popUpinfoData.value)
                        i.putExtra("popUpinfoDataSize",it)
                        resultLauncherPopUp?.launch(i)
                        requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                }else{
                    Log.d("테스트", "SetOserver: 2")
                    if (application!!.Check30days(application!!.checkguide1)) {

                    } else {
//                        var d = GuideDialog(requireContext(), GuidePopType.GUIDE_DRIVE_VIEW) {
//                            application!!.save_guide(GuidePopType.GUIDE_DRIVE_VIEW)
//                            Log.d("HomeFragment", "onResume: ")
//                        }
//                        d.show(fm!!, "")
                        var cond = ConciergeServiceDialog("홈가이드",requireContext(), this){
                            application!!.save_guide(GuidePopType.GUIDE_DRIVE_VIEW)
                            Log.d("HomeFragment", "onResume: ")
                        }
                        cond.show(fm!!, "")
                    }

                }
            }

        })

        viewModel?.permissionEvent?.observe(viewLifecycleOwner, Observer {
            if (it) {
                if (viewModel.isPermission.value == false) {
                    locationPermissionDialog = LocationPermissionDialog(
                        requireContext(),
                        viewModel.tabLayoutBeforePosition.value!!
                    ) { okcancel,index ->
                        if (okcancel) {

                            locationPermissionDialog?.dismiss()
                            viewModel?.isPermission?.value = true
                            binding!!.pager3.currentItem =2
                            var now = binding!!.pager3.currentItem
                            pageradapter!!.changeviewtype(now, viewModel.rvItemType.value!!)
                            binding!!.pager3.setCurrentItem(
                                2,
                                false
                            )

// ...

// Before you perform the actual permission request, check whether your app
// already has the permissions, and whether your app needs to show a permission
// rationale dialog. For more details, see Request permissions.

                        } else {
                            binding!!.pager3.currentItem = index
                            binding!!.pager3.setCurrentItem(index, false)
                            var tab = binding!!.tabLayout.getTabAt(index)
                            tab!!.select()
                            locationPermissionDialog?.dismiss()
                            lifecycleScope.launch(Dispatchers.Main) {


                            }




//                            Handler(Looper.getMainLooper()).postDelayed({
//                                binding!!.pager3.post {
//
//                                }
//
//                                binding!!.pager3.currentItem = index
//                                binding!!.pager3.setCurrentItem(index, false)
//                                binding!!.pager3.currentItem = index
//                                binding!!.pager3.setCurrentItem(index, false)
////                                binding!!.tabLayout.getTabAt(2)?.select()
//                            }, 500)





                        }


                    }
                    locationPermissionDialog!!.show(childFragmentManager, "")
                }
            }
        })

        viewModel.eventPid.observe(viewLifecycleOwner, Observer {

            Log.d("오너패신저","마이픽스 클릭")

            var istip = viewModel.pidlist.value?.get(it)?.istip
            var feeddata = viewModel.pidlist.value?.get(it)
            if (feeddata != null) {
                if (istip == false) {
                    if (application!!.CheckCompany() == false) {
                        var rectype: RecDriveDialogType? = RecDriveDialogType.NOT_REC_DIRVE
                        if (application!!.userData?.u_feed.equals("1")) {
                            var piddialog = PidDialog(requireContext(), feeddata?.type!!) { event, isok ->
                                if (isok) {
                                    viewModel.FeedProfileSearch(feeddata?.type!!)
                                }
                            }
                            piddialog.show(fm!!, "")
                        } else {
                            var did = RecDriveDialog(requireContext(), rectype!!) { type, okcancel ->
                                if (type == RecDriveDialogType.PROFILE_OPEN) {

                                }
                            }
                            did.show(fm!!, "")
                        }
                    } else {
                        viewModel.toast.value = this.getString(R.string.company_profile_open_text)
                    }
                }
            }
        })

    }

    var resultLauncherOnwerAndPassenger =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                if (data != null) {
                    var posi =data.getIntExtra("posi",0)
                    var list =data.getParcelableArrayListExtra<ProfileSuggestionData>("list")
                    if (list?.isNotEmpty() == true) {
                        viewModel.viewWidth.value =viewWidth
                        viewModel.nowPosition.value = posi
                        viewModel.profilesuggest.value = list
                    }

                }

            }
        }
    fun SetOnClick() {

//        binding!!.btnVipMini.setOnClickListener {
//            if (application?.quick_view.equals("퀵유저")) {
//                ShowQuickUserDialog()
//            } else {
//                val i = Intent(requireActivity(), DriveVipRankActivity::class.java)
//                startActivity(i)
//                requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
//
//            }
//        }


//
//        binding!!.btnRecQuestion.setOnClickListener { view ->
//            var dim =
//                view.resources.getDimensionPixelSize(R.dimen.dimen_20)
//            var start = -500
//            setpop(
//                requireContext(),
//                binding!!.btnRecQuestion,
//                "추천 상대를 볼 수 있습니다!\n매일 오전 10시에갱신됩니다.",
//                start,
//                DialogBallonType.MAIN_RIGHT
//            )
//        }
//        binding!!.btnVipQuestion.setOnClickListener { view ->
//            var dim =
//                view.resources.getDimensionPixelSize(R.dimen.dimen_30)
//            var start = -490 - dim
//            setpop(
//                requireContext(),
//                binding!!.btnVipQuestion,
//                "나의 드라이브 글이 상단 노출됩니다!",
//                start,
//                DialogBallonType.MAIN_RIGHT
//            )
//        }
//        binding!!.btnPicQuestion.setOnClickListener { view ->
//            var dim =
//                view.resources.getDimensionPixelSize(R.dimen.dimen_40)
//            var start = -540 - dim
//            setpop(
//                requireContext(),
//                binding!!.btnPicQuestion,
//                "내가 원하는 드라이브 상대를 찾아보세요!",
//                start,
//                DialogBallonType.MAIN_RIGHT
//            )
//        }
        binding!!.cardTopDetail.setOnClickListener {

            val i = Intent(requireContext(), DriveDetailActivity::class.java)
            i.putExtra("data", viewModel.choiceVipDrive.value)
            i.putExtra("isvip", true)
            i.putExtra("type", "신규")
            i.putExtra("page", viewModel.page.value)
            i.putExtra("allList", vipDriveAdapter!!.data)
            i.putExtra("driveType","vip")
            requireContext().startActivity(i)
            requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

    }

    private fun bannerAutoSlide() {
        Log.d("HOMEFRAGMENT", "배너 시작")
        val handler = Handler()
        val Update = java.lang.Runnable {

            viewModel!!.bannerPosition.value = viewModel?.bannerPosition?.value!!.plus(1)
            viewModel!!.banner2Position.value = viewModel?.banner2Position?.value!!.plus(1)
            var posi1 = viewModel?.bannerPosition?.value
            if (posi1 != null) {
                if (binding != null) {
                    if (viewModel?.bannerPosition?.value != null) {

                        if (posi1 < viewModel?.mHomeBannerTopList?.value!!.size) {
                            binding!!.pagerBanner1!!.setCurrentItem(posi1, true)
                        } else {
                            viewModel!!.bannerPosition.value = 0
                            binding!!.pagerBanner1!!.setCurrentItem(0, true)
                        }
                    }
                }
            }

            var posi2 = viewModel?.banner2Position?.value
            if (posi2 != null) {
                if (binding != null) {
                    if (viewModel?.banner2Position?.value != null) {

                        if (posi2 < viewModel?.mHomeBanner2List?.value!!.size) {
                            binding!!.pagerBanner2!!.setCurrentItem(posi2, true)
                        } else {
                            viewModel!!.banner2Position.value = 0
                            binding!!.pagerBanner2!!.setCurrentItem(0, true)
                        }
                    }
                }
            }

//            if (binding != null) {
//                    var nowcount = binding!!.pidpager.currentItem
//                    var count = binding!!.pidpager.childCount
//                    if (nowcount <count) {
//                        var move = nowcount+1
//                        binding!!.pidpager!!.setCurrentItem(move, true)
//                    } else {
//                        binding!!.pidpager!!.setCurrentItem(0, true)
//                    }
//
//            }
        }
        var timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 500, 3000)


    }


    private fun ShowQuickUserDialog() {
        quickUserDialog =
            QuickUserDialog(requireContext(), QuickTypeStatus.QUICK_WARNING) { type, okcancel ->
                if (okcancel) {
                    quickUserDialog!!.dismiss()
                    val i = Intent(requireActivity(), JoinActivity::class.java)
                    i.putExtra("qickview", "퀵유저")
                    i.putExtra("u_name", application.userBasicData?.u_name)
                    i.putExtra("u_gender", application.userBasicData?.u_gender)
                    i.putExtra("u_birthday", application.userBasicData?.u_birthday)
                    i.putExtra("u_phone", application.userBasicData?.u_phone)
                    Log.d("아이템3", application.userBasicData.toString())
                    startActivity(i)
                    requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                } else {
                    quickUserDialog!!.dismiss()
                }
            }
        quickUserDialog!!.show(fm!!, "")
    }

    fun setpop(
        con: Context,
        view: View,
        question: String,
        paddingx: Int,
        type: DialogBallonType
    ) {

        val originalPos = IntArray(2)
        view.getLocationInWindow(originalPos)
        val x = originalPos[0]
        val y = originalPos[1]
        val metrisc: DisplayMetrics = resources.displayMetrics
        val displayPH = metrisc.heightPixels
        var radiusH = displayPH / 2
        var maginH = displayPH / 15
        var h = radiusH - maginH
        var yy = y - h
        if (type == DialogBallonType.RIGHT) {
            var popbinding: DialogRightQuestionBallonBinding = DataBindingUtil.inflate(
                LayoutInflater.from(con),
                R.layout.dialog_right_question_ballon,
                null,
                false
            )
            popbinding!!.tvQuestion.text = question
            menupopup = PopupWindow(
                popbinding.root,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true
            )
            val metrisc: DisplayMetrics = resources.displayMetrics
            val displayPW = metrisc.widthPixels
            var wset = view.x.toInt()
            Log.d("POPOFFSET", "" + paddingx)
//                menupopup.showAtLocation(view, Gravity.CENTER, 99, -30);
            Log.d("뷰테스트", yy.toString())
            menupopup!!.showAtLocation(view, Gravity.CENTER, 0, yy)
            PopupAutoClose()
        } else if (type == DialogBallonType.LEFT) {
            var popbinding: DialogLeftQuestionBallonBinding = DataBindingUtil.inflate(
                LayoutInflater.from(con),
                R.layout.dialog_left_question_ballon,
                null,
                false
            )
            popbinding!!.tvQuestion.text = question
            menupopup = PopupWindow(
                popbinding.root,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true
            )
            Log.d("뷰테스트", yy.toString())
            menupopup!!.showAtLocation(view, Gravity.CENTER, 0, yy)
            PopupAutoClose()
        } else if (type == DialogBallonType.CENTER) {
            var popbinding: DialogCenterQuestionBallonBinding = DataBindingUtil.inflate(
                LayoutInflater.from(con),
                R.layout.dialog_center_question_ballon,
                null,
                false
            )
            popbinding!!.tvQuestion.text = question
            menupopup = PopupWindow(
                popbinding.root,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true
            )
            Log.d("뷰테스트", yy.toString())
            menupopup!!.showAtLocation(view, Gravity.CENTER, 0, yy)
            PopupAutoClose()

        } else if (type == DialogBallonType.MAIN_RIGHT) {
            var popbinding: DialogHelpRightQuestionBallonBinding = DataBindingUtil.inflate(
                LayoutInflater.from(con),
                R.layout.dialog_help_right_question_ballon,
                null,
                false
            )
            popbinding!!.tvQuestion.text = question
            menupopup = PopupWindow(
                popbinding.root,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true
            )

            menupopup!!.showAsDropDown(view, 0, -30, Gravity.CENTER)
            PopupAutoClose()
        }

    }

    private fun PopupAutoClose() {

//        val mTimerTask :TimerTask
//        //타이머 객체 선언
//
//
//        // 반복적으로 사용할 TimerTask
//        mTimerTask = object : TimerTask() {
//            override fun run() {
//            3000)
//                Log.d("?", "run: ")
//            }
//        }

        val mHandler = Handler(Looper.getMainLooper())

        mHandler.removeCallbacksAndMessages(null);
        mHandler.postDelayed({
            val msg = handler.obtainMessage()
            handler.sendMessage(msg)
        }, 3000)

//            val mTimer = Handler()
//
//        mTimer.schedule(mTimerTask, 500, 3000)

    }

    val handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            if (menupopup != null) {
                if (menupopup!!.isShowing) {
                    menupopup!!.dismiss()
                }
            }
        }
    }
    fun isAppInstalled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (ex: PackageManager.NameNotFoundException) {
            false
        }
    }

    override fun okEvent(
        type: Int,
        okAndCancel: Boolean,
        comment_value: String,
        seq: String,
        u_nickname: String
    ) {

    }


    fun getCharmStr(s: String): String {
        var x = s.replace("\",\"","  ·  ")
        x = x.replace("[\"","")
        x = x.replace("\"]","")

        return x
    }

}