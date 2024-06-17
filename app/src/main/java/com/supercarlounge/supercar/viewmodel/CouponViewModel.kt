package com.supercarlounge.supercar.viewmodel

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

class CouponViewModel : ViewModel() {
    var my_seq = MutableLiveData<String>("")
    var inputcoupon = MutableLiveData<String>("")
    var isfinish = MutableLiveData<Boolean>(false)
    var toast = MutableLiveData<String>("")
    var bubbleRick = MutableLiveData<Boolean>(false)
    fun SetCoupon(){
        if (       bubbleRick.value == false){
            bubbleRick.value = true

        var u_seq = my_seq.value.toString()
        var inputcoupon = inputcoupon.value!!
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
                            isfinish.value =true
                            toast.value = "쿠폰등록이 성공했습니다."
                        }else if(type.equals("notfail")){
                            toast.value = "이미 사용한 쿠폰입니다. 입력한 코드를 확인해주세요."
                        }
                        else{
                            toast.value = "쿠폰등록이 실패했습니다. 입력한 코드를 확인해주세요."
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
