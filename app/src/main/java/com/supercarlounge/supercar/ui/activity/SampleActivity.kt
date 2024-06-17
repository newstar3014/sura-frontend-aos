package com.supercarlounge.supercar.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.DriveAwayData
import com.supercarlounge.supercar.databinding.*
import com.supercarlounge.supercar.viewmodel.*
import com.github.dhaval2404.imagepicker.ImagePicker
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.dialog.DriveExposureDialog
import com.supercarlounge.supercar.dialog.DriveRegiDialog
import com.supercarlounge.supercar.dialog.GuideDialog
import com.supercarlounge.supercar.enumset.GuidePopType

class SampleActivity : BaseActivitiy() {

    private lateinit var binding: ActivityLocationWritingBinding
    private lateinit var viewModel: LocationWritingViewModel
    var application: MainApplication? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LocationWritingViewModel::class.java)
        binding = ActivityLocationWritingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel
        application = applicationContext as MainApplication


        var data: DriveAwayData? = intent.getParcelableExtra("data")



        clickListeners()
        observes()

    }


    private fun observes() {
        viewModel.istoast.observe(this, Observer {
            if (viewModel.isstart.value!!) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            } else {
                viewModel.isstart.value = true
            }
        })







    }

    override fun onResume() {
        super.onResume()

    }

    private fun clickListeners() {
        binding!!.ivBack.setOnClickListener {
            finish()
        }
    }

    var resultLauncherimage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                if (data != null) {

                    data!!.data.let { uri ->
                        if (uri != null) {

                        }
                    }
                }

            }
        }

    override fun finish() {
        super.finish()
     //   overridePendingTransition(
     //       com.supercarlounge.supercar.R.anim.fadein,
     //       com.supercarlounge.supercar.R.anim.fadeout
     //   )
    }

    companion object {
        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("setimagedrive")
        fun setimagedrive(view: ImageView, url: String?) {

        }
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
