package com.supercarlounge.supercar.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.supercarlounge.supercar.Constans
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.databinding.DialogTextBinding
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogTextViewModel


class TextDialog(
    context: Context,
//    val listener: View.OnClickListener,
//    val mDismisslistener: DialogListener,
    var type: Int
) : DialogFragment() {

    var TAG = "UpdateDialog"
    var binding: DialogTextBinding? = null
    var viewmodel: DialogTextViewModel? = null
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
            R.layout.dialog_text,
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
                y = (displayPH * 0.2).toInt()
            }
            2 -> {
                x = (displayPW * 0.6f).toInt()
                y = (displayPH * 0.3).toInt()
            }
        }
        var params = FrameLayout.LayoutParams(x, y)
        params.gravity = Gravity.CENTER
        dialog!!.window!!.setLayout(x, y)


    }



    override fun dismiss() {
        super.dismiss()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        viewmodel = ViewModelProvider(this).get(DialogTextViewModel::class.java)
        dialog!!.setCancelable(true)
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

    private fun setText() {
        when (viewmodel!!.type.value) {

            Constans.DIALOG_SPECIAL -> {

                viewmodel!!.tv_sub.value = "서비스 이용 실적 최상위 멤버\n또는 특별 선정 고객만 입장 가능합니다."

            }
            Constans.DIALOG_SERVICE_PREPARATION -> {
                viewmodel!!.tv_main.value = "서비스 준비 중입니다."
                viewmodel!!.tv_sub.value = "자동차 동호회(클럽)을 위한 공간으로\n준비 중입니다\n많은 관심 부탁드립니다."
            }

        }
    }

    private fun setImage(type:Int) {
        when (type) {
            Constans.DIALOG_SPECIAL -> {
                binding!!.ivMain.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.logo_lounge_109x109))
            }
            Constans.DIALOG_SERVICE_PREPARATION -> {
                binding!!.ivMain.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pop_zunbe_40x40))
            }
        }
    }
    private fun setViewType() {
        when (viewmodel!!.type.value) {
            Constans.DIALOG_INSURANCE -> {
                viewmodel!!.viewtype.value =2
            }
            Constans.DIALOG_SPECIAL -> {
                viewmodel!!.viewtype.value =1
            }
            Constans.DIALOG_SERVICE_PREPARATION -> {
                viewmodel!!.viewtype.value =2
            }
        }
    }


    private fun onclickListers() {
        binding!!.cancel.setOnClickListener {
            dismiss()
        }

    }

}