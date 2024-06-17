package com.supercarlounge.supercar.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.Network.UserEditInfoService
import com.supercarlounge.supercar.data.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call

class CarRegistrationViewModel : ViewModel() {

    var text = MutableLiveData<String>("?")
    var reSend = MutableLiveData<String>("재전송")
    var reSendCheck = MutableLiveData<String>("인증번호를 확인해주세요")
    var reSendTime = MutableLiveData<String>("00:00")

    var carCheckReturn= MutableLiveData<String>("수정")
    var insuerCheckReturn = MutableLiveData<String>("수정")
    var tv_bottom = MutableLiveData<String>("다음 단계로 넘어가기")
    var view= MutableLiveData<String>("")
    var carimage = MutableLiveData<String>(null)
    var carNewImage = MutableLiveData<String>(null)
    var insuranceimage = MutableLiveData<String>(null)
    var insuranceNewimage = MutableLiveData<String>(null)
    var apiCall = MutableLiveData<Boolean>(false)
    var apiCallOverlapCheck_owner = MutableLiveData<Boolean>(false)
    var apiCallOverlapCheck_car= MutableLiveData<Boolean>(false)
    var apiCallOverlapCheck_insu = MutableLiveData<Boolean>(false)
    //넘겨줄데이터터
    var nickname = MutableLiveData<String>("")
    var resultState1Array = ArrayList<String>()
    var resultState2Array = ArrayList<String>()
    var resultState3Array = ArrayList<String>()
    var resultState4Array =ArrayList<String>()
    var resultState5Array = ArrayList<String>()
    var resultState6Array = ArrayList<String>()
    var resultState7Array = ArrayList<String>()

    var stateBottomText= MutableLiveData<Boolean>(false)




     var carList = ArrayList<CarBrandListData>()
     var carSeriesList = ArrayList<CarModelListData>()
     var carDetailsSeriesList = ArrayList<CarSubModelListData>()

    val _userdata = MutableLiveData<UserInformationData>()
    var userdata: MutableLiveData<UserInformationData> = _userdata
    var my_car_data_list : MutableLiveData<ArrayList<ArrayList<UserInformationCarData>>> =MutableLiveData(arrayListOf())
    var companionDataList: MutableLiveData<ArrayList<CompanionData>> = MutableLiveData(arrayListOf())



    fun carAndInsuranceCall(
        mcarImage: MultipartBody.Part,
        mInsurance: MultipartBody.Part) {

        if ( apiCallOverlapCheck_owner.value == false) {
            apiCallOverlapCheck_owner.value = true


            val userObj = JsonObject()
            userObj.addProperty("u_seq", userdata.value?.u_seq.toString())
            var call1: Call<ResultData>? =
                NetworkManager.serveradapter!!.create(UserEditInfoService::class.java)
                    .userCompanionEditOwner(
                        mcarImage,
                        mInsurance,
                        requestbodyString(
                            userdata.value?.u_seq.toString()
                        ),
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
                                if (type.equals("success")) {
                                    Log.d("체크", "carAndInsuranceCall: ")
                                    apiCall.value = true
                                }



                            } else {

                            }


                        }
                        apiCallOverlapCheck_owner.value = false
                    }

                    override fun onFailure(call: Call<ResultData>, t: Throwable) {
                        Log.d("setJoin", t.toString())
                        apiCallOverlapCheck_owner.value = false
                    }
                })


        }

    }


    fun carCall(
        mcarImage: MultipartBody.Part, ) {
        
        if ( apiCallOverlapCheck_car.value == false){
            apiCallOverlapCheck_car.value = true
            val userObj = JsonObject()
            userObj.addProperty("u_seq", userdata.value?.u_seq.toString())
            var call1: Call<ResultData>? =
                NetworkManager.serveradapter!!.create(UserEditInfoService::class.java)
                    .userCompanionEditOwner_Car(
                        mcarImage,
                        requestbodyString(userdata.value?.u_seq.toString())
                    ,   userObj
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
                                if (type.equals("success")){
                                    Log.d("체크", "carCall: ")
                                    apiCall.value = true
                                }

                            } else {

                            }
                            apiCallOverlapCheck_car.value =false

                        }
                    }

                    override fun onFailure(call: Call<ResultData>, t: Throwable) {
                        Log.d("setJoin", t.toString())
                        apiCallOverlapCheck_car.value =false
                    }
                })


        }
      



    }

    fun insuranceCall(
        mInsurance: MultipartBody.Part, ) {



        if (apiCallOverlapCheck_insu.value == false){
            apiCallOverlapCheck_insu.value == true
            val userObj = JsonObject()
            userObj.addProperty("u_seq", userdata.value?.u_seq.toString())
            var call1: Call<ResultData>? =
                NetworkManager.serveradapter!!.create(UserEditInfoService::class.java)
                    .userCompanionEditOwner_Insurance(
                        mInsurance,
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
                                if (type.equals("success")){
                                    Log.d("체크", "insuranceCall: ")
                                    apiCall.value = true

                                }

                            } else {

                            }


                        }
                        apiCallOverlapCheck_insu.value == false
                    }

                    override fun onFailure(call: Call<ResultData>, t: Throwable) {
                        Log.d("setJoin", t.toString())
                        apiCallOverlapCheck_insu.value == false
                    }
                })


        }




    }


    fun requestbodyString(text: String): RequestBody {
        return text.toRequestBody("text/plain".toMediaType())
    }

}
