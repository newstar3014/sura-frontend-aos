package com.supercarlounge.supercar.viewmodel

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.Network.UserEditInfoService
import com.supercarlounge.supercar.Network.UserService
import com.supercarlounge.supercar.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONException
import retrofit2.Call
import retrofit2.Response

class ProfileCorrectionModel : ViewModel() {

    var titleText = MutableLiveData<String>("?")
    var mainText = MutableLiveData<String>("")
    var subText =
        MutableLiveData<String>("초대 코드를 입력해 주세요. \n" + "초대 코드가 입력된 가입신청은 \n" + "우선적으로 가입심사를 해 드립니다.")
    var informationText = MutableLiveData<String>("초대 코드가 없으시면 다음 버튼을 눌러주세요.")
    var bottomText = MutableLiveData<String>("다음")
    var job = MutableLiveData<String?>("")
    var tv_job = MutableLiveData<String?>("")
    var height = MutableLiveData<String?>("")
    var tv_height = MutableLiveData<String?>("")
    var region = MutableLiveData<String?>("")
    var tv_region = MutableLiveData<String?>("")
    var checked1 = MutableLiveData<Boolean>(false)
    var checked2 = MutableLiveData<Boolean>(false)
    var checked3 = MutableLiveData<Boolean>(false)
    var carVisi = MutableLiveData<Boolean>(false)
    var u_drvie = MutableLiveData<String?>("")
    var tv_u_drvie = MutableLiveData<String?>("")
    var u_smoking = MutableLiveData<String?>("N")
    var tv_u_smoking = MutableLiveData<String?>("")
    var u_abutme = MutableLiveData<String?>("")
    var toast = MutableLiveData<String?>("")
    var tv_u_abutme = MutableLiveData<String?>("")
    var u_music = MutableLiveData<String?>("")

    var u_drive_w = MutableLiveData<String?>("")
    var tv_u_drive_w = MutableLiveData<String?>("")
    var u_drive_c = MutableLiveData<String?>("")
    var tv_u_drive_c = MutableLiveData<String?>("")
    var u_charm = MutableLiveData<String?>("")
    var isloading =  MutableLiveData<Boolean>(false)
    var list = ArrayList<SubImage>()
    val list2 = ArrayList<String>()


    var u_drive_c_yn = MutableLiveData<String?>("")
    var u_drive_w_yn = MutableLiveData<String?>("")
    var u_abutme_yn = MutableLiveData<String?>("")

    var carImage=  MutableLiveData<String?>("")
    var insuranceImage=  MutableLiveData<String?>("")
    var CarBrand_seq =  MutableLiveData<String?>("")
    var CarModel_seq =  MutableLiveData<String?>("")
    var CarSeries_seq = MutableLiveData<String?>("")
    var CarBrand_name =  MutableLiveData<String?>("")
    var CarBrand_Image =  MutableLiveData<String?>("")
    var CarModel_name = MutableLiveData<String?>("")
    var CarSeries_name =  MutableLiveData<String?>("")


    var email = MutableLiveData<String?>("")
    var subImage_really: MutableLiveData<ArrayList<SubImage>> = MutableLiveData(arrayListOf())
    var mainCheckReturn = MutableLiveData<String>("")
    var sub1CheckReturn = MutableLiveData<String>("")
    var sub2CheckReturn = MutableLiveData<String>("")
    var sub3CheckReturn = MutableLiveData<String>("")

    var u_tempReturn = MutableLiveData<String>("")
    var cartext = MutableLiveData<String>("차량 정보 수정하기")
    var view = MutableLiveData<String>("")
    var update = MutableLiveData<String>("N")
    var checkRegistration= MutableLiveData<Boolean>(false)
    var my_seq = MutableLiveData<String>("")
    var my_user_data = MutableLiveData<UserInformationData>(null)
    var my_car_data_list: MutableLiveData<ArrayList<ArrayList<UserInformationCarData>>> =
        MutableLiveData(arrayListOf())
    var apicall = MutableLiveData<Boolean>(false)
    var apiCallOverlapCheck = MutableLiveData<Boolean>(false)
    var before_my_car_data_list: MutableLiveData<ArrayList<ArrayList<UserInformationCarData>>> =
        MutableLiveData(arrayListOf())

    var delete_my_car_data_list: MutableLiveData<ArrayList<UserInformationCarData>> =
        MutableLiveData(arrayListOf())


