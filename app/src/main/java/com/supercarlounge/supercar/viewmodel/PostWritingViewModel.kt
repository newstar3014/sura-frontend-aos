package com.supercarlounge.supercar.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.Network.PostService
import com.supercarlounge.supercar.data.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.apache.commons.lang3.StringEscapeUtils
import retrofit2.Call
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.regex.Matcher
import java.util.regex.Pattern

class PostWritingViewModel : ViewModel() {

    var titleText = MutableLiveData<String>("게시글 등록 시 유의사항")
    var time= MutableLiveData<String>("")
    var mainText = MutableLiveData<String>("")
    var subText = MutableLiveData<String>("")
    var viewType = MutableLiveData<String>("")
    var anonymousForumDeepPostData = MutableLiveData<BoardDetailData>(null)
    var b_contents = MutableLiveData<String>("")
//    var filterArray = arrayOf<String> (
//        "자유","연애","세차","CAR","캠핑","골프/운동","핫플","재테크","직업","서킷"
//    )
var filterArray = arrayOf<String> (
        "자유","핫플","정보","차량","연애"
    )
    var myUserData  =MutableLiveData<UserInformationData>(null)
    var myu_uuid = MutableLiveData<String>("")
    var boardCover = MutableLiveData<String>(null)
    var filterPosition = MutableLiveData<Int>(0)
    var boardInsertCall = MutableLiveData<Boolean>(false)
    var overlap = MutableLiveData<Boolean>(false)

