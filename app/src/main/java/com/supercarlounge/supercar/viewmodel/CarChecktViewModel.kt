package com.supercarlounge.supercar.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CarChecktViewModel : ViewModel() {

    var text = MutableLiveData<String>("?")
    var reSend = MutableLiveData<String>("재전송")
    var reSendCheck = MutableLiveData<String>("인증번호를 확인해주세요")
    var reSendTime = MutableLiveData<String>("00:00")
    var nickname = MutableLiveData<String>("")
    var resultState1Array = ArrayList<String>()
    var resultState2Array = ArrayList<String>()
    var resultState3Array = ArrayList<String>()
    var resultState4Array =ArrayList<String>()
    var resultState5Array = ArrayList<String>()
    var resultState6Array = ArrayList<String>()
    var resultState7Array = ArrayList<String>()

    var sexType = MutableLiveData<String>("")
}
