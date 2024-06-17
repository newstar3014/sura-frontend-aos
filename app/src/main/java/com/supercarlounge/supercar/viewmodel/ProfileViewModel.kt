package com.supercarlounge.supercar.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.MatchingService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.data.*
import com.supercarlounge.supercar.enumset.ProfileStatus
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

class ProfileViewModel : ViewModel() {
    var my_seq=MutableLiveData<String>("")
    var sub_seq=MutableLiveData<String>("")
    var titleText = MutableLiveData<String>("PASSENGER")
    var time= MutableLiveData<String>("")
    var mainText = MutableLiveData<String>("")
    var subText = MutableLiveData<String>("")
    var checked1 = MutableLiveData<Boolean>(false)
    var charmlist = MutableLiveData<ArrayList<String>>()
    var personerlist = MutableLiveData<ArrayList<String>>()
    var musictypelist = MutableLiveData<ArrayList<String>>()
    var bottomBarText = MutableLiveData<String>("호감 수락하기")
    var user_info:MutableLiveData<UserInformationData>  = MutableLiveData()
    var sub_image:MutableLiveData<ArrayList<SubImage>>  = MutableLiveData()
    var subviewindex:MutableLiveData<Int>  = MutableLiveData(0)
    var user_car:MutableLiveData<ArrayList<UserInformationCarData>>  = MutableLiveData()
    var rpmscore:MutableLiveData<Int>  = MutableLiveData(-1)
    var mynick:MutableLiveData<String>  = MutableLiveData()
    var btn_type:MutableLiveData<ProfileStatus>  = MutableLiveData(ProfileStatus.DEFAULT)
    var matching_data:MutableLiveData<ProfileMatchingData>  = MutableLiveData()
    var btn_main = MutableLiveData<String>("확인 하기")
    var btn_sub = MutableLiveData<String>("호감을 보내고 연락처를 확인하세요.")
    var toast = MutableLiveData<String>("")
    var setact:MutableLiveData<Int>  = MutableLiveData(0)
    var startlike = MutableLiveData<Boolean>(false)
    var apicall = MutableLiveData<Boolean>(false)
    var popencall = MutableLiveData<Boolean>(false)
    var message = MutableLiveData<String>("")
    var mainCheckReturn   = MutableLiveData<String>("")
    var subCheckReturn1   = MutableLiveData<String>("")
    var subCheckReturn2  = MutableLiveData<String>("")
    var subCheckReturn3  = MutableLiveData<String>("")
    var profileOpen = MutableLiveData<Boolean>(false)
    var profileOpenCheck = MutableLiveData<Boolean>(false)
    var type = MutableLiveData<String>("")
    var isdirect =MutableLiveData<Boolean>(false)
    var send_b_seq =MutableLiveData<String>("")
    var given_nickname =MutableLiveData<String>("")
    var position =MutableLiveData<Int>(0)
    var hogamSendCheck =MutableLiveData<Boolean>(false)

