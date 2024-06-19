package com.supercarlounge.supercar.ui.activity




import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.MediaController
import android.widget.VideoView
import com.buzzvil.buzzad.analytics.BATracker
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.MainApplication.Companion.token
import com.supercarlounge.supercar.Network.*
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.data.ResultLoginData
import com.supercarlounge.supercar.data.ResultPasswordSettiongData
import com.supercarlounge.supercar.data.ResultTokenData
import com.supercarlounge.supercar.data.ResultUserInformationData
import com.supercarlounge.supercar.databinding.ActivityIntroBinding
import com.supercarlounge.supercar.ui.priview.activity.PrvMainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class IntroActivity : BaseActivitiy() {

    var mp : MediaPlayer? =null
    var videoView: VideoView? = null
    private lateinit var binding: ActivityIntroBinding
    private lateinit var application: MainApplication
    var type =0
    var u_police = ""
    var u_seq = ""
    var u_phone = ""
    var u_token = ""
    var u_admin = ""
    var u_genfer = ""
    var locationCheck = false
    var screen: String?= ""
    var pushData :String?= ""
    var sub_seq= ""
    var uid= ""
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val window = window
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        application=applicationContext as MainApplication
//        val executor: Executor = Executors.newSingleThreadExecutor()
//        executor.execute(Runnable {
//            try {
//                val adInfo = AdvertisingIdClient.getAdvertisingIdInfo(this)
//                val advertisingId = adInfo.id
//                Log.d("ADID",advertisingId.toString())
//                // Do something with advertising ID
//            } catch (e: IOException) {
//                e.printStackTrace()
//            } catch (e: GooglePlayServicesNotAvailableException) {
//                e.printStackTrace()
//            } catch (e: GooglePlayServicesRepairableException) {
//                e.printStackTrace()
//            }
//        })


        BATracker.init(this, "830429142021853");
        BATracker.cpeCompleted(this);
         screen = intent.getStringExtra("screen").toString()
        if (intent.getStringExtra("pushData") != null){
            pushData = intent.getStringExtra("pushData")

        }






        if (application.u_phone.isNotEmpty()){

            //checkLogin(application.u_phone,"ExponentPushToken[NHqSuILdMU1-YSeK8T0CSi]",application)

                checkLogin(
                    application.u_phone

                    ,application.push_token,application)


        }else{

            type = 0
            typeIntent(type)
        }


//           val path = Uri.parse("android:resource://$packageName/" + com.example.supercarx.R.raw.intro)
        val video = Uri.parse("android.resource://" + packageName + "/" + com.supercarlounge.supercar.R.raw.intro)
//        val path = Uri.parse("https://download.atmark-techno.com/sample/bbb/big-buck-bunny-30sec-800x480.mp4")
            binding.videoView.setVideoURI(video)

            binding.videoView.setMediaController(MediaController(this))
        binding.videoView.requestFocus()
        binding.videoView.setMediaController(null);
        binding.videoView.setOnCompletionListener {


            Log.d("인트로영상", "끝: ")

//            Handler(Looper.getMainLooper()).postDelayed({
//
////                val i = Intent(this@IntroActivity, CarCheckActivity::class.java)
////                startActivity(i)
////                overridePendingTransition(R.anim.fadein, R.anim.fadeout)
////                this@IntroActivity.finish()
////                if (application.userData.u)
////            if(application.getToken())
//
//
//                typeIntent(type)
//
//            }, 100)


            }
        binding.videoView.setOnPreparedListener {
//            binding.videoView.start()
        }
        binding.videoView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                return true
            }
        })
        Firebase.dynamicLinks.getDynamicLink(intent).addOnSuccessListener(this){ pendingdynamicLinkData->
            var deepLink :Uri? = null
            if(pendingdynamicLinkData !=null){
                deepLink = pendingdynamicLinkData.link
            }
            if(deepLink !=null && deepLink.getBooleanQueryParameter("code",false)){
                var code = deepLink.getQueryParameter("code")
                if(code!=null) {
                    application.invite_code = code
                    Log.d("LINKCODE", "$code")
                }
            }

        }
        Firebase.dynamicLinks.getDynamicLink(intent).addOnSuccessListener(this){ pendingdynamicLinkData->
            var deepLink :Uri? = null
            if(pendingdynamicLinkData !=null){
                deepLink = pendingdynamicLinkData.link
            }
            if(deepLink !=null && deepLink!!.getBooleanQueryParameter("seq",false)){
                Log.d("로케이션seq", "onCreate: 1")
                var location = deepLink!!.getQueryParameter("seq")
                Log.d("로케이션seq", location.toString())
                if(location!=null) {
                    locationCheck =true
                    location =   location.trim()
                    var handler = Handler(Looper.getMainLooper())


                    var runnable = Runnable {
                        run {

                            var i  =Intent(this,LocationSpotDetailActivity::class.java )
                            i.putExtra("spot_seq",location)
                            finishAffinity()
                            startActivity(i)
                            locationCheck= false

                        }
                    }

                    handler.postDelayed(runnable, 1000)


                    Log.d("로케이션seq", "$location")
                }
            }

        }
    }
    override fun onPause() {
        super.onPause()

        //비디오 일시 정지
        if (binding.videoView.isPlaying) binding.videoView.pause()

    }

    fun checkLogin(u_phone: String, u_token: String ,application: MainApplication) {
        Log.d("checkLogin", "u_phone =$u_phone , u_token =$u_token ")
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(LoginService::class.java).checkLogin(u_phone, u_token, true)
            d!!.enqueue(object : retrofit2.Callback<ResultLoginData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultLoginData>,
                    response: Response<ResultLoginData>
                ) {

                    var data = response.body()
                    Log.d("데이터", data.toString())
                    if (data != null) {
                        Log.d("checkLogin data.message:", data.message.toString())
                        Log.d("checkLogin data.status:", data.status.toString())
                        u_seq =data.u_seq.toString()
                        if (data.message.equals("가입된 회원 정보가 없습니다")){
                            type =0
                            application.savePhoneNumber("")
                            typeIntent(type)
                        }else if (data.message.equals("승인 대기 상태입니다")){

                            type =3
                            Log.d("폰번호", application.u_phone)
                            typeIntent(type)
                        }else if (data.status.equals("success")){

                            getToken(application.u_phone)

                        }else if (data.message.equals("미승인 상태입니다")){
//                            getToken(application.u_phone)
                            type =4
                            typeIntent(type)
                        }else if (data.message.equals("핸드폰번호를 입력해주세요.")){
                            type =0
//                            getToken(application.u_phone)
                            typeIntent(type)
                        }else if(data.message.equals("퀵 회원입니다")){
                            getQuickUserInformation(u_seq.toString())

                        }else if(data.message.equals("휴면 상태입니다")){
                            type =81
                            typeIntent(type)
                        }else if(data.message.equals("탈퇴신청 상태입니다")){
                            type =82
                            typeIntent(type)
                        }else{
                            type =2
                            typeIntent(type)
                        }

//                        Log.d("setPassword", data.message)

                    }else{
                        getToken(application.u_phone)
                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultLoginData>, t: Throwable) {
                    Log.d("check", t.toString())
                }

            })

        }

    }

    fun getToken(u_phone:String) {


        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(TokenService::class.java).getToken(
                u_phone

                ,application.push_token
            )
            d!!.enqueue(object : retrofit2.Callback<ResultTokenData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultTokenData>,
                    response: Response<ResultTokenData>
                ) {

                    token = "Bearer "+response.body()?.token.toString()
                //    application.tokenBody =response.body()?.token.toString()
                    var data = response.body()
                    Log.d("토큰1", response.body()?.token.toString())
                    if (data != null) {

                        var data = data.data
                        var user = data.user
                        var u_seq = data.user.u_seq
                        application.user_u_uuid = data.user.u_uuid


                        if (data != null) {
//                            Log.d("test", user.toString())
//                            saveip(u_suq = user.u_seq,
//                                u_admin = user.u_admin,
//                                u_uuid = user.u_uuid,
//                                u_type = user.u_type,
//                                u_birthday = user.u_birthday,
//                                u_gender = user.u_gender,
//                                u_police = user.u_police
//                            , u_image = user.u_image
//                                )

                            if (data.user.u_police == 0){

                                if (application.u_phone.isNotEmpty()){

                                    Log.d("확인", "onResponse: 1")
                                    if (user.u_admin.equals("M")){
                                        Log.d("확인", "onResponse: 2")
                                        type=3
                                        typeIntent(type)
                                    }else if (user.u_admin.equals("Y")){
                                           Log.d("확인", "onResponse: 3")
                                        type =1
                                        getUserInformation(data.user.u_seq.toString())
                                    }else if (user.u_admin.equals("R")){
                                        type =81
                                        getRestUserInformation(data.user.u_seq.toString())
                                    }else if (user.u_admin.equals("W")){
                                        Log.d("휴면탈퇴", "인트로에서 바로 W 들어옴")
                                        type =82
                                        getWithdrawUserInformation(data.user.u_seq.toString())
                                    }else if (data.user.u_admin.equals("N")){
                                        type=4
                                        typeIntent(type)
                                        Log.d("확인", "onResponse:4")
                                    }else{
                                        getQuickUserInformation(u_seq.toString())
                                        Log.d("확인", "onResponse: 5")
                                    }
                                }else{
                                    type =0
                                    typeIntent(type)
                                }
                            }else{
                                type=6
                                typeIntent(type)
                            }
                        }
                    }
                }

                override fun onFailure(call: retrofit2.Call<ResultTokenData>, t: Throwable) {
                    Log.d("check", t.toString())
                }

            })

        }

    }
    fun getUserInformation(u_seq : String){
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(UserService::class.java).getUserInformation(u_seq)
            d!!.enqueue(object : retrofit2.Callback<ResultUserInformationData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultUserInformationData>,
                    response: Response<ResultUserInformationData>
                ) {
                    var data = response.body()
                    Log.d("태그1", data.toString())
                    if (data != null) {
                        application.userData = data.rows[0]
                        application.userCarData =data.car

                        if (application.userData!!.u_lock_screen =="Y"){
                            passwordSettingTrueFalse(u_seq)
                        }else{
                            type = 1
                            typeIntent(type)
                        }
                    }
                }
                override fun onFailure(call: retrofit2.Call<ResultUserInformationData>, t: Throwable) {
                    Log.d("check", t.toString())
                    Log.d("태그1", "333")
                }
            })
        }
    }

    fun getRestUserInformation(u_seq : String){
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(UserService::class.java).getUserInformation(u_seq)
            d!!.enqueue(object : retrofit2.Callback<ResultUserInformationData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultUserInformationData>,
                    response: Response<ResultUserInformationData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        application.userData = data.rows[0]
                        application.userCarData =data.car

                        type = 81
                        typeIntent(type)
                    }
                }
                override fun onFailure(call: retrofit2.Call<ResultUserInformationData>, t: Throwable) {

                }
            })
        }
    }
    fun getWithdrawUserInformation(u_seq : String){
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(UserService::class.java).getUserInformation(u_seq)
            d!!.enqueue(object : retrofit2.Callback<ResultUserInformationData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultUserInformationData>,
                    response: Response<ResultUserInformationData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        application.userData = data.rows[0]
                        application.userCarData =data.car

                        type = 82
                        typeIntent(type)
                    }
                }
                override fun onFailure(call: retrofit2.Call<ResultUserInformationData>, t: Throwable) {

                }
            })
        }
    }

    fun getQuickUserInformation(u_seq : String){
        Log.d("태그1", "111")
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(UserService::class.java).getQuickUserInformation(
                u_seq,
                )
            d!!.enqueue(object : retrofit2.Callback<ResultUserInformationData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultUserInformationData>,
                    response: Response<ResultUserInformationData>
                ) {
                    var data = response.body()
                    Log.d("태그1", data.toString())
                    if (data != null) {
                        application.userData = data.rows[0]
                        Log.d("가져온 유저 데이터", application.userData.toString())
                        type = 6
                        typeIntent(type)
                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultUserInformationData>, t: Throwable) {
                    Log.d("check", t.toString())
                    Log.d("태그1", "333")
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

                       if (data.message == "비밀번호 존재") {
                           type = 5
                       }else{
                           type = 1
                       }
                        typeIntent(type)
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
    fun typeIntent(type:Int){

        when (type){
            //성공

            0->{
                //로그인
                val i = Intent(this, LoginActivity::class.java)
                startActivity(i)
              //  overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                this.finish()
            }
            1->{
                val i = Intent(this@IntroActivity, MainActivity::class.java)
                i.putExtra("screen",screen)
                i.putExtra("pushData",pushData)
                i.putExtra("sub_seq",sub_seq)
                i.putExtra("uid",uid)
                startActivity(i)
              //  overridePendingTransition(R.anim.fadein, R.anim.fadeout)

                this@IntroActivity.finish()
            }
            2->{

                val i = Intent(this@IntroActivity, MainActivity::class.java)
                i.putExtra("u_police",u_police)
                i.putExtra("screen",screen)
                i.putExtra("pushData",pushData)
                i.putExtra("sub_seq",sub_seq)
                i.putExtra("uid",uid)

                startActivity(i)
             //   overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                this@IntroActivity.finish()
            }
            3->{
                //승인중
                val i = Intent(this@IntroActivity, SubscriptionApplicationCompleteActivity::class.java)
                i.putExtra("u_admin","M")
                i.putExtra("u_seq",u_seq)
                Log.d("대기u_seq", u_seq)
                    startActivity(i)
                 //   overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                    this@IntroActivity.finish()
            }
            4->{
                //반려
                val i = Intent(this@IntroActivity, SubscriptionApplicationCompleteActivity::class.java)

                i.putExtra("u_admin","N")
                i.putExtra("u_seq",u_seq)

                Log.d("반려u_seq", u_seq)
                startActivity(i)
              //  overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                this@IntroActivity.finish()
            }
            5->{
                //잠금
                val i = Intent(this@IntroActivity, LockActivity::class.java)

                startActivity(i)
               // overridePendingTransition(R.anim. fadein, R.anim.fadeout)
                this@IntroActivity.finish()
            }
            6->{
                //퀵유저 화면
                val i = Intent(this@IntroActivity, PrvMainActivity::class.java)
//                i.putExtra("u_seq","")
//                i.putExtra("u_admin",u_ad)
                i.putExtra("view","퀵유저")
                i.putExtra("u_seq",application.userData?.u_seq.toString())
                i.putExtra("u_name", application?.userData?.u_name)
                i.putExtra("u_gender", application?.userData?.u_gender)
                i.putExtra("u_birthday", application?.userData?.u_birthday)
                i.putExtra("u_phone", application?.userData?.u_phone)
                startActivity(i)
               // overridePendingTransition(R.anim. fadein, R.anim.fadeout)
                this@IntroActivity.finish()
            }
            81->{
                // 휴면중
                val i = Intent(this@IntroActivity, UserRest2Activity::class.java)
                i.putExtra("u_admin","R")
                i.putExtra("u_seq",u_seq)
                startActivity(i)
                this@IntroActivity.finish()
            }
            82->{
                // 탈퇴신청중
                val i = Intent(this@IntroActivity, UserWithdraw3Activity::class.java)
                i.putExtra("u_admin","W")
                i.putExtra("u_seq",u_seq)
                startActivity(i)
                this@IntroActivity.finish()
            }
            else -> {}
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        //

        binding.videoView.stopPlayback()
    }

    override fun finish() {
        super.finish()
       // overridePendingTransition(com.supercarlounge.supercar.R.anim. fadein, com.supercarlounge.supercar.R.anim.fadeout)
    }
}