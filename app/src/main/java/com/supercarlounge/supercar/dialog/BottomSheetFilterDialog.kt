package com.supercarlounge.supercar.dialog


import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnShowListener
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.iterator
import androidx.core.view.size
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.slider.RangeSlider
import com.supercarlounge.supercar.BottomDialogEvent
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.customview.CustomTag
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.BottomSheetRegionDailogViewModel
import java.text.DecimalFormat


class BottomSheetFilterDialog(
    context: Context,
    var type1: String,
    var type2: ArrayList<String>,
    var type3: String,
    var priceMin: Int,
    var priceMax: Int,
    val event: (Boolean, String, ArrayList<String>, String, Int, Int,Boolean) -> Unit
) : BottomSheetDialogFragment() {
    var mListener: BottomDialogEvent? = null
    var viewmodel: BottomSheetRegionDailogViewModel? = null
    var binding: com.supercarlounge.supercar.databinding.DialogBottomSheetFilterBinding? = null
    var locationPermissionDialog: LocationPermissionDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_bottom_sheet_filter,
            null,
            false
        );


//        dialog!!.setOnShowListener { dialog ->
//            val d = dialog as BottomSheetFilterDialog
//            val bottomSheetInternal: View =
//                d.dialog!!.findViewById(android.support.design.R.id.design_bottom_sheet);
//            BottomSheetBehavior.from(bottomSheetInternal).state =
//                BottomSheetBehavior.STATE_EXPANDED
//        }


        (this.dialog as BottomSheetDialog).behavior.state = BottomSheetBehavior.STATE_EXPANDED




        return binding!!.root

    }

    //    private fun setupFullHeight() {
