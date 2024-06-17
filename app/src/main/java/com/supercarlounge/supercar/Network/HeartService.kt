package com.supercarlounge.supercar.Network

import com.supercarlounge.supercar.data.ResultData
import com.supercarlounge.supercar.data.ResultDrivePassData
import com.supercarlounge.supercar.data.ResultHeartCheckData
import com.supercarlounge.supercar.data.ResultHeartLogData
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface HeartService {
    @FormUrlEncoded
    @POST("/front/com_router/v1/paymentHeart")
    fun HeartPayment(
        @Field("ph_type") ph_type: String?,
        @Field("u_seq") u_seq: String?,
        @Field("ph_product_id") ph_product_id: String?,
        @Field("ph_purchase_token") ph_purchase_token: String?,
        @Field("ph_orderId") ph_orderId: String?,
        @Field("ph_price") ph_price: String?,
        @Field("ph_heart") ph_heart: String?,
    ): retrofit2.Call<ResultData>?

    @FormUrlEncoded
    @POST("/front/com_router/v1/driveBuyPass")
    fun DrivePassPayment(
        @Field("u_seq") u_seq: String?,
        @Field("hl_type") hl_type: String?,
        @Field("use_seq") use_seq: String?,
        @Field("hl_count") hl_count: String?,
    ): retrofit2.Call<ResultDrivePassData>?

    @FormUrlEncoded
    @POST("/front/heart/heartUseLog")
    fun GetHeartLog(
        @Field("u_seq") u_seq: String?,
    ): retrofit2.Call<ResultHeartLogData>?


    @FormUrlEncoded
    @POST("/front/other/myheartCheck")
    fun GetMyheart(
        @Field("u_seq") u_seq: String?,
    ): retrofit2.Call<ResultHeartCheckData>?
}