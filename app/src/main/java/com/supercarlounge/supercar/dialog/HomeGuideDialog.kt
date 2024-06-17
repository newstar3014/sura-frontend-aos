package com.supercarlounge.supercar.dialog

import android.annotation.SuppressLint
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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.ConciergeServicePagerAdapter
import com.supercarlounge.supercar.data.AllianceData
import com.supercarlounge.supercar.databinding.DialogConciergeServiceBinding
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogConCiergeServiceViewModel

class HomeGuideDialog(
    var type : String,
    var con :Context,
    var owner :LifecycleOwner,
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
        val x = (displayPW * 0.6f).toInt()
        val y = (displayPH * 0.5f).toInt()
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
            val myImageList = intArrayOf(R.drawable.im_home_guide1_n, R.drawable.im_home_guide2_n)
            for (i in myImageList){

                val imageUri = "drawable://$i"
                var data = AllianceData("","","","","",imageUri,"")
                viewmodel?.alliancelist?.value?.add(data)
            }
            viewmodel?.alliancelist?.value =   viewmodel?.alliancelist?.value
        }else{
            viewmodel!!.GetConciergeServicePage()
        }

        binding!!.pagerConcierge.adapter = ConciergeServicePagerAdapter(con,owner, arrayListOf()){
            dismiss()
        }
    }
    fun setclick(){
        binding!!.rlMain.setOnClickListener {
            dismiss()
        }
    }
}