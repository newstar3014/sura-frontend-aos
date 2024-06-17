package com.supercarlounge.supercar.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.databinding.ActivityJoinBinding
import com.supercarlounge.supercar.viewmodel.JoinViewModel


class JoinActivity : BaseActivitiy() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityJoinBinding
    private lateinit var viewModel: JoinViewModel
    private var application: MainApplication? = null
    var u_name = ""
    var u_gender = ""
    var u_birthday = ""
    var u_phone = ""
    var view = ""
    private var charmCheck = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewModel = ViewModelProvider(this).get(JoinViewModel::class.java)
        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding!!.viewModel = viewModel
        binding!!.setLifecycleOwner(this)
        application = applicationContext as MainApplication
        u_name = intent.getStringExtra("u_name").toString()
        u_gender = intent.getStringExtra("u_gender").toString()
        u_birthday = intent.getStringExtra("u_birthday").toString()
        u_phone = intent.getStringExtra("u_phone").toString()
        view = intent.getStringExtra("qickview").toString()
        viewModel.view.value = intent.getStringExtra("view")
        application!!.quick_view = ""


        if (viewModel.view.value.equals("수정")) {

            viewModel.state.value = intent.getIntExtra("state", 0)
            viewModel.intentContext.value = intent.getStringExtra("data")
            viewModel.intentContext2.value = intent.getStringExtra("data2")

            when (viewModel.state.value) {
                2 -> {
                    CharmListStringIndex()
                }

                3 -> {
                    viewModel.musicTasteCheckedArray.set(
                        listStringIndex(viewModel.musicTasteArray),
                        true
                    )
                    viewModel.musicTasteCheckedArray.set(
                        listStringIndex2(viewModel.musicTasteArray),
                        true
                    )
                    viewModel.resultState3Array.add(
                        viewModel.musicTasteArray[listStringIndex(
                            viewModel.musicTasteArray
                        )]
                    )
                    viewModel.resultState3Array.add(
                        viewModel.musicTasteArray[listStringIndex2(
                            viewModel.musicTasteArray
                        )]
                    )
                }

                4 -> {
                    viewModel.heightCheckedArray.set(listStringIndex(viewModel.heightArray), true)
                    viewModel.resultState4Array.add(viewModel.heightArray[listStringIndex(viewModel.heightArray)])
                }

                5 -> {
                    viewModel.jobCheckedArray.set(listStringIndex(viewModel.jobArray), true)
                    viewModel.resultState5Array.add(viewModel.jobArray[listStringIndex(viewModel.jobArray)])

                }

                6 -> {
                    Log.d("뭐지1", viewModel.intentContext.value.toString())
                    if (viewModel.intentContext.value!!.contains("경기도")) {
                        var context =
                            viewModel.intentContext.value!!.substring(4 until viewModel.intentContext.value!!.length)
                        Log.d("뭐지2", context)
                        viewModel.gyeonggiCheck.value = true
                        viewModel.regionGyeonggiCheckedArray.set(
                            listStringIndex3(
                                context,
                                viewModel.regionGyeonggiArray
                            ), true
                        )
                        viewModel.resultState6Array.add(
                            viewModel.regionGyeonggiArray[listStringIndex(
                                viewModel.regionGyeonggiArray
                            )]
                        )
                    } else if (viewModel.intentContext.value!!.contains("서울")) {
                        viewModel.seoulCheck.value = true
                        var context =
                            viewModel.intentContext.value!!.substring(3 until viewModel.intentContext.value!!.length)
                        Log.d("뭐지3", context)
                        viewModel.regionSeoulCheckedArray.set(
                            listStringIndex3(
                                context,
                                viewModel.regionSeoulArray
                            ), true
                        )
                        viewModel.resultState6Array.add(
                            viewModel.regionSeoulArray[listStringIndex(
                                viewModel.regionSeoulArray
                            )]
                        )
                    } else {
                        viewModel.regionCheckedArray.set(
                            listStringIndex(viewModel.regionArray),
                            true
                        )
                        viewModel.resultState6Array.add(
                            viewModel.regionArray[listStringIndex(
                                viewModel.regionArray
                            )]
                        )
                    }

                }

                7 -> {
                    viewModel.driveStyleCheckedArray.set(
                        listStringIndex(viewModel.driveStyleArray),
                        true
                    )
                    viewModel.resultState7Array.value?.add(
                        viewModel.driveStyleArray[listStringIndex(
                            viewModel.driveStyleArray
                        )]
                    )
                }

                else -> {}
            }
        }

        Log.d("아이템1", u_gender)


