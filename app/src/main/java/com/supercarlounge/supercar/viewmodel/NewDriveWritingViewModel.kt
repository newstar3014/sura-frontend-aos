package com.supercarlounge.supercar.viewmodel

import android.annotation.SuppressLint
import android.os.Environment
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.supercarlounge.supercar.Network.*
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
import retrofit2.Call
import retrofit2.Response
import java.io.File


class NewDriveWritingViewModel : ViewModel() {

    var my_seq = MutableLiveData<String>("")
    var titleText = MutableLiveData<String>("?")
    var mainText = MutableLiveData<String>("")
    var successText= MutableLiveData<Boolean>(false)
    var subText = MutableLiveData<String>("초대 코드를 입력해 주세요. \n" + "초대 코드가 입력된 가입신청은 \n" + "우선적으로 가입심사를 해 드립니다.")
    var bottomText = MutableLiveData<String>("다음")

    var detaildata = MutableLiveData<LocationSpotData>()
    var writingType = MutableLiveData<String>("기본")
    var location_seq = MutableLiveData<String>()
    var state = MutableLiveData<Int>(1)
    var nickname = MutableLiveData<String>("")
    var toast = MutableLiveData<String>("")
    var bubbleRick = MutableLiveData<Boolean>(false)
    var selectproindex = MutableLiveData<Int>(0)
    var endstate = MutableLiveData<Int>(0)
    var totalCount =  MutableLiveData<Int>(0)

    var defaultImage =   MutableLiveData<Boolean>()
    var drivedata = MutableLiveData<DriveAwayData>()
    var like_drive_date = MutableLiveData<Int>(0)
    var like_drive_type = MutableLiveData<Int>(0)

    var open_profile_seq = MutableLiveData<String>("")
    var selectprodata = MutableLiveData<ProfileSuggestionData>()
    var back = MutableLiveData<Boolean>(false)
    var next = MutableLiveData<Boolean>(false)
    var isnotheart = MutableLiveData<Boolean>(false)
    val profilesuggest: MutableLiveData<ArrayList<ProfileSuggestionData>> =
        MutableLiveData(arrayListOf())
    var backEvent = MutableLiveData<Boolean>(false)
    var nextEvent = MutableLiveData<Boolean>(false)
    var time = MutableLiveData<String>("")
    var nameTotalCount = MutableLiveData<String>("8")
    var nameNowCount = MutableLiveData<String>("0")
    var position = MutableLiveData<Int>(0)
    var type1 = MutableLiveData<Boolean>()
    var type2 = MutableLiveData<Boolean>()
    var permissionEvent = MutableLiveData<Boolean>(false)
    var eventAroundMe = MutableLiveData<Boolean>(false)
    var isPermission = MutableLiveData<Boolean>(false)


    var type3 = MutableLiveData<Boolean>()
    var type4 = MutableLiveData<Boolean>()

    var type5 = MutableLiveData<Boolean>()
    var type6 = MutableLiveData<Boolean>()


    var isMainEdit = MutableLiveData<Boolean>(false)
    var day= MutableLiveData<Int>(0)
    var week= MutableLiveData<String>("")
    var month= MutableLiveData<Int>(0)
    var year= MutableLiveData<Int>(0)
    var start = MutableLiveData<Int>(0)
    var hour =MutableLiveData<String>("")
    var min =MutableLiveData<String>("")

    var monthString =MutableLiveData<String>("")
    var yearString =MutableLiveData<String>("")
    var end = MutableLiveData<Int>(0)
    var date = MutableLiveData<Int>(0)
    var viewvip = MutableLiveData<Boolean>(false)
    var isagreeview = MutableLiveData<Boolean>(false)
    var typeUp = MutableLiveData<Boolean>(false)
    var typeDown = MutableLiveData<Boolean>(false)
    var hourUp = MutableLiveData<Boolean>(false)
    var hourDown = MutableLiveData<Boolean>(false)
    var minuteUp = MutableLiveData<Boolean>(false)
    var minuteDown = MutableLiveData<Boolean>(false)
    var sendsuccess = MutableLiveData<String>("")
    var sendVip = MutableLiveData<String>("")
    var b_seq = MutableLiveData<String>("")

