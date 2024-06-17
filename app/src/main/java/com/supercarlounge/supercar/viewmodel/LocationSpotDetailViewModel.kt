package com.supercarlounge.supercar.viewmodel

import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.DriveService
import com.supercarlounge.supercar.Network.LocationService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.Network.PostService
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.DriveAwayData
import com.supercarlounge.supercar.data.LocationAreaCategoryData
import com.supercarlounge.supercar.data.LocationCateData
import com.supercarlounge.supercar.data.LocationSpotData
import com.supercarlounge.supercar.data.LocationSlideData
import com.supercarlounge.supercar.data.LocationSpotDescData
import com.supercarlounge.supercar.data.NotiData
import com.supercarlounge.supercar.data.ResultData
import com.supercarlounge.supercar.data.ResultDriveData
import com.supercarlounge.supercar.data.ResultLocationSpotData
import com.supercarlounge.supercar.data.ResultNotiData
import com.supercarlounge.supercar.databinding.*
import com.supercarlounge.supercar.enumset.DialogBallonType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class LocationSpotDetailViewModel : ViewModel() {
    var my_seq = MutableLiveData<String>("")
    var spotseq = MutableLiveData<String>("")
    var detaildata = MutableLiveData<LocationSpotData>()
    var descdata = MutableLiveData<ArrayList<LocationSpotDescData>>(arrayListOf())
    var menudata = MutableLiveData<ArrayList<LocationSpotDescData>>(arrayListOf())
    var  titleText  = MutableLiveData<String>("")
    var bookmark = MutableLiveData<Boolean>(false)
    var location_seq = MutableLiveData<String>("")
    var apicallspot= MutableLiveData<Boolean>(false)
    var  subimagelist  = MutableLiveData<ArrayList<String>>(arrayListOf())
    var position = MutableLiveData<Int>(0)
    var  toast  = MutableLiveData<String>("")
    var drivelist = MutableLiveData<ArrayList<DriveAwayData>>(arrayListOf())
    var totalCountDrive = MutableLiveData<Int>(10)
    var totalPageDrive = MutableLiveData<Int>(1)
    var recentspotlist = MutableLiveData<ArrayList<LocationSpotData>>(arrayListOf())
    var tvBoockCount = MutableLiveData<String>("북마크 0")
    var boockCount = MutableLiveData<Int?>(0)
    var tvmore = MutableLiveData<String>("")
    var eventMore = MutableLiveData<Boolean>(false)
    var tvEtc= MutableLiveData<String>("관련 드라이브 게시글 0")


    fun eventMore(boolean: Boolean){
        eventMore.value = boolean
    }
    fun  getSpotDetail(){
        if (apicallspot.value ==false) {
            apicallspot.value = true
            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter!!.create(LocationService::class.java)
                    .getOneSpot(
                     my_seq.value!!,spotseq.value
                    )
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
                                if (rows.size != 0) {
                                    detaildata.value = rows[0]
                                    titleText.value = rows[0].spot_title!!
                                    var image =  rows[0].spot_sub_image
                                    image?.add(0,rows[0].spot_main_image!!)
                                    subimagelist.value = image!!
                                    position.value = 0

                                    var list1 = rows[0].spot_desc!!
                                    var list2 = rows[0].spot_menu!!
                                    var resultList1 = arrayListOf<LocationSpotDescData>()
                                    var resultList2 = arrayListOf<LocationSpotDescData>()
                                    for (i in list1){
                                        if (i.title?.isNotEmpty() == true &&i.content?.isNotEmpty() == true){
                                            resultList1.add(i)
                                        }
                                    }
                                    for (i in list2){
                                        if (i.title?.isNotEmpty() == true &&i.content?.isNotEmpty() == true){
                                            resultList2.add(i)
                                        }
                                    }
                                    descdata.value = resultList1

                                    menudata.value = resultList2

                                    Log.d("데이터", rows.toString())
                                    Log.d("데이터1", descdata.value.toString())
                                    Log.d("데이터2", menudata.value.toString())
                                    bookmark.value = rows[0].bookmarkCheck.equals("Y")
                                    location_seq.value = rows[0].spot_seq!!
                                    tvBoockCount.value = "북마크 ${rows[0].bookCount.toString()}"
                                    boockCount.value =rows[0].bookCount
                                    getDrive()
                                    getSpotList()
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

    fun getDrive(){

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(DriveService::class.java).tagDriveList(my_seq.value,"","",10,1,location_seq.value
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

                            drivelist.value  = rows

                        }

                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultDriveData>, t: Throwable) {
                    Log.d("check", "failer")

                }

            })

        }
    }

    fun InsertBookmark() {

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(PostService::class.java)
                .InsertBookmark(
                    "SPOT",
                    my_seq.value.toString(),
                    detaildata.value!!.spot_seq.toString(),
                )
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()

                    if (data != null) {
                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<ResultData>,
                    t: Throwable
                ) {
                    Log.d("onFailure", t.message.toString())
                }

            })

        }


    }

    fun getSpotList(){
//        if (apicallspot.value ==false) {
//            apicallspot.value = true

            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter!!.create(LocationService::class.java)
                    .getRecentSpotList(
                        1, 30,detaildata?.value!!.spot_seq
                    )
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
                                recentspotlist.value = rows
                                tvEtc.value = "관련 드라이브 게시글 ${rows.size}"
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
