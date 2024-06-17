package com.supercarlounge.supercar.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.adapter.DriveAwayAdapter
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.databinding.ActivityCouponBinding
import com.supercarlounge.supercar.databinding.ActivityDriveAwayListBinding
import com.supercarlounge.supercar.databinding.FragmentDriveAwayNewBinding
import com.supercarlounge.supercar.dialog.QuickUserDialog
import com.supercarlounge.supercar.enumset.QuickTypeStatus
import com.supercarlounge.supercar.viewmodel.CouponViewModel
import com.supercarlounge.supercar.viewmodel.DriveAwayListViewModel

class DriveAwayListActivity: BaseActivitiy() {

    var application:MainApplication?=null
    private lateinit var binding: ActivityDriveAwayListBinding
    private lateinit var viewModel: DriveAwayListViewModel
    private var adapter: DriveAwayAdapter? = null
    private var quickUserDialog: QuickUserDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(DriveAwayListViewModel::class.java)
        binding = ActivityDriveAwayListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel
        application = applicationContext as MainApplication
        viewModel.my_seq.value = application!!.userData!!.u_seq.toString()
        clickListeners()
        observes()
        setAdapter()
        val i= intent
        var v = i.getStringExtra("type")
        viewModel.choice.value = v
        Log.d("", viewModel.choice.value.toString())
        when(viewModel?.choice?.value){
            "신규"->{
                viewModel.title.value = "신규 드라이브 목록"
            }
            "인기"->{
                viewModel.title.value = "인기 드라이브 목록"
            }
            "근처"->{
                viewModel.title.value = "근처 드라이브 목록"
            }
        }
        viewModel.getDrive(viewModel.my_seq.value.toString(), false,binding!!)

        //binding!!.rvDriveAwayNew.isNestedScrollingEnabled = false

    }
    var listener = object : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            var lastposition =
                (binding?.rvDriveAwayList?.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            var itemcount = adapter!!.itemCount
            Log.d("스크롤홈확인용3 : last","" )




            if (itemcount == (lastposition + 1)) {
                var totalc = viewModel.totalCount.value!!
                if (itemcount < totalc && totalc != 0) {
                    if (itemcount < viewModel.totalCount.value!!) {
                        var npage = viewModel.page.value!!
                        if (viewModel.issend.value == false) {
                            viewModel.page.value = npage + 1
                            viewModel.getDrive(viewModel.my_seq.value.toString(), false,binding!!)

                        }


                    }
                }
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter() {
        adapter = DriveAwayAdapter(
            this,
            arrayListOf(),
            viewModel.rvItemType.value!!
        ) { data, position ->
            if (application?.quick_view.equals("퀵유저")) {
                ShowQuickUserDialog()
            } else {
                val i = Intent(this, DriveDetailActivity::class.java)
                i.putExtra("data", data)
                i.putExtra("type", "신규")
                i.putExtra("totalCount", viewModel.totalCount.value)
                i.putExtra("page", viewModel.page.value)
                i.putParcelableArrayListExtra("allList", adapter?.data)
                i.putExtra("driveType", "notvip")
                startActivity(i)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }

        }
         binding!!.rvDriveAwayList.adapter = adapter
         binding!!.rvDriveAwayList.addOnScrollListener(listener)
         binding!!.rvDriveAwayList.layoutManager = GridLayoutManager(this, 3)
               //    binding!!.rvDriveAwayNew.edgeEffectFactory = BounceEdgeEffectFactory()
            if (binding!!.rvDriveAwayList.itemDecorationCount != 0) {
                    binding!!.rvDriveAwayList.removeItemDecorationAt(0)
               }
              application!!.setlistview_padding(8, binding!!.rvDriveAwayList, 3, false)
             binding!!.rvDriveAwayList.adapter!!.notifyDataSetChanged()

    }

    fun changeScroll() {
                if (binding != null) {
            Log.d("스크롤확인용", "changeScroll: 11")
            var itemcount = adapter!!.itemCount
            var totalc = viewModel.totalCount.value!!
            if (itemcount < totalc&& totalc !=0) {
                if (itemcount < viewModel.totalCount.value!!) {
                    var npage =  viewModel.page.value!!
                    if(viewModel.issend.value == false) {
                        viewModel.page.value = npage + 1
                        viewModel.getDrive(viewModel.my_seq.value.toString(), false, binding)
                    }
                }
            }


        }

    }
    private fun observes() {
        viewModel.toast.observe(this,Observer{ toast->
            if(!toast.isNullOrEmpty()){
                Toast.makeText(this,toast,Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.isfinish.observe(this,Observer{
            if(it) {
                finish()
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
      //  overridePendingTransition(com.supercarlounge.supercar.R.anim. fadein, com.supercarlounge.supercar.R.anim.fadeout)
    }


    private fun ShowQuickUserDialog() {
        quickUserDialog =
            QuickUserDialog(this, QuickTypeStatus.QUICK_WARNING) { type, okcancel ->
                if (okcancel) {
                    quickUserDialog!!.dismiss()
                    val i = Intent(this, JoinActivity::class.java)
                    i.putExtra("qickview", "퀵유저")
                    i.putExtra("u_name", application?.userBasicData?.u_name)
                    i.putExtra("u_gender", application?.userBasicData?.u_gender)
                    i.putExtra("u_birthday", application?.userBasicData?.u_birthday)
                    i.putExtra("u_phone", application?.userBasicData?.u_phone)
                    Log.d("아이템3", application?.userBasicData.toString())
                    startActivity(i)
                } else {
                    quickUserDialog!!.dismiss()
                }
            }
        quickUserDialog!!.show(supportFragmentManager, "")
    }

}
