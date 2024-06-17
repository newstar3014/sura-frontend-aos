package com.supercarlounge.supercar.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.Network.*
import com.supercarlounge.supercar.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel : ViewModel() {
    var my_seq = MutableLiveData<String>("")
    var title = MutableLiveData<String>("SURA")
    var pagerposition = MutableLiveData<Int>(2)
    var isalim = MutableLiveData<Boolean>(false)
    var iscompany = MutableLiveData<Boolean>(false)
    var toast = MutableLiveData<String>("")

    fun CheckAlim(u_email:String) {

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(HomerService::class.java).AlimReadCheck(
                my_seq.value!!
            )
            d!!.enqueue(object : retrofit2.Callback<ResultAlimCheckData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultAlimCheckData>,
                    response: Response<ResultAlimCheckData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if (type.equals("success")) {
                            var active = data.active
                            if (u_email.isNullOrEmpty()){
                                isalim.value = true
                            }else{
                                isalim.value = active

                            }

                        }
                    }

                }

                override fun onFailure(
                    call: retrofit2.Call<ResultAlimCheckData>,
                    t: Throwable
                ) {
                    Log.d("check", t.toString())
                }

            })

        }
    }

    fun checkLogin(u_phone: String, u_token: String ,application: MainApplication) {
        Log.d("checkLogin", "u_phone =$u_phone , u_token =$u_token ")
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(LoginService::class.java)
                .checkLogin(u_phone, u_token, true)
            d!!.enqueue(object : retrofit2.Callback<ResultLoginData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultLoginData>,
                    response: Response<ResultLoginData>
                ) {

                    var data = response.body()
                    Log.d("데이터", data.toString())
                    var u_seq = data?.u_seq
                    if (data != null) {

                        if (data.message.equals("퀵 회원입니다")) {
                            getQuickUserInformation(application,u_seq.toString())

                        } else {

                        }
//                        Log.d("setPassword", data.message)
                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultLoginData>, t: Throwable) {
                    Log.d("check", t.toString())
                }

            })

        }
    }
    fun CheckLogInsert() {
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(LoginService::class.java)
                .checkloginsert("ANDROID",
                        my_seq.value!!
                )
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    Log.d("태그1", data.toString())
                    if (data != null) {
                        var type =data.type
                        if (type.equals("success")){
                            Log.d("SUCESS","sucess")
                        }else{

                        }

                    }

                }

                override fun onFailure(
                    call: retrofit2.Call<ResultData>,
                    t: Throwable
                ) {
                    Log.d("ERROR","logerror")
                }

            })

        }

    }

    fun getQuickUserInformation(application: MainApplication, u_seq: String) {
        Log.d("태그1", u_seq)
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(UserService::class.java)
                .getQuickUserInformation(
                    u_seq,
                )
            d!!.enqueue(object : retrofit2.Callback<ResultUserInformationData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultUserInformationData>,
                    response: Response<ResultUserInformationData>
                ) {
                    var data = response.body()
                    Log.d("태그1", data.toString())
                    if (data != null) {
                        application.userData = data.rows[0]
                        Log.d("가져온 유저 데이터", application.userData.toString())

                    }

                }

                override fun onFailure(
                    call: retrofit2.Call<ResultUserInformationData>,
                    t: Throwable
                ) {
                    Log.d("check", t.toString())
                    Log.d("태그1", "333")
                }

            })

        }

    }


}
