package com.supercarlounge.supercar.ui.activity


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import android.view.WindowManager
import androidx.navigation.ui.AppBarConfiguration
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.databinding.ActivityCarCheckBinding
import com.supercarlounge.supercar.viewmodel.CarChecktViewModel


class CarCheckActivity : BaseActivitiy() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityCarCheckBinding
    private lateinit var viewModel: CarChecktViewModel
    private var application:MainApplication? = null
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
        viewModel = ViewModelProvider(this).get(CarChecktViewModel::class.java)
        binding = ActivityCarCheckBinding.inflate(layoutInflater)
        setContentView(binding.root)
        application =applicationContext as MainApplication

        viewModel.nickname.value = intent.getStringExtra("nickname")
        if (intent.getSerializableExtra("CharmList")!= null){
            viewModel.resultState2Array = intent.getSerializableExtra("CharmList") as ArrayList<String>
        }

        if (intent.getSerializableExtra("musicTasteList")!= null){
            viewModel.resultState3Array = intent.getSerializableExtra("musicTasteList") as ArrayList<String>
        }
        if (intent.getSerializableExtra("height")!= null){
            viewModel.resultState4Array = intent.getSerializableExtra("height") as ArrayList<String>

        }
        if (intent.getSerializableExtra("job")!= null){
            viewModel.resultState5Array = intent.getSerializableExtra("job") as ArrayList<String>
        }
        if (intent.getSerializableExtra("regin")!= null){
            viewModel.resultState6Array = intent.getSerializableExtra("regin") as ArrayList<String>
        }

        if (intent.getSerializableExtra("driveStyle")!= null){
            viewModel.resultState7Array = intent.getSerializableExtra("driveStyle") as ArrayList<String>
        }




        u_name = intent.getStringExtra("u_name").toString()
        u_gender = intent.getStringExtra("u_gender").toString()
        u_birthday = intent.getStringExtra("u_birthday").toString()
        u_phone = intent.getStringExtra("u_phone").toString()
        view = intent.getStringExtra("view").toString()
        Log.d("아이템4", u_gender.toString())
        binding!!.viewModel = viewModel

        if (!application!!.carLog){
            application.let {
                it?.joinLog(this,"차량소유 선택",u_name)
                it?.carLog = true
            }

        }
        binding!!.setLifecycleOwner(this)
        viewModel.sexType.value = "a"
        clickListeners()

    }

    private fun setUrl(url :kotlin.String){

    }

    @SuppressLint("DefaultLocale", "IntentReset")
    private fun clickListeners(){
        binding.llOwner.setOnClickListener {
            var i = Intent(this,OwnerActivity::class.java)
            i.putExtra("nickname",viewModel.nickname.value)
            i.putExtra("CharmList",viewModel.resultState2Array)
            i.putExtra("musicTasteList",viewModel.resultState3Array)
            i.putExtra("height",viewModel.resultState4Array)
            i.putExtra("job",viewModel.resultState5Array)
            i.putExtra("regin",viewModel.resultState6Array)
            i.putExtra("driveStyle",viewModel.resultState7Array)
            i.putExtra("u_name", u_name)
            i.putExtra("u_gender", u_gender)
            i.putExtra("u_birthday", u_birthday)
            i.putExtra("u_phone", u_phone)
            i.putExtra("type", "all")
            if (view.equals("퀵유저")) {
                i.putExtra("view", "퀵유저")
            }
            startActivity(i)
           // overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        binding.llPassenger.setOnClickListener {


            if (u_gender.equals("MALE")){
                var i = Intent(this,IncomeInformationActivity::class.java)
                i.putExtra("nickname",viewModel.nickname.value)
                i.putExtra("CharmList",viewModel.resultState2Array)
                i.putExtra("musicTasteList",viewModel.resultState3Array)
                i.putExtra("height",viewModel.resultState4Array)
                i.putExtra("job",viewModel.resultState5Array)
                i.putExtra("regin",viewModel.resultState6Array)
                i.putExtra("driveStyle",viewModel.resultState7Array)
                i.putExtra("u_name", u_name)
                i.putExtra("u_gender", u_gender)
                i.putExtra("u_birthday", u_birthday)
                i.putExtra("u_phone", u_phone)
                i.putExtra("type", "all")
                if (view.equals("퀵유저")) {
                    i.putExtra("view", "퀵유저")
                }
                startActivity(i)
            }else{
                var i = Intent(this,MyJoinInfomationActivity::class.java)
                i.putExtra("nickname",viewModel.nickname.value)
                i.putExtra("CharmList",viewModel.resultState2Array)
                i.putExtra("musicTasteList",viewModel.resultState3Array)
                i.putExtra("height",viewModel.resultState4Array)
                i.putExtra("job",viewModel.resultState5Array)
                i.putExtra("regin",viewModel.resultState6Array)
                i.putExtra("driveStyle",viewModel.resultState7Array)
                i.putExtra("u_name", u_name)
                i.putExtra("u_gender", u_gender)
                i.putExtra("u_birthday", u_birthday)
                i.putExtra("u_phone", u_phone)
                i.putExtra("type","PASSENGERWOMAN")
                if (view.equals("퀵유저")) {
                    i.putExtra("view", "퀵유저")
                }
                startActivity(i)
            }


           // overridePendingTransition(com.supercarlounge.supercar.R.anim. fadein, com.supercarlounge.supercar.R.anim.fadeout)
        }
        binding.ivBack.setOnClickListener {
            finish()
        }

    }



    override fun onPause() {
        super.onPause()
       // overridePendingTransition(com.supercarlounge.supercar.R.anim. fadein, com.supercarlounge.supercar.R.anim.fadeout)
    }
    override fun finish() {
        super.finish()
       // overridePendingTransition(com.supercarlounge.supercar.R.anim. fadein, com.supercarlounge.supercar.R.anim.fadeout)
    }
}
