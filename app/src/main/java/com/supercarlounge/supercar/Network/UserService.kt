package com.supercarlounge.supercar.Network



import com.supercarlounge.supercar.data.*
import com.google.gson.JsonObject
import retrofit2.http.*

interface UserService {



    @FormUrlEncoded
    @POST("/front/user/myProfileInfo")
    fun getUserInformation(
        @Field("u_seq") u_seq: String?,
    ): retrofit2.Call<ResultUserInformationData>?



    @FormUrlEncoded
    @POST("/front/other/quickUserInfo")
    fun getQuickUserInformation(
        @Field("u_seq") u_seq: String?,
    ): retrofit2.Call<ResultUserInformationData>?
    @FormUrlEncoded
    @POST("/front/other/refuse/myProfileInfo")
    fun getNewUserInformation(
        @Field("u_seq") u_seq: String?,
    ): retrofit2.Call<ResultUserInformationData>?

    @FormUrlEncoded
    @POST("/front/com_router/invitationCode")
    fun InviteCodeGet(
        @Field("u_seq") u_seq: String?,
    ): retrofit2.Call<ResultInvateCodeData>?

    @FormUrlEncoded
    @POST("/front/profile/friendList")
    fun FriendList(
        @Field("u_seq") u_seq: String?,
    ): retrofit2.Call<ResultFriendCheckData>?


    @FormUrlEncoded
    @POST("/front/other/phoneblock/save")
    fun SendFriendPhone(
        @Field("u_seq") u_seq: String?,
        @Field("phoneList")phoneList: String,
    ): retrofit2.Call<ResultData>?


    @FormUrlEncoded
    @POST("/front/user/addHeart")
    fun CouponSend(
        @Field("u_seq") u_seq: String?,
        @Field("cp_code")cp_code: String?,
    ): retrofit2.Call<ResultData>?


    @POST("/front/profile/userProfile")
    fun getminiprofile(
        @Body jsonobj: JsonObject,
    ): retrofit2.Call<ResultUserInformationData>?

    @FormUrlEncoded
    @POST("/front/user/userExit")
    fun setSecession(
        @Field("u_seq") u_seq: String?,
        @Field("u_exit_msg") u_exit_msg: String,
        @Field("u_exit_now") u_exit_now: String?,

    ): retrofit2.Call<ResultData>?

    @FormUrlEncoded
    @POST("/front/other/quickInsert")
    fun setQuickInsert(
        @Field("u_name") u_name: String?,
        @Field("u_phone") u_phone: String,
        @Field("u_gender") u_gender: String?,
        @Field("u_birthday") u_birthday: String?,
        @Field("u_token") u_token: String?,
        @Field("u_loca") u_loca : String?,
        @Field("u_mt") u_mt  : String?,
        @Field("u_mkt") u_mkt: String?,
        ): retrofit2.Call<ResultData>?

    @FormUrlEncoded
    @POST("/front/other/outCheck")
    fun outCheck(
        @Field("u_seq") u_seq: String?,
        ): retrofit2.Call<ResultUserCheckData>?

    @FormUrlEncoded
    @POST("/front/ceo/gps/update")
    fun setlocation(
        @Field("u_seq") u_seq: String?,
        @Field("u_lat") u_lat: String?,
        @Field("u_lon") u_lon: String?,

    ): retrofit2.Call<ResultData>?
    @FormUrlEncoded
    @POST("/front/other/myheartCheck")
    fun myheartCheck(
        @Field("u_seq") u_seq: String?,
    ): retrofit2.Call<ResultMyheartCheckData>?
    @FormUrlEncoded
    @POST("/front/mypage/driveRead")
    fun getdriveRead(
        @Field("u_seq") u_seq: String?,
        @Field("b_seq") b_seq: String?,
    ): retrofit2.Call<driveReadData>?


}