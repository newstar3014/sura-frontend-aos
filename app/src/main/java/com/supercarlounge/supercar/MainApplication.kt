package com.supercarlounge.supercar


import android.Manifest
import android.R.attr.path
import android.annotation.SuppressLint
import android.app.*
import android.content.*
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.location.Location
import android.media.AudioAttributes
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Looper
import android.os.PowerManager
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.BillingClient
import com.facebook.ads.AudienceNetworkAds
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.dynamiclinks.ktx.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kakao.sdk.common.KakaoSdk
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.NaverMapSdk.NaverCloudPlatformClient
import com.supercarlounge.supercar.Network.*
import com.supercarlounge.supercar.data.*
import com.supercarlounge.supercar.enumset.GuidePopType
import com.supercarlounge.supercar.ui.activity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.commons.lang3.StringEscapeUtils
import retrofit2.Response
import java.io.File
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.regex.Matcher
import java.util.regex.Pattern


val ALL_PUSH_TOPIC = "all_push"

class MainApplication : Application() {

    var prvVersion = "1.9.8"

    var notificationManager: NotificationManager? = null
    var firebaseMessaging: FirebaseMessaging? = null
    private var mFusedLocationProviderClient: FusedLocationProviderClient? =
        null // 현재 위치를 가져오기 위한 변수
    var mLastLocation: Location? = null // 위치 값을 가지고 있는 객체
    var mLocationRequest: LocationRequest? = null// 위치 정보 요청의 매개변수를 저장하는
    private val REQUEST_PERMISSION_LOCATION = 10
    var sharedPref: SharedPreferences? = null
    var suggesstPref: SharedPreferences? = null
    var lat: Double = 0.0
    var lon: Double = 0.0
    var scrollcheck = false
    var test =""
    var locationSearchText :String = ""
    var locationInfoPop = false
    var locationDetailPop = false
    var locationSearchMove = false


    var myCarSaveData :MyCarSaveData = MyCarSaveData("","","","","","",null, arrayListOf(), arrayListOf())

    // var userData : UserInformationData ? = null
    var userData: UserInformationData? = UserInformationData(
        0,
        "",
        "",
        0,
        "",
        "",
        "",
        arrayListOf(),
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "MALE",
        0,
        "https://supercarlounge.com:3002/images/user/c46830e9-cb9c-483c-a770-59dacae4db24/profile/d27ad151a47b430290104c6c563b9e8f.jpg",
        "",
        "",
        "",
        "",
        "",
        "슈라",
        "슈라",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        0,
        "",
        "",
        "1",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",  "","",""
    )
    var userBasicData: UserBasicData? = UserBasicData("", "", "", "")
    var user_u_uuid: String? = "dd6a97dc-da57-41e2-8970-2961c66171a2"
    var userCarData: ArrayList<ArrayList<UserInformationCarData>>? = arrayListOf()
    var kakao_app_key = "40ca7e07f42ef60bf2980a74cc7d8ed7"
    var drive_check_date: String = ""
    var keyHash = ""
    var board_check_date: String = ""
    var u_phone: String = ""
    var htmlText: String = ""
    var push_token: String = ""
    var quick_view: String = ""
    val cor: AtomicInteger = AtomicInteger(0)
    var billingClient: BillingClient? = null
    var checkguide1:String = ""
    var checkguide2:String = ""
    var checkguide3 :String= ""
    var checkguide4 :String= ""
    var checkguide5 :String= ""
    var checkguide6 :String= ""
    var checkguide7 :String= ""
    var checkguide8 :String= ""

    //회원가입 로그 관련 체크
    var joinCheckArray :ArrayList<Int> = arrayListOf()
    var loginLog = false
    var carLog =false
    var carBrandLog =false
    var carImageLog =false
    var informationOwnerLog = false
    var informationPassengerLog = false
    var incomeImageLog = false
    var previewLog = false


    var checkPopUp :String= ""
    var sugeestlist: ArrayList<ProfileSuggestionData>? = null
    var select_index: ArrayList<String>? = null
    var invite_code = ""
    fun getID(): Int {
        return cor.incrementAndGet()
    }

