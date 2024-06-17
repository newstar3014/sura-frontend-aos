package com.supercarlounge.supercar.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.VipDriveAuctionData
import com.supercarlounge.supercar.databinding.DialogDriveRegistrationBinding
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogDriveRankUpViewModel
import java.util.*


class DriveRankUpDialog(
    context: Context,
    var type: Int,
    val data :VipDriveAuctionData,

    val heart :Int,
    val event: ( Boolean) -> Unit
) : DialogFragment() {

    var TAG = "UpdateDialog"
    var binding: DialogDriveRegistrationBinding? = null
    var viewmodel: DialogDriveRankUpViewModel? = null
    var calenderList: ArrayList<String>? = arrayListOf()

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
            R.layout.dialog_drive_registration,
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
        if (viewmodel!!.heartViewChecked.value == true) {

            x = (displayPW * 0.6f).toInt()
            y = (displayPH * 0.55f).toInt()

        }else{
            x = (displayPW * 0.6f).toInt()
            y = (displayPH * 0.30f).toInt()
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
        viewmodel = ViewModelProvider(this).get(DialogDriveRankUpViewModel::class.java)
        binding!!.viewModel = viewmodel
        binding!!.setLifecycleOwner(this)
        binding!!.lifecycleOwner = this.viewLifecycleOwner
        viewmodel!!.viewtype.value =type
        viewmodel!!.driveactiondata.value = data
        Glide.with(binding!!.ivSt).load(R.raw.steering_wheel_gif).override(200,200).centerInside().into(binding!!.ivSt)
        Glide.with(binding!!.ivCash).load(R.raw.cash_gif).override(200,200).centerInside().into(binding!!.ivCash)
        viewmodel!!.heartViewChecked.observe(viewLifecycleOwner, Observer {
            Log.d("test1", it.toString())
               setDialogWindowSize()
        })

        binding!!.cb1.setOnClickListener {
            if (viewmodel!!.heartChecked2.value ==false){
               Toast.makeText(requireContext(), "위의 사항 확인 체크해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        viewmodel!!.heartChecked1.observe(viewLifecycleOwner, Observer {
            Log.d("test2", it.toString())
            if (it) {

            }

        })

        viewmodel!!.heartChecked2.observe(viewLifecycleOwner, Observer {
            Log.d("test3", it.toString())
            if (it) {

            }else{
                viewmodel!!.heartChecked1.value = false
            }

        })

        setText()
        onclickListers()

    }

    private fun setText() {

    }


    private fun onclickListers() {
        binding!!.ok.setOnClickListener {
            if (viewmodel?.heartChecked2?.value == true) {
                event(true)
                this.dismiss()
            }else{
                Toast.makeText(
                    context,
                    "위의 사항을 확인하셨습니다.란을 체크해주세요",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding!!.cancel.setOnClickListener {
            this.dismiss()
            event(false)
        }

    }

}