//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        viewModel.titleText.value = "회원가입"
        clickListeners()
        observes()
        editTextListeners()


    }

    private fun listStringIndex(list: Array<String>): Int {
        var posi = 0
        for (i in list) {
            if (i.equals(viewModel.intentContext.value)) {
                return posi
            }
            posi++
        }

        return 0
    }

    private fun listStringIndex3(s: String, list: Array<String>): Int {
        var posi = 0
        for (i in list) {
            if (i.equals(s)) {
                return posi
            }
            posi++
        }

        return 0
    }


    private fun CharmListStringIndex() {
//        viewModel.outsideCheckedArray.set(listStringIndex(viewModel.outSideArray),true)
//        viewModel.insideCheckedArray.set(listStringIndex(viewModel.inSideArray),true)
        var posi = 0
        for (i in viewModel.inSideArray) {
            if (i == viewModel.intentContext.value || i == viewModel.intentContext2.value) {
                viewModel.setInsideSelect(posi, true)
//                viewModel.resultState2Array.add(i)
            }
            posi++
        }
        posi = 0
        for (i in viewModel.outSideArray) {
            if (i == viewModel.intentContext2.value || i == viewModel.intentContext.value) {
                if (viewModel.resultState2Array.size < 2) {
                    viewModel.outsideCheckedArray.set(posi, true)
                }
//                viewModel.resultState2Array.add(i)

            }
            posi++
        }

    }

    private fun listStringIndex2(list: Array<String>): Int {
        var posi = 0
        for (i in list) {
            if (i.equals(viewModel.intentContext2.value)) {
                return posi
            }
            posi++
        }
        return 0
    }

