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
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.databinding.*
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogLoadingViewModel
import java.util.*


class NumOpenDialog(
    context: Context,
//    val listener: View.OnClickListener,
//    val mDismisslistener: DialogListener,
    var type: Int
) : DialogFragment() {

    var TAG = "UpdateDialog"
    var binding: DialogNumOpenBinding? = null
    var viewmodel: DialogLoadingViewModel? = null
    var calenderList: ArrayList<String>? = arrayListOf()
//    var mListener: DialogListener? = null

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
            R.layout.dialog_num_open,
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

                x = (displayPW * 0.6f).toInt()
                y = (displayPH * 0.43).toInt()
        var params = FrameLayout.LayoutParams(x, y)
        params.gravity = Gravity.CENTER
        binding!!.llDialog.layoutParams = params
    }

    @SuppressLint("ResourceType")
    private fun setText(){

        var content = binding?.tvPoint?.text
        val spannableString = SpannableString(content)
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
            23,
            28,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
            31,
            35,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )


        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor(getString(R.color.text_guide))),
            6,
            8,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor(getString(R.color.text_guide))),
            11,
            14,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            RelativeSizeSpan(1.08f),
            11,
            14,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )



        spannableString.setSpan(
            RelativeSizeSpan(1.08f),
            7,
            9,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding!!.tvPoint.text =spannableString
        viewmodel!!.tv_sub.value = "허위 신고 시 불이익이 있을 수 있습니다"
    }
    override fun dismiss() {
        super.dismiss()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        viewmodel = ViewModelProvider(this).get(DialogLoadingViewModel::class.java)
        binding!!.viewModel = viewmodel
        binding!!.setLifecycleOwner(this)
        viewmodel!!.viewtype.value = type
        setText()

        onclickListers()


    }



    private fun onclickListers() {
        binding!!.ok.setOnClickListener {
            dismiss()
        }

    }

}