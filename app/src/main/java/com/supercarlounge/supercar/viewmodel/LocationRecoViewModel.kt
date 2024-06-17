package com.supercarlounge.supercar.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.data.LocationSpotData

class LocationRecoViewModel : ViewModel() {
    var nottext = MutableLiveData<String>("게시글을 불러오는 중입니다.")
    var recodata = MutableLiveData<ArrayList<LocationSpotData>>(ArrayList())
    init {

    }
}