    var delete_sub_image_list: MutableLiveData<ArrayList<SubImage>> =
        MutableLiveData(arrayListOf())
    var delete_mainImageUrl = MutableLiveData<String>("")


    var carBeforelist: MutableLiveData<ArrayList<String>> = MutableLiveData(arrayListOf())
    var subImageDatalist: MutableLiveData<ArrayList<SubImage>> = MutableLiveData(arrayListOf())


    var subImageBeforelist: MutableLiveData<ArrayList<SubImage>> = MutableLiveData(arrayListOf())

    var subImageBeforelistSize = MutableLiveData<Int>(0)
    var insurBeforelist: MutableLiveData<ArrayList<String>> = MutableLiveData(arrayListOf())
    val _itemList = MutableLiveData<ArrayList<SubImage>>()
    var SubImageUrlList: MutableLiveData<ArrayList<SubImage>> = MutableLiveData(arrayListOf())
    var SubImageUrlListSize = MutableLiveData<Int>(0)
    var mainImageUrl = MutableLiveData<String>(null)
    var original_sub_image_list: MutableLiveData<ArrayList<SubImage>> =
        MutableLiveData(arrayListOf())
    var original_car_image_list: MutableLiveData<ArrayList<UserInformationCarData>> =
        MutableLiveData(arrayListOf())

    init {
        _itemList.value = list
        var subImageData: SubImage? = SubImage("0", "", "", 0, "", "", 0, "", "")

        delete_sub_image_list.value?.add(subImageData!!)
        delete_sub_image_list.value?.add(subImageData!!)
        delete_sub_image_list.value?.add(subImageData!!)
        var d = UserInformationCarData(
            "", "", "", "", "0", 0,
            "", "", "", "", "", "", "", "", "", 0, "", "", "", "", "", "", "",""
        )
        delete_my_car_data_list.value?.add(d)
        delete_my_car_data_list.value?.add(d)
        delete_my_car_data_list.value?.add(d)
    }

    var charmArray = arrayOf(
        "몸매가 좋은", "웃을 때 예쁜",
    )
    var characterArray = arrayOf(
        "착한", "귀염",
    )
    var state = MutableLiveData<Int>(0)
    var endstate = MutableLiveData<Int>(0)
    var nameTotalCount = MutableLiveData<String>("8")
    var nameNowCount = MutableLiveData<String>("0")
    var inside = MutableLiveData<Boolean>(false)
    var outside = MutableLiveData<Boolean>(false)

    var charmlist = MutableLiveData<ArrayList<String>>()
    var tv_charmlist= MutableLiveData<ArrayList<String>>()
    var musictypelist = MutableLiveData<ArrayList<String>>()
    var tv_musictypelist = MutableLiveData<ArrayList<String>>()
    //state 2
    var outsideCheckedArray = ObservableArrayList<Boolean>()
    var insideCheckedArray = ObservableArrayList<Boolean>()
    var totalArray = ObservableArrayList<Boolean>()
    var resultState2Array = ObservableArrayList<String>()

    var outSideArray = arrayOf(
        "몸매가 좋은", "웃을 때 예쁜", "깔끔한",
    )

    var inSideArray = arrayOf(
        "노래 선곡 센스", "베스트 드라이버",
        "자수성가",
    )
    var musicTasteArray = arrayOf(
        "신나는", "분위기",

        )

    //state 3
    var musicTasteCheckedArray = ObservableArrayList<Boolean>()
    var resultState3Array = ObservableArrayList<String>()


    //state 4
    var heightCheckedArray = ObservableArrayList<Boolean>()
    var resultState4Array = ObservableArrayList<String>()
    var heightArray = arrayOf(
        "186 이상"
    )

    //state 5
    var jobCheckedArray = ObservableArrayList<Boolean>()
    var resultState5Array = ObservableArrayList<String>()
    var jobArray = arrayOf(
        "경영·사무"
    )

    //state 6
    var regionCheckedArray = ObservableArrayList<Boolean>()
    var resultState6Array = ObservableArrayList<String>()
    var regionArray = arrayOf(
        "서울 동부",
    )

    //state 7
    var driveStyleCheckedArray = ObservableArrayList<Boolean>()
    var resultState7Array = ObservableArrayList<String>()
    var driveStyleArray = arrayOf(
        "속도를 즐겨요", "느리게",
    )


    fun checkedSmoking1(boolean: Boolean) {
        if (boolean) {
            checked1.value = boolean
            checked2.value = !boolean
            checked3.value = boolean
        } else {
//            checked1.value = boolean
        }
    }

