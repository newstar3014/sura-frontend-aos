package com.supercarlounge.supercar.ui.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.supercarlounge.supercar.Constans
import com.supercarlounge.supercar.DialogEvent
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.*
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.data.*
import com.supercarlounge.supercar.databinding.*
import com.supercarlounge.supercar.dialog.DrivePassPayDialog
import com.supercarlounge.supercar.dialog.RecDriveDialog
import com.supercarlounge.supercar.dialog.TwoButtonDialog
import com.supercarlounge.supercar.enumset.DrivePassDialogStatus
import com.supercarlounge.supercar.enumset.RecDriveDialogType
import com.supercarlounge.supercar.viewmodel.*

class DriveDetailActivity: BaseActivitiy(), DialogEvent {
    var application:MainApplication ? =null
    private lateinit var binding: ActivityDriveDetailBinding
    private lateinit var viewModel: DriveDetailViewModel
    private var vipDrivedetailListAdapter: DriveDetailListAdapter? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(DriveDetailViewModel::class.java)
        binding = ActivityDriveDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        application = applicationContext as MainApplication
        binding!!.setLifecycleOwner(this)
        viewModel.my_seq .value= application!!.userData!!.u_seq.toString()
        binding!!.viewModel = viewModel
        val i= intent
        var data=i.getParcelableExtra<DriveAwayData>("data")
        var lset = i.getParcelableArrayListExtra<DriveAwayData>("allList")
        var searchtype = i.getStringExtra("type")
        var nowpage = i.getIntExtra("page",1)
        var isvip = i.getBooleanExtra("isvip",false)
        var totalcount = i.getIntExtra("totalCount",0)
        viewModel.driveType.value = i.getStringExtra("driveType")
        viewModel.isvip.value =isvip
        viewModel.type.value = searchtype
        viewModel.page.value = nowpage
        viewModel.totalCount.value = totalcount
        var lat = "0"
        var lon = "0"
        if(application!!.mLastLocation!= null){
            lat = application!!.mLastLocation!!.latitude.toString()
            lon =  application!!.mLastLocation!!.longitude.toString()
        }
        viewModel.my_lat.value = lat
        viewModel.my_lon.value = lon

        lset?.remove(data)
        lset?.add(0,data)
        if(lset !=null) {
            viewModel.drivelist.value = lset
        }

