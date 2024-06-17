package com.supercarlounge.supercar.viewmodel

import android.util.Log
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
import retrofit2.Call
import retrofit2.Response
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.collections.ArrayList


class PostViewModel : ViewModel() {

    var titleText = MutableLiveData<String>("게시글 등록 시 유의사항")

    var mainText = MutableLiveData<String>("")

    var filterArray = arrayOf<String>(
        "자유", "연애", "세차", "CAR", "캠핑", "골프/운동", "핫플", "재테크", "직업", "서킷"
    )
    var filterPosition = MutableLiveData<Int>(0)
    var mainid = MutableLiveData<String>("")
    var time = MutableLiveData<String>("")
    var subText = MutableLiveData<String>("")
    var sex = MutableLiveData<String>("")
    var likeCount = MutableLiveData<String>("0")
    var like_type = MutableLiveData<Int>(0)
    var comment_like_type = MutableLiveData<Int>(0)
    var subcomment_like_type = MutableLiveData<Int>(0)
    var bookmark = MutableLiveData<Int>(0)
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
    var my_u_seq = MutableLiveData<Int>(0)
    var board_u_seq =MutableLiveData<String>("")
    var u_seq = MutableLiveData<Int>(0)
    var uid = MutableLiveData<String>("")
    var viewlistCheck = MutableLiveData<Boolean>(false)
    var boardCheckViewStatus = MutableLiveData<BoardCheckViewStatus>(BoardCheckViewStatus.DEFAULT)
    var sub_seq = MutableLiveData<String>("0")
    var select_seq = MutableLiveData<String>("0")
    var select_anonymousNickname = MutableLiveData<String>("")
    var select_nick = MutableLiveData<String>("")
    var us_seq = MutableLiveData<String>("0")
    var page = MutableLiveData<Int>(1)
    var ppp = MutableLiveData<Int>(100)
    var cs_seq = MutableLiveData<String>("")
    var css_seq = MutableLiveData<String>("")
    var cm_seq = MutableLiveData<String>("")
    var update = MutableLiveData<Int>(null)
    var commentInputType = MutableLiveData<Int>(0)
    var subUpdate = MutableLiveData<Int>(null)
    var bubbleRick = MutableLiveData<Boolean>(false)
    var anonymousForumDeepPostData = MutableLiveData<BoardDetailData>(null)
    val Commentlist = ArrayList<BoardCommentData>()
    private val _anonymousForumCommentList = MutableLiveData<ArrayList<BoardCommentData>>()
    var viewList : MutableLiveData<ArrayList<ViewListData>> =  MutableLiveData<ArrayList<ViewListData>>(arrayListOf())
    var anonymousForumCommentList: MutableLiveData<ArrayList<BoardCommentData>> =
        _anonymousForumCommentList
    var anonymousForumCommentCount = MutableLiveData<Int>(null)

    val SubCommentlist = ArrayList<SubComment>()
    private val _SubCommentList =
        MutableLiveData<ArrayList<SubComment>>()
    var SubCommentList: MutableLiveData<ArrayList<SubComment>> =
        _SubCommentList

    var tagNickname = MutableLiveData<String>(null)
    var commentContents = MutableLiveData<String>("")
    var drivepassset = MutableLiveData<Boolean>(null)
    var miniprofilestatus = MutableLiveData<Boolean>(false)
    var ismymanager = MutableLiveData<Boolean>(false)
    var checkmanager = MutableLiveData<Boolean>(false)
    var isnotheart = MutableLiveData<Boolean>(false)
    init {

        _anonymousForumCommentList.value = Commentlist
        _SubCommentList.value = SubCommentlist
    }

