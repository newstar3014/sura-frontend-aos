package com.supercarlounge.supercar.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.*
import com.supercarlounge.supercar.databinding.ItemCarBinding
import com.supercarlounge.supercar.databinding.ItemSpotDescBinding
import com.supercarlounge.supercar.viewmodel.OnwerViewModel


class LocationDescAdapter(val con: Context, val data: ArrayList<LocationSpotDescData>, val itemClick: (CarBrandListData, Int) -> Unit) : RecyclerView.Adapter<LocationDescAdapter.ViewHolder>() {
    var selectindex = 0
    var datasize = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }


    override fun getItemCount(): Int {
        return datasize
    }

    fun setDataSize(size : Int){
        datasize = size
        notifyDataSetChanged()
    }

    fun setdata(datalist:ArrayList<LocationSpotDescData>){
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
        LayoutInflater.from(parent.context).inflate(R.layout.item_spot_desc, parent, false)
    ) {
        private val binding: ItemSpotDescBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: LocationSpotDescData, position: Int) {
            binding?.dataset = data
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner
        }
    }

    companion object {
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:desc")
        @JvmStatic
        fun setbanner(recyclerview: RecyclerView, items: ArrayList<LocationSpotDescData>) {
            var adapter =  (recyclerview.adapter as LocationDescAdapter)
            items?.let {
                adapter.setdata(it)
            }

        }
    }
}