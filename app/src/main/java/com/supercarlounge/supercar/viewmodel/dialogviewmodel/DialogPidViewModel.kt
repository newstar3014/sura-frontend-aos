package com.supercarlounge.supercar.viewmodel.dialogviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.enumset.PidDialogType

class DialogPidViewModel : ViewModel() {
    var vtype = MutableLiveData<PidDialogType>(PidDialogType.DEFAULT)
    var tv_ok = MutableLiveData<String>("")
    var tv_cancel = MutableLiveData<String>("")

    var tv_sub = MutableLiveData<String>("")
}