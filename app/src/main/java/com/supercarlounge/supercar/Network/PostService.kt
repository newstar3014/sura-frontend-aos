package com.supercarlounge.supercar.Network



import com.supercarlounge.supercar.data.*
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface PostService {
   // @Headers("Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NDA5LCJpYXQiOjE2NjM4MzY1MjYsImV4cCI6MTY2MzkyMjkyNn0.6f94ZXZZK2GQDHQIdNOCWJXiMx_H5VZ4rEcmnMy0ZPE")

   // @Headers("Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NDA5LCJpYXQiOjE2NjM4MzY1MjYsImV4cCI6MTY2MzkyMjkyNn0.6f94ZXZZK2GQDHQIdNOCWJXiMx_H5VZ4rEcmnMy0ZPE")

    @POST("/front/notice/noticeTopList")
    fun getAnonymousForumPostNoti(
    ): retrofit2.Call<ResultNotiData>?


    @POST("/front/board/v4/boardList")
    fun getAnonymousForumPost(
//        @Field("u_seq") u_seq: Int?,
//        @Field("c_seq") c_seq: String,
//        @Field("b_contents") b_contents: String?,
//        @Field("bookmark")bookmark :String?,
//        @Field("page") page: Int,
//        @Field("ppp") ppp: Int,
        @Body board: JsonObject,
    ): retrofit2.Call<ResultBoardData>?

    @FormUrlEncoded
    @POST("/front/board/v2/boardOneInfo")
    fun getAnonymousForumDeepPost(
        @Field("b_seq") b_seq: String?,
        @Field("u_seq") u_seq: String,
        @Field("sub_seq") sub_seq: Int?,
    ): retrofit2.Call<ResultBoardDetailData>?


    //익명 & 드라이브 게시글삭제
    @FormUrlEncoded
    @POST("/front/com_router/v1/boardDelete")
    fun anonymousForumPostDelete(
        @Field("b_seq") b_seq: String?,
        @Field("u_seq") u_seq: String?
    ): retrofit2.Call<ResultData>?


    //댓글 삭제
    @FormUrlEncoded
    @POST("/front/com_router/v1/ob/mainComDelete")
    fun anonymousForumCommentDelete(
        @Field("cm_seq") cm_seq: String?,
        @Field("u_seq") u_seq: String?,
    ): retrofit2.Call<ResultData>?

    //대댓글 삭제
    @FormUrlEncoded
    @POST("/front/com_router/v1/ob/subComDelete2")
    fun SubCommentDelete(
        @Field("cs_seq") cs_seq: String?,
        @Field("u_seq") u_seq: String?,
    ): retrofit2.Call<ResultData>?
    @FormUrlEncoded
    @POST("/front/com_router/myBoardLikeList")
    fun getLikeList(
        @Field("b_seq") b_seq: String?,
        @Field("ppp") ppp: Int,
        @Field("page") page: Int?,
    ): retrofit2.Call<ResultLikeListData>?

    @Multipart
    @POST("/front/board/v1/boardInsert")
    fun boardInsert(
        @Part boardCover: MultipartBody.Part,
        @Part("dir") dir: RequestBody,
        @Part("board") board: JsonObject,
    ): Call<ResultBoardInsertData>

    @Multipart
    @POST("/front/board/v1/boardInsert")
    fun boardNoPhotoInsert(
        @Part("dir") dir: RequestBody,
        @Part("board") board: JsonObject,
    ): Call<ResultBoardInsertData>

    @Multipart
    @POST("/front/board/boardUpdate")
    fun boardEditInsert(
        @Part boardCover: MultipartBody.Part,
        @Part("dir") dir: RequestBody,
        @Part("board") board: JsonObject,
    ): Call<ResultBoardInsertData>

    @Multipart
    @POST("/front/board/boardUpdate")
    fun boardNoPhotoEditInsert(
        @Part("dir") dir: RequestBody,
        @Part("board") board: JsonObject,
    ): Call<ResultBoardInsertData>

    @POST("/front/police/block/insert")
    fun PostBlockInsert(
        @Body obj: JsonObject,
    ): retrofit2.Call<ResultData>?

    @POST("/front/police/insert")
    fun PoliceInsert(
        @Body obj: JsonObject,
    ): retrofit2.Call<ResultData>?

    @FormUrlEncoded
    @POST("/front/bookmark/bookMark")
    fun InsertBookmark(
        @Field("bm_type") bm_type: String?,
        @Field("u_seq") u_seq: String,
        @Field("use_seq") use_seq: String?,
    ): retrofit2.Call<ResultData>?


    @POST("/front/board/v4/boardList")
    fun getAnonymousForumPostScroll(
        @Body board: JsonObject,
    ): retrofit2.Call<ResultBoardData>?

    @FormUrlEncoded
    @POST("/front/other/oneBoard")
    fun getOneBoard(
        @Field("b_seq") b_seq: String?,
    ): retrofit2.Call<ResultOneBoardData>?



    @FormUrlEncoded
    @POST("/front/board/boardViewsUpdate")
    fun boardViewsUpdate(
        @Field("b_seq") b_seq: String?,
        @Field("u_seq") u_seq: String,
    ): retrofit2.Call<ResultData>?

    @FormUrlEncoded
    @POST("/front/other/openProfileUser")
    fun boardViewList(
        @Field("b_seq") b_seq: String?,
        @Field("u_seq") u_seq: String,
    ): retrofit2.Call<ResultViewListData>?

    @FormUrlEncoded
    @POST("/front/other/v3/profileCheck")
    fun boardViewCheck(
        @Field("b_seq") b_seq: String?,
        @Field("u_seq") u_seq: String,
        @Field("sub_seq") sub_seq: String,
    ): retrofit2.Call<ResultProfileReadCheck>?
}