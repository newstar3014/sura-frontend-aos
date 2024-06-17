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
import com.google.android.material.tabs.TabLayout
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.Network.MatchingService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.AlimAdapter
import com.supercarlounge.supercar.adapter.AlimPagerAdapter
import com.supercarlounge.supercar.adapter.HeartsShopPagerAdapter
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.data.ResultData
import com.supercarlounge.supercar.databinding.ActivityAlimBinding
import com.supercarlounge.supercar.viewmodel.AlimViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response


class AlimActivity: BaseActivitiy() {

    private lateinit var binding: ActivityAlimBinding
    private lateinit var viewModel: AlimViewModel
    private lateinit var  pageradapter :AlimPagerAdapter
    var application:MainApplication? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(AlimViewModel::class.java)
        binding = ActivityAlimBinding.inflate(layoutInflater)
        setContentView(binding.root)
        application = applicationContext as MainApplication
        viewModel.my_seq.value = application!!.userData!!.u_seq.toString()
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel
        clickListeners()


        var push = intent.getBooleanExtra("push",true)
        if (!push){
            viewModel.toast.value ="드라이브 패스권을 구매 후 확인하실 수 있습니다"
        }

        SetTabAndViewPager()
        setAdapters()
        observes()
    }
    private fun SetTabAndViewPager() {


        binding!!.pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding!!.tabLayout))
        binding!!.tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                var position = tab!!.position
                binding!!.pager.currentItem = position
                viewModel.notipage.value =position
                tab?.position?.let { binding.pager.setCurrentItem(it, false) }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        binding!!.pager.setPagingEnabled(true)

        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("공지"))
        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("활동"))
        binding!!.pager.offscreenPageLimit = 1
        binding!!.tabLayout.selectTab(binding!!.tabLayout.getTabAt(1),true)


    }


    private fun setAdapters() {
        pageradapter =  AlimPagerAdapter(supportFragmentManager)
        binding.pager.adapter = pageradapter
        binding.pager.setCurrentItem(1, false)
    }
    override fun onResume() {
        super.onResume()
    }
    private fun observes() {
        viewModel.toast.observe(this, Observer { toast->
            if(!toast.isNullOrEmpty()){
                Toast.makeText(this,toast,Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun clickListeners() {
        binding!!.ivBack.setOnClickListener {
            this.finish()
        }
        binding.tvAllDelete.setOnClickListener {
            pageradapter.allDelet()
        }
    }
    override fun finish() {
        super.finish()
       // overridePendingTransition(com.supercarlounge.supercar.R.anim. fadein, com.supercarlounge.supercar.R.anim.fadeout)
    }

}
