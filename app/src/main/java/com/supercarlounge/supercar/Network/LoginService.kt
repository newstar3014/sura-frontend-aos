package com.supercarlounge.supercar.Network



import com.supercarlounge.supercar.data.*
import retrofit2.http.*

interface LoginService {


//    "/front/other/v2/sms"
    @FormUrlEncoded
    @POST("/front/other/v4/sms")
    fun getSMS(
        @Field("u_phone") u_phone: String?,
        @Field("hashkey") hashkey: String?,
        @Field("u_token") u_token: String?,
        @Field("smsType")smsType:String?,
        @Field("returnSecureToken") returnSecureToken: Boolean?,
    ): retrofit2.Call<ResultSMSData>?
    @FormUrlEncoded
    @POST("/front/other/login")
    fun checkLogin(
        @Field("u_phone") u_phone: String?,
        @Field("u_token") u_token: String?,
        @Field("returnSecureToken") returnSecureToken: Boolean?,
    ): retrofit2.Call<ResultLoginData>?


    @FormUrlEncoded
    @POST("/front/other/v1/activeLogSave")
    fun checkloginsert(
        @Field("al_app") al_app: String?,
        @Field("u_seq") u_seq: String?,
    ): retrofit2.Call<ResultData>?
}