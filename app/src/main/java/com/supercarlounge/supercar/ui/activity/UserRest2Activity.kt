package com.supercarlounge.supercar.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.Network.ProfileManagementService
import com.supercarlounge.supercar.adapter.UserRestOptionAdapter
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.data.ResultData
import com.supercarlounge.supercar.data.ResultUserRequestData
import com.supercarlounge.supercar.databinding.ActivityUserRest1Binding
import com.supercarlounge.supercar.databinding.ActivityUserRest2Binding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class UserRest2Activity: BaseActivitiy() {

    private lateinit var binding: ActivityUserRest2Binding
    private var application :MainApplication? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserRest2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)

        clickListeners()
        application = applicationContext as MainApplication

    }

    override fun onResume() {
        super.onResume()
        getUserRequest(application!!.userData?.u_seq.toString())
    }

    private fun clickListeners() {
        binding!!.llLogout.setOnClickListener {
            application!!.savePhoneNumber("")
            application!!.u_phone =""
            val i= Intent(this, LoginActivity::class.java)
            i.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(i)
            this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        binding.llRestore.setOnClickListener {
            Log.d("휴면탈퇴", "휴면해제 클릭")
            getUserRequestRestore(application!!.userData?.u_seq.toString(), "휴면", "해제", "회원이 직접 휴면 해제 진행")
        }
    }

    fun getUserRequestRestore(u_seq: String, type: String, kind: String, msg: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(ProfileManagementService::class.java).userRequestRestore(u_seq, type, kind, msg)
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        setUI2()
                    }
                }
                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Toast.makeText(getApplicationContext(),"요청에 실패하였습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }



    fun getUserRequest(u_seq: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(ProfileManagementService::class.java).userRequestOne(u_seq)
            d!!.enqueue(object : retrofit2.Callback<ResultUserRequestData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultUserRequestData>,
                    response: Response<ResultUserRequestData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        if(data.status == "0"){
                            setUI0()
//                            Toast.makeText(getApplicationContext(),"요청 대기중", Toast.LENGTH_SHORT).show()
                        }else{
                            setUI1()
//                            Toast.makeText(getApplicationContext(),"처리 완료", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onFailure(call: retrofit2.Call<ResultUserRequestData>, t: Throwable) {
                    Toast.makeText(getApplicationContext(),"요청에 실패하였습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun setUI0(){
        binding.llInfo1.visibility = View.VISIBLE;
        binding.llLogout.visibility = View.VISIBLE;
    }
    fun setUI1(){
        binding.llInfo2.visibility = View.VISIBLE;
        binding.llRestore.visibility = View.VISIBLE;
    }
    fun setUI2(){
        binding.llInfo2.visibility = View.GONE;
        binding.llRestore.visibility = View.GONE;
        binding.llInfo3.visibility = View.VISIBLE;
        binding.llLogout.visibility = View.VISIBLE;
    }

    override fun finish() {
        super.finish()
    }
}
