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
import com.supercarlounge.supercar.databinding.DialogQuickUserBinding
import com.supercarlounge.supercar.enumset.QuickTypeStatus
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogQuickUsertViewModel

class QuickUserDialog(
    context: Context,
    var type: QuickTypeStatus
    , val event: (QuickTypeStatus,Boolean) -> Unit
) : DialogFragment() {

    var binding: DialogQuickUserBinding? = null
    var viewmodel: DialogQuickUsertViewModel? = null


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
            R.layout.dialog_quick_user,
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
        viewmodel = ViewModelProvider(this).get(DialogQuickUsertViewModel::class.java)
        binding!!.viewModel = viewmodel
        binding!!.lifecycleOwner = this.viewLifecycleOwner
        viewmodel!!.viewtypeset.value = type

        setview()
        setclick()
    }

    @SuppressLint("ResourceType")
    fun setview(){
        when(type){
            QuickTypeStatus.QUICK_HEART->{
                viewmodel!!.tv_main.value = "슈라님이 보내신\n호감을 확인하시겠습니까?"
                viewmodel!!.tv_ok.value = "확인"
                Glide.with(binding!!.ivImage).load(R.raw.heart_gif).override(200,200).centerInside().into(binding!!.ivImage)
                var content = viewmodel!!.tv_main.value!!
                val spannableString = SpannableString(content)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    21,
                    23,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    21,
                    23,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding!!.tvMain.text =spannableString
            }
            QuickTypeStatus.QUICK_WARNING->{
                viewmodel!!.tv_main.value = "회원가입을 하시면 이용 하실 수 있습니다"
                viewmodel!!.tv_ok.value = "회원가입"
                var content = viewmodel!!.tv_main.value!!
                val spannableString = SpannableString(content)
                Glide.with(binding!!.ivImage).load(R.drawable.ic_group_1603).override(200,200).centerInside().into(binding!!.ivImage)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    0,
                    4,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    0,
                    4,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding!!.tvMain.text =spannableString
            }

        }
    }
    fun setclick(){
        binding!!.ok.setOnClickListener {
            event(type,true)
        }
        binding!!.cancel.setOnClickListener {
            event(type,false)
        }
    }
}