package com.supercarlounge.supercar.dialog

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.databinding.DialogServiceCenterBinding
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogServiceCenterViewModel


class ServiceCenterDialog(
    context: Context,
//    val listener: View.OnClickListener,
//    val mDismisslistener: DialogListener,
    var type: Int
) : DialogFragment() {

    var TAG = "UpdateDialog"
    var binding: DialogServiceCenterBinding? = null
    var viewmodel: DialogServiceCenterViewModel? = null
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
            R.layout.dialog_service_center,
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
        when (viewmodel!!.viewtype.value) {
            1 -> {
                x = (displayPW * 0.6f).toInt()
                y = (displayPH * 0.25).toInt()
            }
            2 -> {
                x = (displayPW * 0.6f).toInt()
                y = (displayPH * 0.3).toInt()
            }
            3 -> {
                x = (displayPW * 0.6f).toInt()
                y = (displayPH * 0.3).toInt()
            }
            4 -> {
                x = (displayPW * 0.6f).toInt()
                y = (displayPH * 0.4).toInt()
            }
        }
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
        viewmodel = ViewModelProvider(this).get(DialogServiceCenterViewModel::class.java)
        binding!!.viewModel = viewmodel
        binding!!.setLifecycleOwner(this)
        viewmodel!!.type.value = type
//        mListener = mDismisslistener
//
//        viewmodel!!.ok.observe(viewLifecycleOwner, Observer {
//            it.getContentIfNotHandled()?.let {
//                if (it) {
//
//                }
//            }
//        })
//
//
//
//        viewmodel!!.cancel.observe(viewLifecycleOwner, Observer {
//            it.getContentIfNotHandled()?.let {
//                if (it) {
//
//                }
//            }
//
//        })
        setText()
        setViewType()
        onclickListers()
        setImage(viewmodel!!.type.value!!)


    }

    private fun setImage(type:Int) {

    }

    @SuppressLint("ResourceType")
    private fun setText() {

                viewmodel!!.tv_main.value = "고객센터"
                viewmodel!!.tv_sub.value = "이메일 고객센터만 운영합니다\n답변은 약 1~3일 소요될 수 있습니다."
                viewmodel!!.tv_third.value = "help@supercarlounge.com"
    }

    private fun setViewType() {

    }



    private fun onclickListers() {
        binding!!.ok.setOnClickListener{
            //클립보드 사용 코드
            val clipboardManager = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("CODE", viewmodel!!.tv_third.value.toString().trim())
            clipboardManager!!.setPrimaryClip(clipData)

            Toast.makeText(requireContext(), viewmodel!!.tv_third.value, Toast.LENGTH_SHORT).show()
        }
        binding!!.cancel.setOnClickListener{
            dismiss()
        }
    }



}