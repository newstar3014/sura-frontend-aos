package com.supercarlounge.supercar.ui.activity


import ViewProfileDialog
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.dhaval2404.imagepicker.ImagePicker
import com.supercarlounge.supercar.*
import com.supercarlounge.supercar.adapter.CommentAdapter
import com.supercarlounge.supercar.adapter.MySubInfoListAdapter
import com.supercarlounge.supercar.adapter.ViewListAdapter
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.data.*
import com.supercarlounge.supercar.databinding.*
import com.supercarlounge.supercar.dialog.*
import com.supercarlounge.supercar.enumset.*
import com.supercarlounge.supercar.viewmodel.*
import java.text.ParseException
import java.text.SimpleDateFormat

 class MyCarBoardActivity : BaseActivitiy(), DialogEvent, OnKeyboardVisibilityListener {

    private lateinit var binding: ActivityMyCarBoardBinding
    private lateinit var viewModel: MyCarBoardPostViewModel
    private var cAdapter: MyCarBoardCommentAdapter? = null
    private var viewListAdapter: ViewListAdapter? = null
    private lateinit var application: MainApplication

    private var postPopUpDialog: PostPopUpDialog? = null
    private var likeListDialog: LikeListDialog? = null
    private var oneButtonDialog: OneButtonSetDialog? = null
    private var twoButtonDialog: TwoButtonDialog? = null
    private var blockProfileDialog: PoliceDialog? = null
    private var blockPostDialog: BlockDialog? = null
     private var viewProfileDialog :ViewProfileDialog? = null
     private var drivepop1 : ProfileLikeFriendDialog? =null
    private var fm: FragmentManager? = null

    var adapter: MySubInfoListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            application = applicationContext as MainApplication
        viewModel = ViewModelProvider(this).get(MyCarBoardPostViewModel::class.java)
        binding = ActivityMyCarBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel
        var mmi_seq = intent.getIntExtra("mmi_seq",0)

        viewModel.myCarBoardseq.value = mmi_seq
        viewModel.my_user_data.value = application.userData
        var position = intent.getIntExtra("position", 0)
        viewModel.myCarBoardDataPosition.value = position
        Log.d("b_seq푸시3", mmi_seq.toString())



        fm = supportFragmentManager


        clickListeners()
        observes()
        setAdapters()

        setKeyboardVisibilityListener(this)
    }


    override fun onResume() {
        super.onResume()
        viewModel.getOneMycarData()
    }


    private fun viewRedefinition() {
        val layoutParams = binding.ivImage.layoutParams
        layoutParams.width = binding.ivImage.width
        layoutParams.height = binding.ivImage.width
        binding.ivImage.layoutParams = layoutParams
    }

    private fun setAdapters() {


        adapter = MySubInfoListAdapter(this, arrayListOf(), viewModel) { data, String, position ->

        }

        binding!!.rvSub.adapter = adapter
        binding!!.rvSub.layoutManager = LinearLayoutManager(this)
        var list: ArrayList<MyCarBoardCommentData> = arrayListOf()
        cAdapter = MyCarBoardCommentAdapter(
            this,
            this,
            list,
            viewModel!!,
            supportFragmentManager
        ) { data, position, view, type ->
            viewModel!!.commentPosition.value = position
            viewModel!!.cm_seq.value = data.mmc_seq.toString()
            viewModel!!.cs_seq.value = "0"
            viewModel.select_seq.value = data.u_seq.toString()
            viewModel.select_anonymousNickname.value = data.mrc_nickname.toString()

            if (type == Constans.NICKNAME) {

                viewModel!!.us_seq.value = data.u_seq.toString()
                val originalPos = IntArray(2)
                view?.getLocationInWindow(originalPos)
                val x = originalPos[0]
                val y = originalPos[1]

                var viewtype = 2
                if (viewModel.my_user_data.value?.u_seq == data.u_seq || application.CheckManager()) {
                    viewtype = Constans.MY_SEQ
                    Log.d("체크", "true: ")
                } else {
                    viewtype = Constans.YOU_SEQ
                    Log.d("체크", "false: ")
                }

                postPopUpDialog = PostPopUpDialog(
                    x,
                    y,
                    this,
                    this,
                    viewtype,
                    Constans.COMMENT_MORE,
                    data.mmc_seq.toString(),
                    data.mrc_nickname.toString(),
                    data.mmc_values.toString(),
                )
                postPopUpDialog!!.show(this.supportFragmentManager, "")


            } else if (type == Constans.HEART) {
                if(application!!.CheckCompany() == false) {
                    if (viewModel.my_user_data.value?.u_seq.toString() != data.u_seq.toString()) {
                        viewModel.boardViewCheckClick(viewModel.select_seq.value.toString())
                    }
                }else{
                    viewModel.toast.value = getString(R.string.company_profile_open_text)
                }

            } else {
                viewModel.commentInputType.value =0
                viewModel.tagNickname.value = "@" + data.mrc_nickname
            }


        }


        binding.rvComment.adapter = cAdapter
        binding.rvComment.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvComment?.itemAnimator = null    //리사이클러뷰 애니메이터 get


    }

    var listener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            var lastposition =
                (binding?.rvComment?.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            var itemcount = cAdapter!!.itemCount
            Log.d("getDrive : last", "" + itemcount)
            Log.d("getDrive : itemcount", "" + lastposition)
            if (itemcount == (lastposition + 1)) {
//                var totalc = viewModel!!.totalCount.value!!
//                if (itemcount < totalc&& totalc !=0) {
//                    if (itemcount < viewModel!!.totalCount.value!!) {
//                        viewModel.page.value =  viewModel.page.value?.plus(1)
//                            viewModel!!.getAnonymousForumPostScroll(viewModel!!.filterArray[viewModel!!.viewType.value!!])
//                            Log.d("getAnonymousForumPost", "getAnonymousForumPostScroll")
//
//                    }
//                }
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
        }
    }

    private fun observes() {

        viewModel.viewlistCheck.observe(this, Observer {
            if (it) {
//                showViewList()

            }
        })
        viewModel.boardCheckViewStatus.observe(this, Observer {
            Log.d("상태", it.toString())
            when(it){
                BoardCheckViewStatus.PROFILE_WAIT_DIALOG ->{

                    drivepop1 =
                        ProfileLikeFriendDialog(
                            this,
                            "",
                            PopTypeStatus.DRIVE_FRIEND
                        ) { type, onoff, text ->
                            if (onoff) {
                                if (text.isNotEmpty()) {
                                    viewModel.ProfileRead(120, text)
                                }else{
                                    viewModel.toast.value = "메시지를 작성하고 프로필 승인을 요청해주세요."
                                }

                            }
                            drivepop1!!.dismiss()
                        }
                    drivepop1!!.show(fm!!, "")

                }
                BoardCheckViewStatus.NOT_FRIEND_POPUP ->{

                         viewProfileDialog = ViewProfileDialog(this) { isfinish ->
                            if (isfinish){
                                viewModel.ProfileRead(3,"")
                            }
                         }
                         viewProfileDialog!!.show(fm!!, "")
                }
                BoardCheckViewStatus.COMPANY_PROFILE->{
                    val i = Intent(this, ProfileCompanyActivity::class.java)
                    i.putExtra("sub_seq", viewModel.select_seq.value)
                    startActivity(i)
                }
                BoardCheckViewStatus.FRIEND_POPUP ->{
                    drivepop1 =
                        ProfileLikeFriendDialog(
                            this,
                            "",
                            PopTypeStatus.DRIVE_FRIEND
                        ) { type, onoff, text ->
                            if (onoff) {
                                if (text.isNotEmpty()) {
                                    viewModel.ProfileRead(120, text)
                                }else{
                                    viewModel.toast.value = "메시지를 작성하고 프로필 승인을 요청해주세요."
                                }
                            }
                            drivepop1!!.dismiss()
                        }
                    drivepop1!!.show(fm!!, "")
                }
                BoardCheckViewStatus.PROFILE_SET ->{
                    drivepop1 =
                        ProfileLikeFriendDialog(
                            this,
                            "",
                            PopTypeStatus.DRIVE_FRIEND
                        ) { type, onoff, text ->
                            if (onoff) {
                                if (text.isNotEmpty()) {
                                    viewModel.ProfileRead(120, text)
                                }else{
                                    viewModel.toast.value = "메시지를 작성하고 프로필 승인을 요청해주세요."
                                }
                            }
                            drivepop1!!.dismiss()
                        }
                    drivepop1!!.show(fm!!, "")
                }
                else -> {}
            }
        })

        viewModel.drivepassset.observe(this, Observer {
            if (it != null) {
                if (it) {
                    Log.d("미니", "미니 프로필 팝업: ")

                    var miniprodialog = MiniProfileDialog(
                        this,
                        viewModel!!.my_user_data.value?.u_seq.toString(),
                        viewModel.select_seq.value.toString(),
                        viewModel.select_anonymousNickname.value.toString()
                    ) { isprofile ->

                        var i = Intent(this, LikeSendActivity::class.java)
                        i.putExtra("sub_seq", viewModel.select_seq.value.toString())
                        i.putExtra("type", "익명게시판")
                        startActivity(i)
                    }
                    miniprodialog.show(supportFragmentManager, "")

                } else {
                    var drivepassdialog = DrivePassDialog(this) { isok ->
                        if (isok) {
                            val i = Intent(this, HeartsShopActivity::class.java)
                            startActivity(i)
                            this.overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                        }

                    }
                    drivepassdialog!!.show(fm!!, "")
                }
            }

        })

        viewModel.ProfileReadApiCall.observe(this, Observer {
            Log.d("프로필최종열람", it.toString())
            if (it) {

//                var miniprodialog = MiniProfileDialog(this,viewModel!!.my_u_seq.value!!.toString(),viewModel.u_seq.value.toString()){ isprofile->
//                    var i  = Intent(this, LikeSendActivity::class.java)
//                    i.putExtra("sub_seq",viewModel.select_seq.value.toString())
//                    i.putExtra("type","익명게시판")
////                        i.putExtra("nick",data.u_nickname)
////                        startActivity(i)
//                }
//                miniprodialog.show(supportFragmentManager,"")
//                var i =Intent(this,ProfileActivity::class.java)
//                i.putExtra("sub_seq",viewModel.select_seq.value)
//                startActivity(i)
//                overridePendingTransition(R.anim.fadein, R.anim.fadeout)
            }
        })

        viewModel.update.observe(this, Observer {


            if (it != null) {
                cAdapter?.notifyItemChanged(it)
            }

        })

        viewModel.subUpdate.observe(this, Observer {


            if (it != null) {
                cAdapter?.notifyDataSetChanged()
            }

        })


        viewModel.tagNickname.observe(this, Observer {

            if (it != null) {
                if (it.equals("")) {


                } else {
//                    viewModel.commentInputType.value = 0
//                    keyboardFocus("")
                }
            }

        })
//

        viewModel.myCarBoardData.observe(this, Observer {

            if (it != null) {

                viewModel.bookmark.value = it.bookMarkCheck
                viewModel.like.value = it.likeCheck
                viewModel.likeCount.value = it.likeCnt
            }

        })
        viewModel.toast.observe(this, Observer {
            if (it.isNotEmpty()) {
                if (it.equals("하트가 부족합니다.")) {
                    var drivePassPayDialog = DrivePassPayDialog(
                        this,
                        DrivePassDialogStatus.NOTHEART,
                        "",
                        0
                    ) { type, isok ->
                        if (isok) {
                            val i = Intent(this, HeartsShopActivity::class.java)
                            startActivity(i)
                            this.overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                        }

                    }
                    drivePassPayDialog!!.show(supportFragmentManager, "")
                } else {
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                }

            }
        })

        viewModel.stateCall.observe(this, Observer {

            if (it != null) {
                when (it) {
                    Constans.DIALOG_POST_COMMENT_DELETE_API_CALL -> {
                        viewModel.getMyCarComment()

                        viewModel.tagNickname.value = null
                        viewModel.commentContents.value = ""
                        binding!!.edtCommentSend.text = null
                    }

                    Constans.DIALOG_POST_SUB_COMMENT_DELETE_API_CALL -> {
                        viewModel.getMyCarComment()
                        viewModel!!.cs_seq.value = "0"
                        viewModel.tagNickname.value = null
                        viewModel.commentContents.value = ""
                        binding!!.edtCommentSend.text = null
                    }

                    Constans.MY_CAR_POST_LIKE -> {


                    }
                    Constans.MY_CAR_POST_BOOKMARK -> {

                    }
                    Constans.DIALOG_POST_DELETE_API_CALL_CLEAR -> {
                        var intent = Intent()
                        intent.putExtra("type", "삭제")
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }

                    Constans.DIALOG_POST_BLOCK_API_CALL -> {
                        oneButtonDialog = OneButtonSetDialog(
                            this,
                            PopOneTypeStatus.DIALOG_POST_BLOCK_OK
                        ) { type, check ->

                        }
                        oneButtonDialog!!.show(this.supportFragmentManager, "")
                    }
                    Constans.DIALOG_POST_BLOCK_COMMENT_API_CALL -> {
                        oneButtonDialog = OneButtonSetDialog(
                            this,
                            PopOneTypeStatus.DIALOG_POST_COMMENT_BLOCK_OK
                        ) { type, check ->

                        }
                        oneButtonDialog!!.show(this.supportFragmentManager, "")
                    }


                    Constans.DIALOG_POST_NOTI_API_CALL -> {
                        oneButtonDialog =
                            OneButtonSetDialog(this, PopOneTypeStatus.DIALOG_POST_REPORT)
                            { type, check ->

                            }
                        oneButtonDialog!!.show(this.supportFragmentManager, "")
                    }

                }
            }
        })
        viewModel.isnotheart.observe(this, Observer {
            if(it){
                var notheart  = TwoButtonDialog(this, Constans.DIALOG_LACK_HEART,this, Constans.DIALOG_LACK_HEART)
                notheart!!.show(supportFragmentManager,"")
            }
        })
    }

    private fun clickListeners() {
        binding!!.ivBack.setOnClickListener {
            finish()
        }

        binding!!.tvDrive.setOnClickListener {
            if(application!!.CheckCompany() == false) {
                if (viewModel.myCarBoardData.value?.u_seq != null) {
                    viewModel.select_seq.value = viewModel.myCarBoardData.value?.u_seq.toString()
                    viewModel.boardViewCheckClick(viewModel.select_seq.value!!)
                }
            }else{
                viewModel.toast.value = getString(R.string.company_profile_open_text)
            }

        }
        binding!!.ivSend.setOnClickListener {

            if (viewModel.tagNickname.value == null) {
                if (viewModel.commentInputType.value == 1){
                    if(viewModel.commentContents.value.isNullOrEmpty()) {
                        viewModel.toast.value ="댓글을 1자이상 작성해 주세요."
                    }else{
                        viewModel.updateComment()
                        viewModel.tagNickname.value = null
                        binding!!.edtCommentSend.text = null
                        viewModel.commentInputType.value = 0
                    }
                }else{
                    viewModel.insertComment()
                    viewModel.tagNickname.value = null
                    binding!!.edtCommentSend.text = null
                    viewModel.commentInputType.value = 0
                }
            } else {
                if (viewModel.commentInputType.value == 1){
                    if(viewModel.commentContents.value.isNullOrEmpty()) {
                        viewModel.toast.value ="대댓글을 1자이상 작성해 주세요."
                    }else{
                        viewModel.updateSubComment()

                        viewModel.tagNickname.value = null
                        binding!!.edtCommentSend.text = null
                        viewModel.commentInputType.value = 0
                    }
                }else{
                    viewModel.insertSubComment()
                    viewModel.tagNickname.value = null
                    binding!!.edtCommentSend.text = null
                    viewModel.commentInputType.value = 0
                }

            }



        }
        binding!!.ivLike.setOnClickListener {
            var count = 1
            var like = 0
            if (viewModel.like_type.value == 0) {

                like = count
                viewModel.like_type.value = count
                var int = viewModel.b_like.value?.toInt()
                int = int?.plus(1)
                viewModel.b_like.value = int.toString()
            } else {
                like = 0
                viewModel.like_type.value = 0
                var int = viewModel.b_like.value?.toInt()
                int = int?.minus(1)
                viewModel.b_like.value = int.toString()
            }

            viewModel.InsertBoardLikeSave(
                viewModel.my_user_data.value?.u_seq.toString(),
                viewModel.b_seq.value.toString(),
                "BOARD",
                like
            )

        }


        binding!!.tvBLike.setOnClickListener {
            showProfileLikeDialog()
        }
        binding!!.tvPlus.setOnClickListener {
            showProfileLikeDialog()
        }
        binding!!.ivMore.setOnClickListener {
            val originalPos = IntArray(2)
            binding!!.ivMore.getLocationInWindow(originalPos)
            val x = originalPos[0]
            val y = originalPos[1]
            Log.d("좌표", "$y + $x")


            var viewtype = 0
            if (viewModel.my_user_data.value?.u_seq == viewModel.u_seq.value || application.CheckManager()) {
                viewtype = Constans.MY_SEQ
                Log.d("체크", "true: ")
            } else {
                viewtype = Constans.YOU_SEQ
                Log.d("체크", "false: ")
            }

            postPopUpDialog = PostPopUpDialog(
                x,
                y,
                this,
                this,
                viewtype,
                Constans.POST_MORE,
                viewModel.b_seq.value.toString(),
                "",
                ""
            )
            postPopUpDialog!!.show(this.supportFragmentManager, "")
        }

//        binding.tvViewProfile.setOnClickListener {
//            if (viewModel.anonymousForumDeepPostData.value?.u_seq != null){
//                viewModel.select_seq.value = viewModel.anonymousForumDeepPostData.value?.u_seq!!
//                viewModel.boardViewCheckClick(viewModel.anonymousForumDeepPostData.value?.u_seq!!)
//            }
//
////            viewProfileDialog = ViewProfileDialog(this) { isfinish ->
////
////            }
////            viewProfileDialog!!.show(fm!!, "")
//        }


    }


    private fun showProfileLikeDialog() {
        if (viewModel.u_seq.value == viewModel.my_user_data.value?.u_seq) {
            likeListDialog = LikeListDialog(
                this,
                Constans.YOU_SEQ,
                viewModel.my_user_data.value?.u_seq!!,
                viewModel.u_seq.value!!,
                viewModel.b_seq.value!!
            ) { data, isok ->
                if (isok) {

                    viewModel.select_seq.value = data.u_seq
                    viewModel.select_anonymousNickname.value = data.ri_values
                    if (viewModel.my_user_data.value.toString() != viewModel.select_seq.value.toString()) {
                        viewModel.DrivePassCheck()
                    }
                }
            }


            likeListDialog!!.show(this.supportFragmentManager, "")
        }
    }


     private fun testFilterData(): ArrayList<CommentData> {
        var list: ArrayList<CommentData> = arrayListOf()
        var listSub: ArrayList<CommentSubData> = arrayListOf()
        var x = 0
        for (i: Int in 0..2) {
            var data = CommentSubData(
                "뛰노는 사마귀",
                "남",
                "대댓글입니다,대댓글입니다,대댓글입니다,대댓글입니다,대댓글입니다,대댓글입니다,대댓글입니다,대댓글입니다,대댓글입니다,댓글입니다",
                "1분전",
                "999"
            )
            x = x.plus(1)
            listSub.add(data)
        }
        for (i: Int in 0..10) {
            var data = CommentData(
                "뛰노는 사마귀",
                "남",
                "댓글입니다,댓글입니다,댓글입니다,댓글입니다,댓글입니다,댓글입니다,댓글입니다,댓글입니다,댓글입니다,댓글입니다",
                "1분전",
                "999",
                listSub
            )
            x = x.plus(1)
            list.add(data)
        }


        return list
    }


    @SuppressLint("SimpleDateFormat")
    fun dateTimeToMillSec(dateTime: String): Long {
        Log.d("변환전 시간", dateTime)

        val curTime = System.currentTimeMillis()
        Log.d("현재 시간", curTime.toString())
        val target = "T"
        var result = dateTime.replace(target, " ") // '-'를 공백으로 바꿔준다.
        var timeInMilliseconds: Long = 0
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        try {
            val mDate = sdf.parse(result)

            timeInMilliseconds = mDate.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        Log.d("변환된 시간", timeInMilliseconds.toString())
        return timeInMilliseconds
    }

     fun setTime(time : Long) :String{
         val curTime = System.currentTimeMillis()
         Log.d("현재 시간", curTime.toString())
         var diffTime = (curTime- time) / 1000
         var msg: String? = null
         if(diffTime < TimeValue.SEC.value ) {
             msg = "방금 전"
             return msg.toString()
         }else {
             for (i in TimeValue.values()) {
                 diffTime = diffTime.div(i.value)


                 if (diffTime > 14 && i == TimeValue.HOUR ||i > TimeValue.HOUR){
                     msg = "지난글"
                     return msg.toString()
                 }
                 if (diffTime < i.maximum) {
                     msg=i.msg
                     break
                 }
             }
         }
         return diffTime.toString() + msg.toString()
     }

    private fun keyboardFocus(comment_value: String) {
        binding!!.edtCommentSend.requestFocus()
        binding!!.edtCommentSend.postDelayed(Runnable {
            val keyboard: InputMethodManager =
                getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.showSoftInput(binding!!.edtCommentSend, 0)
            if (!comment_value.equals("")) {
                binding.edtCommentSend.setText(comment_value)
            }
        }, 200)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun okEvent(
        type: Int,
        okAndCancel: Boolean,
        comment_value: String,
        seq: String,
        u_nickname: String
    ) {
        if (okAndCancel) {
            Log.d("다이얼로그 이벤트", type.toString())
            when (type) {
                Constans.POST_MORE_DELETE -> {
                    twoButtonDialog =
                        TwoButtonDialog(
                            this,
                            Constans.DIALOG_POST_DELETE,
                            this,
                            Constans.POST_MORE_DELETE
                        )
                    twoButtonDialog!!.show(this.supportFragmentManager, "")
                }
                Constans.DIALOG_POST_DELETE_API_CALL -> {
                    viewModel.deleteMyCarBoard()
                }
                Constans.DIALOG_POST_COMMENT_DELETE_API_CALL -> {
                    viewModel.deleteComment()
                }
                Constans.DIALOG_POST_SUB_COMMENT_DELETE_API_CALL -> {
                    viewModel.deleteSubComment()
                }
                Constans.COMMENT_MORE_DELETE -> {
                    twoButtonDialog =
                        TwoButtonDialog(
                            this,
                            Constans.DIALOG_POST_DELETE,
                            this,
                            Constans.COMMENT_MORE_DELETE
                        )
                    twoButtonDialog!!.show(this.supportFragmentManager, "")
                }

                Constans.COMMENT_SUB_MORE_DELETE -> {
                    twoButtonDialog = TwoButtonDialog(
                        this,
                        Constans.DIALOG_POST_DELETE,
                        this,
                        Constans.COMMENT_SUB_MORE_DELETE
                    )
                    twoButtonDialog!!.show(this.supportFragmentManager, "")
                }
                Constans.POST_MORE_EDIT -> {
                    val i = Intent(this, MyCarBoardWritingActivity::class.java)
                    //글수정일때
                    i.putExtra("viewType", "글수정")
                    i.putExtra(
                        "myCarBoardData", viewModel.myCarBoardData.value
                    )
                    i.putExtra("position",viewModel.position.value)
                    i.putExtra("myCarBoardSubList",viewModel.subInfoList.value)
                    resultLauncher.launch(i)
                }
                Constans.COMMENT_MORE_EDIT -> {
                    viewModel.commentInputType.value = 1

                    keyboardFocus(comment_value)
                    viewModel.tagNickname.value = null
                    viewModel.cm_seq.value = seq.toString()
                    viewModel.commentContents.value = comment_value
                    binding!!.edtCommentSend.setText(comment_value)

                }

                Constans.COMMENT_SUB_MORE_EDIT -> {
                    viewModel.commentInputType.value = 1
                    keyboardFocus(comment_value)
                    viewModel.cs_seq.value = seq.toString()
                    viewModel.commentContents.value = comment_value
                    binding!!.edtCommentSend.setText(comment_value)
                    viewModel.tagNickname.value = "@" + u_nickname
                }

                Constans.POST_MORE_NOTI -> {
                    blockProfileDialog = PoliceDialog(
                        this,
                        this,
                        Constans.DIALOG_POST_POLICE_CALL,
                        viewModel.myCarBoardData.value?.mmi_seq.toString()
                    )
                    blockProfileDialog!!.show(this.supportFragmentManager, "")
                }

                Constans.COMMENT_MORE_NOTI -> {
                    blockProfileDialog = PoliceDialog(
                        this,
                        this,
                        Constans.DIALOG_COMMENT_NOTI_CALL,
                        seq.toString()
                    )
                    blockProfileDialog!!.show(this.supportFragmentManager, "")
                }

                Constans.COMMENT_SUB_MORE_NOTI -> {
                    blockProfileDialog = PoliceDialog(
                        this,
                        this,
                        Constans.DIALOG_COMMENT_SUB_NOTI_CALL,
                        seq.toString()
                    )
                    blockProfileDialog!!.show(this.supportFragmentManager, "")
                }

                Constans.DIALOG_POST_POLICE_CALL -> {
                    viewModel.PoliceInsert(
                        "MYCAR",
                        comment_value,
                        viewModel.myCarBoardData.value?.mmi_seq.toString()
                    )

                }

                Constans.DIALOG_COMMENT_NOTI_CALL -> {
                    viewModel.PoliceInsert(
                        "MYCARCOMMENT",
                        comment_value,
                        viewModel!!.cm_seq.value.toString()
                    )

                }

                Constans.DIALOG_COMMENT_SUB_NOTI_CALL -> {
                    viewModel.PoliceInsert(
                        "MYCARSUBCOMMENT",
                        comment_value,
                        viewModel!!.cs_seq.value.toString()
                    )

                }


                Constans.POST_MORE_BLOCK -> {
                    blockPostDialog = BlockDialog(
                        this,
                        this,
                        Constans.DIALOG_POST_BLOCK,
                        viewModel.myCarBoardData.value?.mmi_seq.toString()
                    )
                    blockPostDialog!!.show(this.supportFragmentManager, "")

//                    viewModel.PostBlockInsert("BOARD",viewModel.anonymousForumDeepPostData.value?.b_contents!!)
                }

                Constans.COMMENT_MORE_BLOCK -> {
                    blockPostDialog = BlockDialog(
                        this,
                        this,
                        Constans.DIALOG_COMMENT_BLOCK_CALL,
                        seq
                    )
                    blockPostDialog!!.show(this.supportFragmentManager, "")

//                    viewModel.PostBlockInsert("BOARD",viewModel.anonymousForumDeepPostData.value?.b_contents!!)

                }

                Constans.COMMENT_SUB_MORE_BLOCK -> {
                    blockPostDialog = BlockDialog(
                        this,
                        this,
                        Constans.DIALOG_COMMENT_SUB_BLOCK_CALL,
                       seq
                    )
                    blockPostDialog!!.show(this.supportFragmentManager, "")

//                    viewModel.PostBlockInsert("BOARD",viewModel.anonymousForumDeepPostData.value?.b_contents!!)

                }

                Constans.DIALOG_POST_BLOCK -> {
                    viewModel.PostBlockInsert(
                        "MYCAR",
                        "",
                        viewModel.myCarBoardData.value?.mmi_seq.toString()
                    )

                }
                Constans.DIALOG_COMMENT_BLOCK_CALL -> {
                    viewModel.PostBlockInsert(
                        "MYCARCOMMENT",
                        "",
                        viewModel!!.cm_seq.value.toString()
                    )

                }

                Constans.DIALOG_COMMENT_SUB_BLOCK_CALL -> {
                    viewModel.PostBlockInsert(
                        "MYCARSUBCOMMENT",
                        "",
                        viewModel!!.cs_seq.value.toString()
                    )

                }


                //벤확인
                Constans.DIALOG_POST_BLOCK_CHECK -> {
                    finish()
                }


            }
        }


    }
     @RequiresApi(Build.VERSION_CODES.O)
     var resultLauncher =
         registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
             if (result.resultCode == Activity.RESULT_OK) {
                 // There are no request codes
                 val idata: Intent? = result.data
                 if (idata != null) {
                     var data = idata.getParcelableExtra<MyCarBoardData>("data")
                     var position = idata.getIntExtra("position",0)
                     var type = idata.getStringExtra("type")
                     when(type){
                         "글수정"->{
//
                             viewModel.viewtype.value = "글수정"
                             viewModel.myCarBoardData.value = data
                         }


                     }

                 }

             }
         }

    private fun setKeyboardVisibilityListener(onKeyboardVisibilityListener: OnKeyboardVisibilityListener) {
        val parentView = (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
        parentView.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            private var alreadyOpen = false
            private val defaultKeyboardHeightDP = 100
            private val EstimatedKeyboardDP =
                defaultKeyboardHeightDP + if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) 48 else 0
            private val rect = Rect()
            override fun onGlobalLayout() {
                val estimatedKeyboardHeight = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    EstimatedKeyboardDP.toFloat(),
                    parentView.resources.displayMetrics
                ).toInt()
                parentView.getWindowVisibleDisplayFrame(rect)
                val heightDiff = parentView.rootView.height - (rect.bottom - rect.top)
                val isShown = heightDiff >= estimatedKeyboardHeight
                if (isShown == alreadyOpen) {
                    Log.i("Keyboard state", "Ignoring global layout change...")
                    return
                }
                alreadyOpen = isShown
                onKeyboardVisibilityListener.onVisibilityChanged(isShown)
            }
        })
    }

    override fun finish() {

        var intent = Intent()
        intent.putExtra("position", viewModel.myCarBoardDataPosition.value)
        if (viewModel.viewtype.value=="글수정"){
            intent.putExtra("data", viewModel.myCarBoardData.value)
            intent.putExtra("type", "글수정")
        }else{
            if (viewModel.like.value != viewModel.myCarBoardData.value?.likeCheck) {
                intent.putExtra("type", "좋아요")
                intent.putExtra("like", viewModel.like.value)
                intent.putExtra("likecount",viewModel.likeCount.value)
            }
            if (viewModel.bookmark.value != viewModel.myCarBoardData.value?.bookMarkCheck) {
                intent.putExtra("type", "북마크")
                intent.putExtra("bookmark", viewModel.bookmark.value)

            }
        }



        setResult(Activity.RESULT_OK, intent)
        super.finish()

        overridePendingTransition(
            com.supercarlounge.supercar.R.anim.fadein,
            com.supercarlounge.supercar.R.anim.fadeout
        )
    }


    override fun onVisibilityChanged(visible: Boolean) {
        if (!visible) {
            if (viewModel.commentInputType.value == 1) {
                viewModel.commentInputType.value = 0
                binding!!.edtCommentSend.text = null
                viewModel.tagNickname.value = null

            }
            if (binding!!.edtCommentSend.text!!.isEmpty()) {
                viewModel.tagNickname.value = null

            }
        } else {
            Log.d("", viewModel.scrollY.value.toString())
            binding.scroll.scrollY = viewModel.scrollY.value!!
        }


    }
//    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
//        val focusView = currentFocus
//        if (focusView != null) {
//            val rect = Rect()
//            focusView.getGlobalVisibleRect(rect)
//            val x = ev.x.toInt()
//            val y = ev.y.toInt()
//            if (!rect.contains(x, y)) {
//                val imm: InputMethodManager =
//                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//                if (imm != null) imm.hideSoftInputFromWindow(focusView.windowToken, 0)
//                focusView.clearFocus()
//            }
//        }
//        return super.dispatchTouchEvent(ev)
//    }
}
