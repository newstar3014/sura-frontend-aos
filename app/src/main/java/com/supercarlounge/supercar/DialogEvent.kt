package com.supercarlounge.supercar

interface DialogEvent {
    fun okEvent(type:Int,okAndCancel: Boolean,comment_value:String,seq:String,u_nickname:String)

}