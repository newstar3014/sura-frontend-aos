package com.supercarlounge.supercar.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.fragment.app.*
import androidx.viewpager.widget.ViewPager
import com.supercarlounge.supercar.data.ConciergeCategoryData
import com.supercarlounge.supercar.ui.fragment.pagerconicergef.*
import com.supercarlounge.supercar.ui.priview.adapter.PrvConciergePagerAdapter

class ConciergePagerAdapter(fm: FragmentManager,var list:ArrayList<ConciergeCategoryData>) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    var conciergeAllFragment: ConciergeAllFragment? = null
    override fun getCount(): Int {
        return list.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val createdFragment = super.instantiateItem(container, position) as Fragment
        conciergeAllFragment = createdFragment as ConciergeAllFragment
        return createdFragment
    }

    override fun getItem(position: Int): Fragment {
        var data =list.get(position)
        var type = data.cc_seq.toString()
        if(type.equals("0")){
            type = "전체"
        }
      return  ConciergeAllFragment().newInstance(type)
    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }
    companion object {
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:conciergetype")
        @JvmStatic
        fun setItems( ViewPager: ViewPager, items: ArrayList<ConciergeCategoryData>) {
            if (ViewPager.adapter != null) {
                if(ViewPager.adapter is ConciergePagerAdapter) {
                    var adapter = (ViewPager.adapter as ConciergePagerAdapter)
                    items?.let {
                        adapter.list = it
                        adapter.notifyDataSetChanged()

                    }
                }else if (ViewPager.adapter is PrvConciergePagerAdapter){
                    var adapter = (ViewPager.adapter as PrvConciergePagerAdapter)
                    items?.let {
                        adapter.list = it
                        adapter.notifyDataSetChanged()

                    }
                }
            }
        }
    }
}