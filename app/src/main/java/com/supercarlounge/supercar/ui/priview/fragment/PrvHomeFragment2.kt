package com.supercarlounge.supercar.ui.priview.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.*
import com.google.android.material.tabs.TabLayout
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.*
import com.supercarlounge.supercar.customview.MiddleItemFinder
import com.supercarlounge.supercar.customview.SuggestItemDeco
import com.supercarlounge.supercar.data.BannerData
import com.supercarlounge.supercar.data.DriveAwayData
import com.supercarlounge.supercar.data.ProfileSuggestionData
import com.supercarlounge.supercar.databinding.*
import com.supercarlounge.supercar.dialog.PidDialog
import com.supercarlounge.supercar.dialog.QuickUserDialog
import com.supercarlounge.supercar.dialog.RecDriveDialog
import com.supercarlounge.supercar.enumset.DialogBallonType
import com.supercarlounge.supercar.enumset.QuickTypeStatus
import com.supercarlounge.supercar.enumset.RecDriveDialogType
import com.supercarlounge.supercar.ui.activity.*
import com.supercarlounge.supercar.ui.priview.adapter.PrvDriveAwayPagerAdapter
import com.supercarlounge.supercar.ui.priview.fragment.drive.PrvDriveAwayHotFragment
import com.supercarlounge.supercar.ui.priview.fragment.drive.PrvDriveAwayNearFragment
import com.supercarlounge.supercar.ui.priview.fragment.drive.PrvDriveAwayNewFragment
import com.supercarlounge.supercar.viewmodel.HomeViewModel


class PrvHomeFragment2 : Fragment() {
    private var _binding: FragmentHome2Binding? = null
    private val binding get() = _binding
    lateinit var viewModel: HomeViewModel
    var fm: FragmentManager? = null
    lateinit var application: MainApplication
    var pagerbanneradapter1: BannerPager2Adapter? = null
    var pagerbanneradapter2: BannerPagerAdapter? = null
    private var vipDriveAdapter: VipDriveAdapter? = null
    var topAdapter: VipTopDriveAdapter? = null
    var locationpageradapter: LocationPagerAdapter? = null
    var pageradapter: PrvDriveAwayPagerAdapter? = null
    var profileSuggestionAdapter: ProfileSugesstAdapter? = null
    private var quickUserDialog: QuickUserDialog? = null

    var menupopup :PopupWindow? = null
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
        binding!!.lifecycleOwner = this.viewLifecycleOwner
        val root: View = binding!!.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fm = childFragmentManager
        SetOnClick()
        setadapter()
        SetOserver()
        setOnClicks()
        setLikeLocation()

    }

    override fun onStart() {
        super.onStart()

    }

    override fun onResume() {
        super.onResume()
        setbanner()
        setsugesst()
        setvipdrive()
    }

    fun setOnClicks(){

        binding?.ivTopBarShow?.setOnClickListener {
            var height = binding!!.rvTopDrive.height.toString().toFloat()
            viewModel.isShowing.value =true
            if (viewModel.isShow.value == true){
                binding?.rvTopDrive?.visibility = View.VISIBLE

                binding!!.ivTopBarShow.setImageDrawable(  ContextCompat.getDrawable(
                    requireContext(),
                    com.supercarlounge.supercar.R.drawable.ar_up_24x24
                ))
                var ani = TranslateAnimation(0f, 0f, -height, 0f)
                ani.duration = 500
                ani.fillAfter = true
                binding!!.llviptop.startAnimation(ani)
                viewModel.isShow.value = false
            }else{
                binding!!.rvTopDrive.visibility = View.GONE
                binding!!.ivTopBarShow.setImageDrawable(  ContextCompat.getDrawable(
                    requireContext(),
                    com.supercarlounge.supercar.R.drawable.ar_down_24x24

                ))

//                var d =  height * 0.85
//                var x = -height *0.5
//                Log.d("ㅇㅇㅇ", x.toString())
//                binding!!.rvTopDrive.visibility = View.GONE
//                var ani = TranslateAnimation(0f, 0f, 0f, -height)
//                ani.duration = 500
//                ani.fillAfter = true
//                binding!!.rvTopDrive.startAnimation(ani)


                viewModel.isShow.value = true
            }


        }
    }

    fun setadapter() {
        viewModel.setpid(application!!.userData!!.u_gender!!)
        profileSuggestionAdapter = ProfileSugesstAdapter(
            requireContext(),
            1,
            arrayListOf(), viewModel
        ) { data, position,width ->

            ShowQuickUserDialog()
        }

        binding!!.rvProfilesugesst.adapter = profileSuggestionAdapter
        binding!!.rvProfilesugesst.addItemDecoration(SuggestItemDeco(5))
        //var layoutmanager =  CenterZoomLayoutManager(context,RecyclerView.HORIZONTAL,false,profileSuggestionAdapter!!)
        var layoutmanager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding!!.rvProfilesugesst.layoutManager = layoutmanager
        val snapHelper =PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding!!.rvProfilesugesst)
        binding!!.rvProfilesugesst.addOnScrollListener(
            MiddleItemFinder(
                requireContext(),
                layoutmanager,
                object : MiddleItemFinder.MiddleItemCallback {
                    override fun scrollFinished(middleElement: Int) {
                        var listsize = profileSuggestionAdapter!!.datalist.size
                        if (listsize != 0) {
                            viewModel.selectproindex.value =
                                middleElement % listsize
                            viewModel.selectprodata.value =
                                profileSuggestionAdapter!!.datalist.get(middleElement % profileSuggestionAdapter!!.datalist.size)
                        }
                    }

                },
                RecyclerView.SCROLL_STATE_IDLE
            )
        )
        binding!!.rvProfilesugesst.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        topAdapter =
            VipTopDriveAdapter(requireContext(), arrayListOf(), viewModel) { data, position ->
                ShowQuickUserDialog()
            }
        topAdapter =
            VipTopDriveAdapter(requireContext(), arrayListOf(), viewModel) { data, position ->

                    ShowQuickUserDialog()


            }
        vipDriveAdapter =
            VipDriveAdapter(requireContext(), arrayListOf(), viewModel) { data, position ->

                    ShowQuickUserDialog()

            }
