package com.supercarlounge.supercar.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.databinding.ActivityUserWithdraw1Binding

class UserWithdraw1Activity: BaseActivitiy() {

    private lateinit var binding: ActivityUserWithdraw1Binding

    private var application :MainApplication? =null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserWithdraw1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.setLifecycleOwner(this)

        clickListeners()
        application = applicationContext as MainApplication

    }

    override fun onResume() {
        super.onResume()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun clickListeners() {
        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.llRest.setOnClickListener {
            // 휴면 신청으로 이동
            goRest()
        }
        binding.llRest2.setOnClickListener {
            // 휴면 신청으로 이동
            goRest()
        }

        binding.tvWithdraw.setOnClickListener {
            // 탈퇴 신청진행 위한 패스인증 진행
            val i = Intent(this, LoginPassActivity::class.java)
            resultLauncherPass.launch(i)
        }
    }

    fun goRest(){
        val i = Intent(this, UserRest1Activity::class.java)
        startActivity(i)
    }

    override fun finish() {
        super.finish()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    var resultLauncherPass =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                if (data != null) {
                    if(application!!.u_phone == data.getStringExtra("u_phone").toString()){
                        // 인증 성공
                        val i = Intent(this, UserWithdraw2Activity::class.java)
                        startActivity(i)
                    }else{
                        // 인증 실패
                        Toast.makeText(getApplicationContext(),"인증하신 휴대폰번호가 등록된 번호와 다르기때문에 진행하실 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
}
