package com.supercarlounge.supercar.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.get
import androidx.databinding.BindingAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.MySubInfoListAdapter
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.data.*
import com.supercarlounge.supercar.databinding.*
import com.supercarlounge.supercar.dialog.DriveRegiDialog
import com.supercarlounge.supercar.dialog.GuideDialog
import com.supercarlounge.supercar.dialog.RpmDialog
import com.supercarlounge.supercar.enumset.GuidePopType
import com.supercarlounge.supercar.enumset.RpmStatus
import com.supercarlounge.supercar.viewmodel.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MyCarBoardWritingActivity : BaseActivitiy() {

    private lateinit var binding: ActivityMyCarBoardWritingBinding
    private lateinit var viewModel: MyCarBoardWritingViewModel
    var application: MainApplication? = null
    var adapter: MySubInfoListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyCarBoardWritingViewModel::class.java)
        binding = ActivityMyCarBoardWritingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel
        application = applicationContext as MainApplication
        GetMyUserData(application!!)
        myCarListInit()
        clickListeners()
        observes()
        editTextFocus()
        initAdapter()

        viewModel.viptype.value = intent.getStringExtra("viewType")
        viewModel.position.value =  intent.getIntExtra("position",0)
        if (viewModel.viptype.value.equals("글수정")) {
            editInfo()
        } else {
            checkMyCarData()
        }

        binding!!.llMain.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewResize()
                binding!!.llMain.viewTreeObserver.removeOnGlobalLayoutListener(this)

            }


        })

    }


    //수정 데이터 가져오기
    private fun editInfo() {


        var myCarBoardData = intent.getParcelableExtra<MyCarBoardData>("myCarBoardData")


        if (myCarBoardData != null) {
            myCarBoardData.apply {
                selectCar(this)
                viewModel.mainImageUrl.value = this.mmi_img
                viewModel.textmainTitle.value = this.mmi_title
                viewModel.textmainContext.value = this.mmi_contents
                viewModel.textIsMmiFirst.value = this.mmi_first
                viewModel.textIsMmiSecond.value = this.mmi_second
                viewModel.textIsMmithird.value = this.mmi_third
                viewModel.mmi_seq.value = this.mmi_seq


            }


        }
        if (intent.getSerializableExtra("myCarBoardSubList") != null) {
            var myCarBoardSubList =
                intent.getSerializableExtra("myCarBoardSubList") as ArrayList<MySubInfoData>
            addSubList(myCarBoardSubList)

        }

    }

    private fun addSubList(myCarBoardSubList: ArrayList<MySubInfoData>) {
        var sublist = myCarBoardSubList
        if (sublist.size != 0) {
            for (i in sublist) {

                viewModel.my_sub_info.value?.add(i)
                setEditAddView(i)
            }
            viewModel.my_sub_info.value = viewModel.my_sub_info.value
        }

    }

    fun getViewModel() {

    }

    //수정일때 선택된 차량 가져오기
    private fun selectCar(myCarBoardData: MyCarBoardData) {


        if (viewModel.select_car_data_list.value?.size != 0) {
            var noCar = true
            for ((count, i) in viewModel.select_car_data_list.value!!.withIndex()) {

                if (i.card_seq == myCarBoardData?.carb_seq.toString() && i.carm_seq == myCarBoardData?.carm_seq && i.csm_seq == myCarBoardData?.csm_seq) {
                    noCar = false
                    viewModel.select_car_data_list.value?.get(count)?.select = true
                    viewModel.select_car_data.value = viewModel.select_car_data_list.value?.get(count)
                }else{


                }

            }
            if (noCar){
              var  carData =  CarSelectData(  myCarBoardData.carb_image.toString(),   myCarBoardData.carb_name.toString(),  myCarBoardData.carm_name.toString(),  myCarBoardData.csm_name.toString(), myCarBoardData.carb_seq.toString(), myCarBoardData.carm_seq.toString(),
                    myCarBoardData.csm_seq.toString(), select = true, isShowing = true)
                viewModel.select_car_data.value = carData
            }
        }


    }

    private fun myCarListInit() {
        var selectCarData =
            CarSelectData("", "", "", "", "", "", "", select = false, isShowing = false)
        viewModel.select_car_data_list.value?.add(selectCarData)
        viewModel.select_car_data_list.value?.add(selectCarData)
        viewModel.select_car_data_list.value?.add(selectCarData)

        if (viewModel!!.my_car_data_list.value != null) {
            if (viewModel!!.my_car_data_list.value?.size != 0) {
                for ((count, i) in viewModel!!.my_car_data_list.value?.withIndex()!!) {
                    if (i.uc_return.equals("Y")) {
                        var carData = viewModel!!.my_car_data_list.value?.get(count)
                        carData.apply {

                            var carb_image = this?.carb_image.toString()
                            var carb_name = this?.carb_name.toString()
                            var carm_name = this?.carm_name.toString()
                            var csm_name = this?.csm_name.toString()
                            var carb_seq = this?.uc_brand.toString()
                            var carm_seq = this?.uc_model.toString()
                            var csm_seq = this?.csm_seq.toString()
                            var main_url_yn = false
                            var insur_yn = false
                            if(this?.uc_car_main_yn.isNullOrEmpty() == false ) {
                                main_url_yn = this?.uc_car_main_yn.toString().equals("Y")
                            }
                            if (this?.uc_insur_url_yn.isNullOrEmpty() == false ) {
                                insur_yn = this?.uc_insur_url_yn.toString().equals("Y")
                            }
                            var selectCarData = CarSelectData(
                                carb_image,
                                carb_name,
                                carm_name,
                                csm_name,
                                carb_seq,
                                carm_seq,
                                csm_seq,
                                select = false,
                                isShowing = main_url_yn&&insur_yn
                            )
                            viewModel.select_car_data_list.value?.set(count, selectCarData)
                        }


                    }
                }
            }

        }
    }

    private fun initAdapter() {

//        adapter = MySubInfoListAdapter(this, arrayListOf(), viewModel) { data, String, position ->
//            viewModel.mySubInfoListPosition.value = position
//            when (String) {
//                "사진수정" -> {
//                    ImagePicker.with(this)
//                        .compress(1024)         //Final image size will be less than 1 MB(Optional)
//                        .maxResultSize(1080, 1080)
//                        .galleryOnly()
//                        .crop(
//                            4f,
//                            4f
//                        )//Final image resolution will be less than 1080 x 1080(Optional)
//                        .createIntent { intent ->
//                            resultLauncherEditSubimage.launch(intent)
//                        }
//                }
//
//                "삭제" -> {
//                    viewModel.my_sub_info.value?.removeAt(position)
//                    viewModel.my_sub_info.value = viewModel.my_sub_info.value
//                }
//
//                "에딧텍스트" -> {
////                    binding!!.rvMySubInfo.adapter?.notifyItemChanged(position)
//                }
//            }
//
//        }

//        binding!!.rvMySubInfo.adapter = adapter
//        binding!!.rvMySubInfo.layoutManager = LinearLayoutManager(this)
    }

    private fun editTextFocus() {
        binding.edtTitle.setOnFocusChangeListener { view, b ->
            viewModel.isMainEdit.value = b
        }
        binding.edtMainContext.setOnFocusChangeListener { view, b ->
            viewModel.isMainEdit.value = b
        }

        binding.edtMmiFirst.setOnFocusChangeListener { view, b ->
            viewModel.isMmiFirstEdit.value = b

        }
        binding.edtMmiSecond.setOnFocusChangeListener { view, b ->
            viewModel.isMmiSecondEdit.value = b

        }
        binding.edtMmiThird.setOnFocusChangeListener { view, b ->
            viewModel.isMmiThirdEdit.value = b

        }
    }

    private fun GetMyUserData(application: MainApplication) {


        //유저 데이터
        if (application?.userData != null) {
            var myUserdata = application?.userData
            viewModel.my_user_data.value = myUserdata
        }

        //자동차 데이터
        if (application?.userCarData != null) {
            if (application?.userCarData!!.size != 0) {
                var myCardata = application?.userCarData?.get(0)
                viewModel.my_car_data_list.value = myCardata
            }
        }

    }


    private fun observes() {
        viewModel.istoast.observe(this, Observer {
            if (viewModel.isstart.value!!) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            } else {
                viewModel.isstart.value = true
            }
        })

        viewModel.mainImageUrl.observe(this, Observer {
            successCondition()
        })
        viewModel.textmainContext.observe(this, Observer {
            successCondition()
        })

        viewModel.textmainTitle.observe(this, Observer {
            successCondition()
        })
        viewModel.select_car_data.observe(this, Observer {
            successCondition()
        })

        viewModel.textIsMmiFirst.observe(this, Observer {
            successCondition()
        })
        viewModel.textIsMmiSecond.observe(this, Observer {
            successCondition()
        })
        viewModel.textIsMmithird.observe(this, Observer {
            successCondition()
        })


        viewModel.my_sub_info.observe(this, Observer {
            successCondition()
        })
        viewModel.postSuccess.observe(this, Observer {
            if (it) {
                resetMyCarData()

                var i = Intent()
                if(viewModel.myCarBoardUpdateData != null){
                    i.putExtra("data",viewModel.myCarBoardUpdateData.value)
                }
                    i.putExtra("position",viewModel.position.value)

                if (viewModel.viptype.value.equals("글수정")){
                    i.putExtra("type", "글수정")
                }else{
                    i.putExtra("type", "글쓰기")
                }

                this.setResult(Activity.RESULT_OK, i)
                finish()
            }
        })

    }

    private fun successCondition() {

        var isNotContents = false
        if (viewModel.my_sub_info.value?.size != 0) {
            for (i in viewModel.my_sub_info.value!!) {
                if (i.msi_contents?.isEmpty() == true || i.msi_img?.isEmpty() == true) {
                    isNotContents = true
                }
            }
        }

        //선택한 차량이 없으면
        if (viewModel.select_car_data.value == null) {
            binding.tvBottom.setTextColor(ContextCompat.getColor(this, R.color.bottom_bar_text))
        } else if (viewModel.mainImageUrl.value.isNullOrEmpty()) {
            binding.tvBottom.setTextColor(ContextCompat.getColor(this, R.color.bottom_bar_text))
        } else if (viewModel.textmainTitle.value.isNullOrEmpty()) {
            binding.tvBottom.setTextColor(ContextCompat.getColor(this, R.color.bottom_bar_text))
        } else if (viewModel.textmainContext.value.isNullOrEmpty()) {
            binding.tvBottom.setTextColor(ContextCompat.getColor(this, R.color.bottom_bar_text))
        } else if (viewModel.textIsMmiFirst.value.isNullOrEmpty()) {
            binding.tvBottom.setTextColor(ContextCompat.getColor(this, R.color.bottom_bar_text))
        } else if (viewModel.textIsMmiSecond.value.isNullOrEmpty()) {
            binding.tvBottom.setTextColor(ContextCompat.getColor(this, R.color.bottom_bar_text))
        } else if (viewModel.textIsMmithird.value.isNullOrEmpty()) {
            binding.tvBottom.setTextColor(ContextCompat.getColor(this, R.color.bottom_bar_text))
        } else if (isNotContents) {
            binding.tvBottom.setTextColor(ContextCompat.getColor(this, R.color.bottom_bar_text))
        } else {
            binding.tvBottom.setTextColor(ContextCompat.getColor(this, R.color.white))
        }
    }


    private fun EditAddView(src: String) {
        val myView = CustomMyCarBoardItem(this, viewModel)
//        if (v.parent != null) {
//            (v.parent as ViewGroup).removeView(v) // <- fix
//        }
//        v.addView(myView)


        var imageview =
            binding.llAddView[viewModel.mySubInfoListPosition.value!!].findViewById<ImageView>(R.id.iv_image)
        Glide.with(imageview).load(src).into(imageview)
    }

    private fun editTextFocus(check: Boolean) {
        Log.d("CustomMyCarBoardItem : editTextFocus", "$check")
        var edtBox =
            binding.llAddView[viewModel.mySubInfoListPosition.value!!].findViewById<LinearLayoutCompat>(
                R.id.ll_msi_contents
            )
        if (check == true) {
            edtBox.background =
                ContextCompat.getDrawable(this, R.drawable.coner_round_edit_strok_s_bg)
        } else {
            edtBox.background =
                ContextCompat.getDrawable(this, R.drawable.coner_round_edit_strok_bg)
        }
    }

    @SuppressLint("ResourceType", "ClickableViewAccessibility")
    private fun setEditAddView(data: MySubInfoData) {

        val myView = CustomMyCarBoardItem(this, viewModel)
        binding.llAddView.addView(myView) // LinearLayout에 textView 추가


        var edt = myView.binding?.edtMsiContents
        var handler = Handler(Looper.getMainLooper())

        var runnable = Runnable {
            run {
                myView.setText(data.msi_contents);
            }
        }
        myView.setGlide(data.msi_img!!)
        handler.postDelayed(runnable, 1000)


        var ivImage = myView.binding?.ivImage
        var ivCancel = myView.binding?.ivCancel
        var lledtBox = myView.binding?.edtMsiContents
        var item = myView.binding?.item
        var position = 0

        ivImage?.setOnClickListener {
            position = binding.llAddView.indexOfChild(myView)
            viewModel.mySubInfoListPosition.value = position

            ImagePicker.with(this)
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)
                .galleryOnly()
                .crop(
                    4f,
                    4f
                )//Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    resultLauncherEditSubimage.launch(intent)
                }
        }
        edt?.setOnFocusChangeListener { view, b ->
            position = binding.llAddView.indexOfChild(myView)
            viewModel.mySubInfoListPosition.value = position
            editTextFocus(b)
            Log.d("CustomMyCarBoardItem : lledtBox", "$b")
        }

        ivCancel?.setOnClickListener {
            position = binding.llAddView.indexOfChild(myView)
            if (viewModel.viptype.value.equals("글수정")) {
                if (viewModel.my_sub_info.value?.get(position)?.msi_seq != 0) {
                    viewModel.my_sub_info_delete.value?.add(
                        (viewModel.my_sub_info.value?.get(
                            position
                        )?.msi_seq.toString())
                    )
                }

            }
            viewModel.mySubInfoListPosition.value = position
            binding.llAddView.removeViewAt(position)
            viewModel.my_sub_info.value?.removeAt(position)
            viewModel.my_sub_info.value = viewModel.my_sub_info.value

        }
        edt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {
                viewModel.mySubInfoListPosition.value = position
                viewModel.my_sub_info.value?.get(position)?.msi_contents = p0.toString()
                viewModel.my_sub_info.value = viewModel.my_sub_info.value
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })


    }

    @SuppressLint("ResourceType", "ClickableViewAccessibility")
    private fun setAddView(src: String) {
//        val v: LinearLayout = findViewById<LinearLayout>(R.id.ll_add_view) as LinearLayout
        val myView = CustomMyCarBoardItem(this, viewModel)
//        if (v.parent != null) {
//            (v.parent as ViewGroup).removeView(v) // <- fix
//        }
//        v.addView(myView)


        binding.llAddView.addView(myView) // LinearLayout에 textView 추가


//        binding.llMain.setOnTouchListener { view, motionEvent ->
//            var postion = view
//            int position = r.indexOfChild(textView)
//            return@setOnTouchListener false
//        }


//        var ivImage = myView.binding?.ivImage
        myView.setGlide(src)


        var edt = myView.binding?.edtMsiContents
        var ivImage = myView.binding?.ivImage
        var ivCancel = myView.binding?.ivCancel
        var lledtBox = myView.binding?.edtMsiContents
        var item = myView.binding?.item
        var position = 0

        ivImage?.setOnClickListener {
            position = binding.llAddView.indexOfChild(myView)
            viewModel.mySubInfoListPosition.value = position

            ImagePicker.with(this)
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)
                .galleryOnly()
                .crop(
                    4f,
                    4f
                )//Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    resultLauncherEditSubimage.launch(intent)
                }
        }
        edt?.setOnFocusChangeListener { view, b ->
            position = binding.llAddView.indexOfChild(myView)
            viewModel.mySubInfoListPosition.value = position
            editTextFocus(b)
            Log.d("CustomMyCarBoardItem : lledtBox", "$b")
        }

        ivCancel?.setOnClickListener {
            position = binding.llAddView.indexOfChild(myView)
            viewModel.mySubInfoListPosition.value = position
            binding.llAddView.removeViewAt(position)
            viewModel.my_sub_info.value?.removeAt(position)
            viewModel.my_sub_info.value = viewModel.my_sub_info.value
        }
        edt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {
                viewModel.mySubInfoListPosition.value = position
                viewModel.my_sub_info.value?.get(position)?.msi_contents = p0.toString()
                viewModel.my_sub_info.value = viewModel.my_sub_info.value
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })


    }

    override fun onResume() {
        super.onResume()
//        if (application!!.Check30days(application!!.checkguide2)) {
//
//        } else {
//            var d = GuideDialog(this, GuidePopType.GUIDE_DRIVE_WRITE) {
//                application!!.save_guide(GuidePopType.GUIDE_DRIVE_WRITE)
//            }
//            d.show(supportFragmentManager, "")
//        }
//        application?.setlocation()
    }

    private fun clickListeners() {


        binding!!.ivBack.setOnClickListener {
            finish()
        }

        binding!!.ivSave.setOnClickListener {
            saveMyCarData()
        }

        binding.tvHelp.setOnClickListener {
            var d = GuideDialog(this, GuidePopType.GUIDE_MY_CAR_BOARD_POST) {

            }

            d.show(supportFragmentManager, "")
        }

        binding!!.llMain.setOnClickListener {
            if ((viewModel.select_car_data.value?.select == false) || (viewModel.select_car_data.value?.select == null)) {
                val i = Intent(this, OwnerActivity::class.java)
                i.putExtra("view", "수정")
                resultLauncherContent.launch(i)
                this.overridePendingTransition(R.anim.fadein, R.anim.fadeout)
            }
        }

        binding!!.imageSet.setOnClickListener {

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

        binding!!.ivMain.setOnClickListener {

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
        binding!!.ivCancel.setOnClickListener {

            viewModel.mainImageUrl.value = ""
        }
        binding!!.ivCarCancel.setOnClickListener {

            for (i in viewModel.select_car_data_list.value!!) {
                i.select = false
            }
            if (viewModel.select_car_data.value != null) {
                viewModel.select_car_data.value = null
            }
            viewModel.select_car_data_list.value = viewModel.select_car_data_list.value
        }

        binding!!.llAddlist.setOnClickListener {
            if (viewModel.my_sub_info.value?.size!! < 10) {
                ImagePicker.with(this)
                    .compress(1024)         //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)
                    .galleryOnly()
                    .crop(
                        4f,
                        4f
                    )//Final image resolution will be less than 1080 x 1080(Optional)
                    .createIntent { intent ->
                        resultLauncherSubimage.launch(intent)
                    }
            }
        }

        binding.llBottomBar.setOnClickListener {


            if (prerequisite()) {
                if (viewModel.viptype.value.equals("글수정")) {
                    var editList  :ArrayList<MySubInfoData> = arrayListOf()
                    var addList  :ArrayList<MySubInfoData> = arrayListOf()

                    if (viewModel.my_sub_info.value?.size != 0) {
                        for (i in viewModel.my_sub_info.value!!) {
                            if (i.msi_seq == 0){
                                addList.add(i)
                            }else{
                                editList.add(i)
                            }
                        }
                    }

                    viewModel.updateMyCarBoard(
                        absolutelyPath(viewModel.mainImageUrl.value!!, this, "myCarMain"),
                        getmultipartlist(editList,"myCarSub"),
                        getmultipartlist(addList,"myCarSubInsert"),
                        application?.user_u_uuid.toString()
                    )
                } else {
                    viewModel.insertMyCarBoard(
                        absolutelyPath(
                            viewModel.mainImageUrl.value!!,
                            this,
                            "myCarMain"
                        ),
                        getmultipartlist(viewModel.my_sub_info.value!!,"myCarSub"),
                        application?.user_u_uuid.toString()
                    )
                }
            }


        }


        binding.tvBottom.setOnClickListener {

            if (prerequisite()) {
                if (viewModel.viptype.value.equals("글수정")) {
                    var editList  :ArrayList<MySubInfoData> = arrayListOf()
                    var addList  :ArrayList<MySubInfoData> = arrayListOf()

                    if (viewModel.my_sub_info.value?.size != 0) {
                        for (i in viewModel.my_sub_info.value!!) {
                            if (i.msi_seq == 0){
                                addList.add(i)
                            }else{
                                editList.add(i)
                            }
                        }
                    }

                    viewModel.updateMyCarBoard(
                        absolutelyPath(viewModel.mainImageUrl.value!!, this, "myCarMain"),
                        getmultipartlist(editList,"myCarSub"),
                        getmultipartlist(addList,"myCarSubInsert"),
                        application?.user_u_uuid.toString()
                    )
                } else {
                    viewModel.insertMyCarBoard(
                        absolutelyPath(
                            viewModel.mainImageUrl.value!!,
                            this,
                            "myCarMain"
                        ),
                        getmultipartlist(viewModel.my_sub_info.value!!,"myCarSub"),
                        application?.user_u_uuid.toString()
                    )
                }

            }


        }

    }

    private fun resetMyCarData() {
        application?.myCarSaveData.apply {
            this?.mainImageUrl = ""
            this?.textmainTitle = ""
            this?.textmainContext = ""
            this?.textIsMmiFirst = ""
            this?.textIsMmiSecond = ""
            this?.textIsMmithird = ""
            var selectCarData = CarSelectData("", "", "", "", "", "", "", select = false, isShowing = false)
            this?.car_select_data = selectCarData

            viewModel.select_car_data_list.value?.clear()
            this?.select_car_data_list = viewModel.select_car_data_list.value!!

            viewModel.my_sub_info.value?.clear()
            this?.my_sub_info = viewModel.my_sub_info.value!!


        }

    }
    private fun saveMyCarData() {
        application?.myCarSaveData.apply {
            this?.mainImageUrl = viewModel.mainImageUrl.value.toString()
            this?.textmainTitle = viewModel.textmainTitle.value
            this?.textmainContext = viewModel.textmainContext.value.toString()
            this?.textIsMmiFirst = viewModel.textIsMmiFirst.value.toString()
            this?.textIsMmiSecond = viewModel.textIsMmiSecond.value.toString()
            this?.textIsMmithird = viewModel.textIsMmithird.value.toString()
            this?.car_select_data = viewModel.select_car_data.value

            if (viewModel.select_car_data_list.value?.size != 0) {
                this?.select_car_data_list = viewModel.select_car_data_list.value!!
            }
            if (viewModel.my_sub_info.value?.size != 0) {
                this?.my_sub_info = viewModel.my_sub_info.value!!
            }

        }
        viewModel.istoast.value ="저장 되었습니다"
    }

    private fun checkMyCarData() {
        application?.myCarSaveData.apply {
            var check = false
            if (this?.mainImageUrl?.isNotEmpty() == true) {
                check =true
            }
            if (this?.textmainTitle?.isNotEmpty() == true) {
                check =true
            }

            if (this?.textmainContext?.isNotEmpty() == true) {
                check =true
            }

            if (this?.textIsMmiFirst?.isNotEmpty() == true) {
                check =true
            }
            if (this?.textIsMmiSecond?.isNotEmpty() == true) {
                check =true
            }
            if (this?.textIsMmithird?.isNotEmpty() == true) {
                check =true
            }
            if (this?.car_select_data?.select == true){
                check =true
            }

            if (this?.my_sub_info?.size != 0) {
                check = true
            }

            if (check) {
                var di = RpmDialog(this@MyCarBoardWritingActivity, RpmStatus.MY_CAR_SAVE_P, 0) { event, check ->
                    if (check) {
                        getMyCarData()
                    }
                }
                di.show(supportFragmentManager, "")
            }
        }

    }

    private fun getMyCarData() {
        application?.myCarSaveData.apply {
            viewModel.mainImageUrl.value = this?.mainImageUrl
            viewModel.textmainTitle.value = this?.textmainTitle
            viewModel.textmainContext.value = this?.textmainContext
            viewModel.textIsMmiFirst.value = this?.textIsMmiFirst
            viewModel.textIsMmiSecond.value = this?.textIsMmiSecond
            viewModel.textIsMmithird.value = this?.textIsMmithird
            viewModel.select_car_data.value = this?.car_select_data
            if (this?.select_car_data_list?.size != 0) {
                viewModel.select_car_data_list.value = this?.select_car_data_list!!
            }
            if (this.my_sub_info.size != 0) {
                viewModel.my_sub_info.value = this.my_sub_info

                var sublist = viewModel.my_sub_info.value

                    for (i in sublist!!) {
                        setEditAddView(i)
                    }

            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    var resultLauncherContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                if (data != null) {

                    var carBrand = data.getStringExtra("CarBrand_seq").toString()
                    var carModel = data.getStringExtra("CarModel_seq").toString()
                    var selectCarSeries = data.getStringExtra("CarSeries_seq").toString()
                    var carBrandName = data.getStringExtra("CarBrand_name").toString()
                    var carBrand_Image = data.getStringExtra("CarBrand_Image").toString()
                    var carModelName = data.getStringExtra("CarModel_name").toString()
                    var carSeries_name = data.getStringExtra("CarSeries_name").toString()

                    var sData = CarSelectData(
                        carBrand_Image,
                        carBrandName,
                        carModelName,
                        carSeries_name,
                        carBrand,
                        carModel,
                        selectCarSeries,
                        true,
                        false
                    )
//
//                    viewModel.select_car_data.value ?.card_seq = carBrand
//                    viewModel.select_car_data.value ?.carm_seq = carModel
//                    viewModel.select_car_data.value ?.csm_seq = selectCarSeries
//                    viewModel.select_car_data.value ?.carb_name = carBrandName
//                    viewModel.select_car_data.value ?.carb_image = carBrand_Image
//                    viewModel.select_car_data.value ?.carm_name = carModelName
//                    viewModel.select_car_data.value ?.csm_name = carSeries_name
//                    viewModel.select_car_data.value ?.select = true
                    viewModel.select_car_data.value = sData
                    Log.d("셀렉트 자동차", viewModel.select_car_data.value.toString())
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
                            viewResize()

                            // Glide.with(binding.imageSet).load(uri).into(binding.imageSet)
                        }
                    }
                }

            }
        }

    var resultLauncherSubimage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                if (data != null) {

                    data!!.data.let { uri ->
                        if (uri != null) {
                            viewModel.listAdd(uri.toString())
                            setAddView(uri.toString())
                            // Glide.with(binding.imageSet).load(uri).into(binding.imageSet)
                        }
                    }
                }

            }
        }

    var resultLauncherEditSubimage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                if (data != null) {

                    data!!.data.let { uri ->
                        if (uri != null) {

                            viewModel.listSet(uri.toString())
                            EditAddView(uri.toString())
                            // Glide.with(binding.imageSet).load(uri).into(binding.imageSet)
                        }
                    }
                }

            }
        }


    private fun viewResize() {
        var params = RelativeLayout.LayoutParams(binding!!.llMain.width, binding!!.llMain.width)
        binding!!.ivMain.layoutParams = params
    }

    private fun prerequisite(): Boolean {

        if (viewModel.select_car_data.value == null) {
            viewModel.istoast.value = "소개 할 차량을 등록해 주세요"
            return false
        } else if (viewModel.mainImageUrl.value.isNullOrEmpty()) {
            viewModel.istoast.value = "메인 이미지를 등록해 주세요"
            return false
        } else if (viewModel.textmainTitle.value.equals("")) {
            viewModel.istoast.value = "제목을 한 글자 이상 작성해 주세요"
            return false
        }
         else if (viewModel.textmainContext.value.equals("")) {
            viewModel.istoast.value = "내용을 한 글자 이상 작성해 주세요"
            return false
        } else if (
            viewModel.textIsMmiFirst.value.equals("")||
            viewModel.textIsMmiSecond.value.equals("") ||
            viewModel.textIsMmithird.value.equals("")

        ) {
            viewModel.istoast.value = "질문을 입력해주세요."
            return false
        } else if (mySubListLengthCheck()) {
            return false
        } else {

        }

        return true
    }

    private fun mySubListLengthCheck(): Boolean {
        if (viewModel.my_sub_info.value?.size != 0) {
            for ((count, i) in viewModel.my_sub_info.value!!.withIndex()) {
                var num = count + 1
                if (i.msi_contents?.length!! > 2000 || i.msi_contents?.length!! < 10) {
                    viewModel.istoast.value = " ${num}번째 추가 하신 내용을 10자 이상 2000자 이내로 작성해 주세요"
                    return true
                }
                if (i.msi_img?.isEmpty() == true) {
                    viewModel.istoast.value = " ${num}번째 추가 하신 사진을 등록해 주세요"
                    return true
                }

            }

        } else {
            return false
        }


        return false
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(
            com.supercarlounge.supercar.R.anim.fadein,
            com.supercarlounge.supercar.R.anim.fadeout
        )
    }

    companion object {
        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("setimageMyCarBoard")
        fun setimageMyCarBoard(view: ImageView, url: String?) {
            Log.d("Checkimage", url!!)
            // This methods should not have any return type, = declaration would make it return that object declaration.
            if (!url.isNullOrEmpty()) {
                Glide.with(view.context).load(url).override(view.width, view.height)
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

    fun getmultipartlist(list: ArrayList<MySubInfoData>,name: String): ArrayList<MultipartBody.Part> {
        var mlist = ArrayList<MultipartBody.Part>()

        for (i in list) {
            var path = i.msi_img.toString()
            var uri = Uri.parse(path.toString())
            if (!path.startsWith("http") && path != null) {
                var pathmain = getRealPathFromUriForImagesAndVideo(uri!!)
                var policefile = File(pathmain)
                var uri = Uri.parse(path)
                var body2: MultipartBody.Part? = null
                if (path.toString().endsWith("jpg") || path.toString().endsWith("png")) {

                    if (path.startsWith("http")) {

//                        val attachmentEmpty: RequestBody =
//                            RequestBody.create("text/plain".toMediaTypeOrNull(), "")
//                        body2 = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty)
                    } else {
                        body2 = getmultipart(
                            policefile,
                            name + mlist.size,
                            ".jpg",
                            "image/jpeg",
                            name
                        )
                        mlist.add(body2)
                    }


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

    fun absolutelyPath(path: String?, context: Context, name: String): MultipartBody.Part {

        var result = path?.toUri()?.let { getRealPathFromUriForImagesAndVideo(it) }
        val file = File(result)
        var fileName = path
        fileName = fileName
        var requestBody: RequestBody
        if (path?.startsWith("https") == true) {
            requestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), "")
        } else {
            requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        }

        var body: MultipartBody.Part =
            MultipartBody.Part.createFormData(name, fileName, requestBody)



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
}
