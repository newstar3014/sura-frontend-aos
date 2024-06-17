package com.supercarlounge.supercar


import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.PowerManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.android.datatransport.runtime.firebase.transport.LogEventDropped
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.supercarlounge.supercar.Network.MatchingService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.data.ResultAlimYNData
import com.supercarlounge.supercar.data.ResultData
import com.supercarlounge.supercar.ui.activity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.*
import java.util.concurrent.atomic.AtomicInteger


class FcmMessageService : FirebaseMessagingService() {
    var tagset = "FCMSERVICE"
    var notificationManager: NotificationManager? = null
    private var isAppInForeground = false
    private var application:MainApplication? =null
    private val c: AtomicInteger = AtomicInteger(0)

    fun getID(): Int {
        return c.incrementAndGet()
    }



    override fun onCreate() {
        super.onCreate()
        application = applicationContext as MainApplication
    }
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d("푸시", "FCM1: ")
        remoteMessage.data?.let {
            Log.d("푸시", "FCM2: ")
            setpush(it)


        }
    }


    fun setpush(it: MutableMap<String, String>) {
        var title = ""
        var body = ""
        var screen= ""
        var channel_id = it["channel_id"].toString()

        title = it["title"].toString()
        body = it["body"].toString()
        screen = it["screen"].toString()
//         = it["body"].toString()

        var pushdata = it["pushData"]
        if (!pushdata.isNullOrEmpty()) {

            val obj: JsonObject = JsonParser.parseString(pushdata).getAsJsonObject()
            Log.d("테스트FCM1", it.toString())
            Log.d("테스트FCM1", channel_id + ":" + title + ":" + body + ":")

            val intent = Intent()

            Log.d("테스트FCM1", it.toString())
            intent.action = "com.package.notification"
            intent.putExtra("title", title)
            intent.putExtra("body", body)
            intent.putExtra("channel_id", channel_id)


            var b_seq = ""
            var send_seq = ""
            var uid = ""

            if (screen.isNotEmpty()) {
                Log.d("푸시테스트1", screen.toString())

                Log.d("푸시테스트2", obj.toString())
                when(screen){
                    "ApplyProfile","SendProfile" ,"ReadProfile","SendLike","GivenLike","AcceptLike","SendOpenPhone","OpenPhone" ->{
                        val obj: JsonObject = JsonParser.parseString(pushdata).asJsonObject
                        send_seq = obj.get("send_seq").toString().replace("\"", "")

                        intent.putExtra("send_seq", send_seq)
                        intent.putExtra("screen", screen)


                    }
                    "BoardComment","BoardSubComment","BoardLike","DriveLike" ->{
                        val obj: JsonObject = JsonParser.parseString(pushdata).asJsonObject
                        b_seq = obj.get("b_seq").toString().replace("\"", "")
                        send_seq = obj.get("send_seq").toString().replace("\"", "")
                        uid = obj.get("uid").toString().replace("\"", "")
                        var application: MainApplication
                        application = applicationContext as MainApplication
                        application.test = b_seq
                        intent.putExtra("screen", screen)
                        intent.putExtra("b_seq", b_seq)
                        intent.putExtra("send_seq", send_seq)
                        intent.putExtra("u_seq", send_seq.toInt())
                        intent.putExtra("uid", uid)
                        intent.putExtra("type", 1)
                        intent.putExtra("push", false)
                    }
                    "SendProfile","SendDrive"->{
                        b_seq = obj.get("b_seq").toString().replace("\"", "")
                        send_seq = obj.get("send_seq").toString().replace("\"", "")
                        intent.putExtra("screen", screen)
                        intent.putExtra("mmi_seq", b_seq)
                        intent.putExtra("send_seq", send_seq)
                        intent.putExtra("u_seq", send_seq.toInt())
                    }
                    "MycarComment","MycarSubComment","MycarLike" ->{
                        b_seq = obj.get("b_seq").toString().replace("\"", "")
                        send_seq = obj.get("send_seq").toString().replace("\"", "")
                        intent.putExtra("screen", screen)
                        intent.putExtra("mmi_seq", b_seq.toInt())
                        intent.putExtra("send_seq", send_seq)
                        intent.putExtra("u_seq", send_seq.toInt())
                        Log.d("b_seq푸시4", b_seq.toString())
                    }
                    "LocationDrive" ->{
                        val obj: JsonObject = JsonParser.parseString(pushdata).asJsonObject
                        b_seq = obj.get("b_seq").toString().replace("\"", "")
                        send_seq = obj.get("send_seq").toString().replace("\"", "")
                        uid = obj.get("uid").toString().replace("\"", "")
                        var application: MainApplication
                        application = applicationContext as MainApplication
                        application.test = b_seq
                        intent.putExtra("screen", screen)
                        intent.putExtra("b_seq", b_seq)
                        intent.putExtra("send_seq", send_seq)
                        intent.putExtra("u_seq", send_seq.toInt())
                        intent.putExtra("uid", uid)
                        intent.putExtra("type", 1)
                        intent.putExtra("push", false)
                        Log.d("이승주푸시 seq", b_seq)
                    }
                    "MycarRankingHeart" ->{
                        intent.putExtra("screen", screen)
                    }
                    "DrivePass30Buy","DrivePass1Buy","HeartCharge" ->{
                        intent.putExtra("screen", screen)
                    }
                    else -> {}
                }

            }


            if (applicationInForeground()) {

//                sendBroadcast(intent)
                SendForegroundNotification(title, body, channel_id, screen, b_seq, send_seq, uid,application!!)
                Log.d(tagset, "FORGROUND")
            } else {
                SendNotification(title, body, channel_id, screen, b_seq, send_seq, uid)
                Log.d(tagset, "BACKGROUND")
            }


        }


    }

    private fun applicationInForeground(): Boolean {
        val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val services = activityManager.runningAppProcesses
        var isActivityFound = false
        if (services[0].processName
                .equals(
                    packageName,
                    ignoreCase = true
                ) && services[0].importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND
        ) {
            isActivityFound = true
        }
        return isActivityFound
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("token", token)
    }

    override fun onSendError(p0: String, p1: Exception) {
        super.onSendError(p0, p1)
        Log.d("send_erorr", p0)
    }

    fun SendNotification(title: String, text: String, channel_id: String, screen: String, b_seq:String,send_seq:String,uid:String) {


        var notificationIntent: Intent? = null
        notificationIntent = Intent(this, IntroActivity::class.java); //전달할 값
        Log.d("테스트FCM2", b_seq)
//        if (b_seq.isNotEmpty()) {

//        }else{
//        }


        if (notificationManager == null) {
            notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }

//
        notificationIntent!!.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        notificationIntent.putExtra("screen", screen)
        notificationIntent.putExtra("b_seq", b_seq)
        notificationIntent.putExtra("sub_seq", send_seq)
        notificationIntent.putExtra("u_seq", send_seq.toInt())
        notificationIntent.putExtra("uid", uid)

        var pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);


        var builder = NotificationCompat.Builder(this, channel_id)
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

        var notification = builder.build()
        notification.flags = Notification.FLAG_AUTO_CANCEL
        notificationManager!!.notify(cid, notification); // 고유숫자로 노티피케이션 동작시킴
        val pm = getSystemService(POWER_SERVICE) as PowerManager

        @SuppressLint("InvalidWakeLockTag")
        val wakeLock = pm.newWakeLock(
            PowerManager.SCREEN_DIM_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            tagset
        )
        wakeLock.acquire(3000)
    }
    fun SendForegroundNotification(title: String, text: String, channel_id: String, screen: String, b_seq:String,send_seq:String,uid:String,application:MainApplication) {


        var notificationIntent: Intent? = null



        Log.d("테스트FCM2", b_seq)
//        if (b_seq.isNotEmpty()) {

//        }else{
//        }


        if (notificationManager == null) {
            notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }

//
        if (notificationManager == null) {
            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }



//        notificationIntent!!.setAction(Intent.ACTION_MAIN)
//        notificationIntent!!.addCategory(Intent.CATEGORY_LAUNCHER)
//        notificationIntent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        when(screen){
            "ApplyProfile","SendProfile" ,"ReadProfile","SendLike","GivenLike","AcceptLike","SendOpenPhone","OpenPhone" ->{
                notificationIntent = Intent(this, ProfileActivity::class.java);
                notificationIntent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                notificationIntent.putExtra("screen", screen)
                notificationIntent.putExtra("sub_seq", send_seq)

            }
            "BoardComment","BoardSubComment","BoardLike","DriveLike" ->{
                notificationIntent = Intent(this, PostActivity::class.java);
                notificationIntent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                notificationIntent.putExtra("screen", screen)
                notificationIntent.putExtra("u_seq", send_seq.toInt())
                notificationIntent.putExtra("sub_seq", send_seq)
                notificationIntent.putExtra("b_seq", b_seq)
                notificationIntent.putExtra("uid", uid)

            }
            "SendProfile","SendDrive"->{
                getMatchingDataYN(application.userData?.u_seq.toString(),send_seq,this,channel_id,title,text)

            }
            "MycarComment","MycarSubComment","MycarLike" ->{
                var b_seq = b_seq.toInt()
                notificationIntent = Intent(this, MyCarBoardActivity::class.java)
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                notificationIntent.putExtra("screen", screen)
                notificationIntent.putExtra("mmi_seq", b_seq.toInt())
                notificationIntent.putExtra("send_seq", send_seq)
                notificationIntent.putExtra("u_seq", send_seq.toInt())

                Log.d("b_seq푸시1", b_seq.toString())
                var d = notificationIntent.getIntExtra("mmi_seq",0)
                Log.d("b_seq푸시2", d.toString())
            }
            "LocationDrive" ->{
                notificationIntent = Intent(this, LocationSpotDetailActivity::class.java)
                notificationIntent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                notificationIntent.putExtra("screen", screen)
                notificationIntent.putExtra("spot_seq", b_seq)
            }
            "MycarRankingHeart" ->{
                notificationIntent = Intent(this, MyCarBestBoardActivity::class.java)
                notificationIntent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            "DrivePass1Date" ,"DrivePass3Date" ->{
                notificationIntent = Intent(this, HeartsShopActivity::class.java)
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            "DrivePass30Buy", "DrivePass1Buy", "suramaster",->{
                notificationIntent = Intent()
            }
            else -> {notificationIntent = Intent()}
        }
        when(screen){
            "SendProfile","SendDrive"->{

            }
            else ->{
                var pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);


                var builder = NotificationCompat.Builder(this, channel_id)
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

                var notification = builder.build()
                notification.flags = Notification.FLAG_AUTO_CANCEL
                notificationManager!!.notify(cid, notification); // 고유숫자로 노티피케이션 동작시킴
                val pm = getSystemService(POWER_SERVICE) as PowerManager

                @SuppressLint("InvalidWakeLockTag")
                val wakeLock = pm.newWakeLock(
                    PowerManager.SCREEN_DIM_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
                    tagset
                )
                wakeLock.acquire(3000)
            }
        }




    }
    fun getMatchingDataYN(my_seq:String ,send_seq: String,context:Context,channel_id: String,title: String,text: String){
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(MatchingService::class.java).getMatchingDataYN(my_seq,send_seq)
            d!!.enqueue(object : retrofit2.Callback<ResultAlimYNData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultAlimYNData>,
                    response: Response<ResultAlimYNData>
                ) {
                    var data = response.body()
                    var message = data?.message
                    var pcDateCheck = data?.rows
                    if (data != null) {
                        Log.d("알림 테스트", data.toString())
                        if (message.equals("조회 되었습니다")){
                            if (pcDateCheck.equals("true")){
                                var notificationIntent = Intent(context, ProfileActivity::class.java);
                                notificationIntent.putExtra("sub_seq", send_seq)

                                var pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);


                                var builder = NotificationCompat.Builder(context, channel_id)
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

                                var notification = builder.build()
                                notification.flags = Notification.FLAG_AUTO_CANCEL
                                notificationManager!!.notify(cid, notification); // 고유숫자로 노티피케이션 동작시킴
                                val pm = getSystemService(POWER_SERVICE) as PowerManager

                                @SuppressLint("InvalidWakeLockTag")
                                val wakeLock = pm.newWakeLock(
                                    PowerManager.SCREEN_DIM_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
                                    tagset
                                )
                                wakeLock.acquire(3000)
                            }else{
                                Toast.makeText(context, "승인대기 중입니다.", Toast.LENGTH_SHORT).show()
                            }


                        }

                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultAlimYNData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                }

            })

        }
    }
    fun isAppRunning(packageName: String): Boolean {
        val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val procInfos = activityManager.runningAppProcesses
        if (procInfos != null) {
            for (processInfo in procInfos) {
                if (processInfo.processName == packageName) {
                    return true
                }
            }
        }
        return false
    }
    fun DrivePassCheck(context:Context, u_seq:String,screen: String,send_seq: String,b_seq:String,uid: String) {
        var notificationIntent: Intent? = null
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(MatchingService::class.java).DrivePassCheck(
                u_seq.toString(),
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

                            notificationIntent = Intent(context, RecentDriveListActivity::class.java);


                        }else{

                            notificationIntent = Intent(context, HeartsShopActivity::class.java);

                        }
                        notificationIntent?.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        notificationIntent?.putExtra("screen", screen)
                        notificationIntent?.putExtra("sub_seq", send_seq)
                        notificationIntent?.putExtra("b_seq", b_seq)
                        notificationIntent?.putExtra("uid", uid)
                        notificationIntent?.putExtra("type", 1)
                        notificationIntent?.putExtra("push", false)
                        startActivity(notificationIntent)
//                        notificationIntent?.let { pushIntent(it, channel_id, title, text) }


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
}