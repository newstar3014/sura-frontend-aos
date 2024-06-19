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
import com.supercarlounge.supercar.databinding.ActivityUserWithdraw3Binding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class UserWithdraw3Activity: BaseActivitiy() {

    private lateinit var binding: ActivityUserWithdraw3Binding
    private var application :MainApplication? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserWithdraw3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)

        clickListeners()
        application = applicationContext as MainApplication

    }

    override fun onResume() {
        super.onResume()
        Log.d("휴면탈퇴", application!!.userData?.u_seq.toString())

        getUserRequest(application!!.userData?.u_seq.toString())
    }

    private fun clickListeners() {

        binding.llRestore.setOnClickListener {
            Log.d("휴면탈퇴", "탈퇴취소 클릭")
            getUserRequestRestore(application!!.userData?.u_seq.toString(), "탈퇴", "취소", "회원이 직접 탈퇴 취소 진행")
        }

        binding.tvWithdraw.setOnClickListener {
            Log.d("휴면탈퇴", "최종탈퇴 클릭")
            userDelete(application!!.userData?.u_seq.toString())
        }

        binding.llLogout.setOnClickListener {
            logout()
        }
    }

    fun logout(){
        application!!.savePhoneNumber("")
        application!!.u_phone =""
        val i= Intent(this, LoginActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(i)
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
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
                        }else if(data.status == "1"){
                            setUI1()
//                            Toast.makeText(getApplicationContext(),"처리 완료", Toast.LENGTH_SHORT).show()
                        }else if(data.status == "2"){
                            setUI2()
//                            Toast.makeText(getApplicationContext(),"취소 완료", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onFailure(call: retrofit2.Call<ResultUserRequestData>, t: Throwable) {
                    Toast.makeText(getApplicationContext(),"요청에 실패하였습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun userDelete(u_seq: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(ProfileManagementService::class.java).userDelete(u_seq)
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        Toast.makeText(getApplicationContext(),"탈퇴 처리가 완료되었습니다. 언제든 다시 찾아주시기 바랍니다.", Toast.LENGTH_SHORT).show()
                        logout()
                    }
                }
                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Toast.makeText(getApplicationContext(),"요청에 실패하였습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun setUI0(){
        binding.llInfo1.visibility = View.VISIBLE;
    }
    fun setUI1(){
        binding.llInfo2.visibility = View.VISIBLE;
        binding.tvWithdraw.visibility = View.VISIBLE;
    }
    fun setUI2(){
        binding.llInfo1.visibility = View.GONE;
        binding.llInfo2.visibility = View.GONE;
        binding.tvWithdraw.visibility = View.GONE;
        binding.llRestore.visibility = View.GONE;
        binding.llInfo3.visibility = View.VISIBLE;
        binding.llLogout.visibility = View.VISIBLE;
    }

    override fun finish() {
        super.finish()
    }
}
