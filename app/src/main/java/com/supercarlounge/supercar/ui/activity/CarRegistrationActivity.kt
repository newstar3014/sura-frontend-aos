package com.supercarlounge.supercar.ui.activity


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.transition.Slide
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.WindowManager
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.supercarlounge.supercar.CustomTypefaceSpan
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.*
import com.supercarlounge.supercar.databinding.*
import com.supercarlounge.supercar.dialog.ComprehensiveInsuranceDialog
import com.supercarlounge.supercar.viewmodel.CarRegistrationViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.customview.BaseActivitiy
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class CarRegistrationActivity : BaseActivitiy() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityCarRegistrationBinding
    private lateinit var viewModel: CarRegistrationViewModel
    private var comprehensiveInsuranceDialog: ComprehensiveInsuranceDialog? = null
    private var application: MainApplication? = null
    var u_name = ""
    var u_gender = ""
    var u_birthday = ""
    var u_phone = ""
    var selectCarBrand = ""
    var selectCarModel = ""
    var selectCarSeries = ""
    var type = ""
    var view = ""


    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        with(window) {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            // set an slide transition
            enterTransition = Slide(Gravity.END)
            exitTransition = Slide(Gravity.START)
        }
        viewModel = ViewModelProvider(this).get(CarRegistrationViewModel::class.java)
        binding = ActivityCarRegistrationBinding.inflate(layoutInflater)
        binding!!.viewModel = viewModel
        binding!!.setLifecycleOwner(this)
        application =applicationContext as MainApplication
        setContentView(binding.root)
        val SS = SpannableStringBuilder(binding!!.tvExplanation.text)

        val typeface =
            ResourcesCompat.getFont(this, com.supercarlounge.supercar.R.font.inter_bold)
        SS.setSpan(
            typeface?.let {
                CustomTypefaceSpan("", it)
            }, 16, 21, Spanned.SPAN_EXCLUSIVE_INCLUSIVE

        )
        SS.setSpan(
            typeface?.let {
                CustomTypefaceSpan("", it)
            }, 28, 34, Spanned.SPAN_EXCLUSIVE_INCLUSIVE

        )
        SS.setSpan(
            typeface?.let {
                CustomTypefaceSpan("", it)
            }, 38, 47, Spanned.SPAN_EXCLUSIVE_INCLUSIVE

        )
        binding!!.tvExplanation.text = SS

        clickListeners()
        observers()


//        if (intent.getParcelableExtra("userData") != null) {
        viewModel.userdata.value = intent.getParcelableExtra("userData")
        Log.d("받음", viewModel.userdata.value.toString())

