package com.supercarlounge.supercar.Network



import com.supercarlounge.supercar.data.*
import com.google.gson.JsonObject
import retrofit2.http.*


interface InsertService {
    // @Headers("Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NDA5LCJpYXQiOjE2NjM4MzY1MjYsImV4cCI6MTY2MzkyMjkyNn0.6f94ZXZZK2GQDHQIdNOCWJXiMx_H5VZ4rEcmnMy0ZPE")
    @FormUrlEncoded
    @POST("/front/board/v2/boardLikeSave")
    fun InsertBoardLikeSave(
        @Field("u_seq") u_seq: String?,
        @Field("b_seq") b_seq: String,
        @Field("bl_type") ppp: String?,
        @Field("like_type") page: Int?
    ): retrofit2.Call<ResultTypeData>?

    @POST("/front/board/comMainLike")
    fun insertComMainLike(
        @Body obj: JsonObject,
    ): retrofit2.Call<ResultData>?
    @POST("/front/board/comSubLike")
    fun insertSubComMainLike(
        @Body obj: JsonObject,
    ): retrofit2.Call<ResultData>

}