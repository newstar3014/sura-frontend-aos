//package com.supercarlounge.supercar.ui.fragment
//
//import android.R
//import android.content.Intent
//import android.os.Bundle
//import android.text.Editable
//import android.text.TextWatcher
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.EditText
//import androidx.annotation.CheckResult
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentManager
//import androidx.lifecycle.Observer
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.lifecycleScope
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.supercarlounge.supercar.Constans
//import com.supercarlounge.supercar.DialogEvent
//import com.supercarlounge.supercar.MainApplication
//import com.supercarlounge.supercar.ScrollingLinearLayoutManager
//import com.supercarlounge.supercar.adapter.LocationSpotAdapter
//import com.supercarlounge.supercar.adapter.NotiSlideAdapter
//import com.supercarlounge.supercar.data.LocationSlideData
//import com.supercarlounge.supercar.databinding.FragmentLocationMainBinding
//import com.supercarlounge.supercar.dialog.PostOrderUpDialog
//import com.supercarlounge.supercar.dialog.PostPopUpDialog
//import com.supercarlounge.supercar.ui.activity.LocationSpotDetailActivity
//import com.supercarlounge.supercar.ui.activity.PostNoticeActivity
//import com.supercarlounge.supercar.viewmodel.LocationMainViewModel
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.channels.awaitClose
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.callbackFlow
//import kotlinx.coroutines.flow.debounce
//import kotlinx.coroutines.flow.launchIn
//import kotlinx.coroutines.flow.onEach
//import kotlinx.coroutines.flow.onStart
//import java.util.Timer
//import java.util.TimerTask
//
//class LocationSpotFragment:Fragment(), DialogEvent {
//    val TAG = "LocationMainFragment"
//    private var _binding: FragmentLocationMainBinding? = null
//    private val binding get() = _binding
//    private lateinit var viewModel: LocationMainViewModel
//    var fm: FragmentManager? =  null
//    var application: MainApplication? = null
//    var spotadapter: LocationSpotAdapter? = null
//    var nAdapter: NotiSlideAdapter? = null
//    var postOrderUpDialog:PostOrderUpDialog? = null
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        viewModel = ViewModelProvider(this).get(LocationMainViewModel::class.java)
//        application = requireContext()!!.applicationContext as MainApplication
//        viewModel!!.my_seq.value = application!!.userData?.u_seq.toString()
//        _binding = FragmentLocationMainBinding.inflate(inflater, container, false)
//        binding!!.viewModel = viewModel
//        binding!!.setLifecycleOwner(this)
//
//
//        val root: View = binding!!.root
//        return root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        fm = childFragmentManager
//        setadapter()
//        setobserver()
//        setonclick()
//        viewModel.getAnonymousForumPostNoti(1,100)
//
//
//    }
//
//    override fun onResume() {
//        super.onResume()
//        viewModel.getSpotList(true)
//    }
//    fun setadapter(){
//        nAdapter = NotiSlideAdapter(
//            requireContext(),
//            arrayListOf()
//        ) { data, position ->
//            Log.d("ㅎㅇ", "setAdapters: ")
//            if (application!!.quick_view.equals("퀵유저")){
//
//            }else {
//
//                val i = Intent(requireActivity(), PostNoticeActivity::class.java)
//                i.putExtra("n_seq", data.n_seq)
//                i.putExtra("u_seq", data.u_seq)
//                i.putExtra("n_sday", data.n_sday)
//                i.putExtra("n_title", data.n_title)
//                i.putExtra("n_eday", data.n_eday)
//                i.putExtra("n_type", data.n_type)
//                i.putExtra("n_date", data.n_date)
//                i.putExtra("n_click",data.n_click)
//                application!!.htmlText = data.n_text!!
//                startActivity(i)
//                requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
//            }
//
//        }
//
//        // 4F, 2F , 8F 등으로 속도 조절이 가능하다.
//
//
//        var linearLayoutManager = ScrollingLinearLayoutManager(
//            requireContext(), LinearLayoutManager.VERTICAL, false, 30000
//        )
//        binding?.rvAnonymousForumNoti?.adapter = nAdapter
//        binding?.rvAnonymousForumNoti?.layoutManager = linearLayoutManager
//        spotadapter = LocationSpotAdapter(requireContext(),viewModel.spotlist.value!!){ data, index->
//            var i  =Intent(requireContext(),LocationSpotDetailActivity::class.java )
//            i.putExtra("spot_seq",data.spot_seq)
//            startActivity(i)
//        }
//        binding!!.rvLocationMain.adapter =  spotadapter
//        binding!!.rvLocationMain.layoutManager = LinearLayoutManager(requireContext())
//    }
//    fun setonclick(){
//
//        binding!!.ivSearchOff.setOnClickListener {
//            if (application!!.quick_view.equals("퀵유저")){
//               // ShowQuickUserDialog()
//            }else {
//                viewModel!!.search_check.value = false
//            }
//
//        }
//
//
//        binding!!.ivSearchOn.setOnClickListener {
//            if (application!!.quick_view.equals("퀵유저")){
//                //ShowQuickUserDialog()
//            }else {
//                viewModel!!.search_check.value = true
//            }
//
//        }
//
//        binding!!.ivOrder.setOnClickListener {
//            if (application!!.quick_view.equals("퀵유저")){
//            //    ShowQuickUserDialog()
//            }else {
//                val originalPos = IntArray(2)
//                binding!!.ivOrder.getLocationInWindow(originalPos)
//                val x = originalPos[0]
//                val y = originalPos[1]
//                Log.d("좌표", "$y + $x")
//                postOrderUpDialog =
//                    PostOrderUpDialog(
//                        x,
//                        y,
//                        requireContext(),
//                        this,
//
//                    )
//                postOrderUpDialog!!.show(fm!!, "")
//            }
//        }
//
//    }
//    fun setobserver(){
//        viewModel.order.observe(viewLifecycleOwner, Observer {
//            viewModel.resetpage()
//            viewModel.getSpotList(true)
//        })
//        binding!!.edtSearch.textChanges()
//            .debounce(800)
//            .onEach {
//                viewModel.resetpage()
//               viewModel.getSpotList(true)
//            }
//            .launchIn(lifecycleScope)
//        viewModel!!.anonymousForumNoitList.observe(viewLifecycleOwner, Observer
//        {
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
//        viewModel.pagedatalist.value = list
//    }
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
//
//    override fun okEvent(
//        type: Int,
//        okAndCancel: Boolean,
//        comment_value: String,
//        seq: String,
//        u_nickname: String
//    ) {
//        if (type == Constans.ORDER_MORE_REC){
//            viewModel.order.value = "추천순"
//        }else if (type == Constans.ORDER_MORE_HOT){
//            viewModel.order.value = "인기순"
//        }else if (type == Constans.ORDER_MORE_NEWSET){
//            viewModel.order.value = "최신순"
//        }
//    }
//}