    var nextMonth = MutableLiveData<Boolean>()
    var backMonth = MutableLiveData<Boolean>()


    var selectDay = MutableLiveData<String>("")
    var selectTime = MutableLiveData<String>("")
    var stateBottomText = MutableLiveData<Boolean>(false)

    var typeY = MutableLiveData<Int>(0)
    var hourY = MutableLiveData<Int>(0)
    var minY = MutableLiveData<Int>(0)
    var type = MutableLiveData<Int>(0)
    var nowHour = MutableLiveData<String>("00")
    var nowMin = MutableLiveData<String>("00")
    var ampm = MutableLiveData<Int>(1)
    var nottext = MutableLiveData<String>("등록된 추천장소가 없습니다.")
    var spotlist = MutableLiveData<ArrayList<LocationSpotData>>(ArrayList())
    var apicallspot= MutableLiveData<Boolean>(false)
    var ppp = MutableLiveData<Int>(10)
    var page = MutableLiveData<Int>(1)
    var order = MutableLiveData<String>("최신순")
    var arraytype = MutableLiveData<ArrayList<String>>(arrayListOf("전체","내주변","서울","경기","그 외","드라이브 코스","파인다이닝","카페","맛집"
        ,"심야","북마크"))
    var arraytype3 = MutableLiveData<ArrayList<String>>(arrayListOf("누구든 드라이브는 다 좋아요!",
        "차량 여러대가 만나는 드라이브!",
        "설레는 데이트를 하고 싶어요!",
        "오늘은 차 없이 술 한 잔 어때요?",
        "편하게 커피 한 잔 좋아요!",
        "2:2, 3:3 친구들과 같이 만나요!",
        "맛집 같이 가요!"
        ))
    var arrayBooleantype3 = MutableLiveData<ArrayList<Boolean>>(arrayListOf(false,
        false,
        false,
        false,
        false,
        false,
        false
    ))

    var boardInsertCall = MutableLiveData<Boolean>(false)
    var overlap = MutableLiveData<Boolean>(false)

    fun setvipmode() {
        viewvip.value = true

    }

    var select_index = MutableLiveData<Int>(999999)
    var  nowtypeindex = MutableLiveData<Int>(0)
    var  nowtypeindex3 = MutableLiveData<Int>(0)