    fun boardInsert(mainProfile: MultipartBody.Part) {

        if (overlap.value ==false){
            overlap.value = true


        val board = JsonObject()
        board.addProperty("b_type", "BOARD")
//        board.addProperty("ub_type", myUserData.value?.u_type)
//        board.addProperty("u_seq", myUserData.value?.u_type)
        board.addProperty("ub_type", "")
        board.addProperty("u_seq", myUserData.value?.u_seq)
        board.addProperty("b_contents",
//            encodeset(
                b_contents.value!!
//            )
        )
        board.addProperty("c_seq", filterArray[filterPosition.value!!])





        var dir= myu_uuid.value
        var call: Call<ResultBoardInsertData>?= NetworkManager.serveradapter!!.create(PostService::class.java)
            .boardInsert(
                mainProfile,
                requestbodyString(dir
                !!),
                board
            )

        call!!.enqueue(object : retrofit2.Callback<ResultBoardInsertData>{
            override fun onResponse(call: Call<ResultBoardInsertData>, response: retrofit2.Response<ResultBoardInsertData>) {
                if(response.isSuccessful) {

                    var result=response.body()
                    var message= result!!.message
                    var type=result!!.type
                    if(type!=null) {
                        boardInsertCall.value = type.equals("success")
                    }
                    else{
                        boardInsertCall.value = false
                    }


                }
                overlap.value = false
            }
            override fun onFailure(call: Call<ResultBoardInsertData>, t: Throwable) {
                Log.d("onFailure",t.toString())
                overlap.value = false
            }
        })
        }

    }
    fun encodeset(text:String):String{
        var result:String = ""
        var set = ""
        try {
            // var list =text.toCharArray()
            val results: MutableList<String> = ArrayList()
            val m: Matcher =
                Pattern.compile("\\P{M}\\p{M}*+").matcher(text)
            while (m.find()) {
                results.add(m.group())
            }
            for(i in results){
                if(checkemoji(i.toCharArray()[0])){

                    result+= URLEncoder.encode(i, "UTF-8")
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
    private fun checkemoji(text: Char): Boolean {
        val type: Int = Character.getType(text)
        return  (type == Character.SURROGATE.toInt() || type == Character.OTHER_SYMBOL.toInt())
    }
    fun boardEditInsert(mainProfile: MultipartBody.Part) {


        if (overlap.value ==false) {
            overlap.value = true

            val board = JsonObject()
            board.addProperty("b_type", "BOARD")
//        board.addProperty("ub_type", myUserData.value?.u_type)
//        board.addProperty("u_seq", myUserData.value?.u_type)
            board.addProperty("ub_type", "")
            board.addProperty("u_seq", myUserData.value?.u_seq)
            board.addProperty("b_contents", b_contents.value!!)
            board.addProperty("c_seq", filterArray[filterPosition.value!!])
            board.addProperty("b_seq", anonymousForumDeepPostData.value?.b_seq)


            var dir = myu_uuid.value
            var call: Call<ResultBoardInsertData>? =
                NetworkManager.serveradapter!!.create(PostService::class.java)
                    .boardEditInsert(
                        mainProfile,
                        requestbodyString(
                            dir
                            !!
                        ),
                        board
                    )

            call!!.enqueue(object : retrofit2.Callback<ResultBoardInsertData> {
                override fun onResponse(
                    call: Call<ResultBoardInsertData>,
                    response: retrofit2.Response<ResultBoardInsertData>
                ) {
                    if (response.isSuccessful) {

                        var result = response.body()
                        var message = result!!.message
                        var type = result!!.type
                        if (type != null) {
                            boardInsertCall.value = type.equals("success")
                        } else {
                            boardInsertCall.value = false
                        }


                    }
                    overlap.value = false
                }

                override fun onFailure(call: Call<ResultBoardInsertData>, t: Throwable) {
                    Log.d("onFailure", t.toString())
                    overlap.value = false
                }
            })
        }
    }

    fun requestbodyString(text: String): RequestBody {
        return text.toRequestBody("text/plain".toMediaType())
    }


//    fun encode(s: String?): String? {
//        return StringEscapeUtils.escapeJava(s)
//    }
//
    fun encode(text: String): String {
        var result: String = ""
        var set = ""
        try {
            // var list =text.toCharArray()
            val results: MutableList<String> = java.util.ArrayList()
            val m: Matcher =
                Pattern.compile("\\P{M}\\p{M}*+").matcher(text)
            while (m.find()) {
                results.add(m.group())
            }
            for (i in results) {
                if (checkemoji(i.toCharArray()[0])) {

//                    result += URLEncoder.encode(i, "UTF-8")
//                    Log.d("EMOJISET1", i.toString())
                    result +=   StringEscapeUtils.escapeJava(i)
                    Log.d("EMOJISET1", i.toString())

                } else {
                    result += i
                    Log.d("EMOJISET2", i.toString())
                }
            }


        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
    Log.d("EMOJISET3", result.toString())
        return result
    }

    fun boardNoPhotoInsert() {

        if (overlap.value ==false){
            overlap.value=true


        val board = JsonObject()
        board.addProperty("b_type", "BOARD")
//        board.addProperty("ub_type", myUserData.value?.u_type)
//        board.addProperty("u_seq", myUserData.value?.u_type)
        board.addProperty("ub_type", "")
        board.addProperty("u_seq", myUserData.value?.u_seq)
        board.addProperty("b_contents",
//            encodeset(
//            encode(
                b_contents.value!!)
//            )
//        )
        board.addProperty("c_seq", filterArray[filterPosition.value!!])





        var dir= myu_uuid.value
        var call: Call<ResultBoardInsertData>?= NetworkManager.serveradapter!!.create(PostService::class.java)
            .boardNoPhotoInsert(
                requestbodyString(dir
                !!),
                board
            )

        call!!.enqueue(object : retrofit2.Callback<ResultBoardInsertData>{
            override fun onResponse(call: Call<ResultBoardInsertData>, response: retrofit2.Response<ResultBoardInsertData>) {
                if(response.isSuccessful) {

                    var result=response.body()
                    var message= result!!.message
                    var type=result!!.type
                    if(type!=null) {
                        boardInsertCall.value = type.equals("success")
                    }
                    else{
                        boardInsertCall.value =false
                    }


                }
                overlap.value = false
            }
            override fun onFailure(call: Call<ResultBoardInsertData>, t: Throwable) {
                Log.d("onFailure",t.toString())
                overlap.value = false
            }
        })
        }
    }



    fun boardNoPhotoEditInsert() {

        if (overlap.value == false) {
            overlap.value = true
            val board = JsonObject()
            board.addProperty("b_type", "BOARD")
//        board.addProperty("ub_type", myUserData.value?.u_type)
//        board.addProperty("u_seq", myUserData.value?.u_type)
            board.addProperty("ub_type", "")
            board.addProperty("u_seq", myUserData.value?.u_seq)
            //  board.addProperty("b_contents",b_contents.value )
            board.addProperty("b_contents", b_contents.value!!)
            board.addProperty("c_seq", filterArray[filterPosition.value!!])
            board.addProperty("b_seq", anonymousForumDeepPostData.value?.b_seq)


            var dir = myu_uuid.value
            var call: Call<ResultBoardInsertData>? =
                NetworkManager.serveradapter!!.create(PostService::class.java)
                    .boardNoPhotoEditInsert(
                        requestbodyString(
                            dir
                            !!
                        ),
                        board
                    )

            call!!.enqueue(object : retrofit2.Callback<ResultBoardInsertData> {
                override fun onResponse(
                    call: Call<ResultBoardInsertData>,
                    response: retrofit2.Response<ResultBoardInsertData>
                ) {
                    if (response.isSuccessful) {

                        var result = response.body()
                        var message = result!!.message
                        var type = result!!.type
                        if (type != null) {
                            boardInsertCall.value = type.equals("success")
                        } else {
                            boardInsertCall.value = false
                        }


                    }
                    overlap.value = false
                }

                override fun onFailure(call: Call<ResultBoardInsertData>, t: Throwable) {
                    Log.d("onFailure", t.toString())
                    overlap.value = false
                }
            })
        }
    }

}