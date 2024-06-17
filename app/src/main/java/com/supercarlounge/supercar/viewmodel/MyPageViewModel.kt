package com.supercarlounge.supercar.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.*
import com.supercarlounge.supercar.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MyPageViewModel : ViewModel() {

    var text = MutableLiveData<String>("")
    var titleText = MutableLiveData<String>("MY PAGE")
    var driveTimeText = MutableLiveData<String>("등록하기")
    var nowversion =  MutableLiveData<String>("")
    var appVersionText =  MutableLiveData<String>("v.1.1 업데이트")
    var isupdate =  MutableLiveData<Boolean>(false)
    var swPush =  MutableLiveData<Boolean>(false)
    var swLockScreen =  MutableLiveData<Boolean>(false)
    var swRecProfile =  MutableLiveData<Boolean>(true)
    var swLockScreenTouch =  MutableLiveData<Boolean>(false)
    var my_seq = MutableLiveData<String>("")
    var invite_code = MutableLiveData<String>("")
    var inviteset = MutableLiveData<Boolean>(false)
    var nowpage  = MutableLiveData<Int>(0)
    var end_date  = MutableLiveData<Long>(0)
    var nowpage_b_seq  = MutableLiveData<Int>(0)
    var viewCheckCount  = MutableLiveData<Int>(0)
    var likeCheckCount  = MutableLiveData<Int>(0)
    var crushCheckCount  = MutableLiveData<Int>(0)
    var nowpoint =  MutableLiveData<String>("0")


    var isviptimer = MutableLiveData<Boolean>(false)
    var recentdrivelist = MutableLiveData<ArrayList<RecentDriveData>>(arrayListOf())
    var drivepassset = MutableLiveData<RecentDriveData?>()
    var nowrecentpage  = MutableLiveData<Int>(0)
    var rec_profile_update = MutableLiveData<Boolean>()
    var isrec_click = MutableLiveData<Boolean>(false)
    var passwordTrueFalse = MutableLiveData<Int>(0)
    var iscompany = MutableLiveData<Boolean>(false)

    var ispasstimer = MutableLiveData<Boolean>(false)
    var pass_end_date  = MutableLiveData<Long>(0)
    var passTimeText = MutableLiveData<String>("구매하기")
    fun onPushChecked(boolean: Boolean){
        swPush.value =boolean
        var sw = ""
        if (swPush.value == true){
            sw = "u_push = \"Y\""
        }else{
            sw = "u_push = \"N\""
        }
        my_seq.value?.let { setPushAndLockScreen(it,sw) }
    }
    fun setrecoff(){
        isrec_click.value =true
    }
    fun onLockChecked(boolean: Boolean){
        swLockScreen.value =boolean
        swLockScreenTouch.value = boolean
        var sw = ""
        if (swLockScreen.value == true){
            sw = "u_lock_screen = \"Y\""

        }else{
            sw = "u_lock_screen = \"N\""
        }
        my_seq.value?.let { setPushAndLockScreen(it,sw) }
    }
    fun checkoffrec(){
        if(swRecProfile.value!!){
            setrecoff()
        }else{
          onRecChecked(true)
        }
    }
    fun onRecChecked(boolean: Boolean){

        swRecProfile.value =boolean
        var rec  = ""
        if (swRecProfile.value == true){
            rec = "u_feed = \"1\""
        }else{
            rec = "u_feed = \"0\""
        }

        my_seq.value?.let { setRecProfile(it,rec) }
    }
    fun setPushAndLockScreen(u_seq: String, updateStr: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(PasswordService::class.java).setPushAndLockScreen(
                u_seq,
                updateStr,
            )
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        Log.d("setPushAndLockScreen", "$u_seq: $data.message")
                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Log.d("check", t.toString())
                }

            })

        }

    }
    fun setRecProfile(u_seq: String, updateStr: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(PasswordService::class.java).setPushAndLockScreen(
                u_seq,
                updateStr,
            )
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        if(updateStr.contains("1")){
                            rec_profile_update.value = true
                        }else{
                            rec_profile_update.value =false
                        }

                        Log.d("setPushAndLockScreen", "$u_seq: $data.message")
                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Log.d("check", t.toString())
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

    fun GetMyvipDrive() {

            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter.create(DriveService::class.java).GetMyVipData(
                    my_seq.value!!,
                )
                d!!.enqueue(object : retrofit2.Callback<ResultDriveAuctionData> {
                    override fun onResponse(
                        call: retrofit2.Call<ResultDriveAuctionData>,
                        response: Response<ResultDriveAuctionData>
                    ) {
                        var data = response.body()
                        if (data != null) {
                           var type = data.type
                            if(type.equals("success")) {
                                var rows = data.rows
                                if (rows != null) {

                                        var vip =rows
                                        var dd =vip.end_date
                                    end_date.value =dateTimeToMillSec(vip.end_date)
                                    isviptimer.value =true

                                }
                            }
                        }

                    }

                    override fun onFailure(
                        call: retrofit2.Call<ResultDriveAuctionData>,
                        t: Throwable
                    ) {
                        Log.d("check", t.toString())
                    }

                })

            }
    }
    fun GetRecentDrive() {

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(DriveService::class.java).GetRecentDrive(
                       my_seq.value!!

            )

            d!!.enqueue(object : retrofit2.Callback<ResultRecentDriveData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultRecentDriveData>,
                    response: Response<ResultRecentDriveData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if(type.equals("success")) {
                            var rows = data.rows
                            if (rows != null) {
                                if(rows.size!=0) {
                                    var list = rows.reversed()
                                    val MyList :ArrayList<RecentDriveData> = arrayListOf()
                                    MyList.addAll(list)
                                    Log.d("마이페이지 데이터", rows.size.toString())
//                                    if (recentdrivelist.value?.size != rows.size){
                                        recentdrivelist.value =MyList
//                                    }




                                }
                            }
                        }
                    }

                }

                override fun onFailure(
                    call: retrofit2.Call<ResultRecentDriveData>,
                    t: Throwable
                ) {
                    Log.d("내 최근 드라이브 글",t.toString())
                    Log.d("check", t.toString())
                }

            })

        }
    }
    @SuppressLint("SimpleDateFormat")
    fun dateTimeToMillSec(dateTime: String): Long{
        Log.d("변환전 시간", dateTime)

        val curTime = System.currentTimeMillis()
        Log.d("현재 시간", curTime.toString())
        val target = "T"
        var result = dateTime.replace(target," ") // '-'를 공백으로 바꿔준다.
        var timeInMilliseconds: Long = 0
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        try {
            val mDate = sdf.parse(result)

            timeInMilliseconds = mDate.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        Log.d("변환된 시간", timeInMilliseconds.toString())
        return timeInMilliseconds
    }
    fun settime(){
       var endtime =end_date.value!!
        var nowtime = Date().time
        var dd =endtime-nowtime
        var sec = dd /1000;
       var min = sec / 60;
        var hour = min / 60;
        var m = min - (hour*60)
        var s = sec %60
        var lasttime = Date(dd)
        if(hour <=0 && m<=0 && s <0){
            driveTimeText.value = "등록하기"
            isviptimer.value =false
        }else {
            var tt = hour.toString() + ":" + m + ":" + s+" 후 만료"
            driveTimeText.value = tt
        }

    }

    fun setpasstime(){
        var endtime =pass_end_date.value!!
        var nowtime = Date().time
        var dd =endtime-nowtime
        var sec = dd /1000;
        var min = sec / 60;
        var hour = min / 60;
        var m = min - (hour*60)
        var s = sec %60
        var lasttime = Date(dd)
        if(hour <=0 && m<=0 && s <0){
            passTimeText.value = "구매하기"
            ispasstimer.value =false
        }else {
            var tt = hour.toString() + ":" + m + ":" + s+" 후 만료"
            passTimeText.value = tt
        }

    }
    fun getcountCheck() {
        Log.d("체크 my_seq", my_seq.value.toString())
        Log.d("체크 nowpage_b_seq", nowpage_b_seq.value.toString())
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(UserService::class.java).getdriveRead(
                my_seq.value!!,
                nowpage_b_seq.value.toString()
            )
            d!!.enqueue(object : retrofit2.Callback<driveReadData> {
                override fun onResponse(
                    call: retrofit2.Call<driveReadData>,
                    response: Response<driveReadData>
                ) {
                    var data = response.body()

                    if (data != null) {

                        var open = data?.open
                        var like = data?.like
                        var hogam = data?.hogam


//                        Log.d("체크 like", like.toString())
//                        Log.d("체크 hogam", hogam.toString())
                        viewCheckCount.value =open
                        likeCheckCount.value =like
                        crushCheckCount.value = hogam
                        Log.d("체크 viewCheckCount", viewCheckCount.value.toString())

                    }

                }

                override fun onFailure(
                    call: retrofit2.Call<driveReadData>,
                    t: Throwable
                ) {
                    Log.d("check", t.toString())
                }

            })

        }
    }

    fun DrivePassCheck(recentdata:RecentDriveData) {

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(MatchingService::class.java).DrivePassCheck(
                my_seq.value!!,
            )
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if(type.equals("success")) {
                            drivepassset.value = recentdata
                        }else{
                            drivepassset.value = null
                        }
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
    fun GetAppVersion() {

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(HomerService::class.java).GetStoreappversion(
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
                            var nver =nowversion.value
                            if(nver.equals(version)){
                                appVersionText.value = "v."+version+"(최신버전)"
                                isupdate.value = false
                            }else{
                                isupdate.value = true
                                appVersionText.value = "v."+version+" 업데이트"
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


    fun passwordSettingTrueFalse(u_seq: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(PasswordService::class.java)
                .passwordSettingTrueFalse(
                    u_seq,
                )
            d!!.enqueue(object : retrofit2.Callback<ResultPasswordSettiongData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultPasswordSettiongData>,
                    response: Response<ResultPasswordSettiongData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        Log.d("setPassword", data.message)

                        if (data.message == "비밀번호 존재"){
                            passwordTrueFalse.value =1
                        }else{
                            passwordTrueFalse.value =2
                        }


                    }

                }

                override fun onFailure(
                    call: retrofit2.Call<ResultPasswordSettiongData>,
                    t: Throwable
                ) {
                    Log.d("check", t.toString())
                }

            })

        }

    }
    fun GetMyPoint(){

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(PointMallService::class.java).GetMyPoint(my_seq.value!!)
            d!!.enqueue(object : retrofit2.Callback<ResultPointData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultPointData>,
                    response: Response<ResultPointData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if(type.equals("success")) {
                            var rows = data.rows
                            if (rows.size != 0) {
                                var point = rows.get(0).u_point
                                nowpoint.value = point
                            }
                        }
                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultPointData>, t: Throwable) {
                    Log.d("failer", t.message!!)

                }

            })

        }
    }

    fun CheckDrivePassDate() {

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(DriveService::class.java).CheckDrivePassDate(
                my_seq.value!!,
            )
            d!!.enqueue(object : retrofit2.Callback<ResultDrivePassDateData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultDrivePassDateData>,
                    response: Response<ResultDrivePassDateData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if(type.equals("success")) {
                            var rows = data.rows
                            if (rows != null) {


                                if (rows.size != 0 ) {
                                    var pass =rows.get(0)

                                    pass_end_date.value = dateTimeToMillSec(pass.pass_date)
                                    ispasstimer.value = true
                                }
                            }
                        }
                    }

                }

                override fun onFailure(
                    call: retrofit2.Call<ResultDrivePassDateData>,
                    t: Throwable
                ) {
                    Log.d("check", t.toString())
                }

            })

        }
    }
}