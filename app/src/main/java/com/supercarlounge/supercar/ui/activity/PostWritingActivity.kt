package com.supercarlounge.supercar.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.WindowManager
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.supercarlounge.supercar.Constans
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.adapter.PostWritingFilterAdapter
import com.supercarlounge.supercar.data.BoardCategoryData
import com.supercarlounge.supercar.databinding.ActivityPostWritingBinding
import com.supercarlounge.supercar.dialog.*
import com.supercarlounge.supercar.viewmodel.PostWritingViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.supercarlounge.supercar.GradientDrawable
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.CommentAdapter
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.data.BoardCommentData
import com.supercarlounge.supercar.enumset.GuidePopType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class PostWritingActivity: BaseActivitiy() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityPostWritingBinding
    private lateinit var viewModel: PostWritingViewModel
    private var fAdapter : PostWritingFilterAdapter? =null
    private var payment40Dialog : Payment40Dialog? =null
    private var profileBrowseDialog : ProfileLikeFriendDialog? =null
    private var profileBrowseBaseDialog : ProfileLikeBaseDialog? =null
    private var dialogDriveExposureBinding : DriveExposureDialog? =null
    private var loadingDialog : LoadingDialog? =null
    private var driveRegistrationDialog : DriveRankUpDialog? =null
    private var blockPostDialog :BlockDialog? =null
    private var textDialog :TextDialog? =null
    private var oneButtonDialog :OneButtonSetDialog? =null
    private var twoButtonDialog :TwoButtonDialog? =null
    private var profileLikeDialog :ProfileLikeDialog? =null
    private var likeListDialog :LikeListDialog? =null
    private var miniProfileDialog :MiniProfileDialog? =null
    private var blockProifleDialog :PoliceDialog? =null
    private var serviceCenterDialog :ServiceCenterDialog? =null
    private lateinit var application:MainApplication

    private var warningDialog :WarningDialog? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        with(window) {
//            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
//            // set an slide transition
//            enterTransition = Slide(Gravity.END)
//            exitTransition = Slide(Gravity.START)
//        }
        viewModel = ViewModelProvider(this).get(PostWritingViewModel::class.java)
        binding = ActivityPostWritingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        application= applicationContext as MainApplication
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel
        viewModel.myUserData.value = application.userData
        viewModel.myu_uuid.value = application.user_u_uuid
        val i= intent
        viewModel.titleText.value =i.getStringExtra("titleText")
        viewModel.mainText.value =i.getStringExtra("mainText")
        viewModel.subText.value = i.getStringExtra("subText")
        viewModel.time.value = i.getStringExtra("time")
        //글수정인지 글쓰기인지
        viewModel.viewType.value = i.getStringExtra("viewType")
        viewModel.anonymousForumDeepPostData.value = i.getParcelableExtra("anonymousForumDeepPostData")
        Glide.with(binding.ivLoading).load(R.raw.loading_anim).into(binding.ivLoading)
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
        binding!!.llBack.background = d
        if(viewModel.anonymousForumDeepPostData.value != null) {
            if (!viewModel.anonymousForumDeepPostData.value?.b_cover.equals("")) {
                var requestOptions = RequestOptions()
                requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(16))
                viewModel.boardCover.value = null
                Glide.with(binding.ivPhoto).load(viewModel.anonymousForumDeepPostData.value?.b_cover).apply(requestOptions).into(binding.ivPhoto)

            }
            binding!!.edtWriting.setText(application.decodeset(viewModel.anonymousForumDeepPostData.value?.b_contents))


            viewModel.titleText.value = "글수정"
//            application.SaveAnonymousDate()
        }else{

            viewModel.titleText.value = "글쓰기"
        }


        clickListeners()
        observes()
        setAdapters()
        editTextListeners()
    }

    override fun onResume() {
        super.onResume()
        if(application.Check30days(application.checkguide5)) {

        }else{
            // 가이드팝업 제거
//            var d = GuideDialog(this, GuidePopType.GUIDE_BOARD_WRITE) {
//                application!!.save_guide(GuidePopType.GUIDE_BOARD_WRITE)
//            }
//            d.show(supportFragmentManager, "")
        }
        application?.setlocation()
    }


    private fun editTextListeners() {
        binding!!.edtWriting.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {
                viewModel.b_contents.value = p0.toString()
                Log.d("글쓰기 텍스트", p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

    }
    private fun setAdapters() {
        fAdapter = PostWritingFilterAdapter(this, testFilterData(), viewModel!!) { data, position ->
            viewModel!!.filterPosition.value =position
            fAdapter?.notifyDataSetChanged()
        }
        binding.rvPostWriting.adapter = fAdapter
        binding.rvPostWriting.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }
    private fun observes() {

        viewModel.boardInsertCall.observe(this, Observer {
            if (it != null) {
                if (it == true) {

                    val intent = Intent()
                    intent.putExtra("type","EDIT")
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }

        })
    }

    private fun clickListeners() {
        binding!!.ivBack.setOnClickListener {
            finish()
        }
        binding!!.llBottomBar.setOnClickListener {
            if (viewModel.b_contents.value.isNullOrEmpty()) {
                Toast.makeText(this@PostWritingActivity,"내용을 입력해주세요.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (viewModel.b_contents.value!!.length > 2000) {
                Toast.makeText(this@PostWritingActivity,"내용은 2000자 이내로 작성해 주세요.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (viewModel.viewType.value.equals("글쓰기")){
                if (viewModel.boardCover.value == null){
                    viewModel.boardNoPhotoInsert()
                }else{
                    viewModel.boardInsert(absolutelyPath(viewModel.boardCover.value!!,this,0))
                }
            }else{
                if (viewModel.boardCover.value == null){
                    viewModel.boardNoPhotoEditInsert()
                }else{
                    viewModel.boardEditInsert(absolutelyPath(viewModel.boardCover.value!!,this,1))
                }
            }


//            payment40Dialog= Payment40Dialog(this)
//
//            payment40Dialog!!.show(this.supportFragmentManager,"")
//            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
//
//


//            profileBrowseDialog= ProfileBrowseDialog(this,DIALOG_BROWSE_CHECK)
//            profileBrowseDialog!!.show(this.supportFragmentManager,"")
//            profileBrowseDialog= ProfileBrowseDialog(this,DIALOG_BROWSE_REQUEST_PAYMENT)
//            profileBrowseDialog!!.show(this.supportFragmentManager,"")
//            profileBrowseDialog= ProfileBrowseDialog(this,DIALOG_BROWSE_REQUEST)
//            profileBrowseDialog!!.show(this.supportFragmentManager,"")
//            profileBrowseBaseDialog= ProfileBrowseBaseDialog(this,Constans.DIALOG_PROFILE_BASE)
//            profileBrowseBaseDialog!!.show(this.supportFragmentManager,"")
//            profileBrowseBaseDialog= ProfileBrowseBaseDialog(this,Constans.DIALOG_PROFILE_BASE_3)
//            profileBrowseBaseDialog!!.show(this.supportFragmentManager,"")

//
//            loadingDialog = LoadingDialog(this,Constans.DIALOG_LOADING)
//            loadingDialog!!.show(this.supportFragmentManager,"")
//
//            driveRegistrationDialog = DriveRegistrationDialog(this,Constans.DIALOG_DRIVE_EXPOSURE_1)
//            driveRegistrationDialog!!.show(this.supportFragmentManager,"")
//            profileLikeDialog = ProfileLikeDialog(this,Constans.DIALOG_POST_BLOCK)
//            profileLikeDialog!!.show(this.supportFragmentManager,"")
//            likeListDialog = LikeListDialog(this,Constans.DIALOG_POST_BLOCK)
//            likeListDialog!!.show(this.supportFragmentManager,"")
//            miniProfileDialog = MiniProfileDialog(this,Constans.DIALOG_POST_BLOCK)
//            miniProfileDialog!!.show(this.supportFragmentManager,"")
//
//
//            textDialog = TextDialog(this,Constans.DIALOG_SPECIAL)
//
//            textDialog!!.show(this.supportFragmentManager,"")
//
//            textDialog = TextDialog(this,Constans.DIALOG_SERVICE_PREPARATION)
//
//            textDialog!!.show(this.supportFragmentManager,"")
//
//            oneButtonDialog = OneButtonDialog(this,Constans.DIALOG_REPORT)
//            oneButtonDialog!!.show(this.supportFragmentManager,"")
//            oneButtonDialog = OneButtonDialog(this,Constans.DIALOG_POST_CATEGPRY)
//            oneButtonDialog!!.show(this.supportFragmentManager,"")
//            blockProifleDialog = BlockProifleDialog(this,Constans.DIALOG_MAIN_CAR)
//            blockProifleDialog!!.show(this.supportFragmentManager,"")
//            blockPostDialog= BlockPostDialog(this,Constans.DIALOG_POST_BLOCK)
//            blockPostDialog!!.show(this.supportFragmentManager,"")
//            blockPostDialog= BlockPostDialog(this,Constans.DIALOG_DRIVE_BLOCK)
//            blockPostDialog!!.show(this.supportFragmentManager,"")
//            blockPostDialog= BlockPostDialog(this,Constans.DIALOG_PROFILE_BLOCK)
//            blockPostDialog!!.show(this.supportFragmentManager,"")
//            twoButtonDialog = TwoButtonDialog(this,Constans.DIALOG_PASS_BUY)
//            twoButtonDialog!!.show(this.supportFragmentManager,"")
//            twoButtonDialog = TwoButtonDialog(this,Constans.DIALOG_EVALUATION)
//            twoButtonDialog!!.show(this.supportFragmentManager,"")
//
//            twoButtonDialog = TwoButtonDialog(this,Constans.DIALOG_DRIVE_RESET)
//            twoButtonDialog!!.show(this.supportFragmentManager,"")
//            twoButtonDialog = TwoButtonDialog(this,Constans.DIALOG_PROFILE_CHANGE)
//            twoButtonDialog!!.show(this.supportFragmentManager,"")
//
//            twoButtonDialog = TwoButtonDialog(this,Constans.DIALOG_CRUSH_ARRIVAL_PAYMENT)
//            twoButtonDialog!!.show(this.supportFragmentManager,"")
//            twoButtonDialog = TwoButtonDialog(this,Constans.DIALOG_CRUSH_ARRIVAL)
//            twoButtonDialog!!.show(this.supportFragmentManager,"")
//
//            twoButtonDialog = TwoButtonDialog(this,Constans.DIALOG_CONTACT_OPEN)
//            twoButtonDialog!!.show(this.supportFragmentManager,"")
//            twoButtonDialog = TwoButtonDialog(this,Constans.DIALOG_CONTACT_OPEN_COMPLETION)
//            twoButtonDialog!!.show(this.supportFragmentManager,"")
//
//            twoButtonDialog = TwoButtonDialog(this,Constans.DIALOG_CRUSH_ACCEPT)
//            twoButtonDialog!!.show(this.supportFragmentManager,"")
//            twoButtonDialog = TwoButtonDialog(this,Constans.DIALOG_CRUSH_SEND1)
//            twoButtonDialog!!.show(this.supportFragmentManager,"")
//
//            twoButtonDialog = TwoButtonDialog(this,Constans.DIALOG_CRUSH_SEND2)
//            twoButtonDialog!!.show(this.supportFragmentManager,"")
//            twoButtonDialog = TwoButtonDialog(this,Constans.DIALOG_CRUSH_TRY_ACCEPT)
//            twoButtonDialog!!.show(this.supportFragmentManager,"")
//            twoButtonDialog = TwoButtonDialog(this,Constans.DIALOG_LACK_HEART)
//            twoButtonDialog!!.show(this.supportFragmentManager,"")
//            twoButtonDialog = TwoButtonDialog(this,Constans.DIALOG_POST_DELETE)
//            twoButtonDialog!!.show(this.supportFragmentManager,"")
//            serviceCenterDialog = ServiceCenterDialog(this,0)
//            serviceCenterDialog!!.show(this.supportFragmentManager,"")
//            warningDialog = WarningDialog(this,0)
//            warningDialog!!.show(this.supportFragmentManager,"")

        }
        binding!!.test.setOnClickListener {
//            payment40Dialog= Payment40Dialog(this)
//
//            payment40Dialog!!.show(this.supportFragmentManager,"")
//            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
//
//            dialogDriveExposureBinding= DriveExposureDialog(this,Constans.DIALOG_DRIVE_EXPOSURE_1)
//            dialogDriveExposureBinding!!.show(this.supportFragmentManager,"")
//            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
//            dialogDriveExposureBinding= DriveExposureDialog(this,Constans.DIALOG_DRIVE_EXPOSURE_2)
//            dialogDriveExposureBinding!!.show(this.supportFragmentManager,"")
//            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));


        }

        binding!!.ivPhoto.setOnClickListener {
            ImagePicker.with(this)
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
//            selectGallery()
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
                            viewModel.boardCover.value = uri.toString()
                            var requestOptions = RequestOptions()
                            requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(16))
                            Glide.with(binding.ivPhoto).load(uri)   .apply(requestOptions).into(binding.ivPhoto)

                        }
                    }
                }

            }
        }
    fun absolutelyPath(path: String?, context : Context,type: Int): MultipartBody.Part {
        var result = path?.toUri()?.let { getRealPathFromUriForImagesAndVideo(it) }
        var file = File(result)
        var fileName = path
        if (path != null){
            fileName = fileName+".png"
        }else{
            fileName = null

        }

        var requestBody : RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(),file)

        var body : MultipartBody.Part = MultipartBody.Part.createFormData("boardCover",fileName,requestBody)

        return body!!
    }

    private fun selectGallery(){
        val writePermission = ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val readPermission = ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)

        if (writePermission == PackageManager.PERMISSION_DENIED || readPermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ), Constans.REQ_GALLERY
            )
        }else{
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*"
            )
            imageResult.launch(intent)
        }
    }

    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == RESULT_OK){
            val imageUri = result.data?.data
            imageUri?.let {
                var imageFile =  getRealPathFromURL( it)

                Glide.with(this)
                    .load(imageUri)
                    .fitCenter()
                    .override(500,500)
                    .into(binding.ivPhoto)
            }
        }
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
    private fun getRealPathFromURL(uri: Uri): String {
        val buildName = Build.MANUFACTURER
        if (buildName.equals("Xiaomi")){
            return uri.path!!
        }
        var columnIndex =0
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cusor =contentResolver.query(uri,proj,null,null,null)
        if (cusor!!.moveToFirst()){
            columnIndex == cusor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }
        val result = cusor.getString(columnIndex)
        cusor.close()
        return result
    }


    override fun finish() {
        super.finish()
      //  overridePendingTransition(com.supercarlounge.supercar.R.anim. fadein, com.supercarlounge.supercar.R.anim.fadeout)
    }

    private fun testFilterData():ArrayList<BoardCategoryData>{
        var list : ArrayList<BoardCategoryData> = arrayListOf()
        var x = 0
        for(i: Int in 0 until viewModel!!.filterArray.size ){
            var data = BoardCategoryData(viewModel!!.filterArray[x])
            x = x.plus(1)
            list.add(data)
        }


        return  list
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
