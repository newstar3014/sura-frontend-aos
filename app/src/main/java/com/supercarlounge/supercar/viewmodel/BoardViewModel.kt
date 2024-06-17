package com.supercarlounge.supercar.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.AnonymousForumService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.Network.NetworkManager.gson
import com.supercarlounge.supercar.Network.PostService
import com.supercarlounge.supercar.data.*
import com.google.gson.JsonObject
import com.supercarlounge.supercar.Network.BannerService
import com.supercarlounge.supercar.Network.UserService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response


class BoardViewModel : ViewModel() {
    var my_seq = MutableLiveData<String>("")
    var text = MutableLiveData<String>("?")
    var titleText = MutableLiveData<String>("CONCIERGE")
    var nottext = MutableLiveData<String>("게시글을 불러오는 중입니다")
    var userdata = MutableLiveData<UserInformationData>()
//    var filterArray = arrayOf<String> ("공지","전체","베스트",
//        "자유","연애","세차","CAR","캠핑","골프/운동","핫플","재테크","직업","서킷"
//
//    )
    var filterArray = arrayOf<String> ("공지","전체","베스트",
        "자유","핫플","정보","차량","연애"

    )
    var c_seq = MutableLiveData<String>("전체")
    var b_contents = MutableLiveData<String>("")
    var order = MutableLiveData<String>("")
    var bookmark = MutableLiveData<String>("")

    //서치검색 켜기 끄기
    var search_check = MutableLiveData<Boolean>(false)

    //공지
    var notipage = MutableLiveData<Int>(1)
    var notippp = MutableLiveData<Int>(15)

    //게시글
    var page = MutableLiveData<Int>(1)
    var ppp = MutableLiveData<Int>(15)
    var totalCount= MutableLiveData<Int>(0)
    var viewType =  MutableLiveData<Int>(0)

    val list = ArrayList<BoardData>()
    var anonymousForumNoitList : MutableLiveData<ArrayList<NotiData>> =  MutableLiveData<ArrayList<NotiData>>(arrayListOf())
    var anonymousForumList : MutableLiveData<ArrayList<BoardData>> =  MutableLiveData<ArrayList<BoardData>>(arrayListOf())
    var anonymousForumBannerList : MutableLiveData<ArrayList<BannerData>> =  MutableLiveData<ArrayList<BannerData>>(arrayListOf())
    var state =  MutableLiveData<Boolean>(true)
    val tlist = ArrayList<BoardData>()


    var bannerPosition= MutableLiveData<Int>(0)
    var filterPosition = MutableLiveData<Int>(0)

    var bannerlist= MutableLiveData<ArrayList<BannerData>>(arrayListOf())
    var select_bannernoti_data= MutableLiveData<NotiData>()
    var issend= MutableLiveData<Boolean>(false)
    var apicall= MutableLiveData<Boolean>(false)

    var invite_code = MutableLiveData<String>("")
    var inviteset =MutableLiveData<Boolean>(false)
    fun getAnonymousForumPost(c_seq:String,b_contents:String,bookmark:String,order:String) {
        if (apicall.value ==false) {
            apicall.value = true
            Log.d("getAnonymousForumPost", "call")
            if (bookmark.isNotEmpty()) {
                nottext.value = "북마크한 게시글이 없습니다"
            } else {
                if (b_contents.isNotEmpty()) {
                    nottext.value = "검색한 게시글이 없습니다"
                } else {
                    nottext.value = "게시글을 불러오는 중입니다"
                }
            }

            this.order.value = order

            val jsonObjectList = JsonObject()
            val board = JsonObject()
            board.addProperty("u_seq", userdata.value?.u_seq)
            board.addProperty("c_seq", c_seq)
            board.addProperty("b_contents", b_contents)
            board.addProperty("bookmark", bookmark)
            board.addProperty("page", page.value!!)
            board.addProperty("ppp", ppp.value!!)
            board.addProperty("order", order)


            val boardlist = JsonObject()
            boardlist.add("board", board)


//        val boardArrayObject = JSONArray()
//        boardArrayObject.put("board", jsonObjectList)
            //Log.d("뭐지", boardlist.toString())
            val body = gson.toJson(boardlist)

            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter!!.create(PostService::class.java)
                    .getAnonymousForumPost(boardlist)
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
//                                page.value = 1
//                            anonymousForumList.value?.addAll(rows)
                                Log.d("데이터 가져오기", viewType.value.toString())
//                            if (viewType.value == 1){
//                                testList.value = rows
//                            }else{
//                            if (state.value == true) {
                                anonymousForumList.value = rows

//                            }
//                            }

                            } else {
                                var list: ArrayList<BoardData> = arrayListOf()
                                anonymousForumList.value = list
                            }

                        } else {
                            var list: ArrayList<BoardData> = arrayListOf()
                            anonymousForumList.value = list
                        }
                        apicall.value = false
                    }

                    override fun onFailure(call: retrofit2.Call<ResultBoardData>, t: Throwable) {
                        Log.d("check", "failer")
                        apicall.value = false
                    }

                })

            }
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

    fun getAnonymousForumNoti(){

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(AnonymousForumService::class.java).getAnonymousForumNoti(
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
                            anonymousForumNoitList!!.value =data
                        }
                    }
                }

                override fun onFailure(call: retrofit2.Call<ResultNotiData>, t: Throwable) {
                    Log.d("check", t.toString())
                }

            })

        }


    }
    fun getOneNoti(n_seq:String){


        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(BannerService::class.java).getOneNoti(n_seq
            )
            d!!.enqueue(object : retrofit2.Callback<ResultOneNotiData> {
                override fun onResponse(
                    call: Call<ResultOneNotiData>,
                    response: Response<ResultOneNotiData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var data = data.rows
                        select_bannernoti_data.value = data

                    }

                }

                override fun onFailure(call: Call<ResultOneNotiData>, t: Throwable) {
                    Log.d("check", "failer")
                }

            })

        }
    }

    fun viewLogBanner(bc_seq:Int,cl_type:String) {
        var myseq = userdata.value?.u_seq!!
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(BannerService::class.java)
                .viewLogBanner(bc_seq,myseq.toString(),cl_type)
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        var message = data.message
                        if (type.equals("success")) {

                        } else {

                        }


                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                }

            })

        }

    }
    fun GetInviteCode() {
        if(invite_code.value.isNullOrEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter.create(UserService::class.java).InviteCodeGet(
                    my_seq.value,
                )
                d!!.enqueue(object : retrofit2.Callback<ResultInvateCodeData> {
                    override fun onResponse(
                        call: retrofit2.Call<ResultInvateCodeData>,
                        response: Response<ResultInvateCodeData>
                    ) {
                        var data = response.body()
                        if (data != null) {
                            var rows = data.rows
                            if (rows != null) {
                                if (rows.size != 0) {
                                    var codeset = rows.get(0).my_code
                                    invite_code.value = codeset
                                    inviteset.value = true
                                }
                            }
                        }

                    }

                    override fun onFailure(
                        call: retrofit2.Call<ResultInvateCodeData>,
                        t: Throwable
                    ) {
                        Log.d("check", t.toString())
                    }

                })

            }
        }else{
            inviteset.value = true
        }
    }
}