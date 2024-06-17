package com.supercarlounge.supercar.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.Network.UserService
import com.supercarlounge.supercar.data.ResultData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class StartViewModel : ViewModel() {

    var text = MutableLiveData<String>("?")
    var reSend = MutableLiveData<String>("재전송")
    var reSendCheck = MutableLiveData<String>("인증번호를 확인해주세요")
    var reSendTime = MutableLiveData<String>("00:00")

    var u_name =MutableLiveData<String>("")
    var u_gender =MutableLiveData<String>("")
    var u_birthday =MutableLiveData<String>("")
    var u_phone =MutableLiveData<String>("")
    var view =MutableLiveData<String>("퀵유저")
    var QuickInsertApiCall =MutableLiveData<Boolean>(false)
    var BubbleRick =MutableLiveData<Boolean>(false)


    fun setQuickInsert(u_token:String,u_loca :String ,u_mt  :String,u_mkt :String){
        if (BubbleRick.value == false){
            BubbleRick.value = true

            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter!!.create(UserService::class.java).setQuickInsert(u_name.value,u_phone.value!!,u_gender.value,u_birthday.value,u_token,u_loca,u_mt,u_mkt)
                d!!.enqueue(object : retrofit2.Callback<ResultData> {
                    override fun onResponse(
                        call: retrofit2.Call<ResultData>,
                        response: Response<ResultData>
                    ) {
                        var data = response.body()
                        if (data != null) {
                            QuickInsertApiCall.value = true


                        }
                        BubbleRick.value = false
                    }

                    override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                        BubbleRick.value = false
                    }

                })

            }
        }

    }

}
