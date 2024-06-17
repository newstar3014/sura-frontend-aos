package com.supercarlounge.supercar.ui.fragment

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
import androidx.annotation.CheckResult
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.supercarlounge.supercar.*
import com.supercarlounge.supercar.adapter.LocationSpotAdapter
import com.supercarlounge.supercar.adapter.NotiSlideAdapter
import com.supercarlounge.supercar.data.LocationSlideData
import com.supercarlounge.supercar.data.NotiData
import com.supercarlounge.supercar.databinding.ActivityLocationMainDetailBinding
import com.supercarlounge.supercar.databinding.FragTextBinding
import com.supercarlounge.supercar.databinding.FragmentLocationMainBinding
import com.supercarlounge.supercar.databinding.FragmentLocationMainItemBinding
import com.supercarlounge.supercar.dialog.PostOrderUpDialog
import com.supercarlounge.supercar.dialog.QuickUserDialog
import com.supercarlounge.supercar.enumset.QuickTypeStatus
import com.supercarlounge.supercar.ui.activity.*
import com.supercarlounge.supercar.ui.fragment.PagerInformationUse.IUDrivePassFragment
import com.supercarlounge.supercar.viewmodel.LocationMainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import java.util.*
import kotlin.collections.ArrayList


class LocationMainItemFragment(var type: Int) : Fragment(), DialogEvent {
    val LogTag = "BoardImageDetailFragment"
    var binding: FragmentLocationMainItemBinding? = null
    var spotadapter: LocationSpotAdapter? = null
    var nAdapter: NotiSlideAdapter? = null
    var application: MainApplication? = null

    var postOrderUpDialog: PostOrderUpDialog? = null
    var quickUserDialog : QuickUserDialog? =null
    var fm: FragmentManager? = null
    private  var viewModel: LocationMainViewModel? = null
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.fragment_location_main_item,
            null,
            false
        );
        viewModel = ViewModelProvider(this).get(LocationMainViewModel::class.java)
        return binding!!.root
    }

    fun newInstance(type: Int): LocationMainItemFragment {
        val fragment = LocationMainItemFragment(type)
        val args = Bundle()
        args.putInt("type",type)

        fragment.arguments = args
        return fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        application = requireContext().applicationContext as MainApplication
        viewModel!!.my_seq.value = application!!.userData?.u_seq.toString()
        binding!!.viewModel = viewModel
        binding!!.setLifecycleOwner(this)
        fm = childFragmentManager
        setadapter()
        setobserver()

        viewModel?.viewType?.value = type
        viewModel?.nowtypeindex?.value = type


        if (application!!.quick_view.equals("퀵유저")) {
            var data1  = NotiData("","","","슈라, 주식회사 엠투넷과 업무협약 체결","","","","","")
            var data2  = NotiData("","","","슈라, 해리슨테일러와 업무협약 체결","","","","","")
            var data3  = NotiData("","","","슈라, 닥터스피부과와 업무협약 체결","","","","","")
            var data4  = NotiData("","","","슈라, 에어차터코리아와 업무협약 체결","","","","","")
            var data5  = NotiData("","","","슈라, 데어베어(코드디)와 업무협약 체결","","","","","")
            var data6  = NotiData("","","","슈라, 에너캠프와 업무협약 체결","","","","","")
            var data7  = NotiData("","","","슈라, 휴먼성형외과와 업무협약 체결","","","","","")
            var data8  = NotiData("","","","슈라, 비비드골프와 업무협약 체결","","","","","")
            var data9  = NotiData("","","","슈라, 바로가(캐리어탁송) 업무협약 체결","","","","","")
            var data10  = NotiData("","","","슈라, 더엘엑스 솔라가드프리미엄과 업무협약","","","","","")
            var list :ArrayList<NotiData> = arrayListOf()
            list.add(data1)
            list.add(data2)
            list.add(data3)
            list.add(data4)
            list.add(data5)
            list.add(data6)
            list.add(data7)
            list.add(data8)
            list.add(data9)
            list.add(data10)
            viewModel?.anonymousForumNoitList?.value = list
        } else {
            viewModel?.getAnonymousForumPostNoti(1, 100)
        }
        var result =""


    }


    @OptIn(ExperimentalCoroutinesApi::class)
    fun setobserver(){

        viewModel?.order?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            viewModel?.resetpage()
            viewModel?.getSpotList(true)
        })
        viewModel?.typelist?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {


        })

        viewModel?.spotlist?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it.size == 0){
                if (viewModel?.searchVaue?.value?.isNotEmpty()== true){
                    viewModel?.nottext?.value = "검색된 장소가 없습니다."
                }else{
                    viewModel?.nottext?.value = "게시글을 불러오는 중입니다."
                }

            }
        })
