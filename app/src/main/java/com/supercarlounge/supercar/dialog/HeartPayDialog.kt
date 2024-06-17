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
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.databinding.DialogHeartpaymentBinding
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogHeartPayViewModel

class HeartPayDialog(
    context: Context,
    var check1 :Boolean,
    var check2 :Boolean,
     val event: (Boolean,Boolean,Boolean) -> Unit
) : DialogFragment() {

    var binding: DialogHeartpaymentBinding? = null
    var viewmodel: DialogHeartPayViewModel? = null


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
            R.layout.dialog_heartpayment,
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
        viewmodel = ViewModelProvider(this).get(DialogHeartPayViewModel::class.java)
        viewmodel!!.checked1.value =check1
        viewmodel!!.checked2.value = check2
        binding!!.viewModel = viewmodel
        binding!!.lifecycleOwner = this.viewLifecycleOwner

        setview()
        setclick()
    }

    @SuppressLint("ResourceType")
    fun setview(){
                var content = "프로필 열람 또는 호감을 보낼 때\n소모되는 하트를 대신 지불합니다."
                val spannableString = SpannableString(content)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    4,
                    6,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    4,
                    6,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    10,
                    12,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    10,
                    12,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding!!.tvMain.text =spannableString
                viewmodel!!.tv_sub.value ="하트가 소진될 때까지 서비스가 지속됩니다.\n이 옵션은 언제든지 끌 수 있습니다."
    }
    fun setclick(){
        binding!!.ok.setOnClickListener {
            event(true,viewmodel!!.checked1.value!!,viewmodel!!.checked2.value!!)
            dismiss()
        }
        binding!!.cancel.setOnClickListener {
            event(false,false,false)
            dismiss()
        }
    }
}