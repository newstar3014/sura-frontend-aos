package com.supercarlounge.supercar.viewmodel.dialogviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DialogDriveRegiViewModel : ViewModel() {

    var tv_ok = MutableLiveData<String>("")
    var tv_cancel = MutableLiveData<String>("")


    var tv_sub1 = MutableLiveData<String>("")
    var tv_sub2 = MutableLiveData<String>("")

    var foldchecked1 = MutableLiveData<Boolean>(false)
    var foldchecked2 = MutableLiveData<Boolean>(false)

    var checked1 = MutableLiveData<Boolean>(false)
    var checked2 = MutableLiveData<Boolean>(false)
    var checked3 = MutableLiveData<Boolean>(false)


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
    fun checkset3(){
        var n=  checked3.value!!
        if(n){
            checked3.value =false
        }else{
            checked3.value = true
        }
    }

    fun foldcheckset1(){
        var n=  foldchecked1.value!!
        if(n){
            foldchecked1.value =false
        }else{
            foldchecked1.value = true
            foldchecked2.value =false
        }
    }

    fun foldcheckset2(){
        var n=  foldchecked2.value!!
        if(n){
            foldchecked2.value =false
        }else{
            foldchecked2.value = true
            foldchecked1.value =false
        }
    }
}