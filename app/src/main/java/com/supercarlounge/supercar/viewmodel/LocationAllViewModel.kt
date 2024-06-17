package com.supercarlounge.supercar.viewmodel

import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.DriveService
import com.supercarlounge.supercar.Network.LocationService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.data.DriveAwayData
import com.supercarlounge.supercar.data.LocationSpotData
import com.supercarlounge.supercar.data.ResultDriveData
import com.supercarlounge.supercar.data.ResultLocationSpotData
import com.supercarlounge.supercar.databinding.FragmentDriveAwayNewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class LocationAllViewModel : ViewModel() {
    var rvItemType = MutableLiveData<Int>(0)
    var totalCount= MutableLiveData<Int>(0)
    var page= MutableLiveData<Int>(1)
    var ppp= MutableLiveData<Int>(15)

    val mDriveList: MutableLiveData<ArrayList<DriveAwayData>> = MutableLiveData(arrayListOf())
    var issend= MutableLiveData<Boolean>(false)
    var count = MutableLiveData<Int>(2)
    val mVipDriveList: MutableLiveData<ArrayList<DriveAwayData>> = MutableLiveData(arrayListOf())
    var isBoardOne =MutableLiveData<Boolean>(false)
    var intentActivityDriveWayListEvent =MutableLiveData<Boolean>(false)
    var vipItemIndex = MutableLiveData<Int>()
    var awayItemIndex = MutableLiveData<Int>()
    var my_seq =MutableLiveData<String>()
    var apicallspot = MutableLiveData<Boolean>(false)
    var order = MutableLiveData<String>("최신순")
    var spotlist = MutableLiveData<ArrayList<LocationSpotData>>(ArrayList())
    var isPermission =MutableLiveData<Boolean>(false)
    fun intentActivityDriveWayList(intent :Boolean){
        intentActivityDriveWayListEvent.value = intent
    }


    fun vipItemOnClick(index : Int){
        vipItemIndex.value = index
    }
    fun awayItemIndexOnClick(index: Int){
        awayItemIndex.value = index
    }



    fun getSpotList(isreset:Boolean,choice:String){
        Log.d("리스트", "getSpotList: ")
        if (apicallspot.value ==false) {
            apicallspot.value = true
            var location = "전체"
            if (isPermission.value == true){
                location = "내주변"
            }
            Log.d("로케이션 권한1", choice)
            Log.d("로케이션 권한2", location)
            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter!!.create(LocationService::class.java)
                    .getSpotList(page.value, ppp.value!!,my_seq.value!!,choice,order.value!!,"",location,"",
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
                                Log.d("로케이션 사이즈", rows.size.toString())
                                if(isreset){
                                    spotlist.value = rows
                                }else {
                                    var nowlist = spotlist.value!!
                                    nowlist.addAll(rows)
                                    spotlist.value = nowlist
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
