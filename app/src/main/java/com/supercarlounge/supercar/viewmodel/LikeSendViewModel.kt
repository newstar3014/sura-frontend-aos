package com.supercarlounge.supercar.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.supercarlounge.supercar.Network.MatchingService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.data.ProfileMatchingData
import com.supercarlounge.supercar.data.ResultData
import com.supercarlounge.supercar.data.UserInformationData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class LikeSendViewModel : ViewModel() {
    var my_seq = MutableLiveData<String>()
    var sub_seq = MutableLiveData<String>()
    var titleText = MutableLiveData<String>("")
    var time = MutableLiveData<String>("")
    var inputtext = MutableLiveData<String>("")
    var subText = MutableLiveData<String>("")
    var flexcheck = MutableLiveData<Boolean>(false)
    var matching_data: MutableLiveData<ProfileMatchingData> = MutableLiveData()
    var infodata: MutableLiveData<UserInformationData> = MutableLiveData()
    var toast = MutableLiveData<String>("")
    var isfinish = MutableLiveData<Boolean>(false)
    var nick = MutableLiveData<String>("")
    var isdirect = MutableLiveData<Boolean>(false)
    var send_b_seq = MutableLiveData<String>("")
    var given_nickname = MutableLiveData<String>("")
    var position = MutableLiveData<Int>(0)
    var hogamSendCheck = MutableLiveData<Boolean>(false)
    var bubbleRick = MutableLiveData<Boolean>(false)
    var isnotheart = MutableLiveData<Boolean>(false)
    fun GetMatchingData(u_seq: String, sub_seq: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(MatchingService::class.java)
                .GetMatchingData(u_seq, sub_seq)
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
                                if (data != null)
                                    matching_data.value = data!!
                            }
                        }
                    } else {

                    }
                }

                override fun onFailure(call: Call<ProfileMatchingData>, t: Throwable) {
                    Log.d("MatchingDataError", t.message!!)
                }
            })
        }
    }

    fun LikeSendSet() {
        if (inputtext.value!!.isNotEmpty()) {
            if (matching_data.value != null) {

                LikeSend()
            } else {
                if (isdirect.value!!) {
                    LikeSendMiniDirect()
                } else {
                    LikeSendMini()
                }

            }
        } else {
            toast.value = "메시지를 작성해 주세요"
        }

    }

    fun LikeSend() {
        if (bubbleRick.value == false) {
            bubbleRick.value = true

            var rdata = matching_data.value!!
            var pc_seq = rdata.pc_seq
            var u_seq = my_seq.value
            var sub_seq = ""
            if (rdata.subUser.toString().equals(u_seq)) {
                sub_seq = rdata.give_seq.toString()
            } else {
                sub_seq = rdata.subUser.toString()
            }
            var bseq = rdata.b_seq!!
            var giveNick = rdata.re_nickname
            var givennick = rdata.given_nickname
            var isfriend = rdata.isFriend.equals("Y")
            var message = inputtext.value!!

            var heartset = ""
            val JsonObject = JsonObject()
            if (flexcheck.value!!) {
                heartset = "20"
                Log.d("LikeSend", flexcheck.value.toString())
                JsonObject.addProperty("hl_type", "플렉스 호감 보내기")
            } else {
                heartset = "10"
                Log.d("LikeSend", flexcheck.value.toString())
                JsonObject.addProperty("hl_type", "호감 보내기")
            }


            JsonObject.addProperty("b_seq", bseq)
            JsonObject.addProperty("pc_seq", pc_seq)
            JsonObject.addProperty("u_seq", u_seq)
            JsonObject.addProperty("sub_seq", sub_seq)
            JsonObject.addProperty("u_heart", heartset)
            JsonObject.addProperty("pc_message", message)
            JsonObject.addProperty("giveNick", giveNick)
            JsonObject.addProperty("givenNick", givennick)

            val sendjson = JsonObject()
            sendjson.add("obj", JsonObject)
            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter!!.create(MatchingService::class.java)
                    .LikeSend(sendjson)
                d!!.enqueue(object : retrofit2.Callback<ResultData> {
                    override fun onResponse(
                        call: retrofit2.Call<ResultData>,
                        response: Response<ResultData>
                    ) {
                        var data = response.body()
                        if (data != null) {
                            var type = data.type
                            if (type.equals("success")) {
                                isfinish.value = true
                            } else if (type.equals("notheart")) {
                                isnotheart.value = true

                            }
                        }
                        bubbleRick.value = false
                    }

                    override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                        Log.d("failer", t.message!!)
                        bubbleRick.value = false
                    }

                })

            }
        }
    }

    fun LikeSendMini() {
        if (bubbleRick.value == false) {
            bubbleRick.value = true

            var heartset = 10
            var message = inputtext.value!!
            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter!!.create(MatchingService::class.java)
                    .MiniSendLIke(my_seq.value!!, sub_seq.value, heartset, message, nick.value!!)
                d!!.enqueue(object : retrofit2.Callback<ResultData> {
                    override fun onResponse(
                        call: retrofit2.Call<ResultData>,
                        response: Response<ResultData>
                    ) {
                        var data = response.body()
                        if (data != null) {
                            var type = data.type
                            if (type.equals("success")) {
                                isfinish.value = true
                            } else if (type.equals("notheart")) {
                                isnotheart.value = true
                            } else {
                                toast.value = message
                            }

                        }
                        bubbleRick.value = false

                    }

                    override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                        Log.d("failer", t.message!!)
                        bubbleRick.value = false
                    }

                })

            }
        }
    }

    fun LikeSendMiniDirect() {
        if (bubbleRick.value == false) {

            bubbleRick.value = true
            var heartset = 10
            var message = inputtext.value!!
            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter!!.create(MatchingService::class.java)
                    .directHogam(
                        my_seq.value,
                        sub_seq.value,
                        heartset,
                        message,
                        nick.value.toString(),
                        send_b_seq.value
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
                                hogamSendCheck.value = true
                                isfinish.value = true

                            } else if (type.equals("notheart")) {
                                isnotheart.value = true
                            } else {
                                toast.value = message
                            }

                        }
                        bubbleRick.value = false

                    }

                    override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                        Log.d("failer", t.message!!)
                        bubbleRick.value = false
                    }

                })

            }
        }
    }

    fun encode(text: String): String {
        var result: String = ""
        try {
            result = URLEncoder.encode(text, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return result
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

    fun encodeset(text: String): String {
        var result: String = ""
        var set = ""
        try {
            // var list =text.toCharArray()
            val results: MutableList<String> = ArrayList()
            val m: Matcher =
                Pattern.compile("\\P{M}\\p{M}*+").matcher(text)
            while (m.find()) {
                results.add(m.group())
            }
            for (i in results) {
                if (checkemoji(i.toCharArray()[0])) {

                    result += URLEncoder.encode(i, "UTF-8")
                    Log.d("EMOJISET1", i.toString())

                } else {
                    result += i
                    Log.d("EMOJISET3", i.toString())
                }
            }


        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return result
    }

    private fun checkemoji(text: Char): Boolean {
        val type: Int = Character.getType(text)
        return (type == Character.SURROGATE.toInt() || type == Character.OTHER_SYMBOL.toInt())
    }

    // convert UTF-8 to internal Java String format
    fun convertUTF8ToString(s: String): String? {
        var out: String? = null
        out = String(s.toByteArray(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8)
        return out
    }

    // convert internal Java String format to UTF-8
    fun convertStringToUTF8(s: String): String? {
        return String(s.toByteArray(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1)
    }
}