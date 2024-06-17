package com.supercarlounge.supercar.adapter

import android.view.ViewGroup
import androidx.fragment.app.*
import com.supercarlounge.supercar.ui.fragment.pagermatching.*

class MatchingPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    var matchingCrushFragment: MatchingCrushFragment? = null
    var matchingDoingFragment: MatchingDoingFragment? = null
    var matchingCompletionFragment :MatchingCompletionFragment? = null

    override fun getCount(): Int {
        return 3
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val createdFragment = super.instantiateItem(container, position) as Fragment
        when (position) {
            0 -> matchingCrushFragment = createdFragment as MatchingCrushFragment
            1 -> matchingDoingFragment = createdFragment as MatchingDoingFragment
            3 -> matchingCompletionFragment = createdFragment as MatchingCompletionFragment
        }
        return createdFragment
    }

    override fun getItem(position: Int): Fragment {

        return when(position){
            0-> MatchingCrushFragment().newInstance()
            1-> MatchingDoingFragment().newInstance()
            else->MatchingCompletionFragment().newInstance()
        }
    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }

}