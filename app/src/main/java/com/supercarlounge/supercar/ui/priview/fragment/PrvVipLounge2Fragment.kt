package com.supercarlounge.supercar.ui.priview.fragment


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.supercarlounge.supercar.Constans
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.BannerPagerAdapter
import com.supercarlounge.supercar.adapter.ConciergePagerAdapter
import com.supercarlounge.supercar.data.BannerData
import com.supercarlounge.supercar.data.ConciergeCategoryData
import com.supercarlounge.supercar.databinding.TestFragmentVipLoungeBinding
import com.supercarlounge.supercar.dialog.GuideDialog
import com.supercarlounge.supercar.dialog.OneButtonSetDialog
import com.supercarlounge.supercar.dialog.QuickUserDialog
import com.supercarlounge.supercar.enumset.GuidePopType
import com.supercarlounge.supercar.enumset.PopOneTypeStatus
import com.supercarlounge.supercar.enumset.QuickTypeStatus
import com.supercarlounge.supercar.ui.activity.*
import com.supercarlounge.supercar.ui.priview.adapter.PrvConciergePagerAdapter
import com.supercarlounge.supercar.viewmodel.TestVipLoungeViewModel
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList


class PrvVipLounge2Fragment : Fragment() {
    var application:MainApplication?=null
    private var _binding: TestFragmentVipLoungeBinding? = null
    private val binding get() = _binding
    var viewModel :TestVipLoungeViewModel? = null
    var images: List<Bitmap>? =null
    private var thread : Thread? =null
    private var job : Job? =null

    var fm: FragmentManager? =  null
    var pageradapter : PrvConciergePagerAdapter? =null
    var pagerbanneradapter1 : BannerPagerAdapter? =null
    var pagerbanneradapter2 : BannerPagerAdapter? =null
    private var viewPager: ViewPager2? = null
    private var quickUserDialog :QuickUserDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(TestVipLoungeViewModel::class.java)
        _binding = TestFragmentVipLoungeBinding.inflate(inflater, container, false)
        application = requireContext().applicationContext as MainApplication
        viewModel!!.my_seq.value = application!!.userData!!.u_seq.toString()
        viewModel!!.name.value = application!!.userData!!.u_name
        viewModel!!.image.value =  application!!.userData!!.u_image
        binding!!.viewModel = viewModel
        binding!!.setLifecycleOwner(this)
        val root: View = binding!!.root
        viewModel!!.text.value = "vip라운지"

