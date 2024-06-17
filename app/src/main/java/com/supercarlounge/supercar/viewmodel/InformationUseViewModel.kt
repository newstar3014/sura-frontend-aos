package com.supercarlounge.supercar.viewmodel

import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.HeartService
import com.supercarlounge.supercar.Network.HomerService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.Network.UserService
import com.supercarlounge.supercar.data.*
import com.supercarlounge.supercar.dialog.DrivePassPayDialog
import com.supercarlounge.supercar.enumset.DrivePassDialogStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class InformationUseViewModel : ViewModel() {

    var titleText = MutableLiveData<String>("?")
    var my_seq = MutableLiveData<String>("")
    var subTitleText = MutableLiveData<String>("")
    var termsText = MutableLiveData<String>("")
    var viewType = MutableLiveData<Int>(0)
    var heartloglist = MutableLiveData<ArrayList<HeartLogData>>(arrayListOf())
    var toast = MutableLiveData<String>("")
    var pamentstart = MutableLiveData<Boolean>(false)
    var payclickindex = MutableLiveData<Int>(0)
    var invite_code = MutableLiveData<String>("")
    var inviteset =MutableLiveData<String>("")



}