//        }
        if (intent.getSerializableExtra("userCarData") != null) {

            viewModel.my_car_data_list.value =
                intent.getSerializableExtra("userCarData") as ArrayList<ArrayList<UserInformationCarData>>
            Log.d("확인", intent.getSerializableExtra("userCarData").toString())
        }
        var carBrandImage =intent.getStringExtra("CarBrand_Image").toString()
        if (!carBrandImage.isNullOrEmpty()){
            Glide.with(this).load(carBrandImage).centerInside().into(binding.ivBrand)
        }
        viewModel.view.value = intent.getStringExtra("view").toString()
        type = intent.getStringExtra("type").toString()
        view = intent.getStringExtra("view").toString()



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
        if (intent.getSerializableExtra("CarBrandListData") != null) {
            viewModel.carList =
                intent.getSerializableExtra("CarBrandListData") as ArrayList<CarBrandListData>
        }
        if (intent.getSerializableExtra("CarModelListData") != null) {
            viewModel.carSeriesList =
                intent.getSerializableExtra("CarModelListData") as ArrayList<CarModelListData>
        }
        if (intent.getSerializableExtra("CarSubModelListData") != null) {
            viewModel.carDetailsSeriesList =
                intent.getSerializableExtra("CarSubModelListData") as ArrayList<CarSubModelListData>
        }

        if (intent.getSerializableExtra("completeDataList") != null) {
            viewModel.companionDataList.value =
                intent.getSerializableExtra("completeDataList") as ArrayList<CompanionData>
        }

        selectCarBrand = intent.getStringExtra("CarBrand").toString()
        selectCarModel = intent.getStringExtra("CarModel").toString()
        selectCarSeries = intent.getStringExtra("CarSeries").toString()
        viewModel.view.value = intent.getStringExtra("view")
        Log.d("뷰", viewModel.view.value.toString())
        u_name = intent.getStringExtra("u_name").toString()
        u_gender = intent.getStringExtra("u_gender").toString()
        u_birthday = intent.getStringExtra("u_birthday").toString()
        u_phone = intent.getStringExtra("u_phone").toString()

        if (viewModel.view.value.equals("재등록")) {
            viewModel.tv_bottom.value = "등록하기"

            var requestOptions = RequestOptions()
            requestOptions =
                requestOptions.transforms(CenterCrop(), RoundedCorners(16))

            if (intent.getStringExtra("carImage")!= null ) {
                if (!intent.getStringExtra("carImage").toString().equals("null")) {
                    viewModel.carimage.value = intent.getStringExtra("carImage").toString()
                    Glide.with(binding.carImage).load( viewModel.carimage.value).override(250, 250)
                        .apply(requestOptions).into(binding.carImage)
                }

            }
            if (intent.getStringExtra("insuranceimage")!= null) {
                if (!intent.getStringExtra("insuranceimage").toString().equals("null")) {
                    viewModel.insuranceimage.value = intent.getStringExtra("insuranceimage").toString()
                  //  Log.d("뭐지", intent.getStringExtra("insuranceimage").toString())
                    Glide.with(binding.insuranceImage).load(viewModel.insuranceimage.value)
                        .override(250, 250).apply(requestOptions)
                        .into(binding.insuranceImage)
                }
            }




        }else if (viewModel.view.value.equals("수정")) {
            if (!type.equals("all")) {
                viewModel.tv_bottom.value = "수정하기"

            }
            var carData = viewModel.my_car_data_list.value
            Log.d("carData", carData.toString())
            if (carData != null) {
                if (carData.size > 0) {
                    var data = carData?.get(0)?.get(0)
                    var requestOptions = RequestOptions()
                    requestOptions =
                        requestOptions.transforms(CenterCrop(), RoundedCorners(16))
                    if (data!!.uc_car_main!!.isNotEmpty()) {
                        viewModel.carimage.value = data?.uc_car_main

                        Glide.with(binding.carImage).load(data?.uc_car_main).override(250, 250)
                            .apply(requestOptions).into(binding.carImage)

                        Glide.with(binding.carBg).load(binding.carBg).override(250, 250)
                            .apply(requestOptions)
                            .into(binding.carBg)

                        Log.d(
                            "체크용 companionDataList",
                            checkListString(
                                viewModel.companionDataList.value!!,
                                "차량 이미지"
                            ).toString()
                        )
                        Log.d("체크용 uc_return", data.uc_return.toString())
//                        if (!checkListString(viewModel.companionDataList.value!!, "차량 이미지") && data.uc_return.equals("Y")) {
//
//                        } else if (!checkListString(viewModel.companionDataList.value!!, "차량 이미지") && data.uc_return.equals("M")) {
//                            viewModel.carCheckReturn.value = "미승인"
//                        } else if (checkListString(viewModel.companionDataList.value!!, "차량 이미지") && data.uc_return.equals("N")) {
//                            viewModel.carCheckReturn.value = "반려"
//                        }else if (checkListString(viewModel.companionDataList.value!!, "차량 이미지") && data.uc_return.equals("M")) {
//                            viewModel.carCheckReturn.value = "반려"
//                        }else  if (checkListString(viewModel.companionDataList.value!!, "차량 이미지") && data.uc_return.equals("Y")) {
//                            viewModel.carCheckReturn.value = "반려"
//                        }else{
//                            viewModel.carCheckReturn.value = "미승인"
//                        }

                        when (data.uc_car_main_yn) {

                            "Y" -> {
                                viewModel.carCheckReturn.value = "승인"
                                binding!!.llCarImage.isEnabled = false
                                binding!!.carImage.isEnabled = false
                            }
                            "M" -> {
                                viewModel.carCheckReturn.value = "심사중"
                                binding!!.llCarImage.isEnabled = false
                                binding!!.carImage.isEnabled = false
                            }
                            "N" -> {
                                viewModel.carCheckReturn.value = "반려"

                            }
                            else -> {}
                        }

                    }


                    if (data!!.uc_insur_url!!.isNotEmpty()) {
                        viewModel.insuranceimage.value = data?.uc_insur_url
                        Glide.with(binding.insuranceImage).load(data?.uc_insur_url)
                            .override(250, 250).apply(requestOptions)
                            .into(binding.insuranceImage)
                        Glide.with(binding.insuranceBg).load(binding.insuranceBg).override(250, 250)
                            .apply(requestOptions)
                            .into(binding.insuranceBg)
                        Log.d(
                            "체크용 companionDataList",
                            checkListString(
                                viewModel.companionDataList.value!!,
                                "종합보험 가입증서"
                            ).toString()
                        )
                        Log.d("체크용 uc_return", data.uc_return.toString())
//                        if (!checkListString(viewModel.companionDataList.value!!, "종합보험 가입증서") && data.uc_return.equals("Y")) {
//                            viewModel.insuerCheckReturn.value = "승인"
//                        } else if (!checkListString(viewModel.companionDataList.value!!, "종합보험 가입증서") && data.uc_return.equals("M")) {
//                            viewModel.insuerCheckReturn.value = "미승인"
//                        } else if (checkListString(viewModel.companionDataList.value!!, "종합보험 가입증서") && data.uc_return.equals("N")) {
//                            viewModel.insuerCheckReturn.value = "반려"
//                        }else if (checkListString(viewModel.companionDataList.value!!, "종합보험 가입증서") && data.uc_return.equals("M")) {
//                            viewModel.insuerCheckReturn.value = "반려"
//                        }else if (checkListString(viewModel.companionDataList.value!!, "종합보험 가입증서") && data.uc_return.equals("Y")) {
//                            viewModel.insuerCheckReturn.value = "반려"
//                        }else  {
//                            viewModel.insuerCheckReturn.value = "미승인"
//                        }

                        when (data.uc_insur_url_yn) {

                            "Y" -> {
                                viewModel.insuerCheckReturn.value = "승인"
                                binding!!.llInsurance.isEnabled = false
                                binding!!.insuranceImage.isEnabled = false
                            }
                            "M" -> {
                                viewModel.insuerCheckReturn.value = "심사중"
                                binding!!.llInsurance.isEnabled = false
                                binding!!.insuranceImage.isEnabled = false
                            }
                            "N" -> {
                                viewModel.insuerCheckReturn.value = "반려"


                            }
                            else -> {}
                        }

                    }


                        if(viewModel.insuranceimage.value != null && viewModel.carimage.value != null ) {
                            viewModel.stateBottomText.value = true
                        }


                }


            }
        }else{
            if (!application!!.carImageLog){
                application.let {
                    it?.joinLog(this,"차량사진 등록",u_name)
                    it?.carImageLog = true
                }

            }
        }
    }

    private fun observers() {
        viewModel.apiCall.observe(this, Observer {
            it?.let {
                if (it){
                    val i = Intent(this@CarRegistrationActivity, SubscriptionApplicationCompleteActivity::class.java)
                    i.putExtra("u_admin","M")
                    i.putExtra("u_seq",viewModel.userdata.value?.u_seq.toString())
                    Log.d("대기u_seq", viewModel.userdata.value?.u_seq.toString())
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(i)
                  //  overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                }
            }
        })

    }

    private fun checkListString(list: ArrayList<CompanionData>, s: String): Boolean {

        for (i in list) {
            if (i.ur_type.equals(s)) {
                return true
            }
        }
        return false
    }

    private fun setUrl(url: kotlin.String) {

    }

    @SuppressLint("DefaultLocale", "IntentReset")
    private fun clickListeners() {
        binding.llOwner.setOnClickListener {
//            var i = Intent(this,MyJoinInfomationActivity::class.java)
//            startActivity(i)

            ImagePicker.with(this)
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)
                .galleryOnly()
                .crop(
                    4f,
                    4f
                )//Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    resultLauncherimageCarimage.launch(intent)
                }
        }

        binding.tvComprehensiveInsurance.setOnClickListener {
            comprehensiveInsuranceDialog = ComprehensiveInsuranceDialog(this, 0)
            comprehensiveInsuranceDialog!!.show(this.supportFragmentManager, "")
        }

        binding!!.carImage.setOnClickListener {
            ImagePicker.with(this)
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)
                .galleryOnly()
                .crop(
                    4f,
                    4f
                )//Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    resultLauncherimageCarimage.launch(intent)
                }

        }
        binding!!.llInsurance.setOnClickListener {
            ImagePicker.with(this)
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)
                .galleryOnly()
                .crop(
                    4f,
                    4f
                )//Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    resultLauncherimageInsuranceimage.launch(intent)
                }
        }

        binding!!.insuranceImage.setOnClickListener {
            ImagePicker.with(this)
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)
                .galleryOnly()
                .crop(
                    4f,
                    4f
                )//Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    resultLauncherimageInsuranceimage.launch(intent)
                }

        }

        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.bottomBar.setOnClickListener {

                if (viewModel.view.value.equals("재등록")) {
                    if (viewModel.carimage.value != null && viewModel.insuranceimage.value != null) {
                        var i = Intent()
                        i.putExtra("carImage", viewModel.carimage.value)
                        i.putExtra("insuranceimage", viewModel.insuranceimage.value)
                        this.setResult(Activity.RESULT_OK, i)
                        this.finish()
                    }else{
                        Toast.makeText(this, "차량관련 사진을 빠짐없이 등록해주세요", Toast.LENGTH_SHORT).show()
                    }

                }else{
                    var i = Intent(this, MyJoinInfomationActivity::class.java)
                    if (type.equals("all")) {
                        if (application?.userCarData?.count() != 0 ) {
                            var carlist = application?.userCarData?.get(0)
                            if (carlist?.size != 0){
                                var m_uc_car_main_yn = carlist?.get(0)?.uc_car_main_yn
                                var m_uc_insur_url_yn = carlist?.get(0)?.uc_insur_url_yn
                                if (m_uc_car_main_yn.isNullOrEmpty() == false) {
                                    if (viewModel.carimage.value!!.startsWith("http") && m_uc_car_main_yn.equals(
                                            "N"
                                        )
                                    ) {
                                        Toast.makeText(this, "차량 사진을 수정해주세요", Toast.LENGTH_SHORT).show()
                                        return@setOnClickListener
                                    }
                                }
                                if (m_uc_insur_url_yn.isNullOrEmpty() == false) {
                                    if (viewModel.insuranceimage.value!!.startsWith("http") && m_uc_insur_url_yn.equals(
                                            "N"
                                        )
                                    ) {
                                        Toast.makeText(this, "보험 사진을 수정해주세요", Toast.LENGTH_SHORT).show()
                                        return@setOnClickListener
                                    }
                                }
                            }

                        }
                        if (viewModel.carimage.value.isNullOrEmpty() == true ){
                            Toast.makeText(this, "차량 사진을 등록해주세요", Toast.LENGTH_SHORT).show()
                        }else if (viewModel.insuranceimage.value.isNullOrEmpty() == true ) {
                            Toast.makeText(this, "보험 사진을 등록해주세요", Toast.LENGTH_SHORT).show()
                        }

                        else{
                            i.putExtra("nickname", viewModel.nickname.value)
                            i.putExtra("CharmList", viewModel.resultState2Array)
                            i.putExtra("musicTasteList", viewModel.resultState3Array)
                            i.putExtra("height", viewModel.resultState4Array)
                            i.putExtra("job", viewModel.resultState5Array)
                            i.putExtra("regin", viewModel.resultState6Array)
                            i.putExtra("driveStyle", viewModel.resultState7Array)
                            i.putExtra("CarBrand", selectCarBrand)
                            i.putExtra("CarModel", selectCarModel)
                            i.putExtra("CarSeries", selectCarSeries)
                            i.putExtra("carimage", viewModel.carimage.value)
                            i.putExtra("insuranceimage", viewModel.insuranceimage.value)
                            i.putExtra("type", "OWNER")
                            i.putExtra("u_name", u_name)
                            i.putExtra("u_gender", u_gender)
                            i.putExtra("u_birthday", u_birthday)
                            i.putExtra("u_phone", u_phone)
                            if (view.equals("퀵유저")){
                                i.putExtra("view", "퀵유저")
                            }
                            if (viewModel.view.value.equals("수정")) {
                                var list :ArrayList<UserInformationData> = arrayListOf()
                                list.add( viewModel.userdata.value!!)
                                i.putParcelableArrayListExtra("userData",list)
                                i.putExtra("view", viewModel.view.value)

                                var carimage = viewModel.carNewImage.value
                                //11-21
                                if (viewModel.carNewImage.value.isNullOrEmpty())  carimage = viewModel.carNewImage.value
                                i.putExtra("carNewImage", carimage)

                                var insuimage = viewModel.insuranceNewimage.value
                                if (viewModel.carNewImage.value.isNullOrEmpty())  insuimage = viewModel.insuranceNewimage.value
                                i.putExtra("insuranceNewImage", insuimage)
                                Log.d("유저데이터", viewModel.userdata.value.toString())
                            }

                            startActivity(i)
                        }
                    } else {

                        var carData = viewModel.my_car_data_list.value
                        var data = carData?.get(0)?.get(0)
                        if (viewModel.carNewImage.value != null && viewModel.insuranceNewimage.value != null) {

                            viewModel.carAndInsuranceCall(
                                absolutelyPath(viewModel.carNewImage.value, this, "mainCar"),
                                absolutelyPath(viewModel.insuranceNewimage.value, this, "insurance")
                            )
                        } else {
                            if (data?.uc_car_main_yn.equals("N") && data?.uc_insur_url_yn.equals("N")) {
                                if (viewModel.carNewImage.value != null && viewModel.insuranceNewimage.value != null) {
                                    viewModel.carCall(
                                        absolutelyPath(viewModel.carNewImage.value, this, "mainCar")
                                    )
                                    viewModel.insuranceCall(
                                        absolutelyPath(viewModel.insuranceNewimage.value, this, "insurance")
                                    )
                                } else {
                                    Toast.makeText(this, "반려된 사진을 모두 수정해주세요", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                if (data?.uc_car_main_yn.equals("N")) {
                                    if (viewModel.carNewImage.value != null) {
                                        viewModel.carCall(
                                            absolutelyPath(viewModel.carNewImage.value, this, "mainCar")
                                        )
                                    } else {
                                        Toast.makeText(this, "차량 사진을 수정해주세요", Toast.LENGTH_SHORT).show()
                                    }
                                }else if (data?.uc_insur_url_yn.equals("N")) {
                                    if (viewModel.insuranceNewimage.value != null) {
                                        viewModel.insuranceCall(
                                            absolutelyPath(
                                                viewModel.insuranceNewimage.value,
                                                this,
                                                "insurance"
                                            )
                                        )
                                    }else{
                                        Toast.makeText(this, "보험 사진을 수정해주세요", Toast.LENGTH_SHORT).show()
                                    }
                                } else {

                                }


                            }


                        }


                    }
                }

        }

    }


    var resultLauncherimageCarimage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                if (data != null) {

                    data!!.data.let { uri ->
                        if (uri != null) {
                            viewModel.carNewImage.value = uri.toString()
                            viewModel.carimage.value = uri.toString()
                            var requestOptions = RequestOptions()
                            requestOptions =
                                requestOptions.transforms(CenterCrop(), RoundedCorners(16))
                            Glide.with(binding.carImage).load(uri)
                                .apply(requestOptions)
                                .into(binding.carImage)
//                            Glide.with(binding.carBg)
//                                .load(R.color.black_check_80)
////                                .apply(requestOptions)
//                                .into(binding.carBg)
                            viewModel.carCheckReturn.value = "수정"

                            if (!viewModel.view.equals("수정")){
                                if(viewModel.insuranceimage.value != null && viewModel.carimage.value != null ) {
                                    viewModel.stateBottomText.value = true
                                }
                            }
                        }
                    }
                }

            }
        }


    var resultLauncherimageInsuranceimage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                if (data != null) {

                    data!!.data.let { uri ->
                        if (uri != null) {
                            viewModel.insuranceNewimage.value = uri.toString()
                            viewModel.insuranceimage.value= uri.toString()

                            var requestOptions = RequestOptions()
                            requestOptions =
                                requestOptions.transforms(CenterCrop(), RoundedCorners(16))
                            Glide.with(binding.insuranceImage).load(uri).apply(requestOptions)
                                .into(binding.insuranceImage)

//                            Glide.with(binding.insuranceBg)
//                                .load(R.color.black_check_80)
//                                .apply(requestOptions)
//                                .into(binding.insuranceBg)
                            viewModel.insuerCheckReturn.value = "수정"

                            if (!viewModel.view.equals("수정")){
                                if(viewModel.insuranceimage.value != null && viewModel.carimage.value != null ) {
                                    viewModel.stateBottomText.value = true
                                }
                            }

                        }
                    }
                }

            }
        }

    override fun onPause() {
        super.onPause()
       // overridePendingTransition(R.anim.fadein, R.anim.fadeout)
    }

    override fun finish() {
        super.finish()
      //  overridePendingTransition(
      //      com.supercarlounge.supercar.R.anim.fadein,
      //      com.supercarlounge.supercar.R.anim.fadeout
     //   )
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

    fun absolutelyPath(path: String?, context: Context, name: String): MultipartBody.Part {

        var result = path?.toUri()?.let { getRealPathFromUriForImagesAndVideo(it) }
        val file = File(result)
        var fileName = path
        fileName = fileName

        var requestBody: RequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        var body: MultipartBody.Part =

            MultipartBody.Part.createFormData(name, fileName, requestBody)

        return body!!
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
}
