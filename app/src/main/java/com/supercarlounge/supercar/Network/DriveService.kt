package com.supercarlounge.supercar.Network

import com.google.gson.JsonObject
import com.supercarlounge.supercar.data.*
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface DriveService {
    @FormUrlEncoded
    @POST("/front/location_info/v1/tagDriveList")
    fun tagDriveList(
        @Field("u_seq") u_seq: String?,
        @Field("my_lat") my_lat: String,
        @Field("my_lon") my_lon: String?,
        @Field("ppp") ppp: Int?,
        @Field("page") page: Int?,
        @Field("spot_seq") spot_seq: String?
    ): retrofit2.Call<ResultDriveData>?


    @FormUrlEncoded
    @POST("/front/home/v6/vipDriveList")
    fun getVipDrive(
        @Field("u_seq") u_seq: String?,
        @Field("my_lat") my_lat: String,
        @Field("my_lon") my_lon: String?
    ): retrofit2.Call<ResultDriveData>?


    //  @Headers("Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NDA5LCJpYXQiOjE2NjM4MzY1MjYsImV4cCI6MTY2MzkyMjkyNn0.6f94ZXZZK2GQDHQIdNOCWJXiMx_H5VZ4rEcmnMy0ZPE")
    @FormUrlEncoded
    @POST("/front/home/v6/homeChangeDrive")
    fun getDrive(
        @Field("choice") choice: String?,
        @Field("u_seq") u_seq: String,
        @Field("ppp") ppp: Int?,
        @Field("page")page :Int?,
        @Field("my_lat") my_lat: String,
        @Field("my_lon") my_lon: String?
    ): retrofit2.Call<ResultDriveData>?

    @FormUrlEncoded
    @POST("/front/other/myVipLank")
    fun GetMyVipDrive(
        @Field("u_seq") u_seq: String?,
    ):
            retrofit2.Call<ResultVipDriveAuctionData>?

    @FormUrlEncoded
    @POST("/front/other/rankUp")
    fun RankUpVipDrive(
        @Field("u_seq") u_seq: String,
        @Field("da_seq") da_seq: String,
        @Field("heart") heart: String,
    ):
            retrofit2.Call<ResultData>?

    @FormUrlEncoded
    @POST("/front/com_router/drivePassChk")
    fun DrivePassCheck(
        @Field("u_seq") u_seq: String,
    ):
            retrofit2.Call<ResultData>?

    @FormUrlEncoded
    @POST("/front/mypage/driveSaw")
    fun NearDrive(
        @Field("u_seq") u_seq: String,
    ):
            retrofit2.Call<ResultNearDriveData>?
    @POST("/front/drive/v2/driveAuction")
    fun VipDrivePost(
        @Body obj: JsonObject
    ):
            retrofit2.Call<ResultData>?
    @FormUrlEncoded
    @POST("/front/board/v2/boardLikeSave")
    fun LikeDrive(
        @Field("u_seq") u_seq: String,
        @Field("b_seq") b_seq: String,
        @Field("bl_type") bl_type: String,
        @Field("like_type") like_type: String,
    ):
            retrofit2.Call<ResultData>?

    @FormUrlEncoded
    @POST("/front/mypage/driveSaw")
    fun RecentDrive(
        @Field("u_seq") u_seq: String,
    ):
            retrofit2.Call<ResultData>?

    @FormUrlEncoded
    @POST("/front/drive/myRecentVip")
    fun GetMyVipData(
        @Field("u_seq") u_seq: String,
    ): retrofit2.Call<ResultDriveAuctionData>?


    @FormUrlEncoded
    @POST("/front/mypage/v4/driveSaw")
    fun GetRecentDrive(
        @Field("u_seq") u_seq: String,
    ): retrofit2.Call<ResultRecentDriveData>?


    @FormUrlEncoded
    @POST("/front/mypage/myRecentlyDrive")
    fun GetRecentList(
        @Field("u_seq") u_seq: String,
        @Field("b_seq") b_seq: String,
        @Field("showType") showType: String,
        @Field("page") page: Int,
        @Field("ppp") ppp: Int,
    ): retrofit2.Call<ResultRecentDriveListData>?

    @FormUrlEncoded
    @POST("/front/board/v1/oneDrive")
    fun getRecentData(
        @Field("b_seq") b_seq: String,
    ): retrofit2.Call<ResultRecentData>?
    @FormUrlEncoded
    @POST("/front/drive/driveViewsUpdate/v2")
    fun driveViewsUpdate(
        @Field("b_seq") b_seq: String?,
        @Field("u_seq") u_seq: String,
    ): retrofit2.Call<ResultData>?

    @FormUrlEncoded
    @POST("/front/other/myGauge")
    fun GetVipGauge(
        @Field("u_seq") u_seq: String,
    ): retrofit2.Call<ResultVipLoungeData>?
    @FormUrlEncoded
    @POST("/front/other/myVipType")
    fun CheckViptype(
        @Field("u_seq") u_seq: String,
    ): retrofit2.Call<ResultViptypeData>?

    @FormUrlEncoded
    @POST("/front/user/v1/passEndDate")
    fun CheckDrivePassDate(
        @Field("u_seq") u_seq: String,
    ):
            retrofit2.Call<ResultDrivePassDateData>?
}