//        binding!!.rvTopDrive.adapter = topAdapter
//        binding!!.rvTopDrive.layoutManager =
//            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        binding!!.rvVipDrive.adapter = vipDriveAdapter
//        binding!!.rvVipDrive.layoutManager =
//            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
//        binding!!.rvVipDrive.setHasFixedSize(true)
//        application.setlistview_padding(20, binding!!.rvVipDrive, 2, false)
        pagerbanneradapter1 = BannerPager2Adapter(
            requireContext(),
            viewModel.mHomeBanner1List.value!!,"탑"
        ) { data, position ->

                ShowQuickUserDialog()

        }
        binding!!.pagerBanner1.adapter = pagerbanneradapter1
        pagerbanneradapter2 = BannerPagerAdapter(
            requireContext(),
            viewModel.mHomeBanner1List.value!!,"바텀"
        ) { data, position ->
                ShowQuickUserDialog()

        }
        binding!!.pagerBanner2.adapter = pagerbanneradapter2

        pageradapter = PrvDriveAwayPagerAdapter(fm!!)

        binding!!.pager3.adapter = pageradapter
        binding!!.pager3.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding!!.tabLayout))
        binding!!.pager3.setPagingEnabled(false)

        binding!!.tabLayout!!.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

//                var position = tab!!.position
//                binding!!.pager3.currentItem = position
//                var now = binding!!.pager3.currentItem
//                pageradapter!!.changeviewtype(now, viewModel.rvItemType.value!!)
//                tab?.position?.let { binding!!.pager3.setCurrentItem(it, false) }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })


        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("NEW"))
        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("HOT"))
        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("NEAR"))
        var tab1 = binding!!.tabLayout.getTabAt(0)!!.view.setOnClickListener {view->
//            var dim10 =view.resources.getDimensionPixelSize(com.supercarlounge.supercar.R.dimen.dimen_20)
//            var start = view.width/2 -dim10
//            setpop(requireContext(),view,"새로운 드라이브를 확인해 보세요!",start,DialogBallonType.LEFT)
            ShowQuickUserDialog()
        }
        var tab2 = binding!!.tabLayout.getTabAt(1)!!.view.setOnClickListener {view->
//            var dim10 =view.resources.getDimensionPixelSize(com.supercarlounge.supercar.R.dimen.dimen_20)
//            var start = view.width/2
//            setpop(requireContext(),view,"인기 드라이브를 확인해 보세요!",start,DialogBallonType.CENTER)
            ShowQuickUserDialog()
        }
        var tab3 = binding!!.tabLayout.getTabAt(2)!!.view.setOnClickListener {view->
//            var dim10 =view.resources.getDimensionPixelSize(com.supercarlounge.supercar.R.dimen.dimen_40)
//            var start = -view.width/2-dim10
//            setpop(requireContext(),view,"가까운 드라이브를 확인해 보세요!",start,DialogBallonType.RIGHT)
            ShowQuickUserDialog()
        }
        binding!!.pager3.offscreenPageLimit = 1
