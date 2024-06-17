package com.supercarlounge.supercar.ui.priview.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.CheckResult
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.supercarlounge.supercar.adapter.*
import com.supercarlounge.supercar.data.*
import com.supercarlounge.supercar.databinding.FragmentAnonymousForumBinding
import com.supercarlounge.supercar.ui.activity.*
import com.supercarlounge.supercar.viewmodel.BoardViewModel
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.supercarlounge.supercar.*
import com.supercarlounge.supercar.dialog.GuideDialog
import com.supercarlounge.supercar.dialog.PostPopUpDialog
import com.supercarlounge.supercar.dialog.QuickUserDialog
import com.supercarlounge.supercar.enumset.GuidePopType
import com.supercarlounge.supercar.enumset.QuickTypeStatus
import com.supercarlounge.supercar.ui.priview.adapter.PrvBoardPagerAdapter
import kotlinx.coroutines.*

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import java.lang.Runnable
import java.util.*
import kotlin.collections.ArrayList


class PrvBoardFragment() : Fragment(), DialogEvent {

    private var _binding: FragmentAnonymousForumBinding? = null
    var pageradapter: HeartsShopPagerAdapter? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding
    var fAdapter: BoardCategoryAdapter? = null
    var nAdapter: NotiSlideAdapter? = null
    var pAdapter: BoardListAdapter? = null
    var pnAdapter: NotiListAdapter? = null
    var viewModel: BoardViewModel? = null
    var pagerbanneradapter: BannerPagerAdapter? = null
    var anonymousForumPagerAdapter: PrvBoardPagerAdapter? = null
    private var postPopUpDialog: PostPopUpDialog? = null
    lateinit var application: MainApplication
    private var quickUserDialog :QuickUserDialog? = null
    var fm: FragmentManager? = null
    var page = 1
    var ppp = 10
    var initCall = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(BoardViewModel::class.java)
        application = requireContext()!!.applicationContext as MainApplication
        _binding = FragmentAnonymousForumBinding.inflate(inflater, container, false)
        binding!!.viewModel = viewModel
        binding!!.setLifecycleOwner(this)

        viewModel!!.text.value = "BOARD"

        val root: View = binding!!.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapters()
        observes()
        val list: ArrayList<String> = arrayListOf()
        list.add("https://steemit.com/images/poloniex.png")
        list.add("https://cdn.pixabay.com/photo/2015/03/10/17/23/youtube-667451_960_720.png")
        list.add("https://steemit.com/images/poloniex.png")
        list.add("https://cdn.pixabay.com/photo/2015/03/10/17/23/youtube-667451_960_720.png")
        pagerbanneradapter =
            BannerPagerAdapter(requireContext(), arrayListOf(),"바텀") { data, position ->
                    ShowQuickUserDialog()


            }

        binding!!.pagerAnonymousForumBanner.adapter = pagerbanneradapter

        fm = childFragmentManager
        anonymousForumPagerAdapter = PrvBoardPagerAdapter(fm!!)
        binding!!.pagerAnonymousForumPost.adapter = anonymousForumPagerAdapter
        binding!!.pagerAnonymousForumPost.offscreenPageLimit = 1

