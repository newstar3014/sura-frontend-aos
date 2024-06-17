package com.supercarlounge.supercar.ui.priview.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.*
import com.supercarlounge.supercar.data.*
import com.supercarlounge.supercar.ui.activity.PostNoticeActivity
import com.supercarlounge.supercar.viewmodel.ConciergeViewModel
import com.google.android.material.tabs.TabLayout
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.databinding.FragmentPrvconciergeBinding
import com.supercarlounge.supercar.ui.priview.adapter.PrvConciergePagerAdapter
import java.util.*
import kotlin.collections.ArrayList

class PrvConciergeFragment : Fragment() {
    var application:MainApplication?=null
    private lateinit var viewModel: ConciergeViewModel
    private var _binding: FragmentPrvconciergeBinding? = null
    private val binding get() = _binding

    var fm: FragmentManager? =  null
    var pageradapter : PrvConciergePagerAdapter? =null
    var pagerbanneradapter1 : BannerPagerAdapter? =null
    private var viewPager: ViewPager2? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        application = requireContext().applicationContext as MainApplication
        viewModel = ViewModelProvider(this).get(ConciergeViewModel::class.java)
        _binding = FragmentPrvconciergeBinding.inflate(inflater, container, false)
        binding!!.viewModel = viewModel
        binding!!.lifecycleOwner= this

        val root: View = binding!!.root
        return root
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //프래그먼트가 사라질경우  = childFragmentManager
        fm = childFragmentManager

        observe()
        setadapter()
        setOnclickListeners()
        bannerAutoSlide()
        setbanner()
        setcate()
        viewModel.getCategoryList()
    }
    fun setcate(){
        var list:kotlin.collections.ArrayList<ConciergeCategoryData> = arrayListOf()
        list.add(ConciergeCategoryData(0,"전체",))
        list.add(ConciergeCategoryData(1,"병원",))
        list.add(ConciergeCategoryData(2,"패션",))
        list.add(ConciergeCategoryData(3,"차량",))
        list.add(ConciergeCategoryData(4,"여가",))
        list.add(ConciergeCategoryData(5,"푸드",))
        list.add(ConciergeCategoryData(6,"행사",))
        viewModel.tablist.value = list
    }
    fun setbanner(){
        var tlist1 = ArrayList<BannerData>()
        tlist1.add(BannerData("",1,"","","https://supercarlounge.com:3002/images/2022-02-18banner/20220218141843배너7.png","","","","","",""))
        tlist1.add(BannerData("",1,"","","https://supercarlounge.com:3002/images/2022-02-18banner/20220218141852배너8.png","","","","","",""))
        tlist1.add(BannerData("",1,"","","https://supercarlounge.com:3002/images/2022-02-18banner/20220218141900배너9.png","","","","","",""))

        viewModel.bannertop.value = tlist1
    }
    fun observe(){
        viewModel.tablist.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(binding?.tabLayout!!.tabCount==0) {
                setCategory(it)
            }
        })
        viewModel.select_bannernoti_data.observe(viewLifecycleOwner, androidx.lifecycle.Observer { data->
            val i= Intent(requireActivity(), PostNoticeActivity::class.java)

            i.putExtra("n_seq", data.n_seq)
            i.putExtra("u_seq", data.u_seq)
            i.putExtra("n_sday", data.n_sday)
            i.putExtra("n_title", data.n_title)
            i.putExtra("n_eday", data.n_eday)
            i.putExtra("n_type", data.n_type)
            i.putExtra("n_date",data.n_date)
            i.putExtra("n_click",data.n_click)
            application!!.htmlText = data.n_text!!
            startActivity(i)
           // requireActivity().overridePendingTransition(R.anim. fadein, R.anim.fadeout)
        })
    }
    fun setadapter(){
        pagerbanneradapter1 = BannerPagerAdapter(requireContext(), arrayListOf(),"바텀"){ data, position ->
            var bc_link=data.bc_link
            if(!bc_link.isNullOrEmpty()){
                viewModel.getOneNoti(bc_link)
            }

        }

        binding?.pagerBanner1?.adapter = pagerbanneradapter1
        pageradapter =  PrvConciergePagerAdapter(fm!!, arrayListOf())
        binding?.pagerConcierge?.adapter = pageradapter
        viewPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.d("ViewPagerFragment", "Page ${position + 1}")
            }
        })

        binding!!.pagerConcierge.setPagingEnabled(true)

        binding?.pagerConcierge?.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding?.tabLayout))
        binding?.tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                var position = tab!!.position
                binding?.pagerConcierge?.currentItem = position
                tab?.position?.let { binding!!.pagerConcierge.setCurrentItem(it, false) }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

    }
    fun setCategory(list: ArrayList<ConciergeCategoryData>){
        for (i in list){
            binding?.tabLayout?.newTab()?.setText(i.cc_type)?.let { binding?.tabLayout?.addTab(it) }
        }
        binding?.pagerConcierge?.offscreenPageLimit = list.size
    }

    private fun bannerAutoSlide() {

        val handler = Handler()
        val Update = java.lang.Runnable {
            viewModel!!.bannerPosition.value =viewModel!!.bannerPosition.value!!.plus(1)
            var posi = viewModel!!.bannerPosition.value!!
            if (posi <binding!!.pagerBanner1!!.size){
                binding!!.pagerBanner1.setCurrentItem(posi, true)
            }else{
                viewModel!!.bannerPosition.value =0
                binding!!.pagerBanner1!!.setCurrentItem(0, true)
            }


        }
        var timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 500, 3000)


    }
    private fun setOnclickListeners() {

    }




    override fun onResume() {
        super.onResume()
    }
    override fun onDestroyView() {
        super.onDestroyView()

    }
    fun newInstance(): PrvConciergeFragment {
        val args = Bundle()

        val fragment = PrvConciergeFragment()
        fragment.arguments = args
        return fragment
    }


}