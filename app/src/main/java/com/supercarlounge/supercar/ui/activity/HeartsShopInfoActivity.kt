package com.supercarlounge.supercar.ui.activity

import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import android.view.WindowManager
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.databinding.ActivityHeartsShopInfoBinding
import com.supercarlounge.supercar.viewmodel.HeartsShopViewModel

class





































HeartsShopInfoActivity: BaseActivitiy() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHeartsShopInfoBinding
    private lateinit var viewModel: HeartsShopViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        with(window) {
//            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
//            // set an slide transition
//            enterTransition = Slide(Gravity.END)
//            exitTransition = Slide(Gravity.START)
//        }
        viewModel = ViewModelProvider(this).get(HeartsShopViewModel::class.java)
        binding = ActivityHeartsShopInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel

//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        val i= intent
        var type =i.getIntExtra("viewType",0)
        setText(type)
        clickListeners()
        observes()

    }

    private fun observes() {

    }

    private fun clickListeners() {
        binding.tvOk.setOnClickListener {
            this.finish()
        }
        binding!!.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun setText(type :Int){
        if (type == 0){
            viewModel.subTitleText.value = "하트 구매 이용 약관"
            viewModel.GetTerm("구매약관")
        }else{
            viewModel.subTitleText.value = "취소 및 이용 약관"
            viewModel.GetTerm("취소환불")
        }
    }

    override fun finish() {
        super.finish()
      //  overridePendingTransition(com.supercarlounge.supercar.R.anim. fadein, com.supercarlounge.supercar.R.anim.fadeout)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val focusView = currentFocus
        if (focusView != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev.x.toInt()
            val y = ev.y.toInt()
            if (!rect.contains(x, y)) {
                val imm: InputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                if (imm != null) imm.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}
