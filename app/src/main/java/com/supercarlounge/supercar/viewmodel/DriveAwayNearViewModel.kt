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

class DriveAwayNearViewModel : ViewModel() {
    var rvItemType = MutableLiveData<Int>(0)
    var totalCount= MutableLiveData<Int>(0)
    var page= MutableLiveData<Int>(1)
    var ppp= MutableLiveData<Int>(15)
    val mDriveList: MutableLiveData<ArrayList<DriveAwayData>> = MutableLiveData(arrayListOf())
    var my_lat:MutableLiveData<String> = MutableLiveData("0")
    var my_lon:MutableLiveData<String> = MutableLiveData("0")
    var issend= MutableLiveData<Boolean>(false)
    var count = MutableLiveData<Int>(0)

    var isPermission=MutableLiveData<Boolean>(false)
    val mVipDriveList: MutableLiveData<ArrayList<DriveAwayData>> = MutableLiveData(arrayListOf())
    var isBoardOne =MutableLiveData<Boolean>(false)
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




    fun getDrive(u_seq:String,isreset:Boolean){
        issend.value = true
        Log.d("getDrive : page ", page.value.toString())
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(DriveService::class.java).getDrive("근처",u_seq,ppp.value,page.value,my_lat.value!!,my_lon.value
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

//                            mVipDriveList.value!!.add(
//                                DriveAwayData(1,"","","",1,1,"","","https://i.ibb.co/7nQkmmT/drive-sample1.png","https://i.ibb.co/7nQkmmT/drive-sample1.png","시내를 고속도로 처럼 달리고 싶은이에요.","시내를 고속도로 처럼 달리고 싶은이에요.",0,0,"",0,"","","","",null
//                                    ,0,"","","","",0,"11111","https://supercarlounge.com:3002/images/car_brand/20211220031730benz.png","MALE","","",0,0,"",0,0,0)
//                            )
//                            mVipDriveList.value!!.add(
//                                DriveAwayData(2,"","","",1,1,"","","https://supercarlounge.com:3002/images/temp2/drive_sample2.png","https://supercarlounge.com:3002/images/temp2/drive_sample2.png","아우디 차주분들 같이 달려요! 오늘 밤..","아우디 차주분들 같이 달려요! 오늘 밤..",0,0,"",0,"","","","",null
//                                    ,0,"","","","",0,"22222","https://supercarlounge.s3.ap-northeast-2.amazonaws.com/car_brand/1646895890636.png","","","",0,0,"",0,0,0)
//                            )
//                            mVipDriveList.value!!.add(
//                                DriveAwayData(3,"","","",1,1,"","","https://supercarlounge.com:3002/images/temp2/drive_sample3.png","https://supercarlounge.com:3002/images/temp2/drive_sample3.png","오늘도 나를위해 오늘 저녁도 달립니다.","오늘도 나를위해 오늘 저녁도 달립니다.",0,0,"",0,"","","","",null
//                                    ,0,"","","","",0,"","https://supercarlounge.s3.ap-northeast-2.amazonaws.com/car_brand/1646895904160.png","MALE","","",0,0,"",0,0,0)
//                            )
//                            mVipDriveList.value!!.add(
//                                DriveAwayData(4,"","","",1,1,"","","https://supercarlounge.com:3002/images/temp2/drive_sample4.png","https://supercarlounge.com:3002/images/temp2/drive_sample4.png","시내도로를 고속도로 처럼 달리고 싶은날","시내도로를 고속도로 처럼 달리고 싶은날",0,0,"",0,"","","","",null
//                                    ,0,"","","","",0,"444444","https://supercarlounge.com:3002/images/car_brand/20211220031814fox.png","","","",0,0,"",0,0,0)
//                            )
//                            mVipDriveList.value!!.add(
//                                DriveAwayData(5,"","","",1,1,"","","https://supercarlounge.com:3002/images/temp2/drive_sample5.png","https://supercarlounge.com:3002/images/temp2/drive_sample5.png","ㅘㅘㅘ하하핳하하하","시내도로를 고속도로 처럼 달리고 싶은날",0,0,"",0,"","","","",null
//                                    ,0,"","","","",0,"55555","https://supercarlounge.com:3002/images/car_brand/20211220031814fox.png","","","",0,0,"",0,0,0)
//                            )

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
