package com.supercarlounge.supercar.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ui.AppBarConfiguration
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.GsonBuilder
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.OnKeyboardVisibilityListener
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.data.*
import com.supercarlounge.supercar.databinding.ActivityMyJoinInformationBinding
import com.supercarlounge.supercar.viewmodel.MyJoinInfomationViewModel
import kotlinx.coroutines.flow.collectLatest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MyJoinInfomationActivity : BaseActivitiy(), OnKeyboardVisibilityListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMyJoinInformationBinding
    private lateinit var viewModel: MyJoinInfomationViewModel
    private lateinit var application: MainApplication
    var SubImage = SubImage("", "", "", 0, "", "", 0, "", "")
    var type = ""
    var original_sub_image: ArrayList<SubImage> = arrayListOf()
    var view = ""

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        with(window) {
//            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
//            // set an slide transition
//            enterTransition = Slide(Gravity.END)
//            exitTransition = Slide(Gravity.START)
//        }
        viewModel = ViewModelProvider(this).get(MyJoinInfomationViewModel::class.java)
        binding = ActivityMyJoinInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel

        application = applicationContext as MainApplication

        viewModel._userdata.value = application.userData

        val i = intent

        viewModel.view.value = i.getStringExtra("view")
        viewModel.u_birthday.value = i.getStringExtra("u_birthday")
        viewModel.u_phone.value = i.getStringExtra("u_phone")
        viewModel.u_gender.value = i.getStringExtra("u_gender")
        viewModel.u_name.value = intent.getStringExtra("u_name")
        viewModel.u_seq.value = application.userData?.u_seq.toString()

        viewModel.selectCarBrand.value = intent.getStringExtra("CarBrand").toString()
        viewModel.selectCarModel.value = intent.getStringExtra("CarModel").toString()
        viewModel.selectCarSeries.value = intent.getStringExtra("CarSeries").toString()

        viewModel.titleText.value = i.getStringExtra("titleText")
        viewModel.mainText.value = i.getStringExtra("mainText")
        viewModel.subText.value = i.getStringExtra("subText")
        viewModel.nickname.value = intent.getStringExtra("nickname")





        if (intent.getSerializableExtra("CharmList") != null) {
            viewModel.resultState2Array =
                intent.getSerializableExtra("CharmList") as ArrayList<String>
        }
        if (intent.getSerializableExtra("musicTasteList") != null) {
            viewModel.resultState3Array =
                intent.getSerializableExtra("musicTasteList") as ArrayList<String>
        }
        if (intent.getSerializableExtra("height") != null) {
            viewModel.resultState4Array = intent.getSerializableExtra("height") as ArrayList<String>
        }
        if (intent.getSerializableExtra("job") != null) {
            viewModel.resultState5Array = intent.getSerializableExtra("job") as ArrayList<String>
        }
        if (intent.getSerializableExtra("regin") != null) {
            viewModel.resultState6Array = intent.getSerializableExtra("regin") as ArrayList<String>
        }
        if (intent.getSerializableExtra("driveStyle") != null) {
            viewModel.resultState7Array =
                intent.getSerializableExtra("driveStyle") as ArrayList<String>
        }

        if (i.getStringExtra("carimage") != null) {
            viewModel.carimage.value = i.getStringExtra("carimage")
        }
        if (i.getStringExtra("insuranceimage") != null) {
            viewModel.insuranceimage.value = i.getStringExtra("insuranceimage")
        }
        if (i.getStringExtra("income") != null) {
            viewModel.income.value = i.getStringExtra("income")
        }
        type = i.getStringExtra("type").toString()
        if (viewModel.carimage.value != null) {
            viewModel.MultipartBodyCarImage.value =
                absolutelyPath(viewModel.carimage.value!!, this, "mainCar")
        }

        if (viewModel.insuranceimage.value != null) {
            viewModel.MultipartBodyInsuranceimageImage.value =
                absolutelyPath(viewModel.insuranceimage.value, this, "insurance")
        }
        Log.d("가져온 유저 데이터", application.userData.toString())
        view = intent.getStringExtra("view").toString()
        Log.d("퀵유저타입", view)

        if (viewModel.view.value.equals("수정")) {


            if (intent.getParcelableArrayListExtra<UserInformationData>("userData") != null) {
                var list =
                    intent.getParcelableArrayListExtra<UserInformationData>("userData") as ArrayList<UserInformationData>
                if (list.size != 0) {
                    viewModel.userdata.value = list[0]
                }
            }


            viewModel.carNewImage.value = intent.getStringExtra("carNewImage")
            if (viewModel.carNewImage.value?.startsWith("https:") == true) {
                viewModel.carNewImage.value = null
            }
            viewModel.insuranceNewImage.value = intent.getStringExtra("insuranceNewImage")
            if (viewModel.insuranceNewImage.value?.startsWith("https:") == true) {
                viewModel.insuranceNewImage.value = null
            }
            viewModel.incomeNewImage.value = intent.getStringExtra("incomeNewImage")
            if (viewModel.incomeNewImage.value?.startsWith("https:") == true) {
                viewModel.incomeNewImage.value = null
            }

            Log.d("수정 받아온 carNewImage", viewModel.carNewImage.value.toString())
            Log.d("수정 받아온 insuranceNewImage", viewModel.insuranceNewImage.value.toString())
            Log.d("수정 받아온 incomeNewImage", viewModel.incomeNewImage.value.toString())

//            viewModel.u_seq.value?.let { viewModel.getUserInformation(it,application)
            var data = viewModel.userdata.value
            binding.edtAboutMe.setText(data?.u_about_me)
            binding.edtDriveStyle.setText(data?.u_drive_c)
            binding.edtDriveCourse.setText(data?.u_drive_w)
            viewModel.aboutMe.value = data?.u_about_me
            viewModel.driveStyle.value = data?.u_drive_w
            viewModel.driveCourse.value = data?.u_drive_c
            var count = 0

            Glide.with(binding.ivMainImage).load(data?.u_image).override(
                Resources.getSystem().displayMetrics.widthPixels,
                Resources.getSystem().displayMetrics.heightPixels
            ).fitCenter().into(binding.ivMainImage)
            if (data?.u_smoking.equals("Y")) viewModel.onclickSmoking1() else viewModel.onclickSmoking2()

            viewModel.mainImageUrl.value = data?.u_image
            viewModel.SubImageUrlList.value?.addAll(data?.subImage!!)


            if (data?.u_about_me_ck.equals("N")) {
                viewModel.aboutMe_check.value = "반려"

            } else {
                binding.edtAboutMe.visibility = View.INVISIBLE
//                binding.edtAboutMe.setTextColor(ContextCompat.getColor(this, R.color.white_30))
            }

            if (data?.u_drive_w_yn.equals("N")) {
                viewModel.drive_w_check.value = "반려"

            } else {
                binding.edtDriveStyle.visibility = View.INVISIBLE
//                binding.edtDriveStyle.setTextColor(ContextCompat.getColor(this, R.color.white_30))
            }

            if (data?.u_drive_c_yn.equals("N")) {
                viewModel.drive_c_check.value = "반려"

            } else {
                binding.edtDriveCourse.visibility = View.INVISIBLE
//                binding.edtDriveCourse.setTextColor(ContextCompat.getColor(this, R.color.white_30))
            }


            Log.d("사이즈", viewModel.SubImageUrlList.value?.size.toString())
            Log.d("테스트", viewModel.SubImageUrlList.value.toString())
            when (data?.main_return) {
                "N" -> {
                    Glide.with(binding.ivMainImageCancel).load(R.drawable.ic_frame_2276)
                        .into(binding.ivMainImageCancel)
                    binding.llMainImage.isEnabled = false
                }
                "M" -> {
                    viewModel.mainCheckReturn.value = "심사중"
//                    Glide.with(binding.ivMainImageCancel).load(R.drawable.ic_cell).into(binding.ivMainImageCancel)
                    binding.viewBlack.visibility = View.VISIBLE
                    binding.ivMainImageCancel.visibility = View.INVISIBLE
                    binding.llMainImage.isEnabled = false
                }
                "Y" -> {
                    viewModel.mainCheckReturn.value = "반려"
                    Glide.with(binding.ivMainImageCancel).load(R.drawable.ic_frame_2277)
                        .into(binding.ivMainImageCancel)
//                    binding!!.ivMainImage.setBackgroundColor(ContextCompat.getColor(this,R.color.black_70));
                    binding.viewBlack.visibility = View.VISIBLE
                }
                else -> {}
            }
            binding.ivMainImageCancel.isEnabled = false

            if (data?.subImage != null) {

                for (i in data?.subImage!!) {
                    original_sub_image.add(i)
                    if (i.im_return.equals("Y")) {
                        var check = false
                    }

                    when (count) {
                        0 -> {

                            Glide.with(binding.ivSubImage1).load(i.im_url).into(binding.ivSubImage1)
                            SubImage = SubImage("", "", i.im_return!!, 0, "", i.im_url!!, 0, "", "")


                            when (i.im_return) {
                                "N" -> {
                                    Glide.with(binding.ivSubImageCancel1)
                                        .load(R.drawable.ic_frame_2276)
                                        .into(binding.ivSubImageCancel1)


                                }
                                "M" -> {
                                    viewModel.sub1CheckReturn.value = "심사중"
                                    Glide.with(binding.ivSubImageCancel1).load(R.drawable.ic_cell)
                                        .into(binding.ivSubImageCancel1)
                                    binding.viewBlackSub1.visibility = View.VISIBLE


                                }
                                "Y" -> {
                                    viewModel.sub1CheckReturn.value = "반려"
                                    Glide.with(binding.ivSubImageCancel1)
                                        .load(R.drawable.ic_frame_2277)
                                        .into(binding.ivSubImageCancel1)
                                    binding.viewBlackSub1.visibility = View.VISIBLE
                                }
                                else -> {}
                            }

                        }
                        1 -> {
                            Glide.with(binding.ivSubImage2).load(i.im_url).into(binding.ivSubImage2)
                            SubImage = SubImage("", "", i.im_return!!, 0, "", i.im_url!!, 0, "", "")


                            when (i.im_return) {
                                "N" -> {
                                    Glide.with(binding.ivSubImageCancel2)
                                        .load(R.drawable.approval_24x24)
                                        .into(binding.ivSubImageCancel2)


                                }
                                "M" -> {
                                    viewModel.sub2CheckReturn.value = "심사중"
                                    Glide.with(binding.ivSubImageCancel2).load(R.drawable.ic_cell)
                                        .into(binding.ivSubImageCancel2)
                                    binding.viewBlackSub2.visibility = View.VISIBLE


                                }
                                "Y" -> {
                                    viewModel.sub2CheckReturn.value = "반려"
                                    Glide.with(binding.ivSubImageCancel2)
                                        .load(R.drawable.ic_frame_2277)
                                        .into(binding.ivSubImageCancel2)
                                    binding.viewBlackSub2.visibility = View.VISIBLE
                                }
                                else -> {}
                            }


                        }
                        2 -> {
                            Glide.with(binding.ivSubImage3).load(i.im_url).into(binding.ivSubImage3)
                            SubImage = SubImage("", "", i.im_return!!, 0, "", i.im_url!!, 0, "", "")

                            when (i.im_return) {
                                "N" -> {
                                    Glide.with(binding.ivSubImageCancel3)
                                        .load(R.drawable.ic_frame_2276)
                                        .into(binding.ivSubImageCancel3)


                                }
                                "M" -> {
                                    viewModel.sub3CheckReturn.value = "심사중"
                                    Glide.with(binding.ivSubImageCancel3).load(R.drawable.ic_cell)
                                        .into(binding.ivSubImageCancel3)
                                    binding.viewBlackSub3.visibility = View.VISIBLE


                                }
                                "Y" -> {
                                    viewModel.sub3CheckReturn.value = "반려"
                                    Glide.with(binding.ivSubImageCancel3)
                                        .load(R.drawable.ic_frame_2277)
                                        .into(binding.ivSubImageCancel3)
                                    binding.viewBlackSub3.visibility = View.VISIBLE
                                }
                                else -> {}
                            }
                        }
                        else -> {}

                    }
                    count++
                }
                var check = false
                for (i in data?.subImage!!) {
                    if (i.im_return.equals("Y")) {
                        check = true
                    }
                }

                if (!check) {
                    if (data?.subImage?.size!! == 1) {
                        binding.ivSubImageCancel1.isEnabled = false
                        binding.llSubImage1.isEnabled = false
                    } else if (data?.subImage?.size!! == 2) {
                        binding.ivSubImageCancel1.isEnabled = false
                        binding.ivSubImageCancel2.isEnabled = false
                        binding.llSubImage1.isEnabled = false
                        binding.llSubImage2.isEnabled = false
                    } else {

                        binding.ivSubImageCancel1.isEnabled = false
                        binding.ivSubImageCancel2.isEnabled = false
                        binding.ivSubImageCancel3.isEnabled = false
                        binding.llSubImage1.isEnabled = false
                        binding.llSubImage2.isEnabled = false
                        binding.llSubImage3.isEnabled = false
                    }
                }


            }

            viewModel.SubImageUrlListSize.value = viewModel.SubImageUrlList.value?.size


        } else {
            if (type == "OWNER") {
                if (!application!!.informationOwnerLog) {
                    application.let {
                        it?.joinLog(this, "개인정보 입력(OWNER)", viewModel.u_name.value.toString())
                        it?.informationOwnerLog = true
                    }

                }
            } else {
                if (!application!!.informationPassengerLog) {
                    application.let {
                        it?.joinLog(this, "개인정보 입력(PASSENGER)", viewModel.u_name.value.toString())
                        it?.informationPassengerLog = true
                    }

                }
            }

        }
        initCompanionCheckList()
        bottomTextCheck()

        viewModel.titleText.value = "REGISTER"
        clickListeners()
        observes()
        editTextListeners()
        binding!!.llMainImage.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewRedefinition()
            }


        })
        setKeyboardVisibilityListener(this)
        flows()
    }

    private fun bottomTextCheck() {
        var aboutMe = !viewModel.aboutMe.value.isNullOrEmpty()
        var driveStyle = !viewModel.driveStyle.value.isNullOrEmpty()
        var driveCourse = !viewModel.driveCourse.value.isNullOrEmpty()
        var mainImageUrl = !viewModel.mainImageUrl.value.isNullOrEmpty()
        var smoking = viewModel.checked1.value == true || viewModel.checked2.value == true
        var subImageSize = viewModel.SubImageUrlListSize.value

        if (subImageSize != null) {
            viewModel.stateBottomText.value =
                aboutMe && driveStyle && driveCourse && mainImageUrl && smoking && subImageSize > 1


        }
    }

    private fun initCompanionCheckList() {
//        var data1 =  CompanionCheckData("대표 이미지",false,0)
//        var data2 =  CompanionCheckData("서브 이미지",false,0)
//        var data3 =  CompanionCheckData("서브 이미지",false,0)
//        var data4 =  CompanionCheckData("서브 이미지",false,0)
//        var data5 =  CompanionCheckData("자기소개",false,0)
//        var data5 =  CompanionCheckData("자기소개",false,0)
//        var data5 =  CompanionCheckData("자기소개",false,0)
//        var data5 =  CompanionCheckData("자기소개",false,0)
//                viewModel.companionCheckList.value =
    }

    private fun flows() {
        lifecycleScope.launchWhenStarted {
            viewModel.stateFlow.collectLatest {
                when (it) {
                    1 -> {


                            val i = Intent(
                                this@MyJoinInfomationActivity,
                                SubscriptionApplicationCompleteActivity::class.java
                            )
                            i.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            i.putExtra("u_admin", "M")
//                            i.putExtra("u_seq", viewModel.userdata.value?.u_seq.toString())
//                            Log.d("조회 u_seq", viewModel.userdata.value?.u_seq.toString())
                            startActivity(i)
                           // overridePendingTransition(R.anim.fadein, R.anim.fadeout)
//                        viewModel.apiCallOverlapCheck.value = false


                    }
                    2 -> {
                        Toast.makeText(
                            this@MyJoinInfomationActivity,
                            "등록된 번호입니다",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    3 -> {
//                        Toast.makeText(this@MyJoinInfomationActivity,"",Toast.LENGTH_SHORT).show()
                    }

                    else -> {}
                }
            }


        }
    }

    private fun viewRedefinition() {
        val layoutParams = binding.llMainImage.layoutParams
        layoutParams.width = binding.llMainImage.width
        layoutParams.height = binding.llMainImage.width
        binding.llMainImage.layoutParams = layoutParams

        val layoutParamsSub1 = binding.llSubImage1.layoutParams
        layoutParamsSub1.width = binding.llSubImage1.width
        layoutParamsSub1.height = binding.llSubImage1.width

        val layoutParamsSub2 = binding.llSubImage2.layoutParams
        layoutParamsSub2.width = binding.llSubImage2.width
        layoutParamsSub2.height = binding.llSubImage2.width

        val layoutParamsSub3 = binding.llSubImage3.layoutParams
        layoutParamsSub3.width = binding.llSubImage3.width
        layoutParamsSub3.height = binding.llSubImage3.width

        binding.llSubImage1.layoutParams = layoutParamsSub1
        binding.llSubImage2.layoutParams = layoutParamsSub2
        binding.llSubImage3.layoutParams = layoutParamsSub3

    }

    private fun observes() {


        viewModel.userInfoObserver.observe(this, Observer {
            if (it == true) {
//                    aboutMe.value =
//                    driveCourse.value
//                    driveStyle.value =

            }

        })

        viewModel.toast.observe(this, Observer { text ->
            if (!text.isNullOrEmpty()) {
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.editcall.observe(this, Observer {
            if (it == true) {
                val i = Intent(
                    this@MyJoinInfomationActivity,
                    SubscriptionApplicationCompleteActivity::class.java
                )
                i.putExtra("u_admin", "M")
                i.putExtra("u_seq", viewModel.userdata.value?.u_seq.toString())
                Log.d("대기u_seq", viewModel.userdata.value?.u_seq.toString())
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
               // overridePendingTransition(R.anim.fadein, R.anim.fadeout)


            }

        })
    }

    private fun editTextListeners() {
        binding!!.edtDriveStyle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {
                viewModel.driveStyle.value = p0.toString()
                bottomTextCheck()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding!!.edtDriveCourse.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {
                viewModel.driveCourse.value = p0.toString()
                bottomTextCheck()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })


        binding!!.edtAboutMe.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {
                viewModel.aboutMe.value = p0.toString()
                bottomTextCheck()

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })


        binding!!.edtAboutMe.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {
                viewModel.aboutMe.value = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })


    }


    private fun setKeyboardVisibilityListener(onKeyboardVisibilityListener: OnKeyboardVisibilityListener) {
        val parentView = (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
        parentView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
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
//        if(!visible){
//            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//
//
//        }else{
//            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//        }


    }

    private fun clickListeners() {
        binding!!.ivBack.setOnClickListener {
            finish()
        }
        binding!!.llBottomBar.setOnClickListener {
           if (viewModel.driveStyle.value.equals("")) {
                Toast.makeText(this@MyJoinInfomationActivity,"어떤 드라이브를 하고싶은지 입력해주세요.",Toast.LENGTH_SHORT).show()
            } else if (viewModel.driveCourse.value.equals("")) {
                Toast.makeText(this@MyJoinInfomationActivity,"자주 가는 맛집,코스를 입력해주세요.",Toast.LENGTH_SHORT).show()
            }   else if (viewModel.aboutMe.value.equals("")) {
               Toast.makeText(this@MyJoinInfomationActivity,"자기소개를 입력해주세요.",Toast.LENGTH_SHORT).show()
           }  else if (viewModel.aboutMe.value?.length!! <30){
               Toast.makeText(this, "자기소개를 30자 이상 적어주세요", Toast.LENGTH_SHORT).show()
           }else if (viewModel.u_smoking.value.equals("")) {
                Toast.makeText(this@MyJoinInfomationActivity,"흡연여부를 선택해주세요.",Toast.LENGTH_SHORT).show()
            } else if (viewModel.mainImageUrl.value == null) {
                Toast.makeText(this@MyJoinInfomationActivity,"메인프로필을 등록해주세요.",Toast.LENGTH_SHORT).show()
            } else if (viewModel.SubImageUrlList.value?.size!! < 2) {
                Toast.makeText(this@MyJoinInfomationActivity,"서브프로필은 최소 2개 이상 등록해주세요.",Toast.LENGTH_SHORT).show()
            } else {
                if (viewModel.view.value.equals("수정")) {


                    var u_about_me_ck = viewModel.userdata.value?.u_about_me_ck
                    var u_drive_c_yn = viewModel.userdata.value?.u_drive_c_yn
                    var u_drive_w_yn = viewModel.userdata.value?.u_drive_w_yn
                    var m_main_url = viewModel.userdata.value?.main_url
                    var main_return = viewModel.userdata.value?.main_return
                    var m_u_income = viewModel.userdata.value?.u_income
                    var m_u_income_yn = viewModel.userdata.value?.u_income_yn
                    var m_u_abutme = viewModel.userdata.value?.u_about_me
                    var m_u_drive_c = viewModel.userdata.value?.u_drive_c
                    var m_u_drive_w = viewModel.userdata.value?.u_drive_w

                    if (viewModel.userdata.value?.u_type.equals("OWNER")) {

                        var carlist: ArrayList<UserInformationCarData> = arrayListOf()
                        carlist = application.userCarData?.get(0)!!

                        var m_uc_car_main_yn = carlist[0].uc_car_main_yn
                        var m_uc_insur_url_yn = carlist[0].uc_insur_url_yn

                        Log.d("확인용 메인리턴", main_return.toString())
                        Log.d("확인용 메인리턴2", viewModel.mainImageUrl.value.toString())
                        Log.d("확인용 메인리턴3", m_main_url.toString())
                        if (viewModel.SubImageUrlList.value != null || viewModel.SubImageUrlList.value?.size != 0) {
                            var sub_return = false
                            var sub_image = false
                            for (i in viewModel.SubImageUrlList.value!!) {
                                Log.d("서브이미지", i.toString())
                                if (i.im_return.equals("Y")) {
                                    sub_return = true
                                }
                                if (i.im_url?.startsWith("https:") == true) {
                                    sub_image = true
                                }
                            }


                            if (m_uc_car_main_yn?.equals("N") == true && viewModel.carimage.value!!.startsWith("https:")) {
                                viewModel.toast.value = "차량 사진을 수정해 주세요"
                            } else if (m_uc_insur_url_yn?.equals("N") == true && viewModel.insuranceimage.value?.startsWith(
                                    "https:"
                                ) == true
                            ) {
                                viewModel.toast.value = "보험 사진을 수정해 주세요"
                            } else if (u_about_me_ck?.equals("N") == true && m_u_abutme?.equals(
                                    viewModel.aboutMe.value
                                ) == true
                            ) {
                                viewModel.toast.value = "자기소개를 수정해 주세요"
                            } else if (u_drive_c_yn?.equals("N") == true && m_u_drive_c?.equals(
                                    viewModel.driveCourse.value
                                ) == true
                            ) {
                                viewModel.toast.value = "드라이브 코스를 수정해 주세요"
                            } else if (u_drive_w_yn?.equals("N") == true && m_u_drive_w?.equals(
                                    viewModel.driveStyle.value
                                ) == true
                            ) {
                                viewModel.toast.value = "드라이브 스타일을 수정해 주세요"
                            } else if (main_return?.equals("Y") == true && viewModel.mainImageUrl.value?.startsWith("https:") == true) {
                                viewModel.toast.value = "프로필사진을 수정해 주세요"
                            } else if (sub_return && sub_image) {
                                viewModel.toast.value = "서브프로필 사진을 수정해 주세요"
                            } else {
                                viewModel.userCompanionEditManOwner(
                                    absolutelyPathEdit(
                                        viewModel.carNewImage.value,
                                        this,
                                        "mainCar"
                                    ),
                                    absolutelyPathEdit(
                                        viewModel.insuranceNewImage.value,
                                        this,
                                        "insurance"
                                    ),
                                    absolutelyPathEdit(
                                        viewModel.mainImageNewUrl.value,
                                        this,
                                        "mainProfile"
                                    ),
                                    getmultipartlistEdit(
                                        viewModel.SubImageUrlList.value!!,
                                        "subProfile"
                                    ),

                                    )
                            }


                        }


                    } else {


                        if (viewModel.userdata.value?.u_gender.equals("MALE")) {


                            if (viewModel.SubImageUrlList.value != null || viewModel.SubImageUrlList.value?.size != 0) {
                                var sub_return = false
                                var sub_image = false
                                for (i in viewModel.SubImageUrlList.value!!) {
                                    Log.d("서브이미지", i.toString())
                                    if (i.im_return.equals("Y")) {
                                        sub_return = true
                                    }
                                    if (i.im_url?.startsWith("https:") == true) {
                                        sub_image = true
                                    }
                                }


                                if (m_u_income?.equals("N") == true && viewModel.income.value!!.startsWith("https:")) {
                                    viewModel.toast.value = "소득 증명 사진을 수정해 주세요"
                                } else if (u_about_me_ck?.equals("N") == true && m_u_abutme?.equals(
                                        viewModel.aboutMe.value
                                    ) == true
                                ) {
                                    viewModel.toast.value = "자기소개를 수정해 주세요"
                                } else if (u_drive_c_yn?.equals("N") == true && m_u_drive_c?.equals(
                                        viewModel.driveCourse.value
                                    ) == true
                                ) {
                                    viewModel.toast.value = "드라이브 코스를 수정해 주세요"
                                } else if (u_drive_w_yn?.equals("N") == true && m_u_drive_w?.equals(
                                        viewModel.driveStyle.value
                                    ) == true
                                ) {
                                    viewModel.toast.value = "드라이브 스타일을 수정해 주세요"
                                } else if (main_return?.equals("Y") == true && viewModel.mainImageUrl.value?.startsWith("https:") == true) {
                                    viewModel.toast.value = "프로필사진을 수정해 주세요"
                                } else if (sub_return && sub_image) {
                                    viewModel.toast.value = "서브프로필 사진을 수정해 주세요"
                                } else {
                                    viewModel.userCompanionEditManPassenger(
                                        absolutelyPathEdit(
                                            viewModel.incomeNewImage.value,
                                            this,
                                            "income"
                                        ),
                                        absolutelyPathEdit(
                                            viewModel.mainImageNewUrl.value,
                                            this,
                                            "mainProfile"
                                        ),
                                        getmultipartlistEdit(
                                            viewModel.SubImageUrlList.value!!,
                                            "subProfile"
                                        ),

                                        )
                                }


                            }

                        } else {
                            if (viewModel.SubImageUrlList.value != null || viewModel.SubImageUrlList.value?.size != 0) {
                                var sub_return = false
                                var sub_image = false
                                for (i in viewModel.SubImageUrlList.value!!) {
                                    Log.d("서브이미지", i.toString())
                                    if (i.im_return.equals("Y")) {
                                        sub_return = true
                                    }
                                    if (i.im_url?.startsWith("https:") == true) {
                                        sub_image = true
                                    }
                                }


                               if (u_about_me_ck?.equals("N") == true && m_u_abutme?.equals(
                                        viewModel.aboutMe.value
                                    ) == true
                                ) {
                                    viewModel.toast.value = "자기소개를 수정해 주세요"
                                } else if (u_drive_c_yn?.equals("N") == true && m_u_drive_c?.equals(
                                        viewModel.driveCourse.value
                                    ) == true
                                ) {
                                    viewModel.toast.value = "드라이브 코스를 수정해 주세요"
                                } else if (u_drive_w_yn?.equals("N") == true && m_u_drive_w?.equals(
                                        viewModel.driveStyle.value
                                    ) == true
                                ) {
                                    viewModel.toast.value = "드라이브 스타일을 수정해 주세요"
                                } else if (main_return?.equals("Y") == true && viewModel.mainImageUrl.value?.startsWith("https:") == true) {
                                    viewModel.toast.value = "프로필사진을 수정해 주세요"
                                } else if (sub_return && sub_image) {
                                    viewModel.toast.value = "서브프로필 사진을 수정해 주세요"
                                } else {
                                    viewModel.userCompanionEditWoMan(
                                        absolutelyPathEdit(
                                            viewModel.mainImageNewUrl.value,
                                            this,
                                            "mainProfile"
                                        ),
                                        getmultipartlistEdit(
                                            viewModel.SubImageUrlList.value!!,
                                            "subProfile"
                                        ),

                                        )
                                }


                            }

                        }
                    }




                } else {
                    if (view.equals("퀵유저")) {
                        if (type == "OWNER") {
                            viewModel.type.value = "OWNER"
                            viewModel.setQuickUserJoinOwner(
                                absolutelyPath(viewModel.mainImageUrl.value!!, this, "mainProfile"),
                                getmultipartlist(viewModel.SubImageUrlList.value!!, "SubImage"),
                                absolutelyPath(viewModel.carimage.value!!, this, "mainCar"),
                                absolutelyPath(viewModel.insuranceimage.value, this, "insurance"),
                                application
                            )
                        } else if (type == "PASSENGERMAN") {
                            viewModel.type.value = "PASSENGER"
                            viewModel.setQuickUserJoinPassenger(
                                absolutelyPath(viewModel.mainImageUrl.value!!, this, "mainProfile"),
                                getmultipartlist(viewModel.SubImageUrlList.value!!, "SubImage"),
                                absolutelyPath(viewModel.income.value, this, "income"),
                                application
                            )
                        } else if (type == "PASSENGERWOMAN") {

                            viewModel.type.value = "PASSENGER"
                            viewModel.setQuickUserJoinWomanPassenger(
                                absolutelyPath(viewModel.mainImageUrl.value!!, this, "mainProfile"),
                                getmultipartlist(viewModel.SubImageUrlList.value!!, "SubImage"),
                                application
                            )
                        }
                    } else {
                        if (type == "OWNER") {
                            viewModel.type.value = "OWNER"
                            viewModel.setJoinOwner(
                                absolutelyPath(viewModel.mainImageUrl.value!!, this, "mainProfile"),
                                getmultipartlist(viewModel.SubImageUrlList.value!!, "SubImage"),
                                absolutelyPath(viewModel.carimage.value!!, this, "mainCar"),
                                absolutelyPath(viewModel.insuranceimage.value, this, "insurance"),
                                application
                            )
                        } else if (type == "PASSENGERMAN") {
                            viewModel.type.value = "PASSENGER"
                            viewModel.setJoinPassengerMan(
                                absolutelyPath(viewModel.mainImageUrl.value!!, this, "mainProfile"),
                                getmultipartlist(viewModel.SubImageUrlList.value!!, "SubImage"),
                                absolutelyPath(viewModel.income.value, this, "income"),
                                application
                            )
                        } else if (type == "PASSENGERWOMAN") {
                            for (i in viewModel.SubImageUrlList.value!!) {

                            }
                            viewModel.type.value = "PASSENGER"
                            viewModel.setJoinPassengerWoman(
                                absolutelyPath(viewModel.mainImageUrl.value!!, this, "mainProfile"),
                                getmultipartlist(viewModel.SubImageUrlList.value!!, "SubImage"),
                                application
                            )
                        }
                    }

                }


//            var i = Intent(this,SubscriptionApplicationCompleteActivity::class.java)
//            startActivity(i)
            }
        }




        binding!!.llMainImage.setOnClickListener {
            imageStartIntent("메인이미지")

        }
        binding!!.llSubImage1.setOnClickListener {
            imageStartIntent("서브이미지1")

        }
        binding!!.llSubImage2.setOnClickListener {
            imageStartIntent("서브이미지2")

        }
        binding!!.llSubImage3.setOnClickListener {
            imageStartIntent("서브이미지3")
        }

        binding!!.ivMainImageCancel.setOnClickListener {
//            viewModel.mainImageUrl.value = null
//            viewModel.mainImageNewUrl.value = null
//            viewModel.mainCheckReturn.value = ""
//            binding.viewBlack.visibility = View.GONE
//            Glide.with(binding.ivMainImage).load(R.color.trn).into(binding.ivMainImage)
//            Glide.with(binding.ivMainImageCancel).load(R.drawable.ic_cell)
//                .into(binding.ivMainImageCancel)
//            bottomTextCheck()
        }
        binding!!.ivSubImageCancel1.setOnClickListener {
//            imageRemove(0)
//            bottomTextCheck()
//            binding.viewBlackSub1.visibility = View.GONE
        }
        binding!!.ivSubImageCancel2.setOnClickListener {
//            imageRemove(1)
//            bottomTextCheck()
//            binding.viewBlackSub2.visibility = View.GONE
        }
        binding!!.ivSubImageCancel3.setOnClickListener {
            imageRemove(2)
            bottomTextCheck()
            binding.viewBlackSub3.visibility = View.GONE
        }

        binding!!.ivGuide.setOnClickListener {
            var i = Intent(this, GuideActivity::class.java)
            startActivity(i)
            overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        }
        binding!!.tvGuide.setOnClickListener {
            var i = Intent(this, GuideActivity::class.java)
            startActivity(i)
            overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        }

    }

    private fun imageStartIntent(s: String) {
        var i = Intent(this, GuideActivity::class.java)
        i.putExtra("type", s)
        resultLauncherIntent.launch(i)
    }

    var resultLauncherIntent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                if (data != null) {
                    var type = data.getStringExtra("type")
                    when (type) {
                        "메인이미지" -> {
                            ImagePicker.with(this@MyJoinInfomationActivity)
                                .galleryOnly()
                                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)
                                .crop(
                                    4f,
                                    4f
                                )//Final image resolution will be less than 1080 x 1080(Optional)
                                .createIntent { intent ->
                                    resultLauncherimage.launch(intent)
                                }
                        }
                        "서브이미지1" -> {
                            getSubImage(1)
                        }
                        "서브이미지2" -> {
                            getSubImage(2)
                        }
                        "서브이미지3" -> {
                            getSubImage(3)
                        }
                    }

                }

            }
        }

    private fun getSubImage(posi: Int) {
        ImagePicker.with(this@MyJoinInfomationActivity)
            .compress(1024)         //Final image size will be less than 1 MB(Optional)
            .maxResultSize(1080, 1080)
            .galleryOnly()
            .crop(
                4f,
                4f
            )//Final image resolution will be less than 1080 x 1080(Optional)
            .createIntent { intent ->
                when (posi) {
                    1 -> resultLauncherSubImage1.launch(intent)
                    2 -> resultLauncherSubImage2.launch(intent)
                    3 -> resultLauncherSubImage3.launch(intent)
                }

            }
    }


    private fun imageRemove(posi: Int) {
        if (viewModel.SubImageUrlList.value?.size!! >= posi + 1) {


            var data = viewModel.SubImageUrlList.value!!.get(posi)
            for ((count, i) in original_sub_image!!.withIndex()) {
                if (i == data) {

                    viewModel.delete_sub_profile.value?.set(count, i)

//                        viewModel.realSubImagelist.value?.removeAt(posi)

                }
//                else {
//                    if (data.im_url!!.isNotEmpty()) {
//                        viewModel.delete_sub_profile.value?.set(count, i)
//                    }
//                }

            }


            viewModel.SubImageUrlList.value?.removeAt(posi)
            viewModel.SubImageUrlListSize.value = viewModel.SubImageUrlList.value?.size

            if (viewModel.SubImageUrlList.value?.size!! > 0) {
                Log.d("사이즈", viewModel.SubImageUrlList.value?.size.toString())
                when (viewModel.SubImageUrlList.value?.size!!) {
                    0 -> {
                        Glide.with(binding.ivSubImage1).load(R.color.trn).into(binding.ivSubImage1)
                        Glide.with(binding.ivSubImage2).load(R.color.trn).into(binding.ivSubImage2)
                        Glide.with(binding.ivSubImage3).load(R.color.trn).into(binding.ivSubImage3)




                        viewModel.sub1CheckReturn.value = ""
                        viewModel.sub2CheckReturn.value = ""
                        viewModel.sub3CheckReturn.value = ""
                    }
                    1 -> {
                        Glide.with(binding.ivSubImage1)
                            .load(viewModel.SubImageUrlList.value?.get(0)?.im_url)
                            .into(binding.ivSubImage1)
                        Glide.with(binding.ivSubImage2).load(R.color.trn).into(binding.ivSubImage2)
                        Glide.with(binding.ivSubImage3).load(R.color.trn).into(binding.ivSubImage3)

                        when (viewModel.SubImageUrlList.value!![0].im_return) {
                            "Y" -> {
                                viewModel.sub1CheckReturn.value = "반려"
                                binding.viewBlackSub1.visibility = View.VISIBLE
                                Glide.with(binding.ivSubImageCancel1).load(R.drawable.ic_frame_2277)
                                    .into(binding.ivSubImageCancel1)
                            }
                            "N" -> {
                                viewModel.sub1CheckReturn.value = ""
                                binding.viewBlackSub1.visibility = View.GONE
                                Glide.with(binding.ivSubImageCancel1).load(R.drawable.ic_frame_2276)
                                    .into(binding.ivSubImageCancel1)

                            }
                            "M" -> {
                                viewModel.sub1CheckReturn.value = "심사중"
                                binding.viewBlackSub1.visibility = View.VISIBLE
                                Glide.with(binding.ivSubImageCancel1).load(R.drawable.ic_cell)
                                    .into(binding.ivSubImageCancel1)
                            }
                            "E" -> {
                                viewModel.sub1CheckReturn.value = "수정중"
                                binding.viewBlackSub1.visibility = View.GONE
                                Glide.with(binding.ivSubImageCancel1).load(R.drawable.ic_cell)
                                    .into(binding.ivSubImageCancel1)
                            }

                        }

                        viewModel.sub2CheckReturn.value = ""
                        viewModel.sub3CheckReturn.value = ""

                    }
                    2 -> {
                        Glide.with(binding.ivSubImage1)
                            .load(viewModel.SubImageUrlList.value?.get(0)?.im_url)
                            .into(binding.ivSubImage1)
                        Glide.with(binding.ivSubImage2)
                            .load(viewModel.SubImageUrlList.value?.get(1)?.im_url)
                            .into(binding.ivSubImage2)
                        Glide.with(binding.ivSubImage3).load(R.color.trn).into(binding.ivSubImage3)

                        when (viewModel.SubImageUrlList.value!![0].im_return) {
                            "Y" -> {
                                viewModel.sub1CheckReturn.value = "반려"
                                binding.viewBlackSub1.visibility = View.VISIBLE
                                Glide.with(binding.ivSubImageCancel1).load(R.drawable.ic_frame_2277)
                                    .into(binding.ivSubImageCancel1)
                            }
                            "N" -> {
                                viewModel.sub1CheckReturn.value = ""
                                binding.viewBlackSub1.visibility = View.GONE
                                Glide.with(binding.ivSubImageCancel1).load(R.drawable.ic_frame_2276)
                                    .into(binding.ivSubImageCancel1)
                            }
                            "M" -> {
                                viewModel.sub1CheckReturn.value = "심사중"
                                binding.viewBlackSub1.visibility = View.VISIBLE
                                Glide.with(binding.ivSubImageCancel1).load(R.drawable.ic_cell)
                                    .into(binding.ivSubImageCancel1)
                            }
                            "E" -> {
                                viewModel.sub1CheckReturn.value = "수정중"
                                binding.viewBlackSub1.visibility = View.GONE
                                Glide.with(binding.ivSubImageCancel1).load(R.drawable.ic_cell)
                                    .into(binding.ivSubImageCancel1)
                            }
                        }
                        when (viewModel.SubImageUrlList.value!![1].im_return) {
                            "Y" -> {
                                viewModel.sub2CheckReturn.value = "반려"
                                binding.viewBlackSub2.visibility = View.VISIBLE
                                Glide.with(binding.ivSubImageCancel2).load(R.drawable.ic_frame_2277)
                                    .into(binding.ivSubImageCancel2)
                            }
                            "N" -> {
                                viewModel.sub2CheckReturn.value = ""
                                binding.viewBlackSub2.visibility = View.GONE
                                Glide.with(binding.ivSubImageCancel2).load(R.drawable.ic_frame_2276)
                                    .into(binding.ivSubImageCancel2)
                            }
                            "M" -> {
                                viewModel.sub2CheckReturn.value = "심사중"
                                binding.viewBlackSub2.visibility = View.VISIBLE
                                Glide.with(binding.ivSubImageCancel2).load(R.drawable.ic_cell)
                                    .into(binding.ivSubImageCancel2)
                            }
                            "E" -> {
                                viewModel.sub2CheckReturn.value = "수정중"
                                binding.viewBlackSub2.visibility = View.GONE
                                Glide.with(binding.ivSubImageCancel2).load(R.drawable.ic_cell)
                                    .into(binding.ivSubImageCancel2)
                            }
                        }
                        viewModel.sub3CheckReturn.value = ""

                    }

                }
            } else {
                Glide.with(binding.ivSubImage1).load(R.color.trn).into(binding.ivSubImage1)
                viewModel.sub1CheckReturn.value = ""
            }

        }

    }


    var resultLauncherimage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                if (data != null) {

                    data!!.data.let { uri ->
                        if (uri != null) {
                            viewModel.mainImageUrl.value = uri.toString()
                            viewModel.mainImageNewUrl.value = uri.toString()
                            Glide.with(binding.ivMainImage).load(uri).override(
                                Resources.getSystem().displayMetrics.widthPixels,
                                Resources.getSystem().displayMetrics.heightPixels
                            ).fitCenter().into(binding.ivMainImage)
                            if (viewModel.view.value.equals("수정")) {
                                viewModel.mainCheckReturn.value = "수정중"
                                Glide.with(binding.ivMainImageCancel).load(R.drawable.ic_cell)
                                    .into(binding.ivMainImageCancel)
                            }
                            bottomTextCheck()
                        }
                    }
                }

            }
        }


    var resultLauncherSubImage1 =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                if (data != null) {

                    data!!.data.let { uri ->
                        if (uri != null) {

                            if (viewModel.SubImageUrlList.value?.size!! < 1) {

                                SubImage = SubImage("", "", "", 0, "", uri.toString(), 0, "", "")
                                viewModel.SubImageUrlList.value?.add(SubImage)

                                viewModel.SubImageUrlListSize.value =
                                    viewModel.SubImageUrlList.value?.size!!

                                when (viewModel.SubImageUrlList.value?.size) {
                                    1 -> {
                                        Glide.with(binding.ivSubImage1).load(uri)
                                            .into(binding.ivSubImage1)
                                        if (viewModel.view.value.equals("수정")) {
                                            viewModel.sub1CheckReturn.value = "수정중"
                                            Glide.with(binding.ivSubImageCancel1)
                                                .load(R.drawable.ic_cell)
                                                .into(binding.ivSubImageCancel1)
                                        }
                                    }
                                    2 -> {
                                        Glide.with(binding.ivSubImage2).load(uri)
                                            .into(binding.ivSubImage2)
                                        if (viewModel.view.value.equals("수정")) {
                                            viewModel.sub2CheckReturn.value = "수정중"
                                            Glide.with(binding.ivSubImageCancel2)
                                                .load(R.drawable.ic_cell)
                                                .into(binding.ivSubImageCancel2)
                                        }
                                    }

                                    3 -> {
                                        Glide.with(binding.ivSubImage3).load(uri)
                                            .into(binding.ivSubImage3)
                                        if (viewModel.view.value.equals("수정")) {
                                            viewModel.sub3CheckReturn.value = "수정중"
                                            Glide.with(binding.ivSubImageCancel3)
                                                .load(R.drawable.ic_cell)
                                                .into(binding.ivSubImageCancel3)
                                        }
                                    }


                                }


                                viewModel.SubImageUrlListSize.value =
                                    viewModel.SubImageUrlList.value?.size!!

                            } else {


                                Glide.with(binding.ivSubImage1).load(uri)
                                    .into(binding.ivSubImage1)
                                if (viewModel.view.value.equals("수정")) {
                                    viewModel.sub1CheckReturn.value = "수정중"
                                    Glide.with(binding.ivSubImageCancel1).load(R.drawable.ic_cell)
                                        .into(binding.ivSubImageCancel1)

                                    var data = viewModel.SubImageUrlList.value!!.get(0)
                                    for ((count, i) in original_sub_image!!.withIndex()) {
                                        if (i == data) {
                                            viewModel.delete_sub_profile.value?.set(count, i)
                                        }
                                    }
                                    viewModel.SubImageUrlList.value?.get(0)?.im_url = uri.toString()
                                    viewModel.SubImageUrlList.value?.get(0)?.im_return = "M"
                                } else {

                                    SubImage =
                                        SubImage("", "", "", 0, "", uri.toString(), 0, "", "")
                                    viewModel.SubImageUrlList.value?.set(0, SubImage)

                                }
                            }
                            bottomTextCheck()

                        }
                    }
                }

            }
        }

    var resultLauncherSubImage2 =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                if (data != null) {

                    data!!.data.let { uri ->
                        if (uri != null) {
                            Log.d("서브이미지", viewModel.SubImageUrlList.value?.size!!.toString())
                            if (viewModel.SubImageUrlList.value?.size!! < 2) {
                                SubImage = SubImage("", "", "", 0, "", uri.toString(), 0, "", "")
                                viewModel.SubImageUrlList.value?.add(SubImage)
                                viewModel.SubImageUrlListSize.value = viewModel.SubImageUrlList.value?.size!!

                                when (viewModel.SubImageUrlList.value?.size) {
                                    1 -> {
                                        Glide.with(binding.ivSubImage1).load(uri)
                                            .into(binding.ivSubImage1)
                                        if (viewModel.view.value.equals("수정")) {
                                            viewModel.sub1CheckReturn.value = "수정중"
                                            Glide.with(binding.ivSubImageCancel1)
                                                .load(R.drawable.ic_cell)
                                                .into(binding.ivSubImageCancel1)

                                        }
                                    }
                                    2 -> {
                                        Glide.with(binding.ivSubImage2).load(uri)
                                            .into(binding.ivSubImage2)
                                        if (viewModel.view.value.equals("수정")) {
                                            viewModel.sub2CheckReturn.value = "수정중"
                                            Glide.with(binding.ivSubImageCancel2)
                                                .load(R.drawable.ic_cell)
                                                .into(binding.ivSubImageCancel2)
                                        }
                                    }

                                    3 -> {
                                        Glide.with(binding.ivSubImage3).load(uri)
                                            .into(binding.ivSubImage3)
                                        if (viewModel.view.value.equals("수정")) {
                                            viewModel.sub3CheckReturn.value = "수정중"
                                            Glide.with(binding.ivSubImageCancel3)
                                                .load(R.drawable.ic_cell)
                                                .into(binding.ivSubImageCancel3)
                                        }
                                    }


                                }
                            } else {

                                Glide.with(binding.ivSubImage2).load(uri)
                                    .into(binding.ivSubImage2)
                                if (viewModel.view.value.equals("수정")) {
                                    viewModel.sub2CheckReturn.value = "수정중"
                                    Glide.with(binding.ivSubImageCancel2).load(R.drawable.ic_cell)
                                        .into(binding.ivSubImageCancel2)
                                    var data = viewModel.SubImageUrlList.value!!.get(1)
                                    for ((count, i) in original_sub_image!!.withIndex()) {
                                        if (i == data) {
                                            viewModel.delete_sub_profile.value?.set(count, i)
                                        }
                                    }
                                    viewModel.SubImageUrlList.value?.get(1)?.im_url = uri.toString()
                                    viewModel.SubImageUrlList.value?.get(1)?.im_return = "M"
                                } else {
                                    SubImage =
                                        SubImage("", "", "", 0, "", uri.toString(), 0, "", "")
                                    viewModel.SubImageUrlList.value?.set(1, SubImage)
                                }
                            }
                            bottomTextCheck()

                        }
                    }
                }

            }
        }
    var resultLauncherSubImage3 =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                if (data != null) {

                    data!!.data.let { uri ->
                        if (uri != null) {
                            SubImage = SubImage("", "", "", 0, "", uri.toString()!!, 0, "", "")
                            Log.d("서브이미지", viewModel.SubImageUrlList.value?.size!!.toString())
                            if (viewModel.SubImageUrlList.value?.size!! < 3) {

                                viewModel.SubImageUrlList.value?.add(SubImage)
                                viewModel.SubImageUrlListSize.value =
                                    viewModel.SubImageUrlList.value?.size!!

                                when (viewModel.SubImageUrlList.value?.size) {
                                    1 -> {
                                        Glide.with(binding.ivSubImage1).load(uri)
                                            .into(binding.ivSubImage1)
                                        if (viewModel.view.value.equals("수정")) {
                                            viewModel.sub1CheckReturn.value = "수정중"
                                            Glide.with(binding.ivSubImageCancel1)
                                                .load(R.drawable.ic_cell)
                                                .into(binding.ivSubImageCancel1)
                                        }

                                    }
                                    2 -> {
                                        Glide.with(binding.ivSubImage2).load(uri)
                                            .into(binding.ivSubImage2)
                                        if (viewModel.view.value.equals("수정")) {
                                            viewModel.sub2CheckReturn.value = "수정중"
                                            Glide.with(binding.ivSubImageCancel2)
                                                .load(R.drawable.ic_cell)
                                                .into(binding.ivSubImageCancel2)
                                        }

                                    }

                                    3 -> {
                                        Glide.with(binding.ivSubImage3).load(uri)
                                            .into(binding.ivSubImage3)
                                        if (viewModel.view.value.equals("수정")) {
                                            viewModel.sub3CheckReturn.value = "수정중"
                                            Glide.with(binding.ivSubImageCancel3)
                                                .load(R.drawable.ic_cell)
                                                .into(binding.ivSubImageCancel3)
                                        }

                                    }


                                }


                            } else {

                                Glide.with(binding.ivSubImage3).load(uri)
                                    .into(binding.ivSubImage3)
                                if (viewModel.view.value.equals("수정")) {
                                    viewModel.sub3CheckReturn.value = "수정중"
                                    Glide.with(binding.ivSubImageCancel3).load(R.drawable.ic_cell)
                                        .into(binding.ivSubImageCancel3)
                                    var data = viewModel.SubImageUrlList.value!!.get(2)
                                    for ((count, i) in original_sub_image!!.withIndex()) {
                                        if (i == data) {
                                            viewModel.delete_sub_profile.value?.set(count, i)
                                        }
                                    }
                                    viewModel.SubImageUrlList.value?.get(2)?.im_url = uri.toString()
                                    viewModel.SubImageUrlList.value?.get(2)?.im_return = "M"
                                } else {
                                    SubImage =
                                        SubImage("", "", "", 0, "", uri.toString(), 0, "", "")
                                    viewModel.SubImageUrlList.value?.set(2, SubImage)
                                }
                            }
                            bottomTextCheck()

                        }
                    }
                }

            }
        }

    fun absolutelyPath(path: String?, context: Context, name: String): MultipartBody.Part {

        var result = path?.toUri()?.let { getRealPathFromUriForImagesAndVideo(it) }
        val file = File(result)
        var fileName = path
        fileName = fileName

        var requestBody: RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        var body: MultipartBody.Part =
            MultipartBody.Part.createFormData(name, fileName, requestBody)

        return body!!
    }

    fun absolutelyPathEdit(path: String?, context: Context, name: String): MultipartBody.Part {
        var body: MultipartBody.Part? = null
        var editName = name



        if (path != null) {
            if (path.isNotEmpty()) {
                if (!path.equals("https:")) {
                    try {
                        var result = path?.toUri().let { getRealPathFromUriForImagesAndVideo(it!!) }
                        val file = File(result)
                        var fileName = path
                        fileName = fileName
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        var requestBody: RequestBody =
                            RequestBody.create("image/*".toMediaTypeOrNull(), file)
                        body = MultipartBody.Part.createFormData(editName, fileName, requestBody)
                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                    }
                } else {
                    val attachmentEmpty: RequestBody =
                        RequestBody.create("text/plain".toMediaTypeOrNull(), "")
                    body = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty)
                }

            } else {
                val attachmentEmpty: RequestBody =
                    RequestBody.create("text/plain".toMediaTypeOrNull(), "")
                body = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty)
            }


        } else {
            val attachmentEmpty: RequestBody =
                RequestBody.create("text/plain".toMediaTypeOrNull(), "")
            body = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty)
        }



        return body!!
    }


    fun getmultipartlistEdit(
        list: ArrayList<SubImage>,
        type: String
    ): ArrayList<MultipartBody.Part> {


        var mlist = ArrayList<MultipartBody.Part>()
        for ((count, i) in list.withIndex()) {
            var path = i.im_url.toString()
            var uri = Uri.parse(path.toString())
            if (path != null) {

                if (!path.startsWith("http")) {
                    var pathmain = getRealPathFromUriForImagesAndVideo(uri!!)
                    var policefile = File(pathmain)
                    if (!policefile.exists()) {
                        policefile.mkdirs();
                    }
                    var uri = Uri.parse(path)
                    var body2: MultipartBody.Part? = null
                    if (path.toString().endsWith("jpg") || path.toString().endsWith("png")) {
                        if (type.equals("subProfile")) {
                            body2 = getmultipart(
                                policefile,
                                "SubImage" + "[$count]",
                                ".jpg",
                                "image/jpeg",
                                "subProfile"
                            )
                        }
                        mlist.add(body2!!)
                    }

                } else {
                    var body2: MultipartBody.Part? = null
                    val attachmentEmpty: RequestBody =
                        RequestBody.create("text/plain".toMediaTypeOrNull(), "")
                    body2 = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty)
                    mlist.add(body2!!)

                }


            } else {
                var body2: MultipartBody.Part? = null
                val attachmentEmpty: RequestBody =
                    RequestBody.create("text/plain".toMediaTypeOrNull(), "")
                body2 = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty)
                mlist.add(body2!!)


            }
        }
        return mlist
    }

    override fun toString(): String {
        return GsonBuilder().create().toJson(this, MyJoinInfomationActivity::class.java)
    }

    fun getmultipartlist(
        list: ArrayList<SubImage>,
        type: String
    ): ArrayList<MultipartBody.Part> {


        var mlist = ArrayList<MultipartBody.Part>()
        for (i in list) {
            var path = i.im_url.toString()
            var uri = Uri.parse(path.toString())
            if (!path.startsWith("http") && path != null) {
                var pathmain = getRealPathFromUriForImagesAndVideo(uri!!)
                var policefile = File(pathmain)
                var uri = Uri.parse(path)
                var body2: MultipartBody.Part? = null
                if (path.toString().endsWith("jpg") || path.toString().endsWith("png")) {
                    if (type.equals("SubImage")) {

                        body2 = getmultipart(
                            policefile,
                            "SubImage" + mlist.size,
                            ".jpg",
                            "image/jpeg",
                            "subProfile"
                        )
                    } else {
                        body2 = getmultipart(
                            policefile,
                            "MainCar" + mlist.size,
                            ".jpg",
                            "image/jpeg",
                            "mainCar"
                        )
                    }
                    mlist.add(body2)
                }


            }
        }
        return mlist
    }

    fun getmultipart(
        file: File,
        name: String,
        type: String,
        typeset: String,
        formname: String
    ): MultipartBody.Part {
        var time = getnowdatestring().replace("-", "").replace(":", "")
        var name1 = name + time + type
        var requestBody: RequestBody = file.asRequestBody(typeset.toMediaTypeOrNull())
        var body: MultipartBody.Part =
            MultipartBody.Part.createFormData(formname, name1, requestBody)
        return body
    }


    fun getRealPathFromUriForImagesAndVideo(contentUri: Uri): String? {
        var cursor: Cursor? = null


        return try {
            var proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = contentResolver.query(contentUri, proj, null, null, null)
            var column_index: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor!!.moveToFirst()
            cursor!!.getString(column_index)
        } catch (e: Exception) {
            contentUri.path
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
    }

    fun getnowdatestring(): String {
        var now = System.currentTimeMillis()
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var d = Date(now)
        var nowdate = format.format(d)
        return nowdate
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(
            com.supercarlounge.supercar.R.anim.fadein,
            com.supercarlounge.supercar.R.anim.fadeout
        )
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
}
