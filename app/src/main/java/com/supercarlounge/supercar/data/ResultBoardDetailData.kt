package com.supercarlounge.supercar.data

data class ResultBoardDetailData(
    val message: String,
    val type: String,
    val rows: ArrayList<BoardDetailData>,
)