    fun getAnonymousForumDeepPost() {
        Log.d("포스트 u_seq", u_seq.value.toString())
        Log.d("포스트 my_u_seq", my_u_seq.value.toString())
        Log.d("포스트 b_seq", b_seq.value.toString())
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(PostService::class.java)
                .getAnonymousForumDeepPost(
                    b_seq.value.toString(),
                    my_u_seq.value.toString(),
                    u_seq.value
                )

            d!!.enqueue(object : retrofit2.Callback<ResultBoardDetailData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultBoardDetailData>,
                    response: Response<ResultBoardDetailData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var rows = data.rows
                        if (rows != null) {
                            if (rows.size != 0) {
                                var dataset = rows[0]
                                anonymousForumDeepPostData.value =  dataset
                                var checkset =  dataset.u_staff_yn.equals("Y") && (dataset.c_seq.equals("정보") || dataset.c_seq.equals("핫플"))

                                checkmanager.value = checkset
                            }else{
                                toast.value = "해당 게시글이 존재하지 않습니다."
                            }


                        } else {
                            toast.value = "잠시 후 다시 시도해주세요."
                        }

                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<ResultBoardDetailData>,
                    t: Throwable
                ) {
                    Log.d("check", "failer")
                }

            })

        }


    }



    fun getAnonymousForumComment(u_seq: String, b_seq: String) {
        Log.d("anonymousForumCommentList", "$u_seq  $b_seq")
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(CommentService::class.java)
                .getAnonymousForumComment(u_seq, b_seq, ppp.value, page.value)
            d!!.enqueue(object : retrofit2.Callback<ResultBoardCommentData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultBoardCommentData>,
                    response: Response<ResultBoardCommentData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var rows = data.rows
                        if (rows != null) {
                            Log.d("anonymousForumCommentList", data.toString())
                            if (rows.size!= 0){

                            anonymousForumCommentList.value?.clear()
                            anonymousForumCommentList.value?.addAll(rows)
                            anonymousForumCommentList.value = anonymousForumCommentList.value
                            anonymousForumCommentCount.value = anonymousForumCommentList.value!!.size
                                Log.d(
                                    "SubCommentList_size",
                                    rows?.size.toString()
                                )
                            }else{
                                anonymousForumCommentList.value?.clear()
                                anonymousForumCommentList.value = anonymousForumCommentList.value
                                anonymousForumCommentCount.value = 0
                            }

                        } else {
//                            var list: ArrayList<BoardCommentData> = arrayListOf()
//                            var list2: ArrayList<SubComment> = arrayListOf()
//                            anonymousForumCommentList.value = list
//                            SubCommentList.value = list2
//                            anonymousForumCommentCount.value = 0
                        }

                    } else {
//                        var list: ArrayList<BoardCommentData> = arrayListOf()
//                        var list2: ArrayList<SubComment> = arrayListOf()
//                        anonymousForumCommentList.value = list
//                        SubCommentList.value = list2
//                        anonymousForumCommentCount.value = 0
                    }

                }

                override fun onFailure(
                    call: retrofit2.Call<ResultBoardCommentData>,
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
            val commentContentsData = JsonObject()
            commentContentsData.addProperty("u_seq", my_u_seq.value)
            commentContentsData.addProperty("b_seq", b_seq.value)
            commentContentsData.addProperty("cm_values", commentContents.value.toString())

            val comment = JsonObject()
            comment.add("comment", commentContentsData)

            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter!!.create(CommentService::class.java)
                    .insertComment(comment)
                d!!.enqueue(object : retrofit2.Callback<ResultData> {
                    override fun onResponse(
                        call: retrofit2.Call<ResultData>,
                        response: Response<ResultData>
                    ) {
                        var data = response.body()
                        Log.d("check", data.toString())
                        if (data != null) {

                            var rows = data.message
                            if (rows.equals("댓글이 등록 되었습니다")) {

                                getAnonymousForumComment(
                                    my_u_seq.value!!.toString(),
                                    b_seq.value!!.toString()
                                )
                            } else {

                            }

                        } else {

                        }

                        bubbleRick.value = false

                    }

                    override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                        Log.d("check", "failer")
                        bubbleRick.value = false
                    }

                })

            }

        }


    }

    fun insertSubComment() {
        if (bubbleRick.value == false) {
            bubbleRick.value = true
            val commentContentsData = JsonObject()
            commentContentsData.addProperty("u_seq", my_u_seq.value)
            commentContentsData.addProperty("b_seq", b_seq.value)
            commentContentsData.addProperty("cs_values", commentContents.value.toString())
            commentContentsData.addProperty("cm_seq", cm_seq.value)
            commentContentsData.addProperty("css_seq", cs_seq.value)
            val comment = JsonObject()
            comment.add("comment", commentContentsData)
            Log.d("댓글", comment.toString())

            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter!!.create(CommentService::class.java)
                    .insertSubComment(comment)
                d!!.enqueue(object : retrofit2.Callback<ResultData> {
                    override fun onResponse(
                        call: retrofit2.Call<ResultData>,
                        response: Response<ResultData>
                    ) {
                        var data = response.body()
                        Log.d("check", data.toString())
                        if (data != null) {

                            var rows = data.message
                            if (rows.equals("댓글 등록이 되었습니다")) {

                                getAnonymousForumComment(
                                    my_u_seq.value!!.toString(),
                                    b_seq.value!!.toString()
                                )

                            } else {

                            }

                        } else {

                        }
                        bubbleRick.value = false

                    }

                    override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                        Log.d("check", "failer")
                        bubbleRick.value = false
                    }

                })

            }
        }


    }


    fun InsertBoardLikeSave(u_seq: String, b_seq: String, bl_type: String,type_like:Int) {
        Log.d("글 데이터", u_seq.toString())
        Log.d("글 데이터", bl_type)
        Log.d("글 데이터", b_seq)
        Log.d("글 데이터", like_type.value.toString())
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
                        var rows = data.type
                        if (rows != null) {
                            Log.d("InsertBoardLikeSave", rows)
//                            getAnonymousForumDeepPost()
                        } else {

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

    fun insertComMainLike(posi: Int) {
        val data = JsonObject()
        data.addProperty("u_seq", my_u_seq.value)
        data.addProperty("b_seq", b_seq.value)
        data.addProperty("cm_seq", cm_seq.value)
        data.addProperty("like_type", 1)
        val obj = JsonObject()
        obj.add("obj", data)

        Log.d("댓글 데이터", data.toString())
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(InsertService::class.java)
                .insertComMainLike(obj)
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    Log.d("check", data.toString())
                    if (data != null) {

                        var rows = data.message
                        Log.d("결과", rows)


                        if (rows.equals("해당 댓글 좋아요가 등록 되었습니다")) {
                            Log.d("결과  좋아요가 등록  like_yn =", rows)
                            anonymousForumCommentList.value!![posi].like_yn = "Y"
                            anonymousForumCommentList.value!![posi].m_like =
                                anonymousForumCommentList.value!![posi].m_like.plus(1)
                            update.value = posi

                        } else if (rows.equals("해당 댓글 좋아요가 해제 되었습니다")) {
                            Log.d("결과 좋아요가 해제  like_yn =", rows)
                            anonymousForumCommentList.value!![posi].like_yn = "0"
                            anonymousForumCommentList.value!![posi].m_like =
                                anonymousForumCommentList.value!![posi].m_like.minus(1)
                            update.value = posi
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

    fun insertSubComMainLike() {
        val data = JsonObject()
        data.addProperty("u_seq", my_u_seq.value)
        data.addProperty("b_seq", b_seq.value)
        data.addProperty("cs_seq", cs_seq.value)
        data.addProperty("like_type", 1)
        Log.d("대댓글 데이터", data.toString())
        val obj = JsonObject()
        obj.add("obj", data)


        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(InsertService::class.java)
                .insertSubComMainLike(obj)
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    Log.d("check", data.toString())
                    if (data != null) {

                        var rows = data.message
                        Log.d("결과", rows)
                        if (rows.equals("좋아요가 등록 되었습니다") || rows.equals("좋아요가 해제 되었습니다")) {
                            if (rows.equals("좋아요가 등록 되었습니다")){
                                boardViewCheckClick(select_seq.value.toString())
                            }
                            getAnonymousForumComment(my_u_seq.value!!.toString(),b_seq.value!!.toString())
//                            anonymousForumCommentList.value!![posi].like_yn = "Y"
//                            anonymousForumCommentList.value!![posi].m_like =
//                                anonymousForumCommentList.value!![posi].m_like.plus(1)
//                            update.value = posi
                        } else {
//                            list[posi].like_yn = "Y"
//                            list[posi].s_like = list[posi].s_like.plus(1)
//                            update.value = posi
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

    fun anonymousForumPostDelete() {
        if (bubbleRick.value == false){
            bubbleRick.value = true

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(PostService::class.java)
                .anonymousForumPostDelete(
                    b_seq.value.toString(),
                    my_u_seq.value.toString()
                )
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        var rows = data.message
                        Log.d("통신상태", rows)
                        if (type.equals("success")) {
                            if (rows.equals("삭제 되었습니다")) {
                                stateCall.value = Constans.DIALOG_POST_DELETE_API_CALL_CLEAR
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
    fun anonymousForumCommentDelete() {
        if (bubbleRick.value == false) {
            bubbleRick.value = true
            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter!!.create(PostService::class.java)
                    .anonymousForumCommentDelete(
                        cm_seq.value.toString(),
                        my_u_seq.value.toString()
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

    fun SubCommentDelete() {

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(PostService::class.java)
                .SubCommentDelete(
                    cs_seq.value.toString(),
                    my_u_seq.value.toString()
                )
            Log.d("subcomment cs_seq :", cs_seq.value.toString())
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        Log.d("subcomment :", "try se")
                        var type = data.type
                        if (type.equals("success")) {
                            stateCall.value = Constans.DIALOG_POST_SUB_COMMENT_DELETE_API_CALL
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

    fun anonymousForumUpdateComment() {
        if (!commentContents.value.isNullOrEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter!!.create(CommentService::class.java)
                    .UpdateComment(
                        cm_seq.value.toString(),
                        commentContents.value.toString(),
                        my_u_seq.value.toString()
                    )
                Log.d("subcomment cs_seq :", cs_seq.value.toString())
                d!!.enqueue(object : retrofit2.Callback<ResultData> {
                    override fun onResponse(
                        call: retrofit2.Call<ResultData>,
                        response: Response<ResultData>
                    ) {
                        var data = response.body()
                        if (data != null) {
                            Log.d("subcomment :", "try se")
//                        var rows = data.message
//                        Log.d("anonymousForumPostDelete", rows)
                            stateCall.value = Constans.DIALOG_POST_SUB_COMMENT_DELETE_API_CALL

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


    }
    fun anonymousForumUpdateSubComment() {
        if (!commentContents.value.isNullOrEmpty()){
            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter!!.create(CommentService::class.java)
                    .UpdateSubComment(
                        cs_seq.value.toString(),
                        commentContents.value.toString(),
                        my_u_seq.value.toString()
                    )
                Log.d("subcomment cs_seq :", cs_seq.value.toString())
                d!!.enqueue(object : retrofit2.Callback<ResultData> {
                    override fun onResponse(
                        call: retrofit2.Call<ResultData>,
                        response: Response<ResultData>
                    ) {
                        var data = response.body()
                        if (data != null) {
                            Log.d("subcomment :", "try se")
//                        var rows = data.message
//                        Log.d("anonymousForumPostDelete", rows)
                            stateCall.value = Constans.DIALOG_POST_SUB_COMMENT_DELETE_API_CALL

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



    }

    fun PostBlockInsert(p_type:String,content:String,us_seq:String) {
        val objdata = JsonObject()
        objdata.addProperty("u_seq", my_u_seq.value)
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
        objdata.addProperty("u_seq", my_u_seq.value)
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
                    "BOARD",
                    my_u_seq.value.toString(),
                    b_seq.value.toString(),
                )
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()

                    if (data != null) {
//                        if (data.message.equals("북마크가 삭제 되었습니다")){
//                            bookmark.value= 0
//                        }else{
//                            bookmark.value= 1
//                        }



//                        val timer = Timer()

//                        timer.schedule(object : TimerTask() {
//                            override fun run() {
                                getAnonymousForumDeepPost()
//                                Log.d("ㅇ", "run: ")
//                                timer.cancel()
//                            }
//
//                            override fun cancel(): Boolean {
//                                return super.cancel()
//
//                            }
//                        }, 3000, 5000)




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
            val d = NetworkManager.serveradapter!!.create(PostService::class.java).boardViewsUpdate(b_seq.value.toString(),my_u_seq.value.toString())
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
            val d = NetworkManager.serveradapter!!.create(PostService::class.java).boardViewList(b_seq.value.toString(),my_u_seq.value.toString())
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
        Log.d("boardViewCheck", b_seq.value.toString()+":"+my_u_seq.value.toString()+""+sub_seq.toString())
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(PostService::class.java).boardViewCheck(b_seq.value.toString(),my_u_seq.value.toString(),sub_seq)
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

    fun boardViewCheck(){

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(PostService::class.java).boardViewCheck(b_seq.value.toString(),my_u_seq.value.toString(),sub_seq.value.toString())
            d!!.enqueue(object : retrofit2.Callback<ResultProfileReadCheck> {
                override fun onResponse(
                    call: retrofit2.Call<ResultProfileReadCheck>,
                    response: Response<ResultProfileReadCheck>
                ) {
                    var data = response.body()
                    if (data != null) {

                        var type = data.type
                        if(type.equals("open")){
                            viewlistCheck.value = true

                        }else if(type.equals("friend")){

                            viewlistCheck.value = false
                        }else if(type.equals("company")){
                            viewlistCheck.value = false
                        }else{
                            viewlistCheck.value = false
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
                    my_u_seq.value.toString()
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
            val d = NetworkManager.serveradapter!!.create(MatchingService::class.java)
                .ProfileRead(anonymousForumDeepPostData.value?.b_seq.toString(),
                    my_u_seq.value.toString(),select_seq.value,heart.toString(),"BOARD","프로필확인",content,anonymousForumDeepPostData.value?.rn_nickname
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
                my_u_seq.value.toString(),
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
    fun GetMatchingData(u_seq:String,sub_seq:String){
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(MatchingService::class.java).GetMatchingData(u_seq, sub_seq)
            d!!.enqueue(object : retrofit2.Callback<ProfileMatchingData> {
                override fun onResponse(
                    call: Call<ProfileMatchingData>,
                    response: Response<ProfileMatchingData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if (type != null) {
                            if (type.equals("success")) {

                                if (data != null) {
                                    var status = data.pc_status
                                    var pcok= data.pc_ok
                                    if (data.pc_status.equals("2")&&pcok.equals("Y")){
                                        miniprofilestatus.value = true
                                    }else{
                                        miniprofilestatus.value = false
                                    }
                                }
                            }
                        }else{
                            miniprofilestatus.value = false
                        }
                    }else{
                        miniprofilestatus.value = false
                    }
                }

                override fun onFailure(call: Call<ProfileMatchingData>, t: Throwable) {
                    Log.d("MatchingDataError", t.message!!)
                }
            })
        }
    }
}