//    private fun CharmListStringIndex(list1:Array<String>,list2:Array<String>  ): Int{
//        var posi1 = 0
//        var posi2 = 0
//
//        for (i in list1){
//            if (i.equals(viewModel.intentContext.value)){
//                charmCheck = false
//                return posi1
//            }
//            posi1 ++
//        }
//        for (i in list2){
//            if (i.equals(viewModel.intentContext.value)){
//                charmCheck = true
//                return posi2
//            }
//            posi2 ++
//        }
//        return 0
//    }


    private fun editTextListeners() {
        binding!!.joinTrack1.edtCertificationNumber2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {
                viewModel.nickname.value = p0.toString()
                viewModel.nameNowCount.value = viewModel.nickname.value?.length.toString()
                viewModel.state1Success.value = false
                viewModel.nicknameCheck.value = true

                viewModel.stateBottomText.value = !viewModel.nickname.value.equals("")
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    private fun observes() {
        viewModel.state.observe(this, Observer {
            if (it != null) {

                if (!view.equals("수정")) {
                    JoinCheck(it)
                    if (it > 1) {
                        binding.ivNext.visibility = View.VISIBLE
                    } else {
                        binding.ivNext.visibility = View.INVISIBLE
                    }
                }
            }
            setText(state = viewModel.state.value!!)
            if (it == 8) {


            } else if (it == 9) {


            }


            Log.d("observes", viewModel.state.value!!.toString())
        })
        viewModel.endstate.observe(this, Observer {
            if (it == 7) {

            }


        })
        viewModel.resultState7Array.observe(this, Observer {
            if (it != null) {
                if (it.size > 0) {
                    if (viewModel.view.value.equals("수정")) {

                    } else {
                        val i = Intent(this, CarCheckActivity::class.java)
                        i.putExtra("nickname", viewModel.nickname.value)
                        i.putExtra("CharmList", viewModel.resultState2Array)
                        i.putExtra("musicTasteList", viewModel.resultState3Array)
                        i.putExtra("height", viewModel.resultState4Array)
                        i.putExtra("job", viewModel.resultState5Array)
                        i.putExtra("regin", viewModel.resultState6Array)
                        i.putExtra("driveStyle", viewModel.resultState7Array.value)
                        i.putExtra("u_name", u_name)
                        i.putExtra("u_gender", u_gender)
                        i.putExtra("u_birthday", u_birthday)
                        i.putExtra("u_phone", u_phone)
                        if (view.equals("퀵유저")) {
                            i.putExtra("view", "퀵유저")
                        }
                        Log.d("아이템2", u_gender)
                        startActivity(i)
                        //  overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                    }
                }

            }

        })
    }

    private fun clickListeners() {
        binding.llBottomBar.setOnClickListener {

            when (viewModel.state.value) {
                1 -> {
                    if (!viewModel.nickname.value.equals("")) {
                        //  Log.d("로그", "clickListeners: ")
                        if (viewModel.state1Success.value == true) {
                            viewModel.state.value = viewModel.state.value!!.plus(1)
                            viewModel.stateBottomText.value = true
                        } else {
                            if (viewModel.state1Lock.value == false) {
                                viewModel.nicknameCheck(viewModel.nickname.value!!)
                            }
                        }
                    } else {
                        Toast.makeText(this, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    }

                }

                2 -> {
                    if (viewModel.view.value.equals("수정")) {

                        if (viewModel.resultState2Array.size == 2) {
                            var i = Intent().apply {
                                putExtra("type", 2)
                                putExtra("data", data)
                                putExtra("data", viewModel.resultState2Array[0])
                                putExtra("data2", viewModel.resultState2Array[1])
                                Log.d("승주 준1", viewModel.resultState2Array[0].toString())
                                Log.d("승주 준2", viewModel.resultState2Array[1].toString())
                            }
                            this.setResult(Activity.RESULT_OK, i)
                            this.finish()
                        }

                    } else {

                        viewModel.state.value = viewModel.state.value!!.plus(1)
                        viewModel.stateBottomText.value = false
                    }

                }

                3 -> {
                    if (viewModel.view.value.equals("수정")) {

                        if (viewModel.resultState3Array.size == 2) {
                            var i = Intent().apply {
                                putExtra("type", 3)
                                putExtra("data", viewModel.resultState3Array[0])
                                putExtra("data2", viewModel.resultState3Array[1])
                            }
                            this.setResult(Activity.RESULT_OK, i)
                            this.finish()
                        }

                    } else {
                        viewModel.state.value = viewModel.state.value!!.plus(1)
                        viewModel.stateBottomText.value = false
                    }

                }

                4 -> {
                    if (viewModel.view.value.equals("수정")) {

                        if (viewModel.resultState4Array.size != 0) {
                            var i = Intent().apply {
                                putExtra("type", 4)
                                putExtra("data", viewModel.resultState4Array[0])
                            }
                            this.setResult(Activity.RESULT_OK, i)
                            this.finish()
                        }

                    } else {
                        viewModel.state.value = viewModel.state.value!!.plus(1)
                        viewModel.stateBottomText.value = false
                    }

                }

                5 -> {
                    if (viewModel.view.value.equals("수정")) {

                        if (viewModel.resultState5Array.size != 0) {
                            var i = Intent().apply {
                                putExtra("type", 5)
                                putExtra("data", viewModel.resultState5Array[0])
                            }
                            this.setResult(Activity.RESULT_OK, i)
                            this.finish()
                        }

                    } else {
                        viewModel.state.value = viewModel.state.value!!.plus(1)
                        viewModel.stateBottomText.value = false
                    }

                }


                6 -> {
                    if (viewModel.view.value.equals("수정")) {

                        if (viewModel.resultState6Array.size != 0) {
                            var i = Intent().apply {
                                putExtra("type", 6)
                                putExtra("data", viewModel.resultState6Array[0])
                            }
                            this.setResult(Activity.RESULT_OK, i)
                            this.finish()
                        }

                    } else {
                        viewModel.state.value = viewModel.state.value!!.plus(1)
                        viewModel.stateBottomText.value = false
                    }

                }

                7 -> {
                    if (viewModel.view.value.equals("수정")) {

                        if (viewModel.resultState7Array.value?.size != 0) {
                            var i = Intent().apply {
                                putExtra("type", 7)
                                putExtra("data", viewModel.resultState7Array.value?.get(0))
                            }
                            this.setResult(Activity.RESULT_OK, i)
                            this.finish()
                        }

                    } else {
                        viewModel.state.value = viewModel.state.value!!.plus(1)
                        viewModel.stateBottomText.value = false
                    }

                }

                else -> {}
            }
            if (viewModel.state.value!! < 8) {

            }
            Log.d("clickListeners", viewModel.state.value!!.toString())
        }
        binding.ivBack.setOnClickListener {
//            var var1 = viewModel.state.value
//            var var2 = 1
//            viewModel.state.value = var1!!.minus(var2)
            if (viewModel.view.value.equals("수정")) {

                super.finish()
            } else {
                if (viewModel.state.value!! > 1) {
                    viewModel.stateBottomText.value = true
                    if (viewModel.state.value == 6) {
                        if (viewModel.seoulCheck.value == true || viewModel.gyeonggiCheck.value == true) {
                            viewModel.seoulCheck.value = false
                            viewModel.gyeonggiCheck.value = false

                        } else {
                            viewModel.state.value = viewModel.state.value!!.minus(1)
                        }

                    } else {
                        viewModel.state.value = viewModel.state.value!!.minus(1)
                    }

                } else {
                    super.finish()
                }
            }
        }
        binding.ivNext.setOnClickListener {
            if (!viewModel.view.value.equals("수정")) {
                when (viewModel.state.value) {
                    2 -> {
                        if (viewModel.resultState2Array.size == 2) {
                            viewModel.state.value = 3
                        } else {
                            Toast.makeText(this, "매력을 2개 이상 선택해 주세요", Toast.LENGTH_SHORT).show()
                        }

                    }

                    3 -> {
                        if (viewModel.resultState3Array.size == 2) {
                            viewModel.state.value = 4
                        } else {
                            Toast.makeText(this, "음악취향을 2개 이상 선택해 주세요", Toast.LENGTH_SHORT).show()
                        }

                    }

                    4 -> {
                        if (viewModel.resultState4Array.size == 1) {
                            viewModel.state.value = 5
                        } else {
                            Toast.makeText(this, "키를 1개 이상 선택해 주세요", Toast.LENGTH_SHORT).show()
                        }

                    }

                    5 -> {
                        if (viewModel.resultState5Array.size == 1) {
                            viewModel.state.value = 6
                        } else {
                            Toast.makeText(this, "직업을 1개 이상 선택해 주세요", Toast.LENGTH_SHORT).show()
                        }

                    }

                    6 -> {
                        if (viewModel.resultState6Array.size == 1) {
                            viewModel.state.value = 7
                        } else {
                            Toast.makeText(this, "지역을 1개 이상 선택해 주세요", Toast.LENGTH_SHORT).show()
                        }

                    }

                    7 -> {
                        if (viewModel.resultState7Array.value?.size == 1) {
                            val i = Intent(this, CarCheckActivity::class.java)
                            i.putExtra("nickname", viewModel.nickname.value)
                            i.putExtra("CharmList", viewModel.resultState2Array)
                            i.putExtra("musicTasteList", viewModel.resultState3Array)
                            i.putExtra("height", viewModel.resultState4Array)
                            i.putExtra("job", viewModel.resultState5Array)
                            i.putExtra("regin", viewModel.resultState6Array)
                            i.putExtra("driveStyle", viewModel.resultState7Array.value)
                            i.putExtra("u_name", u_name)
                            i.putExtra("u_gender", u_gender)
                            i.putExtra("u_birthday", u_birthday)
                            i.putExtra("u_phone", u_phone)
                            if (view.equals("퀵유저")) {
                                i.putExtra("view", "퀵유저")
                            }
                            Log.d("아이템2", u_gender)
                            startActivity(i)
                            //  overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                        } else {
                            Toast.makeText(this, "드라이브 스타일을 1개 이상 선택해 주세요", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }

                    else -> {}

                }
            }


        }
    }

    private fun setText(state: Int) {
        when (state) {
            0 -> {
                viewModel.mainText.value = "초대 코드"
//                viewModel.subText.value ="\"초대 코드를 입력해 주세요. \\n\" + \"초대 코드가 입력된 가입신청은 \\n\" + \"우선적으로 가입심사를 해 드립니다.\""
            }

            1 -> {
                viewModel.mainText.value = "매력을 선택해 주세요."
                viewModel.subText.value = "쵀대 3개까지 선택 가능합니다다."
            }
        }
    }

    override fun finish() {
        if (viewModel.view.value.equals("수정")) {

            super.finish()
        } else {
            if (viewModel.state.value!! > 1) {

                if (viewModel.state.value == 6) {
                    if (viewModel.seoulCheck.value == true || viewModel.gyeonggiCheck.value == true) {
                        viewModel.seoulCheck.value = false
                        viewModel.gyeonggiCheck.value = false

                    } else {
                        viewModel.state.value = viewModel.state.value!!.minus(1)
                    }

                } else {
                    viewModel.state.value = viewModel.state.value!!.minus(1)
                }
            } else {
                super.finish()
            }
        }


        // overridePendingTransition(com.supercarlounge.supercar.R.anim. fadein, com.supercarlounge.supercar.R.anim.fadeout)
    }

    fun JoinCheck(state: Int) {
        var barke = false

        for (i in application!!.joinCheckArray) {
            if (i == state) {
                barke = true
                Log.d("조인테스트1", "")
            }
        }
        if (!barke) {

            var step = when (state) {
                1 -> {
                    "닉네임 입력"
                }

                2 -> {
                    "매력 입력"
                }

                3 -> {
                    "음악유형 입력"
                }

                4 -> {
                    "키 입력"
                }

                5 -> {
                    "직업 입력"
                }

                6 -> {
                    "지역 입력"
                }

                else -> {
                    "드라이브 스타일 입력"
                }
            }
            Log.d("조인테스트2", step)
            application?.joinLog(this, step, u_name)
            application!!.joinCheckArray.add(state)
        }


    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val focusView = currentFocus
        if (focusView != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev.x.toInt()
            val y = ev.y.toInt()
            if (!rect.contains(x, y)) {
                val imm: InputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                if (imm != null) imm.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}