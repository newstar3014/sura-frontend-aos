package com.supercarlounge.supercar.viewmodel

import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.HeartService
import com.supercarlounge.supercar.Network.HomerService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.Network.UserService
import com.supercarlounge.supercar.data.*
import com.supercarlounge.supercar.dialog.DrivePassPayDialog
import com.supercarlounge.supercar.enumset.DrivePassDialogStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class HeartsShopViewModel : ViewModel() {

    var titleText = MutableLiveData<String>("?")
    var my_seq = MutableLiveData<String>("")
    var subTitleText = MutableLiveData<String>("")
    var termsText = MutableLiveData<String>("")
    var viewType = MutableLiveData<Int>(0)
    var heartloglist = MutableLiveData<ArrayList<HeartLogData>>(arrayListOf())
    var toast = MutableLiveData<String>("")
    var tv_heartset = MutableLiveData<String>("0")
    var tv_heart1 = MutableLiveData<String>("143")
    var tv_heart_pay1 = MutableLiveData<String>("33,000")
    var tv_heart2 = MutableLiveData<String>("66")
    var tv_heart_pay2 = MutableLiveData<String>("17,000")
    var tv_heart3 = MutableLiveData<String>("31")
    var tv_heart_pay3 = MutableLiveData<String>("9,800")
    var tv_heart4 = MutableLiveData<String>("17")
    var tv_heart_pay4 = MutableLiveData<String>("5,500")
    var tv_heart5 = MutableLiveData<String>("2700")
    var tv_heart_pay5 = MutableLiveData<String>("499,000")
    var tv_heart6 = MutableLiveData<String>("1470")
    var tv_heart_pay6 = MutableLiveData<String>("229,000")
    var tv_heart7 = MutableLiveData<String>("614")
    var tv_heart_pay7 = MutableLiveData<String>("110,000")


    var pamentstart = MutableLiveData<Boolean>(false)
    var payclickindex = MutableLiveData<Int>(0)
    var invite_code = MutableLiveData<String>("")
    var inviteset =MutableLiveData<String>("")

    fun GetHeartLog() {

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(HeartService::class.java).GetHeartLog(
                my_seq.value!!,
            )
            d!!.enqueue(object : retrofit2.Callback<ResultHeartLogData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultHeartLogData>,
                    response: Response<ResultHeartLogData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if(type.equals("success")) {
                            var rows = data.rows
                            if (rows != null) {
                                if(rows.size!=0) {
                                    heartloglist.value =rows
                                }
                            }
                        }
                    }

                }

                override fun onFailure(
                    call: retrofit2.Call<ResultHeartLogData>,
                    t: Throwable
                ) {
                    Log.d("check", t.toString())
                }

            })

        }
    }

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
    fun HeartPayment(product_id:String,purchase_token:String,order_id:String,price:String,heart:String) {
        if(pamentstart.value==false) {
            pamentstart.value = true
            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter.create(HeartService::class.java).HeartPayment(
                    "구글", my_seq.value, product_id, purchase_token, order_id, price, heart
                )
                d!!.enqueue(object : retrofit2.Callback<ResultData> {
                    override fun onResponse(
                        call: retrofit2.Call<ResultData>,
                        response: Response<ResultData>
                    ) {
                        var data = response.body()
                        if (data != null) {

                            var type = data.type
                            if (type.equals("success")) {
                                toast.value = heart + "하트가 충전되었습니다."
                            } else {
                                toast.value = "충전에 실패했습니다. 인터넷상태를 확인해주세요."
                            }
                        }
                        pamentstart.value = false
                    }

                    override fun onFailure(
                        call: retrofit2.Call<ResultData>,
                        t: Throwable
                    ) {
                        pamentstart.value = false
                        Log.d("check", t.toString())
                    }

                })

            }
        }
    }
    fun DrivePassPayment1(v :DrivePassDialogStatus, fragmentManager: FragmentManager,con:Context) {
        if(pamentstart.value==false) {
            pamentstart.value = true
        var datestring = ""
        var payheart= ""
        var vtype = v
        var  date_string =""
        var dayType= 0
        if(vtype == DrivePassDialogStatus.DAY1){
            datestring = "1"
            payheart ="30"
        }else{
            datestring = "30"
            payheart ="100"
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

    fun DrivePassPayment(date:Int) {
        var datestring = ""
        var payheart ="100"
        if(date==1){
            datestring = "1"
            var payheart ="100"
        }else{
            datestring = "30"
            var payheart ="180"
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

    fun GetMyHeart() {
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(HeartService::class.java).GetMyheart(
                my_seq.value!!
            )
            d!!.enqueue(object : retrofit2.Callback<ResultHeartCheckData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultHeartCheckData>,
                    response: Response<ResultHeartCheckData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if(type.equals("success")) {
                            var heart =data.u_heart
                            tv_heartset .value= heart
                        }
                    }

                }

                override fun onFailure(
                    call: retrofit2.Call<ResultHeartCheckData>,
                    t: Throwable
                ) {
                    Log.d("check", t.toString())
                }

            })

        }
    }

    fun GetTerm(s:String){
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(HomerService::class.java).getterm()
            d!!.enqueue(object : retrofit2.Callback<ResultTermData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultTermData>,
                    response: Response<ResultTermData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type =data.type
                        if(type.equals("success")) {
                            var rows = data.rows
                            if(rows!=null) {
                                if (rows.size != 0) {
                                    for (termset in rows) {
                                        var vtype = termset.type
                                        var message = termset.content
                                        if (s.equals("구매약관")) {
                                            if (vtype.contains("구매약관")) {
                                                termsText.postValue(message)
                                                break
                                            }

                                        } else {
                                            if (vtype.contains("취소환불")) {
                                                termsText.postValue(message)
                                                break
                                            }
                                        }

                                    }
                                }
                            }

                        }

                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultTermData>, t: Throwable) {
                    Log.d("check", "failer")
                }

            })

        }
    }
}
