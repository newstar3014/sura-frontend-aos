package com.supercarlounge.supercar.Network



import com.supercarlounge.supercar.data.*
import retrofit2.http.*

interface PasswordService {

    @FormUrlEncoded
    @POST("/front/user/lockPwdCheck")
    fun checkPassword(
        @Field("u_seq") u_seq: String?,
        @Field("u_lock") u_lock: String?,
    ): retrofit2.Call<ResultData>?

    @FormUrlEncoded
    @POST("/front/other/lock")
    fun passwordSettingTrueFalse(
        @Field("u_seq") u_seq: String?,
    ): retrofit2.Call<ResultPasswordSettiongData>?

    @FormUrlEncoded
    @POST("/front/user/lockUpdate")
    fun setPassward(
        @Field("u_seq") u_seq: String?,
        @Field("u_lock") u_lock: String?,
    ): retrofit2.Call<ResultData>?

    @FormUrlEncoded
    @POST("/front/profile/onoff")
    fun setPushAndLockScreen(
        @Field("u_seq") u_seq: String?,
        @Field("updateStr") updateStr: String?,
    ): retrofit2.Call<ResultData>?

    @FormUrlEncoded
    @POST("/front/user/sms")
    fun passAuthenticating(
        @Field("u_seq") u_seq: String?
    ): retrofit2.Call<ResultPassAuthenticationData>?
}