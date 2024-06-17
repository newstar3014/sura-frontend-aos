package com.supercarlounge.supercar.viewmodel.dialogviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DialogInviteViewModel : ViewModel() {

    var tv_ok = MutableLiveData<String>("")
    var tv_cancel = MutableLiveData<String>("")
    var invite_code = MutableLiveData<String>("")
    var tv_main =MutableLiveData<String>("")
    var tv_sub=MutableLiveData<String>("")
}