    companion object {
        lateinit var preferences: PreferenceUtil
        lateinit var instance: MainApplication
        var token: String =
            "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NDA5LCJpYXQiOjE2NjkxODcxNTIsImV4cCI6MTY2OTI3MzU1Mn0.mE7xlJekYTOsonc0P-iS3vjRkgz-kBXpBHlDZ_sCcCI"


        fun ApplicationContext(): Context {
            return instance.applicationContext
        }

    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycleObserver(this))
        NaverMapSdk.getInstance(this).client = NaverCloudPlatformClient("6xbzkmlut0")
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.package.notification")
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        firebaseMessaging = FirebaseMessaging.getInstance()
        sharedPref = getSharedPreferences("auto", Context.MODE_PRIVATE)
        suggesstPref = getSharedPreferences("suggesst", Context.MODE_PRIVATE)
        preferences = PreferenceUtil(applicationContext)
//        getKeyHashBase64()
        firebaseMessaging!!.token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {

                return@OnCompleteListener
            }
            push_token = task.result
            Log.d("PUSH_TOKEN", push_token)
        })
        AudienceNetworkAds.initialize(this);
        loginLog = sharedPref!!.getBoolean("loginLog", false)!!

        checkguide1 = sharedPref!!.getString("checkguide1", "")!!
        checkguide2 = sharedPref!!.getString("checkguide2", "")!!
        checkguide3 = sharedPref!!.getString("checkguide3", "")!!
        checkguide4 = sharedPref!!.getString("checkguide4", "")!!
        checkguide5 = sharedPref!!.getString("checkguide5", "")!!
        checkguide6 = sharedPref!!.getString("checkguide6", "")!!
        checkguide7 = sharedPref!!.getString("checkguide7", "")!!
        checkguide8 = sharedPref!!.getString("checkguide8", "")!!
        checkPopUp = sharedPref!!.getString("checkPopUp","")!!
        var u_seq = sharedPref!!.getInt("u_police", 0)
        drive_check_date = sharedPref!!.getString("drive_date", "")!!
        board_check_date = sharedPref!!.getString("anonymous_date", "")!!
        u_phone = sharedPref!!.getString("u_phone", "")!!
        var check1 = getall_push()
        settingpush(check1)
        KakaoSdk.init(this, "40ca7e07f42ef60bf2980a74cc7d8ed7")
        sugeestlist = getStringArrayPrefPr("profilesugesst")
        select_index = getStringArrayPref("SelectSugesst")

        CheckSuggestDate()



    }
    fun CheckSuggestDate():Boolean{
        if (sugeestlist == null){
            return false
        }else {
            val sdfDay = SimpleDateFormat("dd")
            val sdfH = SimpleDateFormat("HH")
            var nDay = sdfDay.format(Date())
            var nHour = sdfH.format(Date())

            var day = suggesstPref!!.getString("profileDay", "0")




            if (!nDay.equals(day)) {
                if (nHour?.toInt()!! >= 10) {
                    suggesstPref!!.edit().putString("profileDay", nDay).commit()
                    suggesstPref!!.edit().putString("profileHour", nHour).commit()
                    sugeestlist = null
                    
                    return false
                } else {

                    return true
                }
            } else {

                return true
            }

        }

    }
    @RequiresApi(Build.VERSION_CODES.P)
    fun getKeyHashBase64(): String {
        try {
            val packageInfo = packageManager.getPackageInfo(
                packageName, PackageManager.GET_SIGNING_CERTIFICATES
            )
            val signingInfo = packageInfo.signingInfo.apkContentsSigners

            for (signature in signingInfo) {
                val messageDigest = MessageDigest.getInstance("SHA")
                messageDigest.update(signature.toByteArray())
                val keyHash = String(Base64.encode(messageDigest.digest(), 0))
                Log.d("KeyHash", keyHash)
                return keyHash
            }

        } catch (e: Exception) {
            Log.e("Exception", e.toString())
        }
        return keyHash
    }

    fun saveip(
        u_suq: Int,
        u_admin: String,
        u_uuid: String,
        u_type: String,
        u_birthday: String,
        u_gender: String,
        u_police: Int,
        u_image: String
    ) {
        sharedPref!!.edit().putInt("u_seq", u_suq).commit()
        sharedPref!!.edit().putString("u_admin", u_admin).commit()
        sharedPref!!.edit().putString("u_uuid", u_uuid).commit()
        sharedPref!!.edit().putString("u_type", u_type).commit()
        sharedPref!!.edit().putString("u_birthday", u_birthday).commit()
        sharedPref!!.edit().putString("u_gender", u_gender).commit()
        sharedPref!!.edit().putInt("u_police", u_police).commit()
        sharedPref!!.edit().putString("u_image", u_image).commit()
    }

    fun save_loginLog(){
        sharedPref?.edit()?.putBoolean("loginLog",true)?.apply()
    }

    fun save_guide(type: GuidePopType) {
        var key = ""
        var result = ""
        var sf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var date = sf.format(Date())

        when (type) {
            GuidePopType.GUIDE_DRIVE_VIEW -> {
                key = "checkguide1"
                checkguide1 = date
            }
            GuidePopType.GUIDE_DRIVE_WRITE -> {
                key = "checkguide2"
                checkguide2 = date
            }
            GuidePopType.GUIDE_VIP_LOUNGE -> {
                key = "checkguide3"
                checkguide3 = date
            }
            GuidePopType.GUIDE_BOARD -> {
                key = "checkguide4"
                checkguide4 = date
            }

            GuidePopType.GUIDE_BOARD_WRITE -> {
                key = "checkguide5"
                checkguide5 = date
            }
            GuidePopType.GUIDE_MY_CAR_BOARD -> {
                key = "checkguide6"
                checkguide6 = date
            }
            GuidePopType.LOCATION -> {
                key = "checkguide7"
                checkguide7= date
            }
            GuidePopType.MY_PAGE -> {
                key = "checkguide8"
                checkguide8 = date
            }

            else -> {}
        }
        Log.d("날짜1", date)
        sharedPref!!.edit().putString(key, date).apply()
    }

    fun savePhoneNumber(u_phoneset: String) {
        sharedPref!!.edit().putString("u_phone", u_phoneset).apply()
        u_phone = u_phoneset
    }

    fun getUserInformation(u_seq: String) {
        Log.d("", u_seq.toString())
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(UserService::class.java).getUserInformation(
                u_seq,

                )
            d!!.enqueue(object : retrofit2.Callback<ResultUserInformationData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultUserInformationData>,
                    response: Response<ResultUserInformationData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        userData = data.rows[0]
                        userCarData = data.car

                        Log.d("가져온 유저 데이터", userData.toString())
                        Log.d("가져온 유저 차 데이터", userCarData.toString())

                    }

                }

                override fun onFailure(
                    call: retrofit2.Call<ResultUserInformationData>,
                    t: Throwable
                ) {
                    Log.d("check", t.toString())
                }

            })

        }

    }
    fun joinLog(context: Context,step :String,name:String) {
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(JoinService::class.java).joinLog(
                "ANDROID",
                step,
                getDeviceUuid(),
                getDeviceName(),
                name
            )
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        Log.d("조인로그", "onResponse: ")

                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Log.d("check", t.toString())
                }

            })

        }

    }

    fun getToken(u_phone: String, u_token: String) {
        Log.d("번호", u_phone)
        var list: ArrayList<DriveAwayData> = arrayListOf()
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(TokenService::class.java).getToken(
                u_phone, u_token
            )
            d!!.enqueue(object : retrofit2.Callback<ResultTokenData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultTokenData>,
                    response: Response<ResultTokenData>
                ) {

                    token = "Bearer " + response.body()?.token.toString()
                    // tokenBody =response.body()?.token.toString()
                    var data = response.body()
                    Log.d("토큰", token)

                    if (data != null) {
                        Log.d("미승인 데이터", data.toString())
                        var data = data.data
                        var user = data.user
                        user_u_uuid = data.user.u_uuid


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
                            getUserInformation(data.user.u_seq.toString())
                        }

                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultTokenData>, t: Throwable) {
                    Log.d("check", t.toString())
                }

            })

        }

    }

    @SuppressLint("RemoteViewLayout")
    fun getRemoteView(title : String, message:String): RemoteViews{
        val remoteView= RemoteViews("com.supercarlounge.supercar",R.layout.notification)
        remoteView.setTextViewText(R.id.title,title)
        remoteView.setTextViewText(R.id.message,message)
        remoteView.setImageViewResource(R.id.iv_main,R.mipmap.ic_launcher)
        return  remoteView
    }

    fun push_channel_create(channel_id: String, name: String, descriptionText: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
//            val name = getString(R.string.like_channel)
//            val descriptionText = getString(R.string.like_channel_description)
            val importance = NotificationManager.IMPORTANCE_LOW
            val mChannel = NotificationChannel(channel_id, name, importance)
            mChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
//            val soundUri =
//                    Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + applicationContext.packageName + "/" + R.raw.noti)
            val audioAttributes: AudioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            //mChannel.setSound(soundUri, audioAttributes)


//                if (!getpush()) {
//                    if (!channel_id.equals(NOTI_CHANNEL)) {
            notificationManager!!.createNotificationChannel(mChannel)
            Log.d("푸시", "33")
            Log.d("노티 생성", "push_channel_create: ")
//                    }
//                }


        }
    }


    fun SendNotification(title: String, text: String, channel_id: String,screen:String, send_seq:String,b_seq: String?,uid:String) {
        if (notificationManager == null) {
            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
        var notificationIntent: Intent? = null


//        notificationIntent!!.setAction(Intent.ACTION_MAIN)
//        notificationIntent!!.addCategory(Intent.CATEGORY_LAUNCHER)
//        notificationIntent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)


        if (screen.equals("ApplyProfile")|| screen.equals("MatchingManage")) {
            notificationIntent = Intent(this, ProfileActivity::class.java);
            notificationIntent!!.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            notificationIntent.putExtra("screen", screen)
            notificationIntent.putExtra("sub_seq", send_seq)


            pushIntent(notificationIntent,channel_id,title,text)
        }else if (screen.equals("BoardDetail")||screen.equals("BoardLike") ){
            notificationIntent = Intent(this, PostActivity::class.java);
            notificationIntent!!.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            notificationIntent.putExtra("screen", screen)
            notificationIntent.putExtra("u_seq", send_seq.toInt())
            notificationIntent.putExtra("sub_seq", send_seq)
            notificationIntent.putExtra("b_seq", b_seq)
            notificationIntent.putExtra("uid", uid)
            pushIntent(notificationIntent,channel_id,title,text)

        }else if (screen.equals("MycarDetail")||screen.equals("MycarLike") ){
            notificationIntent = Intent(this, MyCarBoardActivity::class.java);
            notificationIntent.putExtra("mmi_seq", b_seq)
        }else if(screen.equals("DriveLike")){
            DrivePassCheck(channel_id,title,text,screen,send_seq,b_seq.toString(),uid)

        }else if(screen.equals("MycarRankingHeart")){
            notificationIntent = Intent(this, MyCarBestBoardActivity::class.java);
            pushIntent(notificationIntent,channel_id,title,text)

        }


    }
    fun pushIntent(i :Intent,channel_id: String,title: String,text: String){
        var notificationIntent = i

        val pendingIntent: PendingIntent
        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getActivity(
                this,
                0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
//        var pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

        var builder = NotificationCompat.Builder(this, channel_id)
//        var builder = NotificationCompat.Builder(this, "channel_id")
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    getResources(),
                    R.mipmap.ic_launcher_round
                )
            ) //BitMap 이미지 요구
            .setContentTitle(title)
            .setContentText(text)

            // 더 많은 내용이라서 일부만 보여줘야 하는 경우 아래 주석을 제거하면 setContentText에 있는 문자열 대신 아래 문자열을 보여줌
            //.setStyle(new NotificationCompat.BigTextStyle().bigText("더 많은 내용을 보여줘야 하는 경우..."))
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setDefaults(Notification.DEFAULT_SOUND)

            .setContentIntent(pendingIntent) // 사용자가 노티피케이션을 탭시 ResultActivity로 이동하도록 설정
        var cid = getID();




        //OREO API 26 이상에서는 채널 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            builder.setSmallIcon(R.mipmap.ic_launcher); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남

        } else {
            builder.setSmallIcon(R.mipmap.ic_launcher)
        }