//        viewModel?.tvregion?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
//            viewModel?.resetpage()
//            viewModel?.getSpotList(true)
//        })

//        viewModel?.anonymousForumNoitList?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
//            if (viewModel?.anonymousForumNoitList?.value?.size != 0) {
//                var timer = Timer()
//                timer.schedule(object : TimerTask() {
//                    override fun run() {
//                        binding?.rvAnonymousForumNoti?.adapter?.itemCount?.let { it1 ->
//                            binding?.rvAnonymousForumNoti?.smoothScrollToPosition(
//                                it1
//                            )
//                        };
//                        timer.cancel()
//                    }
//                }, 1000, 1000)
//
//            }
//        })


//        binding!!.edtSearch.textChanges()
//            .debounce(800)
//            .onEach {
//                viewModel?.resetpage()
//                viewModel?.getSpotList(true ,spotadapter!!)
//            }
//            .launchIn(lifecycleScope)
//        viewModel?.order?.observe(viewLifecycleOwner, androidx.lifecycle.Observer  {
//            if (it != null) {
//
//                if (viewModel?.anonymousForumNoitList?.value?.size != 0) {
//                    var timer = Timer()
//                    timer.schedule(object : TimerTask() {
//                        override fun run() {
//                            binding?.rvAnonymousForumNoti?.adapter?.itemCount?.let { it1 ->
//                                binding?.rvAnonymousForumNoti?.smoothScrollToPosition(
//                                    it1
//                                )
//                            };
//                            timer.cancel()
//                        }
//                    }, 1000, 1000)
//
//                }
//            }
//
//        })
//        var list:ArrayList<LocationSlideData> = arrayListOf()
//        list.add(LocationSlideData("","너를 위해 준비한 발렌타인데이 장소 TOP 5!","https://supercarlounge.s3.ap-northeast-2.amazonaws.com/user/6c21f38c-9dea-44b9-972a-252b115779da/board/boardCover/DriveImage20230604202636.png","",""))
//        list.add(LocationSlideData("","맛좋은 장소","https://supercarlounge.s3.ap-northeast-2.amazonaws.com/user/4a3e430b-ef6c-4b37-af46-947c07062b9e/board/boardCover/DriveImage20230605000153.png","",""))
//        list.add(LocationSlideData("","지역구","https://supercarlounge.s3.ap-northeast-2.amazonaws.com/user/undefined/board/boardCover/IMG_20230605_000308374.jpg.png","",""))
//        list.add(LocationSlideData("","맛좋은 장소","https://supercarlounge.s3.ap-northeast-2.amazonaws.com/user/4a3e430b-ef6c-4b37-af46-947c07062b9e/board/boardCover/DriveImage20230605000153.png","",""))
//        list.add(LocationSlideData("","지역구","https://supercarlounge.s3.ap-northeast-2.amazonaws.com/user/undefined/board/boardCover/IMG_20230605_000308374.jpg.png","",""))
//        viewModel?.pagedatalist?.value = list

    }

