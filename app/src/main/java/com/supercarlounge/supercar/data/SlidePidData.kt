package com.supercarlounge.supercar.data

import com.supercarlounge.supercar.enumset.PidDialogType

data class SlidePidData(

    var type  : PidDialogType,
    var istip  : Boolean,
    var title: String,
    var subtitle: String,
    var istop: Boolean = true,
    ){

}
