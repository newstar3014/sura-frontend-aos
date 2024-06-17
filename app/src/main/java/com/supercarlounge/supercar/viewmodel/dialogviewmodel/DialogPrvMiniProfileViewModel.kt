package com.supercarlounge.supercar.viewmodel.dialogviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DialogPrvMiniProfileViewModel : ViewModel() {
    var tv_nick = MutableLiveData<String>("미니 프로필 예시")
    var tv_height = MutableLiveData<String>("168cm")
    var tv_smoke = MutableLiveData<String>("비흡연")
    var tv_charm = MutableLiveData<ArrayList<String>>(arrayListOf("비율이 좋은","글래머"))
    var tv_pm = MutableLiveData<ArrayList<String>>(arrayListOf("사랑스러운","귀여운"))
    var tv_music = MutableLiveData<ArrayList<String>>(arrayListOf("POP","제즈포크/블루스.컨트리"))
    var tv_drive = MutableLiveData<String>("새벽에 하는 드라이브")
}