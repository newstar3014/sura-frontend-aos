package com.supercarlounge.supercar.ui.priview.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.fragment.app.*
import androidx.viewpager.widget.ViewPager
import com.supercarlounge.supercar.data.ConciergeCategoryData
import com.supercarlounge.supercar.ui.priview.fragment.concierge.PrvConciergeAllFragment

class PrvConciergePagerAdapter(fm: FragmentManager, var list:ArrayList<ConciergeCategoryData>) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    var prvconciergeAllFragment: PrvConciergeAllFragment? = null
    override fun getCount(): Int {
        return list.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val createdFragment = super.instantiateItem(container, position) as Fragment
        prvconciergeAllFragment = createdFragment as PrvConciergeAllFragment
        return createdFragment
    }

    override fun getItem(position: Int): Fragment {
        var data =list.get(position)
        var type = data.cc_seq.toString()
        if(type.equals("0")){
            type = "전체"
        }
      return  PrvConciergeAllFragment().newInstance(type)
    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }
    companion object {
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:prvconciergetype")
        @JvmStatic
        fun setItems( ViewPager: ViewPager, items: ArrayList<ConciergeCategoryData>) {
            if (ViewPager.adapter != null) {
                var adapter = (ViewPager.adapter as PrvConciergePagerAdapter)
                items?.let {
                    adapter.list = it
                    adapter.notifyDataSetChanged()

                }
            }
        }
    }
}