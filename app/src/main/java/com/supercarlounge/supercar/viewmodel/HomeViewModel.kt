package com.supercarlounge.supercar.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.Network.*
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.*
import com.supercarlounge.supercar.databinding.AcitivtyOwnerAndPassengerBinding
import com.supercarlounge.supercar.enumset.DialogBallonType
import com.supercarlounge.supercar.enumset.PidDialogType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response


class HomeViewModel : ViewModel() {

    var my_seq = MutableLiveData<String>("")
    var titleText = MutableLiveData<String>("SURA")
    var titleText1 = MutableLiveData<String>("OWNER & PASSENGER")
    var titleText2 = MutableLiveData<String>("VIP DRIVE")
    var titleText3 = MutableLiveData<String>("MY PICKS")
    var titleText4 = MutableLiveData<String>("DRIVE AWAY")
    var rvItemType = MutableLiveData<Int>(0)
    var scrollPosition = MutableLiveData<Int>(0)
    var startTopViewPosition = MutableLiveData<Int>(0)
    var bannerPosition = MutableLiveData<Int>(0)
    var banner2Position = MutableLiveData<Int>(0)
    var topView = MutableLiveData<Boolean>(false)
    var topTitleText = MutableLiveData<String>("")
    var topBrandText = MutableLiveData<String>("")
    var topDistanceText = MutableLiveData<String>("")
    var
            selectproindex = MutableLiveData<Int>(0)
    var selectprodata = MutableLiveData<ProfileSuggestionData>()
    var selectProdataUcharmStr = MutableLiveData<String>("")
    var page = MutableLiveData<Int>(1)
    var ppp = MutableLiveData<Int>(15)
    var isShow = MutableLiveData<Boolean>(false)
    var isShowing = MutableLiveData<Boolean>(false)

    var driveTabPosition =MutableLiveData<Int>(0)
    var isDetail = MutableLiveData<Boolean>(false)
    var suggetionViewW = MutableLiveData<Int>(0)
    var suggetionViewH = MutableLiveData<Int>(0)
    var nowPosition = MutableLiveData<Int>(0)
    var viewpagerNowPosition = MutableLiveData<Int>(0)
    val mDriveList: MutableLiveData<java.util.ArrayList<DriveAwayData>> =
        MutableLiveData(arrayListOf())
    var issend = MutableLiveData<Boolean>(false)
    var totalCount = MutableLiveData<Int>(0)
    val choiceVipDrive: MutableLiveData<DriveAwayData> = MutableLiveData()
    var topBrand = MutableLiveData<String>("")

    val profilesuggest: MutableLiveData<ArrayList<ProfileSuggestionData>> =
        MutableLiveData(arrayListOf())
    val mHomeBanner1List: MutableLiveData<ArrayList<BannerData>> = MutableLiveData(arrayListOf())
    val mHomeBannerTopList: MutableLiveData<ArrayList<BannerData>> = MutableLiveData(arrayListOf())
    val mVipDriveList: MutableLiveData<ArrayList<DriveAwayData>> = MutableLiveData(arrayListOf())
    val mHomeBanner2List: MutableLiveData<ArrayList<BannerData>> = MutableLiveData(arrayListOf())
    var selecttopvipdrive: MutableLiveData<DriveAwayData> = MutableLiveData()
    var iswriteview = MutableLiveData<Boolean>(false)
    var toast = MutableLiveData<String>("")
    var open_profile_seq = MutableLiveData<String>("")
    var pidlist = MutableLiveData<ArrayList<SlidePidData>>(arrayListOf())
    var select_bannernoti_data = MutableLiveData<NotiData>()
    var bubbleRick = MutableLiveData<Boolean>(false)
    var popUpinfoData = MutableLiveData<ArrayList<PopUpinfoData>>(arrayListOf())
    var popUpinfoData_size = MutableLiveData<Int>()
    var nowversion = MutableLiveData<String>("")
    var isupdate = MutableLiveData<String>("")
    var isnotheart = MutableLiveData<Boolean>(false)
    var invite_code = MutableLiveData<String>("")
    var inviteset =MutableLiveData<Boolean>(false)
    var intentIu =MutableLiveData<Boolean>(false)
    var viewWidth =MutableLiveData<Int>(0)
    var intentActivityDriveWayListEvent =MutableLiveData<Boolean>(false)
    var intentFriend =MutableLiveData<Boolean>(false)
    var intentlocationEvent =MutableLiveData<Boolean>(false)
    var intentOnwerAndPassengerEvent =MutableLiveData<Boolean>(false)
    var intentOnwerAndPassengerEvent2 =MutableLiveData<Boolean>(false)
    var permissionEvent =MutableLiveData<Boolean>(false)
    var isPermission = MutableLiveData<Boolean>(false)
    var tabLayoutPosition = MutableLiveData<Int>(0)
    var tabLayoutBeforePosition = MutableLiveData<Int>(0)
    var isDrivePass = MutableLiveData<Boolean>(false)
    var isDrivePassCount = MutableLiveData<Int>(0)

