package com.supercarlounge.supercar.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.Constans
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.MyCarBoardCommentData
import com.supercarlounge.supercar.data.MyCarBoardSubCommentData
import com.supercarlounge.supercar.data.SubComment
import com.supercarlounge.supercar.databinding.ItemCommentSubBinding
import com.supercarlounge.supercar.databinding.ItemMyCarBoardCommentSubBinding
import com.supercarlounge.supercar.enumset.TimeValue
import com.supercarlounge.supercar.viewmodel.MyCarBoardPostViewModel
import com.supercarlounge.supercar.viewmodel.PostViewModel
import java.text.ParseException
import java.text.SimpleDateFormat


class MyCarBoardCommentSubAdapter(val con: Context, val data: ArrayList<MyCarBoardSubCommentData>, val model: MyCarBoardPostViewModel, val itemClick: (MyCarBoardSubCommentData, Int, View, Int) -> Unit) : RecyclerView.Adapter<MyCarBoardCommentSubAdapter.ViewHolder>() {
    var selectindex = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }


    override fun getItemCount(): Int {
        return data!!.size
    }

    fun setdata(datalist:ArrayList<MyCarBoardSubCommentData>){
        data.clear()
        data.addAll(datalist)
        notifyDataSetChanged()
    }
    fun setselect(index:Int){
        selectindex=index
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data.get(position), position)
    }

    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_my_car_board_comment_sub, parent, false)
    ) {
        private val binding: ItemMyCarBoardCommentSubBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: MyCarBoardSubCommentData, position: Int) {
            binding?.dataset = data
            binding?.viewModel = model
            binding?.posi = position
            binding!!.executePendingBindings()

            binding.lifecycleOwner = con as LifecycleOwner

            binding!!.tvHashTag.text = "@"+data.p_nickname
//            binding!!.tvLike.text = data.s_like.toString()

//            if ( data.like_yn.equals("Y")){
                Glide.with(binding!!.ivHeart).load(R.drawable.ic_cell_4).centerInside().into(binding!!.ivHeart)
//            }else{
//                Glide.with(binding!!.ivHeart).load(R.drawable.sendheart_01_24x24).centerInside().into(binding!!.ivHeart)
//            }
            if (data.u_gender.equals("MALE")){
                Glide.with(binding!!.ivSex).load(R.drawable.ic_man_12x12).centerInside().into(binding!!.ivSex)
            }else{
                Glide.with(binding!!.ivSex).load(R.drawable.ic_woman_12x12).centerInside().into(binding!!.ivSex)
            }
            itemView.setOnClickListener { itemClick(data, position ,binding.tvNickname,0) }
            binding!!.tvNickname.setOnClickListener { itemClick(data, position ,binding.tvNickname,Constans.NICKNAME) }
            binding!!.llHeart.setOnClickListener { itemClick(data, position,binding.tvNickname ,Constans.HEART) }
//            Glide.with(binding!!.itemIv).load(data!!.src).centerInside().into(binding!!.)
        }
    }
    @SuppressLint("SimpleDateFormat")
    fun dateTimeToMillSec(dateTime: String): Long{
        Log.d("변환전 시간", dateTime)

        val curTime = System.currentTimeMillis()
        Log.d("현재 시간", curTime.toString())
        val target = "T"
        var result = dateTime.replace(target," ") // '-'를 공백으로 바꿔준다.
        var timeInMilliseconds: Long = 0
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        try {
            val mDate = sdf.parse(result)

            timeInMilliseconds = mDate.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        Log.d("변환된 시간", timeInMilliseconds.toString())
        return timeInMilliseconds
    }
    fun setTime(time : Long) :String{
        val curTime = System.currentTimeMillis()
        Log.d("현재 시간", curTime.toString())
        var diffTime = (curTime- time) / 1000
        var msg: String? = null
        if(diffTime < TimeValue.SEC.value ) {
            msg = "방금 전"
            return msg.toString()
        }else {
            for (i in TimeValue.values()) {
                diffTime = diffTime.div(i.value)


                if (diffTime > 14 && i == TimeValue.HOUR ||i > TimeValue.HOUR){
                    msg = "지난글"
                    return msg.toString()
                }
                if (diffTime < i.maximum) {
                    msg=i.msg
                    break
                }
            }
        }
        return diffTime.toString() + msg.toString()
    }



}