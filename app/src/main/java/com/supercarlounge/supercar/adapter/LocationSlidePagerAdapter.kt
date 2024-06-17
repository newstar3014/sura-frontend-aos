package com.supercarlounge.supercar.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.AlimData
import com.supercarlounge.supercar.data.BannerData
import com.supercarlounge.supercar.data.LocationSlideData
import com.supercarlounge.supercar.databinding.ItemLocationSlideBinding
import com.supercarlounge.supercar.databinding.ItemNoticeBinding


class LocationSlidePagerAdapter(val con: Context, var data: ArrayList<LocationSlideData>, val itemClick: (LocationSlideData, Int) -> Unit) : PagerAdapter() {
    var selectindex = 0

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
       var inflater:View =  LayoutInflater.from(container.context).inflate(R.layout.item_location_slide, container, false)
         var bind =   DataBindingUtil.bind<ItemLocationSlideBinding>(inflater)

        bind!!.itemIv.setOnClickListener { itemClick( data[position],position) }
        Glide.with(con).load(data[position].lb_image).centerInside().override(bind!!.itemIv.width,bind!!.itemIv.height).into(bind!!.itemIv)
        bind!!.posi = position
        bind!!.dataset = data[position]
        container.addView(bind!!.root)
        return bind.root
    }

    //position에 위치한 페이지 제거
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return (view==`object`)
    }
    override fun getCount(): Int {
        return data.size
    }
    fun addAllItem(list: ArrayList<LocationSlideData> /* = java.util.ArrayList<com.example.supercarx.data.BannerData> */){
        data = list
        notifyDataSetChanged()
    }
    companion object {
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:locationlist")
        @JvmStatic
        fun setbanner(pager: ViewPager, items: ArrayList<LocationSlideData>) {
            var adapter =  (pager.adapter as LocationSlidePagerAdapter)
            items?.let {
                adapter.addAllItem(it)
            }

        }
    }

    override fun getPageWidth(position: Int): Float {
        return 0.9f
    }
}