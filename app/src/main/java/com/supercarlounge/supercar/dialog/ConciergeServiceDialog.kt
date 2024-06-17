package com.supercarlounge.supercar.dialog

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.ConciergeServicePagerAdapter
import com.supercarlounge.supercar.data.AllianceData
import com.supercarlounge.supercar.databinding.DialogConciergeServiceBinding
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogConCiergeServiceViewModel

class ConciergeServiceDialog(
    var type : String,
    var con :Context,
    var owner :LifecycleOwner,
     val event: (Boolean) -> Unit
) : DialogFragment() {

    var binding: DialogConciergeServiceBinding? = null
    var viewmodel: DialogConCiergeServiceViewModel? = null


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
            R.layout.dialog_concierge_service,
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
        var x = 0
        var y = 0
        if (type == "홈가이드"){
            Log.d("홈가이드", "1: ")
             x = displayPW
             y = displayPH
        }else{
            x = (displayPW * 0.6f).toInt()
            y = (displayPH * 0.5f).toInt()
        }

        var params = FrameLayout.LayoutParams(x, y)
        params.gravity = Gravity.CENTER
        binding!!.rlMain.layoutParams = params
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        viewmodel = ViewModelProvider(this).get(DialogConCiergeServiceViewModel::class.java)
        binding!!.viewModel = viewmodel
        binding!!.lifecycleOwner = this.viewLifecycleOwner

        setview()
        setclick()
    }

    @SuppressLint("ResourceType")
    fun setview(){



        if (type =="홈가이드"){
//            val myImageList = intArrayOf(R.drawable.im_home_guide1, R.drawable.im_home_guide2)
//            for (i in myImageList){

            val imageUri1=  Uri.Builder().scheme(ContentResolver.SCHEME_ANDROID_RESOURCE).authority(resources.getResourcePackageName(R.drawable.im_home_guide1_n))
                .appendPath(resources.getResourceTypeName(R.drawable.im_home_guide1_n))
                .appendPath(resources.getResourceEntryName(R.drawable.im_home_guide1_n))
                .build()
                .toString()
            val imageUri2=  Uri.Builder().scheme(ContentResolver.SCHEME_ANDROID_RESOURCE).authority(resources.getResourcePackageName(R.drawable.im_home_guide2))
                .appendPath(resources.getResourceTypeName(R.drawable.im_home_guide2_n))
                .appendPath(resources.getResourceEntryName(R.drawable.im_home_guide2_n))
                .build()
                .toString()
//            val imageUri2 = "drawable://"+ R.drawable.im_home_guide2
            var data1 = AllianceData("","","","","",imageUri1,"")
                var data2 = AllianceData("","","","","",imageUri2,"")
                viewmodel?.alliancelist?.value?.add(data1)
            viewmodel?.alliancelist?.value?.add(data2)
                Log.d("홈가이드", viewmodel?.alliancelist?.value.toString())
//            }

            viewmodel?.alliancelist?.value =   viewmodel?.alliancelist?.value
        }else{
            viewmodel!!.GetConciergeServicePage()
        }

        binding!!.pagerConcierge.adapter = ConciergeServicePagerAdapter(con,owner, arrayListOf()){
            event(true)
            dismiss()

        }
    }
    fun setclick(){
        binding!!.rlMain.setOnClickListener {
            event(true)
            dismiss()

        }
    }
}