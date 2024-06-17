package com.supercarlounge.supercar.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.Network.JoinService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.Network.UserService
import com.supercarlounge.supercar.data.*
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.supercarlounge.supercar.Network.UserEditInfoService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONException
import retrofit2.Call
import retrofit2.Response


class MyJoinInfomationViewModel : ViewModel() {

    var titleText = MutableLiveData<String>("")
    var time = MutableLiveData<String>("")
    var mainText = MutableLiveData<String>("")
    var subText = MutableLiveData<String>("")
    var checked1 = MutableLiveData<Boolean>(false)
    var checked2 = MutableLiveData<Boolean>(false)
    var u_smoking = MutableLiveData<String>("")
    var u_seq = MutableLiveData<String>("")
    var view = MutableLiveData<String>("")

    var selectCarBrand = MutableLiveData<String>("")
    var selectCarModel = MutableLiveData<String>("")
    var selectCarSeries = MutableLiveData<String>("")
    var editcall = MutableLiveData<Boolean>(false)
    var apiCallOverlapCheck = MutableLiveData<Boolean>(false)


    var drive_c_check = MutableLiveData<String>("")
    var drive_w_check = MutableLiveData<String>("")
    var aboutMe_check = MutableLiveData<String>("")

    var mainCheckReturn = MutableLiveData<String>("")
    var sub1CheckReturn = MutableLiveData<String>("")
    var sub2CheckReturn = MutableLiveData<String>("")
    var sub3CheckReturn = MutableLiveData<String>("")
    var userInfoObserver = MutableLiveData<Boolean>(false)
    var join_state = MutableLiveData<Boolean>(false)
    var companionCheckList: MutableLiveData<ArrayList<CompanionCheckData>> = MutableLiveData()
    var companionDataList: MutableLiveData<ArrayList<CompanionData>> =
        MutableLiveData(arrayListOf())
    val list = ArrayList<SubImage>()
    private val _itemList = MutableLiveData<ArrayList<SubImage>>()


    val _userdata = MutableLiveData<UserInformationData>()
    var userdata: MutableLiveData<UserInformationData> = _userdata

    var u_name = MutableLiveData<String>("")
    var u_gender = MutableLiveData<String>("")
    var u_birthday = MutableLiveData<String>("")
    var u_phone = MutableLiveData<String>("")
    var toast = MutableLiveData<String>("")


    //전달할 데이터
    var driveStyle = MutableLiveData<String>("")
    var driveCourse = MutableLiveData<String>("")
    var aboutMe = MutableLiveData<String>("")
    var mainImageUrl = MutableLiveData<String>(null)
    val SubImageUrlList: LiveData<ArrayList<SubImage>> = _itemList
    var nickname = MutableLiveData<String>("")
    var resultState2Array = ArrayList<String>()
    var resultState3Array = ArrayList<String>()
    var resultState4Array = ArrayList<String>()
    var resultState5Array = ArrayList<String>()
    var resultState6Array = ArrayList<String>()
    var resultState7Array = ArrayList<String>()
    var carList = ArrayList<CarBrandListData>()
    var carSeriesList = ArrayList<CarModelListData>()
    var carDetailsSeriesList = ArrayList<CarSubModelListData>()

    var stateBottomText = MutableLiveData<Boolean>(false)
    var mainImageNewUrl = MutableLiveData<String>(null)
    var carNewImage = MutableLiveData<String>(null)
    var insuranceNewImage = MutableLiveData<String>(null)
    var incomeNewImage = MutableLiveData<String>(null)
    var delete_sub_profile: MutableLiveData<ArrayList<SubImage>> =
        MutableLiveData<ArrayList<SubImage>>(arrayListOf())


    var income = MutableLiveData<String>("null")
    val carimage = MutableLiveData<String>("null")
    var insuranceimage = MutableLiveData<String>("null")
    var MultipartBodyCarImage = MutableLiveData<MultipartBody.Part>()
    var MultipartBodyInsuranceimageImage = MutableLiveData<MultipartBody.Part>()

    //드라이버인지 아닌지
    var type = MutableLiveData<String>("")


    private val _stateFlow = MutableStateFlow(0)
    val stateFlow = _stateFlow.asStateFlow()
    fun changeStateFlow(state: Int) {
        viewModelScope.launch {
            repeat(10) {
                _stateFlow.value = state

            }
        }
    }

    var ndata = SubImage("", "", "", 0, "", "", 0, "", "")

