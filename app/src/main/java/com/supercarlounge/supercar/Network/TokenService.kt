package com.supercarlounge.supercar.Network



import com.supercarlounge.supercar.data.ResultTokenData
import retrofit2.http.*

interface TokenService {
   // @Headers("Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NDA5LCJpYXQiOjE2NjM4MzY1MjYsImV4cCI6MTY2MzkyMjkyNn0.6f94ZXZZK2GQDHQIdNOCWJXiMx_H5VZ4rEcmnMy0ZPE")
    @FormUrlEncoded
    @POST("/front/other/login")
    fun getToken(
        @Field("u_phone") u_phone: String?,
        @Field("u_token") u_token: String?,
    ): retrofit2.Call<ResultTokenData>?



}