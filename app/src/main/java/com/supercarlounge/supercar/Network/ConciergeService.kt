package com.supercarlounge.supercar.Network



import com.supercarlounge.supercar.data.*
import retrofit2.http.*

interface ConciergeService {

   // @Headers("Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NDA5LCJpYXQiOjE2NjM4MzY1MjYsImV4cCI6MTY2MzkyMjkyNn0.6f94ZXZZK2GQDHQIdNOCWJXiMx_H5VZ4rEcmnMy0ZPE")
    @FormUrlEncoded
    @POST("/front/concierge/all")
    fun getConcierge(
        @Field("cc_seq") cc_seq: String?,
    ):
            retrofit2.Call<ResultConciergeData>?

}