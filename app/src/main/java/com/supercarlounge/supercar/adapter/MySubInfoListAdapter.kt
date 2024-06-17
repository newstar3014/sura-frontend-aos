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
import com.supercarlounge.supercar.data.MySubInfoData
import com.supercarlounge.supercar.databinding.ItemAnonymousForumPostBinding
import com.supercarlounge.supercar.databinding.ItemMySubInfo
import com.supercarlounge.supercar.databinding.ItemMySubInfoRv
import com.supercarlounge.supercar.enumset.TimeValue
import com.supercarlounge.supercar.viewmodel.BoardViewModel
import com.supercarlounge.supercar.viewmodel.MyCarBoardPostViewModel
import com.supercarlounge.supercar.viewmodel.MyCarBoardWritingViewModel
import java.text.ParseException
import java.text.SimpleDateFormat


class MySubInfoListAdapter(val con: Context, val data: ArrayList<MySubInfoData>, val model: MyCarBoardPostViewModel, val itemClick: (MySubInfoData, String, Int) -> Unit) : RecyclerView.Adapter<MySubInfoListAdapter.ViewHolder>() {
    var selectindex = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }


    override fun getItemCount(): Int {
        return data!!.size
    }

    fun setdata(datalist:ArrayList<MySubInfoData>){
         val diffUtil = BaseDiffUtil(data, datalist)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        data.clear()
        data.addAll(datalist)
        notifyDataSetChanged()
//        diffResult.dispatchUpdatesTo(this)
//        notifyDataSetChanged()
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
        LayoutInflater.from(parent.context).inflate(R.layout.item_my_sub_info_rv, parent, false)
    ) {
        private val binding: ItemMySubInfoRv? = DataBindingUtil.bind(itemView)

        fun bind(data: MySubInfoData, position: Int, width:Int) {
            binding?.dataset = data
            binding?.viewModel = model
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner

//            binding.ivImage.setOnClickListener {
//                itemClick(data,"사진수정",position)
//            }
//            binding.ivCancel.setOnClickListener {
//                itemClick(data,"삭제",position)
//            }

        }
    }


    companion object {
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:MySubInfoList")
        @JvmStatic
        fun setItems(recyclerView: RecyclerView, items: ArrayList<MySubInfoData>) {
            if (recyclerView.adapter != null) {
                var adapter = (recyclerView.adapter as MySubInfoListAdapter)
                items?.let {
                    Log.d("데이터", it.toString())
                    adapter.setdata(it)

                }
            }
        }
    }


}