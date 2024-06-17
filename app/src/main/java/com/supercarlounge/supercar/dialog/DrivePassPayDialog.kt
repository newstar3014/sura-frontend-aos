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
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.databinding.DialogDrivePassPayBinding
import com.supercarlounge.supercar.enumset.DrivePassDialogStatus
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogDrivePassPayViewModel

class DrivePassPayDialog(
    context: Context,
    var type :DrivePassDialogStatus,
    var day: String,
    var dayType: Int,
    val event: (DrivePassDialogStatus, Boolean) -> Unit
) : DialogFragment() {

    var binding: DialogDrivePassPayBinding? = null
    var viewmodel: DialogDrivePassPayViewModel? = null


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
            R.layout.dialog_drive_pass_pay,
            null,
            false
        );
        return binding!!.root
    }

    //다이얼로그 사이즈 초기화
    private fun setDialogWindowSize() {
        var y = 0
        val metrisc: DisplayMetrics = resources.displayMetrics
        val displayPW = metrisc.widthPixels
        val displayPH = metrisc.heightPixels
        val window = dialog!!.window
        window!!.setLayout(displayPW, displayPH)
        if (type ==DrivePassDialogStatus.NOTHEART){
             y = (displayPH * 0.25f).toInt()
        }else{
             y = (displayPH * 0.3f).toInt()
        }
        val x = (displayPW * 0.6f).toInt()

        var params = FrameLayout.LayoutParams(x, y)
        params.gravity = Gravity.CENTER
        binding!!.llDialog.layoutParams = params
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        viewmodel = ViewModelProvider(this).get(DialogDrivePassPayViewModel::class.java)

        binding!!.viewModel = viewmodel
        binding!!.lifecycleOwner = this.viewLifecycleOwner
        setclick()
        viewmodel!!.vtype.observe(this.viewLifecycleOwner, Observer { vtype->
            setview(vtype)
        })
        viewmodel!!.vtype.value = type
    }

    @SuppressLint("ResourceType")
    fun setview(type:DrivePassDialogStatus){
        when(type){
            DrivePassDialogStatus.DAY1->{
                Glide.with(binding!!.ivPay).load(R.drawable.day1_24x24).centerInside().into(binding!!.ivPay)
                viewmodel!!.tv_main.value ="드라이브 패스권 1일"
                viewmodel!!.tv_sub.value = "이미 사용된 기간에 대한\n사용료는 환불되지 않습니다."
                viewmodel!!.tv_ok.value = "구매하기"
            }
            DrivePassDialogStatus.DAY30->{
                Glide.with(binding!!.ivPay).load(R.drawable.day30_24x24).centerInside().into(binding!!.ivPay)
                viewmodel!!.tv_main.value ="드라이브 패스권 30일"
                viewmodel!!.tv_sub.value = "이미 사용된 기간에 대한\n사용료는 환불되지 않습니다."
                viewmodel!!.tv_ok.value = "구매하기"
            }
            DrivePassDialogStatus.COMPLETE1->{
                Glide.with(binding!!.ivPay).load(R.drawable.day1_24x24).centerInside().into(binding!!.ivPay)
                viewmodel!!.tv_main.value ="구매가 완료되었습니다."
                viewmodel!!.tv_sub.value = "서비스 종료일"
                viewmodel!!.tv_ok.value = "확인"
            }
            DrivePassDialogStatus.COMPLETE30->{
                Glide.with(binding!!.ivPay).load(R.drawable.day30_24x24).centerInside().into(binding!!.ivPay)
                viewmodel!!.tv_main.value ="구매가 완료되었습니다."
                viewmodel!!.tv_sub.value = "서비스 종료일"
                viewmodel!!.tv_ok.value = "확인"
            }
            DrivePassDialogStatus.EXISTPASS1->{
                if (dayType == 30){
                    Glide.with(binding!!.ivPay).load(R.drawable.day30_24x24).centerInside().into(binding!!.ivPay)
                }else{
                    Glide.with(binding!!.ivPay).load(R.drawable.day1_24x24).centerInside().into(binding!!.ivPay)
                }

                viewmodel!!.tv_main.value = "적용되어 있는 드라이브 패스권이 있습니다."
                viewmodel!!.tv_sub.value = "서비스 종료일"
                viewmodel!!.tv_ok.value = "확인"
                viewmodel!!.date_string.value = day
            }

            DrivePassDialogStatus.NOTHEART->{
                Glide.with(binding!!.ivPay).load(R.drawable.ic2).centerInside().into(binding!!.ivPay)
                var content = "보유 하트가 부족합니다."
                viewmodel!!.tv_sub.value = "상점으로 이동하기"
                viewmodel!!.tv_ok.value = "이동하기"
                val spannableString = SpannableString(content)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    3,
                    5,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    3,
                    5,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding!!.tvMain.text =spannableString
            }
            else -> {}
        }
    }
    fun setclick(){
        binding!!.ok.setOnClickListener {
            if(viewmodel!!.vtype.value!! != DrivePassDialogStatus.DAY1|| type !=DrivePassDialogStatus.DAY30) {
                event(viewmodel!!.vtype.value!!, true)
                Log.d("테스트", "setclick: ")
                dismiss()
            }else{
                Log.d("테스트", "setclick:2 ")
                viewmodel!!.DrivePassPayment()
            }

        }
        binding!!.cancel.setOnClickListener {
          dismiss()

        }
    }
}