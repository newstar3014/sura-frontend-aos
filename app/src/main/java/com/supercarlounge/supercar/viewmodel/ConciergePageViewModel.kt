package com.supercarlounge.supercar.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.ConciergeService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class ConciergePageViewModel : ViewModel() {
    var type = MutableLiveData<String>("0")
    var size = MutableLiveData<String>("0")
    var bannerPosition= MutableLiveData<Int>(0)
    var conciergelist= MutableLiveData<ArrayList<ConciergeData>>(arrayListOf())


    fun getConcierge()  {
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(ConciergeService::class.java).getConcierge(type.value!!
            )
            d!!.enqueue(object : retrofit2.Callback<ResultConciergeData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultConciergeData>,
                    response: Response<ResultConciergeData>
                ) {
                    var data = response.body()
                    if (data != null) {

                        var data = data.rows


                        if (data != null) {
                            Log.d("getConcierge", data.toString())
                            conciergelist.value = data
                            size.value = "총 "+ data.size.toString()+"개"



                        }

                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultConciergeData>, t: Throwable) {
                    Log.d("check", t.toString())
                }

            })

        }
    }
}