package com.supercarlounge.supercar.adapter


import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.supercarlounge.supercar.Constans.Companion.VIEW_AWAY_DRIVE
import com.supercarlounge.supercar.Constans.Companion.VIEW_HOME_BANNER1
import com.supercarlounge.supercar.Constans.Companion.VIEW_HOME_BANNER2
import com.supercarlounge.supercar.Constans.Companion.VIEW_VIP_DRIVE
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.BannerData
import com.supercarlounge.supercar.data.ConciergeData
import com.supercarlounge.supercar.data.DriveAwayData
import com.supercarlounge.supercar.databinding.*
import com.supercarlounge.supercar.ui.activity.DriveDetailActivity
import com.supercarlounge.supercar.viewmodel.HomeViewModel
import com.google.android.material.tabs.TabLayout
import java.util.*
import kotlin.collections.ArrayList


class HomeMainAdapter(
    val con: Context,
    val model: HomeViewModel,
    val banner1list: ArrayList<BannerData>,
    val vipDriveList: ArrayList<DriveAwayData>,
    val banner2list: ArrayList<BannerData>,
    val fm : FragmentManager,
    val itemClick: ( Int) -> Unit



) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val handler = Handler()
//    var timer = Timer()
    var pageradapter : DriveAwayPagerAdapter? =null
    var pagerbanneradapter1: BannerPagerAdapter? = null
    private var  vipDriveAdapter : VipDriveAdapter? = null

    var pagerbanneradapter2: HomeBannerPager2Adapter? = null
   // var profileSuggestionAdapter: ProfileSuggestionAdapter? = null
    override fun getItemCount(): Int {
        return 4
    }




    fun setdata(datalist: ArrayList<ConciergeData>) {
//        data.clear()
//        data.addAll(datalist)
        notifyDataSetChanged()
    }

    // 아이템의 타입을 반환 (position은 0 기반이므로 (전체 갯수 - 1) 일 경우에 Footer 타입 반환)
    override fun getItemViewType(position: Int): Int {
        return when (position) {

            0 -> VIEW_HOME_BANNER1
//            1 ->VIEW_SUGGESTION_DRIVE
            1 ->VIEW_VIP_DRIVE
            2->VIEW_HOME_BANNER2
            else->VIEW_AWAY_DRIVE


        }
    }




    private fun ViewGroup.inflate(layoutRes: Int): View =
        LayoutInflater.from(context).inflate(layoutRes, this, false)

    //만들어진 뷰홀더 없을때 뷰홀더(레이아웃) 생성하는 함수
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        // Holder에 따른 Binding 처리
        when (holder) {

            is homeBanner1ViewHolder -> {

                holder.bind(position)
            }

//            is homeSuggestionDriveViewHolder -> {
//                holder.itemView.setOnClickListener {
//                    itemClick(position)
//                }
//            }
            is homeVipDriveViewHolder -> {
                holder.bind(position)
            }
            is homeBanner2ViewHolder -> {
                holder.bind(position)
            }

            is homeAwayDriveViewHolder  -> {
                holder.bind(position)
                Log.d("체크", "bind:1 ")
            }

        }
    }

        // 각 View의 Type에 따른 ViewHolder 반환
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                VIEW_HOME_BANNER1 -> homeBanner1ViewHolder(parent)
//                VIEW_SUGGESTION_DRIVE -> homeSuggestionDriveViewHolder(parent)
                VIEW_VIP_DRIVE -> homeVipDriveViewHolder(parent)
                VIEW_HOME_BANNER2 -> homeBanner2ViewHolder(parent)
                VIEW_AWAY_DRIVE -> homeAwayDriveViewHolder(parent)
                else -> homeAwayDriveViewHolder(parent)
            }
        }



    inner class homeBanner1ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_home_banner_1, parent, false)
    ) {
        private val binding: ItemHomeBanner1Binding? = DataBindingUtil.bind(itemView)

        fun bind(position: Int) {

            binding?.viewModel = model
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner

            pagerbanneradapter1 = BannerPagerAdapter(con,banner1list,"탑") { data, position ->

            }

            binding.pagerBanner1.adapter = pagerbanneradapter1
            bannerAutoSlide1(binding)


            itemView.setOnClickListener {

            }

        }
    }




    //다음버전때
