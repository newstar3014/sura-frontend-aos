package com.supercarlounge.supercar.viewmodel.dialogviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class DialogServiceCenterViewModel : ViewModel() {

    var tv_ok = MutableLiveData<String>("복사하기")
    var tv_cancel = MutableLiveData<String>("")



    var tv_main = MutableLiveData<String>("")
    var tv_sub = MutableLiveData<String>("")
    var tv_third = MutableLiveData<String>("")
    var viewtype = MutableLiveData<Int>(3)
    var type = MutableLiveData<Int>(0)
    var heartChecked = MutableLiveData<Boolean>(false)
    fun onHeartChecked(boolean: Boolean){
        heartChecked.value = boolean
    }
}