    var confirmProfile =MutableLiveData<Boolean>(false)
    var confirmCrush =MutableLiveData<Boolean>(false)
    var startTime= MutableLiveData<Long>(0 )
    var endTime =MutableLiveData<Long>(0)
    var setnick = MutableLiveData<String>("")
    var opennick = MutableLiveData<Boolean>(false)
    var isnotheart = MutableLiveData<Boolean>(false)
    fun GetMatchingData(u_seq:String,sub_seq:String,type:String){
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

                                    matching_data.value = data!!
                                    if (data.pc_status.equals("2")||data.pc_status.equals("3")) {
                                        opennick.value = true
//                                            setnick.value = "sdfsdf"
                                    }else {
                                        opennick.value = false
                                        if (data!!.give_seq == my_seq.value) {
                                            setnick.value = data.re_nickname
                                        } else {
                                            setnick.value = data.given_nickname
                                        }
                                    }
                                    if (type.equals("호감완료")){
                                        btn_type.value = ProfileStatus.PHONE_OPEN
                                    }
                                    Log.d("GetMatchingData", matching_data.value.toString())
                                }
                            }
                        }
                    }else{

                    }
                }

                override fun onFailure(call: Call<ProfileMatchingData>, t: Throwable) {
                    Log.d("MatchingDataError", t.message!!)
                }
            })
            }
    }
    fun GetProfile(u_seq:String,u_lat:String,u_lon:String,sub_seq:String){
        startTime.value =System.currentTimeMillis()
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(MatchingService::class.java).GetUserProfile(u_seq, u_lat, u_lon, sub_seq)
            d!!.enqueue(object : retrofit2.Callback<ResultUserInformationData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultUserInformationData>,
                    response: Response<ResultUserInformationData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if(type.equals("success")) {
                            endTime.value =System.currentTimeMillis()
                            println("통신elapsedTime : ${endTime.value!! - startTime.value!!}")
                            var rows = data.rows
                            var car = data.car
                            if (rows.size != 0) {
                                user_info.value = rows.get(0)
                                Log.d("가져온", rows.get(0).toString())
                                titleText.value = user_info.value!!.u_type!!
                                charmlist.value = user_info.value!!.GetCharmlist()
                                //personerlist.value = user_info.value!!.GetPersonerlist()
                                musictypelist.value = user_info.value!!.GetMusiclist()
                                sub_image.value = user_info.value!!.subImage!!
                                user_car.value =car.get(0)
                                rows.get(0).u_charm
                            }
                        }
                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultUserInformationData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                }

            })

        }
    }

    fun GetRpmAvg(u_seq:String){
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(MatchingService::class.java).GetRpmScoreAvg(u_seq)
            d!!.enqueue(object : retrofit2.Callback<ResultRpmData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultRpmData>,
                    response: Response<ResultRpmData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        Log.d("RPM", data.toString())
                        var type = data.type
                        if(type.equals("success")) {

                            var score = data.score
                            if(score!=null) {
                                rpmscore.value = score.toInt()
                            }else{
                                rpmscore.value = 0
                            }
                        }
                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultRpmData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                }

            })

        }
    }
    fun ChangeSubIndex(index:Int){
        if(sub_image.value!!.size > index) {
            subviewindex.value = index
        }
    }
    fun SetProfileBottomAction(){
       var type = btn_type .value
        when(type){
            ProfileStatus.PROFILE_READ_ACCEPT->{
                confirmProfile.value = true
//                ProfileReadAccept()
            }
            ProfileStatus.LIKE_ACCEPT->{
                confirmCrush.value = true
//                LikeAccept()
            }
            ProfileStatus.LIKE_SEND->{
                isdirect.value = false
                startlike.value = true
                //LikeSend("","10")
            }
            ProfileStatus. DIRECT_LIKE_SEND->{
                isdirect.value = true
                startlike.value = true
            }
            ProfileStatus.PHONE_READ->{
                profileOpenCheck.value = true
//                PhoneOpen("호감완료")
            }
            ProfileStatus.PHONE_OPEN,  ProfileStatus.RPM_EVAL->{
                setact.value = 2
            }
            else -> {}

        }
    }

    fun ProfileReadAccept(){
        Log.d("매칭", "프로필 승인")
        if (   apicall.value ==false){
            apicall.value = true

        var rdata = matching_data.value!!
        var pc_seq = rdata.pc_seq
        var send_seq = my_seq.value
        var sub_seq = rdata.subUser.toString()
        var giveNick = ""
        var sub_user = matching_data.value!!.subUser
        if (matching_data.value!!.subUser == send_seq){
            sub_user = matching_data?.value!!.give_seq
            giveNick = rdata.given_nickname

        }else{
            giveNick = rdata.re_nickname
        }
        Log.d("매칭", rdata.toString())

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(MatchingService::class.java).ProfileReadAccept(pc_seq,"Y",send_seq,sub_user!!,giveNick)
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if(type.equals("success")) {
                            btn_type.value = ProfileStatus.LIKE_SEND
                        }
                    }
                    apicall.value = false

                }

                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                    apicall.value = false
                }

            })

        }
        }
    }


    fun LikeAccept(){
        if (apicall.value == false){
            apicall.value = true

        Log.d("매칭", "호감수락")
        var rdata = matching_data.value!!
        var pc_seq = rdata.pc_seq
        var u_seq = my_seq.value.toString()
        var sub_seq = ""
        var giveNick = ""
        if(rdata.subUser.toString().equals(u_seq)){
            sub_seq = rdata.give_seq.toString()
            giveNick = rdata.given_nickname!!

        }else{
            giveNick = rdata.re_nickname!!
            sub_seq = rdata.subUser.toString()

        }
        Log.d("매칭", rdata.toString())

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(MatchingService::class.java).LikeAccept(pc_seq,"2","",u_seq,sub_seq,giveNick)
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if(type.equals("success")) {
                            if (rdata.isflex.equals("1")){
                                PhoneOpen("호감완료")
                            }else{
                                btn_type.value = ProfileStatus.PHONE_READ
                            }
                            if (opennick.value!!) {
                                opennick.value = true
                            }
                        }
                    }
                    apicall.value = false
                }

                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                    apicall.value = false
                }

            })

        }
        }
    }