    var eventPid = MutableLiveData<Int>()
    var charm1  = MutableLiveData<String>("")
    var charm2  = MutableLiveData<String>("")
    var charm3  = MutableLiveData<String>("")
    var index =  MutableLiveData<Int>(0)
    var u_charm  = MutableLiveData<String>("")

    fun permissionEvent(intent :Boolean){
        permissionEvent.value = intent
    }
    fun intentOnwerAndPassengerEvent(intent :Boolean){
        intentOnwerAndPassengerEvent.value = intent
    }
    fun intentOnwerAndPassengerEvent2(intent :Boolean){
        intentOnwerAndPassengerEvent2.value = intent
    }
    fun intentFriend(intent :Boolean){
        intentFriend.value = intent
    }
    fun intentIntentLocationEvent(intent :Boolean){
        intentlocationEvent.value = intent
    }
    fun intentIu(intent :Boolean){
        intentIu.value = intent
    }

    fun intentActivityDriveWayList(intent :Boolean){
        intentActivityDriveWayListEvent.value = intent
    }

    fun setwrite() {
        iswriteview.value = true
    }

    fun setrvItemType(int: Int) {

        rvItemType.value = int
        Log.d("옵저버", int.toString())
    }

    fun addItem(item: ArrayList<ProfileSuggestionData>) {
        var itemlistset = profilesuggest.value!!
        itemlistset.addAll(item)
        profilesuggest.value = itemlistset
    }

    fun removeItem(item: ProfileSuggestionData) {
        var itemlistset = profilesuggest.value!!
        itemlistset.remove(item)
        profilesuggest.value = itemlistset
    }

    fun getDrive(u_seq: String, isreset: Boolean) {
        issend.value = true
        var list: ArrayList<DriveAwayData> = arrayListOf()
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(DriveService::class.java).getDrive(
                "신규", u_seq, ppp.value, page.value, "0", "0"
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
                        Log.d("getDrive : rows.size  ", rows.size.toString())
                        if (rows != null) {

                            if (isreset) {
                                var lls = rows.get(0).like_check

                                mDriveList.value = rows
                            } else {
                                var listset = mDriveList.value
                                listset!!.addAll(rows)
                                mDriveList.value = listset!!


                            }

                        }

                    }
                    issend.value = false
                }

