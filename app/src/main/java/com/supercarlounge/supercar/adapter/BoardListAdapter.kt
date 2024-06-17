package com.supercarlounge.supercar.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.supercarlounge.supercar.BaseDiffUtil
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.BoardData
import com.supercarlounge.supercar.databinding.ItemAnonymousForumPostBinding
import com.supercarlounge.supercar.enumset.TimeValue
import com.supercarlounge.supercar.viewmodel.BoardViewModel
import java.text.ParseException
import java.text.SimpleDateFormat


class BoardListAdapter(val con: Context, val data: ArrayList<BoardData>, val model: BoardViewModel, val itemClick: (BoardData, String, Int) -> Unit) : RecyclerView.Adapter<BoardListAdapter.ViewHolder>() {
    var selectindex = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }


    override fun getItemCount(): Int {
        return data!!.size
    }

    fun setdata(datalist:ArrayList<BoardData>){
        val diffUtil = BaseDiffUtil(data, datalist)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        data.clear()
        data.addAll(datalist)
        notifyDataSetChanged()
//        diffResult.dispatchUpdatesTo(this)
//        notifyDataSetChanged()
    }


    fun adddata(datalist:ArrayList<BoardData>){
        val diffUtil = BaseDiffUtil(data, datalist)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        data.addAll(datalist)
//        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }
    fun setselect(index:Int){
        selectindex=index
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val metrisc: DisplayMetrics = con.resources.displayMetrics
        val displayPW = metrisc.widthPixels*3/10
        var width = holder.itemView.width
        holder.bind(data.get(position), position,displayPW)
    }

    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_anonymous_frum_post, parent, false)
    ) {
        private val binding: ItemAnonymousForumPostBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: BoardData, position: Int, width:Int) {
            binding!!.tvBContents.maxWidth = width
            binding?.dataset = data
            binding?.viewModel = model
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner
            if (data.b_date != null){
                binding.tvDate.text =setTime(dateTimeToMillSec(data.b_date))
            }

            itemView.setOnClickListener { itemClick(data, binding.tvDate.text.toString(), position) }
            binding.tvViews.text = data.views.toString()
            binding.tvComments.text = data.b_comment.toString()
            binding.tvBContents.text = data.b_contents


            var requestOptions = RequestOptions()
            requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(16))
            Glide.with(binding!!.ivMain).load(data!!.b_cover).override(150, 150) .apply(requestOptions).into(binding!!.ivMain)

            if (data!!.u_gender.equals("MALE")){
                Glide.with(binding!!.ivSex).load(R.drawable.ic_man_12x12).override(50, 50).centerInside().into(binding!!.ivSex)
            }else{
                Glide.with(binding!!.ivSex).load(R.drawable.ic_woman_12x12).override(50, 50).centerInside().into(binding!!.ivSex)
            }

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
    companion object {
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:AnonymousForumAllList")
        @JvmStatic
        fun setItems(recyclerView: RecyclerView, items: ArrayList<BoardData>) {
            if (recyclerView.adapter != null) {
                var adapter = (recyclerView.adapter as BoardListAdapter)
                items?.let {
                    adapter.setdata(it)

                }
            }
        }
    }


}