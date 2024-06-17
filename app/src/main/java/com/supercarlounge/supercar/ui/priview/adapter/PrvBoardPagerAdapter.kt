package com.supercarlounge.supercar.ui.priview.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.fragment.app.*
import com.supercarlounge.supercar.ui.priview.fragment.board.PrvBoardListFragment
import com.supercarlounge.supercar.ui.priview.fragment.board.PrvBoardNotiFragment

class PrvBoardPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    var anonymousForumNotiFragment: PrvBoardNotiFragment? = null
    var anonymousForumAllFragment: PrvBoardListFragment? = null
    var anonymousForumBestFragment :PrvBoardListFragment? = null
    var anonymousForumfreeFragment :PrvBoardListFragment? = null
    var anonymousForumLoveFragment :PrvBoardListFragment? = null
    var anonymousForumCarWashFragment :PrvBoardListFragment? = null
    var anonymousForumCarFragment :PrvBoardListFragment? = null
    var anonymousForumCampingFragment :PrvBoardListFragment? = null
    var anonymousForumGolfFragment :PrvBoardListFragment? = null
    var anonymousForumHotPlayFragment :PrvBoardListFragment? = null
    var anonymousForumZetechFragment :PrvBoardListFragment? = null
    var anonymousForumJobFragment :PrvBoardListFragment? = null
    var anonymousForumCircuitFragment :PrvBoardListFragment? = null
    var anonymousForumAdultFragment :PrvBoardListFragment? = null

    override fun getCount(): Int {
        return 13
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val createdFragment = super.instantiateItem(container, position) as Fragment
        when (position) {
            0 -> anonymousForumNotiFragment = createdFragment as PrvBoardNotiFragment
            1 -> anonymousForumAllFragment = createdFragment as PrvBoardListFragment
            2 ->anonymousForumBestFragment= createdFragment as PrvBoardListFragment
            3 ->anonymousForumfreeFragment= createdFragment as PrvBoardListFragment
            4 ->anonymousForumLoveFragment= createdFragment as PrvBoardListFragment
            5 ->anonymousForumCarWashFragment= createdFragment as PrvBoardListFragment
            6 ->anonymousForumCarFragment= createdFragment as PrvBoardListFragment
            7 ->anonymousForumCampingFragment= createdFragment as PrvBoardListFragment
            8 ->anonymousForumGolfFragment= createdFragment as PrvBoardListFragment
            9 ->anonymousForumHotPlayFragment= createdFragment as PrvBoardListFragment
            10 ->anonymousForumZetechFragment= createdFragment as PrvBoardListFragment
            11->anonymousForumJobFragment= createdFragment as PrvBoardListFragment
            12 ->anonymousForumCircuitFragment= createdFragment as PrvBoardListFragment
            else-> anonymousForumAdultFragment = createdFragment as PrvBoardListFragment
        }
        return createdFragment
    }

    override fun getItem(position: Int): Fragment {

        return when(position){
            0->  PrvBoardNotiFragment().newInstance(0)
            1 -> PrvBoardListFragment().newInstance(1)
            2 ->PrvBoardListFragment().newInstance(2)
            3-> PrvBoardListFragment().newInstance(3)
            4->  PrvBoardListFragment().newInstance(4)
            5 -> PrvBoardListFragment().newInstance(5)
            6 ->PrvBoardListFragment().newInstance(6)
            7-> PrvBoardListFragment().newInstance(7)
            8->  PrvBoardListFragment().newInstance(8)
            9 -> PrvBoardListFragment().newInstance(9)
            10 ->PrvBoardListFragment().newInstance(10)
            11 -> PrvBoardListFragment().newInstance(11)
            12 ->PrvBoardListFragment().newInstance(12)
            else-> PrvBoardListFragment().newInstance(13)



        }
    }
    fun getfragment(postion: Int): Fragment {
        var frag: Fragment? = null
        when (postion) {
            0 -> {
                if (anonymousForumNotiFragment != null) {
                    frag = anonymousForumNotiFragment as Fragment
                } else {
                    frag = PrvBoardNotiFragment().newInstance(0)
                }
            }
            1 ->{
                if (anonymousForumAllFragment != null) {
                    frag = anonymousForumAllFragment as Fragment
                } else {
                    frag = PrvBoardListFragment().newInstance(1)
                }
            }
            2 ->{
                if (anonymousForumBestFragment != null) {
                    frag = anonymousForumBestFragment as Fragment
                } else {
                    frag = PrvBoardListFragment().newInstance(2)
                }
            }
            3 ->{
                if (anonymousForumfreeFragment != null) {
                    frag = anonymousForumfreeFragment as Fragment
                } else {
                    frag = PrvBoardListFragment().newInstance(3)
                }
            }

            4 ->{
                if (anonymousForumLoveFragment != null) {
                    frag = anonymousForumLoveFragment as Fragment
                } else {
                    frag = PrvBoardListFragment().newInstance(4)
                }
            }
            5 ->{
                if (anonymousForumCarWashFragment != null) {
                    frag = anonymousForumCarWashFragment as Fragment
                } else {
                    frag = PrvBoardListFragment().newInstance(5)
                }
            }
            6 ->{
                if (anonymousForumCarFragment != null) {
                    frag = anonymousForumCarFragment as Fragment
                } else {
                    frag = PrvBoardListFragment().newInstance(6)
                }
            }
            7 ->{
                if (anonymousForumCampingFragment != null) {
                    frag = anonymousForumCampingFragment as Fragment
                } else {
                    frag = PrvBoardListFragment().newInstance(7)
                }
            }
            8 ->{
                if (anonymousForumGolfFragment != null) {
                    frag = anonymousForumGolfFragment as Fragment
                } else {
                    frag = PrvBoardListFragment().newInstance(8)
                }
            }
            9 ->{
                if (anonymousForumHotPlayFragment != null) {
                    frag = anonymousForumHotPlayFragment as Fragment
                } else {
                    frag = PrvBoardListFragment().newInstance(9)
                }
            }
            10 ->{
                if (anonymousForumZetechFragment != null) {
                    frag = anonymousForumZetechFragment as Fragment
                } else {
                    frag = PrvBoardListFragment().newInstance(10)
                }
            }
            11 ->{
                if (anonymousForumJobFragment != null) {
                    frag = anonymousForumJobFragment as Fragment
                } else {
                    frag = PrvBoardListFragment().newInstance(11)
                }
            }
            12 ->{
                if (anonymousForumCircuitFragment != null) {
                    frag = anonymousForumCircuitFragment as Fragment
                } else {
                    frag = PrvBoardListFragment().newInstance(12)
                }
            }
            else -> {
                if (anonymousForumAdultFragment != null) {
                    frag = anonymousForumAdultFragment as Fragment
                } else {
                    frag = PrvBoardListFragment().newInstance(13)
                }
            }

        }
        return frag!!
    }
    fun changeviewtype(position: Int,b_con :String,bookmark:String,order:String){

            // if(driveAwayNewFragment !=null){

        if (position == 0){
            (getfragment(position) as PrvBoardNotiFragment).changeSearch(b_con,bookmark, order)
        }else{
            (getfragment(position) as PrvBoardListFragment).changeSearch(b_con,bookmark,order)
        }

            // }
        Log.d("외부이벤트 changeviewtype",position.toString()+b_con)
    }
    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }

}