//    @ExperimentalCoroutinesApi
//    @CheckResult
//    fun EditText.textChanges(): Flow<CharSequence?> {
//        return callbackFlow<CharSequence?> {
//            val listener = object : TextWatcher {
//                override fun afterTextChanged(s: Editable?) = Unit
//
//                override fun beforeTextChanged(
//                    s: CharSequence?,
//                    start: Int,
//                    count: Int,
//                    after: Int
//                ) = Unit
//
//                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                    viewModel?.searchVaue?.value = s.toString()
//                    trySend(s)
//                }
//            }
//            binding?.edtSearch?.addTextChangedListener(listener)
//            awaitClose { binding?.edtSearch?.removeTextChangedListener(listener) }
//        }.onStart { emit(text) }
//    }

    fun changeList(search:String ,type :String,region:String,payTag:String,min:Int,max:Int,typelist:ArrayList<String>){
        viewModel?.tvregion?.value = region
        viewModel?.searchVaue?.value =search
        viewModel?.order?.value=type
        viewModel?.payTag?.value = payTag
        viewModel?.priceMin?.value = min
        viewModel?.priceMax?.value = max
        viewModel?.typelist?.value = typelist
        Log.d("테스트33", viewModel?.typelist?.value.toString())
        val mHandler = Handler(Looper.getMainLooper())
        mHandler.postDelayed({
            viewModel?.resetpage()
            viewModel?.getSpotList(true)
        },300)

    }

    fun setadapter() {
        nAdapter = NotiSlideAdapter(
            requireContext(),
            arrayListOf()
        ) { data, position ->
            Log.d("ㅎㅇ", "setAdapters: ")
            if (application!!.quick_view.equals("퀵유저")) {
                ShowQuickUserDialog()
            } else {

                val i = Intent(requireContext(), PostNoticeActivity::class.java)
                i.putExtra("n_seq", data.n_seq)
                i.putExtra("u_seq", data.u_seq)
                i.putExtra("n_sday", data.n_sday)
                i.putExtra("n_title", data.n_title)
                i.putExtra("n_eday", data.n_eday)
                i.putExtra("n_type", data.n_type)
                i.putExtra("n_date", data.n_date)
                i.putExtra("n_click", data.n_click)
                application!!.htmlText = data.n_text!!
                startActivity(i)
                requireActivity().overridePendingTransition(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
                )
            }

        }

        // 4F, 2F , 8F 등으로 속도 조절이 가능하다.


        var linearLayoutManager = ScrollingLinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false, 30000
        )



