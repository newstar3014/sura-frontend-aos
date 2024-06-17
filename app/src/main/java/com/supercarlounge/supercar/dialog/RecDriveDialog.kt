package com.supercarlounge.supercar.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.hardware.display.DisplayManager
import android.hardware.usb.UsbDevice.getDeviceName
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.databinding.DialogRecDriveBinding
import com.supercarlounge.supercar.enumset.RecDriveDialogType
import com.supercarlounge.supercar.ui.activity.HeartsShopActivity
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogRecDriveViewModel

class RecDriveDialog(
    context: Context,
    var type:RecDriveDialogType,
    val event: (RecDriveDialogType,Boolean) -> Unit
) : DialogFragment() {

    var binding: DialogRecDriveBinding? = null
    var viewmodel: DialogRecDriveViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
    override fun getTheme() = R.style.RoundedCornersDialog

    fun checkFoldedDisplay(buildDevice:String) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            val dm = context?.applicationContext?.getSystemService(Context.DISPLAY_SERVICE) as? DisplayManager
            dm?.displays?.forEach {
                // mode: 1 펼친 상태
                // mode: 2 접힌 상태

                Log.d("폴드 체크", it.mode.modeId.toString())
                var isFoldState = ""
                if (it.mode.modeId.toString() == "1"){
                    isFoldState= buildDevice+"펼침"
                }else{
                    isFoldState= buildDevice+"접힘"
                }
                setDialogWindowSize(isFoldState)
            }
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
    override fun onResume() {
        super.onResume()
        var buildDevice = getDeviceName()
        Log.d("폴드", buildDevice.toString())
        if (buildDevice?.contains("SM-F907N") == true
            || buildDevice?.contains("SM-F900N") == true
            || buildDevice?.contains("SM-F916N") == true
            || buildDevice?.contains("SM-F926N") == true
            || buildDevice?.contains("SM-F936N") == true
        ){
            checkFoldedDisplay(buildDevice)
        }else{
            setDialogWindowSize(buildDevice.toString())
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_rec_drive,
            null,
            false
        );
        return binding!!.root
    }

    //다이얼로그 사이즈 초기화
    private fun setDialogWindowSize(buildDevice:String) {

        val metrisc: DisplayMetrics = resources.displayMetrics
        val displayPW = metrisc.widthPixels
        val displayPH = metrisc.heightPixels
        val window = dialog!!.window
        window!!.setLayout(displayPW, displayPH)
        var x = 0
        var y = 0
        var fold2 ="SM-F907N"
        var fold3 ="SM-F900N"
        var fold4 ="SM-F916N"
        when(buildDevice){
            "F936N펼침" ->{
                if (viewmodel!!.vtype.value == RecDriveDialogType.PROFILE_OPEN){
                    x = (displayPW * 0.6f).toInt()
                    y = (displayPH * 0.4f).toInt()
                } else if (viewmodel!!.vtype.value == RecDriveDialogType.PROFILE_NOTPASS_OPEN){
                    x = (displayPW * 0.6f).toInt()
                    y = (displayPH * 0.36f).toInt()
                }else if (viewmodel!!.vtype.value ==RecDriveDialogType.NOT_REC_DIRVE) {
                    x = (displayPW * 0.6f).toInt()
                    y = (displayPH * 0.36f).toInt()
                }else if(viewmodel!!.vtype.value == RecDriveDialogType.NOT_REC_DIRVE_2){
                    x = (displayPW * 0.6f).toInt()
                    y = (displayPH * 0.24f).toInt()
                }else{
                    x = (displayPW * 0.6f).toInt()
                    y = (displayPH * 0.36f).toInt()
                }
            }
            "F936N접힘" -> {
                if (viewmodel!!.vtype.value == RecDriveDialogType.PROFILE_OPEN){
                    x = (displayPW * 0.6f).toInt()
                    y = (displayPH * 0.4f).toInt()
                }else if(viewmodel!!.vtype.value == RecDriveDialogType.PROFILE_NOTPASS_OPEN){
                    x = (displayPW * 0.6f).toInt()
                    y = (displayPH * 0.36f).toInt()
                }else if (viewmodel!!.vtype.value ==RecDriveDialogType.NOT_REC_DIRVE) {
                    x = (displayPW * 0.6f).toInt()
                    y = (displayPH * 0.32f).toInt()
                }else if(viewmodel!!.vtype.value == RecDriveDialogType.NOT_REC_DIRVE_2){
                    x = (displayPW * 0.64f).toInt()
                    y = (displayPH * 0.24f).toInt()
                }else{
                    x = (displayPW * 0.6f).toInt()
                    y = (displayPH * 0.36f).toInt()
                }
            }
            else->{
                if (viewmodel!!.vtype.value == RecDriveDialogType.PROFILE_OPEN){
                    x = (displayPW * 0.6f).toInt()
                    y = (displayPH * 0.4f).toInt()
                }else if(viewmodel!!.vtype.value == RecDriveDialogType.PROFILE_NOTPASS_OPEN){
                    x = (displayPW * 0.6f).toInt()
                    y = (displayPH * 0.36f).toInt()
                }else if (viewmodel!!.vtype.value ==RecDriveDialogType.NOT_REC_DIRVE) {
                    x = (displayPW * 0.6f).toInt()
                    y = (displayPH * 0.36f).toInt()
                }else if(viewmodel!!.vtype.value == RecDriveDialogType.NOT_REC_DIRVE_2){
                    x = (displayPW * 0.6f).toInt()
                    y = (displayPH * 0.24f).toInt()
                }else{
                    x = (displayPW * 0.6f).toInt()
                    y = (displayPH * 0.36f).toInt()
                }
            }
        }



        var params = FrameLayout.LayoutParams(x, y)
        params.gravity = Gravity.CENTER
        binding!!.llDialog.layoutParams = params
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        viewmodel = ViewModelProvider(this).get(DialogRecDriveViewModel::class.java)
        viewmodel!!.vtype.value = type
        binding!!.viewModel = viewmodel
        binding!!.lifecycleOwner = this.viewLifecycleOwner

        setview()
        setclick()
    }

    @SuppressLint("ResourceType")
    fun setview(){
        when(viewmodel!!.vtype.value!!){
            RecDriveDialogType.REC_DRIVE_OFF->{
                Glide.with(binding!!.ivMain).load(R.drawable.ic_d_push_40x40).into(binding!!.ivMain)
                var content = "드라이브 추천 기능 혜택을\n끄시겠습니까?"
                val spannableString = SpannableString(content)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    0,
                    10,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    0,
                    10,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding!!.tvMain.text =spannableString
                viewmodel!!.tv_sub.value = "친구 추천 기능은 오너&패신저, 마이픽과 같은\n" +
                        "서비스를 이용할 때 꼭 활성화시켜야 하는\n기능입니다."
                viewmodel!!.tv_sub2.value = "비활성화 시 친구 추천 기능을\n사용하실 수 없습니다."
                viewmodel!!.tv_ok.value = "확인"
            }
            RecDriveDialogType.NOT_REC_DIRVE->{
                Glide.with(binding!!.ivMain).load(R.drawable.pop_icon_40x40).into(binding!!.ivMain)
                var content = "현재 드라이브 추천 기능을\n사용하실 수 없습니다."
                viewmodel!!.tv_sub.value = "1.이용 제재 기간\n" +
                        "2.추천 매칭 기능 OFF 상태\n" +
                        "3.추천 오너가 없는 경우\n" +
                        "4.추천 패신저가 없는 경우"
                binding!!.tvMain.text =content
                viewmodel!!.tv_ok.value = "확인"
            }
            RecDriveDialogType.PROFILE_OPEN->{
                Glide.with(binding!!.ivMain).load(R.drawable.profile_40x40).into(binding!!.ivMain)
                var content = "프로필 열람시\n3 하트가 차감됩니다.\n드라이브 패스권 구매 시 1일 3회 무료"
                val spannableString = SpannableString(content)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    0,
                    6,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    0,
                    6,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding!!.tvMain.text =spannableString
                viewmodel!!.tv_sub.value = "상대는 열람한 사실을 확인할 수 없습니다."
                viewmodel!!.tv_sub2.value = "드라이브 패스권 구매하기"
                viewmodel!!.tv_ok.value = "열람하기"
            }
            RecDriveDialogType.PROFILE_NOTPASS_OPEN->{
                Glide.with(binding!!.ivMain).load(R.drawable.profile_40x40).into(binding!!.ivMain)
                var content = "프로필 열람시\n3 하트가 차감됩니다."
                val spannableString = SpannableString(content)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    0,
                    6,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    0,
                    6,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding!!.tvMain.text =spannableString
                viewmodel!!.tv_sub.value = "상대는 열람한 사실을 확인할 수 없습니다."
                viewmodel!!.tv_ok.value = "열람하기"
            }
            RecDriveDialogType.NOT_REC_DIRVE_2->{
                Glide.with(binding!!.ivMain).load(R.drawable.pop_icon_40x40).into(binding!!.ivMain)


                binding!!.tvMain.text =   "원하시는 조건의 이성이 심사중이에요!\n다른 조건으로 추천받아 보시겠어요?"
                viewmodel!!.tv_ok.value = "확인"
            }
            else -> {}
        }

    }
    fun setclick(){
        binding!!.ok.setOnClickListener {
            event(viewmodel!!.vtype.value!!,true)
            dismiss()
        }
        binding!!.cancel.setOnClickListener {
            event(viewmodel!!.vtype.value!!,false)
            dismiss()
        }

        binding!!.llMini.setOnClickListener {
            var i  = Intent(requireContext(), HeartsShopActivity::class.java)
            startActivity(i)
            dismiss()
        }
    }
}