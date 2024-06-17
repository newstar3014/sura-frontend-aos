package com.supercarlounge.supercar.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.HomerService
import com.supercarlounge.supercar.Network.LocationService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.Network.PostService
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.LocationMainPagerAdapter
import com.supercarlounge.supercar.adapter.LocationSpotAdapter
import com.supercarlounge.supercar.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response

class LocationMainViewModel : ViewModel() {
    var my_seq = MutableLiveData<String>("")
    var nottext = MutableLiveData<String>("게시글을 불러오는 중입니다.")
    var  nowtypeindex = MutableLiveData<Int>(0)
    var spotlist = MutableLiveData<ArrayList<LocationSpotData>>(ArrayList())
    var pagedatalist = MutableLiveData<ArrayList<LocationSlideData>>(ArrayList())
    var areacategorylist = MutableLiveData<ArrayList<LocationAreaCategoryData>>(ArrayList())
    var locationcategory = MutableLiveData<ArrayList<LocationCateData>>(ArrayList())
    var viewStack = MutableLiveData<Int>(1)
    var search_check = MutableLiveData<Boolean>(false)
    var anonymousForumNoitList : MutableLiveData<ArrayList<NotiData>> =  MutableLiveData<ArrayList<NotiData>>(arrayListOf())
    var apicall= MutableLiveData<Boolean>(false)
    var apicallspot= MutableLiveData<Boolean>(false)
    var ppp = MutableLiveData<Int>(10)
    var page = MutableLiveData<Int>(1)
    var driveWriting= MutableLiveData<Boolean>(false)
    var back= MutableLiveData<Boolean>(false)
    var regionEvent= MutableLiveData<Boolean>(false)
    var filterEvent= MutableLiveData<Boolean>(false)
    var regionIndex = MutableLiveData<Int>(0)
    var regionSelectDetailIndex = MutableLiveData<Int>(0)
    var pagerIndex= MutableLiveData<Int>(0)
    var tvregion = MutableLiveData<String>("전체")
    var tvSubregion = MutableLiveData<String>("")
    var viewType  = MutableLiveData<Int>(0)
    var tvhintVisibilty= MutableLiveData<Boolean>(false)
    var totalCount =  MutableLiveData<Int>(0)
    var help= MutableLiveData<Boolean>(false)
    var alim= MutableLiveData<Boolean>(false)
    var isalim = MutableLiveData<Boolean>(false)
    var arraytype = MutableLiveData<ArrayList<String>>(arrayListOf("전체","추천","카페","맛집","드라이브 코스","파인다이닝"))

    var order = MutableLiveData<String>("최신순")
    var typelist = MutableLiveData<ArrayList<String>>(arrayListOf())
    var payTag = MutableLiveData<String>("가격대 상관없음")
    var priceMin = MutableLiveData<Int>(0)
    var priceMax = MutableLiveData<Int>(20)