        clickListeners()
        observes()
        setAdapters()
        application?.setlocation()
    }

    var listener: RecyclerView.OnScrollListener = object: RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            Log.d("스크롤테스트", newState.toString())
            var lastposition=(binding?.rvVipDriveList?.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            var itemcount=vipDrivedetailListAdapter!!.itemCount
            var data = vipDrivedetailListAdapter!!.data.get(lastposition)
            if (newState == 0){
                viewModel.checkviewdrive(data)
            }

            Log.d("getDrive : last" , ""+itemcount)
            Log.d("getDrive : itemcount" , ""+lastposition)
            if(itemcount== (lastposition +1)) {
                var totalc = viewModel.totalCount.value!!
                if (itemcount < totalc&& totalc !=0) {
                    if (itemcount < viewModel.totalCount.value!!) {
                        var npage =  viewModel.page.value!!
                        if(viewModel!!.issend.value == false) {
                            viewModel.page.value = npage + 1
                            viewModel.getDrive(false)
                        }
                    }
                }
            }
        }
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

        }

    }


    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val idata: Intent? = result.data
                if (idata != null) {
                    var data = idata.getParcelableExtra<DriveAwayData>("data")
                    viewModel.driveType.value = "vip"

                }

            }
        }

    var resultLauncherDirect =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val idata: Intent? = result.data
                if (idata != null) {
                    var data = idata.getBooleanExtra("hogamSendCheck",false)
                    var posi = idata.getIntExtra("position",0)
                    Log.d("포지션", posi.toString())
                    Log.d("화면 마지막", data.toString())
                    if (data){

                        viewModel.drivelist.value?.get(posi)?.pc_status = 1
                        viewModel.drivelist.value?.get(posi)?.heart_seq = viewModel.my_seq.value?.toInt()!!
                        var d =   viewModel.drivelist.value?.get(posi)

                        viewModel.drivelist.value =     viewModel.drivelist.value


                        if (d!= null){
                            vipDrivedetailListAdapter?.setindex(posi, d)
                        }

//                        viewModel.drivelist.value =  viewModel.drivelist.value
//                        vipDrivedetailListAdapter.no
                    }

                }

            }
        }

    private fun setAdapters() {

        vipDrivedetailListAdapter = DriveDetailListAdapter(this,this,supportFragmentManager, arrayListOf(), viewModel) { data, position, Tag ,type->
            Log.d("데이타", data.toString())

            if (Tag.equals("삭제")){
                finish()
            }else if (Tag.equals("프로필")){
                val i = Intent(this, ProfileActivity::class.java)
                i.putExtra("sub_seq", data.u_seq.toString())
                if(type.equals("대기중")){
                    viewModel.toast.value = "상대방의 승인을 기다리고 있습니다."
                }else if (type.equals("열람오픈")){
                    i.putExtra("type", type)
                    i.putExtra("send_b_seq",data.b_seq.toString())
                    i.putExtra("nick",data.rn_nickname.toString())
                    i.putExtra("position",position)
                    Log.d("포지션", position.toString())
                    resultLauncherDirect.launch(i)
                } else{
                    startActivity(i)
                }

             //   overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

            }else if (Tag.equals("수정")){
                var intent = Intent(this, NewDriveWritingActivity::class.java)
                intent.putExtra("data", data)
                intent.putExtra("position", position)
                resultLauncherEdit.launch(intent)
            } else if (Tag.equals("맵")){
                var intent = Intent(this, LocationSpotDetailActivity::class.java)
                intent.putExtra("spot_seq", data.spot_seq)
                startActivity(intent)
//                StartNaverMap(data)
            }else{
                val i = Intent(this, DriveVipRankActivity::class.java)
                i.putExtra("sub_seq", data.u_seq.toString())
                i.putExtra("data", data)
                i.putExtra("driveType", "VIP재등록")
                resultLauncher.launch(i)
//                startActivity(i)
             //   overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                Log.d("check", "setAdapters: ")
            }


        }
        binding!!.rvVipDriveList.adapter = vipDrivedetailListAdapter
        binding!!.rvVipDriveList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding!!.rvVipDriveList.addOnScrollListener(listener)
    }
    var resultLauncherEdit = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            if (data != null) {
               var position = data!!.getIntExtra("position",0)
                var driveAwayData =  data!!.getParcelableExtra<DriveAwayData>("data")
                if (driveAwayData != null) {

                    vipDrivedetailListAdapter?.setEditItem(driveAwayData,position)

                }
                   

            }

        }
    }
    private fun observes() {
        viewModel.feed_search_seq.observe(this, Observer { seq->
            if(!seq.isNullOrEmpty()){
                var i  = Intent(this,ProfileActivity::class.java)
                i.putExtra("sub_seq",seq)
                startActivity(i)
            }
        })
        viewModel.toast.observe(this,Observer{
            if(it.isNotEmpty()){

                if (it.equals("하트가 부족합니다. 하트를 충전해주세요.")||it.equals("하트가 부족합니다.")){
                    var drivePassPayDialog = DrivePassPayDialog(this, DrivePassDialogStatus.NOTHEART,"",0) {type, isok->
                        if(isok){
                            val i= Intent(this, HeartsShopActivity::class.java)
                            startActivity(i)
                          //  this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        }

                    }
                    drivePassPayDialog!!.show(supportFragmentManager!!,"")
                }else if (it.equals("over")){

                    var rectype: RecDriveDialogType? = RecDriveDialogType.NOT_REC_DIRVE_2
                    var did = RecDriveDialog(this, rectype!!) { type, okcancel ->

                    }
                    did.show(supportFragmentManager!!, "")
                }else if (it.equals("notexist")){
                    var rectype: RecDriveDialogType? = RecDriveDialogType.NOT_REC_DIRVE
                    var did = RecDriveDialog(this, rectype!!) { type, okcancel ->

                    }
                    did.show(supportFragmentManager!!, "")
                }else{

                    Toast.makeText(this,it,Toast.LENGTH_SHORT).show()

                }

            }
        })
        viewModel!!.issend.observe(this, Observer {
            if(it){
                binding!!.rvVipDriveList.animate().translationY(-200f).setDuration(500).start()
            }else{
                binding!!.rvVipDriveList.animate().translationY(0f).setDuration(500).start()
            }
        })
        viewModel!!.isedit.observe(this,Observer {

        })
        viewModel.isnotheart.observe(this, Observer {
            if(it){
                var notheart  = TwoButtonDialog(this, Constans.DIALOG_LACK_HEART,this, Constans.DIALOG_LACK_HEART)
                notheart!!.show(supportFragmentManager,"")
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
    fun StartNaverMap(data :DriveAwayData){
        var point_lat = data.spot_lat
        var point_lon = data.spot_lon
        var title = data.spot_address
        if(appInstalledOrNot("com.nhn.android.nmap")) {
            var i = Intent(Intent.ACTION_VIEW, Uri.parse("navermaps://?menu=location&pinType=place&lat=${point_lat}&lng=${point_lon}&title=${title}")).apply {
                `package` = "com.nhn.android.nmap"
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(i)
        }else{
            Toast.makeText(this,"해당앱이 없습니다", Toast.LENGTH_SHORT).show()
        }
    }
    private fun appInstalledOrNot(packagename: String): Boolean {
        val pm: PackageManager = packageManager
        try {
            pm.getPackageInfo(packagename,0)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
        }
        return false
    }
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val focusView = currentFocus
        if (focusView != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev.x.toInt()
            val y = ev.y.toInt()
            if (!rect.contains(x, y)) {
                val imm: InputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                if (imm != null) imm.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
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
