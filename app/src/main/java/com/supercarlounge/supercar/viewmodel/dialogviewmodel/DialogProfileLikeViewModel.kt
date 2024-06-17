package com.supercarlounge.supercar.viewmodel.dialogviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DialogProfileLikeViewModel : ViewModel() {

    var tv_ok = MutableLiveData<String>("")
    var tv_cancel = MutableLiveData<String>("")


    var tv_sub = MutableLiveData<String>("이 드라이브글을\n차단하시겠습니까?")

    var viewtype = MutableLiveData<Int>(0)
    var checked = MutableLiveData<Boolean>(false)

    fun onChecked(boolean: Boolean){
        checked.value = boolean
    }
}