package com.supercarlounge.supercar.Network



import com.supercarlounge.supercar.data.*
import retrofit2.http.*

interface HomerService {


    @FormUrlEncoded
    @POST("/front/user/recommendUser")
    fun getProfileSuggestion(
        @Field("u_seq") u_seq: String?,
    ): retrofit2.Call<ResultProfileSuggestionData>?

    @FormUrlEncoded
    @POST("/front/com_router/v2/allimSelect")
    fun getalim(
        @Field("u_seq") u_seq: String,
    ): retrofit2.Call<ResultAlimData>?

    @FormUrlEncoded
    @POST("/front/other/alUpdate")
    fun AlimRead(
        @Field("pa_seq") pa_seq: String,
    ): retrofit2.Call<ResultData>?

    @FormUrlEncoded
    @POST("/front/com_router/alActive")
    fun AlimReadCheck(
        @Field("u_seq") u_seq: String,
    ): retrofit2.Call<ResultAlimCheckData>?

    @POST("/front/com_router/loadyakkwan")
    fun getterm(
    ): retrofit2.Call<ResultTermData>?

    @POST("/front/com_router/allianceSelect")
    fun getallience(
    ): retrofit2.Call<ResultAllianceData>?

    @FormUrlEncoded
    @POST("/front/mypage/GetAppVersion")
    fun GetStoreappversion(
        @Field("type") type: String?,
    ): retrofit2.Call<ResultVersionData>?

    @FormUrlEncoded
    @POST("/front/com_router/v1/allimAllUpdate")
    fun allimDelete(
        @Field("u_seq") u_seq: String?,
    ): retrofit2.Call<ResultData>?
    @FormUrlEncoded
    @POST("/front/other/v1/defaultImgInfo")
    fun defaultImage(
        @Field("page") page: String?,
        @Field("ppp") ppp: String?,
    ): retrofit2.Call<ResultImageData>?
}