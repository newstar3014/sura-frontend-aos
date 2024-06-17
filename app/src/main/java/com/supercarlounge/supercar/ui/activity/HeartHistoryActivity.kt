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
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.AlimAdapter
import com.supercarlounge.supercar.adapter.HeartLogAdapter
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.databinding.ActivityAlimBinding
import com.supercarlounge.supercar.databinding.ActivityPopInfoBinding
import com.supercarlounge.supercar.databinding.FragmentHeartsShopBuyListBinding
import com.supercarlounge.supercar.viewmodel.AlimViewModel
import com.supercarlounge.supercar.viewmodel.HeartsShopViewModel
import com.supercarlounge.supercar.viewmodel.PopInfoViewModel


class HeartHistoryActivity: BaseActivitiy() {

    private var binding: FragmentHeartsShopBuyListBinding?= null
    private var viewModel: HeartsShopViewModel?= null
    var  adapter : HeartLogAdapter? = null
    var application:MainApplication? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentHeartsShopBuyListBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        binding!!.setLifecycleOwner(this)
        viewModel = ViewModelProvider(this).get(HeartsShopViewModel::class.java)
        binding!!.viewModel = viewModel
        clickListeners()
        observes()
        application = applicationContext as MainApplication
        viewModel?.my_seq?.value  =application!!.userData?.u_seq.toString()
        adapter = HeartLogAdapter(this, arrayListOf()) { data, position ->


        }

        binding!!.rvDriveHeartBuyList.adapter = adapter
        binding!!.rvDriveHeartBuyList.layoutManager =  LinearLayoutManager(this)
        binding!!.rvDriveHeartBuyList.setHasFixedSize(true)
        viewModel?.GetHeartLog()

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
