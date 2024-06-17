package com.supercarlounge.supercar.Network

import com.supercarlounge.supercar.data.ResultData
import com.supercarlounge.supercar.data.ResultUserInfomationCompanyData
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface CompanyService {
    @FormUrlEncoded
    @POST("/front/company/v1/companyInfo")
    fun getUserCompanyInfo(
        @Field("u_seq") u_seq: String?,
    ): retrofit2.Call<ResultUserInfomationCompanyData>?
    @FormUrlEncoded
    @POST("/front/company/v1/companyLogSave")
    fun InsertCompanyLog(
        @Field("cl_phone") cl_phone: String?,
        @Field("u_seq") u_seq: String?,
        @Field("sub_seq") sub_seq: String?,
    ): retrofit2.Call<ResultData>?
}