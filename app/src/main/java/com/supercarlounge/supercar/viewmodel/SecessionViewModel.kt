package com.supercarlounge.supercar.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SecessionViewModel : ViewModel() {


    var titleText = MutableLiveData<String>("SUPERCAR LOUNGE")
    var titleText2 = MutableLiveData<String>("VIP DRIVE")
    var titleText3 = MutableLiveData<String>("DRIVE AWAY")

    var checkSecession = MutableLiveData<Boolean>(false)

    var reason = MutableLiveData<String>("")
    fun setcheckSecession(boolean: Boolean){

        checkSecession.value = !boolean
    }

}