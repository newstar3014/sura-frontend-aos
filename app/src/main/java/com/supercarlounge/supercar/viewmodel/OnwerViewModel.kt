package com.supercarlounge.supercar.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.JoinService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class OnwerViewModel : ViewModel() {


    var state= MutableLiveData<Int>(0)
    var text = MutableLiveData<String>("?")
    var reSend = MutableLiveData<String>("재전송")
    var reSendCheck = MutableLiveData<String>("인증번호를 확인해주세요")
    var reSendTime = MutableLiveData<String>("00:00")
    var carCursorPoint = MutableLiveData<Int>(-1)
    var carSeriesCursorPoint = MutableLiveData<Int>(-1)
    var carDetailsSeriesCursorPoint = MutableLiveData<Int>(-1)
    var carFilterCursorPoint = MutableLiveData<Int>(0)
    var selectCarBrand =  MutableLiveData<String>("")
    var selectCarModel =  MutableLiveData<String>("")
    var selectCarSeries =  MutableLiveData<String>("")
    var selectCarBrandName =  MutableLiveData<String>("")
    var selectCarModelName =  MutableLiveData<String>("")
    var selectCarSeriesName =  MutableLiveData<String>(null)
    var selectCarBrandImage =  MutableLiveData<String>("")
    var carImage =  MutableLiveData<String>("")
    var insuranceimage =  MutableLiveData<String>("")
    var my_user_car_data = MutableLiveData<UserInformationCarData>(null)
    var view =  MutableLiveData<String>("")
//    var carList: ArrayList<CarBrandListData> = arrayListOf()

    //넘겨줄데이터터
   var nickname = MutableLiveData<String>("")
    var resultState1Array = ArrayList<String>()
    var resultState2Array = ArrayList<String>()
    var resultState3Array = ArrayList<String>()
    var resultState4Array =ArrayList<String>()
    var resultState5Array = ArrayList<String>()
    var resultState6Array = ArrayList<String>()
    var resultState7Array = ArrayList<String>()


    var stateBottomText= MutableLiveData<Boolean>(false)
    private val clist = ArrayList<CarBrandListData>()
    private val _carList = MutableLiveData<ArrayList<CarBrandListData>>()
    val carList: MutableLiveData<ArrayList<CarBrandListData>> = _carList

    private val cmlist = ArrayList<CarModelListData>()
    private val _carSeriesList = MutableLiveData<ArrayList<CarModelListData>>()
    val carSeriesList: MutableLiveData<ArrayList<CarModelListData>> = _carSeriesList


    private val csmlist = ArrayList<CarSubModelListData>()
    private val _carDetailsSeriesList = MutableLiveData<ArrayList<CarSubModelListData>>()
    val carDetailsSeriesList: MutableLiveData<ArrayList<CarSubModelListData>> = _carDetailsSeriesList

    init {
        _carList.value = clist
        _carSeriesList.value = cmlist
        _carDetailsSeriesList.value = csmlist
    }

    fun addCarBrandList(item: ArrayList<CarBrandListData>) {
        carList.value?.clear()
        carList.value?.addAll(item)
        carList.notifyObserver()
    }

    fun addCarModelList(item: ArrayList<CarModelListData>) {
        carSeriesList.value?.clear()
        carSeriesList.value?.addAll(item)
        carSeriesList.notifyObserver()
    }

    fun addCarSubModelList(item: ArrayList<CarSubModelListData>) {
        Log.d("리스트", "addCarSubModelList: ")
        if (item.size== 0){
            stateBottomText.value = true
        }
        carDetailsSeriesList.value?.clear()
        carDetailsSeriesList.value?.addAll(item)
        carDetailsSeriesList.notifyObserver()
    }
    fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }
    fun getCarBrandList(target:String ) {
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveraManagerdapter!!.create(JoinService::class.java).getCarBrandList(target
            )
            d!!.enqueue(object : retrofit2.Callback<ArrayList<CarBrandListData>> {
                override fun onResponse(
                    call: retrofit2.Call<ArrayList<CarBrandListData>>,
                    response: Response<ArrayList<CarBrandListData>>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var rows = data
                        if (rows != null) {
                            Log.d("카리스트", "getCarBrandList: ")
                            addCarBrandList(data)
                        }else{
                            var list : ArrayList<CarBrandListData> = arrayListOf()
                            addCarBrandList(list)
                        }

                    }else{
                        var list : ArrayList<CarBrandListData> = arrayListOf()
                        addCarBrandList(list)
                    }

                }

                override fun onFailure(call: retrofit2.Call<ArrayList<CarBrandListData>>, t: Throwable) {
                    Log.d("check", "failer")
                }

            })

        }


    }

    fun getCarModelList(card_seq:String,target:String ) {
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveraManagerdapter!!.create(JoinService::class.java).getCarModelList(card_seq,target
            )
            d!!.enqueue(object : retrofit2.Callback<ArrayList<CarModelListData>> {
                override fun onResponse(
                    call: retrofit2.Call<ArrayList<CarModelListData>>,
                    response: Response<ArrayList<CarModelListData>>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var rows = data
                        if (rows != null) {
//                            Log.d("카리스트", card_seq)
                            Log.d("카리스트", target)
                            addCarModelList(data)
                        } else{
                            var list : ArrayList<CarModelListData> = arrayListOf()
                            addCarModelList(list)
                        }
                    }else{
                        var list : ArrayList<CarModelListData> = arrayListOf()
                        addCarModelList(list)

                    }

                }

                override fun onFailure(call: retrofit2.Call<ArrayList<CarModelListData>>, t: Throwable) {
                    Log.d("check", "failer")
                }

            })

        }


    }

    fun getCarSubModelList(carm_seq:String, ) {
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveraManagerdapter!!.create(JoinService::class.java).getCarSubModelList(carm_seq
            )
            d!!.enqueue(object : retrofit2.Callback<ArrayList<CarSubModelListData>> {
                override fun onResponse(
                    call: retrofit2.Call<ArrayList<CarSubModelListData>>,
                    response: Response<ArrayList<CarSubModelListData>>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var rows = data
                        if (rows != null) {

                            Log.d("카리스트", carm_seq)
                            addCarSubModelList(data)
                        } else{
                            var list : ArrayList<CarSubModelListData> = arrayListOf()
                            addCarSubModelList(list)
                        }
                    }else{
                        var list : ArrayList<CarSubModelListData> = arrayListOf()
                        addCarSubModelList(list)
                    }
                }

                override fun onFailure(call: retrofit2.Call<ArrayList<CarSubModelListData>>, t: Throwable) {
                    Log.d("check", "failer")
                }

            })

        }


    }
}
