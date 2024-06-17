package com.supercarlounge.supercar.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import android.view.WindowManager
import com.supercarlounge.supercar.Constans
import com.supercarlounge.supercar.DialogEvent
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.VipDriveRankAdapter
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.databinding.*
import com.supercarlounge.supercar.dialog.*
import com.supercarlounge.supercar.enumset.PopOneTypeStatus
import com.supercarlounge.supercar.enumset.VipRankStatus
import com.supercarlounge.supercar.viewmodel.*

class DriveVipRankActivity: BaseActivitiy(), DialogEvent {

    var application:MainApplication ? =null
    private lateinit var binding: ActivityVipDriveRankBinding
    private lateinit var viewModel: DriveVipRankViewModel
    private var rAdapter : VipDriveRankAdapter? =null
    private var oneButtonDialog : OneButtonSetDialog? =null
    var dialogDriveExposure : DriveExposureDialog? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(DriveVipRankViewModel::class.java)
        binding = ActivityVipDriveRankBinding.inflate(layoutInflater)
        setContentView(binding.root)
        application = applicationContext as MainApplication
        viewModel.my_seq .value = application!!.userData!!.u_seq.toString()
        binding!!.setLifecycleOwner(this)
        var type = intent.getBooleanExtra("inputtype",false)
        viewModel.data.value = intent.getParcelableExtra("data")
        viewModel.isinputtype.value = type
        binding!!.viewModel = viewModel
        if (type ) viewModel.titleText.value = "드라이브 등록"

//        var type = intent.getStringExtra("inputtype",false)
        viewModel.getrank()
        viewModel.checkvipdrive()
        clickListeners()

