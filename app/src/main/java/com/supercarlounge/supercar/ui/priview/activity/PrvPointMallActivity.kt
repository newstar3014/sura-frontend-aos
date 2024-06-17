package com.supercarlounge.supercar.ui.priview.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
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
import com.supercarlounge.supercar.data.PointMallProductData
import com.supercarlounge.supercar.databinding.ActivityAlimBinding
import com.supercarlounge.supercar.databinding.ActivityPointmallBinding
import com.supercarlounge.supercar.dialog.QuickUserDialog
import com.supercarlounge.supercar.enumset.QuickTypeStatus
import com.supercarlounge.supercar.ui.activity.JoinActivity
import com.supercarlounge.supercar.viewmodel.AlimViewModel
import com.supercarlounge.supercar.viewmodel.PointMallViewModel

class PrvPointMallActivity : BaseActivitiy() {
    private lateinit var binding: ActivityPointmallBinding
    private lateinit var viewModel: PointMallViewModel
    private lateinit var  adapter : PointProductAdapter
    var application: MainApplication? =null
    private var quickUserDialog: QuickUserDialog? = null
    var fm: FragmentManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fm = supportFragmentManager
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
        adddata()



    }

    fun setadapter(){
        adapter = PointProductAdapter(this,viewModel.productlist.value!!,viewModel){data,view,posi->
           ShowQuickUserDialog()
        }
        binding!!.rvProduct.adapter = adapter
        binding!!.rvProduct.layoutManager = LinearLayoutManager(this)

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
                    Toast.makeText(this@PrvPointMallActivity,message,Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun adddata(){
        var list :ArrayList<PointMallProductData> = arrayListOf()
        list.add(PointMallProductData("","","","","","","","","","https://biz.giftishow.com/Resource/goods/2022/G00001311585/G00001311585.jpg","","콜드 브루 플로트 T","","스타벅스","4500"))
        list.add(PointMallProductData("","","","","","","","","","https://biz.giftishow.com/Resource/goods/2022/G00001311585/G00001311585.jpg","","콜드 브루 플로트 T","","스타벅스","6400"))
        list.add(PointMallProductData("","","","","","","","","","https://biz.giftishow.com/Resource/goods/2022/G00001305724/G00001305724.jpg","","콜드 브루 몰트 T","","스타벅스","4800"))
        viewModel.productlist.value = list
    }
    private fun ShowQuickUserDialog() {
        quickUserDialog =
            QuickUserDialog(this, QuickTypeStatus.QUICK_WARNING) { type, okcancel ->
                if (okcancel) {

                    quickUserDialog!!.dismiss()
                    val i = Intent(this, JoinActivity::class.java)

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