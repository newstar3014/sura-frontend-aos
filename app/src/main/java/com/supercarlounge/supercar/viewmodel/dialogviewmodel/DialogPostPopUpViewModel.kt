package com.supercarlounge.supercar.viewmodel.dialogviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DialogPostPopUpViewModel : ViewModel() {

    var tv_ok = MutableLiveData<String>("")
    var tv_cancel = MutableLiveData<String>("")

    //게시글 및 댓글 대댓글 seq
    var b_seq = MutableLiveData<Int>(0)

    var tv_main = MutableLiveData<String>("이 드라이브글을\n차단하시겠습니까?")

    var viewtype = MutableLiveData<Int>(0)

//    fun insertSubComMainLike() {
//        val data = JsonObject()
//        data.addProperty("u_seq", my_u_seq.value)
//        data.addProperty("b_seq",b_seq.value)
//        data.addProperty("cs_seq", cs_seq.value)
//        data.addProperty("like_type", comment_like_type.value)
//        val obj = JsonObject()
//        obj.add("obj", data)
//
//        CoroutineScope(Dispatchers.IO).launch {
//            val d = NetworkManager.serveradapter!!.create(InsertService::class.java).insertSubComMainLike(obj)
//            d!!.enqueue(object : retrofit2.Callback<ResultData> {
//                override fun onResponse(
//                    call: retrofit2.Call<ResultData>,
//                    response: Response<ResultData>
//                ) {
//                    var data = response.body()
//                    Log.d("check", data.toString())
//                    if (data != null) {
//
//                        var rows = data.message
//                        Log.d("결과", rows)
//                        if (rows.equals("좋아요가 등록 되었습니다")|| rows.equals("좋아요가 해제 되었습니다")) {
//                            getAnonymousForumComment(my_u_seq.value!!.toString(),b_seq.value!!.toString())
//
//                        }else{
//
//                        }
//
//                    }else{
//
//                    }
//
//                }
//
//                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
//                    Log.d("check", "failer")
//                }
//
//            })
//
//        }
//
//
//    }
//
//    fun insertDeclaration() {
//        val data = JsonObject()
//        data.addProperty("u_seq", my_u_seq.value)
//        data.addProperty("b_seq",b_seq.value)
//        data.addProperty("cs_seq", cs_seq.value)
//        data.addProperty("like_type", comment_like_type.value)
//        val obj = JsonObject()
//        obj.add("obj", data)
//
//        CoroutineScope(Dispatchers.IO).launch {
//            val d = NetworkManager.serveradapter!!.create(InsertService::class.java).insertSubComMainLike(obj)
//            d!!.enqueue(object : retrofit2.Callback<ResultData> {
//                override fun onResponse(
//                    call: retrofit2.Call<ResultData>,
//                    response: Response<ResultData>
//                ) {
//                    var data = response.body()
//                    Log.d("check", data.toString())
//                    if (data != null) {
//
//                        var rows = data.message
//                        Log.d("결과", rows)
//                        if (rows.equals("좋아요가 등록 되었습니다")|| rows.equals("좋아요가 해제 되었습니다")) {
//                            getAnonymousForumComment(my_u_seq.value!!.toString(),b_seq.value!!.toString())
//
//                        }else{
//
//                        }
//
//                    }else{
//
//                    }
//
//                }
//
//                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
//                    Log.d("check", "failer")
//                }
//
//            })
//
//        }
//
//
//    }


}