    var searchVaue = MutableLiveData<String>("")
    var isfilterset= MutableLiveData<Boolean>(false)
    fun InsertBookmark(detaildata:LocationSpotData) {

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(PostService::class.java)
                .InsertBookmark(
                    "SPOT",
                    my_seq.value.toString(),
                    detaildata.spot_seq.toString(),
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

    fun CheckAlim(u_email:String) {

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(HomerService::class.java).AlimReadCheck(
                my_seq.value!!
            )
            d!!.enqueue(object : retrofit2.Callback<ResultAlimCheckData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultAlimCheckData>,
                    response: Response<ResultAlimCheckData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if (type.equals("success")) {
                            var active = data.active
                            if (u_email.isNullOrEmpty()){
                                isalim.value = true
                            }else{
                                isalim.value = active

                            }

                        }
                    }

                }

                override fun onFailure(
                    call: retrofit2.Call<ResultAlimCheckData>,
                    t: Throwable
                ) {
                    Log.d("check", t.toString())
                }

            })

        }
    }
    fun driveWriting(d : Boolean){
        driveWriting.value = d
    }
    fun back(d : Boolean){
        back.value = d
    }
    fun help(d : Boolean){
        help.value = d
    }
    fun regionEvent(d : Boolean){
        regionEvent.value = d
    }
    fun filterEvent(d : Boolean){
        filterEvent.value = d
    }
    fun alim(d : Boolean){
        alim.value = d
    }
    init {
        var list:ArrayList<LocationCateData> = arrayListOf()
        list.add(LocationCateData("","전체", R.drawable.icon_location_cate1))
        list.add(LocationCateData("","북마크", R.drawable.icon_location_cate2))
        list.add(LocationCateData("","슈라의 추천", R.drawable.icon_location_cate3))
        list.add(LocationCateData("","드라이브 코스", R.drawable.icon_location_cate4))
        list.add(LocationCateData("","맛집,카페", R.drawable.icon_location_cate5))
        list.add(LocationCateData("","골프장(운동)", R.drawable.icon_location_cate6))
        list.add(LocationCateData("","차박,캠핑", R.drawable.icon_location_cate7))
        locationcategory.value = list
    }

    fun getAnonymousForumPostNoti(page:Int,ppp:Int){
        if (apicall.value ==false) {
            apicall.value = true
            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter!!.create(PostService::class.java)
                    .getAnonymousForumPostNoti(
                    )
                d!!.enqueue(object : retrofit2.Callback<ResultNotiData> {
                    override fun onResponse(
                        call: retrofit2.Call<ResultNotiData>,
                        response: Response<ResultNotiData>
                    ) {
                        var data = response.body()
                        if (data != null) {

                            var data = data.notice


                            if (data != null) {
                                anonymousForumNoitList.value = data

                            }

                        }
                        apicall.value = false
                    }

                    override fun onFailure(call: retrofit2.Call<ResultNotiData>, t: Throwable) {
                        Log.d("check", t.toString())
                        apicall.value = false
                    }

                })

            }
        }
    }

    fun getSpotList(isreset:Boolean, ){
        if (apicallspot.value ==false) {
            apicallspot.value = true
            var searchtype =  arraytype.value!![nowtypeindex.value!!]
            var choiceArea = ""
            var detailArea = ""
            if (tvregion.value?.contains(" ") == true ){
               var list = tvregion.value!!.split(" ")
                if (list.size> 1){

                    choiceArea = list[0]
                    if (list[1] == "전체"){
                        detailArea= ""
                    }else{
                        detailArea = list[1]
                    }

                }
            }else{
                choiceArea = tvregion.value.toString()
            }
            var paylist =""
            var count =0
            Log.d("테스트1111", choiceArea.toString())
            Log.d("테스트2222", detailArea.toString())
            Log.d("테스트3333", tvregion.value.toString())
            var list : ArrayList<String> = arrayListOf()
            if (typelist.value?.size ==1) {
                list!!.add(typelist.value!![0])
                list!!.add("")
            }else{
                typelist.value?.let { list.addAll(it) }

            }

            CoroutineScope(Dispatchers.IO).launch {
                Log.d("통신준비", "${searchVaue.value}\n${list}\n${priceMin.value}\n${priceMax.value}")

                val d = NetworkManager.serveradapter!!.create(LocationService::class.java)
                    .getSpotList(
                        page.value, ppp.value!!,my_seq.value!!,searchtype,order.value!!,searchVaue.value!!,choiceArea,detailArea,
//                        paylist,
                        "${priceMin.value}","${(priceMax.value!!*10000)}",list
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
                                Log.d("통신완료", "ㅇㅇ")
                                var rows = data.rows
                                if (data.totalCount != 0){
                                    totalCount.value = data.totalCount!!
                                }
                                if(isreset){
                                    spotlist.value = rows

                                }else {
                                    var nowlist = spotlist.value!!
                                    nowlist.addAll(rows)
                                    spotlist.value = nowlist

                                }
                            }else{
                                Log.d("통신완료", "실패1")
                            }


                        }
                        apicallspot.value = false
                    }

                    override fun onFailure(call: retrofit2.Call<ResultLocationSpotData>, t: Throwable) {
                        Log.d("check", t.toString())
                        apicallspot.value = false
                        Log.d("통신완료", "실패2")
                    }

                })

            }
        }
    }
    fun settype(index:Int){
//        nowtypeindex.value = index
//        resetpage()
//        getSpotList(true,spotadapter!!)
    }
    fun resetpage(){
        page.value = 1
    }
    fun requestbodyString(text: String): RequestBody {
        return text.toRequestBody("text/plain".toMediaType())
    }
    fun CheckFilterSet():Boolean {
        var typelistset = typelist.value!!
        var ptag =payTag.value!!
        if (typelistset.size != 0 || ptag.equals("가격대 상관없음") == false){
            return true
        }
        return false
    }
}
