package com.supercarlounge.supercar.viewmodel.dialogviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.enumset.GuidePopType

class DialogGuideViewModel : ViewModel() {
    var viewtypeset = MutableLiveData<GuidePopType>()
}