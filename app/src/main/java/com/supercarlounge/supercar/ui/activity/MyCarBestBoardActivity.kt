package com.supercarlounge.supercar.ui.activity


import ViewProfileDialog
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.supercarlounge.supercar.Constans
import com.supercarlounge.supercar.DialogEvent
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.MyCarBestBoardAdapter
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.data.DriveAwayData
import com.supercarlounge.supercar.data.MyCarBoardData
import com.supercarlounge.supercar.databinding.ActivityMyCarBestBoardBinding
import com.supercarlounge.supercar.dialog.DrivePassPayDialog
import com.supercarlounge.supercar.dialog.ProfileLikeFriendDialog
import com.supercarlounge.supercar.dialog.TwoButtonDialog
import com.supercarlounge.supercar.enumset.*
import com.supercarlounge.supercar.viewmodel.MyCarBestBoardViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class MyCarBestBoardActivity : BaseActivitiy(), DialogEvent {

    private lateinit var binding: ActivityMyCarBestBoardBinding
    private lateinit var viewModel: MyCarBestBoardViewModel
    private lateinit var application: MainApplication
    private var drivepop1 : ProfileLikeFriendDialog? =null
    private var viewProfileDialog :ViewProfileDialog? = null
    private var fm: FragmentManager? = null
    var myCarBoardAdapter: MyCarBestBoardAdapter? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        application = applicationContext as MainApplication
        viewModel = ViewModelProvider(this).get(MyCarBestBoardViewModel::class.java)
        binding = ActivityMyCarBestBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel
        viewModel.my_user_data.value = application.userData



        fm = supportFragmentManager


        clickListeners()
        observes()
        setAdapters()
        viewModel.getStartDate()
       // setNowDay()

//
//        if (viewModel.u_seq.value != viewModel.my_u_seq.value){
//            viewModel.boardViewsUpdate()
////            binding.llViewList.visibility = View.GONE
//        }else{
//
////            binding.llViewList.visibility = View.VISIBLE
//        }
    }


    override fun onResume() {
        super.onResume()

    }






    @RequiresApi(Build.VERSION_CODES.O)
    private fun setAdapters() {

        myCarBoardAdapter = MyCarBestBoardAdapter(this, arrayListOf(), viewModel) { data, tag, position ->
             if (application.quick_view.equals("퀵유저")){
//                 ShowQuickUserDialog()
             }else {


                 if (tag == "기본"){
                     var i =Intent(this,MyCarBoardActivity::class.java)
                     //i.putExtra("data",data)
                     i.putExtra("mmi_seq",data.mmi_seq)
                     i.putExtra("position",position)
                     i.putExtra("type","기본")
                     resultLauncher.launch(i)
                     overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                 }else if (tag == "드라이브 신청"){
                     if(application!!.CheckCompany() == false) {
                         viewModel!!.select_sub_seq.value = data.u_seq
                         viewModel!!.select_mmi_seq.value = data.mmi_seq
                         viewModel!!.select_rn_nick.value = data.mrc_nickname
                         viewModel!!.boardViewCheckClick()
                     }else{
                         viewModel.toast.value = getString(R.string.company_profile_open_text)
                     }
                 }



             }

            }

        binding?.rvMyCaBoard?.adapter = myCarBoardAdapter

        binding?.rvMyCaBoard?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

//        binding?.rvAnonymousForumNoti?.adapter = nAdapter



//            AnonymousForumNotiAutoSlide()

    }

    var listener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