    fun onclickSmoking1() {
        if (checked1.value == false) {
            checked1.value = true
            checked2.value = false
            checked3.value = true
        }

    }

    fun checkedSmoking2(boolean: Boolean) {
        if (boolean) {
            checked2.value = boolean
            checked1.value = !boolean
            checked3.value = false
        }
    }

    fun onclickSmoking2() {
        if (checked2.value == false) {
            checked2.value = true
            checked1.value = false
            checked3.value = false
        }
    }

    fun setText() {

    }


    fun getUserInformation(u_seq: String, application: MainApplication) {

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(UserService::class.java).getUserInformation(
                u_seq,
            )
            d!!.enqueue(object : retrofit2.Callback<ResultUserInformationData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultUserInformationData>,
                    response: Response<ResultUserInformationData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        application.userData = data.rows[0]
                        application.userCarData = data.car
                        var userdata = data.rows[0]

                        my_user_data.value = userdata
                        my_car_data_list.value = data.car
                        for (i in my_car_data_list.value!![0]) {
                            i.new_url = i.uc_car_main
                        }






                        before_my_car_data_list.value = data.car
                        var tempData = my_user_data.value?.u_tempData
//                        var u_area = my_user_data.value?.u_area

//                        if (tempData?.contains("u_area") == true) {
//                                val obj: JsonObject = JsonParser.parseString(tempData).getAsJsonObject()
//                                var temp_u_area = obj.get("u_area").toString().replace("\"", "")
//                                region.value = temp_u_area
//                        }else{
//                            region.value = u_area
//                        }
                        region.value = my_user_data.value?.u_area
                        tv_region.value =  my_user_data.value?.u_area


//                        var u_cm = my_user_data.value?.u_cm
//                        if (tempData?.contains("u_cm") == true) {
//                            val obj: JsonObject = JsonParser.parseString(tempData).getAsJsonObject()
//                            var temp_u_cm = obj.get("u_cm").toString().replace("\"", "")
//                            height.value = temp_u_cm
//                        }else{
//                            height.value = u_cm
//                        }
                        height.value = my_user_data.value!!.u_cm
                        tv_height.value = my_user_data.value!!.u_cm

//                        var u_job = my_user_data.value?.u_job
//                        if (tempData?.contains("u_job") == true) {
//                            val obj: JsonObject = JsonParser.parseString(tempData).getAsJsonObject()
//                            var temp_u_job = obj.get("u_job").toString().replace("\"", "")
//                            job.value = temp_u_job
//                        }else{
//                            job.value = u_job
//                        }
                        job.value =  my_user_data.value!!.u_job
                        tv_job.value = my_user_data.value!!.u_job



                        var u_drive = my_user_data.value?.u_drive
//                        if (tempData?.contains("u_drive") == true) {
//                            val obj: JsonObject = JsonParser.parseString(tempData).getAsJsonObject()
//                            var temp_u_drive = obj.get("u_drive").toString().replace("\"", "")
//                            u_drvie.value = temp_u_drive
//                        }else{
//                            u_drvie.value = u_drive
//                        }
                        u_drvie.value = u_drive
                        tv_u_drvie.value = my_user_data.value!!.u_drive

//                        if (tempData?.contains("u_charm") == true) {
//                            val obj: JsonObject = JsonParser.parseString(tempData).getAsJsonObject()
//                            var temp_charmlist = obj.get("u_charm").toString().replace("\"", "").replace("\\", "").replace("[", "").replace("]", "")
//                            charmlist.value = Getlist(temp_charmlist)
//                        }else{
//                            charmlist.value = my_user_data.value!!.GetCharmlist()
//                        }
                        charmlist.value = my_user_data.value!!.GetCharmlist()
                        tv_charmlist.value = my_user_data.value!!.GetCharmlist()

//                        if (tempData?.contains("u_music") == true) {
//                            val obj: JsonObject = JsonParser.parseString(tempData).getAsJsonObject()
//                            var temp_u_music = obj.get("u_music").toString().replace("\"", "").replace("\\", "").replace("[", "").replace("]", "")
//                            musictypelist.value = Getlist(temp_u_music)
//                            Log.d("데이터타입 true", "onResponse: ")
//
//                        }else{
//                            Log.d("데이터타입 false", "onResponse: ")
//                            musictypelist.value = my_user_data.value!!.GetMusiclist()
//                        }
                        musictypelist.value = my_user_data.value!!.GetMusiclist()
                        tv_musictypelist.value = my_user_data.value!!.GetMusiclist()
//                        var u_smoking = my_user_data.value?.u_smoking
//                        if (tempData?.contains("u_smoking") == true) {
//                            val obj: JsonObject = JsonParser.parseString(tempData).getAsJsonObject()
//                            var temp_u_smoking = obj.get("u_smoking").toString().replace("\"", "")
//                            if (temp_u_smoking.contains("Y")){
//                                checked3.value =true
//                            }else{
//                                checked3.value =false
//                            }
//                        }else{
//                            if (u_smoking?.contains("Y") == true){
//                                checked1.value =  true
//                                checked2.value =  false
//                                checked3.value =true
//                            }else{
//                                checked1.value =  false
//                                checked2.value =  true
//                                checked3.value =false
//                            }
//
//                        }
                           if (my_user_data.value!!.u_smoking?.contains("Y") == true){
                                checked1.value =  true
                                checked2.value =  false
                                checked3.value =true
                            }else{
                                checked1.value =  false
                                checked2.value =  true
                                checked3.value =false
                            }
                        tv_u_smoking.value = my_user_data.value!!.u_smoking

                        subImageDatalist.value?.addAll(my_user_data.value!!.subImage)
                        subImageBeforelistSize.value = my_user_data.value!!.subImage.size
                        original_car_image_list?.value?.addAll(data.car.get(0))
                        var list = my_user_data.value!!.subImage



                        SubImageUrlList.value?.addAll(list)
                        original_sub_image_list.value?.addAll(list)

                        subImageBeforelist.value = list!!
                        subImage_really.value = my_user_data.value!!.subImage


                    }

                }

