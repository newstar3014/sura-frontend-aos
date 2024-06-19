package com.supercarlounge.supercar.ui.activity

import SecessionDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import com.supercarlounge.supercar.databinding.ActivityServuceCenterBinding
import com.supercarlounge.supercar.viewmodel.ServiceCenterViewModel
import com.supercarlounge.supercar.BuildConfig
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.databinding.ActivityProfileManagementBinding

class ProfileManagementActivity: BaseActivitiy() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityProfileManagementBinding

    private var application :MainApplication? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)

        clickListeners()
        application = applicationContext as MainApplication

    }

    override fun onResume() {
        super.onResume()
    }

    private fun clickListeners() {
        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.llRest.setOnClickListener {
            // 휴면 신청으로 이동
            val i = Intent(this, UserRest1Activity::class.java)
            startActivity(i)
        }

        binding.llWithdraw.setOnClickListener {
            // 탈퇴 신청으로 이동
            val i = Intent(this, UserWithdraw1Activity::class.java)
            startActivity(i)
        }
    }

    override fun finish() {
        super.finish()
    }
}
