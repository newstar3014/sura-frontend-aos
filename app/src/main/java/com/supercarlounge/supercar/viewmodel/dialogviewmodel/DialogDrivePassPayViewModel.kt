package com.supercarlounge.supercar.viewmodel.dialogviewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.HeartService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.data.ResultDrivePassData
import com.supercarlounge.supercar.enumset.DrivePassDialogStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class DialogDrivePassPayViewModel : ViewModel() {

    var tv_ok = MutableLiveData<String>("구매하기")
    var my_seq = MutableLiveData<String>("")
    var tv_cancel = MutableLiveData<String>("취소")
    var tv_main = MutableLiveData<String>("")
    var tv_sub = MutableLiveData<String>("")
    var date_string = MutableLiveData<String>("22-04-20")
    var vtype = MutableLiveData<DrivePassDialogStatus>(DrivePassDialogStatus.DAY1)
    var toast = MutableLiveData<String>("")
    fun DrivePassPayment() {
        var datestring = ""
        var payheart= ""
        if(vtype.value == DrivePassDialogStatus.DAY1){
            datestring = "1"
             payheart ="100"
        }else{
            datestring = "30"
             payheart ="180"
        }
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(HeartService::class.java).DrivePassPayment(
                my_seq.value!!,"BUYPASS",datestring,payheart
            )
            d!!.enqueue(object : retrofit2.Callback<ResultDrivePassData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultDrivePassData>,
                    response: Response<ResultDrivePassData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if(type.equals("success")) {
                            var message = data.message
                            if(message.contains("패스권이 있습니다")){
                                vtype.value = DrivePassDialogStatus.EXISTPASS1
                                tv_main.value = message
                                tv_sub.value = "서비스 종료일"
                                date_string.value = data.getdate()
                            }else {
                                if (vtype.value == DrivePassDialogStatus.DAY1) {
                                    vtype.value = DrivePassDialogStatus.COMPLETE1
                                    date_string.value = data.getdate()
                                } else if (vtype.value == DrivePassDialogStatus.DAY30) {
                                    vtype.value = DrivePassDialogStatus.COMPLETE30
                                    date_string.value = data.getdate()
                                }
                            }
                        }else{
                            vtype.value = DrivePassDialogStatus.NOTHEART
                        }
                    }

                }

                override fun onFailure(
                    call: retrofit2.Call<ResultDrivePassData>,
                    t: Throwable
                ) {
                    Log.d("check", t.toString())
                }

            })

        }
    }
}