                override fun onFailure(
                    call: retrofit2.Call<ResultUserInformationData>,
                    t: Throwable
                ) {
                    Log.d("check", t.toString())
                }

            })

        }

    }
    fun Getlist(u_charm:String):ArrayList<String>{
        var list = arrayListOf<String>()
        if(!u_charm.isNullOrEmpty()) {

            var datalist = u_charm.split(",")

            list.addAll(datalist)

        }
        return list
    }

    fun userEditInfo(
        mainImage: MultipartBody.Part?,
        subImage: ArrayList<MultipartBody.Part>,
        carImage: ArrayList<MultipartBody.Part>,
        insurImage: ArrayList<MultipartBody.Part>
    ) {
        if (apiCallOverlapCheck.value == false) {
            apiCallOverlapCheck.value = true
            isloading.value = true

            if (checked3.value!!) {
                u_smoking.value = "Y"
            } else {
                u_smoking.value = "N"
            }
            val tempReturnObj = JsonObject()
            val userObj = JsonObject()
            val inputObj = JsonObject()
            var  m_u_drive_c =my_user_data.value?.u_drive_c
            var  m_u_drive_w =my_user_data.value?.u_drive_w
            var u_tempReturn = my_user_data.value?.u_tempReturn
            var u_tempData = my_user_data.value?.u_tempData
            var u_tempAbout=my_user_data.value?.u_tempAbout
            var u_aboutme_Chek = ""
            if (!u_tempReturn.isNullOrEmpty()&&!u_tempData.isNullOrEmpty()) {
                val obj: JsonObject = JsonParser.parseString(u_tempReturn).asJsonObject
                val obj_u_tempData: JsonObject = JsonParser.parseString(u_tempData).asJsonObject
                if (obj != null) {
//                    if (u_tempReturn?.contains("u_cm") && height.value.equals(
//                            obj_u_tempData.get(
//                                "u_cm"
//                            ).toString().replace("\"", "")
//                        )
//                    ) {
//                        tempReturnObj.addProperty(
//                            "u_cm",
//                            obj.get("u_cm").toString().replace("\"", "")
//                        )
//                    }
//                    if (u_tempReturn?.contains("u_smoking") == true && u_smoking.value == obj_u_tempData.get(
//                            "u_smoking"
//                        ).toString().replace("\"", "")
//                    ) {
//                        tempReturnObj.addProperty(
//                            "u_smoking",
//                            obj.get("u_smoking").toString().replace("\"", "")
//                        )
//                    }
//
//                    if (u_tempReturn?.contains("u_area") == true && region.value == obj_u_tempData.get(
//                            "u_area"
//                        ).toString().replace("\"", "")
//                    ) {
//                        tempReturnObj.addProperty(
//                            "u_area",
//                            obj.get("u_area").toString().replace("\"", "")
//                        )
//                    }
//
//                    if (u_tempReturn?.contains("u_job") == true && job.value == obj_u_tempData.get("u_job")
//                            .toString().replace("\"", "")
//                    ) {
//                        tempReturnObj.addProperty(
//                            "u_job",
//                            obj.get("u_job").toString().replace("\"", "")
//                        )
//                    }
//
//                    if (u_tempReturn?.contains("u_charm") == true && charmlist.value?.toString() == Getlist(obj_u_tempData.get("u_charm").toString().replace("\"", "").replace("\\", "").replace("[", "").replace("]", "")).toString()
//                    ) {
//                        tempReturnObj.addProperty("u_charm", obj.get("u_charm").toString().replace("\"", ""))
//                    }
//
//
//                    if (u_tempReturn?.contains("u_music") == true && musictypelist.value.toString() == Getlist(obj_u_tempData.get("u_music").toString().replace("\"", "").replace("\\", "").replace("[", "").replace("]", "")).toString()
//                    ) {
//                        tempReturnObj.addProperty(
//                            "u_music",
//                            obj.get("u_music").toString().replace("\"", "")
//                        )
//                    }
//
//                    if (u_tempReturn?.contains("u_drive") == true && u_drvie.value == obj_u_tempData.get(
//                            "u_drvie"
//                        ).toString().replace("\"", "")
//                    ) {
//                        tempReturnObj.addProperty(
//                            "u_drive",
//                            obj.get("u_drive").toString().replace("\"", "")
//                        )
//                    }

                    if (u_tempReturn?.contains("u_drive_c") == true && (u_drive_c.value == my_user_data.value?.u_drive_c)) {
                        tempReturnObj.addProperty("u_drive_c", obj.get("u_drive_c").toString().replace("\"", ""))
                    }
                    if (u_tempReturn?.contains("u_drive_w") == true && (u_drive_w.value == my_user_data.value?.u_drive_w)) {
                        tempReturnObj.addProperty("u_drive_w", obj.get("u_drive_w").toString().replace("\"", ""))
                    }
                    if (u_tempReturn?.contains("u_about_me") == true && (u_abutme.value == my_user_data.value?.u_about_me)) {
                        tempReturnObj.addProperty("u_about_me", obj.get("u_about_me").toString().replace("\"", ""))
                    }



                }
            }
//            if (!u_tempAbout.isNullOrEmpty()){
//                if (!u_tempAbout?.equals(u_abutme.value)){
//                    u_aboutme_Chek = ""
//                }else{
//                    u_aboutme_Chek = u_abutme.value.toString()
//                }
//            }


//            if (!my_user_data.value?.u_smoking.equals(u_smoking.value)) {
            inputObj.addProperty("u_smoking", u_smoking.value)
//            }
//            if (!my_user_data.value?.u_cm.equals(height.value)) {
            inputObj.addProperty("u_cm", height.value)
//            }
//            if (!my_user_data.value?.u_area.equals(region.value)) {
            inputObj.addProperty("u_area", region.value)

//            }
//            if (!my_user_data.value?.u_job.equals(job.value)) {
            inputObj.addProperty("u_job", job.value)
//            }
//            if (!my_user_data.value?.u_drive.equals(u_drvie.value)) {
            inputObj.addProperty("u_drive", u_drvie.value)
//            }
//            if (my_user_data.value?.u_music?.contains(musictypelist.value!![0]) ==false ||
//                my_user_data.value?.u_music?.contains(musictypelist.value!![1]) ==false
//                    ) {
            inputObj.addProperty(
                    "u_music",
                    "[\\\"${musictypelist.value!![0]}\\\",\\\"${musictypelist.value!![1]}\\\"]"
                )
//            if (my_user_data.value?.u_charm?.contains(charmlist.value!![0]) ==false ||
//                my_user_data.value?.u_charm?.contains(charmlist.value!![1]) ==false
//                    ) {
            inputObj.addProperty(
                "u_charm",
                "[\\\"${charmlist.value!![0]}\\\",\\\"${charmlist.value!![1]}\\\"]"
            )
            if (!my_user_data.value?.u_about_me.equals(u_abutme.value)){

                u_aboutme_Chek = u_abutme.value.toString()
//                inputObj.addProperty(
//                    "u_tempAbout",
//                    u_abutme.value
//                )
//                userObj.addProperty(
//                    "u_about_me",
//                    u_abutme.value
//                )
            }


                if (email.value?.equals(my_user_data.value?.u_email) == false){
                    userObj.addProperty("u_email", email.value)
                }
            if (u_drive_w.value?.equals(my_user_data.value?.u_drive_w) == false) {
                userObj.addProperty("u_drive_w", u_drive_w.value)
            }

           if (u_drive_c.value?.equals(my_user_data.value?.u_drive_c) == false) {
                userObj.addProperty("u_drive_c", u_drive_c.value)
            }
            Log.d("userObj", userObj.toString())
            var carListOb = JSONArray()

            try {

                if (my_car_data_list.value?.size != 0) {
                    for ((count, i) in my_car_data_list.value?.get(0)!!.withIndex()) {


                        if (my_car_data_list.value!![0][count].uc_seq == 0) {
                            val carObj3 = JsonObject()
                            carObj3.addProperty("uc_brand", i.uc_brand)
                            carObj3.addProperty("uc_model", i.uc_model)
                            carObj3.addProperty("csm_seq", i.csm_seq)
                            carObj3.addProperty("uc_check", i.uc_check)
                            carObj3.addProperty("uc_seq", i.uc_seq)

                            if (i.uc_return!!.isEmpty()) {
                                i.uc_return = "M"
                            }
                            if (checkRegistration.value== true){
                                carObj3.addProperty("uc_check", "Y")
                            }

                            carObj3.addProperty("index", count)
                            carObj3.addProperty("uc_return", i.uc_return)
                            carListOb.put(carObj3)

                        } else {
                            var carData = my_car_data_list.value?.get(0)?.get(count)
                            for ((count2, i) in original_car_image_list.value!!.withIndex())
                                if (i.uc_seq == carData?.uc_seq) {
                                    val carObj3 = JsonObject()
                                    var check = false
                                    if (carData.uc_return!!.isEmpty()) {
                                        carData.uc_return = "M"
                                    }
                                    if (!carData.uc_brand.equals(i.uc_brand)) {
                                        carObj3.addProperty(
                                            "uc_brand", carData.uc_brand
                                        )
                                        check = true
                                    }
                                    if (!carData.uc_model.equals(i.uc_model)
                                    ) {
                                        carObj3.addProperty("uc_model", carData.uc_model)
                                        check = true
                                    }

                                    if (!carData.csm_seq?.equals(i.csm_seq)!!
                                    ) {
                                        carObj3.addProperty("csm_seq", carData.csm_seq)
                                        check = true
                                    }


                                        if (!carData.uc_check.equals(i.uc_check)) {
                                            carObj3.addProperty("uc_check", carData.uc_check)
                                            check = true
                                        }



                                    if (!carData.uc_return.equals(i.uc_return)) {
                                        carObj3.addProperty(
                                            "uc_return",
                                            carData.uc_return
                                        )
                                        check = true
                                    }
                                    if (!carData.new_url.equals(i.uc_car_main)) {
                                        carObj3.addProperty("uc_before_url", i.uc_car_main)

                                        check = true
                                    }




                                    if (check) {

                                        carObj3.addProperty("index", count)
                                        carObj3.addProperty("uc_seq", i.uc_seq)
                                        carListOb.put(carObj3)

                                    }


                                }


                        }


                    }

                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }



            var slist: ArrayList<SubImage> = arrayListOf()
            slist.addAll(subImageDatalist.value!!)
            var list: ArrayList<SubImage> = arrayListOf()

            for (i in slist!!) {
                if (i.im_url!!.startsWith("file") && i.im_seq != 0) {
                    list.add(i)
                }

            }

            //userObj.addProperty("u_about_me", u_abutme.value)
            if (mainImage != null && subImage.size != 0) {

                var call1: Call<ResultData>? =
                    NetworkManager.serveradapter!!.create(UserEditInfoService::class.java)
                        .userProfileEditInfo(
                            requestbodyString(my_user_data.value?.u_seq.toString()),
                            requestbodyString( u_aboutme_Chek),
                            mainImage,
                            requestbodyString(delete_mainImageUrl.value!!),
                            subImage,
                            slist,
                            delete_sub_image_list.value!!,
                            carImage,
                            carBeforelist.value!!,
                            delete_my_car_data_list.value!!,
                            insurImage,
                            insurBeforelist.value!!,
                            userObj,
                            tempReturnObj,
                            carListOb,
                            inputObj
                        )

                call1!!.enqueue(
                    object : retrofit2.Callback<ResultData> {
                        override fun onResponse(
                            call: Call<ResultData>,
                            response: retrofit2.Response<ResultData>
                        ) {
                            if (response.isSuccessful) {

                                var result = response.body()

                                var message = result!!.message
                                var type = result!!.type

                                if (message.equals("정상적으로 수정되었습니다.")) {
                                    apicall.value = true

                                }

                            }
                            apiCallOverlapCheck.value = false
                        }

                        override fun onFailure(call: Call<ResultData>, t: Throwable) {
                            Log.d("setJoin", t.toString())
                            apiCallOverlapCheck.value = false
                        }
                    })


            } else if (mainImage != null || subImage.size != 0) {
                if (mainImage != null) {
                    var call2: Call<ResultData>? =
                        NetworkManager.serveradapter!!.create(UserEditInfoService::class.java)
                            .userNoSubProfileEditInfo(
                                requestbodyString(my_user_data.value?.u_seq.toString()),
                                requestbodyString( u_aboutme_Chek),
                                mainImage,
                                requestbodyString(delete_mainImageUrl.value!!),
                                carImage,
                                carBeforelist.value!!,
                                delete_my_car_data_list.value!!,
                                insurImage,
                                insurBeforelist.value!!,
                                userObj,
                                tempReturnObj,
                                carListOb,
                                inputObj
                            )

                    call2!!.enqueue(
                        object : retrofit2.Callback<ResultData> {
                            override fun onResponse(
                                call: Call<ResultData>,
                                response: retrofit2.Response<ResultData>
                            ) {
                                if (response.isSuccessful) {

                                    var result = response.body()
                                    var message = result!!.message
                                    var type = result!!.type

                                    if (message.equals("정상적으로 수정되었습니다.")) {
                                        apicall.value = true
                                    }


                                }
                                apiCallOverlapCheck.value = false
                            }

                            override fun onFailure(call: Call<ResultData>, t: Throwable) {
                                Log.d("setJoin", t.toString())
                                apiCallOverlapCheck.value = false
                            }
                        })
                } else {

                    var call2: Call<ResultData>? =
                        NetworkManager.serveradapter!!.create(UserEditInfoService::class.java)
                            .userNoMainProfileEditInfo(
                                requestbodyString(my_user_data.value?.u_seq.toString()),
                                requestbodyString( u_aboutme_Chek),
                                subImage,
                                slist,
                                delete_sub_image_list.value!!,
                                carImage,
                                carBeforelist.value!!,
                                delete_my_car_data_list.value!!,
                                insurImage,
                                insurBeforelist.value!!,
                                userObj,
                                tempReturnObj,
                                carListOb,
                                inputObj
                            )

                    call2!!.enqueue(
                        object : retrofit2.Callback<ResultData> {
                            override fun onResponse(
                                call: Call<ResultData>,
                                response: retrofit2.Response<ResultData>
                            ) {
                                if (response.isSuccessful) {

                                    var result = response.body()
                                    var message = result!!.message
                                    var type = result!!.type

                                    if (message.equals("정상적으로 수정되었습니다.")) {
                                        apicall.value = true
                                    }



                                }
                                apiCallOverlapCheck.value = false
                            }

                            override fun onFailure(call: Call<ResultData>, t: Throwable) {
                                Log.d("setJoin", t.toString())
                                apiCallOverlapCheck.value = false
                            }
                        })
                }


            } else {

                if (subImageDatalist.value?.size != original_sub_image_list.value?.size) {
                    var call2: Call<ResultData>? =
                        NetworkManager.serveradapter!!.create(UserEditInfoService::class.java)
                            .userSubProfileDeleteInfo(
                                requestbodyString(my_user_data.value?.u_seq.toString()),
                                requestbodyString( u_aboutme_Chek),
                                slist,
                                delete_sub_image_list.value!!,
                                carImage,
                                carBeforelist.value!!,
                                delete_my_car_data_list.value!!,
                                insurImage,
                                insurBeforelist.value!!,
                                userObj,
                                tempReturnObj,
                                carListOb,
                                inputObj
                            )

                    call2!!.enqueue(
                        object : retrofit2.Callback<ResultData> {
                            override fun onResponse(
                                call: Call<ResultData>,
                                response: retrofit2.Response<ResultData>
                            ) {
                                if (response.isSuccessful) {

                                    var result = response.body()
                                    var message = result!!.message
                                    var type = result!!.type

                                    if (message.equals("정상적으로 수정되었습니다.")) {
                                        apicall.value = true
                                    }


                                }
                                apiCallOverlapCheck.value = false
                            }

                            override fun onFailure(call: Call<ResultData>, t: Throwable) {
                                Log.d("setJoin", t.toString())
                                apiCallOverlapCheck.value = false
                            }
                        })

                } else {

                    var call2: Call<ResultData>? =
                        NetworkManager.serveradapter!!.create(UserEditInfoService::class.java)
                            .userNoProfileEditInfo(
                                requestbodyString(my_user_data.value?.u_seq.toString()),
                                requestbodyString( u_aboutme_Chek),
                                carImage,
                                carBeforelist.value!!,
                                delete_my_car_data_list.value!!,
                                insurImage,
                                insurBeforelist.value!!,
                                userObj,
                                tempReturnObj,
                                carListOb,
                                inputObj
                            )

                    call2!!.enqueue(
                        object : retrofit2.Callback<ResultData> {
                            override fun onResponse(
                                call: Call<ResultData>,
                                response: retrofit2.Response<ResultData>
                            ) {
                                if (response.isSuccessful) {

                                    var result = response.body()
                                    var message = result!!.message
                                    var type = result!!.type

                                    if (message.equals("정상적으로 수정되었습니다.")) {
                                        apicall.value = true
                                    }


                                }
                                apiCallOverlapCheck.value = false
                            }

                            override fun onFailure(call: Call<ResultData>, t: Throwable) {
                                Log.d("setJoin", t.toString())
                                apiCallOverlapCheck.value = false
                            }
                        })


                }
            }
        }


    }

    fun requestbodyString(text: String): RequestBody {
        return text.toRequestBody("text/plain".toMediaType())
    }

    fun CheckUserData():Boolean{
        var mydata = my_user_data.value
        var beforeemail = mydata!!.u_email
        var beforearea = mydata!!.u_area
        var beforejob = mydata!!.u_job
        var beforeheight = mydata!!.u_cm
        var beforecharm = mydata.GetCharmlist()
        var beforemusic = mydata!!.GetMusiclist()
        var beforedrive = mydata!!.u_drive
        var beforesmoke = mydata!!.u_smoking
        var beforedrivehow = mydata!!.u_drive_w
        var beforedrivewhen = mydata!!.u_drive_c
        var beforeu_temp = mydata!!.u_about_me
        var beforemycar = before_my_car_data_list.value!!.get(0)
        var beforemainimg = mydata!!.main_url
        var beforesubimg = mydata!!.subImage
        var mycardata = my_car_data_list.value!!.get(0)
        if (beforemycar.count() == mycardata.count()) {
            beforemycar.forEachIndexed{ index,d ->
                var beforebrand = d.uc_brand
                var beforemodel = d.uc_model
                var beforesubmodel = d.csm_seq
                var setcar = mycardata[index]
                var brand = setcar.uc_brand
                var model = setcar.uc_model
                var submodel = setcar.csm_seq
                if((beforebrand!!.equals(brand) && beforemodel!!.equals(model)&& beforesubmodel.equals(submodel)) == false){
                 return false
                }
            }
        }else{
            return false
        }
        if (!beforeemail!!.equals(email.value!!)){
            return false
        }
        if (!beforearea!!.equals(tv_region.value!!)){
            return false
        }
        if (!beforejob.equals(tv_job.value!!)){
            return false
        }
        if(!beforeheight.equals(height.value!!)){
            return false
        }
        var inputcharmlist = tv_charmlist.value!!
        var inputmusiclist = tv_musictypelist.value!!
        if (!inputcharmlist.equals(beforecharm)){
            return false
        }
        if (!inputmusiclist.equals(beforemusic)){
            return false
        }
        if (!beforedrive!!.equals(tv_u_drvie.value)){
            return false
        }
        if(!beforedrivehow.equals(u_drive_w.value)){
            return false
        }
        if(!beforedrivewhen.equals(u_drive_c.value)){
            return false
        }
        if(!beforeu_temp.equals(u_abutme.value)){
            return false
        }
        if(!beforemainimg!!.equals(mainImageUrl.value)){
            return false
        }

        SubImageUrlList.value!!.forEachIndexed { index, d ->
                var seq = d.im_seq

                if (seq == 0) {
                    return false
                }

            }
            return true
    }
}


