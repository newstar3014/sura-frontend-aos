package com.supercarlounge.supercar.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.hardware.display.DisplayManager
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.databinding.DialogDriveRegiBinding
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogDriveRegiViewModel

class DriveRegiDialog(
    context: Context,

    val event: (Boolean, Boolean, Boolean,Boolean) -> Unit
) : DialogFragment() {

    var binding: DialogDriveRegiBinding? = null
    var viewmodel: DialogDriveRegiViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun getTheme() = R.style.RoundedCornersDialog
    override fun onResume() {
        super.onResume()
        deviceCheck()

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
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_drive_regi,
            null,
            false
        );
        return binding!!.root
    }

    //다이얼로그 사이즈 초기화
    private fun setDialogWindowSize(Divicetype:String) {

        val metrisc: DisplayMetrics = resources.displayMetrics
        val displayPW = metrisc.widthPixels
        val displayPH = metrisc.heightPixels
        val window = dialog!!.window
        var x = 0
        var y = 0
        Log.d("폴드 체크", Divicetype.toString())
        window!!.setLayout(displayPW, displayPH)
        if (Divicetype.equals("폴드펼침")){
            if (viewmodel?.foldchecked1?.value == true || viewmodel?.foldchecked2?.value == true){
                x = (displayPW * 0.44f).toInt()
                y = (displayPH * 0.70f).toInt()
            }else {
                x = (displayPW * 0.44f).toInt()
                y = (displayPH * 0.3f).toInt()
            }

        }else if (Divicetype.equals("폴드접힘")){
            if (viewmodel?.foldchecked1?.value == true || viewmodel?.foldchecked2?.value == true){
                x = (displayPW * 0.62f).toInt()
                y = (displayPH * 0.55f).toInt()
            }else {
                x = (displayPW * 0.62f).toInt()
                y = (displayPH * 0.25f).toInt()
            }
        }else{
            if (viewmodel?.foldchecked1?.value == true || viewmodel?.foldchecked2?.value == true){
                x = (displayPW * 0.62f).toInt()
                y = (displayPH * 0.6f).toInt()
            }else {
                x = (displayPW * 0.62f).toInt()
                y = (displayPH * 0.25f).toInt()
            }
        }



        var params = FrameLayout.LayoutParams(x!!, y!!)
        params.gravity = Gravity.CENTER
        binding!!.llDialog.layoutParams = params
    }
    fun checkFoldedDisplay() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            val dm = context?.applicationContext?.getSystemService(Context.DISPLAY_SERVICE) as? DisplayManager
            dm?.displays?.forEach {
                // mode: 1 펼친 상태
                // mode: 2 접힌 상태

                Log.d("폴드 체크", it.mode.modeId.toString())
                var isFoldState = ""
                if (it.mode.modeId.toString() == "1"){
                    isFoldState="폴드펼침"
                }else{
                    isFoldState="폴드접힘"
                }
                setDialogWindowSize(isFoldState)
            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        viewmodel = ViewModelProvider(this).get(DialogDriveRegiViewModel::class.java)
        binding!!.viewModel = viewmodel
        binding!!.lifecycleOwner = this.viewLifecycleOwner

        viewmodel?.foldchecked1?.observe(this, Observer{  it ->
            deviceCheck()


        })

        viewmodel?.foldchecked2?.observe(this, Observer{  it ->
            deviceCheck()


        })

        setview()
        setclick()

    }
    fun deviceCheck(){
        var buildDevice = getDeviceName()
        Log.d("폴드", buildDevice.toString())
        if (buildDevice?.contains("SM-F907N") == true
            || buildDevice?.contains("SM-F900N") == true
            || buildDevice?.contains("SM-F916N") == true
            || buildDevice?.contains("SM-F926N") == true
            || buildDevice?.contains("SM-F936N") == true
        ){
            checkFoldedDisplay()
        }else{
            setDialogWindowSize("기본")
        }
    }

    @SuppressLint("ResourceType")
    fun setview() {


        var content0 = "드라이브를\n등록하시겠습니까?"
        val spannableString0 = SpannableString(content0)
        spannableString0.setSpan(
            ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
            0,
            4,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString0.setSpan(
            RelativeSizeSpan(1.08f),
            0,
            4,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding!!.tvMain0.text = spannableString0

        Glide.with(binding!!.ivMain).load(R.raw.steering_wheel_gif).override(200,200).centerInside().into(binding!!.ivMain)


        var content1 = "내 프로필을 공개로 설정 시\n상대방이 바로 프로필을\n확인하고 빠른 매칭이 가능합니다."
        val spannableString1 = SpannableString(content1)
        spannableString1.setSpan(
            ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
            7,
            9,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString1.setSpan(
            RelativeSizeSpan(1.08f),
            7,
            9,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString1.setSpan(
            ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
            34,
            39,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString1.setSpan(
            RelativeSizeSpan(1.08f),
            34,
            39,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding!!.tvMain1.text = spannableString1
        viewmodel!!.tv_sub1.value = "비공개인 경우 프로필 열람을 승인하는\n단계가 추가됩니다."


        var content2 = "프로필 열람 또는 호감을 보낼 때\n소모되는 하트를 대신 지불합니다."
        val spannableString2 = SpannableString(content2)
        spannableString2.setSpan(
            ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
            4,
            6,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString2.setSpan(
            RelativeSizeSpan(1.08f),
            4,
            6,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString2.setSpan(
            ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
            10,
            12,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString2.setSpan(
            RelativeSizeSpan(1.08f),
            10,
            12,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding!!.tvMain2.text = spannableString2
        viewmodel!!.tv_sub2.value = "하트가 소진될 때까지 서비스가 지속됩니다.\n이 옵션은 언제든지 끌 수 있습니다."
    }

    fun setclick() {
        binding!!.ok.setOnClickListener {
            event(true, viewmodel!!.checked1.value!!, viewmodel!!.checked2.value!!,viewmodel!!.checked3.value!!)
            dismiss()
        }
        binding!!.cancel.setOnClickListener {
            event(false, false, false,false)
            dismiss()
        }
    }
}