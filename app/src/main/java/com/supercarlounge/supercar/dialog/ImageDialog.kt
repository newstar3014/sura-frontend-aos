package com.supercarlounge.supercar.dialog

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.databinding.DialogBlockBinding
import com.supercarlounge.supercar.databinding.DialogImageBinding
import com.supercarlounge.supercar.databinding.DialogLocationPermissionBinding
import com.supercarlounge.supercar.enumset.PopTypeStatus
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogBlockPostViewModel
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogLocationPermissionViewModel

class ImageDialog(
    context: Context,
    val event: (Boolean) -> Unit
) : DialogFragment() {

    var binding: DialogImageBinding? = null
    var viewmodel: DialogLocationPermissionViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
    override fun getTheme() = R.style.RoundedCornersDialog
    override fun onResume() {
        super.onResume()
        setDialogWindowSize()

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_image,
            null,
            false
        );
        return binding!!.root
    }

    //다이얼로그 사이즈 초기화
    private fun setDialogWindowSize() {

        val metrisc: DisplayMetrics = resources.displayMetrics
        val displayPW = metrisc.widthPixels
        val displayPH = metrisc.heightPixels
        val window = dialog!!.window
        window!!.setLayout(displayPW, displayPH)
        val x = (displayPW * 0.6f).toInt()
        val y = (displayPH * 0.15f).toInt()
        var params = FrameLayout.LayoutParams(x, y)
        params.gravity = Gravity.CENTER
        binding!!.llDialog.layoutParams = params
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        viewmodel = ViewModelProvider(this).get(DialogLocationPermissionViewModel::class.java)
        binding!!.viewModel = viewmodel
        binding!!.lifecycleOwner = this.viewLifecycleOwner



        setview()
        setclick()
        viewmodel?.okEvent?.observe(viewLifecycleOwner, Observer {
          if (it){
              event(true)
          }
        })

        viewmodel?.cancelEvent?.observe(viewLifecycleOwner, Observer {
            if (it){
                event(false)
            }
        })
    }

    @SuppressLint("ResourceType")
    fun setview(){

    }
    fun setclick(){
//        binding!!.ok.setOnClickListener {
//            event(true)
//        }
//        binding!!.cancel.setOnClickListener {
//            event(false)
//        }
    }
}