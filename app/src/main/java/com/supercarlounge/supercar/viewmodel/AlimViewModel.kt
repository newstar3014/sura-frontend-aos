package com.supercarlounge.supercar.viewmodel

import android.content.Intent
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.supercarlounge.supercar.Network.*
import com.supercarlounge.supercar.data.*
import com.supercarlounge.supercar.ui.activity.AlimActivity
import com.supercarlounge.supercar.ui.activity.RecentDriveListActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.logging.Handler

class AlimViewModel : ViewModel() {
    var my_seq = MutableLiveData<String>("")
    var totalCount= MutableLiveData<Int>(0)
    //공지
    var notipage = MutableLiveData<Int>(1)
    var issend= MutableLiveData<Boolean>(false)
    var nowPage= MutableLiveData<Int>(1)
    //게시글
    var page = MutableLiveData<Int>(1)
    var ppp = MutableLiveData<Int>(15)
    var termsText = MutableLiveData<String>("알림")
    var alimlist: MutableLiveData<ArrayList<AlimData>> =  MutableLiveData<ArrayList<AlimData>>(arrayListOf())
    var udata:UserInformationData? = null
    var select_profile_seq = MutableLiveData<String>("")
    var select_board_data = MutableLiveData<BoardData>()
    var select_mycar_data = MutableLiveData<MyCarBoardData>()
    var anonymousForumNoitList : MutableLiveData<ArrayList<NotiData>> =  MutableLiveData<ArrayList<NotiData>>(arrayListOf())
    var anonymousForumList : MutableLiveData<ArrayList<BoardData>> =  MutableLiveData<ArrayList<BoardData>>(arrayListOf())
    var userCheck = MutableLiveData<Boolean>()
    var send_seq = MutableLiveData<String>("")
    var pa_seq = MutableLiveData<String>("")
    var pa_check = MutableLiveData<String>("")
    var screen = MutableLiveData<String>("")
    var toast = MutableLiveData<String>("")
    var email = MutableLiveData<String>("")
    var select_drive = MutableLiveData<String>("")
    var ispasscheck = MutableLiveData<String>("")
    var ismycarrank = MutableLiveData<Boolean>(false)
    var isHartShop = MutableLiveData<Boolean>(false)
    var isLocationDrive = MutableLiveData<Boolean>(false)
    var apicall= MutableLiveData<Boolean>(false)
    var b_contents = MutableLiveData<String>("")
    var order = MutableLiveData<String>("")
    var bookmark = MutableLiveData<String>("")
    var userdata = MutableLiveData<UserInformationData>()
    fun getalim(){
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(HomerService::class.java).getalim(my_seq.value!!)
            d!!.enqueue(object : retrofit2.Callback<ResultAlimData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultAlimData>,
                    response: Response<ResultAlimData>
                ) {
                    var data = response.body()
                    if (data != null) {

                        var rows = data.rows

                        Log.d("getAlim : rows  ", rows.toString())
                        if (rows != null) {
                            var list =rows
                            if(email.value!!.isEmpty()){
                                var alimData = AlimData("","",0,0,0,"","이메일을 입력해주세요.","","","",0)
                                list.add(0,alimData)
                            }

                           alimlist.value = list
                        }

                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultAlimData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                }

            })

        }
    }



    fun getMatchingDataYN(){
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(MatchingService::class.java).getMatchingDataYN(my_seq.value!!,send_seq.value)
            d!!.enqueue(object : retrofit2.Callback<ResultAlimYNData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultAlimYNData>,
                    response: Response<ResultAlimYNData>
                ) {
                    var data = response.body()
                    var message = data?.message
                    var pcDateCheck = data?.rows
                    if (data != null) {
                        Log.d("알림 테스트", data.toString())
                        if (message.equals("조회 되었습니다")){
                            if (pcDateCheck.equals("true")){
                                userOutCheck(send_seq.value.toString())
                            }else{
                                toast.value = "열람기간이 만료되었습니다"
                            }


                        }

                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultAlimYNData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                }

            })

        }
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

    fun getAnonymousForumPostScroll(c_seq:String) {
        issend.value = true

//        val boardArrayObject = JSONArray()
//        boardArrayObject.put("board", jsonObjectList)
//        Log.d("뭐지", boardlist.toString())
//        val body = gson.toJson(boardlist)
        val jsonObjectList = JsonObject()
        val board = JsonObject()
        page.value = page.value?.plus(1)
        board.addProperty("u_seq", userdata.value?.u_seq)
        board.addProperty("c_seq", c_seq)
        board.addProperty("b_contents",b_contents.value)
        board.addProperty("bookmark", bookmark.value)
        board.addProperty("page", page.value!!)
        board.addProperty("ppp", ppp.value!!)
        board.addProperty("order", order.value!!)
        // Log.d("로그", "getAnonymousForumPostScroll:  bookmark = ${bookmark.value} ,order : ${order.value} ")


        val boardlist = JsonObject()
        boardlist.add("board", board)

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(PostService::class.java).getAnonymousForumPostScroll(boardlist)
            d!!.enqueue(object : retrofit2.Callback<ResultBoardData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultBoardData>,
                    response: Response<ResultBoardData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        totalCount.value = data.totalCount
                        var rows = data.rows
                        if (rows != null) {

//                            if (viewType.value == 1) {
//                                testList.value = rows
//                            }else{
                            var nlist =   anonymousForumList.value
                            nlist!!.addAll(rows)
                            anonymousForumList.value = nlist!!

//                            }
//                            anonymousForumList.value?.addAll(rows)

                        }else{
                            var list : ArrayList<BoardData> = arrayListOf()
//                            anonymousForumList.value =list
                        }

                    }else{
                        var list : ArrayList<BoardData> = arrayListOf()
//                        anonymousForumList.value =list
                    }
                    issend.value = false
                }

                override fun onFailure(call: retrofit2.Call<ResultBoardData>, t: Throwable) {
                    Log.d("check", "failer")
                    issend.value = false
                }

            })

        }


    }
    fun alldelete(){

        if (alimlist.value?.size!! == 0 ){
            toast.value = "삭제할 알림내역이 없습니다."
        }else{
            if(alimlist.value?.size!! > 0){
                if (alimlist.value?.size == 1&&alimlist.value!![0].pa_body == "이메일을 입력해주세요."){
                    toast.value = "삭제할 알림내역이 없습니다."
                }else{
                    var my_seq = my_seq.value
                    CoroutineScope(Dispatchers.IO).launch {
                        val d = NetworkManager.serveradapter!!.create(HomerService::class.java).allimDelete(my_seq)
                        d!!.enqueue(object : retrofit2.Callback<ResultData> {
                            override fun onResponse(
                                call: retrofit2.Call<ResultData>,
                                response: Response<ResultData>
                            ) {
                                var data = response.body()
                                if (data != null) {
                                    var type =data.type
                                    if(type.equals("success")){

                                            getalim()


                                        toast.value = "알림이 전부 확인 되었습니다."
                                    }else if (type.equals("notexist")){
                                        toast.value = "확인 할 알람이 없습니다"
                                    }

                                }

                            }

                            override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                                Log.d("failer", t.message!!)
                            }

                        })

                    }
                }

            }

        }

    }

    fun AlimRead(pa_seq:String,vtype:String,seq:String){
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(HomerService::class.java).AlimRead(pa_seq)
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type =data.type
                        if(type.equals("success")){
                            if(vtype.equals("BoardDetail")){
                                getBoard(seq)
                            }else if(vtype.equals("ApplyProfile")){

                                select_profile_seq.value = seq
                            }else if (vtype.equals("MycarDetail")){
                                getOneMycarData(seq)
                            }else if (vtype.equals("DriveLike")){
                                DrivePassCheck(seq)
                            }else if (vtype.equals("MatchingManage") || vtype.equals("ApplyProfile") ) {
                                getMatchingDataYN()
                            }else if (vtype.equals("MycarRankingHeart")){
                                ismycarrank.value = true
                            }else if (vtype.equals("DrivePass30Buy") ||vtype.equals("DrivePass1Buy") ||vtype.equals("HeartCharge") ||vtype.equals("DrivePass1Date")||vtype.equals("DrivePass3Date")){
                                isHartShop.value = true
                            }else if (vtype.equals("LocationDrive")){
                                isLocationDrive.value = true
                            }
                        }else{

                        }

                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                }

            })

        }
    }

    fun getBoard(b_seq:String){
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(PostService::class.java).getOneBoard(b_seq)
            d!!.enqueue(object : retrofit2.Callback<ResultOneBoardData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultOneBoardData>,
                    response: Response<ResultOneBoardData>
                ) {
                    var data = response.body()


                    if (data != null) {
                        var type = data?.type
                        var rows = data.rows
                        if(type.equals("notexist")){
                            getalim()
                            toast.value = "삭제된 게시글 입니다"
                        }else{
                            if (rows != null) {
                                select_board_data.value = rows
                            }
                        }
                       // Log.d("getAlim : rows.size  ", rows.size.toString())


                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultOneBoardData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                }

            })

        }
    }

    fun userOutCheck(u_seq:String){
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(UserService::class.java).outCheck(u_seq )
            d!!.enqueue(object : retrofit2.Callback<ResultUserCheckData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultUserCheckData>,
                    response: Response<ResultUserCheckData>
                ) {
                    var data = response.body()
                    if (data != null) {

                        userCheck.value = data.isBe


                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultUserCheckData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                }

            })

        }
    }
    fun getOneMycarData(mmi_seq:String) {

        var my_seq = my_seq.value
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(MyCarBoardService::class.java)
                .getMycarOne(mmi_seq.toString(), my_seq.toString())
            d!!.enqueue(object : retrofit2.Callback<ResultMyCaBoardData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultMyCaBoardData>,
                    response: Response<ResultMyCaBoardData>
                ) {
                    var data = response.body()
                    Log.d("check", data.toString())
                    if (data != null) {

                        var type = data.type
                        if (type.equals("success")) {
                            var rows = data.rows
                            if (rows.size != 0) {
                                var data = rows.get(0)
                              select_mycar_data .value = data
                            }else{
                                toast.value = "해당 게시글이 존재하지 않습니다."
                            }
                        } else {
                            toast.value = "잠시 후 다시 시도해주세요."
                        }

                    }


                }

                override fun onFailure(call: retrofit2.Call<ResultMyCaBoardData>, t: Throwable) {
                    Log.d("check", "failer")
                    toast.value = "잠시 후 다시 시도해주세요."


                }

            })

        }



    }
    fun DrivePassCheck(b_seq:String) {
        var intent: Intent? = null
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(MatchingService::class.java).DrivePassCheck(my_seq.value.toString())
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if (type.equals("success")){
                            select_drive.value = b_seq
                            ispasscheck.value = "success"

                        }else{
                            select_drive.value = b_seq
                            ispasscheck.value = "fail"
                        }





                    }

                }

                override fun onFailure(
                    call: retrofit2.Call<ResultData>,
                    t: Throwable
                ) {
                    Log.d("check", t.toString())
                }

            })

        }
    }
}
