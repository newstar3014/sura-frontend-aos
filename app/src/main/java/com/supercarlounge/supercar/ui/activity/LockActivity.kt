package com.supercarlounge.supercar.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.data.VipDriveRankData
import com.supercarlounge.supercar.databinding.*
import com.supercarlounge.supercar.viewmodel.*

class LockActivity: BaseActivitiy() {


    private lateinit var binding: ActivityLockBinding
    private lateinit var viewModel: LockViewModel
    private  var application :MainApplication?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        application=applicationContext as MainApplication

//        with(window) {
//            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
//            // set an slide transition
//            enterTransition = Slide(Gravity.END)
//            exitTransition = Slide(Gravity.START)
//        }
        viewModel = ViewModelProvider(this).get(LockViewModel::class.java)
        binding = ActivityLockBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel


        clickListeners()
        observes()
        setAdapters()

        viewModel.u_seq.value = application?.userData?.u_seq.toString()

        var pass =intent.getStringExtra("setpass",)
        if (pass.equals("setpass")){
                viewModel.passwordTrueFalse.value = false
                viewModel.textInit(viewModel.passwordTrueFalse.value!!)
                viewModel.passwordList.clear()
        }else{
            viewModel.passwordSettingTrueFalse(viewModel.u_seq.value!!)
        }

        var s = "https://interactive-examples.mdn.mozilla.net/media/cc0-images/grapefruit-slice-332-332.jpg"


    }


    private fun setAdapters() {

    }
    private fun observes() {
        viewModel.passwordListSize.observe(this, Observer {
            Log.d("observes", viewModel.passwordListSize.value!!.toString())
        })

        viewModel.passwordReset.observe(this, Observer {
            if (it ==true){
                viewModel.passwordTrueFalse.value = true
                viewModel.allDelete()

                viewModel.textInit(viewModel.passwordTrueFalse.value!!)

            }
        })
        viewModel.state.observe(this, Observer {
          if (it ==1){
              val i= Intent(this, MainActivity::class.java)

              startActivity(i)
             // overridePendingTransition(R.anim. fadein, R.anim.fadeout)
              this.finish()
          }
        })
    }

    private fun clickListeners() {
        binding!!.ivIntro.setOnClickListener {
            finish()
        }
        binding!!.onclickPass.setOnClickListener{
            val i= Intent(this, PaswordSearchActivity::class.java)

            startActivity(i)
           // overridePendingTransition(R.anim. fadein, R.anim.fadeout)
        }
        binding!!.tvAllDelete.setOnClickListener{
          finish()
        }
    }

    override fun finish() {
        super.finish()
       // overridePendingTransition(com.supercarlounge.supercar.R.anim. fadein, com.supercarlounge.supercar.R.anim.fadeout)
    }

    private fun testFilterData():ArrayList<VipDriveRankData>{
        var list : ArrayList<VipDriveRankData> = arrayListOf()
        var x = 1

        var data : VipDriveRankData

        for(i: Int in 0 .. 10 ){
            var xs = x.toString().length
            if(xs == 1){
                data = VipDriveRankData(" $x ","BENTREY","PASSENGER"  ,"asdasdasd")
            }else{
                data = VipDriveRankData("$x","BENTREY","PASSENGER"  ,"asdasdasd")
            }
            Log.d("zzz", "$x:$xs")

            x = x.plus(1)
            list.add(data)
        }


        return  list
    }
}
