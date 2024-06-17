package com.supercarlounge.supercar.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.core.widget.NestedScrollView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.supercarlounge.supercar.*
import com.supercarlounge.supercar.adapter.OwnerAndPassengerProfileSugesstAdapter
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.customview.MiddleItemFinder
import com.supercarlounge.supercar.customview.SuggestItemDeco
import com.supercarlounge.supercar.data.ProfileSuggestionData
import com.supercarlounge.supercar.data.SlidePidData
import com.supercarlounge.supercar.databinding.*
import com.supercarlounge.supercar.dialog.*
import com.supercarlounge.supercar.enumset.*
import com.supercarlounge.supercar.viewmodel.OwnerAndPassengerViewModel



class OwnerAndPassengerActivity : BaseActivitiy(), DialogEvent {
    private lateinit var binding: AcitivtyOwnerAndPassengerBinding
    private lateinit var viewModel: OwnerAndPassengerViewModel
    var profileSuggestionAdapter: OwnerAndPassengerProfileSugesstAdapter? = null
    var application: MainApplication? = null
    var warningDialog: WarningDialog? = null
    var mnewState = 0
    var isIntentMypicks = 0
    var nowposi = 0
    var menupopup: PopupWindow? = null
    private var quickUserDialog: QuickUserDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OwnerAndPassengerViewModel::class.java)
        binding = AcitivtyOwnerAndPassengerBinding.inflate(layoutInflater)
        binding!!.setLifecycleOwner(this)
        binding.viewModel = viewModel
        setContentView(binding.root)
        application = applicationContext as MainApplication



        setadapter()
        setclicks()
        observers()
        setBackgrund()
        setpid(application?.userData?.u_gender.toString())
        viewModel.my_seq.value = application?.userData?.u_seq.toString()
        viewModel!!.iscompany.value = application!!.CheckCompany()

        var i = intent
        viewModel.profilesuggest.value =
            i.getSerializableExtra("list") as ArrayList<ProfileSuggestionData>
        viewModel.index.value = i.getIntExtra("index", 0)
        viewModel.selectproindex.value = i.getIntExtra("index", 0)
        viewModel.nowPosition.value = i.getIntExtra("posi", 0)
        isIntentMypicks = i.getIntExtra("mypicks", 0)
        Log.d("인텐트값", isIntentMypicks.toString())


    }

    fun setpid(sex: String) {
        var list: ArrayList<SlidePidData> = arrayListOf()

        if (sex.equals("MALE")) {
            list.add(
                SlidePidData(
                    PidDialogType.FRIEND,
                    false,
                    "나와 맞는 짝꿍 찾기! \uD83D\uDC69\u200D❤️\u200D\uD83D\uDC68",
                    "짝꿍과 함께 신나는 드라이브!"
                )
            )
            list.add(
                SlidePidData(
                    PidDialogType.MAN_GLAMMER,
                    false,
                    "글래머,볼륨있는 여성 회원 만나기 \uD83D\uDD25",
                    "이상형과 함께하는 설레는 드라이브!"
                )
            )
            list.add(
                SlidePidData(
                    PidDialogType.NEW_USER,
                    false,
                    "최근 가입한 신입회원 만나기 \uD83D\uDC25",
                    "신입 회원과 즐거운 드라이브!"
                )
            )
            list.add(
                SlidePidData(
                    PidDialogType.AGE_20,
                    false,
                    "20대 드라이버,패신저 만나기 \uD83D\uDC66\uD83D\uDC67",
                    "PASS인증을 통한 실제 나이를 인증한 회원!"
                )
            )
            list.add(
                SlidePidData(
                    PidDialogType.LIKE_MUSIC,
                    false,
                    "음악취향 비슷한 상대 만나기 \uD83C\uDFB6",
                    "음악취향도 같으면 더 신나는 드라이브!"
                )
            )
            list.add(
                SlidePidData(
                    PidDialogType.SUPER_USE,
                    false,
                    "슈퍼카 오너 만나기 \uD83D\uDE98",
                    "본인 소유의 차량 인증을 한 슈퍼카 오너!"
                )
            )
        } else {
            list.add(
                SlidePidData(
                    PidDialogType.FRIEND,
                    false,
                    "나와 맞는 짝꿍 찾기! \uD83D\uDC69\u200D❤️\u200D\uD83D\uDC68",
                    "짝꿍과 함께 신나는 드라이브!"
                )
            )
            list.add(
                SlidePidData(
                    PidDialogType.SUPER_USE,
                    false,
                    "슈퍼카 오너 만나기 \uD83D\uDE98",
                    "본인 소유의 차량 인증을 한 슈퍼카 오너!"
                )
            )
            list.add(
                SlidePidData(
                    PidDialogType.AGE_20,
                    false,
                    "20대 드라이버,패신저 만나기 \uD83D\uDC66\uD83D\uDC67",
                    "PASS인증을 통한 실제 나이를 인증한 회원!"
                )
            )
            list.add(
                SlidePidData(
                    PidDialogType.FM_10PER,
                    false,
                    "상위 10% 이성 회원 만나기 \uD83D\uDE0E",
                    "호감을 제일 많이 받은 회원!"
                )
            )
            list.add(
                SlidePidData(
                    PidDialogType.LIKE_MUSIC,
                    false,
                    "음악취향 비슷한 상대 만나기 \uD83C\uDFB6",
                    "음악취향도 같으면 더 신나는 드라이브!"
                )
            )
            list.add(
                SlidePidData(
                    PidDialogType.NEW_USER,
                    false,
                    "최근 가입한 신입회원 만나기 \uD83D\uDC25",
                    "신입 회원과 즐거운 드라이브!"
                )
            )
        }
        viewModel.pidlist.value = list
    }

    private fun setBackgrund() {
        var d = GradientDrawable(
            this.getColor(R.color.cooper_gradi_st),
            this.getColor(R.color.cooper_gradi_ed),
            this.getColor(
                R.color.white_4
            ),
            this.getColor(R.color.white_4),
            2f,
            10f,
            GradientDrawable.Direction.TL_BR,
            GradientDrawable.Direction.TL_BR
        )
        binding!!.ll1.background = d
        binding!!.ll2.background = d
        binding!!.ll3.background = d
        binding!!.ll4.background = d
        binding!!.ll5.background = d
        binding!!.ll6.background = d

    }

    override fun onResume() {
        super.onResume()
        viewModel.select_index.value = application?.select_index
        viewModel.CheckAlim("")
        viewModel.CheckDrivePass()

        if(isIntentMypicks == 1){
            binding.nsv.post{binding.nsv.scrollY = 1700}
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun observers() {
        viewModel.toast.observe(this, Observer { toast ->
            if (!toast.isNullOrEmpty()) {
                if (toast.equals("하트가 부족합니다. 하트를 충전해주세요.")) {
                    var drivePassPayDialog = DrivePassPayDialog(
                        this,
                        DrivePassDialogStatus.NOTHEART,
                        "",
                        0
                    ) { type, isok ->
                        if (isok) {
                            val i = Intent(this, HeartsShopActivity::class.java)
                            startActivity(i)
                            overridePendingTransition(
                                android.R.anim.fade_in,
                                android.R.anim.fade_out
                            )
                        }

                    }
                    drivePassPayDialog!!.show(supportFragmentManager, "")
                } else {
                    Toast.makeText(this, toast, Toast.LENGTH_SHORT).show()
                }

            }
        })
        viewModel.index?.observe(this, Observer { it ->

            if (viewModel.profilesuggest.value?.size!! > it) {
//                var list = stringToWords(viewModel.profilesuggest.value!![it].u_charm.toString())

                viewModel.u_charm.value = setCharm(viewModel.profilesuggest.value!![it].u_charm.toString())
                Log.d("2024체크", "1 ======== ${viewModel.index.value}")
                Log.d("데이터 로그", it.toString())

            }
        })
        viewModel.eventRight.observe(this, Observer {
            if (it) {
                Log.d("인덱스 체크1",  viewModel.index.value.toString())
                if (viewModel.profilesuggest.value?.size!! - 1 > viewModel.index.value!!) {
                    var index = viewModel.index.value
                    index = viewModel.index.value!! + 1
                    viewModel.index.value = index
                } else {
                    viewModel.index.value = 0
                }

            }
        })
        viewModel.eventLeft.observe(this, Observer {
            if (it) {
                Log.d("인덱스 체크2",  viewModel.index.value.toString())
                if (0 < viewModel.index.value!!) {
                    var index = viewModel.index.value
                    index = viewModel.index.value!! - 1
                    viewModel.index.value = index
                } else {
                    var size = viewModel.profilesuggest.value?.size!! - 1

                    viewModel.index.value = size
                }
            }
        })

        viewModel.eventCenter.observe(this, Observer {
            if (it) {
                if (!application!!.CheckCompany()) {
                    var data = viewModel.profilesuggest.value?.get(viewModel.index.value!!)
                    var slist = application!!.select_index
                    var check = false
                    if (slist!!.contains(data?.u_seq.toString())) {
                        check = true
                    }
                    if (!data?.u_image.isNullOrEmpty()) {
                        if (check) {
                            val i = Intent(this, ProfileActivity::class.java)
                            i.putExtra("sub_seq", data?.u_seq.toString())
                            startActivity(i)
                            overridePendingTransition(
                                android.R.anim.fade_in,
                                android.R.anim.fade_out
                            )
                        } else {
                            var pid: RecDriveDialogType? = RecDriveDialogType.NOT_REC_DIRVE
                            if (application!!.userData?.u_feed.equals("1") && data?.u_seq!! > 0) {
                                if (viewModel.isDrivePass.value!! && viewModel.isDrivePassCount.value!! >= 3) {
                                    pid = RecDriveDialogType.PROFILE_NOTPASS_OPEN
                                }else{
                                    pid = RecDriveDialogType.PROFILE_OPEN
                                }
                            }
                            var did = RecDriveDialog(this, pid!!) { type, okcancel ->
                                if (type == RecDriveDialogType.PROFILE_OPEN|| type == RecDriveDialogType.PROFILE_NOTPASS_OPEN) {
                                    if (okcancel) {
                                        viewModel.RecProfile(data!!.u_seq.toString(), viewModel.isDrivePass.value!!)
                                        //viewModel.CheckDrivePass(data)

                                    }
                                }
                            }
                            did.show(supportFragmentManager, "")
                        }

                    } else {
                        warningDialog = WarningDialog(this, 0)
                        warningDialog!!.show(supportFragmentManager, "")
                    }
                } else {
                    viewModel.toast.value = this.getString(R.string.company_profile_open_text)
                }

            }
        })
        viewModel.isnotheart.observe(this, Observer {
            if (it) {
                var notheart = TwoButtonDialog(
                    this,
                    Constans.DIALOG_LACK_HEART,
                    this,
                    Constans.DIALOG_LACK_HEART
                )
                notheart!!.show(supportFragmentManager, "")
            }
        })
        viewModel.open_profile_seq.observe(this, Observer { seq ->
            if (!seq.isNullOrEmpty()) {



                val i = Intent(this, ProfileActivity::class.java)
                i.putExtra("sub_seq", seq)
                Log.d("2024체크", "2 ======== ${viewModel.index.value}")
                startActivity(i)
                application?.setselectdata(seq)


                var handler = Handler()
                var runnable = Runnable {
                    run {
                        profileSuggestionAdapter?.notifyDataSetChanged()
                    }
                }
                handler.postDelayed(runnable, 500)


//                profileSuggestionAdapter?.notifyDataSetChanged()
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

            }
        })

        viewModel.eventPid.observe(this, Observer {

            Log.d("오너패신저","마이픽스 클릭")

            var istip = viewModel.pidlist.value?.get(it)?.istip
            var feeddata = viewModel.pidlist.value?.get(it)
            if (feeddata != null) {
                if (istip == false) {
                    if (application!!.CheckCompany() == false) {
                        var rectype: RecDriveDialogType? = RecDriveDialogType.NOT_REC_DIRVE
                        if (application!!.userData?.u_feed.equals("1")) {
                            var piddialog = PidDialog(this, feeddata?.type!!) { event, isok ->
                                if (isok) {
                                    viewModel.FeedProfileSearch(feeddata?.type!!)
                                }
                            }
                            piddialog.show(supportFragmentManager, "")
                        } else {
                            var did = RecDriveDialog(this, rectype!!) { type, okcancel ->
                                if (type == RecDriveDialogType.PROFILE_OPEN) {

                                }
                            }
                            did.show(supportFragmentManager, "")
                        }
                    } else {
                        viewModel.toast.value = this.getString(R.string.company_profile_open_text)
                    }
                }
            }
        })

    }

    fun setCharm(s: String): String {
        var x = ""
        var list = s.substring(1, s.length - 1).split(",").toTypedArray()
        for ((count, i) in list.withIndex()) {

            when (count) {
                0 -> viewModel.charm1.value = removeDot(i)
                1 -> viewModel.charm2.value = removeDot(i)
                2 -> viewModel.charm3.value = removeDot(i)
                else -> {}
            }
        }


        Log.d("매력", x)
        return x
    }

    fun removeDot(str: String): String {
        val re = "^\"|\"$".toRegex()
        return str.replace(re, "")
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setadapter() {
        profileSuggestionAdapter = OwnerAndPassengerProfileSugesstAdapter(
//            deviceW,
//            deviceH,
            this,
            0,
            arrayListOf(), viewModel
        ) { data, position, width ->
            nowposi = position
            var listsize = profileSuggestionAdapter!!.datalist.size
            var selindex = position % listsize
            Log.d("사이즈1", position.toString())
            Log.d("사이즈2", listsize.toString())
            if (viewModel.selectproindex.value == selindex) {
                if (application!!.CheckCompany() == false) {
                    var slist = application?.select_index
                    var check = false
                    if (slist!!.contains(data.u_seq.toString())) {
                        check = true
                    }
                    if (!data.u_image.isNullOrEmpty()) {
                        if (check) {
                            val i = Intent(this, ProfileActivity::class.java)
                            i.putExtra("sub_seq", data.u_seq.toString())
                            startActivity(i)
                            overridePendingTransition(
                                android.R.anim.fade_in,
                                android.R.anim.fade_out
                            )
                        } else {
                            var pid: RecDriveDialogType? = RecDriveDialogType.NOT_REC_DIRVE
                            if (application!!.userData?.u_feed.equals("1") && data.u_seq > 0) {
                                if (viewModel.isDrivePass.value!! && viewModel.isDrivePassCount.value!! >= 3) {
                                    pid = RecDriveDialogType.PROFILE_NOTPASS_OPEN
                                }else{
                                    pid = RecDriveDialogType.PROFILE_OPEN
                                }
                            }
                            var did = RecDriveDialog(this, pid!!) { type, okcancel ->
                                if (type == RecDriveDialogType.PROFILE_OPEN) {
                                    if (okcancel) {
                                        viewModel.RecProfile(data.u_seq.toString(),viewModel.isDrivePass.value!!)

                                        //viewModel.CheckDrivePass(data)

                                    }
                                }
                            }
                            did.show(supportFragmentManager, "")
                        }

                    } else {
                        warningDialog = WarningDialog(this, 0)
                        warningDialog!!.show(supportFragmentManager, "")
                    }
                } else {
                    viewModel.toast.value = getString(R.string.company_profile_open_text)
                }

            } else {
                viewModel.selectprodata.value = data
                viewModel.selectproindex.value = selindex
                viewModel.index.value = viewModel.selectproindex.value
                val offset = (binding!!.rvProfilesugesst.width / 2 - width / 2)
                var mnan = (binding!!.rvProfilesugesst.layoutManager!! as LinearLayoutManager)
                mnan.scrollToPositionWithOffset(position, offset)
            }
        }

        binding!!.rvProfilesugesst.adapter = profileSuggestionAdapter
        binding!!.rvProfilesugesst.addItemDecoration(SuggestItemDeco(5))
        binding!!.rvProfilesugesst.apply {
            itemAnimator = null
        }

        //var layoutmanager =  CenterZoomLayoutManager(context,RecyclerView.HORIZONTAL,false,profileSuggestionAdapter!!)
        var layoutmanager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding!!.rvProfilesugesst.layoutManager = layoutmanager
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding!!.rvProfilesugesst)
        binding!!.rvProfilesugesst.addOnScrollListener(

            MiddleItemFinder(
                this,
                layoutmanager,
                object : MiddleItemFinder.MiddleItemCallback {

                    override fun scrollFinished(middleElement: Int) {
                        var listsize = profileSuggestionAdapter!!.datalist.size
                        if (listsize != 0) {
                            nowposi = middleElement
                            viewModel.selectproindex.value = middleElement % listsize
                            Log.d("선택인덱스", viewModel.selectproindex.value.toString())
                            viewModel.selectprodata.value =
                                profileSuggestionAdapter!!.datalist.get(middleElement % profileSuggestionAdapter!!.datalist.size)
                            viewModel.index.value = viewModel.selectproindex.value
                        }
                    }


                },
                RecyclerView.SCROLL_STATE_IDLE
            )

        )
        binding!!.rvProfilesugesst.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

//                if (mnewState == 0) {
//                    var scrollPosition= (binding!!.rvProfilesugesst.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
//                    val offset = (binding!!.rvProfilesugesst.width / 2 - binding!!.rvProfilesugesst.layoutManager!!.width / 2)
//                    var mnan = (binding!!.rvProfilesugesst.layoutManager!! as LinearLayoutManager)
//                    mnan.scrollToPositionWithOffset(scrollPosition, offset)
//                }

            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                Log.d("스크롤", newState.toString())
                viewModel.scrolling.value = newState != 0


                mnewState = newState


//                    var position =
//                        (binding!!.rvProfilesugesst.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
//                    var width =
//                        (binding!!.rvProfilesugesst.layoutManager as LinearLayoutManager).width
//                    val offset = (binding!!.rvProfilesugesst.width / 2 - width / 2)
//                    var mnan = (binding!!.rvProfilesugesst.layoutManager!! as LinearLayoutManager)
//                    val position = (binding!!.rvProfilesugesst.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
//                    binding!!.rvProfilesugesst.scrollToPosition(position)

//                    mnan.scrollToPositionWithOffset(position, 100)

            }
        })

