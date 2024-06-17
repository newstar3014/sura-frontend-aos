package com.supercarlounge.supercar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.LikeListData
import com.supercarlounge.supercar.databinding.ItemLikeListBinding
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogLikeListViewModel


class LikeListAdapter(val con: Context, val data: ArrayList<LikeListData>, val model: DialogLikeListViewModel, val itemClick: (LikeListData, Int) -> Unit) : RecyclerView.Adapter<LikeListAdapter.ViewHolder>() {
    var selectindex = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }


    override fun getItemCount(): Int {
        return data!!.size
    }

    fun setdata(datalist:ArrayList<LikeListData>){
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
        LayoutInflater.from(parent.context).inflate(R.layout.item_like_list, parent, false)
    ) {
        private val binding: ItemLikeListBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: LikeListData, position: Int) {
            binding?.dataset = data
            binding?.viewModel = model
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner

            if (data.u_gender == "MALE"){
                Glide.with(binding!!.ivSex).load(R.drawable.ic_man_12x12).centerInside().into(binding!!.ivSex)
            }else{
                Glide.with(binding!!.ivSex).load(R.drawable.ic_woman_12x12).centerInside().into(binding!!.ivSex)
            }
            itemView.setOnClickListener { itemClick(data, position) }

        }
    }


}