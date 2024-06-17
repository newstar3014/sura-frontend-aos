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
import com.supercarlounge.supercar.customview.BaseActivitiy

class HeartsShopActivity: BaseActivitiy() {
    var application:MainApplication?=null
    private lateinit var binding: ActivityHeartsShopBinding
    private lateinit var viewModel: HeartsShopViewModel
    var pageradapter : HeartsShopPagerAdapter? =null
    var fm: FragmentManager? =  null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(HeartsShopViewModel::class.java)
        binding = ActivityHeartsShopBinding.inflate(layoutInflater)
        application = applicationContext as MainApplication
        viewModel.my_seq.value  =application!!.userData?.u_seq.toString()
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel

        viewModel.titleText.value = "HEARTS SHOP"
        clickListeners()
        observes()
        setAdapters()
        SetTabAndViewPager()

       var index = intent.getIntExtra("index",0)
        binding!!.pager.currentItem = index
    }

    override fun onResume() {
        super.onResume()
    viewModel.GetMyHeart()
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

        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("하트 구매"))
        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("무제한 하트 받기"))
        binding!!.pager.offscreenPageLimit = 1


    }


    private fun setAdapters() {
        fm = supportFragmentManager
        pageradapter =  HeartsShopPagerAdapter(fm!!)
        binding!!.pager.adapter = pageradapter
    }

    private fun observes() {

    }

    private fun clickListeners() {

        binding!!.ivBack.setOnClickListener {
            finish()
        }
        binding.heartList.setOnClickListener {
            val i = Intent(this, HeartHistoryActivity::class.java)
                startActivity(i)
          //  overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

    }

    override fun finish() {
        super.finish()
      //  overridePendingTransition(com.supercarlounge.supercar.R.anim. fadein, com.supercarlounge.supercar.R.anim.fadeout)
    }
}
