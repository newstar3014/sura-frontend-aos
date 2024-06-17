package com.supercarlounge.supercar.ui.activity


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.*
import android.text.Editable
import android.text.TextWatcher
import android.transition.Slide
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import com.supercarlounge.supercar.BuildConfig
import com.supercarlounge.supercar.Constans
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.CompanionAdapter
import com.supercarlounge.supercar.adapter.DriveAwayAdapter
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.data.UserInformationData
import com.supercarlounge.supercar.databinding.ActivitySubscriptionApplicationCompleteBinding
import com.supercarlounge.supercar.dialog.ShareDialog
import com.supercarlounge.supercar.enumset.ShareDialogType
import com.supercarlounge.supercar.viewmodel.SubscriptionApplicationCompleteViewModel
import kotlinx.coroutines.flow.collectLatest


class SubscriptionApplicationCompleteActivity : BaseActivitiy() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivitySubscriptionApplicationCompleteBinding
    private lateinit var viewModel: SubscriptionApplicationCompleteViewModel
    private var bottomSheetDialog: com.supercarlounge.supercar.dialog.BottomSheetDialog? = null
    private lateinit var application: MainApplication
    private var shareDialog : ShareDialog? = null
    private var adapter : CompanionAdapter? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        with(window) {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            // set an slide transition
            enterTransition = Slide(Gravity.END)
            exitTransition = Slide(Gravity.START)
        }
        viewModel =
            ViewModelProvider(this).get(SubscriptionApplicationCompleteViewModel::class.java)
        binding = ActivitySubscriptionApplicationCompleteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        application = applicationContext as MainApplication
        var invite_code = application.invite_code
        if(!invite_code.isNullOrEmpty()) {
            viewModel.invitation_code.value = invite_code
        }
        binding!!.viewModel = viewModel
        binding!!.setLifecycleOwner(this)
        clickListeners()
        edtListeners()
        flows()

        viewModel.my_u_seq.value = intent.getStringExtra("u_seq")
        viewModel.u_admin.value = intent.getStringExtra("u_admin")



        if (viewModel.u_admin.value.equals("N")) {
            viewModel.tv_title.value = "가입이 반려되었습니다."
            viewModel.u_admin_check.value = true
            viewModel.getCompanion(viewModel.my_u_seq?.value.toString())
            viewModel.getNewUserInformation(viewModel.my_u_seq?.value.toString(),application)
            viewModel.bottom_text.value = "다시 작성하기"
        }else if(viewModel.u_admin.value.equals("M")){

            viewModel.checkLogin(application.u_phone,application.push_token,application)

        }
        observes()

        adapter = CompanionAdapter(this, arrayListOf()) { data, position ->

        }
        binding!!.rvCompanion.adapter = adapter
        binding!!.rvCompanion.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
    }

    private fun observes() {
        viewModel.companionDataList.observe(this, Observer {

        })
        viewModel.my_user_data.observe(this, Observer {
            if (it!= null){
                Log.d("초대코드", it.u_code.toString())
                if (it.u_code!!.isNotEmpty()){
                    viewModel.invitation_code_check.value = true
                }
            }
        })

    }

    override fun onPause() {
        super.onPause()


    }

    private fun flows() {
        lifecycleScope.launchWhenStarted {
            viewModel.stateFlow.collectLatest {
                when (it) {
                    Constans.CODE_CHECK_NULL -> {

                    }
                    Constans.CODE_CHECK_BLANK -> {

                    }

                    Constans.CODE_LENGTH_6 -> {

                    }
                    Constans.CODE_READY -> {
                        viewModel.InsertInvitationCodeCehck()
                    }
                    Constans.CODE_CHECK_SUCCESS -> {

                        viewModel.InsertInvitationCodeSave(viewModel.my_user_data.value?.u_seq.toString())
                    }
                    Constans.CODE_CHECK_FAIL -> {
                        Toast.makeText(
                            this@SubscriptionApplicationCompleteActivity,
                            "없는 초대 코드입니다",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    Constans.INVITATION_CODE_SUCCESS -> {
                        Toast.makeText(
                            this@SubscriptionApplicationCompleteActivity,
                            "초대코드 입력 하트 증정 성공",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {}
                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //

    }

    override fun finish() {
        super.finish()
        overridePendingTransition(
            com.supercarlounge.supercar.R.anim.fadein,
            com.supercarlounge.supercar.R.anim.fadeout
        )
    }

    private fun setUrl(url: kotlin.String) {

    }

    @SuppressLint("DefaultLocale", "IntentReset")
    private fun clickListeners() {


        binding.tvContactUs.setOnClickListener {
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

        binding.ivShare.setOnClickListener {
            shareDialog = ShareDialog(this, ShareDialogType.SMS)
            {type,check->

            }
            shareDialog!!.show(this.supportFragmentManager,"")
        }

        binding.llBottomBar.setOnClickListener {
            if (viewModel.u_admin.value.equals("N")) {
                if (viewModel.companionDataList.value != null) {
                    var profileImage = false
                    var carImage = false
                    var incomImage = false
                    var aboutMe = false




//                    for (i in viewModel.companionDataList.value!!) {
//                        if (i.ur_type.contains("대표 이미지") || i.ur_type.contains("서브 이미지") || i.ur_type.contains("자기소개")  || i.ur_type.contains("드라이브타입") || i.ur_type.contains("드라이브코스")
//                        ) {
//                            profileImage = true
//                        } else if (i.ur_type.contains("차량 이미지") || i.ur_type.contains("종합보험 가입증서")) {
//                            carImage = true
//                        } else if (i.ur_type.contains("소득 금액 증명원")) {
//                            incomImage = true
//                        }
//                        if (i.ur_type.equals("일괄반려")){
//                                if (application.userData?.u_type.equals("OWNER")){
//                                    carImage = true
//                                }else{
//                                    incomImage = true
//                                }
//                            profileImage = true
//                        }
//
//
//                    }


                    var myUserData =viewModel.my_user_data.value
                    var carData = viewModel.my_car_data_list.value?.get(0)


                    if (myUserData?.u_type.equals("OWNER")){
                        if (carData != null) {
                            for (i in carData!!) {

                                if (i.uc_car_main_yn.equals("N")) {
                                    carImage = true
                                }
                                if (i.uc_insur_url_yn.equals("N")) {
                                    carImage = true
                                }
                            }
                        }
                    }else{
                        if (myUserData?.u_gender.equals("MALE")) {

                            if (myUserData?.u_income_yn.equals("N")) {
                                incomImage = true
                            }



                        }
                    }

                    if (myUserData?.main_return.equals("Y")){
                        profileImage = true
                    }

                    for (i in myUserData?.subImage!!){
                        if (i.im_return.equals("Y")){
                            profileImage = true
                        }
                    }
                    if (myUserData?.u_about_me_ck.equals("N")){
                        profileImage = true
                    }

                    if (myUserData?.u_drive_c_yn.equals("N")){
                        profileImage = true
                    }
                    if (myUserData?.u_drive_w_yn.equals("N")){
                        profileImage = true
                    }
                    //승영센터장 추가수정
                    if (myUserData.u_drive_c_yn.equals("M")&&myUserData.u_drive_w_yn.equals("M")&&myUserData.u_about_me_ck.equals("M")){
                        if(profileImage == false && incomImage == false ){
                            Toast.makeText(this,"반려처리가 잘못되었습니다. 슈라 관리자에게 문의해주세요.",Toast.LENGTH_SHORT).show()
                        }
                    }
                    if (profileImage && carImage){
                        val i = Intent(
                            this@SubscriptionApplicationCompleteActivity,
                            CarRegistrationActivity::class.java
                        )
                        i.putExtra("userData",viewModel.my_user_data.value )

                        i.putExtra("userCarData",viewModel.my_car_data_list.value)
                        i.putExtra("completeDataList",viewModel.companionDataList.value)
                        i.putExtra("view" ,"수정")
                        i.putExtra("type" ,"all")
                        startActivity(i)
                       // overridePendingTransition(R.anim. fadein, R.anim.fadeout)
                    }else if (profileImage && incomImage){
                        val i = Intent(this@SubscriptionApplicationCompleteActivity, IncomeInformationActivity::class.java)
                        var list :ArrayList<UserInformationData> = arrayListOf()
                        viewModel.my_user_data.value?.let { it1 -> list.add(it1) }
                        i.putParcelableArrayListExtra("userData",list)
                        i.putExtra("view" ,"수정")
                        i.putExtra("userCarData",viewModel.my_car_data_list.value)
                        i.putExtra("completeDataList",viewModel.companionDataList.value)
                        i.putExtra("type" ,"all")
                        startActivity(i)
                        //overridePendingTransition(R.anim. fadein, R.anim.fadeout)
                    }else if (profileImage){
                        val i = Intent(
                            this@SubscriptionApplicationCompleteActivity,
                            MyJoinInfomationActivity::class.java
                        )
                        i.putExtra("userData",viewModel.my_user_data.value)
                        i.putExtra("completeDataList",viewModel.companionDataList.value)
                        i.putExtra("view" ,"수정")

                        startActivity(i)
                       // overridePendingTransition(R.anim. fadein, R.anim.fadeout)
                    } else if (carImage){
                        val i = Intent(
                            this@SubscriptionApplicationCompleteActivity,
                            CarRegistrationActivity::class.java
                        )
                        i.putExtra("userData",viewModel.my_user_data.value)
                        i.putExtra("userCarData",viewModel.my_car_data_list.value)
                        i.putParcelableArrayListExtra("completeDataList",viewModel.companionDataList.value)
                        i.putExtra("view" ,"수정")
                        startActivity(i)
                        //overridePendingTransition(R.anim. fadein, R.anim.fadeout)
                    } else if (incomImage) {
                        val i = Intent(
                            this@SubscriptionApplicationCompleteActivity,
                            IncomeInformationActivity::class.java
                        )
                        i.putExtra("userData",viewModel.my_user_data.value)
                        i.putExtra("userCarData",viewModel.my_car_data_list.value)

                        i.putParcelableArrayListExtra("completeDataList",viewModel.companionDataList.value)
                        i.putExtra("view" ,"수정")

                        i.putParcelableArrayListExtra("completeDataList",viewModel.companionDataList.value)
                        startActivity(i)
                        //overridePendingTransition(R.anim. fadein, R.anim.fadeout)
                    }

                }


            }else if(viewModel.u_admin.value.equals("M")) {
                this.finish()
            }
        }
        binding.tvSend.setOnClickListener {

            if (viewModel.invitation_code.value?.equals("") == true ) {
                Toast.makeText(
                    this@SubscriptionApplicationCompleteActivity,
                    "초대코드를 작성해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if ( viewModel.invitation_code.value?.contains(" ") == true ){
                Toast.makeText(
                    this@SubscriptionApplicationCompleteActivity,
                    "초대코드에 공백이 포함되지 않아야합니다.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }





                viewModel.changeStateFlow(Constans.CODE_READY)

        }

    }

    fun edtListeners() {
        binding!!.edtInvitation.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {
                viewModel.invitation_code.value = p0.toString()
                Log.d("초대코드 텍스트", p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
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

}
