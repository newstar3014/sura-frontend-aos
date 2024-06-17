package com.supercarlounge.supercar.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.R
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.ViewTreeObserver
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.annotation.CheckResult
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.supercarlounge.supercar.Constans
import com.supercarlounge.supercar.DialogEvent
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.ScrollingLinearLayoutManager
import com.supercarlounge.supercar.adapter.LocationMainPagerAdapter
import com.supercarlounge.supercar.adapter.LocationSpotAdapter
import com.supercarlounge.supercar.adapter.NotiSlideAdapter
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.data.LocationSlideData
import com.supercarlounge.supercar.data.NotiData
import com.supercarlounge.supercar.databinding.DialogHelpTopQuestionBallonBinding
import com.supercarlounge.supercar.databinding.DialogHelpTopRihgtQuestionBallonBinding
import com.supercarlounge.supercar.databinding.FragmentLocationBinding
import com.supercarlounge.supercar.databinding.FragmentLocationMainBinding
import com.supercarlounge.supercar.databinding.FragmentLocationSpotListBinding
import com.supercarlounge.supercar.dialog.BottomSheetFilterDialog
import com.supercarlounge.supercar.dialog.BottomSheetRegionDialog
import com.supercarlounge.supercar.dialog.LocationPermissionDialog
import com.supercarlounge.supercar.dialog.PostOrderUpDialog
import com.supercarlounge.supercar.dialog.QuickUserDialog
import com.supercarlounge.supercar.enumset.QuickTypeStatus
import com.supercarlounge.supercar.ui.activity.AlimActivity
import com.supercarlounge.supercar.ui.activity.JoinActivity
import com.supercarlounge.supercar.ui.activity.LocationGuideActivity
import com.supercarlounge.supercar.ui.activity.LocationSpotSearchActivity
import com.supercarlounge.supercar.ui.activity.NewDriveWritingActivity
import com.supercarlounge.supercar.ui.activity.PostNoticeActivity
import com.supercarlounge.supercar.viewmodel.LocationFragmentViewModel
import com.supercarlounge.supercar.viewmodel.LocationMainViewModel
import com.supercarlounge.supercar.viewmodel.LocationSpotListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import java.util.Timer
import java.util.TimerTask

class LocationSpotListFragment : Fragment() {

    val TAG = "LocationSpotListFragment"
    var menupopup:PopupWindow? = null
    private var quickUserDialog: QuickUserDialog? = null
    var _binding: FragmentLocationSpotListBinding? = null
    private val binding get() = _binding
    var viewModel: LocationSpotListViewModel? = null
    var fm: FragmentManager? = null
    var application: MainApplication? = null
    var spotadapter: LocationSpotAdapter? = null
    var nAdapter: NotiSlideAdapter? = null
    var postOrderUpDialog: PostOrderUpDialog? = null
    var locationMainpageradapter: LocationMainPagerAdapter? = null
    var bottomSheetDialog: BottomSheetRegionDialog? = null
    var bottomFilterDialog: BottomSheetFilterDialog? = null
    var locationPermissionDialog: LocationPermissionDialog? = null
    var fragmentTransaction: FragmentTransaction? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        Log.d("체크", "프래그먼트 ★온크리에잇뷰!!!!")
        viewModel = ViewModelProvider(this).get(LocationSpotListViewModel::class.java)
        _binding = FragmentLocationSpotListBinding.inflate(inflater, container, false)
        application = requireContext().applicationContext as MainApplication
        viewModel!!.my_seq.value = application!!.userData!!.u_seq.toString()
        binding!!.viewModel = viewModel
        binding!!.setLifecycleOwner(this)
        val root: View = binding!!.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("체크", "프래그먼트 온뷰크리에잇")

        fm = childFragmentManager
        fragmentTransaction = parentFragmentManager.beginTransaction()

        viewModel!!.my_seq.value = application!!.userData?.u_seq.toString()


        setAdapter()
        noticeSlide()
        setObserver()

