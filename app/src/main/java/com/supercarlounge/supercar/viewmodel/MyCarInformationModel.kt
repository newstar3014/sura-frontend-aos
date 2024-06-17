package com.supercarlounge.supercar.viewmodel

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.data.*


class MyCarInformationModel : ViewModel() {

    var titleText = MutableLiveData<String>("?")
    var mainText = MutableLiveData<String>("")
    var subText =
        MutableLiveData<String>("초대 코드를 입력해 주세요. \n" + "초대 코드가 입력된 가입신청은 \n" + "우선적으로 가입심사를 해 드립니다.")
    var informationText = MutableLiveData<String>("초대 코드가 없으시면 다음 버튼을 눌러주세요.")
    var bottomText = MutableLiveData<String>("다음")
    var position = MutableLiveData<Int>(0)
    var tvCarBrand = MutableLiveData<String>("")
    var editBoolean = MutableLiveData<Boolean>(false)
    var tvCarBrandNull = MutableLiveData<String>("브랜드 선택")
    var tvCarModel = MutableLiveData<String>("")
    var tvCarModelNull = MutableLiveData<String>("모델명")
    var tvCarDeepModel = MutableLiveData<String>("")
    var tvCarDeepModelNull = MutableLiveData<String>("시리즈명")
    var tvCarInsurance = MutableLiveData<String>("업데이트해주세요")
    var tvCarImage = MutableLiveData<String>("")
    var InsuranceImage = MutableLiveData<String>(null)
    var carImage = MutableLiveData<String>(null)
    var tvInsuranceImageCheck = MutableLiveData<String>("업데이트 완료")
    var tvCarImageCheck = MutableLiveData<String>("업데이트 완료")
    val carDataList : MutableLiveData<ArrayList<UserInformationCarData>> =MutableLiveData(arrayListOf())
     var carDataList_really : ArrayList<UserInformationCarData> = arrayListOf()
    val isEditCheck =MutableLiveData<Boolean>(false)

    var tvInsuranceEdit = MutableLiveData<String>("수정")
    var tvCarEdit = MutableLiveData<String>("수정")


    var mainCarCheck = MutableLiveData<Boolean>(false)

    val list = ArrayList<SubImageDeepData>()

    var   mainCheckReturn  = MutableLiveData<String>("")
    var   sub1CheckReturn  = MutableLiveData<String>("")
    var   sub2CheckReturn  = MutableLiveData<String>("")
    var   sub3CheckReturn  = MutableLiveData<String>("")
    var   cartext = MutableLiveData<String>("차량 정보 수정하기")
     var   view  = MutableLiveData<String>("")
    var my_seq = MutableLiveData<String>("")
    var my_user_data = MutableLiveData<UserInformationData>(null)
    var my_car_data_list : MutableLiveData<ArrayList<ArrayList<UserInformationCarData>>> =MutableLiveData(arrayListOf())
    var delete_my_car_data_list: MutableLiveData<ArrayList<UserInformationCarData>> = MutableLiveData(arrayListOf())
    var before_my_car_data_list: MutableLiveData<ArrayList<UserInformationCarData>> = MutableLiveData(arrayListOf())
    private val _itemList = MutableLiveData<ArrayList<SubImageDeepData>>()
    var SubImageUrlList: LiveData<ArrayList<SubImageDeepData>> = _itemList
    var SubImageUrlListSize = MutableLiveData<Int>(0)
    var mainImageUrl = MutableLiveData<String>(null)
    var car_yn_text=MutableLiveData<String>(null)
    init {
        UserInformationData
        _itemList.value = list
        var d = UserInformationCarData("","","","","0",0,
            "","","","","","","","","",0,"","","","","","","","")
        delete_my_car_data_list.value?.add(d)
        delete_my_car_data_list.value?.add(d)
        delete_my_car_data_list.value?.add(d)
    }

    var state = MutableLiveData<Int>(0)
    var endstate =  MutableLiveData<Int>(0)
    var nameTotalCount = MutableLiveData<String>("8")
    var nameNowCount = MutableLiveData<String>("0")
    var inside = MutableLiveData<Boolean>(false)
    var outside = MutableLiveData<Boolean>(false)

    var charmlist = MutableLiveData<ArrayList<String>>()
    var musictypelist = MutableLiveData<ArrayList<String>>()
    //state 2
    var outsideCheckedArray = ObservableArrayList<Boolean>()
    var insideCheckedArray = ObservableArrayList<Boolean>()
    var totalArray = ObservableArrayList<Boolean>()
    var resultState2Array = ObservableArrayList<String>()


    fun mainCarChecked(boolean: Boolean){
        mainCarCheck.value = boolean
    }

    var original_car_image_list :MutableLiveData<ArrayList<UserInformationCarData>> = MutableLiveData(arrayListOf())

//    object BindingAdapters {
//        @JvmStatic
//        @BindingAdapter("visibleGone")
//        fun showHide(view: View, s :String) {
//            view.visibility = if (s.isEmpty()) View.VISIBLE else View.GONE
//            Log.d("visibleGone", s.toString())
//        }
//    }
}


