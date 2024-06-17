package com.supercarlounge.supercar.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Constans
import com.supercarlounge.supercar.data.*
import com.google.gson.JsonObject
import com.supercarlounge.supercar.Network.*
import com.supercarlounge.supercar.enumset.BoardCheckViewStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder
import java.time.LocalDate
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.collections.ArrayList


class MyCarBoardPostViewModel : ViewModel() {

    var titleText = MutableLiveData<String>("MY CAR")
    var mainText = MutableLiveData<String>("")
    var filterPosition = MutableLiveData<Int>(0)
    var mainid = MutableLiveData<String>("")
    var time = MutableLiveData<String>("")
    var subText = MutableLiveData<String>("")
    var sex = MutableLiveData<String>("")
    var likeCount= MutableLiveData<Int?>(0)
    var position= MutableLiveData<Int?>(0)

    var like_type = MutableLiveData<Int>(0)
    var comment_like_type = MutableLiveData<Int>(0)
    var subcomment_like_type = MutableLiveData<Int>(0)
    var bookmark = MutableLiveData<String>("N")
    var like = MutableLiveData<String>("N")
    var b_like = MutableLiveData<String>("")
    var commentPosition = MutableLiveData<Int>(0)
    var subCommentPosition = MutableLiveData<Int>(0)
    var stateCall = MutableLiveData<Int>(0)
    var image = MutableLiveData<String>(null)
    var toast = MutableLiveData<String>("")
    var ProfileReadApiCall =  MutableLiveData<Boolean>(false)
    var scrollY = MutableLiveData<Int>(0)
    var u_gender = MutableLiveData<String>("")
    var b_seq = MutableLiveData<Int>(0)

    var viewtype = MutableLiveData<String>("")



    var my_user_data: MutableLiveData<UserInformationData> = MutableLiveData()
    var myCarBoardseq = MutableLiveData<Int>(0)
    var myCarBoardData = MutableLiveData<MyCarBoardData>(null)
    var myCarBoardDataPosition = MutableLiveData<Int>(0)
    var subInfoList : MutableLiveData<ArrayList<MySubInfoData>> =  MutableLiveData<ArrayList<MySubInfoData>>(arrayListOf())


    var board_u_seq =MutableLiveData<String>("")
    var u_seq = MutableLiveData<Int>(0)
    var uid = MutableLiveData<String>("")
    var viewlistCheck = MutableLiveData<Boolean>(false)
    var boardCheckViewStatus = MutableLiveData<BoardCheckViewStatus>(BoardCheckViewStatus.DEFAULT)
    var sub_seq = MutableLiveData<String>("0")
    var select_seq = MutableLiveData<String>("0")
    var select_anonymousNickname = MutableLiveData<String>("")

    var us_seq = MutableLiveData<String>("0")
    var page = MutableLiveData<Int>(1)
    var ppp = MutableLiveData<Int>(100)
    var cs_seq = MutableLiveData<String>("0")
    var cm_seq = MutableLiveData<String>("")
    var update = MutableLiveData<Int>(null)
    var commentInputType = MutableLiveData<Int>(0)
    var subUpdate = MutableLiveData<Int>(null)
    var bubbleRick = MutableLiveData<Boolean>(false)
    var anonymousForumDeepPostData = MutableLiveData<BoardDetailData>(null)
    val myCarBoardCommentList2 = ArrayList<MyCarBoardCommentData>()
    var viewList : MutableLiveData<ArrayList<ViewListData>> =  MutableLiveData<ArrayList<ViewListData>>(arrayListOf())
    var myCarBoardCommentList: MutableLiveData<ArrayList<MyCarBoardCommentData>> = MutableLiveData(arrayListOf())
    var myCarBoardCommentListCount = MutableLiveData<Int>(null)


    private val _SubCommentList =
        MutableLiveData<ArrayList<MyCarBoardSubCommentData>>()
    var SubCommentList: MutableLiveData<ArrayList<MyCarBoardSubCommentData>> = _SubCommentList

