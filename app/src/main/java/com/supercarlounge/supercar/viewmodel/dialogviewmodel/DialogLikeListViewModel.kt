package com.supercarlounge.supercar.viewmodel.dialogviewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.Network.PostService
import com.supercarlounge.supercar.data.LikeListData
import com.supercarlounge.supercar.data.ResultLikeListData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class DialogLikeListViewModel : ViewModel() {

    var tv_ok = MutableLiveData<String>("")
    var tv_cancel = MutableLiveData<String>("")

    var my_u_seq = MutableLiveData<String>("")
    var u_seq = MutableLiveData<String>("")
    var b_seq =  MutableLiveData<String>("")
    var ppp = MutableLiveData<Int>(5)
    var page = MutableLiveData<Int>(1)

    var tv_sub = MutableLiveData<String>("이 드라이브글을\n차단하시겠습니까?")

    var viewtype = MutableLiveData<Int>(0)
    var checked = MutableLiveData<Boolean>(false)
    val likeList = ArrayList<LikeListData>()
    private val _LikeList = MutableLiveData<ArrayList<LikeListData>>()
    var mLikeList : MutableLiveData<ArrayList<LikeListData>> = _LikeList

    init {
        _LikeList.value = likeList
    }
    fun onChecked(boolean: Boolean){
        checked.value = boolean
    }


    fun getLikeList() {

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(PostService::class.java).getLikeList(b_seq.value,ppp.value!!,page.value)
            d!!.enqueue(object : retrofit2.Callback<ResultLikeListData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultLikeListData>,
                    response: Response<ResultLikeListData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var rows = data.rows
                        if (rows != null) {

                            mLikeList.value =rows
                            Log.d("getLikeList", mLikeList.toString())
                        }else{
                            var list : ArrayList<LikeListData> = arrayListOf()
                            mLikeList.value =list

                        }

                    }else{
                        var list : ArrayList<LikeListData> = arrayListOf()
                        mLikeList.value =list

                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultLikeListData>, t: Throwable) {
                    Log.d("getLikeList", "failer")
                }

            })

        }


    }
}