package com.supercarlounge.supercar.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.KeyEvent.ACTION_UP
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.viewmodel.NewDriveWritingViewModel
import kotlin.math.log


class CalenderMainAdapter(
    var viewModel:NewDriveWritingViewModel,
    private var context: Context,
    var calendarList: ArrayList<String>,
    m: String,
    y: String,
    val itemClick: (String, Int) -> Unit
) : BaseAdapter() {
    var select_position = 7
    var select_day = 0
    var month = m
    var year = y
    var click: Boolean = false
    override fun getCount(): Int {
        return calendarList.size
    }

    override fun getItem(p0: Int): Any {
        return calendarList[p0]

    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    @SuppressLint("ViewHolder", "InflateParams", "ResourceAsColor", "ClickableViewAccessibility")
    override fun getView(position: Int, convertVuew: View?, parent: ViewGroup?): View {

        val Date = calendarList[position]

        val calenderView = View.inflate(context,R.layout.calender_main_item, null)
        val tv = calenderView.findViewById<TextView>(R.id.tv_title)
        val item = calenderView.findViewById<LinearLayout>(R.id.item)

//        if (position < 7) {
//            tv.setTextColor(ContextCompat.getColor(context, R.color.main_color))
//        } else {

//        }
        var weekTextSize = context.resources.getDimension(R.dimen._4sdp)
        var normalTextSize = context.resources.getDimension(R.dimen._7sdp)
        if (position <7){
            tv.setTextColor(ContextCompat.getColor(context, R.color.text_gray_30))

            tv.textSize = weekTextSize
        }else if (position  % 7 == 0){
            tv.textSize = normalTextSize
            tv.setTextColor(ContextCompat.getColor(context, R.color.red2))
        }else{
            tv.textSize = normalTextSize
            tv.setTextColor(ContextCompat.getColor(context, R.color.white))
        }
        val result: String = calendarList[position].replace(" ", "")
        if (viewModel.selectDay.value == result) {
            item.background =ContextCompat.getDrawable(context, R.drawable.round_select)
        }


        item.setOnClickListener { itemClick(calendarList[position], position) }
        if (position  in 7..viewModel.start.value!! ){
            item.visibility =View.INVISIBLE
        }
        if (position  in viewModel.end.value!! until calendarList.size){
            item.visibility =View.INVISIBLE
        }

        tv.text = calendarList[position]

        return calenderView

    }


    fun setSelectPosition(position: Int) {
        select_position = position
        notifyDataSetChanged()
    }

    fun getData(): String {
        return calendarList[select_position]
    }

    fun setAddAllList(list: ArrayList<String>) {
        Log.d("setAddAllList :", list.size.toString())
        calendarList.clear()
        calendarList.addAll(list)
        notifyDataSetChanged()
    }

    fun setDayPosition(date: Int) {
        //디폴트 전달 나머지 일
        var day = date
        //디폴트 요일
        var daySum = day + 7
        select_day = day
        notifyDataSetChanged()
    }

    @JvmName("setMonth1")
    fun setMonth(m: String) {
        month = m
    }

    @JvmName("setYear1")
    fun setYear(y: String) {
        year = y
    }


    fun getSelectPosition(): Int {
        return select_position
    }


}