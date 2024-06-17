//package com.example.supercarx.adapter
//
//
//import android.content.Context
//import android.content.Intent
//import android.os.Handler
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.core.view.size
//import androidx.databinding.DataBindingUtil
//import androidx.lifecycle.LifecycleOwner
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//
//import com.example.supercarx.data.ConciergeData
//import com.example.supercarx.data.ResultProfileSuggestionData
//import com.example.supercarx.databinding.ItemHomeTopBinding
//import com.example.supercarx.ui.activity.PostNoticeActivity
//import com.example.supercarx.viewmodel.HomeViewModel
//import java.util.*
//
//
//class ItemHomeTOPAdapter(val con: Context, val data: ArrayList<ConciergeData>, val model: HomeViewModel, val itemClick: (ConciergeData, Int) -> Unit) : RecyclerView.Adapter<ItemHomeTOPAdapter.ViewHolder>() {
//
//    var pagerbanneradapter1 : HomeBannerPager1Adapter? =null
//    var profileSuggestionAdapter : ProfileSuggestionAdapter ? =null
//
//    override fun getItemCount(): Int {
//        return 1
//    }
//
//    fun setdata(datalist:ArrayList<ConciergeData>){
//        data.clear()
//        data.addAll(datalist)
//        notifyDataSetChanged()
//    }
//
//
//    //만들어진 뷰홀더 없을때 뷰홀더(레이아웃) 생성하는 함수
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v: View = LayoutInflater.from(parent.context).inflate(com.example.supercarx.R.layout.item_home_header, parent, false)
//
//        return ViewHolder(v as ViewGroup)
//    }
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(data.get(position), position)
//    }
////    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
////        LayoutInflater.from(parent.context).inflate(com.example.supercarx.R.layout.item_home_header, parent, false)
////    ) {
////        private val binding: ItemHomeTopBinding? = DataBindingUtil.bind(itemView)
////
////        fun bind(data: ConciergeData, position: Int) {
////            binding?.dataset = data
////            binding?.viewModel = model
////            binding?.posi = position
////            binding!!.executePendingBindings()
////            binding.lifecycleOwner = con as LifecycleOwner
////            itemView.setOnClickListener { itemClick(data, position) }
////
////
////            initAdpater(binding)
////            bannerAutoSlide(binding)
////
//////            Glide.with(binding!!.itemIv).load(data!!.src).centerInside().into(binding!!.)
////        }
////    }
////    private fun initAdpater(binding:ItemHomeTopBinding){
////        binding.pagerBanner1.adapter = pagerbanneradapter1
////        pagerbanneradapter1 = HomeBannerPager1Adapter(con, model.getBannerTop("드라이브 상단",pagerbanneradapter1!!)) { data, position ->
////            val i = Intent(con, PostNoticeActivity::class.java)
////            con.startActivity(i)
////        }
//
////        binding!!.carouselRecyclerview.adapter = profileSuggestionAdapter
//        var pslist : java.util.ArrayList<ResultProfileSuggestionData> = arrayListOf()
//
//
////        var data1 =  ResultProfileSuggestionData("프로필 변경 신청이 반려되었습니다.", "0")
//
////        for (i in 1..10) {
////            pslist.add(data1)
////        }
////        profileSuggestionAdapter = ProfileSuggestionAdapter(con, pslist, model) { data, position ->
////
////        }
//        val carouselLayoutManager =  LinearLayoutManager(con, LinearLayoutManager.HORIZONTAL, false)
//        binding!!.carouselRecyclerview.layoutManager = carouselLayoutManager
//
//    }
//
//    private fun bannerAutoSlide(binding :ItemHomeTopBinding) {
//
//        val handler = Handler()
//        val Update = java.lang.Runnable {
//            model!!.bannerPosition.value =model!!.bannerPosition.value!!.plus(1)
//            var posi = model!!.bannerPosition.value!!
//            if (posi <binding!!.pagerBanner1!!.size){
//                binding!!.pagerBanner1!!.setCurrentItem(posi, true)
//            }else{
//                model!!.bannerPosition.value =0
//                binding!!.pagerBanner1!!.setCurrentItem(0, true)
//            }
////            if (posi <binding!!.pagerBanner2!!.size){
////                binding!!.pagerBanner2!!.setCurrentItem(posi, true)
////            }else{
////                viewModel!!.bannerPosition.value =0
////                binding!!.pagerBanner2!!.setCurrentItem(0, true)
////            }
//
//        }
//        var timer = Timer()
//        timer.schedule(object : TimerTask() {
//            override fun run() {
//                handler.post(Update)
//            }
//        }, 500, 3000)
//
//
//    }
//
//
//}