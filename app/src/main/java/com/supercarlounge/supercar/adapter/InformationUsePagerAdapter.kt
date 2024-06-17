package com.supercarlounge.supercar.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.supercarlounge.supercar.data.AllianceData
import com.supercarlounge.supercar.databinding.ItemAllianceBinding
import com.supercarlounge.supercar.ui.fragment.HeartsShopBuyFragment
import com.supercarlounge.supercar.ui.fragment.HeartsShopBuyListFragment
import com.supercarlounge.supercar.ui.fragment.PagerInformationUse.*


class InformationUsePagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    var iUAboutShuraFragment: IUAboutShuraFragment? = null
    var iuDriveForumFragment: IUDriveForumFragment? = null
    var iUAnonymousForumFragment: IUAnonymousForumFragment? = null
    var iUDrivePassFragment: IUDrivePassFragment? = null
    var iUHeartShopFragment: IUHeartShopFragment? = null
    var iUMyCarFragment: IUMyCarFragment? = null
    var iUConciergeFragment: IUConciergeFragment? = null
    var iUPointFragment: IUPointFragment? = null
    override fun getCount(): Int {
        return 8
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val createdFragment = super.instantiateItem(container, position) as Fragment
        when (position) {
            0 -> iUAboutShuraFragment = createdFragment as IUAboutShuraFragment
            1 -> iuDriveForumFragment = createdFragment as IUDriveForumFragment
            2 -> iUAnonymousForumFragment = createdFragment as IUAnonymousForumFragment
            3 -> iUDrivePassFragment = createdFragment as IUDrivePassFragment
            4 -> iUHeartShopFragment = createdFragment as IUHeartShopFragment
            5 -> iUMyCarFragment = createdFragment as IUMyCarFragment
            6 -> iUConciergeFragment = createdFragment as IUConciergeFragment
            7 -> iUPointFragment = createdFragment as IUPointFragment

        }
        return createdFragment
    }

    override fun getItem(position: Int): Fragment {

        return when(position){
            0-> IUAboutShuraFragment().newInstance("")
            1-> IUDriveForumFragment().newInstance("")
            2-> IUAnonymousForumFragment().newInstance("")
            3-> IUDrivePassFragment().newInstance("")
            4-> IUHeartShopFragment().newInstance("")
            5-> IUMyCarFragment().newInstance("")
            6-> IUConciergeFragment().newInstance("")
            else-> IUPointFragment().newInstance("")
        }
    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }


}