package com.supercarlounge.supercar.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.fragment.app.*
import com.supercarlounge.supercar.ui.fragment.Alim1Fragment
import com.supercarlounge.supercar.ui.fragment.Alim2Fragment
import com.supercarlounge.supercar.ui.fragment.pageranonymousforumf.*

class AlimPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    var alim1Fragment: Alim1Fragment? = null
    var alim2Fragment: Alim2Fragment? = null


    override fun getCount(): Int {
        return 2
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val createdFragment = super.instantiateItem(container, position) as Fragment
        when (position) {
            0 -> alim1Fragment = createdFragment as Alim1Fragment
            1 -> alim2Fragment = createdFragment as Alim2Fragment
            else -> alim1Fragment = createdFragment as Alim1Fragment

        }
        return createdFragment
    }

    override fun getItem(position: Int): Fragment {

        return when(position){
            0->  Alim1Fragment().newInstance()
            1-> Alim2Fragment().newInstance()
            else-> Alim1Fragment().newInstance()



        }
    }
    fun allDelet(){
        (getfragment(1) as Alim2Fragment) .allDelet()
    }


    fun getfragment(postion: Int): Fragment {
        var frag: Fragment? = null
        when (postion) {
            0 -> {
                if (alim1Fragment != null) {
                    frag = alim1Fragment as Fragment
                } else {
                    frag = Alim1Fragment().newInstance()
                }
            }
            1 -> {
                if (alim2Fragment != null) {
                    frag = alim2Fragment as Fragment
                } else {
                    frag = Alim2Fragment().newInstance()
                }
            }
            else -> {
                if (alim1Fragment != null) {
                    frag = alim1Fragment as Fragment
                } else {
                    frag = Alim1Fragment().newInstance()
                }
            }

        }
        return frag!!
    }
    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }

}