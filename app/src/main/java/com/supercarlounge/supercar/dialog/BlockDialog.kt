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
import android.view.*
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.supercarlounge.supercar.Constans.Companion.DIALOG_COMMENT_BLOCK_CALL
import com.supercarlounge.supercar.Constans.Companion.DIALOG_COMMENT_SUB_BLOCK_CALL
import com.supercarlounge.supercar.Constans.Companion.DIALOG_POST_BLOCK
import com.supercarlounge.supercar.Constans.Companion.DIALOG_PROFILE_BLOCK
import com.supercarlounge.supercar.Constans.Companion.POST_MORE_BLOCK
import com.supercarlounge.supercar.DialogEvent
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.databinding.DialogBlockBinding
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogBlockPostViewModel
import java.util.*


class BlockDialog(
    context: Context,
    val mDismisslistener: DialogEvent,
    var type: Int,
    var seq : String

) : DialogFragment() {

    var TAG = "UpdateDialog"
    var binding: DialogBlockBinding? = null
    var viewmodel: DialogBlockPostViewModel? = null
    var calenderList: ArrayList<String>? = arrayListOf()
//    var mListener: DialogListener? = null
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
            R.layout.dialog_block,
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
        val y = (displayPH * 0.25f).toInt()
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
        viewmodel = ViewModelProvider(this).get(DialogBlockPostViewModel::class.java)
        binding!!.viewModel = viewmodel
        binding!!.lifecycleOwner = this.viewLifecycleOwner
        viewmodel!!.viewtype.value = type
        mListener =mDismisslistener


        setText()
        onclickListers()

    }
    @SuppressLint("ResourceType")
    private fun setText(){
        when(type){
            POST_MORE_BLOCK ->{
                viewmodel!!.tv_main.value = getString(R.string.d_drive_block)
                var content = viewmodel!!.tv_main.value!!
                val spannableString = SpannableString(content)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    9,
                    11,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    9,
                    11,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding!!.tvMain.text =spannableString
            }

            DIALOG_COMMENT_BLOCK_CALL ->{
                viewmodel!!.tv_main.value = getString(R.string.comment_block)
                var content = viewmodel!!.tv_main.value!!
                val spannableString = SpannableString(content)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    9,
                    11,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    9,
                    11,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding!!.tvMain.text =spannableString
            }

            DIALOG_COMMENT_SUB_BLOCK_CALL ->{
                viewmodel!!.tv_main.value = getString(R.string.comment_block)
                var content = viewmodel!!.tv_main.value!!
                val spannableString = SpannableString(content)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    9,
                    11,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    9,
                    11,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding!!.tvMain.text =spannableString
            }
            DIALOG_PROFILE_BLOCK ->{
                viewmodel!!.tv_main.value = getString(R.string.d_profile_block)
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
            DIALOG_POST_BLOCK ->{
                viewmodel!!.tv_main.value = getString(R.string.d_post_block)
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
        }
    }



    private fun onclickListers(){
        binding!!.ok.setOnClickListener {
//            when(type) {
//                Constans.POST_MORE_BLOCK -> {
                    mDismisslistener.okEvent(type, true, "", seq, "")
                    dismiss()
//                }
//                Constans.COMMENT_MORE_BLOCK -> {
//                    mDismisslistener.okEvent(Constans.DIALOG_COMMENT_BLOCK_CALL, true, "", seq, "")
//                    dismiss()
//                }
//                Constans.COMMENT_SUB_MORE_BLOCK -> {
//                    mDismisslistener.okEvent(Constans.DIALOG_COMMENT_SUB_BLOCK_CALL, true, "", seq, "")
//                    dismiss()
//                }
//            }
        }
        binding!!.cancel.setOnClickListener {
            this.dismiss()
        }

    }

}