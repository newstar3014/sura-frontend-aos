package com.supercarlounge.supercar.adapter

import android.view.ViewGroup
import androidx.fragment.app.*
import com.supercarlounge.supercar.data.RecentDriveData
import com.supercarlounge.supercar.ui.fragment.pagermatching.*

class RecentListPagerAdapter(fm: FragmentManager,val rdata:RecentDriveData?, var passCheck :Boolean) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    var recentViewFragment: RecentViewFragment? = null
    var recentLikeFragment: RecentLikeFragment? = null
    var recentConfirmFragment :RecentConfirmFragment? = null

    override fun getCount(): Int {
        return 3
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val createdFragment = super.instantiateItem(container, position) as Fragment
        when (position) {
            0 -> recentViewFragment = createdFragment as RecentViewFragment
            1 -> recentLikeFragment = createdFragment as RecentLikeFragment
            3 -> recentConfirmFragment = createdFragment as RecentConfirmFragment
        }
        return createdFragment
    }

    override fun getItem(position: Int): Fragment {

        return when(position){
            0-> RecentViewFragment().newInstance(rdata,passCheck)
            1-> RecentLikeFragment().newInstance(rdata,passCheck)
            else->RecentConfirmFragment().newInstance(rdata,passCheck)
        }
    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }

}