package com.supercarlounge.supercar.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginPassViewModel : ViewModel() {
    var text = MutableLiveData<String>("")
    var name = MutableLiveData<String>("")
    var viewStack = MutableLiveData<Int>(1)
}