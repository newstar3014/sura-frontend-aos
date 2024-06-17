package com.supercarlounge.supercar.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.NetworkManager.gson
import com.supercarlounge.supercar.data.*
import com.google.gson.JsonObject
import com.supercarlounge.supercar.Network.*
import com.supercarlounge.supercar.enumset.BoardCheckViewStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class MyCarBoardViewModel : ViewModel() {

    var text = MutableLiveData<String>("?")
    var titleText = MutableLiveData<String>("인기글 선정 혜택")
    var nottext = MutableLiveData<String>("게시글을 불러오는 중입니다")
    var userdata = MutableLiveData<UserInformationData>()
    var c_seq = MutableLiveData<String>("전체")
    var b_contents = MutableLiveData<String>("")
    var order = MutableLiveData<String>("")
    var bookmark = MutableLiveData<String>("")
    var ProfileReadApiCall =  MutableLiveData<Boolean>(false)
    var toast = MutableLiveData<String>("")
    var gridOn = MutableLiveData<Boolean>(false)
    var search = MutableLiveData<String>("")
    var tv1 = MutableLiveData<String>("인기글로 선정 된 게시글이 없습니다.")
    var tv2 = MutableLiveData<String>("인기글로 선정 된 게시글이 없습니다.")
    var tv3 = MutableLiveData<String>("인기글로 선정 된 게시글이 없습니다.")
    var rvWidth = MutableLiveData<Int>(0)

    //공지
    var notipage = MutableLiveData<Int>(1)
    var notippp = MutableLiveData<Int>(15)

    //게시글
    var page = MutableLiveData<Int>(1)
    var ppp = MutableLiveData<Int>(15)
    var totalCount = MutableLiveData<Int>(0)
    var boardCheckViewStatus = MutableLiveData<BoardCheckViewStatus>(BoardCheckViewStatus.DEFAULT)
    var myCarBoardLists: MutableLiveData<ArrayList<MyCarBoardData>> = MutableLiveData(arrayListOf())
    var MyCarHotData:MutableLiveData<MyCarHotData> =MutableLiveData()
    var myCarBestBoardLists: MutableLiveData<ArrayList<MyCarBoardData>> = MutableLiveData(arrayListOf())
    var my_user_data: MutableLiveData<UserInformationData> = MutableLiveData()
    var issend = MutableLiveData<Boolean>(false)
    var apicall = MutableLiveData<Boolean>(false)
    var selectDate = MutableLiveData<String>("")
    var intent = MutableLiveData<Int>()

    var select_sub_seq = MutableLiveData<Int>(0)
    var select_mmi_seq = MutableLiveData<Int>(0)
    var select_rn_nick = MutableLiveData<String>("")
    var isnotheart = MutableLiveData<Boolean>(false)
    fun setIntent(i:Int){

        if(myCarBestBoardLists.value?.size!! > i){
            intent.value = i
        }

    }

    fun gridOnClick(onOff :Boolean){
        if (gridOn.value != onOff){
            gridOn.value = onOff
        }

    }
    fun bookmark(){
        if (bookmark.value.equals("")){
            bookmark.value="Y"
        }else{
            bookmark.value=""
        }
        getMyCarBoardList(true)
    }

    fun contextText(){
        var st = ""
        var search = search.value
        var bookmark = bookmark.value
        if (bookmark == "Y"){
           nottext.value = "북마크한 게시글이 없습니다"
        }else{
            if (search?.isNotEmpty() == true){
                nottext.value=    "검색 결과가 없습니다"
            }else{
                nottext.value=    "게시글을 불러오는 중입니다"
            }

        }


    }


    class Solution {
        fun solution(number: IntArray): Int {
            var answer: Int = 0


            return answer
        }
    }


    fun get(number:IntArray): Int{
        var answer = 0

        var numberSize = number.size -2
        var numberSize2 = number.size -1
        for ((count, i) in number.withIndex()){
        }
        for(i in 0..numberSize){

            for(j in 0..numberSize2){
                for(k in number){
                    if(number[i] + number[j] + number[k] == 0) {
                        answer++;
                    }
                }
            }
        }

        return answer
    }
    fun getMyCarBoardList(reset:Boolean) {
      if (apicall.value ==false) {
//          Log.d("getMyCarBoard", "call")
          apicall.value = true

          if (reset){
              page.value = 1
          }else{

              page.value =  page.value?.plus(1)
          }
          Log.d("페이지", page.value.toString())


        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(MyCarBoardService::class.java)
                .getMyCarBoardList(
                    page.value!!,
                    ppp.value!!,
                    userdata.value?.u_seq.toString(),
                    bookmark.value,
                    search.value
                )
            d!!.enqueue(object : retrofit2.Callback<ResultMyCaBoardData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultMyCaBoardData>,
                    response: Response<ResultMyCaBoardData>
                ) {
                    var data = response.body()
                    if (data != null) {
                                totalCount.value = data.totalCount
                        var rows = data.rows
                        if (rows != null) {

                            if (!reset){
                                myCarBoardLists.value?.addAll(rows)
                                myCarBoardLists.value = myCarBoardLists.value
                            }else{
                                myCarBoardLists.value = rows
                            }

                        }

                    }
                    apicall.value = false
                }

                override fun onFailure(call: retrofit2.Call<ResultMyCaBoardData>, t: Throwable) {
                    Log.d("check", "failer")
                    apicall.value = false
                }

            })

        }
            }


    }

    fun getMyCarBestBoardList() {



            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter!!.create(MyCarBoardService::class.java)
                    .getMyCarBestBoardList(
                        selectDate.value,
                        my_user_data.value?.u_seq,
                        page.value!!,
                        ppp.value!!,
                    )
                d!!.enqueue(object : retrofit2.Callback<ResultMyCaBoardData> {
                    override fun onResponse(
                        call: retrofit2.Call<ResultMyCaBoardData>,
                        response: Response<ResultMyCaBoardData>
                    ) {
                        var data = response.body()
                        if (data != null) {
//                                totalCount.value = data.totalCount
                            var rows = data.rows
                            if (rows != null) {
                                    myCarBestBoardLists.value = rows
                            }

                        }

                    }

                    override fun onFailure(
                        call: retrofit2.Call<ResultMyCaBoardData>,
                        t: Throwable
                    ) {
                        Log.d("check", "failer")

                    }

                })

            }



    }


    //프로필 열람
    fun ProfileRead(heart:Int,content: String) {

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(MyCarBoardService::class.java)
                .ProfileRead(select_mmi_seq.value.toString(),
                    my_user_data.value?.u_seq.toString(),select_sub_seq.value.toString(),heart.toString(),"MYCAR","프로필확인",content,select_rn_nick.value
                )
            d!!.enqueue(object : retrofit2.Callback<ResultProfileReadData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultProfileReadData>,
                    response: Response<ResultProfileReadData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if(type.equals("success")){
//                            toast.value = ""
                            ProfileReadApiCall.value = true
                        }else if(type.equals("notheart")){
                           isnotheart.value = true


                        }

                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<ResultProfileReadData>,
                    t: Throwable
                ) {
                    Log.d("check", "failer")
                }

            })

        }
    }

    fun boardViewCheckClick(){
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(PostService::class.java).boardViewCheck(
                select_mmi_seq.value.toString(),my_user_data.value?.u_seq.toString(),
                select_sub_seq.value.toString()
            )
            d!!.enqueue(object : retrofit2.Callback<ResultProfileReadCheck> {
                override fun onResponse(
                    call: retrofit2.Call<ResultProfileReadCheck>,
                    response: Response<ResultProfileReadCheck>
                ) {
                    var data = response.body()
                    if (data != null) {
                        Log.d("boardViewList", data.toString())
                        var type = data.type
                        if(type.equals("open")){
                            var pcok = data.pc_ok
                            if(pcok.equals("M")){
                                boardCheckViewStatus.value =BoardCheckViewStatus.PROFILE_WAIT_DIALOG
                            }else{
                                boardCheckViewStatus.value =BoardCheckViewStatus.PROFILE_SET
                            }
                        }else if(type.equals("friend")){
                            boardCheckViewStatus.value =BoardCheckViewStatus.FRIEND_POPUP
                        }else if(type.equals("company")){
                            boardCheckViewStatus.value =BoardCheckViewStatus.COMPANY_PROFILE
                        }else{
                            var phone = data.phone
                            FriendCheck(phone)
//                            viewlistCheck.value = false
                        }

                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultProfileReadCheck>, t: Throwable) {
                    Log.d("boardViewList", t.message!!)
                }

            })

        }
    }
    fun FriendCheck(phone: String) {

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(UserService::class.java)
                .FriendList(
                    userdata.value?.u_seq.toString()
                )
            d!!.enqueue(object : retrofit2.Callback<ResultFriendCheckData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultFriendCheckData>,
                    response: Response<ResultFriendCheckData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var message = data.message
                        if(message.equals("success")) {
                            var rows = data.rows
                            if (rows != null) {
                                if (rows.size != 0) {
                                    var phonelist = rows.get(0).pb_list
                                    if (phonelist.contains(phone)){
                                        boardCheckViewStatus.value = BoardCheckViewStatus.FRIEND_POPUP
                                    }else{
                                        boardCheckViewStatus.value = BoardCheckViewStatus.NOT_FRIEND_POPUP
                                    }
                                } else {
                                    boardCheckViewStatus.value = BoardCheckViewStatus.NOT_FRIEND_POPUP
                                }
                            }
                        }
                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<ResultFriendCheckData>,
                    t: Throwable
                ) {
                    Log.d("check", "failer")
                }

            })

        }
    }

    fun getMyCarHotList() {



        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(MyCarBoardService::class.java)
                .getMyCarHot()
            d!!.enqueue(object : retrofit2.Callback<ResultMyCarHotData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultMyCarHotData>,
                    response: Response<ResultMyCarHotData>
                ) {
                    var data = response.body()
                    if (data != null) {
//                                totalCount.value = data.totalCount
                        var rows = data.rows
                        if (rows != null) {
                            if (rows.size != 0){
                                MyCarHotData.value = rows.get(0)
                            }

                        }

                    }
                    apicall.value = false
                }

                override fun onFailure(
                    call: retrofit2.Call<ResultMyCarHotData>,
                    t: Throwable
                ) {
                    Log.d("check", "failer")
                    apicall.value = false
                }

            })

        }



    }
    fun LoadQuickmycarRank(){
        var list:ArrayList<MyCarBoardData> = arrayListOf()
        list.add(MyCarBoardData(-1,"최고의 슈퍼카,람보르기니...","","","","","바쁜 미키마우스",0,"MALE","https://supercarlounge.com/supercar_native/1.png",15,56,"2023-09-25 11:11:11","PORSCHE","https://supercarlounge.com:3000/images/car_brand/20211220052522porshe.png","","","","","Y",0,1,4,"","","",0,""))
        list.add(MyCarBoardData(-2,"영국의 슈퍼카 브랜드 애스턴마틴","","","","","귀여운 미키마우스",0,"FEMALE","https://supercarlounge.com/supercar_native/2.png",21,14,"2023-09-23 11:11:11","PORSCHE","https://supercarlounge.com:3000/images/car_brand/20211220032231lamborghini.png","","","","","Y",0,3,3,"","","",0,""))
        list.add(MyCarBoardData(-3,"많이 늦은 내차소","","","","","귀여운 미키마우스",0,"MALE","https://supercarlounge.com/supercar_native/3.png",30,14,"2023-09-24 11:11:11","FERRARI","https://supercarlounge.com:3000/images/car_brand/20211220033303ferrari.png","","","","","Y",0,10,6,"","","",0,""))
        myCarBestBoardLists.value = list
    }
    fun LoadQuickmycarlist(){
        var list:ArrayList<MyCarBoardData> = arrayListOf()
        list.add(MyCarBoardData(-1,"저의 차량 페라리 488을 소개합니다!","","","","","바쁜 미키마우스",0,"MALE","https://supercarlounge.com/supercar_native/Img_1.png",15,56,GetPrvTime(0,0,1),"PORSCHE","https://supercarlounge.com:3000/images/car_brand/20211220052522porshe.png","","","","","Y",0,1,4,"","","",0,""))
        list.add(MyCarBoardData(-2,"제가 요즘 아끼고 있는 최고의 슈퍼카,람보르기니를 소개합니다.","","","","","귀여운 미키마우스",0,"FEMALE","https://supercarlounge.com/supercar_native/Img_2.png",21,14,GetPrvTime(0,0,3),"PORSCHE","https://supercarlounge.com:3000/images/car_brand/20211220032231lamborghini.png","","","","","Y",0,3,3,"","","",0,""))
        list.add(MyCarBoardData(-3,"F8트리뷰토를 소개할게요!","","","","","귀여운 미키마우스",0,"MALE","https://supercarlounge.com/supercar_native/Img_3.png",30,14,GetPrvTime(0,0,5),"FERRARI","https://supercarlounge.com:3000/images/car_brand/20211220033303ferrari.png","","","","","Y",0,10,6,"","","",0,""))
        list.add(MyCarBoardData(-4,"저도 한번 올려요...슈퍼카는 아니지만!","","","","","열광적인 한스",0,"MALE","https://supercarlounge.com/supercar_native/Img_4.png",103,14,GetPrvTime(0,0,10),"BENZ","https://supercarlounge.com:3000/images/car_brand/20211220031730benz.png","","","","","Y",0,18,22,"","","",0,""))
        list.add(MyCarBoardData(-5,"M은 남자의 차라고 할수있죠","","","","","기쁜 모아나",0,"MALE","https://supercarlounge.com/supercar_native/Img_5.png",146,14,GetPrvTime(0,0,11),"BMW","https://supercarlounge.s3.ap-northeast-2.amazonaws.com/car_brand/1646895890636.png","","","","","Y",0,14,7,"","","",0,""))
        list.add(MyCarBoardData(-6,"맥라렌 570 코너링은 예술입니다","","","","","기쁜 모아나",0,"MALE","https://supercarlounge.com/supercar_native/Img_6.png",141,14,GetPrvTime(0,0,30),"MCLAREN","https://supercarlounge.s3.ap-northeast-2.amazonaws.com/car_brand/1646896055812.png","","","","","Y",0,3,10,"","","",0,""))
        if(gridOn.value!!) {
            list.add(
                MyCarBoardData(
                    -7,
                    "맥라렌 570 코너링은 예술입니다",
                    "",
                    "",
                    "",
                    "",
                    "기쁜 모아나",
                    0,
                    "MALE",
                    "https://supercarlounge.com/supercar_native/Img_7.png",
                    141,
                    14,
                    "2023-03-03 11:11:11",
                    "MCLAREN",
                    "https://supercarlounge.s3.ap-northeast-2.amazonaws.com/car_brand/1646896055812.png",
                    "",
                    "",
                    "",
                    "",
                    "Y",
                    0,
                    3,
                    10,
                    "",
                    "",
                    "",
                    0,
                    ""
                )
            )
            list.add(
                MyCarBoardData(
                    -8,
                    "맥라렌 570 코너링은 예술입니다",
                    "",
                    "",
                    "",
                    "",
                    "기쁜 모아나",
                    0,
                    "MALE",
                    "https://supercarlounge.com/supercar_native/Img_8.png",
                    141,
                    14,
                    "2023-03-03 11:11:11",
                    "MCLAREN",
                    "https://supercarlounge.s3.ap-northeast-2.amazonaws.com/car_brand/1646896055812.png",
                    "",
                    "",
                    "",
                    "",
                    "Y",
                    0,
                    3,
                    10,
                    "",
                    "",
                    "",
                    0,
                    ""
                )
            )
            list.add(
                MyCarBoardData(
                    -9,
                    "맥라렌 570 코너링은 예술입니다",
                    "",
                    "",
                    "",
                    "",
                    "기쁜 모아나",
                    0,
                    "MALE",
                    "https://supercarlounge.com/supercar_native/Img_9.png",
                    141,
                    14,
                    "2023-03-03 11:11:11",
                    "MCLAREN",
                    "https://supercarlounge.s3.ap-northeast-2.amazonaws.com/car_brand/1646896055812.png",
                    "",
                    "",
                    "",
                    "",
                    "Y",
                    0,
                    3,
                    10,
                    "",
                    "",
                    "",
                    0,
                    ""
                )
            )
            list.add(
                MyCarBoardData(
                    -10,
                    "맥라렌 570 코너링은 예술입니다",
                    "",
                    "",
                    "",
                    "",
                    "기쁜 모아나",
                    0,
                    "MALE",
                    "https://supercarlounge.com/supercar_native/Img_10.png",
                    141,
                    14,
                    "2023-03-03 11:11:11",
                    "MCLAREN",
                    "https://supercarlounge.s3.ap-northeast-2.amazonaws.com/car_brand/1646896055812.png",
                    "",
                    "",
                    "",
                    "",
                    "Y",
                    0,
                    3,
                    10,
                    "",
                    "",
                    "",
                    0,
                    ""
                )
            )
            list.add(
                MyCarBoardData(
                    -11,
                    "맥라렌 570 코너링은 예술입니다",
                    "",
                    "",
                    "",
                    "",
                    "기쁜 모아나",
                    0,
                    "MALE",
                    "https://supercarlounge.com/supercar_native/Img_11.png",
                    141,
                    14,
                    "2023-03-03 11:11:11",
                    "MCLAREN",
                    "https://supercarlounge.s3.ap-northeast-2.amazonaws.com/car_brand/1646896055812.png",
                    "",
                    "",
                    "",
                    "",
                    "Y",
                    0,
                    3,
                    10,
                    "",
                    "",
                    "",
                    0,
                    ""
                )
            )
            list.add(
                MyCarBoardData(
                    -12,
                    "맥라렌 570 코너링은 예술입니다",
                    "",
                    "",
                    "",
                    "",
                    "기쁜 모아나",
                    0,
                    "MALE",
                    "https://supercarlounge.com/supercar_native/Img_12.png",
                    141,
                    14,
                    "2023-03-03 11:11:11",
                    "MCLAREN",
                    "https://supercarlounge.s3.ap-northeast-2.amazonaws.com/car_brand/1646896055812.png",
                    "",
                    "",
                    "",
                    "",
                    "Y",
                    0,
                    3,
                    10,
                    "",
                    "",
                    "",
                    0,
                    ""
                )
            )
        }
        myCarBoardLists.value = list
    }
    fun GetPrvTime(date:Int,hour:Int,min:Int):String{
        // 현재 시간 가져오기
        val currentTime = Date()

        // Calendar 객체를 사용하여 현재 시간에서 1분 전의 시간 계산
        val calendar = Calendar.getInstance()
        calendar.time = currentTime
        if(date > 0) {
            calendar.add(Calendar.DAY_OF_MONTH, (-1*date))
        }
        if (hour > 0){
            calendar.add(Calendar.HOUR_OF_DAY, (-1*hour))
        }
        if (min > 0){
            calendar.add(Calendar.MINUTE, (-1*min))
        }

        val oneMinuteAgo = calendar.time

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var result = dateFormat.format(oneMinuteAgo)
        return  result
    }
}