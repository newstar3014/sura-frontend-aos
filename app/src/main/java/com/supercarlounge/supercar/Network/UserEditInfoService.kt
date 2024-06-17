package com.supercarlounge.supercar.Network



import com.supercarlounge.supercar.data.*
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.http.*

interface UserEditInfoService {




    //메인사진이 변경 안 됐을경우
    @Multipart
    @POST("/front/user/v3/userEditInfo")
    fun userNoMainProfileEditInfo(
        @Part("u_seq") u_seq: RequestBody,
        @Part("u_tempAbout") u_about_me: RequestBody,
        @Part subProfile: ArrayList<MultipartBody.Part>,
        @Part ("subProfileBeforePath") subProfileBeforePath : ArrayList<SubImage>,
        @Part ("deleteSubProfile")deleteSubProfile : ArrayList<SubImage>,
        @Part mainCar:  ArrayList<MultipartBody.Part>,
        @Part ("carImageBeforePath")carImageBeforePath : ArrayList<String>,
        @Part ("deleteMainCar")deleteMainCar : ArrayList<UserInformationCarData>,
        @Part insurance: ArrayList<MultipartBody.Part>,
        @Part ("insurBeforePath") insurBeforePath : ArrayList<String>,
        @Part("userObj") userObj: JsonObject,
        @Part("userTempReturn") userTempReturn: JsonObject,
        @Part("carListOb") carListOb: JSONArray,
        @Part("inputObj") inputObj: JsonObject,
    ): Call<ResultData>
    //서브사진이 변경 안 됐을경우
    @Multipart
    @POST("/front/user/v3/userEditInfo")
    fun userNoSubProfileEditInfo(
        @Part("u_seq") u_seq: RequestBody,
        @Part("u_tempAbout") u_about_me: RequestBody,
        @Part mainProfile:  MultipartBody.Part?,
        @Part("delete_mainImageUrl") delete_mainImageUrl: RequestBody,
        @Part mainCar:  ArrayList<MultipartBody.Part>,
        @Part ("carImageBeforePath")carImageBeforePath : ArrayList<String>,
        @Part ("deleteMainCar")deleteMainCar : ArrayList<UserInformationCarData>,
        @Part insurance: ArrayList<MultipartBody.Part>,
        @Part ("insurBeforePath") insurBeforePath : ArrayList<String>,
        @Part("userObj") userObj: JsonObject,
        @Part("userTempReturn") userTempReturn: JsonObject,
        @Part("carListOb") carListOb: JSONArray,
        @Part("inputObj") inputObj: JsonObject,
    ): Call<ResultData>
    //메인사진,서브사진이 변경 안 됐을경우
    @Multipart
    @POST("/front/user/v3/userEditInfo")
    fun userNoProfileEditInfo(
        @Part("u_seq") u_seq: RequestBody,
        @Part("u_tempAbout") u_about_me: RequestBody,
        @Part mainCar:  ArrayList<MultipartBody.Part>,
        @Part ("carImageBeforePath")carImageBeforePath : ArrayList<String>,
        @Part ("deleteMainCar")deleteMainCar : ArrayList<UserInformationCarData>,
        @Part insurance: ArrayList<MultipartBody.Part>,
        @Part ("insurBeforePath") insurBeforePath : ArrayList<String>,
        @Part("userObj") userObj: JsonObject,
        @Part("userTempReturn") userTempReturn: JsonObject,
        @Part("carListOb") carListOb: JSONArray,
        @Part("inputObj") inputObj: JsonObject,
    ): Call<ResultData>

    //메인사진 , 서브사진 둘다 변경 됐을경우
    @Multipart
    @POST("/front/user/v3/userEditInfo")
    fun userProfileEditInfo(
        @Part("u_seq") u_seq: RequestBody,
        @Part("u_tempAbout") u_about_me: RequestBody,
        @Part mainProfile:  MultipartBody.Part?,
        @Part("delete_mainImageUrl") delete_mainImageUrl: RequestBody,
        @Part subProfile: ArrayList<MultipartBody.Part>,
        @Part ("subProfileBeforePath") subProfileBeforePath : ArrayList<SubImage>,
        @Part ("deleteSubProfile")deleteSubProfile : ArrayList<SubImage>,
        @Part mainCar :  ArrayList<MultipartBody.Part>,
        @Part ("carImageBeforePath")carImageBeforePath : ArrayList<String>,
        @Part ("deleteMainCar")deleteMainCar : ArrayList<UserInformationCarData>,
        @Part insurance: ArrayList<MultipartBody.Part>,
        @Part ("insurBeforePath") insurBeforePath : ArrayList<String>,
        @Part("userObj") userObj: JsonObject,
        @Part("userTempReturn") userTempReturn: JsonObject,
        @Part("carListOb") carListOb: JSONArray,
        @Part("inputObj") inputObj: JsonObject,
    ): Call<ResultData>


    //서브사진이 삭제 됐을경우

