package com.supercarlounge.supercar.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.fragment.app.*
import com.supercarlounge.supercar.ui.fragment.pageranonymousforumf.*

class BoardPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    var anonymousForumNotiFragment: BoardNotiFragment? = null
    var anonymousForumAllFragment: BoardListFragment? = null
    var anonymousForumBestFragment :BoardListFragment? = null
    var anonymousForumfreeFragment :BoardListFragment? = null
    var anonymousForumLoveFragment :BoardListFragment? = null
    var anonymousForumCarWashFragment :BoardListFragment? = null
    var anonymousForumCarFragment :BoardListFragment? = null
    var anonymousForumCampingFragment :BoardListFragment? = null
    var anonymousForumGolfFragment :BoardListFragment? = null
    var anonymousForumHotPlayFragment :BoardListFragment? = null
    var anonymousForumZetechFragment :BoardListFragment? = null
    var anonymousForumJobFragment :BoardListFragment? = null
    var anonymousForumCircuitFragment :BoardListFragment? = null
    var anonymousForumAdultFragment :BoardListFragment? = null

    override fun getCount(): Int {
        return 13
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val createdFragment = super.instantiateItem(container, position) as Fragment
        when (position) {
            0 -> anonymousForumNotiFragment = createdFragment as BoardNotiFragment
            1 -> anonymousForumAllFragment = createdFragment as BoardListFragment
            2 ->anonymousForumBestFragment= createdFragment as BoardListFragment
            3 ->anonymousForumfreeFragment= createdFragment as BoardListFragment
            4 ->anonymousForumLoveFragment= createdFragment as BoardListFragment
            5 ->anonymousForumCarWashFragment= createdFragment as BoardListFragment
            6 ->anonymousForumCarFragment= createdFragment as BoardListFragment
            7 ->anonymousForumCampingFragment= createdFragment as BoardListFragment
            8 ->anonymousForumGolfFragment= createdFragment as BoardListFragment
            9 ->anonymousForumHotPlayFragment= createdFragment as BoardListFragment
            10 ->anonymousForumZetechFragment= createdFragment as BoardListFragment
            11->anonymousForumJobFragment= createdFragment as BoardListFragment
            12 ->anonymousForumCircuitFragment= createdFragment as BoardListFragment
            else-> anonymousForumAdultFragment = createdFragment as BoardListFragment
        }
        return createdFragment
    }

    override fun getItem(position: Int): Fragment {

        return when(position){
            0->  BoardNotiFragment().newInstance(0)
            1 -> BoardListFragment().newInstance(1)
            2 ->BoardListFragment().newInstance(2)
            3-> BoardListFragment().newInstance(3)
            4->  BoardListFragment().newInstance(4)
            5 -> BoardListFragment().newInstance(5)
            6 ->BoardListFragment().newInstance(6)
            7-> BoardListFragment().newInstance(7)
            8->  BoardListFragment().newInstance(8)
            9 -> BoardListFragment().newInstance(9)
            10 ->BoardListFragment().newInstance(10)
            11 -> BoardListFragment().newInstance(11)
            12 ->BoardListFragment().newInstance(12)
            else-> BoardListFragment().newInstance(13)



        }
    }
    fun getfragment(postion: Int): Fragment {
        var frag: Fragment? = null
        when (postion) {
            0 -> {
                if (anonymousForumNotiFragment != null) {
                    frag = anonymousForumNotiFragment as Fragment
                } else {
                    frag = BoardNotiFragment().newInstance(0)
                }
            }
            1 ->{
                if (anonymousForumAllFragment != null) {
                    frag = anonymousForumAllFragment as Fragment
                } else {
                    frag = BoardListFragment().newInstance(1)
                }
            }
            2 ->{
                if (anonymousForumBestFragment != null) {
                    frag = anonymousForumBestFragment as Fragment
                } else {
                    frag = BoardListFragment().newInstance(2)
                }
            }
            3 ->{
                if (anonymousForumfreeFragment != null) {
                    frag = anonymousForumfreeFragment as Fragment
                } else {
                    frag = BoardListFragment().newInstance(3)
                }
            }

            4 ->{
                if (anonymousForumLoveFragment != null) {
                    frag = anonymousForumLoveFragment as Fragment
                } else {
                    frag = BoardListFragment().newInstance(4)
                }
            }
            5 ->{
                if (anonymousForumCarWashFragment != null) {
                    frag = anonymousForumCarWashFragment as Fragment
                } else {
                    frag = BoardListFragment().newInstance(5)
                }
            }
            6 ->{
                if (anonymousForumCarFragment != null) {
                    frag = anonymousForumCarFragment as Fragment
                } else {
                    frag = BoardListFragment().newInstance(6)
                }
            }
            7 ->{
                if (anonymousForumCampingFragment != null) {
                    frag = anonymousForumCampingFragment as Fragment
                } else {
                    frag = BoardListFragment().newInstance(7)
                }
            }
            8 ->{
                if (anonymousForumGolfFragment != null) {
                    frag = anonymousForumGolfFragment as Fragment
                } else {
                    frag = BoardListFragment().newInstance(8)
                }
            }
            9 ->{
                if (anonymousForumHotPlayFragment != null) {
                    frag = anonymousForumHotPlayFragment as Fragment
                } else {
                    frag = BoardListFragment().newInstance(9)
                }
            }
            10 ->{
                if (anonymousForumZetechFragment != null) {
                    frag = anonymousForumZetechFragment as Fragment
                } else {
                    frag = BoardListFragment().newInstance(10)
                }
            }
            11 ->{
                if (anonymousForumJobFragment != null) {
                    frag = anonymousForumJobFragment as Fragment
                } else {
                    frag = BoardListFragment().newInstance(11)
                }
            }
            12 ->{
                if (anonymousForumCircuitFragment != null) {
                    frag = anonymousForumCircuitFragment as Fragment
                } else {
                    frag = BoardListFragment().newInstance(12)
                }
            }
            else -> {
                if (anonymousForumAdultFragment != null) {
                    frag = anonymousForumAdultFragment as Fragment
                } else {
                    frag = BoardListFragment().newInstance(13)
                }
            }

        }
        return frag!!
    }
    fun changeviewtype(position: Int,b_con :String,bookmark:String,order:String){

            // if(driveAwayNewFragment !=null){

        if (position == 0){
            (getfragment(position) as BoardNotiFragment).changeSearch(b_con,bookmark, order)
        }else{
            (getfragment(position) as BoardListFragment).changeSearch(b_con,bookmark,order)
        }

            // }
        Log.d("외부이벤트 changeviewtype",position.toString()+b_con)
    }
    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }

}