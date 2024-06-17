package com.supercarlounge.supercar.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.BannerService
import com.supercarlounge.supercar.Network.CategoryService
import com.supercarlounge.supercar.Network.DriveService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.Network.UserService
import com.supercarlounge.supercar.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class TestVipLoungeViewModel : ViewModel() {
    var my_seq = MutableLiveData<String>("")
    var text = MutableLiveData<String>("")
    var image = MutableLiveData<String>("")
    var name = MutableLiveData<String>("")
    var viewStack = MutableLiveData<Int>(1)
    var viplounge_gauge = MutableLiveData<Int>(0)
    var viplounge_Check = MutableLiveData<Int>(0)
    var ondial = MutableLiveData<Boolean>(false)
    var toast = MutableLiveData<String>("")

    var titleText = MutableLiveData<String>("LOUNGE")
    var size = MutableLiveData<String>("0")
    var bannerTopPosition= MutableLiveData<Int>(0)
    var bannerBottomPosition= MutableLiveData<Int>(0)
    var bannertop= MutableLiveData<ArrayList<BannerData>>(arrayListOf())
    var tablist= MutableLiveData<ArrayList<ConciergeCategoryData>>(arrayListOf())
    var select_bannernoti_data= MutableLiveData<NotiData>()
    var bannerbottom= MutableLiveData<ArrayList<BannerData>>(arrayListOf())
    var invite_code = MutableLiveData<String>("")
    var inviteset =MutableLiveData<Boolean>(false)
    fun setdial(){
        ondial.value =true
    }
    fun stack(){
        if(viplounge_Check.value!!>=100) {
            viewStack.value = 2
        }else{
            toast.value ="아직 VIP 회원이 아닙니다"
        }
    }

    fun getVipGauge(){
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(DriveService::class.java).GetVipGauge(
                my_seq.value!!

            )
            d!!.enqueue(object : retrofit2.Callback<ResultVipLoungeData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultVipLoungeData>,
                    response: Response<ResultVipLoungeData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type =data.type
                        if(type.equals("success")) {
                            var scoreset = data.score

                            if (scoreset != null) {

                                try {
                                    Log.d("브아이피게이지", scoreset.toInt().toString())
                                    viplounge_gauge.value = scoreset.toInt()
                                } catch (e: NumberFormatException) {
//                                    viplounge_gauge.value = 0
                                }
                            }

                        }

                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultVipLoungeData>, t: Throwable) {
                    Log.d("check", "failer")
                }

            })

        }
    }

    fun CheckVipType(){
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(DriveService::class.java).CheckViptype(
                my_seq.value!!

            )

            d!!.enqueue(object : retrofit2.Callback<ResultViptypeData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultViptypeData>,
                    response: Response<ResultViptypeData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        Log.d("VIP_DATA", data.toString())
                        var type =data.type
                        if(type.equals("success")) {
                            var u_vip = data.u_vip
                            if (u_vip!= null) {
                                if (u_vip.equals("vip")) {
                                    viplounge_Check.value = 100
                                }else{
                                  getVipGauge()
                                }
                            }else{
                                getVipGauge()
                            }

                        }

                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultViptypeData>, t: Throwable) {
                    Log.d("check", "failer")
                }

            })

        }
    }

    fun getBannerTop(bl_name:String){


        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(BannerService::class.java).getHomeBanner(bl_name
            )
            d!!.enqueue(object : retrofit2.Callback<ResultBannerData> {
                override fun onResponse(
                    call: Call<ResultBannerData>,
                    response: Response<ResultBannerData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var rows = data.rows
                        if (rows != null) {
                            bannertop.value =rows
                        }

                    }

                }

                override fun onFailure(call: Call<ResultBannerData>, t: Throwable) {
                    Log.d("check", "failer")
                }

            })

        }
    }
    fun getBannerBottom(bl_name:String){


        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(BannerService::class.java).getHomeBanner(bl_name
            )
            d!!.enqueue(object : retrofit2.Callback<ResultBannerData> {
                override fun onResponse(
                    call: Call<ResultBannerData>,
                    response: Response<ResultBannerData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var rows = data.rows
                        if (rows != null) {
                            bannerbottom.value =rows
                        }

                    }

                }

                override fun onFailure(call: Call<ResultBannerData>, t: Throwable) {
                    Log.d("check", "failer")
                }

            })

        }
    }
    fun getOneNoti(n_seq:String){


        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(BannerService::class.java).getOneNoti(n_seq
            )
            d!!.enqueue(object : retrofit2.Callback<ResultOneNotiData> {
                override fun onResponse(
                    call: Call<ResultOneNotiData>,
                    response: Response<ResultOneNotiData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var data = data.rows
                        select_bannernoti_data.value = data

                    }

                }

                override fun onFailure(call: Call<ResultOneNotiData>, t: Throwable) {
                    Log.d("check", "failer")
                }

            })

        }
    }
    fun getCategoryList()  {

        var list :ArrayList<ConciergeCategoryData> = arrayListOf()
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(CategoryService::class.java).getCategory()
            d!!.enqueue(object : retrofit2.Callback<ResultCategoryData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultCategoryData>,
                    response: Response<ResultCategoryData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var rows = data.rows
                        if (rows != null) {
                            list.addAll(rows)
                            list.add(0, ConciergeCategoryData(0,"전체"))
                            tablist.value = list

                        }

                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultCategoryData>, t: Throwable) {
                    Log.d("check", "failer")
                }

            })

        }
    }
    fun viewLogBanner(bc_seq:Int,cl_type:String) {
        var myseq = my_seq.value
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(BannerService::class.java)
                .viewLogBanner(bc_seq,myseq,cl_type)
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        var message = data.message
                        if (type.equals("success")) {

                        } else {

                        }


                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                }

            })

        }

    }
    fun GetInviteCode() {
        if(invite_code.value.isNullOrEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter.create(UserService::class.java).InviteCodeGet(
                    my_seq.value,
                )
                d!!.enqueue(object : retrofit2.Callback<ResultInvateCodeData> {
                    override fun onResponse(
                        call: retrofit2.Call<ResultInvateCodeData>,
                        response: Response<ResultInvateCodeData>
                    ) {
                        var data = response.body()
                        if (data != null) {
                            var rows = data.rows
                            if (rows != null) {
                                if (rows.size != 0) {
                                    var codeset = rows.get(0).my_code
                                    invite_code.value = codeset
                                    inviteset.value = true
                                }
                            }
                        }

                    }

                    override fun onFailure(
                        call: retrofit2.Call<ResultInvateCodeData>,
                        t: Throwable
                    ) {
                        Log.d("check", t.toString())
                    }

                })

            }
        }else{
            inviteset.value = true
        }
    }
}