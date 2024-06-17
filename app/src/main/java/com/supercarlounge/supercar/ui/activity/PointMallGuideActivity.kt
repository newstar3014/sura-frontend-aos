package com.supercarlounge.supercar.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.TypefaceSpan
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.GradientDrawable
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.AlimAdapter
import com.supercarlounge.supercar.adapter.PointProductAdapter
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.databinding.ActivityAlimBinding
import com.supercarlounge.supercar.databinding.ActivityPointmallBinding
import com.supercarlounge.supercar.databinding.ActivityPointmallGuideBinding
import com.supercarlounge.supercar.viewmodel.AlimViewModel
import com.supercarlounge.supercar.viewmodel.PointMallGuideViewModel
import com.supercarlounge.supercar.viewmodel.PointMallViewModel

class PointMallGuideActivity : BaseActivitiy() {
    private lateinit var binding: ActivityPointmallGuideBinding
    private lateinit var viewModel: PointMallGuideViewModel
    var application: MainApplication? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PointMallGuideViewModel::class.java)
        binding = ActivityPointmallGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)
        application = applicationContext as MainApplication

        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel
        setclick()
        setguide()
    }
    fun setguide(){
        Glide.with(binding!!.ivWheel).load(R.drawable.steering_wheel_gif).override(200,200).centerInside().into(binding!!.ivWheel)
        Glide.with(binding!!.ivBoard).load(R.drawable.ic_list_alt_80x80).override(200,200).centerInside().into(binding!!.ivBoard)
        var content = getString(com.supercarlounge.supercar.R.string.point_guide_subtext_2)
        val spannableString = SpannableString(content)
        var span = TypefaceSpan( "optimus_princeps_pemi_bold.ttf")
        spannableString.setSpan(
            span,
            0,
            12,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
            0,
            12,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )


        binding!!.tvMonthPoint.text = spannableString
       var d = GradientDrawable(
            getColor(R.color.cooper_gradi_st),
            getColor(R.color.cooper_gradi_ed),
            getColor(
                R.color.white_4
            ),
            getColor(R.color.white_4),
            2f,
            10f,
            GradientDrawable.Direction.TL_BR,
            GradientDrawable.Direction.TL_BR
        )
        binding!!.llPointmall.background = d
    }
    fun setclick(){
        binding!!.ivBack.setOnClickListener {
            finish()
        }
        binding!!.btnDrive.setOnClickListener {
            setResult(10)
            finish()

        }
        binding!!.btnBoard.setOnClickListener {
            setResult(11)
            finish()
        }
        binding!!.llPointmall.setOnClickListener {
            setResult(12)
            finish()
        }
    }
}