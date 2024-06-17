package com.supercarlounge.supercar.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.HomerService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.Network.UserService
import com.supercarlounge.supercar.data.ResultData
import com.supercarlounge.supercar.data.ResultTermData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class ServiceCenterViewModel : ViewModel() {


    var titleText = MutableLiveData<String>("SUPERCAR LOUNGE")
    var titleText2 = MutableLiveData<String>("VIP DRIVE")
    var titleText3 = MutableLiveData<String>("DRIVE AWAY")
    var isopen_service = MutableLiveData<Boolean>(false)
    var isopen_location_term = MutableLiveData<Boolean>(false)
    var isopen_privacy_term = MutableLiveData<Boolean>(false)
    var isopen_teenager_term = MutableLiveData<Boolean>(false)
    var isopen_organizer_info = MutableLiveData<Boolean>(false)
    var checkSpace = MutableLiveData<Boolean>(true)
    var service_term = MutableLiveData<String>("")
    var location_term = MutableLiveData<String>("")
    var privacy_term = MutableLiveData<String>("")
    var teenager_term = MutableLiveData<String>("")
    var organizer_info = MutableLiveData<String>("")
    var my_seq = MutableLiveData<String>("")
    var toast = MutableLiveData<String>("")
    var bubbleRick =MutableLiveData<Boolean>(false)
    fun setopen_service(){
        var location = isopen_service.value!!
        if(location){
            isopen_service.value = false
            checkSpace.value = true
        }else{
            isopen_service.value = true
            isopen_location_term.value = false
            isopen_privacy_term.value = false
            isopen_teenager_term.value = false
            isopen_organizer_info.value = false
            checkSpace.value = false

        }
    }
    fun setopen_location(){
        var location = isopen_location_term.value!!
        if(location){
            isopen_location_term.value = false
            checkSpace.value = true
        }else{
            isopen_service.value = false
            isopen_location_term.value = true
            isopen_privacy_term.value = false
            isopen_teenager_term.value = false
            isopen_organizer_info.value = false
            checkSpace.value = false

        }
    }
    fun setopen_teenager(){
        var location = isopen_teenager_term.value!!
        if(location){
            isopen_teenager_term.value = false
            checkSpace.value = true
        }else{
            isopen_service.value = false
            isopen_location_term.value = false
            isopen_privacy_term.value = false
            isopen_teenager_term.value = true
            isopen_organizer_info.value = false
            checkSpace.value = false

        }
    }
    fun setopen_privacy(){
        var location = isopen_privacy_term.value!!
        if(location){
            isopen_privacy_term.value = false
            checkSpace.value = true
        }else{
            isopen_service.value = false
            isopen_location_term.value = false
            isopen_privacy_term.value = true
            isopen_teenager_term.value = false
            isopen_organizer_info.value = false
            checkSpace.value = false
        }
    }

    fun setopen_organizer(){
        var location = isopen_organizer_info.value!!
        if(location){
            isopen_organizer_info.value = false
            checkSpace.value = true
        }else{
            isopen_service.value = false
            isopen_location_term.value = false
            isopen_privacy_term.value = false
            isopen_teenager_term.value = false
            isopen_organizer_info.value = true
            checkSpace.value = false

        }
    }


    fun setSecession(checked:Boolean,text:String){
        if (bubbleRick.value == false){
            bubbleRick.value = true

        var now = if (checked) "1" else "0"
        Log.d("체크탈퇴", my_seq.value.toString()+":"+checked.toString()+":"+text)
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(UserService::class.java).setSecession(my_seq.value,text,now)
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        Log.d("탈퇴사유", data.toString())


                    }
                    bubbleRick.value = false
                }

                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Log.d("check", "failer")
                    bubbleRick.value = false
                }

            })
        }
        }
    }
    fun GetTerm(){
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
                            if(rows!=null){
                                if(rows.size != 0){
                                    for(termset in rows){
                                        var vtype = termset.type
                                        var message =termset.content
                                       if(vtype.contains("개인정보")){
                                            privacy_term.postValue(message)
                                        }else if(vtype.contains("위치기반")){
                                            location_term.postValue(message)
                                        }else if(vtype.contains("사업자")){
                                            organizer_info.postValue(message)
                                        }
                                        else if(vtype.contains("청소년")){
                                            teenager_term.postValue(message)
                                        }
                                       else if(vtype.contains("이용약관")){
                                           service_term.postValue(message)
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