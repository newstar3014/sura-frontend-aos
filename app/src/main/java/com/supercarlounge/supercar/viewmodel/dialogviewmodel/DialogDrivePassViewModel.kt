package com.supercarlounge.supercar.viewmodel.dialogviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DialogDrivePassViewModel : ViewModel() {

    var tv_ok = MutableLiveData<String>("")
    var tv_cancel = MutableLiveData<String>("")
    var tv_main = MutableLiveData<String>("이 드라이브글을\n차단하시겠습니까?")
}