//        binding.rvProfilesugesst.smoothScrollToPosition( viewModel.nowPosition.value!!)
    }

    fun setclicks() {
        binding!!.ivBack.setOnClickListener {
            finish()
        }
        binding!!.btnPicQuestion.setOnClickListener { view ->
            var dim = view.resources.getDimensionPixelSize(R.dimen.dimen_40)
            var start = -540 - dim
            setpop(
                this,
                binding!!.btnPicQuestion,
                "내가 원하는 드라이브 상대를 찾아보세요!",
                start,
                DialogBallonType.MAIN_RIGHT
            )
        }
        binding!!.btnRecQuestion.setOnClickListener { view ->
            var dim =
                view.resources.getDimensionPixelSize(R.dimen.dimen_20)
            var start = -500
            setpop(
                this,
                binding!!.btnRecQuestion,
                "추천 상대를 볼 수 있습니다!\n매일 오전 10시에 갱신됩니다.",
                start,
                DialogBallonType.MAIN_RIGHT
            )
        }
        binding!!.ivHeartShop.setOnClickListener {
            if (application?.quick_view.equals("퀵유저")) {
                ShowQuickUserDialog()
            } else {
                val i = Intent(this, HeartsShopActivity::class.java)
                startActivity(i)
                //  overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

            }
        }

        binding!!.ivVipNotice.setOnClickListener {
            if (application?.quick_view.equals("퀵유저")) {
                ShowQuickUserDialog()
            } else {
                val i = Intent(this, AlimActivity::class.java)
                startActivity(i)
                // overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }
    }

    override fun finish() {
        var i = Intent()
        i.putExtra("posi", nowposi)
        i.putExtra("list", viewModel.profilesuggest.value)
        this.setResult(Activity.RESULT_OK, i)
        super.finish()




    }

    fun setpop(
        con: Context,
        view: View,
        question: String,
        paddingx: Int,
        type: DialogBallonType
    ) {

        val originalPos = IntArray(2)
        view.getLocationInWindow(originalPos)
        val x = originalPos[0]
        val y = originalPos[1]
        val metrisc: DisplayMetrics = resources.displayMetrics
        val displayPH = metrisc.heightPixels
        var radiusH = displayPH / 2
        var maginH = displayPH / 15
        var h = radiusH - maginH
        var yy = y - h
        if (type == DialogBallonType.RIGHT) {
            var popbinding: DialogRightQuestionBallonBinding = DataBindingUtil.inflate(
                LayoutInflater.from(con),
                R.layout.dialog_right_question_ballon,
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
            Log.d("POPOFFSET", "" + paddingx)
//                menupopup.showAtLocation(view, Gravity.CENTER, 99, -30);
            Log.d("뷰테스트", yy.toString())
            menupopup!!.showAtLocation(view, Gravity.CENTER, 0, yy)
            PopupAutoClose()
        } else if (type == DialogBallonType.LEFT) {
            var popbinding: DialogLeftQuestionBallonBinding = DataBindingUtil.inflate(
                LayoutInflater.from(con),
                R.layout.dialog_left_question_ballon,
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
            Log.d("뷰테스트", yy.toString())
            menupopup!!.showAtLocation(view, Gravity.CENTER, 0, yy)
            PopupAutoClose()
        } else if (type == DialogBallonType.CENTER) {
            var popbinding: DialogCenterQuestionBallonBinding = DataBindingUtil.inflate(
                LayoutInflater.from(con),
                R.layout.dialog_center_question_ballon,
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
            Log.d("뷰테스트", yy.toString())
            menupopup!!.showAtLocation(view, Gravity.CENTER, 0, yy)
            PopupAutoClose()

        } else if (type == DialogBallonType.MAIN_RIGHT) {
            var popbinding: DialogHelpRightQuestionBallonBinding = DataBindingUtil.inflate(
                LayoutInflater.from(con),
                R.layout.dialog_help_right_question_ballon,
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

            menupopup!!.showAsDropDown(view, 0, -30, Gravity.CENTER)
            PopupAutoClose()
        }

    }

    private fun PopupAutoClose() {

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
        val handler = Handler()
        val mHandler = Handler(Looper.getMainLooper())

        mHandler.removeCallbacksAndMessages(null);
        mHandler.postDelayed({
            val msg = handler.obtainMessage()
            handler.sendMessage(msg)
        }, 3000)

//            val mTimer = Handler()
//
//        mTimer.schedule(mTimerTask, 500, 3000)

    }

    companion object {
        @BindingAdapter("entriesset")
        @JvmStatic
        fun Spinner.setEntries(entries: ArrayList<String>?) {
            entries?.run {
                val arrayAdapter =
                    ArrayAdapter(context, R.layout.item_spinner_set, R.id.tv_spin, entries)
                arrayAdapter.setDropDownViewResource(R.layout.item_spinner_set)
                adapter = arrayAdapter
            }
        }

        @InverseBindingAdapter(attribute = "selectedValue", event = "selectedValueAttrChanged")
        @JvmStatic
        fun Spinner.getSelectedValue(): Any? {
            return selectedItem
        }

        @JvmStatic
// 위에것이 실행되고 아래의 bindingapdater가 실행된다.
        @BindingAdapter("selectedValueAttrChanged")
        fun Spinner.setInverseBindingListener(inverseBindingListener: InverseBindingListener?) {

            inverseBindingListener?.run {
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                    ) {
                        if (tag != position) {
                            inverseBindingListener.onChange()
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {}
                }
            }
        }

        @JvmStatic
        @BindingAdapter("selectedValue")
        fun Spinner.setSelectedValue(selectedValue: Any?) {
            adapter?.run {
                val position =
                    (adapter as ArrayAdapter<Any>).getPosition(selectedValue)
                setSelection(position, false)
                tag = position
            }
        }
    }

    override fun okEvent(
        type: Int,
        okAndCancel: Boolean,
        comment_value: String,
        seq: String,
        u_nickname: String
    ) {
        TODO("Not yet implemented")
    }

    private fun ShowQuickUserDialog() {
        quickUserDialog =
            QuickUserDialog(this, QuickTypeStatus.QUICK_WARNING) { type, okcancel ->
                if (okcancel) {

                    quickUserDialog!!.dismiss()
                    val i = Intent(this, JoinActivity::class.java)

                    i.putExtra("u_name", application?.userBasicData?.u_name)
                    i.putExtra("u_gender", application?.userBasicData?.u_gender)
                    i.putExtra("u_birthday", application?.userBasicData?.u_birthday)
                    i.putExtra("u_phone", application?.userBasicData?.u_phone)

                    startActivity(i)
                } else {
                    quickUserDialog!!.dismiss()
                }
            }
        quickUserDialog!!.show(supportFragmentManager, "")
    }
}