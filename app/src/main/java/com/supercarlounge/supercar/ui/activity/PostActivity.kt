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
import com.supercarlounge.supercar.*
import com.supercarlounge.supercar.adapter.CommentAdapter
import com.supercarlounge.supercar.adapter.ViewListAdapter
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.data.BoardCommentData
import com.supercarlounge.supercar.data.CommentData
import com.supercarlounge.supercar.data.CommentSubData
import com.supercarlounge.supercar.data.ViewListData
import com.supercarlounge.supercar.databinding.*
import com.supercarlounge.supercar.dialog.*
import com.supercarlounge.supercar.enumset.*
import com.supercarlounge.supercar.viewmodel.*
import java.text.ParseException
import java.text.SimpleDateFormat


open class PostActivity : BaseActivitiy(), DialogEvent, OnKeyboardVisibilityListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityPostBinding
    private lateinit var viewModel: PostViewModel
    private var cAdapter: CommentAdapter? = null
    private var viewListAdapter: ViewListAdapter? = null
    private lateinit var application: MainApplication
    private var postPopUpDialog: PostPopUpDialog? = null
    private var likeListDialog: LikeListDialog? = null
    private var twoButtonDialog: TwoButtonDialog? = null
    private var blockProfileDialog : PoliceDialog? =null
    private var blockPostDialog :BlockDialog? =null
    private var oneButtonDialog : OneButtonSetDialog? =null
    private var viewProfileDialog:ViewProfileDialog? =null
    private var fm :FragmentManager? =null
    var drivepop1: ProfileLikeFriendDialog? = null
    var position_flag = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        application = applicationContext as MainApplication
        viewModel = ViewModelProvider(this).get(PostViewModel::class.java)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel
        binding!!.llNoComment.visibility = View.GONE
        val i = intent
        viewModel.titleText.value = i.getStringExtra("c_seq")


        var bseq = i.getStringExtra("b_seq")
        Log.d("가져온 seq", bseq.toString())
        viewModel.b_seq.value = bseq?.toInt()
        viewModel.my_u_seq.value = application.userData?.u_seq
        viewModel.ismymanager.value = application.CheckManager()
        viewModel.u_seq.value = i.getIntExtra("u_seq", 0)
        var uid = i.getStringExtra("uid")

        if (uid != null){
            viewModel.u_seq.value = uid.toInt()

        }




        fm= supportFragmentManager

//        if (like == "Y") viewModel.like_type.value = 1 else  viewModel.like_type.value =0


        clickListeners()
        observes()
        setAdapters()
        editTextListeners()




        setKeyboardVisibilityListener(this)
        binding!!.ivImage.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {


            }


        })

//        if (viewModel.u_seq.value != viewModel.my_u_seq.value){
            viewModel.boardViewsUpdate()
//            binding.llViewList.visibility = View.GONE
//        }else{
//
////            binding.llViewList.visibility = View.VISIBLE
//        }
    }


    private fun showViewList(){
//        if (viewModel.my_u_seq.value == viewModel.u_seq.value){

            var list :ArrayList<ViewListData> = arrayListOf()




            viewListAdapter = ViewListAdapter(this,  list, viewModel!!) { data, position ->
//                Log.d("어뎁터", data.target_seq)
                viewModel.select_seq.value = data.target_seq
                viewModel.select_anonymousNickname.value = data.rn_nickname
                if (viewModel.my_u_seq.value.toString() != viewModel.select_seq.value.toString()) {
                    viewModel.DrivePassCheck()
                }
            }
            binding.rvViewList.adapter = viewListAdapter
            binding.rvViewList.layoutManager = GridLayoutManager(this,2, GridLayoutManager.VERTICAL, false)
            viewModel.boardViewList()
//        }
    }



    override fun onResume() {
        super.onResume()

        viewModel.getAnonymousForumDeepPost()
    }

    private fun editTextListeners() {
        binding!!.edtCommentSend.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {
                Log.d("테스트로그 1", p0.toString())
                Log.d("테스트로그 2", viewModel.commentInputType.value.toString())
                Log.d("테스트로그 3", viewModel.tagNickname.toString())
                if (p0!!.isNotEmpty()) {
                    viewModel.commentContents.value = p0.toString()
                } else {
                    if (viewModel.commentInputType.value == 1) {
                        viewModel.commentContents.value = ""
//                        viewModel.tagNickname.value = null

//                        viewModel.css_seq.value = null
                    } else {
                        viewModel.tagNickname.value = null
                        viewModel.commentContents.value = ""
                        viewModel.css_seq.value = null
                        viewModel.cm_seq.value = null
                    }

                }

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })


    }