        val searchQuery = application!!.locationSearchText
        binding?.edtSearch?.setText(searchQuery)
        viewModel!!.searchVaue.value = searchQuery

        setClicks()
        setLikeLocation()
        setTabSelect()

        binding?.tabLayout?.viewTreeObserver?.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                for (i in 0..5){
                    binding!!.tabLayout.setTabWidthAsWrapContent(i)
                }
                binding?.tabLayout?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
            }
        })

    }

    override fun onResume() {
        super.onResume()
        Log.d("체크", "프래그먼트 온리썸")

        Log.d("체크 locationInfoPop", application!!.locationInfoPop.toString())
        if(!application!!.locationInfoPop){
            application!!.locationInfoPop = true
            infoPop()
        }

        viewModel!!.getAnonymousForumPostNoti(1, viewModel!!.ppp.value!!)
        viewModel!!.CheckAlim("")

        if(application!!.locationSearchMove){
            application!!.locationSearchMove = false
            val searchQuery = application!!.locationSearchText
            binding?.edtSearch?.setText(searchQuery)
            viewModel!!.searchVaue.value = searchQuery
        }

    }

    fun setClicks() {
        binding!!.ivSearchOff.setOnClickListener{
            goToSearch()
        }

        binding!!.edtSearch.setOnClickListener{
            goToSearch()
        }
    }

    private fun goToSearch(){
        val i = Intent(requireActivity(), LocationSpotSearchActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        startActivity(i)
        val fragmentTransaction2 = parentFragmentManager.beginTransaction()
        fragmentTransaction2.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
        fragmentTransaction2.commit()
    }

    private fun setLikeLocation(){
        Log.d("체크", "셋라이크로케이션")
        locationMainpageradapter = LocationMainPagerAdapter(fm!!)

        binding!!.pager3.adapter = locationMainpageradapter
        binding!!.pager3.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding!!.tabLayout))
        binding!!.pager3.setPagingEnabled(true)

    }

    private fun setTabSelect(){
        binding!!.tabLayout!!.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.d("체크", "온탭셀렉티드")
                var position = tab!!.position
                binding!!.pager3.currentItem = position

                var now = binding!!.pager3.currentItem
                viewModel!!.pagerIndex.value = position

                Log.d("체크", "체인지리스트 호출 직전")
                locationMainpageradapter!!.changeList(viewModel!!.pagerIndex.value!!, viewModel!!.searchVaue.value.toString(),viewModel!!.order.value.toString(),viewModel!!.tvregion.value.toString(),viewModel!!.payTag.value.toString(),viewModel!!.priceMin.value!!,viewModel!!.priceMax.value!!  ,viewModel!!.typelist.value!!
                )
                tab?.position?.let { binding!!.pager3.setCurrentItem(it, false) }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("전체"))
        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("추천"))
        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("카페"))
        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("맛집"))
        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("드라이브 코스"))
        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("파인다이닝"))
        binding!!.pager3.offscreenPageLimit = 5
    }

    fun infoPop(){
        binding!!.tvFilter.post {
            setpop(requireContext(), binding!!.tvRegion,"현재 설정된 지역입니다. ","관심지역","을 골라 주세요!")
        }
    }

    fun noticeSlide(){
        viewModel!!.anonymousForumNoitList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (viewModel?.anonymousForumNoitList?.value?.size != 0) {
                var timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        binding?.rvAnonymousForumNoti?.adapter?.itemCount?.let { it1 ->
                            binding!!.rvAnonymousForumNoti.smoothScrollToPosition(it1)
                        };
                        timer.cancel()
                    }
                }, 1000, 1000)
            }
        })
    }

    fun setObserver(){

        val handler: Handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                menupopup?.dismiss()
            }
        }

        viewModel!!.regionEvent.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it){

                viewModel!!.tvhintVisibilty.value = true
                val mHandler = Handler(Looper.getMainLooper())

                mHandler.removeCallbacksAndMessages(null);
                mHandler.postDelayed({
                    val msg = handler.obtainMessage()
                    handler.sendMessage(msg)
                },3000)

                bottomSheetDialog = BottomSheetRegionDialog(
                    requireContext(),
                    viewModel!!.tvregion.value.toString(),
                    viewModel!!.regionIndex.value!!,
                    viewModel!!.regionSelectDetailIndex.value,
                ) {  okcancel,index,region,subIndex ->
                    if (okcancel) {
                        if (index == 2){
                            if (subIndex != viewModel!!.regionSelectDetailIndex.value!! || subIndex ==0){
                                viewModel!!.tvregion.value = region
                                viewModel!!.regionIndex.value = index
                                viewModel!!.regionSelectDetailIndex.value = subIndex
                                locationMainpageradapter!!.changeList(viewModel!!.pagerIndex.value!!, viewModel!!.searchVaue.value.toString(),viewModel!!.order.value.toString(),viewModel!!.tvregion.value.toString(),viewModel!!.payTag.value.toString(),viewModel!!.priceMin.value!!,viewModel!!.priceMax.value!!,viewModel!!.typelist.value!!)
                            }
                        }else if (index ==3){
                            if (subIndex != viewModel!!.regionSelectDetailIndex.value!! || subIndex ==0) {
                                viewModel!!.tvregion.value = region
                                viewModel!!.regionIndex.value = index
                                viewModel!!.regionSelectDetailIndex.value = subIndex
                                locationMainpageradapter!!.changeList(viewModel!!.pagerIndex.value!!, viewModel!!.searchVaue.value.toString(),viewModel!!.order.value.toString(),viewModel!!.tvregion.value.toString(),viewModel!!.payTag.value.toString(),viewModel!!.priceMin.value!!,viewModel!!.priceMax.value!!,viewModel!!.typelist.value!!)
                            }
                        }else if (index == 0 && region == "내주변"){

                            viewModel!!.tvregion.value = region
                            viewModel!!.regionIndex.value = index
                            locationMainpageradapter!!.changeList(viewModel!!.pagerIndex.value!!, viewModel!!.searchVaue.value.toString(),viewModel!!.order.value.toString(),viewModel!!.tvregion.value.toString(),viewModel!!.payTag.value.toString(),viewModel!!.priceMin.value!!,viewModel!!.priceMax.value!!,viewModel!!.typelist.value!!)

                        } else if (region == "위치 팝업") {
                            locationPermissionDialog = LocationPermissionDialog(
                                requireContext(),
                                0
                            ) { okcancel ,index->
                                if (okcancel) {
                                    viewModel!!.tvregion.value = "내주변"
                                    viewModel!!.regionIndex.value = index
                                    locationMainpageradapter!!.changeList(viewModel!!.pagerIndex.value!!, viewModel!!.searchVaue.value.toString(),viewModel!!.order.value.toString(),viewModel!!.tvregion.value.toString(),viewModel!!.payTag.value.toString(),viewModel!!.priceMin.value!!,viewModel!!.priceMax.value!!,viewModel!!.typelist.value!!)
                                    locationPermissionDialog?.dismiss()

                                } else {
                                    locationPermissionDialog?.dismiss()
                                }
                            }
                            locationPermissionDialog!!.show(fm!!, "")
                        } else{
                            viewModel!!.tvregion.value = region
                            viewModel!!.regionIndex.value = index
                            viewModel!!.regionSelectDetailIndex.value = subIndex
                            locationMainpageradapter!!.changeList(viewModel!!.pagerIndex.value!!, viewModel!!.searchVaue.value.toString(),viewModel!!.order.value.toString(),viewModel!!.tvregion.value.toString(),viewModel!!.payTag.value.toString(),viewModel!!.priceMin.value!!,viewModel!!.priceMax.value!!,viewModel!!.typelist.value!!)
                        }
                    }
                    bottomSheetDialog?.dismiss()
                }
                bottomSheetDialog!!.show(fm!!, "")
            }
        })


        viewModel!!.filterEvent.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            if (it){
                bottomFilterDialog = BottomSheetFilterDialog(
                    requireContext(),
                    viewModel!!.order.value!!,
                    viewModel!!.typelist.value!!,
                    viewModel!!.payTag.value!!,
                    viewModel!!.priceMin.value!!,
                    viewModel!!.priceMax.value!!
                ) {  okcancel,type1,type2,type3,min,max,filterOn->
                    if (okcancel) {
                        Log.d("필터데이터보낸", "$type1\n  $type2\n  $type3\n  $min\n  $min\n " )
                        viewModel!!.order.value = type1
                        viewModel!!.typelist.value = type2
                        viewModel!!.payTag.value = type3
                        viewModel!!.priceMin.value = min
                        viewModel!!.priceMax.value = max
                        locationMainpageradapter!!.changeList(viewModel!!.pagerIndex.value!!, viewModel!!.searchVaue.value.toString(),viewModel!!.order.value.toString(),viewModel!!.tvregion.value.toString(),viewModel!!.payTag.value.toString(),viewModel!!.priceMin.value!!,viewModel!!.priceMax.value!!,
                            viewModel!!.typelist.value!!
                        )

                        viewModel!!.isfilterset.value = filterOn
                        if (  viewModel!!.isfilterset.value!!){
                            binding!!.tvFilter.post {
                                setpop2(requireContext(), binding!!.tvFilter,"상세필터 적용중")
                            }
                        }
                        Log.d("필터데이터보낸2", viewModel!!.typelist.value.toString())
                    } else {

                    }
                    bottomFilterDialog?.dismiss()
                }

                bottomFilterDialog!!.show(fm!!, "")
            }
        })


        viewModel!!.alim.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it){
                var i = Intent(requireActivity(), AlimActivity::class.java)
                startActivity(i)
                fragmentTransaction!!.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                fragmentTransaction!!.commit()
            }
        })

        viewModel!!.help.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it){
                var i = Intent(requireActivity(), LocationGuideActivity::class.java)
                startActivity(i)
                fragmentTransaction!!.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                fragmentTransaction!!.commit()
            }
        })

        viewModel!!.driveWriting.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it){
                var i = Intent(requireActivity(), NewDriveWritingActivity::class.java)
                startActivity(i)
            }
        })

        binding?.edtSearch?.textChanges()
            ?.debounce(800)
            ?.onEach {
                Log.d("체크", "텍스트체인지")
                locationMainpageradapter!!.changeList(viewModel!!.pagerIndex.value!!, viewModel!!.searchVaue.value.toString(),viewModel!!.order.value.toString(),viewModel!!.tvregion.value.toString(),viewModel!!.payTag.value.toString(),viewModel!!.priceMin.value!!,viewModel!!.priceMax.value!!,viewModel!!.typelist.value!!)
            }
            ?.launchIn(lifecycleScope)

        viewModel!!.order.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
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

        var list:ArrayList<LocationSlideData> = arrayListOf()
        list.add(LocationSlideData("","너를 위해 준비한 발렌타인데이 장소 TOP 5!","https://supercarlounge.s3.ap-northeast-2.amazonaws.com/user/6c21f38c-9dea-44b9-972a-252b115779da/board/boardCover/DriveImage20230604202636.png","",""))
        list.add(LocationSlideData("","맛좋은 장소","https://supercarlounge.s3.ap-northeast-2.amazonaws.com/user/4a3e430b-ef6c-4b37-af46-947c07062b9e/board/boardCover/DriveImage20230605000153.png","",""))
        list.add(LocationSlideData("","지역구","https://supercarlounge.s3.ap-northeast-2.amazonaws.com/user/undefined/board/boardCover/IMG_20230605_000308374.jpg.png","",""))
        list.add(LocationSlideData("","맛좋은 장소","https://supercarlounge.s3.ap-northeast-2.amazonaws.com/user/4a3e430b-ef6c-4b37-af46-947c07062b9e/board/boardCover/DriveImage20230605000153.png","",""))
        list.add(LocationSlideData("","지역구","https://supercarlounge.s3.ap-northeast-2.amazonaws.com/user/undefined/board/boardCover/IMG_20230605_000308374.jpg.png","",""))
        viewModel!!.pagedatalist.value = list

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
                    viewModel?.searchVaue?.value = s.toString()
                    trySend(s)
                }
            }
            binding?.edtSearch?.addTextChangedListener(listener)
            awaitClose { binding?.edtSearch?.removeTextChangedListener(listener) }
        }.onStart { emit(text) }
    }

    fun setpop(con: Context, view: View, question1:String,question2:String,question3:String){

        val originalPos = IntArray(2)
        view.getLocationInWindow(originalPos)


        val location = IntArray(2)
        view.getLocationOnScreen(location)

        val realRight = location[0] + view.width
        val realBottom = location[1] + view.height

        val x = originalPos[0]
        val y = originalPos[1]
        val metrisc: DisplayMetrics = resources.displayMetrics
        val displayPH = metrisc.heightPixels

        var radiusH = displayPH / 2
        var maginH = displayPH / 3.5
        var h =radiusH - maginH
        var yy =maginH
        Log.d("해상도", yy.toString())

        var popbinding: DialogHelpTopQuestionBallonBinding = DataBindingUtil.inflate(
            LayoutInflater.from(con),
            com.supercarlounge.supercar.R.layout.dialog_help_top_question_ballon,
            null,
            false
        )
        popbinding.tvQuestion1.text = question1
        popbinding.tvQuestion2.text = question2
        popbinding.tvQuestion3.text = question3
        menupopup = PopupWindow(
            popbinding.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )
        menupopup?.showAtLocation(view, Gravity.TOP or Gravity.LEFT, (realRight-realRight/1.5).toInt()  , realBottom)
        PopupAutoClose()

    }

    private fun PopupAutoClose() {
        val handler = Handler()
        val mHandler = Handler(Looper.getMainLooper())

        mHandler.removeCallbacksAndMessages(null);
        mHandler.postDelayed({
            val msg = handler.obtainMessage()
            handler.sendMessage(msg)
            menupopup?.dismiss()
        }, 3000)

    }

    fun setpop2(con: Context, view: View, question:String){

        val originalPos = IntArray(2)
        view.getLocationInWindow(originalPos)


        val location = IntArray(2)
        view.getLocationOnScreen(location)

        val realRight = location[0] + view.width
        val realBottom = location[1] + view.height

        val x = originalPos[0]
        val y = originalPos[1]
        val metrisc: DisplayMetrics = resources.displayMetrics
        val displayPW = metrisc.widthPixels
        Log.d("해상도W", displayPW.toString())

        var popbinding: DialogHelpTopRihgtQuestionBallonBinding = DataBindingUtil.inflate(
            LayoutInflater.from(con),
            com.supercarlounge.supercar.R.layout.dialog_help_top_rihgt_question_ballon,
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
        realRight
//        -displayPH/12
        if (displayPW > 1500){
            menupopup?.showAsDropDown(view,   (realRight-realRight*1.11).toInt()   , 0)
        }else{
            menupopup?.showAsDropDown(view,   (realRight-realRight*1.15).toInt()   , 0)
        }

        PopupAutoClose()
    }


    fun setAdapter() {
        if (application!!.quick_view == "퀵유저") {
            val list = arrayListOf(
                NotiData("","","공지","슈라! 문자 기능 업데이트!","","","","2022-10-24 11:00:00",""),
                NotiData("","","공지","2022 대한민국 프리미엄 브랜드 대상 수상","","","","2022-10-15 11:00:00",""),
                NotiData("","","공지","2022 대한민국 굿컴퍼니 대상 수상","","","","2022-10-15 11:00:00",""),
                NotiData("","","공지","기쁨과 사랑이 가득한,추석 보내세요!","","","","2022-09-09 11:00:00",""),
                NotiData("","","공지","슈라 드라이브 이용 가이드","","","","2022-08-06 11:00:00",""),
                NotiData("","","공지","슈라 게시판 이용 가이드","","","","2022-08-06 11:00:00","")
            )
            viewModel!!.anonymousForumNoitList.value = list
        }

        nAdapter = NotiSlideAdapter(
            requireActivity(),
            arrayListOf()
        ) { data, position ->
            Log.d("ㅎㅇ", "setAdapters: ")
            if (application!!.quick_view == "퀵유저") {
                showQuickUserDialog()
            } else {
                val i = Intent(requireActivity(), PostNoticeActivity::class.java).apply {
                    putExtra("n_seq", data.n_seq)
                    putExtra("u_seq", data.u_seq)
                    putExtra("n_sday", data.n_sday)
                    putExtra("n_title", data.n_title)
                    putExtra("n_eday", data.n_eday)
                    putExtra("n_type", data.n_type)
                    putExtra("n_date", data.n_date)
                    putExtra("n_click", data.n_click)
                }
                application!!.htmlText = data.n_text!!
                startActivity(i)
                requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }

        val linearLayoutManager = ScrollingLinearLayoutManager(
            requireActivity(), LinearLayoutManager.VERTICAL, false, 30000
        )

        binding?.rvAnonymousForumNoti?.adapter = nAdapter
        binding?.rvAnonymousForumNoti?.layoutManager = linearLayoutManager

        binding?.rvAnonymousForumNoti?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition = (recyclerView.layoutManager as ScrollingLinearLayoutManager?)?.findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter?.itemCount ?: 0 - 1

                if (!binding?.rvAnonymousForumNoti!!.canScrollVertically(1)) {
                    Log.d("SCROLL", "last Position...")
                    (binding?.rvAnonymousForumNoti!!.layoutManager as LinearLayoutManager).scrollToPosition(0)
                    binding?.rvAnonymousForumNoti?.smoothScrollToPosition(binding?.rvAnonymousForumNoti?.adapter?.itemCount!!)
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == 0) {
                    val timer = Timer()
                    timer.schedule(object : TimerTask() {
                        override fun run() {
                            binding?.rvAnonymousForumNoti?.smoothScrollToPosition(binding?.rvAnonymousForumNoti?.adapter?.itemCount!!)
                            timer.cancel()
                        }
                    }, 1000, 1000)
                }
            }
        })
    }


    private fun showQuickUserDialog() {
        quickUserDialog = QuickUserDialog(requireContext(), QuickTypeStatus.QUICK_WARNING) { type, okcancel ->
            if (okcancel) {
                quickUserDialog!!.dismiss()
                val i = Intent(requireActivity(), JoinActivity::class.java)
                i.putExtra("qickview", "퀵유저")
                i.putExtra("u_name", application!!.userBasicData?.u_name)
                i.putExtra("u_gender", application!!.userBasicData?.u_gender)
                i.putExtra("u_birthday", application!!.userBasicData?.u_birthday)
                i.putExtra("u_phone", application!!.userBasicData?.u_phone)
                Log.d("아이템3", application!!.userBasicData.toString())
                startActivity(i)
            } else {
                quickUserDialog!!.dismiss()
            }
        }
        quickUserDialog!!.show(fm!!, "")
    }


    private fun TabLayout.setTabWidthAsWrapContent(tabPosition: Int) {
        val layout = (this.getChildAt(0) as LinearLayout).getChildAt(tabPosition) as LinearLayout
        val layoutParams = layout.layoutParams as LinearLayout.LayoutParams

        var short = layout.width * 70 /100
        layoutParams.weight = 0f
        if ( tabPosition > 3){
            layoutParams.width = layout.width
        }else{
            layoutParams.width = short
        }
        layout.layoutParams = layoutParams
    }

}