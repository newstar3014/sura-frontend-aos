package com.supercarlounge.supercar.ui.fragment

import ViewProfileDialog
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.CheckResult
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.supercarlounge.supercar.DialogEvent
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.SpannedGridLayoutManager
import com.supercarlounge.supercar.adapter.*
import com.supercarlounge.supercar.data.*
import com.supercarlounge.supercar.databinding.FragmentMyCarBoardBinding
import com.supercarlounge.supercar.dialog.DrivePassPayDialog
import com.supercarlounge.supercar.dialog.GuideDialog
import com.supercarlounge.supercar.dialog.ProfileLikeFriendDialog
import com.supercarlounge.supercar.dialog.QuickUserDialog
import com.supercarlounge.supercar.enumset.*
import com.supercarlounge.supercar.ui.activity.*
import com.supercarlounge.supercar.viewmodel.MyCarBoardViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import java.util.*


class MyCarBoardFragment() : Fragment(), DialogEvent {

    private var binding: FragmentMyCarBoardBinding? = null
    var myCarBoardAdapter: MyCarBoardAdapter? = null
    private var drivepop1 : ProfileLikeFriendDialog? =null
    private var viewProfileDialog :ViewProfileDialog? = null
    var viewModel: MyCarBoardViewModel? = null
    lateinit var application: MainApplication
    private var quickUserDialog: QuickUserDialog? = null
    var onResumeCheck = false
    var fm: FragmentManager? = null

    var resultLauncher : ActivityResultLauncher<Intent>?? =null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(MyCarBoardViewModel::class.java)
        application = requireContext()!!.applicationContext as MainApplication
        binding = FragmentMyCarBoardBinding.inflate(inflater, container, false)
        binding!!.viewModel = viewModel
        binding!!.setLifecycleOwner(this)
        viewModel!!.text.value = "MY CAR"
        viewModel!!.my_user_data.value = application.userData

