package com.supercarlounge.supercar.adapter

import android.view.ViewGroup
import androidx.fragment.app.*
import com.supercarlounge.supercar.ui.fragment.*

class LocationPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    var locationAllFragment: LocationAllFragment? = null
    var locationCourseFragment: LocationCourseFragment? = null
    var locationFinediningFragment: LocationFinediningFragment? = null
    var locationFoodFragment: LocationFoodFragment? = null
    var locationCafeFragment: LocationCafeFragment? = null
    var locationRecommendationFragment :LocationRecommendationFragment? =null
    var type = 0
    override fun getCount(): Int {
        return 6
    }
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val createdFragment = super.instantiateItem(container, position) as Fragment

        when (position) {

            0 -> {
                locationAllFragment = createdFragment as LocationAllFragment
            }
            1->{
                locationRecommendationFragment = createdFragment as LocationRecommendationFragment
            }
            2->{
                locationCafeFragment = createdFragment as LocationCafeFragment
            }
            3->{

                locationFoodFragment = createdFragment as LocationFoodFragment
            }
            4->{
                locationCourseFragment = createdFragment as LocationCourseFragment
            }
            else -> {
                locationFinediningFragment = createdFragment as LocationFinediningFragment



            }

        }
        return createdFragment
    }
    fun getfragment(postion: Int): Fragment {
        var frag: Fragment? = null
        when (postion) {
            0 -> {
                if (locationAllFragment != null) {
                    frag = locationAllFragment as Fragment
                } else {
                    frag = LocationAllFragment().newInstance(type)
                }
            }
            1->{
                if (locationRecommendationFragment != null) {
                    frag = locationRecommendationFragment as Fragment
                } else {
                    frag = LocationRecommendationFragment().newInstance(type)
                }

            }
            2->{
                if (locationCafeFragment != null) {
                    frag = locationCafeFragment as Fragment
                } else {
                    frag = LocationCafeFragment().newInstance(type)
                }

            }
           3->{
                if (locationFoodFragment != null) {
                    frag = locationFoodFragment as Fragment
                } else {
                    frag = LocationFoodFragment().newInstance(type)
                }
            }
            4->{
                if (locationCourseFragment != null) {
                    frag = locationCourseFragment as Fragment
                } else {
                    frag = LocationCourseFragment().newInstance(type)
                }
            }
            else -> {
                if (locationFinediningFragment != null) {
                    frag = locationFinediningFragment as Fragment
                } else {
                    frag = LocationFinediningFragment().newInstance(type)
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

//    fun changeScroll(nowposition:Int,type :Boolean){
//        if(nowposition == 0){
//            // if(driveAwayNewFragment !=null){
//            (getfragment(0) as DriveAwayNewFragment).changeScroll()
//            // }
//        }else if(nowposition == 1){
//            (getfragment(1) as DriveAwayHotFragment).changeScroll()
//        }
//        else{
//            // if(driveAwayHotFragment !=null){
//
//            (getfragment(2) as DriveAwayNearFragment) .changeScroll()
//            // }
//        }
//    }
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

            0-> LocationAllFragment().newInstance(type)
            1->LocationRecommendationFragment().newInstance(type)
            2->LocationCafeFragment().newInstance(type)
            3->LocationFoodFragment().newInstance(type)
            4->LocationCourseFragment().newInstance(type)
            else->  LocationFinediningFragment().newInstance(type)

        }

    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }

}