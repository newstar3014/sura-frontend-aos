package com.supercarlounge.supercar.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.supercarlounge.supercar.Network.DriveService
import com.supercarlounge.supercar.Network.LocationService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.Network.PostService
import com.supercarlounge.supercar.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.regex.Matcher
import java.util.regex.Pattern


class LocationWritingViewModel : ViewModel() {

    var titleText = MutableLiveData<String>("드라이브 등")
    var time = MutableLiveData<String>("")
    var my_seq = MutableLiveData<String>("")
    var myu_uuid = MutableLiveData<String>("")
    var mainImageUrl = MutableLiveData<String>("")
    var isvipmode = MutableLiveData<Boolean>(false)
    var dtitletext = MutableLiveData<String>("")
    var dcontenttext = MutableLiveData<String>("")
    var dcontentCount = MutableLiveData<String>("0")
    var ispost = MutableLiveData<Boolean>(false)
    var sendsuccess = MutableLiveData<String>("")
    var drivedata = MutableLiveData<DriveAwayData>()
    var isstart = MutableLiveData<Boolean>(false)
    var mapVisible = MutableLiveData<Boolean>(false)
    var successText = MutableLiveData<Boolean>(false)

    var myCarPosition1Check = MutableLiveData<Boolean>(false)
    var myCarPosition2Check = MutableLiveData<Boolean>(false)
    var myCarPosition3Check = MutableLiveData<Boolean>(false)
    var isownercheck = MutableLiveData<Boolean>(false)

    var istoast = MutableLiveData<String>()
    var viptype = MutableLiveData<Int>(0)
    var vip_heart = MutableLiveData<String>("0")
    var auctiondata = MutableLiveData<VipDriveAuctionData>()
    var like_drive_date = MutableLiveData<Int>(0)
    var like_drive_type = MutableLiveData<Int>(0)
    var position = MutableLiveData<Int>(0)
    var isagreeview = MutableLiveData<Boolean>(false)
    var viewvip = MutableLiveData<Boolean>(false)
    var my_car_data_list: MutableLiveData<ArrayList<ArrayList<UserInformationCarData>>> =
        MutableLiveData(arrayListOf())
    var myCarData = MutableLiveData<UserInformationCarData>()
    var apicall = MutableLiveData<Boolean>(false)
    var myCarChecked1 = MutableLiveData<Boolean>(false)
    var myCarChecked2 = MutableLiveData<Boolean>(false)
    var myCarChecked3 = MutableLiveData<Boolean>(false)
    var image_list: MutableLiveData<ArrayList<String>> = MutableLiveData(arrayListOf())
    var eventImage = MutableLiveData<Int>()
    var eventAddAndCancel = MutableLiveData<Int>()

    var isDriveCourse = MutableLiveData<Boolean>(false)
    var isDiningRoom = MutableLiveData<Boolean>(false)
    var isCafe = MutableLiveData<Boolean>(false)
    var isEtc = MutableLiveData<Boolean>(false)

    var tvNomination = MutableLiveData<String>("")
    var tvAddress = MutableLiveData<String>("")
    var tvPhoneNumber = MutableLiveData<String>("")
    var tvOpeningHours = MutableLiveData<String>("")
    var tvParking = MutableLiveData<String>("")


    var isMainEdit = MutableLiveData<Boolean>(false)
    var isNominationEdit = MutableLiveData<Boolean>(false)
    var isAddressEdit = MutableLiveData<Boolean>(false)
    var isPhoneNumberEdit = MutableLiveData<Boolean>(false)
    var isEatEdit = MutableLiveData<Boolean>(false)
    var tvEat = MutableLiveData<String>("")
    var isOpeningHoursEdit = MutableLiveData<Boolean>(false)
    var isParkingEdit = MutableLiveData<Boolean>(false)
    var lat = MutableLiveData<String>("")
    var lon = MutableLiveData<String>("")
    var addressSearch = MutableLiveData<Boolean>(false)

    fun addressSearch(b: Boolean) {
        addressSearch.value = b
    }


    fun eventImage(index: Int) {
        eventImage.value = index
    }

    fun eventAddAndCancel(index: Int) {
        eventAddAndCancel.value = index
    }


    fun setvipmode() {
        viewvip.value = true

    }

    fun setlikedate(index: Int) {
        like_drive_date.value = index
    }

    fun setliketype(index: Int) {
        like_drive_type.value = index
    }

