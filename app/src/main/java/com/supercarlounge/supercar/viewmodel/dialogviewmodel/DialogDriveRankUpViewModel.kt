package com.supercarlounge.supercar.viewmodel.dialogviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.data.VipDriveAuctionData

class DialogDriveRankUpViewModel : ViewModel() {

    var tv_ok = MutableLiveData<String>("")
    var tv_cancel = MutableLiveData<String>("")


    var tv_sub = MutableLiveData<String>("")
    var viewtype = MutableLiveData<Int>(0)
    var okText = MutableLiveData<String>("재등록")
    var minheart = MutableLiveData<String>("최소 필요 하트 196")
    var tv_time = MutableLiveData<String>("00:00:00")



    var heartViewChecked = MutableLiveData<Boolean>(false)

    var heartChecked1 = MutableLiveData<Boolean>(false)
    var heartChecked2 = MutableLiveData<Boolean>(false)
    var driveactiondata = MutableLiveData<VipDriveAuctionData>()

    fun onHeartViewChecked(boolean: Boolean){
        heartViewChecked.value = boolean
    }

    fun onHeartChecked1(boolean: Boolean){
        if (heartChecked2.value == true){
            heartChecked1.value = boolean
        }else{
            heartChecked1.value = false
        }

    }

    fun onHeartChecked2(boolean: Boolean){
        heartChecked2.value = boolean
    }

//    fun RankUpVipDrive(da_seq:String){
//        CoroutineScope(Dispatchers.IO).launch {
//            val d = NetworkManager.serveradapter!!.create(DriveService::class.java).RankUpVipDrive(my_seq.value!!,da_seq,heart.value!!)
//            d!!.enqueue(object : retrofit2.Callback<ResultData> {
//                override fun onResponse(
//                    call: retrofit2.Call<ResultData>,
//                    response: Response<ResultData>
//                ) {
//                    var data = response.body()
//                    if (data != null) {
//                        var type = data.type
//                        var message = data.message
//                        if(type.equals("success")){
//
//                        }else{
//
//                        }
//
//
//                    }
//
//                }
//
//                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
//                    Log.d("failer", t.message!!)
//                }
//
//            })
//
//        }
//    }
}