package com.supercarlounge.supercar.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import java.time.LocalDate


class MyCarBestBoardViewModel : ViewModel() {

    var text = MutableLiveData<String>("?")
    var titleText = MutableLiveData<String>("HALL OF FAME")
    var nottext = MutableLiveData<String>("게시글을 불러오는 중입니다")
    var c_seq = MutableLiveData<String>("전체")
    var b_contents = MutableLiveData<String>("")
    var order = MutableLiveData<String>("")
    var bookmark = MutableLiveData<String>("")
    var my_user_data: MutableLiveData<UserInformationData> = MutableLiveData()
    var position = MutableLiveData<Int>(0)
    var gridOn = MutableLiveData<Boolean>(false)
    var search = MutableLiveData<String>("")
    var viewtype = MutableLiveData<String>("")
    var rvWidth = MutableLiveData<Int>(0)

    var tv1 = MutableLiveData<String>("인기글로 선정 된 게시글이 없습니다.")
    var tv2 = MutableLiveData<String>("인기글로 선정 된 게시글이 없습니다.")
    var tv3 = MutableLiveData<String>("인기글로 선정 된 게시글이 없습니다.")

    //공지
    var notipage = MutableLiveData<Int>(1)
    var notippp = MutableLiveData<Int>(3)
    var intent = MutableLiveData<Int>()
    //게시글
    var page = MutableLiveData<Int>(1)
    var ppp = MutableLiveData<Int>(3)
    var totalCount = MutableLiveData<Int>(0)
    var toast= MutableLiveData<String>("")


    var selectYear = MutableLiveData<Int>(0)
    var selectMonth = MutableLiveData<Int>(0)
    var selectDate = MutableLiveData<String>("")
    var selectDateString = MutableLiveData<String>("")
    var nowDate = MutableLiveData<String>("")
    var nowYear = MutableLiveData<Int>(0)
    var nowMonth = MutableLiveData<Int>(0)
    var startDate = MutableLiveData<String>("")
    var startYear = MutableLiveData<Int>(0)
    var startMonth = MutableLiveData<Int>(0)
    var startDateString = MutableLiveData<String>("")
    var myCarBoardLists: MutableLiveData<ArrayList<MyCarBoardData>> = MutableLiveData(arrayListOf())


    var issend = MutableLiveData<Boolean>(false)
    var apicall = MutableLiveData<Boolean>(false)



    var ProfileReadApiCall =  MutableLiveData<Boolean>(false)
    var boardCheckViewStatus = MutableLiveData<BoardCheckViewStatus>(BoardCheckViewStatus.DEFAULT)

    var select_sub_seq = MutableLiveData<Int>(0)
    var select_mmi_seq = MutableLiveData<Int>(0)
    var select_rn_nick = MutableLiveData<String>("")
    var mycardatelist: MutableLiveData<ArrayList<MyCarStartDateData>> = MutableLiveData<ArrayList<MyCarStartDateData>>(arrayListOf())
    var mycarselectdateindex = MutableLiveData<Int>(-1)
    var isnotheart = MutableLiveData<Boolean>(false)
    fun setIntent(i:Int){

        if(myCarBoardLists.value?.size!! > i){
            intent.value = i
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun nextMonth() {
       var nowindex:Int = mycarselectdateindex.value!!
        if (nowindex < mycardatelist.value!!.size-1) {
            mycarselectdateindex.value = nowindex!!+1
        }
        var seldate = mycardatelist.value!![mycarselectdateindex.value!!].mmi_date!!
        selectDate.value = seldate
        var datelist = seldate.split("-")
        selectDateString.value = "${datelist[0]}년 ${datelist[1]}월"

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun previousMonth() {
        var nowindex:Int = mycarselectdateindex.value!!
        if (nowindex > 0) {
            mycarselectdateindex.value = nowindex!!-1
        }
        var seldate = mycardatelist.value!![mycarselectdateindex.value!!].mmi_date!!
        selectDate.value = seldate
        var datelist = seldate.split("-")
        selectDateString.value = "${datelist[0]}년 ${datelist[1]}월"


    }
    fun getStartDate() {


            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter!!.create(MyCarBoardService::class.java)
                    .getStartDate(
                        my_user_data.value?.u_seq
                    )
                d!!.enqueue(object : retrofit2.Callback<ResultStartDate> {
                    override fun onResponse(
                        call: retrofit2.Call<ResultStartDate>,
                        response: Response<ResultStartDate>
                    ) {
                        var data = response.body()
                        if (data != null) {
                            var type = data.type
                            var message = data.message
                            if (data.rows.size != 0){
                                mycardatelist.value = data.rows
                                mycarselectdateindex.value = data.rows.size-1
                                var seldate = mycardatelist.value!![mycarselectdateindex.value!!].mmi_date!!
                                selectDate.value = seldate
                                var datelist = seldate.split("-")
                                selectDateString.value = "${datelist[0]}년 ${datelist[1]}월"
                                //startDateString.value  = data.rows [0].mmi_date
                            }else{
                                selectDateString.value = "랭킹 집계중"
                            }
                            Log.d("통신상태 결과", data.toString())


                        }

                    }

                    override fun onFailure(
                        call: retrofit2.Call<ResultStartDate>,
                        t: Throwable
                    ) {
                        Log.d("check", "failer")

                    }

                })

            }

    }
    fun getMyCarBestBoardList(reset: Boolean) {
        if (apicall.value == false) {
//          Log.d("getMyCarBoard", "call")
            apicall.value = true


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
                                page.value = 1
                                if (!reset) {
                                    myCarBoardLists.value?.addAll(rows)
                                    myCarBoardLists.value = myCarBoardLists.value
                                } else {
                                    myCarBoardLists.value = rows
                                }

                            }else{
                                myCarBoardLists.value = rows
                            }

                        }
                        apicall.value = false
                    }

                    override fun onFailure(
                        call: retrofit2.Call<ResultMyCaBoardData>,
                        t: Throwable
                    ) {
                        Log.d("check", "failer")
                        apicall.value = false
                    }

                })

            }
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
                                boardCheckViewStatus.value = BoardCheckViewStatus.PROFILE_WAIT_DIALOG
                            }else{
                                boardCheckViewStatus.value = BoardCheckViewStatus.PROFILE_SET
                            }
                        }else if(type.equals("friend")){
                            boardCheckViewStatus.value = BoardCheckViewStatus.FRIEND_POPUP
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
                    my_user_data.value?.u_seq.toString()
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

}