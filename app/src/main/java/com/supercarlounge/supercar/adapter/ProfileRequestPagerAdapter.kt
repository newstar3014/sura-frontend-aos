package com.supercarlounge.supercar.adapter

import android.view.ViewGroup
import androidx.fragment.app.*
import com.supercarlounge.supercar.ui.fragment.*

class ProfileRequestPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    var profileRequest1Fragment: ProfileRequest1Fragment? = null
    var profileRequest2Fragment: ProfileRequest2Fragment? = null
    var profileRequest3Fragment: ProfileRequest3Fragment? = null
    var type = 0
    override fun getCount(): Int {
        return 3
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val createdFragment = super.instantiateItem(container, position) as Fragment

        when (position) {

            0 -> {
                profileRequest1Fragment = createdFragment as ProfileRequest1Fragment
            }
            1->{
                profileRequest2Fragment = createdFragment as ProfileRequest2Fragment
            }
            else -> {
                profileRequest3Fragment = createdFragment as ProfileRequest3Fragment

            }

        }
        return createdFragment
    }
    fun getfragment(postion: Int): Fragment {
        var frag: Fragment? = null
        when (postion) {
            0 -> {
                if (profileRequest1Fragment != null) {
                    frag = profileRequest1Fragment as Fragment
                } else {
                    frag = ProfileRequest1Fragment().newInstance()
                }
            }
            1->{
                if (profileRequest2Fragment != null) {
                    frag = profileRequest2Fragment as Fragment
                } else {
                    frag = ProfileRequest2Fragment().newInstance()
                }
            }
            else -> {
                if (profileRequest3Fragment != null) {
                    frag = profileRequest3Fragment as Fragment
                } else {
                    frag = ProfileRequest3Fragment().newInstance()
                }
            }

        }
        return frag!!
    }


    override fun getItem(position: Int): Fragment {

        return when(position){

            0-> ProfileRequest1Fragment().newInstance()
            1->ProfileRequest2Fragment().newInstance()
            else->ProfileRequest3Fragment().newInstance()

        }

    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }

}