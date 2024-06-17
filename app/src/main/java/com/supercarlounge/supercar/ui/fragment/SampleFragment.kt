//package com.example.supercarx.ui.fragment
//
//
//import android.R
//import android.annotation.SuppressLint
//import android.content.Intent
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.View.OnTouchListener
//import android.view.ViewGroup
//import androidx.annotation.Nullable
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentManager
//import androidx.lifecycle.ViewModelProvider
//import androidx.recyclerview.widget.ConcatAdapter
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.bumptech.glide.Glide
//import com.example.supercarx.*
//import com.example.supercarx.`interface`.Iawaytype
//import com.example.supercarx.adapter.*
//import com.example.supercarx.data.*
//import com.example.supercarx.databinding.FragmentSampleBinding
//import com.example.supercarx.ui.activity.LockActivity
//import com.example.supercarx.ui.activity.NoticeActivity
//import com.example.supercarx.viewmodel.HomeViewModel
//import java.util.*
//
//
//class SampleFragment() : Fragment() {
//
//    private var _binding: FragmentSampleBinding? = null
//    private val binding get() = _binding
//    private lateinit var viewModel: HomeViewModel
////    private lateinit var homeTopAdapter : ItemHomeTOPAdapter
//    private lateinit var homeMiddelAdapter : ItemHomeMiddelAdapter
//    private lateinit var homeBottomAdapter : ItemHomeBottomAdapter
//    var fm: FragmentManager? =  null
//
//
//
//    var topAdapter : VipTopDriveAdapter? =null
//
//
//
//    private var timerTask: Timer? = null
//
//    var awatypeCallback: Iawaytype? = null
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//
//        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
//        _binding = FragmentSampleBinding.inflate(inflater, container, false)
//        binding!!.viewModel = viewModel
//        binding!!.lifecycleOwner = this.viewLifecycleOwner
//        val root: View = binding!!.root
//
//        return root
//    }
//
//    private fun callbackMethod( paramFunc : (Boolean)-> Unit){
//
//            paramFunc(false)
//    }
//
//    @SuppressLint("UseCompatLoadingForDrawables", "ClickableViewAccessibility")
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        var list :ArrayList<VipDriveData> = arrayListOf()
//
//        val config = ConcatAdapter.Config.Builder()
//            .setIsolateViewTypes(false)
//            .build()
//        var lists :ArrayList<ConciergeData> = arrayListOf()
//        var data  = ConciergeData(0,0,"","",0,"","","","","","")
//        lists.add(data)
////        homeTopAdapter = ItemHomeTOPAdapter(requireContext(), lists, viewModel) { data, position ->
////
////        }
//        homeMiddelAdapter = ItemHomeMiddelAdapter(requireContext(), lists, viewModel) { data, position ->
//
//        }
//        homeBottomAdapter = ItemHomeBottomAdapter(requireContext(),childFragmentManager, lists, viewModel) { data, position ->
//
//        }
////
////        //어댑터 연결하기
////        val concatAdapter = ConcatAdapter(config,homeTopAdapter, homeMiddelAdapter,homeBottomAdapter)
////        binding!!.rvMain.adapter = concatAdapter
////
//
//
//        topAdapter = VipTopDriveAdapter(requireContext(), list, viewModel) { data, position ->
//            viewModel.topTitleText.value = data.b_title
//            viewModel.topBrandText.value = data.b_contents
//            viewModel.topView.value = true
//            Glide.with(this).load(data.b_cover).into(binding!!.ivTopImage)
//        }
//        val list2: ArrayList<String> = arrayListOf()
//        list2.add("https://cdn.pixabay.com/photo/2015/03/10/17/23/youtube-667451_960_720.png")
//        list2.add("https://steemit.com/images/poloniex.png")
//        list2.add("https://cdn.pixabay.com/photo/2015/03/10/17/23/youtube-667451_960_720.png")
//        list2.add("https://cdn.pixabay.com/photo/2015/03/10/17/23/youtube-667451_960_720.png")
//
//
//
//
//            binding!!.rvTopDrive.adapter = topAdapter
//            binding!!.rvTopDrive.layoutManager =
//                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//
////            binding!!.rvVipDrive.addItemDecoration(
////                Spacing()
////            )
//
//
//
//
//
//
//
//
////        binding!!.carouselRecyclerview.apply {
////            set3DItem(false)
////            setAlpha(false)
////            setInfinite(true)
////            setFlat(false)
////            setIntervalRatio(0.7f)
////        }
//
//
//
//
////        binding!!.carouselRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
////            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
////                super.onScrollStateChanged(recyclerView, newState)
////                binding!!.carouselRecyclerview.getSelectedPosition()
////
////            }
////        })
//    }
//
//
//
//
//
//    private fun viewControl(){
//        binding!!.llTopDrive.setOnTouchListener(OnTouchListener { v, e -> true })
//    }
////    fun getViewCoordinate(resId: Int) : Int{
////        val location = IntArray(2)
////        Log.d("위치", binding!!.pagerBanner2.getLocationOnScreen(location).toString())
////
////        val v: View = binding!!.pagerBanner2
////        val r = Rect()
////        v.getGlobalVisibleRect(r) //RootView 레이아웃을 기준으로한 좌표.
////        var left = r.left
////        var top = r.top
////        var bottom = r.bottom
////        var right = r.right
////
////
////        //custom Log
////        Log.d(
////            v.resources.getResourceName(resId).split(":".toRegex()).toTypedArray()[1] + " 의절대좌표::", "$left : $top : $bottom : $right"
////        )
////
////        return top
////    }
//
//
//
//
//
//
//
//    private fun scrollviewListeners(){
////        binding!!.scrollView.viewTreeObserver.addOnScrollChangedListener(OnScrollChangedListener {
////            val scrollY: Int = binding!!.scrollView.scrollY // For ScrollView
////            val scrollX: Int = binding!!.scrollView.scrollX // For HorizontalScrollView
////            viewModel.scrollPosition.value = scrollY
////            Log.d("homef", "스크롤 y :$scrollY 스크롤 x :$scrollX")
////            // DO SOMETHING WITH THE SCROLL COORDINATES
////        })
//    }
//    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        //getActivity() is fully created in onActivityCreated and instanceOf differentiate it between different Activities
//        if (activity is Iawaytype) awatypeCallback = activity as Iawaytype?
//    }
//
//
//    override fun onResume() {
//        super.onResume()
//
//
//    }
//
//    override fun onPause() {
//        super.onPause()
//
//    }
//
//
//    private fun setOnclickListeners() {
//        binding!!.tvTitle.setOnClickListener {
//            val i= Intent(requireActivity(), LockActivity::class.java)
//            startActivity(i)
//            requireActivity().overridePendingTransition(R.anim. fade_in, R.anim.fade_out)
//
//        }
//        binding!!.ivVipNotice.setOnClickListener {
//            val i= Intent(requireActivity(), NoticeActivity::class.java)
//            startActivity(i)
//            requireActivity().overridePendingTransition(R.anim. fade_in, R.anim.fade_out)
//        }
//        binding!!.ivTopBarCancel.setOnClickListener {
//            viewModel.topView.value = false
//        }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//
//    }
//    fun newInstance(): SampleFragment {
//        val args = Bundle()
//
//        val fragment = SampleFragment()
//        fragment.arguments = args
//        return fragment
//    }
//
////    fun testCallback(callback:(Int)-> Unit1){
////        b(0)
////    }
////
////    //#4
////    fun b(str:String){
////        //#5
////        callback.invoke(str)
////    }
//
//
//
//    private fun testPostNoticeData():ArrayList<PostNoticeData>{
//        var list : ArrayList<PostNoticeData> = arrayListOf()
//        var x = 1
//
//        var data : PostNoticeData
//
//        for(i: Int in 0 .. 10 ){
//                data = PostNoticeData("공지사항","qweqwe","슈라 드라이브 이용 가이드"  ,"공지내용","2022-08-08")
//            x = x.plus(1)
//            list.add(data)
//        }
//
//
//        return  list
//    }
//
//
//}
//
//
