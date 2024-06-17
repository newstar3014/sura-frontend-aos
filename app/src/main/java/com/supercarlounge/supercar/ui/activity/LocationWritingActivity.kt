package com.supercarlounge.supercar.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.data.DriveAwayData
import com.supercarlounge.supercar.data.SubImage
import com.supercarlounge.supercar.databinding.*
import com.supercarlounge.supercar.viewmodel.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class LocationWritingActivity : BaseActivitiy(), OnMapReadyCallback {

    private lateinit var binding: ActivityLocationWritingBinding
    private lateinit var viewModel: LocationWritingViewModel
    var application: MainApplication? = null
    private var naverMap: NaverMap? = null
    private var addressWebView: AddressWebViewActivity? = null
    val marker = Marker()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LocationWritingViewModel::class.java)
        binding = ActivityLocationWritingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel
        application = applicationContext as MainApplication


        var data: DriveAwayData? = intent.getParcelableExtra("data")
        var myUserdata = application?.userData
        var myCardata = application?.userCarData
        viewModel.my_car_data_list.value = myCardata
        viewModel.myu_uuid.value = application!!.user_u_uuid
        viewModel.my_seq.value = myUserdata?.u_seq.toString()

        clickListeners()
        observes()
        editTextFocus()
        binding!!.ivAdd.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                setView()
                binding!!.ivAdd.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }


        })

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
    }

    private fun setView() {


        var params =
            LinearLayoutCompat.LayoutParams(binding.ivAdd.width, binding.edtBoxAddress.height)
        params.marginStart = 20
        binding.ivAdd.layoutParams = params

    }


    private fun observes() {
        viewModel.istoast.observe(this, Observer {
            if (it.isNotEmpty()) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }

        })

        viewModel.dtitletext.observe(this, Observer {
            if (it.isNotEmpty()){
                marker.captionText = it
            }
        })



        viewModel.eventImage.observe(this, Observer {
            ImagePicker.with(this)
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)
                .galleryOnly()
                .crop(
                    4f,
                    4f
                )//Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    var index = it
                    var add = 999
                    if (add == index) {
                        if (viewModel.image_list.value?.size!! > 9) {
                            viewModel.istoast.value = "사진을 10장이상 등록 하실 수 없습니다"
                        } else {
                            resultLauncherimageAdd.launch(intent)
                        }


                    } else {
                        if (viewModel.image_list.value?.size!! <= index) {
                            resultLauncherimageAdd.launch(intent)
                        } else {
                            resultLauncherimageEdit.launch(intent)

                        }
                    }

                }
        })

        viewModel.eventAddAndCancel.observe(this, Observer {

            if (viewModel.image_list.value?.size!! > it) {
                viewModel.image_list.value?.removeAt(it)
                viewModel.image_list.value = viewModel.image_list.value

            } else {
                ImagePicker.with(this)
                    .compress(1024)         //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)
                    .galleryOnly()
                    .crop(
                        3f,
                        4f
                    )//Final image resolution will be less than 1080 x 1080(Optional)
                    .createIntent { intent ->
                        var index = it
                        if (viewModel.image_list.value?.size!! <= index) {
                            resultLauncherimageAdd.launch(intent)
                        }
                    }
            }

        })


        viewModel.sendsuccess.observe(this, Observer {
            if (it.isNotEmpty()) {

                viewModel.istoast.value = "성공적으로 추천 장소가 등록되었습니다."
//                var i = Intent()
//                if (it.equals("vip")) {
//                    i.putExtra("vip", true)
//                }
//
//                var data: DriveAwayData? = intent.getParcelableExtra("data")
//                data?.b_cover = viewModel.mainImageUrl.value.toString()
//                data?.b_title = viewModel.dtitletext.value.toString()
//                data?.b_contents = viewModel.dcontenttext.value.toString()
//
//                if (data != null) {
//                    Log.d("테스트", data.toString())
//                    i.putExtra("data", data)
//                    i.putExtra("position", viewModel.position.value)
//                }
//
//
//                setResult(Activity.RESULT_OK, i)
                this.finish()
            }
        })
        viewModel.isDriveCourse.observe(this, Observer {
            textSet()
            if (it) {
                viewModel.isDiningRoom.value = !it

                viewModel.isCafe.value = !it
                viewModel.isEtc.value = !it
            }
        })
        viewModel.isDiningRoom.observe(this, Observer {
            textSet()
            if (it) {
                viewModel.isDriveCourse.value = !it

                viewModel.isCafe.value = !it
                viewModel.isEtc.value = !it
            }
        })
        viewModel.isCafe.observe(this, Observer {
            textSet()
            if (it) {
                viewModel.isDriveCourse.value = !it

                viewModel.isDiningRoom.value = !it
                viewModel.isEtc.value = !it
            }
        })

        viewModel.isEtc.observe(this, Observer {
            textSet()
            if (it) {
                viewModel.isDriveCourse.value = !it

                viewModel.isDiningRoom.value = !it
                viewModel.isCafe.value = !it
            }
        })
        viewModel.tvParking.observe(this, Observer {
            textSet()
        })
        viewModel.tvPhoneNumber.observe(this, Observer {
            textSet()
        })
        viewModel.tvOpeningHours.observe(this, Observer {
            textSet()
        })
        viewModel.tvNomination.observe(this, Observer {
            textSet()
        })
        viewModel.image_list.observe(this, Observer {
            textSet()
        })


        viewModel.tvAddress.observe(this, Observer {
            textSet()
        })
        viewModel.dtitletext.observe(this, Observer {
            textSet()
        })

        viewModel.dcontenttext.observe(this, Observer {
            viewModel.dcontentCount.value = it.length.toString()
            textSet()
        })

        viewModel.addressSearch.observe(this, Observer {
            if (it) {
                addressWebView = AddressWebViewActivity() { address, latitude, longitude ->


                    viewModel.tvNomination.value = address
                    viewModel.mapVisible.value = true
                    viewModel.lat.value = latitude
                    viewModel.lon.value = longitude
                    binding.edtNomination.setText(address)



                    var handler = Handler()

                    var runnable = Runnable {
                        run {
                            val carmeraUpdate = CameraUpdate.scrollTo(
                                LatLng(
                                    latitude.toDouble(),
                                    longitude.toDouble()
                                )
                            )
                            marker.position = LatLng(latitude.toDouble(), longitude.toDouble())
                            naverMap?.moveCamera(carmeraUpdate)
                            marker.map = naverMap
                        }
                    }
                    handler.postDelayed(runnable, 500)


                }
                addressWebView!!.show(supportFragmentManager, "")
            }
        })





        viewModel.ispost.observe(this, Observer {
            if (it) {
                var isChecked = false
                if (viewModel.isDriveCourse.value == true) {
                    isChecked = true
                }
                if (viewModel.isDiningRoom.value == true) {
                    isChecked = true
                }
                if (viewModel.isCafe.value == true) {
                    isChecked = true
                }
                if (viewModel.isEtc.value == true) {
                    isChecked = true
                }

                var pathstart = viewModel.image_list.value!!
                if (pathstart.size == 0) {
                    viewModel.istoast.value = "사진을 등록해 주세요"
                } else if (viewModel.dtitletext.value.isNullOrEmpty()) {
                    viewModel.istoast.value = "제목을 입력해 주세요"
                } else if (viewModel.dcontenttext.value.isNullOrEmpty()) {
                    viewModel.istoast.value = "내용을 입력해 주세요"
                } else if (viewModel.tvNomination.value.isNullOrEmpty()) {
                    viewModel.istoast.value = "주소를 등록해 주세요"
//                } else if (viewModel.tvAddress.value.isNullOrEmpty()) {
//                    viewModel.istoast.value = "상세주소를 입력해 주세요"
                } else if (!isChecked) {
                    viewModel.istoast.value = "카테고리를 지정해 주세요"
                } else if (viewModel.tvPhoneNumber.value.isNullOrEmpty()) {
                    viewModel.istoast.value = "전화번호를 입력해 주세요"
                } else if (viewModel.tvOpeningHours.value.isNullOrEmpty()) {
                    viewModel.istoast.value = "영업시간을 입력해 주세요"
                } else if (viewModel.tvOpeningHours.value.isNullOrEmpty()) {
                    viewModel.istoast.value = "주차가능여부를 입력해 주세요"
                } else {
                    if (viewModel.drivedata.value == null) {
//                        var driveRegiDialog =
//                            DriveRegiDialog(this) { onoff, c_gender, c_pay2, c_pay1 ->
//                                if (onoff) {
                        viewModel.boardInsert(
                            getmultipartlist(viewModel.image_list.value!!, "SubImage"),
                        )
//                                }
//                            }
//                        driveRegiDialog.show(supportFragmentManager, "")

                    } else {
                        var realpath = ""
//                        if (!pathstart.startsWith("https")) {
//                            realpath = application!!.getRealPathFromUriForImagesAndVideo(pathstart.toUri())!!
//                        }
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


    private fun textSet() {
        if (viewModel.image_list.value?.size!! == 0) {
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
        if (viewModel.tvNomination.value.isNullOrEmpty()) {
            viewModel.successText.value = false
            return
        }
        if (viewModel.tvAddress.value.isNullOrEmpty()) {
            viewModel.successText.value = false
            return
        }
        if (viewModel.tvAddress.value.isNullOrEmpty()) {
            viewModel.successText.value = false
            return
        }

        var checked = false
        if (viewModel.isDriveCourse.value == true) {
            checked = true
        } else if (viewModel.isDiningRoom.value == true) {
            checked = true
        } else if (viewModel.isCafe.value == true) {
            checked = true
        } else if (viewModel.isEtc.value == true) {
            checked = true
        }
        if (!checked) {
            viewModel.successText.value = false
            return
        }

        if (viewModel.tvPhoneNumber.value.isNullOrEmpty()) {
            viewModel.successText.value = false
            return
        }

        if (viewModel.tvOpeningHours.value.isNullOrEmpty()) {
            viewModel.successText.value = false
            return
        }
        if (viewModel.tvParking.value.isNullOrEmpty()) {
            viewModel.successText.value = false
            return
        }
        viewModel.successText.value = true
    }

    fun getmultipartlist(
        list: ArrayList<String>,
        type: String
    ): ArrayList<MultipartBody.Part> {


        var mlist = ArrayList<MultipartBody.Part>()
        for (i in list) {
            var path = i
            var uri = Uri.parse(path.toString())
            if (!path.startsWith("http") && path != null) {
                var pathmain = getRealPathFromUriForImagesAndVideo(uri!!)
                var policefile = File(pathmain)
                var uri = Uri.parse(path)
                var body2: MultipartBody.Part? = null
                if (path.toString().endsWith("jpg") || path.toString().endsWith("png")) {
                    body2 = getmultipart(
                        policefile,
                        "locationImage" + mlist.size,
                        ".jpg",
                        "image/jpeg",
                        "locationImage"
                    )
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

    fun getnowdatestring(): String {
        var now = System.currentTimeMillis()
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var d = Date(now)
        var nowdate = format.format(d)
        return nowdate
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

    private fun editTextFocus() {
        binding.edtTitle.setOnFocusChangeListener { view, b ->
            viewModel.isMainEdit.value = b
        }
        binding.edtContent.setOnFocusChangeListener { view, b ->
            viewModel.isMainEdit.value = b
        }

        binding.edtNomination.setOnFocusChangeListener { view, b ->
            viewModel.isNominationEdit.value = b

        }
        binding.edtAddress.setOnFocusChangeListener { view, b ->
            viewModel.isAddressEdit.value = b

        }
        binding.edtPhoneNumber.setOnFocusChangeListener { view, b ->
            viewModel.isPhoneNumberEdit.value = b

        }

        binding.edtPhoneNumber.setOnFocusChangeListener { view, b ->
            viewModel.isPhoneNumberEdit.value = b

        }
        binding.edtEat.setOnFocusChangeListener { view, b ->
            viewModel.isEatEdit.value = b

        }
        binding.edtOpeningHours.setOnFocusChangeListener { view, b ->
            viewModel.isOpeningHoursEdit.value = b

        }

        binding.edtParking.setOnFocusChangeListener { view, b ->
            viewModel.isParkingEdit.value = b

        }
    }


    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
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
//        binding!!.imageSet.setOnClickListener {
//
//            ImagePicker.with(this)
//                .compress(1024)         //Final image size will be less than 1 MB(Optional)
//                .maxResultSize(1080, 1080)
//                .galleryOnly()
//                .crop(
//                    3f,
//                    4f
//                )//Final image resolution will be less than 1080 x 1080(Optional)
//                .createIntent { intent ->
//                    resultLauncherimage.launch(intent)
//                }
//        }


    }

    var resultLauncherimageAdd =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                if (data != null) {

                    data!!.data.let { uri ->
                        if (uri != null) {
                            viewModel.image_list.value?.add(uri.toString())
                            viewModel.image_list.value = viewModel.image_list.value
                        }
                    }
                }

            }
        }

    var resultLauncherimageEdit =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                if (data != null) {

                    data!!.data.let { uri ->
                        if (uri != null) {
                            viewModel.image_list.value?.set(
                                viewModel.eventImage.value!!,
                                uri.toString()
                            )
                            viewModel.image_list.value = viewModel.image_list.value
                        }
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
                    .override(view.width, view.height).centerInside()
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

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map
        naverMap?.maxZoom = 18.0
        naverMap?.minZoom = 10.0
    }


}