//
//        binding?.rvAnonymousForumNoti?.adapter = nAdapter
//        binding?.rvAnonymousForumNoti?.layoutManager = linearLayoutManager
//
//        binding?.rvAnonymousForumNoti?.addOnScrollListener(object :
//            RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                val lastVisibleItemPosition =
//                    (recyclerView.layoutManager as ScrollingLinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
//                val itemTotalCount = recyclerView.adapter!!.itemCount - 1
//                var posi = recyclerView.scrollY
//
//                if (!binding?.rvAnonymousForumNoti!!.canScrollVertically(1)) {
//                    Log.d("SCROLL", "last Position...");
//
//                    (binding?.rvAnonymousForumNoti!!.layoutManager as LinearLayoutManager).scrollToPosition(
//                        0
//                    )
//
//                    binding?.rvAnonymousForumNoti?.smoothScrollToPosition(binding?.rvAnonymousForumNoti?.adapter?.itemCount!!);
//
//                }
//
//
////                Log.d("리사이클러뷰", "$lastVisibleItemPosition : $itemTotalCount ")
//
//                if (lastVisibleItemPosition == itemTotalCount) {
//
//
//                }
//            }
//
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                // Log.d("로그", newState.toString())
//                if (newState == 0) {
//                    var timer = Timer()
//                    timer.schedule(object : TimerTask() {
//                        override fun run() {
//                            binding?.rvAnonymousForumNoti?.smoothScrollToPosition(binding?.rvAnonymousForumNoti?.adapter?.itemCount!!);
//                            timer.cancel()
//                        }
//                    }, 1000, 1000)
//                }
//
//            }
//        })

        spotadapter = LocationSpotAdapter(requireContext(), arrayListOf()!!) { data, type,index ->
            when(type){
                "메인"->{
                    var i = Intent(requireContext(), LocationSpotDetailActivity::class.java)
                    i.putExtra("spot_seq", data.spot_seq)
                    startActivity(i)
                    requireActivity().overridePendingTransition(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                    )
                }
                "북마크"->{
                    if (application!!.quick_view.equals("퀵유저")) {
                        ShowQuickUserDialog()
                    } else {
                        var bookmarkCheck = data.bookmarkCheck
                        var realData = data
                        if (bookmarkCheck == "N") {
                            realData.bookmarkCheck = "Y"
                        } else {
                            realData.bookmarkCheck = "N"
                        }


                        spotadapter?.setIndexData(index, realData)
                        viewModel?.InsertBookmark(data)
                    }
                }
                else -> {}
            }

            }
        binding!!.rvLocationMain.adapter = spotadapter
        binding!!.rvLocationMain.layoutManager = LinearLayoutManager(requireContext())
        binding!!.rvLocationMain.addOnScrollListener(listener)
        viewModel?.getSpotList(false!!)
    }
    var listener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            var lastposition =
                (binding?.rvLocationMain?.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            var itemcount = spotadapter!!.itemCount
            Log.d("getDrive : last", "" + itemcount)
            Log.d("getDrive : itemcount", "" + lastposition)
            if (itemcount == (lastposition + 1)) {
                var totalc = viewModel?.totalCount?.value!!
                if (itemcount < totalc && totalc != 0) {
                    if (itemcount < viewModel?.totalCount?.value!!) {
                        viewModel?.page?.value = viewModel?.page?.value?.plus(1)
                        viewModel!!.getSpotList(false)
                        Log.d("getAnonymousForumPost", "getAnonymousForumPostScroll")

                    }
                }
            }

        }
    }
    fun setclicks() {

//        binding!!.ivSearchOff.setOnClickListener {
//            if (application!!.quick_view.equals("퀵유저")) {
//                // ShowQuickUserDialog()
//            } else {
//                viewModel!!.search_check.value = false
//            }
//
//        }
//
//
//        binding!!.ivSearchOn.setOnClickListener {
//            if (application!!.quick_view.equals("퀵유저")) {
//                //ShowQuickUserDialog()
//            } else {
//                viewModel!!.search_check.value = true
//            }
//
//        }
//
//        binding!!.ivOrder.setOnClickListener {
//            if (application!!.quick_view.equals("퀵유저")) {
//                //    ShowQuickUserDialog()
//            } else {
//                val originalPos = IntArray(2)
//                binding!!.ivOrder.getLocationInWindow(originalPos)
//                val x = originalPos[0]
//                val y = originalPos[1]
//                Log.d("좌표", "$y + $x")
//                postOrderUpDialog = PostOrderUpDialog(x, y, requireContext(), this,)
//                postOrderUpDialog!!.show(fm!!, "")
//            }
//        }

    }
    private fun ShowQuickUserDialog() {
        quickUserDialog =
            QuickUserDialog(requireContext(), QuickTypeStatus.QUICK_WARNING) { type, okcancel ->
                if (okcancel) {
                    quickUserDialog!!.dismiss()
                    val i = Intent(requireContext(), JoinActivity::class.java)
                    i.putExtra("qickview", "퀵유저")
                    i.putExtra("u_name", application?.userBasicData?.u_name)
                    i.putExtra("u_gender", application?.userBasicData?.u_gender)
                    i.putExtra("u_birthday", application?.userBasicData?.u_birthday)
                    i.putExtra("u_phone", application?.userBasicData?.u_phone)
                    Log.d("아이템3", application?.userBasicData.toString())
                    startActivity(i)
                } else {
                    quickUserDialog!!.dismiss()
                }
            }
        quickUserDialog!!.show(fm!!, "")
    }

    override fun okEvent(
        type: Int,
        okAndCancel: Boolean,
        comment_value: String,
        seq: String,
        u_nickname: String
    ) {
        if (type == Constans.ORDER_MORE_REC) {
            viewModel?.order?.value = "추천순"
        } else if (type == Constans.ORDER_MORE_HOT) {
            viewModel?.order?.value = "인기순"
        } else if (type == Constans.ORDER_MORE_NEWSET) {
            viewModel?.order?.value = "최신순"
        }
    }
}