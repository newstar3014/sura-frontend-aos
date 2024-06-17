package com.supercarlounge.supercar.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.BoardData
import com.supercarlounge.supercar.data.LikeListData
import com.supercarlounge.supercar.data.LocationSpotData
import com.supercarlounge.supercar.databinding.ItemLikeListBinding
import com.supercarlounge.supercar.databinding.ItemLocationBinding
import com.supercarlounge.supercar.viewmodel.LocationAllViewModel
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogLikeListViewModel


class LocationListAdapter(val con: Context, val data: ArrayList<LocationSpotData>, val model: LocationAllViewModel, val itemClick: (LocationSpotData, Int) -> Unit) : RecyclerView.Adapter<LocationListAdapter.ViewHolder>() {
    var selectindex = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }


    override fun getItemCount(): Int {
        return 3
    }

    fun setdata(datalist:ArrayList<LocationSpotData>){
        data.clear()
        data.addAll(datalist)
        notifyDataSetChanged()
    }
    fun setselect(index:Int){
        selectindex=index
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (data.size > position){
            Log.d("로케이션 데이터", data[position].toString())
            holder.bind(data[position], position)
        }

    }

    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_location, parent, false)
    ) {
        private val binding: ItemLocationBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: LocationSpotData, position: Int) {
            Log.d("데이터 리스트", data.toString())
            binding?.dataset = data
            binding?.viewModel = model
            binding?.posi = position
            binding!!.executePendingBindings()

            binding.item.visibility = View.VISIBLE

            binding.lifecycleOwner = con as LifecycleOwner
            binding.item.setOnClickListener { itemClick(data, position) }
        }
    }

    companion object {
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:locationList")
        @JvmStatic
        fun setItems(recyclerView: RecyclerView, items: ArrayList<LocationSpotData>) {
            if (recyclerView.adapter != null) {
                var adapter = (recyclerView.adapter as LocationListAdapter)
                items?.let {
                    adapter.setdata(it)

                }
            }
        }
    }
}