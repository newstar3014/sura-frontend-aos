package com.supercarlounge.supercar.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.supercarlounge.supercar.adapter.HeartsShopPagerAdapter
import com.supercarlounge.supercar.databinding.ActivityHeartsShopBinding
import com.supercarlounge.supercar.viewmodel.HeartsShopViewModel
import android.view.WindowManager
import com.google.android.material.tabs.TabLayout
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.adapter.InformationUsePagerAdapter
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.databinding.ActivityInformationUseBinding
import com.supercarlounge.supercar.viewmodel.InformationUseViewModel

class InformationUseActivity: BaseActivitiy() {
    var application:MainApplication?=null
    private lateinit var binding: ActivityInformationUseBinding
    private lateinit var viewModel: InformationUseViewModel
    var pageradapter : InformationUsePagerAdapter? =null
    var fm: FragmentManager? =  null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(InformationUseViewModel::class.java)
        binding = ActivityInformationUseBinding.inflate(layoutInflater)
        application = applicationContext as MainApplication
        viewModel.my_seq.value  =application!!.userData?.u_seq.toString()
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel

        viewModel.titleText.value = "슈라 이용 안내"
        clickListeners()
        observes()
        setAdapters()
        SetTabAndViewPager()

       var index = intent.getIntExtra("index",0)
        binding!!.pager.currentItem = index
    }

    override fun onResume() {
        super.onResume()

    }
    private fun SetTabAndViewPager() {


        binding!!.pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding!!.tabLayout))
        binding!!.tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                var position = tab!!.position
                binding!!.pager.currentItem = position
                tab?.position?.let { binding.pager.setCurrentItem(it, false) }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        binding!!.pager.setPagingEnabled(true)

        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("슈라 소개"))
        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("드라이브 게시판"))
        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("익명 게시판"))
        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("드라이브 패스권"))
        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("하트 상점"))
        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("로케이션"))
        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("컨시어지"))
        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("포인트물"))
        binding!!.pager.offscreenPageLimit = 1


    }


    private fun setAdapters() {
        fm = supportFragmentManager
        pageradapter =  InformationUsePagerAdapter(fm!!)
        binding!!.pager.adapter = pageradapter
    }

    private fun observes() {

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
}
