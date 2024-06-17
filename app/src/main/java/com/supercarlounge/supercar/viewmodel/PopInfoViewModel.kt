package com.supercarlounge.supercar.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.BannerService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.Network.UserService
import com.supercarlounge.supercar.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class PopInfoViewModel : ViewModel() {
    var my_seq= MutableLiveData<String>("")
    var popUpinfoData= MutableLiveData<ArrayList<PopUpinfoData>>(arrayListOf())
    var  popUpinfoData_size= MutableLiveData<Int>(0)
    var position= MutableLiveData<Int>(1)
    var select_bannernoti_data= MutableLiveData<NotiData>()
    var invite_code = MutableLiveData<String>("")
    var inviteset =MutableLiveData<Boolean>(false)
    fun getOneNoti(n_seq:String){


        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(BannerService::class.java).getOneNoti(n_seq
            )
            d!!.enqueue(object : retrofit2.Callback<ResultOneNotiData> {
                override fun onResponse(
                    call: Call<ResultOneNotiData>,
                    response: Response<ResultOneNotiData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var data = data.rows
                        select_bannernoti_data.value = data

                    }

                }

                override fun onFailure(call: Call<ResultOneNotiData>, t: Throwable) {
                    Log.d("check", "failer")
                }

            })

        }
    }
    fun viewLogBanner(bc_seq:Int,cl_type:String) {
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(BannerService::class.java)
                .viewLogBanner(bc_seq,my_seq.value!!,cl_type)
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        var message = data.message
                        if (type.equals("success")) {

                        } else {

                        }


                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                }

            })

        }

    }
    fun GetInviteCode() {
        if(invite_code.value.isNullOrEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter.create(UserService::class.java).InviteCodeGet(
                    my_seq.value,
                )
                d!!.enqueue(object : retrofit2.Callback<ResultInvateCodeData> {
                    override fun onResponse(
                        call: retrofit2.Call<ResultInvateCodeData>,
                        response: Response<ResultInvateCodeData>
                    ) {
                        var data = response.body()
                        if (data != null) {
                            var rows = data.rows
                            if (rows != null) {
                                if (rows.size != 0) {
                                    var codeset = rows.get(0).my_code
                                    invite_code.value = codeset
                                    inviteset.value = true
                                }
                            }
                        }

                    }

                    override fun onFailure(
                        call: retrofit2.Call<ResultInvateCodeData>,
                        t: Throwable
                    ) {
                        Log.d("check", t.toString())
                    }

                })

            }
        }else{
            inviteset.value = true
        }
    }
}
