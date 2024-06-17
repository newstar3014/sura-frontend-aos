package com.supercarlounge.supercar.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.Network.UserEditInfoService
import com.supercarlounge.supercar.data.ResultData
import com.supercarlounge.supercar.data.UserInformationCarData
import com.supercarlounge.supercar.data.UserInformationData
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call

class IncomeInformationViewModel : ViewModel() {

    var text = MutableLiveData<String>("?")
    var reSend = MutableLiveData<String>("재전송")
    var reSendCheck = MutableLiveData<String>("인증번호를 확인해주세요")
    var reSendTime = MutableLiveData<String>("00:00")
    var tv_bottom = MutableLiveData<String>("다음 단계로 넘어가기")
    var incomeImage =  MutableLiveData<String>(null)
    var stateBottomText = MutableLiveData<Boolean>(false)
    var incomeNewImage =  MutableLiveData<String>(null)
    var     incmeCheckReturn =  MutableLiveData<String>("")
    var nickname = MutableLiveData<String>("")
    var resultState1Array = ArrayList<String>()
    var resultState2Array = ArrayList<String>()
    var resultState3Array = ArrayList<String>()
    var resultState4Array =ArrayList<String>()
    var resultState5Array = ArrayList<String>()
    var resultState6Array = ArrayList<String>()
    var resultState7Array = ArrayList<String>()
    var apiCall =  MutableLiveData<Boolean>(false)
    var apiCallOverlapCheck = MutableLiveData<Boolean>(false)
    var view = MutableLiveData<String>("")
    val _userdata = MutableLiveData<UserInformationData>()
    var userdata: MutableLiveData<UserInformationData> = _userdata
    var my_car_data_list : MutableLiveData<ArrayList<ArrayList<UserInformationCarData>>> =MutableLiveData(arrayListOf())

    fun incomePassengerCall(mincomeImage: MultipartBody.Part, ) {
        val userObj = JsonObject()
        userObj.addProperty("u_seq", userdata.value?.u_seq.toString())
        if (apiCallOverlapCheck.value == false){
            apiCallOverlapCheck.value=true
            var call1: Call<ResultData>? =
                NetworkManager.serveradapter!!.create(UserEditInfoService::class.java)
                    .userCompanionEditPassenger(
                        mincomeImage,

                        requestbodyString(userdata.value?.u_seq.toString()),
                                userObj
                    )

            call1!!.enqueue(
                object : retrofit2.Callback<ResultData> {
                    override fun onResponse(
                        call: Call<ResultData>,
                        response: retrofit2.Response<ResultData>
                    ) {
                        if (response.isSuccessful) {

                            var result = response.body()
                            var message = result!!.message
                            var type = result!!.type

                            if (type != null) {
                                apiCall.value = true
//                                if (type.equals("success")){
//


                            } else {

                            }


                        }
                        apiCallOverlapCheck.value=false
                    }

                    override fun onFailure(call: Call<ResultData>, t: Throwable) {
                        Log.d("setJoin", t.toString())
                        apiCallOverlapCheck.value=false
                    }
                })

        }



    }

    fun requestbodyString(text: String): RequestBody {
        return text.toRequestBody("text/plain".toMediaType())
    }
}
