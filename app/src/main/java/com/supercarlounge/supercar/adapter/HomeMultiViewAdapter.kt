package com.supercarlounge.supercar.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.supercarlounge.supercar.Constans
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.customview.MiddleItemFinder
import com.supercarlounge.supercar.customview.SuggestItemDeco
import com.supercarlounge.supercar.data.*
import com.supercarlounge.supercar.databinding.*
import com.supercarlounge.supercar.dialog.PidDialog
import com.supercarlounge.supercar.dialog.RecDriveDialog
import com.supercarlounge.supercar.enumset.RecDriveDialogType
import com.supercarlounge.supercar.ui.activity.DriveDetailActivity
import com.supercarlounge.supercar.viewmodel.HomeViewModel


class HomeMultiViewAdapter(
    val con: Context,
    val model: HomeViewModel,
    val lifecycleOwner: LifecycleOwner,
    val fm: FragmentManager,
    var application: MainApplication
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var onclickTopBanner: ((BannerData, Int) -> Unit)? = null
    var onclickSugesst: ((ProfileSuggestionData, Int) -> Unit)? = null
    var onclickVipDrive: ((DriveAwayData, Int) -> Unit)? = null
    var onclickSlidePid: ((SlidePidData, Int) -> Unit)? = null
    var onclickBottomBanner: ((BannerData, Int) -> Unit)? = null
    var onclickDriveAway: ((ProfileSuggestionData, Int) -> Unit)? = null
    private var data: List<HomeData> = emptyList()

    var pagerbanneradapter1: BannerPagerAdapter? = null

    var pagerbanneradapter2: BannerPagerAdapter? = null
    var profileSuggestionAdapter: ProfileSugesstAdapter? = null
    private var vipDriveAdapter: VipDriveAdapter? = null
    var pageradapter: DriveAwayPagerAdapter? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Constans.MULTI_VIEW_ADAPTER_TOP_BANNER -> TopBannerViewHolder(parent)
            Constans.MULTI_VIEW_ADAPTER_SUGESST_HEADER -> SugesstHeaderViewHolder(parent)
            Constans.MULTI_VIEW_ADAPTER_SUGESST_RECYCLERVIEW -> SugesstRecyclerviewViewHolder(parent)
            Constans.MULTI_VIEW_ADAPTER_VIP_DRIVE_HEADER -> VipDriveHeaderViewHolder(parent)
            Constans.MULTI_VIEW_ADAPTER_VIP_DRIVE_RECYCLERVIEW -> VipDriveRecyclerviewViewHolder(
                parent
            )
            Constans.MULTI_VIEW_ADAPTER_MY_PICK_HEADER -> MyPickHeaderViewHolder(parent)
            Constans.MULTI_VIEW_ADAPTER_MY_PICK_RECYCLERVIEW -> MyPickRecyclerviewViewHolder(parent)
            Constans.MULTI_VIEW_ADAPTER_BOTTOM_BANNER -> BottomBannerViewHolder(parent)
            Constans.MULTI_VIEW_ADAPTER_DRIVE_AWAY_HEADER -> DriveAwayHeaderViewHolder(parent)
            else -> DriveAwayRecyclerviewViewHolder(parent)
        }

    }


    override fun getItemCount(): Int {
        return 10
    }

    override fun getItemViewType(position: Int): Int {
        when (data[position].title) {
            is String -> {
                if (data[position].title!! == "TOP_BANNER") {
                    return Constans.MULTI_VIEW_ADAPTER_TOP_BANNER
                } else if (data[position].title!! == "SUGESST_HEADER") {
                    return Constans.MULTI_VIEW_ADAPTER_SUGESST_HEADER
                } else if (data[position].title!! == "SUGESST_RECYCLERVIEW") {
                    return Constans.MULTI_VIEW_ADAPTER_SUGESST_RECYCLERVIEW
                } else if (data[position].title!! == "VIP_DRIVE_HEADER") {
                    return Constans.MULTI_VIEW_ADAPTER_VIP_DRIVE_HEADER
                } else if (data[position].title!! == "VIP_DRIVE_RECYCLERVIEW") {
                    return Constans.MULTI_VIEW_ADAPTER_VIP_DRIVE_RECYCLERVIEW
                } else if (data[position].title!! == "MY_PICK_HEADER") {
                    return Constans.MULTI_VIEW_ADAPTER_MY_PICK_HEADER
                } else if (data[position].title!! == "MY_PICK_RECYCLERVIEW") {
                    return Constans.MULTI_VIEW_ADAPTER_MY_PICK_RECYCLERVIEW
                } else if (data[position].title!! == "BOTTOM_BANNER") {
                    return Constans.MULTI_VIEW_ADAPTER_BOTTOM_BANNER
                } else if (data[position].title!! == "AWAY_HEADER") {
                    return Constans.MULTI_VIEW_ADAPTER_DRIVE_AWAY_HEADER
                } else if (data[position].title!! == "AWAY_RECYCLERVIEW") {
                    return Constans.MULTI_VIEW_ADAPTER_DRIVE_AWAY_RECYCLERVIEW
                }

            }
            else -> return Constans.MULTI_VIEW_ADAPTER_TOP_BANNER

        }
        return 0
    }


    fun setdata(datalist: ArrayList<BoardCategoryData>) {
//        data.clear()
//        data.addAll(datalist)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        return when (holder) {
            is TopBannerViewHolder -> holder.bind(data.get(position), position)
            is SugesstHeaderViewHolder -> holder.bind(data.get(position), position)
            is SugesstRecyclerviewViewHolder -> holder.bind(data.get(position), position)
            is VipDriveHeaderViewHolder -> holder.bind(data.get(position), position)
            is VipDriveRecyclerviewViewHolder -> holder.bind(data.get(position), position)
            is MyPickHeaderViewHolder -> holder.bind(data.get(position), position)
            is MyPickRecyclerviewViewHolder -> holder.bind(data.get(position), position)
            is BottomBannerViewHolder -> holder.bind(data.get(position), position)
            is DriveAwayHeaderViewHolder -> holder.bind(data.get(position), position)
            is DriveAwayRecyclerviewViewHolder -> holder.bind(data.get(position), position)
            else -> {}
        }

    }

    sealed class SampleItem {
        data class NameItem(val str: String) : SampleItem()
        data class ImageItem(val str: String) : SampleItem()
    }


    inner class TopBannerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_top_banner_multi, parent, false)
    ) {
        private val binding: ItemHomeTopBannerMultiBinding? = DataBindingUtil.bind(itemView)

        init {
            pagerbanneradapter1 =
                BannerPagerAdapter(con, model.mHomeBanner1List.value!!,"바텀") { data, position ->
                    onclickTopBanner?.invoke(data, position)
                }
            binding?.pagerBanner1?.apply {
                adapter = pagerbanneradapter1
//                layoutManager  = LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)
            }
        }

        fun bind(data: HomeData, position: Int) {
            binding?.dataset = data
            binding?.viewModel = model
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner
            itemView.setOnClickListener {}
//            Glide.with(binding!!.itemIv).load(data!!.src).centerInside().into(binding!!.)
        }
    }

    inner class SugesstHeaderViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_sugesst_header, parent, false)
    ) {
        private val binding: ItemHomeSugesstHeaderBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: HomeData, position: Int) {
            binding?.dataset = data
            binding?.viewModel = model
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner

//            itemView.setOnClickListener { itemClick(data, position) }
//            Glide.with(binding!!.itemIv).load(data!!.src).centerInside().into(binding!!.)
        }
    }


    inner class SugesstRecyclerviewViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_sugesst_recyclerview, parent, false)
    ) {
        private val binding: ItemHomeSugesstRecyclerviewBinding? = DataBindingUtil.bind(itemView)

        init {

            profileSuggestionAdapter = ProfileSugesstAdapter(
                con,
                0,
                arrayListOf(), model
            ) { data, position, width ->
                onclickSugesst?.invoke(data, position)
                var listsize = profileSuggestionAdapter!!.datalist.size
                var selindex = position % listsize
                if (model.selectproindex.value == selindex) {
                    if (!data.u_image.isNullOrEmpty()) {
                        var pid: RecDriveDialogType? = RecDriveDialogType.NOT_REC_DIRVE
                        if (application!!.userData?.u_feed.equals("1")) {
                            pid = RecDriveDialogType.PROFILE_OPEN
                        }
                        var did = RecDriveDialog(con, pid!!) { type, okcancel ->
                            if (type == RecDriveDialogType.PROFILE_OPEN) {
                                if (okcancel) {
                                    model.CheckDrivePass(data)

                                }
                            }
                        }
                        did.show(fm, "")
                    }else{
//                        warningDialog = WarningDialog(requireContext(),0)
//                        warningDialog!!.show(this.childFragmentManager,"")
                    }

                } else {
                    model.selectprodata.value = data
                    model.selectproindex.value = selindex
                    val offset = (binding!!.rvProfilesugesst.width / 2 - width / 2)
                    var mnan = (binding!!.rvProfilesugesst.layoutManager!! as LinearLayoutManager)
                    mnan.scrollToPositionWithOffset(position, offset )


                }
            }

            binding!!.rvProfilesugesst.adapter = profileSuggestionAdapter
            binding!!.rvProfilesugesst.addItemDecoration(SuggestItemDeco(5))
            var layoutmanager = LinearLayoutManager(con, RecyclerView.HORIZONTAL, false)
            binding!!.rvProfilesugesst.layoutManager = layoutmanager
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(binding!!.rvProfilesugesst)
            binding!!.rvProfilesugesst.addOnScrollListener(
                MiddleItemFinder(
                    con,
                    layoutmanager,
                    object : MiddleItemFinder.MiddleItemCallback {
                        override fun scrollFinished(middleElement: Int) {
                            var listsize = profileSuggestionAdapter!!.datalist.size
                            if (listsize != 0) {
                                model.selectproindex.value = middleElement % listsize
                                model.selectprodata.value =
                                    profileSuggestionAdapter!!.datalist.get(middleElement % profileSuggestionAdapter!!.datalist.size)

                            }
                        }

                    },
                    RecyclerView.SCROLL_STATE_IDLE
                )
            )
        }

        fun bind(data: HomeData, position: Int) {
            binding?.dataset = data
            binding?.viewModel = model
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner
//            itemView.setOnClickListener { itemClick(data, position) }
//            Glide.with(binding!!.itemIv).load(data!!.src).centerInside().into(binding!!.)
        }
    }

    inner class VipDriveHeaderViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_vip_drive_header, parent, false)
    ) {
        private val binding: ItemHomeVipDriveHeaderBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: HomeData, position: Int) {
            binding?.dataset = data
            binding?.viewModel = model
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner
//            itemView.setOnClickListener { itemClick(data, position) }
//            Glide.with(binding!!.itemIv).load(data!!.src).centerInside().into(binding!!.)
        }
    }

    inner class VipDriveRecyclerviewViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_vip_drive_recyclerview, parent, false)
    ) {
        private val binding: ItemHomeVipDriveRecyclerviewBinding? = DataBindingUtil.bind(itemView)

        init {

            vipDriveAdapter = VipDriveAdapter(con, arrayListOf(), model) { data, position ->
                onclickVipDrive?.invoke(data, position)
                    if (application?.quick_view.equals("퀵유저")) {
//                        ShowQuickUserDialog()
                    } else {
                        val i = Intent(con, DriveDetailActivity::class.java)
                        i.putExtra("data", data)
                        i.putExtra("isvip", true)
                        i.putExtra("type", "신규")
                        i.putExtra("page", model.page.value)
                        i.putExtra("allList", vipDriveAdapter!!.data)
                        i.putExtra("driveType","vip")
                        con.startActivity(i)
                    }
            }
            binding!!.rvVipDrive.apply {
                adapter = vipDriveAdapter
                layoutManager = GridLayoutManager(con, 2, GridLayoutManager.VERTICAL, false)
                setHasFixedSize(true)
            }

        }

        fun bind(data: HomeData, position: Int) {
            binding?.dataset = data
            binding?.viewModel = model
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner
//            itemView.setOnClickListener { itemClick(data, position) }
//            Glide.with(binding!!.itemIv).load(data!!.src).centerInside().into(binding!!.)
        }
    }


    inner class MyPickHeaderViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_my_pick_header, parent, false)
    ) {
        private val binding: ItemHomeMyPickHeaderBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: HomeData, position: Int) {
            binding?.dataset = data
            binding?.viewModel = model
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner
//            itemView.setOnClickListener { itemClick(data, position) }
//            Glide.with(binding!!.itemIv).load(data!!.src).centerInside().into(binding!!.)
        }
    }

    inner class MyPickRecyclerviewViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_my_pick_recyclerview, parent, false)
    ) {
        private val binding: ItemHomeMyPickRecyclerviewBinding? = DataBindingUtil.bind(itemView)

        init {
            binding!!.pidpager.apply {

                adapter = SlidePidPagerAdapter(
                    con, lifecycleOwner,
                    model.pidlist.value!!
                ) { feeddata, position ->
                    onclickSlidePid?.invoke(feeddata, position)
                    var istip = feeddata.istip
                    if (!istip) {
                        var rectype: RecDriveDialogType? = RecDriveDialogType.NOT_REC_DIRVE
                        if (application!!.userData?.u_feed.equals("1")) {
                            var piddialog = PidDialog(con, feeddata.type) { event, isok ->
                                if (isok) {
                                    model.FeedProfileFeeedCehck(feeddata.type)
                                }
                            }
                            piddialog.show(fm!!, "")
                        } else {
                            var did = RecDriveDialog(con, rectype!!) { type, okcancel ->
                                if (type == RecDriveDialogType.PROFILE_OPEN) {

                                }
                            }
                            did.show(fm!!, "")
                        }

                    }
                    pageMargin = 20;
                    binding!!.tlDots.setupWithViewPager(binding!!.pidpager)

                }
            }


        }

        fun bind(data: HomeData, position: Int) {
            binding?.dataset = data
            binding?.viewModel = model
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner
//            itemView.setOnClickListener { itemClick(data, position) }
//            Glide.with(binding!!.itemIv).load(data!!.src).centerInside().into(binding!!.)
        }
    }

    inner class BottomBannerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_bottom_banner_multi, parent, false)
    ) {
        private val binding: ItemHomeBottomBannerMultiBinding? = DataBindingUtil.bind(itemView)

        init {

            pagerbanneradapter2 =
                BannerPagerAdapter(con, model.mHomeBanner1List.value!!,"바텀") { data, position ->
                    onclickBottomBanner?.invoke(data, position)
//                if (application?.quick_view.equals("퀵유저")) {
//                    ShowQuickUserDialog()
//                } else {
//                    var bc_link = data.bc_link
//                    if (!bc_link.isNullOrEmpty()) {
//                        viewModel.getOneNoti(bc_link)
//                    }
//                }


                }
            binding!!.pagerBanner2.apply {
                adapter = pagerbanneradapter2
            }
        }

        fun bind(data: HomeData, position: Int) {
            binding?.dataset = data
            binding?.viewModel = model
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner
//            itemView.setOnClickListener { itemClick(data, position) }
//            Glide.with(binding!!.itemIv).load(data!!.src).centerInside().into(binding!!.)
        }
    }

    inner class DriveAwayHeaderViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_drive_away_header, parent, false)
    ) {
        private val binding: ItemHomeAwayDriveHeaderBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: HomeData, position: Int) {
            binding?.dataset = data
            binding?.viewModel = model
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner
//            itemView.setOnClickListener { itemClick(data, position) }
//            Glide.with(binding!!.itemIv).load(data!!.src).centerInside().into(binding!!.)
        }
    }

    inner class DriveAwayRecyclerviewViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_drive_away_recyclerview, parent, false)
    ) {
        private val binding: ItemHomeDriveAwayRecyclerviewBinding? = DataBindingUtil.bind(itemView)

        init {

            pageradapter = DriveAwayPagerAdapter(fm)
            binding!!.pager3.apply {
                adapter = pageradapter
                addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding!!.tabLayout))
                setPagingEnabled(true)
            }

            binding!!.tabLayout.apply {
                addOnTabSelectedListener(object :
                    TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {

                        var position = tab!!.position
                        binding!!.pager3.currentItem = position
                        var now = binding!!.pager3.currentItem
                        pageradapter!!.changeviewtype(now, model.rvItemType.value!!)
                        tab?.position?.let { binding!!.pager3.setCurrentItem(it, false) }
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {

                    }

                    override fun onTabReselected(tab: TabLayout.Tab?) {

                    }

                })

                addTab(binding!!.tabLayout.newTab().setText("NEW"))
                addTab(binding!!.tabLayout.newTab().setText("HOT"))
                addTab(binding!!.tabLayout.newTab().setText("NEAR"))
            }

            binding!!.pager3.offscreenPageLimit = 3
        }

        fun bind(data: HomeData, position: Int) {
            binding?.dataset = data
            binding?.viewModel = model
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner
//            itemView.setOnClickListener { itemClick(data, position) }
//            Glide.with(binding!!.itemIv).load(data!!.src).centerInside().into(binding!!.)
        }
    }

    fun setData() {
        val newData = mutableListOf<HomeData>()
        newData += HomeData("TOP_BANNER", "", null)
        newData += HomeData("SUGESST_HEADER", "", null)
        newData += HomeData("SUGESST_RECYCLERVIEW", "", null)
        newData += HomeData("VIP_DRIVE_HEADER", "", null)
        newData += HomeData("VIP_DRIVE_RECYCLERVIEW", "", null)
        newData += HomeData("MY_PICK_HEADER", "", null)
        newData += HomeData("MY_PICK_RECYCLERVIEW", "", null)
        newData += HomeData("BOTTOM_BANNER", "", null)
        newData += HomeData("AWAY_HEADER", "", null)
        newData += HomeData("AWAY_RECYCLERVIEW", "", null)

        data = newData
    }

}