package com.supercarlounge.supercar.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.DisplayMetrics
import android.view.*
import android.widget.FrameLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.supercarlounge.supercar.DialogEvent
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.databinding.DialogPoliceBinding
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogPoliceViewModel


class PoliceDialog(
    context: Context,
    val mDismisslistener: DialogEvent,
    var type: Int,
    var seq: String
) : DialogFragment() {

    var TAG = "UpdateDialog"
    private var oneButtonDialog : OneButtonSetDialog? =null
    var binding: DialogPoliceBinding? = null
    var viewmodel: DialogPoliceViewModel? = null
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


    override fun dismiss() {
        super.dismiss()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        viewmodel = ViewModelProvider(this).get(DialogPoliceViewModel::class.java)
        binding!!.viewModel = viewmodel
        binding!!.lifecycleOwner = this.viewLifecycleOwner
        viewmodel!!.viewtype.value = type
        mListener =mDismisslistener


        editTextListeners()
        setText()
        onclickListers()

    }


    private fun editTextListeners() {
        binding?.edtTitle?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {
                if (p0!!.isNotEmpty()) {
                    viewmodel?.contents?.value = p0.toString()
                } else {
                    viewmodel?.contents?.value = ""

                }

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })


    }

    @SuppressLint("ResourceType")
    private fun setText(){
        viewmodel!!.tv_main.value = "이 프로필을\n신고하시겠습니까?"
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
        viewmodel!!.tv_sub.value = "허위 신고 시 불이익이 있을 수 있습니다"
    }



    private fun onclickListers(){
        binding!!.ok.setOnClickListener {

                    if(viewmodel?.contents?.value.equals("")){
                        Toast.makeText(requireContext(), "신고사유를 작성해주세요.", Toast.LENGTH_SHORT).show()

                    }else{
                        mDismisslistener.okEvent(type, true, viewmodel?.contents?.value!!, seq.toString(),"")
                        dismiss()
                    }

        }
        binding!!.cancel.setOnClickListener {
            dismiss()
        }

    }

}