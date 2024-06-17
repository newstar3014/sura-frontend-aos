package com.supercarlounge.supercar.viewmodel.dialogviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.enumset.RpmStatus

class DialogRpmViewModel : ViewModel() {

    var tv_ok = MutableLiveData<String>("확인")
    var tv_cancel = MutableLiveData<String>("취소")
    var sub_text = MutableLiveData<String>("3500 하트")
    var type = MutableLiveData<RpmStatus>(RpmStatus.RPM_SET)
    var heart = MutableLiveData<String>("")
}