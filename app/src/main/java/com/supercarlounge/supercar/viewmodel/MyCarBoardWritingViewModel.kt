package com.supercarlounge.supercar.viewmodel

import android.annotation.SuppressLint
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonArray
import com.supercarlounge.supercar.data.*
import com.google.gson.JsonObject
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.Network.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONException
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class MyCarBoardWritingViewModel : ViewModel() {

    var titleText = MutableLiveData<String>("드라이브 등")
    var time = MutableLiveData<String>("")
    var my_seq = MutableLiveData<String>("")
    var mainImageUrl = MutableLiveData<String>("")
    var ispost = MutableLiveData<Boolean>(false)
    var isstart = MutableLiveData<Boolean>(false)
    var istoast = MutableLiveData<String>("")
    var viptype = MutableLiveData<String>("")
    var position = MutableLiveData<Int>(0)
    var postSuccess = MutableLiveData<Boolean>(false)
    var apiCallOverlapCheck = MutableLiveData<Boolean>(false)
    var my_user_data: MutableLiveData<UserInformationData> = MutableLiveData()
    var my_car_data_list: MutableLiveData<ArrayList<UserInformationCarData>> =
        MutableLiveData(arrayListOf())
    var myCarData = MutableLiveData<UserInformationCarData>()
    var select_car_data_list: MutableLiveData<ArrayList<CarSelectData>> =
        MutableLiveData(arrayListOf())
    var select_car_data: MutableLiveData<CarSelectData> = MutableLiveData()
    var my_sub_info: MutableLiveData<ArrayList<MySubInfoData>> = MutableLiveData(arrayListOf())
    var apicall = MutableLiveData<Boolean>(false)
    var my_sub_info_delete: MutableLiveData<ArrayList<String>> = MutableLiveData(arrayListOf())

    var myCarBoardUpdateData = MutableLiveData<MyCarBoardData>(null)

    var mmi_seq = MutableLiveData<Int>(0)
    var isMainEdit = MutableLiveData<Boolean>(false)
    var isMmiFirstEdit = MutableLiveData<Boolean>(false)
    var isMmiSecondEdit = MutableLiveData<Boolean>(false)
    var isMmiThirdEdit = MutableLiveData<Boolean>(false)

    var textmainTitle = MutableLiveData<String>("")
    var textmainContext = MutableLiveData<String>("")
    var textIsMmiFirst = MutableLiveData<String>("")
    var textIsMmiSecond = MutableLiveData<String>("")
    var textIsMmithird = MutableLiveData<String>("")

    var mySubInfoListPosition = MutableLiveData<Int>(0)


    fun setMyCarCheck(position: Int) {
        if (select_car_data_list.value?.size != 0) {
            for ((count, i) in select_car_data_list.value!!.withIndex()) {
                var selectCarData =
                    CarSelectData("", "", "", "", "", "", "", select = false, isShowing = false)
                if (position == count) {
                    if (select_car_data_list.value?.get(position)?.select == true) {
                        select_car_data_list.value?.get(position)?.select = false
                        select_car_data.value = selectCarData
                    } else {
                        select_car_data_list.value?.get(position)?.select = count == position
                        select_car_data.value = select_car_data_list.value?.get(position)
                    }

                }else{
                    select_car_data_list.value?.get(count)?.select = false

                }

            }
            Log.d("셀렉트", select_car_data_list.value?.get(position)?.select.toString())
            select_car_data_list.value = select_car_data_list.value
        }

    }

    fun listAdd(msi_img: String) {
        if (my_sub_info.value?.size!! < 10) {
            var mySubInfoData = MySubInfoData(0, 0, 0, msi_img, "", "", false)
            my_sub_info.value?.add(mySubInfoData)
            my_sub_info.value = my_sub_info.value

        }
    }

    fun listSet(msi_img: String) {
        var postion = mySubInfoListPosition.value
        var msi_contents = my_sub_info.value?.get(postion!!)?.msi_contents
        var msi_seq = my_sub_info.value?.get(postion!!)?.msi_seq
        var mySubInfoData =
            MySubInfoData(msi_seq!!, 0, 0, msi_img, msi_contents.toString(), "", false)

        my_sub_info.value?.set(mySubInfoListPosition.value!!, mySubInfoData)
        my_sub_info.value = my_sub_info.value

    }


    fun insertMyCarBoard(
        mainProfile: MultipartBody.Part,
        subProfile: ArrayList<MultipartBody.Part>,
        uuid: String
    ) {
        if (apiCallOverlapCheck.value == false) {
            apiCallOverlapCheck.value = true

            var u_seq = my_user_data.value?.u_seq.toString()
            var mmi_title = textmainTitle.value.toString()
            var mmi_contents = textmainContext.value.toString()


            var carb_seq = select_car_data.value?.card_seq.toString()
            var carm_seq = select_car_data.value?.carm_seq.toString()
            var csm_seq = select_car_data.value?.csm_seq.toString()
            var mmi_first = textIsMmiFirst.value.toString()
            var mmi_second = textIsMmiSecond.value.toString()
            var mmi_third = textIsMmithird.value.toString()


            var sub_contnet_list = "["
            for ((count, i) in my_sub_info.value!!.withIndex()) {

                if (count != my_sub_info.value!!.size - 1) {
                    sub_contnet_list += "\"${i.msi_contents}\","
                } else {
                    sub_contnet_list += "\"${i.msi_contents}\"]"
                }

            }
            Log.d("리스트", sub_contnet_list)
            var dir = uuid
            var call: Call<ResultData>? = null

            if (subProfile.size != 0) {
                call = NetworkManager.serveradapter!!.create(MyCarBoardService::class.java)
                    .insertMyCarBoard(
                        requestbodyString(dir),
                        mainProfile,
                        subProfile,
                        requestbodyString(u_seq),
                        requestbodyString(mmi_title),
                        requestbodyString(mmi_contents),
                        requestbodyString(sub_contnet_list),
                        requestbodyString(carb_seq),
                        requestbodyString(carm_seq),
                        requestbodyString(csm_seq),
                        requestbodyString(mmi_first),
                        requestbodyString(mmi_second),
                        requestbodyString(mmi_third),
                    )
            } else {
                call = NetworkManager.serveradapter!!.create(MyCarBoardService::class.java)
                    .insertMyCarNoSubBoard(
                        requestbodyString(dir),
                        mainProfile,
                        requestbodyString(u_seq),
                        requestbodyString(mmi_title),
                        requestbodyString(mmi_contents),
                        requestbodyString(carb_seq),
                        requestbodyString(carm_seq),
                        requestbodyString(csm_seq),
                        requestbodyString(mmi_first),
                        requestbodyString(mmi_second),
                        requestbodyString(mmi_third),
                    )
            }


            call!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: Call<ResultData>,
                    response: retrofit2.Response<ResultData>
                ) {
                    Log.d("상태", "성공")
                    if (response.isSuccessful) {

                        var result = response.body()
                        var message = result!!.message
                        var type = result!!.type
                        Log.d("상태", "성공2")
                        postSuccess.value = true
                    }
                    apiCallOverlapCheck.value = false
                }

                override fun onFailure(call: Call<ResultData>, t: Throwable) {

                    Log.d("상태", "실패")
                    apiCallOverlapCheck.value = false
                }
            })
        }

    }


    fun updateMyCarBoard(
        mainProfile: MultipartBody.Part,
        subProfile: ArrayList<MultipartBody.Part>,
        myCarSubInsert: ArrayList<MultipartBody.Part>,
        uuid: String
    ) {
        if (apiCallOverlapCheck.value == false) {
            apiCallOverlapCheck.value = true


            var u_seq = my_user_data.value?.u_seq.toString()
            var mmi_title = textmainTitle.value.toString()
            var mmi_contents = textmainContext.value.toString()
            var carb_seq = select_car_data.value?.card_seq.toString()
            var carm_seq = select_car_data.value?.carm_seq.toString()
            var csm_seq = select_car_data.value?.csm_seq.toString()
            var mmi_first = textIsMmiFirst.value.toString()
            var mmi_second = textIsMmiSecond.value.toString()
            var mmi_third = textIsMmithird.value.toString()



            val  list = arrayListOf<MySubInfoData>()
            val  addlist = arrayListOf<MySubInfoData>()
            val  Sublist = arrayListOf<MySubInfoData>()

            var sub_seq_list = "["
            for ((count, i) in my_sub_info.value!!.withIndex()) {
                if (i.msi_img?.startsWith("http") == false && i.msi_seq != 0) {
                    list.add(i)
                }
                if (i.msi_img?.startsWith("http") == false &&i.msi_seq == 0) {
                    addlist.add(i)
                }
            }


            if (list?.size != 0) {
                for ((count, i) in list!!.withIndex()) {
                    if (count != list.size - 1) {
                        sub_seq_list += "{\"msi_seq\":\"${i.msi_seq}\"},"

                    } else {
                        sub_seq_list += "{\"msi_seq\":\"${i.msi_seq}\"}]"

                    }
                }
            }else{
                sub_seq_list += "]"
            }

            var sub_contnet_list_add = "["

            if (addlist?.size != 0) {
                for ((count, i) in addlist!!.withIndex()) {
                    if (count != addlist.size - 1) {
                        sub_contnet_list_add += "\"${i.msi_contents}\","
                    } else {
                        sub_contnet_list_add += "\"${i.msi_contents}\"]"
                    }
                }
            }else{
                sub_contnet_list_add += "]"
            }

            var sub_contnet_list = "["

            for ((count, i) in my_sub_info.value!!.withIndex()) {

                if (i.msi_seq != 0){
                    Sublist.add(i)
                }


            }
            if (Sublist.size!= 0){
                for ((count, i) in Sublist.withIndex()) {
                    if (count != Sublist.size - 1) {
                        sub_contnet_list += "{\"msi_seq\":\"${i.msi_seq}\",\"msi_contents\":\"${i.msi_contents}\"},"
                    } else {
                        sub_contnet_list += "{\"msi_seq\":\"${i.msi_seq}\",\"msi_contents\":\"${i.msi_contents}\"}]"
                    }
                }

            }else{
                sub_contnet_list = ""
            }

            Log.d("테스트리스트", my_sub_info.value.toString())

            var deleteSublist = "["

            if (my_sub_info_delete.value?.size != 0) {
                for ((count, i) in my_sub_info_delete.value!!.withIndex()) {

                    if (count != my_sub_info_delete.value!!.size - 1) {
                        deleteSublist += "\"${i}\","
                    } else {
                        deleteSublist += "\"${i}\"]"
                    }

                }
            } else {
                deleteSublist = ""
            }


            Log.d("리스트", sub_contnet_list)
            var dir = uuid
            var call: Call<ResultData>? = null
            if (mainImageUrl.value?.startsWith("http") == true && subProfile.size == 0 &&myCarSubInsert.size == 0) {
                call = NetworkManager.serveradapter!!.create(MyCarBoardService::class.java)
                    .updateAllNoMyCarBoard(
                        requestbodyString(dir),
                        requestbodyString(deleteSublist),
                        requestbodyString(mmi_seq.value.toString()),
                        requestbodyString(u_seq),
                        requestbodyString(mmi_title),
                        requestbodyString(mmi_contents),
                        requestbodyString(sub_contnet_list),
                        requestbodyString(carb_seq),
                        requestbodyString(carm_seq),
                        requestbodyString(csm_seq),
                        requestbodyString(mmi_first),
                        requestbodyString(mmi_second),
                        requestbodyString(mmi_third),
                    )
            } else if (subProfile.size != 0 &&myCarSubInsert.size != 0 &&  mainImageUrl.value?.startsWith("http") == false) {
                call = NetworkManager.serveradapter!!.create(MyCarBoardService::class.java)
                    .updateMyCarBoard(
                        requestbodyString(dir),
                        mainProfile,
                        subProfile,
                        myCarSubInsert,
                                requestbodyString(sub_contnet_list_add),
                        requestbodyString(sub_seq_list),
                        requestbodyString(deleteSublist),
                        requestbodyString(mmi_seq.value.toString()),
                        requestbodyString(u_seq),
                        requestbodyString(mmi_title),
                        requestbodyString(mmi_contents),
                        requestbodyString(sub_contnet_list),
                        requestbodyString(carb_seq),
                        requestbodyString(carm_seq),
                        requestbodyString(csm_seq),
                        requestbodyString(mmi_first),
                        requestbodyString(mmi_second),
                        requestbodyString(mmi_third),
                    )
            } else if (subProfile.size != 0 &&myCarSubInsert.size != 0){
                call = NetworkManager.serveradapter!!.create(MyCarBoardService::class.java).
                updateSubEditMyCarSubInsertMyCarBoard(
                    requestbodyString(dir),
                    subProfile,
                    myCarSubInsert,
                    requestbodyString(sub_contnet_list_add),
                    requestbodyString(sub_seq_list),
                    requestbodyString(deleteSublist),
                    requestbodyString(mmi_seq.value.toString()),
                    requestbodyString(u_seq),
                    requestbodyString(mmi_title),
                    requestbodyString(mmi_contents),
                    requestbodyString(sub_contnet_list),
                    requestbodyString(carb_seq),
                    requestbodyString(carm_seq),
                    requestbodyString(csm_seq),
                    requestbodyString(mmi_first),
                    requestbodyString(mmi_second),

                   requestbodyString(mmi_third),
                )

            }  else if (subProfile.size != 0 && mainImageUrl.value?.startsWith("http") == false){
                call = NetworkManager.serveradapter!!.create(MyCarBoardService::class.java).
                updateSubEditMyCarMainMyCarBoard(
                    requestbodyString(dir),
                    mainProfile,
                    myCarSubInsert,

                    requestbodyString(sub_seq_list),
                    requestbodyString(deleteSublist),
                    requestbodyString(mmi_seq.value.toString()),
                    requestbodyString(u_seq),
                    requestbodyString(mmi_title),
                    requestbodyString(mmi_contents),
                    requestbodyString(sub_contnet_list),
                    requestbodyString(carb_seq),
                    requestbodyString(carm_seq),
                    requestbodyString(csm_seq),
                    requestbodyString(mmi_first),
                    requestbodyString(mmi_second),
                    requestbodyString(mmi_third),
                )

            }  else if ( mainImageUrl.value?.startsWith("http") == false &&myCarSubInsert.size != 0){
                call = NetworkManager.serveradapter!!.create(MyCarBoardService::class.java).
                updateMainCarEditMyCarSubInsertMyCarBoard(
                    requestbodyString(dir),
                    mainProfile,
                    myCarSubInsert,
                    requestbodyString(sub_contnet_list_add),
                    requestbodyString(sub_seq_list),
                    requestbodyString(deleteSublist),
                    requestbodyString(mmi_seq.value.toString()),
                    requestbodyString(u_seq),
                    requestbodyString(mmi_title),
                    requestbodyString(mmi_contents),
                    requestbodyString(sub_contnet_list),
                    requestbodyString(carb_seq),
                    requestbodyString(carm_seq),
                    requestbodyString(csm_seq),
                    requestbodyString(mmi_first),
                    requestbodyString(mmi_second),
                    requestbodyString(mmi_third),
                )

            }else if (mainImageUrl.value?.startsWith("http") == false) {
                call = NetworkManager.serveradapter!!.create(MyCarBoardService::class.java)
                    .updateMyCarNoSubBoard(
                        requestbodyString(dir),
                        mainProfile,
                        requestbodyString(deleteSublist),
                        requestbodyString(mmi_seq.value.toString()),
                        requestbodyString(u_seq),
                        requestbodyString(mmi_title),
                                requestbodyString(mmi_contents),
                        requestbodyString(sub_contnet_list),
                        requestbodyString(carb_seq),
                        requestbodyString(carm_seq),
                        requestbodyString(csm_seq),
                        requestbodyString(mmi_first),
                        requestbodyString(mmi_second),
                        requestbodyString(mmi_third),
                    )
            } else if (subProfile.size != 0) {
                call = NetworkManager.serveradapter!!.create(MyCarBoardService::class.java)
                    .updateSubMyCarBoard(
                        requestbodyString(dir),
                        subProfile,
                        requestbodyString(sub_seq_list),
                        requestbodyString(deleteSublist),
                        requestbodyString(mmi_seq.value.toString()),
                        requestbodyString(u_seq),
                        requestbodyString(mmi_title),
                        requestbodyString(mmi_contents),
                        requestbodyString(sub_contnet_list),
                        requestbodyString(carb_seq),
                        requestbodyString(carm_seq),
                        requestbodyString(csm_seq),
                        requestbodyString(mmi_first),
                        requestbodyString(mmi_second),
                        requestbodyString(mmi_third)

                    )
            } else if (myCarSubInsert.size != 0) {
                call = NetworkManager.serveradapter!!.create(MyCarBoardService::class.java)
                    .updateOnlyMyCarSubInsertMyCarBoard(
                        requestbodyString(dir),
                        myCarSubInsert,
                        requestbodyString(sub_contnet_list_add),
                        requestbodyString(sub_seq_list),
                        requestbodyString(deleteSublist),
                        requestbodyString(mmi_seq.value.toString()),
                        requestbodyString(u_seq),
                        requestbodyString(mmi_title),
                        requestbodyString(mmi_contents),
                        requestbodyString(sub_contnet_list),
                        requestbodyString(carb_seq),
                        requestbodyString(carm_seq),
                        requestbodyString(csm_seq),
                        requestbodyString(mmi_first),
                        requestbodyString(mmi_second),
                        requestbodyString(mmi_third)

                    )
            }


            call!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: Call<ResultData>,
                    response: retrofit2.Response<ResultData>
                ) {
                    Log.d("상태", "성공")
                    if (response.isSuccessful) {

                        var result = response.body()
                        var message = result!!.message
                        var type = result!!.type
                        Log.d("상태", "성공2")
                        getMyCarBoardUpdateData()

                    }
                    apiCallOverlapCheck.value = false
                }

                override fun onFailure(call: Call<ResultData>, t: Throwable) {

                    Log.d("상태", "실패")
                    apiCallOverlapCheck.value = false
                }
            })
        }

    }

    fun getMyCarBoardUpdateData() {
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(MyCarBoardService::class.java).getMyCarBoardUpdateData(mmi_seq.value,my_user_data.value?.u_seq.toString())
            d!!.enqueue(object : retrofit2.Callback<ResultMyCaBoardData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultMyCaBoardData>,
                    response: Response<ResultMyCaBoardData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var rows = data.rows
                        if (rows != null) {
                            if (rows.size != 0){
                                myCarBoardUpdateData.value = rows[0]
                                postSuccess.value = true
                            }


                        }


                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<ResultMyCaBoardData>,
                    t: Throwable
                ) {
                    Log.d("check", "failer")
                }

            })

        }


    }
    fun encodeset(text: String): String {
        var result: String = ""
        var set = ""
        try {
            // var list =text.toCharArray()
            val results: MutableList<String> = ArrayList()
            val m: Matcher =
                Pattern.compile("\\P{M}\\p{M}*+").matcher(text)
            while (m.find()) {
                results.add(m.group())
            }
            for (i in results) {
                if (checkemoji(i.toCharArray()[0])) {

                    result += URLEncoder.encode(i, "UTF-8")
                    Log.d("EMOJISET1", i.toString())

                } else {
                    result += i
                    Log.d("EMOJISET3", i.toString())
                }
            }


        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return result
    }

    private fun checkemoji(text: Char): Boolean {
        val type: Int = Character.getType(text)
        return (type == Character.SURROGATE.toInt() || type == Character.OTHER_SYMBOL.toInt())
    }

    fun requestbodyString(text: String): RequestBody {
        return text.toRequestBody("text/plain".toMediaType())
    }
}