    @Multipart
    @POST("/front/user/v3/userEditInfo")
    fun userSubProfileDeleteInfo(
        @Part("u_seq") u_seq: RequestBody,
        @Part("u_tempAbout") u_about_me: RequestBody,
        @Part ("subProfileBeforePath") subProfileBeforePath : ArrayList<SubImage>,
        @Part ("deleteSubProfile")deleteSubProfile : ArrayList<SubImage>,
        @Part mainCar:  ArrayList<MultipartBody.Part>,
        @Part ("carImageBeforePath")carImageBeforePath : ArrayList<String>,
        @Part ("deleteMainCar")deleteMainCar : ArrayList<UserInformationCarData>,
        @Part insurance: ArrayList<MultipartBody.Part>,
        @Part ("insurBeforePath") insurBeforePath : ArrayList<String>,
        @Part("userObj") userObj: JsonObject,
        @Part("userTempReturn") userTempReturn: JsonObject,
        @Part("carListOb") carListOb: JSONArray,
        @Part("inputObj") inputObj: JsonObject,
    ): Call<ResultData>

















//    반려수정


    @Multipart
    @POST("/front/user/v2/userReturn")
    fun userCompanionEditOwner(
        //자동차 사진
        @Part mainCar:  MultipartBody.Part,
        //보험 사진
        @Part insurance: MultipartBody.Part,
        @Part("u_seq") u_seq: RequestBody,
        @Part("userObj") userObj: JsonObject,
    ): Call<ResultData>

    @Multipart
    @POST("/front/user/v2/userReturn")
    fun userCompanionEditOwner_Car(
        //자동차 사진
        @Part mainCar:  MultipartBody.Part,
        @Part("u_seq") u_seq: RequestBody,
        @Part("userObj") userObj: JsonObject,
        ): Call<ResultData>

    @Multipart
    @POST("/front/user/v2/userReturn")
    fun userCompanionEditOwner_Insurance(
        //보험 사진
        @Part insurance: MultipartBody.Part,
        @Part("u_seq") u_seq: RequestBody,
        @Part("userObj") userObj: JsonObject,
        ): Call<ResultData>





    @Multipart
    @POST("/front/user/v2/userReturn")
    fun userCompanionEditPassenger(
        //소득증명 사진
        @Part income:  MultipartBody.Part,
        @Part("u_seq") u_seq: RequestBody,
        @Part("userObj") userObj: JsonObject,
        ): Call<ResultData>




    @Multipart
    @POST("/front/user/userCompanionEditInfo")
    fun userCompanionEditPassengerManAll(

        @Part income: MultipartBody.Part,
        @Part mainProfile:  MultipartBody.Part?,
        @Part subProfile: ArrayList<MultipartBody.Part>,
        @Part ("deleteSubProfile")deleteSubProfile : ArrayList<SubImageDeepData>,
        @Part("userObj") userObj: JsonObject,

    ): Call<ResultData>

    @Multipart
    @POST("/front/user/userCompanionEditInfo")
    fun userCompanionEditPassengerWomanAll(

        @Part mainProfile:  MultipartBody.Part?,
        @Part subProfile: ArrayList<MultipartBody.Part>,
        @Part ("deleteSubProfile")deleteSubProfile : ArrayList<SubImageDeepData>,
        @Part("userObj") userObj: JsonObject,

        ): Call<ResultData>





    @Multipart
    @POST("/front/user/userCompanionEditInfo")
    fun userCompanionEditOwnerAll_Man_Car(
        @Part mainCar:  MultipartBody.Part,
        @Part mainProfile:  MultipartBody.Part?,
        @Part subProfile: ArrayList<MultipartBody.Part>,
        @Part ("deleteSubProfile")deleteSubProfile : ArrayList<String>,
        @Part("userObj") userObj: JsonObject,
        ): Call<ResultData>

    @Multipart
    @POST("/front/user/userCompanionEditInfo")
    fun userCompanionEditWoman(
        @Part mainCar:  MultipartBody.Part,
        @Part insurance: MultipartBody.Part,
        @Part income: MultipartBody.Part,
        @Part mainProfile:  MultipartBody.Part,
        @Part subProfile: ArrayList<MultipartBody.Part>,
        @Part ("deleteSubProfile")deleteSubProfile : ArrayList<SubImage>,
        @Part("userObj") userObj: JsonObject,


        ): Call<ResultData>

    @Multipart
    @POST("/front/user/v2/userReturn")
    fun userCompanionEditWoMan(
        @Part mainProfile:  MultipartBody.Part,
        @Part subProfile: ArrayList<MultipartBody.Part>,
        @Part ("deleteSubProfile")deleteSubProfile : ArrayList<SubImage>,
        @Part("userObj") userObj: JsonObject,
        @Part("u_seq") u_seq: RequestBody,
        ): Call<ResultData>

    @Multipart
    @POST("/front/user/v2/userReturn")
    fun userCompanionEditManPassenger(
        @Part income: MultipartBody.Part,
        @Part mainProfile:  MultipartBody.Part,
        @Part subProfile: ArrayList<MultipartBody.Part>,
        @Part ("deleteSubProfile")deleteSubProfile : ArrayList<SubImage>,
        @Part("userObj") userObj: JsonObject,
        @Part("u_seq") u_seq: RequestBody,
        ): Call<ResultData>
    @Multipart
    @POST("/front/user/v2/userReturn")
    fun userCompanionEditManOwner(
        @Part mainCar:  MultipartBody.Part,
        @Part insurance: MultipartBody.Part,
        @Part mainProfile:  MultipartBody.Part,
        @Part subProfile: ArrayList<MultipartBody.Part>,
        @Part ("deleteSubProfile")deleteSubProfile : ArrayList<SubImage>,
        @Part("userObj") userObj: JsonObject,
        @Part("u_seq") u_seq: RequestBody,
        ): Call<ResultData>
}