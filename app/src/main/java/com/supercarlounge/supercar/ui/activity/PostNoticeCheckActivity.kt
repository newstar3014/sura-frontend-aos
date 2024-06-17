package com.supercarlounge.supercar.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import android.view.WindowManager
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.databinding.ActivityPostNoticeCheckBinding
import com.supercarlounge.supercar.viewmodel.PostNoticeCheckViewModel

class PostNoticeCheckActivity: BaseActivitiy() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityPostNoticeCheckBinding
    private lateinit var viewModel: PostNoticeCheckViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(PostNoticeCheckViewModel::class.java)
        binding = ActivityPostNoticeCheckBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel

        val i= intent
        viewModel.titleText.value =i.getStringExtra("titleText")
        viewModel.mainText.value =i.getStringExtra("mainText")
        viewModel.subText.value = i.getStringExtra("subText")



        viewModel.titleText.value = "초대코드"
        clickListeners()
        observes()

    }

    private fun observes() {

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
