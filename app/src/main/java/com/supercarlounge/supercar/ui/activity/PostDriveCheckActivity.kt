package com.supercarlounge.supercar.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.view.WindowManager
import androidx.navigation.ui.AppBarConfiguration
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.databinding.ActivityPostDriveCheckBinding
import com.supercarlounge.supercar.viewmodel.*

class PostDriveCheckActivity: BaseActivitiy() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityPostDriveCheckBinding
    private lateinit var viewModel: PostDriveCheckViewModel
    private lateinit var application: MainApplication
    var type = 0
    var heart = 0
    var viewType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(PostDriveCheckViewModel::class.java)
        binding = ActivityPostDriveCheckBinding.inflate(layoutInflater)
        application = applicationContext as MainApplication
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel

        type = intent.getIntExtra("viptype",0)
        heart = intent.getIntExtra("vipheart",0)
        viewType = intent.getStringExtra("viewtype").toString()

        if (viewType.equals("익명게시판")){
            viewModel.titleText.value = "익명게시판 등록 시 유의사항"
            viewModel.mainText.value = "익명게시판 등록 시 유의사항"
        }
        clickListeners()
        observes()

    }

    private fun observes() {
        viewModel.istoast.observe(this,Observer{
            if(viewModel.isstart.value!!){
                Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
            }else{
                viewModel.isstart.value = true
            }
        })
        viewModel.isnext.observe(this,Observer{
            if (viewType.equals("익명게시판")){
                if(viewModel.checked1.value!!){
                    if(viewModel.checked2.value!!){

                    }
                    application.SaveAnonymousDate()
                    finish()
//                    var i = Intent(this,PostWritingActivity::class.java)
//                    i.putExtra("viewtype",viewType)
//                    startActivity(i)
//                    finish()

                }
            }else{
                if(viewModel.checked1.value!!){
                    if(viewModel.checked2.value!!){

                    }
                    application.SaveDriveDate()
                    var i = Intent(this,NewDriveWritingActivity::class.java)
                    i.putExtra("viptype",type)
                    i.putExtra("isagree",true)
                    i.putExtra("vipheart",heart)
                    startActivity(i)
                    finish()
                }
            }

        })
    }

    private fun clickListeners() {
        binding!!.ivBack.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
    }

    override fun onBackPressed() {
        setResult(RESULT_OK)
        super.onBackPressed()
    }
    override fun finish() {
        super.finish()
      //  overridePendingTransition(com.supercarlounge.supercar.R.anim. fadein, com.supercarlounge.supercar.R.anim.fadeout)
    }
}
