package com.supercarlounge.supercar.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.DriveService
import com.supercarlounge.supercar.Network.HomerService
import com.supercarlounge.supercar.Network.MatchingService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.*
import com.supercarlounge.supercar.enumset.PidDialogType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class OwnerAndPassengerViewModel : ViewModel() {
    var nottext = MutableLiveData<String>("게시글을 불러오는 중입니다.")
    var recodata = MutableLiveData<ArrayList<LocationSpotData>>(ArrayList())
    var areacategorylist = MutableLiveData<ArrayList<LocationAreaCategoryData>>(ArrayList())
    var locationcategory = MutableLiveData<ArrayList<LocationCateData>>(ArrayList())
    var areacatelist = MutableLiveData<String>("게시글을 불러오는 중입니다.")
    var my_seq = MutableLiveData<String>("")
    var regiposition = MutableLiveData<Int>(0)
    var select_index = MutableLiveData<ArrayList<String>>(ArrayList())
    val profilesuggest =    MutableLiveData<ArrayList<ProfileSuggestionData>>(ArrayList())
    var open_profile_seq = MutableLiveData<String>("")
    var isnotheart = MutableLiveData<Boolean>(false)
    var bubbleRick = MutableLiveData<Boolean>(false)
    var pidlist = MutableLiveData<ArrayList<SlidePidData>>(arrayListOf())
    var selectproindex = MutableLiveData<Int>(0)
    var nowPosition =MutableLiveData<Int>(0)
    var selectprodata = MutableLiveData<ProfileSuggestionData>()
    var eventPid = MutableLiveData<Int>()
    var scrolling  = MutableLiveData<Boolean>(false)
//    val charmList =    MutableLiveData<ArrayList<String>>(ArrayList())
    var charm1  = MutableLiveData<String>("")
    var charm2  = MutableLiveData<String>("")
    var charm3  = MutableLiveData<String>("")
    var index =  MutableLiveData<Int>(0)
    var toast = MutableLiveData<String>("")

    var eventLeft = MutableLiveData<Boolean>(false)
    var eventRight = MutableLiveData<Boolean>(false)
    var eventCenter = MutableLiveData<Boolean>(false)
    var u_charm  = MutableLiveData<String>("")
    var isalim = MutableLiveData<Boolean>(false)
    var iscompany = MutableLiveData<Boolean>(false)

    var isDrivePass = MutableLiveData<Boolean>(false)
    var isDrivePassCount = MutableLiveData<Int>(0)
    fun eventLeft(d :Boolean){
        eventLeft.value = d
    }
    fun eventRight(d :Boolean){
        eventRight.value = d
    }
    fun eventCenter(d: Boolean){
        eventCenter.value = d
    }
    fun eventPid(int : Int){
        eventPid.value =int
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
//                    my_seq.value,
                    my_seq.value!!,
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

    fun CheckAlim(u_email:String) {

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(HomerService::class.java).AlimReadCheck(
                my_seq.value!!
            )
            d!!.enqueue(object : retrofit2.Callback<ResultAlimCheckData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultAlimCheckData>,
                    response: Response<ResultAlimCheckData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if (type.equals("success")) {
                            var active = data.active
                            if (u_email.isNullOrEmpty()){
                                isalim.value = true
                            }else{
                                isalim.value = active

                            }

                        }
                    }

                }

                override fun onFailure(
                    call: retrofit2.Call<ResultAlimCheckData>,
                    t: Throwable
                ) {
                    Log.d("check", t.toString())
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
                                        isDrivePassCount.value = 3-data.count!!
                                    }
                                    if (data.count != 0){
                                        toast.value = "드라이브 패스권 무료 소진 기회가 ${data.count}회 남았습니다."
                                        }

                                }
                                var u_seq = data.u_seq
                                if (u_seq != null) {
                                    open_profile_seq.value = u_seq!!
                                    Log.d("2024체크", "3 ======== ${index.value}")
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
