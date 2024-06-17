package com.supercarlounge.supercar.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.CompanyPhoneData
import com.supercarlounge.supercar.data.HeartLogData
import com.supercarlounge.supercar.databinding.ItemCompanyphoneBinding
import com.supercarlounge.supercar.databinding.ItemHeartBuyListBinding


class CompanyPhoneAdapter(val con: Context, var data: ArrayList<CompanyPhoneData>, val itemClick: (CompanyPhoneData, Int) -> Unit) : RecyclerView.Adapter<CompanyPhoneAdapter.ViewHolder>() {
    var selectindex = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }


    override fun getItemCount(): Int {
        return data!!.size
    }

    fun setdata(datalist:ArrayList<CompanyPhoneData>){
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
        LayoutInflater.from(parent.context).inflate(R.layout.item_companyphone, parent, false)
    ) {
        private val binding: ItemCompanyphoneBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: CompanyPhoneData, position: Int) {
            binding?.dataset = data
            binding?.posi = position
//            binding!!.executePendingBindings()
            binding?.lifecycleOwner = con as LifecycleOwner
            itemView.setOnClickListener {
                itemClick(data,position)
            }
//            Glide.with(binding!!.itemIv).load(data!!.src).centerInside().into(binding!!.)
        }
    }
}