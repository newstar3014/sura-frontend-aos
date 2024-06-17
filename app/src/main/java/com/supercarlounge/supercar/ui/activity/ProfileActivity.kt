package com.supercarlounge.supercar.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Rect
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.Constans
import com.supercarlounge.supercar.DialogEvent
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.VipDriveRankAdapter
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.data.ProfileMatchingData
import com.supercarlounge.supercar.databinding.ActivityProfileBinding
import com.supercarlounge.supercar.dialog.*
import com.supercarlounge.supercar.enumset.ConfirmStatus
import com.supercarlounge.supercar.enumset.DrivePassDialogStatus
import com.supercarlounge.supercar.enumset.ProfileStatus
import com.supercarlounge.supercar.viewmodel.ProfileViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue



@OptIn(ExperimentalTime::class)
class ProfileActivity : BaseActivitiy() , DialogEvent {
    private var job: Job? = null
    var application: MainApplication? = null
    private lateinit var binding: ActivityProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private var rAdapter: VipDriveRankAdapter? = null
    var numOpenDialog : NumOpenDialog? = null
    var twoButtonDialog : TwoButtonDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        application = applicationContext as MainApplication
        var mseq = application!!.userData!!.u_seq.toString()
        viewModel.my_seq.value = mseq
        viewModel.mynick.value = application!!.userData!!.u_nickname
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel

        val i = intent
        var sub_seq: String = i.getStringExtra("sub_seq")!!
        viewModel.type.value  = i.getStringExtra("type")
        viewModel.send_b_seq.value =  i.getStringExtra("send_b_seq")
        viewModel.given_nickname.value =  i.getStringExtra("nick")
        viewModel.position.value = intent.getIntExtra("position",0)
        Log.d("포지션", viewModel.position.value.toString())
        Log.d("타겟 seq", sub_seq)
        if (sub_seq != null) {
            viewModel.sub_seq.value = sub_seq
        }

        if (mseq == sub_seq) {
            binding.tvEdit.visibility = View.VISIBLE
//            binding.tvSafeNumber.visibility = View.GONE
        }
        if (viewModel.type.value.equals("열람오픈")){
            viewModel.btn_type.value = ProfileStatus.DIRECT_LIKE_SEND
        }


