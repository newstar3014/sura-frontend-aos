package com.supercarlounge.supercar.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.DialogEvent
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.databinding.*
import com.supercarlounge.supercar.enumset.DialogCheckVipTimeType
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.*


class CheckVipTimeDialog(
    context: Context,
    var type: DialogCheckVipTimeType,
    var event: (DialogCheckVipTimeType, Boolean) -> Unit
) : DialogFragment() {

    var TAG = "UpdateDialog"
    var binding: DialogCheckVipTimeBinding? = null
    var viewmodel: DialogCheckVipTimeViewModel? = null
    var mListener: DialogEvent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun getTheme() = R.style.RoundedCornersDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_oneset_button,
            null,
            false
        );
        return binding!!.root
    }

    override fun onResume() {
        super.onResume()
        setDialogWindowSize()
    }

    //다이얼로그 사이즈 초기화
    private fun setDialogWindowSize() {

        val metrisc: DisplayMetrics = resources.displayMetrics
        val displayPW = metrisc.widthPixels
        val displayPH = metrisc.heightPixels
        val window = dialog!!.window
        window!!.setLayout(displayPW, displayPH)
        var x = 0
        var y = 0


        x = (displayPW * 0.6f).toInt()
        y = (displayPH * 0.25).toInt()


        var params = FrameLayout.LayoutParams(x, y)
        params.gravity = Gravity.CENTER
        binding!!.llDialog.layoutParams = params
    }


    override fun dismiss() {
        super.dismiss()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        viewmodel = ViewModelProvider(this).get(DialogCheckVipTimeViewModel::class.java)
        binding!!.viewModel = viewmodel
        binding!!.setLifecycleOwner(this)
        viewmodel!!.type.value = type
        setText(viewmodel!!.type.value!!)
        setImage(viewmodel!!.type.value!!)
        onclickListers()

    }

    private fun setText(type: DialogCheckVipTimeType) {
        when (type) {
            DialogCheckVipTimeType.DRIVE_TIME -> {
                viewmodel!!.tv_main.value = "대표차량은\n삭제하실 수 없습니다."
                viewmodel!!.tv_sub.value = "대표차량을 다른 차량으로 변경하신 후\n삭제해 주세요."

            }
        }
    }

    private fun setImage(type: DialogCheckVipTimeType) {
        when (type) {
            DialogCheckVipTimeType.DRIVE_TIME -> {
                Glide.with(binding!!.ivMain).load(R.drawable.declaration_40x40).override(200, 200)
                    .centerInside().into(binding!!.ivMain)
            }
        }
    }


    private fun onclickListers() {
        binding!!.ok.setOnClickListener {
            event(type, true)
            dismiss()
        }

    }

}