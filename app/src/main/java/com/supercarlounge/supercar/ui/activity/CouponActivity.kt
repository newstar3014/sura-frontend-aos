package com.supercarlounge.supercar.ui.activity

import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.databinding.ActivityCouponBinding
import com.supercarlounge.supercar.viewmodel.CouponViewModel

class CouponActivity: BaseActivitiy() {

    var application:MainApplication?=null
    private lateinit var binding: ActivityCouponBinding
    private lateinit var viewModel: CouponViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(CouponViewModel::class.java)
        binding = ActivityCouponBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel
        application = applicationContext as MainApplication
        viewModel.my_seq.value = application!!.userData!!.u_seq.toString()
        clickListeners()
        observes()



    }
    private fun observes() {
        viewModel.toast.observe(this,Observer{ toast->
            if(!toast.isNullOrEmpty()){
                Toast.makeText(this,toast,Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.isfinish.observe(this,Observer{
            if(it) {
                finish()
            }
        })
    }

    private fun clickListeners() {

        binding!!.ivBack.setOnClickListener {
            finish()
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