        binding!!.ivSub1.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewRedefinition()
                binding!!.ivSub1.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }


        })


        clickListeners()
        observes()
    }

    @OptIn(ExperimentalTime::class)
    override fun onResume() {
        super.onResume()
        var lat = "0"
        var lon = "0"
        if (application!!.mLastLocation != null) {
            lat = application!!.mLastLocation!!.latitude.toString()
            lon = application!!.mLastLocation!!.longitude.toString()
        }
        var mseq = viewModel.my_seq.value!!
        var sub_seq = viewModel.sub_seq.value!!
        Log.d("프로필 mseq", mseq)
        Log.d("프로필 sub_seq", sub_seq)
        if (!mseq.equals(sub_seq)) {
            viewModel.GetMatchingData(
                mseq, sub_seq,
                ""
            )
        }
        binding.viewBlack.visibility = View.GONE
        binding.viewBlackSub1.visibility = View.GONE
        binding.viewBlackSub2.visibility = View.GONE
        val mtValue = measureTimedValue {
            viewModel.GetProfile(mseq, lat, lon, sub_seq)
        }
        Log.d("시간체크",mtValue.toString())


        application?.setlocation()
        Log.d("CHECK_ONRESUME", " $lat  : $lon")
    }

    private fun viewRedefinition() {
        val layoutParams1 = binding.rlMain.layoutParams
        layoutParams1.width = binding.rlMain.width
        layoutParams1.height = binding.rlMain.width
        binding.rlMain.layoutParams = layoutParams1

        val layoutParams2 = binding.rlMain2.layoutParams
        layoutParams2.width = binding.rlMain2.width
        layoutParams2.height = binding.rlMain2.width
        binding.rlMain2.layoutParams = layoutParams2
        val layoutParams3 = binding.rlMain3.layoutParams
        layoutParams3.width = binding.rlMain3.width
        layoutParams3.height = binding.rlMain3.width
        binding.rlMain3.layoutParams = layoutParams3
        val layoutParams4 = binding.rlMain4.layoutParams
        layoutParams4.width = binding.rlMain4.width
        layoutParams4.height = binding.rlMain4.width
        binding.rlMain4.layoutParams = layoutParams4
        val matrix = ColorMatrix()
        matrix.setSaturation(0f)
        binding.ivSub1.colorFilter = ColorMatrixColorFilter(matrix)
        binding.ivSub2.colorFilter = ColorMatrixColorFilter(matrix)
        binding.ivSub3.colorFilter = ColorMatrixColorFilter(matrix)

        val layoutParamsSub1 = binding.ivSub1.layoutParams
        layoutParamsSub1.width = binding.ivSub1.width
        layoutParamsSub1.height = binding.ivSub1.width

        val layoutParamsSub2 = binding.ivSub2.layoutParams
        layoutParamsSub2.width = binding.ivSub2.width
        layoutParamsSub2.height = binding.ivSub2.width

        val layoutParamsSub3 = binding.ivSub3.layoutParams
        layoutParamsSub3.width = binding.ivSub3.width
        layoutParamsSub3.height = binding.ivSub3.width

        Log.d("이미지 1",  binding.rlMain.width.toString())
        Log.d("이미지 2",  binding.rlMain2.width.toString())
        Log.d("이미지 3",  binding.rlMain3.width.toString())
        Log.d("이미지 43",  binding.rlMain4.width.toString())
        Log.d("이미지 서브1",  binding.ivSub1.width.toString())
        Log.d("이미지 서브2",  binding.ivSub2.width.toString())
        Log.d("이미지 서브3",  binding.ivSub3.width.toString())
        binding.ivSub1.layoutParams = layoutParamsSub1
        binding.ivSub2.layoutParams = layoutParamsSub2
        binding.ivSub3.layoutParams = layoutParamsSub3

    }

    private fun observes() {
        viewModel.rpmscore.observe(this, Observer {
            if (it >= 0) {
                runOnUiThread {
//                viewModel.rpmscore.value?.let {
                    setRpmAnimation(it)
                    Log.d("rpm geah", it.toString())
//            }
                }
            }

        })
        viewModel.matching_data.observe(this, Observer { data ->
            if (data != null) {
                setview(data)
                Log.d("test1", "observes: ")
            } else {
                Log.d("test2", "observes: ")
            }
        })

        viewModel.confirmProfile.observe(this, Observer { boolean ->
            if (boolean) {
                var confirmDialog = ConfirmDilaog(this@ProfileActivity, ConfirmStatus.PROFILE_CONFIRM,1,"") { type, isonoff ->
                    if (isonoff) {
                        viewModel.ProfileReadAccept()

                    }
                }
                confirmDialog!!.show(supportFragmentManager, "")

            }
        })

        viewModel.confirmCrush.observe(this, Observer { boolean ->
            if (boolean) {
                var confirmDialog = ConfirmDilaog(this@ProfileActivity, ConfirmStatus.CRUSH_CONFIRM,1,viewModel.matching_data.value?.re_nickname) { type, isonoff ->
                    if (isonoff) {
                        viewModel.LikeAccept()

                    }
                }
                confirmDialog!!.show(supportFragmentManager, "")
            }
        })
        viewModel.user_info.observe(this, Observer { data ->
            if (data != null) {

                Log.d("나이", data.toString())
                var lat = "0"
                var lon = "0"
                var sub_seq = viewModel.sub_seq.value!!
                if (data.u_type.equals("OWNER")) {
                    binding.tvCar.visibility = View.VISIBLE
                    binding.llCar.visibility = View.VISIBLE
                }


                if (sub_seq == viewModel.my_seq.value) {
                    if (data.main_return.equals("Y")) {
                        viewModel.mainCheckReturn.value = "반려"
                        binding.viewBlack.visibility = View.VISIBLE
                        binding.tvMainCheckReturn.visibility = View.VISIBLE

                    } else if (data.main_return.equals("M")) {
                        viewModel.mainCheckReturn.value = "승인중"
                        binding.viewBlack.visibility = View.VISIBLE
                        binding.tvMainCheckReturn.visibility = View.VISIBLE
                    } else {
                        binding.viewBlack.visibility = View.GONE
                        binding.tvMainCheckReturn.visibility = View.GONE
                    }
                    for ((count, i) in data.subImage.withIndex()) {
                        if (!i.im_return.equals("N")) {
                            when (count) {
                                0 -> {
                                    binding.viewBlackSub1.visibility = View.VISIBLE
                                    binding.tvSubCheckReturn1.visibility = View.VISIBLE
                                    if (i.im_return.equals("Y")) {
                                        viewModel.subCheckReturn1.value = "반려"
                                    } else {
                                        viewModel.subCheckReturn1.value = "승인중"
                                    }
                                }
                                1 -> {
                                    binding.viewBlackSub2.visibility = View.VISIBLE
                                    binding.tvSubCheckReturn2.visibility = View.VISIBLE
                                    if (i.im_return.equals("Y")) {
                                        viewModel.subCheckReturn2.value = "반려"
                                    } else {
                                        viewModel.subCheckReturn2.value = "승인중"
                                    }
                                }
                                2 -> {
//                                binding.viewBlackSub3.visibility = View.VISIBLE
//                                binding.tvSubCheckReturn3.visibility = View.VISIBLE
//                                if (i.im_return.equals("Y")){
//                                    viewModel.subCheckReturn3.value ="반려"
//                                }else{
//                                    viewModel.subCheckReturn3.value ="승인중"
//                                }
                                }
                                else -> {}
                            }


                        }
                    }
                }
                viewModel.GetRpmAvg(sub_seq)


            }
        })
        viewModel.isnotheart.observe(this, Observer {
            if(it){
                var notheart  = TwoButtonDialog(this,Constans.DIALOG_LACK_HEART,this,Constans.DIALOG_LACK_HEART)
                notheart!!.show(supportFragmentManager,"")
            }
        })


        viewModel.btn_type.observe(this, Observer { status ->
            setVisibles(status)
            setHintVisibles(status)
            when (status) {

                ProfileStatus.PROFILE_READ_ACCEPT -> {
                    viewModel.bottomBarText.value = "프로필 열람 승인하기"
                    viewModel.btn_main.value = "확인하기"

                }
                ProfileStatus.LIKE_SEND -> {
                    viewModel.bottomBarText.value = "호감 보내기"
                    viewModel.btn_main.value = "확인하기"

                }

                ProfileStatus.DIRECT_LIKE_SEND -> {
                    viewModel.bottomBarText.value = "호감 보내기"
                    viewModel.btn_main.value = "확인하기"
                }
                ProfileStatus.LIKE_WAIT -> {
                    viewModel.bottomBarText.value = "호감을 보냈습니다."
                    viewModel.btn_main.value = "확인하기"

                }
                ProfileStatus.LIKE_ACCEPT -> {

                    viewModel.bottomBarText.value = "호감 수락하기"
                    viewModel.btn_main.value = "확인하기"

                }
                ProfileStatus.PHONE_READ -> {

                    viewModel.bottomBarText.value = "연락처 보기"
                    viewModel.btn_main.value = "확인하기"

                }
                ProfileStatus.PHONE_OPEN -> {
                    viewModel.bottomBarText.value = "RPM 평가하기"
                    viewModel.setlasttime()
                }
                ProfileStatus.RPM_EVAL -> {
                    viewModel.bottomBarText.value = "RPM 평가하기"
                    viewModel.btn_main.value = "RPM"
                    viewModel.RealNickRead()
                }
                ProfileStatus.DEFAULT -> {
                    viewModel.btn_main.value = "RPM"
                }
            }
        })

        viewModel.profileOpen.observe(this, Observer {  check ->
            if (check){
//                numOpenDialog = NumOpenDialog(this,0)
//                numOpenDialog?.show(supportFragmentManager,"")
            }

        })

        viewModel.btn_main.observe(this, Observer {
            if (it.isNotEmpty()){
                if (it.equals("확인하기") || it.equals("RPM")){

                }else{
                    numOpenDialog = NumOpenDialog(this,0)
                    numOpenDialog?.show(supportFragmentManager,"")
                }
            }
        })

        viewModel.profileOpenCheck.observe(this, Observer { it ->
            if (it) {
                twoButtonDialog = TwoButtonDialog(this, Constans.DIALOG_CONTACT_OPEN, this, 0)
                twoButtonDialog!!.show(this.supportFragmentManager, "")
            }


        })
        viewModel.setact.observe(this, Observer { act ->
            when (act) {
                1 -> {

                    var data = viewModel.matching_data.value!!
                    val number: Uri = Uri.parse("tel:" + data.safeNumber)
                    val callIntent = Intent(Intent.ACTION_DIAL, number)
                    startActivity(callIntent)

                }
                2 -> {
                    var info = viewModel.user_info!!.value

                    var i = Intent(this, RpmActivity::class.java)
                    var subseq = viewModel.sub_seq.value!!
                    i.putExtra("sub_seq", subseq)
                    i.putExtra("profile", info!!.u_image)
                    if (subseq.equals(viewModel.my_seq.value!!)) {
                        i.putExtra("type", 0)
                        i.putExtra("rpm", viewModel.rpmscore.value)
                    } else {
                        var data = viewModel.matching_data.value!!
                        Log.d("체크데이터", data.toString())
                        Log.d("체크 나의seq", viewModel.my_seq.value.toString())
                        if (data.give_seq.toString().equals(viewModel.my_seq.value)) {
                            if (data.u_rpm_open.equals("Y")) {

                                i.putExtra("type", 4)
                                Log.d("RPM 오픈", "1111: ")
                            } else {
                                i.putExtra("type", 3)
                                Log.d("RPM 오픈", "2222: ")
                            }

                        } else {
                            if (data.sub_rpm_open.equals("Y")) {
                                i.putExtra("type", 2)
                                Log.d("RPM 오픈", "3333: ")
                            } else {
//                                if (data.u_rpm_open.equals("Y")) {
//                                    i.putExtra("type", 2)
//                                    }else{
                                        i.putExtra("type", 1)
                                Log.d("RPM 오픈", "4444: ")
//                                    }

                                }


                            }

                        }
                        startActivity(i)
                    }
                }
            })
            viewModel.startlike.observe(this, Observer {
                if (it) {
                    var i = Intent(this, LikeSendActivity::class.java)
                    i.putExtra("sub_seq", viewModel.sub_seq.value)
                    if (viewModel.matching_data.value != null){
                        i.putExtra("matching_data", viewModel.matching_data.value)
                    }
                    i.putExtra("nick", viewModel.given_nickname.value)
                    i.putExtra("isdirect", viewModel.isdirect.value)
                    i.putExtra("send_b_seq", viewModel.send_b_seq.value)
                    i.putExtra("position", viewModel.position.value)
                    Log.d("포지션", viewModel.position.value.toString())
                    resultlikesendlistenr.launch(i)
                }
            })

            viewModel.toast.observe(this, Observer { text ->
                if (!text.isNullOrEmpty()) {
                    if (text.equals("하트 갯수가 부족합니다.")){
                        var drivePassPayDialog = DrivePassPayDialog(this, DrivePassDialogStatus.NOTHEART,"",0) { type, isok->
                            if(isok){
                                val i= Intent(this, HeartsShopActivity::class.java)
                                startActivity(i)
                               // this.overridePendingTransition(R.anim. fadein, R.anim.fadeout)
                            }

                        }
                        drivePassPayDialog!!.show(supportFragmentManager,"")
                    }else{
                        Toast.makeText(this,text, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
                private fun setHintVisibles(status: ProfileStatus) {
            when (status) {
                ProfileStatus.PHONE_OPEN, ProfileStatus.RPM_EVAL -> binding.tvHint.visibility =
                    View.VISIBLE
                else -> binding.tvHint.visibility = View.GONE
            }
        }

                private fun setVisibles(status: ProfileStatus) {
            when (status) {
                ProfileStatus.PROFILE_READ_ACCEPT, ProfileStatus.LIKE_SEND,ProfileStatus. DIRECT_LIKE_SEND,ProfileStatus.LIKE_WAIT, ProfileStatus.LIKE_ACCEPT, ProfileStatus.PHONE_READ, ProfileStatus.PHONE_OPEN, ProfileStatus.RPM_EVAL, ProfileStatus.DEFAULT -> {
                    var data = viewModel.matching_data.value
                    if (data != null) {
                        if (data?.heart_seq.equals("0")) {
                            if (data?.give_seq != viewModel.my_seq.value.toString()) {
                                if (!data?.pc_b_message.isNullOrEmpty()) {
                                    viewModel.message.value = data.getpc_b_message()
                                    binding.llMessage.visibility = View.VISIBLE
                                } else {
                                    binding.llMessage.visibility = View.GONE
                                }
                            } else {
                                binding.llMessage.visibility = View.GONE
                            }
                        } else {
                            if (data?.heart_seq != viewModel.my_seq.value.toString()) {
                                if (!data?.pc_message.isNullOrEmpty()) {
                                    viewModel.message.value = data.getpc_message()
                                    binding.llMessage.visibility = View.VISIBLE
                                } else {
                                    binding.llMessage.visibility = View.GONE
                                }
                            } else {
                                if (!data?.pc_b_message.isNullOrEmpty()) {
                                    viewModel.message.value = data.getpc_b_message()
                                    binding.llMessage.visibility = View.VISIBLE
                                } else {
                                    binding.llMessage.visibility = View.GONE
                                }
                            }
                        }
                    }

                }
            }
        }

                private fun clickListeners() {
            binding!!.ivBack.setOnClickListener {
                finish()
            }
            binding!!.llEdit.setOnClickListener {
                val i = Intent(this, ProfileCorrectionActivity::class.java)
                i.putExtra("u_seq", viewModel.my_seq.value)
                i.putExtra("view", "수정")
                startActivity(i)
               // overridePendingTransition(R.anim.fadein, R.anim.fadeout)
            }
            binding!!.llBottomBar.setOnClickListener {

                    val i = Intent(this, LikeSendActivity::class.java)
                    i.putExtra("sub_seq", viewModel.sub_seq.value)
                    i.putExtra("matching_data", viewModel.matching_data.value!!)
                    startActivity(i)
                   // overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                    this.finish()


            }

        }

                override fun finish() {

                    if (viewModel.type.value.equals("열람오픈")) {
                        var i = Intent()
                        Log.d("프로필화면", viewModel.hogamSendCheck.value.toString())
                        i.putExtra("position", viewModel.position.value)
                        i.putExtra("hogamSendCheck", viewModel.hogamSendCheck.value)
                        Log.d("포지션", viewModel.position.value.toString())
                        setResult(Activity.RESULT_OK, i)
                    }
            super.finish()

//            overridePendingTransition(
//                com.supercarlounge.supercar.R.anim.fadein,
//                com.supercarlounge.supercar.R.anim.fadeout
//            )
        }

            fun chkTouchInside(view: View, x: Int, y: Int): Boolean {
                val location = IntArray(2)
                view.getLocationOnScreen(location)
                if (x >= location[0]) {
                    if (x <= location[0] + view.width) {
                        if (y >= location[1]) {
                            if (y <= location[1] + view.height) {
                                return true
                            }
                        }
                    }
                }
                return false
            }

            @SuppressLint("UseCompatLoadingForDrawables")
            fun setRpmAnimation(percent: Int) {
                job = CoroutineScope(Dispatchers.IO).launch {
                    var animation = AnimationDrawable()
                    var percents: ArrayList<Int> = arrayListOf()
                    var duration = 30
                    animation.stop()

                    for (i in 0..60) {
                        when (i) {
                            20 -> duration = 20
                            40 -> duration = 10
                        }
                        percents?.add(i)
                        val name = "rpm_$i"
                        val drawableResourceId =
                            this@ProfileActivity.resources.getIdentifier(
                                name,
                                "drawable",
                                packageName
                            )
                        animation.addFrame(resources.getDrawable(drawableResourceId), duration)

                    }
                    Log.d("rpm_percent", percent.toString())
                    for (i in 59 downTo (percent * 6)) {
                        percents?.add(i)
                        val name = "rpm_$i"
                        Log.d("rpm", name)
                        val drawableResourceId =
                            this@ProfileActivity.resources.getIdentifier(
                                name,
                                "drawable",
                                packageName
                            )
                        animation.addFrame(resources.getDrawable(drawableResourceId), duration)
                    }


                    binding?.rpm?.setImageDrawable(animation);
                    animation.isOneShot = true
                    animation.start()

                    Log.d("ㅎㅇ", "setVIPanimation: ")
                }
//        delay(2000L) // delay a bit
            }

            fun setview(data: ProfileMatchingData) {
                var pc_status = data.pc_status!!.toInt()
                var pc_ok = data.pc_ok
                var give_seq = data.give_seq.toString()
                var sub_seq = data.subUser.toString()
                when (pc_status) {
                    0 -> {
                        if (sub_seq!!.equals(viewModel.my_seq.value)) {
                            if (pc_ok.equals("M")) {
                                viewModel.btn_type.value = ProfileStatus.PROFILE_READ_ACCEPT
                            } else if (pc_ok.equals("Y")) {
                                viewModel.btn_type.value = ProfileStatus.LIKE_SEND
                            }
                        } else {
                            if (pc_ok.equals("Y")) {
                                viewModel.btn_type.value = ProfileStatus.LIKE_SEND
                            }
                        }
                    }
                    1 -> {
                        var heart_seq = data.heart_seq.toString()
                        if (heart_seq.equals(viewModel.my_seq.value)) {
                            viewModel.btn_type.value = ProfileStatus.LIKE_WAIT
                        } else if (!heart_seq.equals("0")) {
                            viewModel.btn_type.value = ProfileStatus.LIKE_ACCEPT
                        } else {
                            viewModel.btn_type.value = ProfileStatus.LIKE_SEND
                        }
                    }
                    2 -> {
                        viewModel.btn_type.value = ProfileStatus.PHONE_READ
                    }
                    3 -> {
                        var endcheck = viewModel.EndDateCheck()
                        if (endcheck) {
                            viewModel.btn_type.value = ProfileStatus.PHONE_OPEN
                        } else {
                            viewModel.btn_type.value = ProfileStatus.RPM_EVAL
                        }
                    }

                }
            }

            var resultlikesendlistenr =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val idata: Intent? = result.data
                if (viewModel.type.value.equals("열람오픈")){
                    Log.d("프로필 화면2", idata?.getBooleanExtra("hogamSendCheck",false).toString())
                    viewModel.position.value = idata?.getIntExtra("position",0)
                    viewModel.hogamSendCheck.value = idata?.getBooleanExtra("hogamSendCheck",false)
                }
                val random = Random()
                val num = random.nextInt(2)
                var type = 0
                if (num == 1){
                    type = Constans.DIALOG_CRUSH_SEND1
                }else{
                    type = Constans.DIALOG_CRUSH_SEND2
                }
                twoButtonDialog = TwoButtonDialog(this, type, this, Constans.COMMENT_MORE_DELETE)
                twoButtonDialog!!.show(this.supportFragmentManager, "")

                viewModel.btn_type.value = ProfileStatus.LIKE_WAIT
            }
        }

        override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
            val focusView = currentFocus
            if (focusView != null) {
                val rect = Rect()
                focusView.getGlobalVisibleRect(rect)
                val x = ev.x.toInt()
                val y = ev.y.toInt()
                if (!rect.contains(x, y)) {
                    val imm: InputMethodManager =
                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    if (imm != null) imm.hideSoftInputFromWindow(focusView.windowToken, 0)
                    focusView.clearFocus()
                }
            }
            return super.dispatchTouchEvent(ev)
        }


    override fun onDestroy() {

        super.onDestroy()

    }
    override fun okEvent(
        type: Int,
        okAndCancel: Boolean,
        comment_value: String,
        seq: String,
        u_nickname: String
    ) {
        viewModel.PhoneOpen("호감완료")
    }

}
