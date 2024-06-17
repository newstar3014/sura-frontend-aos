package com.supercarlounge.supercar.dialog

import android.content.Context
import android.content.DialogInterface
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
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.databinding.DialogGuideBinding
import com.supercarlounge.supercar.enumset.GuidePopType
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogGuideViewModel
import kotlin.reflect.typeOf

class GuideDialog(
    context: Context,
    var vtype:GuidePopType
    , val event: (Boolean) -> Unit
) : DialogFragment() {

    var binding: DialogGuideBinding? = null
    var viewmodel: DialogGuideViewModel? = null


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
            R.layout.dialog_guide,
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
        val x = (displayPW * 1.0f).toInt()
        val y = (displayPH * 1.0f).toInt()
        var params = FrameLayout.LayoutParams(x, y)
        params.gravity = Gravity.CENTER
        binding!!.llDialog.layoutParams = params
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        viewmodel = ViewModelProvider(this).get(DialogGuideViewModel::class.java)
        binding!!.viewModel = viewmodel
        binding!!.lifecycleOwner = this.viewLifecycleOwner
        setview()
        setclick()
    }
    fun setview(){
        when(vtype){

            GuidePopType.GUIDE_DRIVE_VIEW->{
                Glide.with(binding!!.ivGuide).load(R.drawable.guide_img_new1).override(binding!!.ivGuide.width,binding!!.ivGuide.height).into(binding!!.ivGuide)
            }
            GuidePopType.GUIDE_DRIVE_WRITE->{
                Glide.with(binding!!.ivGuide).load(R.drawable.guide_img_new2).override(binding!!.ivGuide.width,binding!!.ivGuide.height).into(binding!!.ivGuide)
            }
            GuidePopType.GUIDE_VIP_LOUNGE->{
                Glide.with(binding!!.ivGuide).load(R.drawable.guide_img_new3).override(binding!!.ivGuide.width,binding!!.ivGuide.height).into(binding!!.ivGuide)
            }
            GuidePopType.GUIDE_BOARD->{
                Glide.with(binding!!.ivGuide).load(R.drawable.img_quickguide_8_n).override(binding!!.ivGuide.width,binding!!.ivGuide.height).into(binding!!.ivGuide)
            }
            GuidePopType.GUIDE_BOARD_WRITE->{
                Glide.with(binding!!.ivGuide).load(R.drawable.guide_img_new5).override(binding!!.ivGuide.width,binding!!.ivGuide.height).into(binding!!.ivGuide)
            }
            GuidePopType.GUIDE_MY_CAR_BOARD->{
                Glide.with(binding!!.ivGuide).load(R.drawable.guide_img_new6).override(binding!!.ivGuide.width,binding!!.ivGuide.height).into(binding!!.ivGuide)
            }
            GuidePopType.GUIDE_MY_CAR_BOARD_POST->{
                Glide.with(binding!!.ivGuide).load(R.drawable.guide_img_new7).override(binding!!.ivGuide.width,binding!!.ivGuide.height).into(binding!!.ivGuide)
            }
            GuidePopType.LOCATION->{
                Glide.with(binding!!.ivGuide).load(R.drawable.img_quickguide_12).override(binding!!.ivGuide.width,binding!!.ivGuide.height).into(binding!!.ivGuide)
            }
            GuidePopType.MY_PAGE->{
                Glide.with(binding!!.ivGuide).load(R.drawable.img_quickguide_9_n).override(binding!!.ivGuide.width,binding!!.ivGuide.height).into(binding!!.ivGuide)
            }
        }
    }
    fun setclick(){

        binding!!.close.setOnClickListener {

            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        event(false)
    }
}