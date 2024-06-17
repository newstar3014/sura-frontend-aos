package com.supercarlounge.supercar.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.Constans
import com.supercarlounge.supercar.DialogEvent
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.AlimAdapter
import com.supercarlounge.supercar.adapter.PointLogAdapter
import com.supercarlounge.supercar.adapter.PointProductAdapter
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.databinding.ActivityAlimBinding
import com.supercarlounge.supercar.databinding.ActivityPointmallBinding
import com.supercarlounge.supercar.databinding.ActivityPointmallLogBinding
import com.supercarlounge.supercar.dialog.PointOrderPopDialog
import com.supercarlounge.supercar.viewmodel.AlimViewModel
import com.supercarlounge.supercar.viewmodel.PointMallLogViewModel
import com.supercarlounge.supercar.viewmodel.PointMallViewModel

class PointMallLogActivity : BaseActivitiy(), DialogEvent {
    private lateinit var binding: ActivityPointmallLogBinding
    private lateinit var viewModel: PointMallLogViewModel
    private lateinit var  adapter : PointLogAdapter
    var application: MainApplication? =null
    var orderpop:PointOrderPopDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PointMallLogViewModel::class.java)
        binding = ActivityPointmallLogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        application = applicationContext as MainApplication
        viewModel.my_seq.value = application!!.userData!!.u_seq.toString()
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel
        setclick()
        setadapter()
        Glide.with(binding.ivLoading).load(R.raw.loading_anim).into(binding.ivLoading)
        viewModel.getpointlog(true)
        viewModel.GetMyPoint()

    }
    var listener: RecyclerView.OnScrollListener = object: RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            var lastposition=(binding?.rvPointlog?.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            var itemcount=adapter!!.itemCount
            Log.d("getlog : last" , ""+itemcount)
            Log.d("getlog : itemcount" , ""+lastposition)
            if(itemcount== (lastposition +1)) {
                var totalc = viewModel!!.totalCount.value!!
                if (itemcount < totalc&& totalc !=0) {
                    if (itemcount < viewModel!!.totalCount.value!!) {
                        if(viewModel!!.issend.value == false) {
                           var page = viewModel.nowpage.value!!
                            viewModel.nowpage.value = page+1
                            viewModel!!.getpointlog(false)
                        }
                    }
                }
            }
        }
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
        }
    }
    fun setadapter(){
        adapter = PointLogAdapter(this,viewModel.logdata.value!!,viewModel){data,view,posi->

        }
        binding!!.rvPointlog.adapter = adapter
        binding!!.rvPointlog.layoutManager = LinearLayoutManager(this)
        binding!!.rvPointlog.addOnScrollListener(listener)
    }
    fun setclick(){
        binding!!.ivBack.setOnClickListener {
            finish()
        }
        binding!!.llOrder.setOnClickListener {
            showorderpop()
        }
    }
    fun showorderpop(){
        val originalPos = IntArray(2)

        binding!!.llOrder.getLocationInWindow(originalPos)
        val x =  originalPos[0]
        val y = originalPos[1]
        viewModel.isorderup.value = true
        orderpop = PointOrderPopDialog(x,y,this,this)
        orderpop!!.show(supportFragmentManager,"")
    }

    override fun okEvent(
        type: Int,
        okAndCancel: Boolean,
        comment_value: String,
        seq: String,
        u_nickname: String
    ) {
        if (okAndCancel){
            if(type == Constans.ORDER_MORE_ALL){
                viewModel.order.value = "전체"
            }else if (type == Constans.ORDER_MORE_SAVE){
                viewModel.order.value = "적립"
            }else if (type == Constans.ORDER_MORE_DEDUCTION){
                viewModel.order.value = "차감"
            }
            viewModel.resetpage()
            viewModel.getpointlog(true)
        }
        viewModel.isorderup.value = false
    }
}