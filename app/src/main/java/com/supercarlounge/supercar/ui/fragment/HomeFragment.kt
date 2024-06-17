package com.supercarlounge.supercar.ui.fragment


import android.R
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.*
import com.supercarlounge.supercar.`interface`.Iawaytype
import com.supercarlounge.supercar.adapter.*
import com.supercarlounge.supercar.data.*
import com.supercarlounge.supercar.databinding.FragmentHomeBinding
import com.supercarlounge.supercar.ui.activity.LockActivity
import com.supercarlounge.supercar.ui.activity.AlimActivity
import com.supercarlounge.supercar.viewmodel.HomeViewModel
import java.util.*


class HomeFragment() : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: HomeViewModel
    private var  adapter : VipDriveAdapter? = null
    private lateinit var homeMainAdapter : HomeMainAdapter
    private var viewPager: ViewPager2? = null
    var fm: FragmentManager? =  null
//    var pageradapter : DriveAwayPagerAdapter? =null

    var topAdapter : VipTopDriveAdapter? =null
    //var profileSuggestionAdapter : ProfileSuggestionAdapter ? =null

    lateinit var application:MainApplication

    private var timerTask: Timer? = null

    var awatypeCallback: Iawaytype? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding!!.viewModel = viewModel
        binding!!.lifecycleOwner = this.viewLifecycleOwner
        val root: View = binding!!.root

        return root
    }

    private fun callbackMethod( paramFunc : (Boolean)-> Unit){

            paramFunc(false)
    }

    @SuppressLint("UseCompatLoadingForDrawables", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        application= context?.applicationContext as MainApplication
        var list :ArrayList<DriveAwayData> = arrayListOf()
        var data = PostNoticeData(" "," "," "  ," ","")



        viewModel.getBannerTop2("드라이브 상단")
        viewModel.getVipDrive()
        viewModel.getBannerBottom("드라이브 하단")



//
//        adapter = VipDriveAdapter(requireContext(), getVipDrive("409"), viewModel) { data, position ->
//            val i = Intent(requireActivity(), DrivePostActivity::class.java)
//            i.putExtra("data",data)
//            i.putExtra("position",position)
//            startActivity(i)
//            requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
//        }


        //어댑터 연결하기

//        binding!!.scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
//            if (v.getChildAt(v.childCount - 1) != null) {
//                if (scrollY >= v.getChildAt(v.childCount - 1)
//                        .measuredHeight - v.measuredHeight &&
//                    scrollY > oldScrollY
//                ) {
//                    viewModel.page.value = viewModel.page.value?.plus(1)
//                    viewModel.getDrive(viewModel.my_u_seq.value!!)
//                    Log.d("마지막", "onViewCreated: ")
//                }
//            }
//        } as NestedScrollView.OnScrollChangeListener)



        topAdapter = VipTopDriveAdapter(requireContext(), list, viewModel) { data, position ->
            viewModel.topTitleText.value = data.b_title
            viewModel.topBrandText.value = data.b_contents
            viewModel.topView.value = true
            Glide.with(this).load(data.b_cover).into(binding!!.ivTopImage)
        }
        val list2: ArrayList<String> = arrayListOf()
        list2.add("https://cdn.pixabay.com/photo/2015/03/10/17/23/youtube-667451_960_720.png")
        list2.add("https://steemit.com/images/poloniex.png")
        list2.add("https://cdn.pixabay.com/photo/2015/03/10/17/23/youtube-667451_960_720.png")
        list2.add("https://cdn.pixabay.com/photo/2015/03/10/17/23/youtube-667451_960_720.png")





            binding!!.rvTopDrive.adapter = topAdapter
            binding!!.rvTopDrive.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

//            binding!!.rvVipDrive.adapter = adapter
//            binding!!.rvVipDrive.layoutManager =
//                GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
//            binding!!.rvVipDrive.setHasFixedSize(true)

//            binding!!.rvVipDrive.addItemDecoration(
//                Spacing()
//            )


            fm = childFragmentManager
//          //  pageradapter = DriveAwayPagerAdapter(fm!!,viewModel.rvItemType.value!!)
//         pageradapter = DriveAwayPagerAdapter(fm!!)
//        binding!!.pager3.adapter = pageradapter



            viewPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    Log.d("ViewPagerFragment", "Page ${position + 1}")
                }
            })
//            binding!!.pager3.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding!!.tabLayout))
//            binding!!.pager3.setPagingEnabled(true)
//            binding!!.tabLayout!!.addOnTabSelectedListener(object :
//                TabLayout.OnTabSelectedListener {
//                override fun onTabSelected(tab: TabLayout.Tab?) {
//
//                    var position = tab!!.position
//                    binding!!.pager3.currentItem = position
//                    var now = binding!!.pager3.currentItem
//                    pageradapter!!.changeviewtype(now,viewModel.rvItemType.value!!)
//                    tab?.position?.let { binding!!.pager3.setCurrentItem(it, false) }
//                }
//
//                override fun onTabUnselected(tab: TabLayout.Tab?) {
//
//                }
//
//                override fun onTabReselected(tab: TabLayout.Tab?) {
//
//                }
//
//            })