//            var lastposition =
//                (binding?.rvComment?.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
//            var itemcount = cAdapter!!.itemCount
//            Log.d("getDrive : last", "" + itemcount)
//            Log.d("getDrive : itemcount", "" + lastposition)
//            if (itemcount == (lastposition + 1)) {
//                var totalc = viewModel!!.totalCount.value!!
//                if (itemcount < totalc&& totalc !=0) {
//                    if (itemcount < viewModel!!.totalCount.value!!) {
//                        viewModel.page.value =  viewModel.page.value?.plus(1)
//                            viewModel!!.getAnonymousForumPostScroll(viewModel!!.filterArray[viewModel!!.viewType.value!!])
//                            Log.d("getAnonymousForumPost", "getAnonymousForumPostScroll")
//
//                    }
//                }
//        }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
        }
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun observes() {


        viewModel.myCarBoardLists.observe(this, Observer {
            if (it != null){
                if (it.size >0){
                    if (it[0].getTitle().isNotEmpty()){
                        viewModel.tv1.value = "1. ${it[0].getTitle()}"
                    }else{
                        viewModel.tv1.value = "인기글로 선정 된 게시글이 없습니다."
                    }
                    if (it.size >1){
                        if (it[1].getTitle().isNotEmpty()){
                            viewModel.tv2.value = "2. ${it[1].getTitle()}"
                        }else{
                            viewModel.tv2.value = "인기글로 선정 된 게시글이 없습니다."
                        }
                        if (it.size >2){
                            if (it[2].getTitle().isNotEmpty()){
                                viewModel.tv3.value = "3. ${it[2].getTitle()}"
                            }else{
                                viewModel.tv3.value = "인기글로 선정 된 게시글이 없습니다."
                            }
                        }else{
                            viewModel.tv3.value = "인기글로 선정 된 게시글이 없습니다."
                        }
                    }else{
                        viewModel.tv2.value = "인기글로 선정 된 게시글이 없습니다."
                        viewModel.tv3.value = "인기글로 선정 된 게시글이 없습니다."
                    }

                }else{
                    viewModel.tv1.value = "인기글로 선정 된 게시글이 없습니다."
                    viewModel.tv2.value = "인기글로 선정 된 게시글이 없습니다."
                    viewModel.tv3.value = "인기글로 선정 된 게시글이 없습니다."
                }

            }
        })
        viewModel.selectDate.observe(this, Observer {
            if (it.isNotEmpty()){
                viewModel?.getMyCarBestBoardList(true)
            }
        })
        viewModel.toast.observe(this, Observer {
            if(!it.isNullOrEmpty()){
                Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.intent.observe(this, Observer {


                var i =Intent(this,MyCarBoardActivity::class.java)
                var data = viewModel.myCarBoardLists.value?.get(it)
                i.putExtra("mmi_seq",data!!.mmi_seq)
//                i.putExtra("data",data)
                i.putExtra("position",it)
                i.putExtra("type","기본")
                resultLauncher.launch(i)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)



        })
        viewModel.startDateString.observe(this, Observer {
            if (it.isNotEmpty()){
                val date: String = it
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val mDate = sdf.parse(date)
                val calendar: Calendar = GregorianCalendar()
                calendar.time = mDate
                val year = calendar[Calendar.YEAR]
                val month = calendar[Calendar.MONTH] + 1
                val day = calendar[Calendar.DAY_OF_MONTH]

                val startDate: LocalDate = LocalDate.of(year,month,day)
                viewModel.startDate.value = startDate.toString()
                viewModel.startYear.value = startDate.year
                viewModel.startMonth.value = startDate.monthValue
            }

        })

        viewModel!!.toast.observe(this, Observer {
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
                    drivePassPayDialog!!.show(fm!!, "")
                } else {
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                }

            }
        })
        viewModel!!.boardCheckViewStatus.observe(this, Observer {
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
                    val i = Intent(this, ProfileCompanyActivity::class.java)
                    i.putExtra("sub_seq", viewModel.select_sub_seq.value)
                    startActivity(i)
                }
                BoardCheckViewStatus.NOT_FRIEND_POPUP ->{

                    viewProfileDialog = ViewProfileDialog(this) { isfinish ->
                        if (isfinish){
                            viewModel!!.ProfileRead(3,"")
                        }
                    }
                    viewProfileDialog!!.show(fm!!, "")
                }
                BoardCheckViewStatus.COMPANY_PROFILE->{
                    val i = Intent(this, ProfileCompanyActivity::class.java)
                    i.putExtra("sub_seq", viewModel.select_sub_seq.value)
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
                            this,
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
        viewModel.isnotheart.observe(this, Observer {
            if(it){
                var notheart  = TwoButtonDialog(this, Constans.DIALOG_LACK_HEART,this, Constans.DIALOG_LACK_HEART)
                notheart!!.show(supportFragmentManager,"")
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun clickListeners() {
        binding.ivWriting.setOnClickListener {
            var i = Intent(this, MyCarBoardWritingActivity::class.java)
            resultLauncher.launch(i)

        }
        binding.ivBack.setOnClickListener {
            finish()
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
                    viewModel.viewtype.value = type
                    when(type){
                        "글쓰기"->{

                            viewModel.getStartDate()
                            viewModel.getMyCarBestBoardList(true)

                        }
                        "글수정"->{

                            if (viewModel.myCarBoardLists.value?.size!! > position){
                                if (data != null) {
                                    viewModel.myCarBoardLists.value!![position] = data
                                    viewModel.position.value = position
                                    viewModel.myCarBoardLists.value = viewModel.myCarBoardLists.value
                                    myCarBoardAdapter?.setChangeData(data,position!!)
                                }
                            }


                        }
                        "북마크","좋아요"->{
                            var like  = idata?.getStringExtra("like")
                            var bookmark  = idata?.getStringExtra("bookmark")
                            var  likeCount = idata?.getIntExtra("likecount",0)
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

                        else -> {}
                    }

                }

            }
        }


    override fun finish() {

        var intent = Intent()
        intent.putExtra("position", viewModel.position.value)
        if (viewModel.viewtype.value=="글수정"){
            intent.putExtra("data", viewModel.myCarBoardLists.value?.get(viewModel.position.value!!))
            intent.putExtra("type", "글수정")
        }



        setResult(Activity.RESULT_OK, intent)
        super.finish()

        overridePendingTransition(
            com.supercarlounge.supercar.R.anim.fadein,
            com.supercarlounge.supercar.R.anim.fadeout
        )
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

    override fun okEvent(
        type: Int,
        okAndCancel: Boolean,
        comment_value: String,
        seq: String,
        u_nickname: String
    ) {

    }


}