    fun onisownercheck(boolean: Boolean) {
        isownercheck.value = boolean
    }


    fun getmultipart(
        file: File,
        name: String,
        type: String,
        typeset: String,
        formname: String,
    ): MultipartBody.Part {

        var name1 = name + time + type
        var requestBody: RequestBody = file.asRequestBody(typeset.toMediaTypeOrNull())
        var body: MultipartBody.Part =
            MultipartBody.Part.createFormData(formname, name1, requestBody)
        return body
    }

    fun boardInsert(imageList: ArrayList<MultipartBody.Part>) {

        var spot_cate = ""
//        ["title":"영업시간","content":time]
        if (isDriveCourse.value == true) {
            spot_cate = "드라이브 코스"
        } else if (isDiningRoom.value == true) {
            spot_cate = "맛집"
        } else if (isCafe.value == true) {
            spot_cate = "카페"
        } else if (isEtc.value == true) {
            spot_cate = "기타"
        }
        val jArray = JSONArray() //배열

//        val sb = StringBuffer()
//        val str = "[{'title':'종류','content':''}," +
//                "{'id':'BBB','name':'구상진','pwd':'222'}," +
//                "{'id':'CCC','name':'바디리','pwd':'333'}]"

        val sObject3 = JSONObject()
        val sObject4 = JSONObject()


//        val jarray = JSONArray(str)

        sObject3.put("title", "영업시간")
        sObject3.put("content", tvOpeningHours.value)
        sObject4.put("title", "주차")
        sObject4.put("content", tvParking.value)


        jArray.put(sObject3)
        jArray.put(sObject4)

        Log.d("제이슨 데이터", jArray.toString())
        if (apicall.value == false) {
            apicall.value = true


            var dir = myu_uuid.value
            var call: Call<ResultData>? = null

            call = NetworkManager.serveradapter.create(LocationService::class.java)
                .setLocationWriting(
                    requestbodyString(dir.toString()),
                    imageList,
                    requestbodyString(my_seq.value.toString()),
                    requestbodyString(getSpotArea(tvNomination.value.toString())),
                    requestbodyString(getSpotDetailArea(tvNomination.value.toString())),
                    requestbodyString(spot_cate),
                    requestbodyString(dtitletext.value.toString()),
                    requestbodyString(dcontenttext.value.toString()),
                    requestbodyString(tvNomination.value.toString()),
                    requestbodyString(tvAddress.value.toString()),
                    requestbodyString(tvPhoneNumber.value.toString()),
                    requestbodyString(jArray.toString()),
                    requestbodyString(""),
                    requestbodyString(""),
                    requestbodyString(lat.value.toString()),
                    requestbodyString(lon.value.toString())
                )

            call!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: Call<ResultData>,
                    response: retrofit2.Response<ResultData>
                ) {
                    if (response.isSuccessful) {
                        sendsuccess.value = "success"

                    }
                    apicall.value = false
                }

                override fun onFailure(call: Call<ResultData>, t: Throwable) {
                    Log.d("onFailure", t.toString())
                    apicall.value = false
                }
            })
        }

    }

    fun getSpotArea(s: String): String {
        var result = ""
        var area = s.split(" ")

        if (area.isNotEmpty()) {
            if (area.contains("충북") || area.contains("충남")) {
                result = "충청도"
            } else if (area.contains("전북") || area.contains("전남")) {
                result = "전라도"
            } else if (area.contains("경남") || area.contains("경북")) {
                result = "전라도"
            } else if (area.contains("강원")) {
                result = "강원도"
            } else if (area.contains("세종")) {
                result = "세종"
            } else {
                if (area.isNotEmpty()) {

                    result = area[0].toString()
                }
            }

        }

        return result


    }

    fun getSpotDetailArea(s: String): String {
        var result = ""
        var area = s.split(" ")

        if (area.isNotEmpty()) {

            if (area.size >= 2) {
                var ends = area[1]
                if (area.contains("서울")) {
                    result = ends
                } else if (area.contains("경기")) {
                    result = ends
                } else {
                    result = ""
                }


            }
        }

        return result


    }


    fun boardEditInsert(
        path: String?,
        b_profile: Boolean,
        b_payment: Boolean,
        b_same_gender: Boolean
    ) {
        if (apicall.value == false) {
            apicall.value = true

            if (dtitletext.value.isNullOrEmpty()) {
                istoast.value = "제목을 입력해주세요."
                apicall.value = false
                return
            }


            if (dtitletext.value!!.length > 40) {
                istoast.value = "제목은 40자 이내로 작성해 주세요."
                apicall.value = false
                return
            }
            if (dcontenttext.value.isNullOrEmpty()) {
                istoast.value = "내용을 입력해주세요."
                apicall.value = false
                return
            }
            if (dcontenttext.value!!.length > 2000) {
                istoast.value = "내용은 2000자 이내로 작성해 주세요."
                apicall.value = false
                return
            }
            var multipart: MultipartBody.Part? = null
            if (!mainImageUrl.value.isNullOrEmpty()) {
                if (!path.isNullOrEmpty()) {
                    var file = File(path)
                    multipart = getmultipart(file, file.name, ".jpg", "image/*", "boardCover")
                }
            } else {
                istoast.value = "사진을 등록해주세요."
                apicall.value = false
                return
            }

            val board = JsonObject()

            board.addProperty("b_type", "DRIVE")
            board.addProperty("ub_type", if (isownercheck.value!!) "DRIVER" else "PASSENGER")
            board.addProperty("u_seq", my_seq.value.toString())
            board.addProperty("b_title", dtitletext.value!!)
            board.addProperty("b_contents", dcontenttext.value!!)

            board.addProperty("c_seq", "")

            if (isownercheck.value!!) {
                board.addProperty("uc_seq", myCarData.value?.uc_seq)
            } else {
                board.addProperty("uc_seq", 0)
            }
            board.addProperty("b_seq", drivedata?.value?.b_seq)
            board.addProperty("b_how", like_drive_type.value)
            board.addProperty("b_when", like_drive_date.value)
            board.addProperty(
                "b_profile", if (b_profile) {
                    "Y"
                } else {
                    "N"
                }
            )
            board.addProperty(
                "b_payment", if (b_payment) {
                    "Y"
                } else {
                    "N"
                }
            )
            board.addProperty(
                "b_same_gender", if (b_same_gender) {
                    "Y"
                } else {
                    "N"
                }
            )

            Log.d("글쓰기 오너", board.toString())
            var dir = myu_uuid.value
            var call: Call<ResultBoardInsertData>? = null
            if (multipart != null) {
                call = NetworkManager.serveradapter!!.create(PostService::class.java)
                    .boardEditInsert(
                        multipart!!,
                        requestbodyString(
                            dir
                            !!
                        ),
                        board
                    )
            } else {
                call = NetworkManager.serveradapter!!.create(PostService::class.java)
                    .boardNoPhotoEditInsert(
                        requestbodyString(
                            dir
                            !!
                        ),
                        board
                    )
            }
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
                            var viptypeset = viptype.value
                            if (viptypeset == 1 || viptypeset == 2) {

                                var seq = drivedata.value!!.b_seq.toString()
                                VipDriveUpload(seq)

                            } else {
                                sendsuccess.value = "normal"
                            }
                        } else {
                            sendsuccess.value = "false"
                        }


                    }
                    apicall.value = false
                }

                override fun onFailure(call: Call<ResultBoardInsertData>, t: Throwable) {
                    Log.d("onFailure", t.toString())
                    apicall.value = false
                }
            })
        }
    }


    fun setpost() {

        ispost.value = true
    }

    fun requestbodyString(text: String): RequestBody {
        return text.toRequestBody("text/plain".toMediaType())
    }

    fun GetMyNearDrive() {

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(DriveService::class.java)
                .NearDrive(my_seq.value!!)
            d!!.enqueue(object : retrofit2.Callback<ResultNearDriveData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultNearDriveData>,
                    response: Response<ResultNearDriveData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        var message = data.message
                        if (type.equals("success")) {
                            var rows = data.rows
                            if (rows.size != 0) {

                            } else {

                            }
                        } else {

                        }


                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultNearDriveData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                }

            })

        }

    }

    @SuppressLint("SuspiciousIndentation")
    fun VipDriveUpload(b_seq: String) {
        var json = JsonObject()
        json.addProperty("u_seq", my_seq.value)
        json.addProperty("heart_count", vip_heart.value.toString())
        json.addProperty("b_seq", b_seq)

        var jsonsend = JsonObject()
        jsonsend.add("driveObj", json)
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(DriveService::class.java)
                .VipDrivePost(jsonsend)
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

                            sendsuccess.value = "vip"
                        } else {

                            sendsuccess.value = "false"
                        }


                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                }

            })

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
}