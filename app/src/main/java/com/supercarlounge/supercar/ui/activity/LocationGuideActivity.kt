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
import androidx.fragment.app.FragmentManager
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
import com.supercarlounge.supercar.dialog.QuickUserDialog
import com.supercarlounge.supercar.enumset.GuidePopType
import com.supercarlounge.supercar.enumset.QuickTypeStatus

class LocationGuideActivity : BaseActivitiy() {

    private lateinit var binding: AcitivtyLocationGuideBinding
    private lateinit var viewModel: LocationWritingViewModel
    var application: MainApplication? = null
    private var quickUserDialog: QuickUserDialog? = null
    var fm: FragmentManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LocationWritingViewModel::class.java)
        binding = AcitivtyLocationGuideBinding.inflate(layoutInflater)
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

    private fun showQuickUserDialog() {
        quickUserDialog = QuickUserDialog(this, QuickTypeStatus.QUICK_WARNING) { type, okcancel ->
            if (okcancel) {
                quickUserDialog!!.dismiss()
                val i = Intent(this, JoinActivity::class.java)
                i.putExtra("qickview", "퀵유저")
                i.putExtra("u_name", application!!.userBasicData?.u_name)
                i.putExtra("u_gender", application!!.userBasicData?.u_gender)
                i.putExtra("u_birthday", application!!.userBasicData?.u_birthday)
                i.putExtra("u_phone", application!!.userBasicData?.u_phone)
                startActivity(i)
            } else {
                quickUserDialog!!.dismiss()
            }
        }
        quickUserDialog!!.show(fm!!, "")
    }

    private fun clickListeners() {
        binding!!.ivBack.setOnClickListener {
            finish()
        }
        binding.bottombar?.setOnClickListener {
            if (application!!.quick_view.equals("퀵유저")) {
                showQuickUserDialog()
                Log.d("하이", "setobserver: ")
            }else{
                var intent = Intent(this, LocationWritingActivity::class.java)
                startActivity(intent)
            }
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
