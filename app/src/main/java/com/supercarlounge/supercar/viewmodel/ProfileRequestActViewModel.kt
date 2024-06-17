package com.supercarlounge.supercar.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.MatchingService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.Network.PasswordService
import com.supercarlounge.supercar.data.ProfileRequestData
import com.supercarlounge.supercar.data.ResultData
import com.supercarlounge.supercar.data.ResultProfileRequestData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class ProfileRequestActViewModel : ViewModel() {
    var my_seq = MutableLiveData<String>("")
    var titleText = MutableLiveData<String>("?")
    var ischeck = MutableLiveData<Boolean>(false)
    var requestlist:MutableLiveData<ArrayList<ProfileRequestData>> = MutableLiveData(arrayListOf())
    var delindex =MutableLiveData<Int>()
    var toast = MutableLiveData<String>()
    var rec_profile_update = MutableLiveData<Boolean>()
    var isrec_click = MutableLiveData<Boolean>(false)
    fun getrequest(){
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(MatchingService::class.java).GetProfileRequest(my_seq.value,"","")
            d!!.enqueue(object : retrofit2.Callback<ResultProfileRequestData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultProfileRequestData>,
                    response: Response<ResultProfileRequestData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var message = data.message
                        if(message.equals("success")) {
                            var rows = data.rows
                            if (rows.size != 0) {
                                requestlist.value = rows
                            }
                        }
                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultProfileRequestData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                }

            })

        }
    }

    fun delete_request(pc_seq:String,target_seq:String,sub_seq:String,index:Int){
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(MatchingService::class.java).DeleteRequest(pc_seq,target_seq,sub_seq)
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if(type.equals("success")) {
                            delindex.value = index
                            toast.value = "요청이 삭제되었습니다."
                        }else{
                            toast.value = "요청삭제에 실패하였습니다."
                        }
                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                }

            })

        }
    }
    fun setcheck(){
        ischeck.value = !ischeck.value!!
        checkoffrec()
    }
    fun setrecoff(){
        isrec_click.value =true
    }
    fun checkoffrec(){

        if(ischeck.value!!){
            setrecoff()
        }else{
            onRecChecked(true)
        }
    }
    fun onRecChecked(boolean: Boolean){

        ischeck.value =boolean
        var rec  = ""
        if (ischeck.value == true){
            rec = "u_feed = \"1\""
        }else{
            rec = "u_feed = \"0\""
        }

        my_seq.value?.let { setRecProfile(it,rec) }
    }
    fun setRecProfile(u_seq: String, updateStr: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(PasswordService::class.java).setPushAndLockScreen(
                u_seq,
                updateStr,
            )
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        if(updateStr.contains("1")){
                            rec_profile_update.value = true
                        }else{
                            rec_profile_update.value =false
                        }

                        Log.d("setPushAndLockScreen", "$u_seq: $data.message")
                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Log.d("check", t.toString())
                }

            })

        }

    }

}
