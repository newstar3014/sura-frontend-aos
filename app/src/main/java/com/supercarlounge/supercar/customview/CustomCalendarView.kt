package com.supercarlounge.supercar.customview

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.SkuDetails
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.databinding.CustomCalendarBinding
import com.supercarlounge.supercar.databinding.ItemCustomCalendarBinding
import java.util.Calendar

class CustomCalendarView : LinearLayout {
    var nowyear = 0
    var nowmonth = 0
    var nowdate = 0
    var adapter:CustomCalendarAdapter? = null
    var selectcallback:SelectDateCallback? =null
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.binding = CustomCalendarBinding.inflate(LayoutInflater.from(context), this, true)
        initattrs(attrs)
        initview()
    }

    private  var binding:CustomCalendarBinding
    fun initattrs(attrs:AttributeSet){
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomCalendarView)
        nowyear = typedArray.getInt(R.styleable.CustomCalendarView_nowyear, 2023)
        nowmonth = typedArray.getInt(R.styleable.CustomCalendarView_nowmonth, 1)
        nowdate = typedArray.getInt(R.styleable.CustomCalendarView_nowdate, 1)
        setTypeArray(typedArray)
    }
    private fun setTypeArray(typedArray: TypedArray) {
        typedArray.recycle()
    }
    fun initview(){
        binding!!.tvMonth.text = nowyear.toString()+"년 "+(nowmonth+1).toString()+"월"
        var adapter =  CustomCalendarAdapter(context,nowyear,nowmonth,nowdate){ year, month,date ->
            if (selectcallback != null){
                selectcallback!!.SetDate(year,month,date)
            }

        }
        binding!!.rvCal.adapter = adapter
        binding!!.rvCal.layoutManager = GridLayoutManager(context,7)
        binding!!.btnPrv.setOnClickListener {

            adapter.settingcalendar(false)
            var nowyear = adapter.nowyear
            var nowmonth = adapter.nowmonth
            binding!!.tvMonth.text = nowyear.toString()+"년 "+(nowmonth+1).toString()+"월"
        }
        binding!!.btnNext.setOnClickListener {
            adapter.settingcalendar(true)
            var nowyear = adapter.nowyear
            var nowmonth = adapter.nowmonth
            binding!!.tvMonth.text = nowyear.toString()+"년 "+(nowmonth+1).toString()+"월"
        }
    }
    fun getselyear():Int{
        return adapter!!.selectyear
    }
    fun getselmonth():Int{
        return adapter!!.selectmonth
    }
    fun getseldate():Int{
        return adapter!!.selectdate
    }
    fun setcallback(call:SelectDateCallback){
        selectcallback = call
    }
    inner class CustomCalendarAdapter(val con: Context,var nowyear:Int,var nowmonth:Int,var nowdate:Int, val itemClick: (Int, Int,Int) -> Unit) : RecyclerView.Adapter<CustomCalendarAdapter.ViewHolder>() {
        var selectyear = 0
        var selectmonth = 0
        var selectdate = 0
        private val daysOfWeek = arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        private val calendar = Calendar.getInstance()
        init {
            calendar.set(Calendar.MONTH, nowmonth)
            calendar.set(Calendar.YEAR, nowyear)
            calendar.set(Calendar.DAY_OF_MONTH, nowdate)
            selectmonth = nowmonth
            selectdate = nowdate
            selectyear = nowyear
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

            return ViewHolder(parent)
        }

        override fun getItemCount(): Int {
            return  49
        }


        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val startdate = getStartDayOfWeek(nowyear,nowmonth)-1

            val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            var offset = position-(6+startdate)
            var title = ""
            if (position <7){
                title = daysOfWeek.get(position)
            }else if ((offset <= daysInMonth) && offset > 0){
                title = offset.toString()
            }
            var isset = selectmonth == nowmonth && selectyear == nowyear && selectdate == offset
            holder.bind(title, position,isset)
            var month = calendar.get(Calendar.MONTH)
            var year = calendar.get(Calendar.YEAR)
        }
        fun settingcalendar(isplus: Boolean){
            if (isplus == true) {
                calendar.add(Calendar.MONTH, 1)

            }else{
                calendar.add(Calendar.MONTH,-1)
            }
            nowmonth = calendar.get(Calendar.MONTH)
            nowyear = calendar.get(Calendar.YEAR)
            nowdate = calendar.get(Calendar.DAY_OF_MONTH)
            notifyDataSetChanged()
        }
        fun setcalendar (date:Int){
            calendar.set(Calendar.MONTH, nowmonth)
            calendar.set(Calendar.YEAR, nowyear)
            calendar.set(Calendar.DAY_OF_MONTH,date)
            selectyear = nowyear
            selectmonth = nowmonth
            val prv_date = selectdate
            selectdate = date
            val startdate = getStartDayOfWeek(nowyear,nowmonth)-1
            notifyItemChanged(prv_date+startdate+6)
            notifyItemChanged(date+startdate+6)
        }
        fun getCurrentDate(): Int {
            val calendar = Calendar.getInstance()
            return calendar.get(Calendar.DAY_OF_MONTH)
        }
        fun getStartDayOfWeek(year: Int, month: Int): Int {
            val calendar = Calendar.getInstance()
            calendar.set(year, month, 1)
            var startdate =  calendar.get(Calendar.DAY_OF_WEEK)
            return startdate
        }
        inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_custom_calendar, parent, false)
        ) {
            private val binding: ItemCustomCalendarBinding? = DataBindingUtil.bind(itemView)

            fun bind(date:String,position: Int,isset:Boolean) {
                binding!!.istitle = position <7
                binding!!.date = date
                binding!!.issun = (position %7 == 0) && position >6
                binding!!.isset = isset
                if (date.isNullOrEmpty() == false&& position >=7) {
                    itemView.setOnClickListener {
                        setcalendar(date.toInt())
                        itemClick(nowyear,nowmonth,date.toInt())
                    }
                }
            }
        }
    }
    interface SelectDateCallback{
        fun SetDate(year: Int,month: Int,date: Int)
    }
}