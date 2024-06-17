package com.supercarlounge.supercar.viewmodel.dialogviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.enumset.PopOneTypeStatus

class DialogOneButtonViewModel : ViewModel() {

    var tv_ok = MutableLiveData<String>("확인")
    var tv_cancel = MutableLiveData<String>("")


    var tv_sub = MutableLiveData<String>("")
    var tv_main = MutableLiveData<String>("")
    var tv_main_color = MutableLiveData<String>("")
    var type = MutableLiveData<PopOneTypeStatus>()
}