package com.supercarlounge.supercar.viewmodel

import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.supercarlounge.supercar.Network.*
import com.supercarlounge.supercar.data.*
import com.supercarlounge.supercar.ui.activity.RecentDriveListActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class ImageViewModel : ViewModel() {
    var my_seq = MutableLiveData<String>("")
    var totalCount= MutableLiveData<Int>(0)
    //공지
    var notipage = MutableLiveData<Int>(1)
    var issend= MutableLiveData<Boolean>(false)
    var nowPage= MutableLiveData<Int>(1)
    //게시글
    var page = MutableLiveData<Int>(1)
    var ppp = MutableLiveData<Int>(15)
    var rvWidth = MutableLiveData<Int>(0)
    var imagelist: MutableLiveData<ArrayList<SampleImageData>> =  MutableLiveData<ArrayList<SampleImageData>>(arrayListOf())
    var select_profile_seq = MutableLiveData<String>("")

    var userdata = MutableLiveData<UserInformationData>()

    fun getImage(){
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(HomerService::class.java).defaultImage("1","100")
            d!!.enqueue(object : retrofit2.Callback<ResultImageData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultImageData>,
                    response: Response<ResultImageData>
                ) {
                    var data = response.body()
                    if (data != null) {

                        var rows = data.rows

                        Log.d("getImage : rows  ", rows.toString())
                        if (rows != null) {
                            imagelist.value = rows
                        }

                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultImageData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                }

            })

        }
    }

}
