package com.supercarlounge.supercar.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supercarlounge.supercar.Constans
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.Network.*
import com.supercarlounge.supercar.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class SubscriptionApplicationCompleteViewModel : ViewModel() {

    var text = MutableLiveData<String>("?")
    var reSend = MutableLiveData<String>("재전송")
    var reSendCheck = MutableLiveData<String>("인증번호를 확인해주세요")
    var reSendTime = MutableLiveData<String>("00:00")
    var Checked =  MutableLiveData<Boolean>(false)
    var invitation_code = MutableLiveData<String>("")
    var my_u_seq = MutableLiveData<String>("")
    var apicall = MutableLiveData<Boolean>(false)

    var u_admin = MutableLiveData<String>("")
    var u_admin_check = MutableLiveData<Boolean>(false)
    var tv_title = MutableLiveData<String>("가입 신청이\n완료되었습니다.")
    var my_user_data = MutableLiveData<UserInformationData?>()
    var my_car_data_list : MutableLiveData<ArrayList<ArrayList<UserInformationCarData>>> =MutableLiveData(arrayListOf())
    var companionDataList: MutableLiveData<ArrayList<CompanionData>> = MutableLiveData(arrayListOf())
    var tv_sub = MutableLiveData<String>(  "현재 가입 심사가 진행 중입니다\n가입 심사는 1~3일이 소요됩니다.\n\n가입 반려 이후 재심사를 하지 않는다면 모든 개인 정보는\n3일 이내 완전히 삭제되어 다시 작성하셔야 합니다.")
    var bottom_text = MutableLiveData<String>("앱 종료하기")
    var invitation_code_check =MutableLiveData <Boolean>(false)
    //바텀시트
    var checkedPass = MutableLiveData<Boolean>(false)
    var bubbleRick = MutableLiveData<Boolean>(false)

//    companion object {
//        const val  = 5
//        const val SUCCESS = 5
//        const val CHECK_SUCCESS = 5
//        const val SUCCESS = 5
//}

    private val _stateFlow = MutableStateFlow(0)
    val stateFlow = _stateFlow.asStateFlow()
    fun changeStateFlow(state :Int){
        viewModelScope.launch {
            repeat(10){
                _stateFlow.value = state
            }
        }
    }




    fun InsertInvitationCodeCehck() {
        if ( bubbleRick.value == false) {
            bubbleRick.value =true
            Log.d("InsertInvitationCodeCehck", invitation_code.value.toString())
            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter!!.create(JoinService::class.java)
                    .InsertInvitationCodeCehck(invitation_code.value)
                d!!.enqueue(object : retrofit2.Callback<ResultData> {
                    override fun onResponse(
                        call: retrofit2.Call<ResultData>,
                        response: Response<ResultData>
                    ) {
                        var data = response.body()
                        if (data != null) {
                            var rows = data.type
                            var message = data.message
                            Log.d("InsertInvitationCodeCehck  message:", message)

                            if (rows != null) {

                                if (message.equals("존재하지 않는 코드 입니다")) {
                                    changeStateFlow(Constans.CODE_CHECK_FAIL)
                                } else {
                                    changeStateFlow(Constans.CODE_CHECK_SUCCESS)
                                }
//                            InsertInvitationCodeSave()

                            } else {

                            }

                        } else {

                        }
                        bubbleRick.value =false
                    }

                    override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                        Log.d("check", "failer")
                        bubbleRick.value =false
                    }

                })

            }
        }

    }


    fun InsertInvitationCodeSave(u_seq:String) {
        Log.d("InsertInvitationCodeCehck", invitation_code.value.toString())

        if (apicall.value==false){
            apicall.value = true

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(JoinService::class.java)
                .InsertInvitationCodeSave(my_u_seq.value,invitation_code.value)
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var rows = data.type
                        var message =data.message
                        if (rows != null) {
                            if (message.equals("초대코드 입력 하트 증정 성공")){
                                invitation_code_check.value = true
                            }

                        } else {

                        }

                    } else {

                    }
                    apicall.value = false
                }

                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Log.d("check", "failer")
                    apicall.value = false
                }

            })

        }
        }

    }

    fun getCompanion(u_seq:String) {

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(JoinService::class.java)
                .getCompanion(u_seq)
            d!!.enqueue(object : retrofit2.Callback<ResultCompanionData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultCompanionData>,
                    response: Response<ResultCompanionData>
                ) {
                    Log.d("getCompanion", "0")
                    var data = response.body()
                    if (data != null) {
                        var rows = data.rows
                        var message =data.message
                        Log.d("getCompanion", "1")

                        if (rows != null) {
                            Log.d("getCompanion", rows.size.toString())
                            companionDataList.value = rows

                        } else {

                        }

                    } else {

                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultCompanionData>, t: Throwable) {
                    Log.d("check", "failer")
                    Log.d("getCompanion", "3")
                }

            })

        }


    }

    fun checkLogin(u_phone: String, u_token: String ,application: MainApplication) {

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(LoginService::class.java).checkLogin(u_phone, u_token, true)
            d!!.enqueue(object : retrofit2.Callback<ResultLoginData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultLoginData>,
                    response: Response<ResultLoginData>
                ) {

                    var data = response.body()
                    if (data != null) {
                        Log.d("checkLogin data.message:", data.message.toString())
                        Log.d("checkLogin data.status:", data.status.toString())
                        my_u_seq.value =data.u_seq.toString()
                        getNewUserInformation(my_u_seq.value.toString(),application)
                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultLoginData>, t: Throwable) {
                    Log.d("check", t.toString())
                }

            })

        }

    }
    fun getNewUserInformation(u_seq : String,application: MainApplication){

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(UserService::class.java).getNewUserInformation(u_seq,
            )
            d!!.enqueue(object : retrofit2.Callback<ResultUserInformationData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultUserInformationData>,
                    response: Response<ResultUserInformationData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        if (data.rows!= null ){
                            if (data.rows.size != 0){

                                application.userData = data.rows[0]
                                application. userCarData =data.car
                                var userdata = data.rows[0]
                                my_user_data.value = userdata
                                my_car_data_list.value = data.car
                                my_u_seq.value = userdata.u_seq.toString()

                            }
                        }




                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultUserInformationData>, t: Throwable) {
                    Log.d("check", t.toString())
                }

            })

        }

    }


}
