package com.supercarlounge.supercar.ui.activity

import android.R
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.adapter.BannerPagerAdapter
import com.supercarlounge.supercar.adapter.ConciergePagerAdapter
import com.supercarlounge.supercar.adapter.LocationNearListAdapter
import com.supercarlounge.supercar.adapter.LocationTotalViewListAdapter
import com.supercarlounge.supercar.adapter.LocationViewListAdapter
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.databinding.ActivityLocationSpotSearchBinding
import com.supercarlounge.supercar.dialog.GuideDialog
import com.supercarlounge.supercar.dialog.OneButtonSetDialog
import com.supercarlounge.supercar.enumset.GuidePopType
import com.supercarlounge.supercar.enumset.PopOneTypeStatus
import com.supercarlounge.supercar.ui.priview.activity.PrvMainActivity
import com.supercarlounge.supercar.viewmodel.LocationFragmentViewModel

class LocationSpotSearchActivity : BaseActivitiy() {

    var application: MainApplication? = null
    private lateinit var binding: ActivityLocationSpotSearchBinding
    private lateinit var viewModel: LocationFragmentViewModel

    var images: List<Bitmap>? =null

    var fm: FragmentManager? =  null
    var pageradapter : ConciergePagerAdapter? =null
    var pagerbanneradapter1 : BannerPagerAdapter? =null
    var pagerbanneradapter2 : BannerPagerAdapter? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationSpotSearchBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(LocationFragmentViewModel::class.java)
        setContentView(binding.root)

        application = this.applicationContext as MainApplication
        viewModel.my_seq.value = application!!.userData?.u_seq.toString()
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        fm = supportFragmentManager

        setViewAdapter()
        setNearAdapter()
        setTotalViewAdapter()
        observe()

        binding.edtSearch.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.eventSearch(true)
                handled = true
            }
            handled
        }

        binding.ivBack.setOnClickListener {
            finish()
        }
    }


    fun observe(){
        viewModel.inviteset.observe(this, Observer {

        })

        viewModel.toast.observe(this, Observer { toast ->
            if (!toast.isNullOrEmpty()) {
                if (toast.equals("아직 VIP 회원이 아닙니다")) {
                    val oneButtonDialog = OneButtonSetDialog(this, PopOneTypeStatus.DIALOG_VIP_CHECK) { type, check ->
                        // Dialog 내용 처리
                    }
                    oneButtonDialog.show(fm!!, "")
                } else {
                    Toast.makeText(this, toast, Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.eventSearch.observe(this, Observer {
            if (it){
                if (application!!.quick_view.equals("퀵유저")) {
                    val i= Intent(this, PrvMainActivity::class.java)
                    application!!.locationSearchText = viewModel.searchValue.value?:"";
                    application!!.locationSearchMove = true
                    i.putExtra("FRAGMENT_ID", 1)
                    i.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    startActivity(i)
                    finish()
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    hideKeyboard(this)
                }else{
                    val i= Intent(this, MainActivity::class.java)
                    application!!.locationSearchText = viewModel.searchValue.value?:"";
                    application!!.locationSearchMove = true
                    i.putExtra("FRAGMENT_ID", 1)
                    i.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    startActivity(i)
                    finish()
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    hideKeyboard(this)
                }
            }
        })
    }


    fun setViewAdapter(){

        val spotadapter = LocationViewListAdapter(this, arrayListOf(),viewModel) { data, index, ->

            val i= Intent(this, LocationSpotDetailActivity::class.java)
            i.putExtra("spot_seq", data.spot_seq)
            startActivity(i)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)

        }
        binding.rvViewList.adapter = spotadapter
        binding.rvViewList.layoutManager =  LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
    }

    fun hideKeyboard(activity: Activity){
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity.window.decorView.applicationWindowToken, 0)
    }

    fun setNearAdapter(){

        val spotadapter = LocationNearListAdapter(this, arrayListOf(),viewModel) { data, index, ->
            if (data.spot_seq.isNullOrEmpty()){
                val i= Intent(this, LocationMainActivity::class.java)
                startActivity(i)
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }else{
                val i= Intent(this, LocationSpotDetailActivity::class.java)
                i.putExtra("spot_seq", data.spot_seq)
                startActivity(i)
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
        }
        binding.rvNear.adapter = spotadapter
        binding.rvNear.layoutManager =  LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
    }

    fun setTotalViewAdapter(){
        val spotadapter = LocationTotalViewListAdapter(this, arrayListOf(),viewModel) { data, index, ->
            val i= Intent(this, LocationSpotDetailActivity::class.java)
            i.putExtra("spot_seq", data.spot_seq)
            startActivity(i)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
        binding.rvTotalView.adapter = spotadapter
        binding.rvTotalView.layoutManager  =
            GridLayoutManager(this, 5, GridLayoutManager.HORIZONTAL, false)
    }

    override fun onResume() {
        super.onResume()
        if(application!!.Check30days(application!!.checkguide7)) {

        }else{
            // 가이드팝업 제거
//            val d = GuideDialog(this, GuidePopType.LOCATION) {
//                application!!.save_guide(GuidePopType.LOCATION)
//            }
//            d.show(fm!!, "")
        }
        val metrisc: DisplayMetrics = resources.displayMetrics
        viewModel.widthPixels.value = metrisc.widthPixels
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            viewModel.isPermission.value =
                ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED||
                        ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
        }else{
            viewModel.isPermission.value =
                ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED||
                        ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        }

        viewModel.getViewList(application!!)
        viewModel.getNearList(true,application!!)
        viewModel.getTotalViewList(application!!)
    }
}