package com.supercarlounge.supercar.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.*
import com.supercarlounge.supercar.databinding.ItemLocationMainBinding
import com.supercarlounge.supercar.databinding.ItemLocationWritingMainBinding
import com.supercarlounge.supercar.viewmodel.NewDriveWritingViewModel


class LocationSpotWritingAdapter(var viewModel: NewDriveWritingViewModel, val con: Context, val data: ArrayList<LocationSpotData>, val itemClick: (LocationSpotData, Int) -> Unit) : RecyclerView.Adapter<LocationSpotWritingAdapter.ViewHolder>() {

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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data.get(position), position)
    }

    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_location_writing_main, parent, false)
    ) {
        private val binding: ItemLocationWritingMainBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: LocationSpotData, position: Int) {
            binding?.dataset = data
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner
            if (viewModel.select_index.value == position){
                binding.itemCardCenter.strokeColor = ContextCompat.getColor(con, R.color.main_color)
                binding.llSelet.visibility = View.VISIBLE
            }else{
                binding.itemCardCenter.strokeColor = ContextCompat.getColor(con, R.color.trn)
                binding.llSelet.visibility = View.GONE
            }
            Glide.with(binding!!.ivMain).load(data!!.spot_main_image).override(binding.ivMain.width, binding.ivMain.height).centerCrop().into(binding!!.ivMain)
            itemView.setOnClickListener { itemClick(data, position)

            }
        }
    }

    companion object {
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:spotlist2")
        @JvmStatic
        fun setItems(recyclerView: RecyclerView, items: ArrayList<LocationSpotData>) {
            if (recyclerView.adapter != null) {
                var adapter = (recyclerView.adapter as LocationSpotWritingAdapter?)
                items?.let {
                    adapter?.setdata(it)

                }
            }
        }
    }
}