    var subImageData: SubImage? = SubImage("0", "", "", 0, "", "", 0, "", "")

    var SubImageUrlListSize = MutableLiveData<Int>(0)
    fun checkedSmoking1(boolean: Boolean) {
        if (boolean) {
            checked1.value = boolean
            checked2.value = !boolean
            u_smoking.value = "Y"


        } else {
//            checked1.value = boolean
            u_smoking.value = "N"
        }
    }

    init {
        _itemList.value = list
        delete_sub_profile.value?.add(ndata)
        delete_sub_profile.value?.add(ndata)
        delete_sub_profile.value?.add(ndata)
    }


    fun onclickSmoking1() {
        if (checked1.value == false) {
            checked1.value = true
            checked2.value = false
            u_smoking.value = "Y"

        } else {
            u_smoking.value = "N"
//            checked1.value = false
//            checked2.value = false
        }

    }

    fun checkedSmoking2(boolean: Boolean) {
        if (boolean) {
            checked2.value = boolean
            checked1.value = !boolean
            u_smoking.value = "N"
        } else {
//            checked2.value = boolean
            u_smoking.value = "Y"
        }


    }

    fun onclickSmoking2() {
        if (checked2.value == false) {
            checked2.value = true
            checked1.value = false
            u_smoking.value = "N"

        } else {
//            checked2.value = false
//            checked1.value = false
            u_smoking.value = "Y"
        }
    }


