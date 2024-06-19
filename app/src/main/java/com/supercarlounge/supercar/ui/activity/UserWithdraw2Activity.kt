package com.supercarlounge.supercar.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.Network.ProfileManagementService
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.data.ResultData
import com.supercarlounge.supercar.databinding.ActivityUserWithdraw1Binding
import com.supercarlounge.supercar.databinding.ActivityUserWithdraw2Binding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class UserWithdraw2Activity: BaseActivitiy() {

    private lateinit var binding: ActivityUserWithdraw2Binding
    private var isAgree = false

    private var application :MainApplication? =null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserWithdraw2Binding.inflate(layoutInflater)
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

        binding.llAgree.setOnClickListener {
            if(isAgree){
                isAgree = false
                binding.ivAgreeIcon.setImageResource(R.drawable.ic_frame_2408)
                binding.tvAgreeText.setTextColor(ContextCompat.getColor(this, R.color.white_70))
                binding.tvAgreeText.setTypeface(null, Typeface.NORMAL)
            }else{
                isAgree = true
                binding.ivAgreeIcon.setImageResource(R.drawable.ic_frame_2409)
                binding.tvAgreeText.setTextColor(Color.WHITE)  // 원하는 색상으로 변경
                binding.tvAgreeText.setTypeface(null, Typeface.BOLD)
            }
        }

        binding.llWithdraw.setOnClickListener {
            Log.d("휴면탈퇴", "최종 탈퇴버튼 클릭")
            var msg = binding.edtMsg.getText().toString()
            Log.d("휴면탈퇴", msg)
            if(msg.length < 10){
                Toast.makeText(this,"탈퇴사유를 최소 10자 이상 입력해주세요.", Toast.LENGTH_SHORT).show()
            }else if(!isAgree){
                Toast.makeText(this,"내용확인 동의사항에 체크해주세요.", Toast.LENGTH_SHORT).show()
            }else{
                profileManagementRequest(application!!.userData?.u_seq.toString(), "탈퇴", "신청", msg)
            }
        }
    }

    fun profileManagementRequest(u_seq: String, type: String, kind: String, msg: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(ProfileManagementService::class.java).sendProfileManagement(u_seq, type, kind, msg)
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        // 탈퇴완료화면으로 이동
                        goWithrawDone()
                    }
                }
                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Toast.makeText(getApplicationContext(),"요청에 실패하였습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun goWithrawDone(){
        val i = Intent(this, UserWithdraw3Activity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    override fun finish() {
        super.finish()
    }

}