    var my_car_data_list: MutableLiveData<java.util.ArrayList<java.util.ArrayList<UserInformationCarData>>> =
        MutableLiveData(arrayListOf())
    var myCarData = MutableLiveData<UserInformationCarData>()
    var apicall = MutableLiveData<Boolean>(false)
    var myCarChecked1 = MutableLiveData<Boolean>(false)
    var myCarChecked2 = MutableLiveData<Boolean>(false)
    var myCarChecked3 = MutableLiveData<Boolean>(false)
    var isownercheck = MutableLiveData<Boolean>(false)
    var viptype = MutableLiveData<Int>(0)
    var vip_heart = MutableLiveData<String>("0")
    var isowner = MutableLiveData<Boolean>(false)
    var myu_uuid = MutableLiveData<String>("")
    var mainImageUrl = MutableLiveData<String>("")
    var dtitletext = MutableLiveData<String>("")
    var dcontenttext = MutableLiveData<String>("")
    var isDrivePass = MutableLiveData<Boolean>(false)
    var isDrivePassCount = MutableLiveData<Int>(0)
    fun arrayBooleantype3Event(index: Int){

        if (arrayBooleantype3.value?.get(index) == true){
            arrayBooleantype3.value?.set(index,false)
        }else{
            for ((count, i) in arrayBooleantype3.value!!.withIndex()){
              arrayBooleantype3.value!![count] = false
            }
            arrayBooleantype3.value?.set(index,true)
        }
        arrayBooleantype3.value = arrayBooleantype3.value



    }
    fun backEvent(b: Boolean){
        backEvent.value = b
    }
    fun nextEvent(b: Boolean){
        nextEvent.value = b
    }
    fun getSpotList(isreset:Boolean){
        if (apicallspot.value ==false) {
            apicallspot.value = true



            var choice ="전체"
            var searchtype =  arraytype.value!![nowtypeindex.value!!]

            if (nowtypeindex.value!! > 4){
                choice = searchtype
                searchtype = "전체"
            }
            if (searchtype == "그 외"){
                searchtype = "그외"
            }
            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter!!.create(LocationService::class.java)
                    .getSpotList(
                        page.value, ppp.value!!,my_seq.value!!,choice,order.value!!,"",searchtype,"",
//                        null,
                        "",""
                        ,null)
                d!!.enqueue(object : retrofit2.Callback<ResultLocationSpotData> {
                    override fun onResponse(
                        call: retrofit2.Call<ResultLocationSpotData>,
                        response: Response<ResultLocationSpotData>
                    ) {
                        var data = response.body()
                        if (data != null) {
                            var type = data.type
                            var message = data.message
                            if(type.equals("success")){
                                var rows = data.rows
                                if (data.totalCount != 0){
                                    totalCount.value = data.totalCount!!
                                }
                                if(isreset){
                                    spotlist.value = rows
                                }else {
                                    var nowlist = spotlist.value!!
                                    nowlist.addAll(rows)
                                    spotlist.value = nowlist
                                }
                            }else{

                            }


                        }
                        apicallspot.value = false
                    }

                    override fun onFailure(call: retrofit2.Call<ResultLocationSpotData>, t: Throwable) {
                        Log.d("check", t.toString())
                        apicallspot.value = false
                    }

                })

            }
        }
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
        var body: MultipartBody.Part = MultipartBody.Part.createFormData(formname, name1, requestBody)
        return body
    }
    fun  getOneSpot(spot_seq:String){
        if (apicallspot.value ==false) {
            apicallspot.value = true
            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter!!.create(LocationService::class.java)
                    .getOneSpot(
                        my_seq.value!!,spot_seq
                    )
                d!!.enqueue(object : retrofit2.Callback<ResultLocationSpotData> {
                    override fun onResponse(
                        call: retrofit2.Call<ResultLocationSpotData>,
                        response: Response<ResultLocationSpotData>
                    ) {
                        var data = response.body()
                        if (data != null) {
                            var type = data.type
                            var message = data.message
                            if(type.equals("success")){

                                var rows = data.rows
                                if (rows.size != 0) {

                                    spotlist.value = rows
                                    Log.d("데이터", rows.toString())
                                }

                            }else{

                            }


                        }
                        apicallspot.value = false
                    }

                    override fun onFailure(call: retrofit2.Call<ResultLocationSpotData>, t: Throwable) {
                        Log.d("check", t.toString())
                        apicallspot.value = false
                    }

                })

            }
        }
    }

    fun boardInsert(path: String, b_profile: Boolean, b_payment: Boolean, b_same_gender: Boolean) {

        if (apicall.value == false) {
            apicall.value = true



//        if(vip_heart.value.isNullOrEmpty() || vip_heart.value == "0"){
//            istoast.value = "사용할 하트를 입력해주세요."
//            return
//        }
//        if(isagreeview.value == false){
//            istoast.value = "약관에 동의해주세요."
//            return
//        }
            var multipart: MultipartBody.Part? = null
            if (!mainImageUrl.value.isNullOrEmpty()) {
//                val path = Environment.getExternalStoragePublicDirectory(
//                    Environment.DIRECTORY_PICTURES
//                )
//                val file = File(path, mainImageUrl.value)
//                if(!path.isDirectory) {
//                    path.mkdirs();
//                }
                var file = File(path)
//                if(!path.isDirectory) {
//                    path.mkdirs();
//                }
                if (!file.startsWith("https")) {
                    multipart = getmultipart(file, file.name, ".jpg", "image/*", "boardCover")
                }
            } else {
                toast.value = "사진을 등록해주세요."
                apicall.value = false
                return
            }

            var ub_type = ""

            if (type5.value == true) {
                ub_type = "DRIVER"
            } else {
                ub_type = "PASSENGER"
            }
            Log.d("ㅎㅇ", ub_type)
            var index = 0
            for ((count, i) in arrayBooleantype3.value!!.withIndex()){
                if (i){
                  index = count
                }
            }
            val board = JsonObject()
            board.addProperty("b_type", "DRIVE")
            board.addProperty("b_title", dtitletext.value!!)
            board.addProperty("ub_type", ub_type)
            board.addProperty("u_seq", my_seq.value.toString())
            board.addProperty("b_contents", dcontenttext.value!!)
            board.addProperty("c_seq", "")
            board.addProperty("b_when", if (type3.value!!) "0" else "1")
            board.addProperty("b_how",  index.toString())
            var url = ""
            if (defaultImage.value == true){
                url = mainImageUrl.value!!
            }
            board.addProperty("b_cover",url)
            //장소 선택할떄
            if (type2.value == true || location_seq.value?.isNotEmpty() == true){
                board.addProperty("spot_seq",  location_seq.value.toString())
            }else{
                board.addProperty("spot_seq",  "")
            }

            if (type4.value == true){
                board.addProperty("meeting_day",   dateMapping())
            }else{
                board.addProperty("meeting_day",   "")
            }

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
            if (type5.value == true) {
                board.addProperty("uc_seq", myCarData.value?.uc_seq)
            }
            Log.d("글쓰기 드라이브2", board.toString())
            var dir = myu_uuid.value
            var call: Call<ResultBoardInsertData>? = null
            if (multipart != null ) {
                if (defaultImage.value== true){
                    call = NetworkManager.serveradapter!!.create(PostService::class.java)
                        .boardNoPhotoInsert(
                            requestbodyString(
                                dir
                                !!
                            ),
                            board
                        )
                }else{
                    call = NetworkManager.serveradapter!!.create(PostService::class.java)
                        .boardInsert(
                            multipart!!,
                            requestbodyString(
                                dir
                                !!
                            ),
                            board
                        )
                }

            } else {
                call = NetworkManager.serveradapter!!.create(PostService::class.java)
                    .boardNoPhotoInsert(
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
                        Log.d("vip", result.toString())
                        if (type != null) {

                            state.value = state.value?.plus(1)
                            b_seq.value =  result!!.rows[0].b_seq


                            if (viptype.value == 1){
                                VipDriveUpload(b_seq.value.toString())
                            }


                                sendsuccess.value = "normal"

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


    fun boardEditInsert(
        path: String?,
        b_profile: Boolean,
        b_payment: Boolean,
        b_same_gender: Boolean
    ) {
        if (apicall.value == false) {
            apicall.value = true

            if (dtitletext.value.isNullOrEmpty()) {
                toast.value = "제목을 입력해주세요."
                apicall.value = false
                return
            }


            if (dtitletext.value!!.length > 40) {
                toast.value ="제목은 40자 이내로 작성해 주세요."
                apicall.value = false
                return
            }
            if (dcontenttext.value.isNullOrEmpty()) {
                toast.value = "내용을 입력해주세요."
                apicall.value = false
                return
            }
            if (dcontenttext.value!!.length > 2000) {
                toast.value = "내용은 2000자 이내로 작성해 주세요."
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
                toast.value = "사진을 등록해주세요."
                apicall.value = false
                return
            }

            val board = JsonObject()
            var ub_type = ""

            if (type5.value== true) {
                ub_type = "DRIVER"
            } else {
                ub_type = "PASSENGER"
            }
            Log.d("ㅎㅇ", ub_type)
            var index = 0
            for ((count, i) in arrayBooleantype3.value!!.withIndex()){
                if (i){
                    index = count
                }
            }
            board.addProperty("b_type", "DRIVE")
            board.addProperty("b_title", dtitletext.value!!)
            board.addProperty("ub_type", ub_type)
            board.addProperty("u_seq", my_seq.value.toString())
            board.addProperty("b_contents", dcontenttext.value!!)
            board.addProperty("c_seq", "")
            board.addProperty("b_when", if (type3.value!!) "0" else "1")
            board.addProperty("b_how",  index.toString())
            board.addProperty("b_seq",  drivedata.value?.b_seq)
            var url = ""
            if (defaultImage.value == true){
                url = mainImageUrl.value!!
            }
            board.addProperty("b_cover",url)
//            //장소 선택할떄
            if (type2.value == true){
                board.addProperty("spot_seq",  location_seq.value.toString())
            }else{
                board.addProperty("spot_seq",  "")
            }

            if (type4.value == true){
                board.addProperty("meeting_day",   dateMapping())
            }else{
                board.addProperty("meeting_day",   "")
            }

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
            if (type5.value ==true) {
                board.addProperty("uc_seq", myCarData.value?.uc_seq)
            }else{
                board.addProperty("uc_seq", "0")
            }
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

                if (defaultImage.value== true){
                    call = NetworkManager.serveradapter!!.create(PostService::class.java)
                        .boardNoPhotoEditInsert(
                            requestbodyString(
                                dir
                                !!
                            ),
                            board
                        )
                }else{
                    call = NetworkManager.serveradapter!!.create(PostService::class.java)
                        .boardEditInsert(
                            multipart!!,
                            requestbodyString(
                                dir
                                !!
                            ),
                            board
                        )
                }
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
                            state.value = state.value?.plus(1)
                            b_seq.value = drivedata.value!!.b_seq.toString()
//                                VipDriveUpload(seq)

                                sendsuccess.value = "normal"

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

                            sendVip.value = "vip"
                        } else {

                            sendVip.value = "false"
                        }


                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                }

            })

        }

    }
    fun requestbodyString(text: String): RequestBody {
        return text.toRequestBody("text/plain".toMediaType())
    }

    fun settype(index:Int){
        if (index == 1){
            if (isPermission.value ==false){
                permissionEvent.value = true
            }
        }else{
            nowtypeindex.value = index
        }

    }
    fun settype3(index:Int){
        nowtypeindex.value = index
    }
    fun resetpage(){
        page.value = 1
    }

    fun nextMonth(b :Boolean){
        nextMonth.value=  b
    }
    fun backMonth(b :Boolean){
        backMonth.value=  b
    }
    fun CheckDrivePass(pdata: ProfileSuggestionData) {
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(DriveService::class.java)
                .DrivePassCheck(my_seq.value!!)
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
                            RecProfile(pdata.u_seq.toString(), true)
                            Log.d("체크", "onResponse: 1")
                        } else {
                            RecProfile(pdata.u_seq.toString(), false)
                            Log.d("체크", "onResponse: 2")
                        }


                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                }

            })

        }

    }

    fun RecProfile(sub_seq: String, pcheck: Boolean) {
        if (bubbleRick.value == false){
            bubbleRick.value = true

            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter!!.create(MatchingService::class.java)
                    .RecProfileSearch(
                        my_seq.value,
                        sub_seq, "3", pcheck
                    )
                d!!.enqueue(object : retrofit2.Callback<ResultFeedData> {
                    override fun onResponse(
                        call: retrofit2.Call<ResultFeedData>,
                        response: Response<ResultFeedData>
                    ) {
                        var data = response.body()
                        if (data != null) {
                            var type = data.type
                            Log.d("테스트 데이터", data.toString())

                            if (type.equals("success")) {
                                if (!pcheck) {
                                    if (data.count !=null){
                                        isDrivePassCount.value = 3-data.count!!
                                    }
                                    if (data.count != 0){
                                        toast.value = "드라이브 패스권 무료 소진 기회가 ${data.count}회 남았습니다."
                                    }
                                }
                                var u_seq = data.u_seq
                                if (u_seq != null) {
                                    open_profile_seq.value = u_seq!!
                                }
                            } else if (type.equals("notheart")) {
                                isnotheart.value = true
                            } else if (type.equals("nochance")) {
                                if (pcheck) {
                                    toast.value = data.message
                                }
                                var u_seq = data.u_seq
                                if (u_seq != null) {
                                    open_profile_seq.value = u_seq!!
                                }
                            }
                        }
                        bubbleRick.value = false
                    }

                    override fun onFailure(
                        call: retrofit2.Call<ResultFeedData>,
                        t: Throwable
                    ) {
                        Log.d("check", "failer")
                        bubbleRick.value = false
                    }

                })

            }
        }
    }

    fun getprofileSuggestio() {
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(HomerService::class.java)
                .getProfileSuggestion(
                    my_seq.value!!
                )
            d!!.enqueue(object : retrofit2.Callback<ResultProfileSuggestionData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultProfileSuggestionData>,
                    response: Response<ResultProfileSuggestionData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if (type.equals("success")) {
                            var rows = data.reco
                            if (rows != null) {

                                if (rows.count() != 0) {

                                    profilesuggest.value = rows
                                } else {
                                    profilesuggest.value = EmptyProfileSugesst()
                                }

                            }

                        }
                    }

                }

                override fun onFailure(
                    call: retrofit2.Call<ResultProfileSuggestionData>,
                    t: Throwable
                ) {
                    Log.d("check", "failer")
                }

            })
        }

    }

    fun EmptyProfileSugesst():ArrayList<ProfileSuggestionData>{
        var list :ArrayList<ProfileSuggestionData> = arrayListOf()
        list.add(
            ProfileSuggestionData(
                -1,
                "https://supercarlounge.com:3002/images/cover/cover_5.png",
                "",
                "",
                "",
                "",
            )
        )
        list.add(
            ProfileSuggestionData(
                -2,
                "https://supercarlounge.com:3002/images/cover/cover_5.png",
                "",
                "",
                "",
                "",
            )
        )
        list.add(
            ProfileSuggestionData(
                -3,
                "https://supercarlounge.com:3002/images/cover/cover_5.png",
                "",
                "",
                "",
                "",
            )
        )
        list.add(
            ProfileSuggestionData(
                -4,
                "https://supercarlounge.com:3002/images/cover/cover_5.png",
                "",
                "",
                "",
                "",
            )
        )
        list.add(
            ProfileSuggestionData(
                -5,
                "https://supercarlounge.com:3002/images/cover/cover_5.png",
                "",
                "",
                "",
                "",
            )
        )
        list.add(
            ProfileSuggestionData(
                -6,
                "https://supercarlounge.com:3002/images/cover/cover_5.png",
                "",
                "",
                "",
                "",
            )
        )
        list.add(
            ProfileSuggestionData(
                -7,
                "https://supercarlounge.com:3002/images/cover/cover_5.png",
                "",
                "",
                "", "",

                )
        )
        list.add(
            ProfileSuggestionData(
                -8,
                "https://supercarlounge.com:3002/images/cover/cover_5.png",
                "",
                "",
                "",
                "",
            )
        )
        list.add(
            ProfileSuggestionData(
                -9,
                "https://supercarlounge.com:3002/images/cover/cover_5.png",
                "",
                "",
                "",
                "",
            )
        )
        list.add(
            ProfileSuggestionData(
                -10,
                "https://supercarlounge.com:3002/images/cover/cover_5.png",
                "",
                "",
                "",
                "",
            )
        )

        return list
    }


    fun dateMapping(): String{
        var date =""
        var year = year.value.toString()
        var month = month.value.toString()
        if (month.length !=2){
            month =  "0$month"
        }
        var day = selectDay.value
        if (day?.length !=2){
            day =  "0$day"
        }
        var hou = Hour24()


        var min = min.value
        if (min?.length !=2){
            min =  "0$min"
        }
        date ="$year-$month-$day $hou:$min"
        Log.d("날짜", "$year-$month-$day $hou:$min")
        return date
    }
    fun Hour24(): String {
       var hou = hour.value
       var ampm =ampm.value
        if (ampm== 0){
            if (hou == "12"){
                return  "00"
            }
            if (hou?.length !=2){
                hou ="0$hou"

            }

        }else{
            if (hou!!.isNotEmpty()){
                var d = hou.toInt()
                var h = d + 12
                if (h == 24){
                    return  "12"
                }
                hou = h.toString()
            }

        }


        return hou.toString()
    }
    fun CheckDrivePass() {
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(DriveService::class.java)
                .DrivePassCheck(my_seq.value!!)
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
                            isDrivePass.value = true
                            CheckDrivePassCount()
                            Log.d("체크", "onResponse: 1")
                        } else {
                            isDrivePass.value = false
                            Log.d("체크", "onResponse: 2")
                        }


                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                }

            })

        }

    }
    fun CheckDrivePassCount() {
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(MatchingService::class.java).
            DrivePassCount(my_seq.value!!)
            d!!.enqueue(object : retrofit2.Callback<ResultDrivePassCountData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultDrivePassCountData>,
                    response: Response<ResultDrivePassCountData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        var message = data.message
                        if (type.equals("success")) {
                            var count = data.rows
                            isDrivePassCount.value = count
                        } else {

                            Log.d("체크", "onResponse: 2")
                        }


                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultDrivePassCountData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                }

            })

        }

    }
}


