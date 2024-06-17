package com.supercarlounge.supercar.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.BaseDiffUtil
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.MyCarBoardData
import com.supercarlounge.supercar.databinding.ItemMyCarBoardGridBinding
import com.supercarlounge.supercar.enumset.TimeValue
import com.supercarlounge.supercar.viewmodel.MyCarBoardViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import com.supercarlounge.supercar.SpannedGridLayoutManager
import com.supercarlounge.supercar.data.SampleImageData
import com.supercarlounge.supercar.databinding.ItemImageGridBinding
import com.supercarlounge.supercar.viewmodel.ImageViewModel


class ImageAdapter(val con: Context, var data: ArrayList<SampleImageData>, val model: ImageViewModel, val itemClick: (SampleImageData, String, Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var selectindex = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return  gridViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setdata(datalist:ArrayList<SampleImageData>){
        val diffUtil = BaseDiffUtil(data, datalist)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        data.clear()
        data.addAll(datalist)
        diffResult.dispatchUpdatesTo(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun adddata(datalist:ArrayList<SampleImageData>){
        data.addAll(datalist)
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setselect(index:Int){
        selectindex=index
        notifyDataSetChanged()
    }

    fun setChangeData(mdata:SampleImageData,position: Int){
        data[position] = mdata
        notifyDataSetChanged()
    }
    fun cleardata(){
        data.clear()
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                (holder as gridViewHolder).bind(data[position], position)

    }


    inner class gridViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_image_grid, parent, false)
    ) {
        private val binding: ItemImageGridBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: SampleImageData, position: Int) {
            binding?.dataset = data
            binding?.viewModel = model
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner
            val lp: ViewGroup.LayoutParams = itemView.layoutParams
            val sglp = lp as SpannedGridLayoutManager.LayoutParams
            var itemWidth = model.rvWidth.value!!.div(3)
            var itemWidth0 = itemWidth *2
            var f = con.resources.getDimension(R.dimen._1sdp)

                    sglp.width =(itemWidth -f).toInt()
                    sglp.height =(itemWidth -f).toInt()

            itemView.layoutParams =sglp




            itemView.setOnClickListener { itemClick(data, "기본",position) }

        }
    }


    companion object {
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:imagelist")
        @JvmStatic
        fun setmycarboard(recyclerView: RecyclerView, items: ArrayList<SampleImageData> ) {
            if (recyclerView.adapter != null) {
                var adapter = (recyclerView.adapter as ImageAdapter)
                items?.let {

                    adapter.data = it
                    adapter.notifyDataSetChanged()

                }
            }
        }


        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("mainPhoto")
        fun setphoto(view: ImageView, url: String?) {
            // This methods should not have any return type, = declaration would make it return that object declaration.
            if (!url.isNullOrEmpty()) Glide.with(view).load(url).override(view.width, view.height).centerInside().into(view)
        }

        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("mainPhotoGrid")
        fun setphotoGrid(view: ImageView, url: String?) {
            // This methods should not have any return type, = declaration would make it return that object declaration.
            if (!url.isNullOrEmpty()) Glide.with(view).load(url).override(view.width, view.height).centerInside().into(view)
        }

        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("mainBrand")
        fun setBrand(view: ImageView, url: String?) {
            // This methods should not have any return type, = declaration would make it return that object declaration.
            if (!url.isNullOrEmpty()) Glide.with(view).load(url).override(view.width, view.height).into(view)
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