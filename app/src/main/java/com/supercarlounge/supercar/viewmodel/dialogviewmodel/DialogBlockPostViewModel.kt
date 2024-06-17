package com.supercarlounge.supercar.viewmodel.dialogviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.enumset.PopTypeStatus

class DialogBlockPostViewModel : ViewModel() {

    var tv_ok = MutableLiveData<String>("")
    var tv_cancel = MutableLiveData<String>("")


    var tv_main = MutableLiveData<String>("")

    var viewtype = MutableLiveData<Int>(0)
    var viewtypeset = MutableLiveData<PopTypeStatus>()
}