//
//        val bottomSheetDialog = dialog as BottomSheetDialog
//        val behavior = bottomSheetDialog.behavior
//        behavior.state = BottomSheetBehavior.STATE_EXPANDED
//    }
    override fun onResume() {
        super.onResume()
//        setDialogWindowSize()
    }

    //다이얼로그 사이즈 초기화
    private fun setDialogWindowSize() {

        val metrisc: DisplayMetrics = resources.displayMetrics
        val displayPW = metrisc.widthPixels
        val displayPH = metrisc.heightPixels
        val window = dialog!!.window
        window!!.setLayout(displayPW, displayPH)
        var x = 0
        var y = 0

        x = (displayPW * 1f).toInt()
        y = (displayPH * 0.8f).toInt()

        var params = FrameLayout.LayoutParams(x, y)
        params.gravity = Gravity.BOTTOM
        dialog!!.window!!.setLayout(x, y)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        viewmodel = ViewModelProvider(this).get(BottomSheetRegionDailogViewModel::class.java)
        binding!!.viewModel = viewmodel
        binding!!.setLifecycleOwner(this)
        binding!!.lifecycleOwner = this.viewLifecycleOwner


        observers()


//        setListaddView(0)
//        setListaddView(1)
//        setListaddView(2)

        Log.d("필터데이터받아온", "$type1\n  $type2\n  $type3\n  $priceMin\n  $priceMax\n ")

        getlistPosition(type1, 0)
        getlistPosition2(type2)
        getlistPosition(type3, 2)
        viewmodel?.priceMin?.value = priceMin
        viewmodel?.priceMax?.value = priceMax
        setslider()


    }

    private fun setListaddView(index: Int) {
        when (index) {
            0 -> {
                for ((count, i) in viewmodel?.sortBooleanArray?.value!!.withIndex()) {
                    var text = ""
                    text = viewmodel?.sortArray?.value!![count]
                    setEditAddView(text, false, i)


                }
            }

            1 -> {
                for ((count, i) in viewmodel?.categoryBooleanArray?.value!!.withIndex()) {

                    var text = ""
                    text = viewmodel?.categoryArray?.value!![count]
                    setEditAddView(text, true, i)


                }
            }

            2 -> {
                for ((count, i) in viewmodel?.category2BooleanArray?.value!!.withIndex()) {
                    var text = ""

                    text = viewmodel?.category2Array?.value!![count]
                    if (i) {
                        when (text) {
                            "만원 미만" -> {
                                binding?.slider?.values = listOf(0f, 1f)

                            }

                            "최대 10만원" -> {
                                binding?.slider?.values = listOf(0f, 10f)

                            }

                            "가격대 상관없음" -> {
                                binding?.slider?.values = listOf(0f, 20f)
                            }
                        }
                    }

                    setEditAddView(text, false, i)


                }
            }
        }

    }

    private fun setEditAddView(text: String, boolean: Boolean, checked: Boolean) {


        val myView = CustomTag(requireContext(), viewmodel!!)
        // LinearLayout에 textView 추가

        if (checked) {
            var handler = Handler(Looper.getMainLooper())
            var b = true
            if (viewmodel?.tagArray?.value?.isNotEmpty() == true) {
                for ((position, i) in viewmodel?.tagArray?.value!!.withIndex()) {

                    if (i == text) {
                        b = false
                    }
                    when (text) {
                        "추천순" -> {
                            if (viewmodel?.tagArray?.value!!.contains("최신순") && i == "최신순") {
                                removePosition(position)
                                break
                            } else if (viewmodel?.tagArray?.value!!.contains("인기순") && i == "인기순") {
                                removePosition(position)
                                break
                            }


                        }

                        "최신순" -> {
                            if (viewmodel?.tagArray?.value!!.contains("인기순") && i == "인기순") {
                                removePosition(position)
                                break
                            } else if (viewmodel?.tagArray?.value!!.contains("추천순") && i == "추천순") {
                                removePosition(position)
                                break
                            }

                        }

                        "인기순" -> {
                            if (viewmodel?.tagArray?.value!!.contains("최신순") && i == "최신순") {
                                removePosition(position)
                                break
                            } else if (viewmodel?.tagArray?.value!!.contains("추천순") && i == "추천순") {
                                removePosition(position)
                                break
                            }

                        }

                        "만원 미만" -> {
                            if (viewmodel?.tagArray?.value!!.contains("최대 10만원") && i == "최대 10만원") {
                                removePosition(position)
                                break
                            } else if (viewmodel?.tagArray?.value!!.contains("가격대 상관없음") && i == "가격대 상관없음") {
                                removePosition(position)
                                break
                            } else if (viewmodel?.tagArray?.value!!.contains("무료 ~ 20,000원") && i == "무료 ~ 20,000원") {
                                removePosition(position)
                                break

                            } else if (i.length > 8) {
                                removePosition(position)
                                break

                            }

                        }

                        "최대 10만원" -> {
                            if (viewmodel?.tagArray?.value!!.contains("만원 미만") && i == "만원 미만") {
                                removePosition(position)
                                break
                            } else if (viewmodel?.tagArray?.value!!.contains("가격대 상관없음") && i == "가격대 상관없음") {
                                removePosition(position)
                                break
                            } else if (viewmodel?.tagArray?.value!!.contains("무료 ~ 20,000원") && i == "무료 ~ 20,000원") {
                                removePosition(position)
                                break

                            } else if (i.length > 8) {
                                removePosition(position)
                                break

                            }

                        }

                        "가격대 상관없음" -> {
                            if (viewmodel?.tagArray?.value!!.contains("만원 미만") && i == "만원 미만") {
                                removePosition(position)
                                break

                            } else if (viewmodel?.tagArray?.value!!.contains("최대 10만원") && i == "최대 10만원") {
                                removePosition(position)
                                break

                            } else if (viewmodel?.tagArray?.value!!.contains("무료 ~ 20,000원") && i == "무료 ~ 20,000원") {
                                removePosition(position)
                                break

                            } else if (i.length > 8) {
                                removePosition(position)
                                break

                            }

                        }

                        "북마크" -> {
                            if (viewmodel?.tagArray?.value!!.contains("북마크") && i == "북마크") {
                                b = false
                                break

                            }

                        }

                        "심야" -> {
                            if (viewmodel?.tagArray?.value!!.contains("심야") && i == "심야") {
                                b = false
                                break

                            }

                        }

                        "슈퍼카 주차 가능" -> {
                            if (viewmodel?.tagArray?.value!!.contains("슈퍼카 주차 가능") && i == "슈퍼카 주차 가능") {
                                b = false
                                break

                            }

                        }

                        else -> {
                            if (text.length > 8) {
                                if (viewmodel?.tagArray?.value!!.contains("만원 미만") && i == "만원 미만") {
                                    removePosition(position)
                                    break

                                } else if (viewmodel?.tagArray?.value!!.contains("최대 10만원") && i == "최대 10만원") {
                                    removePosition(position)
                                    break

                                } else if (viewmodel?.tagArray?.value!!.contains("가격대 상관없음") && i == "가격대 상관없음") {
                                    removePosition(position)
                                    break

                                } else if (viewmodel?.tagArray?.value!!.contains("무료 ~ 0원") && i == "무료 ~ 0원") {
                                    removePosition(position)
                                    break

                                } else if (i.length > 8 && i != text) {
                                    removePosition(position)
                                    break
                                }
                            }

                        }
                    }


                }
            }

            if (b) {

                var runnable = Runnable {
                    run {
                        binding?.llAddView?.addView(myView)
                        myView.setText(text);
                        myView.setVisible(boolean)
                        viewmodel?.tagArray?.value?.add(text)
                        viewmodel?.tagArray?.value = viewmodel?.tagArray?.value
                    }
                }
                handler.postDelayed(runnable, 50)
            }


        } else {
            var position = 0
            for ((count, i) in viewmodel?.tagArray?.value!!.withIndex()) {
                if (i == text) {

                    position = count
                    binding?.llAddView?.removeViewAt(position)
                    viewmodel?.tagArray?.value!!.removeAt(position)
                    viewmodel?.tagArray?.value = viewmodel?.tagArray?.value!!
                    break
                }
            }


        }

        var ivclose = myView.binding?.close

        var position = 0
        ivclose?.setOnClickListener {
            position = binding?.llAddView?.indexOfChild(myView)!!
            binding?.llAddView?.removeViewAt(position)

            var deleteText = viewmodel?.tagArray?.value!![position]
            viewmodel?.tagArray?.value!!.removeAt(position)
            viewmodel?.tagArray?.value = viewmodel?.tagArray?.value!!

            for ((count, i) in viewmodel?.categoryArray?.value!!.withIndex()) {
                if (i == deleteText) {
                    viewmodel?.categoryBooleanArray?.value?.set(count, false)
                    viewmodel?.categoryBooleanArray?.value = viewmodel?.categoryBooleanArray?.value
                    break

                }
            }

        }


    }

    private fun removePosition(position: Int) {
        binding?.llAddView?.removeViewAt(position)
        viewmodel?.tagArray?.value!!.removeAt(position)
        viewmodel?.tagArray?.value = viewmodel?.tagArray?.value!!
    }

    private fun observers() {
        viewmodel?.okEvent?.observe(viewLifecycleOwner, Observer
        {
            if (it) {
                var filterOn = false
                for (i in viewmodel?.categoryBooleanArray?.value!!){
                    if (i){
                        filterOn = true
                        Log.d("필터1", filterOn.toString())
                        break
                    }
                }
                if (!filterOn){
                    for ((position, i) in viewmodel?.sortBooleanArray?.value!!.withIndex()){
                        if (position == 0 && i){
                            filterOn = true
                            Log.d("필터2", filterOn.toString())
                            break
                        }
                        if (position == 2 && i){
                            filterOn = true
                            Log.d("필터3", filterOn.toString())
                            break
                        }
                    }
                }
                if (!filterOn){
                    for ((position, i) in viewmodel?.category2BooleanArray?.value!!.withIndex()){
                        if (position == 0 && i){
                            filterOn = true
                            break
                        }
                        if (position == 1 && i){
                            filterOn = true
                            break
                        }

                    }
                }

                if (!filterOn){
                    if (viewmodel?.priceRange?.value != "가격대 상관없음"){
                        filterOn = true
                    }
                }
                event(
                    true,
                    getlistItem(viewmodel?.sortBooleanArray?.value!!, 0),
                    getlistItem2(viewmodel?.categoryBooleanArray?.value!!),
                    getlistItem(viewmodel?.category2BooleanArray?.value!!, 2),
                    viewmodel?.priceMin?.value!!,
                    viewmodel?.priceMax?.value!!,
                            filterOn
                )
                dismiss()
            }
        })

        viewmodel?.okReset?.observe(viewLifecycleOwner, Observer {
            if (it) {
//                if (binding?.llAddView?.size != 0) {
//                    for (i in binding?.llAddView!!) {
//                            binding?.llAddView?.removeViewAt(0)
//                    }
//
//                }
                viewmodel?.categoryBooleanArray?.value?.set(0,false)
                viewmodel?.categoryBooleanArray?.value?.set(1,  false)
                viewmodel?.categoryBooleanArray?.value?.set(2,  false)
                viewmodel?.category2BooleanArray?.value?.set(0,  false)
                viewmodel?.category2BooleanArray?.value?.set(1,  false)
                viewmodel?.category2BooleanArray?.value?.set(2,  true)
                viewmodel?.categoryBooleanArray?.value =viewmodel?.categoryBooleanArray?.value
                viewmodel?.category2BooleanArray?.value =viewmodel?.category2BooleanArray?.value
                viewmodel?.sortEvent(1)

            }
        })

        viewmodel?.sortBooleanArray?.observe(viewLifecycleOwner, Observer {

            setListaddView(0)


        })
        viewmodel?.categoryBooleanArray?.observe(viewLifecycleOwner, Observer {

            setListaddView(1)


        })
        viewmodel?.category2BooleanArray?.observe(viewLifecycleOwner, Observer {

            setListaddView(2)


        })


        viewmodel?.priceMin?.observe(viewLifecycleOwner, Observer {
            var mintv = "무료"
            var maxtv = ""
            val df = DecimalFormat("###,###")
            val minMoney: String = df.format((it * 10000))
            val maxMoney: String = df.format((viewmodel?.priceMax?.value!! * 10000))
            if (it > 0) {
                mintv = minMoney
            }
            maxtv = maxMoney


            if (viewmodel?.priceMax?.value == 20) {
                viewmodel?.priceRange?.value = "가격대 상관없음"
            } else {
                viewmodel?.priceRange?.value = "$mintv ~ ${maxtv}원"
            }


        })
        viewmodel?.priceMax?.observe(viewLifecycleOwner, Observer {
            var mintv = "무료"
            var maxtv = ""
            val df = DecimalFormat("###,###")
            val minMoney: String = df.format((viewmodel?.priceMin?.value!! * 10000))
            val maxMoney: String = df.format((it * 10000))
            if (viewmodel?.priceMin?.value!! > 0) {
                mintv = minMoney
            }
            maxtv = maxMoney

            if (viewmodel?.priceMax?.value == 20 && viewmodel?.priceMin?.value == 0) {
                viewmodel?.priceRange?.value = "가격대 상관없음"
            } else {
                viewmodel?.priceRange?.value = "$mintv ~ ${maxtv}원"
            }

        })

        viewmodel?.priceRange?.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {

            }
        })
    }

    private fun getlistItem(list: ArrayList<Boolean>, type: Int): String {
        var result = ""
        when (type) {
            0 -> {
                for ((count, i) in list.withIndex()) {
                    if (i) {
                        result = viewmodel?.sortArray?.value!![count]
                    }
                }
            }

            2 -> {
                for ((count, i) in list.withIndex()) {
                    if (i) {
                        result = viewmodel?.category2Array?.value!![count]
                    }
                }
            }
        }

        return result
    }

    private fun getlistItem2(list: ArrayList<Boolean>): ArrayList<String> {
        var resultList: ArrayList<String> = arrayListOf()


        for ((count, i) in list.withIndex()) {
            if (i) {
                resultList.add(viewmodel?.categoryArray?.value!![count])
            }
        }
        Log.d("리스트 개수1", list.toString())
        Log.d("리스트 개수2", resultList.toString())


        return resultList
    }

    private fun getlistPosition(s: String, type: Int) {
        when (type) {
            0 -> {
                for ((count, i) in viewmodel?.sortArray?.value!!.withIndex()) {
                    if (i == s) {
                        viewmodel?.sortEvent(count)
                    }
                }
            }

            2 -> {
                for ((count, i) in viewmodel?.category2Array?.value!!.withIndex()) {
                    if (i == s) {

                        viewmodel?.category2Event(count)
                    }
                }

            }
        }

    }

    private fun getlistPosition2(list: ArrayList<String>) {
        if (list.size != 0) {
            for ((count, i) in viewmodel?.categoryArray?.value!!.withIndex()) {
                for (j in list)
                    if (i == j) {
                        viewmodel?.categoryEvent(count)
                        break
                    }
            }
        }


    }

    private fun setslider() {
        binding?.slider?.valueFrom = 0f
        binding?.slider?.valueTo = 20f
        binding?.slider?.stepSize = 1f
        binding?.slider?.values =
            listOf(viewmodel?.priceMin?.value?.toFloat()!!, viewmodel?.priceMax?.value?.toFloat()!!)
        binding?.slider?.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
                viewmodel?.priceMin?.value = slider.values[0].toInt()
                viewmodel?.priceMax?.value = slider.values[1].toInt()
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {

                if (viewmodel?.priceMax?.value == 10 && viewmodel?.priceMin?.value == 0) {
                    viewmodel?.category2Event(1)
//                    setEditAddView("최대 10만원", false, true)
                } else if (viewmodel?.priceMax?.value == 1 && viewmodel?.priceMin?.value == 0) {
                    viewmodel?.category2Event(0)
//                    setEditAddView("만원 이하", false, true)
                } else if (viewmodel?.priceMax?.value == 20 && viewmodel?.priceMin?.value == 0) {
                    viewmodel?.category2Event(2)
                } else {
                    viewmodel?.category2BooleanArray?.value!!.set(0, false)
                    viewmodel?.category2BooleanArray?.value!!.set(1, false)
                    viewmodel?.category2BooleanArray?.value!!.set(2, false)
                    viewmodel?.category2BooleanArray!!.value =
                        viewmodel?.category2BooleanArray!!.value
                    setEditAddView(viewmodel?.priceRange?.value.toString(), false, true)
                }

            }


        })
        binding?.slider?.addOnChangeListener { slider, value, formUser ->
            val values = slider.values
            var num1 = values[0].toString()
            var num2 = values[1].toString()
            viewmodel?.priceMin?.value = slider.values[0].toInt()
            viewmodel?.priceMax?.value = slider.values[1].toInt()
        }
    }


    override fun onDismiss(dialog: DialogInterface) {
        if (viewmodel?.regionIndex?.value == 2) {


        }
        super.onDismiss(dialog)

    }

    override fun onDestroy() {
        super.onDestroy()


    }
}