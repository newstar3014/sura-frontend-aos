package com.supercarlounge.supercar.dialog

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.hardware.display.DisplayManager
import android.os.Build
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
import com.supercarlounge.supercar.databinding.DialogLocationPermissionBinding
import com.supercarlounge.supercar.enumset.PopTypeStatus
import com.supercarlounge.supercar.enumset.RecDriveDialogType
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogBlockPostViewModel
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogLocationPermissionViewModel

class LocationPermissionDialog(
    context: Context,
    var index : Int,
    val event: (Boolean,Int) -> Unit
) : DialogFragment() {

    var binding: DialogLocationPermissionBinding? = null
    var viewmodel: DialogLocationPermissionViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
    override fun getTheme() = R.style.RoundedCornersDialog
    override fun onResume() {
        super.onResume()
        var buildDevice = getDeviceName()
        Log.d("폴드", buildDevice.toString())
        if (buildDevice?.contains("SM-F907N") == true
            || buildDevice?.contains("SM-F900N") == true
            || buildDevice?.contains("SM-F916N") == true
            || buildDevice?.contains("SM-F926N") == true
            || buildDevice?.contains("SM-F936N") == true
        ){
            checkFoldedDisplay(buildDevice)
        }else{
            setDialogWindowSize(buildDevice.toString())
        }

    }
    fun getDeviceName(): kotlin.String? {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            capitalize(model)
        } else {
            capitalize(manufacturer).toString() + " " + model
        }
    }
    private fun capitalize(s: kotlin.String?): kotlin.String? {
        if (s == null || s.length == 0) {
            return ""
        }
        val first = s[0]
        return if (Character.isUpperCase(first)) {
            s
        } else {
            first.uppercaseChar().toString() + s.substring(1)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_location_permission,
            null,
            false
        );
        return binding!!.root
    }

    //다이얼로그 사이즈 초기화
    private fun setDialogWindowSize(buildDevice:String) {

        val metrisc: DisplayMetrics = resources.displayMetrics
        val displayPW = metrisc.widthPixels
        val displayPH = metrisc.heightPixels
        val window = dialog!!.window
        window!!.setLayout(displayPW, displayPH)
        var x = 0
        var y = 0
        var fold2 ="SM-F907N"
        var fold3 ="SM-F900N"
        var fold4 ="SM-F916N"
        when(buildDevice){
            "F936N펼침" ->{
                    x = (displayPW * 0.6f).toInt()
                    y = (displayPH * 0.36f).toInt()
            }
            else->{
                    x = (displayPW * 0.6f).toInt()
                    y = (displayPH * 0.25f).toInt()
            }
        }



        var params = FrameLayout.LayoutParams(x, y)
        params.gravity = Gravity.CENTER
        binding!!.llDialog.layoutParams = params
    }

    fun checkFoldedDisplay(buildDevice:String) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            val dm = context?.applicationContext?.getSystemService(Context.DISPLAY_SERVICE) as? DisplayManager
            dm?.displays?.forEach {
                // mode: 1 펼친 상태
                // mode: 2 접힌 상태

                Log.d("폴드 체크", it.mode.modeId.toString())
                var isFoldState = ""
                if (it.mode.modeId.toString() == "1"){
                    isFoldState= buildDevice+"펼침"
                }else{
                    isFoldState= buildDevice+"접힘"
                }
                setDialogWindowSize(isFoldState)
            }
        }
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
                locationPermissionRequest.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )

            }
        })

        viewmodel?.cancelEvent?.observe(viewLifecycleOwner, Observer {
            if (it){
//                dialog?.cancel()
                event(false,index)

            }
        })
    }
    val locationPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.
                    Log.d("test_location", ": 1")
//                    dialog?.cancel()
                    event(true,index)

                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
                    Log.d("test_location", ": 2")
                }
                else -> {
                    binding?.tvMain?.text ="내 주변 정보를 확인하시려면\n설정-> 개인 정보 보호 위치 서비스 에서\nGPS를 켜주세요 더욱더 정확한\n위치 확인이 가능합니다."
                    binding?.ok?.visibility = View.GONE
                    binding?.cancel?.text = "닫기"
                    Log.d("test_location", ": 3")
                    // No location access granted.
                }
            }
        }

//    override fun onDismiss(dialog: DialogInterface) {
//        event(false,index)
//        super.onDismiss(dialog)
//    }
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