    fun setJoinOwner(
        mainProfile: MultipartBody.Part,
        subProfile: ArrayList<MultipartBody.Part>,
        maincar: MultipartBody.Part,
        insurance: MultipartBody.Part, application: MainApplication
    ) {
        if (apiCallOverlapCheck.value == false) {
            apiCallOverlapCheck.value = true
            var token = application!!.push_token
            if(token.isNullOrEmpty()){
                token = ""
            }
            val js2Array = JSONArray(resultState2Array)
            val js3Array = JSONArray(resultState3Array)
            val js7Array = JSONArray(resultState7Array)
            Log.d("ㅎㅇ", "setJoin: ")
            val userObj = JsonObject()

            userObj.addProperty("u_phone", u_phone.value)
            userObj.addProperty("u_name", u_name.value)
            userObj.addProperty("u_nickname", nickname.value)
            userObj.addProperty("u_smoking", u_smoking.value)
            userObj.addProperty("u_gender", u_gender.value)
            userObj.addProperty("u_birthday", u_birthday.value)
            userObj.addProperty("u_cm", "${resultState4Array[0]}")
            userObj.addProperty("u_type", type.value)
            userObj.addProperty("u_area", "${resultState6Array[0]}")
            userObj.addProperty("u_job", "${resultState5Array[0]}")
            userObj.addProperty("u_drive", "${resultState7Array[0]}")
            userObj.addProperty(
                "u_music",
                "[\"${resultState3Array[0]}\",\"${resultState3Array[1]}\"]"
            )
            userObj.addProperty("u_drive_w", driveStyle.value)
            userObj.addProperty("u_drive_c", driveCourse.value)
            userObj.addProperty(
                "u_charm",
                "[\"${resultState2Array[0]}\",\"${resultState2Array[1]}\"]"
            )
            userObj.addProperty("u_personality", "")
            userObj.addProperty("u_about_me", aboutMe.value)
            userObj.addProperty("u_code", "")
            userObj.addProperty("u_lat", "")
            userObj.addProperty("u_lon", "")
            userObj.addProperty("ocr_code", "")
            userObj.addProperty("u_email", "")
            userObj.addProperty("u_token", token)
            userObj.addProperty("u_loca", application.userData?.u_loca)
            userObj.addProperty("u_mt", application.userData?.u_mt)
            userObj.addProperty("u_mkt", application.userData?.u_mkt)
            Log.d(
                "약관1",
                application.userData?.u_loca.toString() + application.userData?.u_mt.toString() + application.userData?.u_mkt.toString()
            )

            val carObj = JsonObject()
            carObj.addProperty("uc_brand", selectCarBrand.value)
            carObj.addProperty("uc_model", selectCarModel.value)
            carObj.addProperty("csm_seq", selectCarSeries.value)
            carObj.addProperty("uc_check", "Y")


            Log.d("조인 JSON USER OBJ", carObj.toString())
            var carObjarray = JsonArray()
            try {
                carObjarray.add(carObj)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            var dir = "USER_INSERT"
            var call: Call<ResultData>? =
                NetworkManager.serveradapter!!.create(JoinService::class.java)
                    .setJoinOwner(
                        requestbodyString(dir),
                        mainProfile,
                        subProfile,
                        userObj,
                        carObj,
                        maincar,
                        insurance,
                    )

            call!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: Call<ResultData>,
                    response: retrofit2.Response<ResultData>
                ) {
                    if (response.isSuccessful) {

                        var result = response.body()
                        var message = result!!.message
                        var type = result!!.type

                        Log.d("setJoin", "2")
                        if (type != null) {
                            if (message.equals("회원 가입되었습니다")) {
                                application.savePhoneNumber(u_phone.value!!)
                                changeStateFlow(1)
                            } else if (message.equals("휴대폰 번호가 중복 됩니다")) {
                                toast.value = message
                            }

                        } else {
                            Log.d("netsuccess", "null")
                            changeStateFlow(3)
                        }


                    }
                    apiCallOverlapCheck.value = false
                }

                override fun onFailure(call: Call<ResultData>, t: Throwable) {
                    changeStateFlow(4)
                    Log.d("setJoin", "3")
                    apiCallOverlapCheck.value = false
                }
            })
        }

    }

    fun setJoinPassengerWoman(
        mainProfile: MultipartBody.Part,
        subProfile: ArrayList<MultipartBody.Part>, application: MainApplication
    ) {
        if (apiCallOverlapCheck.value == false) {
            apiCallOverlapCheck.value = true
            var token = application!!.push_token
            if(token.isNullOrEmpty()){
                token = ""
            }
            val userObj = JsonObject()
            userObj.addProperty("u_phone", u_phone.value)
            userObj.addProperty("u_name", u_name.value)
            userObj.addProperty("u_nickname", nickname.value)
            userObj.addProperty("u_smoking", u_smoking.value)
            userObj.addProperty("u_gender", u_gender.value)
            userObj.addProperty("u_birthday", u_birthday.value)
            userObj.addProperty("u_cm", "${resultState4Array[0]}")
            userObj.addProperty("u_type", type.value)
            userObj.addProperty("u_area", "${resultState6Array[0]}")
            userObj.addProperty("u_job", "${resultState5Array[0]}")
            userObj.addProperty("u_drive", "${resultState7Array[0]}")
            userObj.addProperty(
                "u_music",
                "[\"${resultState3Array[0]}\",\"${resultState3Array[1]}\"]"
            )
            userObj.addProperty("u_drive_w", driveStyle.value)
            userObj.addProperty("u_drive_c", driveCourse.value)
            userObj.addProperty(
                "u_charm",
                "[\"${resultState2Array[0]}\",\"${resultState2Array[1]}\"]"
            )
            userObj.addProperty("u_personality", "")
            userObj.addProperty("u_about_me", aboutMe.value)
            userObj.addProperty("u_code", "")
            userObj.addProperty("u_lat", "")
            userObj.addProperty("u_lon", "")
            userObj.addProperty("ocr_code", "")
            userObj.addProperty("u_email", "")
            userObj.addProperty("u_token", token)
            userObj.addProperty("u_loca", application.userData?.u_loca)
            userObj.addProperty("u_mt", application.userData?.u_mt)
            userObj.addProperty("u_mkt", application.userData?.u_mkt)
            Log.d(
                "약관2",
                application.userData?.u_loca.toString() + application.userData?.u_mt.toString() + application.userData?.u_mkt.toString()
            )
            Log.d("setJoin", "1")
            var dir = "USER_INSERT"
            var call: Call<ResultData>? =
                NetworkManager.serveradapter!!.create(JoinService::class.java)
                    .setJoinPassengerWoman(
                        requestbodyString(dir),
                        mainProfile,
                        subProfile,
                        userObj,

                        )

            call!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: Call<ResultData>,
                    response: retrofit2.Response<ResultData>
                ) {
                    if (response.isSuccessful) {

                        var result = response.body()
                        var message = result!!.message
                        var type = result!!.type

                        Log.d("setJoin", "2")
                        if (type != null) {
                            if (message.equals("회원 가입되었습니다")) {
                                application.savePhoneNumber(u_phone.value!!)
                                changeStateFlow(1)
                            } else if (message.equals("휴대폰 번호가 중복 됩니다")) {
                                toast.value = message
                            }

                        } else {
                            Log.d("netsuccess", "null")
                            changeStateFlow(3)
                        }


                    }
                    apiCallOverlapCheck.value = false
                }

                override fun onFailure(call: Call<ResultData>, t: Throwable) {
                    changeStateFlow(4)
                    Log.d("setJoin", "3")
                    apiCallOverlapCheck.value = false
                }
            })
        }
    }


    fun setJoinPassengerMan(
        mainProfile: MultipartBody.Part,
        subProfile: ArrayList<MultipartBody.Part>,
        income: MultipartBody.Part, application: MainApplication
    ) {
        if (apiCallOverlapCheck.value == false) {
            apiCallOverlapCheck.value = true
            var token = application!!.push_token
            if(token.isNullOrEmpty()){
                token = ""
            }
            Log.d("ㅎㅇ", "setJoin: ")
            val userObj = JsonObject()
            userObj.addProperty("u_phone", u_phone.value)
            userObj.addProperty("u_name", u_name.value)
            userObj.addProperty("u_nickname", nickname.value)
            userObj.addProperty("u_smoking", u_smoking.value)
            userObj.addProperty("u_gender", u_gender.value)
            userObj.addProperty("u_birthday", u_birthday.value)
            userObj.addProperty("u_cm", "${resultState4Array[0]}")
            userObj.addProperty("u_type", type.value)
            userObj.addProperty("u_area", "${resultState6Array[0]}")
            userObj.addProperty("u_job", "${resultState5Array[0]}")
            userObj.addProperty("u_drive", "${resultState7Array[0]}")
            userObj.addProperty(
                "u_music",
                "[\"${resultState3Array[0]}\",\"${resultState3Array[1]}\"]"
            )
            userObj.addProperty("u_drive_w", driveStyle.value)
            userObj.addProperty("u_drive_c", driveCourse.value)
            userObj.addProperty(
                "u_charm",
                "[\"${resultState2Array[0]}\",\"${resultState2Array[1]}\"]"
            )
            userObj.addProperty("u_personality", "")
            userObj.addProperty("u_about_me", aboutMe.value)
            userObj.addProperty("u_code", "")
            userObj.addProperty("u_lat", "")
            userObj.addProperty("u_lon", "")
            userObj.addProperty("ocr_code", "")
            userObj.addProperty("u_email", "")
            userObj.addProperty("u_token", token)
            userObj.addProperty("u_loca", application.userData?.u_loca)
            userObj.addProperty("u_mt", application.userData?.u_mt)
            userObj.addProperty("u_mkt", application.userData?.u_mkt)
            Log.d("조인 JSON USER OBJ", userObj.toString())
            Log.d(
                "약관3",
                application.userData?.u_loca.toString() + application.userData?.u_mt.toString() + application.userData?.u_mkt.toString()
            )

            Log.d("setJoin", "1")
            var dir = "USER_INSERT"
            var call: Call<ResultData>? =
                NetworkManager.serveradapter!!.create(JoinService::class.java)
                    .setJoinPassengerMan(
                        requestbodyString(dir),
                        mainProfile,
                        subProfile,
                        userObj,
                        income,
                    )

            call!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: Call<ResultData>,
                    response: retrofit2.Response<ResultData>
                ) {
                    if (response.isSuccessful) {

                        var result = response.body()
                        var message = result!!.message
                        var type = result!!.type

                        Log.d("setJoin", "2")
                        if (type != null) {
                            if (message.equals("회원 가입되었습니다")) {
                                application.savePhoneNumber(u_phone.value!!)
                                changeStateFlow(1)

                            } else if (message.equals("휴대폰 번호가 중복 됩니다")) {
                                toast.value = message
                            }
                        } else {
                            Log.d("netsuccess", "null")
                            changeStateFlow(3)
                        }


                    }
                    apiCallOverlapCheck.value = false
                }

                override fun onFailure(call: Call<ResultData>, t: Throwable) {
                    changeStateFlow(4)
                    Log.d("setJoin", "3")
                    apiCallOverlapCheck.value = false
                }
            })
        }

    }


    fun setQuickUserJoinOwner(
        mainProfile: MultipartBody.Part,
        subProfile: ArrayList<MultipartBody.Part>,
        maincar: MultipartBody.Part,
        insurance: MultipartBody.Part, application: MainApplication
    ) {

        if (apiCallOverlapCheck.value == false) {
            apiCallOverlapCheck.value = true
            var token = application!!.push_token
            if(token.isNullOrEmpty()){
                token = ""
            }
            Log.d("ㅎㅇ", "setJoin: ")
            val userObj = JsonObject()
            userObj.addProperty("u_seq", application.userData?.u_seq)
            userObj.addProperty("u_phone", u_phone.value)
            userObj.addProperty("u_name", u_name.value)
            userObj.addProperty("u_nickname", nickname.value)
            userObj.addProperty("u_smoking", u_smoking.value)
            userObj.addProperty("u_gender", u_gender.value)
            userObj.addProperty("u_birthday", u_birthday.value)
            userObj.addProperty("u_cm", "${resultState4Array[0]}")
            userObj.addProperty("u_type", type.value)
            userObj.addProperty("u_area", "${resultState6Array[0]}")
            userObj.addProperty("u_job", "${resultState5Array[0]}")
            userObj.addProperty("u_drive", "${resultState7Array[0]}")
            userObj.addProperty(
                "u_music",
                "[\"${resultState3Array[0]}\",\"${resultState3Array[1]}\"]"
            )
            userObj.addProperty("u_drive_w", driveStyle.value)
            userObj.addProperty("u_drive_c", driveCourse.value)
            userObj.addProperty(
                "u_charm",
                "[\"${resultState2Array[0]}\",\"${resultState2Array[1]}\"]"
            )
            userObj.addProperty("u_personality", "")
            userObj.addProperty("u_about_me", aboutMe.value)
            userObj.addProperty("u_code", "")
            userObj.addProperty("u_lat", "")
            userObj.addProperty("u_lon", "")
            userObj.addProperty("ocr_code", "")
            userObj.addProperty("u_email", "")
            userObj.addProperty("u_token", token)


            val carObj = JsonObject()
            carObj.addProperty("uc_brand", selectCarBrand.value)
            carObj.addProperty("uc_model", selectCarModel.value)
            carObj.addProperty("csm_seq", selectCarSeries.value)
            carObj.addProperty("uc_check", "Y")


            Log.d("조인 JSON USER OBJ", carObj.toString())
            var carObjarray = JsonArray()
            try {
                carObjarray.add(carObj)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            var dir = "USER_INSERT"
            var call: Call<ResultData>? =
                NetworkManager.serveradapter!!.create(JoinService::class.java)
                    .setQuickUserJoinOwner(
                        requestbodyString(dir),
                        mainProfile,
                        subProfile,
                        userObj,
                        carObj,
                        maincar,
                        insurance,
                    )

            call!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: Call<ResultData>,
                    response: retrofit2.Response<ResultData>
                ) {
                    if (response.isSuccessful) {

                        var result = response.body()
                        var message = result!!.message
                        var type = result!!.type

                        Log.d("setJoin", "2")
                        if (type != null) {
                            if (message.equals("회원 가입되었습니다")) {
                                application.savePhoneNumber(u_phone.value!!)
                                changeStateFlow(1)
                            } else if (message.equals("휴대폰 번호가 중복 됩니다")) {
                                toast.value = message
                                apiCallOverlapCheck.value = false
                            }

                        } else {
                            Log.d("netsuccess", "null")
                            changeStateFlow(3)
                            apiCallOverlapCheck.value = false
                        }


                    }else{
                        apiCallOverlapCheck.value = false
                    }

                }

                override fun onFailure(call: Call<ResultData>, t: Throwable) {
                    changeStateFlow(4)
                    Log.d("setJoin", "3")
                    apiCallOverlapCheck.value = false
                }
            })
        }

    }

    fun setQuickUserJoinPassenger(
        mainProfile: MultipartBody.Part,
        subProfile: ArrayList<MultipartBody.Part>,
        income: MultipartBody.Part, application: MainApplication
    ) {
        if (apiCallOverlapCheck.value == false) {
            apiCallOverlapCheck.value = true
            var token = application!!.push_token
            if(token.isNullOrEmpty()){
                token = ""
            }
            Log.d("apiCallOver1", apiCallOverlapCheck.value.toString())
            Log.d("ㅎㅇ", "setJoin: ")
            val userObj = JsonObject()
            userObj.addProperty("u_seq", application.userData?.u_seq)
            userObj.addProperty("u_phone", u_phone.value)
            userObj.addProperty("u_name", u_name.value)
            userObj.addProperty("u_nickname", nickname.value)
            userObj.addProperty("u_smoking", u_smoking.value)
            userObj.addProperty("u_gender", u_gender.value)
            userObj.addProperty("u_birthday", u_birthday.value)
            userObj.addProperty("u_cm", "${resultState4Array[0]}")
            userObj.addProperty("u_type", type.value)
            userObj.addProperty("u_area", "${resultState6Array[0]}")
            userObj.addProperty("u_job", "${resultState5Array[0]}")
            userObj.addProperty("u_drive", "${resultState7Array[0]}")
            userObj.addProperty(
                "u_music",
                "[\"${resultState3Array[0]}\",\"${resultState3Array[1]}\"]"
            )
            userObj.addProperty("u_drive_w", driveStyle.value)
            userObj.addProperty("u_drive_c", driveCourse.value)
            userObj.addProperty(
                "u_charm",
                "[\"${resultState2Array[0]}\",\"${resultState2Array[1]}\"]"
            )
            userObj.addProperty("u_personality", "")
            userObj.addProperty("u_about_me", aboutMe.value)
            userObj.addProperty("u_code", "")
            userObj.addProperty("u_lat", "")
            userObj.addProperty("u_lon", "")
            userObj.addProperty("ocr_code", "")
            userObj.addProperty("u_email", "")
            userObj.addProperty("u_token", token)
            Log.d("조인 JSON USER OBJ", userObj.toString())


            Log.d("setJoin", "1")
            Log.d("setJoin", userObj.toString())
            var dir = "USER_INSERT"
            var call: Call<ResultData>? =
                NetworkManager.serveradapter!!.create(JoinService::class.java)
                    .setQuickUserJoinPassenger(
                        requestbodyString(dir),
                        mainProfile,
                        subProfile,
                        userObj,
                        income,
                    )

            call!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: Call<ResultData>,
                    response: retrofit2.Response<ResultData>
                ) {
                    if (response.isSuccessful) {

                        var result = response.body()
                        var message = result!!.message
                        var type = result!!.type
                        Log.d("데이터", result.toString())
                        Log.d("setJoin", "2")
                        if (type != null) {
                            if (message.equals("회원 가입되었습니다")) {
                                application.savePhoneNumber(u_phone.value!!)
                                changeStateFlow(1)

                            } else if (message.equals("휴대폰 번호가 중복 됩니다")) {
                                toast.value = message
                                apiCallOverlapCheck.value = false
                            }

                        } else {
                            Log.d("netsuccess", "null")
                            changeStateFlow(3)
                            apiCallOverlapCheck.value = false
                        }


                    }else{
                        apiCallOverlapCheck.value = false
                    }
                    Log.d("apiCallOver2", apiCallOverlapCheck.value.toString())
                }

                override fun onFailure(call: Call<ResultData>, t: Throwable) {
                    changeStateFlow(4)
                    Log.d("apiCallOver3", apiCallOverlapCheck.value.toString())
                    Log.d("setJoin", "3")
                    apiCallOverlapCheck.value = false
//                    apiCallOverlapCheck.value = false
                }
            })
        }
    }

    fun setQuickUserJoinWomanPassenger(
        mainProfile: MultipartBody.Part,
        subProfile: ArrayList<MultipartBody.Part>,
        application: MainApplication
    ) {
        if (apiCallOverlapCheck.value == false) {
            apiCallOverlapCheck.value = true
            var token = application!!.push_token
            if(token.isNullOrEmpty()){
                token = ""
            }
            Log.d("ㅎㅇ", "setJoin: ")
            val userObj = JsonObject()
            userObj.addProperty("u_seq", application.userData?.u_seq)
            userObj.addProperty("u_phone", u_phone.value)
            userObj.addProperty("u_name", u_name.value)
            userObj.addProperty("u_nickname", nickname.value)
            userObj.addProperty("u_smoking", u_smoking.value)
            userObj.addProperty("u_gender", u_gender.value)
            userObj.addProperty("u_birthday", u_birthday.value)
            userObj.addProperty("u_cm", "${resultState4Array[0]}")
            userObj.addProperty("u_type", type.value)
            userObj.addProperty("u_area", "${resultState6Array[0]}")
            userObj.addProperty("u_job", "${resultState5Array[0]}")
            userObj.addProperty("u_drive", "${resultState7Array[0]}")
            userObj.addProperty(
                "u_music",
                "[\"${resultState3Array[0]}\",\"${resultState3Array[1]}\"]"
            )
            userObj.addProperty("u_drive_w", driveStyle.value)
            userObj.addProperty("u_drive_c", driveCourse.value)
            userObj.addProperty(
                "u_charm",
                "[\"${resultState2Array[0]}\",\"${resultState2Array[1]}\"]"
            )
            userObj.addProperty("u_personality", "")
            userObj.addProperty("u_about_me", aboutMe.value)
            userObj.addProperty("u_code", "")
            userObj.addProperty("u_lat", "")
            userObj.addProperty("u_lon", "")
            userObj.addProperty("ocr_code", "")
            userObj.addProperty("u_email", "")
            userObj.addProperty("u_token", token)
            Log.d("조인 JSON USER OBJ", userObj.toString())


            Log.d("setJoin", "1")
            Log.d("setJoin", userObj.toString())
            var dir = "USER_INSERT"
            var call: Call<ResultData>? =
                NetworkManager.serveradapter!!.create(JoinService::class.java)
                    .setQuickUserJoinWomanPassenger(
                        requestbodyString(dir),
                        mainProfile,
                        subProfile,
                        userObj,
                    )

            call!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: Call<ResultData>,
                    response: retrofit2.Response<ResultData>
                ) {
                    if (response.isSuccessful) {

                        var result = response.body()
                        var message = result!!.message
                        var type = result!!.type

                        Log.d("setJoin", "2")
                        if (type != null) {
                            if (message.equals("회원 가입되었습니다")) {
                                application.savePhoneNumber(u_phone.value!!)
                                changeStateFlow(1)

                            } else if (message.equals("휴대폰 번호가 중복 됩니다")) {
                                toast.value = message
                                apiCallOverlapCheck.value = false
                            }

                        } else {
                            Log.d("netsuccess", "null")
                            changeStateFlow(3)
                            apiCallOverlapCheck.value = false
                        }


                    }else{
                        apiCallOverlapCheck.value = false
                    }

                }

                override fun onFailure(call: Call<ResultData>, t: Throwable) {
                    changeStateFlow(4)
                    Log.d("setJoin", "3")
                    apiCallOverlapCheck.value = false
                }
            })
        }

    }

    fun requestbodyString(text: String): RequestBody {
        return text.toRequestBody("text/plain".toMediaType())
    }

    fun getBody(key: String, value: Any): MultipartBody.Part {
        return MultipartBody.Part.createFormData(key, value.toString())
    }

    fun getUserInformation(u_seq: String, application: MainApplication) {
        Log.d("", u_seq.toString())
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(UserService::class.java).getUserInformation(
                u_seq,
            )
            d!!.enqueue(object : retrofit2.Callback<ResultUserInformationData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultUserInformationData>,
                    response: Response<ResultUserInformationData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        application.userData = data.rows[0]
                        application.userCarData = data.car
                        var userdata = data.rows[0]

                        userInfoObserver.value = true
                        Log.d("가져온 유저 데이터", application.userData.toString())
                        Log.d("가져온 유저 차 데이터", application.userCarData.toString())

                    }

                }

                override fun onFailure(
                    call: retrofit2.Call<ResultUserInformationData>,
                    t: Throwable
                ) {
                    Log.d("check", t.toString())
                }

            })

        }

    }


    fun userCompanionEditManOwner(
        mainCar: MultipartBody.Part,
        insurance: MultipartBody.Part,
        mainProfile: MultipartBody.Part,
        subProfile: ArrayList<MultipartBody.Part>
    ) {
        if (apiCallOverlapCheck.value == false) {
            apiCallOverlapCheck.value = true
            if (checked1.value!!) {
                u_smoking.value = "Y"
            } else {
                u_smoking.value = "N"
            }
            val userObj = JsonObject()
            var check = false
            if (!userdata.value?.u_smoking.equals(u_smoking.value)) {
                userObj.addProperty("u_smoking", u_smoking.value)
                check = true
            }
            if (!userdata.value?.u_drive_w.equals(driveStyle.value)) {
                userObj.addProperty("u_drive_w", driveStyle.value)
                check = true
            }
            if (!userdata.value?.u_drive_c.equals(driveCourse.value)) {
                userObj.addProperty("u_drive_c", driveCourse.value)
                check = true
            }
            if (!userdata.value?.u_about_me.equals(aboutMe.value)) {
                userObj.addProperty("u_about_me", aboutMe.value)
                check = true
            }

//            if (check) {
                userObj.addProperty("u_seq", userdata.value?.u_seq.toString())
//            }


            Log.d("통신1", userObj.toString())


            var call1: Call<ResultData>? =
                NetworkManager.serveradapter!!.create(UserEditInfoService::class.java)
                    .userCompanionEditManOwner(
                        mainCar,
                        insurance,
                        mainProfile,
                        subProfile,
                        delete_sub_profile.value!!,
                        userObj,
                        requestbodyString(userdata.value?.u_seq.toString())
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

                            if (type.equals("success")) {
                                editcall.value = true

                                Log.d("userCompanionEditManOwner", "성공")
                            }

                        } else {

                        }
                        apiCallOverlapCheck.value = false
                    }

                    override fun onFailure(call: Call<ResultData>, t: Throwable) {
                        Log.d("userCompanionEditManOwner", t.toString())
                        apiCallOverlapCheck.value = false
                    }
                })
        }


    }


    fun userCompanionEditManPassenger(
        income: MultipartBody.Part,
        mainProfile: MultipartBody.Part,
        subProfile: ArrayList<MultipartBody.Part>
    ) {
        if (apiCallOverlapCheck.value == false) {
            apiCallOverlapCheck.value = true

            if (checked1.value!!) {
                u_smoking.value = "Y"
            } else {
                u_smoking.value = "N"
            }
            val userObj = JsonObject()
            var check = false
            if (!userdata.value?.u_smoking.equals(u_smoking.value)) {
                userObj.addProperty("u_smoking", u_smoking.value)
                check = true
            }
            if (!userdata.value?.u_drive_w.equals(driveStyle.value)) {
                userObj.addProperty("u_drive_w", driveStyle.value)
                check = true
            }
            if (!userdata.value?.u_drive_c.equals(driveCourse.value)) {
                userObj.addProperty("u_drive_c", driveCourse.value)
                check = true
            }
            if (!userdata.value?.u_about_me.equals(aboutMe.value)) {
                userObj.addProperty("u_about_me", aboutMe.value)
                check = true
            }

//            if (check) {
                userObj.addProperty("u_seq", userdata.value?.u_seq.toString())
//            }


            Log.d("통신1", userObj.toString())


            var call1: Call<ResultData>? =
                NetworkManager.serveradapter!!.create(UserEditInfoService::class.java)
                    .userCompanionEditManPassenger(
                        income,
                        mainProfile,
                        subProfile,
                        delete_sub_profile.value!!,
                        userObj,
                        requestbodyString(userdata.value?.u_seq.toString())
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

                            if (type.equals("success")) {
                                editcall.value = true
                            }


                        }
                        apiCallOverlapCheck.value = false
                    }

                    override fun onFailure(call: Call<ResultData>, t: Throwable) {
                        Log.d("setJoin", t.toString())
                        apiCallOverlapCheck.value = false
                    }
                })
        }

    }

    fun userCompanionEditWoMan(
        mainProfile: MultipartBody.Part,
        subProfile: ArrayList<MultipartBody.Part>
    ) {
        if (apiCallOverlapCheck.value == false) {
            apiCallOverlapCheck.value = true

            if (checked1.value!!) {
                u_smoking.value = "Y"
            } else {
                u_smoking.value = "N"
            }
            val userObj = JsonObject()
            var check = false
            if (!userdata.value?.u_smoking.equals(u_smoking.value)) {
                userObj.addProperty("u_smoking", u_smoking.value)
                check = true
            }
            if (!userdata.value?.u_drive_w.equals(driveStyle.value)) {
                userObj.addProperty("u_drive_w", driveStyle.value)
                check = true
            }
            if (!userdata.value?.u_drive_c.equals(driveCourse.value)) {
                userObj.addProperty("u_drive_c", driveCourse.value)
                check = true
            }
            if (!userdata.value?.u_about_me.equals(aboutMe.value)) {
                userObj.addProperty("u_about_me", aboutMe.value)
                check = true
            }

//            if (check) {
                userObj.addProperty("u_seq", userdata.value?.u_seq.toString())
//            }


            Log.d("통신1", userObj.toString())


            var call1: Call<ResultData>? =
                NetworkManager.serveradapter!!.create(UserEditInfoService::class.java)
                    .userCompanionEditWoMan(
                        mainProfile,
                        subProfile,
                        delete_sub_profile.value!!,
                        userObj,
                        requestbodyString(userdata.value?.u_seq.toString())
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

                            if (type.equals("success")) {
                                editcall.value = true
                            }


                        }
                        apiCallOverlapCheck.value = false
                    }

                    override fun onFailure(call: Call<ResultData>, t: Throwable) {
                        Log.d("setJoin", t.toString())
                        apiCallOverlapCheck.value = false

                    }
                })
        }
    }

}