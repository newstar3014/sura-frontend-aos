package com.supercarlounge.supercar.ui.activity

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.adapter.MatchingPagerAdapter
import com.supercarlounge.supercar.databinding.ActivityMatchingBinding
import com.supercarlounge.supercar.viewmodel.MatchingViewModel
import com.google.android.material.tabs.TabLayout
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.customview.BaseActivitiy

class MatchingActivity: BaseActivitiy() {

    private lateinit var binding: ActivityMatchingBinding
    private lateinit var viewModel: MatchingViewModel
    var pageradapter : MatchingPagerAdapter? =null
    var fm: FragmentManager? =  null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(MatchingViewModel::class.java)
        binding = ActivityMatchingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel

        viewModel.titleText.value = "MATCHING"
        clickListeners()
        observes()
        setAdapters()
        SetTabAndViewPager()

        Glide.with(binding!!.ivHeart).load(R.raw.heart_gif).override(200,200).centerInside().into(binding!!.ivHeart)

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

        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("받은 호감"))
        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("보낸 호감"))
        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("매칭 완료"))
        binding!!.pager.offscreenPageLimit = 1
        binding!!.pager.setPagingEnabled(true)

    }


    private fun setAdapters() {
        fm = supportFragmentManager
        pageradapter =  MatchingPagerAdapter(fm!!)
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
        overridePendingTransition(com.supercarlounge.supercar.R.anim. fadein, com.supercarlounge.supercar.R.anim.fadeout)
    }
}
