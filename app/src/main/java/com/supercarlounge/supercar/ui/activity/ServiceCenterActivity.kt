package com.supercarlounge.supercar.ui.activity

import SecessionDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import com.supercarlounge.supercar.databinding.ActivityServuceCenterBinding
import com.supercarlounge.supercar.viewmodel.ServiceCenterViewModel
import com.supercarlounge.supercar.BuildConfig
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.customview.BaseActivitiy

class ServiceCenterActivity: BaseActivitiy() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityServuceCenterBinding
    private lateinit var viewModel: ServiceCenterViewModel
    private  var secessionDialog: SecessionDialog? = null
    private var fm : FragmentManager? =null
    private var application :MainApplication? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(ServiceCenterViewModel::class.java)
        binding = ActivityServuceCenterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel

        viewModel.titleText.value = "초대코드"
        clickListeners()
        observes()
        fm= supportFragmentManager
        application = applicationContext as MainApplication

        viewModel.my_seq.value = application?.userData?.u_seq.toString()
    }

    override fun onResume() {
        super.onResume()
        viewModel.GetTerm()
    }
    private fun observes() {
        viewModel.toast.observe(this, Observer{ toast->
            if(!toast.isNullOrEmpty()){
                Toast.makeText(this,toast, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun clickListeners() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.llCenter.setOnClickListener {

            val gmailPackageName =  "com.google.android.gm"
            val intent : Intent? =  packageManager.getLaunchIntentForPackage(gmailPackageName)
            if(intent == null){
               viewModel.toast.value = "Gmail 앱을 설치 해주세요"
            }else{
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


//
//            var serviceCenterDialog = ServiceCenterDialog(this,0)
//            serviceCenterDialog!!.show(this.supportFragmentManager,"")
        }
        binding.llSecession.setOnClickListener {
            // 프로필 관리하기 클릭
            val i = Intent(this, ProfileManagementActivity::class.java)
            startActivity(i)
            // 기존 즉시 탈퇴 팝업대신 프로필관리 액티비티로 이동
//            secessionDialog = SecessionDialog(this) { isfinish ,ischeckde,text->
//                if (isfinish){
//                    viewModel.setSecession(ischeckde,text)
//                }
//
//                }
//            secessionDialog!!.show(fm!!, "")
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

    override fun finish() {
        super.finish()
      //  overridePendingTransition(com.supercarlounge.supercar.R.anim. fadein, com.supercarlounge.supercar.R.anim.fadeout)
    }
}
