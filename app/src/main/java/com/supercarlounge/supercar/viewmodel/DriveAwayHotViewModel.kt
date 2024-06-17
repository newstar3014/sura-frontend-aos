package com.supercarlounge.supercar.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.DriveService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.data.DriveAwayData
import com.supercarlounge.supercar.data.ResultDriveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class DriveAwayHotViewModel : ViewModel() {
    var rvItemType = MutableLiveData<Int>(0)
    var totalCount= MutableLiveData<Int>(0)
    var page= MutableLiveData<Int>(1)
    var ppp= MutableLiveData<Int>(15)
    var count = MutableLiveData<Int>(2)
    val mDriveList: MutableLiveData<ArrayList<DriveAwayData>> = MutableLiveData(arrayListOf())
    var issend= MutableLiveData<Boolean>(false)
    var isBoardOne =MutableLiveData<Boolean>(false)
    val mVipDriveList: MutableLiveData<ArrayList<DriveAwayData>> = MutableLiveData(arrayListOf())
        var intentActivityDriveWayListEvent =MutableLiveData<Boolean>(false)
    var vipItemIndex = MutableLiveData<Int>()
    var awayItemIndex = MutableLiveData<Int>()
    fun intentActivityDriveWayList(intent :Boolean){
        intentActivityDriveWayListEvent.value = intent
    }


    fun vipItemOnClick(index : Int){
        vipItemIndex.value = index
    }
    fun awayItemIndexOnClick(index: Int){
        awayItemIndex.value = index
    }

    fun getDrive(u_seq:String,isreset:Boolean) : ArrayList<DriveAwayData> {
        issend.value =true
        var list :ArrayList<DriveAwayData> = arrayListOf()
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(DriveService::class.java).getDrive("인기",u_seq,ppp.value,page.value,"0","0"
            )
            d!!.enqueue(object : retrofit2.Callback<ResultDriveData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultDriveData>,
                    response: Response<ResultDriveData>
                ) {
                    var data = response.body()
                    if (data != null) {

                        var rows = data.drives
                        totalCount.value = data.totalCount
                        Log.d("getDrive : totalCount ", totalCount.value.toString())
                        Log.d("getDrive : rows.size  ", rows.size.toString())
                        if (rows != null) {
                            if(isreset){
                                mDriveList.value = rows
                                page.value  = 1
                            }else {
                                var listset = mDriveList.value
                                    listset!!.addAll(rows)
                                    mDriveList.value = listset!!


                            }
                        }
                    }
                    issend.value =false
                }

                override fun onFailure(call: retrofit2.Call<ResultDriveData>, t: Throwable) {
                    Log.d("check", "failer")
                    issend.value =false
                }

            })

        }


        return list
    }


    fun getVipDrive(my_seq :String) {
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(DriveService::class.java).getVipDrive(
                my_seq, "0", "0"
            )
            d!!.enqueue(object : retrofit2.Callback<ResultDriveData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultDriveData>,
                    response: Response<ResultDriveData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var rows = data.drives
                        if (rows != null) {
                            mVipDriveList.value = rows


                            mVipDriveList.value =mVipDriveList.value
                            count.value = mVipDriveList.value!!.size
                            Log.d("VIP승주", "통신: 갯수 = ${count.value} ")
                        }

                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultDriveData>, t: Throwable) {
                    Log.d("check", "failer")
                }

            })

        }
    }
    fun resetdata(){
       var dataset = mDriveList.value!!
        dataset.clear()
        mDriveList .value= dataset

    }
}
