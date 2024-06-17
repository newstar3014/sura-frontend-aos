package com.supercarlounge.supercar.data

data class ResultProfileSuggestionData(
    var type:String,
    var message:String ,
    var reco:ArrayList<ProfileSuggestionData>) {
}