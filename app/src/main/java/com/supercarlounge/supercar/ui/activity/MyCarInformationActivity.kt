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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.github.dhaval2404.imagepicker.ImagePicker

import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.MyCarListAdapter
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.data.UserInformationCarData
import com.supercarlounge.supercar.databinding.ActivityMyCarInformationBinding
import com.supercarlounge.supercar.viewmodel.MyCarInformationModel



class MyCarInformationActivity : BaseActivitiy() {

    private lateinit var binding: ActivityMyCarInformationBinding
    private lateinit var viewModel: MyCarInformationModel
    private lateinit var adapter: MyCarListAdapter
    var application: MainApplication? = null
    var type = 0

    var carBrand = ""
    var carModel = ""
    var selectCarSeries = ""
    var carBrand_Image = ""
    var carBrandName = ""
    var carModelName = ""
    var carSeries_name = ""
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
        uc_income = ""
        ,"","",""
    )
    var nonData2 = UserInformationCarData(
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
        uc_income = ""
        ,"","",""
    )
    var nonData3 = UserInformationCarData(
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
        uc_income = ""
        ,"","",""
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(MyCarInformationModel::class.java)
        binding = ActivityMyCarInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        application = applicationContext as MainApplication
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel
        clickListeners()
        observes()
        var cardatalist = intent.getSerializableExtra("carDataList") as ArrayList<UserInformationCarData>
//        original_car_image_list
        viewModel.delete_my_car_data_list.value =intent.getSerializableExtra("delete_my_car_data_list") as ArrayList<UserInformationCarData>
        viewModel.original_car_image_list.value =intent.getSerializableExtra("original_car_image_list") as ArrayList<UserInformationCarData>

        viewModel.carDataList.value?.addAll(cardatalist)
        viewModel.carDataList_really.addAll(cardatalist)
        var list: ArrayList<String> = arrayListOf("", "", "", "", "")

        var BlankData = UserInformationCarData(
            carb_image = null,
            carb_name = "",
            carm_name = "",
            csm_name = "",
            csm_seq = "-1",
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
            uc_income = ""
            ,"","",""
        )
        when (viewModel.carDataList.value!!.size) {
            0 -> {
                viewModel.carDataList.value!!.add(0, BlankData)
                viewModel.carDataList.value!!.add(nonData1)
                viewModel.carDataList.value!!.add(nonData2)
                viewModel.carDataList.value!!.add(nonData3)
                viewModel.carDataList.value!!.add(BlankData)
            }
            1 -> {
                viewModel.carDataList.value!!.add(0, BlankData)
                viewModel.carDataList.value!!.add(nonData1)
                viewModel.carDataList.value!!.add(nonData2)
                viewModel.carDataList.value!!.add(BlankData)
            }
            2 -> {
                viewModel.carDataList.value!!.add(0, BlankData)
                viewModel.carDataList.value!!.add(nonData1)
                viewModel.carDataList.value!!.add(BlankData)
            }
            3 -> {
                viewModel.carDataList.value!!.add(0, BlankData)
                viewModel.carDataList.value!!.add(BlankData)
            }
            else -> {}
        }
        //어댑터 연결하기
        adapter = MyCarListAdapter(
            resources,
            this,
            viewModel.carDataList.value!!,
            viewModel
        ) { data, position ->


            if (!data.uc_check.equals("Y")) {
                val nonData3 = UserInformationCarData(
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
                    uc_income = ""
                    ,"","",""
                )
                val nonData4 = UserInformationCarData(
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
                    uc_income = ""
                    ,"","",""
                )
                val nonData5 = UserInformationCarData(
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
                    uc_income = ""
                ,"","",""
                )

                if (viewModel.carDataList.value!![position].uc_seq != 0) {

                    for ((count, i) in viewModel.carDataList_really.withIndex()){


                        if (i==  viewModel.carDataList.value!![position]){
                            viewModel.delete_my_car_data_list.value?.set(count,viewModel.carDataList.value!![position])
                        }
                    }


                    Log.d("확인 차",  viewModel.delete_my_car_data_list.value.toString())
                }
                viewModel.carDataList.value!!.removeAt(position)

                if (position == 1) {
                    viewModel.carDataList.value!!.add(
                        viewModel.carDataList.value!!.size - 1,
                        nonData3
                    )
                } else if (position == 2) {
                    viewModel.carDataList.value!!.add(
                        viewModel.carDataList.value!!.size - 1,
                        nonData4
                    )
                } else {
                    viewModel.carDataList.value!!.add(
                        viewModel.carDataList.value!!.size - 1,
                        nonData5
                    )
                }
                viewModel.carDataList.value = viewModel.carDataList.value
                setViewState()
                adapter.notifyDataSetChanged()
            }


        }

        val snap = PagerSnapHelper()
        snap.attachToRecyclerView(binding!!.rvCarModel)
        binding!!.rvCarModel.adapter = adapter
        binding!!.rvCarModel.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding!!.rvCarModel.setHasFixedSize(true)
        val decoration = DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL)
        binding!!.rvCarModel.addItemDecoration(decoration)
        viewModel.isEditCheck.value = false

        Log.d("데이터 확인", viewModel.carDataList.value.toString())
        binding!!.rvCarModel.setOnScrollListener(onScrollListener)
        setViewState()
