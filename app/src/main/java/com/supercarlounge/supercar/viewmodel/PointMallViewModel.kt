package com.supercarlounge.supercar.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.HomerService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.Network.PointMallService
import com.supercarlounge.supercar.Network.UserService
import com.supercarlounge.supercar.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class PointMallViewModel : ViewModel() {
    var my_seq = MutableLiveData<String>("")
    var titleText = MutableLiveData<String>("포인트몰")
    var nowpoint = MutableLiveData<String>("0")
    var toast = MutableLiveData<String>("")
    var productlist = MutableLiveData<ArrayList<PointMallProductData>>(arrayListOf())
    var ppp = MutableLiveData<Int>(10)
    var nowpage = MutableLiveData<Int>(1)
    var totalPage = MutableLiveData<Int>(0)
    var totalCount = MutableLiveData<Int>(0)
    var issend = MutableLiveData<Boolean>(false)
    fun getproduct(isreset:Boolean){
        issend.value = true
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(PointMallService::class.java).GetPointProductlist(nowpage.value!!,ppp.value!!)
            d!!.enqueue(object : retrofit2.Callback<ResultPointMallProductData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultPointMallProductData>,
                    response: Response<ResultPointMallProductData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if(type.equals("success")) {
                            nowpage.value = nowpage.value?.plus(1)
                            var totalpage = data.totalPage
                            var totalcount = data.totalCount
                            totalPage.value = totalpage
                            totalCount.value = totalcount
                            var rows = data.rows
                            if (rows != null) {
                                if (isreset) {
                                    productlist.value = rows
                                }else{
                                    var d = productlist.value
                                    d!!.addAll(rows)
                                    productlist.value = d!!
                                }
                            }
                        }
                    }
                    issend.value = false
                }

                override fun onFailure(call: retrofit2.Call<ResultPointMallProductData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                    issend.value = false
                }

            })

        }
    }
    fun GetMyPoint(){

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(PointMallService::class.java).GetMyPoint(my_seq.value!!)
            d!!.enqueue(object : retrofit2.Callback<ResultPointData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultPointData>,
                    response: Response<ResultPointData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if(type.equals("success")) {
                            var rows = data.rows
                            if (rows.size != 0) {
                                var point = rows.get(0).u_point
                                nowpoint.value = point
                            }
                        }
                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultPointData>, t: Throwable) {
                    Log.d("failer", t.message!!)

                }

            })

        }
    }

    fun BuyProduct(goodsNo:String,goodsCode:String){

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(PointMallService::class.java).BuyProduct(my_seq.value!!,goodsNo,goodsCode)
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if(type.equals("success")) {
                            toast.value = "상품 교환에 성공했습니다. SMS로 쿠폰이 발송되었습니다."
                            GetMyPoint()
                        }else if(type.equals("notpoint")){
                            toast.value = "보유 포인트가 부족합니다. 포인트를 확인해주세요."
                        }else{
                            toast.value = "잠시 후 다시 실행해주세요."
                        }
                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                    toast.value = "잠시 후 다시 실행해주세요."

                }

            })

        }
    }
}
