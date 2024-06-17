package com.supercarlounge.supercar.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.supercarlounge.supercar.GradientDrawable
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.SlidePidData
import com.supercarlounge.supercar.databinding.ItemSlidePid


class SlidePidPagerAdapter(context: Context,var owner:LifecycleOwner,var list :ArrayList<SlidePidData>, val itemClick: (SlidePidData, Int) -> Unit) : PagerAdapter(){
    private var mContext =context
    private var mlist = list



    //position에 해당하는 페이지 생성
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var data = list.get(position)
        var binding = ItemSlidePid.inflate(LayoutInflater.from(mContext), container, false)
        binding!!.dataset =  data
        binding!!.lifecycleOwner = owner
        var d = GradientDrawable(
            mContext.getColor(R.color.cooper_gradi_st),
            mContext.getColor(R.color.cooper_gradi_ed),
            mContext.getColor(
                R.color.white_4
            ),
            mContext.getColor(R.color.white_4),
            2f,
            10f,
            GradientDrawable.Direction.TL_BR,
            GradientDrawable.Direction.TL_BR
        )
        binding!!.rlPidback.background = d
        container.addView(binding.root)
        binding.root.setOnClickListener { itemClick( mlist[position],position) }
        return binding.root
    }

    //position에 위치한 페이지 제거
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }



    //사용가능한 뷰 개수 리턴
    override fun getCount(): Int {
        return mlist.size
    }

    //페이지뷰가 특정 키 객체(key object)와 연관 되는지 여부
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return (view==`object`)
    }
    fun addAllItem(list: ArrayList<SlidePidData> /* = java.util.ArrayList<com.example.supercarx.data.BannerData> */){
        mlist = list
        notifyDataSetChanged()
    }
    companion object {
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:slidepid")
        @JvmStatic
        fun setbanner(pager: ViewPager, items: ArrayList<SlidePidData>) {
          var adapter =  (pager.adapter as SlidePidPagerAdapter)
            items?.let {
                adapter.addAllItem(it)
            }

        }
    }


}