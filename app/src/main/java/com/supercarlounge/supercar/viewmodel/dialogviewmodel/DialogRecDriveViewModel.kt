package com.supercarlounge.supercar.viewmodel.dialogviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.enumset.RecDriveDialogType

class DialogRecDriveViewModel : ViewModel() {
    var vtype = MutableLiveData<RecDriveDialogType>(RecDriveDialogType.DEFAULT)
    var tv_ok = MutableLiveData<String>("확인")
    var tv_cancel = MutableLiveData<String>("취소")
    var tv_sub = MutableLiveData<String>("")
    var tv_sub2 = MutableLiveData<String>("드라이브 패스권 구매하기")

}