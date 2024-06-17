package com.supercarlounge.supercar.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.LocationAreaCategoryData
import com.supercarlounge.supercar.data.LocationCateData
import com.supercarlounge.supercar.data.LocationSpotData

class LocationAreaViewModel : ViewModel() {
    var nottext = MutableLiveData<String>("게시글을 불러오는 중입니다.")
    var recodata = MutableLiveData<ArrayList<LocationSpotData>>(ArrayList())
    var areacategorylist = MutableLiveData<ArrayList<LocationAreaCategoryData>>(ArrayList())
    var locationcategory = MutableLiveData<ArrayList<LocationCateData>>(ArrayList())
    var areacatelist = MutableLiveData<String>("게시글을 불러오는 중입니다.")
    var regiposition = MutableLiveData<Int>(0)
    var regionArray =  MutableLiveData<ArrayList<String>>(
        arrayListOf(
        "서울 강남구","서울 강동구",
        "서울 강북구","서울 강서구",
        "서울 관악구","서울 광진구",
        "서울 구로구","서울 금천구",
        "서울 노원구","서울 도봉구",
        "서울 동대문구","서울 동작구",
        "서울 마포구","서울 서대문구",
        "서울 서초구","서울 성동구",
        "서울 성북구","서울 송파구",
        "서울 양천구","서울 영등포구",
        "서울 용산구","서울 은평구",
        "서울 종로구","서울 중구",
        "서울 중랑구","경기 안양시",
        "경기 의왕시","경기 과천시",
        "경기 군포시","경기 안산시",
        "경기 시흥시","경기 부천시",
        "경기 광명시","경기 성남시",
        "경기 용인시","경기 광주시",
        "경기 구리시","경기 남양주시",
        "경기 하남시","경기 오산시",
        "경기 화성시","경기 수원시",
        "경기 수원시","경기 평택시",
        "경기 안성시","경기 여주시",
        "경기 양평군","경기 이천시",
        "경기 고양시","경기 김포시",
        "경기 파주시","경기 동두천시",
        "경기 양주시","경기 의정부시",
        "경기 연천군","경기 포천시",
        "경기 가평군",
        "인천", "강원도",
        "대전", "세종",
        "충청도", "대구",
        "전라도", "경상도",
        "울산", "부산",
        "제주", "해외",

        )
    )

    init {
        var list:ArrayList<LocationCateData> = arrayListOf()
        list.add(LocationCateData("","전체", R.drawable.icon_location_cate1))
        list.add(LocationCateData("","북마크", R.drawable.icon_location_cate2))
        list.add(LocationCateData("","슈라의 추천", R.drawable.icon_location_cate3))
        list.add(LocationCateData("","드라이브 코스", R.drawable.icon_location_cate4))
        list.add(LocationCateData("","맛집,카페", R.drawable.icon_location_cate5))
        list.add(LocationCateData("","골프장(운동)", R.drawable.icon_location_cate6))
        list.add(LocationCateData("","차박,캠핑", R.drawable.icon_location_cate7))
    }
}
