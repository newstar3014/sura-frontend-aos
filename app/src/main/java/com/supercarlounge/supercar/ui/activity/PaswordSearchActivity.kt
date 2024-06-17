package com.supercarlounge.supercar.ui.activity

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.Network.PasswordService
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.data.ResultPassAuthenticationData
import com.supercarlounge.supercar.data.VipDriveRankData
import com.supercarlounge.supercar.databinding.*
import com.supercarlounge.supercar.viewmodel.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.*
import kotlin.concurrent.timer

class PaswordSearchActivity: BaseActivitiy() {


    private lateinit var binding: ActivityPasswordSearchBinding
    private lateinit var viewModel: PasswordSearchViewModel
    private lateinit var application: MainApplication

    var second = 0
    var minute = 0
    var timeCheck = 0
    var timeTick = 180


    var timer: Timer? = null
    private val tt: TimerTask? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        with(window) {
//            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
//            // set an slide transition
//            enterTransition = Slide(Gravity.END)
//            exitTransition = Slide(Gravity.START)
//        }
        viewModel = ViewModelProvider(this).get(PasswordSearchViewModel::class.java)
        binding = ActivityPasswordSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel
        application=applicationContext as MainApplication

        clickListeners()
        observes()
        setAdapters()

        var count = 0
        val timerAction : () -> Unit = { viewModel.reSendTime.value = "${count++}" }
//        viewModel.setInterval(500)
//        viewModel.timerStart(timerAction)
//        binding.tvSend.setOnClickListener {
//            viewModel.apply {
//                timerStart(timerAction)
//            }
//        }


        var s = "https://interactive-examples.mdn.mozilla.net/media/cc0-images/grapefruit-slice-332-332.jpg"


    }
    fun passAuthenticating(u_seq: String) {

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(PasswordService::class.java).passAuthenticating(
                u_seq
            )
            d!!.enqueue(object : retrofit2.Callback<ResultPassAuthenticationData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultPassAuthenticationData>,
                    response: Response<ResultPassAuthenticationData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        Log.d("passAuthenticating", "$u_seq: $data.message: ${data.number}")
                        viewModel.number.value = data.number
                        viewModel.authenticatingTime.value = true
                        viewModel.authenticating.value = true
                        setTimer()
                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultPassAuthenticationData>, t: Throwable) {
                    Log.d("check", t.toString())

                    viewModel.authenticatingTime.value = false
                }

            })

        }

    }
    private fun setTimer() {

        second = timeTick % 60
        minute = timeTick / 60

        if (timer != null){
            timer!!.cancel()
        }

        timer= timer(period = 1000, initialDelay = 1000) {

            runOnUiThread {
                viewModel.reSendTime.value = String.format("0$minute : %02d", second)
            }
            if (second == 0 && minute == 0) {
                // 타이머 종료 로직
                cancel()

                runOnUiThread {
                    viewModel.authenticatingTime.value = false
                    viewModel.reSendTime.value =String.format("00 : 00")
                    Log.d("테스트", "인증 시간이 만료되었습니다.")
                    timeCheck = 1
                    binding.tvSend.setOnClickListener {
                        viewModel.authenticatingTime.value = true

                        setTimer()
                        // requestTokenByPhone()
                    }
                }
                return@timer
            }
            if (second == 0) {
                minute--
                second = 60
            }
            second--
        }


    }
    private fun setAdapters() {

    }
    private fun observes() {
        viewModel.passwordListSize.observe(this, Observer {
            Log.d("observes", viewModel.passwordListSize.value!!.toString())
        })

    }

    private fun clickListeners() {
        binding!!.ivBack.setOnClickListener {
            finish()
        }
        binding!!.tvSend.setOnClickListener {
            Log.d("체크1", viewModel.authenticating.value.toString())
            Log.d("체크2", viewModel.authenticatingTime.value.toString())
            Log.d("체크----", "----------------------------------------------------------------")
            Log.d("체크22", viewModel.number.value.toString())
            Log.d("체크23", viewModel.sendText.value.toString())
            if (viewModel.authenticating.value == true ) {

                if (viewModel.authenticatingTime.value == true && viewModel.number.value.equals(viewModel.sendText.value) ){      var i = Intent(this, LockActivity::class.java)
                    i.putExtra("setpass","setpass")
                    startActivity(i)}


            }else{
                passAuthenticating(application.userData?.u_seq.toString())
            }

//
        }
        binding!!.tvResend.setOnClickListener {
            passAuthenticating(application.userData?.u_seq.toString())
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        timer!!.cancel()
        timer = null
    }

    override fun finish() {
        super.finish()
      //  overridePendingTransition(com.supercarlounge.supercar.R.anim. fadein, com.supercarlounge.supercar.R.anim.fadeout)
    }

    private fun testFilterData():ArrayList<VipDriveRankData>{
        var list : ArrayList<VipDriveRankData> = arrayListOf()
        var x = 1

        var data : VipDriveRankData

        for(i: Int in 0 .. 10 ){
            var xs = x.toString().length
            if(xs == 1){
                data = VipDriveRankData(" $x ","BENTREY","PASSENGER"  ,"asdasdasd")
            }else{
                data = VipDriveRankData("$x","BENTREY","PASSENGER"  ,"asdasdasd")
            }
            Log.d("zzz", "$x:$xs")

            x = x.plus(1)
            list.add(data)
        }


        return  list
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
}
