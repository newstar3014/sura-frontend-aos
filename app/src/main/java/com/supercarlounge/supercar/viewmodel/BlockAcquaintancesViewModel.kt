package com.supercarlounge.supercar.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.HomerService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.Network.UserService
import com.supercarlounge.supercar.data.ResultData
import com.supercarlounge.supercar.data.ResultFriendCheckData
import com.supercarlounge.supercar.data.ResultTermData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class BlockAcquaintancesViewModel : ViewModel() {
    var my_seq = MutableLiveData<String>("")
    var titleText = MutableLiveData<String>("")
    var time = MutableLiveData<String>("")
    var mainText = MutableLiveData<String>("")
    var subText = MutableLiveData<String>("")
    var termsText = MutableLiveData<String>("")
    var checked = MutableLiveData<Boolean>(false)
    var apicall = MutableLiveData<Boolean>(false)
    var SetList = MutableLiveData<ArrayList<String>>(arrayListOf())
    var NowList = MutableLiveData<ArrayList<String>>(arrayListOf())

    var AllContacts = MutableLiveData<String>("+0")
    var Setcontacts = MutableLiveData<String>("+0")
    var sendon = MutableLiveData<Boolean>(false)
    var toast = MutableLiveData<String>("")
    fun onChecked() {
        var checkset = checked.value!!
        if (checkset) {
            checked.value = false
        } else {
            checked.value = true
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
                            if(rows!=null) {
                                if (rows.size != 0) {
                                    for (termset in rows) {
                                        var vtype = termset.type
                                        var message = termset.content
                                            if (vtype.contains("슈퍼카라운지 이용약관")) {
                                                termsText.postValue(message)
                                                break
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
    fun FriendList() {
        apicall.value = true
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(UserService::class.java)
                .FriendList(
                    my_seq.value
                )
            d!!.enqueue(object : retrofit2.Callback<ResultFriendCheckData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultFriendCheckData>,
                    response: Response<ResultFriendCheckData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var message = data.message
                        if (message.equals("success")) {
                            var rows = data.rows
                            if (rows != null) {
                                if (rows.size != 0) {
                                    var phonelist = rows.get(0).pb_list
                                    var data = phonelist.replace("\"", "").replace("[", "").replace("]", "")
                                    var datalist = data.split(",")
                                    var array:ArrayList<String> = arrayListOf()
                                    array.addAll(datalist)
                                    NowList.value = array
                                    AllContacts.value = "+"+array.size.toString()

                                }
                            }
                        }
                    }
                    apicall.value = false
                }

                override fun onFailure(
                    call: retrofit2.Call<ResultFriendCheckData>,
                    t: Throwable
                ) {
                    apicall.value = false
                    Log.d("check", "failer")
                }

            })

        }
    }
    fun SendFriend() {
        if (apicall.value == false) {
            var nlist = SetList.value!!
            // nlist!!.addAll(SetList.value!!)
            var set: ArrayList<String> = arrayListOf()
            for (i in nlist) {
                var ff = "\\\"" + i + "\\\""
                set.add(ff)
            }
            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter!!.create(UserService::class.java)
                    .SendFriendPhone(
                        my_seq.value,
                        set.toString()
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
                                toast.value = data.message
                                FriendList()
                            }
                            Log.d("SendFriend", data.toString())
                        }
                    }

                    override fun onFailure(
                        call: retrofit2.Call<ResultData>,
                        t: Throwable
                    ) {
                        Log.d("check", "failer")
                    }

                })

            }
        }else{
            toast.value = "연락처를 불러온 뒤 등록을 해주세요"
        }
    }
    fun SendPhoneNum() {
        if (checked.value!!) {
            sendon.value = true
        } else {
            toast.value = "약관을 동의해주세요."
        }
    }
}