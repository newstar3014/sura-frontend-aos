package com.supercarlounge.supercar.ui.activity


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.util.Log
import android.view.Gravity
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.databinding.ActivityStartBinding
import com.supercarlounge.supercar.ui.priview.activity.PrvMainActivity
import com.supercarlounge.supercar.viewmodel.StartViewModel




class StartActivity : BaseActivitiy() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityStartBinding
    private lateinit var viewModel: StartViewModel
    private var application:MainApplication?= null
    var u_loca = ""
    var u_mt = ""
    var u_mkt = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        with(window) {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            // set an slide transition
            enterTransition = Slide(Gravity.END)
            exitTransition = Slide(Gravity.START)
        }
        viewModel = ViewModelProvider(this).get(StartViewModel::class.java)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        application = applicationContext as MainApplication

        binding!!.viewModel = viewModel
        binding!!.setLifecycleOwner(this)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        viewModel.reSend.value = getString(R.string.resend_text)
        clickListeners()
        viewModel.u_name.value = intent.getStringExtra("u_name").toString()
        viewModel.u_gender.value = intent.getStringExtra("u_gender").toString()
        viewModel.u_birthday.value = intent.getStringExtra("u_birthday").toString()
        viewModel.u_phone.value = intent.getStringExtra("u_phone").toString()
        u_loca = intent.getStringExtra("u_loca").toString()
        u_mt = intent.getStringExtra("u_mt").toString()
        u_mkt = intent.getStringExtra("u_mkt").toString()
        application?.joinLog(this,"회원가입,미리보기 선택", viewModel.u_name.value.toString())

//        passLoginThirdPartyHandler = object:PassLoginThirdPartyHandler(){
//            override fun run(code: Int, isSuccess: Boolean, reponse: BaseResponse?) {
//                Log.d("리턴", "$code  : $isSuccess : $ $reponse")
//            }
//
//        }
//
//        var url ="https://id.passlogin.com"
//        var teleType ="S"
//
//        var mConnection = PassLoginThirdPartyConnection.getInstance(this);
//        mConnection.init(this, mClientId, mClientSecret, mAppName, mCallbackUrl);
//        var mPreference = PassLoginThirdPartyPreference(this);
//        if(mConnection.getState() == PassLoginThirdPartyState.NEED_INIT) {
//            mConnection.requestUserInfo();
//            mConnection.requestLoginId();
//        } else if(mConnection.getState() == PassLoginThirdPartyState.NEED_LOGIN) {
//
//        } else if(mConnection.getState() == PassLoginThirdPartyState.OK) {
//
//        }
//
//
//        mConnection.setPassLoginHandler(passLoginThirdPartyHandler)
//        mPreference.url = url;
//        mConnection.requestLogin(this)
//
//        mPreference.teleType = teleType;
//        mConnection.accessToken
//
////        Log.d("토큰", mConnection.accessToken)
//        Log.d("토큰", mConnection.version)

        viewModel.QuickInsertApiCall.observe(this, Observer {
            if (it){
                val i = Intent(this@StartActivity, PrvMainActivity::class.java)
                i.putExtra("u_name", viewModel.u_name.value)
                i.putExtra("u_gender", viewModel.u_gender.value)
                i.putExtra("u_birthday",viewModel.u_birthday.value)
                i.putExtra("u_phone", viewModel.u_phone.value)
                Log.d("폰11", viewModel.u_phone.value!!)
                i.putExtra("u_loca,",u_loca)
                i.putExtra("u_mt,",u_mt)
                i.putExtra("u_mkt,",u_mkt)
                i.putExtra("view", viewModel.view.value)
                startActivity(i)
            }
        })
    }

    private fun setUrl(url :kotlin.String){

    }



    @SuppressLint("DefaultLocale", "IntentReset")
    private fun clickListeners(){
        binding.ll1.setOnClickListener {
            val i = Intent(this@StartActivity, JoinActivity::class.java)
            i.putExtra("u_name", viewModel.u_name.value)
            i.putExtra("u_gender", viewModel.u_gender.value)
            i.putExtra("u_birthday", viewModel.u_birthday.value)
            i.putExtra("u_phone", viewModel.u_phone.value)
            i.putExtra("u_loca,",u_loca)
            i.putExtra("u_mt,",u_mt)
            i.putExtra("u_mkt,",u_mkt)
            Log.d("아이템3", viewModel.u_gender.value.toString())
            startActivity(i)
        }

        binding.ll2.setOnClickListener {
            var userData = application?.userData

            viewModel.setQuickInsert(application!!.push_token,userData!!.u_loca!!,userData!!.u_mt!!,userData!!.u_mkt!!)

//            val i = Intent(this@StartActivity, PrvMainActivity::class.java)
//            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            i.putExtra("u_name", viewModel.u_name.value)
//            i.putExtra("u_gender", viewModel.u_gender.value)
//            i.putExtra("u_birthday", viewModel.u_birthday.value)
//            i.putExtra("u_phone", viewModel.u_phone.value)
//            i.putExtra("view", viewModel.view.value)
//            i.putExtra("u_loca,",u_loca)
//            i.putExtra("u_mt,",u_mt)
//            i.putExtra("u_mkt,",u_mkt)
//            startActivity(i)


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

    override fun onPause() {
        super.onPause()
       // overridePendingTransition(R.anim. fadein, R.anim.fadeout)
    }
    override fun finish() {
        super.finish()
       // overridePendingTransition(com.supercarlounge.supercar.R.anim. fadein, com.supercarlounge.supercar.R.anim.fadeout)
    }
}
