package com.supercarlounge.supercar.ui.priview.activity

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
import androidx.fragment.app.FragmentManager
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
import com.supercarlounge.supercar.data.SubImage
import com.supercarlounge.supercar.data.UserInformationCarData
import com.supercarlounge.supercar.data.UserInformationData
import com.supercarlounge.supercar.databinding.ActivityProfileBinding
import com.supercarlounge.supercar.dialog.*
import com.supercarlounge.supercar.enumset.ConfirmStatus
import com.supercarlounge.supercar.enumset.DrivePassDialogStatus
import com.supercarlounge.supercar.enumset.ProfileStatus
import com.supercarlounge.supercar.enumset.QuickTypeStatus
import com.supercarlounge.supercar.ui.activity.JoinActivity
import com.supercarlounge.supercar.viewmodel.ProfileViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class PrvProfileActivity : BaseActivitiy() , DialogEvent {
    private var job: Job? = null
    var application: MainApplication? = null
    private lateinit var binding: ActivityProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private var rAdapter: VipDriveRankAdapter? = null
    var numOpenDialog : NumOpenDialog? = null
    var twoButtonDialog : TwoButtonDialog? = null
    private var quickUserDialog: QuickUserDialog? = null
    var fm: FragmentManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fm = supportFragmentManager
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        application = applicationContext as MainApplication
        var mseq = application!!.userData!!.u_seq.toString()
        viewModel.my_seq.value = mseq
        viewModel.mynick.value = application!!.userData!!.u_nickname

        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel


        binding.tvEdit.visibility = View.VISIBLE
        binding.tvSafeNumber.visibility = View.GONE



        binding!!.ivSub1.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewRedefinition()
                binding!!.ivSub1.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }


        })
        SetPrvData()
        clickListeners()
        observes()
    }

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

        binding.viewBlack.visibility = View.GONE
        binding.viewBlackSub1.visibility = View.GONE
        binding.viewBlackSub2.visibility = View.GONE
        //viewModel.GetProfile(mseq, lat, lon, sub_seq)


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
               // viewModel.GetRpmAvg(sub_seq)


            }
        })


        viewModel.btn_type.observe(this, Observer { status ->
//            setVisibles(status)
//            setHintVisibles(status)
        })

        viewModel.profileOpen.observe(this, Observer {  check ->
            if (check){
//                numOpenDialog = NumOpenDialog(this,0)
//                numOpenDialog?.show(supportFragmentManager,"")
            }

        })

        viewModel.setact.observe(this, Observer { act ->
            if(act == 2){
                ShowQuickUserDialog()
            }

        })



            viewModel.toast.observe(this, Observer { text ->
                        if(text.isNullOrEmpty() == false) {
                            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
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
                else -> {}
            }
        }

                private fun clickListeners() {
            binding!!.ivBack.setOnClickListener {
                finish()
            }
            binding!!.llEdit.setOnClickListener {
                ShowQuickUserDialog()

            }
            binding!!.llBottomBar.setOnClickListener {

                ShowQuickUserDialog()
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
                            else -> {}
                        }
                        percents?.add(i)
                        val name = "rpm_$i"
                        val drawableResourceId =
                            this@PrvProfileActivity.resources.getIdentifier(
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
                            this@PrvProfileActivity.resources.getIdentifier(
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
                    else -> {}
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
        quickUserDialog!!.show(fm!!, "")
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
    fun SetPrvData(){
        var profileimage = ""
        var sublist:ArrayList<SubImage> = arrayListOf()
        var age = ""
        var uarea = ""
        var u_job = ""
        var u_nick = ""
        var ucm = ""
        var charm = ""
        var music = ""
        var drive = ""
        var gender = ""
        var drivew = ""
        var drivec = ""
        var intro = "즐거운 드라이브 하고싶어요. 잘부탁드립니다."
        var typeset  =""
        var type = 0
        val range = (0..1)
        type = range.random()
        if(application!!.userData?.u_gender.equals("MALE")){

            if(type == 0){
                typeset =  "OWNER"
                charm = "[\"손이 예쁜\",\"다정한\"]"
                music = "[\"K-POP\",\"재즈\"]"
                drive = "즉흥적인 드라이브"
                ucm = "183 cm"
                u_nick = "심심한 한스"
                age = "29"
                uarea = "서울"
                u_job = "배우"
                drivec = "어디론가 훌쩍 떠나고 싶을때 마음 맞는 친구나\n설레는 드라이브를 하고 싶어요."
                drivew = "서울 시내의 야경, 시원한 밤에 보는 한강을 너무 좋아해요!\n함께 가실분 계신가요?"
                profileimage = "https://supercarlounge.s3.ap-northeast-2.amazonaws.com/user/undefined/testinfo/testImage/1686548926711.png"
                var carlist:ArrayList<UserInformationCarData> = arrayListOf()
                carlist.add(UserInformationCarData("https://supercarlounge.s3.ap-northeast-2.amazonaws.com/car_brand/1646896055812.png","MCLAREN","","","",0,"","","","","","","","","",0,"","","","","","","",""))
                viewModel.user_car.value = carlist
                viewModel.charmlist.value = arrayListOf("손이 예쁜","다정한")
                viewModel.musictypelist.value = arrayListOf("K-POP","재즈")
            }else{
                typeset =  "OWNER"
                charm = "[\"좋은 목소리\",\"친절한\"]"
                music = "[\"아이돌\",\"발라드\"]"
                drive = "새벽에하는 드라이브"
                ucm = "182 cm"
                u_nick = "한가한 너구리"
                age = "26"
                uarea = "서울"
                u_job = "자영업"
                drivec = "예전에는 혼자 여행하는걸 좋아해서 여러군데 다녀왔는데\n요즘에는 즉흥적인 목적지 없는 드라이브 하고 싶어요!"
                drivew = "어딜가든 다 좋아합니다.\n다녀와 본 곳을 가도 갈때마다 새로운거 같아요."
                profileimage = "https://supercarlounge.s3.ap-northeast-2.amazonaws.com/user/undefined/testinfo/testImage/1686548926728.png"
                var carlist:ArrayList<UserInformationCarData> = arrayListOf()
                carlist.add(UserInformationCarData("https://supercarlounge.com:3000/images/car_brand/20211220032231lamborghini.png","LAMBORGHINI","","","",0,"","","","","","","","","",0,"","","","","","","",""))
                viewModel.user_car.value = carlist
                viewModel.charmlist.value = arrayListOf("좋은 목소리","친절한")
                viewModel.musictypelist.value = arrayListOf("아이돌","발라드")
            }


        }else{
            var type = 0
            if(type == 0){
                typeset =  "PASSENGER"
                charm = "[\"몸매가 좋은\",\"피부가 좋은\"]"
                music = "[\"R&B\",\"POP\"]"
                drive = "맛집탐방 드라이브"
                ucm = "171 cm"
                u_nick = "즐거운 미니마우스"
                age = "23"
                uarea = "서울"
                u_job = "무용"
                drivec = "같이 운동도 즐기고 활동적인 데이트 하고 싶어요!\n최근에 골프 레슨 시작해서 같이 즐겼으면 좋을거 같아요!"
                drivew = "가까운 근교 카페가거나 맛집탐방 하면서 대화나누는걸 좋아해요!"
                profileimage = "https://supercarlounge.s3.ap-northeast-2.amazonaws.com/user/undefined/testinfo/testImage/1686548926646.png"
                viewModel.charmlist.value = arrayListOf("몸매가 좋은","피부가 좋은")
                viewModel.musictypelist.value = arrayListOf("R&B","POP")
            }else{
                typeset =  "OWNER"
                charm = "[\"비율이 좋은\",\"글래머\"]"
                music = "[\"K-POP\",\"힙합\"]"
                drive = "가까운곳 드라이브"
                ucm = "168 cm"
                u_nick = "행복한 미키마우스"
                age = "25"
                uarea = "서울"
                u_job = "연예인"
                drivec = "속이 뻥 뚫리는 시원한 드라이브를 함께 하고싶어요\n제 차로 드라이브 해도 좋아요!"
                drivew = "가까운 근교 카페가거나 맛집탐방 하면서 대화나누는걸 좋아해요!"
                profileimage = "https://supercarlounge.s3.ap-northeast-2.amazonaws.com/user/undefined/testinfo/testImage/1686548926691.png"
                var carlist:ArrayList<UserInformationCarData> = arrayListOf()
                carlist.add(UserInformationCarData("https://supercarlounge.com:3000/images/car_brand/20211220033303ferrari.png","FERRARI","","","",0,"","","","","","","","","",0,"","","","","","","",""))
                viewModel.user_car.value = carlist
                viewModel.charmlist.value = arrayListOf("비율이 좋은","글래머")
                viewModel.musictypelist.value = arrayListOf("K-POP","힙합")
            }

        }
        sublist.add(SubImage("","","Y",0,"",profileimage,0,"",""))
        sublist.add(SubImage("","","Y",0,"",profileimage,0,"",""))
        viewModel.sub_image.value = sublist
        var userData: UserInformationData? = UserInformationData(
            0,
            "",
            0,
            "",
            "Y",
            profileimage,
            sublist,
            "",
            age,
            uarea,
            "",
            charm,
            ucm,
            drive,
            "",
            "",
            gender,
            0,
            "https://supercarlounge.com:3002/images/user/c46830e9-cb9c-483c-a770-59dacae4db24/profile/d27ad151a47b430290104c6c563b9e8f.jpg",
            u_job,
            "",
            "",
            "",
            music,
            u_nick,
            u_nick,
            "",
            "",
            "N",
            intro,
            "",
            "",
            typeset,
            0,
            drivew,
            drivec,
            "1",
            "Y",
            "Y",
            "Y",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",  "","",""
        )
        viewModel.user_info.value = userData
    }
}
