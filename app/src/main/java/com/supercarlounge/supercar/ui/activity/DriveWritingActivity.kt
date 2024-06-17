package com.supercarlounge.supercar.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.DriveAwayData
import com.supercarlounge.supercar.databinding.*
import com.supercarlounge.supercar.viewmodel.*
import com.github.dhaval2404.imagepicker.ImagePicker
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.dialog.DriveExposureDialog
import com.supercarlounge.supercar.dialog.DriveRegiDialog
import com.supercarlounge.supercar.dialog.GuideDialog
import com.supercarlounge.supercar.enumset.GuidePopType

class DriveWritingActivity : BaseActivitiy() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDriveWritingBinding
    private lateinit var viewModel: DriveWritingViewModel
    var dialogDriveExposure: DriveExposureDialog? = null
    var application: MainApplication? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DriveWritingViewModel::class.java)
        binding = ActivityDriveWritingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel
        application = applicationContext as MainApplication
        var data: DriveAwayData? = intent.getParcelableExtra("data")

        var myUserdata = application?.userData
        viewModel.isowner.value = myUserdata?.u_type.equals("OWNER")
        viewModel.isownercheck.value = myUserdata?.u_type.equals("OWNER")
        var vtype = intent.getIntExtra("viptype", 0)
        var heart = intent.getIntExtra("vipheart", 0)
        viewModel.position.value = intent.getIntExtra("position",0)
        Log.d("DriveWritingActivity", vtype.toString())
        viewModel.viptype.value = vtype
        viewModel.vip_heart.value = heart.toString()
        Glide.with(binding.ivLoading).load(R.raw.loading_anim).into(binding.ivLoading)

        var myCardata = application?.userCarData
        viewModel.my_car_data_list.value = myCardata
        if (data != null) {

            viewModel.dtitletext.value = data?.b_title
            viewModel.drivedata.value = data
            viewModel.dcontenttext.value = data?.b_contents
            viewModel.mainImageUrl.value = data?.b_cover
            viewModel.like_drive_date.value = data?.b_when
            viewModel.like_drive_type.value = data?.b_how
            viewModel.isownercheck.value = data.ub_type.equals("DRIVER")
            if (viewModel.my_car_data_list.value?.get(0) != null) {
                if (viewModel.my_car_data_list.value?.get(0)!!.size != 0) {
                    if (!data.uc_seq.equals("0")) {
                        for ((count, i) in viewModel.my_car_data_list.value!!.get(0)!!
                            .withIndex()) {
                            if (data.uc_seq.equals(i.uc_seq)) {
                                when (count) {
                                    0 -> {
                                        viewModel.myCarChecked1.value = true
                                    }
                                    1 -> {
                                        viewModel.myCarChecked2.value = true
                                    }
                                    2 -> {
                                        viewModel.myCarChecked3.value = true
                                    }
                                    else -> {}
                                }
                            }
                        }
                    }
                }
            }
            Log.d("드라이브 타입", data?.ub_type.toString())
            var heart = data?.heart_count.toString()
            if (data?.heart_count != 0) {
                viewModel.vip_heart.value = heart
                viewModel.viptype.value = 3
            }

        }

        if (viewModel.isowner.value!!) {


            Log.d("오너패신저1", viewModel.isowner.value.toString())





            if (viewModel.isownercheck.value!!) {
                viewModel.myCarData.value = viewModel.my_car_data_list.value?.get(0)?.get(0)
            }
            when (viewModel.my_car_data_list.value?.get(0)?.size) {
                1 -> {

//                    binding.llMyCar1.visibility = View.VISIBLE
                    binding.tvMyCar1.text =
                        viewModel.my_car_data_list.value?.get(0)!!.get(0).carb_name
                    Glide.with(this)
                        .load(viewModel.my_car_data_list.value?.get(0)!!.get(0).carb_image)
                        .centerInside().into(binding.ivMyCar1)

                }
                2 -> {
//                    binding.llMyCar1.visibility = View.VISIBLE
//                    binding.llMyCar2.visibility = View.VISIBLE
                    binding.tvMyCar1.text =
                        viewModel.my_car_data_list.value?.get(0)!!.get(0).carb_name
                    binding.tvMyCar2.text =
                        viewModel.my_car_data_list.value?.get(0)!!.get(1).carb_name
                    Glide.with(this)
                        .load(viewModel.my_car_data_list.value?.get(0)!!.get(0).carb_image)
                        .centerInside().into(binding.ivMyCar1)
                    Glide.with(this)
                        .load(viewModel.my_car_data_list.value?.get(0)!!.get(1).carb_image)
                        .centerInside().into(binding.ivMyCar2)
                }
                3 -> {

//                    binding.llMyCar1.visibility = View.VISIBLE
//                    binding.llMyCar2.visibility = View.VISIBLE
//                    binding.llMyCar3.visibility = View.VISIBLE
                    binding.tvMyCar1.text =
                        viewModel.my_car_data_list.value?.get(0)!!.get(0).carb_name
                    binding.tvMyCar2.text =
                        viewModel.my_car_data_list.value?.get(0)!!.get(1).carb_name
                    binding.tvMyCar3.text =
                        viewModel.my_car_data_list.value?.get(0)!!.get(2).carb_name
                    Glide.with(this)
                        .load(viewModel.my_car_data_list.value?.get(0)!!.get(0).carb_image)
                        .centerInside().into(binding.ivMyCar1)
                    Glide.with(this)
                        .load(viewModel.my_car_data_list.value?.get(0)!!.get(1).carb_image)
                        .centerInside().into(binding.ivMyCar2)
                    Glide.with(this)
                        .load(viewModel.my_car_data_list.value?.get(0)!!.get(2).carb_image)
                        .centerInside().into(binding.ivMyCar3)
                }
                else -> {}
            }

            if (viewModel!!.my_car_data_list.value?.get(0) != null) {
                for ((count, i) in viewModel!!.my_car_data_list.value?.get(0)!!.withIndex()) {
                    if (i.uc_return.equals("Y") && i.uc_car_main_yn.equals("Y") && i.uc_insur_url_yn.equals("Y")) {
                        if (count == 0) {
                            binding.llMyCar1.visibility = View.VISIBLE
                        } else if (count == 1) {
                            binding.llMyCar2.visibility = View.VISIBLE
                        } else if (count == 2) {
                            binding.llMyCar3.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        viewModel.myu_uuid.value = application!!.user_u_uuid
        viewModel.my_seq.value = application!!.userData!!.u_seq.toString()
        clickListeners()
        observes()

    }


    private fun observes() {
        viewModel.istoast.observe(this, Observer {
            if (viewModel.isstart.value!!) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            } else {
                viewModel.isstart.value = true
            }
        })
        viewModel.sendsuccess.observe(this, Observer {
            if (it.isNotEmpty()) {

                viewModel.istoast.value = "성공적으로 드라이브가 등록되었습니다."
                var i = Intent()
                if (it.equals("vip")) {
                    i.putExtra("vip", true)
                }

                var data: DriveAwayData? = intent.getParcelableExtra("data")
                data?.b_cover = viewModel.mainImageUrl.value.toString()
                data?.b_title = viewModel.dtitletext.value.toString()
                data?.b_contents = viewModel.dcontenttext.value.toString()

                if (data != null) {
                    Log.d("테스트", data.toString())
                    i.putExtra("data", data)
                    i.putExtra("position", viewModel.position.value)
                }


                setResult(Activity.RESULT_OK, i)
                this.finish()
            }
        })
        viewModel.viewvip.observe(this, Observer {
            if (it) {
                var i = Intent(this, DriveVipRankActivity::class.java)
                i.putExtra("inputtype", true)
                resultLauncherVip.launch(i)
            }
        })


        viewModel.mainImageUrl.observe(this, Observer {


            if (it.isNullOrEmpty()) {
                binding.tvBottom.setTextColor(ContextCompat.getColor(this, R.color.bottom_bar_text))
            } else if (viewModel.dtitletext.value.isNullOrEmpty()) {
                binding.tvBottom.setTextColor(ContextCompat.getColor(this, R.color.bottom_bar_text))
            } else if (viewModel.dcontenttext.value.isNullOrEmpty()) {
                binding.tvBottom.setTextColor(ContextCompat.getColor(this, R.color.bottom_bar_text))
            } else {
                binding.tvBottom.setTextColor(ContextCompat.getColor(this, R.color.white))
            }
        })
        viewModel.dtitletext.observe(this, Observer {
            Log.d("테스트", "${viewModel.dtitletext.value},${viewModel.mainImageUrl.value},${viewModel.dcontenttext.value}")
            if (it.isNullOrEmpty()) {
                binding.tvBottom.setTextColor(ContextCompat.getColor(this, R.color.bottom_bar_text))
            } else if (viewModel.mainImageUrl.value.isNullOrEmpty()) {
                binding.tvBottom.setTextColor(ContextCompat.getColor(this, R.color.bottom_bar_text))
            } else if (viewModel.dcontenttext.value.isNullOrEmpty()) {
                binding.tvBottom.setTextColor(ContextCompat.getColor(this, R.color.bottom_bar_text))
            } else {
                binding.tvBottom.setTextColor(ContextCompat.getColor(this, R.color.white))
            }
        })

        viewModel.dcontenttext.observe(this, Observer {
            if (it.isNullOrEmpty()) {
                binding.tvBottom.setTextColor(ContextCompat.getColor(this, R.color.bottom_bar_text))
            } else if (viewModel.mainImageUrl.value.isNullOrEmpty()) {
                binding.tvBottom.setTextColor(ContextCompat.getColor(this, R.color.bottom_bar_text))
            } else if (viewModel.dtitletext.value.isNullOrEmpty()) {
                binding.tvBottom.setTextColor(ContextCompat.getColor(this, R.color.bottom_bar_text))
            } else {
                binding.tvBottom.setTextColor(ContextCompat.getColor(this, R.color.white))
            }
        })


        viewModel.ispost.observe(this, Observer {
            if (it) {

                var pathstart = viewModel.mainImageUrl.value!!
                if (pathstart.isNullOrEmpty()) {
                    viewModel.istoast.value = "사진을 등록해 주세요"
                } else if (viewModel.dtitletext.value.isNullOrEmpty()) {
                    viewModel.istoast.value = "제목을 입력해 주세요"
                } else if (viewModel.dcontenttext.value.isNullOrEmpty()) {
                    viewModel.istoast.value = "내용을 입력해 주세요"
                } else {
                    if (viewModel.drivedata.value == null) {
//                        var driveRegiDialog =
//                            DriveRegiDialog(this) { onoff, c_gender, c_pay2, c_pay1 ->
//                                if (onoff) {
                                    viewModel.boardInsert(
                                        application!!.getRealPathFromUriForImagesAndVideo(
                                            pathstart.toUri(),
                                        )!!,
                                        false,
                                        false,
                                        false
                                    )
//                                }
//                            }
//                        driveRegiDialog.show(supportFragmentManager, "")

                    } else {
                        var realpath = ""
                        if (!pathstart.startsWith("https")) {
                            realpath =
                                application!!.getRealPathFromUriForImagesAndVideo(pathstart.toUri())!!
                        }
//                        var driveRegiDialog =
//                            DriveRegiDialog(this) { onoff, c_gender, c_pay2, c_pay1 ->
//                                if (onoff) {
                                    viewModel.boardEditInsert(
                                        realpath, false,
                                        false,
                                        false
                                    )
//                                }
//                            }
//                        driveRegiDialog.show(supportFragmentManager, "")

                    }
                }

            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (application!!.Check30days(application!!.checkguide2)) {

        } else {
            var d = GuideDialog(this, GuidePopType.GUIDE_DRIVE_WRITE) {
                application!!.save_guide(GuidePopType.GUIDE_DRIVE_WRITE)
            }
            d.show(supportFragmentManager, "")
        }
        application?.setlocation()
    }

    private fun clickListeners() {
        binding!!.ivBack.setOnClickListener {
            finish()
        }
        binding!!.imageSet.setOnClickListener {

            ImagePicker.with(this)
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)
                .galleryOnly()
                .crop(
                    3f,
                    4f
                )//Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    resultLauncherimage.launch(intent)
                }
        }
        binding!!.llMyCar1.setOnClickListener {
            if (viewModel.myCarChecked1.value == false) {
                viewModel.myCarChecked1.value = true
                viewModel.myCarChecked2.value = false
                viewModel.myCarChecked3.value = false
                viewModel.myCarData.value = viewModel.my_car_data_list.value?.get(0)?.get(0)
            } else {
                viewModel.myCarData.value = null
                viewModel.myCarChecked1.value = false
            }

        }

        binding!!.llMyCar2.setOnClickListener {
            if (viewModel.myCarChecked2.value == false) {
                viewModel.myCarChecked1.value = false
                viewModel.myCarChecked2.value = true
                viewModel.myCarChecked3.value = false
                viewModel.myCarData.value = viewModel.my_car_data_list.value?.get(0)?.get(1)
            } else {
                viewModel.myCarData.value = null
                viewModel.myCarChecked2.value = false
            }
        }
        binding!!.llMyCar3.setOnClickListener {
            if (viewModel.myCarChecked3.value == false) {
                viewModel.myCarChecked1.value = false
                viewModel.myCarChecked2.value = false
                viewModel.myCarChecked3.value = true
                viewModel.myCarData.value = viewModel.my_car_data_list.value?.get(0)?.get(2)
            } else {
                viewModel.myCarData.value = null
                viewModel.myCarChecked3.value = false
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
                            // Glide.with(binding.imageSet).load(uri).into(binding.imageSet)
                        }
                    }
                }

            }
        }

    var resultLauncherVip =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                if (data != null) {
                    var heart = data.getStringExtra("vipheart")
                    var agree = data.getBooleanExtra("isagree", false)
                    if (agree) {
                        viewModel.viptype.value = 1
                        viewModel.vip_heart.value = heart
                        viewModel.isagreeview.value = agree
                    }
                }

            }
        }

    override fun finish() {
        super.finish()
     //   overridePendingTransition(
     //       com.supercarlounge.supercar.R.anim.fadein,
     //       com.supercarlounge.supercar.R.anim.fadeout
     //   )
    }




    companion object {
        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("setimagedrive")
        fun setimagedrive(view: ImageView, url: String?) {
            Log.d("Checkimage", url!!)
            // This methods should not have any return type, = declaration would make it return that object declaration.
            if (!url.isNullOrEmpty()) {
                if (url.equals("null")) {
                    Glide.with(view.context).load(R.drawable.imgupload_131x131)
                        .override(view.width, view.height).centerInside()
                        .into(view)
                } else {
                    Glide.with(view.context).load(url).override(view.width, view.height)
                        .centerInside()
                        .into(view)
                }
            } else {
                Glide.with(view.context).load(R.drawable.imgupload_131x131)
                    .centerInside()
                    .into(view)
            }
        }

        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("setimageLocation","setimageLocationIndex")
        fun setimageLocation(view: ImageView, imageList: ArrayList<String>?,index:Int) {

            if (imageList?.size!! > index){
                var url = imageList[index]
                if (url.isNotEmpty()) {
                        Glide.with(view.context).load(url).override(view.width, view.height)
                            .centerInside()
                            .into(view)
                }else{
                    Glide.with(view.context).load(R.color.trn).override(view.width, view.height)
                        .centerInside()
                        .into(view)
                }
            }else{
                Glide.with(view.context).load(R.color.trn).override(view.width, view.height)
                    .centerInside()
                    .into(view)
            }

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
}
