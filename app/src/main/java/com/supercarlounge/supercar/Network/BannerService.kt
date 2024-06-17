package com.supercarlounge.supercar.Network



import com.supercarlounge.supercar.data.ResultBannerData
import com.supercarlounge.supercar.data.ResultData
import com.supercarlounge.supercar.data.ResultOneNotiData
import com.supercarlounge.supercar.data.ResultPopUpinfotData
import retrofit2.http.*

interface BannerService {
  //  @Headers("Authorization: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NDA5LCJpYXQiOjE2NjM4MzY1MjYsImV4cCI6MTY2MzkyMjkyNn0.6f94ZXZZK2GQDHQIdNOCWJXiMx_H5VZ4rEcmnMy0ZPE")
    @FormUrlEncoded
    @POST("/front/com_router/bannerSelect")
    fun getHomeBanner(
        @Field("bl_name") u_id: String?
    ): retrofit2.Call<ResultBannerData>?
  @FormUrlEncoded
  @POST("/front/com_router/bannerSelect")
  fun getHomeBannerTop(
    @Field("bl_name") u_id: String?
  ): retrofit2.Call<ResultBannerData>?
  @FormUrlEncoded
  @POST("/front/other/oneNotice")
  fun getOneNoti(
    @Field("n_seq") n_seq: String?,
  ): retrofit2.Call<ResultOneNotiData>?



  @POST("/front/com_router/popUpinfo")
  fun getPopUpinfo(
  ): retrofit2.Call<ResultPopUpinfotData>?

  @FormUrlEncoded
  @POST("/front/notice/v1/bannerLogSave")
  fun viewLogBanner(
    @Field("bc_seq") bc_seq: Int?,
    @Field("u_seq") u_seq: String?,
    @Field("cl_type") cl_type: String?,
  ): retrofit2.Call<ResultData>?
}