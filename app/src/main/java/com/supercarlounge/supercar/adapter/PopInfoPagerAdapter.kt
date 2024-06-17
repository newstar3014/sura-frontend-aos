package com.supercarlounge.supercar.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.supercarlounge.supercar.data.PopUpinfoData
import jp.wasabeef.glide.transformations.RoundedCornersTransformation


class PopInfoPagerAdapter(context: Context, list :ArrayList<PopUpinfoData>, val itemClick: (PopUpinfoData, Int) -> Unit) : PagerAdapter(){
    private var mContext =context
    private var mlist = list



    //position에 해당하는 페이지 생성
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view=LayoutInflater.from(container.context).inflate(com.supercarlounge.supercar.R.layout.fragment_pop_info,container,false)
        val imageView: ImageView = view.findViewById(com.supercarlounge.supercar.R.id.iv_image)
//        val multiLeftTopCorner: MultiTransformation<Bitmap> = MultiTransformation(
//            RoundedCornersTransformation( 75, 0, RoundedCornersTransformation.CornerType.TOP_RIGHT),
//            RoundedCornersTransformation( 75, 0, RoundedCornersTransformation.CornerType.TOP_LEFT)
//        )
//        transform(multiLeftTopCorner)
        imageView.setOnClickListener { itemClick( mlist[position],position) }
        Glide.with(mContext).load(mlist[position].p_pic).override(imageView.width,imageView.height).into(imageView)
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
    fun addAllItem(list: ArrayList<PopUpinfoData> /* = java.util.ArrayList<com.example.supercarx.data.BannerData> */){
        mlist = list
        notifyDataSetChanged()
    }
    companion object {
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:popbanners")
        @JvmStatic
        fun setbanner(pager: ViewPager, items: ArrayList<PopUpinfoData>) {
          var adapter =  (pager.adapter as PopInfoPagerAdapter)
            items?.let {
                adapter.addAllItem(it)
            }

        }
    }


}