    var tagNickname = MutableLiveData<String>(null)
    var commentContents = MutableLiveData<String>("")
    var drivepassset = MutableLiveData<Boolean>(null)
    var isnotheart = MutableLiveData<Boolean>(false)






    fun bookMarkEvent(){
        InsertBookmark()
    }
    fun likeEvent(){
        myCarLikeSave()


    }




    fun getMyCarBoardSubList() {
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(MyCarBoardService::class.java).getMyCarBoardSubList(myCarBoardData.value?.mmi_seq,myCarBoardData.value?.u_seq.toString())
            d!!.enqueue(object : retrofit2.Callback<ResultMyCaBoardSubData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultMyCaBoardSubData>,
                    response: Response<ResultMyCaBoardSubData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var rows = data.rows
                        if (rows != null) {
                            subInfoList.value = rows

                        }


                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<ResultMyCaBoardSubData>,
                    t: Throwable
                ) {
                    Log.d("check", "failer")
                }

            })

        }


    }


    fun getMyCarComment() {
        var my_seq = my_user_data.value!!.u_seq!!
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(MyCarBoardService::class.java).getMyCarComment(myCarBoardData.value?.mmi_seq,my_seq)
            d!!.enqueue(object : retrofit2.Callback<ResultMyCaBoardCommentData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultMyCaBoardCommentData>,
                    response: Response<ResultMyCaBoardCommentData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var rows = data.rows
                        if (rows != null) {
                            myCarBoardCommentList.value = rows

                        }


                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<ResultMyCaBoardCommentData>,
                    t: Throwable
                ) {
                    Log.d("check", "failer")
                }

            })

        }


    }


    fun insertComment() {
        if (bubbleRick.value == false){
            bubbleRick.value = true

            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter!!.create(MyCarBoardService::class.java)
                    .insertComment(my_user_data.value?.u_seq.toString(),myCarBoardData.value?.mmi_seq,commentContents.value)
                d!!.enqueue(object : retrofit2.Callback<ResultData> {
                    override fun onResponse(
                        call: retrofit2.Call<ResultData>,
                        response: Response<ResultData>
                    ) {
                        var data = response.body()
                        Log.d("check", data.toString())
                        if (data != null) {

                            var message = data.message
                            if (message.equals("메인댓글 등록되었습니다")) {
                                Log.d("댓글 인서트", "onResponse: ")
                                getMyCarComment()

                            } else {

                            }

                        } else {

                        }
                        commentContents.value = ""
                        bubbleRick.value = false

                    }

                    override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                        Log.d("check", "failer")
                        commentContents.value = ""
                        bubbleRick.value = false

                    }

                })

            }

        }


    }

    fun updateComment() {
        if (bubbleRick.value == false){
            bubbleRick.value = true

            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter!!.create(MyCarBoardService::class.java)
                    .updateComment(cm_seq.value,myCarBoardData.value?.mmi_seq,my_user_data.value?.u_seq,commentContents.value)
                d!!.enqueue(object : retrofit2.Callback<ResultData> {
                    override fun onResponse(
                        call: retrofit2.Call<ResultData>,
                        response: Response<ResultData>
                    ) {
                        var data = response.body()
                        Log.d("check", data.toString())
                        if (data != null) {

                            var type = data.type
                            if (type.equals("success")) {
                                Log.d("댓글 인서트", "onResponse: ")
                                getMyCarComment()

                            } else {

                            }

                        } else {

                        }
                        commentContents.value = ""
                        bubbleRick.value = false

                    }

                    override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                        Log.d("check", "failer")
                        commentContents.value = ""
                        bubbleRick.value = false

                    }

                })

            }

        }


    }

    fun insertSubComment() {
        if (bubbleRick.value == false) {
            bubbleRick.value = true
            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter!!.create(MyCarBoardService::class.java)
                    .insertSubComment(
                        my_user_data.value?.u_seq.toString()
                        ,myCarBoardData.value?.mmi_seq
                        ,cm_seq.value
                        ,commentContents.value.toString()
                        ,cs_seq.value)
                d!!.enqueue(object : retrofit2.Callback<ResultData> {
                    override fun onResponse(
                        call: retrofit2.Call<ResultData>,
                        response: Response<ResultData>
                    ) {
                        var data = response.body()

                        if (data != null) {


                            var type = data.type
                            var message = data.message
                            Log.d("check11", type.toString())
                            Log.d("check22", message.toString())
                            if (type == "success"){
                                if (message == "대댓글이 저장 되었습니다") {
                                    getMyCarComment()
                                }
                            }


                        } else {

                        }
                        commentContents.value = ""
                        bubbleRick.value = false

                    }

                    override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                        Log.d("check", "failer")
                        commentContents.value = ""
                        bubbleRick.value = false

                    }

                })

            }
        }


    }

    fun updateSubComment() {

        if (bubbleRick.value == false){
            bubbleRick.value = true

            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter!!.create(MyCarBoardService::class.java)
                    .updateSubComment(cs_seq.value ,my_user_data.value?.u_seq,myCarBoardData.value?.mmi_seq,commentContents.value)
                d!!.enqueue(object : retrofit2.Callback<ResultData> {
                    override fun onResponse(
                        call: retrofit2.Call<ResultData>,
                        response: Response<ResultData>
                    ) {
                        var data = response.body()
                        Log.d("check", data.toString())
                        if (data != null) {

                            var type = data.type
                            if (type.equals("success")) {
                                Log.d("댓글 인서트", "onResponse: ")
                                getMyCarComment()

                            } else {

                            }

                        } else {

                        }
                        commentContents.value = ""
                        bubbleRick.value = false

                    }

                    override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                        Log.d("check", "failer")
                        commentContents.value = ""
                        bubbleRick.value = false

                    }

                })

            }

        }


    }
    fun insertViewSave() {

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(MyCarBoardService::class.java)
                .insertViewSave(myCarBoardData.value?.mmi_seq,my_user_data.value?.u_seq.toString())
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var rows = data.type
                        if (rows != null) {
//                            Log.d("InsertBoardLikeSave", rows)
//                            getAnonymousForumDeepPost()
                        } else {

                        }

                    } else {

                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Log.d("check", "failer")
                }

            })

        }


    }
    fun InsertBoardLikeSave(u_seq: String, b_seq: String, bl_type: String,type_like:Int) {

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(InsertService::class.java)
                .InsertBoardLikeSave(u_seq, b_seq, bl_type, type_like)
            d!!.enqueue(object : retrofit2.Callback<ResultTypeData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultTypeData>,
                    response: Response<ResultTypeData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type

                        if (type.equals("success")) {
                            Log.d("InsertBoardLikeSave", "success")
//                            getAnonymousForumDeepPost()
                        } else if (type.equals("notpoint")){
                            toast.value = "차감할 포인트가 부족합니다. 삭제할 수 없습니다."
                        }else {
                           // toast.value = "잠시 후 다시 시도해주세요."
                        }

                    } else {

                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultTypeData>, t: Throwable) {
                    Log.d("check", "failer")
                }

            })

        }


    }



    fun deleteMyCarBoard() {
        if (bubbleRick.value == false){
            bubbleRick.value = true

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(MyCarBoardService::class.java)
                .deleteMyCarBoard(
                    myCarBoardData.value?.mmi_seq,
                    my_user_data.value?.u_seq.toString()

                )
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        var message = data.message

                        Log.d("통신상태 mmi_seq", myCarBoardData.value?.mmi_seq.toString())
                        Log.d("통신상태 보내기 u_seq", my_user_data.value?.u_seq.toString())
                        Log.d("통신상태 결과", data.toString())

                        if (type.equals("success")){
                            if(message.equals("관련된 데이터가 삭제 되었습니다")){
                                stateCall.value = Constans.DIALOG_POST_DELETE_API_CALL_CLEAR
                            }else{
                                toast.value ="게시글 삭제를 실패하였습니다"
                            }
                        }else if (type.equals("notpoint")){
                            toast.value = "차감할 포인트가 부족합니다. 삭제할 수 없습니다."
                        }else {
                            toast.value = "잠시 후 다시 시도해주세요."
                        }

//                        Log.d("anonymousForumPostDelete", rows)


                    }
                    bubbleRick.value =false
                }

                override fun onFailure(
                    call: retrofit2.Call<ResultData>,
                    t: Throwable
                ) {
                    Log.d("check", "failer")
                    bubbleRick.value =false
                }

            })

        }
        }

    }
    fun deleteComment() {
        if (bubbleRick.value == false) {
            bubbleRick.value = true
            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter!!.create(MyCarBoardService::class.java)
                    .deleteComment(
                        my_user_data.value?.u_seq.toString(),
                        cm_seq.value,
                        myCarBoardData.value?.mmi_seq.toString()
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
                                stateCall.value = Constans.DIALOG_POST_COMMENT_DELETE_API_CALL
                            }else if (type.equals("notpoint")){
                                toast.value = "차감할 포인트가 부족합니다. 삭제할 수 없습니다."
                            }else {
                                toast.value = "잠시 후 다시 시도해주세요."
                            }
                        }
                        bubbleRick.value =false
                    }

                    override fun onFailure(
                        call: retrofit2.Call<ResultData>,
                        t: Throwable
                    ) {
                        bubbleRick.value =false
                        Log.d("check", "failer")
                    }

                })

            }
        }


    }

    fun myCarLikeSave(){

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(MyCarBoardService::class.java)
                .myCarLikeSave(
                    myCarBoardData.value?.mmi_seq.toString(),
                    my_user_data.value?.u_seq.toString(),
                )

            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type

                        if (type.equals("success")){
                            if (like.value.equals("Y")){
                                like.value= "N"
                                var min = likeCount.value?.minus(1)
                                likeCount.value = min
                            }else{
                                like.value= "Y"
                                var min = likeCount.value?.plus(1)
                                likeCount.value = min
                            }


                        stateCall.value = Constans.MY_CAR_POST_LIKE
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
    fun deleteSubComment() {

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(MyCarBoardService::class.java)
                .deleteSubComment(
                    cs_seq.value.toString(),
                    my_user_data.value?.u_seq.toString(),
                    myCarBoardData.value?.mmi_seq.toString(),
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
                            stateCall.value = Constans.DIALOG_POST_SUB_COMMENT_DELETE_API_CALL
                        }else if (type.equals("notpoint")){
                            toast.value = "차감할 포인트가 부족합니다. 삭제할 수 없습니다."
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



    fun PostBlockInsert(p_type:String,content:String,us_seq:String) {
        val objdata = JsonObject()
        objdata.addProperty("u_seq", my_user_data.value?.u_seq)
        objdata.addProperty("use_seq", us_seq)
        objdata.addProperty("p_type", p_type)
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

                            if (p_type.equals("BOARD")) {
                                stateCall.value = Constans.DIALOG_POST_BLOCK_API_CALL
                            }else{
                                stateCall.value = Constans.DIALOG_POST_BLOCK_COMMENT_API_CALL
                            }


                    } else {

                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Log.d("check", "failer")
                }

            })

        }


    }

    fun PoliceInsert(p_type:String, content:String, us_seq: String) {
        val objdata = JsonObject()
        objdata.addProperty("u_seq", my_user_data.value?.u_seq)
        objdata.addProperty("use_seq", us_seq)
        objdata.addProperty("p_type", p_type)
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

                        stateCall.value= Constans.DIALOG_POST_NOTI_API_CALL



                    } else {

                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Log.d("check", "failer")
                }

            })

        }


    }

    fun InsertBookmark() {

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(PostService::class.java)
                .InsertBookmark(
                    "MYCAR",
                    my_user_data.value?.u_seq.toString(),
                    myCarBoardData.value?.mmi_seq.toString(),
                )
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()

                    if (data != null) {
                        if (data.message.equals("북마크가 삭제 되었습니다")){
                            bookmark.value= "N"
                        }else{
                            bookmark.value= "Y"
                        }
                        stateCall.value = Constans.MY_CAR_POST_BOOKMARK

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

    fun boardViewsUpdate(){
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(PostService::class.java).boardViewsUpdate(b_seq.value.toString(),myCarBoardData.value?.u_seq.toString())
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        Log.d("boardViewsUpdate", "성공")
                        var rows = data.message


                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Log.d("boardViewsUpdate", t.message!!)
                }

            })

        }
    }

    fun boardViewList(){
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(PostService::class.java).boardViewList(b_seq.value.toString(),myCarBoardData.value?.u_seq.toString())
            d!!.enqueue(object : retrofit2.Callback<ResultViewListData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultViewListData>,
                    response: Response<ResultViewListData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        Log.d("boardViewList", "성공")
                        var rows = data.rows
                        viewList.value =rows
                        Log.d("boardViewList", rows.toString())

                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultViewListData>, t: Throwable) {
                    Log.d("boardViewList", t.message!!)
                }

            })

        }
    }


    fun boardViewCheckClick(sub_seq:String){
        Log.d("boardViewCheck", myCarBoardData.value?.mmi_seq.toString()+":"+myCarBoardData.value?.u_seq.toString()+""+sub_seq.toString())
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(PostService::class.java).boardViewCheck(myCarBoardData.value?.mmi_seq.toString(),my_user_data.value?.u_seq.toString(),sub_seq)
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
    fun encodeset(text:String):String{
        var result:String = ""
        var set = ""
        try {
            // var list =text.toCharArray()
            val results: MutableList<String> = java.util.ArrayList()
            val m: Matcher =
                Pattern.compile("\\P{M}\\p{M}*+").matcher(text)
            while (m.find()) {
                results.add(m.group())
            }
            for(i in results){
                if(checkemoji(i.toCharArray()[0])){

                    result+= URLEncoder.encode(i, "UTF-8")+" "
                    Log.d("EMOJISET1",i.toString())

                }else{
                    result+= i
                    Log.d("EMOJISET3",i.toString())
                }
            }


        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return result
    }
    //프로필 열람
    fun ProfileRead(heart:Int,content: String) {

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(MyCarBoardService::class.java)
                .ProfileRead(myCarBoardData.value?.mmi_seq.toString(),
                    my_user_data.value?.u_seq.toString(),select_seq.value,heart.toString(),"MYCAR","프로필확인",content,myCarBoardData.value?.mrc_nickname
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

    fun DrivePassCheck() {

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(MatchingService::class.java).DrivePassCheck(
                myCarBoardData.value?.u_seq.toString(),
            )
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        drivepassset.value = type.equals("success")
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
    fun getOneMycarData() {
            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter!!.create(MyCarBoardService::class.java)
                    .getMycarOne(myCarBoardseq.value.toString(), my_user_data.value?.u_seq.toString())
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
                                    myCarBoardData.value = data
                                    u_seq.value = data.u_seq
                                    getMyCarBoardSubList()
                                    insertViewSave()
                                   getMyCarComment()
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
    private fun checkemoji(text: Char): Boolean {
        val type: Int = Character.getType(text)
        return  (type == Character.SURROGATE.toInt() || type == Character.OTHER_SYMBOL.toInt())
    }
    fun decodeset(text: String?): String? {
        val myString: String? = null
        return try {

            URLDecoder
                .decode(
                    text, "UTF-8"
                )
        } catch (e: UnsupportedEncodingException) {
            text
        }
    }
}