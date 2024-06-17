package com.supercarlounge.supercar.viewmodel.dialogviewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.HomerService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.data.AllianceData
import com.supercarlounge.supercar.data.ResultAllianceData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class DialogConCiergeServiceViewModel : ViewModel() {

    var tv_ok = MutableLiveData<String>("")
    var tv_cancel = MutableLiveData<String>("")
    var alliancelist = MutableLiveData<ArrayList<AllianceData>>(arrayListOf())
    fun GetConciergeServicePage() {

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(HomerService::class.java).getallience(
            )
            d!!.enqueue(object : retrofit2.Callback<ResultAllianceData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultAllianceData>,
                    response: Response<ResultAllianceData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if(type.equals("success")) {
                           var rows = data.rows
                            if(rows!=null){
                                if(rows.size!=0){
                                    alliancelist.value = rows

                                }
                            }
                        }
                    }

                }

                override fun onFailure(
                    call: retrofit2.Call<ResultAllianceData>,
                    t: Throwable
                ) {
                    Log.d("check", t.toString())
                }

            })

        }
    }
}