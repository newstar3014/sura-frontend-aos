package com.supercarlounge.supercar.viewmodel.dialogviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.enumset.PopTypeStatus


class DialogTwoButtonViewModel : ViewModel() {

    var tv_ok = MutableLiveData<String>("확인")
    var tv_cancel = MutableLiveData<String>("")



    var tv_main = MutableLiveData<String>("")
    var tv_sub = MutableLiveData<String>("")
    var tv_third = MutableLiveData<String>("")
    var b_seq = MutableLiveData<Int>(0)
    var viewtype = MutableLiveData<Int>(0)
    var type = MutableLiveData<Int>(0)
    var heartChecked = MutableLiveData<Boolean>(false)
    var viewtypeset = MutableLiveData<PopTypeStatus>()
    fun onHeartChecked(boolean: Boolean){
        heartChecked.value = boolean
    }



}