        val root: View = binding!!.root
        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fm = childFragmentManager
        setAdapters(1)
        setOnclickListeners()
        observes()
        setNowDay()
        viewModel?.getMyCarBestBoardList()
        binding!!.rvMyCaBoard.viewTreeObserver
            .addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {

                    viewModel?.rvWidth?.value  = binding?.rvMyCaBoard?.width
                    binding!!.rvMyCaBoard.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
        viewModel?.userdata?.value = application.userData

         resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    // There are no request codes
                    val data: Intent? = result.data

                    var mData  = data?.getParcelableExtra<MyCarBoardData>("data")
                    var type  = data?.getStringExtra("type")
                    var position  = data?.getIntExtra("position",0)
                    when (type){
                        "삭제"->{
                            viewModel?.getMyCarBoardList(true)
                        }
                        "북마크","좋아요"->{
                            var like  = data?.getStringExtra("like")
                            var bookmark  = data?.getStringExtra("bookmark")
                            var  likeCount = data?.getIntExtra("likecount",0)
                            if (bookmark?.isNotEmpty() == true){
                                viewModel?.myCarBoardLists?.value?.get(position!!)?.bookMarkCheck = bookmark
                            }
                            if (like?.isNotEmpty() == true){
                                viewModel?.myCarBoardLists?.value?.get(position!!)?.likeCheck = like
                                viewModel?.myCarBoardLists?.value?.get(position!!)?.likeCnt = likeCount!!
                            }


                            var data = viewModel?.myCarBoardLists?.value?.get(position!!)
                            Log.d("resultLauncher.bookMarkCheck", data?.bookMarkCheck.toString())
                            if (data!= null){
                                myCarBoardAdapter?.setChangeData(data,position!!)
                            }


                        }
                        "글쓰기"->{
                            viewModel?.getMyCarBoardList(true)
                        }
                        "글수정"->{
                            if (mData!= null){
                                myCarBoardAdapter?.setChangeData(mData!!,position!!)
                                if (viewModel?.myCarBestBoardLists?.value!!.size > position!!){
                                    viewModel?.myCarBestBoardLists?.value!![position!!] = mData
                                    viewModel?.myCarBestBoardLists?.value = viewModel?.myCarBestBoardLists!!.value

                                }

                            }
                        }
                        else -> {}


                    }
//                anonymousForumPagerAdapter?.changeviewtype(1, "", "", "")


                }
            }
    }

    private fun observes() {
        viewModel!!.myCarBestBoardLists.observe(viewLifecycleOwner, Observer {

            if (it != null) {


                        if (it.size >0){
                            if (it[0].getTitle().isNotEmpty()){
                                viewModel!!.tv1.value = "1. ${it[0].getTitle()}"
                            }else{
                                viewModel!!.tv1.value = "인기글로 선정 된 게시글이 없습니다."
                            }
                            if (it.size >1){
                                if (it[1].getTitle().isNotEmpty()){
                                    viewModel!!.tv2.value = "2. ${it[1].getTitle()}"
                                }else{
                                    viewModel!!.tv2.value = "인기글로 선정 된 게시글이 없습니다."
                                }
                                if (it.size >2){
                                    if (it[2].getTitle().isNotEmpty()){
                                        viewModel!!.tv3.value = "3. ${it[2].getTitle()}"
                                    }else{
                                        viewModel!!.tv3.value = "인기글로 선정 된 게시글이 없습니다."
                                    }
                                }else{
                                    viewModel!!.tv3.value = "인기글로 선정 된 게시글이 없습니다."
                                }
                            }else{
                                viewModel!!.tv2.value = "인기글로 선정 된 게시글이 없습니다."
                                viewModel!!.tv3.value = "인기글로 선정 된 게시글이 없습니다."
                            }

                        }else{
                            viewModel!!.tv1.value = "인기글로 선정 된 게시글이 없습니다."
                            viewModel!!.tv2.value = "인기글로 선정 된 게시글이 없습니다."
                            viewModel!!.tv3.value = "인기글로 선정 된 게시글이 없습니다."
                        }



            }

        })
        viewModel!!.gridOn.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it){
                    setAdapters(0)
                }else{
                    setAdapters(1)
                }
            }
        })
        viewModel!!.intent.observe(viewLifecycleOwner, Observer {
                var i =Intent(requireContext(),MyCarBoardActivity::class.java)
                var data = viewModel!!.myCarBestBoardLists.value?.get(it)
                i.putExtra("mmi_seq",data!!.mmi_seq)
                i.putExtra("position",it)
                i.putExtra("type","기본")
                resultLauncher?.launch(i)
                requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)


        })
        viewModel!!.search.observe(viewLifecycleOwner, Observer {
            viewModel!!.contextText()
        })
        viewModel!!.bookmark.observe(viewLifecycleOwner, Observer {
            viewModel!!.contextText()
        })
        viewModel!!.toast.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                if (it.equals("하트가 부족합니다.")) {
                    var drivePassPayDialog = DrivePassPayDialog(
                        requireContext(),
                        DrivePassDialogStatus.NOTHEART,
                        "",
                        0
                    ) { type, isok ->
                        if (isok) {
                            val i = Intent(requireContext(), HeartsShopActivity::class.java)
                            startActivity(i)
                            requireActivity().overridePendingTransition(com.supercarlounge.supercar.R.anim.fadein, com.supercarlounge.supercar.R.anim.fadeout)
                        }

                    }
                    drivePassPayDialog!!.show(fm!!, "")
                } else {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }

            }
        })
        viewModel!!.boardCheckViewStatus.observe(viewLifecycleOwner, Observer {
            Log.d("상태", it.toString())
            when(it){
                BoardCheckViewStatus.PROFILE_WAIT_DIALOG ->{

                    drivepop1 =
                        ProfileLikeFriendDialog(
                            requireContext(),
                            "",
                            PopTypeStatus.DRIVE_FRIEND
                        ) { type, onoff, text ->
                            if (onoff) {
                                if (text.isNotEmpty()) {
                                    viewModel!!.ProfileRead(120, text)
                                }else{
                                    viewModel!!.toast.value = "메시지를 작성하고 프로필 승인을 요청해주세요."
                                }

                            }
                            drivepop1!!.dismiss()
                        }
                    drivepop1!!.show(fm!!, "")

                }
                BoardCheckViewStatus.COMPANY_PROFILE->{
                    val i = Intent(requireContext(), ProfileCompanyActivity::class.java)
                    i.putExtra("sub_seq", viewModel!!.select_sub_seq.value.toString())
                    startActivity(i)
                }
                BoardCheckViewStatus.NOT_FRIEND_POPUP ->{

                    viewProfileDialog = ViewProfileDialog(requireContext()) { isfinish ->
                        if (isfinish){
                            viewModel!!.ProfileRead(3,"")
                        }
                    }
                    viewProfileDialog!!.show(fm!!, "")
                }
                BoardCheckViewStatus.FRIEND_POPUP ->{
                    drivepop1 =
                        ProfileLikeFriendDialog(
                            requireContext(),
                            "",
                            PopTypeStatus.DRIVE_FRIEND
                        ) { type, onoff, text ->
                            if (onoff) {
                                if (text.isNotEmpty()) {
                                    viewModel!!.ProfileRead(120, text)
                                }else{
                                    viewModel!!.toast.value = "메시지를 작성하고 프로필 승인을 요청해주세요."
                                }
                            }
                            drivepop1!!.dismiss()
                        }
                    drivepop1!!.show(fm!!, "")
                }
                BoardCheckViewStatus.PROFILE_SET ->{
                    drivepop1 =
                        ProfileLikeFriendDialog(
                            requireContext(),
                            "",
                            PopTypeStatus.DRIVE_FRIEND
                        ) { type, onoff, text ->
                            if (onoff) {
                                if (text.isNotEmpty()) {
                                    viewModel!!.ProfileRead(120, text)
                                }else{
                                    viewModel!!.toast.value = "메시지를 작성하고 프로필 승인을 요청해주세요."
                                }
                            }
                            drivepop1!!.dismiss()
                        }
                    drivepop1!!.show(fm!!, "")
                }
                else -> {}
            }
        })
    }

    override fun onResume() {
        super.onResume()

        Log.d("테스트", application!!.checkguide4)
        if (!onResumeCheck) {
            Log.d("날짜", "30일 됨: ")
            if (application!!.Check30days(application!!.checkguide6)) {

            } else {

                var d = GuideDialog(requireContext(), GuidePopType.GUIDE_MY_CAR_BOARD) {
                    application!!.save_guide(GuidePopType.GUIDE_MY_CAR_BOARD)
                }

                d.show(childFragmentManager, "")
            }
            onResumeCheck = true
        }

    }

    override fun onStart() {
        super.onStart()

    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun setNowDay(){
        val nowDate: LocalDate = LocalDate.now()
        var year = 0
        var month = 0
        viewModel?.selectDate?.value =  "${nowDate.year}-${nowDate.monthValue}-01"
    }

    private fun setAdapters(type: Int) {

        myCarBoardAdapter = MyCarBoardAdapter(requireContext(), arrayListOf(), type,viewModel!!) { data,tag, position ->



            if (application.quick_view.equals("퀵유저")){
                ShowQuickUserDialog()
            }else {
                if (tag == "기본"){
                    var i =Intent(requireContext(),MyCarBoardActivity::class.java)
                    i.putExtra("mmi_seq",data!!.mmi_seq)
                    i.putExtra("position",position)
                    resultLauncher?.launch(i)
                    requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }else if (tag == "드라이브 신청"){
                    if (application.CheckCompany() == false) {
                        viewModel!!.select_sub_seq.value = data.u_seq
                        viewModel!!.select_mmi_seq.value = data.mmi_seq
                        viewModel!!.select_rn_nick.value = data.mrc_nickname
                        viewModel!!.select_rn_nick.value = data.mrc_nickname
                        viewModel!!.boardViewCheckClick()
                    }else{
                        viewModel!!.toast.value = requireContext().getString(R.string.company_profile_open_text)
                    }
                }

            }

        }


        binding?.rvMyCaBoard?.adapter = myCarBoardAdapter


        if (type == 1){
            binding?.rvMyCaBoard?.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        }else{
            binding?.rvMyCaBoard?.layoutManager = SpannedGridLayoutManager(object :
                SpannedGridLayoutManager.GridSpanLookup {
                    override fun getSpanInfo(position: Int): SpannedGridLayoutManager.SpanInfo {
                        return if (position == 0) {
                            SpannedGridLayoutManager.SpanInfo(2, 2)
                        } else {
                            SpannedGridLayoutManager.SpanInfo(1, 1)
                        }
                    }
                },
                3 /* Three columns */,
                1f /* We want our items to be 1:1 ratio */
            )
        }
        binding!!.rvMyCaBoard.addOnScrollListener(listener)

        viewModel?.getMyCarBoardList(true)

    }
    var listener: RecyclerView.OnScrollListener = object: RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            var lastposition  = 0
            if (viewModel?.gridOn?.value == true){

                var staggeredGridLayoutManager = binding?.rvMyCaBoard?.layoutManager as SpannedGridLayoutManager

                lastposition = staggeredGridLayoutManager.lastVisiblePosition
            }else{
                 lastposition=(binding?.rvMyCaBoard?.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

            }

            var itemcount=myCarBoardAdapter!!.itemCount
            if(itemcount== (lastposition +1)) {
                var totalc = viewModel!!.totalCount.value!!
                if (itemcount < totalc&& totalc !=0) {
                        if(viewModel!!.issend.value == false) {
                            viewModel!!.getMyCarBoardList(false)
                        }
                }
            }
        }
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
        }
    }
    private fun findMax(lastPositions: IntArray): Int {
        return lastPositions[0].coerceAtLeast(lastPositions[1])
    }
    var Glistener: RecyclerView.OnScrollListener = object: RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            var staggeredGridLayoutManager = binding?.rvMyCaBoard?.layoutManager as StaggeredGridLayoutManager
            var lastPositions = IntArray(staggeredGridLayoutManager.spanCount)
            lastPositions = staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(lastPositions)
            var lastposition = Math.max(lastPositions.get(0), lastPositions.get(1)) //findMax(lastPositions);
            var itemcount=myCarBoardAdapter!!.itemCount
            Log.d("getDrive : last" , ""+itemcount)
            Log.d("getDrive : itemcount" , ""+lastposition)
            if(itemcount== (lastposition +1)) {
                var totalc = viewModel!!.totalCount.value!!
                if (itemcount < totalc&& totalc !=0) {
                    if(viewModel!!.issend.value == false) {


                        viewModel!!.getMyCarBoardList(false)
                        Log.d("getAnonymousForumPost", "getAnonymousForumPostScroll")
                    }
                }
            }
        }
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
        }
    }

    private fun setOnclickListeners() {
        binding!!.ivWriting.setOnClickListener {

            if (application.quick_view.equals("퀵유저")) {
                ShowQuickUserDialog()
            } else {

                val i = Intent(requireActivity(), MyCarBoardWritingActivity::class.java)
                resultLauncher?.launch(i)
                requireActivity().overridePendingTransition(com.supercarlounge.supercar.R.anim.fadein, com.supercarlounge.supercar.R.anim.fadeout)


            }

        }

        binding!!.tvHot.setOnClickListener {
            val i = Intent(requireActivity(), MyCarHotBoardActivity::class.java)
            resultLauncher?.launch(i)
            requireActivity().overridePendingTransition(com.supercarlounge.supercar.R.anim.fadein, com.supercarlounge.supercar.R.anim.fadeout)
        }
        binding!!.llBest.setOnClickListener {
            val i = Intent(requireActivity(), MyCarBestBoardActivity::class.java)
            resultLauncher?.launch(i)
            requireActivity().overridePendingTransition(com.supercarlounge.supercar.R.anim.fadein, com.supercarlounge.supercar.R.anim.fadeout)
        }



        binding!!.ivSearch.setOnClickListener {
            if (application.quick_view.equals("퀵유저")) {
                ShowQuickUserDialog()
            } else {
                viewModel?.getMyCarBoardList(true)
            }

        }

        binding!!.edtSearch.textChanges()
            .debounce(800)
            .onEach {

                viewModel?.getMyCarBoardList(true)
            }
            .launchIn(lifecycleScope)

        binding!!.ivBookmark.setOnClickListener {
            viewModel!!.bookmark()
        }
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





    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    fun newInstance(): MyCarBoardFragment {
        val args = Bundle()

        val fragment = MyCarBoardFragment()
        fragment.arguments = args

        return fragment
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

    override fun okEvent(
        type: Int,
        okAndCancel: Boolean,
        comment_value: String,
        seq: String,
        u_nickname: String
    ) {


        if (okAndCancel) {
            when (type) {
            }
        }
    }


}