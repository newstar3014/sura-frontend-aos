package com.supercarlounge.supercar.ui.priview.adapter

import android.view.ViewGroup
import androidx.fragment.app.*
import com.supercarlounge.supercar.ui.priview.fragment.drive.PrvDriveAwayHotFragment
import com.supercarlounge.supercar.ui.priview.fragment.drive.PrvDriveAwayNearFragment
import com.supercarlounge.supercar.ui.priview.fragment.drive.PrvDriveAwayNewFragment

class PrvDriveAwayPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    var prvdriveAwayNewFragment: PrvDriveAwayNewFragment? = null
    var prvdriveAwayNearFragment: PrvDriveAwayNearFragment? = null
    var prvdriveAwayHotFragment: PrvDriveAwayHotFragment? = null
    var type = 0
    override fun getCount(): Int {
        return 3
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val createdFragment = super.instantiateItem(container, position) as Fragment

        when (position) {

            0 -> {
                prvdriveAwayNewFragment = createdFragment as PrvDriveAwayNewFragment
            }
            1->{
                prvdriveAwayHotFragment = createdFragment as PrvDriveAwayHotFragment
            }
            else -> {
                prvdriveAwayNearFragment = createdFragment as PrvDriveAwayNearFragment


            }

        }
        return createdFragment
    }
    fun getfragment(postion: Int): Fragment {
        var frag: Fragment? = null
        when (postion) {
            0 -> {
                if (prvdriveAwayNewFragment != null) {
                    frag = prvdriveAwayNewFragment as Fragment
                } else {
                    frag = PrvDriveAwayNewFragment().newInstance(type)
                }
            }
            1->{
                if (prvdriveAwayHotFragment != null) {
                    frag = prvdriveAwayHotFragment as Fragment
                } else {
                    frag = PrvDriveAwayHotFragment().newInstance(type)
                }
            }
            else -> {
                if (prvdriveAwayNearFragment != null) {
                    frag = prvdriveAwayNearFragment as Fragment
                } else {
                    frag = PrvDriveAwayNearFragment().newInstance(type)
                }

            }

        }
        return frag!!
    }
    fun changeviewtype(nowposition:Int,type :Int){
        if(nowposition == 0){
           // if(driveAwayNewFragment !=null){
//                (getfragment(0) as PrvDriveAwayNewFragment) .changeviewtype(type)
           // }
        }else if(nowposition == 1){
//            (getfragment(1) as PrvDriveAwayHotFragment).changeviewtype(type)
        }
        else{
           // if(driveAwayHotFragment !=null){
//            (getfragment(2) as PrvDriveAwayNearFragment) .changeviewtype(type)

           // }
        }
    }

    override fun getItem(position: Int): Fragment {

        return when(position){

            0-> PrvDriveAwayNewFragment().newInstance(type)
            1->PrvDriveAwayHotFragment().newInstance(type)
            else->PrvDriveAwayNearFragment().newInstance(type)

        }

    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }

}