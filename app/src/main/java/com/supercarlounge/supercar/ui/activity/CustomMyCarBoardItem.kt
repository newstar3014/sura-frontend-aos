package com.supercarlounge.supercar.ui.activity

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
import com.supercarlounge.supercar.viewmodel.MyCarBoardWritingViewModel

class CustomMyCarBoardItem : LinearLayout {

    var binding:ItemMySubInfo?= null
    var mViewModel :MyCarBoardWritingViewModel? = null

    constructor(context: Context?,viewModel: MyCarBoardWritingViewModel,) : super(context) {
        initView(viewModel)

    }


    constructor(context: Context?,viewModel: MyCarBoardWritingViewModel, attrs: AttributeSet?) : super(context, attrs) {
        initView(viewModel)
        getAttrs(attrs)
        setOnClickListeners()
    }

    constructor(context: Context?,viewModel: MyCarBoardWritingViewModel, attrs: AttributeSet?, defStyle: Int) : super(context, attrs) {
        initView(viewModel)
        getAttrs(attrs, defStyle)
        setOnClickListeners()
    }

    private fun setOnClickListeners() {

    }

    init { // inflate binding and add as view


    }

    private fun initView(viewModel:MyCarBoardWritingViewModel) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        binding = ItemMySubInfo.inflate(inflater)
        binding = ItemMySubInfo.inflate(LayoutInflater.from(context), this, true)
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

    fun setGlide(src: String) {
        Glide.with(binding!!.ivImage).load(src).into(binding!!.ivImage)
    }

    fun setText(text_string: String?) {
        binding?.edtMsiContents?.setText (  text_string.toString())

    }
    fun setBackGround(check : Boolean?) {
        Log.d("CustomMyCarBoardItem : setBackGround", "$check")
        if (check == true){
            binding?.llMsiContents!!.background = ContextCompat.getDrawable(context,R.drawable.coner_round_edit_strok_s_bg)
        }else{
            binding?.llMsiContents!!.background = ContextCompat.getDrawable(context,R.drawable.coner_round_edit_strok_bg)
        }

    }

}