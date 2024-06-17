package com.supercarlounge.supercar.ui.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.TypefaceSpan
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import android.view.WindowManager
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.ProfileRequestPagerAdapter
import com.supercarlounge.supercar.databinding.ActivityProfileRequestBinding
import com.supercarlounge.supercar.viewmodel.ProfileRequestActViewModel
import com.google.android.material.tabs.TabLayout
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.dialog.RecDriveDialog
import com.supercarlounge.supercar.enumset.RecDriveDialogType


class ProfileRequestActivity: BaseActivitiy() {
    var application:MainApplication? = null
    private lateinit var binding: ActivityProfileRequestBinding
    private lateinit var viewModel: ProfileRequestActViewModel
    var pageradapter:ProfileRequestPagerAdapter?=null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewModel = ViewModelProvider(this).get(ProfileRequestActViewModel::class.java)
        binding = ActivityProfileRequestBinding.inflate(layoutInflater)
        application = applicationContext as MainApplication
        viewModel.my_seq.value = application!!.userData!!.u_seq.toString()
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel
        pageradapter = ProfileRequestPagerAdapter(supportFragmentManager!!)
        clickListeners()
        var feed = application!!.userData?.u_feed
        viewModel.ischeck.value = feed.equals( "1")
        binding!!.pager3.adapter = pageradapter
        binding!!.pager3.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding!!.tabLayout))
        binding!!.pager3.setPagingEnabled(true)
        binding!!.tabLayout!!.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

                var position = tab!!.position
                binding!!.pager3.currentItem = position
                var now = binding!!.pager3.currentItem
                tab?.position?.let { binding!!.pager3.setCurrentItem(it, false) }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        Glide.with(binding.ivDrive).load(R.raw.steering_wheel_gif).into(binding.ivDrive)
        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("나를 열람한"))
        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("내가 열람한"))
        binding!!.pager3.offscreenPageLimit = 3
        settextspan()
        observes()
    }
    private fun observes() {
        viewModel.rec_profile_update.observe(this, Observer {
            if (it != null) {
                if (it) {
                    application!!.userData?.u_feed = "1"
                } else {
                    application!!.userData?.u_feed = "0"
                }
            }

        })

        viewModel.isrec_click.observe(this, Observer {
            if(it){
                var rectype : RecDriveDialogType? = RecDriveDialogType.REC_DRIVE_OFF
                var did = RecDriveDialog(this, rectype!!) { type, okcancel ->
                    if(okcancel) {
                        viewModel.onRecChecked(false)
                    }

                }
                did.show(supportFragmentManager, "")
            }
        })
    }
    fun settextspan(){
        var content =getString(R.string.profile_request_text1)
        val spannableString = SpannableString(content)
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
            0,
            10,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding!!.tvSet.text =spannableString

        var content2 =getString(R.string.profile_request_text2)
        val spannableString2 = SpannableString(content2)
        //val fontset = Typeface.createFromAsset(assets, "fonts/optimus_princeps_pemi_bold.ttf")
       var span = TypefaceSpan("optimus_princeps_pemi_bold")
        spannableString2.setSpan(
            span,
            25,
            30,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding!!.tvSet2.text =spannableString2
    }
    override fun onResume() {
        super.onResume()
        viewModel.getrequest()
    }
    private fun clickListeners() {
        binding!!.ivBack.setOnClickListener {
            this.finish()
        }
    }
    override fun finish() {
        super.finish()
      //  overridePendingTransition(com.supercarlounge.supercar.R.anim. fadein, com.supercarlounge.supercar.R.anim.fadeout)
    }

}
