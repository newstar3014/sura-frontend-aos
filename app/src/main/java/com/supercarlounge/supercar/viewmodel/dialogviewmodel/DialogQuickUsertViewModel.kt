package com.supercarlounge.supercar.viewmodel.dialogviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.enumset.QuickTypeStatus

class DialogQuickUsertViewModel : ViewModel() {

    var tv_ok = MutableLiveData<String>("")
    var tv_cancel = MutableLiveData<String>("")


    var tv_main = MutableLiveData<String>("이 드라이브글을\n차단하시겠습니까?")

    var viewtype = MutableLiveData<Int>(0)
    var viewtypeset = MutableLiveData<QuickTypeStatus>()
}