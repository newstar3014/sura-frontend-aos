package com.supercarlounge.supercar.Network

import com.supercarlounge.supercar.data.*
import com.google.gson.JsonObject
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MatchingService {
    @FormUrlEncoded
    @POST("/front/profile/pc_seq")
    fun GetMatchingData(
        @Field("u_seq") u_seq: String?,
        @Field("sub_seq") sub_seq: String?,
    ): retrofit2.Call<ProfileMatchingData>?

    @FormUrlEncoded
    @POST("/front/other/v3/profileCheck")
    fun ProfileReadCheck(
        @Field("b_seq") b_seq: String?,
        @Field("u_seq") u_seq: String?,
        @Field("sub_seq") sub_seq: String?,
    ): retrofit2.Call<ResultProfileReadCheck>?



    @FormUrlEncoded
    @POST("/front/other/v1/openProfile")
    fun ProfileRead(
        @Field("b_seq") b_seq: String?,
        @Field("u_seq") u_seq: String?,
        @Field("sub_seq") sub_seq: String?,
        @Field("u_heart") u_heart: String?,
        @Field("b_type") b_type: String?,
        @Field("hl_type") hl_type: String?,
        @Field("pc_b_message") pc_b_message: String?,
        @Field("given_nickname") given_nickname: String?,
    ): retrofit2.Call<ResultProfileReadData>?
    @FormUrlEncoded
    @POST("/front/profile/v5/apply/list")
    fun GetProfileRequest(
        @Field("u_seq") u_seq: String?,
        @Field("pc_ok") pc_ok: String?,
        @Field("pc_type") pc_type: String?,
    ): retrofit2.Call<ResultProfileRequestData>?


    @FormUrlEncoded
    @POST("/front/profile/v1/pcDateCheck")
    fun getMatchingDataYN(
        @Field("u_seq") u_seq: String?,
        @Field("sub_seq") sub_seq: String?,
    ): retrofit2.Call<ResultAlimYNData>?


    @FormUrlEncoded
    @POST("/front/other/deleteProfileConfirm")
    fun DeleteRequest(
        @Field("pc_seq") pc_seq: String?,
        @Field("u_seq") u_seq: String?,
        @Field("sub_seq") sub_seq: String?,
    ): retrofit2.Call<ResultData>?

    @FormUrlEncoded
    @POST("/front/profile/v2/userProfile")
    fun GetUserProfile(
        @Field("u_seq") u_seq: String?,
        @Field("u_lat") u_lat: String?,
        @Field("u_lon") u_lon: String?,
        @Field("sub_seq") sub_seq: String?,
    ): retrofit2.Call<ResultUserInformationData>?

    @FormUrlEncoded
    @POST("/front/rpm/all")
    fun GetRpmScoreAvg(
        @Field("u_seq") u_seq: String?,
    ): retrofit2.Call<ResultRpmData>?

    @FormUrlEncoded
    @POST("/front/rpm/score")
    fun GetRpmScore(
        @Field("u_seq") u_seq: String?,
        @Field("sub_seq") sub_seq: String?,
    ): retrofit2.Call<ResultRpmData>?
    @FormUrlEncoded
    @POST("/front/profile/v1/pc_ok")
    fun ProfileReadAccept(
        @Field("pc_seq") pc_seq: String?,
        @Field("pc_ok") pc_ok: String?,
        @Field("send_seq") send_seq: String?,
        @Field("sub_seq") sub_seq: String?,
        @Field("giveNick") giveNick: String?,
    ): retrofit2.Call<ResultData>?

    @POST("/front/other/v2/sendFavorit")
    fun LikeSend(
        @Body obj: JsonObject,
    ): retrofit2.Call<ResultData>?

    @FormUrlEncoded
    @POST("/front/other/v1/favoritStatus")
    fun LikeAccept(
        @Field("pc_seq") pc_seq: String?,
        @Field("pc_status") pc_status: String?,
        @Field("safeNumber") safeNumber: String?,
        @Field("u_seq") u_seq: String,
        @Field("sub_seq") sub_seq: String,
        @Field("giveNick") giveNick: String,
    ): retrofit2.Call<ResultData>?

    @FormUrlEncoded
    @POST("/front/other/v4/openSafeNumber")
    fun PhoneOpen(
        @Field("u_seq") u_seq: String?,
        @Field("sub_seq") sub_seq: String?,
        @Field("pc_seq") pc_seq: String?,
        @Field("giveNick") giveNick: String?,
        @Field("givenNick")givenNick:String?,
        @Field("flex") flex: String?,
    ): retrofit2.Call<ResultData>?

    @FormUrlEncoded
    @POST("/front/profile/realName")
    fun ReadRealNick(
        @Field("u_seq") u_seq: String?,
        @Field("sub_seq") sub_seq: String?,
    ): retrofit2.Call<ResultData>?


    @POST("/front/rpm/insert")
    fun InsertRpm(
        @Body jsonobject:JsonObject
    ): retrofit2.Call<ResultData>?

    @FormUrlEncoded
    @POST("/front/other/rpmReset")
    fun ResetRpm(
        @Field("u_seq") u_seq: String?,
    ): retrofit2.Call<ResultData>?

    @FormUrlEncoded
    @POST("/front/other/v3/matchingInfo")
    fun GetMatchingMini(
        @Field("u_seq") u_seq: String?,
        @Field("u_type") u_type: String?,
        @Field("page") page: Int?,
        @Field("ppp") ppp: Int,
    ): retrofit2.Call<ResultMatchingMiniData>?

    @FormUrlEncoded
    @POST("/front/other/v1/sendHogam")
    fun MiniSendLIke(
        @Field("u_seq") u_seq: String?,
        @Field("sub_seq") sub_seq: String?,
        @Field("u_heart") u_heart: Int?,
        @Field("pc_message") pc_message: String,
        @Field("given_nickname") given_nickname: String,
    ): retrofit2.Call<ResultData>?

    @FormUrlEncoded
    @POST("/front/com_router/drivePassChk")
    fun DrivePassCheck(
        @Field("u_seq") u_seq: String?,
    ): retrofit2.Call<ResultData>?
    @FormUrlEncoded
    @POST("/front/com_router/v1/drivePassLogCount")
    fun DrivePassCount(
        @Field("u_seq") u_seq: String?,
    ): retrofit2.Call<ResultDrivePassCountData>?
    @FormUrlEncoded
    @POST("/front/other/paymentUpdate")
    fun HeartPaySet(
        @Field("b_seq") b_seq: String?,
        @Field("b_same_gender") b_same_gender: String?,
        @Field("b_payment") b_payment: String?,
    ): retrofit2.Call<ResultData>?


    @FormUrlEncoded
    @POST("/front/other/feed")
    fun FeedProfileSearch(
        @Field("u_seq") u_seq: String?,
        @Field("type") type: String?,
    ): retrofit2.Call<ResultFeedData>?

    @FormUrlEncoded
    @POST("/front/other/feed/check")
    fun FeedProfileFeeedCehck(
        @Field("u_seq") u_seq: String?,
        @Field("type") type: String?,
    ): retrofit2.Call<ResultFeedData>?


    @FormUrlEncoded
    @POST("/front/other/recoMatching")
    fun RecProfileSearch(
        @Field("u_seq") u_seq: String?,
        @Field("sub_seq") sub_seq: String?,
        @Field("heart") heart: String?,
        @Field("isPass") isPass: Boolean,
    ): retrofit2.Call<ResultFeedData>?

    @FormUrlEncoded
    @POST("/front/mypage/myPageViewChk")
    fun myPageViewChk(
        @Field("mypage_type") mypage_type: String?,
        @Field("use_seq") use_seq: String?,
    ): retrofit2.Call<ResultData>?

    @FormUrlEncoded
    @POST("/front/other/v1/directHogam")
    fun directHogam(
        @Field("u_seq") u_seq: String?,
        @Field("sub_seq") sub_seq: String?,
        @Field("u_heart") u_heart: Int?,
        @Field("pc_message") pc_message: String,
        @Field("given_nickname") given_nickname: String,
        @Field("b_seq") b_seq: String?,
    ): retrofit2.Call<ResultData>?
}