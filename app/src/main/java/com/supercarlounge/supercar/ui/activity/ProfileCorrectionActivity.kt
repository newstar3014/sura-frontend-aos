package com.supercarlounge.supercar.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.CommentAdapter
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.data.*
import com.supercarlounge.supercar.databinding.ActivityProfileCorrectionBinding
import com.supercarlounge.supercar.dialog.ConfirmDilaog
import com.supercarlounge.supercar.dialog.OneButtonSetDialog
import com.supercarlounge.supercar.dialog.ProfileLikeBaseDialog
import com.supercarlounge.supercar.dialog.TwoButtonDialog
import com.supercarlounge.supercar.enumset.ConfirmStatus
import com.supercarlounge.supercar.enumset.PopOneTypeStatus
import com.supercarlounge.supercar.enumset.PopTypeStatus
import com.supercarlounge.supercar.ui.fragment.VipLoungeFragment
import com.supercarlounge.supercar.viewmodel.ProfileCorrectionModel

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class ProfileCorrectionActivity : BaseActivitiy() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityProfileCorrectionBinding
    private lateinit var viewModel: ProfileCorrectionModel
    private lateinit var application: MainApplication
    private var cAdapter: CommentAdapter? = null
    private var twoButtonDialog: TwoButtonDialog? = null
    private var drivepop2: ProfileLikeBaseDialog? = null

    var SubImage = SubImageDeepData("", "")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        with(window) {
//            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
//            // set an slide transition
//            enterTransition = Slide(Gravity.END)
//            exitTransition = Slide(Gravity.START)
//        }
        viewModel = ViewModelProvider(this).get(ProfileCorrectionModel::class.java)
        binding = ActivityProfileCorrectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        application = applicationContext as MainApplication
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel

        binding!!.ivMainImage.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewRedefinition()

            }


        })
        viewModel.my_seq.value = intent.getStringExtra("u_seq")
        viewModel.view.value = intent.getStringExtra("view")
        viewModel.view.value = intent.getStringExtra("view")
        viewModel.update.value = intent.getStringExtra("update")

        clickListeners()
        observes()
        setAdapters()
        viewModel.getUserInformation(viewModel.my_seq.value!!, application)
        edtListeners()
        updateSet()
    }

    private fun updateSet() {
        if (viewModel.update.value == "Y") {
            binding.ivBack.visibility = View.INVISIBLE

        }

    }

    private fun updateDialog() {
        if (viewModel.update.value == "Y") {
            if (!viewModel.my_user_data.value?.u_tempData.isNullOrEmpty()) {
                Log.d("체크", "updateSet: ")
                var oneButtonDialog = OneButtonSetDialog(
                    this, PopOneTypeStatus.DIALOG_UPDATE_APPROVAL
                ) { type, check ->

                }
                oneButtonDialog!!.show(supportFragmentManager, "")
            }
        }
    }

    private fun edtListeners() {
        binding!!.edtAboutMe.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {
//
                viewModel.u_abutme.value = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        binding!!.edtDriveCourse.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {

                viewModel.u_drive_c.value = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        binding!!.edtDriveStyle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {
                viewModel.u_drive_w.value = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding!!.edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {
                    viewModel.email.value = p0.toString()


            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }


    private fun setAdapters() {

    }

    private fun observes() {
        viewModel!!.apicall.observe(this, Observer {
            if (it == true) {
                Toast.makeText(this, "정보가 수정되었습니다.", Toast.LENGTH_SHORT).show()
                if (viewModel.update.value == "Y") {
                    viewModel.subImageDatalist.value?.clear()
                    viewModel.SubImageUrlList.value?.clear()
                    viewModel.original_sub_image_list.value?.clear()
                    viewModel.delete_sub_image_list.value?.clear()
                    var subImageData: SubImage? = SubImage("0", "", "", 0, "", "", 0, "", "")
                    viewModel.delete_sub_image_list.value?.add(subImageData!!)
                    viewModel.delete_sub_image_list.value?.add(subImageData!!)
                    viewModel.delete_sub_image_list.value?.add(subImageData!!)
                    viewModel.getUserInformation(viewModel.my_seq.value.toString(), application)
                    var oneButtonDialog = OneButtonSetDialog(
                        this, PopOneTypeStatus.DIALOG_UPDATE_APPROVAL
                    ) { type, check ->

                    }
                    oneButtonDialog!!.show(supportFragmentManager, "")

                } else {
                    finish()
                }

            }
        })

        viewModel!!.my_user_data.observe(this, Observer {

            if (it != null) {
                if (viewModel.view.value.equals("수정")) {

//            viewModel.u_seq.value?.let { viewModel.getUserInformation(it,application)
                    var data = it

                    var tempData = it.u_tempData
                    viewModel.u_tempReturn.value = it.u_tempReturn
                    var u_drive_c = it.u_drive_c
                    var u_email = it.u_email

                    if (tempData?.contains("u_email") == true) {
                        val obj: JsonObject = JsonParser.parseString(tempData).getAsJsonObject()
                        var temp_u_email = obj.get("u_email").toString().replace("\"", "")
                         viewModel.email.value = temp_u_email
                         binding.edtEmail.setText(temp_u_email)
                    } else {
                        viewModel.email.value = u_email
                        binding.edtEmail.setText(u_email)
                    }

                    if (tempData?.contains("u_drive_c") == true) {
                        val obj: JsonObject = JsonParser.parseString(tempData).getAsJsonObject()
                        var temp_u_drive_c = obj.get("u_drive_c").toString().replace("\"", "")
                        viewModel.u_drive_c.value = temp_u_drive_c
                        binding.edtDriveCourse.setText(temp_u_drive_c)
                        viewModel.u_drive_c_yn.value = "승인중"
                        Log.d("테스트 드라이브 c1",temp_u_drive_c)
                    } else {
                        Log.d("테스트 드라이브 c2", u_drive_c.toString())
                        binding.edtDriveCourse.setText(u_drive_c)
                        viewModel.u_drive_c_yn.value = ""
                        viewModel.u_drive_c.value = u_drive_c
                    }

                    if (it.u_tempReturn?.contains("u_drive_c")==true){
                        viewModel.u_drive_c_yn.value = "반려"
                    }


                    var u_drive_w = it.u_drive_w
                    if (tempData?.contains("u_drive_w") == true) {
                        val obj: JsonObject = JsonParser.parseString(tempData).getAsJsonObject()
                        var temp_u_drive_w = obj.get("u_drive_w").toString().replace("\"", "")
                        viewModel.u_drive_w.value = temp_u_drive_w
                        viewModel.u_drive_w_yn.value = "승인중"
                        binding.edtDriveStyle.setText(temp_u_drive_w)
                        Log.d("테스트 드라이브 w1",temp_u_drive_w)
                    } else {
                        Log.d("테스트 드라이브 w2", u_drive_w.toString())
                        binding.edtDriveStyle.setText(u_drive_w)
                        viewModel.u_drive_w.value = u_drive_w
                        viewModel.u_drive_w_yn.value = ""
                    }
                    if (it.u_tempReturn?.contains("u_drive_w")==true){
                        viewModel.u_drive_w_yn.value = "반려"
                    }
                    var u_about_me = it.u_about_me
                    var u_temp_about =it.u_tempAbout
                    if (u_temp_about.isNullOrEmpty()) {
                        viewModel.u_abutme.value = u_about_me
                        binding.edtAboutMe.setText(u_about_me)
                    } else {
                        viewModel.u_abutme.value = u_temp_about
                        binding.edtAboutMe.setText(u_temp_about)
                    }
                    if (it.u_tempAbout.isNullOrEmpty()){
                        viewModel.u_abutme_yn.value = ""
                    }else{
                        viewModel.u_abutme_yn.value = "승인중"
                    }

                    if (!it.u_tempAboutReturn.isNullOrEmpty()){
                        viewModel.u_abutme_yn.value = "반려"
                    }



//                    binding.edtAboutMe.setText(data?.u_about_me)
//                    binding.edtDriveStyle.setText(data?.u_drive_w)
//                    binding.edtDriveCourse.setText(data?.u_drive_c)
//                    userEditHistory()
                    updateDialog()
                    if (data.u_type.equals("OWNER")) {
                        viewModel.cartext.value = "차량 정보 수정하기"
                        viewModel.carVisi.value = true
                    } else {
                        viewModel.cartext.value = "차량 정보 등록하기"
                        viewModel.carVisi.value = true
//                        if (viewModel.my_car_data_list.value != null) {
//                            if (viewModel.my_car_data_list.value!!.size != 0) {
//
//                                var carData = viewModel.my_car_data_list.value?.get(0)
//                                if (carData?.get(0)?.uc_income != null) {
//                                    if (carData?.get(0)?.uc_income!!.isNotEmpty()) {
//                                        viewModel.cartext.value = "소득 정보 수정하기"
//                                        viewModel.carVisi.value = true
//                                    } else {
//                                        viewModel.carVisi.value = false
//                                    }
//                                } else {
//                                    viewModel.carVisi.value = false
//                                }
//                            }
//
//
//                        }


                    }
                    var count = 0

                    Glide.with(binding.ivMainImage).load(data?.main_url).override(
                        1080,
                        1080
                    ).centerCrop().into(binding.ivMainImage)
                    if (data?.u_smoking.equals("Y")) viewModel.onclickSmoking1() else viewModel.onclickSmoking2()

                    viewModel.mainImageUrl.value = data?.main_url

                    viewModel.delete_mainImageUrl.value = viewModel.mainImageUrl.value

                    when (data?.main_return) {
                        "N" -> {
                            Glide.with(binding.ivMainImageCancel).load(R.drawable.ic_cell)
                                .into(binding.ivMainImageCancel)
                        }
                        "M" -> {
                            viewModel.mainCheckReturn.value = "심사중"
//                    Glide.with(binding.ivMainImageCancel).load(R.drawable.ic_cell).into(binding.ivMainImageCancel)
                            binding.viewBlack.visibility = View.VISIBLE
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
                    for ((count, i) in data.subImage.withIndex()) {

                        when (count) {
                            0 -> {

                                Glide.with(binding.ivSubImage1).load(i.im_url).override(
                                    720,
                                    720
                                ).centerCrop()
                                    .into(binding.ivSubImage1)
                                SubImage = SubImageDeepData(i.im_url!!, i.im_return!!)


                                when (i.im_return) {
                                    "N" -> {
                                        Glide.with(binding.ivSubImageCancel1)
                                            .load(R.drawable.ic_cell)
                                            .into(binding.ivSubImageCancel1)
                                    }
                                    "M" -> {
                                        viewModel.sub1CheckReturn.value = "심사중"
                                        Glide.with(binding.ivSubImageCancel1)
                                            .load(R.drawable.ic_cell)
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
                                Glide.with(binding.ivSubImage2).load(i.im_url)
                                    .override(
                                        720,
                                        720
                                    ).centerCrop().into(binding.ivSubImage2)
                                SubImage = SubImageDeepData(i.im_url!!, i.im_return!!)


                                when (i.im_return) {
                                    "N" -> {
                                        Glide.with(binding.ivSubImageCancel2)
                                            .load(R.drawable.ic_cell)
                                            .into(binding.ivSubImageCancel2)
                                    }
                                    "M" -> {
                                        viewModel.sub2CheckReturn.value = "심사중"
                                        Glide.with(binding.ivSubImageCancel2)
                                            .load(R.drawable.ic_cell)
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
                                Glide.with(binding.ivSubImage3).load(i.im_url).override(
                                    720,
                                    720
                                ).centerCrop()
                                    .into(binding.ivSubImage3)
                                SubImage = SubImageDeepData(i.im_url!!, i.im_return!!)

                                when (i.im_return) {
                                    "N" -> {
                                        Glide.with(binding.ivSubImageCancel3)
                                            .load(R.drawable.ic_cell)
                                            .into(binding.ivSubImageCancel3)
                                    }
                                    "M" -> {
                                        viewModel.sub3CheckReturn.value = "심사중"
                                        Glide.with(binding.ivSubImageCancel3)
                                            .load(R.drawable.ic_cell)
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

                    }
                    viewModel.SubImageUrlListSize.value = data.subImage.size


                }
            }
        })
        viewModel.apiCallOverlapCheck.observe(this, Observer {
            if (it) {

            }
        })
        viewModel.toast.observe(this, Observer { toast ->
            if (!toast.isNullOrEmpty()) {
                Toast.makeText(this, toast, Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun clickListeners() {
        binding!!.ivBack.setOnClickListener {
            if (viewModel.CheckUserData() == false) {
                drivepop2 =
                    ProfileLikeBaseDialog(this, "", PopTypeStatus.USER_EDIT_CHECK) { type, onoff ->
                        if (onoff) {

                            if (viewModel.email.value!!.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(
                                    viewModel.email.value
                                ).matches()
                            ) {
                                Toast.makeText(this, "이메일 양식을 제대로 작성해주세요.", Toast.LENGTH_SHORT).show()
                            } else if (viewModel.u_abutme.value?.length!! < 30) {
                                Toast.makeText(this, "자기소개를 30자 이상 적어주세요", Toast.LENGTH_SHORT).show()
                            } else if (viewModel.subImageDatalist.value?.size!! <= 1) {
                                Toast.makeText(this, "서브 이미지는 최소 2장이상 등록해야합니다", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                viewModel.userEditInfo(
                                    absolutelyPath(viewModel.mainImageUrl.value, this, "mainProfile"),
                                    getSubImagemultipartlist(
                                        viewModel.subImageDatalist.value!!,
                                        "CarImage"
                                    ),
                                    getmultipartlist(
                                        viewModel.my_car_data_list.value!![0]!!,
                                        "carImage"
                                    ),
                                    getmultipartlist(
                                        viewModel.my_car_data_list.value!![0]!!,
                                        "insurImage"
                                    )
                                )
                            }
                            drivepop2?.dismiss()

                        } else {
                            super.onBackPressed()
                        }

                    }
                drivepop2!!.show(supportFragmentManager, "")
            }else{
                super.onBackPressed()
            }
        }

        binding!!.llMainImage.setOnClickListener {
            var confirmDialog = ConfirmDilaog(
                this@ProfileCorrectionActivity,
                ConfirmStatus.PROFILE_EDIT,
                2,
                ""
            ) { type, isonoff ->
                if (isonoff) {
                    imageStartIntent("메인이미지")

                }
            }
            confirmDialog!!.show(supportFragmentManager, "")

//            ImagePicker.with(this)
//                .compress(1024)         //Final image size will be less than 1 MB(Optional)
//                .maxResultSize(1080, 1080)
//                .crop(
//                    4f,
//                    4f
//                )//Final image resolution will be less than 1080 x 1080(Optional)
//                .createIntent { intent ->
//                    resultLauncherimage.launch(intent)
//                }
        }
        binding!!.rlSubImage1.setOnClickListener {

            var confirmDialog = ConfirmDilaog(
                this@ProfileCorrectionActivity,
                ConfirmStatus.PROFILE_EDIT,
                2,
                ""
            ) { type, isonoff ->
                if (isonoff) {
                    imageStartIntent("서브이미지1")

                }
            }
            confirmDialog!!.show(supportFragmentManager, "")
        }
        binding!!.rlSubImage2.setOnClickListener {

            var confirmDialog = ConfirmDilaog(
                this@ProfileCorrectionActivity,
                ConfirmStatus.PROFILE_EDIT,
                2,
                ""
            ) { type, isonoff ->
                if (isonoff) {
                    imageStartIntent("서브이미지2")

                }
            }
            confirmDialog!!.show(supportFragmentManager, "")
        }
        binding!!.rlSubImage3.setOnClickListener {
            var confirmDialog = ConfirmDilaog(
                this@ProfileCorrectionActivity,
                ConfirmStatus.PROFILE_EDIT,
                2,
                ""
            ) { type, isonoff ->
                if (isonoff) {
                    imageStartIntent("서브이미지3")

                }
            }
            confirmDialog!!.show(supportFragmentManager, "")

        }
        binding!!.ivMainImageCancel.setOnClickListener {

//            twoButtonDialog = TwoButtonDialog(this, Constans.DIALOG_IMAGE_DELETE, this, this)
//            twoButtonDialog!!.show(this.supportFragmentManager, "")

//            viewModel.mainImageUrl.value = null
//            binding.viewBlack.visibility = View.GONE
//            viewModel.mainCheckReturn.value = ""
//            Glide.with(binding.ivMainImage).load(R.color.trn).into(binding.ivMainImage)

        }
        binding!!.ivSubImageCancel1.setOnClickListener {
//            binding.viewBlackSub1.visibility = View.GONE
//            imageRemove(0)

        }
        binding!!.ivSubImageCancel2.setOnClickListener {
//            imageRemove(1)
//            binding.viewBlackSub2.visibility = View.GONE
        }
        binding!!.ivSubImageCancel3.setOnClickListener {
            imageRemove(2)
            binding.viewBlackSub3.visibility = View.GONE
        }
        binding!!.llRegion.setOnClickListener {
            var i = Intent(this, JoinActivity::class.java)
            i.putExtra("view", "수정")
            i.putExtra("state", 6)

            i.putExtra("data", viewModel.tv_region.value)
            resultLauncherContent.launch(i)
        }
        binding!!.llJob.setOnClickListener {
            var i = Intent(this, JoinActivity::class.java)
            i.putExtra("view", "수정")
            i.putExtra("state", 5)
            i.putExtra("data", viewModel.tv_job.value)
            resultLauncherContent.launch(i)
        }
        binding!!.llDrive.setOnClickListener {
            var i = Intent(this, JoinActivity::class.java)
            i.putExtra("view", "수정")
            i.putExtra("state", 7)
            i.putExtra("data", viewModel.tv_u_drvie.value)
            resultLauncherContent.launch(i)
        }
        binding!!.llHeight.setOnClickListener {
            var i = Intent(this, JoinActivity::class.java)
            i.putExtra("view", "수정")
            i.putExtra("state", 4)
            i.putExtra("data", viewModel.tv_height.value)
            resultLauncherContent.launch(i)
        }
        binding!!.llCharm.setOnClickListener {
            var i = Intent(this, JoinActivity::class.java)
            i.putExtra("view", "수정")
            i.putExtra("state", 2)
            i.putExtra("data", viewModel.tv_charmlist.value?.get(0))
            i.putExtra("data2", viewModel.tv_charmlist.value?.get(1))
            resultLauncherContent.launch(i)
        }
        binding!!.llMusic.setOnClickListener {
            var i = Intent(this, JoinActivity::class.java)
            i.putExtra("view", "수정")
            i.putExtra("state", 3)
            i.putExtra("data", viewModel.tv_musictypelist.value?.get(0))
            i.putExtra("data2", viewModel.tv_musictypelist.value?.get(1))
            resultLauncherContent.launch(i)
        }

        binding!!.llDriveType.setOnClickListener {
            var data = viewModel.my_user_data.value
            var carData = viewModel.my_car_data_list.value?.get(0)


//            if (data?.u_type.equals("OWNER")) {
            if (viewModel.checkRegistration.value == false) {
                if (carData != null) {
                    if (carData.size != 0) {
                        var i = Intent(this, MyCarInformationActivity::class.java)
                        i.putExtra("view", "수정")
                        i.putExtra("carDataList", carData)
                        i.putExtra(
                            "delete_my_car_data_list",
                            viewModel.delete_my_car_data_list.value
                        )
                        i.putExtra(
                            "original_car_image_list",
                            viewModel.original_car_image_list.value
                        )
                        resultLauncherEditCarList.launch(i)
                    } else {
                        var i = Intent(this, OwnerActivity::class.java)
                        i.putExtra("view", "재등록")
                        resultLauncherRegistrationCarList.launch(i)
                    }

                } else {
                    var i = Intent(this, OwnerActivity::class.java)
                    i.putExtra("view", "재등록")
                    resultLauncherRegistrationCarList.launch(i)
                }
            } else {
                var i = Intent(this, OwnerActivity::class.java)

                i.putExtra("view", "재등록")
                i.putExtra("carImage", viewModel.carImage.value)
                i.putExtra("insuranceimage", viewModel.insuranceImage.value)

                i.putExtra("CarBrand_seq", viewModel.CarBrand_seq.value)
                i.putExtra("CarModel_seq", viewModel.CarModel_seq.value)
                i.putExtra("CarSeries_seq", viewModel.CarSeries_seq.value)
                i.putExtra("CarBrand_name", viewModel.CarBrand_name.value)
                i.putExtra("CarBrand_Image", viewModel.CarBrand_Image.value)
                i.putExtra("CarModel_name", viewModel.CarModel_name.value)
                i.putExtra("CarSeries_name", viewModel.CarSeries_name.value)
                resultLauncherRegistrationCarList.launch(i)
            }

        }

        binding!!.llBottomBar.setOnClickListener {


            if (viewModel.email.value!!?.length!! > 0 && !Patterns.EMAIL_ADDRESS.matcher(viewModel.email.value).matches()) {
                Toast.makeText(this, "이메일 양식을 제대로 작성해주세요.", Toast.LENGTH_SHORT).show()
            } else if (viewModel.u_drive_w.value.isNullOrEmpty()) {
                Toast.makeText(this, "어떤 드라이브를 하고싶은지 입력해주세요.", Toast.LENGTH_SHORT).show()
            }else if (viewModel.u_drive_c.value.isNullOrEmpty()){
                Toast.makeText(this, "자주 가는 맛집,드라이브 코스를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }else if (viewModel.u_abutme.value?.length!! <30){
                Toast.makeText(this, "자기소개를 30자 이상 적어주세요", Toast.LENGTH_SHORT).show()
            } else if (viewModel.subImageDatalist.value?.size!! <= 1) {
                Toast.makeText(this, "서브 이미지는 최소 2장이상 등록해야합니다", Toast.LENGTH_SHORT).show()

            } else {
                viewModel.userEditInfo(
                    absolutelyPath(viewModel.mainImageUrl.value, this, "mainProfile"),
                    getSubImagemultipartlist(viewModel.subImageDatalist.value!!, "CarImage"),
                    getmultipartlist(viewModel.my_car_data_list.value!![0]!!, "carImage"),
                    getmultipartlist(viewModel.my_car_data_list.value!![0]!!, "insurImage")
                )
            }


        }
        binding!!.edtAboutMe.setOnClickListener { }

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
                            ImagePicker.with(this@ProfileCorrectionActivity)
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

    fun absolutelyPath(path: String?, context: Context, name: String): MultipartBody.Part? {
        var body: MultipartBody.Part? = null

        if (path!!.startsWith("https:")) {

        } else {
            var result = path?.toUri()?.let { getRealPathFromUriForImagesAndVideo(it) }
            val file = File(result)
            var fileName = path
            fileName = fileName

            var requestBody: RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            body = MultipartBody.Part.createFormData(name, fileName, requestBody)
        }



        return body
    }

    var resultLauncherEditCarList =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                if (data != null) {
                    if (data.getSerializableExtra("carList") != null) {
                        viewModel.my_car_data_list.value =
                            data.getSerializableExtra("carList") as ArrayList<ArrayList<UserInformationCarData>>
                        viewModel.delete_my_car_data_list.value =
                            data.getSerializableExtra("delete_carList") as ArrayList<UserInformationCarData>


                    }


//                    data.
                }

            }
        }

    var resultLauncherRegistrationCarList =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                if (data != null) {

                    viewModel.carImage.value = data.getStringExtra("carImage")
                    viewModel.insuranceImage.value = data.getStringExtra("insuranceimage")
                    viewModel.CarBrand_seq.value = data.getStringExtra("CarBrand_seq")
                    viewModel.CarModel_seq.value = data.getStringExtra("CarModel_seq")
                    viewModel.CarSeries_seq.value = data.getStringExtra("CarSeries_seq")
                    viewModel.CarBrand_name.value = data.getStringExtra("CarBrand_name")
                    viewModel.CarBrand_Image.value = data.getStringExtra("CarBrand_Image")
                    viewModel.CarModel_name.value = data.getStringExtra("CarModel_name")
                    viewModel.CarSeries_name.value = data.getStringExtra("CarSeries_name")
                    var nonData1 = UserInformationCarData(
                        carb_image = "",
                        carb_name = "브랜드 선택",
                        carm_name = "모델명",
                        csm_name = "시리즈명",
                        csm_seq = "0",
                        u_seq = -1,
                        uc_brand = null,
                        uc_car_main = "",
                        uc_check = "",
                        uc_contract_url = "",
                        uc_date = "",
                        uc_insur_date = "",
                        uc_insur_url = "",
                        uc_model = "",
                        uc_return = "",
                        uc_seq = 0,
                        uc_car_main_yn = "",
                        uc_contract_url_yn = "",
                        uc_insur_url_yn = "",
                        uc_income_yn = "",
                        uc_income = "", "", "", ""
                    )

                    if (viewModel.my_car_data_list.value?.get(0)?.size!! == 0) {
                        viewModel.my_car_data_list.value?.get(0)?.add(nonData1)
                    }


                    var carList = viewModel.my_car_data_list.value?.get(0)?.get(0)

                    carList?.apply {
                        uc_brand = viewModel.CarBrand_seq.value
                        uc_model = viewModel.CarModel_seq.value
                        csm_seq = viewModel.CarSeries_seq.value
                        carb_name = viewModel.CarBrand_name.value
                        carm_name = viewModel.CarModel_name.value
                        csm_name = viewModel.CarSeries_name.value
                        carb_image = viewModel.CarBrand_Image.value
                        uc_before_url = ""
                        new_url = viewModel.carImage.value
                        uc_insur_url = viewModel.insuranceImage.value
                        uc_check = "Y"
                    }
                    viewModel.my_car_data_list.value = viewModel.my_car_data_list.value
                    viewModel.checkRegistration.value = true


//
                }

            }
        }
    var resultLauncherContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                if (data != null) {
                    var type = data.getIntExtra("type", 0)

                    when (type) {
                        2 -> {
                            viewModel.charmlist.value?.set(0, data.getStringExtra("data")!!)
                            viewModel.charmlist.value?.set(1, data.getStringExtra("data2")!!)
                            viewModel.charmlist.value = viewModel.charmlist.value


                            viewModel.tv_charmlist.value?.set(0, data.getStringExtra("data")!!)
                            viewModel.tv_charmlist.value?.set(1, data.getStringExtra("data2")!!)
                            viewModel.tv_charmlist.value = viewModel.tv_charmlist.value
                        }
                        3 -> {
                            viewModel.musictypelist.value?.set(0, data.getStringExtra("data")!!)
                            viewModel.musictypelist.value?.set(1, data.getStringExtra("data2")!!)
                            viewModel.musictypelist.value = viewModel.musictypelist.value

                            viewModel.tv_musictypelist.value?.set(0, data.getStringExtra("data")!!)
                            viewModel.tv_musictypelist.value?.set(1, data.getStringExtra("data2")!!)
                            viewModel.tv_musictypelist.value = viewModel.tv_musictypelist.value
                        }
                        4 -> {
                            viewModel.height.value = data.getStringExtra("data")
                            viewModel.tv_height.value = data.getStringExtra("data")
                        }

                        5 -> {
                            viewModel.job.value = data.getStringExtra("data")
                            viewModel.tv_job.value = data.getStringExtra("data")
                        }
                        6 -> {
                            viewModel.region.value = data.getStringExtra("data")
                            viewModel.tv_region.value = data.getStringExtra("data")
                        }
                        7 -> {
                            viewModel.u_drvie.value = data.getStringExtra("data")
                            viewModel.tv_u_drvie.value = data.getStringExtra("data")
                        }


                    }


//                    data.
                }

            }
        }

    private fun getSubImage(posi: Int) {
        ImagePicker.with(this@ProfileCorrectionActivity)
            .galleryOnly()
            .compress(1024)         //Final image size will be less than 1 MB(Optional)
            .maxResultSize(1080, 1080)
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
            viewModel.subImageDatalist.value?.removeAt(posi)
            var data = viewModel.SubImageUrlList.value?.get(posi)!!
            var subImageData: SubImage? = SubImage("0", "", "", 0, "", "", 0, "", "")
            Log.d("리스트 데이터", viewModel.original_sub_image_list.value.toString())
            Log.d("삭제 리스트 데이터", viewModel.delete_sub_image_list.value.toString())
            for ((count, i) in viewModel.original_sub_image_list.value!!.withIndex()) {

                if (i == data) {
                    if (data.im_seq != 0) {

                        viewModel.delete_sub_image_list.value?.set(count, data)


                    }
                }

            }
            viewModel.SubImageUrlList.value?.removeAt(posi)
//
//         iewModel.delete_sub_image_list.value?.set(posi,data)
//            v
//                viewModel.original_sub_image_list.value?.remove(posi,data)


            viewModel.SubImageUrlListSize.value = viewModel.SubImageUrlList.value?.size

            if (viewModel.SubImageUrlList.value?.size!! > 0) {

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
                            .load(viewModel.SubImageUrlList.value?.get(0)?.im_url).centerCrop()
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
                                Glide.with(binding.ivSubImageCancel1).load(R.drawable.ic_cell)
                                    .into(binding.ivSubImageCancel1)

                            }
                            "M" -> {
                                viewModel.sub1CheckReturn.value = "심사중"
                                binding.viewBlackSub1.visibility = View.VISIBLE
                                Glide.with(binding.ivSubImageCancel1).load(R.drawable.ic_cell)
                                    .into(binding.ivSubImageCancel1)
                                Glide.with(binding.ivSubImage1).load(R.drawable.ic_cell)
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
                            .load(viewModel.SubImageUrlList.value?.get(0)?.im_url).centerCrop()
                            .into(binding.ivSubImage1)
                        Glide.with(binding.ivSubImage2)
                            .load(viewModel.SubImageUrlList.value?.get(1)?.im_url).centerCrop()
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
                                Glide.with(binding.ivSubImageCancel1).load(R.drawable.ic_cell)
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
                                Glide.with(binding.ivSubImageCancel2).load(R.drawable.ic_cell)
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
                viewModel.sub2CheckReturn.value = ""
                viewModel.sub3CheckReturn.value = ""
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
                            Glide.with(binding.ivMainImage).load(uri).override(
                                1080,
                                1080
                            ).into(binding.ivMainImage)

                            if (viewModel.view.value!!.equals("수정")) {
                                viewModel.mainCheckReturn.value = "수정중"
                                Glide.with(binding.ivMainImageCancel).load(R.drawable.ic_cell)
                                    .into(binding.ivMainImageCancel)
                            }
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

                                SubImage = SubImageDeepData(uri.toString(), "E")
                                var subImageData: SubImage? = SubImage(
                                    "0",
                                    "",
                                    "",
                                    0,
                                    "",
                                    uri.toString(),
                                    0,
                                    "",
                                    uri.toString()
                                )

                                if (viewModel.SubImageUrlList.value!!.size != 0) {
                                    viewModel.SubImageUrlList.value?.set(0, subImageData!!)
                                } else {
                                    viewModel.SubImageUrlList.value?.add(0, subImageData!!)
                                }
                                viewModel.SubImageUrlListSize.value =
                                    viewModel.SubImageUrlList.value?.size!!


                                when (viewModel.SubImageUrlList.value?.size) {
                                    1 -> {
                                        Glide.with(binding.ivSubImage1).load(uri).centerCrop()
                                            .into(binding.ivSubImage1)
                                        if (viewModel.view.value!!.equals("수정")) {
                                            viewModel.sub1CheckReturn.value = "수정중"
                                            Glide.with(binding.ivSubImageCancel1)
                                                .load(R.drawable.ic_cell)
                                                .into(binding.ivSubImageCancel1)
                                        }
                                        if (viewModel.subImageDatalist.value?.size!! > 0) {

                                            viewModel.subImageDatalist.value?.set(0, subImageData!!)

                                        } else {
                                            viewModel.subImageDatalist.value?.add(subImageData!!)


                                        }

                                    }
                                    2 -> {
                                        Glide.with(binding.ivSubImage2).load(uri).centerCrop()
                                            .into(binding.ivSubImage2)
                                        if (viewModel.view.value!!.equals("수정")) {
                                            viewModel.sub2CheckReturn.value = "수정중"
                                            Glide.with(binding.ivSubImageCancel2)
                                                .load(R.drawable.ic_cell)
                                                .into(binding.ivSubImageCancel2)
                                        }
                                        if (viewModel.subImageDatalist.value?.size!! > 1) {

                                            viewModel.subImageDatalist.value?.set(1, subImageData!!)

                                        } else {
                                            viewModel.subImageDatalist.value?.add(subImageData!!)

                                        }

                                    }

                                    3 -> {
                                        Glide.with(binding.ivSubImage3).load(uri).centerCrop()
                                            .into(binding.ivSubImage3)
                                        if (viewModel.view.value!!.equals("수정")) {
                                            viewModel.sub3CheckReturn.value = "수정중"
                                            Glide.with(binding.ivSubImageCancel3)
                                                .load(R.drawable.ic_cell)
                                                .into(binding.ivSubImageCancel3)
                                        }
                                        if (viewModel.subImageDatalist.value?.size!! > 2) {

                                            viewModel.subImageDatalist.value?.set(2, subImageData!!)

                                        } else {
                                            viewModel.subImageDatalist.value?.add(subImageData!!)
                                        }

                                    }


                                }


                            } else {


                                var subImageData: SubImage? =
                                    SubImage(
                                        "0",
                                        "",
                                        "",
                                        0,
                                        "",
                                        uri.toString(),
                                        0,
                                        "",
                                        uri.toString()
                                    )
                                viewModel.SubImageUrlList.value?.set(0, subImageData!!)
                                if (viewModel.original_sub_image_list.value?.get(0)?.im_seq != 0) {
                                    var data = viewModel.subImageDatalist.value?.get(0)
                                    for ((count, i) in viewModel.original_sub_image_list.value!!.withIndex()) {
                                        if (i == data) {
                                            if (data.im_seq != 0) {
                                                viewModel.subImageDatalist.value?.get(0)?.new_Url =
                                                    uri.toString()
                                                viewModel.subImageDatalist.value?.get(0)?.im_before_Url =
                                                    viewModel.original_sub_image_list.value?.get(0)?.im_url.toString()

                                            }
                                        }

                                    }


                                } else {
                                    viewModel.subImageDatalist.value?.set(0, subImageData!!)
                                }

                                Glide.with(binding.ivSubImage1).load(uri).centerCrop()
                                    .into(binding.ivSubImage1)
                                if (viewModel.view.value!!.equals("수정")) {
                                    viewModel.sub1CheckReturn.value = "수정중"
                                    Glide.with(binding.ivSubImageCancel1).load(R.drawable.ic_cell)
                                        .into(binding.ivSubImageCancel1)
                                }
                            }


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

                            if (viewModel.SubImageUrlList.value?.size!! < 2) {
                                SubImage = SubImageDeepData(uri.toString(), "E")
                                var subImageData: SubImage? = SubImage(
                                    "0",
                                    "",
                                    "",
                                    0,
                                    "",
                                    uri.toString(),
                                    0,
                                    "",
                                    uri.toString()
                                )


                                viewModel.SubImageUrlList.value?.add(subImageData!!)
                                viewModel.SubImageUrlListSize.value =
                                    viewModel.SubImageUrlList.value?.size!!

                                when (viewModel.SubImageUrlList.value?.size) {
                                    1 -> {
                                        Glide.with(binding.ivSubImage1).load(uri).centerCrop()
                                            .into(binding.ivSubImage1)
                                        if (viewModel.view.value!!.equals("수정")) {
                                            viewModel.sub1CheckReturn.value = "수정중"
                                            Glide.with(binding.ivSubImageCancel1)
                                                .load(R.drawable.ic_cell)
                                                .into(binding.ivSubImageCancel1)

                                        }
                                        if (viewModel.subImageDatalist.value?.size!! > 0) {
                                            viewModel.subImageDatalist.value?.set(0, subImageData!!)

                                        } else {
                                            viewModel.subImageDatalist.value?.add(subImageData!!)
                                        }


                                    }
                                    2 -> {
                                        Glide.with(binding.ivSubImage2).load(uri).centerCrop()
                                            .into(binding.ivSubImage2)
                                        if (viewModel.view.value!!.equals("수정")) {
                                            viewModel.sub2CheckReturn.value = "수정중"
                                            Glide.with(binding.ivSubImageCancel2)
                                                .load(R.drawable.ic_cell)
                                                .into(binding.ivSubImageCancel2)
                                        }
                                        if (viewModel.subImageDatalist.value?.size!! > 1) {
                                            viewModel.subImageDatalist.value?.set(1, subImageData!!)

                                        } else {
                                            viewModel.subImageDatalist.value?.add(subImageData!!)
                                        }

                                    }

                                    3 -> {
                                        Glide.with(binding.ivSubImage3).load(uri).centerCrop()
                                            .into(binding.ivSubImage3)
                                        if (viewModel.view.value!!.equals("수정")) {
                                            viewModel.sub3CheckReturn.value = "수정중"
                                            Glide.with(binding.ivSubImageCancel3)
                                                .load(R.drawable.ic_cell)
                                                .into(binding.ivSubImageCancel3)
                                        }
                                        if (viewModel.subImageDatalist.value?.size!! > 2) {
                                            viewModel.subImageDatalist.value?.set(2, subImageData!!)

                                        } else {
                                            viewModel.subImageDatalist.value?.add(subImageData!!)
                                        }


                                    }


                                }
                            } else {
                                SubImage = SubImageDeepData(uri.toString(), "E")


                                var subImageData: SubImage? = SubImage(
                                    "0",
                                    "",
                                    "",
                                    0,
                                    "",
                                    uri.toString(),
                                    0,
                                    "",
                                    uri.toString()
                                )
                                if (viewModel.original_sub_image_list.value?.get(1)?.im_seq != 0) {
                                    var data = viewModel.subImageDatalist.value?.get(1)
                                    for ((count, i) in viewModel.original_sub_image_list.value!!.withIndex()) {
                                        if (i == data) {
                                            if (data.im_seq != 0) {
                                                viewModel.subImageDatalist.value?.get(1)?.new_Url =
                                                    uri.toString()

                                                viewModel.subImageDatalist.value?.get(1)?.im_before_Url =
                                                    viewModel.original_sub_image_list.value?.get(1)?.im_url.toString()

                                            }
                                        }

                                    }


                                } else {
                                    viewModel.subImageDatalist.value?.set(1, subImageData!!)
                                }

                                viewModel.SubImageUrlList.value?.set(1, subImageData!!)

                                Glide.with(binding.ivSubImage2).load(uri).centerCrop()
                                    .into(binding.ivSubImage2)
                                if (viewModel.view.value!!.equals("수정")) {
                                    viewModel.sub2CheckReturn.value = "수정중"
                                    Glide.with(binding.ivSubImageCancel2).load(R.drawable.ic_cell)
                                        .into(binding.ivSubImageCancel2)
                                }
                            }


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
                            SubImage = SubImageDeepData(uri.toString(), "E")

                            if (viewModel.SubImageUrlList.value?.size!! < 3) {
                                var subImageData: SubImage? =
                                    SubImage(
                                        "0",
                                        "",
                                        "",
                                        0,
                                        "",
                                        uri.toString(),
                                        0,
                                        "",
                                        uri.toString()
                                    )

                                viewModel.SubImageUrlList.value?.add(subImageData!!)
                                viewModel.SubImageUrlListSize.value =
                                    viewModel.SubImageUrlList.value?.size!!

                                when (viewModel.SubImageUrlList.value?.size) {
                                    1 -> {
                                        Glide.with(binding.ivSubImage1).load(uri).centerCrop()
                                            .into(binding.ivSubImage1)
                                        if (viewModel.view.value!!.equals("수정")) {
                                            viewModel.sub1CheckReturn.value = "수정중"
                                            Glide.with(binding.ivSubImageCancel1)
                                                .load(R.drawable.ic_cell)
                                                .into(binding.ivSubImageCancel1)
                                        }
                                        if (viewModel.subImageDatalist.value?.size!! > 0) {
                                            viewModel.subImageDatalist.value?.set(0, subImageData!!)
                                        } else {
                                            viewModel.subImageDatalist.value?.add(subImageData!!)
                                        }

                                    }
                                    2 -> {
                                        Glide.with(binding.ivSubImage2).load(uri).centerCrop()
                                            .into(binding.ivSubImage2)
                                        if (viewModel.view.value!!.equals("수정")) {
                                            viewModel.sub2CheckReturn.value = "수정중"
                                            Glide.with(binding.ivSubImageCancel2)
                                                .load(R.drawable.ic_cell)
                                                .into(binding.ivSubImageCancel2)
                                        }
                                        if (viewModel.subImageDatalist.value?.size!! > 1) {
                                            viewModel.subImageDatalist.value?.set(1, subImageData!!)
                                        } else {
                                            viewModel.subImageDatalist.value?.add(subImageData!!)
                                        }
                                    }

                                    3 -> {
                                        Glide.with(binding.ivSubImage3).load(uri).centerCrop()
                                            .into(binding.ivSubImage3)
                                        if (viewModel.view.value!!.equals("수정")) {
                                            viewModel.sub3CheckReturn.value = "수정중"
                                            Glide.with(binding.ivSubImageCancel3)
                                                .load(R.drawable.ic_cell)
                                                .into(binding.ivSubImageCancel3)
                                        }
                                        if (viewModel.subImageDatalist.value?.size!! > 2) {
                                            viewModel.subImageDatalist.value?.set(2, subImageData!!)
                                        } else {
                                            viewModel.subImageDatalist.value?.add(subImageData!!)
                                        }

                                    }


                                }


                            } else {


                                var subImageData: SubImage? = SubImage(
                                    "0",
                                    "",
                                    "",
                                    0,
                                    "",
                                    uri.toString(),
                                    0,
                                    "",
                                    uri.toString()
                                )

                                if (viewModel.subImage_really.value?.get(2)?.im_seq != 0) {
                                    var data = viewModel.subImageDatalist.value?.get(2)
                                    for ((count, i) in viewModel.original_sub_image_list.value!!.withIndex()) {
                                        if (i == data) {
                                            if (data.im_seq != 0) {
                                                viewModel.subImageDatalist.value?.get(2)?.new_Url =
                                                    uri.toString()
                                                viewModel.subImageDatalist.value?.get(2)?.im_before_Url =
                                                    viewModel.original_sub_image_list.value?.get(2)?.im_url.toString()

                                            }
                                        }

                                    }

                                } else {
                                    viewModel.subImageDatalist.value?.set(2, subImageData!!)
                                }
                                viewModel.SubImageUrlList.value?.set(2, subImageData!!)
                                Glide.with(binding.ivSubImage3).load(uri).centerCrop()
                                    .into(binding.ivSubImage3)
                                if (viewModel.view.value!!.equals("수정")) {
                                    viewModel.sub3CheckReturn.value = "수정중"
                                    Glide.with(binding.ivSubImageCancel3).load(R.drawable.ic_cell)
                                        .into(binding.ivSubImageCancel3)
                                }
                            }


                        }
                    }
                }

            }
        }


    override fun finish() {
        super.finish()
//        overridePendingTransition(
//            com.supercarlounge.supercar.R.anim.fadein,
//            com.supercarlounge.supercar.R.anim.fadeout
//        )
    }


//    private fun viewRedefinition() {
//        val layoutParams = binding.llMainImage.layoutParams
//        layoutParams.width = binding.llMainImage.width
//        layoutParams.height = binding.llMainImage.width
//        binding.llMainImage.layoutParams = layoutParams
//
//        val layoutParamsSub1 = binding.rlSubImage1.layoutParams
//        layoutParamsSub1.width = binding.rlSubImage1.width
//        layoutParamsSub1.height = binding.rlSubImage1.width
//
//        val layoutParamsSub2 = binding.rlSubImage2.layoutParams
//        layoutParamsSub2.width = binding.rlSubImage2.width
//        layoutParamsSub2.height = binding.rlSubImage2.width
//
//        val layoutParamsSub3 = binding.rlSubImage3.layoutParams
//        layoutParamsSub3.width = binding.rlSubImage3.width
//        layoutParamsSub3.height = binding.rlSubImage3.width
//
//        binding.rlSubImage1.layoutParams = layoutParamsSub1
//        binding.rlSubImage2.layoutParams = layoutParamsSub2
//        binding.rlSubImage3.layoutParams = layoutParamsSub3
//
//    }


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


    fun getmultipartlist(
        list: java.util.ArrayList<UserInformationCarData>,
        type: String
    ): java.util.ArrayList<MultipartBody.Part> {
        if (type.equals("carImage")) {
            viewModel.carBeforelist.value?.clear()
        } else {
            viewModel.insurBeforelist.value?.clear()
        }


        var mlist = java.util.ArrayList<MultipartBody.Part>()
        for ((count, i) in list.withIndex()) {
            Log.d("확인", i.uc_car_main.toString())
            var d = ""

            if (type.equals("carImage")) {
                d = i.new_url.toString()
            } else {
                d = i.uc_insur_url.toString()
            }


            var path = d
            var checkPath = i.uc_before_url
            var uri = Uri.parse(path.toString())
            if (path != null) {
                if (!path.startsWith("http")) {

                    var pathmain = getRealPathFromUriForImagesAndVideo(uri!!)
                    var policefile = File(pathmain)
                    var uri = Uri.parse(path)
                    var body2: MultipartBody.Part? = null
                    if (path.toString().endsWith("jpg") || path.toString().endsWith("png")) {
                        if (type.equals("carImage")) {

                            body2 = getmultipart(
                                policefile,

                                "mainCar" + "[$count]", ".jpg", "image/jpeg", "mainCar"
                            )
                        } else {
                            body2 = getmultipart(
                                policefile,
                                "insurance" + "[$count]",
                                ".jpg",
                                "image/jpeg",
                                "insurance"
                            )
                        }
                        mlist.add(body2)


                    }

                } else {
                    if (type.equals("carImage")) {
                        viewModel.carBeforelist.value?.add(path)
                    } else {
                        viewModel.insurBeforelist.value?.add(path)
                    }
                }
            }

        }
        return mlist
    }

    fun getSubImagemultipartlist(
        list: java.util.ArrayList<SubImage>,
        type: String
    ): java.util.ArrayList<MultipartBody.Part> {

        var mlist = java.util.ArrayList<MultipartBody.Part>()

        for ((count, i) in list.withIndex()) {


            var path = i.new_Url
            var checkPath = i.im_before_Url


            if (path != null) {
                if (!path.startsWith("http")) {
                    var uri = Uri.parse(path.toString())
                    if (i.new_Url != null) {
                        var pathmain = getRealPathFromUriForImagesAndVideo(uri!!)
                        var policefile = File(pathmain)
                        var uri = Uri.parse(path)
                        var body2: MultipartBody.Part? = null

                        if (path.toString().endsWith("jpg") || path.toString().endsWith("png")) {

                            body2 = getmultipart(
                                policefile,
                                "SubImage" + "[$count]",
                                ".jpg",
                                "image/jpeg",
                                "subProfile"
                            )
                            Log.d("테스트", path)
                            mlist.add(body2)

                        }

                    } else {


                    }
                }
            }
        }
        return mlist
    }

    fun getnowdatestring(): String {
        var now = System.currentTimeMillis()
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var d = Date(now)
        var nowdate = format.format(d)
        return nowdate
    }

    override fun onBackPressed() {
        if (viewModel.CheckUserData() == false) {
            drivepop2 =
                ProfileLikeBaseDialog(this, "", PopTypeStatus.USER_EDIT_CHECK) { type, onoff ->
                    if (onoff) {

                        if (viewModel.email.value!!?.length!! > 0 && !Patterns.EMAIL_ADDRESS.matcher(
                                viewModel.email.value
                            ).matches()
                        ) {
                            Toast.makeText(this, "이메일 양식을 제대로 작성해주세요.", Toast.LENGTH_SHORT).show()
                        } else if (viewModel.u_abutme.value?.length!! < 30) {
                            Toast.makeText(this, "자기소개를 30자 이상 적어주세요", Toast.LENGTH_SHORT).show()
                        } else if (viewModel.subImageDatalist.value?.size!! <= 1) {
                            Toast.makeText(this, "서브 이미지는 최소 2장이상 등록해야합니다", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            viewModel.userEditInfo(
                                absolutelyPath(viewModel.mainImageUrl.value, this, "mainProfile"),
                                getSubImagemultipartlist(
                                    viewModel.subImageDatalist.value!!,
                                    "CarImage"
                                ),
                                getmultipartlist(
                                    viewModel.my_car_data_list.value!![0]!!,
                                    "carImage"
                                ),
                                getmultipartlist(
                                    viewModel.my_car_data_list.value!![0]!!,
                                    "insurImage"
                                )
                            )
                        }
                        drivepop2?.dismiss()

                    } else {
                        super.onBackPressed()
                    }

                }
            drivepop2!!.show(supportFragmentManager, "")
        }else{
            super.onBackPressed()
        }

    }


    fun getmultipart(
        file: File,
        name: String,
        type: String,
        typeset: String,
        formname: String,
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

//
//    override fun onClick(v: View?) {
//        Log.d("확인", "okEvent:1 ")
//    }
//
//    override fun okEvent(int: Int) {
//        Log.d("확인", "okEvent:2 ")
//    }

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

    private fun userEditHistory() {

        if (!viewModel.update.value.equals("Y")) {
            if (!viewModel.my_user_data.value?.u_tempData.isNullOrEmpty()) {
                var data = viewModel.my_user_data.value?.u_tempData
                val obj: JsonObject = JsonParser.parseString(data).getAsJsonObject()
                var u_area = viewModel.my_user_data.value?.u_area
                var u_cm = viewModel.my_user_data.value?.u_cm

                var u_job = viewModel.my_user_data.value?.u_job

                var u_smoking = viewModel.my_user_data.value?.u_smoking

                var u_email = viewModel.my_user_data.value?.u_email


                var u_music = viewModel.my_user_data.value?.u_music


                var u_drive = viewModel.my_user_data.value?.u_drive

                var u_drive_w = viewModel.my_user_data.value?.u_drive_w

                var u_drive_c = viewModel.my_user_data.value?.u_drive_c

                var u_charm = viewModel.my_user_data.value?.u_charm
                var u_about_me = viewModel.my_user_data.value?.u_about_me
//            if (data?.contains("u_area") == true) {
//                var temp_u_area = obj.get("u_area").toString().replace("\"", "")
//                if (temp_u_area != u_area) {
//                    binding.llRegion.isEnabled = false
//                    binding.tvRegion.setTextColor(
//                        ContextCompat.getColor(
//                            this,
//                            R.color.white_50
//                        )
//                    )
//                }
//
//            }
//            if (data?.contains("u_cm") == true) {
//                var temp_u_cm = obj.get("u_cm").toString().replace("\"", "")
//                if (temp_u_cm != u_cm) {
//                    binding.llHeight.isEnabled = false
//                    binding.tvHeight.setTextColor(
//                        ContextCompat.getColor(
//                            this,
//                            R.color.white_50
//                        )
//                    )
//                }
//            }

//            if (data?.contains("u_job") == true) {
//                var temp_u_job = obj.get("u_job").toString().replace("\"", "")
//                if (temp_u_job != u_job) {
//                    binding.llJob.isEnabled = false
//                    binding.tvJob.setTextColor(ContextCompat.getColor(this, R.color.white_50))
//                }
//
//            }
//            if (data?.contains("u_smoking") == true) {
//                var temp_u_smoking = obj.get("u_smoking").toString().replace("\"", "")
//                if (temp_u_smoking != u_smoking) {
//                    binding.cb1.isEnabled = false
//                    binding.cb2.isEnabled = false
//                    binding.tvSmoking1.isEnabled = false
//                    binding.tvSmoking2.isEnabled = false
//                }
//            }

//            if (data?.contains("u_email") == true) {
//                var temp_u_email = obj.get("u_email").toString().replace("\"", "")
//                if (temp_u_email != u_email) {
//                    binding.edtTitle.isEnabled = false
//                    binding.edtTitle.setTextColor(
//                        ContextCompat.getColor(
//                            this,
//                            R.color.white_50
//                        )
//                    )
//                }
//            }


//            if (data?.contains("u_music") == true) {
//                var temp_u_music =
//                    obj.get("u_music").toString().replace("\"", "").replace("\\", "\"")
//                if (temp_u_music != u_music) {
//                    binding.llMusic.isEnabled = false
//                    binding.tvMusictypelist1.setTextColor(
//                        ContextCompat.getColor(
//                            this,
//                            R.color.white_50
//                        )
//                    )
//                    binding.tvMusictypelist2.setTextColor(
//                        ContextCompat.getColor(
//                            this,
//                            R.color.white_50
//                        )
//                    )
//                }
//            }
//            if (data?.contains("u_drive") == true) {
//                var temp_u_drive = obj.get("u_drive").toString().replace("\"", "")
//                if (temp_u_drive != u_drive) {
//                    binding.llDrive.isEnabled = false
//                    binding.tvDrive.setTextColor(ContextCompat.getColor(this, R.color.white_50))
//                }
//            }

//                if (data?.contains("u_drive_w") == true) {
//                    var temp_u_drive_w = obj.get("u_drive_w").toString().replace("\"", "")
//                    if (temp_u_drive_w != u_drive_w) {
//                        binding.edtDriveStyle.isEnabled = false
//                        binding.edtDriveStyle.setTextColor(
//                            ContextCompat.getColor(
//                                this,
//                                R.color.white_50
//                            )
//                        )
//                    }
//                }
//                if (data?.contains("u_drive_c") == true) {
//                    var temp_u_drive_c = obj.get("u_drive_c").toString().replace("\"", "")
//                    if (temp_u_drive_c != u_drive_c) {
//                        binding.edtDriveCourse.isEnabled = false
//                        binding.edtDriveCourse.setTextColor(
//                            ContextCompat.getColor(
//                                this,
//                                R.color.white_50
//                            )
//                        )
//                    }
//                }

//            if (data?.contains("u_charm") == true) {
//                var temp_u_charm =
//                    obj.get("u_charm").toString().replace("\"", "").replace("\\", "\"")
//
//
//                if (temp_u_charm != u_charm) {
//                    binding.llCharm.isEnabled = false
//                    binding.tvCharmlist1.setTextColor(
//                        ContextCompat.getColor(
//                            this,
//                            R.color.white_50
//                        )
//                    )
//                    binding.tvCharmlist2.setTextColor(
//                        ContextCompat.getColor(
//                            this,
//                            R.color.white_50
//                        )
//                    )
//                }
//
//            }

                if (data?.contains("u_about_me") == true) {
                    var temp_u_about_me = obj.get("u_about_me").toString().replace("\"", "")
                    if (temp_u_about_me != u_about_me) {
                        binding.edtBoxAboutMe.isEnabled = false
                        binding.edtAboutMe.setTextColor(
                            ContextCompat.getColor(
                                this,
                                R.color.white_50
                            )
                        )
                    }
                }


            }
        }
    }
}
