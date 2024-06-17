package com.supercarlounge.supercar.ui.activity


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.graphics.Rect
import android.media.MediaPlayer.OnPreparedListener
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.transition.Slide
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.supercarlounge.supercar.*
import com.supercarlounge.supercar.Constans.Companion.PHONE_SMS
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.databinding.ActivityLoginBinding
import com.supercarlounge.supercar.dialog.ShareDialog
import com.supercarlounge.supercar.dialog.UpdateDialog
import com.supercarlounge.supercar.enumset.ShareDialogType
import com.supercarlounge.supercar.ui.priview.activity.PrvMainActivity
import com.supercarlounge.supercar.viewmodel.LoginViewModel
import com.yqritc.scalablevideoview.ScalableType
import com.yqritc.scalablevideoview.ScalableVideoView
import kotlinx.coroutines.flow.collectLatest
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.time.LocalDate
import java.time.Period
import java.util.*
import java.util.regex.Pattern
import kotlin.concurrent.timer


class LoginActivity : BaseActivitiy(), BottomDialogEvent {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private var bottomSheetDialog: com.supercarlounge.supercar.dialog.BottomSheetDialog? = null
    private lateinit var mvideoview: ScalableVideoView
    private lateinit var application: MainApplication
    private var shareDialog: ShareDialog? = null

    // 키해시 구하기 2
    private val HASH_TYPE = "SHA-256"
    val NUM_HASHED_BYTES = 9
    val NUM_BASE64_CHAR = 11
    var hashKey = ""
    var timer = Timer()
    var second = 0
    var minute = 0
    var timeCheck = 1
    var timeTick = 180
    var clickable = true
    var llagree = false
    var init = false
    private lateinit var updateUIReciver: BroadcastReceiver
    var smsReceiver: SMSReceiver? = null

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //requirePerms()
        with(window) {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            // set an slide transition
            enterTransition = Slide(Gravity.END)
            exitTransition = Slide(Gravity.START)
        }
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        application = applicationContext as MainApplication
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel

        if (!application!!.loginLog) {
            application.let {
                it?.joinLog(this, "로그인", "")
                it.save_loginLog()
            }

        }
        GetNowAppVersion()
        viewModel?.GetAppVersion(application)

        var hash = getAppSignatures(this)
        if (hash!!.size != 0)
            viewModel.hashKey.value = hash.get(0)

        viewModel?.isupdate?.observe(this, Observer { it ->
            if (it) {
                var d = UpdateDialog(this) { btn ->
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.addCategory(Intent.CATEGORY_DEFAULT)
                    intent.data = Uri.parse("market://details?id=com.supercarlounge.supercar")
                    startActivity(intent)
                }
                d.isCancelable = false;
                d.show(supportFragmentManager, "")
            }
        })
        viewModel.toast.observe(this, Observer {
            if (it.isNotEmpty()){
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })


//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        viewModel.reSend.value = getString(R.string.resend_text)
        clickListeners()
//        val video = Uri.parse("android.resource://" + packageName + "/" + com.example.supercarx.)
//        val path = Uri.parse("https://download.atmark-techno.com/sample/bbb/big-buck-bunny-30sec-800x480.mp4")

//        binding.vLogin.setMediaController(MediaController(this))
        binding.vLogin.requestFocus()
//        binding.vLogin.setMediaController(null);

        mvideoview = binding.vLogin

        try {
            mvideoview.setRawData(R.raw.login_bg)
        } catch (ioe: IOException) {
            //handle error
        }
        mvideoview.prepare(OnPreparedListener {
            mvideoview.start()
        })



        mvideoview.setScalableType(ScalableType.CENTER_CROP);
        mvideoview.invalidate();
        mvideoview.setOnCompletionListener {
            Log.d("로그인영상", "끝: ")
            binding.vLogin.start()
        }

        mvideoview.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                return true
            }
        })
        binding.llAgree.setOnClickListener {

            if (llagree) {
                bottomSheetDialog = com.supercarlounge.supercar.dialog.BottomSheetDialog(
                    this,
                    this,
                    0,
                    viewModel.allchecked.value!!,
                    viewModel.checked1.value!!,
                    viewModel.checked2.value!!,
                    viewModel.checked3.value!!,
                    viewModel.checked4.value!!,
                    viewModel.checked5.value!!
                )
                bottomSheetDialog!!.show(this.supportFragmentManager, "")
            } else {
                viewModel.ToastText.value = "먼저 핸드폰 번호를 입력해 주세요"
            }

        }

        flows()

        binding!!.llAgree.isEnabled = true
        llagree = false


