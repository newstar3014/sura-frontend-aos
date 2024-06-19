package com.supercarlounge.supercar.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.Network.ProfileManagementService
import com.supercarlounge.supercar.adapter.UserRestOptionAdapter
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.data.ResultData
import com.supercarlounge.supercar.data.ResultUserRequestData
import com.supercarlounge.supercar.databinding.ActivityUserRest1Binding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class UserRest1Activity: BaseActivitiy() {

    private lateinit var binding: ActivityUserRest1Binding
    private lateinit var adapter: UserRestOptionAdapter
    private var application :MainApplication? =null
    private var isRestAvailable = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserRest1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)

        clickListeners()
        application = applicationContext as MainApplication

        setupRecyclerView()
        getUserRequest(application!!.userData?.u_seq.toString())
    }

    private fun setupRecyclerView() {
        val options = listOf(
            "잠시 쉬고 싶어요.",
            "매칭되어 만나는 사람이 생겼어요",
            "내가 생각한 어플이 아님",
            "개인적인 사정",
            "같이 드라이브할 사람을 찾을 수 없음"
        )

        adapter = UserRestOptionAdapter(options, this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
    }

    fun getUserRequest(u_seq: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(ProfileManagementService::class.java).userIsRestCan(u_seq)
            d!!.enqueue(object : retrofit2.Callback<ResultUserRequestData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultUserRequestData>,
                    response: Response<ResultUserRequestData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        isRestAvailable = data.status
                        if(isRestAvailable == "0"){
                            Toast.makeText(getApplicationContext(),"최근 30일 내 휴면해제 이력이 있어 휴면신청이 불가합니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onFailure(call: retrofit2.Call<ResultUserRequestData>, t: Throwable) {
                    Toast.makeText(getApplicationContext(),"요청에 실패하였습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun clickListeners() {
        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.llRest2.setOnClickListener {
            if(isRestAvailable == "0"){
                Toast.makeText(getApplicationContext(),"최근 30일 내 휴면해제 이력이 있어 휴면신청이 불가합니다.", Toast.LENGTH_SHORT).show()
            }else{
                val selectedOption = adapter.getSelectedOption()
                if (selectedOption != null) {
                    profileManagementRequest(application!!.userData?.u_seq.toString(), "휴면", "신청", selectedOption)
                } else {
                    Toast.makeText(this, "휴면 사유를 선택해주세요.", Toast.LENGTH_SHORT).show()
                }
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
                        goUserRest2()
                    }
                }
                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Toast.makeText(getApplicationContext(),"요청에 실패하였습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun goUserRest2(){
        val i = Intent(this, UserRest2Activity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    override fun finish() {
        super.finish()
    }
}