//        viewModel.getalim(application!!.userData!!.u_seq)
    }


    private fun observes() {
        viewModel.position.observe(this, Observer {


        })
        viewModel.isEditCheck.observe(this, Observer {
            isEditCheck(it)



            if (it == true){
                var positionData = viewModel.carDataList.value?.get(viewModel.position.value!! + 1)
                if (positionData?.uc_car_main_yn.equals("N")||positionData?.uc_car_main_yn.equals("S")){

                    binding.rlCarImage.isEnabled = true
                    binding.ivCarImage.isEnabled = true
                    Log.d("테스트1111", viewModel.isEditCheck.value.toString())
                }else{
                    binding.rlCarImage.isEnabled = false
                    binding.ivCarImage.isEnabled = false
                    Log.d("테스트22222", viewModel.isEditCheck.value.toString())

                }
                if(positionData?.uc_car_main_yn.equals("Y")){
                    binding.llMaincar.visibility = View.VISIBLE
                }else{
                    binding.llMaincar.visibility = View.INVISIBLE
                }
            }else{
                    binding.llMaincar.visibility = View.INVISIBLE
                binding.rlCarImage.isEnabled = false
                binding.ivCarImage.isEnabled = false
                Log.d("테스트33333", viewModel.isEditCheck.value.toString())
            }




        })

        viewModel.mainCarCheck.observe(this, Observer {


//


        })
    }


    val onScrollListener = object : RecyclerView.OnScrollListener() {
        var temp: Int = 1
        var sc = 0
        override fun onScrolled(@NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
//            if (temp == 2) {
            super.onScrolled(recyclerView, dx, dy)


//                setViewState()

//            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)



            if (newState == 0) {
                viewModel.position.value =
                    (binding!!.rvCarModel.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                setViewState()
                Log.d("포지션123", " newState = 0")
            }

            temp = newState

        }
    }

    private fun isEditCheck(boolean: Boolean) {

        var position = viewModel.position.value!! + 1
        var carImageIsNotEmpty1 = viewModel.carDataList.value?.get(position)?.uc_car_main!!.isNotEmpty()
        var carImageIsNotEmpty2 = viewModel.carDataList.value?.get(position)?.new_url!!.isNotEmpty()
        var insurImageIsNotEmpty = viewModel.carDataList.value?.get(position)?.uc_insur_url!!.isNotEmpty()
        var carDataIsNotEmpty = viewModel.carDataList.value?.get(position)?.carb_image!!.isNotEmpty()
        var positionData = viewModel.carDataList.value?.get(viewModel.position.value!! + 1)
        Log.d("테스트 position", position.toString())
        Log.d("테스트 carImageIsNotEmpty", carImageIsNotEmpty2.toString())
        Log.d("테스트 insurImageIsNotEmpty", insurImageIsNotEmpty.toString())
        Log.d("테스트 carDataIsNotEmpty", carDataIsNotEmpty.toString())




        //수정중이면 true
        if (boolean) {




            binding.llEdit.visibility = View.INVISIBLE
            binding.ivCarSelect.visibility = View.VISIBLE
//            binding.ivCarImage.visibility = View.VISIBLE


            viewModel.bottomText.value = "수정"

            if (carImageIsNotEmpty1 || carImageIsNotEmpty2) {
//                binding.rlCarImage.visibility = View.VISIBLE

                binding.tvCarEdit.visibility = View.INVISIBLE
            } else {
//                binding.rlCarImage.visibility = View.INVISIBLE

                binding.tvCarEdit.visibility = View.VISIBLE
            }

            if (insurImageIsNotEmpty) {

                if (positionData?.uc_car_main_yn.equals("N")) {
                    binding.tvCarEdit.visibility = View.VISIBLE
                }
                Log.d("보험 yn", positionData?.uc_insur_url_yn.toString())
                if (positionData?.uc_insur_url_yn!!.equals("N")) {
                    binding.tvInsurEdit.visibility = View.VISIBLE
                } else {
                    binding.tvInsurEdit.visibility = View.INVISIBLE
                }

            }
            if (insurImageIsNotEmpty) {
                if (positionData?.uc_insur_url_yn.equals("N")) {
                    binding.tvInsurEdit.visibility = View.VISIBLE
                } else if (positionData?.uc_insur_url_yn!!.isEmpty()) {
//                    binding.tvInsurEdit.visibility= View.VISIBLE
                    binding.tvInsurEdit.visibility = View.INVISIBLE
                } else {
                    binding.tvInsurEdit.visibility = View.INVISIBLE
                }
            } else {
                binding.tvInsurEdit.visibility = View.VISIBLE
            }
//            if (viewModel.carDataList.value?.get(viewModel.position.value!!+1)?.uc_insur_url!!.isNotEmpty()){
//                binding.tvInsurEdit.visibility = View.INVISIBLE
//            }else{
//                binding.tvInsurEdit.visibility = View.VISIBLE
//            }
        } else {
            binding.rlCarImage.isEnabled = false
            binding.ivCarImage.isEnabled = false
        if (positionData?.uc_insur_url_yn!!.isEmpty()) {
//                    binding.tvInsurEdit.visibility= View.VISIBLE
            binding.tvInsurEdit.visibility = View.VISIBLE
        }else{
            binding.tvInsurEdit.visibility = View.INVISIBLE
        }


            viewModel.bottomText.value = "확인"


            if (carImageIsNotEmpty1 || carImageIsNotEmpty2) {

                binding.llEdit.visibility = View.VISIBLE
                binding.ivCarSelect.visibility = View.INVISIBLE
            } else {
                binding.llEdit.visibility = View.INVISIBLE
                binding.ivCarSelect.visibility = View.VISIBLE
//                binding.rlCarImage.visibility = View.GONE

                binding.tvCarEdit.visibility = View.VISIBLE
            }



        }


    }

    private fun clickListeners() {
        binding!!.ivBack.setOnClickListener {
            this.finish()
        }
        binding!!.llEdit.setOnClickListener {
            viewModel.isEditCheck.value = true
        }

        binding!!.llMaincar.setOnClickListener {
           binding.cbMaincar.isChecked = !binding.cbMaincar.isChecked
            var position = viewModel.position.value!! + 1
            var cardata = viewModel.carDataList.value?.get(position)

            viewModel.carDataList.value?.get(position)?.uc_check = "Y"
                if (position == 1) {
                    if (viewModel.carDataList.value?.get(2)?.carb_image!!.isNotEmpty()) {
                        viewModel.carDataList.value?.get(2)?.uc_check = "N"
                        Log.d("포지션 체크1111", position.toString())
                    }
                    if (viewModel.carDataList.value?.get(3)?.carb_image!!.isNotEmpty()) {
                        Log.d("포지션 체크1222", position.toString())
                        viewModel.carDataList.value?.get(3)?.uc_check = "N"
                    }

                } else if (position == 2) {
                    if (viewModel.carDataList.value?.get(1)?.carb_image!!.isNotEmpty()) {
                        viewModel.carDataList.value?.get(1)?.uc_check = "N"
                        Log.d("포지션 체크2111", position.toString())
                    }
                    if (viewModel.carDataList.value?.get(3)?.carb_image!!.isNotEmpty()) {
                        viewModel.carDataList.value?.get(3)?.uc_check = "N"
                        Log.d("포지션 체크2222", position.toString())
                    }
                    Log.d("포지션 체크2", position.toString())

                } else if (position == 3) {
                    if (viewModel.carDataList.value?.get(1)?.carb_image!!.isNotEmpty()) {
                        viewModel.carDataList.value?.get(1)?.uc_check = "N"
                    }
                    if (viewModel.carDataList.value?.get(2)?.carb_image!!.isNotEmpty()) {
                        viewModel.carDataList.value?.get(2)?.uc_check = "N"
                    }
                    Log.d("포지션 체크3", position.toString())
                }
                viewModel.carDataList.value = viewModel.carDataList.value
                adapter.notifyDataSetChanged()

        }


        binding!!.llBottomBar.setOnClickListener {

            if (viewModel.isEditCheck.value == true) {

                viewModel.isEditCheck.value = false

            } else {

                var list: ArrayList<ArrayList<UserInformationCarData>> = arrayListOf()
                var innerList: ArrayList<UserInformationCarData> = arrayListOf()
                var count = 0
                var mainCarCheck = false
                for (i in viewModel.carDataList.value!!) {

                    if (i.carb_image?.isNotEmpty() == true) {
                        innerList.add(i)

                    }

                }


                for (x in innerList) {
                    if (x.carb_image?.isNotEmpty() == true && x.uc_insur_url?.isNotEmpty() == true && x.uc_car_main?.isNotEmpty() == true||x.new_url?.isNotEmpty() == true) {
                        count++
                        list.add(innerList)
                    }
                    if (x.uc_check?.equals("Y") == true) {
                        mainCarCheck = true
                    }
                }


                if (mainCarCheck){
                    if (innerList.size == count ) {

                        Log.d("넘겨준 차량리스트 사이즈", innerList.size.toString())
                        var i = Intent()
                        i.putExtra("carList", list)
                        i.putExtra("delete_carList", viewModel.delete_my_car_data_list.value)

                        setResult(Activity.RESULT_OK, i)
                        this.finish()
                    } else {
                        Toast.makeText(this, "차량관련 사진을 빠짐없이 등록해주세요", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this, "대표차량을 지정해주세요", Toast.LENGTH_SHORT).show()
                }

            }


        }

        binding!!.ivCarSelect.setOnClickListener {
            var selectData = viewModel.position.value?.let { it1 ->
                viewModel.carDataList.value?.get(
                    it1 + 1
                )
            }
            val i = Intent(this, OwnerActivity::class.java)
            i.putExtra("data", selectData)
            i.putExtra("view", "수정")
            resultLauncherContent.launch(i)
            this.overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        }
        binding!!.tvCarEdit.setOnClickListener {
            ImagePicker.with(this@MyCarInformationActivity)
                .galleryOnly()
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)
                .crop(
                    4f,
                    4f
                )//Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->

                    resultLauncherCarImage.launch(intent)


                }
        }

        binding!!.tvInsurEdit.setOnClickListener {
            ImagePicker.with(this@MyCarInformationActivity)
                .galleryOnly()
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)
                .crop(
                    4f,
                    4f
                )//Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    resultLauncherinsurImage.launch(intent)
                }
        }
        binding!!.rlCarImage.setOnClickListener {
            ImagePicker.with(this@MyCarInformationActivity)
                .galleryOnly()
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)
                .crop(
                    4f,
                    4f
                )//Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->

                    resultLauncherCarImage.launch(intent)


                }
        }
        var d = "d"
        d.isNotEmpty()
    }

    @SuppressLint("NotifyDataSetChanged")
    var resultLauncherCarImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                if (data != null) {

                    data!!.data.let { uri ->
                        Log.d("포지션99", viewModel.position.value.toString())
                        viewModel.position.value?.let { it1 ->

                            var position = it1 + 1

                            if (viewModel.carDataList_really.size  > it1) {
                                viewModel.carDataList.value?.get(position)!!.uc_before_url = viewModel.carDataList_really?.get(it1)!!.uc_car_main
                            }

                                viewModel.carDataList.value?.get(position)!!.new_url = uri.toString()
                               viewModel.carDataList.value?.get(position)!!.uc_car_main_temp = ""
                            viewModel.carDataList.value?.get(position)!!.uc_car_main = uri.toString()
                              viewModel.carDataList.value?.get(position)!!.uc_car_main_yn = "S"
                            var requestOptions = RequestOptions()
                            requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(16))
                            Glide.with(binding.ivCarImage)
                                .load(viewModel.carDataList.value?.get(viewModel.position.value!! + 1)?.new_url)
                                .apply(requestOptions).into(binding.ivCarImage)
                            Glide.with(binding.ivCarBg).load(R.color.bg).apply(requestOptions).into(binding.ivCarBg)


                            viewModel.mainImageUrl.value = uri.toString()
                            setViewState()
                        }

                    }

                }
            }


        }

    @SuppressLint("NotifyDataSetChanged")
    var resultLauncherinsurImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                if (data != null) {

                    data!!.data.let { uri ->

                        viewModel.position.value?.let { it1 ->
                            var position = it1 + 1
                            if (viewModel.carDataList.value?.get(it1)!!.carb_name?.equals("브랜드 선택") == true) {
                                position = it1
                            }

                            viewModel.carDataList.value?.get(position)!!.uc_insur_url =
                                uri.toString()
                            setViewState()
                        }

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

                    carBrand = data.getStringExtra("CarBrand_seq").toString()
                    carModel = data.getStringExtra("CarModel_seq").toString()
                    selectCarSeries = data.getStringExtra("CarSeries_seq").toString()
                    carBrandName = data.getStringExtra("CarBrand_name").toString()
                    carBrand_Image = data.getStringExtra("CarBrand_Image").toString()
                    carModelName = data.getStringExtra("CarModel_name").toString()


                        carSeries_name = data.getStringExtra("CarSeries_name").toString()

//                    if (carSeries_name.equals("null")){
//                        binding.llCarDeepModel1.visibility = View.GONE
//                        binding.llCarDeepModel2.visibility = View.GONE
//                        binding.viewCarDeepModel.visibility = View.GONE
//                    }else{
//                        binding.llCarDeepModel1.visibility = View.VISIBLE
//                        binding.llCarDeepModel2.visibility = View.VISIBLE
//                        binding.viewCarDeepModel.visibility = View.VISIBLE
//                    }
                    Log.d(
                        "데이터",
                        "carBrand = $carBrand,carModel = $carModel,selectCarSeries = $selectCarSeries,carBrandName = $carBrandName,carModelName = $carModelName,carSeries_name = $carSeries_name"
                    )
                    viewModel.position.value?.let { it1 ->
                        var position = it1 + 1
                        if (viewModel.carDataList.value?.get(it1)!!.carb_name?.equals("브랜드 선택") == true) {
                            position = it1
                        }

                        viewModel.carDataList.value?.get(position)!!.uc_brand = carBrand
                        viewModel.carDataList.value?.get(position)!!.uc_model = carModel
                        viewModel.carDataList.value?.get(position)!!.csm_seq = selectCarSeries
                        viewModel.carDataList.value?.get(position)!!.csm_seq = selectCarSeries
                        viewModel.carDataList.value?.get(position)!!.carb_name = carBrandName
                        viewModel.carDataList.value?.get(position)!!.carm_name = carModelName
                        viewModel.carDataList.value?.get(position)!!.csm_name = carSeries_name
                        viewModel.carDataList.value?.get(position)!!.carb_image = carBrand_Image
                        viewModel.carDataList.value?.get(position)!!.uc_before_url = ""

//                        viewModel.carDataList.value?.get(position)!!.uc_insur_url = ""
//                        viewModel.carDataList.value?.get(position)!!.uc_car_main = ""
                        viewModel.carDataList.value = viewModel.carDataList.value


                        setViewState()

                        adapter.notifyItemChanged(position)
                        Log.d("확인 포지션", position.toString())

                    }


//                    data.
                }

            }
        }

    private fun setViewState() {
        var position = viewModel.position.value!! + 1
        var positionData = viewModel.carDataList.value?.get(position)

        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(16))
        Log.d("사진 url", positionData?.uc_car_main_temp.toString())



        if (positionData?.uc_car_main_temp?.startsWith("https") == true){
            Glide.with(binding.ivCarImage)
                .load(positionData.uc_car_main_temp)
                .apply(requestOptions).into(binding.ivCarImage)
            binding.ivCarImage.visibility = View.VISIBLE
            binding.rlCarImage.visibility = View.VISIBLE
            Log.d("타입 3", "setViewState: 1")
        }else {
            if (positionData?.uc_car_main!!.isNullOrEmpty()){
                binding.ivCarImage.visibility = View.GONE
                binding.rlCarImage.visibility= View.GONE
            }else{
                Log.d("타입 2", "setViewState: 1")
                Glide.with(binding.ivCarImage)
                    .load(positionData?.uc_car_main)
                    .apply(requestOptions).into(binding.ivCarImage)
                binding.ivCarImage.visibility = View.VISIBLE
                binding.rlCarImage.visibility = View.VISIBLE
            }

        }

                when(positionData?.uc_car_main_yn){
                    "Y" -> viewModel.car_yn_text.value = "승인"
                    "M" -> viewModel.car_yn_text.value = "승인중"
                    "N" -> viewModel.car_yn_text.value = "반려"
                    else -> viewModel.car_yn_text.value = "수정"
                }


        Log.d("포지션 ", position.toString())
        Log.d("포지션 데이터", positionData.toString())
        if (positionData?.uc_check!!.isNotEmpty()) {
            binding.cbMaincar.isChecked = positionData?.uc_check.equals("Y")
        } else {
            Log.d("포지션", "setViewState: 3")
            binding.cbMaincar.isChecked = false
        }

        if (positionData.carm_name == null){
            binding.tvModel.text = positionData.uc_model
        }else{
            binding.tvModel.text = positionData.carm_name
        }
        if (positionData.csm_name == null){
            binding.llCarDeepModel1.visibility = View.GONE
            binding.llCarDeepModel2.visibility = View.GONE
            binding.viewCarDeepModel.visibility = View.GONE
        }else{
            if (positionData.csm_name .equals("null") ||positionData.csm_name .equals("0")){
                binding.llCarDeepModel1.visibility = View.GONE
                binding.llCarDeepModel2.visibility = View.GONE
                binding.viewCarDeepModel.visibility = View.GONE
            }else{
                binding.llCarDeepModel1.visibility = View.VISIBLE
                binding.llCarDeepModel2.visibility = View.VISIBLE
                binding.viewCarDeepModel.visibility = View.VISIBLE
            }

        }

        if (positionData?.carb_image!!.isNotEmpty()) {

            binding.tvBland.setTextColor(
                ContextCompat.getColor(
                    this@MyCarInformationActivity,
                    R.color.white
                )
            )
            binding.tvModel.setTextColor(
                ContextCompat.getColor(
                    this@MyCarInformationActivity,
                    R.color.white
                )
            )
            binding.tvSeries.setTextColor(
                ContextCompat.getColor(
                    this@MyCarInformationActivity,
                    R.color.white
                )
            )
            binding.llInsurance.visibility = View.VISIBLE
            binding.llCar.visibility = View.VISIBLE
            binding.insurBottomBar.visibility = View.VISIBLE
            binding.carBottomBar.visibility = View.VISIBLE
        } else {

            binding.tvBland.setTextColor(
                ContextCompat.getColor(
                    this@MyCarInformationActivity,
                    R.color.Blank
                )
            )
            binding.tvModel.setTextColor(
                ContextCompat.getColor(
                    this@MyCarInformationActivity,
                    R.color.Blank
                )
            )
            binding.tvSeries.setTextColor(
                ContextCompat.getColor(
                    this@MyCarInformationActivity,
                    R.color.Blank
                )
            )
            binding.llInsurance.visibility = View.INVISIBLE
            binding.llCar.visibility = View.INVISIBLE
            binding.insurBottomBar.visibility = View.INVISIBLE
            binding.carBottomBar.visibility = View.INVISIBLE
        }

        if (positionData.uc_insur_url!!.isNotEmpty()) {

            viewModel.tvInsuranceEdit.value = "수정"

            viewModel.tvInsuranceImageCheck.value = "업데이트완료"

            binding.tvInsuranceImageCheck.setTextColor(
                ContextCompat.getColor(
                    this@MyCarInformationActivity,
                    R.color.white
                )
            )


        } else {

            viewModel.tvInsuranceEdit.value = "업로드"
            viewModel.tvInsuranceImageCheck.value = "업데이트를 해주세요"
            binding.tvInsuranceImageCheck.setTextColor(
                ContextCompat.getColor(
                    this@MyCarInformationActivity,
                    R.color.Blank
                )
            )
        }
        if (positionData.new_url!!.isNotEmpty()) {
            viewModel.tvCarEdit.value = "수정"
            viewModel.tvCarImageCheck.value = "업데이트완료"


            binding.tvCarImageCheck.setTextColor(
                ContextCompat.getColor(
                    this@MyCarInformationActivity,
                    R.color.white
                )
            )
        } else {

            viewModel.tvCarEdit.value = "업로드"
            viewModel.tvCarImageCheck.value = "번호판이 나온 사진을 업로드 해주세요"
            binding.tvCarImageCheck.setTextColor(
                ContextCompat.getColor(
                    this@MyCarInformationActivity,
                    R.color.Blank
                )
            )
        }
        viewModel.isEditCheck.value = false

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
