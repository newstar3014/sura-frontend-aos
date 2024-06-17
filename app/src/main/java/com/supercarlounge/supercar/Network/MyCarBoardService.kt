package com.supercarlounge.supercar.Network



import com.supercarlounge.supercar.data.*
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface MyCarBoardService {

    @Multipart
    @POST("/front/my_car/v1/myCarSave")
    fun insertMyCarBoard(
        @Part("dir") dir: RequestBody,
        @Part myCarMain: MultipartBody.Part,
        @Part myCarSub: ArrayList<MultipartBody.Part>,
        @Part("u_seq") u_seq: RequestBody,
        @Part("mmi_title") mmi_title: RequestBody,
        @Part("mmi_contents") mmi_contents: RequestBody,
        @Part("msi_contents") msi_contents: RequestBody,
        @Part("carb_seq") carb_seq: RequestBody,
        @Part("carm_seq") carm_seq: RequestBody?,
        @Part("csm_seq") csm_seq: RequestBody?,
        @Part("mmi_first") mmi_first: RequestBody?,
        @Part("mmi_second") mmi_second: RequestBody?,
        @Part("mmi_third") mmi_third: RequestBody?,
    ): Call<ResultData>

    @Multipart
    @POST("/front/my_car/v1/myCarSave")
    fun insertMyCarNoSubBoard(
        @Part("dir") dir: RequestBody,
        @Part myCarMain: MultipartBody.Part,
        @Part("u_seq") u_seq: RequestBody,
        @Part("mmi_title") mmi_title: RequestBody,
        @Part("mmi_contents") mmi_contents: RequestBody,
        @Part("carb_seq") carb_seq: RequestBody,
        @Part("carm_seq") carm_seq: RequestBody?,
        @Part("csm_seq") csm_seq: RequestBody?,
        @Part("mmi_first") mmi_first: RequestBody?,
        @Part("mmi_second") mmi_second: RequestBody?,
        @Part("mmi_third") mmi_third: RequestBody?,
    ): Call<ResultData>

    @Multipart
    @POST("/front/my_car/v1/myCarUpdate")
    fun updateMyCarBoard(
        @Part("dir") dir: RequestBody,
        @Part myCarMain: MultipartBody.Part,
        @Part myCarSub: ArrayList<MultipartBody.Part>,
        @Part myCarSubInsert: ArrayList<MultipartBody.Part>,
        @Part ("subInsertArr")subInsertArr: RequestBody,
        @Part ("subImgSeqArr")subImgSeqArr: RequestBody,
        @Part ("subDeleteArr")subDeleteArr: RequestBody,
        @Part("mmi_seq") mmi_seq: RequestBody,
        @Part("u_seq") u_seq: RequestBody,
        @Part("mmi_title") mmi_title: RequestBody,
        @Part("mmi_contents") mmi_contents: RequestBody,
        @Part("msi_contents") msi_contents: RequestBody,
        @Part("carb_seq") carb_seq: RequestBody,
        @Part("carm_seq") carm_seq: RequestBody?,
        @Part("csm_seq") csm_seq: RequestBody?,
        @Part("mmi_first") mmi_first: RequestBody?,
        @Part("mmi_second") mmi_second: RequestBody?,
        @Part("mmi_third") mmi_third: RequestBody?,
    ): Call<ResultData>

    @Multipart
    @POST("/front/my_car/v1/myCarUpdate")
    fun updateAllNoMyCarBoard(
        @Part("dir") dir: RequestBody,
        @Part ("subDeleteArr")subDeleteArr: RequestBody,
        @Part("mmi_seq") mmi_seq: RequestBody,
        @Part("u_seq") u_seq: RequestBody,
        @Part("mmi_title") mmi_title: RequestBody,
        @Part("mmi_contents") mmi_contents: RequestBody,
        @Part("msi_contents") msi_contents: RequestBody,
        @Part("carb_seq") carb_seq: RequestBody,
        @Part("carm_seq") carm_seq: RequestBody?,
        @Part("csm_seq") csm_seq: RequestBody?,
        @Part("mmi_first") mmi_first: RequestBody?,
        @Part("mmi_second") mmi_second: RequestBody?,
        @Part("mmi_third") mmi_third: RequestBody?,
    ): Call<ResultData>

    @Multipart
    @POST("/front/my_car/v1/myCarUpdate")
    fun updateMainCarEditMyCarSubInsertMyCarBoard(
        @Part("dir") dir: RequestBody,
        @Part myCarMain: MultipartBody.Part,
        @Part myCarSubInsert: ArrayList<MultipartBody.Part>,
        @Part ("subInsertArr")subInsertArr: RequestBody,
        @Part ("subImgSeqArr")subImgSeqArr: RequestBody,
        @Part ("subDeleteArr")subDeleteArr: RequestBody,
        @Part("mmi_seq") mmi_seq: RequestBody,
        @Part("u_seq") u_seq: RequestBody,
        @Part("mmi_title") mmi_title: RequestBody,
        @Part("mmi_contents") mmi_contents: RequestBody,
        @Part("msi_contents") msi_contents: RequestBody,
        @Part("carb_seq") carb_seq: RequestBody,
        @Part("carm_seq") carm_seq: RequestBody?,
        @Part("csm_seq") csm_seq: RequestBody?,
        @Part("mmi_first") mmi_first: RequestBody?,
        @Part("mmi_second") mmi_second: RequestBody?,
        @Part("mmi_third") mmi_third: RequestBody?,
    ): Call<ResultData>


    @Multipart
    @POST("/front/my_car/v1/myCarUpdate")
    fun updateSubEditMyCarMainMyCarBoard(
        @Part("dir") dir: RequestBody,
        @Part myCarMain: MultipartBody.Part,
        @Part myCarSub: ArrayList<MultipartBody.Part>,
        @Part ("subImgSeqArr")subImgSeqArr: RequestBody,
        @Part ("subDeleteArr")subDeleteArr: RequestBody,

        @Part("mmi_seq") mmi_seq: RequestBody,
        @Part("u_seq") u_seq: RequestBody,
        @Part("mmi_title") mmi_title: RequestBody,
        @Part("mmi_contents") mmi_contents: RequestBody,
        @Part("msi_contents") msi_contents: RequestBody,
        @Part("carb_seq") carb_seq: RequestBody,
        @Part("carm_seq") carm_seq: RequestBody?,
        @Part("csm_seq") csm_seq: RequestBody?,
        @Part("mmi_first") mmi_first: RequestBody?,
        @Part("mmi_second") mmi_second: RequestBody?,
        @Part("mmi_third") mmi_third: RequestBody?,
    ): Call<ResultData>

    @Multipart
    @POST("/front/my_car/v1/myCarUpdate")
    fun updateSubEditMyCarSubInsertMyCarBoard(
        @Part("dir") dir: RequestBody,
        @Part myCarSub: ArrayList<MultipartBody.Part>,
        @Part myCarSubInsert: ArrayList<MultipartBody.Part>,
        @Part ("subInsertArr")subInsertArr: RequestBody,
        @Part ("subImgSeqArr")subImgSeqArr: RequestBody,
        @Part ("subDeleteArr")subDeleteArr: RequestBody,
        @Part("mmi_seq") mmi_seq: RequestBody,
        @Part("u_seq") u_seq: RequestBody,
        @Part("mmi_title") mmi_title: RequestBody,
        @Part("mmi_contents") mmi_contents: RequestBody,
        @Part("msi_contents") msi_contents: RequestBody,
        @Part("carb_seq") carb_seq: RequestBody,
        @Part("carm_seq") carm_seq: RequestBody?,
        @Part("csm_seq") csm_seq: RequestBody?,
        @Part("mmi_first") mmi_first: RequestBody?,
        @Part("mmi_second") mmi_second: RequestBody?,
        @Part("mmi_third") mmi_third: RequestBody?,
    ): Call<ResultData>
    @Multipart
    @POST("/front/my_car/v1/myCarUpdate")
    fun updateOnlyMyCarSubInsertMyCarBoard(
        @Part("dir") dir: RequestBody,
        @Part myCarSubInsert: ArrayList<MultipartBody.Part>,
        @Part ("subInsertArr")subInsertArr: RequestBody,
        @Part ("subImgSeqArr")subImgSeqArr: RequestBody,
        @Part ("subDeleteArr")subDeleteArr: RequestBody,
        @Part("mmi_seq") mmi_seq: RequestBody,
        @Part("u_seq") u_seq: RequestBody,
        @Part("mmi_title") mmi_title: RequestBody,
        @Part("mmi_contents") mmi_contents: RequestBody,
        @Part("msi_contents") msi_contents: RequestBody,
        @Part("carb_seq") carb_seq: RequestBody,
        @Part("carm_seq") carm_seq: RequestBody?,
        @Part("csm_seq") csm_seq: RequestBody?,
        @Part("mmi_first") mmi_first: RequestBody?,
        @Part("mmi_second") mmi_second: RequestBody?,
        @Part("mmi_third") mmi_third: RequestBody?,
    ): Call<ResultData>
    @Multipart
    @POST("/front/my_car/v1/myCarUpdate")
    fun updateSubMyCarBoard(
        @Part("dir") dir: RequestBody,
        @Part myCarSub: ArrayList<MultipartBody.Part>,
        @Part ("subImgSeqArr")subImgSeqArr: RequestBody,
        @Part ("subDeleteArr")subDeleteArr: RequestBody,
        @Part("mmi_seq") mmi_seq: RequestBody,
        @Part("u_seq") u_seq: RequestBody,
        @Part("mmi_title") mmi_title: RequestBody,
        @Part("mmi_contents") mmi_contents: RequestBody,
        @Part("msi_contents") msi_contents: RequestBody,
        @Part("carb_seq") carb_seq: RequestBody,
        @Part("carm_seq") carm_seq: RequestBody?,
        @Part("csm_seq") csm_seq: RequestBody?,
        @Part("mmi_first") mmi_first: RequestBody?,
        @Part("mmi_second") mmi_second: RequestBody?,
        @Part("mmi_third") mmi_third: RequestBody?,
    ): Call<ResultData>

    @Multipart
    @POST("/front/my_car/v1/myCarUpdate")
    fun updateMyCarNoSubBoard(
        @Part("dir") dir: RequestBody,
        @Part myCarMain: MultipartBody.Part,
        @Part ("subDeleteArr")subDeleteArr: RequestBody,
        @Part("mmi_seq") mmi_seq: RequestBody,
        @Part("u_seq") u_seq: RequestBody,
        @Part("mmi_title") mmi_title: RequestBody,
        @Part("mmi_contents") mmi_contents: RequestBody,
        @Part("msi_contents") msi_contents: RequestBody,
        @Part("carb_seq") carb_seq: RequestBody,
        @Part("carm_seq") carm_seq: RequestBody?,
        @Part("csm_seq") csm_seq: RequestBody?,
        @Part("mmi_first") mmi_first: RequestBody?,
        @Part("mmi_second") mmi_second: RequestBody?,
        @Part("mmi_third") mmi_third: RequestBody?,
    ): Call<ResultData>


    @FormUrlEncoded
    @POST("/front/my_car/v1/myCarMainList")
    fun getMyCarBoardList(
        @Field("page") page: Int?,
        @Field("ppp") ppp: Int?,
        @Field("u_seq") u_seq: String?,
        @Field("bookMark") bookMark: String?,
        @Field("keyword") keyword: String?,
    ): Call<ResultMyCaBoardData>

    @FormUrlEncoded
    @POST("/front/my_car/v1/myCarRankingInfo")
    fun getMyCarBestBoardList(
        @Field("rankDate") rankDate: String?,
        @Field("u_seq") u_seq: Int?,
        @Field("page") page: Int?,
        @Field("ppp") ppp: Int?,
    ): Call<ResultMyCaBoardData>

    @FormUrlEncoded
    @POST("/front/my_car/v1/myCarRankDateList")
    fun getStartDate(
        @Field("u_seq") u_seq: Int?,
    ): Call<ResultStartDate>

    @FormUrlEncoded
    @POST("/front/my_car/v1/myCarSubList")
    fun getMyCarBoardSubList(
        @Field("mmi_seq") mmi_seq: Int?,
        @Field("u_seq") u_seq: String?,
    ): Call<ResultMyCaBoardSubData>

    @FormUrlEncoded
    @POST("/front/my_car/v1/myCarOneBoardInfo")
    fun getMyCarBoardUpdateData(
        @Field("mmi_seq") mmi_seq: Int?,
        @Field("u_seq") u_seq: String?,
    ): Call<ResultMyCaBoardData>

    @FormUrlEncoded
    @POST("/front/my_car/v1/myCarViewsSave")
    fun insertViewSave(
        @Field("mmi_seq") mmi_seq: Int?,
        @Field("u_seq") u_seq: String?,
    ): Call<ResultData>
    @FormUrlEncoded
    @POST("/front/my_car/v1/myCarMainInfoDelete")
    fun deleteMyCarBoard(
        @Field("mmi_seq") mmi_seq: Int?,
        @Field("u_seq") u_seq: String?,
    ): Call<ResultData>

    @FormUrlEncoded
    @POST("/front/my_car/v2/myCarCommentInfo")
    fun getMyCarComment(
        @Field("mmi_seq") mmi_seq: Int?,
        @Field("u_seq") u_seq: Int?,
    ): Call<ResultMyCaBoardCommentData>

    @FormUrlEncoded
    @POST("/front/my_car/v2/myCarCommentSave")
    fun insertComment(
        @Field("u_seq") u_seq: String?,
        @Field("mmi_seq") mmi_seq: Int?,
        @Field("mmc_values") mmc_values: String?,
    ): Call<ResultData>
    @FormUrlEncoded
    @POST("/front/my_car/v2/myCarSubCommentSave")
    fun insertSubComment(
        @Field("u_seq") u_seq: String?,
        @Field("mmi_seq") mmi_seq: Int?,
        @Field("mmc_seq") mmc_seq: String?,
        @Field("msc_values") msc_values: String?,
        @Field("msc_sub_seq") msc_sub_seq: String?,
    ): Call<ResultData>
    @FormUrlEncoded
    @POST("/front/my_car/v1/myCarMainCommentUpdate")
    fun updateComment(
        @Field("mmc_seq") mmc_seq: String?,
        @Field("mmi_seq") mmi_seq: Int?,
        @Field("u_seq") u_seq: Int?,
        @Field("mmc_values") mmc_values: String?,
    ): Call<ResultData>

    @FormUrlEncoded
    @POST("/front/my_car/v1/myCarSubCommentUpdate")
    fun updateSubComment(
        @Field("msc_seq") msc_seq: String?,
        @Field("u_seq") u_seq: Int?,
        @Field("mmi_seq") mmi_seq: Int?,
        @Field("msc_values") msc_values: String?,
    ): Call<ResultData>

    @FormUrlEncoded
    @POST("/front/my_car/v1/myCarMainCommentDelete")
    fun deleteComment(
        @Field("u_seq") u_seq: String?,
        @Field("mmc_seq") mmc_seq: String?,
        @Field("mmi_seq") mmi_seq: String?,
    ): Call<ResultData>
    @FormUrlEncoded
    @POST("/front/my_car/v1/myCarSubCommentDelete")
    fun deleteSubComment(
        @Field("msc_seq") msc_seq: String?,
        @Field("u_seq") u_seq: String?,
        @Field("mmi_seq") mmi_seq: String?,
    ): Call<ResultData>

    @FormUrlEncoded
    @POST("/front/my_car/v2/myCarLikeSave")
    fun myCarLikeSave(
        @Field("mmi_seq") mmi_seq: String?,
        @Field("u_seq") u_seq: String?,
    ): Call<ResultData>


    @POST("/front/my_car/v1/myCarEventList")
    fun getMyCarHot(
    ): Call<ResultMyCarHotData>

    @FormUrlEncoded
    @POST("/front/other/v2/myCarOpenProfile")
    fun ProfileRead(
        @Field("mmi_seq") mmi_seq: String?,
        @Field("u_seq") u_seq: String?,
        @Field("sub_seq") sub_seq: String?,
        @Field("u_heart") u_heart: String?,
        @Field("b_type") b_type: String?,
        @Field("hl_type") hl_type: String?,
        @Field("pc_b_message") pc_b_message: String?,
        @Field("given_nickname") given_nickname: String?,
    ): retrofit2.Call<ResultProfileReadData>?

    @FormUrlEncoded
    @POST("/front/my_car/v1/myCarOneBoardInfo")
    fun getMycarOne(
        @Field("mmi_seq") mmi_seq: String?,
        @Field("u_seq") u_seq: String?,
    ): Call<ResultMyCaBoardData>
}