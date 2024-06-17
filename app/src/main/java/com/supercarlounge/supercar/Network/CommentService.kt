package com.supercarlounge.supercar.Network



import com.supercarlounge.supercar.data.*
import com.google.gson.JsonObject
import retrofit2.http.*


interface CommentService {
   // @Headers("Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NDA5LCJpYXQiOjE2NjM4MzY1MjYsImV4cCI6MTY2MzkyMjkyNn0.6f94ZXZZK2GQDHQIdNOCWJXiMx_H5VZ4rEcmnMy0ZPE")
    @FormUrlEncoded
    @POST("/front/board/mainCommentList")
    fun getAnonymousForumComment(
        @Field("u_seq") u_seq: String?,
        @Field("b_seq") b_seq: String,
        @Field("ppp") ppp: Int?,
        @Field("page") page: Int?
    ): retrofit2.Call<ResultBoardCommentData>?


    @POST("/front/board/v1/comInsert")
    fun insertComment(
        @Body comment: JsonObject,
        ): retrofit2.Call<ResultData>?

    @POST("/front/board/v1/comSubInsert")
    fun insertSubComment(
        @Body comment: JsonObject,
    ): retrofit2.Call<ResultData>?




    @FormUrlEncoded
    @POST("/front/com_router/v1/mainComUpdate")
    fun UpdateComment(
        @Field("cm_seq") cm_seq: String?,
        @Field("cm_values") cm_values: String,
        @Field("u_seq") u_seq: String,
    ): retrofit2.Call<ResultData>?

    @FormUrlEncoded
    @POST("/front/com_router/v1/subComUpdate")
    fun UpdateSubComment(
        @Field("cs_seq") cs_seq: String?,
        @Field("cs_values") cs_values: String,
        @Field("u_seq") u_seq: String
    ): retrofit2.Call<ResultData>?

}