//        builder = builder.setContent(getRemoteView(title,text))
        var notification = builder.build()
        notification.flags = Notification.FLAG_AUTO_CANCEL
        notificationManager!!.notify(cid, notification); // 고유숫자로 노티피케이션 동작시킴
        val pm = getSystemService(POWER_SERVICE) as PowerManager

        @SuppressLint("InvalidWakeLockTag")
        val wakeLock = pm.newWakeLock(
            PowerManager.SCREEN_DIM_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "PUSH"
        )
        Log.d("푸시", "SendNotification: ")
        wakeLock.acquire(3000)
    }
    fun DrivePassCheck(channel_id: String,title: String,text: String,screen: String,send_seq: String,b_seq:String,uid: String) {
        var notificationIntent: Intent? = null
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(MatchingService::class.java).DrivePassCheck(
                userData?.u_seq.toString(),
            )
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if (type.equals("success")){

                            notificationIntent = Intent(this@MainApplication, RecentDriveListActivity::class.java);


                        }else{

                            notificationIntent = Intent(this@MainApplication, AlimActivity::class.java);

                        }
                        notificationIntent?.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        notificationIntent?.putExtra("screen", screen)
                        notificationIntent?.putExtra("sub_seq", send_seq)
                        notificationIntent?.putExtra("b_seq", b_seq)
                        notificationIntent?.putExtra("uid", uid)
                        notificationIntent?.putExtra("type", 1)
                        notificationIntent?.putExtra("push", false)

                        notificationIntent?.let { pushIntent(it, channel_id, title, text) }


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
    fun setlistview_padding(
        size: Int,
        listview: RecyclerView,
        sumcount: Int,
        includeEdge: Boolean,
    ) {
        val density = resources.displayMetrics.density
        var dip20: Int = (size * density).toInt()
        listview.addItemDecoration(GridSpacingItemDecoration(sumcount, dip20, includeEdge))
    }

    fun SaveDriveDate() {

        var sf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var date = sf.format(Date())
        drive_check_date = date
        sharedPref!!.edit().putString("drive_date", date).commit()
    }

    @SuppressLint("MissingPermission")
    fun getMac(context: Context): String {
        val manager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val info = manager.connectionInfo
        return info.macAddress.toUpperCase()
    }
    fun getDeviceUuid(): String? {
        return Settings.Secure.getString(applicationContext.contentResolver,Settings.Secure.ANDROID_ID)
    }



    fun getDeviceName(): kotlin.String? {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            capitalize(model)
        } else {
            capitalize(manufacturer).toString() + " " + model
        }
    }
    private fun capitalize(s: kotlin.String?): kotlin.String? {
        if (s == null || s.length == 0) {
            return ""
        }
        val first = s[0]
        return if (Character.isUpperCase(first)) {
            s
        } else {
            first.uppercaseChar().toString() + s.substring(1)
        }
    }
    fun SavePopUpDate() {

        var sf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var date = sf.format(Date())
        checkPopUp = date
        sharedPref!!.edit().putString("checkPopUp", date).commit()
    }

    fun checkDriveDateMonth(): Boolean {
        var saveData = sharedPref!!.getString("drive_date", "")
        var saveTime = convertStringToDate(saveData!!)
        var calendar = Calendar.getInstance()

        calendar.time = saveTime
        calendar.add(Calendar.MONTH, 1)

        var today = Date().time
        if (today > calendar.time.time) {
            Log.d("", "지남")
        } else {
            Log.d("", "안지남")
        }

        return false
    }

    fun checkAnonymousDateMonth(): Boolean {
        var saveData = sharedPref!!.getString("anonymous_date", "")
        var saveTime = convertStringToDate(saveData!!)
        var calendar = Calendar.getInstance()

        calendar.time = saveTime
        calendar.add(Calendar.MONTH, 1)

        var today = Date().time
        if (today > calendar.time.time) {
            Log.d("", "지남")
        } else {
            Log.d("", "안지남")
        }

        return false
    }

    fun convertStringToDate(x: String): Date {
        var format: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var date: Date? = null
        try {

            date = format.parse(x)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date!!
    }

    fun SaveAnonymousDate() {
        var sf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var date = sf.format(Date())
        board_check_date = date
        sharedPref!!.edit().putString("anonymous_date", date).commit()
    }


    fun Check30days(date: String): Boolean {
        var result = false
        Log.d("날짜", date.toString())
        if (!date.isNullOrEmpty()) {
            var sampleDate = date
            var sf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            var date = sf.parse(sampleDate)

            var today = Calendar.getInstance()

            var calcuDate = (date.time - today.time.time) / (60 * 60 * 24 * 1000)
            if (calcuDate < 30) {
                Log.d("날짜", "Check30days 30일안됨")
                result = true
            }

        }
        return result

    }

    fun Check1days(date: String): Boolean {
        var result = false
        if (!date.isNullOrEmpty()) {
            var sampleDate = date
            var sf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            var date = sf.parse(sampleDate)

            var today = Calendar.getInstance()

            var calcuDate = (date.time - today.time.time) / (60 * 60 * 24 * 1000)
            if (calcuDate < 1 && calcuDate>=0) {
                result = true
            }
        }
        return result

    }

    fun getRealPathFromUriForImagesAndVideo(contentUri: Uri): String? {
        var cursor: Cursor? = null


        return try {
            var proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = contentResolver.query(contentUri, proj, null, null, null)
            var column_index: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor!!.moveToFirst()
            cursor!!.getString(column_index)
        } catch (e: Exception) {
            contentUri.path
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setsetting(check1: Boolean) {
        sharedPref!!.edit().putBoolean("all_push", check1).commit()
        settingpush(check1)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun settingpush(check1: Boolean) {
        var noti1 = notificationManager!!.getNotificationChannel(ALL_PUSH_TOPIC)
        if (check1) {
            Log.d("푸시체크", "push생성: ")
            Log.d("푸시", "11")
            push_channel_create(
                ALL_PUSH_TOPIC,
                "전체알림입니다.",
                "전체알림 설정입니다."
            )
            firebaseMessaging!!.subscribeToTopic("ALL")
                .addOnCompleteListener { task: Task<Void?> ->
                    if (task.isSuccessful) {
                        Log.d(ContentValues.TAG, "토픽 구독 완료: 전체")
                    } else {
                        Log.e(ContentValues.TAG, "토픽 구독 실패", task.exception)
                    }
                }
            firebaseMessaging!!.subscribeToTopic("AOS")
                .addOnCompleteListener { task: Task<Void?> ->
                    if (task.isSuccessful) {
                        Log.d(ContentValues.TAG, "토픽 구독 완료: AOS")
                    } else {
                        Log.e(ContentValues.TAG, "토픽 구독 실패", task.exception)
                    }
                }
        } else {
            notificationManager!!.deleteNotificationChannel(ALL_PUSH_TOPIC)
            firebaseMessaging!!.unsubscribeFromTopic("ALL")
            firebaseMessaging!!.unsubscribeFromTopic("AOS")
            Log.d("푸시체크", "unsubscribeFromTopic: ")
        }
    }

    fun getall_push(): Boolean {
        return sharedPref!!.getBoolean("all_push", true)
    }

    fun startLocationUpdates() {

        mLocationRequest = LocationRequest.create().apply {
            interval = 1000
            fastestInterval = 4000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        //FusedLocationProviderClient의 인스턴스를 생성.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        // 기기의 위치에 관한 정기 업데이트를 요청하는 메서드 실행
        // 지정한 루퍼 스레드(Looper.myLooper())에서 콜백(mLocationCallback)으로 위치 업데이트를 요청
        mFusedLocationProviderClient!!.requestLocationUpdates(
            mLocationRequest!!,
            mLocationCallback,
            Looper.myLooper()
        )
    }
    fun CheckManager():Boolean{
        var type = userData!!.u_staff_yn
        if (type != null) {
            // Log.d("CHECKOB",ob!!)
            if (type.equals("Y")) {

                return true
            } else {
                return false
            }
        }
        return false

    }
    fun CheckCompany():Boolean{
        var type = userData!!.u_staff_yn
        if (type != null) {
            // Log.d("CHECKOB",ob!!)
            if (type.equals("C")) {

                return true
            } else {
                return false
            }
        }
        return false

    }

    // 시스템으로 부터 위치 정보를 콜백으로 받음
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            // 시스템에서 받은 location 정보를 onLocationChanged()에 전달
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation!!)
        }
    }

    // 시스템으로 부터 받은 위치정보를 화면에 갱신해주는 메소드
    fun onLocationChanged(location: Location) {
        mLastLocation = location
        Log.d("Location:latitude", "위도 : " + mLastLocation!!.latitude)// 갱신 된 위도
        Log.d("Location:longitude", "경도 : " + mLastLocation!!.longitude) // 갱신 된 경도

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

//                    result += URLEncoder.encode(i, "UTF-8")
//                    Log.d("EMOJISET1", i.toString())
//                    result +=

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

    fun setlocation(){
        if (mLastLocation != null ) {
            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter!!.create(UserService::class.java).setlocation(
                    userData?.u_seq.toString(),
                    mLastLocation!!.latitude.toString(),
                    mLastLocation!!.longitude.toString()
                )
                d!!.enqueue(object : retrofit2.Callback<ResultData> {
                    override fun onResponse(
                        call: retrofit2.Call<ResultData>,
                        response: Response<ResultData>
                    ) {
                        var data = response.body()
                        if (data != null) {
                            Log.d("setlocation1", mLastLocation!!.latitude.toString())
                            Log.d("setlocation2", mLastLocation!!.longitude.toString())

                        }

                    }

                    override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {

                    }

                })

            }
        }
    }

    private fun checkemoji(text: Char): Boolean {
        val type: Int = Character.getType(text)
        return (type == Character.SURROGATE.toInt() || type == Character.OTHER_SYMBOL.toInt())
    }

    fun decodeset(s: String?): String? {
        return StringEscapeUtils.unescapeJava(s)
    }
//    fun decodeset(text: String?): String? {
//        val myString: String? = null
//        return try {
//            URLDecoder
//                .decode(
//                    text, "UTF-8"
//                )
//        } catch (e: UnsupportedEncodingException) {
//            text
//        }
//    }

    fun setStringArrayPref(key: String, values: ArrayList<ProfileSuggestionData>) {
        val gson = Gson()
        val json = gson.toJson(values)
        val prefs = suggesstPref
        val editor = prefs!!.edit()
        editor.putString(key, json)
        editor.apply()
        sugeestlist = values
    }

    fun getStringArrayPrefPr(key: String): ArrayList<ProfileSuggestionData>? {
        val prefs = suggesstPref
        val json = prefs!!.getString(key, null)
        val gson = Gson()
        var storedData: ArrayList<ProfileSuggestionData>? = null
        if (json != null) {
            storedData = gson.fromJson(
                json,
                object : TypeToken<ArrayList<ProfileSuggestionData>>() {}.type
            )
        }
        return storedData
    }

    fun getStringArrayPref(key: String): ArrayList<String>? {
        val prefs = suggesstPref
        val json = prefs!!.getString(key, null)
        val gson = Gson()
        var storedData: ArrayList<String>? = arrayListOf()
        if (json != null) {
            storedData = gson.fromJson(
                json,
                object : TypeToken<ArrayList<String>>() {}.type
            )
        }
        return storedData
    }

    fun setStringArrayPrefs(key: String, values: ArrayList<String>) {
        val gson = Gson()
        val json = gson.toJson(values)
        val prefs = suggesstPref
        val editor = prefs!!.edit()
        editor.putString(key, json)
        editor.apply()
        select_index = values
    }

    fun setselectdata(seq: String) {
        if (!select_index!!.contains(seq)) {
            select_index!!.add(seq)
            setStringArrayPrefs("SelectSugesst", select_index!!)
        }

    }
    fun onDynamicLinkClick(context: Context,code:String) {

        var img= Uri.parse("https://supercarlounge.com/img/dynamic_img3.png")


        var text2="초대코드 : "+code
        val dynamicLink = Firebase.dynamicLinks.shortLinkAsync{
            link = Uri.parse("https://supercarnative.page.link/rniX/Invite?code=$code")
            domainUriPrefix = "https://supercarnative.page.link"
            // Open links with this app on Android
            androidParameters { }
            // Open links with com.example.ios on iOS
            iosParameters("com.supercarlounge.supercar") { }
            socialMetaTagParameters {
                title = "저랑 드라이브하러 갈까요?"
                description = " 지금 바로 슈라에서 만나요!"
                imageUrl=img
            }
        }
        dynamicLink.addOnCompleteListener{
            if(it.isSuccessful) {
                var shortLink = it.getResult().getShortLink();


                var sendIntent = Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, shortLink.toString());
                sendIntent.putExtra(Intent.EXTRA_TEXT, text2);
                sendIntent.setType("text/plain");




                context.startActivity(Intent.createChooser(sendIntent, "Share"));
            }

        }

    }
}

private fun FusedLocationProviderClient.requestLocationUpdates(
    mLocationRequest: LocationRequest,
    mLocationCallback: LocationCallback,
    myLooper: Looper?
) {


}