//        // 리시버 등록
//        val intentFilter = IntentFilter()
//        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED")
//        this.registerReceiver(receiver, intentFilter)
        val filter = IntentFilter()
        filter.addAction("SuperCar")
        updateUIReciver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                //UI update here
                if (intent != null) {

                        var content = intent.getStringExtra("number").toString()
                        val intStr = content.replace("[^0-9]".toRegex(), "")
                        binding.edtCertificationNumber.setText(intStr)

                    Log.d("인증번호1", viewModel.certificationNumber1.value!!)

                }
            }
        }
        registerReceiver(updateUIReciver, filter)
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU && PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)){
            // 푸쉬 권한 없음
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 100)
        }
    }

    fun GetNowAppVersion() {
        var version = ""
        try {
            val pInfo = this.packageManager.getPackageInfo(this.packageName, 0)
            version = pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        viewModel?.nowversion?.value = version

    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val focusView = currentFocus
        if (focusView != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev.x.toInt()
            val y = ev.y.toInt()
            if (!rect.contains(x, y)) {
                val imm: InputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                if (imm != null) imm.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }


    private fun setTimer() {

        second = timeTick % 60
        minute = timeTick / 60
        timeCheck = 0
        if (timer != null) {
            timer!!.cancel()

        }
        timer = timer(period = 1000, initialDelay = 1000) {

            runOnUiThread {
                viewModel.reSendTime.value = String.format("0$minute : %02d", second)
            }
            if (second == 0 && minute == 0) {
                // 타이머 종료 로직
                cancel()

                runOnUiThread {

                    viewModel.reSendTime.value = String.format("00 : 00")

                    timeCheck = 1
                    binding.tvLoginBtn.setOnClickListener {


                        setTimer()
                        // requestTokenByPhone()
                    }
                }
                return@timer
            }
            if (second == 0) {
                minute--
                second = 60
            }
            second--
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun flows() {
        lifecycleScope.launchWhenStarted {
            viewModel.stateFlow.collectLatest {
                when (it) {

                    Constans.PHONE_PASS -> {
//                        binding!!.llAgree.isEnabled
                        if (viewModel.checked1.value == true && viewModel.checked2.value == true) {
                            viewModel.changeStateFlow(PHONE_SMS)
                            val i = Intent(this@LoginActivity, LoginPassActivity::class.java)
                            resultLauncherPass.launch(i)
                        } else {
                            viewModel.changeStateFlow(PHONE_SMS)
                            Toast.makeText(this@LoginActivity, "이용악관을 확인해주세요1", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    Constans.PHONE_LOGIN -> {
                        viewModel.viewType.value = true
//                        binding!!.llAgree.isEnabled = true
                        Log.d("llAgree", "onCreate: 5")
                        setTimer()

                    }
                    Constans.PHONE_ADMIN_LOGIN -> {
                        viewModel.getToken(
                            viewModel.number.value.toString(), application.push_token, application
                        )
                    }
                    Constans.PHONE_SUCCESS -> {

                            if (viewModel.checked1.value == false && viewModel.checked1.value == false) {
                                viewModel.ToastText.value = "이용악관을 확인해주세요"
//                    Toast.makeText(this@LoginActivity,"이용악관을 확인해주세요",Toast.LENGTH_SHORT).show()
                            } else if (minute == 0 && second == 0) {
//                            viewModel.changeStateFlow(PHONE_SMS)
                                viewModel.ToastText.value = "인증시간이 유효하지 않습니다"
//                    Toast.makeText(this@LoginActivity,"인증시간이 유효하지 않습니다",Toast.LENGTH_SHORT).show()
                            } else if (!viewModel.certificationNumber1.value.equals(viewModel.certificationNumber2.value)) {
                                viewModel.ToastText.value = "인증번호가 일치하지 않습니다"
//                    Toast.makeText(this@LoginActivity,"인증번호가 일치하지 않습니다",Toast.LENGTH_SHORT).show()
                            } else {


                                if (application.userData?.u_lock_screen.equals("Y")) {
                                    val i = Intent(this@LoginActivity, LockActivity::class.java)
                                    startActivity(i)
                                    this@LoginActivity.finish()
                                } else {
                                    val i = Intent(this@LoginActivity, MainActivity::class.java)
                                    i.putExtra("u_phone", application.userData?.u_phone)

                                    startActivity(i)
                                    this@LoginActivity.finish()
                                }


                            }



                    }
                    Constans.PHONE_ADMIN_SUCCESS->{
                        val i = Intent(this@LoginActivity, MainActivity::class.java)
                        i.putExtra("u_phone", application.userData?.u_phone)

                        startActivity(i)
                        this@LoginActivity.finish()
                    }
                    Constans.PHONE_WAITING -> {

                        val i = Intent(
                            this@LoginActivity,
                            SubscriptionApplicationCompleteActivity::class.java
                        )
                        i.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        i.putExtra("u_admin", "M")
                        i.putExtra("phoneNumber", viewModel.phoneNumber.value)

                        i.putExtra("u_seq", application.userData?.u_seq)

                        startActivity(i)

                    }
                    Constans.PHONE_COMPANION -> {

                        val i = Intent(
                            this@LoginActivity,
                            SubscriptionApplicationCompleteActivity::class.java
                        )
                        i.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        i.putExtra("u_admin", "N")
                        i.putExtra("phoneNumber", viewModel.phoneNumber.value)
                        i.putExtra("u_seq", application.userData?.u_seq.toString())
                        Log.d("확인2", "flows: ")
                        startActivity(i)


                    }
                    Constans.PHONE_NUMBER_CALL_FAIL -> {

                    }
                    Constans.PHONE_NUMBER_6 -> {
//                        viewModel.InsertInvitationCodeSave()
                    }
                    Constans.PHONE_POLICE -> {
                        Toast.makeText(this@LoginActivity, "계정이 정지되었습니다.", Toast.LENGTH_SHORT)
                            .show()
//                        viewModel.
                    }
                    Constans.PHONE_Q -> {
                        val i = Intent(this@LoginActivity, PrvMainActivity::class.java)
                        i.putExtra("view", "퀵유저")
                        i.putExtra("u_seq", application.userData?.u_seq.toString())
                        i.putExtra("u_phone", application.userData?.u_seq.toString())
//                        i.putExtra("u_admin",viewModel.u_admin.value.toString())

//                        i.putExtra()
//                        i.putExtra("u_gender",application.userData?.u_seq.toString())
                        startActivity(i)
                     //   overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                        this@LoginActivity.finish()
                    }
                    else -> {}
                }

            }


        }
    }


    fun checkedCheck() {
        if (viewModel.checked1.value == true && viewModel.checked2.value == true) {
//            changeStateFlow(Constans.TERMS_TURE)
        } else {
//            changeStateFlow(Constans.TERMS_FALSE)
        }
    }

    override fun onResume() {
        super.onResume()
        if (!binding.vLogin.isPlaying)
            binding.vLogin.start()
        setsmsreceiver()
        Smsset()
        application.setlocation()
    }

    override fun onPause() {
        super.onPause()
        removereceiver()
        //비디오 일시 정지
        if (binding.vLogin.isPlaying) binding.vLogin.pause()

    }


    override fun onDestroy() {
//        if (::receiver.isInitialized){
//            this.unregisterReceiver(receiver)
//        }
        super.onDestroy()
        binding.vLogin.stop()
    }

    override fun finish() {
        super.finish()
//        overridePendingTransition(
//            com.supercarlounge.supercar.R.anim.fadein,
//            com.supercarlounge.supercar.R.anim.fadeout
//        )
    }

    private fun setUrl(url: kotlin.String) {

    }

    @SuppressLint("DefaultLocale", "IntentReset")
    private fun clickListeners() {
        binding.tvLoginBtn.setOnClickListener {
//            if (clickable) {
            clickable = false
            isThrottleClick()
            if (viewModel.stateFlow.value == Constans.PHONE_SMS) {
                var chk = Pattern.matches("^[0-9]*$", viewModel.number.value.toString())


                if (viewModel.number.value?.length != 11) {
                    Toast.makeText(this@LoginActivity, "번호를 정확히 작성해주세요", Toast.LENGTH_SHORT)
                        .show()
//                    viewModel.changeStateFlow(Constans.PHONE_NUMBER_SUCCESS)
                } else if (viewModel.number.value.toString().isEmpty()) {
                    Toast.makeText(this@LoginActivity, "번호를 작성해주세요", Toast.LENGTH_SHORT).show()
                } else if (!chk) {
                    Toast.makeText(this@LoginActivity, "번호를 숫자로만 작성해주세요", Toast.LENGTH_SHORT)
                        .show()
                } else if (viewModel.checked1.value == false && viewModel.checked1.value == false) {
                    Toast.makeText(this@LoginActivity, "이용약관을 확인해주세요", Toast.LENGTH_SHORT)
                        .show()
//                    Toast.makeText(this@LoginActivity,"이용악관을 확인해주세요",Toast.LENGTH_SHORT).show()
                } else {
                    if (viewModel.number.value!!.startsWith("010") || viewModel.number.value!!.startsWith("011")|| viewModel.number.value!!.startsWith("090")) {
                        llagree = true
                        binding!!.llAgree.isEnabled = true
                        if(viewModel.checked1.value == true && viewModel.checked2.value == true){
                            viewModel.getSMS(viewModel.number.value.toString(), application.push_token, application,"SMS")
                        }
                        Log.d("llAgree", "onCreate: 4")
                    }else{
                        Toast.makeText(
                            this@LoginActivity,
                            "010 또는 011로 시작하는 번호만 작성해주세요",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
//                if(viewModel.stateFlow.value )
            } else if (viewModel.stateFlow.value == Constans.PHONE_LOGIN) {
                if (timeCheck == 0) {
                    viewModel.getToken(
                        viewModel.number.value.toString(), application.push_token, application
                    )
                }
            } else if (viewModel.stateFlow.value == Constans.PHONE_SUCCESS) {
                if (viewModel.checked1.value == false && viewModel.checked1.value == false) {
                    viewModel.ToastText.value = "이용악관을 확인해주세요"
//                    Toast.makeText(this@LoginActivity,"이용악관을 확인해주세요",Toast.LENGTH_SHORT).show()
                } else if (minute == 0 && second == 0) {
//                            viewModel.changeStateFlow(PHONE_SMS)
                    viewModel.ToastText.value = "인증시간이 유효하지 않습니다"
//                    Toast.makeText(this@LoginActivity,"인증시간이 유효하지 않습니다",Toast.LENGTH_SHORT).show()
                } else if (!viewModel.certificationNumber1.value.equals(viewModel.certificationNumber2.value)) {
                    viewModel.ToastText.value = "인증번호가 일치하지 않습니다"
//                    Toast.makeText(this@LoginActivity,"인증번호가 일치하지 않습니다",Toast.LENGTH_SHORT).show()
                } else {

                    val i = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(i)
                    this@LoginActivity.finish()
                }
            } else if (viewModel.stateFlow.value == Constans.PHONE_Q) {
                val i = Intent(this@LoginActivity, PrvMainActivity::class.java)
                i.putExtra("view", "퀵유저")
                i.putExtra("u_seq", application.userData?.u_seq.toString())
                i.putExtra("u_phone", application.userData?.u_seq.toString())
//                        i.putExtra("u_admin",viewModel.u_admin.value.toString())

//                        i.putExtra()
//                        i.putExtra("u_gender",application.userData?.u_seq.toString())
                startActivity(i)
              //  overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                this@LoginActivity.finish()

            }
         //   overridePendingTransition(R.anim.fadein, R.anim.fadeout)
//                this.finish()

//            }


        }


        binding.tvResend.setOnClickListener {
            if (viewModel.number.value.toString() == "01189070101"||viewModel.number.value.toString() == "01092615611"||viewModel.number.value.toString().startsWith("090")){
                viewModel.checkLogin(
                    viewModel.number.value.toString(),
                    application.push_token,
                    application
                )
            }else {
                viewModel.getSMS(
                    viewModel.number.value.toString(),
                    application.push_token,
                    application,
                    "RESMS"
                )
            }
        }

        binding.ivShare.setOnClickListener {
            shareDialog = ShareDialog(this, ShareDialogType.SMS)
            { type, check ->

            }
            shareDialog!!.show(this.supportFragmentManager, "")
        }

        binding.tvInquiry.setOnClickListener {
            val eMailId1 = "help@supercarlounge.com"
            var eMailIds: Array<kotlin.String> = arrayOf(eMailId1)
            var buildName = BuildConfig.VERSION_NAME
            var buildDevice = getDeviceName()
            var buildVersion = Build.VERSION.SDK_INT
            var buildRelease = Build.VERSION.RELEASE

            var name = ""
            var contents = ""
            var phone = ""
            val email = Intent(Intent.ACTION_SEND)
            email.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            email.setPackage("com.google.android.gm");
            email.putExtra(Intent.EXTRA_SUBJECT, title)
            email.data = Uri.parse("mailto:")
            email.putExtra(android.content.Intent.EXTRA_EMAIL, eMailIds);
            email.putExtra(
                Intent.EXTRA_TEXT,
                "안녕하세요 슈퍼카라운지입니다.\n" +
                        "성함과 연락처를\n" +
                        "문의하실 내용과 함께 남겨주시면\n" +
                        "확인 후 연락드리겠습니다.\n\nApp Version : $buildName \nDevice : $buildDevice \nAndroid(SDK) :  $buildVersion  android os :$buildRelease \n" +
                        "사용자 명 : $name \n연락처 : $phone \n내용 : $contents  "
            );
            email.type = "message/rfc822";
            this.startActivity(email);
        }

        binding.tvInquiry.setOnClickListener {
            val eMailId1 = "help@supercarlounge.com"
            var eMailIds: Array<kotlin.String> = arrayOf(eMailId1)
            var buildName = BuildConfig.VERSION_NAME
            var buildDevice = getDeviceName()
            var buildVersion = Build.VERSION.SDK_INT
            var buildRelease = Build.VERSION.RELEASE

            var name = ""
            var contents = ""
            var phone = ""
            val email = Intent(Intent.ACTION_SEND)
            email.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            email.setPackage("com.google.android.gm");
            email.putExtra(Intent.EXTRA_SUBJECT, title)
            email.data = Uri.parse("mailto:")
            email.putExtra(android.content.Intent.EXTRA_EMAIL, eMailIds);
            email.putExtra(
                Intent.EXTRA_TEXT,
                "안녕하세요 슈퍼카라운지입니다.\n" +
                        "성함과 연락처를\n" +
                        "문의하실 내용과 함께 남겨주시면\n" +
                        "확인 후 연락드리겠습니다.\n\nApp Version : $buildName \nDevice : $buildDevice \nAndroid(SDK) :  $buildVersion  android os :$buildRelease \n" +
                        "사용자 명 : $name \n연락처 : $phone \n내용 : $contents  "
            );
            email.type = "message/rfc822";
            this.startActivity(email);
        }

        binding.ivInstagram.setOnClickListener {
            val url = getString(R.string.supercar_lounge_rul1)
            val packageManager: PackageManager = packageManager
            if (isAppInstalled(getString(R.string.chrome_package), packageManager)) {

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            } else {
                if (isAppInstalled(getString(R.string.internet_package), packageManager)) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "크롬이나 삼성 인터넷이 설치되어 있지 않습니다", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.ivYoutube.setOnClickListener {

            val url = getString(R.string.supercar_lounge_rul2)
            val packageManager: PackageManager = packageManager
            if (isAppInstalled(getString(R.string.chrome_package), packageManager)) {

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            } else {
                if (isAppInstalled(getString(R.string.internet_package), packageManager)) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "크롬이나 삼성 인터넷이 설치되어 있지 않습니다", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.ivHome.setOnClickListener {
            val url = getString(R.string.supercar_lounge_rul3)
            val packageManager: PackageManager = packageManager
            if (isAppInstalled(getString(R.string.chrome_package), packageManager)) {

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            } else {
                if (isAppInstalled(getString(R.string.internet_package), packageManager)) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "크롬이나 삼성 인터넷이 설치되어 있지 않습니다", Toast.LENGTH_SHORT).show()
                }
            }
        }




        binding!!.edtPhoneNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {
                viewModel.number.value = p0.toString()
                if (viewModel.stateFlow.value == 0) {
                    var chk = Pattern.matches("^[0-9]*$", viewModel.number.value.toString())

                    if (viewModel.number.value?.length != 11) {

                    } else if (viewModel.number.value.toString().isEmpty()) {

                    } else if (!chk) {

                    } else {
//                        binding.llAgree.isEnabled = true

                        if (viewModel.number.value!!.startsWith("010") || viewModel.number.value!!.startsWith("011")|| viewModel.number.value!!.startsWith("090")) {
                            llagree = true
                            binding!!.llAgree.isEnabled = true
                        }
                        Log.d("llAgree", "onCreate: 3")
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding!!.edtCertificationNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {
                viewModel.certificationNumber1.value = p0.toString()
                Log.d("인증번호", viewModel.certificationNumber1.value!!)
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })


    }

    @RequiresApi(Build.VERSION_CODES.O)
    var resultLauncherPass =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                Log.d("resultLauncherPass data:", ": $data")
                if (data != null) {
                    viewModel.u_name.value = data.getStringExtra("u_name")
                    viewModel.u_gender.value = data.getStringExtra("u_gender")
                    viewModel.u_birthday.value = data.getStringExtra("u_birthday")
                    viewModel.u_phone.value = data.getStringExtra("u_phone")
                    Log.d("회원가입 u_birthday", viewModel.u_birthday.value.toString())
                    var passphone = viewModel.u_phone.value!!
                    var inputphone = binding!!.edtPhoneNumber.text.toString()

                    if (viewModel.u_name.value?.isEmpty() == true && viewModel.u_gender.value?.isEmpty() == true && viewModel.u_birthday.value?.isEmpty() == true && viewModel.u_phone.value?.isEmpty() == true) {
                        Toast.makeText(this, "패스인증에 실패하였습니다. 다시한번 시도해주세요.", Toast.LENGTH_SHORT).show()

                    }else if (!inputphone.equals(passphone)){
                        Toast.makeText(this, "입력하신 휴대폰 번호와 인증받은 휴대폰 번호가 다릅니다.", Toast.LENGTH_SHORT).show()
                    }  else{
                        if (ageLimit(viewModel.u_birthday.value.toString())) {
                            var u_loca = if (viewModel.checked3.value == true) "Y" else "N"
                            var u_mt = if (viewModel.checked4.value == true) "Y" else "N"
                            var u_mkt = if (viewModel.checked5.value == true) "Y" else "N"
                            application.userData?.u_loca = u_loca
                            application.userData?.u_mt = u_mt
                            application.userData?.u_mkt = u_mkt
                            Log.d("선택 약관 ", u_loca.toString() + u_mt.toString() + u_mkt.toString())
                            val i = Intent(this@LoginActivity, StartActivity::class.java)
                            i.putExtra("u_name", viewModel.u_name.value)
                            i.putExtra("u_gender", viewModel.u_gender.value)
                            i.putExtra("u_birthday", viewModel.u_birthday.value)
                            i.putExtra("u_phone", viewModel.u_phone.value)

                            startActivity(i)

                            Log.d(
                                "resultLauncherPass",
                                "u_name =${viewModel.u_name.value} ,u_gender =${viewModel.u_gender.value} ,u_birthday =${viewModel.u_birthday.value} ,u_phone =${viewModel.u_phone.value} ,type =${viewModel.type.value},"
                            )

                        } else {
                            Toast.makeText(this, "만 20세 이상 생일이 지난 후 가입하실 수 있습니다.", Toast.LENGTH_SHORT).show()

                        }
                    }


                }

            }
        }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun ageLimit(u_birthday: String): Boolean {
        var ar = u_birthday.split("-");
        var count = 0
        var year = ""
        var month = ""
        var dayOfMonth = ""
        for (i in ar.indices) {

            when (count) {
                0 -> {
                    year = ar[i].toString()

                }
                1 -> {
                    month = ar[i].toString()

                }
                2 -> {
                    dayOfMonth = ar[i].toString()

                }
                else -> {}
            }
            count++
        }
        val birthDate = LocalDate.of(year.toInt(), month.toInt(), dayOfMonth.toInt())
        val today = LocalDate.now()
        val age = Period.between(birthDate, today).years
        Log.d("나이", age.toString())
        if (age >= 20) {
            Log.d("나이 성인입니다", age.toString())
            return true
        } else {
            return false
            Log.d("나이 학생입니다", age.toString())
        }


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

    override fun okEvent(
        allchecked: Boolean,
        check1: Boolean,
        check2: Boolean,
        check3: Boolean,
        check4: Boolean,
        check5: Boolean
    ) {
        viewModel.allchecked.value = allchecked
        viewModel.checked1.value = check1
        viewModel.checked2.value = check2
        viewModel.checked3.value = check3
        viewModel.checked4.value = check4
        viewModel.checked5.value = check5

        if (viewModel.checked1.value == true && viewModel.checked2.value == true) {


            if (viewModel.number.value.toString() == "01189070101"||viewModel.number.value.toString() == "01092615611"||viewModel.number.value.toString().startsWith("090")){
                            viewModel.checkLogin(
                viewModel.number.value.toString(),
                application.push_token,
                application
            )
            }else{
                viewModel.getSMS(viewModel.number.value.toString(), application.push_token, application,"SMS")
            }


            llagree = true
            binding!!.llAgree.isEnabled = false
            Log.d("llAgree", "onCreate: 2")
        }


    }

    fun isAppInstalled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (ex: PackageManager.NameNotFoundException) {
            false
        }
    }

    override fun edtListener() {

    }

    fun isThrottleClick(): Boolean {
        if (clickable) {
            clickable = false
            Handler(Looper.getMainLooper()).postDelayed({
                clickable = true
            }, 300L)
            return true
        } else {
            Log.i("TAG", "waiting for a while")
            return false
        }
    }

    fun getAppSignatures(context: Context): ArrayList<String>? {
        val appCodes: ArrayList<String> = ArrayList()
        try {
            // Get all package signatures for the current package
            val packageName = context.packageName
            val packageManager = context.packageManager
            val signatures: Array<Signature> = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES
            ).signatures

            // For each signature create a compatible hash
            for (signature in signatures) {
                val hash = getHash(packageName, signature.toCharsString())
                if (hash != null) {
                    appCodes.add(String.format("%s", hash))
                }

            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.d("해쉬키 ", "Unable to find package to obtain hash. : $e")
        }
        return appCodes
    }


    private fun getHash(packageName: String, signature: String): String? {
        val appInfo = "$packageName $signature"
        try {
            val messageDigest = MessageDigest.getInstance(HASH_TYPE)
            // minSdkVersion이 19이상이면 체크 안해도 됨
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                messageDigest.update(appInfo.toByteArray(StandardCharsets.UTF_8))
            }
            var hashSignature = messageDigest.digest()

            // truncated into NUM_HASHED_BYTES
            hashSignature = Arrays.copyOfRange(hashSignature, 0, NUM_HASHED_BYTES)
            // encode into Base64
            var base64Hash =
                Base64.encodeToString(hashSignature, Base64.NO_PADDING or Base64.NO_WRAP)
            base64Hash = base64Hash.substring(0, NUM_BASE64_CHAR)
            Log.d("해쉬키", String.format("\nPackage : %s\nHash : %s", packageName, base64Hash))
            viewModel.hashKey.value = base64Hash
            return base64Hash
        } catch (e: NoSuchAlgorithmException) {
            Log.d("해쉬키", "hash:NoSuchAlgorithm : $e")
        }
        return null
    }

    fun setsmsreceiver() {
        if (smsReceiver == null) {
            smsReceiver = SMSReceiver()
        }
        val intentFilter =
            IntentFilter() // SMS_RETRIEVED_ACTION 필수입니다.
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(smsReceiver, intentFilter)
    }

    fun removereceiver() {
        unregisterReceiver(smsReceiver)
    }

    fun Smsset() {

        var sretriever = SmsRetriever.getClient(this)
        var task = sretriever.startSmsRetriever()
        task.addOnSuccessListener {
            Log.d("RETRIVER_SUCCESS", "SUCCESS")
        }

        task.addOnFailureListener {
            Log.d("RETRIVER_ERROR", it.message.toString())
        }
    }
}
