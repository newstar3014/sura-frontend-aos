package com.supercarlounge.supercar.viewmodel.dialogviewmodel

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

class BottomSheetRegionDailogViewModel : ViewModel() {
    var my_seq = MutableLiveData<String>("")
    var totalCount= MutableLiveData<Int>(0)
    var termsText = MutableLiveData<String>("알림")
    var alimlist: MutableLiveData<ArrayList<AlimData>> =  MutableLiveData<ArrayList<AlimData>>(arrayListOf())
    var udata:UserInformationData? = null
    var select_profile_seq = MutableLiveData<String>("")
    var select_board_data = MutableLiveData<BoardData>()
    var select_mycar_data = MutableLiveData<MyCarBoardData>()
    var eventAroundMe=MutableLiveData<Boolean>(false)
    var regionSelectIndex = MutableLiveData<Int>(0)
    var priceMin = MutableLiveData<Int>(0)
    var priceMax = MutableLiveData<Int>(20)
    var regionSelectDetailIndex = MutableLiveData<Int>(0)
    var regionIndex = MutableLiveData<Int>(0)
    var seoulIndex = MutableLiveData<Int>(0)
    var gyeonggidoIndex = MutableLiveData<Int>(0)
    var anonymousForumList : MutableLiveData<ArrayList<BoardData>> =  MutableLiveData<ArrayList<BoardData>>(arrayListOf())

    var priceRange = MutableLiveData<String>("")

    var regionArray =  MutableLiveData<ArrayList<String>>(arrayListOf("전체","서울","경기","인천","강원도","대전","세종","충청도","대구","전라도","경상도","울산","부산","제주"))
    var seoulRegionArray  =  MutableLiveData<ArrayList<String>>(arrayListOf("강남구","강동구","강북구","강서구","관악구","광진구","구로구","금천구",
        "노원구","도봉구","동대문구","동작구","마포구","서대문구","서초구","성동구","성북구","송파구","양천구","영등포구","용산구","은평구","종로구","중구","중량구","서울 전체"))

    var gyeonggidoRegionArray  =  MutableLiveData<ArrayList<String>>(arrayListOf("안양시","의왕시","과천시","군포시","안산시","시흥시","부천시","광명시"
        ,"성남시","용인시","광주시","구리시","남양주시","하남시","오산시","화성시","수원시","평택시","안성시","여주시","양평군","이천시","고양시","김포시","파주시","동두천시","양주시","의정부시","연천군","포천시","가평군","경기 전체"))

    var seoulBooleanArray =  MutableLiveData<ArrayList<Boolean>>(arrayListOf(false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false))
    var gyeonggidoBooleanArray =  MutableLiveData<ArrayList<Boolean>>(arrayListOf(false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false))




    var issend= MutableLiveData<Boolean>(false)
    var regionBooleanArray =  MutableLiveData<ArrayList<Boolean>>(arrayListOf(false,false,false,false,false,false,false,false,false,false,false,false,false,false,false))
    var okEvent = MutableLiveData<Boolean>(false)
    var isPermission = MutableLiveData<Boolean>(false)
    var permissionEvent = MutableLiveData<Boolean>(false)
    var okReset =MutableLiveData<Boolean>(false)
    var sortIndex = MutableLiveData<Int>(0)
    var categoryIndex = MutableLiveData<Int>(0)
    var category2Text = MutableLiveData<String>("")
    var tagArray =MutableLiveData<ArrayList<String>>(arrayListOf())
    var sortArray  =  MutableLiveData<ArrayList<String>>(arrayListOf("추천순","최신순","인기순",))
    var categoryArray  =  MutableLiveData<ArrayList<String>>(arrayListOf("북마크","심야","슈퍼카 주차 가능",))
    var category2Array  =  MutableLiveData<ArrayList<String>>(arrayListOf("만원 미만","최대 10만원","가격대 상관없음",))
    var sortBooleanArray =  MutableLiveData<ArrayList<Boolean>>(arrayListOf(false,false,false))
    var categoryBooleanArray =  MutableLiveData<ArrayList<Boolean>>(arrayListOf(false,false,false))
    var category2BooleanArray =  MutableLiveData<ArrayList<Boolean>>(arrayListOf(false,false,false))
    var locationOk =MutableLiveData<Boolean>(false)
    fun eventAroundMe(boolean: Boolean){

        if (isPermission.value == true){
            eventAroundMe.value = boolean

            if (boolean){
                locationOk.value = true
            }

        }else{
            if (eventAroundMe.value== false) {
                permissionEvent.value = boolean

            }else{

            }
        }

    }
    fun okEvent(boolean: Boolean){
        okEvent.value = boolean
    }
fun okReset(boolean: Boolean){
    okReset.value = boolean
}
    fun sortEvent(index :Int){

            for ((count, i) in sortBooleanArray.value!!.withIndex()){
                sortBooleanArray.value!![count] = false
            }
            sortBooleanArray.value?.set(index,true)
            sortBooleanArray.value = sortBooleanArray.value
            sortIndex.value = index


    }
    fun categoryEvent(index :Int){

            categoryBooleanArray.value?.set(index, !categoryBooleanArray.value!![index])
            categoryBooleanArray.value = categoryBooleanArray.value
            categoryIndex.value = index

    }
    fun category2Event(index :Int){
        for ((count, i) in category2BooleanArray.value!!.withIndex()){
            category2BooleanArray.value!![count] = false
        }
        category2BooleanArray.value?.set(index,true)
        category2BooleanArray.value = category2BooleanArray.value
    }
    fun buttonEvent(index :Int){

        if (index == 1 && isPermission.value ==false){
            permissionEvent.value =true

        }else{
            for ((count, i) in regionBooleanArray.value!!.withIndex()){
                regionBooleanArray.value!![count] = false
            }
            regionBooleanArray.value?.set(index,true)
            regionIndex.value = index
            if (index !=2){
                buttonSeoulEvent(0)

            }else if (index !=3){
                buttonGyeonggidoEvent(0)
            }

            regionBooleanArray.value = regionBooleanArray.value
        }



    }
    fun buttonSeoulEvent(index :Int){
            for ((count, i) in seoulBooleanArray.value!!.withIndex()){
                seoulBooleanArray.value!![count] = false
            }
            seoulBooleanArray.value?.set(index,true)
            seoulIndex.value = index

            seoulBooleanArray.value = seoulBooleanArray.value
    }

    fun buttonGyeonggidoEvent(index :Int){
        for ((count, i) in gyeonggidoBooleanArray.value!!.withIndex()){
            gyeonggidoBooleanArray.value!![count] = false
        }
        gyeonggidoBooleanArray.value?.set(index,true)
        gyeonggidoIndex.value = index

        gyeonggidoBooleanArray.value = gyeonggidoBooleanArray.value

    }
}
