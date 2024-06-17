package com.supercarlounge.supercar.customview

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.databinding.ItemMySubInfo
import com.supercarlounge.supercar.databinding.ItemTabBinding
import com.supercarlounge.supercar.databinding.ItemTagBinding
import com.supercarlounge.supercar.viewmodel.MyCarBoardWritingViewModel
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.BottomSheetRegionDailogViewModel

class CustomTag : LinearLayout {

    var binding: ItemTagBinding?= null
    var mViewModel :BottomSheetRegionDailogViewModel? = null

    constructor(context: Context?, viewModel: BottomSheetRegionDailogViewModel,) : super(context) {
        initView(viewModel)

    }


    constructor(context: Context?,viewModel: BottomSheetRegionDailogViewModel, attrs: AttributeSet?) : super(context, attrs) {
        initView(viewModel)
        getAttrs(attrs)
        setOnClickListeners()
    }

    constructor(context: Context?,viewModel: BottomSheetRegionDailogViewModel, attrs: AttributeSet?, defStyle: Int) : super(context, attrs) {
        initView(viewModel)
        getAttrs(attrs, defStyle)
        setOnClickListeners()
    }

    private fun setOnClickListeners() {

    }

    init { // inflate binding and add as view


    }

    private fun initView(viewModel:BottomSheetRegionDailogViewModel) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        binding = ItemMySubInfo.inflate(inflater)
        binding = ItemTagBinding.inflate(LayoutInflater.from(context), this, true)
        mViewModel = viewModel
    }

    private fun getAttrs(attrs: AttributeSet?) {

        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemMySubInfo)
        setTypeArray(typedArray)
    }

    private fun getAttrs(attrs: AttributeSet?, defStyle: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemMySubInfo, defStyle, 0)
        setTypeArray(typedArray)
    }

    private fun setTypeArray(typedArray: TypedArray) {
        typedArray.recycle()
    }


    fun setText(text_string: String?) {
        binding?.tvTitle?.text = text_string.toString()

    }
    fun setVisible(boolean: Boolean) {
        if (boolean){
            binding?.close?.visibility = View.VISIBLE
        }else{
            binding?.close?.visibility = View.GONE
        }


    }

}