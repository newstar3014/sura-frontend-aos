package com.supercarlounge.supercar.viewmodel.dialogviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.enumset.PopTypeStatus

class DialogPoliceViewModel : ViewModel() {

    var tv_ok = MutableLiveData<String>("")
    var tv_cancel = MutableLiveData<String>("")


    var tv_sub = MutableLiveData<String>("")
    var tv_main = MutableLiveData<String>("")
    var viewtype = MutableLiveData<Int>(0)
    var type = MutableLiveData<Int>(0)
    var contents = MutableLiveData<String>("")

    var viewtypeset = MutableLiveData<PopTypeStatus>()
}