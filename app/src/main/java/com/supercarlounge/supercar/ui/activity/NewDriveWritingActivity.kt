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
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.supercarlounge.supercar.*
import com.supercarlounge.supercar.adapter.*
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.customview.MiddleItemFinder
import com.supercarlounge.supercar.customview.SuggestItemDeco
import com.supercarlounge.supercar.data.DriveAwayData
import com.supercarlounge.supercar.data.LocationSpotData
import com.supercarlounge.supercar.databinding.ActivityNewWritingBinding
import com.supercarlounge.supercar.dialog.*
import com.supercarlounge.supercar.enumset.DrivePassDialogStatus
import com.supercarlounge.supercar.enumset.RecDriveDialogType
import com.supercarlounge.supercar.viewmodel.NewDriveWritingViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*


class NewDriveWritingActivity : BaseActivitiy(), DialogEvent {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityNewWritingBinding
    private lateinit var viewModel: NewDriveWritingViewModel
    private var application: MainApplication? = null
    var profileSuggestionAdapter: DriveWritingProfileSugesstAdapter? = null
    var calenderadapter: CalenderMainAdapter? = null
    var locationSpotWritingAdapter: LocationSpotWritingAdapter? = null
    var warningDialog: WarningDialog? = null
    var imageDialog : ImageDialog? =null
    var cal: Calendar? = null
    var lastMonth: Calendar? = null
    var u_name = ""
    var u_gender = ""
    var u_birthday = ""
    var u_phone = ""
    var view = ""
    val TYPE_UP = 299;
    val TYPE_DOWN = 300;
    val HOUR_UP = 301;
    val HOUR_DOWN = 302;
    val MIN_UP = 303;
    val MIN_DOWN = 304;
    var save_nowtypeindex = 0
    var mnewState = 0
    var locationPermissionDialog: LocationPermissionDialog? = null

    private var charmCheck = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewModel = ViewModelProvider(this).get(NewDriveWritingViewModel::class.java)
        binding = ActivityNewWritingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel

        application = applicationContext as MainApplication
        u_name = intent.getStringExtra("u_name").toString()
        u_gender = intent.getStringExtra("u_gender").toString()
        u_birthday = intent.getStringExtra("u_birthday").toString()
        u_phone = intent.getStringExtra("u_phone").toString()
        view = intent.getStringExtra("qickview").toString()

        application!!.quick_view = ""

//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        viewModel.titleText.value = "글쓰기"
        viewModel.my_seq.value = application?.userData?.u_seq.toString()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            viewModel?.isPermission?.value =
                ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED||
                        ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
        }else{
            viewModel?.isPermission?.value =
                ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED||
                        ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        }

        clickListeners()
        observes()
        cal = Calendar.getInstance()
        lastMonth = Calendar.getInstance()

        viewModel.year.value = cal?.get(Calendar.YEAR)
        viewModel.month.value = cal?.get(Calendar.MONTH)
        viewModel.day.value = cal?.get(Calendar.DATE)
        viewModel.hour.value = cal?.get(Calendar.HOUR).toString()
        viewModel.min.value = cal?.get(Calendar.MINUTE).toString()
        var data: DriveAwayData? = intent.getParcelableExtra("data")

        viewModel.drivedata.value = data
        Log.d("받은 데이터", data.toString())
        var location_seq = intent.getStringExtra("location_seq")
        if (location_seq.isNullOrEmpty()){

        }else{
            viewModel.type2.value = true

        }
        viewModel.location_seq.value = location_seq
        Log.d("seq테스트3", viewModel.location_seq.value.toString())
        var writingType = intent.getStringExtra("writingType")
        viewModel.writingType.value = writingType

        if (viewModel.writingType.value == "location") {
            viewModel.state.value = 2
        }


        var vtype = intent.getIntExtra("viptype", 0)
        var heart = intent.getIntExtra("vipheart", 0)
        viewModel.position.value = intent.getIntExtra("position", 0)

        Log.d("DriveWritingActivity", vtype.toString())
        viewModel.viptype.value = vtype
        viewModel.vip_heart.value = heart.toString()

        var myCardata = application?.userCarData
        viewModel.my_car_data_list.value = myCardata


        setadapter1()
        setAdapter2()

        initMyCar()
        setpicker()
        setOnValueChangeListenerInScrolling()
        editTextFocus()
        if ( viewModel.drivedata.value?.spot_seq !="0" ){
            if (viewModel.drivedata.value?.spot_seq == null){
                viewModel.getSpotList(true)
//                viewModel.type1.value = true
            }else{
                viewModel.location_seq.value =  viewModel.drivedata.value?.spot_seq
                viewModel.getOneSpot(viewModel.drivedata.value?.spot_seq.toString())
                viewModel.type2.value = true
                viewModel.nowtypeindex.value = -1
            }

        }else{

            viewModel.getSpotList(true)
            viewModel.type1.value = true
        }