                override fun onFailure(call: retrofit2.Call<ResultDriveData>, t: Throwable) {
                    Log.d("check", "failer")
                    issend.value = false
                }

            })

        }
    }

    fun getPopUpinfo() {
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(BannerService::class.java).getPopUpinfo(
            )
            d!!.enqueue(object : retrofit2.Callback<ResultPopUpinfotData> {
                override fun onResponse(
                    call: Call<ResultPopUpinfotData>,
                    response: Response<ResultPopUpinfotData>
                ) {
                    Log.d("getPopUpinfo", "11")
                    var data = response.body()
                    if (data != null) {

                        Log.d("getPopUpinfo", data.toString())
                        var data = data.rows


                        popUpinfoData.value = data
                        popUpinfoData_size.value = popUpinfoData.value?.size

                    }

                }

                override fun onFailure(call: Call<ResultPopUpinfotData>, t: Throwable) {
                    Log.d("check", "failer")
                }

            })

        }
    }

    fun getBannerTop(bl_name: String): ArrayList<BannerData> {

        var list: ArrayList<BannerData> = arrayListOf()
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(BannerService::class.java).getHomeBanner(
                bl_name
            )
            d!!.enqueue(object : retrofit2.Callback<ResultBannerData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultBannerData>,
                    response: Response<ResultBannerData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var rows = data.rows
                        if (rows != null) {
                            list.addAll(rows)

                            mHomeBanner1List.value = list

                        }

                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultBannerData>, t: Throwable) {
                    Log.d("check", "failer")
                }

            })

        }


        return list
    }

    fun getBannerTop2(bl_name: String): ArrayList<BannerData> {
        Log.d("데이터 getBannerTop2 1", "getBannerTop2: ")
        var list: ArrayList<BannerData> = arrayListOf()
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(BannerService::class.java).getHomeBannerTop(
                bl_name
            )
            d!!.enqueue(object : retrofit2.Callback<ResultBannerData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultBannerData>,
                    response: Response<ResultBannerData>
                ) {
                    var data = response.body()
                    Log.d("데이터 getBannerTop2 2 ", data.toString())
                    if (data != null) {
                        Log.d("데이터 getBannerTop2 3", "getBannerTop2: ")
                        var rows = data.rows
                        if (rows != null) {
                            list.addAll(rows)

                            mHomeBannerTopList.value = list

                        }

                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultBannerData>, t: Throwable) {
                    Log.d("check", "failer")
                    Log.d("데이터 getBannerTop2 4","failer")
                }

            })

        }


        return list
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

    fun getBannerBottom(bl_name: String): ArrayList<BannerData> {

        var list: ArrayList<BannerData> = arrayListOf()
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(BannerService::class.java).getHomeBanner(
                bl_name
            )
            d!!.enqueue(object : retrofit2.Callback<ResultBannerData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultBannerData>,
                    response: Response<ResultBannerData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var rows = data.rows
                        if (rows != null) {
                            list.addAll(rows)
                            Log.d("사이즈바텀", list.size.toString())
                            mHomeBanner2List.value = list
                        }
                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultBannerData>, t: Throwable) {
                    Log.d("check", "failer")
                }

            })
        }
        return list
    }

    fun getVipDrive() {
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(DriveService::class.java).getVipDrive(
                my_seq.value!!, "0", "0"
            )
            d!!.enqueue(object : retrofit2.Callback<ResultDriveData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultDriveData>,
                    response: Response<ResultDriveData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var rows = data.drives
                        if (rows != null) {
                            mVipDriveList.value = rows
                        }

                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultDriveData>, t: Throwable) {
                    Log.d("check", "failer")
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
                                if (data.count != null){
                                    isDrivePassCount.value = 3 - data.count!!
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

    fun check_suggestcenter(position: Int): Boolean {
        var selposi = selectproindex.value
        var listsize = profilesuggest.value!!.size
        if (listsize != 0) {
            if (selposi == position % listsize) {
                return true
            } else {
                return false
            }
        } else {
            return false
        }
    }


    fun setpid(sex: String) {
        var list: ArrayList<SlidePidData> = arrayListOf()

        if (sex.equals("MALE")) {
            list.add(
                SlidePidData(
                    PidDialogType.FRIEND,
                    false,
                    "나와 맞는 짝꿍 찾기! \uD83D\uDC69\u200D❤️\u200D\uD83D\uDC68",
                    "짝꿍과 함께 신나는 드라이브!"
                )
            )
            list.add(
                SlidePidData(
                    PidDialogType.MAN_GLAMMER,
                    false,
                    "글래머,볼륨있는 여성 회원 만나기 \uD83D\uDD25",
                    "이상형과 함께하는 설레는 드라이브!"
                )
            )
            list.add(
                SlidePidData(
                    PidDialogType.NEW_USER,
                    false,
                    "최근 가입한 신입회원 만나기 \uD83D\uDC25",
                    "신입 회원과 즐거운 드라이브!"
                )
            )
            list.add(
                SlidePidData(
                    PidDialogType.AGE_20,
                    false,
                    "20대 드라이버,패신저 만나기 \uD83D\uDC66\uD83D\uDC67",
                    "PASS인증을 통한 실제 나이를 인증한 회원!"
                )
            )
            list.add(
                SlidePidData(
                    PidDialogType.LIKE_MUSIC,
                    false,
                    "음악취향 비슷한 상대 만나기 \uD83C\uDFB6",
                    "음악취향도 같으면 더 신나는 드라이브!"
                )
            )
            list.add(
                SlidePidData(
                    PidDialogType.SUPER_USE,
                    false,
                    "슈퍼카 오너 만나기 \uD83D\uDE98",
                    "본인 소유의 차량 인증을 한 슈퍼카 오너!"
                )
            )
        } else {
            list.add(
                SlidePidData(
                    PidDialogType.FRIEND,
                    false,
                    "나와 맞는 짝꿍 찾기! \uD83D\uDC69\u200D❤️\u200D\uD83D\uDC68",
                    "짝꿍과 함께 신나는 드라이브!"
                )
            )
            list.add(
                SlidePidData(
                    PidDialogType.SUPER_USE,
                    false,
                    "슈퍼카 오너 만나기 \uD83D\uDE98",
                    "본인 소유의 차량 인증을 한 슈퍼카 오너!"
                )
            )
            list.add(
                SlidePidData(
                    PidDialogType.AGE_20,
                    false,
                    "20대 드라이버,패신저 만나기 \uD83D\uDC66\uD83D\uDC67",
                    "PASS인증을 통한 실제 나이를 인증한 회원!"
                )
            )
            list.add(
                SlidePidData(
                    PidDialogType.FM_10PER,
                    false,
                    "상위 10% 이성 회원 만나기 \uD83D\uDE0E",
                    "호감을 제일 많이 받은 회원!"
                )
            )
            list.add(
                SlidePidData(
                    PidDialogType.LIKE_MUSIC,
                    false,
                    "음악취향 비슷한 상대 만나기 \uD83C\uDFB6",
                    "음악취향도 같으면 더 신나는 드라이브!"
                )
            )
            list.add(
                SlidePidData(
                    PidDialogType.NEW_USER,
                    false,
                    "최근 가입한 신입회원 만나기 \uD83D\uDC25",
                    "신입 회원과 즐거운 드라이브!"
                )
            )
        }
        pidlist.value = list
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
                                open_profile_seq.value = u_seq!!
                                Log.d("피드 seq2", u_seq.toString())

                            }
                        } else if(type.equals("notexist")) {
                            toast.value = type
                        }else if (type.equals("notheart")) {
                            isnotheart.value = true
                        } else if (type.equals("nochance")) {
                            var u_seq = data.u_seq
                            open_profile_seq.value = u_seq!!
                           // toast.value = "무료 열람 기회를 소진하였습니다. 하트가 소모됩니다."
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

                            if (type.equals("success")) {

                                FeedProfileSearch(mtype)

                            } else if (type.equals("notheart")) {
                               isnotheart.value = true
                            } else if (type.equals("over")) {
                                toast.value = type
                            } else if (type.equals("nochance")) {
                                var u_seq = data.u_seq
                                open_profile_seq.value = u_seq!!
                              //  toast.value = "무료 열람 기회를 소진하였습니다. 하트가 소모됩니다."
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

    fun getOneNoti(n_seq: String) {


        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(BannerService::class.java).getOneNoti(
                n_seq
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
    fun GetAppVersion(application:MainApplication) {

        CoroutineScope(Dispatchers.IO).launch {
            val d =
                NetworkManager.serveradapter.create(HomerService::class.java).GetStoreappversion(
                    "Android",
                )
            d!!.enqueue(object : retrofit2.Callback<ResultVersionData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultVersionData>,
                    response: Response<ResultVersionData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if (type.equals("success")) {
                            var version = data.version
                            var nver = nowversion.value
                            var prvVersion = application.prvVersion
                            if(nver.equals(version)) {
                                isupdate.value = "false"
                            } else {
                                if (version.equals(prvVersion)){
                                    isupdate.value = "false"
                                }else{
                                    isupdate.value = "true"
                                }
                            }

                        }
                    }

                }

                override fun onFailure(
                    call: retrofit2.Call<ResultVersionData>,
                    t: Throwable
                ) {
                    Log.d("check", t.toString())
                }

            })

        }
    }
    fun viewLogBanner(bc_seq:Int,cl_type:String) {
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(BannerService::class.java)
                .viewLogBanner(bc_seq,my_seq.value!!,cl_type)
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
    companion object {

        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("selimg")
        fun loadImage(view: ImageView, url: String?) {
            // This methods should not have any return type, = declaration would make it return that object declaration.
            if (!url.isNullOrEmpty())
                Glide.with(view.context).load(url).into(view)
        }
    }

    fun eventPid(int : Int){

        Log.d("마이픽스","마이픽스 클릭")
        eventPid.value =int
    }




}