package com.supercarlounge.supercar.ui.priview.activity


import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.Window
import androidx.navigation.ui.AppBarConfiguration
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.ui.priview.adapter.PrvGuidePagerAdapter
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.databinding.ActivityPrvGuideBinding


class PrvGuideActivity : BaseActivitiy() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityPrvGuideBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        with(window) {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            // set an slide transition
            enterTransition = Slide(Gravity.END)
            exitTransition = Slide(Gravity.START)
        }

        binding = ActivityPrvGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding!!.setLifecycleOwner(this)

        binding!!.pagerGuide.adapter = PrvGuidePagerAdapter(this, arrayListOf(R.drawable.im_home_guide1_n,R.drawable.im_home_guide2_n,R.drawable.img_quickguide_2,R.drawable.img_quickguide_3
            ,R.drawable.img_quickguide_4,R.drawable.img_quickguide_5
            ,R.drawable.img_quickguide_8_n,R.drawable.img_quickguide_9_n
            ,R.drawable.img_quickguide_10,R.drawable.img_quickguide_11,R.drawable.img_quickguide_12)){ data,index ->

        }
        binding.close.setOnClickListener {
            finish()
        }
    }




    override fun onPause() {
        super.onPause()
      //  overridePendingTransition(R.anim. fadein, R.anim.fadeout)
    }
    override fun finish() {
        super.finish()
     //   overridePendingTransition(com.supercarlounge.supercar.R.anim. fadein, com.supercarlounge.supercar.R.anim.fadeout)
    }
}
