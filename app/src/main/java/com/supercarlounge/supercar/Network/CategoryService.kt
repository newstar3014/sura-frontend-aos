package com.supercarlounge.supercar.Network



import com.supercarlounge.supercar.data.*
import retrofit2.http.*

interface CategoryService {
   // @Headers("Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NDA5LCJpYXQiOjE2NjM4MzY1MjYsImV4cCI6MTY2MzkyMjkyNn0.6f94ZXZZK2GQDHQIdNOCWJXiMx_H5VZ4rEcmnMy0ZPE")
    @POST("/front/concierge/category")
    fun getCategory(
    ): retrofit2.Call<ResultCategoryData>?

}