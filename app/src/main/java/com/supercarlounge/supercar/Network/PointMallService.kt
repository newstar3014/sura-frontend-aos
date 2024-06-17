package com.supercarlounge.supercar.Network

import com.supercarlounge.supercar.data.*
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PointMallService {
    @FormUrlEncoded
    @POST("/front/point_mall/v1/pointMallList")
    fun GetPointProductlist(
        @Field("page") page: Int?,
        @Field("ppp") ppp: Int?,
    ): retrofit2.Call<ResultPointMallProductData>?
    @FormUrlEncoded
    @POST("/front/point_mall/v1/pointLogList")
    fun GetPointLogList(
        @Field("u_seq") u_seq: String?,
        @Field("point_check") point_check: String?,
        @Field("page") page: Int?,
        @Field("ppp") ppp: Int?,
    ): retrofit2.Call<ResultPointMallLogData>?
    @FormUrlEncoded
    @POST("/front/point_mall/v1/myPointInfo")
    fun GetMyPoint(
        @Field("u_seq") u_seq: String?
    ): retrofit2.Call<ResultPointData>?

    @FormUrlEncoded
    @POST("/front/point_mall/v1/pointMinus")
    fun BuyProduct(
        @Field("u_seq") u_seq: String?,
        @Field("goodsNo") goodsNo: String?,
        @Field("goodsCode") goodsCode: String?,
    ): retrofit2.Call<ResultData>?

}