        setAdapters()
        observes()
//        var driveRegiDialog = DriveRegiDialog(this){ onoff,c_gender,c_pay2,c_pay1->
//            if(onoff) {
//
//            }
//        }
//        driveRegiDialog.show(supportFragmentManager,"")
    }
    private fun setAdapters() {
        rAdapter = VipDriveRankAdapter(this, arrayListOf()) { data, position ->

        }
        binding.rvVipDrive.adapter = rAdapter
        binding.rvVipDrive.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    }
    private fun observes() {
        viewModel.vipranklist.observe(this,Observer{
            if(it.size != 0){

                Glide.with(binding!!.ivSt1).load(it[0].b_cover).override(200,200).centerInside().circleCrop().into(binding!!.ivSt1)
                if(it.size > 1){
                    Glide.with(binding!!.ivSt2).load(it[1].b_cover).override(200,200).centerInside().circleCrop().into(binding!!.ivSt2)
                }
                if(it.size > 2){
                    Glide.with(binding!!.ivSt3).load(it[2].b_cover).override(200,200).centerInside().circleCrop().into(binding!!.ivSt3)
                }
            }

        })
        viewModel.isvipcheck.observe(this,Observer{
                if(viewModel.isinputtype.value == true){
                    if(it != VipRankStatus.DEFAULT) {
                        viewModel.myHeartCheck(viewModel.my_seq.value!!)
                    }
                }else {
                    Log.d("테스트1", "observes: ")
                    if (it == VipRankStatus.VIP_EXIST) {

//                        DriveRankUpDialog(
//                            this,
//                            Constans.DIALOG_DRIVE_EXPOSURE_1,
//                            viewModel.driveAuctionData.value!!,
//                            viewModel.heart.value!!.toInt()
//                        ){b ->
//                            if (b){
//                                viewModel.RankUpVipDrive(viewModel?.driveAuctionData?.value!!.da_seq.toString())
//                            }
//
//                        }.show(supportFragmentManager, "")
                        oneButtonDialog = OneButtonSetDialog(this, PopOneTypeStatus.DIALOG_VIP_OVERLAP){ type, check->

                        }
                        oneButtonDialog!!.show(this.supportFragmentManager,"")
                    } else if (it == VipRankStatus.VIP_NOT_EXIST) {
                        viewModel.myHeartCheck(viewModel.my_seq.value!!)

                    }else if (it == VipRankStatus.VIP_OVERLAP){
                        oneButtonDialog = OneButtonSetDialog(this, PopOneTypeStatus.DIALOG_VIP_OVERLAP){ type, check->

                        }

                        oneButtonDialog!!.show(this.supportFragmentManager,"")

                    }
                }

        })
        viewModel.toast.observe(this,Observer{
            if(viewModel.isstart.value!!) {
                if (it.equals("보유 하트가 부족합니다.")){
                    var notheart  = TwoButtonDialog(this,Constans.DIALOG_LACK_HEART,this,Constans.DIALOG_LACK_HEART)
                    notheart!!.show(supportFragmentManager,"")
//                    var drivepassdialog = DrivePassDialog(this) { isok->
//                        if(isok){
//                            val i= Intent(this, HeartsShopActivity::class.java)
//                            startActivity(i)
//                            this.overridePendingTransition(R.anim. fadein, R.anim.fadeout)
//                        }
//
//                    }
//                    drivepassdialog!!.show(supportFragmentManager,"")
                }else{
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                }

            }else{
                viewModel.isstart.value = true
            }
        })
        viewModel.myHeart.observe(this, Observer {
            if(it!= null){
                var heart = viewModel.heart.value?.toInt()
                 if (it >= heart!!) {
                     if (viewModel.data.value != null){
                         viewModel.VipDriveUpload(viewModel.data.value?.b_seq.toString())
                     }else{
                         var iset = Intent(this@DriveVipRankActivity, NewDriveWritingActivity::class.java)
                         iset.putExtra("isagree", viewModel.checked1.value)
                         iset.putExtra("vipheart", viewModel.heart.value?.toInt())
                         iset.putExtra("viptype", 1)
                         iset.putExtra("c", true)
                         if (viewModel.isinputtype.value == true){
                             setResult(Activity.RESULT_OK, iset)
                             Log.d("확인용", ": 4")
                             finish()
                         }else{
                             resultLauncher.launch(iset)

                         }
                     }


//                    startActivity(iset)

//                    setResult(Activity.RESULT_OK, iset)
                     Log.d("적은 하트", viewModel.heart.value.toString())
                }else{
                    viewModel.toast.value = "보유 하트가 부족합니다."
                }
            }
        })

        viewModel.sendsuccess.observe(this, Observer {
            if (it.isNotEmpty()){
                if (it.equals("vip")){
                    viewModel.toast.value = "성공적으로 VIP 게시글이 등록 되었습니다."
                    var i = Intent().apply {
                        putExtra("data", viewModel.data.value)
                    }
                    setResult(Activity.RESULT_OK,i)
                    Log.d("확인용", ": 3")
                    finish()
                }else{
                    viewModel.toast.value = "VIP 게시글 등록을 실패 하였습니다."
                }
            }
        })
        viewModel.vipDrvieFull.observe(this,Observer{

            if (it== 2){
                Log.d("나의하트", viewModel.myHeartCount.value.toString())
                Log.d("최소하트", viewModel.minHeartCount.value.toString())

                if (viewModel.minHeartCount.value!! > viewModel.myHeartCount.value!!){
                    var deficientCount =  viewModel.minHeartCount.value!!.minus(viewModel.myHeartCount.value!!)
                    Log.d("테스트2", "observes: ")
                    dialogDriveExposure = DriveExposureDialog(
                        this,
                        Constans.DIALOG_DRIVE_EXPOSURE_2,
                        application!!.userData?.u_seq.toString()
                        ,deficientCount
                    ){ type, isonoff ,addHeart->
                        if (type == Constans.DIALOG_DRIVE_EXPOSURE_2){
                            if (isonoff){
                                  viewModel.heart.value =addHeart
                                viewModel.RankUpVipDrive(viewModel.driveAuctionData.value?.da_seq.toString())
                                dialogDriveExposure?.dismiss()
                            }else{
                                dialogDriveExposure?.dismiss()
                            }

                        }
                    }
                    dialogDriveExposure!!.show(supportFragmentManager, "")

                }
            }
        })
        viewModel.isnotheart.observe(this, Observer {
            if(it){
                var notheart  = TwoButtonDialog(this,Constans.DIALOG_LACK_HEART,this,Constans.DIALOG_LACK_HEART)
                notheart!!.show(supportFragmentManager,"")
            }
        })
//        viewModel.myReallyHeartCount.observe(this, Observer {
//            if(it != null){
//                if (application!!.Check30days(application!!.drive_check_date)|| application!!.Check1days(application!!.drive_check_date)) {
//                    var i = Intent(this, DriveWritingActivity::class.java)
//                    i.putExtra("viptype", 1)
//                    i.putExtra("c", true)
//                    i.putExtra("vipheart", viewModel.heart.value)
//                    startActivity(i)
//                } else {
//                    var i = Intent(this, PostDriveCheckActivity::class.java)
//                    var data = viewModel.driveAuctionData.value
//                    i.putExtra("viptype", 1)
//                    i.putExtra("vipheart", viewModel.heart.value)
//                    startActivity(i)
//                }
//
//            }
//
////            }
//        })


        viewModel.driveAuctionData.observe(this, Observer {
            if(it != null){
//                if (viewModel.vipDrvieFull.value == true){
//                    Log.d("나의하트", viewModel.myHeartCount.value.toString())
//                    Log.d("최소하트", viewModel.minHeartCount.value.toString())
//                    if (viewModel.minHeartCount.value!! == viewModel.myHeartCount.value!!){
//                        var deficientCount =  viewModel.minHeartCount.value!!.minus(viewModel.myHeartCount.value!!)
//
//                        dialogDriveExposure = DriveExposureDialog(
//                            this,
//                            Constans.DIALOG_DRIVE_EXPOSURE_2,
//                            application!!.userData?.u_seq.toString()
//                            ,deficientCount
//                        ){ type, isonoff ->
//                            if (type == Constans.DIALOG_DRIVE_EXPOSURE_2){
//                                dialogDriveExposure?.dismiss()
//                            }
//                        }
//                        dialogDriveExposure!!.show(supportFragmentManager, "")
//
//                    }

                }

//            }
        })
    }
    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                if (data != null) {
                    Log.d("확인용", ":2 ")
                    data!!.data.let { it->
                        finish()

                    }
                }

            }
        }

    private fun clickListeners() {
        binding!!.ivBack.setOnClickListener {
            Log.d("확인용", ": 1")
            finish()
        }
    }

    override fun finish() {
        super.finish()
       // overridePendingTransition(com.supercarlounge.supercar.R.anim. fadein, com.supercarlounge.supercar.R.anim.fadeout)
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
