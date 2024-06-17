package com.supercarlounge.supercar.ui.activity


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.supercarlounge.supercar.Constans
import com.supercarlounge.supercar.DialogEvent
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.adapter.MyCarBestBoardAdapter
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.data.DriveAwayData
import com.supercarlounge.supercar.databinding.ActivityMyCarBestBoardBinding
import com.supercarlounge.supercar.databinding.ActivityMyCarHotBoardBinding
import com.supercarlounge.supercar.dialog.TwoButtonDialog
import com.supercarlounge.supercar.enumset.TimeValue
import com.supercarlounge.supercar.viewmodel.MyCarBestBoardViewModel
import com.supercarlounge.supercar.viewmodel.MyCarBoardViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class MyCarHotBoardActivity : BaseActivitiy(), DialogEvent {

    private lateinit var binding: ActivityMyCarHotBoardBinding
    private lateinit var viewModel: MyCarBoardViewModel
    private lateinit var application: MainApplication
    private var fm: FragmentManager? = null
    var myCarBoardAdapter: MyCarBestBoardAdapter? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        application = applicationContext as MainApplication
        viewModel = ViewModelProvider(this).get(MyCarBoardViewModel::class.java)
        binding = ActivityMyCarHotBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel
        viewModel.my_user_data.value = application.userData



        fm = supportFragmentManager


        clickListeners()
        observes()
        setAdapters()
        viewModel.getMyCarHotList()
//
//        if (viewModel.u_seq.value != viewModel.my_u_seq.value){
//            viewModel.boardViewsUpdate()
////            binding.llViewList.visibility = View.GONE
//        }else{
//
////            binding.llViewList.visibility = View.VISIBLE
//        }
    }


    override fun onResume() {
        super.onResume()

    }





    @RequiresApi(Build.VERSION_CODES.O)
    private fun setAdapters() {



    }

    var listener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
//            var lastposition =
//                (binding?.rvComment?.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
//            var itemcount = cAdapter!!.itemCount
//            Log.d("getDrive : last", "" + itemcount)
//            Log.d("getDrive : itemcount", "" + lastposition)
//            if (itemcount == (lastposition + 1)) {
//                var totalc = viewModel!!.totalCount.value!!
//                if (itemcount < totalc&& totalc !=0) {
//                    if (itemcount < viewModel!!.totalCount.value!!) {
//                        viewModel.page.value =  viewModel.page.value?.plus(1)
//                            viewModel!!.getAnonymousForumPostScroll(viewModel!!.filterArray[viewModel!!.viewType.value!!])
//                            Log.d("getAnonymousForumPost", "getAnonymousForumPostScroll")
//
//                    }
//                }
//        }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
        }
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun observes() {
        viewModel.isnotheart.observe(this, Observer {
            if(it){
                var notheart  = TwoButtonDialog(this, Constans.DIALOG_LACK_HEART,this, Constans.DIALOG_LACK_HEART)
                notheart!!.show(supportFragmentManager,"")
            }
        })
//        viewModel.selectDate.observe(this, Observer {
//            if (it.isNotEmpty()){
//                viewModel?.getMyCarBestBoardList(true)
//            }
//        })
//
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun clickListeners() {
        binding!!.ivBack.setOnClickListener {
            finish()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

            }

        }

    override fun okEvent(
        type: Int,
        okAndCancel: Boolean,
        comment_value: String,
        seq: String,
        u_nickname: String
    ) {

    }


}
