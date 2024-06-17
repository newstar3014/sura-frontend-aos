package com.supercarlounge.supercar.viewmodel.dialogviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.enumset.DialogCheckVipTimeType

class DialogCheckVipTimeViewModel : ViewModel() {

    var tv_ok = MutableLiveData<String>("")
    var tv_cancel = MutableLiveData<String>("")


    var tv_sub = MutableLiveData<String>("")
    var tv_main = MutableLiveData<String>("")
    var type = MutableLiveData<DialogCheckVipTimeType>()
}