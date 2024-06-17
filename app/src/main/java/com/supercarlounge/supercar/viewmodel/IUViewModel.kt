package com.supercarlounge.supercar.viewmodel

import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.ConciergeService
import com.supercarlounge.supercar.Network.HeartService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.Network.UserService
import com.supercarlounge.supercar.data.*
import com.supercarlounge.supercar.dialog.DrivePassPayDialog
import com.supercarlounge.supercar.enumset.DrivePassDialogStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class IUViewModel : ViewModel() {
    var type = MutableLiveData<String>("0")
    var size = MutableLiveData<String>("0")
    var bannerPosition= MutableLiveData<Int>(0)
    var my_seq = MutableLiveData<String>("0")
    var invite_code = MutableLiveData<String>("")
    var inviteset =MutableLiveData<String>("")
    var pamentstart =MutableLiveData<Boolean>(false)
    fun GetInviteCode(type:String) {
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
                                    inviteset.value = type
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
            inviteset.value = type
        }
    }

    fun DrivePassPayment1(v : DrivePassDialogStatus, fragmentManager: FragmentManager, con: Context) {
        if(pamentstart.value==false) {
            pamentstart.value = true
            var datestring = ""
            var payheart= ""
            var vtype = v
            var  date_string =""
            var dayType= 0
            if(vtype == DrivePassDialogStatus.DAY1){
                datestring = "1"
                payheart ="100"
            }else{
                datestring = "30"
                payheart ="180"
            }
            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter.create(HeartService::class.java).DrivePassPayment(
                    my_seq.value!!, "BUYPASS", datestring, payheart
                )
                d!!.enqueue(object : retrofit2.Callback<ResultDrivePassData> {
                    override fun onResponse(
                        call: retrofit2.Call<ResultDrivePassData>,
                        response: Response<ResultDrivePassData>
                    ) {
                        var data = response.body()
                        if (data != null) {
                            var type = data.type
                            if (type.equals("success")) {
                                var message = data.message
                                if (message.contains("패스권이 있습니다")) {
                                    if (v == DrivePassDialogStatus.DAY1) {
                                        dayType = 1
                                    } else {
                                        dayType = 30
                                    }
                                    vtype = DrivePassDialogStatus.EXISTPASS1
                                    date_string = data.getdate()

                                } else {
                                    if (vtype == DrivePassDialogStatus.DAY1) {
                                        vtype = DrivePassDialogStatus.COMPLETE1
                                        date_string = data.getdate()
                                    } else if (vtype == DrivePassDialogStatus.DAY30) {
                                        vtype = DrivePassDialogStatus.COMPLETE30
                                        date_string = data.getdate()
                                    }
                                }

                            } else {
                                vtype = DrivePassDialogStatus.NOTHEART

                            }

                            var dialog =
                                DrivePassPayDialog(con, vtype, date_string, dayType) { type, event ->
                                    Log.d("테스트", "onResponse: ㅎㅇ")


                                }
                            dialog.show(fragmentManager, "")
                        }
                        pamentstart.value = false
                    }

                    override fun onFailure(
                        call: retrofit2.Call<ResultDrivePassData>,
                        t: Throwable
                    ) {
                        Log.d("check", t.toString())
                        pamentstart.value = false
                    }

                })
            }
        }
    }

}