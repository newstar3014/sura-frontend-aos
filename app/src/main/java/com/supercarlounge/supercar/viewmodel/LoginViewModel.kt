package com.supercarlounge.supercar.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supercarlounge.supercar.Constans
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.MainApplication.Companion.token
import com.supercarlounge.supercar.Network.*
import com.supercarlounge.supercar.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel : ViewModel() {

    var text = MutableLiveData<String>("?")
    var reSend = MutableLiveData<String>("재전송")
    var reSendCheck = MutableLiveData<String>("인증번호를 확인해주세요")
    var reSendTime = MutableLiveData<String>("00:00")
    var Checked =  MutableLiveData<Boolean>(false)
    var u_name =MutableLiveData<String>("")
    var u_gender =MutableLiveData<String>("")
    var u_birthday =MutableLiveData<String>("")
    var u_phone =MutableLiveData<String>("")
    var type =MutableLiveData<String>("")
    var number = MutableLiveData<String>("")
    var certificationNumber1 = MutableLiveData<String>("")
    var certificationNumber2 = MutableLiveData<String>("8978978789456456")
    var certificationCheckShow =  MutableLiveData<Boolean>(false)
    var ToastText = MutableLiveData<String>("")
    var hashKey = MutableLiveData<String>("")
    var phoneNumber = MutableLiveData<String>("")
    var smscallRick = MutableLiveData<Boolean>(false)
    var toast = MutableLiveData<String>("")

    //바텀시트
    var allchecked = MutableLiveData<Boolean>(false)
    var checked1 = MutableLiveData<Boolean>(false)
    var checked2 = MutableLiveData<Boolean>(false)
    var checked3 = MutableLiveData<Boolean>(false)
    var checked4 = MutableLiveData<Boolean>(false)
    var checked5 = MutableLiveData<Boolean>(false)
    var checked_success = MutableLiveData<Boolean>(false)
    var nowversion = MutableLiveData<String>("")
    var isupdate= MutableLiveData<Boolean>(false)
    var viewType = MutableLiveData<Boolean>(false)
    var u_admin = MutableLiveData<String?>("")
    fun onChecked(check :Boolean){
        allchecked.value = check

    }


    private val _stateFlow = MutableStateFlow(0)
    val stateFlow = _stateFlow.asStateFlow()
    fun changeStateFlow(state :Int){
        viewModelScope.launch {
            repeat(10){
                _stateFlow.value = state
            }
        }
    }

    fun onclickAll(){
        allchecked.value = allchecked.value ==false

        checked1.value = allchecked.value
        checked2.value = allchecked.value
        checked3.value = allchecked.value
        checked4.value = allchecked.value
        checked5.value = allchecked.value
    }

    fun checkedCheck(){
        if (checked1.value == true && checked2.value == true){
//            changeStateFlow(Constans.TERMS_TURE)
        }else{
//            changeStateFlow(Constans.TERMS_FALSE)
        }
    }


    fun setCheckAll(checked: Boolean){
        allchecked.value = !checked
        checked1.value = checked
        checked2.value = checked
        checked3.value = checked
        checked4.value = checked
        checked5.value = checked
    }

    fun setCheck1(checked: Boolean){
        checked1.value = checked
        if (checked1.value ==true) {
            allchecked.value =
                checked1.value == true && checked2.value == true && checked3.value == true && checked4.value == true&& checked5.value==true
        }else{
            allchecked.value = false
        }
    }


    fun setCheck2(checked: Boolean){
        checked2.value = checked
        if (checked2.value ==true) {
            allchecked.value =
                checked1.value == true && checked2.value == true && checked3.value == true && checked4.value == true&& checked5.value==true
        }else{
            allchecked.value = false
        }
    }


    fun setCheck3(checked: Boolean){
        checked3.value = checked
        if (checked3.value ==true) {
            allchecked.value =
                checked1.value == true && checked2.value == true && checked3.value == true && checked4.value == true&& checked5.value==true
        }else{
            allchecked.value = false
        }
    }


    fun setCheck4(checked: Boolean){
        checked4.value = checked
        if (checked4.value ==true){
            allchecked.value = checked1.value == true && checked2.value==true && checked3.value==true && checked4.value==true&& checked5.value==true
        }else{
            allchecked.value = false
        }

    }
    fun setCheck5(checked: Boolean){
        checked5.value = checked
        if (checked5.value ==true){
            allchecked.value = checked1.value == true && checked2.value==true && checked3.value==true && checked4.value==true && checked5.value==true
        }else{
            allchecked.value = false
        }

    }
    fun GetAppVersion(application: MainApplication) {

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(HomerService::class.java).GetStoreappversion(
                "Android",
            )
            Log.d("테스트", "onResponse:2 ")
            d!!.enqueue(object : retrofit2.Callback<ResultVersionData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultVersionData>,
                    response: Response<ResultVersionData>
                ) {
                    var data = response.body()
                    Log.d("테스트", data.toString())
                    if (data != null) {
                        var type = data.type

                        if (type.equals("success")) {
                            Log.d("테스트", "onResponse:1 ")
                            var version = data.version
                            var nver =nowversion.value
                            var prvVersion = application.prvVersion
                            if(nver.equals(version)){
                                isupdate.value = false
                            }else{
                                if (version.equals(prvVersion)){
                                    isupdate.value = false
                                }else{
                                    isupdate.value = true
                                }
                            }

                        }
                    }

                }

                override fun onFailure(
                    call: retrofit2.Call<ResultVersionData>,
                    t: Throwable
                ) {
                    Log.d("check", t.toString())
                }

            })

        }
    }
    fun getSMS(u_phone: String, u_token: String,application: MainApplication ,type:String) {

        if (smscallRick.value == false){
            smscallRick.value = true
            certificationNumber2.value = "315314124125"

            CoroutineScope(Dispatchers.IO).launch {

                val d = NetworkManager.serveradapter.create(LoginService::class.java).getSMS(
                    u_phone,
                    hashKey.value,
                    u_token,
                    type,
                    true,

                )
                d!!.enqueue(object : retrofit2.Callback<ResultSMSData> {
                    override fun onResponse(
                        call: retrofit2.Call<ResultSMSData>,
                        response: Response<ResultSMSData>
                    ) {

                        var data = response.body()
                        if (data != null) {
                            if (data.type.equals("fail")){
                                checkLogin(u_phone,u_token,application)

                            }else if (data.type.equals("reconnect")){

                                toast.value = "연속으로 인증문자를 요청하였습니다. 연결상태를확인해주세요. 3분후에 다시시도해주세요."

                            }else{

                                certificationNumber2.value =data.number

                                checkLogin(u_phone,u_token,application)


                            }

//                                changeStateFlow(Constans.PHONE_LOGIN)


                            Log.d("setPassword", data.toString())

                        }
                        smscallRick.value = false
                    }

                    override fun onFailure(call: retrofit2.Call<ResultSMSData>, t: Throwable) {
                        Log.d("check", t.toString())
                        smscallRick.value = false
                    }

                })

            }
        }


    }


    fun checkLogin(u_phone: String, u_token: String ,application: MainApplication) {

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(LoginService::class.java).checkLogin(
                u_phone,

                u_token,
                true
            )
            d!!.enqueue(object : retrofit2.Callback<ResultLoginData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultLoginData>,
                    response: Response<ResultLoginData>
                ) {

                    var data = response.body()

                    if (data != null) {
                        if (data.message.equals("가입된 회원 정보가 없습니다")){
                            changeStateFlow(Constans.PHONE_PASS)
                        }else if (data.message.equals("승인 대기 상태입니다")){
//                            application.savePhoneNumber(u_phone)
//                            changeStateFlow(Constans.PHONE_WAITING)
                            getNewUserInformation(data.u_seq.toString(), application,Constans.PHONE_WAITING )
                        }else if (data.status.equals("success")){
//                            getSMS(number.value.toString(),application.push_token, application  )

//                            getNewUserInformation(data.u_seq.toString(), application  ,Constans.PHONE_COMPANION)
                            if (u_phone == "01189070101"||u_phone == "01092615611"||u_phone.startsWith("090")) {
                                changeStateFlow(Constans.PHONE_ADMIN_LOGIN)
                            }else{
                                changeStateFlow(Constans.PHONE_LOGIN)
                            }
                        }else if (data.message.equals("미승인 상태입니다")){
                            getNewUserInformation(data.u_seq.toString(), application  ,Constans.PHONE_COMPANION)
//                            changeStateFlow(Constans.PHONE_LOGIN)
//                            application.savePhoneNumber(u_phone)
//                            getToken(number.value.toString(),application.push_token, application)
//                            changeStateFlow(Constans.PHONE_COMPANION)
                        }else if(data.message.equals("퀵 회원입니다")){

                            u_admin.value = data.u_admin
                            getQuickUserInformation(application,data.u_seq.toString())
                        }else{

                        }

//                        Log.d("setPassword", data.message)

                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultLoginData>, t: Throwable) {
                    Log.d("check", t.toString())
                }

            })

        }

    }


    fun getQuickUserInformation(application: MainApplication,u_seq : String){

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(UserService::class.java).getQuickUserInformation(
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

                        changeStateFlow(Constans.PHONE_Q)
                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultUserInformationData>, t: Throwable) {
                    Log.d("check", t.toString())

                }

            })

        }

    }

    fun getToken(u_phone: String,u_token:String,application: MainApplication) {
        Log.d("체크 번호", u_token)
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(TokenService::class.java).getToken(u_phone,u_token
            )
            d!!.enqueue(object : retrofit2.Callback<ResultTokenData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultTokenData>,
                    response: Response<ResultTokenData>
                ) {

                    var data = response.body()
                    if (data != null) {
                        token = "Bearer "+response.body()?.token.toString()

                        var data = data.data
                        var user = data.user
                        application.user_u_uuid = data.user.u_uuid




                            if (data != null) {
//                            Log.d("test", user.toString())
//                            saveip(u_suq = user.u_seq,
//                                u_admin = user.u_admin,
//                                u_uuid = user.u_uuid,
//                                u_type = user.u_type,
//                                u_birthday = user.u_birthday,
//                                u_gender = user.u_gender,
//                                u_police = user.u_police
//                            , u_image = user.u_image
//                                )

                                if (data.user.u_police == 0){

                                    if (user.u_admin.equals("M")){
                                        getNewUserInformation(data.user.u_seq.toString(),application,Constans.PHONE_WAITING)

                                    }else if (user.u_admin.equals("Y")){

                                        getUserInformation(application,data.user.u_seq.toString(),u_phone)
                                    }else{
                                        getNewUserInformation(data.user.u_seq.toString(),application,Constans.PHONE_COMPANION)

                                    }
                                }else{


                                    changeStateFlow(Constans.PHONE_POLICE)

                                }

                            }




//                            )


                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultTokenData>, t: Throwable) {
                    Log.d("check", t.toString())
                }

            })

        }

    }

    fun getUserInformation(application: MainApplication,u_seq : String,u_phone: String){

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
                        if (data.rows != null){

                            application.userData = data.rows[0]
                            application.userCarData =data.car
                            if (u_phone == "01189070101"||u_phone == "01092615611"||u_phone.startsWith("090")){
                                changeStateFlow(Constans.PHONE_ADMIN_SUCCESS)
                            }else{
                                changeStateFlow(Constans.PHONE_SUCCESS)
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

    fun getNewUserInformation(u_seq : String,application: MainApplication,type:Int){

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



                        application.userData = data.rows[0]
                        application.userCarData =data.car
                        phoneNumber.value =data.rows[0].u_phone.toString()

                        changeStateFlow(type)



                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultUserInformationData>, t: Throwable) {
                    Log.d("check", t.toString())
                }

            })

        }

    }


}
