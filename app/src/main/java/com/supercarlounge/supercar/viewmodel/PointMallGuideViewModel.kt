package com.supercarlounge.supercar.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.HomerService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.Network.PointMallService
import com.supercarlounge.supercar.Network.UserService
import com.supercarlounge.supercar.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class PointMallGuideViewModel : ViewModel() {
    var titleText = MutableLiveData<String>("슈라 포인트 안내")
    var toast = MutableLiveData<String>("")
    var point_1_text = MutableLiveData<String>("50")
    var point_2_text = MutableLiveData<String>("100")
    var point_3_text = MutableLiveData<String>("30")
    var point_4_text = MutableLiveData<String>("10")
    var point_5_text = MutableLiveData<String>("2")
}
