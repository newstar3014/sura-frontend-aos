package com.supercarlounge.supercar.ui.activity


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import android.view.WindowManager
import com.supercarlounge.supercar.Constans
import com.supercarlounge.supercar.DialogEvent
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.databinding.ActivityRpmBinding
import com.supercarlounge.supercar.dialog.DrivePassPayDialog
import com.supercarlounge.supercar.dialog.RpmDialog
import com.supercarlounge.supercar.dialog.TwoButtonDialog
import com.supercarlounge.supercar.enumset.DrivePassDialogStatus
import com.supercarlounge.supercar.enumset.RpmStatus
import com.supercarlounge.supercar.viewmodel.RpmViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class RpmActivity : BaseActivitiy(), DialogEvent {
    var application:MainApplication?=null
    private lateinit var binding: ActivityRpmBinding
    private lateinit var viewModel: RpmViewModel
    private var job : Job? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(RpmViewModel::class.java)
        binding = ActivityRpmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)
        application = applicationContext as MainApplication
        var mseq= application!!.userData!!.u_seq.toString()
        var profile = intent.getStringExtra("profile")
        var sub_seq = intent.getStringExtra("sub_seq")
        var type = intent.getIntExtra("type",1)
        var rpm = intent.getIntExtra("rpm",0)
        viewModel.rpmindex.value= rpm
        viewModel.rpminputtype.value = type
        viewModel.my_seq.value = mseq
        viewModel.sub_seq.value = sub_seq
        viewModel.profile.value = profile
        if(type == 0){
            viewModel.rpmstatus.value = RpmStatus.VIEW
            setRpmAnimation(rpm)
        }else if(type == 1){
            viewModel.rpmstatus.value = RpmStatus.RPM_SET
        }else if(type == 2) {
            viewModel.rpmstatus.value = RpmStatus.RPM_COMPLETE
            viewModel.GetRpmScore()
        }else if( type == 3){
            viewModel.rpmstatus.value = RpmStatus.RPM_SET
        }else if( type == 4){
            viewModel.rpmstatus.value = RpmStatus.RPM_COMPLETE
            viewModel.GetRpmScore()
        }
        Log.d("RPM_COMPLETE1", viewModel.rpmstatus.value.toString())
        binding!!.viewModel = viewModel
        observer()
        clickListeners()

    }

    var mCurrAngle:Double = 0.0
    @SuppressLint("DefaultLocale", "IntentReset")
    private fun clickListeners(){

        binding.ivBack.setOnClickListener {
            finish()
        }

            binding.ivRpm.setOnTouchListener(object : OnTouchListener {
                override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                    if(viewModel.rpmstatus.value == RpmStatus.RPM_SET) {
                        var centerOfWidth: Double = (p0!!.width / 2).toString().toDouble()
                        var centerOfHeight: Double = (p0!!.height / 2).toString().toDouble()
                        var x: Double = p1!!.x.toString().toDouble()
                        var y: Double = p1!!.y.toString().toDouble()

                        when (p1.getAction()) {
                            MotionEvent.ACTION_DOWN -> {
                                mCurrAngle =
                                    Math.toDegrees(
                                        Math.atan2(
                                            x - centerOfWidth,
                                            centerOfHeight - y
                                        )
                                    )
//                         viewModel.rpm_subtext.value = ""+mCurrAngle
                                settouchrpm(mCurrAngle)
                            }
                            else -> {}
                        }

                    }
                    return true
                }


            })

    }
    @SuppressLint("SuspiciousIndentation")
    fun observer(){
        viewModel.rpmindex.observe(this,Observer{ index->
            if(viewModel.rpmstatus.value == RpmStatus.RPM_SET) {
                setRpmmoveAnimation(viewModel.rpm_prvindex.value!!,index)
            }else{
                if(index != -1)
                setRpmAnimation(index)
            }
        })
        viewModel.rpmstatus.observe(this,Observer{ type ->
            when(type){
                RpmStatus.VIEW->{
                    viewModel.btn_text.value = "리셋하기"
                }
                RpmStatus.RPM_SET->{
                    viewModel.btn_text.value = "평가하기"
                }
                RpmStatus.RPM_COMPLETE->{
                    viewModel.btn_text.value = "평가완료"
                }
                else -> {}
            }
        })
        viewModel.setdialog.observe(this,Observer{ isshow->
            if(isshow){

                var heart = viewModel.rpmindex.value!!
                if (viewModel.rpmstatus.value !=  RpmStatus.RPM_COMPLETE) {
                    var di = RpmDialog(this, viewModel.rpmstatus.value!!, heart) { event, check ->
                        if (check) {
                            if (event == RpmStatus.VIEW) {
                                viewModel.ResetRpm()
                            } else if (event == RpmStatus.RPM_SET) {
                                viewModel.InsertRpm()
                            }
                        }
                    }
                    di.show(supportFragmentManager,"")
                }else{
                    this.finish()
                }

            }
        })
        viewModel.rpminputtype.observe(this, Observer {


        })
        viewModel.toast.observe(this, Observer { toast->
            if(!toast.isNullOrEmpty()){
                if (toast.equals("하트 갯수가 부족합니다.")){
                    var drivePassPayDialog = DrivePassPayDialog(this, DrivePassDialogStatus.NOTHEART,"",0) { type, isok->
                        if(isok){
                            val i= Intent(this, HeartsShopActivity::class.java)
                            startActivity(i)
                           // this.overridePendingTransition(R.anim. fadein, R.anim.fadeout)
                        }

                    }
                    drivePassPayDialog!!.show(supportFragmentManager,"")
                }else{
                    Toast.makeText(this,toast, Toast.LENGTH_SHORT).show()
                }

            }
        })
        viewModel.isnotheart.observe(this, Observer {
            if(it){
                var notheart  = TwoButtonDialog(this, Constans.DIALOG_LACK_HEART,this, Constans.DIALOG_LACK_HEART)
                notheart!!.show(supportFragmentManager,"")
            }
        })
    }


    override fun onPause() {
        super.onPause()
       // overridePendingTransition(R.anim. fadein, R.anim.fadeout)
    }
    override fun finish() {
        super.finish()
       // overridePendingTransition(com.supercarlounge.supercar.R.anim. fadein, com.supercarlounge.supercar.R.anim.fadeout)
    }
    fun setimage(index:Int){
        when(index){
            0->{
                Glide.with(binding!!.ivRpm).load(R.drawable.rpm_0).into(binding!!.ivRpm)
            }
            1->{
                Glide.with(binding!!.ivRpm).load(R.drawable.rpm_6).into(binding!!.ivRpm)
            }
            2->{
                Glide.with(binding!!.ivRpm).load(R.drawable.rpm_12).into(binding!!.ivRpm)
            }
            3->{
                Glide.with(binding!!.ivRpm).load(R.drawable.rpm_18).into(binding!!.ivRpm)
            }
            4->{
                Glide.with(binding!!.ivRpm).load(R.drawable.rpm_24).into(binding!!.ivRpm)
            }
            5->{
                Glide.with(binding!!.ivRpm).load(R.drawable.rpm_30).into(binding!!.ivRpm)
            }
            6->{
                Glide.with(binding!!.ivRpm).load(R.drawable.rpm_36).into(binding!!.ivRpm)
            }
            7->{
                Glide.with(binding!!.ivRpm).load(R.drawable.rpm_42).into(binding!!.ivRpm)
            }
            8->{
                Glide.with(binding!!.ivRpm).load(R.drawable.rpm_48).into(binding!!.ivRpm)
            }
            9->{
                Glide.with(binding!!.ivRpm).load(R.drawable.rpm_54).into(binding!!.ivRpm)
            }
            10->{
                Glide.with(binding!!.ivRpm).load(R.drawable.rpm_60).into(binding!!.ivRpm)
            }
            else -> {}
        }
    }
    fun settouchrpm(degree:Double){
        var index = viewModel.rpmindex.value
        viewModel.rpm_prvindex.value =index
        if(degree>-175&&degree<-155) {
            viewModel.rpmindex.value = 0
        }else if(degree>-155&&degree<-135){
            viewModel.rpmindex.value = 1
        }else if(degree>-135&&degree<-115){
            viewModel.rpmindex.value = 2
        }else if(degree>-115&&degree<-85){
            viewModel.rpmindex.value = 3
        }else if(degree>-85&&degree<-55){
            viewModel.rpmindex.value = 4
        }else if(degree>-55&&degree<-20){
            viewModel.rpmindex.value = 5
        }else if(degree>-20&&degree<0){
            viewModel.rpmindex.value = 6
        }else if(degree>0&&degree<20){
            viewModel.rpmindex.value = 7
        }
        else if(degree>20&&degree<40){
            viewModel.rpmindex.value = 8
        }else if(degree>45&&degree<60){
            viewModel.rpmindex.value = 9
        }else if(degree>65&&degree<90){
            viewModel.rpmindex.value = 10
        }
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    fun setRpmmoveAnimation(prv:Int,next :Int) {
        Log.d("알피엠  터치1", "setRpmmoveAnimation: ")
        job = CoroutineScope(Dispatchers.Main).launch {
            var animation = AnimationDrawable()
            var percents: ArrayList<Int> = arrayListOf()
            var duration = 30
            animation.stop()
            Log.d("알피엠 pv", prv.toString())
            Log.d("알피엠 next", next.toString())
            Log.d("rpm이미지", "====================")
            if (prv <= next){
                for (i in (prv * 6)..(next * 6)) {
                    when (i) {
                        20 -> duration = 20
                        40 -> duration = 10
                        else -> {}
                    }
                    percents?.add(i)
                    if (i >=0) {
                        val name = "rpm_$i"

                        Log.d("rpm이미지", name.toString())

                        val drawableResourceId =
                            this@RpmActivity.resources.getIdentifier(name, "drawable", packageName)

                        animation.addFrame(resources.getDrawable(drawableResourceId), duration)
                        Log.d("알피엠  에드", "setRpmmoveAnimation: ")
                    }
                }
            }else{
                for (i in  (prv * 6)downTo (next * 6)) {
                    when (i) {
                        20 -> duration = 20
                        40 -> duration = 10
                        else -> {}
                    }
                    percents?.add(i)
                    if (i >=0) {
                        val name = "rpm_$i"

                        Log.d("rpm이미지", name.toString())

                        val drawableResourceId =
                            this@RpmActivity.resources.getIdentifier(name, "drawable", packageName)

                        animation.addFrame(resources.getDrawable(drawableResourceId), duration)
                        Log.d("알피엠  에드", "setRpmmoveAnimation: ")
                    }
                }
            }

            Log.d("rpm이미지", "====================")
            Log.d("알피엠  셋", "setRpmmoveAnimation: ")
            binding.ivRpm.setImageDrawable(animation);
            animation.isOneShot = true
            animation.start()
        }
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    fun setRpmAnimation(percent :Int){
        job = CoroutineScope(Dispatchers.Main).launch {
            var animation = AnimationDrawable()
            var percents: ArrayList<Int> = arrayListOf()
            var duration = 30
            animation.stop()

            for (i in 0..60){
                when(i) {
                    20 -> duration = 20
                    40 -> duration = 10
                    else -> {}
                }
                percents?.add(i)
                val name = "rpm_$i"
                val drawableResourceId = this@RpmActivity.resources.getIdentifier(name, "drawable", packageName)
                animation.addFrame(resources.getDrawable(drawableResourceId), duration)

            }
            for (i in 59 downTo (percent*6)){
                percents?.add(i)
                val name = "rpm_$i"
                val drawableResourceId = this@RpmActivity.resources.getIdentifier(name, "drawable", packageName)
                animation.addFrame(resources.getDrawable(drawableResourceId), duration)
            }


            binding?.ivRpm?.setImageDrawable(animation);
            animation.isOneShot = true
            animation.start()

            Log.d("ㅎㅇ", "setVIPanimation: ")
        }
//        delay(2000L) // delay a bit
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
