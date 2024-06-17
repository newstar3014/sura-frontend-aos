package com.supercarlounge.supercar.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.Network.BannerService
import com.supercarlounge.supercar.Network.CategoryService
import com.supercarlounge.supercar.Network.DriveService
import com.supercarlounge.supercar.Network.LocationService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.Network.UserService
import com.supercarlounge.supercar.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class LocationFragmentViewModel : ViewModel() {
    var my_seq = MutableLiveData<String>("")
    var toast = MutableLiveData<String>("")

    var titleText = MutableLiveData<String>("LOUNGE")

    var viewStack = MutableLiveData<Int>(1)
    var invite_code = MutableLiveData<String>("")
    var inviteset =MutableLiveData<Boolean>(false)
    var apicallspot =MutableLiveData<Boolean>(false)
    var isPermission =MutableLiveData<Boolean>(false)
    var eventSearch = MutableLiveData<Boolean>()
    var order = MutableLiveData<String>("최신순")
    var widthPixels =MutableLiveData<Int>(0)
    var searchValue =MutableLiveData<String>("")

    var totalViewList = MutableLiveData<ArrayList<LocationSpotData>>(arrayListOf())
    var totalView_ppp= MutableLiveData<Int>(10)
    var totalView_page= MutableLiveData<Int>(1)


    var viewList = MutableLiveData<ArrayList<LocationSpotData>>(arrayListOf())
    var viewList_ppp= MutableLiveData<Int>(10)
    var viewList_page= MutableLiveData<Int>(1)


    var nearList = MutableLiveData<ArrayList<LocationSpotData>>(arrayListOf())
    var nearList_ppp= MutableLiveData<Int>(10)
    var nearList_page= MutableLiveData<Int>(1)


    fun eventSearch (boolean: Boolean){
        eventSearch.value = boolean
    }

    fun getTotalViewList(application: MainApplication) {
        var userData = application.userData
        var u_seq = userData?.u_seq.toString()
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(LocationService::class.java).getTotalViewList(
                u_seq,
                totalView_page.value,
                totalView_ppp.value
            )
            d!!.enqueue(object : retrofit2.Callback<ResultLocationSpotData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultLocationSpotData>,
                    response: Response<ResultLocationSpotData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var rows = data.rows
                        if (rows != null) {
                            if (rows.size != 0) {
                                totalViewList.value = rows
                                Log.d("로케이션 아이템", rows.size.toString())
                            }
                        }
                    }

                }

                override fun onFailure(
                    call: retrofit2.Call<ResultLocationSpotData>,
                    t: Throwable
                ) {
                    Log.d("check", t.toString())
                }

            })

        }

    }
    fun getViewList(application: MainApplication) {
        var userData = application.userData
        var u_seq = userData?.u_seq.toString()
            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter.create(LocationService::class.java).getViewList(
                    u_seq,
                    viewList_page.value,
                    viewList_ppp.value
                )
                d!!.enqueue(object : retrofit2.Callback<ResultLocationSpotData> {
                    override fun onResponse(
                        call: retrofit2.Call<ResultLocationSpotData>,
                        response: Response<ResultLocationSpotData>
                    ) {
                        var data = response.body()
                        if (data != null) {
                            var rows = data.rows
                            if (rows != null) {
                                if (rows.size != 0) {
                                    viewList.value = rows
                                    Log.d("로케이션 아이템", rows.size.toString())
                                }
                            }
                        }

                    }

                    override fun onFailure(
                        call: retrofit2.Call<ResultLocationSpotData>,
                        t: Throwable
                    ) {
                        Log.d("check", t.toString())
                    }

                })

            }

    }


    fun getNearList(isreset:Boolean ,application: MainApplication){
        Log.d("리스트", "getSpotList: ")
        if (apicallspot.value ==false) {
            apicallspot.value = true
            var userData =application.userData
            var u_seq = userData?.u_seq.toString()


            var location = "전체"
            if (isPermission.value == true){
                location = "내주변"
            }
            Log.d("로케이션 권한2", location)
            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter!!.create(LocationService::class.java)
                    .getSpotList(1, 10,u_seq,"전체",order.value!!,"",location,"",
//                        null,
                        "","",null)
                d!!.enqueue(object : retrofit2.Callback<ResultLocationSpotData> {
                    override fun onResponse(
                        call: retrofit2.Call<ResultLocationSpotData>,
                        response: Response<ResultLocationSpotData>
                    ) {
                        var data = response.body()
                        if (data != null) {
                            var type = data.type
                            var message = data.message
                            if(type.equals("success")){
                                var rows = data.rows
                                if(isreset){
                                    nearList.value = rows

                                }else {
                                    var nowlist = nearList.value!!
                                    nowlist.addAll(rows)
                                    nearList.value = nowlist
                                }
                            }else{

                            }


                        }
                        apicallspot.value = false
                    }

                    override fun onFailure(call: retrofit2.Call<ResultLocationSpotData>, t: Throwable) {
                        Log.d("check", t.toString())
                        apicallspot.value = false
                    }

                })

            }
        }
    }

}