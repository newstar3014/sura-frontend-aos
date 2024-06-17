package com.supercarlounge.supercar.adapter

import android.view.ViewGroup
import androidx.fragment.app.*
import com.supercarlounge.supercar.ui.fragment.*

class HeartsShopPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    var heartsShopBuyFragment: HeartsShopBuyFragment? = null
    var heartsShopBuyListFragment: HeartsShopBuyListFragment? = null
    override fun getCount(): Int {
        return 2
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val createdFragment = super.instantiateItem(container, position) as Fragment
        when (position) {
            0 -> heartsShopBuyFragment = createdFragment as HeartsShopBuyFragment
            1 -> heartsShopBuyListFragment = createdFragment as HeartsShopBuyListFragment
        }
        return createdFragment
    }

    override fun getItem(position: Int): Fragment {

        return when(position){
            0-> HeartsShopBuyFragment().newInstance()
            else->HeartsShopBuyListFragment().newInstance()
        }
    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }

}