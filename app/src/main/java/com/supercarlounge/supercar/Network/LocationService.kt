package com.supercarlounge.supercar.Network


import com.google.gson.JsonObject
import com.supercarlounge.supercar.data.ResultData
import com.supercarlounge.supercar.data.ResultLocationSpotData
import com.supercarlounge.supercar.data.ResultTokenData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface LocationService {
    // @Headers("Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NDA5LCJpYXQiOjE2NjM4MzY1MjYsImV4cCI6MTY2MzkyMjkyNn0.6f94ZXZZK2GQDHQIdNOCWJXiMx_H5VZ4rEcmnMy0ZPE")
    @FormUrlEncoded
    @POST("/front/location_info/v1/spotAllList")
    fun getSpotList(
        @Field("page") page: Int?,
        @Field("ppp") ppp: Int?,
        @Field("u_seq") u_seq: String?,
        @Field("choice") choice: String?,
        @Field("orderChoice") orderChoice: String?,
        @Field("searchVaue") searchVaue: String?,
        @Field("choiceArea")choiceArea:String?,
        @Field("detailArea")detailArea:String?,
//        @Field("filterArr")filterArr:String?,
        @Field("priceStart")priceStart :String?,
        @Field("priceEnd")priceEnd:String?,
        @Field("filterArr")filterArr: List<String>?
    ): retrofit2.Call<ResultLocationSpotData>?


    @FormUrlEncoded
    @POST("/front/location_info/v1/spotDetaiInfo")
    fun getOneSpot(
        @Field("u_seq") u_seq: String?,
        @Field("spot_seq") spot_seq: String?,
    ): retrofit2.Call<ResultLocationSpotData>?

    @FormUrlEncoded
    @POST("/front/location_info/v1/recPlace")
    fun getRecentSpotList(
        @Field("page") page: Int?,
        @Field("ppp") ppp: Int?,
        @Field("spot_seq") u_seq: String?,
    ): retrofit2.Call<ResultLocationSpotData>?


    @Multipart
    @POST("/front/location_info/v1/locationSpotSave")
    fun setLocationWriting(
        @Part("dir") dir: RequestBody,
        @Part locationImage: ArrayList<MultipartBody.Part>,
        @Part("u_seq") u_seq: RequestBody?,
        @Part("spot_area") spot_area: RequestBody?,
        @Part("spot_detail_area") spot_detail_area: RequestBody?,
        @Part("spot_cate") spot_cate: RequestBody?,
        @Part("spot_title") spot_title: RequestBody?,
        @Part("spot_detail") spot_detail: RequestBody?,
        @Part("spot_address") spot_address: RequestBody?,
        @Part("spot_detail_address") spot_detail_address: RequestBody?,
        @Part("spot_phone") spot_phone: RequestBody?,
        @Part("spot_desc") spot_desc: RequestBody?,
        @Part("spot_menu") spot_menu: RequestBody?,
        @Part("spot_fee_use") spot_fee_use: RequestBody?,
        @Part("spot_lat") spot_lat: RequestBody?,
        @Part("spot_lon") spot_lon: RequestBody?,
    ): Call<ResultData>
    @FormUrlEncoded
    @POST("/front/location_info/v1/manyViewsSpot")
    fun getTotalViewList(
        @Field("u_seq") u_seq : String?,
        @Field("page") page: Int?,
        @Field("ppp") ppp: Int?,
    ): retrofit2.Call<ResultLocationSpotData>?
    @FormUrlEncoded
    @POST("/front/location_info/v1/myRecentSpot")
    fun getViewList(
        @Field("u_seq") u_seq : String?,
        @Field("page") page: Int?,
        @Field("ppp") ppp: Int?,
    ): retrofit2.Call<ResultLocationSpotData>?
}