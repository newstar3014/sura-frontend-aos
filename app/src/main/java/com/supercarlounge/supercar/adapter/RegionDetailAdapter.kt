package com.supercarlounge.supercar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.databinding.ItemRegionBinding
import com.supercarlounge.supercar.databinding.ItemRegionDetailBinding
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.BottomSheetRegionDailogViewModel


class RegionDetailAdapter(val con: Context, val data: ArrayList<String>, val model: BottomSheetRegionDailogViewModel, val itemClick: (String, Int) -> Unit) : RecyclerView.Adapter<RegionDetailAdapter.ViewHolder>() {
    var selectindex = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }


    override fun getItemCount(): Int {
        return data!!.size
    }

    fun setdata(datalist:ArrayList<String>){
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
        LayoutInflater.from(parent.context).inflate(R.layout.item_region_detail, parent, false)
    ) {
        private val binding: ItemRegionDetailBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: String, position: Int) {
            binding?.dataset = data
            binding?.viewModel = model
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner
            itemView.setOnClickListener { itemClick(data, position) }
        }
    }


}