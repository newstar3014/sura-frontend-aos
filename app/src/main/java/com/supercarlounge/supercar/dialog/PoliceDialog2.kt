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

import com.supercarlounge.supercar.databinding.DialogPoliceBinding
import com.supercarlounge.supercar.enumset.PopTypeStatus
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogPoliceViewModel

class PoliceDialog2(
    context: Context,
    var type: PopTypeStatus
    , val event: (PopTypeStatus, Boolean,String) -> Unit
) : DialogFragment() {
    var binding: DialogPoliceBinding? = null
    var viewmodel: DialogPoliceViewModel? = null


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
            R.layout.dialog_police,
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
        val x = (displayPW * 0.6f).toInt()
        val y = (displayPH * 0.4f).toInt()
        var params = FrameLayout.LayoutParams(x, y)
        params.gravity = Gravity.CENTER
        binding!!.llDialog.layoutParams = params
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        viewmodel = ViewModelProvider(this).get(DialogPoliceViewModel::class.java)
        binding!!.viewModel = viewmodel
        binding!!.lifecycleOwner = this.viewLifecycleOwner
        viewmodel!!.viewtypeset.value = type
        setview()
        setclick()


    }
    @SuppressLint("ResourceType")
    fun setview(){
        when(type){
            PopTypeStatus.DRIVE_POLICE->{
                viewmodel!!.tv_main.value = getString(R.string.drive_police)
                var content = viewmodel!!.tv_main.value!!
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
            PopTypeStatus.PROFILE_POLICE->{
                viewmodel!!.tv_main.value = getString(R.string.proflie_police)
                var content = viewmodel!!.tv_main.value!!
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
            }
            PopTypeStatus.POST_POLICE->{
                viewmodel!!.tv_main.value = getString(R.string.police_post)
                var content = viewmodel!!.tv_main.value!!
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
            }
            else -> {}
        }
    }
    fun setclick(){
        binding!!.ok.setOnClickListener {

            event(type,true,viewmodel!!.contents.value!!)
        }
        binding!!.cancel.setOnClickListener {
            event(type,false,viewmodel!!.contents.value!!)
        }
    }
}