package com.supercarlounge.supercar.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.MatchingService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.data.*
import com.supercarlounge.supercar.enumset.ProfileStatus
import com.google.gson.JsonObject
import com.supercarlounge.supercar.Network.CompanyService
import com.supercarlounge.supercar.Network.UserService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

class ProfileCompanyViewModel : ViewModel() {
    var my_seq=MutableLiveData<String>("")
    var myphone = MutableLiveData<String>("")
    var sub_seq=MutableLiveData<String>("")
    var titleText = MutableLiveData<String>("PASSENGER")
    var time= MutableLiveData<String>("")
    var mainText = MutableLiveData<String>("")
    var subText = MutableLiveData<String>("")
    var checked1 = MutableLiveData<Boolean>(false)
    var bottomBarText = MutableLiveData<String>("문의하기")
    var user_info:MutableLiveData<UserInfomationCompanyData>  = MutableLiveData()
    var sub_image:MutableLiveData<ArrayList<SubImage>>  = MutableLiveData()
    var open_type:MutableLiveData<Boolean>  = MutableLiveData(false)
    var toast = MutableLiveData<String>("")
    var apicall = MutableLiveData<Boolean>(false)
    var message = MutableLiveData<String>("")
    var mainCheckReturn   = MutableLiveData<String>("")
    var subCheckReturn1   = MutableLiveData<String>("")
    var subCheckReturn2  = MutableLiveData<String>("")
    var subCheckReturn3  = MutableLiveData<String>("")
    var type = MutableLiveData<String>("")
    var position =MutableLiveData<Int>(0)
    var islogcall = MutableLiveData<Boolean>(false)
    var subviewindex:MutableLiveData<Int>  = MutableLiveData(0)
    fun GetProfile(sub_seq:String){
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(CompanyService::class.java).getUserCompanyInfo(sub_seq)
            d!!.enqueue(object : retrofit2.Callback<ResultUserInfomationCompanyData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultUserInfomationCompanyData>,
                    response: Response<ResultUserInfomationCompanyData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if(type.equals("success")) {
                            var rows = data.rows
                            if (rows.size != 0) {
                                user_info.value = rows.get(0)
                                Log.d("가져온", rows.get(0).toString())
                                titleText.value = user_info.value!!.u_nickname!!
                                sub_image.value = user_info.value!!.subImage!!
                            }
                        }
                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultUserInfomationCompanyData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                }

            })

        }
    }

    fun InsertLog(){
        var my_seqt= my_seq.value
        var myphone = myphone.value!!
        var sub_seq = user_info!!.value!!.u_seq.toString()
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(CompanyService::class.java).InsertCompanyLog(myphone ,my_seqt,sub_seq!!)
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    var type = data!!.type
                    if(type.equals("success")){
                        islogcall.value = true
                    }else{

                    }
                }

                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                }

            })

        }
    }
    fun ChangeSubIndex(index:Int){
        if(sub_image.value!!.size > index) {
            subviewindex.value = index
        }
    }
}