        binding!!.pagerAnonymousForumPost.addOnPageChangeListener(object : OnPageChangeListener {
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


                    viewModel!!.filterPosition.value = position
                    if (initCall) {
                        anonymousForumPagerAdapter!!.changeviewtype(

                            position,
                            viewModel!!.b_contents.value.toString(), viewModel?.bookmark?.value!!,
                            viewModel?.order?.value!!
                        )
                        Log.d("getAnonymousForumPost : onPageSelected =", position.toString())
                    }

                    anonymousForumPagerAdapter!!.changeviewtype(

                        position,
                        viewModel!!.b_contents.value.toString(), viewModel?.bookmark?.value!!,
                        viewModel?.order?.value!!
                    )


                if (!application.quick_view.equals("퀵유저")) {

                }else{

                    binding?.pagerAnonymousForumPost?.setPagingEnabled(false)
                }






            }
        })

        bannerAutoSlide()
        viewModel?.userdata?.value = application.userData
        Handler(Looper.getMainLooper()).postDelayed({

            binding!!.pagerAnonymousForumPost.currentItem = 1
            initCall =true
        }, 200L)

    }

    private fun observes() {
        viewModel!!.anonymousForumNoitList.observe(viewLifecycleOwner, Observer {
            if (it != null) {

                if (viewModel?.anonymousForumNoitList?.value?.size != 0) {
                    var timer = Timer()
                    timer.schedule(object : TimerTask() {
                        override fun run() {
                            binding?.rvAnonymousForumNoti?.adapter?.itemCount?.let { it1 ->
                                binding?.rvAnonymousForumNoti?.smoothScrollToPosition(
                                    it1
                                )
                            };
                            timer.cancel()
                        }
                    }, 1000, 1000)

                }
            }

        })
        viewModel!!.select_bannernoti_data.observe(viewLifecycleOwner, androidx.lifecycle.Observer { data->
            val i= Intent(requireActivity(), PostNoticeActivity::class.java)

            i.putExtra("n_seq", data.n_seq)
            i.putExtra("u_seq", data.u_seq)
            i.putExtra("n_sday", data.n_sday)
            i.putExtra("n_title", data.n_title)
            i.putExtra("n_eday", data.n_eday)
            i.putExtra("n_type", data.n_type)
            i.putExtra("n_date",data.n_date)
            i.putExtra("n_click",data.n_click)
            application!!.htmlText = data.n_text!!
            startActivity(i)
           // requireActivity().overridePendingTransition(R.anim. fadein, R.anim.fadeout)
        })
    }

    private fun bannerAutoSlide() {

        val handler = Handler()
        val Update = java.lang.Runnable {
            viewModel!!.bannerPosition.value = viewModel?.bannerPosition?.value!!.plus(1)
            var posi = viewModel?.bannerPosition?.value
            if (posi != null) {
                if (binding != null) {
                    if (binding?.pagerAnonymousForumBanner != null) {
                        if (posi < binding?.pagerAnonymousForumBanner!!.size) {
                            binding!!.pagerAnonymousForumBanner!!.setCurrentItem(posi, true)
                        } else {
                            viewModel!!.bannerPosition.value = 0
                            binding!!.pagerAnonymousForumBanner!!.setCurrentItem(0, true)
                        }
                    }
                }
            }


        }
        var timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 500, 3000)


    }

    override fun onResume() {
        super.onResume()
        addbanner()
        addnoti()
        //viewModel?.getAnonymousForumNoti()

        if(application!!.Check30days(application!!.checkguide4)) {

        }else{

            // 가이드팝업 제거
//            var d = GuideDialog(requireContext(), GuidePopType.GUIDE_BOARD) {
//                application!!.save_guide(GuidePopType.GUIDE_BOARD)
//            }
//            d.show(childFragmentManager, "")
        }
    }
    fun addbanner(){
        var tlist1: ArrayList<BannerData> = arrayListOf()
        tlist1.add(BannerData("",1,"","","https://supercarlounge.com:3002/images/2022-02-18banner/20220218141709배너.png","","","","","",""))
        tlist1.add(BannerData("",1,"","","https://supercarlounge.com:3002/images/2022-02-18banner/20220218141746배너1.png","","","","","",""))
        tlist1.add(BannerData("",1,"","","https://supercarlounge.com:3002/images/2022-02-18banner/20220218141755배너2.png","","","","","",""))
        viewModel!!.bannerlist.value = tlist1
    }
    fun addnoti(){
        var list : ArrayList<NotiData> = arrayListOf()
        list.add(NotiData("","","공지","슈라! 문자 기능 업데이트!","","","","2022-10-24 11:00:00",""))
        list.add(NotiData("","","공지","2022 대한민국 프리미엄 브랜드 대상 수상","","","","2022-10-15 11:00:00",""))
        list.add(NotiData("","","공지","2022 대한민국 굿컴퍼니 대상 수상","","","","2022-10-15 11:00:00",""))
        list.add(NotiData("","","공지","기쁨과 사랑이 가득한,추석 보내세요!","","","","2022-09-09 11:00:00",""))
        list.add(NotiData("","","공지","슈라 드라이브 이용 가이드","","","","2022-08-06 11:00:00",""))
        list.add(NotiData("","","공지","슈라 게시판 이용 가이드","","","","2022-08-06 11:00:00",""))
        viewModel!!.anonymousForumNoitList.value = list
    }
    private fun setAdapters() {
        fAdapter = BoardCategoryAdapter(
            requireContext(),
            testFilterData(),
            viewModel!!
        ) { data, position ->

            if (application.quick_view.equals("퀵유저")){
                ShowQuickUserDialog()
            }else {
                viewModel!!.filterPosition.value = position
                SetpostAdpater(position)
                binding?.pagerAnonymousForumPost?.setCurrentItem(position, false);
            }

            // viewModel!!.getAnonymousForumPost(data.text)
        }



        binding?.rvAnonymousForumFilter?.adapter = fAdapter
        binding?.rvAnonymousForumFilter?.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)

        nAdapter = NotiSlideAdapter(
            requireContext(),
            arrayListOf()

        ) { data, position ->
            Log.d("ㅎㅇ", "setAdapters: ")
            if (application.quick_view.equals("퀵유저")){
                ShowQuickUserDialog()
            }else {
                val i = Intent(requireActivity(), PostNoticeActivity::class.java)

                i.putExtra("n_seq", data.n_seq)
                i.putExtra("u_seq", data.u_seq)
                i.putExtra("n_sday", data.n_sday)
                i.putExtra("n_title", data.n_title)
                i.putExtra("n_eday", data.n_eday)
                i.putExtra("n_type", data.n_type)
                i.putExtra("n_date", data.n_date)
                i.putExtra("n_click",data.n_click)
                application.htmlText = data.n_text!!
                startActivity(i)
                //requireActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout)
            }

        }

        // 4F, 2F , 8F 등으로 속도 조절이 가능하다.


        var linearLayoutManager = ScrollingLinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false, 30000
        )
        binding?.rvAnonymousForumNoti?.adapter = nAdapter
        binding?.rvAnonymousForumNoti?.layoutManager = linearLayoutManager
