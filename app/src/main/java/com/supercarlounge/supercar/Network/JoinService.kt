package com.supercarlounge.supercar.Network



import com.supercarlounge.supercar.data.*
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface JoinService {

    @Multipart
    @POST("/front/other/v3/userInsert")
    fun setJoinOwner(
        @Part("dir") dir: RequestBody,
        @Part mainProfile: MultipartBody.Part,
        @Part subProfile: ArrayList<MultipartBody.Part>,
        @Part("userObj") userObj: JsonObject,
        @Part("carObj") carObj: JsonObject?,
        @Part mainCar: MultipartBody.Part,
        @Part insurance: MultipartBody.Part,
    ): Call<ResultData>


    @Multipart
    @POST("/front/other/v3/userInsert")
    fun setJoinPassengerWoman(
        @Part("dir") dir: RequestBody,
        @Part mainProfile: MultipartBody.Part,
        @Part subProfile: ArrayList<MultipartBody.Part>,
        @Part("userObj") userObj: JsonObject,
    ): Call<ResultData>


    @Multipart
    @POST("/front/other/v3/userInsert")
    fun setJoinPassengerMan(
        @Part("dir") dir: RequestBody,
        @Part mainProfile: MultipartBody.Part,
        @Part subProfile: ArrayList<MultipartBody.Part>,
        @Part("userObj") userObj: JsonObject,
        @Part income: MultipartBody.Part,
    ): Call<ResultData>


    //퀵유저 업데이트




    @Multipart
    @POST("/front/other/quickUpdate")
    fun setQuickUserJoinOwner(
        @Part("dir") dir: RequestBody,
        @Part mainProfile: MultipartBody.Part,
        @Part subProfile: ArrayList<MultipartBody.Part>,
        @Part("userObj") userObj: JsonObject,
        @Part("carObj") carObj: JsonObject?,
        @Part mainCar: MultipartBody.Part,
        @Part insurance: MultipartBody.Part,
    ): Call<ResultData>
    @Multipart
    @POST("/front/other/quickUpdate")
    fun setQuickUserJoinPassenger(
        @Part("dir") dir: RequestBody,
        @Part mainProfile: MultipartBody.Part,
        @Part subProfile: ArrayList<MultipartBody.Part>,
        @Part("userObj") userObj: JsonObject,
        @Part income: MultipartBody.Part,
    ): Call<ResultData>
    @Multipart
    @POST("/front/other/quickUpdate")
    fun setQuickUserJoinWomanPassenger(
        @Part("dir") dir: RequestBody,
        @Part mainProfile: MultipartBody.Part,
        @Part subProfile: ArrayList<MultipartBody.Part>,
        @Part("userObj") userObj: JsonObject,
    ): Call<ResultData>


   @FormUrlEncoded
    @POST("/car/brand/list")
    fun getCarBrandList(
        @Field("target") target: String?,
    ): retrofit2.Call<ArrayList<CarBrandListData>>?

    @FormUrlEncoded
    @POST("/car/model/list")
    fun getCarModelList(
        @Field("carb_seq") carb_seq: String?,
        @Field("target") target: String?,
    ): retrofit2.Call<ArrayList<CarModelListData>>?

    @FormUrlEncoded
    @POST("/car/sub_model/list")
    fun getCarSubModelList(
        @Field("carm_seq") carm_seq: String?,
    ): retrofit2.Call<ArrayList<CarSubModelListData>>?

    @FormUrlEncoded
    @POST("/front/user/nicknameCheck")
    fun nicknameCheck(
        @Field("u_nickname") u_nickname: String?,
    ): retrofit2.Call<ResultData>?

    @FormUrlEncoded
    @POST("/front/other/joinLog")
    fun joinLog(
        @Field("jl_device") jl_device: String?,
        @Field("jl_step") jl_step: String?,
        @Field("jl_uuid") jl_mac: String?,
        @Field("jl_model") jl_model: String?,
        @Field("jl_name") jl_name: String?,
    ): retrofit2.Call<ResultData>?

    @FormUrlEncoded
    @POST("/front/user/useCodeCheck")
    fun InsertInvitationCodeCehck(
        @Field("use_code") use_code: String?,
    ): retrofit2.Call<ResultData>?

    @FormUrlEncoded
    @POST("/front/user/updateCode")
    fun InsertInvitationCodeSave(
        @Field("u_seq") u_seq: String?,
        @Field("u_code") u_code: String?,
    ): retrofit2.Call<ResultData>?

    @FormUrlEncoded
    @POST("/front/other/return")
    fun getCompanion(
        @Field("u_seq") u_seq: String?,
    ): retrofit2.Call<ResultCompanionData>?
}