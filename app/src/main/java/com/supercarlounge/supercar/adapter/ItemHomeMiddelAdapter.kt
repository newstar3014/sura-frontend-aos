//package com.example.supercarx.adapter
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.databinding.DataBindingUtil
//import androidx.lifecycle.LifecycleOwner
//import androidx.recyclerview.widget.RecyclerView
//import com.example.supercarx.R
//import com.example.supercarx.data.ConciergeData
//
//import com.example.supercarx.viewmodel.HomeViewModel
//import kotlin.collections.ArrayList
//
//
//class ItemHomeMiddelAdapter(val con: Context, val data: ArrayList<ConciergeData>, val model: HomeViewModel, val itemClick: (ConciergeData, Int) -> Unit) : RecyclerView.Adapter<ItemHomeMiddelAdapter.ViewHolder>() {
//
//    var pagerbanneradapter2 : HomeBannerPager2Adapter? =null
//    private var  adapter : VipDriveAdapter? = null
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
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
//        LayoutInflater.from(parent.context).inflate(R.layout.item_home_banner_1, parent, false) as ViewGroup
//    )
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(data.get(position), position)
//    }
//    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
//        LayoutInflater.from(parent.context).inflate(R.layout.item_home_away_drive, parent, false)
//    ) {
//        private val binding: ItemHomeMiddleBinding? = DataBindingUtil.bind(itemView)
//
//        fun bind(data: ConciergeData, position: Int) {
//            binding?.dataset = data
//            binding?.viewModel = model
//            binding?.posi = position
//            binding!!.executePendingBindings()
//            binding.lifecycleOwner = con as LifecycleOwner
//
//            itemView.setOnClickListener { itemClick(data, position) }
//
//
////            initAdpater(binding)
////            bannerAutoSlide(binding)
//
////            Glide.with(binding!!.itemIv).load(data!!.src).centerInside().into(binding!!.)
//        }
//    }
////    private fun initAdpater(binding:ItemHomeMiddleBinding){
////        pagerbanneradapter2 = HomeBannerPager2Adapter(con, model.getBannerBottom("드라이브 하단",pagerbanneradapter2!!))
////        binding!!.pagerBanner2.adapter = pagerbanneradapter2
////
////
////        adapter = VipDriveAdapter(con, model.getVipDrive("409",adapter!! ), model) { data, position ->
////            val i = Intent(con, DrivePostActivity::class.java)
////            i.putExtra("data",data)
////            i.putExtra("position",position)
////            con.startActivity(i)
////
////        }
////        binding!!.rvVipDrive.adapter = adapter
////        binding!!.rvVipDrive.layoutManager =
////            GridLayoutManager(con, 2, GridLayoutManager.VERTICAL, false)
////        binding!!.rvVipDrive.setHasFixedSize(true)
////
////        binding!!.pagerBanner2.viewTreeObserver.addOnGlobalLayoutListener(object :
////            ViewTreeObserver.OnGlobalLayoutListener {
////            override fun onGlobalLayout() {
////
//////                    viewModel.startTopViewPosition.value =getViewCoordinate(com.example.supercarx.R.id.pager_banner_2)
//////                    binding!!.pagerBanner2.viewTreeObserver.removeOnGlobalLayoutListener(this)
////            }
////        })
////    }
////
////    private fun bannerAutoSlide(binding :ItemHomeMiddleBinding) {
////
////        val handler = Handler()
////        val Update = java.lang.Runnable {
////            model!!.bannerPosition.value =model!!.bannerPosition.value!!.plus(1)
////            var posi = model!!.bannerPosition.value!!
////            if (posi <binding!!.pagerBanner2!!.size){
////                binding!!.pagerBanner2!!.setCurrentItem(posi, true)
////            }else{
////                model!!.bannerPosition.value =0
////                binding!!.pagerBanner2!!.setCurrentItem(0, true)
////            }
//////            if (posi <binding!!.pagerBanner2!!.size){
//////                binding!!.pagerBanner2!!.setCurrentItem(posi, true)
//////            }else{
//////                viewModel!!.bannerPosition.value =0
//////                binding!!.pagerBanner2!!.setCurrentItem(0, true)
//////            }
////
////        }
////        var timer = Timer()
////        timer.schedule(object : TimerTask() {
////            override fun run() {
////                handler.post(Update)
////            }
////        }, 500, 3000)
////
////
////    }
//
//
//}