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
import com.supercarlounge.supercar.data.AllianceData
import com.supercarlounge.supercar.databinding.ItemAllianceBinding


class ConciergeServicePagerAdapter(context: Context, var owner: LifecycleOwner, var list :ArrayList<AllianceData>,val event: (Boolean) -> Unit) : PagerAdapter(){
    private var mContext =context
    private var mlist = list



    //position에 해당하는 페이지 생성
    override fun instantiateItem(container: ViewGroup, position: Int): Any {


        var binding = ItemAllianceBinding.inflate(LayoutInflater.from(mContext), container, false)
        binding!!.lifecycleOwner = owner
            var data = mlist[position]
            var img =data.al_bgimg
            binding!!.dataset = data
        container.addView(binding.root)
        binding!!.root.setOnClickListener {
            event(true)
        }
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
    fun addAllItem(list: ArrayList<AllianceData> /* = java.util.ArrayList<com.example.supercarx.data.BannerData> */){
        mlist = list
        notifyDataSetChanged()
    }
    companion object {
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:alliance")
        @JvmStatic
        fun setalliance(pager: ViewPager, items: ArrayList<AllianceData>) {
          var adapter =  (pager.adapter as ConciergeServicePagerAdapter)
            items?.let {
                adapter.addAllItem(it)
            }

        }
    }


}