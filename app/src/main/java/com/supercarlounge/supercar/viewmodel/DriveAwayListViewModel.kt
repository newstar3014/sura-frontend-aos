package com.supercarlounge.supercar.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.DriveService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.data.DriveAwayData
import com.supercarlounge.supercar.data.ResultDriveData
import com.supercarlounge.supercar.databinding.ActivityDriveAwayListBinding
import com.supercarlounge.supercar.databinding.FragmentDriveAwayNewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class DriveAwayListViewModel : ViewModel() {
    var rvItemType = MutableLiveData<Int>(0)
    var totalCount= MutableLiveData<Int>(0)
    var page= MutableLiveData<Int>(1)
    var ppp= MutableLiveData<Int>(15)
    var my_seq= MutableLiveData<String>()
    var toast = MutableLiveData<String>()
    var isfinish = MutableLiveData<Boolean>()
    var title =MutableLiveData<String>()

    val mDriveList: MutableLiveData<ArrayList<DriveAwayData>> = MutableLiveData(arrayListOf())
    var issend= MutableLiveData<Boolean>(false)
    var count = MutableLiveData<Int>(0)
    val mVipDriveList: MutableLiveData<ArrayList<DriveAwayData>> = MutableLiveData(arrayListOf())
    var isBoardOne =MutableLiveData<Boolean>(false)
    var choice = MutableLiveData<String>("신규")


    fun isFinish(intent :Boolean){
        isfinish.value = intent
    }

    fun getDrive(u_seq:String,isreset:Boolean,binding:   ActivityDriveAwayListBinding){
        issend.value = true
        Log.d("스크롤홈확인용3 : last",choice.value.toString() )

        var list :ArrayList<DriveAwayData> = arrayListOf()
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(DriveService::class.java).getDrive(choice.value,u_seq,ppp.value,page.value,"0","0"
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
                                }else {
                                    var listset = mDriveList.value
                                    listset!!.addAll(rows)
                                    mDriveList.value = listset!!


                                }

                        }

                    }

                    Log.d("스크롤홈확인용3 : last","로딩 멈춤" )
                    issend.value = false
                }

                override fun onFailure(call: retrofit2.Call<ResultDriveData>, t: Throwable) {
                    Log.d("check", "failer")
                    issend.value = false
                }

            })

        }
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

                            count.value = mVipDriveList.value!!.size
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
