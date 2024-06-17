package com.supercarlounge.supercar.viewmodel

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job


class PasswordSearchViewModel : ViewModel() {

    var titleText = MutableLiveData<String>("")
    var time= MutableLiveData<String>("")
    var mainText = MutableLiveData<String>("")
    var subText = MutableLiveData<String>("")
    var NumberSendText = MutableLiveData<String>("인증번호 발송하기")
    var LockText = MutableLiveData<String>("잠금 비밀번호 재설정")
    var reSend= MutableLiveData<String>("")
    var sendText = MutableLiveData<String>("")
    var reSendTime= MutableLiveData<String>("")
    var checked1 = MutableLiveData<Boolean>(false)
    var authenticatingTime = MutableLiveData<Boolean>(false)
    var authenticating = MutableLiveData<Boolean>(false)
    var passwordList = arrayListOf<String> ()
    var passwordListSize= MutableLiveData<Int>(0)
    var u_seq= MutableLiveData<String>("")
    var number = MutableLiveData<String>("48645641234")

    var second = 0
    var minute = 0
    var timeCheck = 0
    var timeTick = 100000

    var isRun: Boolean = false
    private lateinit var job : Job









    private var interval : Long = 1000


    var watcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            sendText.value = s.toString()
        }
        override fun afterTextChanged(s: Editable) {

        }
    }

//    fun setInterval (n : Long ) {
//        interval = n
//    }
//    fun timerStart(fnCallback :() ->Unit){
//        authenticatingTime.value = true
//        isRun = true
//        if(::job.isInitialized) job.cancel()
//
//        job = viewModelScope.launch {
//            while(true) {
//                delay(interval)
//                fnCallback()
//            }
//        }
//    }



//
//    fun timerStop(){
//        isRun = false
//        if(::job.isInitialized) job.cancel()
//    }
      fun onauthenticatingTime(){
          u_seq.value?.let {  }
      }



}
