package com.supercarlounge.supercar.Network



import com.supercarlounge.supercar.data.*
import com.google.gson.JsonObject
import retrofit2.http.*

interface ProfileManagementService {

    @FormUrlEncoded
    @POST("/front/user/userRequest")
    fun sendProfileManagement(
        @Field("u_seq") u_seq: String?,
        @Field("type") type: String?,
        @Field("kind") kind: String?,
        @Field("msg") msg: String?,
    ): retrofit2.Call<ResultData>?

    @FormUrlEncoded
    @POST("/front/user/userRequest/one")
    fun userRequestOne(
        @Field("u_seq") u_seq: String?,
    ): retrofit2.Call<ResultUserRequestData>?

    @FormUrlEncoded
    @POST("/front/user/userRequest/restore")
    fun userRequestRestore(
        @Field("u_seq") u_seq: String?,
        @Field("type") type: String?,
        @Field("kind") kind: String?,
        @Field("msg") msg: String?,
    ): retrofit2.Call<ResultData>?

    @FormUrlEncoded
    @POST("/front/user/is_rest_can")
    fun userIsRestCan(
        @Field("u_seq") u_seq: String?,
    ): retrofit2.Call<ResultUserRequestData>?

    @FormUrlEncoded
    @POST("/front/user/userdelete")
    fun userDelete(
        @Field("u_seq") u_seq: String?,
    ): retrofit2.Call<ResultData>?
}