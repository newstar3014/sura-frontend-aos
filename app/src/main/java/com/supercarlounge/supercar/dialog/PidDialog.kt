package com.supercarlounge.supercar.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.DisplayMetrics
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
import com.supercarlounge.supercar.databinding.DialogPidBinding
import com.supercarlounge.supercar.enumset.PidDialogType
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogPidViewModel

class PidDialog(
    context: Context,
    var type: PidDialogType
    , val event: (PidDialogType,Boolean) -> Unit
) : DialogFragment() {

    var binding: DialogPidBinding? = null
    var viewmodel: DialogPidViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
    override fun getTheme() = R.style.RoundedCornersDialog
    override fun onResume() {
        super.onResume()
//        setDialogWindowSize()

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_pid,
            null,
            false
        );
        return binding!!.root
    }

    //다이얼로그 사이즈 초기화
    private fun setDialogWindowSize() {

        val metrisc: DisplayMetrics = resources.displayMetrics
        val displayPW = metrisc.widthPixels
        val displayPH = metrisc.heightPixels
        val window = dialog!!.window
        window!!.setLayout(displayPW, displayPH)
        val x = (displayPW * 0.6f).toInt()
        val y = (displayPH * 0.4f).toInt()
        var params = FrameLayout.LayoutParams(x, y)
        params.gravity = Gravity.CENTER
        binding!!.llDialog.layoutParams = params
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        viewmodel = ViewModelProvider(this).get(DialogPidViewModel::class.java)
        viewmodel!!.vtype.value = type
        binding!!.viewModel = viewmodel
        binding!!.lifecycleOwner = this.viewLifecycleOwner

        setview()
        setclick()
    }

    @SuppressLint("ResourceType")
    fun setview(){
        when(type){
            PidDialogType.FRIEND->{
                Glide.with(binding!!.ivView).load(R.drawable.icon_pid_patner).centerInside().into(binding!!.ivView)
                var content = "나와 맞는 짝꿍 찾기!\n10하트가 소모됩니다."
                val spannableString = SpannableString(content)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    0,
                    8,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    0,
                    8,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding!!.tvMain.text =spannableString
                viewmodel!!.tv_sub.value = "짝꿍과 함께 신나는 드라이브!"
            }
            PidDialogType.MAN_GLAMMER->{
             Glide.with(binding!!.ivView).load(R.drawable.icon_pid_gram).centerInside().into(binding!!.ivView)
                var content = "글래머,볼륨있는 여성 회원 만나기\n15 하트가 소모됩니다."
                val spannableString = SpannableString(content)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    0,
                    14,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    0,
                    14,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding!!.tvMain.text =spannableString
                viewmodel!!.tv_sub.value = "이상형과 함께하는 설레는 드라이브!"
            }
            PidDialogType.FM_10PER->{
                Glide.with(binding!!.ivView).load(R.drawable.icon_pid_10per).centerInside().into(binding!!.ivView)
                var content = "상위 10% 이성 회원 만나기\n15 하트가 소모됩니다."
                val spannableString = SpannableString(content)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    7,
                    9,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    7,
                    9,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding!!.tvMain.text =spannableString
                viewmodel!!.tv_sub.value = "호감을 제일 많이 받은 회원!"
            }
            PidDialogType.NEW_USER->{
                Glide.with(binding!!.ivView).load(R.drawable.icon_pid_new).centerInside().into(binding!!.ivView)
                var content = "최근 가입한 신입회원 만나기\n10 하트가 소모됩니다."
                val spannableString = SpannableString(content)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    0,
                    11,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    0,
                    11,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding!!.tvMain.text =spannableString
                viewmodel!!.tv_sub.value = "신입 회원과 즐거운 드라이브!"
            }
            PidDialogType.AGE_20->{
                Glide.with(binding!!.ivView).load(R.drawable.icon_pid_20_1).centerInside().into(binding!!.ivView)
                Glide.with(binding!!.ivView2).load(R.drawable.icon_pid_20_2).centerInside().into(binding!!.ivView2)
                var content = "20대 드라이버,패신저 만나기\n10 하트가 소모됩니다."
                val spannableString = SpannableString(content)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    0,
                    12,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    0,
                    12,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding!!.tvMain.text =spannableString
                viewmodel!!.tv_sub.value = "본인 인증을 통한 실제 나이를 인증한 회원"
            }
            PidDialogType.LIKE_MUSIC->{
                Glide.with(binding!!.ivView).load(R.drawable.icon_pid_music).centerInside().into(binding!!.ivView)
                var content = "음악취향 비슷한 상대 만나기\n10 하트가 소모됩니다."
                val spannableString = SpannableString(content)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    0,
                    5,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    0,
                    5,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding!!.tvMain.text =spannableString
                viewmodel!!.tv_sub.value = "음악취향도 같으면 더 신나는 드라이브!"
            }
            PidDialogType.SUPER_USE->{
                Glide.with(binding!!.ivView).load(R.drawable.icon_pid_super).centerInside().into(binding!!.ivView)
                var content = "슈퍼카 오너 만나기\n10하트가 소모됩니다."
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
                viewmodel!!.tv_sub.value = "본인 소유의 차량 인증을 한 슈퍼카 오너!"
            }
            else -> {}
        }
    }
    fun setclick(){
        binding!!.ok.setOnClickListener {
            event(type,true)
            dismiss()
        }
        binding!!.cancel.setOnClickListener {
            event(type,false)
            dismiss()
        }
    }
}