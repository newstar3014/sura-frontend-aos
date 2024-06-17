package com.supercarlounge.supercar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.BoardCategoryData
import com.supercarlounge.supercar.databinding.ItemAnonymousForumFilterBinding
import com.supercarlounge.supercar.viewmodel.BoardViewModel


class BoardCategoryAdapter(val con: Context, val data: ArrayList<BoardCategoryData>, val model: BoardViewModel, val itemClick: (BoardCategoryData, Int) -> Unit) : RecyclerView.Adapter<BoardCategoryAdapter.ViewHolder>() {
    var selectindex = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }


    override fun getItemCount(): Int {
        return data!!.size
    }

    fun setdata(datalist:ArrayList<BoardCategoryData>){
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
        LayoutInflater.from(parent.context).inflate(R.layout.item_anonymous_frum_filter, parent, false)
    ) {
        private val binding: ItemAnonymousForumFilterBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: BoardCategoryData, position: Int) {
            binding?.dataset = data
            binding?.viewModel = model
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner
            itemView.setOnClickListener { itemClick(data, position) }
//            Glide.with(binding!!.itemIv).load(data!!.src).centerInside().into(binding!!.)
        }
    }


}