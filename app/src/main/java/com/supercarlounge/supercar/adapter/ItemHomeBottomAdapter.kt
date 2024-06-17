package com.supercarlounge.supercar.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.ConciergeData
import com.supercarlounge.supercar.databinding.ItemHomeBottomBinding
import com.supercarlounge.supercar.viewmodel.HomeViewModel
import com.google.android.material.tabs.TabLayout
import kotlin.collections.ArrayList


class ItemHomeBottomAdapter(val con: Context, var childFragmentManager : FragmentManager, val data: ArrayList<ConciergeData>, val model: HomeViewModel, val itemClick: (ConciergeData, Int) -> Unit) : RecyclerView.Adapter<ItemHomeBottomAdapter.ViewHolder>() {

    var pageradapter : DriveAwayPagerAdapter? =null
    private var viewPager: ViewPager2? = null
    var fm: FragmentManager? =  null

    override fun getItemCount(): Int {
        return 1
    }

    fun setdata(datalist:ArrayList<ConciergeData>){
        data.clear()
        data.addAll(datalist)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_home_banner_1, parent, false) as ViewGroup
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data.get(position), position)
    }
    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_home_bottom, parent, false)
    ) {
        private val binding: ItemHomeBottomBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: ConciergeData, position: Int) {
            binding?.dataset = data
            binding?.viewModel = model
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner
            itemView.setOnClickListener { itemClick(data, position) }


            initAdpater(binding)


//            Glide.with(binding!!.itemIv).load(data!!.src).centerInside().into(binding!!.)
        }
    }
    private fun initAdpater(binding:ItemHomeBottomBinding){
        fm = childFragmentManager
        //  pageradapter = DriveAwayPagerAdapter(fm!!,viewModel.rvItemType.value!!)
        pageradapter = DriveAwayPagerAdapter(fm!!)
        binding!!.pager3.adapter = pageradapter



        viewPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.d("ViewPagerFragment", "Page ${position + 1}")
            }
        })
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


//            bannerAutoSlide()
//        viewModel.rvItemType.observe(viewLifecycleOwner, Observer {
//            var now = binding!!.pager3.currentItem
//            pageradapter!!.changeviewtype(now,it)
//
//        })

    }





}