//            binding!!.pagerBanner2.viewTreeObserver.addOnGlobalLayoutListener(object :
//                ViewTreeObserver.OnGlobalLayoutListener {
//                override fun onGlobalLayout() {
//
//                    viewModel.startTopViewPosition.value =getViewCoordinate(com.example.supercarx.R.id.pager_banner_2)
//                    binding!!.pagerBanner2.viewTreeObserver.removeOnGlobalLayoutListener(this)
//                }
//            })
//            binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("NEW"))
//            binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("HOT"))
//            binding!!.pager3.offscreenPageLimit = 2
            setOnclickListeners()
            scrollviewListeners()
            viewControl()

//            bannerAutoSlide()
        observes()

        var pslist : ArrayList<ProfileSuggestionData> = arrayListOf()


//        var data1 =  ResultProfileSuggestionData("프로필 변경 신청이 반려되었습니다.", "0")
//
//        for (i in 1..10) {
//            pslist.add(data1)
//        }

//        profileSuggestionAdapter = ProfileSuggestionAdapter(requireContext(),pslist, viewModel) { data, position ->
//
//        }
//        viewModel.getprofileSuggestio()
//        binding!!.carouselRecyclerview.adapter = profileSuggestionAdapter
//        binding!!.carouselRecyclerview.apply {
//            set3DItem(false)
//            setAlpha(false)
//            setInfinite(true)
//            setFlat(false)
//            setIntervalRatio(0.7f)
//        }



        val carouselLayoutManager =      LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding!!.carouselRecyclerview.layoutManager = carouselLayoutManager



