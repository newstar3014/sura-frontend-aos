package com.supercarlounge.supercar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.*
import com.supercarlounge.supercar.databinding.ItemCarBinding
import com.supercarlounge.supercar.databinding.ItemLocationCateBinding
import com.supercarlounge.supercar.viewmodel.OnwerViewModel


class LocationCateAdapter(val con: Context, val data: ArrayList<LocationCateData>, val itemClick: (LocationCateData, Int) -> Unit) : RecyclerView.Adapter<LocationCateAdapter.ViewHolder>() {
    var selectindex = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }


    override fun getItemCount(): Int {
        return data!!.size
    }

    fun setdata(datalist:ArrayList<LocationCateData>){
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
        LayoutInflater.from(parent.context).inflate(R.layout.item_location_cate, parent, false)
    ) {
        private val binding: ItemLocationCateBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: LocationCateData, position: Int) {
            binding?.dataset = data
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner
            Glide.with(binding!!.ivCate).load(data!!.loca_image).into(binding!!.ivCate)
            itemView.setOnClickListener { itemClick(data, position)
            }
        }
    }


}