package com.supercarlounge.supercar.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.*
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.Constans
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.databinding.DialogDriveExposureBinding
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogDriveExposureViewModel
import java.util.*
import kotlin.concurrent.timer


class DriveExposureDialog(
    context: Context,
//    val listener: View.OnClickListener,
//    val mDismisslistener: DialogListener,
    var type: Int,
    var my_u_seq : String,
    var deficientCount :Int,
    val event: (Int, Boolean,String) -> Unit
) : DialogFragment() {

    var TAG = "UpdateDialog"
    var binding: DialogDriveExposureBinding? = null
    var viewmodel: DialogDriveExposureViewModel? = null
    var calenderList: ArrayList<String>? = arrayListOf()
    var timer = Timer()
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
            R.layout.dialog_drive_exposure,
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
            Constans.DIALOG_DRIVE_EXPOSURE_1 -> {
                x = (displayPW * 0.6f).toInt()
                y = (displayPH * 0.35f).toInt()
            }
            Constans.DIALOG_DRIVE_EXPOSURE_2 -> {
                x = (displayPW * 0.6f).toInt()
                y = (displayPH * 0.47f).toInt()
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
        viewmodel = ViewModelProvider(this).get(DialogDriveExposureViewModel::class.java)
        binding!!.viewModel = viewmodel
        binding!!.setLifecycleOwner(this)
        viewmodel!!.viewtype.value = type
//        mListener = mDismisslistener
        Glide.with(binding!!.ivDiamond).load(R.raw.diamond_gif).override(200,200).centerInside().into(binding!!.ivDiamond)
        viewmodel?.my_seq?.value = my_u_seq
        viewmodel!!.GetMyvipDrive()
        viewmodel!!.deficientCount.value = "최소 필요 하트 $deficientCount"

        viewmodel?.isviptimer?.observe(this, androidx.lifecycle.Observer {
            if(it){
                setTimer()
            }else{
                stoptimer()
            }
        })

        binding!!.edtHeart.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {
                viewmodel?.addHeartCount?.value = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

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

    override fun onPause() {
        super.onPause()
        stoptimer()
    }

    private fun setText() {
        when (viewmodel!!.viewtype.value) {
            Constans.DIALOG_DRIVE_EXPOSURE_1 -> {
                viewmodel!!.tv_sub.value = getString(R.string.d_drive_exposure_1)
                viewmodel!!.okText.value = "확인"


            }
            Constans.DIALOG_DRIVE_EXPOSURE_2 -> {
                viewmodel!!.tv_sub.value = getString(R.string.d_drive_exposure_2)
                viewmodel!!.okText.value = "재등록"
            }
        }
    }

    fun setTimer() {


        if (timer != null){
            timer!!.cancel()

        }
        timer= timer(period = 1000, initialDelay = 1000) {

            requireActivity().runOnUiThread {
                viewmodel?.settime()
            }
        }


    }
    fun stoptimer(){
        if (timer != null){
            timer!!.cancel()

        }
    }
    private fun onclickListers() {
        binding!!.ok.setOnClickListener {
            event(type,true,viewmodel?.addHeartCount?.value.toString())
        }
        binding!!.cancel.setOnClickListener {
            event(type,false,"")
        }

    }

}