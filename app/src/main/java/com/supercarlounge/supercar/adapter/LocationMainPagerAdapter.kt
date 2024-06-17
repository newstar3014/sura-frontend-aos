package com.supercarlounge.supercar.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.fragment.app.*
import com.supercarlounge.supercar.ui.fragment.*

class LocationMainPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    var locationMainFragment1: LocationMainItemFragment? = null
    var locationMainFragment2: LocationMainItemFragment? = null
    var locationMainFragment3: LocationMainItemFragment? = null
    var locationMainFragment4: LocationMainItemFragment? = null
    var locationMainFragment5: LocationMainItemFragment? = null
    var locationMainFragment6: LocationMainItemFragment? = null

    var type = 0
    override fun getCount(): Int {
        return 6
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val createdFragment = super.instantiateItem(container, position) as Fragment

        when (position) {

            0 -> {
                locationMainFragment1 = createdFragment as LocationMainItemFragment
            }
            1->{
                locationMainFragment2 = createdFragment as LocationMainItemFragment
            }
            2->{
                locationMainFragment3 = createdFragment as LocationMainItemFragment
            }
            3->{
                locationMainFragment4 = createdFragment as LocationMainItemFragment
            }
            4->{
                locationMainFragment5 = createdFragment as LocationMainItemFragment
            }

//            5->{
//                locationMainFragment6 = createdFragment as LocationMainItemFragment
//            }


            else -> {
               // locationMainFragment7 = createdFragment as LocationMainItemFragment
                locationMainFragment6= createdFragment as LocationMainItemFragment

            }

        }
        return createdFragment
    }
    fun getfragment(postion: Int): Fragment {
        var frag: Fragment? = null
        when (postion) {
            0 -> {
                if (locationMainFragment1 != null) {
                    frag = locationMainFragment1 as Fragment
                } else {
                    frag = LocationMainItemFragment(0).newInstance(type)
                }
            }
            1->{
                if (locationMainFragment2 != null) {
                    frag = locationMainFragment2 as Fragment
                } else {
                    frag = LocationMainItemFragment(1).newInstance(type)
                }
            }
            2->{
                if (locationMainFragment3 != null) {
                    frag = locationMainFragment3 as Fragment
                } else {
                    frag = LocationMainItemFragment(2).newInstance(type)
                }
            }
           3->{
                if (locationMainFragment4 != null) {
                    frag = locationMainFragment4 as Fragment
                } else {
                    frag = LocationMainItemFragment(3).newInstance(type)
                }
            }
            4->{
                if (locationMainFragment5 != null) {
                    frag = locationMainFragment5 as Fragment
                } else {
                    frag = LocationMainItemFragment(4).newInstance(type)
                }
            }

            else -> {
                if (locationMainFragment6 != null) {
                    frag = locationMainFragment6 as Fragment
                } else {
                    frag = LocationMainItemFragment(5).newInstance(type)
                }
//                if (locationMainFragment7 != null) {
//                    frag = locationMainFragment7 as Fragment
//                } else {
//                    frag = LocationMainItemFragment(6).newInstance(type)
//                }
            }


        }
        return frag!!
    }
    fun changeList(nowposition:Int,search :String,type:String,region:String,payTag:String,min:Int,max:Int,typelist:ArrayList<String>){
        Log.d("체크", "체인지리스트")
        Log.d("체크 검색어", search)
        when(nowposition){
            0->{
                 if(locationMainFragment1 !=null){
                (getfragment(0) as LocationMainItemFragment).changeList(search,type,region,payTag,min,max,typelist)
                 }
            }
            1->{
                if(locationMainFragment2 !=null){
                    (getfragment(1) as LocationMainItemFragment).changeList(search,type,region,payTag,min,max,typelist)
                }
            }
            2->{
                if(locationMainFragment3 !=null){
                    (getfragment(2) as LocationMainItemFragment).changeList(search,type,region,payTag,min,max,typelist)
                }
            }
            3->{
                if(locationMainFragment4 !=null){
                    (getfragment(3) as LocationMainItemFragment).changeList(search,type,region,payTag,min,max,typelist)
                }
            }
            4->{
                if(locationMainFragment5 !=null){
                    (getfragment(4) as LocationMainItemFragment).changeList(search,type,region,payTag,min,max,typelist)
                }
            }
            5->{
                if(locationMainFragment6 !=null){
                    (getfragment(5) as LocationMainItemFragment).changeList(search,type,region,payTag,min,max,typelist)
                }
            }

            else ->{
                if(locationMainFragment6 !=null){
                    (getfragment(6) as LocationMainItemFragment).changeList(search,type,region,payTag,min,max,typelist)
                }
            }
        }

           // if(driveAwayHotFragment !=null){

//            (getfragment(2) as DriveAwayNearFragment) .changeviewtype(type)
           // }

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

            0-> LocationMainItemFragment(0).newInstance(position)
            1->LocationMainItemFragment(1).newInstance(position)
            2->LocationMainItemFragment(2).newInstance(position)
            3->LocationMainItemFragment(3).newInstance(position)
            4->LocationMainItemFragment(4).newInstance(position)
            5->LocationMainItemFragment(5).newInstance(position)
            else->LocationMainItemFragment(6).newInstance(position)


        }

    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }

}