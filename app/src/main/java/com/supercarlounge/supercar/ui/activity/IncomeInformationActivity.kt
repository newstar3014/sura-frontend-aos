package com.supercarlounge.supercar.ui.activity



import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.view.WindowManager
import android.os.Bundle
import android.provider.MediaStore
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
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
import com.supercarlounge.supercar.data.UserInformationCarData
import com.supercarlounge.supercar.data.UserInformationData
import com.supercarlounge.supercar.databinding.*
import com.supercarlounge.supercar.viewmodel.IncomeInformationViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.customview.BaseActivitiy
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class IncomeInformationActivity : BaseActivitiy() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityIncomeInformationBinding
    private lateinit var viewModel: IncomeInformationViewModel
    private var application:MainApplication? =null
    var u_name = ""
    var u_gender = ""
    var u_birthday = ""
    var u_phone = ""
    var type = ""
    var list : ArrayList<UserInformationData> = arrayListOf()
    var view = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        with(window) {
//            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
//            // set an slide transition
//            enterTransition = Slide(Gravity.END)
//            exitTransition = Slide(Gravity.START)
//        }

        viewModel = ViewModelProvider(this).get(IncomeInformationViewModel::class.java)
        binding = ActivityIncomeInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel
        application =  applicationContext as MainApplication
        setTextStyle()
            clickListeners()
        viewModel.nickname.value = intent.getStringExtra("nickname")
        if(intent.getSerializableExtra("CharmList") != null){
            viewModel.resultState2Array = intent.getSerializableExtra("CharmList") as ArrayList<String>
        }
        if(intent.getSerializableExtra("musicTasteList") != null) {
            viewModel.resultState3Array = intent.getSerializableExtra("musicTasteList") as ArrayList<String>
        }
        type = intent.getStringExtra("type").toString()
        if (!type.equals("all")) {
            viewModel.tv_bottom.value = "수정하기"
        }
        if(intent.getSerializableExtra("height") != null) {
            viewModel.resultState4Array = intent.getSerializableExtra("height") as ArrayList<String>
        }
        if(intent.getSerializableExtra("job") != null) {
            viewModel.resultState5Array = intent.getSerializableExtra("job") as ArrayList<String>
        }
        if(intent.getSerializableExtra("regin") != null) {
            viewModel.resultState6Array = intent.getSerializableExtra("regin") as ArrayList<String>
        }
        if(intent.getSerializableExtra("driveStyle") != null) {
            viewModel.resultState7Array = intent.getSerializableExtra("driveStyle") as ArrayList<String>
        }
        viewModel.userdata.value = application?.userData
        view = intent.getStringExtra("view").toString()

        Log.d("받음", viewModel.userdata.value.toString())
        Log.d("유저데이터2", list.toString())
//        if ( != null) {
//
//
//
//            if (list.size != 0) {
//                viewModel._userdata.value = list[0]
//            }
//        }
        observers()

        if (intent.getSerializableExtra("userCarData") != null) {

            viewModel.my_car_data_list.value = intent.getSerializableExtra("userCarData") as ArrayList<ArrayList<UserInformationCarData>>
            Log.d("확인", intent.getSerializableExtra("userCarData").toString())
        }


        u_name = intent.getStringExtra("u_name").toString()
        viewModel.view.value = intent.getStringExtra("view")
        u_name = intent.getStringExtra("u_name").toString()
        u_gender = intent.getStringExtra("u_gender").toString()
        u_birthday = intent.getStringExtra("u_birthday").toString()
        u_phone = intent.getStringExtra("u_phone").toString()

        if (viewModel.view.value.equals("수정")) {

            var userData = viewModel.userdata.value
            Log.d("체크용 uc_return", userData.toString())
            if (userData != null) {

                    var data = userData
                    var requestOptions = RequestOptions()
                    requestOptions =
                        requestOptions.transforms(CenterCrop(), RoundedCorners(16))

                    if (data.u_income != null) {
                        if (data.u_income!!.isNotEmpty()) {
                            viewModel.incomeImage.value = data?.u_income
                            viewModel.stateBottomText.value = true
                            Glide.with(binding.incomeImage).load(data?.u_income).override(250, 250)
                                .apply(requestOptions).into(binding.incomeImage)

                            Glide.with(binding.incomeBg).load(binding.incomeBg).override(250, 250)
                                .apply(requestOptions)
                                .into(binding.incomeBg)




                            when (data.u_income_yn) {

                                "Y" -> {
                                    viewModel.incmeCheckReturn.value = "승인"
                                }
                                "M" -> {
                                    viewModel.incmeCheckReturn.value = "심사중"
                                    binding!!.incomeImage.isEnabled = false
                                    binding!!.llIncome.isEnabled = false
                                }
                                "N" -> {
                                    viewModel.incmeCheckReturn.value = "반려"
                                }
                                else -> {}
                            }


                        }


                }


            }
        }else{

            if (!application!!.incomeImageLog){
                application.let {
                    it?.joinLog(this,"차량사진 등록",u_name)
                    it?.incomeImageLog = true
                }

            }
        }
    }

    private fun observers() {
        viewModel.apiCall.observe(this, Observer {
            it?.let {
                if (it){
                    val i = Intent(this@IncomeInformationActivity, SubscriptionApplicationCompleteActivity::class.java)
                    i.putExtra("u_admin","M")
                    i.putExtra("u_seq",viewModel.userdata.value?.u_seq.toString())
                    Log.d("대기u_seq", viewModel.userdata.value?.u_seq.toString())
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(i)
                 //   overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                }
            }
        })

    }

    @SuppressLint("ResourceType")
    private fun setTextStyle(){
        val SS = SpannableStringBuilder(binding!!.tvSub.text)

        val typeface = ResourcesCompat.getFont(this, com.supercarlounge.supercar.R.font.inter_bold)

        SS.setSpan(typeface?.let {
            CustomTypefaceSpan("", it) }, 47, 54, Spanned.SPAN_EXCLUSIVE_INCLUSIVE

        )
        SS.setSpan(typeface?.let {
            CustomTypefaceSpan("", it) }, 58, 65, Spanned.SPAN_EXCLUSIVE_INCLUSIVE

        )
        SS.setSpan(typeface?.let {
            CustomTypefaceSpan("", it) }, 67, 72, Spanned.SPAN_EXCLUSIVE_INCLUSIVE


        )
        binding!!.tvSub.text = SS

        var content = binding!!.tvExplanation.text
        val spannableString = SpannableString(content)
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor(getString(R.color.red2))),
            20,
            23,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor(getString(R.color.red2))),
            76,
            83,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor(getString(R.color.red2))),
            110,
            125
            ,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding!!.tvExplanation.text =spannableString
    }
    private fun setUrl(url :kotlin.String){

    }

    @SuppressLint("DefaultLocale", "IntentReset")
    private fun clickListeners(){
        binding.llIncome.setOnClickListener {
            ImagePicker.with(this)
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)
                .galleryOnly()
                .crop(
                    4f,
                    4f
                )//Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    resultLauncherimage.launch(intent)
                }
        }
        binding!!.incomeImage.setOnClickListener {

            ImagePicker.with(this)
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)
                .galleryOnly()
                .crop(
                    4f,
                    4f
                )//Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    resultLauncherimage.launch(intent)
                }
        }


        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.bottomBar.setOnClickListener {


            var i = Intent(this,MyJoinInfomationActivity::class.java)
            var userData = application?.userData
            var m_uc_income_yn = userData?.u_income_yn
            if (type.equals("all")) {
              if (viewModel.incomeImage.value?.startsWith("http") == true && m_uc_income_yn.equals("N")) {
                    Toast.makeText(this, "소득정보 사진을 변경해주세요.", Toast.LENGTH_SHORT).show()

                }else{
                    if (viewModel.incomeImage.value.isNullOrEmpty()) {
                        Toast.makeText(this, "소득정보 사진을 등록해주세요.", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    i.putExtra("nickname", viewModel.nickname.value)
                    i.putExtra("CharmList", viewModel.resultState2Array)
                    i.putExtra("musicTasteList", viewModel.resultState3Array)
                    i.putExtra("height", viewModel.resultState4Array)
                    i.putExtra("job", viewModel.resultState5Array)
                    i.putExtra("regin", viewModel.resultState6Array)
                    i.putExtra("driveStyle", viewModel.resultState7Array)
                    i.putExtra("income", viewModel.incomeImage.value)
                    i.putExtra("type", "PASSENGERMAN")
                    i.putExtra("u_name", u_name)
                    i.putExtra("u_gender", u_gender)
                    i.putExtra("u_birthday", u_birthday)
                    i.putExtra("u_phone", u_phone)
                    if (view.equals("퀵유저")) {
                        i.putExtra("view", "퀵유저")
                    }

                    if (viewModel.view.value.equals("수정")) {
                        i.putParcelableArrayListExtra("userData", list)
                        i.putExtra("view", viewModel.view.value)

                        var image = viewModel.incomeNewImage.value
                        if (image.isNullOrEmpty()) image= viewModel.incomeImage.value.toString()
                        i.putExtra("incomeNewImage", image)
                        Log.d("유저데이터", viewModel.userdata.value.toString())
                    }
                    startActivity(i)
                 // overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                }
            }else{
                if (viewModel.incomeImage.value?.startsWith("http") == true && m_uc_income_yn.equals("N")) {
                    Toast.makeText(this, "소득정보 사진을 변경해주세요.", Toast.LENGTH_SHORT).show()
                }else{
                    viewModel.incomePassengerCall(
                        absolutelyPath(viewModel.incomeNewImage.value, this, "income"),

                        )

                }


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
                            viewModel.incomeNewImage.value = uri.toString()
                            if (!viewModel.incomeImage.value.equals("http")){
                                viewModel.incomeImage.value = uri.toString()
                            }
                            viewModel.stateBottomText.value = true



                            var requestOptions = RequestOptions()
                            requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(16))
                            Glide.with(binding.incomeImage).load(uri)   .apply(requestOptions).into(binding.incomeImage)

//                            Glide.with(binding.incomeBg)
//                                .load(R.color.black_check_80)
//                                .apply(requestOptions)
//                                .into(binding.incomeBg)
                            viewModel.incmeCheckReturn.value = "수정"
                        }
                    }
                }

            }
        }

    override fun onPause() {
        super.onPause()
       // overridePendingTransition(R.anim. fadein, R.anim.fadeout)
    }
    override fun finish() {
        super.finish()
       // overridePendingTransition(com.supercarlounge.supercar.R.anim. fadein, com.supercarlounge.supercar.R.anim.fadeout)
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
