package com.supercarlounge.supercar.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.BannerService
import com.supercarlounge.supercar.Network.CategoryService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class ConciergeViewModel : ViewModel() {
    var text = MutableLiveData<String>("")
    var titleText = MutableLiveData<String>("CONCIERGE")
    var size = MutableLiveData<String>("0")
    var bannerPosition= MutableLiveData<Int>(0)
    var bannertop= MutableLiveData<ArrayList<BannerData>>(arrayListOf())
    var tablist= MutableLiveData<ArrayList<ConciergeCategoryData>>(arrayListOf())
    var select_bannernoti_data= MutableLiveData<NotiData>()
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
}