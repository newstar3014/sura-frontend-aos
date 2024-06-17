package com.supercarlounge.supercar.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
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
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.supercarlounge.supercar.BaseDiffUtil
import com.supercarlounge.supercar.Constans.Companion.GRID_ADAPTER
import com.supercarlounge.supercar.Constans.Companion.LINEAR_ADAPTER
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.DriveAwayData
import com.supercarlounge.supercar.data.MyCarBoardData
import com.supercarlounge.supercar.enumset.TimeValue
import com.supercarlounge.supercar.viewmodel.DriveDetailViewModel
import com.supercarlounge.supercar.viewmodel.MyCarBoardViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.ParseException
import java.text.SimpleDateFormat
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.supercarlounge.supercar.databinding.*
import com.supercarlounge.supercar.viewmodel.MyCarBestBoardViewModel


class MyCarBestBoardAdapter(val con: Context, var data: ArrayList<MyCarBoardData>, val model: MyCarBestBoardViewModel, val itemClick: (MyCarBoardData, String, Int) -> Unit) : RecyclerView.Adapter<MyCarBestBoardAdapter.ViewHolder>() {
    var selectindex = 0


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data.get(position), position)
    }

    override fun getItemCount(): Int {
        return data!!.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setdata(datalist:ArrayList<MyCarBoardData>){
        val diffUtil = BaseDiffUtil(data, datalist)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        data.clear()
        data.addAll(datalist)
        diffResult.dispatchUpdatesTo(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun adddata(datalist:ArrayList<MyCarBoardData>){
        data.addAll(datalist)
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setselect(index:Int){
        selectindex=index
        notifyDataSetChanged()
    }

    fun setChangeData(mdata:MyCarBoardData,position: Int){
        data[position] = mdata
//        notifyItemChanged(position)
        notifyDataSetChanged()
    }
    fun cleardata(){
        data.clear()
        notifyDataSetChanged()
    }




    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_my_car_best_board, parent, false)
    ) {
        private val binding: ItemMyCarBestBoardBinding? = DataBindingUtil.bind(itemView)

        @SuppressLint("SetTextI18n")
        fun bind(data: MyCarBoardData, position: Int) {
            binding?.dataset = data
            binding?.viewModel = model
//            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner


            when(position){
                0,1,2 ->{
                    binding.tvRank.text = "Bset ${position+1}"
                }
            }


//            Glide.with(con).load(data.b_cover).override(binding!!.itemIv.width,binding!!.itemIv.height).centerCrop().into(binding!!.itemIv)
            binding!!.tvViewProfile.setOnClickListener {
                itemClick(data, "드라이브 신청",position)
            }
            itemView.setOnClickListener { itemClick(data, "기본",position) }
//            if (data.u_gender == "MALE"){
//                Glide.with(binding!!.itemIv).load(R.drawable.ic_man_12x12).centerInside().into(binding!!.ivSex)
//            }else{
//                Glide.with(binding!!.itemIv).load(R.drawable.ic_woman_12x12).centerInside().into(binding!!.ivSex)
//            }
        }
    }

    companion object {
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:mycarbestboard")
        @JvmStatic
        fun setmycarboard(recyclerView: RecyclerView, items: ArrayList<MyCarBoardData>) {
            if (recyclerView.adapter != null) {
                var adapter = (recyclerView.adapter as MyCarBestBoardAdapter)
                items?.let {
                    adapter.data = it
                    adapter.notifyDataSetChanged()

                }
            }
        }

//            @SuppressLint("SuspiciousIndentation")
//            @BindingAdapter("app:viptoplists")
//            @JvmStatic
//            fun setItems(recyclerView: RecyclerView, items: ArrayList<MyCarBoardData>) {
//                var adapter =  (recyclerView.adapter as MyCarBoardAdapter)
//                items?.let {
//                    adapter.data = it
//                    adapter.notifyDataSetChanged()
//                }
//            }

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