//        if (viewModel.drivedata.value.meeting_day)

    }

    private fun setAdapter3() {
        viewModel.CheckDrivePass()
        profileSuggestionAdapter = DriveWritingProfileSugesstAdapter(
//            deviceW,
//            deviceH,
            this,
            0,
            arrayListOf(), viewModel
        ) { data, position, width ->
            var listsize = profileSuggestionAdapter!!.datalist.size
            var selindex = position % listsize
            if (viewModel.selectproindex.value == selindex) {
                if (application!!.CheckCompany() == false) {
                    var slist = application?.select_index
                    var check = false
                    if (slist!!.contains(data.u_seq.toString())) {
                        check = true
                    }
                    if (!data.u_image.isNullOrEmpty()) {
                        if (check) {
                            val i = Intent(this, ProfileActivity::class.java)
                            i.putExtra("sub_seq", data.u_seq.toString())
                            startActivity(i)
                            overridePendingTransition(
                                android.R.anim.fade_in,
                                android.R.anim.fade_out
                            )
                        } else {
                            var pid: RecDriveDialogType? = RecDriveDialogType.NOT_REC_DIRVE
                            if (application!!.userData?.u_feed.equals("1") && data.u_seq > 0) {
                                if (viewModel.isDrivePass.value!! && viewModel.isDrivePassCount.value!! >= 3) {
                                    pid = RecDriveDialogType.PROFILE_NOTPASS_OPEN
                                }else{
                                    pid = RecDriveDialogType .PROFILE_OPEN
                                }
                            }
                            var did = RecDriveDialog(this, pid!!) { type, okcancel ->
                                if (type == RecDriveDialogType.PROFILE_OPEN || type == RecDriveDialogType.PROFILE_NOTPASS_OPEN) {
                                    if (okcancel) {
                                        viewModel.CheckDrivePass(data)

                                    }
                                }
                            }
                            did.show(supportFragmentManager!!, "")
                        }

                    } else {
                        warningDialog = WarningDialog(this, 0)
                        warningDialog!!.show(supportFragmentManager, "")
                    }
                } else {
                    viewModel.toast.value = this.getString(R.string.company_profile_open_text)
                }

            } else {
                viewModel.selectprodata.value = data
                viewModel.selectproindex.value = selindex
                val offset = (binding!!.locationTrack5.rvProfilesugesst.width / 2 - width / 2)
                var mnan =
                    (binding!!.locationTrack5.rvProfilesugesst.layoutManager!! as LinearLayoutManager)
                mnan.scrollToPositionWithOffset(position, offset)


            }
        }

        binding!!.locationTrack5.rvProfilesugesst.adapter = profileSuggestionAdapter
        binding!!.locationTrack5.rvProfilesugesst.addItemDecoration(SuggestItemDeco(5))
        binding!!.locationTrack5.rvProfilesugesst.apply {
            itemAnimator = null
        }

        //var layoutmanager =  CenterZoomLayoutManager(context,RecyclerView.HORIZONTAL,false,profileSuggestionAdapter!!)
        var layoutmanager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding!!.locationTrack5.rvProfilesugesst.layoutManager = layoutmanager
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.locationTrack5.rvProfilesugesst)
        binding.locationTrack5.rvProfilesugesst.addOnScrollListener(
            MiddleItemFinder(
                this,
                layoutmanager,
                object : MiddleItemFinder.MiddleItemCallback {
                    override fun scrollFinished(middleElement: Int) {
                        var listsize = profileSuggestionAdapter!!.datalist.size
                        if (listsize != 0) {
                            viewModel.selectproindex.value = middleElement % listsize
                            viewModel.selectprodata.value =
                                profileSuggestionAdapter!!.datalist.get(middleElement % profileSuggestionAdapter!!.datalist.size)

                        }
                    }

                },
                RecyclerView.SCROLL_STATE_IDLE
            )
        )
        binding!!.locationTrack5.rvProfilesugesst.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

//                if (mnewState == 0) {
//                    var scrollPosition= (binding!!.rvProfilesugesst.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
//                    val offset = (binding!!.rvProfilesugesst.width / 2 - binding!!.rvProfilesugesst.layoutManager!!.width / 2)
//                    var mnan = (binding!!.rvProfilesugesst.layoutManager!! as LinearLayoutManager)
//                    mnan.scrollToPositionWithOffset(scrollPosition, offset)
//                }

            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                Log.d("스크롤", newState.toString())

                mnewState = newState


//                    var position =
//                        (binding!!.rvProfilesugesst.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
//                    var width =
//                        (binding!!.rvProfilesugesst.layoutManager as LinearLayoutManager).width
//                    val offset = (binding!!.rvProfilesugesst.width / 2 - width / 2)
//                    var mnan = (binding!!.rvProfilesugesst.layoutManager!! as LinearLayoutManager)
//                    val position = (binding!!.rvProfilesugesst.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
//                    binding!!.rvProfilesugesst.scrollToPosition(position)

