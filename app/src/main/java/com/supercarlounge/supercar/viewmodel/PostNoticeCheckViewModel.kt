package com.supercarlounge.supercar.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PostNoticeCheckViewModel : ViewModel() {

    var titleText = MutableLiveData<String>("")
    var time= MutableLiveData<String>("")
    var mainText = MutableLiveData<String>("")
    var subText = MutableLiveData<String>("")
    var checked1 = MutableLiveData<Boolean>(false)
    var checked2 = MutableLiveData<Boolean>(false)



}