package com.supercarlounge.supercar.viewmodel

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.JoinService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.data.ResultData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class JoinViewModel : ViewModel() {

    var titleText = MutableLiveData<String>("?")
    var mainText = MutableLiveData<String>("")
    var subText =
        MutableLiveData<String>("초대 코드를 입력해 주세요. \n" + "초대 코드가 입력된 가입신청은 \n" + "우선적으로 가입심사를 해 드립니다.")
    var informationText = MutableLiveData<String>("초대 코드가 없으시면 다음 버튼을 눌러주세요.")
    var bottomText = MutableLiveData<String>("다음")
    var state = MutableLiveData<Int>(1)
    var nickname = MutableLiveData<String>("")

    var endstate = MutableLiveData<Int>(0)
    var view = MutableLiveData<String>("")
    var nameTotalCount = MutableLiveData<String>("8")
    var nameNowCount = MutableLiveData<String>("0")
    var state1Lock = MutableLiveData<Boolean>(false)
    var state1Success = MutableLiveData<Boolean>(false)
    var nicknameCheck = MutableLiveData<Boolean>(true)
    var inside = MutableLiveData<Boolean>(false)
    var outside = MutableLiveData<Boolean>(false)
    var intentContext = MutableLiveData<String>("")
    var intentContext2 = MutableLiveData<String>("")
    var regionState = MutableLiveData<Int>(0)

    val stateCheckArray = ObservableArrayList<Int>()
    var seoulCheck = MutableLiveData<Boolean>(false)

    var gyeonggiCheck = MutableLiveData<Boolean>(false)


    var stateBottomText = MutableLiveData<Boolean>(false)


    var state2Count = 0
    var state3Count = 0
    var state4Count = 0
    var state5Count = 0
    var state6Count = 0
    var state7Count = 0
    var textArray = arrayOf(
        "몸매가 좋은", "웃을 때 예쁜", "깔끔한",
        "엉덩이가 예쁜", "비율이 좋은", "동안",
        "노래 선곡", "다리가 예쁜", "글래머",
        "출중한 외모", "패션 감각", "큰눈",
        "베스트 드라이버", "무쌍", "슬림 탄탄",
        "근육질", "슬렌더", "넓은 어깨",
        "보조개", "섹시한 타투", "섹시한 뇌",
        "좋은 목소리", "잔근육", "요리 실력",
        "예쁜 손", "좋은 피부결", "자수성가",
        "뛰어난 커리어", "구릿빛 피부"

    )

    //state 2
    var outsideCheckedArray = ObservableArrayList<Boolean>()
    var insideCheckedArray = ObservableArrayList<Boolean>()
    var totalArray = ObservableArrayList<Boolean>()
    var resultState2Array = ObservableArrayList<String>()

    var outSideArray = arrayOf(
        "몸매가 좋은", "웃을 때 예쁜",
        "깔끔한", "엉덩이가 예쁜",
        "비율이 좋은", "동안",
        "글래머", "다리가 예쁜",
        "출중한 외모", "큰눈",
        "무쌍", "보조개",
        "구릿빛 피부", "좋은 피부결",
        "슬림탄탄", "잔근육",
        "넓은 어깨", "근육질",
        "섹시한 타투", "예쁜 손"

    )

    var inSideArray = arrayOf(
        "노래 선곡 센스", "베스트 드라이버",
        "자수성가", "요리 실력",
        "뛰어난 커리어", "섹시한 뇌",
        "좋은 목소리", "성실한",
        "착한", "친절한",
        "다정한", "유쾌한",
        "쿨한", "매너있는",
        "대범한", "섬세하고 꼼꼼한",
        "완전 화끈한", "까칠 도도한",
        "여성스러운", "남자다운",
        "보다 보면 귀여운", "완전 발랄한",
        "든든하고 듬직한", "몹시 열정적인",
        "낙천적인", "유머있는",
        "사랑스러운", "볼수록 매력적인"

    )

    //state 3
    var musicTasteCheckedArray = ObservableArrayList<Boolean>()
    var resultState3Array = ObservableArrayList<String>()
    var musicTasteArray = arrayOf(
        "신나는", "분위기",
        "댄스", "랩/힙합",
        "아이돌", "록/메탈",
        "성인가요", "일렉트로니카",
        "키즈", "POP",
        "포크/블루스/컨트리", "인디음악",
        "재즈 블루스", "뮤지컬",
        "R&B/Soul", "종교",
        "재즈", "발라드",
        "클래식", "J-POP",
        "국악", "뉴에이지", "OST", "CCM"

    )

    //state 4
    var heightCheckedArray = ObservableArrayList<Boolean>()
    var resultState4Array = ObservableArrayList<String>()
    var heightArray = arrayOf(
        "186 이상", "181 ~ 185",
        "176 ~ 180", "171 ~ 175",
        "166 ~ 170", "161 ~ 165",
        "156 ~ 160", " 155 이하",
    )

    //state 5
    var jobCheckedArray = ObservableArrayList<Boolean>()
    var resultState5Array = ObservableArrayList<String>()
    var jobArray = arrayOf(
        "사업", "전문직",
        "모델", "연예인",
        "법조인", "의료직",
        "인플루언서", "공무원",
        "금융업", "PD·작가 등",
        "프리랜서", "학생",
        "서비스직", "요식업",
        "회사원", "교육업",
        "예술·디자인", "언론인",
        "기술직", "관리직",
        "체육", "마케터",
        "IT·인터넷", "군인",
        "건설", "기타"
    )

    //state 6
    var regionCheckedArray = ObservableArrayList<Boolean>()
    var resultState6Array = ObservableArrayList<String>()
    var regionArray = arrayOf(
        "인천", "강원도",
        "대전", "세종",
        "충청도", "대구",
        "전라도", "경상도",
        "울산", "부산",
        "제주", "해외",

        )

    var regionSeoulCheckedArray = ObservableArrayList<Boolean>()

    var regionSeoulArray = arrayOf(
        "강남구", "강동구",
        "강북구", "강서구",
        "관악구", "광진구",
        "구로구", "금천구",
        "노원구", "도봉구",
        "동대문구", "동작구",
        "마포구", "서대문구",
        "서초구", "성동구",
        "성북구", "송파구",
        "양천구", "영등포구",
        "용산구", "은평구",
        "종로구", "중구",
        "중랑구", "영등포구",


        )

    var regionGyeonggiCheckedArray = ObservableArrayList<Boolean>()

    var regionGyeonggiArray = arrayOf(
        "안양시", "의왕시",
        "과천시", "군포시",
        "안산시", "시흥시",
        "부천시", "광명시",
        "성남시", "용인시",
        "광주시", "구리시",
        "남양주시", "하남시",
        "오산시", "화성시",
        "수원시", "평택시",
        "안성시", "여주시",
        "양평군", "이천시",
        "고양시", "김포시",
        "파주시", "동두천시",
        "양주시", "의정부시",
        "연천군", "포천시",
        "가평군",


        )

    //state 7
    var driveStyleCheckedArray = ObservableArrayList<Boolean>()
    private val list = ArrayList<String>()
    private val _itemList = MutableLiveData<ArrayList<String>>()
    val resultState7Array: MutableLiveData<ArrayList<String>> = _itemList

    var driveStyleArray = arrayOf(
        "속도를 즐겨요", "천천히 여유있는",
        "가까운 곳 드라이브", "멀리 떠나는 드라이브",
        "밤에 하는 드라이브", "낮에 하는 드라이브",
        "새벽에 하는 드라이브", "편안한 사람과 드라이브",
        "새로운 사람과 드라이브 ", "즉흥적인 드라이브",
        "계획적인 드라이브", "맛집 탐방 드라이브",
    )

    init {
        _itemList.value = list
    }

    fun setOutside(checked: Boolean) {

        outside.value = checked
        if (outside.value == true) {
            inside.value = false
        }
    }

    fun setInside(checked: Boolean) {
        inside.value = checked
        if (inside.value == true) {
            outside.value = false
        }
    }

    init {
//        outsideCheckedArray = ObservableArrayList()

            for ((count, i) in outSideArray.withIndex()) {
                outsideCheckedArray?.add(count, false)
            }
        for ((count, i) in inSideArray.withIndex()) {
            insideCheckedArray?.add(count, false)
        }

        for (i in 0..23) {
            musicTasteCheckedArray?.add(i, false)
        }
        for (i in 0..7) {
            heightCheckedArray?.add(i, false)
        }
        for (i in 0..25) {
            jobCheckedArray?.add(i, false)
        }
        for ((count, i) in regionArray.withIndex()) {
            regionCheckedArray?.add(count, false)
        }
        for ((count, i) in regionSeoulArray.withIndex()) {
            regionSeoulCheckedArray?.add(count, false)
        }

        for ((count, i) in regionGyeonggiArray.withIndex()) {
            regionGyeonggiCheckedArray?.add(count, false)
        }
        for (i in 0..11) {
            driveStyleCheckedArray?.add(i, false)
        }

    }

    fun setDriveStyleSelect(position: Int, boolean: Boolean) {
        state7Count = 0
        resultState7Array.value?.clear()

        if (driveStyleCheckedArray[position]) {
            driveStyleCheckedArray?.set(position, false)


        } else {
            for ((c, i) in driveStyleCheckedArray.withIndex()) {
                driveStyleCheckedArray[c] = false
            }
            driveStyleCheckedArray?.set(position, true)
            resultState7Array.value?.add(driveStyleArray[position])

            if (!view.value.equals("수정")) {
                resultState7Array.value =   resultState7Array.value
            }
            Log.d("드라이브 결과2", resultState7Array.value.toString())
        }
    }

    fun setRegionSeoul(boolean: Boolean) {
        seoulCheck.value = boolean
        if (seoulCheck.value == true) {
            gyeonggiCheck.value = false
            resultState6Array.clear()
            for ((c, i) in regionGyeonggiCheckedArray.withIndex()) {
                regionGyeonggiCheckedArray[c] = false
            }
            for ((c, i) in regionCheckedArray.withIndex()) {
                regionCheckedArray[c] = false
            }
        }
    }

    fun setRegionGyeonggi(boolean: Boolean) {

        gyeonggiCheck.value = boolean
        if (gyeonggiCheck.value == true) {
            seoulCheck.value = false
            resultState6Array.clear()
            for ((c, i) in regionSeoulCheckedArray.withIndex()) {
                regionSeoulCheckedArray[c] = false
            }
            for ((c, i) in regionCheckedArray.withIndex()) {
                regionCheckedArray[c] = false
            }
        }

    }


    fun setRegionGyeonggiSelect(position: Int, boolean: Boolean) {
        state6Count = 0
        resultState6Array.clear()

        if (regionGyeonggiCheckedArray[position]) {
            regionGyeonggiCheckedArray?.set(position, false)


        } else {
            for ((c, i) in regionGyeonggiCheckedArray.withIndex()) {
                regionGyeonggiCheckedArray[c] = false
            }
            regionGyeonggiCheckedArray?.set(position, true)
            resultState6Array.add("경기 " + regionGyeonggiArray[position])
            if (!view.value.equals("수정")) {
                state.value = 7
            }
            Log.d("경기 결과", resultState6Array.toString())
        }


    }

    fun setRegionSeoulSelect(
        position: Int,
        cehcked: Boolean
    ) {
        state6Count = 0
        resultState6Array.clear()

        if (regionSeoulCheckedArray[position]) {
            regionSeoulCheckedArray?.set(position, false)


        } else {
            for ((c, i) in regionSeoulCheckedArray.withIndex()) {
                regionSeoulCheckedArray[c] = false
            }
            regionSeoulCheckedArray?.set(position, true)
            resultState6Array.add("서울 " + regionSeoulArray[position])
            if (!view.value.equals("수정")) {
                state.value = 7
            }
            Log.d("서울 결과2", resultState6Array.toString())
        }


    }

    fun setRegionSelect(position: Int, boolean: Boolean) {
        state6Count = 0
        resultState6Array.clear()

        if (regionCheckedArray[position]) {
            regionCheckedArray?.set(position, false)


        } else {
            for ((c, i) in regionCheckedArray.withIndex()) {
                regionCheckedArray[c] = false
            }
            regionCheckedArray?.set(position, true)
            resultState6Array.add(regionArray[position])
            if (!view.value.equals("수정")) {
                state.value = 7
            }
            Log.d("지역 결과2", resultState6Array.toString())
        }


    }

    fun setJobSelect(position: Int, boolean: Boolean) {
        state5Count = 0
        resultState5Array.clear()


        if (jobCheckedArray[position]) {
            jobCheckedArray?.set(position, false)


        } else {
            for ((c, i) in jobCheckedArray.withIndex()) {
                jobCheckedArray[c] = false
            }
            jobCheckedArray?.set(position, true)
            resultState5Array.add(jobArray[position])
            if (!view.value.equals("수정")) {
                state.value = 6
            }
            Log.d("키 결과2", resultState5Array.toString())
        }
    }

    fun setHeightSelect(position: Int, boolean: Boolean) {
        state4Count = 0
        resultState4Array.clear()



        if (heightCheckedArray[position]) {
            heightCheckedArray?.set(position, false)


        } else {
            for ((c, i) in heightCheckedArray.withIndex()) {
                heightCheckedArray[c] = false
            }
            heightCheckedArray?.set(position, true)
            resultState4Array.add(heightArray[position])
            if (!view.value.equals("수정")) {
                state.value = 5
            }
            Log.d("키 결과2", resultState4Array.toString())
        }


    }

    fun setMusicTasteSelect(position: Int, boolean: Boolean) {
        state3Count = 0
        resultState3Array.clear()
        musicTasteCheckedArray?.set(position, boolean)

        for (i in musicTasteCheckedArray!!) {
            if (i == true) {
                state3Count++

            }
        }
        if (state3Count == 3) {

            state3Count--
            musicTasteCheckedArray?.set(position, false)

        } else {
            if (state3Count == 2 && boolean) {
                if (view.value.equals("수정")) {

                } else {
                    state.value = 4
                }

            }
        }

        var c = 0
        for (i in musicTasteCheckedArray) {
            if (i == true) {
                resultState3Array.add(musicTasteArray[c])
            }
            c++
        }

        Log.d("state-3 결과", resultState3Array.toString())
    }

    fun setOutsideSelect(position: Int, boolean: Boolean) {
        state2Count = 0
        totalArray?.clear()
        totalArray?.addAll(outsideCheckedArray)
        totalArray?.addAll(insideCheckedArray)
        resultState2Array.clear()

        if (outsideCheckedArray[position]) {
            outsideCheckedArray?.set(position, false)
        } else {
            for (i in totalArray!!) {
                if (i == true) {
                    state2Count++

                }
            }
            Log.d("갯수 결과", state2Count.toString())
            if (state2Count == 0) {
                outsideCheckedArray?.set(position, true)
            } else if (state2Count == 1) {
                outsideCheckedArray?.set(position, true)
                if (!view.value.equals("수정")) {
                    state.value = 3
                }
            }
            for ((c, i) in outsideCheckedArray.withIndex()) {
                if (i == true) {
                    resultState2Array.add(outSideArray[c])
                }
            }
            for ((c, i) in insideCheckedArray.withIndex()) {
                if (i == true) {
                    resultState2Array.add(inSideArray[c])
                }
            }

            Log.d("state-2 결과", resultState2Array.toString())

        }
    }

    fun setInsideSelect(position: Int, boolean: Boolean) {
        state2Count = 0
        totalArray?.clear()
        totalArray?.addAll(outsideCheckedArray)
        totalArray?.addAll(insideCheckedArray)
        resultState2Array.clear()

        if (insideCheckedArray[position]) {
            insideCheckedArray?.set(position, false)
        } else {
            for (i in totalArray!!) {
                if (i == true) {
                    state2Count++

                }
            }
            Log.d("갯수 결과", state2Count.toString())
            if (state2Count == 0) {
                insideCheckedArray?.set(position, true)
            } else if (state2Count == 1) {
                insideCheckedArray?.set(position, true)
                if (!view.value.equals("수정")) {
                    state.value = 3
                }
            }
            for ((c, i) in outsideCheckedArray.withIndex()) {
                if (i == true) {
                    resultState2Array.add(outSideArray[c])
                }
            }
            for ((c, i) in insideCheckedArray.withIndex()) {
                if (i == true) {
                    resultState2Array.add(inSideArray[c])
                }
            }

            Log.d("state-2 결과", resultState2Array.toString())

        }

    }

    fun nicknameCheck(u_nickname: String) {
        state1Lock.value = true
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(JoinService::class.java).nicknameCheck(
                u_nickname
            )
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        Log.d("nicknameCheck", data.message)
                        if (data.message.equals("사용 가능 합니다")) {
                            nicknameCheck.value = true
                            state1Success.value = true
                            state.value = state.value!!.plus(1)
                        } else {
                            nicknameCheck.value = false
                            state1Success.value = false
                        }
                        state1Lock.value = false

                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Log.d("check", t.toString())
                }

            })

        }

    }

    fun setText() {

    }

}