//        binding!!.appbar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
//
//            @SuppressLint("SuspiciousIndentation")
//            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
//
//                if (Math.abs(verticalOffset) - appBarLayout!!.getTotalScrollRange() == 0) {
//                    if(viewModel.mVipDriveList.value!!.size!=0) {
//                        var zeroset = viewModel.mVipDriveList.value!!.get(0)
//                        if(zeroset.b_seq>0)
//                        viewModel.topView.value = true
//                    }
//
//
//                } else {
//                    viewModel.topView.value = false;  //  on expand
//
//                }
//            }
//        });
//
//      binding!!.pidpager.adapter =  SlidePidPagerAdapter(
//            requireContext(),viewLifecycleOwner,
//           viewModel.pidlist.value!!
//        ) { feeddata, position ->
//          ShowQuickUserDialog()
//        }
//        binding!!.pidpager.setPageMargin(20);
//        binding!!.tlDots.setupWithViewPager(binding!!.pidpager)
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
        binding!!.pager2.offscreenPageLimit = 1
    }
    @SuppressLint("SuspiciousIndentation")
    fun SetOserver() {

        viewModel.intentOnwerAndPassengerEvent2.observe(viewLifecycleOwner, Observer {
            if (it){
                ShowQuickUserDialog()
            }
        })

        viewModel.eventPid.observe(viewLifecycleOwner, Observer {
            ShowQuickUserDialog()
        })

        viewModel.rvItemType.observe(viewLifecycleOwner, Observer {
            var page = binding!!.pager3.currentItem
            if (page == 0) {
                var drivenewfrag = pageradapter!!.getfragment(0) as PrvDriveAwayNewFragment
//                if (it == 0) {
//                    drivenewfrag.changeviewtype(0)
//                } else {
//                    drivenewfrag.changeviewtype(1)
//                }
            } else if (page == 1) {
//                var drivehotfrag = pageradapter!!.getfragment(1) as PrvDriveAwayHotFragment
//                if (it == 0) {
//                    drivehotfrag.changeviewtype(0)
//                } else {
//                    drivehotfrag.changeviewtype(1)
//                }
            } else {
                var drivenewfrag = pageradapter!!.getfragment(2) as PrvDriveAwayNearFragment
//                if (it == 0) {
//                    drivenewfrag.changeviewtype(0)
//                } else {
//                    drivenewfrag.changeviewtype(1)
//                }



            }
        })
        viewModel.iswriteview.observe(viewLifecycleOwner, Observer {
            if(it)
            ShowQuickUserDialog()
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
        viewModel.open_profile_seq.observe(viewLifecycleOwner, Observer { seq->
//            if(!seq.isNullOrEmpty()){
//                val i= Intent(requireActivity(), ProfileActivity::class.java)
//                i.putExtra("sub_seq",seq)
//                startActivity(i)
//            }
        })
        viewModel.toast.observe(viewLifecycleOwner, Observer { toast->
            if(!toast.isNullOrEmpty()){
                Toast.makeText(requireContext(),toast,Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.select_bannernoti_data.observe(viewLifecycleOwner, Observer { data->
            if(data!=null)
            ShowQuickUserDialog()
        })
        viewModel.intentActivityDriveWayListEvent.observe(viewLifecycleOwner, Observer { data->

            if (data){
                ShowQuickUserDialog()
            }

        })
        viewModel.intentIu.observe(viewLifecycleOwner, Observer {
            if (it){
                ShowQuickUserDialog()
            }
        })
        viewModel.intentFriend.observe(viewLifecycleOwner, Observer {
            if (it){
                ShowQuickUserDialog()

            }
        })
        viewModel.intentlocationEvent.observe(viewLifecycleOwner, Observer {
            if (it){
                val i= Intent(requireActivity(), LocationMainActivity::class.java)
                startActivity(i)

                requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
//                ShowQuickUserDialog()
            }
        })
        viewModel.intentOnwerAndPassengerEvent.observe(viewLifecycleOwner, Observer {
            if (it){
                ShowQuickUserDialog()
            }
        })
    }

    fun SetOnClick() {

//        binding!!.btnVipMini.setOnClickListener {
//                ShowQuickUserDialog()
//
//        }
//        binding!!.btnRecQuestion.setOnClickListener { view->
//            var dim =view.resources.getDimensionPixelSize(com.supercarlounge.supercar.R.dimen.dimen_20)
//            var start =-500
//            setpop(requireContext(),binding!!.btnRecQuestion,"추천 상대를 볼 수 있습니다!\n매일 오전 10시에 갱신됩니다.",start,
//                DialogBallonType.MAIN_RIGHT)
//        }
//        binding!!.btnVipQuestion.setOnClickListener {view->
//            var dim =view.resources.getDimensionPixelSize(com.supercarlounge.supercar.R.dimen.dimen_30)
//            var start =-490-dim
//            setpop(requireContext(),binding!!.btnVipQuestion,"나의 드라이브 글이 상단 노출됩니다!",start,
//                DialogBallonType.MAIN_RIGHT)
//        }
//        binding!!.btnPicQuestion.setOnClickListener {view->
//            var dim =view.resources.getDimensionPixelSize(com.supercarlounge.supercar.R.dimen.dimen_40)
//            var start =-540-dim
//            setpop(requireContext(),binding!!.btnPicQuestion,"내가 원하는 드라이브 상대를 찾아보세요!",start,
//                DialogBallonType.MAIN_RIGHT)
//        }

    }


    private fun ShowQuickUserDialog() {
        quickUserDialog =
            QuickUserDialog(requireContext(), QuickTypeStatus.QUICK_WARNING) { type, okcancel ->
                if (okcancel) {
                    quickUserDialog!!.dismiss()
                    val i = Intent(requireActivity(), JoinActivity::class.java)

                    i.putExtra("qickview","퀵유저")
                    i.putExtra("u_name", application.userData?.u_name)
                    i.putExtra("u_gender", application.userData?.u_gender)
                    i.putExtra("u_birthday", application.userData?.u_birthday)
                    i.putExtra("u_phone", application.userData?.u_phone)
                    startActivity(i)
                    Log.d("가져온 유저 데이터", application.userData.toString())
                } else {
                    quickUserDialog!!.dismiss()
                }
            }
        quickUserDialog!!.show(fm!!, "")
    }
    fun setbanner(){
        var tlist1 = ArrayList<BannerData>()
        tlist1.add(BannerData("",1,"","","https://supercarlounge.com:3000/images/2023-08-10banner/20230810135252KakaoTalk2023080119441196201.jpg","","","","","",""))
        tlist1.add(BannerData("",1,"","","https://supercarlounge.com:3000/images/2023-08-10banner/20230810135312KakaoTalk2023080119441196202.jpg","","","","","",""))
        tlist1.add(BannerData("",1,"","","https://supercarlounge.com:3000/images/2023-08-10banner/20230810135341KakaoTalk2023080119441196203.jpg","","","","","",""))
        tlist1.add(BannerData("",1,"","","https://supercarlounge.com:3000/images/2023-08-10banner/20230810135355KakaoTalk2023080119441196204.jpg","","","","","",""))
        tlist1.add(BannerData("",1,"","","https://supercarlounge.com:3000/images/2023-08-10banner/20230810135406KakaoTalk2023080119441196205.jpg","","","","","",""))
        tlist1.add(BannerData("",1,"","","https://supercarlounge.com:3000/images/2023-08-10banner/20230810135419KakaoTalk2023080119441196206.jpg","","","","","",""))
        tlist1.add(BannerData("",1,"","","https://supercarlounge.com:3000/images/2023-08-10banner/20230810135430KakaoTalk2023080119441196207.jpg","","","","","",""))
        tlist1.add(BannerData("",1,"","","https://supercarlounge.com:3000/images/2023-08-10banner/20230810135444KakaoTalk2023080119441196208.jpg","","","","","",""))
        tlist1.add(BannerData("",1,"","","https://supercarlounge.com:3000/images/2023-08-10banner/20230810135500KakaoTalk2023080119441196210.jpg","","","","","",""))
        tlist1.add(BannerData("",1,"","","https://supercarlounge.com:3000/images/2023-08-10banner/20230810135514KakaoTalk2023080119441196211.jpg","","","","","",""))
        var tlist2 = ArrayList<BannerData>()
        tlist2.add(BannerData("",1,"","","https://supercarlounge.com:3002/images/2022-02-18banner/20220218141804배너3.png","","","","","",""))
        tlist2.add(BannerData("",1,"","","https://supercarlounge.com:3002/images/2022-02-18banner/20220218141814배너4.png","","","","","",""))
        tlist2.add(BannerData("",1,"","","https://supercarlounge.com:3002/images/2022-02-18banner/20220218141832배너6.png","","","","","",""))
        viewModel.mHomeBannerTopList.value = tlist1
        viewModel.mHomeBanner2List.value = tlist2
     }
    fun setsugesst(){

        var list = ArrayList<ProfileSuggestionData>()
        if (application!!.userData?.u_gender.equals("MALE")) {
            list.add(
                ProfileSuggestionData(
                    1,
                    "https://supercarlounge.s3.ap-northeast-2.amazonaws.com/user/undefined/testinfo/testImage/1686560488937.png",
                    "https://supercarlounge.com:3002/images/car_brand/20211220032231lamborghini.png",
                    "LAMBORGHINI",
                    "",
                    "",

                )
            )
            list.add(
                ProfileSuggestionData(
                    2,
                    "https://supercarlounge.s3.ap-northeast-2.amazonaws.com/user/undefined/testinfo/testImage/1686560489009.png",
                    null,
                    null,
                    "",
                    "",
                )
            )
            list.add(
                ProfileSuggestionData(
                    3,
                    "https://supercarlounge.s3.ap-northeast-2.amazonaws.com/user/undefined/testinfo/testImage/1686560489019.png",
                    "https://supercarlounge.s3.ap-northeast-2.amazonaws.com/car_brand/1646895890636.png",
                    "BMW",
                    "",
                    "",
                )
            )
            list.add(
                ProfileSuggestionData(
                    4,
                    "https://supercarlounge.s3.ap-northeast-2.amazonaws.com/user/undefined/testinfo/testImage/1686560489035.png",
                    "https://supercarlounge.s3.ap-northeast-2.amazonaws.com/car_brand/1646895904160.png",
                    "AUDI",
                    "",
                    "",
                )
            )
            list.add(
                ProfileSuggestionData(
                    4,
                    "https://supercarlounge.s3.ap-northeast-2.amazonaws.com/user/undefined/testinfo/testImage/1686560489058.png",
                    "https://supercarlounge.com:3002/images/car_brand/20211220031814fox.png",
                    "VOLKSWAGEN",
                    "",
                    "",
                )
            )
            list.add(
                ProfileSuggestionData(
                    5,
                    "https://supercarlounge.s3.ap-northeast-2.amazonaws.com/user/undefined/testinfo/testImage/1686560489076.png",
                    "https://supercarlounge.com:3002/images/car_brand/20211220032231lamborghini.png",
                    "LAMBORGHINI",
                    "",
                    "",
                )
            )
            list.add(
                ProfileSuggestionData(
                    6,
                    "https://supercarlounge.com:3002/images/cover/cover_2.png",
                    "https://supercarlounge.com:3002/images/car_brand/20211220032610ben.png",
                    "BENTLEY",
                    "",
                    "",
                )
            )
            list.add(
                ProfileSuggestionData(
                    7,
                    "https://supercarlounge.com:3002/images/cover/cover_3.png",
                    "https://supercarlounge.s3.ap-northeast-2.amazonaws.com/car_brand/1646896090020.png",
                    "VOLVO",
                    "",
                    "",
                )
            )
            list.add(
                ProfileSuggestionData(
                    8,
                    "https://supercarlounge.com:3002/images/cover/cover_4.png",
                    "https://supercarlounge.com:3002/images/car_brand/20211220032636buga.png",
                    "BUGATTI",
                    "",
                    "",
                )
            )
            list.add(
                ProfileSuggestionData(
                    9,
                    "https://supercarlounge.com:3002/images/cover/cover_5.png",
                    "https://supercarlounge.com:3002/images/car_brand/20211220032449mazuda.png",
                    "MAZDA",
                    "",
                    "",
                )
            )
        }else{
            list.add(
                ProfileSuggestionData(
                    10,
                    "https://supercarlounge.com:3002/images/preview/cover6.png",
                    "https://supercarlounge.com:3002/images/car_brand/20211220032610ben.png",
                    "BENTLEY",
                    "",
                    "",
                )
            )
            list.add(
                ProfileSuggestionData(
                    11,
                    "https://supercarlounge.com:3002/images/preview/cover7.png",
                    "https://supercarlounge.com:3002/images/car_brand/20211220032610ben.png",
                    "BENTLEY",
                    "",
                    "",
                )
            )
            list.add(
                ProfileSuggestionData(
                    12,
                    "https://supercarlounge.com:3002/images/preview/cover8.png",
                    "https://supercarlounge.s3.ap-northeast-2.amazonaws.com/car_brand/1646896090020.png",
                    "VOLVO",
                    "",
                    "",
                )
            )
            list.add(
                ProfileSuggestionData(
                    13,
                    "https://supercarlounge.com:3002/images/preview/cover9.png",
                    "https://supercarlounge.com:3002/images/car_brand/20211220032636buga.png",
                    "BUGATTI",
                    "",
                    "",
                )
            )
            list.add(
                ProfileSuggestionData(
                    14,
                    "https://supercarlounge.com:3002/images/preview/cover10.png",
                    "https://supercarlounge.com:3002/images/car_brand/20211220032449mazuda.png",
                    "MAZDA",
                    "",
                    "",
                )
            )
        }
        viewModel.profilesuggest.value = list
    }

    fun setvipdrive(){
        var list = ArrayList<DriveAwayData>()

        viewModel.mVipDriveList.value
    }
    fun setpop(con: Context, view:View,question:String,paddingx:Int,type:DialogBallonType){

        val originalPos = IntArray(2)
        view.getLocationInWindow(originalPos)
        val x = originalPos[0]
        val y = originalPos[1]
        val metrisc: DisplayMetrics = resources.displayMetrics
        val displayPH = metrisc.heightPixels
        var radiusH = displayPH/2
        var maginH= displayPH/15
        var h =radiusH - maginH
        var yy =y -  h
        if(type==DialogBallonType.RIGHT) {
            var popbinding: DialogRightQuestionBallonBinding = DataBindingUtil.inflate(
                LayoutInflater.from(con),
                com.supercarlounge.supercar.R.layout.dialog_right_question_ballon,
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
            Log.d("POPOFFSET",""+ paddingx)
//                menupopup.showAtLocation(view, Gravity.CENTER, 99, -30);
            Log.d("뷰테스트",  yy.toString())
            menupopup!!.showAtLocation(view,Gravity.CENTER, 0,   yy)
            PopupAutoClose()
        }else if(type ==DialogBallonType.LEFT){
            var popbinding: DialogLeftQuestionBallonBinding = DataBindingUtil.inflate(
                LayoutInflater.from(con),
                com.supercarlounge.supercar.R.layout.dialog_left_question_ballon,
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
            Log.d("뷰테스트",  yy.toString())
            menupopup!!.showAtLocation(view,Gravity.CENTER, 0,   yy)
            PopupAutoClose()
        }else if(type == DialogBallonType.CENTER){
            var popbinding: DialogCenterQuestionBallonBinding = DataBindingUtil.inflate(
                LayoutInflater.from(con),
                com.supercarlounge.supercar.R.layout.dialog_center_question_ballon,
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
            Log.d("뷰테스트",  yy.toString())
            menupopup!!.showAtLocation(view,Gravity.CENTER, 0,   yy)
            PopupAutoClose()

        }else if (type == DialogBallonType.MAIN_RIGHT){
            var popbinding: DialogHelpRightQuestionBallonBinding = DataBindingUtil.inflate(
                LayoutInflater.from(con),
                com.supercarlounge.supercar.R.layout.dialog_help_right_question_ballon,
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

            menupopup!!.showAsDropDown(view, 0, -30,Gravity.CENTER)
            PopupAutoClose()
        }

    }
    private fun PopupAutoClose(){

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
        },3000)

//            val mTimer = Handler()
//
//        mTimer.schedule(mTimerTask, 500, 3000)

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
}