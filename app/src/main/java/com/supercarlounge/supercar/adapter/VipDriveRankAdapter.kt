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
import com.supercarlounge.supercar.data.DriveAwayData
import com.supercarlounge.supercar.data.VipDriveRankData
import com.supercarlounge.supercar.databinding.ItemVipDriveRankBinding


class VipDriveRankAdapter(val con: Context, val data: ArrayList<DriveAwayData>, val itemClick: (VipDriveRankData, Int) -> Unit) : RecyclerView.Adapter<VipDriveRankAdapter.ViewHolder>() {
    var selectindex = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }


    override fun getItemCount(): Int {
        return 7
    }

    fun setdata(datalist:ArrayList<DriveAwayData>){
        data.clear()
        data.addAll(datalist)
        notifyDataSetChanged()
    }
    fun setselect(index:Int){
        selectindex=index
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var driveData:DriveAwayData? =null
        if(position < data.size){
            driveData = data.get(position)
        }
        holder.bind(driveData, position)
    }

    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_vip_dirve_rank, parent, false)
    ) {
        private val binding: ItemVipDriveRankBinding? = DataBindingUtil.bind(itemView)

        fun bind(indata: DriveAwayData?, position: Int) {
            binding?.dataset = indata
            binding?.rank = (position+4).toString()
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner

            Log.d("랭크데이터", data.toString())
            if (indata!= null){

                if (position ==6){
                    binding.textView8.text = indata?.heart_count.toString()
                }else{
                    binding.textView8.text = "SECRET"
                }
                Glide.with(binding!!.ivImage).load(indata.b_cover).override(200,200).centerInside().circleCrop().into(binding!!.ivImage)
//                Glide.with(binding.ivImage).load(indata).circleCrop().into(binding.ivImage)
            }else{
                Glide.with(binding!!.ivImage).load(R.drawable.ic_main).override(200,200).centerInside().circleCrop().into(binding!!.ivImage)
//                Glide.with(binding.ivImage).load(R.drawable.ic_main).circleCrop().into(binding.ivImage)
            }


        }
    }

    companion object {
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:driveranklist")
        @JvmStatic
        fun setItems(recyclerView: RecyclerView, items: ArrayList<DriveAwayData>) {
            if (recyclerView.adapter != null) {
                var adapter = (recyclerView.adapter as VipDriveRankAdapter)
                items?.let {
                    if(it.size == 0){
                        adapter.data.clear()
                    }else {
                        adapter.data.addAll(it)

                    }

                    var rowSize = adapter.data.size
                    if (rowSize> 0){
                        adapter.data.removeAt(0)
                    }
                    if (rowSize> 1){
                        adapter.data.removeAt(0)
                    }
                    if (rowSize> 2){
                        adapter.data.removeAt(0)
                    }
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }
}