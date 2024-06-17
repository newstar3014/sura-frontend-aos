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
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.databinding.DialogPayment40Binding
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.Payment40ViewModel
import java.util.*


class Payment40Dialog(
    context: Context,
//    val listener: View.OnClickListener,
//    val mDismisslistener: DialogListener,
//    var type: Int
) : DialogFragment() {

    var TAG = "UpdateDialog"
    var binding: DialogPayment40Binding? = null
    var viewmodel: Payment40ViewModel? = null
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
            R.layout.dialog_payment40,
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
        val y = (displayPH * 0.2f).toInt()
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
        viewmodel = ViewModelProvider(this).get(Payment40ViewModel::class.java)
        binding!!.viewModel = viewmodel
        binding!!.setLifecycleOwner(this)
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
        onclickListers()

    }
    private fun setText(){
        viewmodel!!.title.value = "회원님께서는 대납 기능을\n사용한 게시글만 작성 가능합니다"
    }



    private fun onclickListers(){
        binding!!.ok.setOnClickListener {
            this.dismiss()
        }
    }

}