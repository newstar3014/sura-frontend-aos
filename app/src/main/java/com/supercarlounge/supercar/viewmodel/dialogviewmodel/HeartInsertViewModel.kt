package com.supercarlounge.supercar.viewmodel.dialogviewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.Network.UserService
import com.supercarlounge.supercar.data.ResultData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class HeartInsertViewModel : ViewModel() {


    var titleText = MutableLiveData<String>("SUPERCAR LOUNGE")
    var titleText2 = MutableLiveData<String>("VIP DRIVE")
    var titleText3 = MutableLiveData<String>("DRIVE AWAY")

    var checkSecession = MutableLiveData<Boolean>(false)
    var my_seq = MutableLiveData<String>("")
    var apiCall = MutableLiveData<String>("")
    var bubbleRick = MutableLiveData<Boolean>(false)
    var reason = MutableLiveData<String>("")
    var toast =MutableLiveData<String>("")

    fun setcheckSecession(boolean: Boolean){

        checkSecession.value = !boolean
    }
    fun SetCoupon(){
        if (       bubbleRick.value == false){
            bubbleRick.value = true

            var u_seq = my_seq.value.toString()
            var inputcoupon = reason.value!!
            if(inputcoupon.isNullOrEmpty()){
                bubbleRick.value = false
                return
            }
            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter!!.create(UserService::class.java).CouponSend(u_seq,inputcoupon)
                d!!.enqueue(object : retrofit2.Callback<ResultData> {
                    override fun onResponse(
                        call: retrofit2.Call<ResultData>,
                        response: Response<ResultData>
                    ) {
                        var data = response.body()
                        if (data != null) {
                            var type = data.type
                            if(type.equals("success")) {
                                apiCall.value = "등록완료"
                            }else if(type.equals("notinvalid")){
                                apiCall.value = "불일치"
                            } else{
                                apiCall.value = "쿠폰등록실패"
                            }
                        }
                        bubbleRick.value = false

                    }

                    override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                        Log.d("failer", t.message!!)
                        bubbleRick.value = false
                    }

                })

            }
        }
    }
}