//        binding?.rvAnonymousForumNoti?.isNestedScrollingEnabled = true;
//
//        SetpostAdpater(0)
        setOnclickListeners()

        binding?.rvAnonymousForumNoti?.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as ScrollingLinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter!!.itemCount - 1
                var posi = recyclerView.scrollY

                if (!binding?.rvAnonymousForumNoti!!.canScrollVertically(1)) {
                    Log.d("SCROLL", "last Position...");

                    (binding?.rvAnonymousForumNoti!!.layoutManager as LinearLayoutManager).scrollToPosition(
                        0
                    )

                    binding?.rvAnonymousForumNoti?.smoothScrollToPosition(binding?.rvAnonymousForumNoti?.adapter?.itemCount!!);

                }


//                Log.d("리사이클러뷰", "$lastVisibleItemPosition : $itemTotalCount ")

                if (lastVisibleItemPosition == itemTotalCount) {


                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
              //  Log.d("로그", newState.toString())
                if (newState == 0) {
                    var timer = Timer()
                    timer.schedule(object : TimerTask() {
                        override fun run() {
                            binding?.rvAnonymousForumNoti?.smoothScrollToPosition(binding?.rvAnonymousForumNoti?.adapter?.itemCount!!);
                            timer.cancel()
                        }
                    }, 1000, 1000)
                }

            }
        })


