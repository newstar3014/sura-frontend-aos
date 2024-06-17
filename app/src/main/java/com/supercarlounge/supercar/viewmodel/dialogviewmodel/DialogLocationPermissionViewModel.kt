package com.supercarlounge.supercar.viewmodel.dialogviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.enumset.PopTypeStatus

class DialogLocationPermissionViewModel : ViewModel() {

    var tv_ok = MutableLiveData<String>("")
    var tv_cancel = MutableLiveData<String>("")


    var tv_main = MutableLiveData<String>("")

    var viewtype = MutableLiveData<Int>(0)
    var viewtypeset = MutableLiveData<PopTypeStatus>()

    var okEvent = MutableLiveData<Boolean>(false)
    var cancelEvent = MutableLiveData<Boolean>(false)


    fun okEvent(boolean: Boolean){
        okEvent.value =boolean
    }

    fun cancelEvent(boolean: Boolean){
        cancelEvent.value =boolean
    }
}