//    fun GetRpmScore(){
//        CoroutineScope(Dispatchers.IO).launch {
//            val d = NetworkManager.serveradapter!!.create(MatchingService::class.java).GetRpmScore(my_seq.value,sub_seq.value)
//            d!!.enqueue(object : retrofit2.Callback<ResultRpmData> {
//                override fun onResponse(
//                    call: retrofit2.Call<ResultRpmData>,
//                    response: Response<ResultRpmData>
//                ) {
//                    var data = response.body()
//                    if (data != null) {
//                        Log.d("데이터", data.toString())
//                        var type = data.type
//
//                        if(type.equals("success")) {
//                            var score =data.score
//                            rpmindex.value = score.toInt()
//
//                        }
//                    }
//
//                }
//
//                override fun onFailure(call: retrofit2.Call<ResultRpmData>, t: Throwable) {
//                    Log.d("failer", t.message!!)
//                }
//
//            })
//
//        }
//    }
    fun PhoneOpen(type2: String){

    if (popencall.value==false){
        popencall.value =true

        var rdata = matching_data.value!!
        var pc_seq = rdata.pc_seq
        var u_seq = my_seq.value.toString()
        var sub_seq = ""

       var giveNick =""
      var givennick =""
        if(rdata.subUser.toString().equals(u_seq)){
            sub_seq = rdata.give_seq.toString()
            giveNick = rdata.given_nickname!!
            givennick = rdata.re_nickname
        }else{
            sub_seq = rdata.subUser.toString()
            giveNick = rdata.re_nickname!!
            givennick = rdata.given_nickname
        }


        Log.d("플렉스", rdata.toString())
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(MatchingService::class.java).PhoneOpen(u_seq,sub_seq,pc_seq,giveNick,givennick,rdata.isflex)
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        var message = data.message
                        Log.d("PhoneOpen", data.toString())
                        if(type.equals("success")) {
                            if(!my_seq.value.equals(sub_seq)) {
                                if (type2.equals("호감완료")){
                                    GetMatchingData(my_seq.value!!, sub_seq,type2)
                                    profileOpen.value = true

                                }

                            }

                        }else if (type.equals("notheart")){
//                            if (message.equals("하트 갯수가 부족합니다.")){
                                isnotheart.value = true
                                //toast.value = "하트 갯수가 부족합니다."
//                            }
                        }
                    }
                    popencall.value =false

                }

                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                    popencall.value =false
                }

            })

        }
    }
    }


    fun RealNickRead(){
        if (apicall.value ==false){
            apicall.value = true

        Log.d("매칭", "4")
        var rdata = matching_data.value!!

        var u_seq = my_seq.value.toString()
        var sub_seq = ""
        if(rdata.subUser.toString().equals(u_seq)){
            sub_seq = rdata.give_seq.toString()
        }else{
            sub_seq = rdata.subUser.toString()
        }


        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(MatchingService::class.java).ReadRealNick(u_seq,sub_seq)
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if(type.equals("success")) {

                        }
                    }
                    apicall.value = false

                }

                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                    apicall.value = false

                }

            })

        } }
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
    fun EndDateCheck():Boolean{

        var rdata = matching_data.value!!
        var endstring = rdata.p_phone_end
        if(endstring != null){
        var enddate= dateTimeToMillSec(endstring!!)
        var nowdata =Date()
        var ed = Date(enddate)
        if(nowdata.compareTo(ed)<0){
            return true
        }else{
            return false
        }
        }else{
            return false
        }
    }
    fun setlasttime(){
        if (matching_data.value!!.safeNumber != null){
            btn_main.value = matching_data.value!!.safeNumber!!
        }

        var rdata = matching_data.value!!
        var endstring = rdata.p_phone_end
        if(endstring != null) {
            var enddate = dateTimeToMillSec(endstring!!)
            var ed = Date(enddate)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
            var string = sdf.format(ed)
            btn_sub.value = string+" 까지 연락 가능합니다."
        }
    }
    fun SetTopBtn(){
        when(btn_type.value){
            ProfileStatus.PHONE_READ->{
                profileOpenCheck.value = true
              //  PhoneOpen("")
            }
            ProfileStatus.PHONE_OPEN->{
                setact.value = 1
            }
            ProfileStatus.RPM_EVAL->{
                setact.value = 2
            }
            ProfileStatus.LIKE_SEND->{
                toast.value = "서로 호감을 승인해야 연락처를 확인할수 있습니다."
            }
            ProfileStatus.LIKE_WAIT->{
                toast.value = "서로 호감을 승인해야 연락처를 확인할수 있습니다."
            }
            ProfileStatus.PROFILE_READ_ACCEPT->{
                toast.value = "서로 호감을 승인해야 연락처를 확인할수 있습니다."
            }
            ProfileStatus.LIKE_ACCEPT->{
                toast.value = "서로 호감을 승인해야 연락처를 확인할수 있습니다."
            }
            ProfileStatus.DEFAULT->{
                setact.value = 2
            }

            else -> {}
        }
    }

}