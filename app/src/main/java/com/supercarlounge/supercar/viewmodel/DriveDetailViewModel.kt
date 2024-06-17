package com.supercarlounge.supercar.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.*
import com.supercarlounge.supercar.data.*
import com.supercarlounge.supercar.enumset.DrivePopMenuStatus
import com.google.gson.JsonObject
import com.supercarlounge.supercar.enumset.PidDialogType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class DriveDetailViewModel() : ViewModel() {
    var titleText = MutableLiveData<String>("DRIVE AWAY")
    var checked1 = MutableLiveData<Boolean>(false)
    var my_seq = MutableLiveData("")
    var drivelist:MutableLiveData<ArrayList<DriveAwayData>> = MutableLiveData(arrayListOf())
    var isvip = MutableLiveData<Boolean>(false)
    var type = MutableLiveData<String>("신규")
    var totalCount = MutableLiveData<Int>(0)
    var page = MutableLiveData<Int>(1)
    var ppp = MutableLiveData<Int>(10)
    var my_lat = MutableLiveData<String>("0")
    var my_lon = MutableLiveData<String>("0")
    var toast = MutableLiveData<String>("")
    var editdata = MutableLiveData<DriveAwayData>()
    var hintText = MutableLiveData<String>("드라이브 신청 후 프로필 확인이 가능합니다.")
    var editindex = MutableLiveData<Int>()
    var isedit = MutableLiveData<Boolean>()
    var drivePopMenuStatus = MutableLiveData<DrivePopMenuStatus>(DrivePopMenuStatus.DEFAULT)
    var feed_search_seq = MutableLiveData<String>("")
    var readlist = MutableLiveData<ArrayList<String>>(arrayListOf())
    var issend= MutableLiveData<Boolean>(false)
    var driveType = MutableLiveData<String>("")
    var deleteCall = MutableLiveData<Boolean>(false)
    var apicall =MutableLiveData<Boolean>(false)
    var bubbleRick = MutableLiveData<Boolean>(false)
    var isnotheart = MutableLiveData<Boolean>(false)
    fun getDrive(isreset:Boolean){
        issend.value =true
        Log.d("getDrive : page ", page.value.toString())
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(DriveService::class.java).getDrive(type.value,my_seq.value!!,ppp.value,page.value,my_lat.value!!,my_lon.value
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
                        Log.d("받은 데이터2", data.toString())
                        if (rows != null) {

                            if(isreset){
                                drivelist.value = rows
                            }else {
                                var listset = drivelist.value
                                listset!!.addAll(rows)
                                drivelist.value = listset!!


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
    }
    fun likedrive(b_seq:Int,ischeck:Boolean,position:Int){
        var islike = if (ischeck) "1" else "0"
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(DriveService::class.java).LikeDrive(my_seq.value!!,b_seq.toString(),"DRIVE",islike)
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                      var   type = data.type
                        var message =data.message
                        if(type.equals("success")){
                            if(message == null){
                               var setyn  ="0"
                                var dd = drivelist.value!!.get(position)
                                dd.like_check = setyn
                                drivelist.value!!.set(position, dd)
                                editdata.value = dd
                                editindex.value = position
                                toast.value ="해제"
                            }else {
                                if (!message.contains("중복")) {
                                    var setyn = "1"
                                    var dd = drivelist.value!!.get(position)
                                    dd.like_check = setyn
                                    drivelist.value!!.set(position, dd)
                                    editdata.value = dd
                                    editindex.value = position
                                    toast.value ="좋아요"
                                }else{
                                    toast.value ="중복"
                                }
                            }
                            Log.d("LIKERESULT","TRUE")
                        }else{
                            Log.d("LIKERESULT","FALSE")
                        }

                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Log.d("check", "failer")
                }

            })

        }
    }
    fun delete_Drive(dataset:DriveAwayData) {

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(PostService::class.java)
                .anonymousForumPostDelete(
                    dataset.b_seq.toString(),
                    my_seq.value.toString()
                )
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if (type.equals("success")) {
                            var rows = data.message
                            Log.d("통신상태", rows)
                            if (rows.equals("삭제 되었습니다")) {

                                var list: ArrayList<DriveAwayData> = drivelist.value!!
                                list!!.remove(dataset)
                                drivelist.value = list!!
                                deleteCall.value = true
                            }
                        }else if (type.equals("notpoint")){
                            toast.value = "차감할 포인트가 부족합니다. 삭제할 수 없습니다."
                        }else {
                            toast.value = "잠시 후 다시 시도해주세요."
                        }
                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<ResultData>,
                    t: Throwable
                ) {
                    Log.d("check", "failer")
                }

            })

        }
    }
    fun PoliceInsert( content:String, us_seq: String) {
        if (apicall.value == false){
           apicall.value =true
        val objdata = JsonObject()
        objdata.addProperty("u_seq", my_seq.value)
        objdata.addProperty("use_seq", us_seq)
        objdata.addProperty("p_type", "DRIVE")
        objdata.addProperty("p_contents", content)
        val obj = JsonObject()
        obj.add("obj", objdata)
        Log.d("PostNotiInsert", obj.toString())

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(PostService::class.java)
                .PoliceInsert(obj)
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    Log.d("check", data.toString())
                    if (data != null) {

                        var rows = data.message
                        toast.value = data.message
                    } else {

                    }
                    apicall.value =false
                }

                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Log.d("check", "failer")
                    apicall.value =false
                }

            })

        }

        }
    }
    fun PostBlockInsert(content:String,us_seq:String) {
        val objdata = JsonObject()
        objdata.addProperty("u_seq", my_seq.value)
        objdata.addProperty("use_seq", us_seq)
        objdata.addProperty("p_type", "DRIVE")
        objdata.addProperty("p_contents", content)
        val obj = JsonObject()
        obj.add("obj", objdata)
        Log.d("PostBlockInsert", obj.toString())

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(PostService::class.java)
                .PostBlockInsert(obj)
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    Log.d("check", data.toString())
                    if (data != null) {

                        var rows = data.message
                        Log.d("onResponse", rows.toString())
                        toast.value = data.message
                    } else {

                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Log.d("check", "failer")
                }

            })

        }


    }

    fun ProfileReadCheck(dataset: DriveAwayData,position: Int) {

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(MatchingService::class.java)
                .ProfileReadCheck(
                    dataset.b_seq.toString(),my_seq.value,dataset.u_seq.toString()
                )
            d!!.enqueue(object : retrofit2.Callback<ResultProfileReadCheck> {
                override fun onResponse(
                    call: retrofit2.Call<ResultProfileReadCheck>,
                    response: Response<ResultProfileReadCheck>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        editdata.value = dataset
                        editindex.value = position
                        if(type.equals("open")){
                            var pcok =data.pc_ok
                            if(pcok.equals("M")){
                                drivePopMenuStatus.value =DrivePopMenuStatus.PROFILE_WAIT_DIALOG
                            }else{
                                drivePopMenuStatus.value =DrivePopMenuStatus.FRIEND_POPUP
                            }
                        }else if(type.equals("friend")){
                            Log.d("프로필체크", "friend")
                            drivePopMenuStatus.value =DrivePopMenuStatus.FRIEND_POPUP
                        }else if(type.equals("company")){
                            drivePopMenuStatus.value = DrivePopMenuStatus.COMPANY_PROFILE
                        }else{
                            var phone = data.phone
                            FriendCheck(phone)
                        }

                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<ResultProfileReadCheck>,
                    t: Throwable
                ) {
                    Log.d("check", "failer")
                }

            })

        }
    }

    fun FriendCheck(phone: String) {

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(UserService::class.java)
                .FriendList(
                    my_seq.value
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
                                        drivePopMenuStatus.value = DrivePopMenuStatus.FRIEND_POPUP
                                    }else{
                                        drivePopMenuStatus.value = DrivePopMenuStatus.NOT_FRIEND_POPUP
                                    }
                                } else {
                                    drivePopMenuStatus.value = DrivePopMenuStatus.NOT_FRIEND_POPUP
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

    fun ProfileRead(data: DriveAwayData,position: Int,heart:Int,content: String) {

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(MatchingService::class.java)
                .ProfileRead(data.b_seq.toString(),
                    my_seq.value,data.u_seq.toString(),heart.toString(),"DRIVE","프로필확인",content,data.rn_nickname
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
                            toast.value = "드라이브 신청을 성공했습니다."
                            isedit.value = true
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
    fun driveset(dataset: DriveAwayData){
        var status = dataset.pc_status
        when(status){
            0->{
                Log.d("DRIVE_SET","drive0")
            }
            1->{
                Log.d("DRIVE_SET","drive1")
            }
            2->{
                Log.d("DRIVE_SET","drive2")
            }
            3->{
                Log.d("DRIVE_SET","drive3")
            }
        }

    }
    fun FeedProfileFeeedCehck(mtype: PidDialogType) {
        if (bubbleRick.value == false) {
            bubbleRick.value = true

            var searchtype = ""
            when (mtype) {
                PidDialogType.MAN_GLAMMER -> {
                    searchtype = "글래머"
                }
                PidDialogType.FM_10PER -> {
                    searchtype = "상위 10퍼"
                }
                PidDialogType.FRIEND -> {
                    searchtype = "짝꿍"
                }
                PidDialogType.AGE_20 -> {
                    searchtype = "20대 드라이버"
                }
                PidDialogType.NEW_USER -> {
                    searchtype = "최근가입 신입회원"
                }
                PidDialogType.SUPER_USE -> {
                    searchtype = "슈퍼카오너"
                }
                PidDialogType.LIKE_MUSIC -> {
                    searchtype = "음악취향"
                }
                else -> {}
            }
            Log.d("피드 seq1", my_seq.value.toString())
            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter!!.create(MatchingService::class.java)
                    .FeedProfileFeeedCehck(
                        my_seq.value,
                        searchtype
                    )
                d!!.enqueue(object : retrofit2.Callback<ResultFeedData> {
                    override fun onResponse(
                        call: retrofit2.Call<ResultFeedData>,
                        response: Response<ResultFeedData>
                    ) {
                        var data = response.body()
                        if (data != null) {
                            var type = data.type

                            Log.d("타입2", type)
                            if (type.equals("success")) {

                                FeedProfileSearch(mtype)

                            } else if (type.equals("notheart")) {
                               isnotheart.value = true
                            } else if (type.equals("over")) {
                                toast.value = type
                            } else if (type.equals("nochance")) {
                                var u_seq = data.u_seq
                                feed_search_seq.value = u_seq!!
                             //   toast.value = "무료 열람 기회를 소진하였습니다. 하트가 소모됩니다."
                            }
                        }
                        bubbleRick.value = false
                    }

                    override fun onFailure(
                        call: retrofit2.Call<ResultFeedData>,
                        t: Throwable
                    ) {
                        bubbleRick.value = false
                        Log.d("check", "failer")
                    }

                })

            }
        }
    }
    fun FeedProfileSearch(mtype: PidDialogType) {
        var searchtype = ""
        when (mtype) {
            PidDialogType.MAN_GLAMMER -> {
                searchtype = "글래머"
            }
            PidDialogType.FM_10PER -> {
                searchtype = "상위 10퍼"
            }
            PidDialogType.FRIEND -> {
                searchtype = "짝꿍"
            }
            PidDialogType.AGE_20 -> {
                searchtype = "20대 드라이버"
            }
            PidDialogType.NEW_USER -> {
                searchtype = "최근가입 신입회원"
            }
            PidDialogType.SUPER_USE -> {
                searchtype = "슈퍼카오너"
            }
            PidDialogType.LIKE_MUSIC -> {
                searchtype = "음악취향"
            }
            else -> {}
        }
        Log.d("피드 seq1", my_seq.value.toString())
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(MatchingService::class.java)
                .FeedProfileSearch(
                    my_seq.value,
                    searchtype
                )
            d!!.enqueue(object : retrofit2.Callback<ResultFeedData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultFeedData>,
                    response: Response<ResultFeedData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        Log.d("타입", type)
                        if (type.equals("success")) {
                            var u_seq = data.u_seq
                            if (u_seq != null) {
                                feed_search_seq.value = u_seq!!
                                Log.d("피드 seq2", u_seq.toString())

                            }
                        } else if(type.equals("notexist")) {
                            toast.value = type
                        }else if (type.equals("notheart")) {
                            isnotheart.value = true
                        } else if (type.equals("nochance")) {
                            var u_seq = data.u_seq
                            feed_search_seq.value = u_seq!!
                        //    toast.value = "무료 열람 기회를 소진하였습니다. 하트가 소모됩니다."
                        }
                    }

                }

                override fun onFailure(
                    call: retrofit2.Call<ResultFeedData>,
                    t: Throwable
                ) {
                    Log.d("check", "failer")
                }

            })

        }
    }
    fun checkviewdrive(data:DriveAwayData){
        if(drivelist.value!!.size !=0) {
            if (!readlist.value!!.contains(data.b_seq.toString())) {
                driveViewsUpdate(data.b_seq.toString())
                Log.d("boardViewsCALL", "호출"+data.b_seq)
            }
        }
    }
    fun driveViewsUpdate(b_seq:String){
        Log.d("boardViewsUpdate", "호출")
//        if (apicall.value == false){
//            apicall.value == true

            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter!!.create(DriveService::class.java).driveViewsUpdate(b_seq,my_seq.value!!)
                d!!.enqueue(object : retrofit2.Callback<ResultData> {
                    override fun onResponse(
                        call: retrofit2.Call<ResultData>,
                        response: Response<ResultData>
                    ) {
                        var data = response.body()
                        Log.d("boardViewsUpdate", "onResponse")
                        if (data != null) {
                            Log.d("boardViewsUpdate", data.toString())
                            if (!data.type.equals("fail")){

                                if (data.message.equals("이미 조회한 게시글입니다.")){

                                }
                            }else{
                                var rows = data.message
                                var list = readlist.value!!
                                if(!list.contains(b_seq)) {
                                    list.add(b_seq)
                                }
                            }


                        }

                        apicall.value == false
                    }

                    override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                        Log.d("boardViewsUpdate", t.message!!)
                        apicall.value == false
                    }

                })

            }
        }

//    }
}