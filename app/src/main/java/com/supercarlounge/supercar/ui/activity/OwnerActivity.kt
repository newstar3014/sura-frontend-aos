package com.supercarlounge.supercar.ui.activity


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.CarAdapter
import com.supercarlounge.supercar.adapter.CarFilterAdapter
import com.supercarlounge.supercar.adapter.CarSeriesAdapter
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.data.*
import com.supercarlounge.supercar.databinding.*
import com.supercarlounge.supercar.viewmodel.OnwerViewModel


class OwnerActivity : BaseActivitiy() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityOwnerBinding
    private lateinit var viewModel: OnwerViewModel
    private lateinit var carFilterAdapter: CarFilterAdapter
    private lateinit var carAdapter: CarAdapter
    private var application: MainApplication? = null
    var u_name = ""
    var u_gender = ""
    var u_birthday = ""
    var u_phone = ""
    var view = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        with(window) {
//            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
//            // set an slide transition
//            enterTransition = Slide(Gravity.END)
//            exitTransition = Slide(Gravity.START)
//        }
        viewModel = ViewModelProvider(this).get(OnwerViewModel::class.java)
        binding = ActivityOwnerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel

        clickListeners()
        observers()
        application =applicationContext as MainApplication
        view = intent.getStringExtra("view").toString()
        viewModel.nickname.value = intent.getStringExtra("nickname")
        if(intent.getSerializableExtra("CharmList")!= null){
            viewModel.resultState2Array = intent.getSerializableExtra("CharmList") as ArrayList<String>
        }
        if(intent.getSerializableExtra("CharmList")!= null) {
            viewModel.resultState3Array =
                intent.getSerializableExtra("musicTasteList") as ArrayList<String>
        }
        if(intent.getSerializableExtra("height")!= null) {
            viewModel.resultState4Array = intent.getSerializableExtra("height") as ArrayList<String>
        }
            if(intent.getSerializableExtra("job")!= null) {
                viewModel.resultState5Array =
                    intent.getSerializableExtra("job") as ArrayList<String>
            }
        if(intent.getSerializableExtra("regin")!= null) {
            viewModel.resultState6Array = intent.getSerializableExtra("regin") as ArrayList<String>
        }
        if(intent.getSerializableExtra("driveStyle")!= null) {
            viewModel.resultState7Array =
                intent.getSerializableExtra("driveStyle") as ArrayList<String>
        }
        u_name = intent.getStringExtra("u_name").toString()
        u_gender = intent.getStringExtra("u_gender").toString()
        u_birthday = intent.getStringExtra("u_birthday").toString()
        u_phone = intent.getStringExtra("u_phone").toString()









        viewModel.view.value = intent.getStringExtra("view").toString()
        if (viewModel.view.value.equals("재등록")) {
            viewModel.carImage.value = intent.getStringExtra("carImage").toString()
            Log.d("2222", viewModel.carImage.value.toString())
            viewModel.insuranceimage.value = intent.getStringExtra("insuranceimage").toString()
            viewModel.selectCarBrand.value = intent.getStringExtra("CarBrand_seq").toString()
            viewModel.selectCarModel.value = intent.getStringExtra("CarModel_seq").toString()
            viewModel.selectCarSeries.value = intent.getStringExtra("selectCarSeries").toString()
            viewModel.selectCarSeriesName.value = intent.getStringExtra("CarBrand_name").toString()
            viewModel.selectCarBrandImage.value = intent.getStringExtra("CarBrand_Image").toString()
            viewModel.selectCarModelName.value = intent.getStringExtra("CarModel_name").toString()
            viewModel.selectCarSeriesName.value = intent.getStringExtra("CarSeries_name").toString()
           // Log.d("뭐지", viewModel.selectCarSeriesName.value.toString())

        }else{
            if (!application!!.carBrandLog){
                application.let {
                    it?.joinLog(this,"차량브랜드 선택",u_name)
                    it?.carBrandLog = true
                }

            }
        }
        if (intent.getParcelableExtra<UserInformationCarData>("data") != null) {
            viewModel.my_user_car_data.value = intent.getParcelableExtra<UserInformationCarData>("data")

        }

        setAdapter()
        edtListeners()

    }

    private fun edtListeners() {
        binding!!.edtCar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {
                viewModel.selectCarModel.value =   "0"
                viewModel.selectCarModelName.value =  "0"
                viewModel.selectCarSeries.value =  p0.toString()
                viewModel.selectCarSeriesName.value =   p0.toString()

                viewModel.carSeriesCursorPoint.value = -1
                viewModel.carDetailsSeriesCursorPoint.value = -1

                viewModel.stateBottomText.value = p0!!.isNotEmpty()

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    private fun observers() {
        viewModel.carList.observe(this, Observer {
            it?.let {



                carAdapter =
                    CarAdapter(this, viewModel.carList.value!!, viewModel!!) { data, position ->
                        viewModel.selectCarBrand.value = data.carb_seq.toString()
                        viewModel.selectCarBrandName.value = data.carb_name.toString()
                        viewModel.selectCarBrandImage.value = data.carb_image

                            viewModel.getCarModelList(viewModel.selectCarBrand.value!!, "-1")
                            Log.d("getCarModelList", viewModel.selectCarBrand.value!!)
                            viewModel.state.value = 1

                    }
                binding!!.rvOwner.adapter = carAdapter
                if (viewModel.view.value.equals("재등록")){
                    for ((count, i) in it.withIndex()){
                        if (i.carb_seq.toString() == viewModel.selectCarBrand.value){
                            viewModel.carCursorPoint.value = count
                        }
                    }
                }else if (viewModel.view.value.equals("수정")){

                        var count = 0
                        for (i in it){
                            if (      i.carb_name.equals(viewModel.my_user_car_data.value?.carb_name)){
                                viewModel.carCursorPoint.value =count
                                viewModel.selectCarBrand.value = i.carb_seq.toString()
                                viewModel.selectCarBrandName.value = i.carb_name.toString()
                                viewModel.selectCarBrandImage.value = i.carb_image.toString()
                                break
                            }
                            count++
                        }



                    Log.d("하이", viewModel.my_user_car_data.value.toString())
                }
            }

        })

        viewModel.carSeriesList.observe(this, Observer {
            it?.let {

                binding.rvOwner.adapter = CarSeriesAdapter(this, viewModel.carSeriesList.value!!, viewModel.carDetailsSeriesList.value!!, viewModel) { data, position ->
                    Log.d("카리스트", "observers: 2")
                    viewModel.selectCarModel.value = data.carm_seq.toString()
                    viewModel.selectCarModelName.value = data.carm_name
                }
                if (viewModel.view.value.equals("재등록")){
                    for ((count, i) in it.withIndex()){
                        if (i.carm_seq.toString() == viewModel.selectCarModel.value){
                            viewModel.carSeriesCursorPoint.value = count
                        }
                    }
                }else if (viewModel.view.value.equals("수정")){

                        var count = 0
                        for (i in it){
                            if (i.carm_name.equals(viewModel.my_user_car_data.value?.carm_name)){
                                viewModel.carSeriesCursorPoint.value =count
                                viewModel.selectCarModel.value = i.carm_seq.toString()
                                viewModel.selectCarModelName.value = i.carm_name

                                break
                            }
                            count++
                        }



                    Log.d("하이", viewModel.my_user_car_data.value.toString())
                }
                if (viewModel.view.value.equals("재등록")){

                }
            }

        })


        viewModel.state.observe(this, Observer {
            it?.let {
                if (it == 0) {
//                   viewModel.selectCarBrand.value = "-1"
                }
            }

        })
    }

    private fun setAdapter() {

        viewModel.getCarBrandList("-1")

        carAdapter = CarAdapter(this, viewModel.carList.value!!, viewModel!!) { data, position ->
            viewModel.selectCarBrand.value = data.carb_seq.toString()
            viewModel.selectCarBrandName.value = data.carb_name
            viewModel.selectCarBrandImage.value = data.carb_image

            viewModel.state.value = 1
        }




        binding!!.rvOwner.adapter = carAdapter
        binding!!.rvOwner.layoutManager = LinearLayoutManager(this)
        binding!!.rvOwner.setHasFixedSize(true)

        var carfilterList: ArrayList<CarFilterData> = arrayListOf()
        var all = CarFilterData("0", "ALL")
        var a = CarFilterData("1", "A")
        var b = CarFilterData("2", "B")
        var c = CarFilterData("3", "C")
        var d = CarFilterData("4", "D")
        var e = CarFilterData("5", "E")
        var f = CarFilterData("1", "F")
        var g = CarFilterData("2", "G")
        var h = CarFilterData("3", "H")
        var i = CarFilterData("4", "I")
        var j = CarFilterData("5", "J")
        var k = CarFilterData("5", "K")
        var l = CarFilterData("5", "L")
        var m = CarFilterData("5", "M")
        var n = CarFilterData("5", "N")
        var o = CarFilterData("5", "O")
        var p = CarFilterData("5", "P")
        var q = CarFilterData("5", "Q")
        var r = CarFilterData("5", "R")
        var s = CarFilterData("5", "S")
        var t = CarFilterData("5", "T")
        var u = CarFilterData("5", "U")
        var v = CarFilterData("5", "V")
        var w = CarFilterData("5", "W")
        var x = CarFilterData("5", "X")
        var y = CarFilterData("5", "Y")
        var z = CarFilterData("5", "Z")
        carfilterList.add(all)
        carfilterList.add(a)
        carfilterList.add(b)
        carfilterList.add(c)
        carfilterList.add(d)
        carfilterList.add(e)
        carfilterList.add(f)
        carfilterList.add(g)
        carfilterList.add(h)
        carfilterList.add(i)
        carfilterList.add(j)
        carfilterList.add(k)
        carfilterList.add(l)
        carfilterList.add(m)
        carfilterList.add(n)
        carfilterList.add(o)
        carfilterList.add(p)
        carfilterList.add(q)
        carfilterList.add(r)
        carfilterList.add(s)
        carfilterList.add(t)
        carfilterList.add(u)
        carfilterList.add(v)
        carfilterList.add(w)
        carfilterList.add(x)
        carfilterList.add(y)
        carfilterList.add(z)
        carFilterAdapter = CarFilterAdapter(this, carfilterList, viewModel!!) { data, position ->
            if (viewModel.state.value == 0) {
                if (position == 0) {
                    viewModel.getCarBrandList("-1")
                } else {
                    viewModel.getCarBrandList(data.carb_name)
                }
                viewModel.carCursorPoint.value = -1
            } else {

                if (position == 0) {
                    viewModel.getCarModelList(viewModel.selectCarBrand.value!!, "-1")
                } else {
                    viewModel.getCarModelList(viewModel.selectCarBrand.value!!, data.carb_name)
                }
                viewModel.carSeriesCursorPoint.value = -1
            }



        }

        binding!!.rvOwnerFilter.adapter = carFilterAdapter
        binding!!.rvOwnerFilter.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding!!.rvOwnerFilter.setHasFixedSize(true)
    }


    private fun setUrl(url: kotlin.String) {

    }
    @SuppressLint("NotifyDataSetChanged")
    var resultLauncherCarImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {

                // There are no request codes
                val data: Intent? = result.data
                if (data != null) {
                    var i = Intent()
                    viewModel.carImage.value = data.getStringExtra("carImage" )
                    viewModel.insuranceimage.value = data.getStringExtra("insuranceimage" )
                    i.putExtra("insuranceimage", viewModel.insuranceimage.value)
                    i.putExtra("CarBrand_seq",viewModel.selectCarBrand.value)
                    i.putExtra("CarModel_seq",viewModel.selectCarModel.value)
                    i.putExtra("CarSeries_seq",viewModel.selectCarSeries.value)
                    i.putExtra("CarBrand_name",viewModel.selectCarBrandName.value)
                    i.putExtra("CarBrand_Image",viewModel.selectCarBrandImage.value)
                    i.putExtra("CarModel_name",viewModel.selectCarModelName.value)
                    i.putExtra("CarSeries_name",viewModel.selectCarSeriesName.value)
                    i.putExtra("carImage", viewModel.carImage.value)
                    i.putExtra("insuranceimage", viewModel.insuranceimage.value)

                    Log.d("777", viewModel.carImage.value.toString())
                    this.setResult(Activity.RESULT_OK, i)
                    this.finish()
                }
            }


        }

    @SuppressLint("DefaultLocale", "IntentReset")
    private fun clickListeners() {
//        binding.llOwner.setOnClickListener {
//            var i = Intent(this,)
//        }
        binding.llBottomBar.setOnClickListener {

            if (viewModel.state.value!! == 0) {
                if (viewModel.carCursorPoint.value!! >= 0) {
                    viewModel.getCarModelList(viewModel.selectCarBrand.value!!, "-1")
                    Log.d("getCarModelList", viewModel.selectCarBrand.value!!)
                    viewModel.state.value = 1
                    binding.edtCar.setText("")
                }
            }else{
               if (viewModel.carDetailsSeriesCursorPoint.value!! >= 0 ||viewModel.carDetailsSeriesList.value?.size == 0|| binding.edtCar.length() > 0) {
                    if (viewModel.selectCarSeries.value.isNullOrEmpty() == false || binding.edtCar.length() > 0) {
                        if (viewModel.view.value.equals("수정")) {
                            val i = Intent()
                            i.putExtra("CarBrand_seq", viewModel.selectCarBrand.value)
                            i.putExtra("CarModel_seq", viewModel.selectCarModel.value)
                            i.putExtra("CarSeries_seq", viewModel.selectCarSeries.value)
                            i.putExtra("CarBrand_name", viewModel.selectCarBrandName.value)
                            i.putExtra("CarBrand_Image", viewModel.selectCarBrandImage.value)
                            i.putExtra("CarModel_name", viewModel.selectCarModelName.value)
                            i.putExtra("CarSeries_name", viewModel.selectCarSeriesName.value)

                            this.setResult(Activity.RESULT_OK, i)
                            finish()
//                       this.setResult(Activity.RESULT_OK,i)
//                       finish()
                        } else if (viewModel.view.value.equals("재등록")) {
                            val i = Intent(this, CarRegistrationActivity::class.java)
                            i.putExtra("view", "재등록")
                            i.putExtra("carImage", viewModel.carImage.value)
                            i.putExtra("insuranceimage", viewModel.insuranceimage.value)
                            Log.d("3333", viewModel.carImage.value.toString())
                            i.putExtra("CarBrand_Image", viewModel.selectCarBrandImage.value)
                            if (view.equals("퀵유저")) {
                                i.putExtra("view", "퀵유저")
                            }
                            resultLauncherCarImage.launch(i)
//                       i.putExtra("CarBrand_seq",viewModel.selectCarBrand.value)
//                       i.putExtra("CarModel_seq",viewModel.selectCarModel.value)
//                       i.putExtra("CarSeries_seq",viewModel.selectCarSeries.value)
//                       i.putExtra("CarBrand_name",viewModel.selectCarBrandName.value)

//                       i.putExtra("CarModel_name",viewModel.selectCarModelName.value)
//                       i.putExtra("CarSeries_name",viewModel.selectCarSeriesName.value)


//                       i.putExtra("CarBrand_seq",viewModel.selectCarBrand.value)
//                       i.putExtra("CarModel_seq",viewModel.selectCarModel.value)
//                       i.putExtra("CarSeries_seq",viewModel.selectCarSeries.value)
//                       i.putExtra("CarBrand_name",viewModel.selectCarBrandName.value)
//                       i.putExtra("CarBrand_Image",viewModel.selectCarBrandImage.value)
//                       i.putExtra("CarModel_name",viewModel.selectCarModelName.value)
//                       i.putExtra("CarSeries_name",viewModel.selectCarSeriesName.value)
                            overridePendingTransition(R.anim.fadein, R.anim.fadeout)

                        } else {
                            val i = Intent(this, CarRegistrationActivity::class.java)
                            i.putExtra("nickname", viewModel.nickname.value)
                            i.putExtra("CharmList", viewModel.resultState2Array)
                            i.putExtra("musicTasteList", viewModel.resultState3Array)
                            i.putExtra("height", viewModel.resultState4Array)
                            i.putExtra("job", viewModel.resultState5Array)
                            i.putExtra("regin", viewModel.resultState6Array)
                            i.putExtra("driveStyle", viewModel.resultState7Array)
                            i.putExtra("CarBrand_Image", viewModel.selectCarBrandImage.value)
                            i.putExtra("CarBrand", viewModel.selectCarBrand.value)
                            i.putExtra("CarModel", viewModel.selectCarModel.value)
                            i.putExtra("CarSeries", viewModel.selectCarSeries.value)
                            i.putExtra("type", "all")
                            Log.d("확인용1", viewModel.carList.value.toString())
                            Log.d("확인용2", viewModel.carSeriesList.value.toString())
                            Log.d("확인용3", viewModel.carDetailsSeriesList.value.toString())
                            i.putExtra("u_name", u_name)
                            i.putExtra("u_gender", u_gender)
                            i.putExtra("u_birthday", u_birthday)
                            i.putExtra("u_phone", u_phone)
                            if (view.equals("퀵유저")) {
                                i.putExtra("view", "퀵유저")
                            }
                            overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                            startActivity(i)
                        }
                    }

                }
            }


        }
        binding.ivBack.setOnClickListener {
            if (viewModel.state.value!! != 0) {
                    viewModel.carSeriesCursorPoint.value = -1
                carAdapter =
                    CarAdapter(this, viewModel.carList.value!!, viewModel!!) { data, position ->
                        viewModel.selectCarBrand.value = data.carb_seq.toString()
                        viewModel.selectCarBrandName.value = data.carb_name
                        viewModel.selectCarBrandImage.value = data.carb_image
                        viewModel.getCarModelList(viewModel.selectCarBrand.value!!, "-1")
                        viewModel.state.value = 1
                    }
                binding!!.rvOwner.adapter = carAdapter
                viewModel.state.value = 0
            }else{
                viewModel.stateBottomText.value= false
                viewModel.carSeriesCursorPoint.value = -1
                viewModel.carCursorPoint.value = -1
                finish()
            }
        }


    }


    override fun onPause() {
        super.onPause()
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
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
