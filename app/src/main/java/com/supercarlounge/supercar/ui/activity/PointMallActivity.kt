package com.supercarlounge.supercar.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.AlimAdapter
import com.supercarlounge.supercar.adapter.PointProductAdapter
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.databinding.ActivityAlimBinding
import com.supercarlounge.supercar.databinding.ActivityPointmallBinding
import com.supercarlounge.supercar.viewmodel.AlimViewModel
import com.supercarlounge.supercar.viewmodel.PointMallViewModel

class PointMallActivity : BaseActivitiy() {
    private lateinit var binding: ActivityPointmallBinding
    private lateinit var viewModel: PointMallViewModel
    private lateinit var  adapter : PointProductAdapter
    var application: MainApplication? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PointMallViewModel::class.java)
        binding = ActivityPointmallBinding.inflate(layoutInflater)
        setContentView(binding.root)
        application = applicationContext as MainApplication
        viewModel.my_seq.value = application!!.userData!!.u_seq.toString()
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel
        setclick()
        setadapter()
        setobserver()
        Glide.with(binding.ivLoading).load(R.raw.loading_anim).into(binding.ivLoading)
        viewModel.getproduct(true )
        viewModel.GetMyPoint()


    }
    var listener: RecyclerView.OnScrollListener = object: RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            var lastposition=(binding?.rvProduct?.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            var itemcount=adapter!!.itemCount
            Log.d("getproduct : last" , ""+itemcount)
            Log.d("getproduct : itemcount" , ""+lastposition)
            if(itemcount== (lastposition +1)) {
                var totalc = viewModel!!.totalCount.value!!
                if (itemcount < totalc&& totalc !=0) {
                    if (itemcount < viewModel!!.totalCount.value!!) {
                        if(viewModel!!.issend.value == false) {

                            viewModel!!.getproduct(false)
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
        adapter = PointProductAdapter(this,viewModel.productlist.value!!,viewModel){data,view,posi->
            if(view.id == R.id.ll_trade){
                viewModel.BuyProduct(data.goodsNo!!,data.goodsCode!!)
            }else{
               // Toast.makeText(this,"확인",Toast.LENGTH_SHORT).show()
            }
        }
        binding!!.rvProduct.adapter = adapter
        binding!!.rvProduct.layoutManager = LinearLayoutManager(this)
        binding!!.rvProduct.addOnScrollListener(listener)
    }
    fun setclick(){
        binding!!.ivBack.setOnClickListener {
            finish()
        }
    }
    fun setobserver(){
        viewModel.toast.observe(this, Observer { message->
            if (message.isNullOrEmpty() == false){
                runOnUiThread {
                    Toast.makeText(this@PointMallActivity,message,Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}