//                    mnan.scrollToPositionWithOffset(position, 100)

            }
        })
        viewModel.profilesuggest.value = arrayListOf()
        if (application?.CheckSuggestDate() == false) {
            viewModel.getprofileSuggestio()
        } else {
            viewModel.profilesuggest.value = application?.sugeestlist
            Log.d("데이터확인", viewModel.profilesuggest.value.toString())
        }
    }


    var listener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
        @SuppressLint("SuspiciousIndentation")
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            var lastposition =
                (binding.locationTrack1.rvLocationMain.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            var itemcount = locationSpotWritingAdapter!!.itemCount
            Log.d("getDrive : last", "" + itemcount)
            Log.d("getDrive : itemcount", "" + lastposition)
            if (itemcount == (lastposition + 1)) {
                var totalc = viewModel.totalCount.value!!
                if (itemcount < totalc && totalc != 0) {
                    if (itemcount < viewModel.totalCount.value!!) {
                        viewModel.page.value = viewModel.page.value?.plus(1)

                          viewModel!!.getSpotList(false,)


                        Log.d("getAnonymousForumPost", "getAnonymousForumPostScroll")

                    }
                }
            }

        }
    }

    private fun  setAdapter2() {
        var driveData = viewModel.drivedata.value
        if (driveData != null) {
            if (driveData.meeting_day.isNullOrEmpty() == false) {
                viewModel.type4.value = true

                var arry = driveData.meeting_day!!.split("-", " ", ":")
                Log.d("승주 짤린글1", arry.toString())
                for ((count, i) in arry.withIndex()) {
                    Log.d("승주 짤린글2", i)
                    when (count) {
                        0 -> viewModel.year.value = i.toInt()
                        1 -> viewModel.month.value = i.toInt()-1
                        2 -> {
                            viewModel.selectDay.value = i.toInt().toString()
                            viewModel.day.value = i.toInt()
                        }
                        3 -> {

                            var hou = i.toInt()
                            if (hou == 0) {
                                hou = 12
                                viewModel.ampm.value = 0
                            } else if (hou == 12) {
                                viewModel.ampm.value = 1
                            } else if (hou > 12) {
                                viewModel.ampm.value = 1
                                hou -= 12
                            } else {
                                viewModel.ampm.value = 0
                            }
                            viewModel.hour.value = hou.toString()

                        }
                        else -> viewModel.min.value = i.toInt().toString()
                    }
//                        else -> {
//                            var arry2 = driveData.meeting_day!!.split(" ")
//                            for ((count, j) in arry2.withIndex()){
//                                when(count) {
//                                    0 -> {
//                                        viewModel.day.value = i.toInt()
//                                        viewModel.selectDay.value = i.toInt().toString()
//                                    }
//                                    else ->{
//                                        var arry2 = driveData.meeting_day!!.split(":")
//                                        when(count) {
//                                            0 -> {
//
//                                                viewModel.hour.value =  i.toInt().toString()
//                                                if (i.toInt() > 11){
//                                                    viewModel.ampm.value = 1
//                                                }else{
//                                                    viewModel.ampm.value = 0
//                                                }
//                                            }
//                                            else->{
//                                                viewModel.min.value =  i.toInt().toString()
//                                            }
//                                        }
//                                    }
//
//                                }                                }
//                            }
//                        }

                }


            } else {
                viewModel.type3.value = true
            }
        }
        calenderadapter = CalenderMainAdapter(
            viewModel,
            this,
            setCalender(
                year = viewModel.year.value!!,
                mon = viewModel.month.value!!,
                date = viewModel.date.value!!
            ),
            viewModel.month.value!!.toString(),
            viewModel.year.value!!.toString()
        ) { data, position ->

            try {
//


                if (data.isNotEmpty()) {
                    if (position > 6) {
                        if (viewModel.selectDay.value == Integer.parseInt(data).toString()) {
                            viewModel.selectDay.value = ""
                        } else {
                            viewModel.selectDay.value = Integer.parseInt(data).toString()

                        }
                        calenderadapter?.notifyDataSetChanged()
                    }
                }


            } catch (e: NumberFormatException) {
                val result: String = data.replace(" ", "")

                if (viewModel.selectDay.value == Integer.parseInt(result).toString()) {
                    viewModel.selectDay.value = ""
                } else {
                    viewModel.selectDay.value = Integer.parseInt(result).toString()

                }
                calenderadapter?.notifyDataSetChanged()
            } catch (e: Exception) {

            }

        }
        var date = viewModel.date.value


        binding.locationTrack2.gridView.adapter = calenderadapter;
        calenderadapter!!.setDayPosition(date!!)
        calenderadapter!!.setMonth(viewModel.month.value.toString())
        calenderadapter!!.setYear(viewModel.year.value.toString())


    }

    @SuppressLint("NotifyDataSetChanged")
    fun setadapter1() {


        locationSpotWritingAdapter =
            LocationSpotWritingAdapter(viewModel, this, viewModel.spotlist.value!!) { data, index ->


                if (viewModel.select_index.value == index) {
                    viewModel.select_index.value = 999999
                    viewModel.location_seq.value = ""
                    viewModel.detaildata.value = null
                } else {
                    viewModel.detaildata.value = data
                    viewModel.location_seq.value = data.spot_seq
                    viewModel.select_index.value = index
                    Log.d("seq테스트2", viewModel.location_seq.value.toString())
                }

                locationSpotWritingAdapter?.notifyDataSetChanged()
            }

        binding!!.locationTrack1.rvLocationMain.adapter = locationSpotWritingAdapter
        binding!!.locationTrack1.rvLocationMain.layoutManager = LinearLayoutManager(this)
        binding.locationTrack1.rvLocationMain.addOnScrollListener(listener)
//        var driveData = viewModel.drivedata.value
//        if (driveData != null) {
//            if (driveData.spot_seq?.isNotEmpty() == true) {
//
//                viewModel.type2.value = true
//            } else {
//                viewModel.type1.value = true
//            }
//
//
//        }
    }

    fun initMyCar() {
        var myCardata = application?.userCarData
        var myUserdata = application?.userData
        viewModel.my_car_data_list.value = myCardata

        viewModel.isownercheck.value = myUserdata?.u_type.equals("OWNER")
        viewModel.isowner.value = myUserdata?.u_type.equals("OWNER")
        var data: DriveAwayData? = intent.getParcelableExtra("data")
        var driveData = viewModel.drivedata.value
        if (driveData != null) {
           if (driveData?.uc_seq != 0){
               viewModel.type5.value = true
           }else{
               viewModel.type6.value = true
           }
            if (driveData?.b_how!= null){
                viewModel.arrayBooleantype3.value?.set(driveData.b_how,true)
            }
            if (!driveData.b_title.isNullOrEmpty()){
                viewModel.dtitletext.value = driveData.b_title
            }
            if (!driveData.b_contents.isNullOrEmpty()){
                viewModel.dcontenttext.value = driveData.b_contents
            }
            if (!driveData.b_cover.isNullOrEmpty()){
                viewModel.mainImageUrl.value = driveData.b_cover
//                Glide.with(binding.locationTrack4.imageSet).load(viewModel.mainImageUrl.value)
//                    .centerInside()
//                    .into(binding.locationTrack4.imageSet)
            }
//            viewModel.isownercheck.value = data?.ub_type.equals("DRIVER")
            if (viewModel.my_car_data_list.value?.get(0) != null) {
                if (viewModel.my_car_data_list.value?.get(0)!!.size != 0) {
                    if (data?.uc_seq?.equals("0") == false) {
                        for ((count, i) in viewModel.my_car_data_list.value!!.get(0)!!
                            .withIndex()) {
                            if (data?.uc_seq!!.equals(i.uc_seq)) {
                                when (count) {
                                    0 -> {
                                        viewModel.myCarData.value = i
                                        viewModel.myCarChecked1.value = true

                                    }
                                    1 -> {
                                        viewModel.myCarData.value = i
                                        viewModel.myCarChecked2.value = true
                                    }
                                    2 -> {
                                        viewModel.myCarData.value = i
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
//                viewModel.myCarData.value = viewModel.my_car_data_list.value?.get(0)?.get(0)
            }
            when (viewModel.my_car_data_list.value?.get(0)?.size) {
                1 -> {

//                    binding.llMyCar1.visibility = View.VISIBLE
                    binding.locationTrack3.tvMyCar1.text =
                        viewModel.my_car_data_list.value?.get(0)!!.get(0).carb_name
                    Glide.with(this)
                        .load(viewModel.my_car_data_list.value?.get(0)!!.get(0).carb_image)
                        .centerInside().into(binding.locationTrack3.ivMyCar1)

                }
                2 -> {
//                    binding.llMyCar1.visibility = View.VISIBLE
//                    binding.llMyCar2.visibility = View.VISIBLE
                    binding.locationTrack3.tvMyCar1.text =
                        viewModel.my_car_data_list.value?.get(0)!!.get(0).carb_name
                    binding.locationTrack3.tvMyCar2.text =
                        viewModel.my_car_data_list.value?.get(0)!!.get(1).carb_name
                    Glide.with(this)
                        .load(viewModel.my_car_data_list.value?.get(0)!!.get(0).carb_image)
                        .centerInside().into(binding.locationTrack3.ivMyCar1)
                    Glide.with(this)
                        .load(viewModel.my_car_data_list.value?.get(0)!!.get(1).carb_image)
                        .centerInside().into(binding.locationTrack3.ivMyCar2)
                }
                3 -> {

//                    binding.llMyCar1.visibility = View.VISIBLE
//                    binding.llMyCar2.visibility = View.VISIBLE
//                    binding.llMyCar3.visibility = View.VISIBLE
                    binding.locationTrack3.tvMyCar1.text =
                        viewModel.my_car_data_list.value?.get(0)!!.get(0).carb_name
                    binding.locationTrack3.tvMyCar2.text =
                        viewModel.my_car_data_list.value?.get(0)!!.get(1).carb_name
                    binding.locationTrack3.tvMyCar3.text =
                        viewModel.my_car_data_list.value?.get(0)!!.get(2).carb_name
                    Glide.with(this)
                        .load(viewModel.my_car_data_list.value?.get(0)!!.get(0).carb_image)
                        .centerInside().into(binding.locationTrack3.ivMyCar1)
                    Glide.with(this)
                        .load(viewModel.my_car_data_list.value?.get(0)!!.get(1).carb_image)
                        .centerInside().into(binding.locationTrack3.ivMyCar2)
                    Glide.with(this)
                        .load(viewModel.my_car_data_list.value?.get(0)!!.get(2).carb_image)
                        .centerInside().into(binding.locationTrack3.ivMyCar3)
                }
                else -> {}
            }

            if (viewModel!!.my_car_data_list.value?.get(0) != null) {
                for ((count, i) in viewModel!!.my_car_data_list.value?.get(0)!!.withIndex()) {
                    if (i.uc_return.equals("Y") && i.uc_car_main_yn.equals("Y") && i.uc_insur_url_yn.equals(
                            "Y"
                        )
                    ) {
                        if (count == 0) {
                            binding.locationTrack3.llMyCar1.visibility = View.VISIBLE
                        } else if (count == 1) {
                            binding.locationTrack3.llMyCar2.visibility = View.VISIBLE
                        } else if (count == 2) {
                            binding.locationTrack3.llMyCar3.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        viewModel.myu_uuid.value = application!!.user_u_uuid

    }

    private fun editTextFocus() {
        binding.locationTrack4.edtTitle.setOnFocusChangeListener { view, b ->
            viewModel.isMainEdit.value = b
        }
        binding.locationTrack4.edtCertificationNumber.setOnFocusChangeListener { view, b ->
            viewModel.isMainEdit.value = b
        }


    }


    private fun observes() {

        viewModel.toast.observe(this, Observer { toast ->
            if (!toast.isNullOrEmpty()) {
                if (toast.equals("하트가 부족합니다. 하트를 충전해주세요.")) {
                    var drivePassPayDialog = DrivePassPayDialog(
                        this,
                        DrivePassDialogStatus.NOTHEART,
                        "",
                        0
                    ) { type, isok ->
                        if (isok) {
                            val i = Intent(this, HeartsShopActivity::class.java)
                            startActivity(i)
                            overridePendingTransition(
                                android.R.anim.fade_in,
                                android.R.anim.fade_out
                            )
                        }

                    }
                    drivePassPayDialog!!.show(supportFragmentManager, "")
                } else {
                    Toast.makeText(this, toast, Toast.LENGTH_SHORT).show()
                }

            }
        })

        viewModel.spotlist.observe(this, Observer {
            if (it.size != 0) {
                if (viewModel.drivedata.value != null) {
                    if (viewModel.drivedata.value?.spot_seq!!.isNotEmpty()) {
                        for ((count, i) in it.withIndex()) {
                            if (viewModel.drivedata.value?.spot_seq.toString() == i.spot_seq.toString()) {
                                viewModel.select_index.value = count
                                locationSpotWritingAdapter?.notifyDataSetChanged()
                            }

                        }

                    }
                }
            }
        })
        viewModel.isnotheart.observe(this, Observer {
            if (it) {
                var notheart = TwoButtonDialog(
                    this,
                    Constans.DIALOG_LACK_HEART,
                    this,
                    Constans.DIALOG_LACK_HEART
                )
                notheart!!.show(supportFragmentManager, "")
            }
        })
        viewModel.open_profile_seq.observe(this, Observer { seq ->
            if (!seq.isNullOrEmpty()) {
                application?.setselectdata(seq)
                profileSuggestionAdapter?.notifyDataSetChanged()
//                profileSuggestionAdapter!!.notifyDataSetChanged()
                val i = Intent(this, ProfileActivity::class.java)
                i.putExtra("sub_seq", seq)
                startActivity(i)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

            }
        })
        viewModel.state.observe(this, Observer {
            if (it != null) {
                textSet()
                if (it == 5) {
                    viewModel.bottomText.value = "닫기"
                    binding.ivBack.visibility = View.INVISIBLE
                    if (profileSuggestionAdapter == null) {
                        setAdapter3()
                    }
                } else {
                    binding.ivBack.visibility = View.VISIBLE
                    viewModel.bottomText.value = "다음"
                }
            }
        })
        viewModel.viewvip.observe(this, Observer {
            if (it) {
                var i = Intent(this, DriveVipRankActivity::class.java)
                i.putExtra("inputtype", true)
                resultLauncherVip.launch(i)
            }
        })

        viewModel.back.observe(this, Observer {
//            if (it){
//                if (viewModel.state.value == 1){
//                    finish()
//                }else{
//                    viewModel.state.value = viewModel.state.value?.minus(1)
//                }
//
//            }
        })
        viewModel.next.observe(this, Observer {
//            if(it){
//                if (viewModel.state.value == 4){
//                    finish()
//                }else{
//                    viewModel.state.value = viewModel.state.value?.plus(1)
//                }
//            }
        })

        viewModel.backEvent.observe(this, Observer {
            if (it) {
                var page = 1
                if (viewModel.writingType.value == "location") {
                    page++
                }

                if (viewModel.state.value!! <= page) {
                    this.finish()
                } else {
                    if (viewModel.state.value!! > page) {
                        backPage()


                    }

                }

            }
        })
        viewModel.nextEvent.observe(this, Observer {
            if (it) {

                if (viewModel.state.value == 5) {
                    var i = Intent()
                    if (viewModel.sendsuccess.value.equals("vip")) {
                        i.putExtra("vip", true)
                    }

                    var data: DriveAwayData? = intent.getParcelableExtra("data")
                    var index = 0
                    for ((count, i) in viewModel.arrayBooleantype3.value!!.withIndex()){
                        if (i){
                            index = count
                        }
                    }
                    data?.b_cover = viewModel.mainImageUrl.value.toString()
                    data?.b_title = viewModel.dtitletext.value.toString()
                    data?.b_contents = viewModel.dcontenttext.value.toString()

                    data?.b_when = if (viewModel.type3.value!!) 0 else 1
                    data?.b_how = index
                    if (viewModel.type2.value == true){
                        data?.spot_seq = viewModel.location_seq.value.toString()
                        Log.d("seq테스트1", viewModel.location_seq.value.toString())
                    }else{
                        data?.spot_seq = "0"
                    }
                    if (viewModel.type4.value == true){
                        data?.meeting_day =viewModel.dateMapping()
                    }else{
                        data?.meeting_day = ""
                    }
                    if (viewModel.type6.value  == true){
                        data?.uc_seq = 0
                        data?.carb_image = ""
                        data?.carb_name = ""
                    }else{
                        data?.carb_image = viewModel?.myCarData?.value?.carb_image!!
                        data?.uc_seq = viewModel?.myCarData?.value?.uc_seq!!
                    }


                    if (data != null) {
                        Log.d("테스트", data.toString())
                        i.putExtra("data", data)
                        i.putExtra("position", viewModel.position.value)
                    }


                    setResult(Activity.RESULT_OK, i)
                    this.finish()
                } else {
                    if (viewModel.state.value!! < 5) {
                        if (viewModel.state.value == 1){
                            if (viewModel.type1.value ==true || viewModel.type2.value ==true){
                                nextPage()
                            }else{
                                viewModel.toast.value = "필수로 항목 하나를 선택해 주세요"

                            }

                        }else if (viewModel.state.value == 2){
                            if (viewModel.type3.value ==true|| viewModel.type4.value ==true){
                                nextPage()
                            }else{
                                viewModel.toast.value = "필수로 항목 하나를 선택해 주세요"

                            }

                        }else{
                            nextPage()
                        }


                    }
                }
            }
        })



        viewModel.type1.observe(this, Observer {
            if (it) {
                viewModel.type2.value = !it
                val toolbar: CollapsingToolbarLayout = binding.locationTrack1.toolbar

                val params = toolbar.layoutParams as AppBarLayout.LayoutParams
                params.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL
                textSet()
            }
        })

        viewModel.type2.observe(this, Observer {
            if (it) {
                viewModel.type1.value = !it
                val toolbar: CollapsingToolbarLayout = binding.locationTrack1.toolbar

                val params = toolbar.layoutParams as AppBarLayout.LayoutParams
                params.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL

                textSet()
            }
        })

        viewModel.type3.observe(this, Observer {
            if (it) {
                viewModel.type4.value = !it
                textSet()
            }
        })

        viewModel.type4.observe(this, Observer {
            if (it) {
                viewModel.type3.value = !it
                textSet()
            }
        })

        viewModel.type5.observe(this, Observer {
            if (it) {
                viewModel.type6.value = !it
                textSet()
            }
        })

        viewModel.type6.observe(this, Observer {
            if (it) {
                viewModel.type5.value = !it
                textSet()
            }
        })
        viewModel.select_index.observe(this, Observer {
            textSet()
        })

        viewModel.selectDay.observe(this, Observer {
            textSet()
            viewModel.dateMapping()
        })
        viewModel.myCarData.observe(this, Observer {
            textSet()
        })
        viewModel.arrayBooleantype3.observe(this, Observer {
            textSet()
        })
        viewModel.mainImageUrl.observe(this, Observer {
            textSet()
        })
        viewModel.dtitletext.observe(this, Observer {
            textSet()
        })
        viewModel.dcontenttext.observe(this, Observer {
            textSet()
        })

        viewModel!!.nextMonth.observe(this, Observer {

            if (it) {
                viewModel.selectDay.value = ""
                var mon = viewModel.month.value!! - 1
                var year = viewModel.year.value
                var date = viewModel.date.value
                if (mon < 12) {
                    mon += 1
                    calenderadapter?.setAddAllList(
                        setCalender(
                            year = year!!,
                            mon = mon,
                            date = date!!
                        )
                    )
                    calenderadapter!!.setMonth(mon.toString())

                    calenderadapter!!.setYear(year.toString())

                    Log.d("다음달", "year = $year , mon = $mon , date = $date")


                } else if (year != null) {

                    mon = 1
                    year = year?.plus(1)
                    calenderadapter?.setAddAllList(
                        setCalender(
                            year = year!!,
                            mon = mon,
                            date = date!!
                        )
                    )
                    calenderadapter!!.setMonth(mon.toString())

                    calenderadapter!!.setYear(year.toString())


                }
                viewModel.year.value = year
                viewModel.month.value = mon + 1


            }

        })

        viewModel!!.backMonth.observe(this, Observer {

            if (it) {
                viewModel.selectDay.value = ""
                var mon = viewModel.month.value!! - 1
                var year = viewModel.year.value
                var start = viewModel.start.value
                var end = viewModel.end.value
                var date = viewModel.date.value

                if (mon!! < 2) {
                    mon = 12
                    year = year?.minus(1)
                    calenderadapter?.setAddAllList(
                        setCalender(
                            year = year!!,
                            mon = mon!!,
                            date = date!!
                        )
                    )
                    calenderadapter!!.setMonth(mon.toString())
                    calenderadapter!!.setYear(year.toString())


                } else {
                    mon -= 1
                    calenderadapter?.setAddAllList(
                        setCalender(
                            year = year!!,
                            mon = mon!!,
                            date = date!!
                        )
                    )

                    calenderadapter!!.setMonth(mon.toString())

                    calenderadapter!!.setYear(year.toString())


                }
                viewModel.year.value = year
                viewModel.month.value = mon + 1


            }

        })







        viewModel!!.typeUp.observe(this, Observer {

            if (it) {
                timePickerController(TYPE_UP, 1)

            }

        })

        viewModel!!.typeDown.observe(this, Observer {

            if (it) {
                timePickerController(TYPE_DOWN, 1)
            }

        })

        viewModel!!.hourUp.observe(this, Observer {

            if (it) {
                timePickerController(HOUR_UP, 1)

            }

        })

        viewModel!!.hourDown.observe(this, Observer {

            if (it) {
                timePickerController(HOUR_DOWN, 1)
            }

        })

        viewModel!!.minuteUp.observe(this, Observer {

            if (it) {
                timePickerController(MIN_UP, 1)

            }

        })

        viewModel!!.minuteDown.observe(this, Observer {

            if (it) {
                timePickerController(MIN_DOWN, 1)
            }

        })

        viewModel.nowtypeindex.observe(this, Observer {
            if (it != save_nowtypeindex) {
                viewModel.select_index.value = 999999
                save_nowtypeindex = it
            }


                viewModel.resetpage()
                viewModel.getSpotList(true)


        })
        viewModel.permissionEvent.observe(this, Observer {
            if (it) {
                locationPermissionDialog = LocationPermissionDialog(this, 0) { okcancel ,index->
                    if (okcancel) {
                        viewModel.isPermission.value = true
                        viewModel.eventAroundMe.value = true
                        locationPermissionDialog?.dismiss()

                    } else {
                        locationPermissionDialog?.dismiss()
                    }

                }
                locationPermissionDialog!!.show(supportFragmentManager, "")
            }
        })
        viewModel.eventAroundMe.observe(this, Observer {
            if (it){
                viewModel.nowtypeindex.value  =1
            }

        })
        viewModel.sendsuccess.observe(this, Observer {
            if (it.isNotEmpty()) {
                viewModel.toast.value = "성공적으로 드라이브가 등록되었습니다."
            }
        })
        viewModel.sendVip.observe(this, Observer {
            if (it.isNotEmpty()) {
                viewModel.toast.value = "성공적으로 VIP 드라이브가 등록되었습니다."
            }
        })
    }

    private fun textSet() {
        var page = viewModel.state.value
        var type1 = viewModel.type1.value
        var type2 = viewModel.type2.value
        var type3 = viewModel.type3.value
        var type4 = viewModel.type4.value
        var type5 = viewModel.type5.value
        var type6 = viewModel.type6.value
        var sex = application?.userData?.u_gender

        when (page) {
            1 -> {
                if (type1 == false && type2 == false) {
                    viewModel.successText.value = false
                    return
                }
                if (type2 == true) {
                    if (viewModel.select_index.value == 999999) {
                        viewModel.successText.value = false
                        return
                    }

                }

            }
            2 -> {
                if (type3 == false && type4 == false) {
                    viewModel.successText.value = false
                    return
                }
                if (type4 == true) {
                    if (viewModel.selectDay.value.isNullOrEmpty()) {
                        viewModel.successText.value = false
                        return
                    }

                }
            }

            3 -> {


                if (viewModel.isowner.value == true) {
                    if (type5 == false && type6 == false) {
                        viewModel.successText.value = false
                        return
                    }
                    if (type5 == true) {
                        if (viewModel.myCarData.value == null) {
                            viewModel.successText.value = false
                            return
                        }
                    }

                }

                var d = false
                for (i in viewModel.arrayBooleantype3.value!!) {
                    if (i) {
                        d = i
                    }
                }
                if (!d) {
                    viewModel.successText.value = false
                    return
                }

            }
            4 -> {

                if (viewModel.mainImageUrl.value.isNullOrEmpty()) {
                    viewModel.successText.value = false
                    return
                }
                if (viewModel.dtitletext.value.isNullOrEmpty()) {
                    viewModel.successText.value = false
                    return
                }
                if (viewModel.dcontenttext.value.isNullOrEmpty()) {
                    viewModel.successText.value = false
                    return
                }

            }
            else -> {}
        }
        viewModel.successText.value = true
    }

    private fun backPage() {

        viewModel.state.value = viewModel.state.value?.minus(1)
    }

    private fun nextPage() {
        var page = viewModel.state.value
        var type1 = viewModel.type1.value
        var type2 = viewModel.type2.value
        var type3 = viewModel.type3.value
        var type4 = viewModel.type4.value
        var type5 = viewModel.type5.value
        var type6 = viewModel.type6.value
        var sex = application?.userData?.u_gender

        when (page) {
            1 -> {
                if (type1 == false && type2 == false) {
                    viewModel.toast.value = "둘중 하나를 선택해 주세요"
                    return
                }
                if (type2 == true) {
                    if (viewModel.select_index.value == 999999) {
                        viewModel.toast.value = "장소를 선택해 주세요"
                        return
                    }

                }
            }
            2 -> {
                if (type3 == false && type4 == false) {
                    viewModel.toast.value = "둘중 하나를 선택해 주세요"
                    return
                }
                if (type4 == true) {
                    if (viewModel.selectDay.value.isNullOrEmpty()) {
                        viewModel.toast.value = "날짜를 지정해 주세요"
                        return
                    }

                }
            }

            3 -> {


                if (viewModel.isowner.value == true) {

                    if (type5 == false && type6 == false) {
                        viewModel.toast.value = "둘중 하나를 선택해 주세요"
                        return
                    }
                    if (type5 == true) {
                        if (viewModel.myCarData.value == null) {
                            viewModel.toast.value = "차량을 선택해 주세요"
                            return
                        }
                    }

                }

                var d = false
                for (i in viewModel.arrayBooleantype3.value!!) {
                    if (i) {
                        d = i
                    }
                }
                if (!d) {
                    viewModel.toast.value = "드라이브 유형을 선택해 주세요"
                    return
                }

            }
            4 -> {

                if (viewModel.mainImageUrl.value.isNullOrEmpty()) {
                    viewModel.toast.value = "이미지를 등록해 주세요"
                    return
                }
                if (viewModel.dtitletext.value.isNullOrEmpty()) {
                    viewModel.toast.value = "제목을 적어주세요"
                    return
                }
                if (viewModel.dcontenttext.value.isNullOrEmpty()) {
                    viewModel.toast.value = "내용을 적어주세요"
                    return
                }

                if (viewModel.drivedata.value == null) {
                        var driveRegiDialog = DriveRegiDialog(this) { onoff, c_gender, c_pay2, c_pay1 ->
                                if (onoff) {
                                    viewModel.boardInsert(application?.getRealPathFromUriForImagesAndVideo(viewModel.mainImageUrl.value!!.toUri()).toString(), false, false, false)
                                }
                            }
                        driveRegiDialog.show(supportFragmentManager, "")
                    return
                } else {
                    var realpath = ""
                    if (!viewModel.mainImageUrl.value!!.startsWith("https")) {
                        realpath = application!!.getRealPathFromUriForImagesAndVideo(viewModel.mainImageUrl.value!!.toUri())!!
                    }
                    viewModel.boardEditInsert(realpath, false, false, false)
                    return


                }
            }
        }


        viewModel.state.value = viewModel.state.value?.plus(1)
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
    private fun timePickerController(type: Int, count: Int) {
        var typeY = viewModel.typeY.value
        var hourY = viewModel.hourY.value
        var minY = viewModel.minY.value
        when (type) {
            TYPE_UP -> {
                if (typeY == 1) typeY = 0 else typeY = 1
                binding!!.locationTrack2.typePicker.smoothScrollToValue(typeY)
                viewModel.typeY.value = typeY
            }

            TYPE_DOWN -> {
                if (typeY == 0) typeY = 1 else typeY = 0
                binding!!.locationTrack2.typePicker.smoothScrollToValue(typeY)
                viewModel.typeY.value = typeY
            }


            HOUR_UP -> if (hourY != null) {
                {
                    hourY -= count
                    binding!!.locationTrack2.hourPicker.smoothScrollToValue(hourY)
                }
            }

            HOUR_DOWN -> if (hourY != null) {
                {
                    hourY += count
                    binding!!.locationTrack2.hourPicker.smoothScrollToValue(hourY)
                }
            }


            MIN_UP -> if (minY != null) {
                {
                    minY -= count
                    binding!!.locationTrack2.minPicker.smoothScrollToValue(minY)
                }
            }


            MIN_DOWN -> if (minY != null) {
                {
                    minY += count
                    binding!!.locationTrack2.minPicker.smoothScrollToValue(minY)
                }
            }


        }

    }


    private fun setOnValueChangeListenerInScrolling() {
        binding!!.locationTrack2.typePicker.setOnValueChangeListenerInScrolling { picker, oldVal, newVal ->
            Log.d(
                "typePicker _ setOnValueChangeListenerInScrolling:",
                "picker: $picker ,oldVal: $oldVal , newVal: $newVal "
            )

            viewModel.ampm.value = newVal


        }
        binding!!.locationTrack2.hourPicker.setOnValueChangeListenerInScrolling { picker, oldVal, newVal ->
            Log.d(
                "hourPicker _ setOnValueChangeListenerInScrolling:",
                "picker: $picker ,oldVal: $oldVal , newVal: $newVal "
            )

            val value = newVal + 1
            viewModel.hour.value = (value).toString()


        }
        binding!!.locationTrack2.minPicker.setOnValueChangeListenerInScrolling { picker, oldVal, newVal ->
            Log.d(
                "hourPicker _ setOnValueChangeListenerInScrolling:",
                "picker: $picker ,oldVal: $oldVal , newVal: $newVal "
            )

            if (newVal < 10) {
                viewModel.min.value = "0" + (newVal).toString()
            } else {
                viewModel.min.value = (newVal).toString()
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
                        viewModel.VipDriveUpload(viewModel.b_seq.value.toString())
                    }
                }

            }
        }

    private fun clickListeners() {
        binding.llBottomBar.setOnClickListener {
        }
        binding.ivBack.setOnClickListener {
        }
        binding.ivNext.setOnClickListener {
        }

        binding!!.locationTrack4.imageSet.setOnClickListener {
            imageDialog = ImageDialog(this) {  okcancel ->

                    if (okcancel) {
                        com.github.dhaval2404.imagepicker.ImagePicker.with(this)
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
                        imageDialog?.dismiss()
                    }else{
                        imageDialog?.dismiss()
                        var i = Intent(this, ImageActivity::class.java)

                        imageLauncher.launch(i)

                    }

            }
            imageDialog?.show(supportFragmentManager,"")

        }

        binding!!.locationTrack3.llMyCar1.setOnClickListener {
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

        binding!!.locationTrack3.llMyCar2.setOnClickListener {
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
        binding!!.locationTrack3.llMyCar3.setOnClickListener {
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
    var imageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data

                if (data != null) {
                    var src=  data.getStringExtra("data")
                    if (src?.isNotEmpty() == true){
                        viewModel.defaultImage.value = true
                    }
                    viewModel.mainImageUrl.value = src


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
                            viewModel.defaultImage.value = false
                            viewModel.mainImageUrl.value = uri.toString()
                            // Glide.with(binding.imageSet).load(uri).into(binding.imageSet)
                        }
                    }
                }

            }
        }

    private fun setText(state: Int) {
        when (state) {
            0 -> {
                viewModel.mainText.value = "초대 코드"
//                viewModel.subText.value ="\"초대 코드를 입력해 주세요. \\n\" + \"초대 코드가 입력된 가입신청은 \\n\" + \"우선적으로 가입심사를 해 드립니다.\""
            }
            1 -> {
                viewModel.mainText.value = "매력을 선택해 주세요."
                viewModel.subText.value = "쵀대 3개까지 선택 가능합니다다."
            }
        }
    }


    fun setCalender(year: Int, mon: Int, date: Int): ArrayList<String> {

        val list: ArrayList<String> = arrayListOf()

        //이번달력
        //고른 년도,월,일

        val month = mon + 1
        val maxMonth = 12;
        val minMonth = 0;

        //이번달
        cal!!.set(year, month, date)

        //이전달
        if (mon > 1) {
            lastMonth!!.set(year, month, date)
        } else {
            lastMonth!!.set(year - 1, maxMonth, date)
        }
        val min = cal!!.getMinimum(Calendar.DAY_OF_MONTH)
        val max = cal!!.getActualMaximum(Calendar.DAY_OF_MONTH)
        val week = cal!!.get(Calendar.DAY_OF_WEEK)
        val lastMonthMin = lastMonth!!.getMinimum(Calendar.DAY_OF_MONTH)
        val lastMonthMax = lastMonth!!.getActualMaximum(Calendar.DAY_OF_MONTH)
        val lastMonthWeek = lastMonth!!.get(Calendar.DAY_OF_WEEK)
        val minWeek = dayMinWeek(year, month)
        //1일,2월,3화,4수,5목,6금,7토
        viewModel.monthString.value = MonthDay(month)
        viewModel.yearString.value = year.toString()
        viewModel.week.value = dayWeek(week)
        viewModel!!.day.value = date
        viewModel!!.month.value = month
        viewModel!!.year.value = year

        //
        Log.d("day_min", min.toString())
        Log.d("day_max", max.toString())

        //요일
        list.add("SUN")
        list.add("MON")
        list.add("TUE")
        list.add("WED")
        list.add("THU")
        list.add("FRI")
        list.add("SAT")

        //저번달 나머지
        var d = minWeek - 1
        for (i in 1 until minWeek) {
            var x = lastMonthMax
            d -= 1
            var day = lastMonthMax - d
            list.add(day.toString())
        }

        viewModel.start.value = list.size - 1

        //이번달
        (min..max).forEach { i ->
            var blank = " "
            if (i < 10) {
                list.add(blank + i.toString() + blank)
            } else {
                list.add(i.toString())
            }
        }
        viewModel.end.value = list.size

        //다음달 나머지
        var day = 0
        (list.size..48).forEach { i ->
            var blank = " "
            day += 1
            if (day < 10) {
                list.add(blank + day.toString())
            } else {
                list.add(day.toString())
            }
        }



        Log.d("셋 캘린더 사이즈", list.size.toString())
        return list
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

    private fun dayMinWeek(year: Int, mon: Int): Int {
        val cal: Calendar = Calendar.getInstance()


        val month = mon - 1
        val maxMonth = 11;
        val date = 1

        cal.set(year, month, date)


        val week = cal.get(Calendar.DAY_OF_WEEK)

        return week
    }

    private fun dayWeek(week: Int): String {
        var day = ""
        when (week) {
            1 -> day = "Sunday"
            2 -> day = "Monday"
            3 -> day = "Tuesday"
            4 -> day = "Wednesday"
            5 -> day = "Thursday"
            6 -> day = "Friday"
            7 -> day = "Saturday"
        }
        Log.d("day_WEEK", day.toString())
        return day
    }

    private fun MonthDay(mm: Int): String {
        var Month = ""
        when (mm) {

            2 -> Month = "February"
            3 -> Month = "March"
            4 -> Month = "April"
            5 -> Month = "May"
            6 -> Month = "June"
            7 -> Month = "July"
            8 -> Month = "August"
            9 -> Month = "September"
            10 -> Month = "October"
            11 -> Month = "November"
            12 -> Month = "December"
            13 -> Month = "January"

        }
        Log.d("day_WEEK", Month.toString())
        return Month
    }

    private fun setpicker() {
        var typeDisplay = arrayOf<String>()
        var hourDisplay = arrayOf<String>()
        var minDisplay = arrayOf<String>()
        val hourTime = 13
        var recentHour = viewModel.hour.value
        val recentMin = viewModel.min.value
        var startTime = 1






        typeDisplay += "오전"
        typeDisplay += "오후"


        //시간
        val x = "0"
        for (i in startTime until hourTime) {
//            if (timetype) {

//                if (i < 10) {
//                    hourDisplay += x + i.toString()
//                } else {
//                    hourDisplay += i.toString()
//                }

//            } else {
            hourDisplay += i.toString()
//            }
        }
        for (i in 0..59) {
            if (i < 10) {
                minDisplay += x + i.toString()
            } else {
                minDisplay += i.toString()
            }
        }

        binding!!.locationTrack2.typePicker.displayedValues = typeDisplay
        binding!!.locationTrack2.typePicker.maxValue = typeDisplay.size - 1

        binding!!.locationTrack2.hourPicker.displayedValues = hourDisplay
        binding!!.locationTrack2.hourPicker.maxValue = hourDisplay.size - 1

        binding!!.locationTrack2.minPicker.displayedValues = minDisplay
        binding!!.locationTrack2.minPicker.maxValue = minDisplay.size - 1



        viewModel.typeY.value = getTimeType(recentHour.toString())
        viewModel.hourY.value = getHour(recentHour.toString())
        viewModel.minY.value = getMin(recentMin!!)

        binding!!.locationTrack2.typePicker.value = viewModel.ampm.value!!

//            getTimeType(recentHour)

        //시간 12,24, 타입변경

        binding!!.locationTrack2.hourPicker.value = getHour(recentHour.toString())
        binding!!.locationTrack2.minPicker.value = getMin(recentMin)

    }

    private fun getTimeType(time: String): Int {
        val time = time.toInt()
        var type = 0

        if (time >= 12) {
            type = 1
        }
        return type
    }

    private fun getHour(time: String): Int {

        var hour = time.toInt() - 1
        if (hour < 0) hour = 11
        return hour

    }

    override fun onResume() {
        super.onResume()



    }


    private fun getMin(time: String): Int {
        var min = 0
        min = time.toInt()
        return min
    }

    companion object {
        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("setnewimagedrive")
        fun setnewimagedrive(view: ImageView, url: String?) {
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
    }

    override fun finish() {
        var start = 2
        if (viewModel.writingType.value == "location") {
            start++
        }


        if (viewModel.state.value!! in start..4) {
            viewModel.state.value = viewModel.state.value!!.minus(1)
        } else {
            super.finish()
        }



        overridePendingTransition(
            com.supercarlounge.supercar.R.anim.fadein,
            com.supercarlounge.supercar.R.anim.fadeout
        )
    }

    override fun okEvent(
        type: Int,
        okAndCancel: Boolean,
        comment_value: String,
        seq: String,
        u_nickname: String
    ) {
        TODO("Not yet implemented")
    }


}