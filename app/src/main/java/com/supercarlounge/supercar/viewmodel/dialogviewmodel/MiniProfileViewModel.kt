package com.supercarlounge.supercar.viewmodel.dialogviewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.Network.UserService
import com.supercarlounge.supercar.data.ResultUserInformationData
import com.supercarlounge.supercar.data.UserInformationCarData
import com.supercarlounge.supercar.data.UserInformationData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MiniProfileViewModel : ViewModel() {
    var tv_ok = MutableLiveData<String>("")
    var tv_cancel = MutableLiveData<String>("")
    var tv_sub = MutableLiveData<String>("")
    var my_seq = MutableLiveData<String>("")
    var sub_seq = MutableLiveData<String>("")
    var u_lat = MutableLiveData<String>("0")
    var u_lon = MutableLiveData<String>("0")
    var checked1 = MutableLiveData<Boolean>(false)
    var checked2 = MutableLiveData<Boolean>(false)
    var user_info:MutableLiveData<UserInformationData> = MutableLiveData()
    var usercar:MutableLiveData<ArrayList<UserInformationCarData>> = MutableLiveData()
    var charmlist = MutableLiveData<ArrayList<String>>()
    var personerlist = MutableLiveData<ArrayList<String>>()
    var musictypelist = MutableLiveData<ArrayList<String>>()
    var anonymousNickname = MutableLiveData<String>("")
    fun getUserInformation(){
        val userselect = JsonObject()
        userselect.addProperty("u_seq", my_seq.value)
        userselect.addProperty("sub_seq", sub_seq.value)
        userselect.addProperty("u_lat",u_lat.value)
        userselect.addProperty("u_lon", u_lon.value)


        val userObj = JsonObject()
        userObj.add("userObj", userselect)
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(UserService::class.java).getminiprofile(userObj
            )
            d!!.enqueue(object : retrofit2.Callback<ResultUserInformationData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultUserInformationData>,
                    response: Response<ResultUserInformationData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if(type.equals("success")) {
                            var rows = data.rows
                            var car = data.car
                            if (rows.size != 0) {
                                Log.d("테스트", rows.get(0).toString())
                                user_info.value = rows.get(0)
                                charmlist.value = user_info.value!!.GetCharmlist()
                               // personerlist.value = user_info.value!!.GetPersonerlist()
                                musictypelist.value = user_info.value!!.GetMusiclist()
                                usercar.value =car.get(0)
                                rows.get(0).u_charm
                            }
                        }
                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultUserInformationData>, t: Throwable) {
                    Log.d("check", t.toString())
                    Log.d("태그1", "333")
                }

            })

        }

    }
}