//        binding!!.carouselRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                binding!!.carouselRecyclerview.getSelectedPosition()
//
//            }
//        })
    }

    private fun observes() {
        viewModel.rvItemType.observe(viewLifecycleOwner, Observer {
//            var now = binding!!.pager3.currentItem
//            pageradapter!!.changeviewtype(now,it)

        })
        viewModel.profilesuggest.observe(viewLifecycleOwner, Observer {
            //profileSuggestionAdapter?.setdata(it)
        })

        viewModel.mHomeBanner1List.observe(viewLifecycleOwner, Observer {
            Log.d("mHomeBanner1List 사이즈", it.size.toString())
            initMainAdapter()

        })
        viewModel.mVipDriveList.observe(viewLifecycleOwner, Observer {
            Log.d("mVipDriveList 사이즈", it.size.toString())
            initMainAdapter()
        })
        viewModel.mHomeBanner2List.observe(viewLifecycleOwner, Observer {
            initMainAdapter()

        })
    }

    private fun initMainAdapter(){

        if (viewModel.mHomeBanner1List.value != null && viewModel.mVipDriveList.value != null && viewModel.mHomeBanner2List.value!= null) {

            homeMainAdapter = HomeMainAdapter(requireContext(), viewModel, viewModel.mHomeBanner1List.value!!, viewModel.mVipDriveList.value!!,viewModel.mHomeBanner2List.value!!,fm!!) { position ->
            }
            binding!!.rvRecyclerview.adapter = homeMainAdapter
            binding!!.rvRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun bannerAutoSlide() {

        val handler = Handler()
        val Update = java.lang.Runnable {
            viewModel!!.bannerPosition.value =viewModel!!.bannerPosition.value!!.plus(1)
            var posi = viewModel!!.bannerPosition.value!!
            if (posi <binding!!.pagerBanner1!!.size){
                binding!!.pagerBanner1!!.setCurrentItem(posi, true)
            }else{
                viewModel!!.bannerPosition.value =0
                binding!!.pagerBanner1!!.setCurrentItem(0, true)
            }
            if (posi <binding!!.pagerBanner2!!.size){
                binding!!.pagerBanner2!!.setCurrentItem(posi, true)
            }else{
                viewModel!!.bannerPosition.value =0
                binding!!.pagerBanner2!!.setCurrentItem(0, true)
            }

        }
        var timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 500, 3000)


    }



    private fun viewControl(){
        binding!!.llTopDrive.setOnTouchListener(OnTouchListener { v, e -> true })
    }
    fun getViewCoordinate(resId: Int) : Int{
        val location = IntArray(2)
        Log.d("위치", binding!!.pagerBanner2.getLocationOnScreen(location).toString())

        val v: View = binding!!.pagerBanner2
        val r = Rect()
        v.getGlobalVisibleRect(r) //RootView 레이아웃을 기준으로한 좌표.
        var left = r.left
        var top = r.top
        var bottom = r.bottom
        var right = r.right


        //custom Log
        Log.d(
            v.resources.getResourceName(resId).split(":".toRegex()).toTypedArray()[1] + " 의절대좌표::", "$left : $top : $bottom : $right"
        )

        return top
    }


//    fun getBannerTop(bl_name:String) : ArrayList<BannerData> {
//
//        var list :ArrayList<BannerData> = arrayListOf()
//        CoroutineScope(Dispatchers.IO).launch {
//            val d = NetworkManager.serveradapter!!.create(BannerService::class.java).getHomeBanner(bl_name
//            )
//            d!!.enqueue(object : retrofit2.Callback<ResultBannerData> {
//                override fun onResponse(
//                    call: retrofit2.Call<ResultBannerData>,
//                    response: Response<ResultBannerData>
//                ) {
//                    var data = response.body()
//                    if (data != null) {
//                        var rows = data.rows
//                        if (rows != null) {
//                            list.addAll(rows)
//
//                            pagerbanneradapter1?.addAllItem(list)
//                        }
//
//                    }
//
//                }
//
//                override fun onFailure(call: retrofit2.Call<ResultBannerData>, t: Throwable) {
//                    Log.d("check", "failer")
//                }
//
//            })
//
//        }
//
//
//        return list
//    }
//
//    fun getBannerBottom(bl_name:String) : ArrayList<BannerData> {
//
//        var list :ArrayList<BannerData> = arrayListOf()
//        CoroutineScope(Dispatchers.IO).launch {
//            val d = NetworkManager.serveradapter!!.create(BannerService::class.java).getHomeBanner(bl_name
//            )
//            d!!.enqueue(object : retrofit2.Callback<ResultBannerData> {
//                override fun onResponse(
//                    call: retrofit2.Call<ResultBannerData>,
//                    response: Response<ResultBannerData>
//                ) {
//                    var data = response.body()
//                    if (data != null) {
//                        var rows = data.rows
//                        if (rows != null) {
//                            list.addAll(rows)
//                            Log.d("사이즈바텀", list.size.toString())
//                            pagerbanneradapter2?.addAllItem(list)
//                        }
//                    }
//
//                }
//
//                override fun onFailure(call: retrofit2.Call<ResultBannerData>, t: Throwable) {
//                    Log.d("check", "failer")
//                }
//
//            })
//        }
//        return list
//    }

    private fun scrollviewListeners(){
//        binding!!.scrollView.viewTreeObserver.addOnScrollChangedListener(OnScrollChangedListener {
//            val scrollY: Int = binding!!.scrollView.scrollY // For ScrollView
//            val scrollX: Int = binding!!.scrollView.scrollX // For HorizontalScrollView
//            viewModel.scrollPosition.value = scrollY
//            Log.d("homef", "스크롤 y :$scrollY 스크롤 x :$scrollX")
//            // DO SOMETHING WITH THE SCROLL COORDINATES
//        })
    }
    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //getActivity() is fully created in onActivityCreated and instanceOf differentiate it between different Activities
        if (activity is Iawaytype) awatypeCallback = activity as Iawaytype?
    }


    override fun onResume() {
        super.onResume()


    }

    override fun onPause() {
        super.onPause()

    }


    private fun setOnclickListeners() {
        binding!!.tvTitle.setOnClickListener {
            val i= Intent(requireActivity(), LockActivity::class.java)
            startActivity(i)
            requireActivity().overridePendingTransition(R.anim. fade_in, R.anim.fade_out)

        }
        binding!!.ivVipNotice.setOnClickListener {
            val i= Intent(requireActivity(), AlimActivity::class.java)
            startActivity(i)
            requireActivity().overridePendingTransition(R.anim. fade_in, R.anim.fade_out)
        }
        binding!!.ivTopBarCancel.setOnClickListener {
            viewModel.topView.value = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }
    fun newInstance(): HomeFragment {
        val args = Bundle()

        val fragment = HomeFragment()
        fragment.arguments = args
        return fragment
    }

//    fun testCallback(callback:(Int)-> Unit1){
//        b(0)
//    }
//
//    //#4
//    fun b(str:String){
//        //#5
//        callback.invoke(str)
//    }



    private fun testPostNoticeData():ArrayList<PostNoticeData>{
        var list : ArrayList<PostNoticeData> = arrayListOf()
        var x = 1

        var data : PostNoticeData

        for(i: Int in 0 .. 10 ){
                data = PostNoticeData("공지사항","qweqwe","슈라 드라이브 이용 가이드"  ,"공지내용","2022-08-08")
            x = x.plus(1)
            list.add(data)
        }


        return  list
    }


}


