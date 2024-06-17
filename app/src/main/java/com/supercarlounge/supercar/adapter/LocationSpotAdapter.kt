package com.supercarlounge.supercar.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.*
import com.supercarlounge.supercar.databinding.ItemLocationMainBinding


class LocationSpotAdapter(val con: Context, val data: ArrayList<LocationSpotData>, val itemClick: (LocationSpotData,String, Int) -> Unit) : RecyclerView.Adapter<LocationSpotAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }


    override fun getItemCount(): Int {
        return data!!.size
    }

    fun setdata(datalist:ArrayList<LocationSpotData>){
        data.clear()
        data.addAll(datalist)
        notifyDataSetChanged()
    }
    fun setIndexData(index : Int,data3: LocationSpotData ){
        if (data.size >index){
            data[index] = data3
            notifyItemChanged(index)
        }


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data.get(position), position)
    }

    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_location_main, parent, false)
    ) {
        private val binding: ItemLocationMainBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: LocationSpotData, position: Int) {
            binding?.dataset = data
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner
            Glide.with(binding!!.ivMain).load(data!!.spot_main_image).override(binding.ivMain.width, binding.ivMain.height).centerCrop().into(binding!!.ivMain)
            itemView.setOnClickListener {
                itemClick(data,"메인", position)

            }
            binding.ivBookmark.setOnClickListener {
                itemClick(data,"북마크", position)
            }
        }
    }

    companion object {
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:spotlist")
        @JvmStatic
        fun setItems(recyclerView: RecyclerView?, items: ArrayList<LocationSpotData>?) {
            if (recyclerView?.adapter != null) {
                var adapter = (recyclerView.adapter as LocationSpotAdapter?)
                items?.let {
                    adapter?.setdata(it)

                }
            }
        }
    }
}