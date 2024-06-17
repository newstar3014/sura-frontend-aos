package com.supercarlounge.supercar.viewmodel.dialogviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DialogHeartPayViewModel : ViewModel() {

    var tv_ok = MutableLiveData<String>("")
    var tv_cancel = MutableLiveData<String>("")


    var tv_sub = MutableLiveData<String>("")

    var checked1 = MutableLiveData<Boolean>(false)
    var checked2 = MutableLiveData<Boolean>(false)

    fun checkset1(){
       var n=  checked1.value!!
        if(n){
            checked1.value =false
        }else{
            checked1.value = true
        }
    }

    fun checkset2(){
        var n=  checked2.value!!
        if(n){
            checked2.value =false
        }else{
            checked2.value = true
        }
    }
}