package com.supercarlounge.supercar.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.view.WindowManager
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.RecentListPagerAdapter
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.data.RecentDriveData
import com.supercarlounge.supercar.databinding.ActivityRecentDriveBinding
import com.supercarlounge.supercar.dialog.HeartPayDialog
import com.supercarlounge.supercar.viewmodel.RecentDriveViewModel

class RecentDriveListActivity: BaseActivitiy() {

    private lateinit var binding: ActivityRecentDriveBinding
    private lateinit var viewModel: RecentDriveViewModel
    var pageradapter : RecentListPagerAdapter? =null
    var fm: FragmentManager? =  null
    var rdata:RecentDriveData?=null
    var type :Int = 0
    var passCheck = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(RecentDriveViewModel::class.java)
        binding = ActivityRecentDriveBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel
         rdata =intent.getParcelableExtra<RecentDriveData>("data")
        viewModel.b_seq.value =intent.getStringExtra("b_seq")
        passCheck = intent.getBooleanExtra("passCheck",false)

        var push = intent.getBooleanExtra("push",false)


        if (push){
            viewModel.recentdata.value = rdata
            type =intent.getIntExtra("type",0)
        }else{
            viewModel.GetRecentData()
            type=1
        }


        if (rdata?.b_title?.isNotEmpty()==true){
            viewModel.titleText.value = rdata?.b_title
        }

        clickListeners()
        observes()


        settext()
        Glide.with(binding.ivCoin).load(R.raw.cash_gif).into(binding.ivCoin)

    }



    @SuppressLint("ResourceType")
    fun settext(){
        var content = "드라이브 패스권을 아시나요?"
        val spannableString = SpannableString(content)
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
            0,
            8,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
//        spannableString.setSpan(
//            RelativeSizeSpan(1.08f),
//            0,
//            5,
//            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
        binding!!.tvMain.text =spannableString
        var content2 = "내 드라이브 게시글을 누가 열람하고 좋아요를 눌렀는지\n확인하실 수 있습니다!"
        val spannableString2 = SpannableString(content2)
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
            0,
            10,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            RelativeSizeSpan(1.08f),
            0,
            10,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding!!.tvSub.text =spannableString2
    }
    private fun SetTabAndViewPager() {


        binding!!.pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding!!.tabLayout))
        binding!!.tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                var position = tab!!.position
                binding!!.pager.currentItem = position
                tab?.position?.let { binding.pager.setCurrentItem(it, false) }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("열람 기록"))
        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("받은 좋아요"))
        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("받은 호감"))
        binding!!.pager.offscreenPageLimit = 1
        binding!!.pager.setPagingEnabled(true)

            binding!!.tabLayout.selectTab(binding!!.tabLayout.getTabAt(type),true)


    }


    private fun setAdapters() {
        fm = supportFragmentManager
        pageradapter =  RecentListPagerAdapter(fm!!,viewModel.recentdata.value,passCheck)
        binding!!.pager.adapter = pageradapter
    }

    private fun observes() {
        viewModel.dialogset.observe(this, Observer {
            if(it){
//                var check_pay =viewModel.recentdata.value!!.b_payment.equals("Y")
//                var check_gender =viewModel.recentdata.value!!.b_same_gender.equals("Y")
//                var heartd = HeartPayDialog(this,check_gender,check_pay){ onoff,c_gender,c_pay->
//                    if(onoff) {
//                        var cgender = if (c_gender) "Y" else "N"
//                        var cpay = if (c_pay) "Y" else "N"
//                        viewModel.HeartPaymentSet(cgender, cpay)
//                    }
//                }
//                heartd.show(supportFragmentManager,"")
                val i= Intent(this, HeartsShopActivity::class.java)
                startActivity(i)
                this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        })
        viewModel.recentdata.observe(this, Observer {data->
            if (data != null){
                viewModel.checked_heart_pay.value = data.b_payment.equals("Y")

            }
            setAdapters()
            SetTabAndViewPager()
        })

        viewModel.toast.observe(this, Observer { toast->
            if(!toast.isNullOrEmpty()){

                Toast.makeText(this,toast, Toast.LENGTH_SHORT).show()
                if (toast.equals("해당 게시글이 없습니다")){
                    finish()
                }
            }
        })

    }

    private fun clickListeners() {
        binding!!.ivBack.setOnClickListener {
            finish()
        }

    }

    override fun finish() {
        super.finish()
        overridePendingTransition(com.supercarlounge.supercar.R.anim. fadein, com.supercarlounge.supercar.R.anim.fadeout)
    }
}
