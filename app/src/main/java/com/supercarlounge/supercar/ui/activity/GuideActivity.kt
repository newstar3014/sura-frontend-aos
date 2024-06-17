package com.supercarlounge.supercar.ui.activity


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import android.view.WindowManager
import androidx.navigation.ui.AppBarConfiguration
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.databinding.ActivityGuideBinding
import com.supercarlounge.supercar.viewmodel.StartViewModel




class GuideActivity : BaseActivitiy() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityGuideBinding
    private lateinit var viewModel: StartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        with(window) {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            // set an slide transition
            enterTransition = Slide(Gravity.END)
            exitTransition = Slide(Gravity.START)
        }
        viewModel = ViewModelProvider(this).get(StartViewModel::class.java)
        binding = ActivityGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding!!.viewModel = viewModel
        binding!!.setLifecycleOwner(this)
        var i = intent
        var type = i.getStringExtra("type").toString()

        if (type.equals("null")){
            binding.llBottomBar.visibility = View.INVISIBLE
        }else{
            binding.llBottomBar.visibility = View.VISIBLE

        }

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.llBottomBar.setOnClickListener {
            var i = Intent()
            i.putExtra("type", type)
            this.setResult(Activity.RESULT_OK, i)
            this.finish()
        }
    }




    override fun onPause() {
        super.onPause()
      //  overridePendingTransition(R.anim. fadein, R.anim.fadeout)
    }
    override fun finish() {
        super.finish()
     //   overridePendingTransition(com.supercarlounge.supercar.R.anim. fadein, com.supercarlounge.supercar.R.anim.fadeout)
    }
}