//
//    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
//        val contentsView = binding!!.rvPost
//        val test1 = IntArray(2)
//        contentsView.getLocationInWindow(test1)
//        val test2 = IntArray(2)
//        contentsView.getLocationOnScreen(test2)
//        Log.d(TAG, "dispatchTouchEvent: ")
//        println(test1[1].toString() + " " + test2[1])
//        return super.dispatchTouchEvent(ev)
//    }


    private fun viewRedefinition() {
        val layoutParams = binding.ivImage.layoutParams
        layoutParams.width = binding.ivImage.width
        layoutParams.height = binding.ivImage.width
        binding.ivImage.layoutParams = layoutParams
    }

    private fun setAdapters() {
        var list: ArrayList<BoardCommentData> = arrayListOf()
        cAdapter = CommentAdapter(this, this, list, viewModel!!, supportFragmentManager) { data, position, view, type ->
            viewModel!!.commentPosition.value = position
            viewModel!!.cm_seq.value = data.cm_seq.toString()
            viewModel.select_seq.value = data.u_seq.toString()
            viewModel.select_anonymousNickname.value = data.u_nickname.toString()

            if (type == Constans.NICKNAME) {

                viewModel!!.us_seq.value = data.u_seq.toString()
                val originalPos = IntArray(2)
                view.getLocationInWindow(originalPos)
                val x = originalPos[0]
                val y = originalPos[1]


                var viewtype = 2

                if (viewModel.my_u_seq.value == data.u_seq || application!!.CheckManager()) {
                    viewtype = Constans.MY_SEQ
                    Log.d("체크", "true: ")
                } else {
                    viewtype = Constans.YOU_SEQ
                    Log.d("체크", "false: ")
                }
                Log.d("테스트로그", data.cm_values)

                postPopUpDialog = PostPopUpDialog(x, y, this, this, viewtype, Constans.COMMENT_MORE, data.cm_seq.toString(),data.u_nickname,data.cm_values,)

                postPopUpDialog!!.show(this.supportFragmentManager, "")


            } else if (type == Constans.HEART) {
                if(application.CheckCompany() == false) {
                    if (viewModel.my_u_seq.value.toString() != data.u_seq.toString()) {
                        viewModel.boardViewCheckClick(viewModel.select_seq.value.toString())
                    }
                }else{
                    viewModel.toast.value = getString(R.string.company_profile_open_text)
                }
//                if (viewModel.comment_like_type.value == 1) {
//                    viewModel.comment_like_type.value = 0
//                } else {
//
//                    viewModel.comment_like_type.value = 1
//
//                }
//                viewModel.insertComMainLike(position)

            } else {
                viewModel.tagNickname.value = "@" + data.u_nickname


            }


        }

//        binding!!.rvPost.setOnScrollChangeListener(object :View.OnScrollChangeListener{
//            override fun onScrollChange(v: View?, scrollx: Int, scrolly: Int, oldscrollx: Int, oldscrolly: Int) {
//               if (position_flag){
//                   if ((!v?.canScrollVertically(1)!!)){
//
//
//                       viewModel.getAnonymousForumComment(     application.userData?.u_seq.toString(),
//                           viewModel.b_seq.value.toString())
//                       Log.d("댓글 페이지", viewModel.page.value.toString())
//                   }
//               }
//            }
//
//
//        })

        viewModel.b_seq.value?.let {
            viewModel.getAnonymousForumComment(
                application.userData?.u_seq.toString(),
                it.toString()
            )
        }
        binding.rvPost.adapter = cAdapter
        binding.rvPost.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.rvPost?.itemAnimator  =null    //리사이클러뷰 애니메이터 get
//        if (animator is SimpleItemAnimator) {          //아이템 애니메이커 기본 하위클래스
//            animator.supportsChangeAnimations =
//                false  //애니메이션 값 false (리사이클러뷰가 화면을 다시 갱신 했을때 뷰들의 깜빡임 방지)
//        }
    }
    var listener: RecyclerView.OnScrollListener = object: RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            var lastposition=(binding?.rvPost?.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            var itemcount=cAdapter!!.itemCount
            Log.d("getDrive : last" , ""+itemcount)
            Log.d("getDrive : itemcount" , ""+lastposition)
            if(itemcount== (lastposition +1)) {
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
        viewModel.anonymousForumCommentList.observe(this, Observer {


            if (it != null) {

//                cAdapter?.setdata(it)

            }

        })
        viewModel.viewlistCheck.observe(this, Observer {
            if (it){
                showViewList()

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
                BoardCheckViewStatus.COMPANY_PROFILE->{
                    val i = Intent(this, ProfileCompanyActivity::class.java)
                    i.putExtra("sub_seq", viewModel.select_seq.value)
                    startActivity(i)
                }
                BoardCheckViewStatus.NOT_FRIEND_POPUP ->{

                         viewProfileDialog = ViewProfileDialog(this) { isfinish ->
                            if (isfinish){
                                viewModel.ProfileRead(3,"")
                            }
                         }
                         viewProfileDialog!!.show(fm!!, "")
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
            if (it !=null) {
                if (it) {
                    Log.d("미니", "미니 프로필 팝업: ")
                    var myseq =  viewModel.my_u_seq.value!!.toString()
                    viewModel.GetMatchingData(myseq,viewModel.select_seq.value!!)

                } else {
                    var drivepassdialog = DrivePassDialog(this) { isok ->
                        if (isok) {
                            val i = Intent(this, HeartsShopActivity::class.java)
                            startActivity(i)
                           // this.overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                        }

                    }
                    drivepassdialog!!.show(fm!!, "")
                }
            }

        })

        viewModel.ProfileReadApiCall.observe(this, Observer {
            Log.d("프로필최종열람", it.toString())
            if(it){

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

        viewModel.anonymousForumCommentCount.observe(this, Observer {

            if (it != null) {
                if (it > 0) {
                    binding!!.llNoComment.visibility = View.GONE

                } else {
                    binding!!.llNoComment.visibility = View.VISIBLE
                }
            }

        })

        viewModel.anonymousForumDeepPostData.observe(this, Observer {

            if (it != null) {

                viewModel.image.value = it.b_cover
                viewModel.sub_seq.value = it.u_seq

                if (viewModel.my_u_seq.value.toString() == viewModel.sub_seq.value ){
                    binding.tvViewProfile.visibility = View.GONE
                }

                var like = it.like_yn
                var bookmark = it.bookmark
                var b_like = it.b_like
                binding.tvBContents.text = it.b_contents?.let { it1 ->
//                    application.decodeset(
                    it1
//                )
                }
                viewModel.time.value = setTime(dateTimeToMillSec(it.b_date!!))
                if (like == "Y") viewModel.like_type.value = 1 else viewModel.like_type.value = 0
                viewModel.b_like.value = it.b_like
                if (bookmark == "Y") viewModel.bookmark.value = 1 else viewModel.bookmark.value = 0



                if (it.b_cover.equals("")) {

                    viewModel.image.value = null
                } else {
                    Glide.with(binding.ivImage).load(it.b_cover).centerInside()
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {


                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                viewRedefinition()

                                return false
                            }

                        }).into(binding.ivImage)
                }
                if (it.u_gender.equals("MALE")) {
                    Glide.with(binding!!.ivSex).load(R.drawable.ic_man_12x12).centerInside()
                        .into(binding!!.ivSex)
                } else {
                    Glide.with(binding!!.ivSex).load(R.drawable.ic_woman_12x12).centerInside()
                        .into(binding!!.ivSex)
                }

                viewModel.boardViewCheck()
            }

        })
        viewModel.toast.observe(this,Observer{
            if(it.isNotEmpty()){
                if (it.equals("하트가 부족합니다.")){
                    var drivePassPayDialog = DrivePassPayDialog(this, DrivePassDialogStatus.NOTHEART,"",0) { type, isok->
                        if(isok){
                            val i= Intent(this, HeartsShopActivity::class.java)
                            startActivity(i)
                           // this.overridePendingTransition(R.anim. fadein, R.anim.fadeout)
                        }

                    }
                    drivePassPayDialog!!.show(supportFragmentManager,"")
                }else{
                    Toast.makeText(this,it, Toast.LENGTH_SHORT).show()
                }

            }
        })

        viewModel.stateCall.observe(this, Observer {

            if (it != null) {
                when (it) {
                    Constans.DIALOG_POST_COMMENT_DELETE_API_CALL -> {
                        viewModel.getAnonymousForumComment(
                            application.userData?.u_seq.toString(),
                            viewModel.b_seq.value.toString()
                        )

                        viewModel.tagNickname.value = null
                        viewModel.commentContents.value = null
                        binding!!.edtCommentSend.text = null
                    }

                    Constans.DIALOG_POST_SUB_COMMENT_DELETE_API_CALL -> {
                        viewModel.getAnonymousForumComment(
                            application.userData?.u_seq.toString(),
                            viewModel.b_seq.value.toString()
                        )
                        viewModel!!.cs_seq.value = "0"
                        viewModel.tagNickname.value = null
                        viewModel.commentContents.value = null
                        binding!!.edtCommentSend.text = null
                    }

                    Constans.DIALOG_POST_DELETE_API_CALL_CLEAR -> {
                        var intent  = Intent()
                        intent.putExtra("type","삭제")
                        setResult(Activity.RESULT_OK,intent)
                        finish()
                    }

                    Constans.DIALOG_POST_BLOCK_API_CALL -> {
                        oneButtonDialog = OneButtonSetDialog(this, PopOneTypeStatus.DIALOG_POST_BLOCK_OK){type,check->

                        }
                        oneButtonDialog!!.show(this.supportFragmentManager,"")
                    }
                    Constans.DIALOG_POST_BLOCK_COMMENT_API_CALL -> {
                        oneButtonDialog = OneButtonSetDialog(this, PopOneTypeStatus.DIALOG_POST_COMMENT_BLOCK_OK){type,check->

                        }
                        oneButtonDialog!!.show(this.supportFragmentManager,"")
                    }


                    Constans.DIALOG_POST_NOTI_API_CALL -> {
                        oneButtonDialog = OneButtonSetDialog(this,PopOneTypeStatus.DIALOG_POST_REPORT)
                        {type,check->

                        }
                        oneButtonDialog!!.show(this.supportFragmentManager,"")
                    }
                    else -> {}
                }
            }

        })
        viewModel.miniprofilestatus.observe(this, Observer {
            if (viewModel.select_seq.value.isNullOrEmpty() == false && !viewModel.select_seq.value.equals("0")) {
                if (it == false) {
                    showMiniProfile()
                } else {
                    var i = Intent(this, ProfileActivity::class.java)
                    i.putExtra("sub_seq", viewModel.select_seq.value)
                    startActivity(i)
                  //  overridePendingTransition(R.anim.fadein, R.anim.fadeout)
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
        binding!!.ivSend.setOnClickListener {

            if (viewModel.tagNickname.value == null) {
                if (viewModel.commentInputType.value == 1){
                    if(viewModel.commentContents.value.isNullOrEmpty()) {

                        viewModel.toast.value ="댓글을 1자이상 작성해 주세요."
                    }else{
                        viewModel.anonymousForumUpdateComment()
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
                        viewModel.anonymousForumUpdateSubComment()
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


//            viewModel.commentContents.value = null



        }
        binding!!.ivLike.setOnClickListener {
            var count =1
            var like =0
            if (viewModel.like_type.value == 0) {

                like=  count
                viewModel.like_type.value = count
                var int =        viewModel.b_like.value?.toInt()
                int =int?.plus(1)
                viewModel.b_like.value = int.toString()
            } else {
                like=  0
                viewModel.like_type.value = 0
                var int =   viewModel.b_like.value?.toInt()
                int =int?.minus(1)
                viewModel.b_like.value  = int.toString()
            }

            viewModel.InsertBoardLikeSave(
                viewModel.my_u_seq.value.toString(),
                viewModel.b_seq.value.toString(),
                "BOARD",
                        like
            )

        }

        binding!!.ivBookmark.setOnClickListener {
//            if (viewModel.bookmark.value == 0) {
//                viewModel.bookmark.value = 1
//            } else {
//                viewModel.bookmark.value = 0
//            }
            viewModel.InsertBookmark()
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
            if (viewModel.my_u_seq.value == viewModel.u_seq.value || application!!.CheckManager()) {
                viewtype = Constans.MY_SEQ
                Log.d("체크", "true: ")
            } else {
                viewtype = Constans.YOU_SEQ
                Log.d("체크", "false: ")
            }

            postPopUpDialog = PostPopUpDialog(x, y, this, this, viewtype, Constans.POST_MORE, viewModel.b_seq.value.toString(),"","")
            postPopUpDialog!!.show(this.supportFragmentManager, "")
        }

        binding.tvViewProfile.setOnClickListener {
            if(application.CheckCompany() == false) {
                if (viewModel.anonymousForumDeepPostData.value?.u_seq != null) {
                    viewModel.select_seq.value = viewModel.anonymousForumDeepPostData.value?.u_seq!!
                    viewModel.boardViewCheckClick(viewModel.anonymousForumDeepPostData.value?.u_seq!!)
                }
            }else{
                viewModel.toast.value = getString(R.string.company_profile_open_text)
            }

//            viewProfileDialog = ViewProfileDialog(this) { isfinish ->
//
//            }
//            viewProfileDialog!!.show(fm!!, "")
        }


    }


    private fun showProfileLikeDialog() {
        if (viewModel.u_seq.value == viewModel.my_u_seq.value) {
            likeListDialog = LikeListDialog(this, Constans.YOU_SEQ, viewModel.my_u_seq.value!!, viewModel.u_seq.value!!, viewModel.b_seq.value!!) {data, isok ->
                if (isok) {

                        viewModel.select_seq.value = data.u_seq
                    viewModel.select_anonymousNickname.value = data.ri_values
                    if (viewModel.my_u_seq.value.toString() != viewModel.select_seq.value.toString()) {
                        viewModel.DrivePassCheck()
                    }
                }
            }


            likeListDialog!!.show(this.supportFragmentManager, "")
        }
    }

    override fun finish() {
        super.finish()
//        overridePendingTransition(
//            com.supercarlounge.supercar.R.anim.fadein,
//            com.supercarlounge.supercar.R.anim.fadeout
//        )
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


    private fun keyboardFocus(comment_value: String){
        binding!!.edtCommentSend.requestFocus()
        binding!!.edtCommentSend.postDelayed(Runnable {
            val keyboard: InputMethodManager =
                getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.showSoftInput(binding!!.edtCommentSend, 0)
            if (!comment_value.equals("")){
                binding.edtCommentSend.setText(comment_value)
                Log.d("테스트로그","5")
            }
        }, 200)
    }
    override fun okEvent(type: Int, okAndCancel: Boolean,comment_value:String ,seq :String ,u_nickname:String) {
        if (okAndCancel) {
            Log.d("다이얼로그 이벤트", type.toString())
            when (type) {
                Constans.POST_MORE_DELETE -> {
                    twoButtonDialog =
                        TwoButtonDialog(this, Constans.DIALOG_POST_DELETE, this, Constans.POST_MORE_DELETE)
                    twoButtonDialog!!.show(this.supportFragmentManager, "")
                }
                Constans.DIALOG_POST_DELETE_API_CALL -> {
                    viewModel.anonymousForumPostDelete()
                }
                Constans.DIALOG_POST_COMMENT_DELETE_API_CALL -> {
                    viewModel.anonymousForumCommentDelete()
                }
                Constans.DIALOG_POST_SUB_COMMENT_DELETE_API_CALL -> {
                    viewModel.SubCommentDelete()
                }
                Constans.COMMENT_MORE_DELETE -> {
                    twoButtonDialog =
                        TwoButtonDialog(this, Constans.DIALOG_POST_DELETE, this, Constans.COMMENT_MORE_DELETE)
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
                    val i= Intent(this, PostWritingActivity::class.java)
                    //글수정일때
                    i.putExtra("viewType","글수정")
                    i.putExtra("anonymousForumDeepPostData",viewModel.anonymousForumDeepPostData.value)
                    startActivity(i)
                }
                Constans.COMMENT_MORE_EDIT -> {
                    viewModel.commentInputType.value = 1

                    keyboardFocus(comment_value)
                    viewModel.tagNickname.value = null
                    viewModel.cm_seq.value = seq.toString()
                    viewModel.commentContents.value = comment_value
                    binding!!.edtCommentSend.setText(comment_value)
                    Log.d("테스트로그","4")
                }

                Constans.COMMENT_SUB_MORE_EDIT -> {
                    viewModel.commentInputType.value = 1
                    keyboardFocus(comment_value)
                    viewModel.cs_seq.value = seq.toString()
                    viewModel.commentContents.value = comment_value
                    binding!!.edtCommentSend.setText(comment_value)
                    viewModel.tagNickname.value = "@" + u_nickname
                    Log.d("테스트로그","3")
                }

                Constans.POST_MORE_NOTI -> {
                    blockProfileDialog = PoliceDialog(this,this,Constans.DIALOG_POST_POLICE_CALL,viewModel.anonymousForumDeepPostData.value?.b_seq!!)
                    blockProfileDialog!!.show(this.supportFragmentManager,"")
                }

                Constans.COMMENT_MORE_NOTI -> {
                    blockProfileDialog = PoliceDialog(this,this,Constans.DIALOG_COMMENT_NOTI_CALL,seq)
                    blockProfileDialog!!.show(this.supportFragmentManager,"")
                }

                Constans.COMMENT_SUB_MORE_NOTI-> {
                    blockProfileDialog = PoliceDialog(this,this,Constans.DIALOG_COMMENT_SUB_NOTI_CALL,seq)
                    blockProfileDialog!!.show(this.supportFragmentManager,"")
                }

                Constans.DIALOG_POST_POLICE_CALL -> {
                    viewModel.PoliceInsert("BOARD",comment_value,seq)

                }

                Constans.DIALOG_COMMENT_NOTI_CALL -> {
                    viewModel.PoliceInsert("COMMENT",comment_value,seq)

                }

                Constans.DIALOG_COMMENT_SUB_NOTI_CALL -> {
                    viewModel.PoliceInsert("SUBCOMMENT",comment_value,seq)

                }


                Constans.POST_MORE_BLOCK -> {
                 blockPostDialog= BlockDialog(this,this,Constans.DIALOG_POST_BLOCK,viewModel.anonymousForumDeepPostData.value?.b_seq!!)
                 blockPostDialog!!.show(this.supportFragmentManager,"")

//                    viewModel.PostBlockInsert("BOARD",viewModel.anonymousForumDeepPostData.value?.b_contents!!)
                }

                Constans.COMMENT_MORE_BLOCK -> {
                    blockPostDialog= BlockDialog(this,this,Constans.DIALOG_COMMENT_BLOCK_CALL,seq)
                    blockPostDialog!!.show(this.supportFragmentManager,"")

//                    viewModel.PostBlockInsert("BOARD",viewModel.anonymousForumDeepPostData.value?.b_contents!!)

                }

                Constans.COMMENT_SUB_MORE_BLOCK-> {
                    blockPostDialog= BlockDialog(this,this,Constans.DIALOG_COMMENT_SUB_BLOCK_CALL,seq)
                    blockPostDialog!!.show(this.supportFragmentManager,"")

//                    viewModel.PostBlockInsert("BOARD",viewModel.anonymousForumDeepPostData.value?.b_contents!!)

                }

                Constans.DIALOG_POST_BLOCK -> {
                    viewModel.PostBlockInsert("BOARD","",seq)

                }
                Constans.DIALOG_COMMENT_BLOCK_CALL -> {
                    viewModel.PostBlockInsert("COMMENT","",seq)

                }

                Constans.DIALOG_COMMENT_SUB_BLOCK_CALL -> {
                    viewModel.PostBlockInsert("SUBCOMMENT","",seq)

                }





                //벤확인
                Constans.DIALOG_POST_BLOCK_CHECK -> {
                    finish()
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
    override fun onVisibilityChanged(visible: Boolean) {
        if(!visible){
            if (viewModel.commentInputType.value == 1) {
                viewModel.commentInputType.value = 0
                binding!!.edtCommentSend.text = null
                viewModel.tagNickname.value = null
                Log.d("테스트로그","2")

            }
            if (binding!!.edtCommentSend.text!!.isEmpty()){
                viewModel.tagNickname.value = null
                Log.d("테스트로그","1")

            }
        }else{
            Log.d("", viewModel.scrollY.value.toString())
            binding.scroll.scrollY = viewModel.scrollY.value!!
        }


    }
    fun showMiniProfile(){
        var miniprodialog = MiniProfileDialog(this,viewModel!!.my_u_seq.value!!.toString(),viewModel.select_seq.value.toString(),viewModel.select_anonymousNickname.value.toString()){ isprofile->

            var i  = Intent(this, LikeSendActivity::class.java)
            i.putExtra("sub_seq",viewModel.select_seq.value.toString())
            i.putExtra("type","익명게시판")

            startActivity(i)
        }
        miniprodialog.show(supportFragmentManager,"")
    }
}
