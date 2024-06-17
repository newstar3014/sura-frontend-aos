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
import com.supercarlounge.supercar.databinding.DialogRpmBinding
import com.supercarlounge.supercar.enumset.RpmStatus
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogRpmViewModel

class RpmDialog(
    context: Context,
    var type: RpmStatus,
    var selectrpm:Int
    , val event: (RpmStatus, Boolean) -> Unit
) : DialogFragment() {

    var binding: DialogRpmBinding? = null
    var viewmodel: DialogRpmViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
    override fun getTheme() = R.style.RoundedCornersDialog
    override fun onResume() {
        super.onResume()
        setDialogWindowSize()

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_rpm,
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
        val y = (displayPH * 0.25f).toInt()
        var params = FrameLayout.LayoutParams(x, y)
        params.gravity = Gravity.CENTER
        binding!!.llDialog.layoutParams = params
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        viewmodel = ViewModelProvider(this).get(DialogRpmViewModel::class.java)
        binding!!.viewModel = viewmodel
        binding!!.lifecycleOwner = this.viewLifecycleOwner
        viewmodel!!.type.value = type
        setview()
        setclick()
    }

    @SuppressLint("ResourceType")
    fun setview(){
        when(type){
            RpmStatus.RPM_SET->{
                var rpm = selectrpm.toString()
                var content =rpm+"점으로\n평가하시겠습니까?"
                val spannableString = SpannableString(content)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    0,
                    rpm.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    0,
                    rpm.length,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding!!.tvMain.text =spannableString
            }
            RpmStatus.VIEW->{
                var content = "RPM 점수를\n리셋하시겠습니까?"
                val spannableString = SpannableString(content)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    8,
                    10,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    8,
                    10,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding!!.tvMain.text =spannableString
            }
            RpmStatus.MY_CAR_SAVE_P->{

                Glide.with(binding!!.ivMain)
                    .load(R.drawable.pop_icon_40x40)
                    .into(binding!!.ivMain)
                var content = "임시 저장된 글을 불러올까요?"

                binding!!.tvMain.text =content
                viewmodel!!.tv_ok.value ="불러오기"
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