//
//    inner class homeSuggestionDriveViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
//        LayoutInflater.from(parent.context).inflate(R.layout.item_home_suggestion, parent, false)
//    ) {
//        private val binding: ItemHomeSuggestionBinding? = DataBindingUtil.bind(itemView)
//
//        fun bind( position: Int) {
//
//            binding?.viewModel = model
//            binding?.posi = position
//            binding!!.executePendingBindings()
//            binding.lifecycleOwner = con as LifecycleOwner
//
//            itemView.setOnClickListener {
//
//            }
//
//        }
//    }


    inner class homeVipDriveViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_home_vip_drive, parent, false)
    ) {
        private val binding: ItemHomeVipDriveBinding? = DataBindingUtil.bind(itemView)

        fun bind( position: Int) {

            binding?.viewModel = model
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner



            vipDriveAdapter = VipDriveAdapter(con, vipDriveList, model) { data, position ->
                val i = Intent(con, DriveDetailActivity::class.java)
                i.putExtra("data",data)
                i.putExtra("position",position)
                con.startActivity(i)

//                con.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
            binding!!.rvVipDrive.adapter = vipDriveAdapter
            binding!!.rvVipDrive.layoutManager = GridLayoutManager(con, 2, GridLayoutManager.VERTICAL, false)
            binding!!.rvVipDrive.setHasFixedSize(true)

        }
    }

    inner class homeBanner2ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_home_banner_2, parent, false)
    ) {
        private val binding: ItemHomeBanner2Binding? = DataBindingUtil.bind(itemView)

        fun bind( position: Int) {

            binding?.viewModel = model
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner

            itemView.setOnClickListener {

            }

            pagerbanneradapter2 = HomeBannerPager2Adapter(con,banner2list) {data, position ->

            }
            binding!!.pagerBanner2.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {

//                    model.startTopViewPosition.value =con.getViewCoordinate(com.example.supercarx.R.id.pager_banner_2)
                    binding!!.pagerBanner2.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
            binding.pagerBanner2.adapter = pagerbanneradapter2
            bannerAutoSlide2(binding)


            itemView.setOnClickListener {

            }

        }
    }
    inner class homeAwayDriveViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_home_away_drive, parent, false)
    ) {
        private val binding: ItemHomeAwayDriveBinding? = DataBindingUtil.bind(itemView)

        fun bind( position: Int) {
            Log.d("체크", "bind:2 ")
            binding?.viewModel = model
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner

            pageradapter = DriveAwayPagerAdapter(fm!!)

            binding!!.pager3.adapter = pageradapter
            binding!!.pager3.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding!!.tabLayout))
            binding!!.pager3.setPagingEnabled(true)
            binding!!.tabLayout!!.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {

                    var position = tab!!.position
                    binding!!.pager3.currentItem = position
                    var now = binding!!.pager3.currentItem
                    pageradapter!!.changeviewtype(now,model.rvItemType.value!!)
                    tab?.position?.let { binding!!.pager3.setCurrentItem(it, false) }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

            })


            binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("NEW"))
            binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("HOT"))
            binding!!.pager3.offscreenPageLimit = 2

            itemView.setOnClickListener {

            }

        }
    }
    fun setBannerData(datalist: ArrayList<BannerData>) {
        banner1list.clear()
        banner1list.addAll(datalist)
        notifyItemChanged(0)

    }

//    fun setBannerList(){
//        model.getBannerTop("드라이브 상단")
//
//    }
//    fun
//
//    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
//        LayoutInflater.from(parent.context).inflate(com.example.supercarx.R.layout.item_home_top, parent, false)
//    ) {
//        private val binding: ItemHomeTopBinding? = DataBindingUtil.bind(itemView)
//
//        fun bind(data: ConciergeData, position: Int) {
//            binding?.dataset = data
//            binding?.viewModel = model
//            binding?.posi = position
//            binding!!.executePendingBindings()
//            binding.lifecycleOwner = con as LifecycleOwner
//            itemView.setOnClickListener { itemClick(data, position) }
//
//
//            initAdpater(binding)
//            bannerAutoSlide(binding)
//
////            Glide.with(binding!!.itemIv).load(data!!.src).centerInside().into(binding!!.)
//        }
//    }


//    private fun initAdpater(binding: ItemHomeTopBinding) {
//        binding.pagerBanner1.adapter = pagerbanneradapter1
//        pagerbanneradapter1 = HomeBannerPager1Adapter(
//            con,
//            model.getBannerTop("드라이브 상단", pagerbanneradapter1!!)
//        ) { data, position ->
//            val i = Intent(con, PostNoticeActivity::class.java)
//            con.startActivity(i)
//        }
//
//        binding!!.carouselRecyclerview.adapter = profileSuggestionAdapter
//        var pslist: java.util.ArrayList<ResultProfileSuggestionData> = arrayListOf()
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
//        val carouselLayoutManager = LinearLayoutManager(con, LinearLayoutManager.HORIZONTAL, false)
//        binding!!.carouselRecyclerview.layoutManager = carouselLayoutManager
//
//    }

    private fun bannerAutoSlide1(binding: ItemHomeBanner1Binding) {
        val timer = Timer()
            val Update = java.lang.Runnable {

                var posi = model!!.bannerPosition.value!!
                Log.d("배너 포지션", posi.toString())
                if (posi < banner1list.size) {
                    binding.pagerBanner1.setCurrentItem(posi, true)

                    model!!.bannerPosition.value = model!!.bannerPosition.value!!.plus(1)
                } else {
                    model!!.bannerPosition.value = 0
                    binding.pagerBanner1.setCurrentItem(model!!.bannerPosition.value!!, true)
                }

            }



                timer.schedule(object : TimerTask() {
                    override fun run() {

                        handler.post(Update)

                    }

                    override fun cancel(): Boolean {
                        return super.cancel()

                    }
                }, 500, 3000)

    }
    private fun bannerAutoSlide2(binding2: ItemHomeBanner2Binding) {
        val timer = Timer()
        val Update = java.lang.Runnable {



            var posi2 = model!!.banner2Position.value!!
            if (posi2 < banner2list.size) {
                binding2.pagerBanner2.setCurrentItem(posi2, true)
                model!!.banner2Position.value = model!!.banner2Position.value!!.plus(1)
            } else {
                model!!.banner2Position.value = 0
                binding2.pagerBanner2.setCurrentItem(model!!.banner2Position.value!!, true)
            }
        }



        timer.schedule(object : TimerTask() {
            override fun run() {

                handler.post(Update)

            }

            override fun cancel(): Boolean {
                return super.cancel()

            }
        }, 500, 3000)






    }


}