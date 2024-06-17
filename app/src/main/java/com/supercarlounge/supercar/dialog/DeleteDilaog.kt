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
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.databinding.DialogTwoButtonBinding
import com.supercarlounge.supercar.enumset.PopTypeStatus
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogTwoButtonViewModel

class DeleteDilaog(
    context: Context,
    var type: PopTypeStatus
    , val event: (PopTypeStatus, Boolean) -> Unit
) : DialogFragment() {
    var binding: DialogTwoButtonBinding? = null
    var viewmodel: DialogTwoButtonViewModel? = null
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
            R.layout.dialog_two_button,
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
        var y= 0
//        when (viewmodel!!.viewtype.value) {
//            1 -> {
//                x = (displayPW * 0.6f).toInt()
//                y = (displayPH * 0.25).toInt()
//            }
//            2 -> {
//                x = (displayPW * 0.6f).toInt()
//                y = (displayPH * 0.3).toInt()
//            }
//            3 -> {
//                x = (displayPW * 0.6f).toInt()
//                y = (displayPH * 0.4).toInt()
//            }
//            4 -> {
                x = (displayPW * 0.6f).toInt()
                y = (displayPH * 0.3).toInt()
//            }
//        }
        var params = FrameLayout.LayoutParams(x, y)

        params.gravity = Gravity.CENTER
        dialog!!.window!!.setLayout(x, y)
//        binding!!.llDialog.layoutParams = params
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        viewmodel = ViewModelProvider(this).get(DialogTwoButtonViewModel::class.java)
        binding!!.viewModel = viewmodel
        binding!!.setLifecycleOwner(this)
        viewmodel!!.viewtypeset.value = type
        viewmodel!!.viewtype.value = 1
        setview()
//        setViewType()
        onclickListers()

    }
    @SuppressLint("ResourceType")
    fun setview(){
        binding!!.ivMain.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_group_2242))
        when(type){
            PopTypeStatus.DRIVE_DELETE->{
                viewmodel!!.tv_main.value = getString(R.string.drive_delete)
                val content: String = viewmodel!!.tv_main.value!!
                val spannableString = SpannableString(content)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    8,
                    11,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    8,
                    11,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding!!.tvMain.text =spannableString
            }
            PopTypeStatus.COMMENT_DELETE->{
                viewmodel!!.tv_main.value = getString(R.string.comment_delete)
                val content: String = viewmodel!!.tv_main.value!!
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
            PopTypeStatus.POST_DELETE->{
                viewmodel!!.tv_main.value = getString(R.string.post_delete)
                val content: String = viewmodel!!.tv_main.value!!
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
            else -> {}
        }

    }
    private fun onclickListers() {
        binding!!.ok.setOnClickListener{
            event(type,true)
            dismiss()
        }
        binding!!.cancel.setOnClickListener{
            event(type,false)
            dismiss()
        }
    }
}