package com.supercarlounge.supercar.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.Constans
import com.supercarlounge.supercar.DialogEvent
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.data.ProfileMatchingData
import com.supercarlounge.supercar.databinding.*
import com.supercarlounge.supercar.dialog.DrivePassPayDialog
import com.supercarlounge.supercar.dialog.TwoButtonDialog
import com.supercarlounge.supercar.enumset.DrivePassDialogStatus
import com.supercarlounge.supercar.viewmodel.*

class LikeSendActivity: BaseActivitiy(), DialogEvent {

    var application:MainApplication? = null
    private lateinit var binding: ActivityLikeSendBinding
    private lateinit var viewModel: LikeSendViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        application = applicationContext as MainApplication
        viewModel = ViewModelProvider(this).get(LikeSendViewModel::class.java)
        binding = ActivityLikeSendBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel
        viewModel.my_seq.value =application!!.userData!!.u_seq!!.toString()
        var sub_seq = intent.getStringExtra("sub_seq")
        viewModel.sub_seq.value =sub_seq
        var matching_data = intent.getParcelableExtra<ProfileMatchingData>("matching_data")
        var type = intent.getStringExtra("type")
        viewModel.isdirect.value = intent.getBooleanExtra("isdirect",false)
        viewModel.send_b_seq.value = intent.getStringExtra("send_b_seq")
        viewModel.position.value = intent.getIntExtra("position",0)
        Log.d("포지션", viewModel.position.value.toString())
        if (type == "익명게시판") {
            viewModel.GetMatchingData( viewModel.my_seq.value!!,sub_seq.toString())
        }else{
            if (matching_data != null) {
                viewModel.matching_data.value = matching_data
            } else {
                var nickname = intent.getStringExtra("nick")
                viewModel.nick.value = nickname
            }
        }
        clickListeners()
        observes()
        setAdapters()
        Glide.with(binding!!.ivAn).load(R.raw.heart_gif).override(200,200).centerInside().circleCrop().into(binding!!.ivAn)

    }
    private fun setAdapters() {

    }
    private fun observes() {
        viewModel.isfinish.observe(this,Observer{
            if(it){
                var i = Intent()
                if (viewModel.isdirect.value == true) {

                    i.putExtra("position", viewModel.position.value)
                    i.putExtra("hogamSendCheck", viewModel.hogamSendCheck.value)
                    Log.d("포지션", viewModel.position.value.toString())
                    Log.d("호감화면 1", i?.getBooleanExtra("hogamSendCheck",false).toString())

                }
                setResult(Activity.RESULT_OK,i)
                finish()
            }
        })
        viewModel.toast.observe(this,Observer{
            if(it.isNotEmpty()){
                if (it.equals("하트가 부족합니다. 하트를 충전해주세요.")){
                    var drivePassPayDialog = DrivePassPayDialog(this, DrivePassDialogStatus.NOTHEART,"",0) { type, isok->
                        if(isok){
                            val i= Intent(this, HeartsShopActivity::class.java)
                            startActivity(i)
                          //  this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        }

                    }
                    drivePassPayDialog!!.show(supportFragmentManager,"")
                }else{
                    Toast.makeText(this,it, Toast.LENGTH_SHORT).show()
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

    private fun clickListeners() {
        binding!!.ivBack.setOnClickListener {
            finish()
        }
    }
    override fun onDestroy() {
        super.onDestroy()

    }
    override fun finish() {
        super.finish()
       // overridePendingTransition(com.supercarlounge.supercar.R.anim. fadein, com.supercarlounge.supercar.R.anim.fadeout)
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