        return root
    }

    fun observe(){
        viewModel!!.ondial.observe(viewLifecycleOwner, Observer {
            if(it){

                    val number: Uri = Uri.parse("tel:"+"01020675724")
                    val callIntent = Intent(Intent.ACTION_DIAL, number)
                    startActivity(callIntent)


            }
        })
        viewModel!!.viplounge_gauge.observe(viewLifecycleOwner, Observer { percent->
            Log.d("게이지1", percent.toString())
            setVIPAnimation(percent)
        })
        viewModel!!.toast.observe(viewLifecycleOwner, Observer {toast->
            if(!toast.isNullOrEmpty()){

                if (toast.equals("아직 VIP 회원이 아닙니다")){
                   var  oneButtonDialog = OneButtonSetDialog(requireContext(), PopOneTypeStatus.DIALOG_VIP_CHECK){ type, check->

                    }
                    oneButtonDialog!!.show(fm!!,"")
                }else{
                    Toast.makeText(requireContext(), toast, Toast.LENGTH_SHORT).show()
                }
            }
        })
        viewModel!!.viplounge_Check.observe(viewLifecycleOwner, Observer { percent->
            Log.d("게이지2", percent.toString())
            setVIPAnimation(percent)
        } )

        viewModel!!.tablist.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(binding?.tabLayout!!.tabCount==0) {
                setCategory(it)
            }
        })
        viewModel!!.select_bannernoti_data.observe(viewLifecycleOwner, androidx.lifecycle.Observer { data->
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
            requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        })
        viewModel!!.bannerbottom.observe(viewLifecycleOwner, Observer {
            if (it!= null){
                if (it.size != 0){
                    bannerAutoSlide()
                }
            }
        })
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    fun setVIPAnimation(percent :Int){
        job = CoroutineScope(Dispatchers.Main).launch {
            var animation = AnimationDrawable()
            var percents: ArrayList<Int> = arrayListOf()
            var duration = 30
            animation.stop()

            for (i in 0..100){
                var du = 30
                when(i) {
                    20 -> duration = 20
                    40 -> duration = 10
                    else -> {}
                }
                percents?.add(i)
                val name = "vip_$i"
                val drawableResourceId = this@PrvVipLounge2Fragment.resources.getIdentifier(name, "drawable", requireActivity().packageName)
                animation.addFrame(resources.getDrawable(drawableResourceId), duration)

            }
            for (i in 99 downTo percent){
                percents?.add(i)
                val name = "vip_$i"
                val drawableResourceId = this@PrvVipLounge2Fragment.resources.getIdentifier(name, "drawable", requireActivity().packageName)
                animation.addFrame(resources.getDrawable(drawableResourceId), duration)
            }


            binding?.rpm?.setBackgroundDrawable(animation);
            animation.isOneShot = true
            animation.start()

            Log.d("ㅎㅇ", "setVIPanimation: ")
        }
//        delay(2000L) // delay a bit
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fm = childFragmentManager
        setOnclickListeners()
        setadapter()
        observe()

        setbanner()
        setcate()
        viewModel
       // viewModel?.getBannerTop("클럽라운지")
//        viewModel?.getBannerBottom("컨시어지 상단")
//        viewModel?.getCategoryList()

    }

    private fun setOnclickListeners() {

        binding!!.rpm.setOnClickListener {

        }


    }

    override fun onResume() {
        super.onResume()
        Log.d("생명주기", "onResume: ")
        viewModel!!.viewStack.value =1

        viewModel!!.CheckVipType()
        if(application!!.Check30days(application!!.checkguide3)) {

        }else{
            var d = GuideDialog(requireContext(), GuidePopType.GUIDE_VIP_LOUNGE) {
                application!!.save_guide(GuidePopType.GUIDE_VIP_LOUNGE)
            }
            d.show(childFragmentManager, "")
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("생명주기", "onPause: ")
        var animation = AnimationDrawable()
        val name = "vip_0"
        val drawableResourceId = this.resources.getIdentifier(name, "drawable", requireActivity().packageName)
        animation.addFrame(resources.getDrawable(drawableResourceId), 10)
        binding?.rpm?.setBackgroundDrawable(animation);
        job?.cancel() // cancels the job

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("생명주기", "onAttach: ")
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(hidden) {
            return
        }
        Log.d("생명주기", hidden.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun newInstance(): PrvVipLounge2Fragment {
        val args = Bundle()

        val fragment = PrvVipLounge2Fragment()
        fragment.arguments = args
        return fragment
    }
    fun reset(){
        if(viewModel!!.viewStack.value ==2) {
            viewModel!!.viewStack.value = 1
        }
    }

    fun setadapter(){
        pagerbanneradapter1 = BannerPagerAdapter(requireContext(), arrayListOf(),"바텀"){ data, position ->
          ShowQuickUserDialog()

        }
        pagerbanneradapter2= BannerPagerAdapter(requireContext(), arrayListOf(),"바텀"){ data, position ->
            ShowQuickUserDialog()

        }
        binding?.pagerBanner1?.adapter = pagerbanneradapter1
        binding?.pagerBanner2?.adapter = pagerbanneradapter2
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
            if (binding != null) {
                if (viewModel != null) {
                    viewModel!!.bannerTopPosition.value =
                        viewModel!!.bannerTopPosition.value!!.plus(1)
                    var posi = viewModel!!.bannerTopPosition.value!!

                    if (binding!!.pagerBanner1.size != 0) {
                        if (posi < binding!!.pagerBanner1!!.size) {
                            binding!!.pagerBanner1.setCurrentItem(posi, true)
                        } else {
                            viewModel!!.bannerTopPosition.value = 0
                            binding!!.pagerBanner1.setCurrentItem(0, true)
                        }
                    }
                    viewModel!!.bannerBottomPosition.value =
                        viewModel!!.bannerBottomPosition.value!!.plus(1)
                    var posi2 = viewModel!!.bannerBottomPosition.value!!
                    if (posi2 < binding!!.pagerBanner2!!.size) {
                        binding!!.pagerBanner2.setCurrentItem(posi2, true)
                    } else {
                        viewModel!!.bannerBottomPosition.value = 0
                        binding!!.pagerBanner2.setCurrentItem(0, true)
                    }
                }
            }
        }
        var timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 500, 3000)


    }
    fun isAppInstalled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (ex: PackageManager.NameNotFoundException) {
            false
        }
    }
    var   resultLauncherPopUp = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Constans.BRIDGE_MOVE_NOTICE){
            (activity as MainActivity).movepage()
        }
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
        viewModel!!.tablist.value = list
    }
    fun setbanner(){
        var tlist1 = ArrayList<BannerData>()
        tlist1.add(BannerData("",1,"","","https://supercarlounge.com:3000/images/2022-11-18banner/20221118112110배너.png","","","","","",""))
        tlist1.add(BannerData("",1,"","","https://supercarlounge.com:3000/images/2023-05-22banner/20230522112545슈라로또2.png","","","","","",""))
        tlist1.add(BannerData("",1,"","","https://supercarlounge.com:3000/images/2023-05-24banner/20230524105752친구초대배너.png","","","","","",""))

        viewModel!!.bannertop.value = tlist1

        var tlist2 = ArrayList<BannerData>()
        tlist2.add(BannerData("",1,"","","https://supercarlounge.com:3000/images/2022-11-18banner/20221118112110배너.png","","","","","",""))
        tlist2.add(BannerData("",1,"","","https://supercarlounge.com:3000/images/2022-11-18banner/20221118112145배너1.png","","","","","",""))
        viewModel!!.bannerbottom.value = tlist2
    }
    private fun ShowQuickUserDialog() {
        quickUserDialog =
            QuickUserDialog(requireContext(), QuickTypeStatus.QUICK_WARNING) { type, okcancel ->
                if (okcancel) {

                    quickUserDialog!!.dismiss()
                    val i = Intent(requireContext(), JoinActivity::class.java)

                    i.putExtra("u_name", application?.userBasicData?.u_name)
                    i.putExtra("u_gender", application?.userBasicData?.u_gender)
                    i.putExtra("u_birthday", application?.userBasicData?.u_birthday)
                    i.putExtra("u_phone", application?.userBasicData?.u_phone)

                    startActivity(i)
                } else {
                    quickUserDialog!!.dismiss()
                }
            }
        quickUserDialog!!.show(fm!!, "")
    }
}