//            AnonymousForumNotiAutoSlide()

    }


    private fun AnonymousForumNotiAutoSlide() {
        var plus = 1
        var plustposi = 0 + plus
        binding?.rvAnonymousForumNoti?.smoothScrollToPosition(0)
        val handler = Handler()
        val Update = java.lang.Runnable {
            var posi = plustposi++
//            binding.rvAnonymousForumNoti.smoothScrollBy(posi, 3);
            binding?.rvAnonymousForumNoti?.post(Runnable {
                if (viewModel?.anonymousForumNoitList?.value?.size!! <= posi) {
                    plustposi = 0
                    binding?.rvAnonymousForumNoti?.smoothScrollToPosition(plustposi)
                } else {
                    binding?.rvAnonymousForumNoti?.smoothScrollToPosition(posi)
                }


            })
//            (binding.rvAnonymousForumNoti.layoutManager as LinearLayoutManager).scroy(posi,0)
        }
        var timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 3000, 3000)


    }

    private fun setOnclickListeners() {
        binding!!.ivWriting.setOnClickListener {
               ShowQuickUserDialog()


        }

        binding!!.ivSearchOff.setOnClickListener {

                ShowQuickUserDialog()


        }


        binding!!.ivSearchOn.setOnClickListener {

                ShowQuickUserDialog()


        }

        binding!!.ivOrder.setOnClickListener {
                ShowQuickUserDialog()

        }

        binding!!.ivBookmark.setOnClickListener {
                ShowQuickUserDialog()

        }
        binding!!.edtSearch.textChanges()
            .debounce(800)
            .onEach {

                anonymousForumPagerAdapter!!.changeviewtype(
                    viewModel?.filterPosition?.value!!,
                    viewModel!!.b_contents.value.toString(), viewModel?.bookmark?.value!!,
                    viewModel?.order?.value!!
                )
            }
            .launchIn(lifecycleScope)


//        binding!!.edtSearch.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {
//                viewModel?.b_contents?.value = p0.toString()
//
//
//            }
//
//            override fun afterTextChanged(p0: Editable?) {
//
//            }
//
//        })

    }

    @ExperimentalCoroutinesApi
    @CheckResult
    fun EditText.textChanges(): Flow<CharSequence?> {
        return callbackFlow<CharSequence?> {
            val listener = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) = Unit

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) = Unit

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel?.b_contents?.value = s.toString()
                    trySend(s)
                }
            }
            binding?.edtSearch?.addTextChangedListener(listener)
            awaitClose { binding?.edtSearch?.removeTextChangedListener(listener) }
        }.onStart { emit(text) }
    }


    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                anonymousForumPagerAdapter?.changeviewtype(1, "", "", "")


            }
        }


    private fun SetpostAdpater(poi: Int) {
        if (poi == 0) {
//            pnAdapter = AnonymousForumPostNotiAdapter(requireContext(), testPostNotiData(), viewModel!!) { data, position ->
//                val i= Intent(requireActivity(), PostNoticeActivity::class.java)
//                i.putExtra("titleText",data.type)
//                i.putExtra("time",data.time)
//                i.putExtra("subText",data.sub)
//                i.putExtra("mainText",data.main)
//                startActivity(i)
//                requireActivity().overridePendingTransition(R.anim. fadein, R.anim.fadeout)
//            }
//            binding.rvAnonymousForumPostNoti.adapter =pnAdapter
//            binding.rvAnonymousForumPostNoti.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        } else {
//            var list : ArrayList<AnonymousForumPostData> = arrayListOf()
//            pAdapter = AnonymousForumPostAdapter(requireContext(), list, viewModel!!) { data, position ->
//                val i= Intent(requireActivity(), PostActivity::class.java)
//                i.putExtra("titleText",data.type)
//                i.putExtra("time",data.time)
//                i.putExtra("subText",data.title)
//                i.putExtra("mainText",data.name)
//                startActivity(i)
//                requireActivity().overridePendingTransition(R.anim. fadein, R.anim.fadeout)
//            }
//            binding.rvAnonymousForumPostAll.adapter =pAdapter
//            binding.rvAnonymousForumPostAll.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        }


    }

//    private fun testNotiData(): ArrayList<AnonymousForumNotiData> {
//        var list : ArrayList<AnonymousForumNotiData> = arrayListOf()
//        var x = 0
//        for(i: Int in 0 .. 10){
//            x = x.plus(1)
//            var data = AnonymousForumNotiData("[공지] 슈라에서 서포터즈를 모집합니다.")
//            list.add(data)
//        }
//        return  list
//    }

//    private fun testPostData(): ArrayList<AnonymousForumPostData> {
//        var list : ArrayList<AnonymousForumPostData> = arrayListOf()
//        var x = 0
//        for(i: Int in 0 .. 10){
//            x = x.plus(1)
//            var data = AnonymousForumPostData("$x +공지입니다","헬린이 입니다 헬스장 추천좀 부탁해요 헬린이 입니다 헬스장 추천좀 부탁해요 헬린이 입니다 헬스장 추천좀 부탁해요헬린이 입니다 헬스장 추천좀 부탁해요헬린이 입니다 헬스장 추천좀 부탁해요헬린이 입니다 헬스장 추천좀 부탁해요","1분전","뛰노는 사마귀","100","8","54","자유게시판","남")
//            list.add(data)
//        }
//        return  list
//    }

//    private fun testPostNotiData(): ArrayList<AnonymousForumPostNotiData> {
//        var list : ArrayList<AnonymousForumPostNotiData> = arrayListOf()
//        var x = 0
//        for(i: Int in 0 .. 10){
//            x = x.plus(1)
//            var data = AnonymousForumPostNotiData("$x +공지입444","공지사항제목","[공지]","설정 즉시 적용되며, 지인으로 등록된 회원과 매칭이\n" +
//                    "불가능한 것은 아닙니다.\n" +
//                    "\n" +
//                    "지인 매칭, 리매칭 시 상대방에게 매칭 동의를 얻는\n" +
//                    "절차가 추가됩니다.\n" +
//                    "\n" +
//                    "원치 않으실 경우 지인으로 등록된 회원이 누구인지\n" +
//                    "서로 알 수 없으며 지인 매칭, 리매칭의 경우 소모되는하트가 최초 매칭과 달라 신중하게 선택하셔야 합니다.\n" +
//                    "\n" +
//                    "\n" +
//                    "감사합니다.\n", "2022.02.01")
//            list.add(data)
//        }
//        return  list
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun newInstance(): PrvBoardFragment {
        val args = Bundle()

        val fragment = PrvBoardFragment()
        fragment.arguments = args

        return fragment
    }

    private fun testFilterData(): ArrayList<BoardCategoryData> {
        var list: ArrayList<BoardCategoryData> = arrayListOf()
        var x = 0
        for (i: Int in 0 until viewModel!!.filterArray.size) {
            var data = BoardCategoryData(viewModel!!.filterArray[x])
            x = x.plus(1)
            list.add(data)
        }


        return list
    }

    private fun testPostNoticeData(): ArrayList<PostNoticeData> {
        var list: ArrayList<PostNoticeData> = arrayListOf()
        var x = 1

        var data: PostNoticeData

        for (i: Int in 0..10) {
            data = PostNoticeData("공지사항", "qweqwe", "슈라 드라이브 이용 가이드", "공지내용", "2022-08-08")
            x = x.plus(1)
            list.add(data)
        }


        return list
    }

    override fun okEvent(
        type: Int,
        okAndCancel: Boolean,
        comment_value: String,
        seq: String,
        u_nickname: String
    ) {


        if (okAndCancel) {
            when (type) {
                Constans.ORDER_MORE_NEW -> {
//                    if (viewModel?.bookmark.value){
//
//                    }

                    Log.d("이벤트", "ORDER_MORE_NEW: ")
                    viewModel?.order?.value = ""
                    anonymousForumPagerAdapter!!.changeviewtype(
                        viewModel?.filterPosition?.value!!,
                        viewModel!!.b_contents.value.toString(),
                        viewModel?.bookmark?.value!!,
                        viewModel?.order?.value!!
                    )
                    Log.d("getAnonymousForumPost : ORDER_MORE_NEW =", "")
                }
                Constans.ORDER_MORE_COMMENT -> {
                    viewModel?.order?.value = "댓글"
                    Log.d("이벤트", "ORDER_MORE_COMMENT: ")
                    anonymousForumPagerAdapter!!.changeviewtype(
                        viewModel?.filterPosition?.value!!,
                        viewModel!!.b_contents.value.toString(),
                        viewModel?.bookmark?.value!!,
                        viewModel?.order?.value!!
                    )
                    Log.d("getAnonymousForumPost : ORDER_MORE_COMMENT =", "")
                }
                Constans.ORDER_MORE_VIEWS -> {
                    viewModel?.order?.value = "조회"
                    anonymousForumPagerAdapter!!.changeviewtype(
                        viewModel?.filterPosition?.value!!,
                        viewModel!!.b_contents.value.toString(),
                        viewModel?.bookmark?.value!!,
                        viewModel?.order?.value!!
                    )
                    Log.d("getAnonymousForumPost : ORDER_MORE_VIEWS =", "")
                    Log.d("이벤트", "ORDER_MORE_VIEWS: ")
                }
                else -> {}
            }
        }
    }

    fun MoveBoardPage(postition: Int) {
        binding!!.pagerAnonymousForumPost.currentItem = postition
    }

    private fun ShowQuickUserDialog(){
        quickUserDialog = QuickUserDialog(requireContext(), QuickTypeStatus.QUICK_WARNING) { type, okcancel ->
            if (okcancel) {
                quickUserDialog!!.dismiss()
                val i = Intent(requireActivity(), JoinActivity::class.java)
                i.putExtra("qickview","퀵유저")
                i.putExtra("u_name", application.userBasicData?.u_name)
                i.putExtra("u_gender", application.userBasicData?.u_gender)
                i.putExtra("u_birthday",application.userBasicData?.u_birthday)
                i.putExtra("u_phone", application.userBasicData?.u_phone)
                Log.d("아이템3", application.userBasicData.toString())
                startActivity(i)
            } else {
                quickUserDialog!!.dismiss()
            }
        }
        quickUserDialog!!.show(fm!!, "")
    }
}