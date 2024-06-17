package com.supercarlounge.supercar.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import android.view.WindowManager
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.AlimAdapter
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.databinding.ActivityAlimBinding
import com.supercarlounge.supercar.databinding.ActivityDrivePassGuideBinding
import com.supercarlounge.supercar.viewmodel.AlimViewModel


class DrivePassGuideActivity: BaseActivitiy() {

    private lateinit var binding: ActivityDrivePassGuideBinding
    private lateinit var viewModel: AlimViewModel
    private lateinit var  adapter : AlimAdapter
    var application:MainApplication? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(AlimViewModel::class.java)
        binding = ActivityDrivePassGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)
        application = applicationContext as MainApplication
        viewModel.my_seq.value = application!!.userData!!.u_seq.toString()
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel
        clickListeners()
        observes()

    }

    override fun onResume() {
        super.onResume()

    }
    private fun observes() {


    }

    private fun clickListeners() {
        binding!!.ivBack.setOnClickListener {
            this.finish()
        }
    }
    override fun finish() {
        super.finish()
        overridePendingTransition(com.supercarlounge.supercar.R.anim. fadein, com.supercarlounge.supercar.R.anim.fadeout)
    }
}
