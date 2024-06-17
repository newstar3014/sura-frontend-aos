package com.supercarlounge.supercar.ui.priview.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.data.BannerData


class PrvGuidePagerAdapter(context: Context, list :ArrayList<Int>, val itemClick: (String, Int) -> Unit) : PagerAdapter(){
    private var mContext =context
    private var mlist = list



    //position에 해당하는 페이지 생성
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view=LayoutInflater.from(container.context).inflate(com.supercarlounge.supercar.R.layout.fragment_prv_guide,container,false)
        val imageView: ImageView = view.findViewById(com.supercarlounge.supercar.R.id.iv_image)

        Glide.with(mContext).load(mlist[position]).override(imageView.width,imageView.height).into(imageView)
        container.addView(view)
        return view
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
    fun addAllItem(list: ArrayList<Int> /* = java.util.ArrayList<com.example.supercarx.data.BannerData> */){
        mlist = list
        notifyDataSetChanged()
    }



}