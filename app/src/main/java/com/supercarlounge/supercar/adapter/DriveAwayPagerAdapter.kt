package com.supercarlounge.supercar.adapter

import android.view.ViewGroup
import androidx.fragment.app.*
import com.supercarlounge.supercar.ui.fragment.*

class DriveAwayPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    var driveAwayNewFragment: DriveAwayNewFragment? = null
    var driveAwayNearFragment: DriveAwayNearFragment? = null
    var driveAwayHotFragment: DriveAwayHotFragment? = null
    var type = 0
    override fun getCount(): Int {
        return 3
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val createdFragment = super.instantiateItem(container, position) as Fragment

        when (position) {

            0 -> {
                driveAwayNewFragment = createdFragment as DriveAwayNewFragment
            }
            1->{
                driveAwayHotFragment = createdFragment as DriveAwayHotFragment
            }
            else -> {
                driveAwayNearFragment = createdFragment as DriveAwayNearFragment


            }

        }
        return createdFragment
    }
    fun getfragment(postion: Int): Fragment {
        var frag: Fragment? = null
        when (postion) {
            0 -> {
                if (driveAwayNewFragment != null) {
                    frag = driveAwayNewFragment as Fragment
                } else {
                    frag = DriveAwayNewFragment().newInstance(type)
                }
            }
            1->{
                if (driveAwayHotFragment != null) {
                    frag = driveAwayHotFragment as Fragment
                } else {
                    frag = DriveAwayHotFragment().newInstance(type)
                }
            }
            else -> {
                if (driveAwayNearFragment != null) {
                    frag = driveAwayNearFragment as Fragment
                } else {
                    frag = DriveAwayNearFragment().newInstance(type)
                }

            }

        }
        return frag!!
    }
    fun changeviewtype(nowposition:Int,type :Int){
        if(nowposition == 0){
           // if(driveAwayNewFragment !=null){
//                (getfragment(0) as DriveAwayNewFragment) .changeviewtype(type)
           // }
        }else if(nowposition == 1){
//            (getfragment(1) as DriveAwayHotFragment).changeviewtype(type)
        }
        else{
           // if(driveAwayHotFragment !=null){

//            (getfragment(2) as DriveAwayNearFragment) .changeviewtype(type)
           // }
        }
    }

    fun changePost(nowposition:Int){
        if(nowposition == 0){
            // if(driveAwayNewFragment !=null){
            (getfragment(0) as DriveAwayNewFragment).changePost()
            // }
        }else if(nowposition == 1){
            (getfragment(1) as DriveAwayHotFragment).changePost()
        }
        else{
            // if(driveAwayHotFragment !=null){

            (getfragment(2) as DriveAwayNearFragment) .changePost()
            // }
        }
    }
//
//    fun changeScrollFocus(nowposition:Int,type :Boolean){
//        if(nowposition == 0){
//            // if(driveAwayNewFragment !=null){
//            (getfragment(0) as DriveAwayNewFragment) .changeScrollFocus(type)
//            // }
//        }else if(nowposition == 1){
//            (getfragment(1) as DriveAwayHotFragment).changeScrollFocus(type)
//        }
//        else{
//            // if(driveAwayHotFragment !=null){
//
//            (getfragment(2) as DriveAwayNearFragment) .changeScrollFocus(type)
//            // }
//        }
//    }

    override fun getItem(position: Int): Fragment {

        return when(position){

            0-> DriveAwayNewFragment().newInstance(type)
            1->DriveAwayHotFragment().newInstance(type)
            else->  DriveAwayNearFragment().newInstance(type)

        }

    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }

}