package com.supercarlounge.supercar.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import com.google.gson.JsonArray
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import com.supercarlounge.supercar.BuildConfig
import com.supercarlounge.supercar.Constans
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.data.CompanyPhoneData
import com.supercarlounge.supercar.databinding.ActivityProfileCompanyBinding
import com.supercarlounge.supercar.dialog.CompanyPhoneDialog
import com.supercarlounge.supercar.dialog.TwoButtonDialog
import com.supercarlounge.supercar.viewmodel.ProfileCompanyViewModel

class ProfileCompanyActivity: BaseActivitiy() {
    private lateinit var binding: ActivityProfileCompanyBinding
    private lateinit var viewModel: ProfileCompanyViewModel
    private lateinit var application: MainApplication
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(ProfileCompanyViewModel::class.java)
        val i= intent
        var subseq = intent.getStringExtra("sub_seq")
        application = applicationContext as MainApplication
        viewModel.my_seq .value = application!!.userData!!.u_seq.toString()
        viewModel.sub_seq.value = subseq
        viewModel.myphone.value = application!!.userData!!.u_phone
        binding = ActivityProfileCompanyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel
        binding!!.ivSub1.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewRedefinition()
                binding!!.ivSub1.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }


        })
        viewModel.GetProfile(subseq.toString())

        clickListeners()
        observes()

    }

    private fun observes() {
        viewModel.user_info.observe(this, Observer { data ->
            if (data != null) {
                Log.d("나이", data.toString())
                var lat = "0"
                var lon = "0"
                var sub_seq = viewModel.sub_seq.value!!
                var email = data.u_email
                var u_com_phone = data.u_com_phone
                if(email.isNullOrEmpty() == false|| u_com_phone.isNullOrEmpty() == false) {
                    viewModel.open_type.value = true
                }
                    if (data.main_return.equals("Y")) {
                        viewModel.mainCheckReturn.value = "반려"
                        binding.viewBlack.visibility = View.VISIBLE
                        binding.tvMainCheckReturn.visibility = View.VISIBLE

                    } else if (data.main_return.equals("M")) {
                        viewModel.mainCheckReturn.value = "승인중"
                        binding.viewBlack.visibility = View.VISIBLE
                        binding.tvMainCheckReturn.visibility = View.VISIBLE
                    } else {
                        binding.viewBlack.visibility = View.GONE
                        binding.tvMainCheckReturn.visibility = View.GONE
                    }
                    for ((count, i) in data.subImage.withIndex()) {
                        if (!i.im_return.equals("N")) {
                            when (count) {
                                0 -> {
                                  //  binding.viewBlackSub1.visibility = View.VISIBLE
                                  //  binding.tvSubCheckReturn1.visibility = View.VISIBLE
                                    if (i.im_return.equals("Y")) {
                                        viewModel.subCheckReturn1.value = "반려"
                                    } else {
                                        viewModel.subCheckReturn1.value = "승인중"
                                    }
                                }
                                1 -> {
                                   // binding.viewBlackSub2.visibility = View.VISIBLE
                                   // binding.tvSubCheckReturn2.visibility = View.VISIBLE
                                    if (i.im_return.equals("Y")) {
                                        viewModel.subCheckReturn2.value = "반려"
                                    } else {
                                        viewModel.subCheckReturn2.value = "승인중"
                                    }
                                }
                                2 -> {
                               // binding.viewBlackSub3.visibility = View.VISIBLE
                               // binding.tvSubCheckReturn3.visibility = View.VISIBLE
                                if (i.im_return.equals("Y")){
                                    viewModel.subCheckReturn3.value ="반려"
                                }else{
                                    viewModel.subCheckReturn3.value ="승인중"
                                }
                                }
                                else -> {}
                            }


                        }
                    }



            }
        })
    }

    private fun clickListeners() {
        binding!!.ivBack.setOnClickListener {
            finish()
        }
        binding.llBottomBar.setOnClickListener {
            if (viewModel.user_info?.value!!.u_com_phone.isNullOrEmpty() == false) {
                ShowPhoneDialog()
            } else {
                val gmailPackageName = "com.google.android.gm"
                val intent: Intent? = packageManager.getLaunchIntentForPackage(gmailPackageName)
                if (intent == null) {
                    viewModel.toast.value = "Gmail 앱을 설치 해주세요"
                } else {
                    // val eMailId1 = "help@supercarlounge.com"
                    val eMailId1 = "seungyeung1@naver.com"
                    var eMailIds: Array<kotlin.String> = arrayOf(eMailId1)
                    var buildName = BuildConfig.VERSION_NAME
                    var buildDevice = getDeviceName()
                    var buildVersion = Build.VERSION.SDK_INT
                    var buildRelease = Build.VERSION.RELEASE

                    var name = application.userData!!.u_name
                    var contents = ""
                    var phone = application.userData!!.u_phone
                    val email = Intent(Intent.ACTION_SEND)
                    // email.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    email.setPackage("com.google.android.gm");
                    email.putExtra(Intent.EXTRA_SUBJECT, title)
                    // email.data = Uri.parse("mailto:")
                    email.putExtra(android.content.Intent.EXTRA_EMAIL, eMailIds);
                    email.putExtra(
                        Intent.EXTRA_TEXT,
                        "슈라 회원으로부터 문의사항이 도착했습니다!\n \n" +
                                "사용자 명 : $name \n연락처 : $phone \n내용 : $contents  "
                    );
                    email.type = "message/rfc822";
                    if (intent.resolveActivity(packageManager) != null) {
                        resultLaunchermail.launch(email);
                    } else {
                        Toast.makeText(this, "메일보내기에 실패했습니다. 구글메일을 확인해주세요.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }
    private fun viewRedefinition() {
        val layoutParams1 = binding.rlMain.layoutParams
        layoutParams1.width = binding.rlMain.width
        layoutParams1.height = binding.rlMain.width
        binding.rlMain.layoutParams = layoutParams1

        val layoutParams2 = binding.rlMain2.layoutParams
        layoutParams2.width = binding.rlMain2.width
        layoutParams2.height = binding.rlMain2.width
        val matrix = ColorMatrix()
        matrix.setSaturation(0f)
        binding.ivSub1.colorFilter = ColorMatrixColorFilter(matrix)
        binding.ivSub2.colorFilter = ColorMatrixColorFilter(matrix)
        binding.ivSub3.colorFilter = ColorMatrixColorFilter(matrix)

        val layoutParamsSub1 = binding.ivSub1.layoutParams
        layoutParamsSub1.width = binding.ivSub1.width
        layoutParamsSub1.height = binding.ivSub1.width

        val layoutParamsSub2 = binding.ivSub2.layoutParams
        layoutParamsSub2.width = binding.ivSub2.width
        layoutParamsSub2.height = binding.ivSub2.width

        val layoutParamsSub3 = binding.ivSub3.layoutParams
        layoutParamsSub3.width = binding.ivSub3.width
        layoutParamsSub3.height = binding.ivSub3.width

        Log.d("이미지 1",  binding.rlMain.width.toString())

        Log.d("이미지 서브1",  binding.ivSub1.width.toString())
        Log.d("이미지 서브2",  binding.ivSub2.width.toString())
        Log.d("이미지 서브3",  binding.ivSub3.width.toString())
        binding.ivSub1.layoutParams = layoutParamsSub1
        binding.ivSub2.layoutParams = layoutParamsSub2
        binding.ivSub3.layoutParams = layoutParamsSub3

    }
    override fun finish() {
        super.finish()
        overridePendingTransition(com.supercarlounge.supercar.R.anim. fadein, com.supercarlounge.supercar.R.anim.fadeout)
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

    var resultLaunchermail =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(viewModel.islogcall.value == false) {
                viewModel.InsertLog()
            }
        }
    fun ShowPhoneDialog(){

        var phone = viewModel.user_info.value!!.u_com_phone
        if (phone.isNullOrEmpty() == false) {
        var list = settingPhonelist(phone!!)
        if (list.size != 0) {

            var phonedialog = CompanyPhoneDialog(this, list)
            phonedialog!!.show(supportFragmentManager, "")
        }else{
            Toast.makeText(this, "전화번호를 가져올수 없습니다. help@supercarlounge.com 로 문의 메일을 부탁드립니다.", Toast.LENGTH_SHORT)
                .show()
        }
        }
    }
    fun settingPhonelist(phone_num:String):ArrayList<CompanyPhoneData>{
        var list = arrayListOf<CompanyPhoneData>()
        try {
            val obj: JsonArray = JsonParser.parseString(phone_num).asJsonArray
            for (i in obj) {
                var ob = i.asJsonObject
                var name = ob.get("name").asString
                var num = ob.get("number").asString
                if(name.isNullOrEmpty()==false && num.isNullOrEmpty()==false) {
                    list.add(CompanyPhoneData(name, num))
                }
            }
        }catch (e:JsonParseException){

        }
        return list
    }
}
